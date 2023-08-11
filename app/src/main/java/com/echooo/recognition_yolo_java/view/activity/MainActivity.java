package com.echooo.recognition_yolo_java.view.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
//import android.support.constraint.ConstraintLayout;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
//import android.support.design.widget.FloatingActionButton;
import com.echooo.recognition_yolo_java.utils.FloatingRefreshTask;
import com.echooo.recognition_yolo_java.utils.LogUtils;
import com.echooo.recognition_yolo_java.yolov5ncnn.yoloMainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.echooo.recognition_yolo_java.R;
import com.echooo.recognition_yolo_java.adapter.RVMainAdapter;
import com.echooo.recognition_yolo_java.base.BaseActivity;
import com.echooo.recognition_yolo_java.listener.HidingScrollListener;
import com.echooo.recognition_yolo_java.presenter.MainPresenter;
import com.echooo.recognition_yolo_java.service.FloatingPetService;
import com.echooo.recognition_yolo_java.utils.DimenUtils;
import com.echooo.recognition_yolo_java.view.vinterface.MainVInterface;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity<MainVInterface, MainPresenter> implements MainVInterface, View.OnClickListener {


    private RecyclerView mRvMain;
    private FloatingActionButton mFABSetting;
    private SwitchCompat yolo_test;

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
        mFABSetting.setOnClickListener(this);
        yolo_test.setOnClickListener(this);
    }

    /**
     * 加载R
     */
    private void initRv() {
        RVMainAdapter rvAdapter = mPresenter.getRVAdapter(this);
        mRvMain.setLayoutManager(new LinearLayoutManager(this));
        mRvMain.setAdapter(rvAdapter);
        mRvMain.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideFAB();
            }

            @Override
            public void onShow() {
                showFAB();
            }
        });
        ItemTouchHelper itemHelper = mPresenter.getItemTouchHelper(rvAdapter);
        itemHelper.attachToRecyclerView(mRvMain);
    }


    /**
     * 显示悬浮按钮
     */
    private void showFAB() {
        mFABSetting.animate().translationY(0).setInterpolator(new DecelerateInterpolator(1)).start();
    }

    /**
     * 隐藏悬浮按钮
     */
    private void hideFAB() {
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) mFABSetting.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFABSetting.animate().translationY(mFABSetting.getHeight() + fabBottomMargin + DimenUtils.getNavBarHeight(this) + DimenUtils.getStatusBarHeight(this)).
                setInterpolator(new AccelerateInterpolator(2)).start();
    }

    /**
     * 主界面、设置*/
    @Override
    protected void findAllViewById() {
        mRvMain = $(R.id.rv_main);
        mFABSetting = $(R.id.fab_setting);
        yolo_test = $(R.id.yolo_test);
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
        } else if (viewId == R.id.fab_setting) {
            System.out.println("====R.id.fab_setting =======");
            LogUtils.e("====R.id.fab_setting =======");
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivityWithAnim(intent);
        }
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


//        测试调用yolo是否可行 【临时代码】
        else if (viewId == R.id.yolo_test) {
            System.out.println("单选按钮1点击！");
            LogUtils.e("单选按钮1点击！");
//            启动yoloMainActivity
            Intent intentYolo = new Intent(MainActivity.this, yoloMainActivity.class);
            startActivity(intentYolo);

        }

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
        System.out.println("进入launchDesktopPet");
        LogUtils.e("进入launchDesktopPet");
        //启动悬浮pet
        Intent intent = new Intent(MainActivity.this, FloatingPetService.class);
        startService(intent);

        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);

//        以下为新添加
        // 调用显示悬浮窗的方法
        new FloatingRefreshTask(getPackageManager(), (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE), getApplicationContext()).showFloatingWindow();

    }
}
