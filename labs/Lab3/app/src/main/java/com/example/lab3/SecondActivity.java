package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    private TextView textViewUserInfo, textViewRoute;
    private Button buttonSetPath, buttonCallTaxi;
    private static final int REQUEST_CODE_PATH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textViewUserInfo = findViewById(R.id.textViewUserInfo);
        textViewRoute = findViewById(R.id.textViewRoute);
        buttonSetPath = findViewById(R.id.buttonSetPath);
        buttonCallTaxi = findViewById(R.id.buttonCallTaxi);

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");

        textViewUserInfo.setText("Имя: " + firstName + " " + lastName + "\nТелефон: " + phone);

        buttonSetPath.setOnClickListener(v -> {
            Intent pathIntent = new Intent(SecondActivity.this, ThirdActivity.class);
            startActivityForResult(pathIntent, REQUEST_CODE_PATH);
        });

        buttonCallTaxi.setOnClickListener(v -> {
            Toast.makeText(SecondActivity.this, "Такси успешно вызвано!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PATH && resultCode == RESULT_OK) {
            String route = data.getStringExtra("route");
            textViewRoute.setText("Маршрут: " + route);
            buttonCallTaxi.setEnabled(true);
        }
    }
}