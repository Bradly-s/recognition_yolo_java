package com.echooo.recognition_yolo_java.pythonProject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.python.util.PythonInterpreter;

import java.io.IOException;

public class PythonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PythonInterpreter interpreter = new PythonInterpreter();
        String scriptPath = null;
        try {
            scriptPath = getAssets().open("helloworld.py").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        PythonExecutor.runScript(scriptPath);

    }
}