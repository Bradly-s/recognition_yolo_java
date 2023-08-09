package com.echooo.recognition_yolo_java.view.activity;

import com.echooo.recognition_yolo_java.R;
import com.echooo.recognition_yolo_java.base.BaseSwipeBackActivity;
import com.echooo.recognition_yolo_java.presenter.SettingPresenter;
import com.echooo.recognition_yolo_java.view.vinterface.SettingVInterface;

public class SettingActivity extends BaseSwipeBackActivity<SettingVInterface, SettingPresenter> implements SettingVInterface {


    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    protected int setContentViewById() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initAttributes() {

    }

    @Override
    protected void initView() {
        showToolbarAndShowNavigation("设置");
    }

    @Override
    protected void findAllViewById() {

    }
}
