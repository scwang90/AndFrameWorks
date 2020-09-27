package com.andpack.activity;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.andframe.activity.AfItemsActivity;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.adapter.ItemsViewerAdapter;
import com.andframe.api.pager.status.RefreshLayoutManager;
import com.andframe.api.pager.status.StatusLayoutManager;
import com.andframe.api.query.ViewQuery;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfIntent;
import com.andpack.api.ApItemsPager;
import com.andpack.impl.ApItemsHelper;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
@RuntimePermissions
public abstract class ApItemsActivity<T> extends AfItemsActivity<T> implements ApItemsPager<T> {

    protected ApItemsHelper<T> mApHelper;// = new ApItemsHelper<>(this);

    public ApItemsActivity() {
        this.mApHelper = new ApItemsHelper<>(this);
    }

    public ApItemsActivity(ApItemsHelper<T> helper) {
        this.mApHelper = helper;
    }

    @Override
    public void setTheme(@StyleRes int resId) {
        mApHelper.setTheme(resId);
        super.setTheme(resId);
    }

    @Override
    protected void onCreated(Bundle bundle) {
        mApHelper.onCreate();
        super.onCreated(bundle);
    }

    @Override
    protected void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    @CallSuper
    public void onViewCreated()  {
        mApHelper.onViewCreated();
        super.onViewCreated();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null)
            return mApHelper.findViewById(id);
        return v;
    }

    @Override
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        mApHelper.onPostCreate(bundle);
    }

    @Override
    public void finish() {
        if (mApHelper.finish()) {
            return;
        }
        super.finish();
    }

    @NonNull
    @Override
    public ItemsViewerAdapter<T> newAdapter(@NonNull Context context, @Nullable List<T> list) {
        ItemsViewerAdapter<T> adapter = mApHelper.newAdapter(list);
        if (adapter != null) {
            return mAdapter = adapter;
        }
        return super.newAdapter(context, list);
    }

    @NonNull
    @Override
    public RefreshLayoutManager newRefreshLayoutManager(Context context) {
        RefreshLayoutManager layoutManager = mApHelper.newRefreshManager(context);
        if (layoutManager != null) {
            mRefreshLayoutManager = layoutManager;
            return layoutManager;
        }
        return super.newRefreshLayoutManager(context);
    }

    @NonNull
    @Override
    public StatusLayoutManager newStatusLayoutManager(Context context) {
        StatusLayoutManager layoutManager = mApHelper.newStatusManager(context);
        if (layoutManager != null) {
            return layoutManager;
        }
        return super.newStatusLayoutManager(context);
    }

    @NonNull
    @Override
    public ItemViewer<T> newItemViewer(int viewType) {
        throw new AfToastException("请重写 newItemViewer(int viewType) 方法");
    }

    @Override
    public void onItemBinding(ViewQuery<? extends ViewQuery> $, T model, int index) {

    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean startPager(Class clazz, Object... args) {
        if (Fragment.class.isAssignableFrom(clazz)) {
            ApFragmentActivity.start(this, clazz, args);
        } else {
            return super.startPager(clazz, args);
        }
        return true;
    }

    @Override
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        ApFragmentActivity.start(this, clazz, args);
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        ApFragmentActivity.startResult(this, clazz, request, args);
    }

    @Override
    public void postEvent(Object event) {
        mApHelper.postEvent(event);
    }

