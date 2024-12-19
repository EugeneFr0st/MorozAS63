package com.example.lab6savkops;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editTextId;
    private Button buttonDownload, buttonView, buttonDelete;
    private String filePath;

    private static final String PREFS_NAME = "MyAppPreferences";
    private static final String PREF_SHOW_POPUP = "show_popup";
    private boolean isPopupShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = findViewById(R.id.editTextId);
        buttonDownload = findViewById(R.id.buttonDownload);
        buttonView = findViewById(R.id.buttonView);
        buttonDelete = findViewById(R.id.buttonDelete);

        buttonDownload.setOnClickListener(v -> downloadFile());
        buttonView.setOnClickListener(v -> viewFile());
        buttonDelete.setOnClickListener(v -> deleteFile());

        buttonView.setVisibility(View.GONE);
        buttonDelete.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInstructionPopupIfNeeded();
    }

    private void showInstructionPopupIfNeeded() {
        if (isPopupShown || isFinishing() || isDestroyed()) return;

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean showPopup = preferences.getBoolean(PREF_SHOW_POPUP, true);

        if (showPopup) {

            new android.os.Handler().postDelayed(this::showInstructionPopup, 100);
        }
    }

    private void showInstructionPopup() {

        if (isFinishing() || isDestroyed()) {
            return;
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView textViewInstruction = popupView.findViewById(R.id.textViewInstruction);
        CheckBox checkBoxDoNotShow = popupView.findViewById(R.id.checkBoxDoNotShow);
        Button buttonOk = popupView.findViewById(R.id.buttonOk);

        buttonOk.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREF_SHOW_POPUP, !checkBoxDoNotShow.isChecked());
            editor.apply();
            popupWindow.dismiss();
            isPopupShown = false;
        });

        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        isPopupShown = true;
    }

    private void downloadFile() {
        String journalId = editTextId.getText().toString().trim();
        if (journalId.isEmpty()) {
            Toast.makeText(this, "Введите ID журнала", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://ntv.ifmo.ru/file/journal/" + journalId + ".pdf";
        Log.d("Download URL", url);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Download Error", e.getMessage());
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Response Code", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    String contentType = response.header("Content-Type");
                    Log.d("Content Type", contentType);

                    byte[] bytes = response.body().bytes();
                    Log.d("Downloaded Size", String.valueOf(bytes.length));

                    if ("application/pdf".equals(contentType) && bytes.length > 0) {
                        saveFile(bytes, journalId);
                    } else {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Файл не найден или неверный тип", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Файл не найден. Код ответа: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void saveFile(byte[] bytes, String journalId) {
        File directory = new File(getExternalFilesDir(null), "Journals");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "journal_" + System.currentTimeMillis() + ".pdf";
        filePath = new File(directory, fileName).getAbsolutePath();

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(bytes);
            fos.flush();
            Log.d("File Save Size", String.valueOf(bytes.length));
            runOnUiThread(() -> {
                buttonView.setVisibility(View.VISIBLE);
                buttonDelete.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Загрузка завершена", Toast.LENGTH_SHORT).show();
            });
            Log.d("File Save Path", filePath);
        } catch (IOException e) {
            Log.e("File Save Error", e.getMessage());
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Ошибка сохранения файла", Toast.LENGTH_SHORT).show());
        }
    }

    private void viewFile() {
        File file = new File(filePath);
        if (file.exists()) {
            Log.d("File Check", "File exists: " + filePath);
            Log.d("File Size", String.valueOf(file.length()));

            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
            Log.d("File URI", uri.toString());

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.putExtra(Intent.EXTRA_TITLE, "journal_" + System.currentTimeMillis() + ".pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "Нет приложения для открытия PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteFile() {
        File file = new File(filePath);
        if (file.exists() && file.delete()) {
            Toast.makeText(this, "Файл удалён", Toast.LENGTH_SHORT).show();
            buttonView.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "Ошибка удаления файла", Toast.LENGTH_SHORT).show();
        }
    }
}