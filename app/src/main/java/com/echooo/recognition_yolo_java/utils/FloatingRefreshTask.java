package com.echooo.recognition_yolo_java.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;

import java.util.TimerTask;

/**
 * Created by Goo on 2016-9-18.
 */
public class FloatingRefreshTask extends TimerTask {
    private PackageManager mPackageManager;
    private ActivityManager mActivityManager;
    private Context mContext;
    private Application mApplication;
    private Handler handler = new Handler();

    public FloatingRefreshTask(PackageManager packageManager, ActivityManager activityManager, Context context, Application application) {
        mPackageManager = packageManager;
        mActivityManager = activityManager;
        mContext = context;
        mApplication = application;
    }

    @Override
    public void run() {
        LogUtils.logWithMethodInfo();
//       第二次打开悬浮窗的时候，没有进入if判断   ===>>  连续打开/关闭悬浮窗的时候，第二次会 打不开悬浮窗，然后关闭的时候闪退  【解决】
        //当前界面为桌面且没有显示悬浮窗，则显示悬浮窗，否则移除悬浮窗
        if (FloatingUtils.isHome(mActivityManager, mPackageManager) && !FloatingPetManager.isFloatingWindowShowing()) {
            LogUtils.logWithMethodInfo("进入if判断");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    LogUtils.logWithMethodInfo("启动 run");
                    FloatingPetManager.createPetWindow(mContext, mApplication, mActivityManager);
                }
            });
        }
//        //当前界面不为桌面，而有显示悬浮窗，需要移除悬浮窗
//        if (!FloatingUtils.isHome(mActivityManager, mPackageManager) && FloatingPetManager.isFloatingWindowShowing()) {
//
//        }
//        //当前界面为桌面，有显示悬浮窗，需要内容更新
//        if (FloatingUtils.isHome(mActivityManager, mPackageManager) && FloatingPetManager.isFloatingWindowShowing()) {
//
//        }
    }

//    以下为新添加

    public void showFloatingWindow() {
        if (FloatingUtils.isHome(mActivityManager, mPackageManager) && !FloatingPetManager.isFloatingWindowShowing()) {
            FloatingPetManager.createPetWindow(mContext, mApplication, mActivityManager);
        }
    }

    /**关闭悬浮窗
     * */
    public static void closeFloatingWindow(){
//        有悬浮窗的话，则关闭
        if (FloatingPetManager.isFloatingWindowShowing()) {
            LogUtils.logWithMethodInfo("关闭悬浮窗");
            FloatingPetManager.isCloseWindowShowing();

        }
    }


}
