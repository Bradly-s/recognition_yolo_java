package com.echooo.recognition_yolo_java.model.minterface;

import android.util.SparseArray;

import com.echooo.recognition_yolo_java.base.BaseIntroFragment;

import java.util.List;

/**
 * Created by Goo on 2016-8-31.
 */
public interface IntroMInterface {

    List<BaseIntroFragment> getIntroFragemnts();

    SparseArray<int[]> getLayoutViewIdsMap();
}
