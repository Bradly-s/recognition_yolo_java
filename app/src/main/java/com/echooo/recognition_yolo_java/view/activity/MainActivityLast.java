package com.echooo.recognition_yolo_java.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.echooo.recognition_yolo_java.R;
import com.echooo.recognition_yolo_java.service.FloatingPetService;
import com.echooo.recognition_yolo_java.utils.FloatingRefreshTask;
import com.echooo.recognition_yolo_java.utils.LogUtils;
import com.echooo.recognition_yolo_java.view.fragment.Fragment1;
import com.echooo.recognition_yolo_java.view.fragment.Fragment2;
import com.echooo.recognition_yolo_java.view.fragment.Fragment3;
import com.echooo.recognition_yolo_java.view.widget.FloatingPetView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.widget.SwitchCompat;

public class MainActivityLast extends AppCompatActivity implements Fragment1.OnSwitchChangeListener {

    private FloatingPetView floatingPetView;
    private View rootView;

    private static final int IMAGE_PICK_REQUEST_CODE = 101;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private SwitchCompat switchCompat;
    private Fragment1 fragment1; // 声明Fragment1的变量

    private Fragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

//        todo : 如何获取fragment1中的 开关，点击按钮的时候调用launchDesktopPet函数？？
        // 获取SwitchCompat组件

        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // 获取Fragment1的实例
        // 获取ViewPager的适配器
        ViewPagerAdapter adapterMY = (ViewPagerAdapter) viewPager.getAdapter();

        // 获取Fragment1的实例
        fragment1 = (Fragment1) adapterMY.getItem(0);
        LogUtils.logWithMethodInfo("fragment1：" + myFragment);
//
//        // 检查Fragment1是否成功获取
//        if (myFragment != null) {
//            // 获取Fragment1中的按钮
//            switchCompat = myFragment.requireView().findViewById(R.id.sw_select);
//            LogUtils.logWithMethodInfo("switchCompat");
//            // 设置开关的状态变化监听器
//            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        // SwitchCompat被选中时执行的操作
//                        launchDesktopPet();
//                    } else {
//                        // SwitchCompat被取消选中时执行的操作
//                        // 如果有需要的话，可以在此处添加相应的操作
//                    }
//                }
//            });
//        }




        // 创建 FloatingPetView 实例并设置 MainActivity
        // 设置SwitchCompat的状态更改监听器
        fragment1.setOnSwitchChangeListener(this);

        floatingPetView = new FloatingPetView(MainActivityLast.this, this.getApplication()); // 这里的 this 是指当前的 MainActivity
//        floatingPetView = new FloatingPetView(MainActivity.this, null); // 这里的 this 是指当前的 MainActivity
        floatingPetView.setMainActivity(this); // 将 MainActivity 实例传递给 FloatingPetView

//        if (!Python.isStarted()){
//            LogUtils.logWithMethodInfo("!Python.isStarted()");
//            Python.start(new AndroidPlatform(this));
//        }
//        Python python=Python.getInstance();
//        PyObject pyObject=python.getModule("helloworld");
//        pyObject.callAttr("sayHello");

    }

    // 实现OnSwitchChangeListener接口的方法
    @Override
    public void onSwitchChanged(boolean isChecked) {
        if (isChecked) {
            // SwitchCompat被选中时执行的操作
            launchDesktopPet();
        }
    }

    /**
     * 启动悬浮pet
     */
    public void launchDesktopPet() {
        LogUtils.logWithMethodInfo("调用成功啦！！");

//        原先代码
        //启动悬浮pet
        Intent intent = new Intent(MainActivityLast.this, FloatingPetService.class);
        startService(intent);

        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);

//        以下为新添加
        // 调用显示悬浮窗的方法
        new FloatingRefreshTask(getPackageManager(), (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE), getApplicationContext(), getApplication()).showFloatingWindow();

    }







    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void setupViewPager(ViewPager viewPager) {
        MainActivityLast.ViewPagerAdapter adapter = new MainActivityLast.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment1(), "首页");
        adapter.addFragment(new Fragment2(), "账户充值");
        adapter.addFragment(new Fragment3(), "使用教程");
        viewPager.setAdapter(adapter);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> titleList = new ArrayList<>();

        private Fragment1 fragment1;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        // 获取Fragment1的实例
        public Fragment1 getFragment1() {
            return fragment1;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            titleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }


    @Override
    protected void onDestroy() {
//        stopService();
        super.onDestroy();
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