// VideoActivity.java
package com.example.lab7;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoView videoView = findViewById(R.id.videoView);

        try {
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videofile); // Убедитесь, что файл существует в res/raw
            videoView.setVideoURI(videoUri);
            videoView.start();
        } catch (Exception e) {
            Toast.makeText(this, "Error playing video", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
