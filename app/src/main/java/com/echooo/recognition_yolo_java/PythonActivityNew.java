package com.echooo.recognition_yolo_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import com.echooo.recognition_yolo_java.utils.LogUtils;
import com.echooo.recognition_yolo_java.utils.PythonConsoleActivity;

public class PythonActivityNew extends PythonConsoleActivity {


    @Override
    protected Class<? extends Task> getTaskClass() {
        return Task.class;
    }


    public static class Task extends PythonConsoleActivity.Task {
        public Task(Application app) {
            super(app);
            LogUtils.logWithMethodInfo("Task");
        }


        @Override
        public void run() {
            LogUtils.logWithMethodInfo("run()");
            py.getModule("helloworld").callAttr("print_hi");
        }
    }
}