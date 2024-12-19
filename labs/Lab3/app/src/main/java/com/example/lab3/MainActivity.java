package com.example.lab3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText editTextPhone, editTextFirstName, editTextLastName;
    private Button buttonRegister;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        buttonRegister = findViewById(R.id.buttonRegister);

        sharedPreferences = getSharedPreferences("TaxiApp", MODE_PRIVATE);
        loadUserData();

        buttonRegister.setOnClickListener(v -> {
            String phone = editTextPhone.getText().toString();
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("phone", phone);
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        String phone = sharedPreferences.getString("phone", "");
        String firstName = sharedPreferences.getString("firstName", "");
        String lastName = sharedPreferences.getString("lastName", "");

        editTextPhone.setText(phone);
        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);
        buttonRegister.setText(phone.isEmpty() ? "Registration" : "Log in");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", editTextPhone.getText().toString());
        editor.putString("firstName", editTextFirstName.getText().toString());
        editor.putString("lastName", editTextLastName.getText().toString());
        editor.apply();
    }
}