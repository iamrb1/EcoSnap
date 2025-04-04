package edu.msu.cse476.baragurr.ecosnap;

import static edu.msu.cse476.baragurr.ecosnap.R.layout.activity_camera;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.Manifest;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import org.jspecify.annotations.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Camera extends AppCompatActivity implements View.OnClickListener, ImageAnalysis.Analyzer {

    private int counter = 0;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    ImageButton bTakePicture;
    ImageButton flashToggleIB;
    ImageButton flipCameraIB;
    ImageButton backIB;
    private ImageCapture imageCapture;
    private ImageAnalysis imageAnalysis;

    private boolean flashEnabled = false;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;

    private String recyclabilityLabel = "Unknown"; // Stores the ML Kit result


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        bTakePicture = findViewById(R.id.captureIB);
        previewView = findViewById(R.id.previewView);
        flashToggleIB = findViewById(R.id.flashToggleIB);
        flipCameraIB = findViewById(R.id.flipCameraIB);
        backIB = findViewById(R.id.backIB);

        bTakePicture.setOnClickListener(this);
        flashToggleIB.setOnClickListener(this);
        flipCameraIB.setOnClickListener(this);
        backIB.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        } else {

            cameraProviderFuture = ProcessCameraProvider.getInstance(this);
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    startCameraX(cameraProvider);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, getExecutor());
        }
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        //Camera selector use case
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();

        //Preview use case
        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        //Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setFlashMode(flashEnabled ? ImageCapture.FLASH_MODE_ON : ImageCapture.FLASH_MODE_OFF)
                .build();

        //Image analysis use case
        imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.captureIB) {
            counter += 1;
            capturePhoto();
        } else if (id == R.id.flashToggleIB) {
            toggleFlash();
        } else if (id == R.id.flipCameraIB) {
            flipCamera();
        } else if (id == R.id.backIB) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }
    }

    private void toggleFlash() {
        flashEnabled = !flashEnabled;
        // Update the flash icon
        flashToggleIB.setImageResource(flashEnabled ?
                R.drawable.flash_on : R.drawable.flash_off);

        // Restart camera with new flash setting
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    private void flipCamera() {
        lensFacing = (lensFacing == CameraSelector.LENS_FACING_BACK) ?
                CameraSelector.LENS_FACING_FRONT : CameraSelector.LENS_FACING_BACK;
        // Restart camera with new lens facing setting
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    private void capturePhoto() {
        File photoDir = new File(getExternalFilesDir(null), "CameraXPhotos");
        if (!photoDir.exists()) {
            photoDir.mkdir();
        }
        Date date = new Date();
        String timestamp = String.valueOf(date.getTime());
        String photoFilePath = photoDir.getAbsolutePath() + "/" + timestamp + ".jpg";

        File photoFile = new File(photoFilePath);

        Log.e("Capture", "Saving to: " + photoFile.getAbsolutePath());

        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.@NonNull OutputFileResults outputFileResults) {
                        Toast.makeText(Camera.this, "Photo has been saved successfully", Toast.LENGTH_SHORT).show();
                        analyzeImage(photoFile.getAbsolutePath());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(Camera.this, "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        // Image processing for current frame
        Log.d("camera_analyze", "analyze: got the frame at " + image.getImageInfo().getTimestamp());
        image.close();
    }

    private void analyzeImage(String imagePath) {
        String base64Image = encodeImageToBase64(imagePath);
        if (base64Image == null) {
            runOnUiThread(() ->
                    Toast.makeText(this, "Failed to encode image", Toast.LENGTH_SHORT).show()
            );
            return;
        }

        VisionRequest.Request request = new VisionRequest.Request(base64Image);
        VisionRequest visionRequest = new VisionRequest(request);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vision.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VisionApiService service = retrofit.create(VisionApiService.class);
        Call<VisionResponse> call = service.annotateImage(visionRequest, "");

        call.enqueue(new Callback<VisionResponse>() {
            @Override
            public void onResponse(Call<VisionResponse> call, Response<VisionResponse> response) {
                recyclabilityLabel = "Unknown"; // Default recyclability label
                String itemLabel = "Unknown item"; // Default item label

                if (response.isSuccessful()) {
                    Log.d("VisionAPI", "Successful response: " + response.body());

                    if (response.body() != null) {
                        try {
                            Log.d("VisionAPI", "Response Body: " + response.body().toString());

                            List<VisionResponse.LabelAnnotation> labels =
                                    response.body().responses.get(0).labelAnnotations;

                            if (labels != null && !labels.isEmpty()) {
                                for (VisionResponse.LabelAnnotation label : labels) {
                                    String desc = label.description.toLowerCase();
                                    Log.d("VisionAPI", "Label: " + desc);

                                    // Check if the description contains recyclable material keywords
                                    if (isRecyclable(desc)) {
                                        recyclabilityLabel = "Recyclable";
                                        itemLabel = label.description; // Update item label
                                        break;
                                    }
                                }

                                if (itemLabel.equals("Unknown item")) {
                                    itemLabel = labels.get(0).description; // Default to first label if none match
                                }
                            }
                        } catch (Exception e) {
                            Log.e("VisionAPI", "Exception while parsing response", e);
                        }
                    }
                } else {
                    Log.e("VisionAPI", "Unsuccessful response: " + response.code() + " " + response.message());
                }

                // Show the item label directly from Vision API along with recyclability status
                String resultMessage = itemLabel + " is " + recyclabilityLabel;

                runOnUiThread(() ->
                        Toast.makeText(Camera.this, resultMessage, Toast.LENGTH_LONG).show()
                );
            }

            @Override
            public void onFailure(Call<VisionResponse> call, Throwable t) {
                Log.e("VisionAPI", "Failed to call Vision API", t);
                if (t instanceof HttpException) {
                    HttpException httpException = (HttpException) t;
                    Response<?> response = httpException.response();
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("VisionAPI", "Response Body: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(() ->
                        Toast.makeText(Camera.this, "Vision API call failed: " + t.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private String encodeImageToBase64(String imagePath) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isRecyclable(String item) {
        List<String> recyclableItems = Arrays.asList(
                "plastic", "glass", "paper", "cardboard", "can", "tin", "aluminum",
                "magazine", "newspaper", "carton", "bottle", "box", "jar"
        );

        for (String recyclable : recyclableItems) {
            if (item.contains(recyclable)) return true;
        }
        return false;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}