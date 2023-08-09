package com.echooo.recognition_yolo_java.presenter;

import com.echooo.recognition_yolo_java.base.BasePresenter;
import com.echooo.recognition_yolo_java.model.SettingModel;
import com.echooo.recognition_yolo_java.model.minterface.SettingMInterface;
import com.echooo.recognition_yolo_java.view.vinterface.SettingVInterface;

/**
 * Created by Goo on 2016-10-24.
 */

public class SettingPresenter extends BasePresenter<SettingVInterface> {

    private SettingMInterface mModel;

    public SettingPresenter(SettingVInterface viewInterface) {
        super(viewInterface);
        mModel = new SettingModel();
    }
}
