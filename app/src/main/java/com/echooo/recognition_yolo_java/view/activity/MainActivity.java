package com.echooo.recognition_yolo_java.view.activity;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
//import android.support.constraint.ConstraintLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
//import android.support.design.widget.FloatingActionButton;
import com.echooo.recognition_yolo_java.utils.FloatingRefreshTask;
import com.echooo.recognition_yolo_java.utils.LogUtils;
import com.echooo.recognition_yolo_java.view.fragment.Fragment1;
import com.echooo.recognition_yolo_java.view.fragment.Fragment2;
import com.echooo.recognition_yolo_java.view.fragment.Fragment3;
import com.echooo.recognition_yolo_java.yoloobjdetect.MainActivity2;
import com.echooo.recognition_yolo_java.yolov5ncnn.yoloMainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.echooo.recognition_yolo_java.R;
import com.echooo.recognition_yolo_java.adapter.RVMainAdapter;
import com.echooo.recognition_yolo_java.base.BaseActivity;
import com.echooo.recognition_yolo_java.listener.HidingScrollListener;
import com.echooo.recognition_yolo_java.presenter.MainPresenter;
import com.echooo.recognition_yolo_java.service.FloatingPetService;
import com.echooo.recognition_yolo_java.utils.DimenUtils;
import com.echooo.recognition_yolo_java.view.vinterface.MainVInterface;

import com.echooo.recognition_yolo_java.view.widget.FloatingPetView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity<MainVInterface, MainPresenter> implements MainVInterface, View.OnClickListener {


    private static final int IMAGE_PICK_REQUEST_CODE = 101;

    private FloatingPetView floatingPetView;
    private RecyclerView mRvMain;
    private FloatingActionButton mFABSetting;
    private SwitchCompat yolo_test;

    private static final int REQUEST_IMAGE_PICK = 101; // Arbitrary request code

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int setContentViewById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initAttributes() {

    }

    @Override
    protected void initView() {
        findAllViewById();
        initRv();
//        mFABSetting.setOnClickListener(this);
//        yolo_test.setOnClickListener(this);

        // 创建 FloatingPetView 实例并设置 MainActivity
        floatingPetView = new FloatingPetView(this, getApplication()); // 这里的 this 是指当前的 MainActivity
//        floatingPetView = new FloatingPetView(MainActivity.this, null); // 这里的 this 是指当前的 MainActivity
//        floatingPetView.setMainActivity(this); // 将 MainActivity 实例传递给 FloatingPetView
//        floatingPetView.setMainActivity(this); // 将 MainActivity 实例传递给 FloatingPetView


//        viewPager = findViewById(R.id.view_pager);
//        setupViewPager(viewPager);
//
//        tabLayout = findViewById(R.id.tab_layout);
//        tabLayout.setupWithViewPager(viewPager);
    }


    /**
     * 加载R
     */
    private void initRv() {
        RVMainAdapter rvAdapter = mPresenter.getRVAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        mRvMain.setLayoutManager(gridLayoutManager);
        mRvMain.setAdapter(rvAdapter);
        ItemTouchHelper itemHelper = mPresenter.getItemTouchHelper(rvAdapter);
        itemHelper.attachToRecyclerView(mRvMain);
    }



    /**
     * 主界面、设置*/
    @Override
    protected void findAllViewById() {
        mRvMain = $(R.id.rv_main);
//        mFABSetting = $(R.id.fab_setting);
//        yolo_test = $(R.id.yolo_test);
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.rv_main:
//                break;
//            case R.id.fab_setting:
//                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//                startActivityWithAnim(intent);
//                break;
////            case R.id.btn_show:
////                //启动悬浮pet
////                Intent intent = new Intent(MainActivity.this, FloatingPetService.class);
////                startService(intent);
////                Intent home = new Intent(Intent.ACTION_MAIN);
////                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                home.addCategory(Intent.CATEGORY_HOME);
////                startActivity(home);
////                break;
//        }
//    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.rv_main) {
            System.out.println("====R.id.rv_main=======");
            LogUtils.e("====R.id.rv_main=======");

            // 处理 RecyclerView 的点击事件
        }
//        else if (viewId == R.id.fab_setting) {
//            System.out.println("====R.id.fab_setting =======");
//            LogUtils.e("====R.id.fab_setting =======");
//            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//            startActivityWithAnim(intent);
//        }
//        注：源代码中此处已注释掉
////        else if (viewId == R.id.btn_show) {
//        else if (viewId == R.id.sw_select) {
//            System.out.println("--------R.id.sw_select-----");
//
//            // 启动悬浮pet
//            Intent intent = new Intent(MainActivity.this, FloatingPetService.class);
//            startService(intent);
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
//        }


////        测试调用yolo是否可行 【临时代码】
//        else if (viewId == R.id.yolo_test) {
//            System.out.println("单选按钮1点击！");
//            LogUtils.e("单选按钮1点击！");
////            启动yoloMainActivity
////            ncnn方式
////            Intent intentYolo = new Intent(MainActivity.this, yoloMainActivity.class);
////            startActivity(intentYolo);
//
////            torchscript.ptl方式
//            Intent intentYolo = new Intent(MainActivity.this, MainActivity2.class);
//            startActivity(intentYolo);
//
//        }

    }



    @Override
    protected void onDestroy() {
//        stopService();
        super.onDestroy();
    }

    /** 启动悬浮pet
     * */
    @Override
    public void launchDesktopPet() {
        LogUtils.logWithMethodInfo();

//        原先代码
        //启动悬浮pet
        Intent intent = new Intent(MainActivity.this, FloatingPetService.class);
        startService(intent);

        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);

//        以下为新添加
        // 调用显示悬浮窗的方法
        new FloatingRefreshTask(getPackageManager(), (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE), getApplicationContext(), getApplication()).showFloatingWindow();

    }

    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.logWithMethodInfo();

        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            LogUtils.logWithMethodInfo("requestCode:" + requestCode + ",IMAGE_PICK_REQUEST_CODE:" + IMAGE_PICK_REQUEST_CODE);
            Uri selectedImageUri = data.getData();

            // 将选定的图片数据传递给 FloatingPetView 处理
            floatingPetView.handleImageSelection(selectedImageUri);
        }
    }


}
