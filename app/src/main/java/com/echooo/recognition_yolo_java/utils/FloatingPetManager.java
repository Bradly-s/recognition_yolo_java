package com.echooo.recognition_yolo_java.utils;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.echooo.recognition_yolo_java.utils.ToastUtil.showToast;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager;

import com.echooo.recognition_yolo_java.view.widget.FloatingPetView;

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

    /** 悬浮窗权限标识码 */
    public static final int CODE_WINDOW = 0;

    /**
     * 创建悬浮宠物
     *
     * @param context
     */
    public static void createPetWindow(Context context) {
        // 在Activity中动态请求悬浮窗口权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            showToast(context, "请打开此应用悬浮窗权限");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } else {
            // 在获得权限后再调用createPetWindow方法
            WindowManager windowManager = getWindowManager(context);
            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            int screenHeight = windowManager.getDefaultDisplay().getHeight();
            if (mFPetView == null) {
                mFPetView = new FloatingPetView(context);
                if (mParams == null) {
                    mParams = new WindowManager.LayoutParams();
//                    mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                    mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    mParams.format = PixelFormat.RGBA_8888;
                    mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                    mParams.gravity = Gravity.LEFT | Gravity.TOP;
//                    mParams.width = mFPetView.viewWidth;
//                    mParams.height = mFPetView.viewHeight;

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


    }

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


}
