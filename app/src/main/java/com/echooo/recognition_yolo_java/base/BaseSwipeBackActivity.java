
package com.echooo.recognition_yolo_java.base;

import android.os.Bundle;
import android.view.View;

import com.echooo.recognition_yolo_java.swipeback.SwipeBackActivityBase;
import com.echooo.recognition_yolo_java.swipeback.SwipeBackActivityHelper;
import com.echooo.recognition_yolo_java.swipeback.SwipeBackLayout;
import com.echooo.recognition_yolo_java.swipeback.Utils;


public abstract class BaseSwipeBackActivity<V, P extends BasePresenter<V>> extends BaseActivity<V, P> implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
