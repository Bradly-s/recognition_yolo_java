package com.echooo.recognition_yolo_java.view.activity;

import android.content.Intent;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.MaterialEditText;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.echooo.recognition_yolo_java.R;
import com.echooo.recognition_yolo_java.adapter.IntroFragmentAdapter;
import com.echooo.recognition_yolo_java.base.BaseActivity;
import com.echooo.recognition_yolo_java.listener.IntroPageChangedListener;
import com.echooo.recognition_yolo_java.presenter.IntroPresenter;
import com.echooo.recognition_yolo_java.utils.AppConstants;
import com.echooo.recognition_yolo_java.utils.DialogUtils;
import com.echooo.recognition_yolo_java.utils.LogUtils;
import com.echooo.recognition_yolo_java.utils.ToastUtil;
import com.echooo.recognition_yolo_java.view.vinterface.IntroVInterface;
import com.echooo.recognition_yolo_java.view.widget.CirclePageIndicator;

/**
 * Created by Goo on 2016-8-28.
 * 介绍界面（界面设计暂定）
 */
public class IntroActivity extends BaseActivity<IntroVInterface, IntroPresenter> implements IntroVInterface, View.OnClickListener {

    private ViewPager mVpIntro;
    private CirclePageIndicator mIndicator;
    private IntroFragmentAdapter mPagerAdapter;

    private TextView mTvIntroLogin, mTvIntroRegister;
//    private TextView mTvLogin, mTvSocialLogin, mTvRegister, mTvPhoneLogin;
    private TextView mTvLogin, mTvRegister, mTvPhoneLogin;
    private TextSwitcher mTSwitcher;

    private EditText mEtAccount, mEtPsw, mEtRAccount, mEtREmail, mEtRPsw, mEtRPswAgain, mEtRPhone;

//    private EditText mEtAccount, mEtPsw, mEtRAccount, mEtRPhone, mEtRPsw, mEtRPswAgain;

    private AlertDialog mLoginDialog, mRegisterDialog = null;

    private MaterialEditText viewId_phone, viewId_captcha, viewId__psw, viewId_account;


    @Override
    protected IntroPresenter createPresenter() {
        return new IntroPresenter(this);
    }


    @Override
    protected int setContentViewById() {
        return R.layout.activity_intro;
    }

    @Override
    protected void initAttributes() {

    }

    @Override
    protected void initView() {
        findAllViewById();
        setAllClickListener();
        initVp();
        initIndicator();
    }

    private void initVp() {
        mPagerAdapter = mPresenter.getPagerAdapter(getSupportFragmentManager());
        mVpIntro.setOffscreenPageLimit(0);
        mVpIntro.setAdapter(mPagerAdapter);
        mVpIntro.setPageTransformer(true, mPresenter.getTransformer());
    }

    private void initIndicator() {
        mIndicator.setViewPager(mVpIntro);
        mIndicator.setPageColor(getResources().getColor(R.color.standardWhite));
        mIndicator.setFillColor(getResources().getColor(R.color.colorAccent));
        mIndicator.setOnPageChangeListener(new IntroPageChangedListener(mVpIntro, mTSwitcher, getWindowManager().getDefaultDisplay().getWidth(), mPagerAdapter.getCount(), getResources()));
    }

    private void setAllClickListener() {
        mTvIntroLogin.setOnClickListener(this);
        mTvIntroRegister.setOnClickListener(this);
    }

