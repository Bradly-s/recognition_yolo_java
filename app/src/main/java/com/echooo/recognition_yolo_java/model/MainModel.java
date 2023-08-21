package com.echooo.recognition_yolo_java.model;

import com.echooo.recognition_yolo_java.R;
import com.echooo.recognition_yolo_java.entity.PetInfo;
import com.echooo.recognition_yolo_java.model.minterface.MainMInterface;
import com.echooo.recognition_yolo_java.utils.AppConstants;

import java.util.ArrayList;

/**
 * Created by Goo on 2016-9-18.
 */
public class MainModel implements MainMInterface {
    @Override
    public ArrayList<PetInfo> getPetData() {
        ArrayList<PetInfo> data = new ArrayList<>();
//        data.add(new PetInfo(AppConstants.PET_OWL, "Bean", "A sleepless owl", R.drawable.pic_owl_main, false));
//        data.add(new PetInfo(AppConstants.PERSON_INFO, "个人中心", "Person info", R.drawable.pic_pig_main, false));
        data.add(new PetInfo(AppConstants.ELFIN_SET, "小精灵配置", "", R.drawable.pic_pig_main, false));
//        data.add(new PetInfo(AppConstants.PET_BIRD, "Pear", "A cute bird", R.drawable.pic_bird_main, false));
        return data;
    }
}
