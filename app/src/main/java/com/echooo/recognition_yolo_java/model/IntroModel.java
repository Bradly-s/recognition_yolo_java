package com.echooo.recognition_yolo_java.model;

import android.util.SparseArray;

import com.echooo.recognition_yolo_java.base.BaseIntroFragment;
import com.echooo.recognition_yolo_java.model.minterface.IntroMInterface;
import com.echooo.recognition_yolo_java.view.fragment.IntroFirstFragment;
import com.echooo.recognition_yolo_java.view.fragment.IntroFourthFragment;
import com.echooo.recognition_yolo_java.view.fragment.IntroSecondFragment;
import com.echooo.recognition_yolo_java.view.fragment.IntroThirdFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Goo on 2016-8-31.
 */
public class IntroModel implements IntroMInterface {
    private static List<BaseIntroFragment> list = null;
    private static SparseArray<int[]> layoutViewIdsMap = null;

    @Override
    public List<BaseIntroFragment> getIntroFragemnts() {
        if (list == null) {
            list = new ArrayList<>();
            list.add(new IntroFirstFragment());
            list.add(new IntroSecondFragment());
            list.add(new IntroThirdFragment());
            list.add(new IntroFourthFragment());
        }
        return list;
    }

    @Override
    public SparseArray<int[]> getLayoutViewIdsMap() {
        if (list != null && layoutViewIdsMap == null) {
            layoutViewIdsMap = new SparseArray<int[]>();
            for (int i = 0; i < list.size(); i++) {
                layoutViewIdsMap.put(list.get(i).getViewTag(), list.get(i).getChildViewIds());
            }
            return layoutViewIdsMap;
        }
        return null;
    }
}
