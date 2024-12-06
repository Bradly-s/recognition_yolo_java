package com.echooo.recognition_yolo_java.presenter;

//import android.support.v4.app.FragmentManager;
import androidx.fragment.app.FragmentManager;

//import com.avos.avoscloud.AVException;
//import com.avos.avoscloud.AVUser;
//import com.avos.avoscloud.LogInCallback;
//import com.avos.avoscloud.SignUpCallback;
import com.echooo.recognition_yolo_java.adapter.IntroFragmentAdapter;
import com.echooo.recognition_yolo_java.base.BasePresenter;
import com.echooo.recognition_yolo_java.model.IntroModel;
import com.echooo.recognition_yolo_java.model.minterface.IntroMInterface;
import com.echooo.recognition_yolo_java.utils.AVOSUtils;
import com.echooo.recognition_yolo_java.utils.AppConstants;
import com.echooo.recognition_yolo_java.utils.ParallaxTransformer;
import com.echooo.recognition_yolo_java.view.vinterface.IntroVInterface;

import java.util.regex.Pattern;

/**
 * Created by Goo on 2016-8-31.
 */
public class IntroPresenter extends BasePresenter<IntroVInterface> {

    private IntroMInterface mModel;

    public IntroPresenter(IntroVInterface viewInterface) {
        super(viewInterface);
        mModel = new IntroModel();
    }

    /**
     * 获得 introAdapter
     *
     * @param fm
     * @return
     */
    public IntroFragmentAdapter getPagerAdapter(FragmentManager fm) {
        return new IntroFragmentAdapter(fm, mModel.getIntroFragemnts());
    }

    public ParallaxTransformer getTransformer() {
        return new ParallaxTransformer(AppConstants.PARALLAX_COEFFICIENT, AppConstants.DISTANCE_COEFFICIENT, mModel.getLayoutViewIdsMap());
    }

    /**flag： true：手机号登录
     *        false：小精灵名称登录
    * */
    public void login(final String userInfo1, String userInfo2, Boolean flag) {
        if (userInfo1.isEmpty() || userInfo2.isEmpty()) {
            view.errorEmptyInfo();
        } else {
            view.showProgressDialog();
//            todo: 根据flag判断用户是手机号登录or用户名登录，若是手机号登录，则查找用户该手机号对应的用户名
            if (userInfo1 != null) {
                view.loginSuccess(userInfo1);
            } else {
                view.errorLoginFail();
            }
//            AVUser.logInInBackground(userName, psw, new LogInCallback<AVUser>() {
//                @Override
//                public void done(AVUser avUser, AVException e) {
//                    view.dismissDialog();
//                    if (avUser != null) {
//                        view.loginSuccess(avUser.getUsername());
//                    } else {
//                        view.errorLoginFail();
//                    }
//                }
//            });
        }

    }

//    public void register(final String userName, String email, String psw, String pswAgain) {
//        if (userName.isEmpty() || email.isEmpty() || psw.isEmpty() || pswAgain.isEmpty()) {
//            view.errorEmptyInfo();
//        } else if (!psw.equals(pswAgain)) {
//            view.errorPswNotEqual();
//        } else if (!Pattern.compile(AppConstants.REGEX_EMAIL).matcher(email).find()) {
//            view.errorEmailInvalid();
//        } else {
//            view.showProgressDialog();
//
//            view.registerSuccess(userName);
//
////            AVOSUtils.signUp(userName, psw, email, new SignUpCallback() {
////                @Override
////                public void done(AVException e) {
////                    view.dismissDialog();
////                    if (e == null) {
////                        view.registerSuccess(userName);
////                    } else {
////                        switch (e.getCode()) {
////                            case 202:
////                                view.errorUserNameRepeat();
////                                break;
////                            case 203:
////                                view.errorEmailRepeat();
////                                break;
////                            default:
////                                view.errorNetWork();
////                                break;
////                        }
////                    }
////                }
////            });
//        }
//
//    }
public void register(final String userName, String phone, String psw, String pswAgain) {
    if (userName.isEmpty() || phone.isEmpty() || psw.isEmpty() || pswAgain.isEmpty()) {
        view.errorEmptyInfo();
    } else if (!psw.equals(pswAgain)) {
        view.errorPswNotEqual();
    } else if (!Pattern.compile(AppConstants.REGEX_PHONE).matcher(phone).find()) {
        view.errorPhoneInvalid();
    } else {
        view.showProgressDialog();

        view.registerSuccess(userName);

//            AVOSUtils.signUp(userName, psw, email, new SignUpCallback() {
//                @Override
//                public void done(AVException e) {
//                    view.dismissDialog();
//                    if (e == null) {
//                        view.registerSuccess(userName);
//                    } else {
//                        switch (e.getCode()) {
//                            case 202:
//                                view.errorUserNameRepeat();
//                                break;
//                            case 203:
//                                view.errorEmailRepeat();
//                                break;
//                            default:
//                                view.errorNetWork();
//                                break;
//                        }
//                    }
//                }
//            });
    }

}

}
