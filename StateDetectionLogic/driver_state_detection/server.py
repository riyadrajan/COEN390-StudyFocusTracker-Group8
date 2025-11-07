import os
import sys
import subprocess
import time
import logging
from flask import Flask, jsonify, request
from flask_sock import Sock
import requests
from driver_state_detection.focus_score_calculator import SessionServerStore

app = Flask(__name__)
sock = Sock(app)

logging.getLogger("werkzeug").setLevel(logging.INFO)
app.logger.setLevel(logging.INFO)

proc = None
light_on_state = False
ws_clients = set()  # track connected WebSocket clients
session_store = None  # type: SessionServerStore | None
session_id = None     # type: str | None


@app.route('/')
def root():
    return jsonify({
        "status": "ok",
        "endpoints": ["/start", "/stop", "/status", "/light", "/ws"]
    })


@app.route('/status')
def status():
    global proc
    running = proc is not None and proc.poll() is None
    pid = proc.pid if running else None
    return jsonify({"running": running, "pid": pid})

@app.route('/light', methods=['POST'])
def light():
    global light_on_state
    global session_store, session_id
    data = request.get_json(silent=True) or {}

    # Robust parsing of light_on
    val = data.get("light_on", True)
    if isinstance(val, bool):
        light_on_state = val
    elif isinstance(val, str):
        light_on_state = val.lower() == "true"  # Only "true" is True, everything else False
    else:
        light_on_state = bool(val)

    app.logger.info(f"Light state set to: {light_on_state}")

    # also add to database (since this is when a distracted call occurs anyways)
    # Persist distracted edges to Firestore (sessionServer) if a session is active
    try:
        if session_store and session_id:
            if light_on_state:
                session_store.mark_distracted(session_id)
            else:
                session_store.mark_focused(session_id)
    except Exception as e:
        app.logger.warning(f"Firestore update skipped: {e}")

    # Broadcast to all connected WebSocket clients as raw string
    for ws in list(ws_clients):
        try:
            ws.send("ON" if light_on_state else "OFF")
        except Exception:
            ws_clients.discard(ws)

    return jsonify({"status": "ok", "light_on": light_on_state})

@app.route('/start', methods=['POST'])
def start():
    """
    Starts the driver_state_detection.main process if not already running.
    Returns JSON with status and PID if started successfully.
    """
    global proc

    # Check if process is already running
    if proc is not None and proc.poll() is None:
        app.logger.info("/start called but process already running (pid=%s)", proc.pid)
        return jsonify({"status": "already running", "pid": proc.pid})

    python_exec = sys.executable
    cmd = [python_exec, "-m", "driver_state_detection.main"]
    app.logger.info("Attempting to start vision process: %s", " ".join(cmd))

    try:
        # Start subprocess
        proc = subprocess.Popen(
            cmd,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
        )

        # Short delay to catch immediate failures
        time.sleep(0.3)

        if proc.poll() is not None:
            # Process exited immediately, fetch stdout for debugging
            output = proc.stdout.read() if proc.stdout else ""
            app.logger.error("Vision process exited immediately. Output:\n%s", output)
            proc = None
            return jsonify({"status": "failed", "output": output}), 500

        # Fail-safe: ensure LED starts OFF
        light_on_state = False
        for ws in list(ws_clients):
            try:
                ws.send("OFF")
            except Exception:
                ws_clients.discard(ws)

        # Initialize a new server-side session for focus scoring
        try:
            global session_store, session_id
            session_store = SessionServerStore()
            session_id = session_store.start_session(user_id=None, username=None)
            app.logger.info(f"Started sessionServer session: {session_id}")
        except Exception as e:
            app.logger.warning(f"Could not start sessionServer session: {e}")

        # Successfully started
        app.logger.info("Vision process started successfully (pid=%s)", proc.pid)
        return jsonify({"status": "started", "pid": proc.pid})

    except Exception as e:
        app.logger.exception("Failed to start vision process: %s", e)
        proc = None
        return jsonify({"status": "error", "error": str(e)}), 500



@app.route('/stop', methods=['POST'])
def stop():
    global proc, session_store, session_id
    if proc is None or proc.poll() is not None:
        return jsonify({"status": "not running"})

    try:
        proc.terminate()
        proc.wait(timeout=3)
    except Exception:
        proc.kill()
    finally:
        proc = None

        # Ensure LED OFF after stopping
        light_on_state = False
        for ws in list(ws_clients):
            try:
                ws.send("OFF")
            except Exception:
                ws_clients.discard(ws)

        # Finalize session in Firestore
        try:
            if session_store and session_id:
                elapsed_ms, focus = session_store.stop_session(session_id)
                app.logger.info(f"Session finalized: {session_id} elapsedMs={elapsed_ms} focus={focus:.2f}")
        except Exception as e:
            app.logger.warning(f"Could not finalize sessionServer session: {e}")
        finally:
            session_id = None
    return jsonify({"status": "stopped"})


@sock.route('/ws')
def websocket(ws):
    """Handle ESP32 WebSocket clients."""
    app.logger.info("ESP32 connected via WebSocket")
    ws_clients.add(ws)
    try:
        while True:
            msg = ws.receive()
            if msg is None:
                break
            app.logger.info(f"Received from ESP32: {msg}")
    finally:
        ws_clients.discard(ws)
        app.logger.info("ESP32 disconnected")


if __name__ == '__main__':
    host = os.environ.get('HOST', '0.0.0.0')
    port = int(os.environ.get('PORT', '3000'))
    app.run(host=host, port=port)
