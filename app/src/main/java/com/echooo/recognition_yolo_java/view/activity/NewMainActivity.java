package com.echooo.recognition_yolo_java.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CompoundButton;

import com.echooo.recognition_yolo_java.R;
import com.echooo.recognition_yolo_java.service.FloatingWindowService;
import com.echooo.recognition_yolo_java.utils.LogUtils;

public class NewMainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        SwitchCompat floatSwitch = findViewById(R.id.float_switch);
        floatSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                LogUtils.logWithMethodInfo("isChecked" + isChecked);
                requestFloatingWindowPermission();
            } else {
                LogUtils.logWithMethodInfo();
                stopService(new Intent(NewMainActivity.this, FloatingWindowService.class));
            }
        });
    }

    private void requestFloatingWindowPermission() {
        if (Settings.canDrawOverlays(this)) {
            startService(new Intent(NewMainActivity.this, FloatingWindowService.class));
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
            startActivityForResult(intent, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(NewMainActivity.this, FloatingWindowService.class));
            }
        }
    }


}