package com.example.lab_2;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.gesture_info);
        gestureDetector = new GestureDetector(this, new GestureListener());
        textView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        showInfo();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            textView.setText("Single Tap");
            Toast.makeText(MainActivity.this, "Single Tap detected", Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            textView.setText("Double Tap");
            Toast.makeText(MainActivity.this, "Double Tap detected", Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            textView.setText("Long Press");
            Toast.makeText(MainActivity.this, "Long Press detected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            textView.setText("Fling");
            Toast.makeText(MainActivity.this, "Fling detected", Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            textView.setText("Scroll");
            Toast.makeText(MainActivity.this, "Scroll detected", Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.gestures) {
            showGestures();
            return true;
        } else if (id == R.id.info) {
            showInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showGestures() {
        textView.setText("Perform a gesture on the screen.");
    }

    private void showInfo() {
        textView.setText("This app demonstrates various standard gestures such as:\n\n- Single Tap\n- Double Tap\n- Long Press\n- Scroll\n- Fling");
    }
}