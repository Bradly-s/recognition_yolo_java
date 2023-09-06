package com.echooo.recognition_yolo_java.view.activity;

import static com.echooo.recognition_yolo_java.utils.FloatingPetManager.CODE_WINDOW;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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

    private static final int REQUEST_CODE = 123;
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
        // 设置SwitchCompat的状态更改监听器
        fragment1.setOnSwitchChangeListener(this);

        floatingPetView = new FloatingPetView(MainActivityLast.this, this.getApplication()); // 这里的 this 是指当前的 MainActivity
        floatingPetView.setMainActivity(this); // 将 MainActivity 实例传递给 FloatingPetView


    }

    // 实现OnSwitchChangeListener接口的方法
    @Override
    public void onSwitchChanged(boolean isChecked) {
        if (isChecked) {
            // SwitchCompat被选中时执行的操作
            getPerssion();
//            launchDesktopPet();
        } else {
//            关闭悬浮窗
            closeDesktopPet();
        }
    }

    /**在此处判断是否授予权限，然后再启动悬浮窗
     * */
    public void getPerssion(){
        LogUtils.logWithMethodInfo();
        // 申请悬浮窗权限
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "请打开此应用悬浮窗权限", Toast.LENGTH_SHORT).show();
                LogUtils.logWithMethodInfo("请打开此应用悬浮窗权限");
                //【 提示"请打开此应用悬浮窗权限"后进入桌面，应用变为了后台 】
//                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), REQUEST_CODE);

                //【 提示"请打开此应用悬浮窗权限"后进入桌面，应用变为了后台,再次打开应用即进入权限设置界面 】
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + MainActivityLast.this.getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//为待启动的Activity指定FLAG_ACTIVITY_NEW_TASK标记位。[ 加上会不起作用  ]
                startActivityForResult(intent, REQUEST_CODE);
            }else {
                LogUtils.logWithMethodInfo("已经获得权限，则直接启动悬浮窗");
                launchDesktopPet();
            }
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
//        new FloatingRefreshTask(getPackageManager(), (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE), getApplicationContext(), getApplication()).showFloatingWindow();

    }

    //    关闭悬浮窗
    public void closeDesktopPet() {
        LogUtils.logWithMethodInfo("调用成功啦！！");

//        new FloatingRefreshTask(getPackageManager(), (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE), getApplicationContext(), getApplication()).closeFloatingWindow();

        Intent intent = new Intent(MainActivityLast.this, FloatingPetService.class);
        stopService(intent);
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

        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                // 用户已经授予悬浮窗权限，可以执行需要权限的操作
                LogUtils.logWithMethodInfo("用户已经授予悬浮窗权限");
                launchDesktopPet();
            } else {
                // 用户未授予悬浮窗权限，可以给出提示或采取其他操作
                LogUtils.logWithMethodInfo("用户未授予悬浮窗权限");
                // 用户未授予悬浮窗权限，给出提示
                Toast.makeText(this, "请授予悬浮窗权限以继续使用应用", Toast.LENGTH_SHORT).show();

                // 退出应用
                finishAffinity(); // 关闭当前 Activity 和所有相关的 Activity
//                System.exit(0); // 退出应用的其他进程 【注释该行代码，可以让用户看到上边的提示】
            }
        }

//        switch (requestCode) {
//            // 不给权限就退出
//            case CODE_WINDOW:
//                LogUtils.logWithMethodInfo("CODE_WINDOW:" + CODE_WINDOW);
//                if (resultCode != Activity.RESULT_OK) {
//                    LogUtils.logWithMethodInfo("resultCode != Activity.RESULT_OK;resultCode:" + resultCode + ";Activity.RESULT_OK:" + Activity.RESULT_OK);
//                    System.exit(0);
//                }
//                break;
//            default:
//                LogUtils.logWithMethodInfo("default");
//                Toast.makeText(this, "未知权限回调: " + requestCode, Toast.LENGTH_SHORT).show();
//        }


        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            LogUtils.logWithMethodInfo("requestCode:" + requestCode + ",IMAGE_PICK_REQUEST_CODE:" + IMAGE_PICK_REQUEST_CODE);
            Uri selectedImageUri = data.getData();

            // 将选定的图片数据传递给 FloatingPetView 处理
            floatingPetView.handleImageSelection(selectedImageUri);
        }
    }

}