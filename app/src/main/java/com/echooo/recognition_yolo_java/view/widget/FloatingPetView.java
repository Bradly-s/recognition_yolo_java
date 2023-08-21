package com.echooo.recognition_yolo_java.view.widget;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import static com.echooo.recognition_yolo_java.yoloobjdetect.PrePostProcessor.nonMaxSuppression;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.echooo.recognition_yolo_java.R;
import com.echooo.recognition_yolo_java.pythonProject.PythonExecutor;
import com.echooo.recognition_yolo_java.utils.FloatingUtils;
import com.echooo.recognition_yolo_java.utils.LogUtils;
import com.echooo.recognition_yolo_java.view.activity.MainActivity;
import com.echooo.recognition_yolo_java.yoloobjdetect.MainActivity2;
import com.echooo.recognition_yolo_java.yoloobjdetect.PrePostProcessor;
import com.echooo.recognition_yolo_java.yoloobjdetect.Result;

//import org.python.core.PyObject;
import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Created by Goo on 2016-9-18.
 */
public class FloatingPetView extends LinearLayout implements Runnable{
    /**
     * 窗体宽高
     */
    public static int viewWidth, viewHeight;

    /**
     * 系统状态栏高度
     */
    private static int statusBarHeight;

    /**
     * 屏幕宽高
     */
    private int screenHeight, screenWidth;

    /**
     * 窗体管理
     */
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

    /**
     * 悬浮窗Iv
     */
    private ImageView mIvPet;

    /**
     * 位置参数
     */
    private float xInView, yInView, xDownInScreen, yDownInScreen, xInScreen, yInScreen;

    /**
     * 是否按住状态
     */
    private boolean isPressed = false;

    /**
     * 是否需要隐藏
     */
    private boolean isNeedHide = false;

    /**
     * 动画 - 按住状态
     */
    private ValueAnimator mTouchedAnim;

    /**
     * 动画 - 移动状态
     */
    private ValueAnimator mMovingAnim;

    /**
     * 收缩界面*/
    private boolean isExpanded = false;
    private View expandableView;

    private static final int REQUEST_IMAGE_PICK = 101; // Arbitrary request code
    private MainActivity mainActivity;
    private Context mContext; // 添加这个成员变量

    private Module mModule = null;
    private float mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY;
    private ProgressBar mProgressBar;

    public FloatingPetView(Context context) {
        super(context);
        mContext = context; // 初始化 mContext 成员变量
        initView(context);
        LogUtils.logWithMethodInfo();
//        setMainActivity(mainActivity);
    }

    // 设置 Context 的方法
    public void setContext(Context context) {
        mContext = context;
    }


    public void setMainActivity(MainActivity mainActivity) {
        LogUtils.logWithMethodInfo("mainActivity:" + mainActivity);
        this.mainActivity = mainActivity;
    }

    /**
     * 初始化视图
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_widget_pet, this);
        findAllViewById();
        initViewParams(context);
        defaultPetStatus();
        initExpandableView(context);
    }


    /**
     *          初始化可收缩布局
     *          todo:  调整为结果显示界面
     *          */
    public void initExpandableView(Context context){
//        LayoutInflater inflater = LayoutInflater.from(context);
//        expandableView = inflater.inflate(R.layout.layout_expandable_view, this, false);
//
//        addView(expandableView);
//        expandableView.setVisibility(View.GONE); // 初始状态不可见

        expandableView = (TextView) findViewById(R.id.contract);
        expandableView.setVisibility(View.GONE); // 初始状态不可见
    }

    private void findAllViewById() {
        mIvPet = (ImageView) findViewById(R.id.iv_pet);

    }

    /**
     * 初始化窗体宽高参数
     */
    private void initViewParams(Context context) {
        View view = findViewById(R.id.ll_pet);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        statusBarHeight = FloatingUtils.getStatusBarHeight(this);
        if (statusBarHeight != -1) {
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels - statusBarHeight;
        } else {
            LogUtils.e("statusBarHeight = -1");
        }
    }