//    @Override
//    public void initItemEffect() {
//        mApHelper.initItemEffect();
//    }


    //<editor-fold desc="权限获取">
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ApItemsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(AfIntent intent, int requestCode, int resultCode) throws Exception {
        super.onActivityResult(intent, requestCode, resultCode);
        mApHelper.onActivityResult(intent, requestCode, resultCode);
    }

    //<editor-fold desc="设备权限">
    @Override
    public void doDeviceWithPermissionCheck(Runnable runnable) {
        mApHelper.doDeviceWithPermissionCheck(runnable);
        doDeviceWithPermissionCheck();
    }

    public void doDeviceWithPermissionCheck() {
        ApItemsActivityPermissionsDispatcher.doDeviceWithPermissionCheck(this);
    }

    @NeedsPermission({READ_PHONE_STATE})
    public void doDevice() {
        mApHelper.doDevice();
    }

    @OnShowRationale({READ_PHONE_STATE})
    public void showRationaleForDevice(final PermissionRequest request) {
        mApHelper.showRationaleForDevice(request);
    }

    @OnPermissionDenied({READ_PHONE_STATE})
    public void showDeniedForDevice() {
        mApHelper.showDeniedForDevice();
    }

    @OnNeverAskAgain({READ_PHONE_STATE})
    public void showNeverAskForDevice() {
        mApHelper.showNeverAskForDevice();
    }
    //</editor-fold>

    //<editor-fold desc="定位权限">

    @Override
    public void doLocationWithPermissionCheck(Runnable runnable) {
        mApHelper.doLocationWithPermissionCheck(runnable);
        doLocationWithPermissionCheck();
    }

    public void doLocationWithPermissionCheck() {
        ApItemsActivityPermissionsDispatcher.doLocationWithPermissionCheck(this);
    }

    @NeedsPermission({ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    public void doLocation() {
        mApHelper.doLocation();
    }

    @OnShowRationale({ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    public void showRationaleForLocation(final PermissionRequest request) {
        mApHelper.showRationaleForLocation(request);
    }

    @OnPermissionDenied({ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    public void showDeniedForLocation() {
        mApHelper.showDeniedForLocation();
    }

    @OnNeverAskAgain({ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    public void showNeverAskForLocation() {
        mApHelper.showNeverAskForLocation();
    }

    //</editor-fold>

    //<editor-fold desc="扩展卡权限">

    public void doStorageWithPermissionCheck(Runnable runnable) {
        mApHelper.doStorageWithPermissionCheck(runnable);
        doStorageWithPermissionCheck();
    }

    public void doStorageWithPermissionCheck() {
        ApItemsActivityPermissionsDispatcher.doStorageWithPermissionCheck(this);
    }

    @NeedsPermission({WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE})
    public void doStorage() {
        mApHelper.doStorage();
    }

    @OnShowRationale({WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE})
    public void showRationaleForStorage(final PermissionRequest request) {
        mApHelper.showRationaleForStorage(request);
    }

    @OnPermissionDenied({WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE})
    public void showDeniedForStorage() {
        mApHelper.showDeniedForStorage();
    }

    @OnNeverAskAgain({WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE})
    public void showNeverAskForStorage() {
        mApHelper.showNeverAskForStorage();
    }

    //</editor-fold>

    //<editor-fold desc="相机权限">
    @Override
    public void doCameraWithPermissionCheck(Runnable runnable) {
        mApHelper.doCameraWithPermissionCheck(runnable);
        doCameraWithPermissionCheck();
    }

    public void doCameraWithPermissionCheck() {
        ApItemsActivityPermissionsDispatcher.doCameraWithPermissionCheck(this);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void doCamera() {
        mApHelper.doCamera();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    public void showRationaleForCamera(final PermissionRequest request) {
        mApHelper.showRationaleForCamera(request);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void showDeniedForCamera() {
        mApHelper.showDeniedForCamera();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void showNeverAskForCamera() {
        mApHelper.showNeverAskForCamera();
    }
    //</editor-fold>

    //<editor-fold desc="安装权限">

    @Override
    public void doInstallWithPermissionCheck(Runnable runnable) {
        mApHelper.doInstallWithPermissionCheck(runnable);
        doInstallWithPermissionCheck();
    }

    public void doInstallWithPermissionCheck() {
        mApHelper.doInstallWithPermissionCheck();
//        ApActivityPermissionsDispatcher.doInstallWithPermissionCheck(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NeedsPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
    public void doInstall() {
        mApHelper.doInstall();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnShowRationale(Manifest.permission.REQUEST_INSTALL_PACKAGES)
    public void showRationaleForInstall(final PermissionRequest request) {
        mApHelper.showRationaleForInstall(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnPermissionDenied(Manifest.permission.REQUEST_INSTALL_PACKAGES)
    public void showDeniedForInstall() {
        mApHelper.showDeniedForInstall();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnNeverAskAgain(Manifest.permission.REQUEST_INSTALL_PACKAGES)
    public void showNeverAskForInstall() {
        mApHelper.showNeverAskForInstall();
    }
    //</editor-fold>

    //<editor-fold desc="录音权限">

    @Override
    public void doRecordAudioWithPermissionCheck(Runnable runnable) {
        mApHelper.doRecordAudioWithPermissionCheck(runnable);
        doRecordAudioWithPermissionCheck();
    }

    public void doRecordAudioWithPermissionCheck() {
        ApItemsActivityPermissionsDispatcher.doRecordAudioWithPermissionCheck(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void doRecordAudio() {
        mApHelper.doRecordAudio();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    public void showRationaleForRecordAudio(final PermissionRequest request) {
        mApHelper.showRationaleForRecordAudio(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    public void showDeniedForRecordAudio() {
        mApHelper.showDeniedForRecordAudio();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    public void showNeverAskForRecordAudio() {
        mApHelper.showNeverAskForRecordAudio();
    }
    //</editor-fold>

    //</editor-fold>

}
