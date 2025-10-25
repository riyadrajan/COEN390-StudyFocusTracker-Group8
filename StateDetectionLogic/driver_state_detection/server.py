import os
import sys
import signal
import subprocess
import time
import logging
from flask import Flask, jsonify, request
import requests

##TEST 123

# Optional: keep import available if you want to run in-process
# from . import main  # execute package main when requested from start in app

app = Flask(__name__)

# Configure logging to ensure we see requests and server logs
logging.getLogger("werkzeug").setLevel(logging.INFO)
app.logger.setLevel(logging.INFO)

# Track a child process running the vision pipeline
proc = None

# Optional: read shared state for debugging/testing
try:
    from . import state as shared_state
except Exception:
    shared_state = None

#define endpoints
@app.route('/', methods=['GET'])
def root():
    return jsonify({"status": "ok", "endpoints": ["POST /start", "POST /stop", "GET /status", "GET /health", "POST /light"]})


@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "healthy"})


@app.route('/status', methods=['GET'])
def status():
    global proc
    running = proc is not None and (proc.poll() is None)
    pid = proc.pid if running else None
    return jsonify({"running": running, "pid": pid})

#test response for lighting LED on esp32
@app.route('/light', methods=['POST'])
def light():
    '''
    Right now this is just a local response in terminal to see if the /light request is received by the server
    This will be changed to send to the esp32 later on
    '''
    resp = {"status": "LED ON", "esp32_response": "works"}
    app.logger.info("/light response: %s", resp)
    return jsonify(resp)


@app.route('/start', methods=['POST'])
def start():
    global proc
    # If already running, return current status
    if proc is not None and (proc.poll() is None):
        app.logger.info("/start called: process already running (pid=%s)", proc.pid)
        return jsonify({"status": "already running", "pid": proc.pid})

    # Build command to run the vision pipeline as a separate process
    python_exec = sys.executable  # uses the same venv interpreter
    cmd = [python_exec, "-m", "driver_state_detection.main"]
    app.logger.info("Starting vision process: %s", " ".join(cmd))

    try:
        # Start child detached enough to survive route return, capture stdout/stderr for debugging
        proc = subprocess.Popen(
            cmd,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
        )
        # Brief delay to surface immediate failures
        time.sleep(0.3)
        if proc.poll() is not None and proc.returncode is not None:
            # Process exited immediately, fetch output
            output = proc.stdout.read() if proc.stdout else ""
            app.logger.error("Vision process exited (%s). Output:\n%s", proc.returncode, output)
            return jsonify({"status": "failed to start", "code": proc.returncode, "output": output}), 500

        app.logger.info("Vision process started (pid=%s)", proc.pid)
        return jsonify({"status": "started", "pid": proc.pid})
    except Exception as e:
        app.logger.exception("Failed to start vision process: %s", e)
        return jsonify({"status": "error", "error": str(e)}), 500

@app.route('/stop', methods=['POST'])
def stop():
    global proc
    if proc is None or (proc.poll() is not None):
        app.logger.info("/stop called: no running process")
        return jsonify({"status": "not running"})

    app.logger.info("Stopping vision process (pid=%s)", proc.pid)
    try:
        proc.terminate()
        try:
            proc.wait(timeout=3)
        except subprocess.TimeoutExpired:
            app.logger.warning("Terminate timeout, killing process (pid=%s)", proc.pid)
            proc.kill()
            proc.wait(timeout=2)
        status = {"status": "stopped"}
    except Exception as e:
        app.logger.exception("Failed to stop process: %s", e)
        status = {"status": "error", "error": str(e)}
    finally:
        proc = None
    return jsonify(status)

def main():
    host = os.environ.get('HOST', '0.0.0.0')
    port = int(os.environ.get('PORT', '3000'))
    app.run(host=host, port=port)


if __name__ == '__main__':
    main()
