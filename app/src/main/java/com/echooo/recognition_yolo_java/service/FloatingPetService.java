package com.echooo.recognition_yolo_java.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
//import android.support.annotation.Nullable;
import androidx.annotation.Nullable;

import com.echooo.recognition_yolo_java.utils.FloatingRefreshTask;
import com.echooo.recognition_yolo_java.utils.LogUtils;

import java.util.Timer;

/**
 * Created by Goo on 2016-9-18.
 */
public class FloatingPetService extends Service {
    private Timer mTimer = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        LogUtils.e("FloatingPetService - onCreate");
        System.out.println("FloatingPetService - onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e("FloatingPetService - onStartCommand");
        System.out.println("FloatingPetService - onStartCommand");
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new FloatingRefreshTask(this.getPackageManager(), (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE), getApplicationContext()), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtils.e("FloatingPetService - onDestroy");
        mTimer.cancel();
        mTimer = null;
        super.onDestroy();
    }
}