    /**
     * 设置宠物样式
     *
     * @param resid
     */
    private void setPetImg(int resid) {
        mIvPet.setBackgroundResource(resid);
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 处理控件触摸事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInScreen = x;
                yInScreen = y;
                xInView = event.getX();
                yInView = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算悬浮窗的偏移量
                int offsetX = (int) (x - xInScreen);
                int offsetY = (int) (y - yInScreen);

                // 更新悬浮窗的位置
                mParams.x += offsetX;
                mParams.y += offsetY;

//                // 限制悬浮窗只能在屏幕最右侧拖拽
//                int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
                int floatingWindowWidth = getWidth();

                // 右侧边界限制
                if (mParams.x < screenWidth - floatingWindowWidth) {
                    mParams.x = screenWidth - floatingWindowWidth;
                }
                // 左侧边界限制
                if (mParams.x > screenWidth) {
                    mParams.x = screenWidth;
                }
                // 上侧边界限制
                if (mParams.y < 0) {
                    mParams.y = 0;
                }
                // 下侧边界限制
                int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
                int floatingWindowHeight = getHeight();
                if (mParams.y > screenHeight - floatingWindowHeight) {
                    mParams.y = screenHeight - floatingWindowHeight;
                }

                // 更新悬浮窗的位置
                mWindowManager.updateViewLayout(FloatingPetView.this, mParams);

                // 更新触摸起始点
                xInScreen = x;
                yInScreen = y;
                break;
            case MotionEvent.ACTION_UP:
//                break;
                isPressed = false;

                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    //触摸坐标不变，即点击事件
                    try {
                        LogUtils.logWithMethodInfo("触摸坐标不变，即点击事件");
                        updatePetState();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (xInScreen < screenWidth / 8) {  //当在这个位置放手时宠物贴边隐藏
                    hideLeft();
                } else if (xInScreen > screenWidth * 7 / 8) {
                    hideRight();
                    break;
                }
                else {
//                    更新状态
                    try {
                        LogUtils.logWithMethodInfo("更新状态");
                        updatePetState();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        }
        return true;
    }




    /**
     * 右边贴边
     */
    private void hideRight() {
        setPetImg(R.drawable.ic_pet_hide_right);
    }

    /**
     * 左边贴边
     */
    private void hideLeft() {
        setPetImg(R.drawable.ic_pet_hide_left);
    }

    /**
     * 更新宠物状态
     */
    private void updatePetState() throws IOException {
        if (isPressed) {
            //按住状态
            touchPetStatus();
        } else if (isNeedHide) {
            //没有按住，需要贴边，贴边即可
//            新加上的（自动贴边）
            hideRight();
        } else {
            //没有按住，也不需要贴边，默认状态即可
//            todo: 点击的时候，选择本地图片，然后调用yolo算法，将结果显示在可收缩的界面上，并展开该收缩界面
            LogUtils.logWithMethodInfo("上传图片，处理算法");
            processYolo();


        }
    }




    /**
    *   原先：选择本地图片，然后调用yolo算法，将结果显示在可收缩的界面上，并展开该收缩界面
     *   改为：调用算法识别项目中的图片，将结果显示在可收缩的界面上，并展开该收缩界面
    * */
    private String[] mTestImages = {"test1.png"};
    private Bitmap mBitmap = null;

    private List<String> yoloClasses = null;


    // 定义一个回调接口
    public interface OnFinishListener {
        void onFinishRequested();
    }

    private OnFinishListener onFinishListener;

    public void setOnFinishListener(OnFinishListener listener) {
        onFinishListener = listener;
    }

    // 在需要结束的地方调用该方法
    private void requestFinish() {
        if (onFinishListener != null) {
            onFinishListener.onFinishRequested();
        }
    }
    public void processYolo() throws IOException {
        LogUtils.logWithMethodInfo();

        // 使用 mContext 从 assets 中加载测试图片
        InputStream inputStream = mContext.getAssets().open("test1.png");
//        Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
        mBitmap = BitmapFactory.decodeStream(inputStream);
        LogUtils.logWithMethodInfo("inputStream:" + inputStream);
        LogUtils.logWithMethodInfo("mBitmap:" + mBitmap);

        try {
            mModule = LiteModuleLoader.load(MainActivity.assetFilePath(mContext.getApplicationContext(), "yolov5s.torchscript.ptl"));
            LogUtils.logWithMethodInfo("mModule:" + mModule);
            BufferedReader br = new BufferedReader(new InputStreamReader(mContext.getAssets().open("classes.txt")));
            LogUtils.logWithMethodInfo("br:" + br);
            String line;
            List<String> classes = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                classes.add(line);
            }
            yoloClasses = classes;
            LogUtils.logWithMethodInfo("classes:" + classes);
            PrePostProcessor.mClasses = new String[classes.size()];
            classes.toArray(PrePostProcessor.mClasses);
            LogUtils.logWithMethodInfo("classes:" + classes);
            run();
        } catch (IOException e) {
            Log.e("Object Detection", "Error reading assets", e);
            requestFinish();
        }
    }

    @Override
    public void run() {
        LogUtils.logWithMethodInfo();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(mBitmap, PrePostProcessor.mInputWidth, PrePostProcessor.mInputHeight, true);

        final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resizedBitmap, PrePostProcessor.NO_MEAN_RGB, PrePostProcessor.NO_STD_RGB);
        IValue[] outputTuple = mModule.forward(IValue.from(inputTensor)).toTuple();
        final Tensor outputTensor = outputTuple[0].toTensor();
        final float[] outputs = outputTensor.getDataAsFloatArray();
        LogUtils.logWithMethodInfo("mImgScaleX:" + mImgScaleX);
        LogUtils.logWithMethodInfo("mImgScaleY:" + mImgScaleY);
        LogUtils.logWithMethodInfo("mIvScaleX:" + mIvScaleX);
        LogUtils.logWithMethodInfo("mIvScaleY:" + mIvScaleY);
        LogUtils.logWithMethodInfo("mStartX:" + mStartX);
        LogUtils.logWithMethodInfo("mStartY:" + mStartY);
        LogUtils.logWithMethodInfo("————————————————————————————————————————————————————————————————————");

//        processAngle(mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY);
        mImgScaleX = (float)mBitmap.getWidth() / PrePostProcessor.mInputWidth;
        mImgScaleY = (float)mBitmap.getHeight() / PrePostProcessor.mInputHeight;
        float mImageViewWidth = 1080.0F;
        float mImageViewHeight = 1080.0F;

        mIvScaleX = (mBitmap.getWidth() > mBitmap.getHeight() ? mImageViewWidth / mBitmap.getWidth() : mImageViewHeight / mBitmap.getHeight());
        mIvScaleY  = (mBitmap.getHeight() > mBitmap.getWidth() ? mImageViewHeight / mBitmap.getHeight() : mImageViewWidth / mBitmap.getWidth());

        mStartX = (mImageViewWidth - mIvScaleX * mBitmap.getWidth())/2;
        mStartY = (mImageViewHeight -  mIvScaleY * mBitmap.getHeight())/2;
        LogUtils.logWithMethodInfo("mImgScaleX:" + mImgScaleX);
        LogUtils.logWithMethodInfo("mImgScaleY:" + mImgScaleY);
        LogUtils.logWithMethodInfo("====================================================================");

        //
        final ArrayList<Result> results =  PrePostProcessor.outputsToNMSPredictions(outputs, mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY);
//        final ArrayList<Result> results =  PrePostProcessor.outputsToNMSPredictions2(outputs, mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY);

        LogUtils.logWithMethodInfo("inputTensor:" + inputTensor);
        LogUtils.logWithMethodInfo("outputTuple:" + outputTuple);
        LogUtils.logWithMethodInfo("outputTensor:" + outputTensor);
        LogUtils.logWithMethodInfo("outputs:" + outputs);
        LogUtils.logWithMethodInfo("results:" + results);
        LogUtils.logWithMethodInfo("yoloClasses.size():" + yoloClasses.size());

//        for (String className : yoloClasses) {
//            LogUtils.logWithMethodInfo("className:" + className);
//        }

        StringBuilder finalResult = new StringBuilder();
        for (Result result : results) {
//            String classIndex = String.valueOf(result.classIndex); // 这里假设类名是一个整数
            int classIndex = result.classIndex;
            String confidence = String.valueOf(result.score);
            String boundingBox = result.rect.toString();

            LogUtils.logWithMethodInfo("Result中" + "className: " + yoloClasses.get(classIndex) + ", Confidence: " + confidence + ", BBox: " + boundingBox);
            finalResult.append("className: ").append(yoloClasses.get(classIndex)).append(", Confidence: ").append(confidence).append(", BBox: ").append(boundingBox);
        }

//        将结果存入字典，写入收缩界面
        TextView resultTextView = expandableView.findViewById(R.id.contract);
        // 在这里设置处理结果文本
        resultTextView.setText(finalResult);
        processExpandableView();
        processPython();



//        runOnUiThread(() -> {
//            mButtonDetect.setEnabled(true);
//            mButtonDetect.setText(getString(R.string.detect));
//            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
//            mResultView.setResults(results);
//            mResultView.invalidate();
//            mResultView.setVisibility(View.VISIBLE);
//        });
    }

