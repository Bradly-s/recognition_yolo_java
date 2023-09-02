package com.echooo.recognition_yolo_java.adapter;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.echooo.recognition_yolo_java.R;
import com.echooo.recognition_yolo_java.entity.PetInfo;
import com.echooo.recognition_yolo_java.utils.LogUtils;
import com.echooo.recognition_yolo_java.view.viewholder.RVMainViewHolder;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Adapter - 主界面RecyclerView
 * Created by Goo on 2016-10-19.
 */
public class RVMainAdapter extends RecyclerView.Adapter<RVMainViewHolder> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<PetInfo> mData;

    public RVMainAdapter(Context context, ArrayList<PetInfo> data) {
        LogUtils.logWithMethodInfo("测试是否运行");
        LogUtils.logWithMethodInfo("data.get(0).getPicId():" + data.get(0).getPicId());
        LogUtils.logWithMethodInfo("data.get(0).getPetId():" + data.get(0).getPetId());
        LogUtils.logWithMethodInfo("data.get(0).getName():" + data.get(0).getName());
        LogUtils.logWithMethodInfo("data.get(0).getDescription():" + data.get(0).getDescription());
        LogUtils.logWithMethodInfo("data.get(0).getClass():" + data.get(0).getClass());
        LogUtils.logWithMethodInfo("context:" + context + ";data.get(0):" + data.get(0) + ";LayoutInflater.from(context)" + LayoutInflater.from(context));
        mContext = context;
//        mData = data;
        // 检查数据源是否为空
        if (data != null) {
            mData = data;
        } else {
            // 如果数据源为空，可以创建一个空的数据源或者进行适当的错误处理
            mData = new ArrayList<>();
            LogUtils.logWithMethodInfo("Data source is null");
        }
        mLayoutInflater = LayoutInflater.from(context);
        LogUtils.logWithMethodInfo("Data size: " + data.size());

    }

//    @NonNull
    @Override
    public RVMainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtils.logWithMethodInfo("运行了");
//        View rootView = mLayoutInflater.inflate(R.layout.layout_rv_item_main, parent, false);
        View rootView = mLayoutInflater.inflate(R.layout.layout_rv_item_main, parent, false);
//        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_item_main, parent, false);
        RVMainViewHolder holder = new RVMainViewHolder(rootView);
        rootView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVMainViewHolder holder, int position) {
        PetInfo tempInfo = mData.get(position);
        LogUtils.logWithMethodInfo("Item at position " + position + ";tempInfo.getName(): " + tempInfo.getName());
            LogUtils.logWithMethodInfo("holder instanceof RVMainViewHolder");
            RVMainViewHolder mainHolder = (RVMainViewHolder) holder;
            mainHolder.tvTitle.setText(tempInfo.getName());
            mainHolder.tvDescription.setText(tempInfo.getDescription());
            mainHolder.swSelect.setChecked(tempInfo.isSelected());
            mainHolder.swSelect.setTag(tempInfo.getPicId());
            mainHolder.swSelect.setOnCheckedChangeListener(this);
            mainHolder.ivItemPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mainHolder.ivItemPic.setBackgroundResource(tempInfo.getPicId());
            LogUtils.logWithMethodInfo("tempInfo.getName():" + tempInfo.getName());

            holder.itemView.setTag(mData.get(position));
    }


    @Override
    public int getItemCount() {
        LogUtils.logWithMethodInfo("Data size: " + mData.size());
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (PetInfo) v.getTag());
        }
    }

    private OnRvItemClickListener mOnItemClickListener = null;
    private OnPetSelectedListener mSelectedListener = null;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mSelectedListener != null) {
            mSelectedListener.onPetSelected(buttonView, (Integer) buttonView.getTag());
        }
    }

    public static interface OnRvItemClickListener {
        void onItemClick(View view, PetInfo petInfo);
    }

    public static interface OnPetSelectedListener {
        void onPetSelected(CompoundButton buttonView, int petId);
    }

    public void setOnPetSelectedListener(OnPetSelectedListener listener) {
        mSelectedListener = listener;
    }

    public void setOnRvItemClickListener(OnRvItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void swapData(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
}
