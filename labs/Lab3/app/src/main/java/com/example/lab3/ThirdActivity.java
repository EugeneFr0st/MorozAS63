package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {
    private EditText editTextRouteParam1, editTextRouteParam2, editTextRouteParam3, editTextRouteParam4, editTextRouteParam5, editTextRouteParam6;
    private Button buttonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        editTextRouteParam1 = findViewById(R.id.editTextRouteParam1);
        editTextRouteParam2 = findViewById(R.id.editTextRouteParam2);
        editTextRouteParam3 = findViewById(R.id.editTextRouteParam3);
        editTextRouteParam4 = findViewById(R.id.editTextRouteParam4);
        editTextRouteParam5 = findViewById(R.id.editTextRouteParam5);
        editTextRouteParam6 = findViewById(R.id.editTextRouteParam6);
        buttonOk = findViewById(R.id.buttonOk);

        buttonOk.setOnClickListener(v -> {
            String route = editTextRouteParam1.getText().toString() + ", " +
                    editTextRouteParam2.getText().toString() + ", " +
                    editTextRouteParam3.getText().toString() + ", " +
                    editTextRouteParam4.getText().toString() + ", " +
                    editTextRouteParam5.getText().toString() + ", " +
                    editTextRouteParam6.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("route", route);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}