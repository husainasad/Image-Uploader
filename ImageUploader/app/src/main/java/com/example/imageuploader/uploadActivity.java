package com.example.imageuploader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class uploadActivity extends AppCompatActivity {
    private Spinner spinnerTag;
//    temp code delete later
//    private Button prevPage;
//    temp code delete later
    private String tag;
//    private String URL = "http://192.168.0.192:5000/";
    private String URL = "http:10.157.222.191:5000/";
    private String imageNameValue = MainActivity.getImageName();
//    private Bitmap imageBitmapValue = MainActivity.getImageBitmap();
    private Button upload;
//    private byte[] imgArray = MainActivity.getImageArray();
    private String imageBase64 = MainActivity.getImageBase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);

        upload = findViewById(R.id.uploadBtn);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest("POST", "categoryName", tag);
            }
        });

        spinnerTag = findViewById(R.id.tagSelect);
        String[] tagNames = getResources().getStringArray(R.array.imageTags);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tagNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTag.setAdapter(adapter);
//        spinnerTag.setSelection(0, false);  //must

//        temp code delete later
//        prevPage=findViewById(R.id.prevPgBtn);
//        prevPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(uploadActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
//        temp code delete later

        spinnerTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                tag = parent.getItemAtPosition(pos).toString();
//                Toast.makeText(uploadActivity.this, tag, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void sendRequest(String type, String categoryName, String categoryValue) {

        OkHttpClient client = new OkHttpClient();
        RequestBody formbody = new FormBody.Builder().add(categoryName,categoryValue).add("imageName",imageNameValue).add("imageBase",imageBase64).build();
        Request request = new Request.Builder().url(URL).post(formbody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(uploadActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(uploadActivity.this, response.body().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Intent intent = new Intent(uploadActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}