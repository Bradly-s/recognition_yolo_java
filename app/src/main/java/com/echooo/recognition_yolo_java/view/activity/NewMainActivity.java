package com.echooo.recognition_yolo_java.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.echooo.recognition_yolo_java.R;
import com.echooo.recognition_yolo_java.adapter.RVMainAdapter;
import com.echooo.recognition_yolo_java.base.BaseActivity;
import com.echooo.recognition_yolo_java.listener.HidingScrollListener;
import com.echooo.recognition_yolo_java.presenter.MainPresenter;
import com.echooo.recognition_yolo_java.service.FloatingPetService;
import com.echooo.recognition_yolo_java.service.FloatingWindowService;
import com.echooo.recognition_yolo_java.utils.DimenUtils;
import com.echooo.recognition_yolo_java.utils.FloatingRefreshTask;
import com.echooo.recognition_yolo_java.utils.LogUtils;
import com.echooo.recognition_yolo_java.view.fragment.EmptyFragment;
import com.echooo.recognition_yolo_java.view.fragment.Fragment1;
import com.echooo.recognition_yolo_java.view.fragment.Fragment2;
import com.echooo.recognition_yolo_java.view.fragment.Fragment3;
import com.echooo.recognition_yolo_java.view.vinterface.MainVInterface;
import com.echooo.recognition_yolo_java.view.widget.FloatingPetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

//public class NewMainActivity extends AppCompatActivity {
//
//    private ViewPager viewPager;
//    private TabLayout tabLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_main);
//
//        viewPager = findViewById(R.id.view_pager);
//        tabLayout = findViewById(R.id.tab_layout);
//
//        setupViewPager(viewPager);
//        // 将TabLayout与ViewPager关联起来
//        tabLayout.setupWithViewPager(viewPager);
//
//        // 设置Tab选中监听器
////        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
////            @Override
////            public void onTabSelected(TabLayout.Tab tab) {
////                int position = tab.getPosition();
////                if (position == 0) {
////                    // 点击Tab1时启动MainActivity
////                    Intent intent = new Intent(NewMainActivity.this, MainActivity.class);
////                    startActivity(intent);
////                }
////            }
////
////            @Override
////            public void onTabUnselected(TabLayout.Tab tab) {
////                // Do nothing
////            }
////
////            @Override
////            public void onTabReselected(TabLayout.Tab tab) {
////                // Do nothing
////            }
////        });
//    }
//
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new Fragment1(), "Tab 1");
//        adapter.addFragment(new Fragment2(), "Tab 2");
//        adapter.addFragment(new Fragment3(), "Tab 3");
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0); // 默认选中Tab1
//    }
//
//    private static class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> fragmentList = new ArrayList<>();
//        private final List<String> titleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return fragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return fragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            fragmentList.add(fragment);
//            titleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titleList.get(position);
//        }
//    }
//
//
//}



public class NewMainActivity extends BaseActivity<MainVInterface, MainPresenter> implements MainVInterface, View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;


    private static final int IMAGE_PICK_REQUEST_CODE = 101;

    private FloatingPetView floatingPetView;
    private RecyclerView mRvMain;
    private View rootView;
    private FloatingActionButton mFABSetting;
    private SwitchCompat yolo_test;

    private static final int REQUEST_IMAGE_PICK = 101; // Arbitrary request code

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int setContentViewById() {
        return R.layout.activity_new_main;
    }

    @Override
    protected void initAttributes() {

    }

    @Override
    protected void initView() {

        findAllViewById();

        initRv();

        // 创建 FloatingPetView 实例并设置 MainActivity
        floatingPetView = new FloatingPetView(NewMainActivity.this, this.getApplication()); // 这里的 this 是指当前的 MainActivity
//        floatingPetView = new FloatingPetView(MainActivity.this, null); // 这里的 this 是指当前的 MainActivity
//        floatingPetView.setMainActivity(this); // 将 MainActivity 实例传递给 FloatingPetView

        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 主界面、设置*/
    @Override
    protected void findAllViewById() {
//        mRvMain = $(R.id.rv_main);
//        mRvMain = findViewById(R.id.rv_main);
        rootView = getLayoutInflater().inflate(R.layout.activity_main, null);
        mRvMain = rootView.findViewById(R.id.rv_main);
    }

    /**
     * 加载R
     */
    private void initRv() {
        LogUtils.logWithMethodInfo("context=>this:" + this);

        //        todo: [ 报错bug ] RecyclerView ,  No adapter attached; skipping layout
        RVMainAdapter rvAdapter = mPresenter.getRVAdapter(this);
        if (rvAdapter != null) {
            LogUtils.logWithMethodInfo("rvAdapter not null");
            mRvMain.setLayoutManager(new LinearLayoutManager(this));
            mRvMain.setAdapter(rvAdapter);


            ItemTouchHelper itemHelper = mPresenter.getItemTouchHelper(rvAdapter);
            itemHelper.attachToRecyclerView(mRvMain);
        } else {
            // 在Adapter为空的情况下进行适当的错误处理或日志记录
            LogUtils.logWithMethodInfo("RecyclerView  E  No adapter attached; skipping layout=rvAdapter is null" );
        }

    }


    private void setupViewPager(ViewPager viewPager) {
//        MainActivity.ViewPagerAdapter adapter = new MainActivity.ViewPagerAdapter(getSupportFragmentManager());
        NewMainActivity.ViewPagerAdapter adapter = new NewMainActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment1(), "首页");
        adapter.addFragment(new Fragment2(), "账户充值");
        adapter.addFragment(new Fragment3(), "使用教程");
        viewPager.setAdapter(adapter);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> titleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

//        @Override
//        public Fragment getItem(int position) {
//            return fragmentList.get(position);
//        }
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            // 返回一个空的 Fragment，因为您希望 Tab1 对应的是 activity_main.xml 的内容
//            return new EmptyFragment(); // 您需要创建一个名为 EmptyFragment 的空 Fragment 类
            return new Fragment1();
        } else if (position == 1) {
            return new Fragment2();
        } else {
            return new Fragment3();
        }
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
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.rv_main) {
            System.out.println("====R.id.rv_main=======");
            LogUtils.e("====R.id.rv_main=======");

            // 处理 RecyclerView 的点击事件
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
        LogUtils.logWithMethodInfo();

//        原先代码
        //启动悬浮pet
        Intent intent = new Intent(NewMainActivity.this, FloatingPetService.class);
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
