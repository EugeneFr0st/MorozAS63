package com.example.lab1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends Activity {

    EditText searchQueryEditText;
    Button searchButton;
    ImageView imagePreview;
    TextView imageSourceUrl;
    Button downloadButton;
    Button openWebsiteButton;
    String imageUrl = "";
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchQueryEditText = findViewById(R.id.searchQueryEditText);
        searchButton = findViewById(R.id.searchButton);
        imagePreview = findViewById(R.id.imagePreview);
        imageSourceUrl = findViewById(R.id.imageSourceUrl);
        downloadButton = findViewById(R.id.downloadButton);
        openWebsiteButton = findViewById(R.id.openWebsiteButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchQueryEditText.getText().toString();
                //  Замените на реальную логику поиска изображений
                new ImageSearchTask().execute(searchQuery);
            }

        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imageUrl.isEmpty()) {
                    new DownloadImageTask().execute(imageUrl);
                } else {
                    Toast.makeText(MainActivity.this, "Нет изображения для скачивания", Toast.LENGTH_SHORT).show();
                }
            }
        });

        openWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imageUrl.isEmpty()) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(imageUrl)); // Замените на URL веб-сайта
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Нет URL для открытия", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private class ImageSearchTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            String searchQuery = params[0];
            try {
                //  Замените на реальную логику поиска и получения URL изображения
                URL url = new URL("https://via.placeholder.com/150"); // Пример
                imageUrl = url.toString();
                InputStream in = url.openStream();
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imagePreview.setImageBitmap(result);
                imageSourceUrl.setText(imageUrl);
            } else {
                Toast.makeText(MainActivity.this, "Изображение не найдено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            try {
                URL url = new URL(urlString);
                InputStream in = url.openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);

                File storagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File imageFile = new File(storagePath, "downloaded_image.jpg"); //  Имя файла

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                return "Изображение скачано в " + imageFile.getAbsolutePath();

            } catch (Exception e) {
                e.printStackTrace();
                return "Ошибка скачивания";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}