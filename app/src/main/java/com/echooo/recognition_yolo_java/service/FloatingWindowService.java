package com.echooo.recognition_yolo_java.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.echooo.recognition_yolo_java.R;
import com.echooo.recognition_yolo_java.utils.LogUtils;

public class FloatingWindowService extends Service {

    private WindowManager windowManager;
    private View floatingView;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.logWithMethodInfo();

        // Initialize the WindowManager
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        // Inflate the floating window layout
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_window, null);

        // Set up the layout parameters for the floating window
        WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LogUtils.logWithMethodInfo("Build.VERSION.SDK_INT >= Build.VERSION_CODES.O");
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        }

        // Set up the position of the floating window
        params.x = 0;
        params.y = 0;

        // Add the view to the WindowManager
        windowManager.addView(floatingView, params);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) {
            windowManager.removeView(floatingView);
        }
    }
}