    @Override
    protected void findAllViewById() {
        mVpIntro = $(R.id.vp_intro);
        mIndicator = $(R.id.cpi);
        mTvIntroLogin = $(R.id.tv_intro_login);
        mTvIntroRegister = $(R.id.tv_intro_register);
        mTSwitcher = $(R.id.ts_intro);
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_intro_login:
//                showLoginDialog();
//                break;
//            case R.id.tv_intro_register:
//                showRegisterDialog();
//                break;
//            case R.id.tv_login:
//                dismissDialog();
//                mPresenter.login(mEtAccount.getText().toString(), mEtPsw.getText().toString());
//                break;
//            case R.id.tv_social_login:
//                break;
//            case R.id.tv_register:
//                mPresenter.register(mEtRAccount.getText().toString(), mEtREmail.getText().toString(), mEtRPsw.getText().toString(), mEtRPswAgain.getText().toString());
//                break;
//        }
//    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_intro_login) {
            showLoginDialog();
        } else if (viewId == R.id.tv_intro_register) {
            showRegisterDialog();
        } else if (viewId == R.id.tv_login) {
            dismissDialog();
            mPresenter.login(mEtAccount.getText().toString(), mEtPsw.getText().toString());
//        } else if (viewId == R.id.tv_social_login) {
//            // 处理 tv_social_login 的点击事件
        } else if (viewId == R.id.tv_phone_login) {
            // 处理 tv_phone_login 的点击事件
//          todo:  手机号登录的时候，手机号、验证码框显示。精灵名、密码隐藏
            LogUtils.e("R.id.tv_phone_login");

            viewId__psw = findViewById(R.id.et_account);
            viewId_account = findViewById(R.id.et_psw);
            viewId__psw.setVisibility(View.VISIBLE);
            viewId__psw.setVisibility(View.VISIBLE);

            viewId_phone = findViewById(R.id.et_phone);
            viewId_captcha = findViewById(R.id.et_captcha);
            // 设置 MaterialEditText 为可见
            viewId_phone.setVisibility(View.VISIBLE);
            viewId_captcha.setVisibility(View.VISIBLE);
            showLoginDialog();




        } else if (viewId == R.id.tv_register) {
            mPresenter.register(mEtRAccount.getText().toString(), mEtRPhone.getText().toString(), mEtRPsw.getText().toString(), mEtRPswAgain.getText().toString());
        }
    }



    private void showLoginDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View loginDialog = inflater.inflate(R.layout.layout_dialog_login, (ViewGroup) findViewById(R.id.ll_dialog));

        mEtAccount = (MaterialEditText) loginDialog.findViewById(R.id.et_account);
        mEtPsw = (MaterialEditText) loginDialog.findViewById(R.id.et_psw);
        mTvLogin = (TextView) loginDialog.findViewById(R.id.tv_login);
//        mTvSocialLogin = (TextView) loginDialog.findViewById(R.id.tv_social_login);
        mTvPhoneLogin = (TextView) loginDialog.findViewById(R.id.tv_phone_login);
        mTvLogin.setOnClickListener(this);

//        mTvSocialLogin.setOnClickListener(this);
        mTvPhoneLogin.setOnClickListener(this);

        mLoginDialog = DialogUtils.showCoustomDialog(this, loginDialog, "登录");

    }

    private void showRegisterDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View registerDialog = inflater.inflate(R.layout.layout_dialog_register, (ViewGroup) findViewById(R.id.ll_dialog));

        mTvRegister = (TextView) registerDialog.findViewById(R.id.tv_register);
        mEtRAccount = (EditText) registerDialog.findViewById(R.id.et_register_account);
//        mEtREmail = (EditText) registerDialog.findViewById(R.id.et_register_email);
        mEtRPhone = (EditText) registerDialog.findViewById(R.id.et_phone_number);
        mEtRPsw = (EditText) registerDialog.findViewById(R.id.et_register_psw);
        mEtRPswAgain = (EditText) registerDialog.findViewById(R.id.et_register_psw_again);
        mTvRegister.setOnClickListener(this);

        mRegisterDialog = DialogUtils.showCoustomDialog(this, registerDialog, "注册");
    }

    @Override
    public void showProgressDialog() {
        super.showProgressDialog(getString(R.string.tips_net_working));
    }

    @Override
    public void dismissDialog() {
        super.dismissProgressDialog();
    }

    @Override
    public void registerSuccess(String userName) {
        ToastUtil.showToast(this, getString(R.string.tips_register_success));
        EnterMain(userName);

    }

    @Override
    public void loginSuccess(String userName) {
        EnterMain(userName);
    }

    @Override
    public void errorLoginFail() {
        ToastUtil.showToast(this, getString(R.string.tips_error_login));
    }

    @Override
    public void errorEmptyInfo() {
        ToastUtil.showToast(this, getString(R.string.error_empty_info));
    }

    @Override
    public void errorPswNotEqual() {
        ToastUtil.showToast(this, getString(R.string.error_psw_not_equal));
    }

    @Override
    public void errorEmailInvalid() {
        ToastUtil.showToast(this, getString(R.string.error_email_invalid));
    }

    @Override
    public void errorUserNameRepeat() {
        ToastUtil.showToast(this, getString(R.string.error_username_repeat));

    }

    @Override
    public void errorEmailRepeat() {
        ToastUtil.showToast(this, getString(R.string.error_email_repeat));
    }

    @Override
    public void errorNetWork() {
        ToastUtil.showToast(this, getString(R.string.error_network));
    }

    /**
     * 进入主界面
     *
     * @param userName
     */
    private void EnterMain(String userName) {
        AppConstants.USER_NAME = userName;
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivityWithAnim(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mLoginDialog != null) {
            mLoginDialog.dismiss();
        }
        if (mRegisterDialog != null) {
            mRegisterDialog.dismiss();
        }
        super.onDestroy();
    }
}
