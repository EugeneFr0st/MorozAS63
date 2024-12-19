// AudioActivity.java
package com.example.lab7;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AudioActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.audiofile); // Убедитесь, что файл существует в res/raw
            if (mediaPlayer == null) {
                throw new RuntimeException("Audio file not found");
            }

            findViewById(R.id.btnPlayAudio).setOnClickListener(view -> mediaPlayer.start());
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing audio", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
