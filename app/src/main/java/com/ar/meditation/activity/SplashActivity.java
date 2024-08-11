package com.ar.meditation.activity;

import static kotlinx.coroutines.DelayKt.delay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.ar.meditation.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge();
        setContentView(R.layout.activity_splash);

        // Create a new thread to handle the delay and transition
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000); // Delay for 2 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Start the new activity and finish the current one on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                        finish();
                    }
                });
            }
        }).start();
    }

    private void enableEdgeToEdge() {
        // Set the decor view to enable full-screen layout
        Window window = getWindow();

        // Make sure that the content extends into the system bars (status bar and navigation bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false);
        }

        // Set system bars to be transparent
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        // Optionally, handle light or dark mode for the status bar icons
        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // Light status bar (dark icons)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR; // Light navigation bar (dark icons)
        }
        window.getDecorView().setSystemUiVisibility(flags);
    }
}