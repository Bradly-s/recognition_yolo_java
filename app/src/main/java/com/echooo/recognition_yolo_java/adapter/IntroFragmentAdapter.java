package com.echooo.recognition_yolo_java.adapter;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.echooo.recognition_yolo_java.base.BaseIntroFragment;

import java.util.List;

/**
 * Created by Goo on 2016-8-31.
 */
public class IntroFragmentAdapter extends FragmentStatePagerAdapter {

    private List<BaseIntroFragment> mFragments = null;

    public IntroFragmentAdapter(FragmentManager fm, List<BaseIntroFragment> fragments) {
        super(fm);
        mFragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        if (mFragments != null) {
            return mFragments.get(position);
        }
        return null;

    }

    @Override
    public int getCount() {
        if (mFragments != null) {
            return mFragments.size();
        }
        return 0;

    }
}
