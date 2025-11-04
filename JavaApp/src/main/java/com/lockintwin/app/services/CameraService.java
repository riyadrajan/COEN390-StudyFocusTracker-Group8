package com.lockintwin.app.services;

import javafx.application.Platform;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Manages camera capture and frame processing
 */
public class CameraService {
    
    private static final Logger logger = LoggerFactory.getLogger(CameraService.class);
    private static CameraService instance;
    
    private VideoCapture camera;
    private ScheduledExecutorService executor;
    private boolean isRunning = false;
    private Consumer<Image> frameCallback;
    private Consumer<Mat> matCallback;
    
    static {
        // Load OpenCV native library
        nu.pattern.OpenCV.loadLocally();
    }
    
    private CameraService() {}
    
    public static synchronized CameraService getInstance() {
        if (instance == null) {
            instance = new CameraService();
        }
        return instance;
    }
    
    public boolean startCamera(int cameraIndex) {
        if (isRunning) {
            logger.warn("Camera is already running");
            return false;
        }
        
        camera = new VideoCapture(cameraIndex);
        
        if (!camera.isOpened()) {
            logger.error("Failed to open camera {}", cameraIndex);
            return false;
        }
        
        isRunning = true;
        logger.info("Camera {} started successfully", cameraIndex);
        return true;
    }
    
    public void startFrameCapture(Consumer<Image> callback) {
        startFrameCapture(callback, null);
    }
    
    public void startFrameCapture(Consumer<Image> imageCallback, Consumer<Mat> matCallback) {
        if (!isRunning) {
            logger.error("Camera is not running");
            return;
        }
        
        this.frameCallback = imageCallback;
        this.matCallback = matCallback;
        
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::captureFrame, 0, 33, TimeUnit.MILLISECONDS);
        
        logger.info("Frame capture started");
    }
    
    private void captureFrame() {
        if (!isRunning || camera == null) {
            return;
        }
        
        Mat frame = new Mat();
        
        if (camera.read(frame)) {
            // Send Mat to callback if registered (for processing)
            if (matCallback != null) {
                matCallback.accept(frame.clone());
            }
            
            // Convert to JavaFX Image for display
            if (frameCallback != null) {
                MatOfByte buffer = new MatOfByte();
                Imgcodecs.imencode(".png", frame, buffer);
                
                Image image = new Image(new ByteArrayInputStream(buffer.toArray()));
                
                Platform.runLater(() -> frameCallback.accept(image));
            }
        }
        
        frame.release();
    }
    
    public Mat captureFrameMat() {
        if (!isRunning || camera == null) {
            return null;
        }
        
        Mat frame = new Mat();
        if (camera.read(frame)) {
            return frame;
        }
        
        return null;
    }
    
    public void stopFrameCapture() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("Error stopping frame capture", e);
                Thread.currentThread().interrupt();
            }
        }
        
        frameCallback = null;
        matCallback = null;
        
        logger.info("Frame capture stopped");
    }
    
    public void stopCamera() {
        stopFrameCapture();
        
        if (camera != null && camera.isOpened()) {
            camera.release();
        }
        
        isRunning = false;
        logger.info("Camera stopped");
    }
    
    public boolean isRunning() {
        return isRunning;
    }
}
