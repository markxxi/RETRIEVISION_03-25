package com.example.retrievision;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.retrievision.ml.Rtrv;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class foundimage extends AppCompatActivity {
    ImageView imageView;
    Button submit;
    int imageSize = 224;
    String currentPhotoPath;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int THREAD_COUNT = 3;
    private final Executor imageClassification;
    private final Executor colorExtraction;

    public Executor getImageClassification(){return imageClassification;}
    public Executor getColorExtraction(){return colorExtraction;}
    StringBuilder colorStringBuilder = new StringBuilder();

    public foundimage() {
        this.imageClassification = Executors.newFixedThreadPool(THREAD_COUNT);
//        this.imageClassification = new ImageClassificationExecutor();
        this.colorExtraction = new colorExtractionExecutor();
    }
    private static class colorExtractionExecutor implements Executor{
        private Handler colorExtractionHandler = new Handler(Looper.getMainLooper());
        @Override
        public  void execute(Runnable command){colorExtractionHandler.post(command);}
    }
    private static class ImageClassificationExecutor implements Executor {
        private Handler ImageClassificationHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            ImageClassificationHandler.post(command);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foundimage);
        imageView = findViewById(R.id.imageView3);
        submit = findViewById(R.id.submit);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFoundImage();
                scannedObject();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is granted, start the camera
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, start the camera
                startCamera();
            } else {
                // Show a message or disable the camera functionality when the permission is denied
            }
        }
    }

    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK) {
                setPic();
        }
    }

    private void extractColorsFromImage(Bitmap bitmap) {
        Python py = Python.getInstance();
        PyObject module = py.getModule("colorIdentifier");

        if (currentPhotoPath == null) {
            Log.e("Error", "currentPhotoPath is null");
            return;
        }
        String imagePath = currentPhotoPath;
        PyObject result = module.callAttr("get_colors", imagePath);

        List<PyObject> resultList = result.asList();
        PyObject[] colorsArray = resultList.toArray(new PyObject[0]);
//        StringBuilder colorStringBuilder = new StringBuilder();

        for(PyObject color:colorsArray){
            colorStringBuilder.append(color.toString()).append("\n");
        }
//        Log.d("ColorList", String.valueOf(colorStringBuilder));
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void setPic() {
        Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
        Bitmap resizedBitmap = forHQView(imageBitmap, 4096, 4096);
        imageView.setImageBitmap(resizedBitmap);

    }
    private Bitmap forHQView(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) maxWidth / (float) maxHeight;

        int finalWidth = maxWidth;
        int finalHeight = maxHeight;

        if (ratioMax > ratioBitmap) {
            finalWidth = (int) ((float)maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) ((float)maxWidth / ratioBitmap);
        }
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true);
        return resizedBitmap;
    }
    private void scannedObject(){
        Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
        Bitmap resizedBitmap = ThumbnailUtils.extractThumbnail(imageBitmap, imageSize, imageSize);
        getColorExtraction().execute(() -> {
            extractColorsFromImage(resizedBitmap);
            classificationFunction(resizedBitmap);
        });
    }
    public void classificationFunction(Bitmap image) {
        try {
            Rtrv model = Rtrv.newInstance(getApplicationContext());
            TensorBuffer inputf = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
                }
            }

            inputf.loadBuffer(byteBuffer);

            Rtrv.Outputs outputs = model.process(inputf);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;

            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            Intent objectIntent = new Intent(foundimage.this, generatedfoundobject.class);
            String[] classes = {"Cap", "Charger", "Earphones", "Flash Drive", "Glasses", "Headphones", "ID", "Jacket", "Keyboard", "Keys", "Money", "Pen", "Smartphone", "Tshirt", "Umbrella", "Wallet", "Watch", "Water Bottle", "Wireless Earphones"};
            //key and value
            objectIntent.putExtra("highestObject", classes[maxPos]);
            objectIntent.putExtra("dominant_colors", colorStringBuilder.toString());

            startActivity(objectIntent);
            model.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openFoundImage(){
        Intent intent = new Intent(this, generatedfoundobject.class);
        startActivity(intent);
    }

}
