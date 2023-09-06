package com.echooo.recognition_yolo_java.utils;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.echooo.recognition_yolo_java.utils.ToastUtil.showToast;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.echooo.recognition_yolo_java.view.widget.FloatingPetView;

import java.util.List;

/**
 * 悬浮宠物管理类
 * Created by Goo on 2016-9-18.
 */
public class FloatingPetManager {

    /**
     * 悬浮宠物实例
     */
    private static FloatingPetView mFPetView;

    /**
     * 增添及删除悬浮窗
     */
    private static WindowManager mWindowManager;

    /**
     * 参数
     */
    private static WindowManager.LayoutParams mParams;

    /**
     * 悬浮窗权限标识码
     */
    public static final int CODE_WINDOW = 0;

    /**
     * 请求权限的标识码
     */
    private static final int REQUEST_CODE = 123;

    /**
     * 创建悬浮宠物
     *
     * @param context
     */
//    修改
    public static void createPetWindow(Context context, Application application, ActivityManager activityManager) {
        LogUtils.logWithMethodInfo();

        // 在获得权限后再调用createPetWindow方法
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (mFPetView == null) {
            mFPetView = new FloatingPetView(context, application);
            if (mParams == null) {
                mParams = new WindowManager.LayoutParams();
                mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                mParams.format = PixelFormat.RGBA_8888;
                mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                mParams.gravity = Gravity.LEFT | Gravity.TOP; // 悬浮窗始终贴在屏幕的右边
                mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                mParams.x = screenWidth;
                mParams.y = screenHeight / 2; // 初始位置居中
            }
            mFPetView.setParams(mParams);
            windowManager.addView(mFPetView, mParams);
        }
    }
//    public static void createPetWindow(Context context, Application application, ActivityManager activityManager) {
//        LogUtils.logWithMethodInfo();
//
//        // 在Activity中动态请求悬浮窗口权限
//        // 申请悬浮窗权限
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
//            LogUtils.logWithMethodInfo("进入判断");
////            showToast(context, "请打开此应用悬浮窗权限");
//            ToastUtil.showToast(context.getApplicationContext(), "请打开此应用悬浮窗权限");
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//为待启动的Activity指定FLAG_ACTIVITY_NEW_TASK标记位。
////            没有授予权限就返回，则会出现强制回到授予权限页面
//            context.startActivity(intent);
////            ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
//
//        }
//        else {
//            // 在获得权限后再调用createPetWindow方法
//            WindowManager windowManager = getWindowManager(context);
//            int screenWidth = windowManager.getDefaultDisplay().getWidth();
//            int screenHeight = windowManager.getDefaultDisplay().getHeight();
//            if (mFPetView == null) {
//                mFPetView = new FloatingPetView(context, application);
//                if (mParams == null) {
//                    mParams = new WindowManager.LayoutParams();
//                    mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//                    mParams.format = PixelFormat.RGBA_8888;
//                    mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                    mParams.gravity = Gravity.LEFT | Gravity.TOP; // 悬浮窗始终贴在屏幕的右边
//                    mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                    mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                    mParams.x = screenWidth;
//                    mParams.y = screenHeight / 2; // 初始位置居中
//                }
//                mFPetView.setParams(mParams);
//                windowManager.addView(mFPetView, mParams);
//            }
//        }
//    }


    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isFloatingWindowShowing() {
        return mFPetView != null;
    }

    /**
     * 关闭悬浮窗
     */
    public static void isCloseWindowShowing() {
        if (mFPetView != null && mWindowManager != null) {
            LogUtils.logWithMethodInfo();
            mWindowManager.removeView(mFPetView);

            mFPetView = null;
        }
    }

}
