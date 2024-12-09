package com.echooo.recognition_yolo_java.presenter;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.View;
import android.widget.CompoundButton;

import com.echooo.recognition_yolo_java.adapter.RVMainAdapter;
import com.echooo.recognition_yolo_java.base.BasePresenter;
import com.echooo.recognition_yolo_java.entity.PetInfo;
import com.echooo.recognition_yolo_java.model.MainModel;
import com.echooo.recognition_yolo_java.model.minterface.MainMInterface;
import com.echooo.recognition_yolo_java.utils.AppConstants;
import com.echooo.recognition_yolo_java.utils.LogUtils;
import com.echooo.recognition_yolo_java.utils.ToastUtil;
import com.echooo.recognition_yolo_java.view.vinterface.MainVInterface;

import java.io.File;
import java.net.URL;

/**
 * Created by Goo on 2016-9-18.
 */
public class MainPresenter extends BasePresenter<MainVInterface> {

    private MainMInterface mModel;

    public MainPresenter(MainVInterface viewInterface) {
        super(viewInterface);
        LogUtils.logWithMethodInfo("这是问题？？----------------------------------------------------------");
        mModel = new MainModel();
        LogUtils.logWithMethodInfo("这是问题？？+++++++++++++++++++++++++++++++++++++++++++++++");
    }

    /**
     * 获取已配置的RVAdapter
     *
     * @param context
     * @return
     */
    public RVMainAdapter getRVAdapter(final Context context) {
        RVMainAdapter adapter = new RVMainAdapter(context, mModel.getPetData());
        LogUtils.logWithMethodInfo("adapter:" + adapter);
        LogUtils.logWithMethodInfo("mModel.getPetData().get(0).getName():" + mModel.getPetData().get(0).getName());
        adapter.setOnRvItemClickListener(new RVMainAdapter.OnRvItemClickListener() {
            @Override
            public void onItemClick(View view, PetInfo petInfo) {
                ToastUtil.showToast(context, "PetInfo:" + petInfo.getName() + "\n" + petInfo.getDescription());
            }
        });
        adapter.setOnPetSelectedListener(new RVMainAdapter.OnPetSelectedListener() {

            @Override
            public void onPetSelected(CompoundButton buttonView, int petId) {
                LogUtils.logWithMethodInfo();

                boolean isCheck = buttonView.isChecked();
                if (isCheck) {
                    LogUtils.logWithMethodInfo("isCheck");
                    view.launchDesktopPet();
                }
                switch (petId) {
                    case AppConstants.PET_BIRD:
                        break;
                    case AppConstants.PET_COW:
                        break;
                    case AppConstants.PET_PIG:
                        break;
                    case AppConstants.PET_OWL:
                        break;
                    default:
                        break;
                }
            }
        });
        return adapter;
    }


    public ItemTouchHelper getItemTouchHelper(final RVMainAdapter adapter) {
        LogUtils.logWithMethodInfo();
        return new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP |
                        ItemTouchHelper.DOWN, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                adapter.swapData(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

        });
    }


}
