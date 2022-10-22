package com.example.imageuploader;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {



    ImageView imageView;
    Button openCam;
    //    temp code delete later
    private Button nextPage;
    //    temp code delete later

    String currentPhotoPath;
    static String imageName;
    static Bitmap imageBitmap;
    static byte[] byteArray;
    static String imageb64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imgCam);
        openCam = findViewById(R.id.btnCam);

        //  temp code delete later
//        imageView.setVisibility(View.GONE);
        nextPage=findViewById(R.id.nxtPgBtn);
        nextPage.setVisibility(View.GONE);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, uploadActivity.class);
                startActivity(intent);
            }
        });
        //  temp code delete later

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
        }

        openCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 100);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 100);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode ==100  && resultCode == Activity.RESULT_OK){
//            imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
//            imageView.setImageBitmap(imageBitmap);
//        }
        if (requestCode == 100) {
//            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(captureImage);
            if(resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                imageView.setImageURI(Uri.fromFile(f));
//                Log.d("tag", "Absolute URL of image is:"+Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                imageName = f.getName();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();
                    imageb64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    nextPage.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    nextPage.setVisibility(View.GONE);
                }
            }
        }
    }


    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public static String getImageName(){
        return imageName;
    }

    public static Bitmap getImageBitmap(){
        return imageBitmap;
    }

    public static byte[] getImageArray() {
        return  byteArray;
    }

    public static String getImageBase() {
        return imageb64;
    }

}