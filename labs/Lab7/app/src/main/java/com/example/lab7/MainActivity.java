// MainActivity.java
package com.example.lab7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnAudio).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, AudioActivity.class)));

        findViewById(R.id.btnVideo).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, VideoActivity.class)));

        findViewById(R.id.btnPhoto).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, PhotoActivity.class)));
    }
}
