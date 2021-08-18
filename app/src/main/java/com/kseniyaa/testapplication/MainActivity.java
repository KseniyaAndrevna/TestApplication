package com.kseniyaa.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    int RESULT = 1;
    public static ConstraintLayout rootView;
    ImageButton soundBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Settings.canDrawOverlays(this)) {
            Intent intent;
            intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, RESULT);
        }

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 0;

        rootView = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        soundBtn = rootView.findViewById(R.id.sound_btn);
        soundBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent service = new Intent(MainActivity.this, SoundService.class);
                if (isMyServiceRunning(SoundService.class)) {
                    stopService(service);
                    soundBtn.setImageResource(R.drawable.ic_play);
                } else {
                    startService(service);
                    soundBtn.setImageResource(R.drawable.ic_pause);
                }
            }
        });
        manager.addView(rootView, params);
    }

    @SuppressWarnings("deprecation")
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
