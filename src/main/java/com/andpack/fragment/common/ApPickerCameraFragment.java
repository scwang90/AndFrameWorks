package com.andpack.fragment.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.annotation.lifecycle.OnDestroy;
import com.andframe.annotation.view.BindViewCreated;
import com.andframe.api.pager.Pager;
import com.andframe.feature.AfIntent;
import com.andpack.application.ApApp;
import com.andpack.fragment.ApFragment;

import java.io.File;

/**
 * 通用相机拍照页面
 * Created by SCWANG on 2017/5/18.
 */
public class ApPickerCameraFragment extends ApFragment {


    private static final int REQUEST_CAMERA = 100;
    // 设置系统相机拍照后的输出路径
    private File mTmpCameraFile = new File(ApApp.getApp().getImageDiskCache().getDirectory(), "temp.jpg");

    //<editor-fold desc="页面传递">
    public interface SelectedListener {
        void onSelected(File item);
    }
    private static SelectedListener selectedListener;

    public static void select(Pager pager, SelectedListener listener) {
        selectedListener = listener;
        pager.startFragment(ApPickerCameraFragment.class);
    }
    //</editor-fold>

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return new View(inflater.getContext());
    }

    @OnDestroy
    public void onDestory() {
        selectedListener = null;
    }

    @BindViewCreated
    public void onCreate() {
        if (selectedListener == null) {
            finish();
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
            if(mTmpCameraFile != null && mTmpCameraFile.exists() && !mTmpCameraFile.delete() && ApApp.get().isDebug()) {
                makeToastShort("图片错误");
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpCameraFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }else{
            makeToastShort("没有系统相机");
        }
    }
    /**
     * 页面跳转数据返回接受和处理
     * @param intent      Intent 的子类 支持对象持久化
     * @param requestcode 请求码
     * @param resultcode  返回码
     */
    @Override
    protected void onActivityResult(final AfIntent intent, int requestcode, int resultcode) {
        if(requestcode == REQUEST_CAMERA){
            if(resultcode == Activity.RESULT_OK) {
                if (mTmpCameraFile != null) {
                    selectedListener.onSelected(mTmpCameraFile);
                }
            } else {
                while (mTmpCameraFile != null && mTmpCameraFile.exists()){
                    boolean success = mTmpCameraFile.delete();
                    if(success){
                        mTmpCameraFile = null;
                    }
                }
            }
            finish();
            finish();
        }
    }

}
