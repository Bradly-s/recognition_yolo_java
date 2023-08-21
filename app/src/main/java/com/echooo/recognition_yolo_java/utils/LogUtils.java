package com.echooo.recognition_yolo_java.utils;

import android.util.Log;

/**
 * Created by Goo on 2016-8-28.
 * Log 工具类
 */
public class LogUtils {
    private final static String TAG = "AI-Pet";
    private static boolean isShowLog = true;

    public static void e(String msg) {
        if (isShowLog) {
            Log.e(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (isShowLog) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (isShowLog) {
            Log.d(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (isShowLog) {
            Log.v(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (isShowLog) {
            Log.wtf(TAG, msg);
        }
    }

    public static void logWithMethodInfo(String... message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length >= 4) {
            StackTraceElement caller = stackTrace[3]; // Index 3 corresponds to the calling method
            String className = caller.getClassName();
            String methodName = caller.getMethodName();
            String logMessage = "";
            if (message.length > 0) {
                logMessage = "Class: " + className + ", Method: " + methodName + " - " + message[0];
            }else {
                logMessage = "Class: " + className + ", Method: " + methodName + " - ";
            }

            String prefix = "com.echooo.recognition_yolo_java.";
            int startIndex = logMessage.indexOf(prefix);
            if (startIndex != -1) {
                String newlogMessage = logMessage.substring(startIndex + prefix.length());
                Log.e(TAG, newlogMessage);
            }
        }
    }
}
