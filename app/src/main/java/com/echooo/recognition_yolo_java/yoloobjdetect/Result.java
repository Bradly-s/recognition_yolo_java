package com.echooo.recognition_yolo_java.yoloobjdetect;

import android.graphics.Rect;

public class Result {
    public int classIndex;
    public Float score;
    public Rect rect;

    public Result(int cls, Float output, Rect rect) {
        this.classIndex = cls;
        this.score = output;
        this.rect = rect;
    }
}
