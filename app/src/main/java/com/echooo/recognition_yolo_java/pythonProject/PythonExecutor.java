package com.echooo.recognition_yolo_java.pythonProject;


import com.echooo.recognition_yolo_java.utils.LogUtils;

import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * java调用python的执行器
 */

public class PythonExecutor {
    public static String runScript(InputStream scriptPath) {
        LogUtils.logWithMethodInfo("scriptPath:" + scriptPath);
        PythonInterpreter interpreter = new PythonInterpreter();

        interpreter.execfile(scriptPath);

        PyObject obj = interpreter.get("print_hi");
        PyObject result = obj.__call__();
        return result.toString();
    }

}