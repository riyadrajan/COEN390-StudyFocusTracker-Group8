package com.lockintwin.app.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lockintwin.app.models.AttentionData;
import com.lockintwin.app.utils.Constants;
import okhttp3.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Monitors user attention by communicating with the Python backend
 */
public class AttentionMonitorService {
    
    private static final Logger logger = LoggerFactory.getLogger(AttentionMonitorService.class);
    private static AttentionMonitorService instance;
    
    private final OkHttpClient httpClient;
    private final Gson gson;
    private Consumer<AttentionData> attentionCallback;
    private boolean isMonitoring = false;
    
    private AttentionMonitorService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }
    
    public static synchronized AttentionMonitorService getInstance() {
        if (instance == null) {
            instance = new AttentionMonitorService();
        }
        return instance;
    }
    
    public void startMonitoring(Long sessionId, Consumer<AttentionData> callback) {
        this.attentionCallback = callback;
        this.isMonitoring = true;
        
        // Start camera with frame processing
        CameraService.getInstance().startFrameCapture(
            null, // No image callback needed
            frame -> processFrame(sessionId, frame)
        );
        
        logger.info("Attention monitoring started for session {}", sessionId);
    }
    
    private void processFrame(Long sessionId, Mat frame) {
        if (!isMonitoring) {
            return;
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                AttentionData data = analyzeFrame(frame);
                if (data != null) {
                    data.setSessionId(sessionId);
                    
                    // Save to database
                    DatabaseService.getInstance().saveAttentionData(data);
                    
                    // Notify callback
                    if (attentionCallback != null) {
                        attentionCallback.accept(data);
                    }
                }
            } catch (Exception e) {
                logger.error("Error processing frame", e);
            }
        });
    }
    
    private AttentionData analyzeFrame(Mat frame) {
        try {
            // Convert Mat to byte array
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".jpg", frame, buffer);
            byte[] imageBytes = buffer.toArray();
            
            // Send to backend
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "frame.jpg",
                            RequestBody.create(imageBytes, MediaType.parse("image/jpeg")))
                    .build();
            
            Request request = new Request.Builder()
                    .url(Constants.BACKEND_URL + Constants.ATTENTION_ENDPOINT)
                    .post(requestBody)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JsonObject json = gson.fromJson(responseBody, JsonObject.class);
                    
                    AttentionData data = new AttentionData();
                    data.setAttentionScore(json.get("attention_score").getAsDouble());
                    data.setHeadPoseYaw(json.get("head_pose_yaw").getAsDouble());
                    data.setHeadPosePitch(json.get("head_pose_pitch").getAsDouble());
                    data.setHeadPoseRoll(json.get("head_pose_roll").getAsDouble());
                    data.setEyeAspectRatio(json.get("eye_aspect_ratio").getAsDouble());
                    data.setDistracted(json.get("is_distracted").getAsBoolean());
                    
                    return data;
                }
            }
            
        } catch (IOException e) {
            logger.error("Error analyzing frame", e);
        }
        
        return null;
    }
    
    public CompletableFuture<Boolean> checkBackendConnection() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Request request = new Request.Builder()
                        .url(Constants.BACKEND_URL + "/health")
                        .get()
                        .build();
                
                try (Response response = httpClient.newCall(request).execute()) {
                    return response.isSuccessful();
                }
            } catch (IOException e) {
                logger.error("Backend connection check failed", e);
                return false;
            }
        });
    }
    
    public void stopMonitoring() {
        this.isMonitoring = false;
        this.attentionCallback = null;
        logger.info("Attention monitoring stopped");
    }
    
    public boolean isMonitoring() {
        return isMonitoring;
    }
}