    public void processPython() {
        LogUtils.logWithMethodInfo();
        String scriptPath = "helloworld.py";  // 脚本文件名，不包含路径
        try {
            InputStream inputStream = mContext.getAssets().open(scriptPath);

            LogUtils.logWithMethodInfo("inputStream:" + inputStream);
            String re = PythonExecutor.runScript(inputStream);
            LogUtils.logWithMethodInfo(re);

            LogUtils.logWithMethodInfo("runScript——after");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void processAngle( float mImgScaleX,float mImgScaleY,float mIvScaleX,float mIvScaleY,float mStartX,float mStartY){

        mImgScaleX = (float)mBitmap.getWidth() / PrePostProcessor.mInputWidth;
        mImgScaleY = (float)mBitmap.getHeight() / PrePostProcessor.mInputHeight;
        float mImageViewWidth = 1080.0F;
        float mImageViewHeight = 1080.0F;

        mIvScaleX = (mBitmap.getWidth() > mBitmap.getHeight() ? mImageViewWidth / mBitmap.getWidth() : mImageViewHeight / mBitmap.getHeight());
        mIvScaleY  = (mBitmap.getHeight() > mBitmap.getWidth() ? mImageViewHeight / mBitmap.getHeight() : mImageViewWidth / mBitmap.getWidth());

        mStartX = (mImageViewWidth - mIvScaleX * mBitmap.getWidth())/2;
        mStartY = (mImageViewHeight -  mIvScaleY * mBitmap.getHeight())/2;
    }


    public void handleImageSelection(Uri selectedImage) {
        try {
            Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
            // 在这里调用你的 YOLO 算法和相关处理逻辑
            // 例如，你可以将 selectedBitmap 传递给算法并显示结果
            // 更新宠物状态以展示处理结果
            // 例如，你可以将展开界面的 TextView 设置为处理结果文本
            TextView resultTextView = expandableView.findViewById(R.id.contract);
            // 在这里设置处理结果文本
            resultTextView.setText("Processing result: ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 判断是否要展开*/
    public void processExpandableView(){
        LogUtils.logWithMethodInfo();
        if (isExpanded) {
            collapseExpandableView();
        } else {
            expandExpandableView();
        }
    }

    /**
     * 展开界面*/
    private void expandExpandableView() {
        expandableView.setVisibility(View.VISIBLE);
        isExpanded = true;
    }

    /**
     * 收缩界面*/
    private void collapseExpandableView() {
        expandableView.setVisibility(View.GONE);
        isExpanded = false;
    }

    /** 原先的触摸方法：切换样式*/
    private void defaultPetStatus() {
        switch (new Random().nextInt(5) + 3) {
            case 3:
                setPetImg(R.drawable.ic_face_03);
                break;
            case 4:
                setPetImg(R.drawable.ic_face_04);
                break;
            case 5:
                setPetImg(R.drawable.ic_face_05);
                break;
            case 6:
                setPetImg(R.drawable.ic_face_06);
                break;
            case 7:
                setPetImg(R.drawable.ic_face_07);
                break;
        }
    }

    /**
     * 按住（拎起）状态，随机更换表情
     */
    private void touchPetStatus() {
        setPetImg(R.drawable.ic_face_02);
    }

    /**
     * 更新参数 - 移动时刻
     *
     * @param event
     */
    private void refreshParamsForMove(MotionEvent event) {
        xInScreen = event.getRawX();
        yInScreen = event.getRawY() - FloatingUtils.getStatusBarHeight(this);
    }

    /**
     * 更新参数 - 按下时刻
     *
     * @param event
     */
    private void refreshParamsForDown(MotionEvent event) {
        xInView = event.getX();
        yInView = event.getY();
        xDownInScreen = event.getRawX();
        yDownInScreen = event.getRawY() - FloatingUtils.getStatusBarHeight(this);
        refreshParamsForMove(event);
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updatePetPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        mWindowManager.updateViewLayout(this, mParams);
    }

}
