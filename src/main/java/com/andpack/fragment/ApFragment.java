package com.andpack.fragment;

import android.Manifest;
import android.os.Build;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.andframe.feature.AfIntent;
import com.andframe.fragment.AfTabFragment;
import com.andpack.activity.ApFragmentActivity;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

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
public class ApFragment extends AfTabFragment implements ApPager {

    protected ApPagerHelper mApHelper = new ApPagerHelper(this);

    @Override
    protected void onCreated() {
        mApHelper.onCreate();
        super.onCreated();
    }

    @CallSuper
    public void onViewCreated()  {
        super.onViewCreated();
        mApHelper.onViewCreated();
    }

    @Override
    public void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        mApHelper.onDestroyView();
        super.onDestroyView();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean startPager(Class<?> clazz, Object... args) {
        if (Fragment.class.isAssignableFrom(clazz)) {
            ApFragmentActivity.start(this, (Class<? extends Fragment>) clazz, args);
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


    //<editor-fold desc="获取权限">
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ApFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    @Override
    protected void onActivityResult(AfIntent intent, int requestCode, int resultCode) {
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
        ApFragmentPermissionsDispatcher.doDeviceWithPermissionCheck(this);
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
        ApFragmentPermissionsDispatcher.doLocationWithPermissionCheck(this);
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
        ApFragmentPermissionsDispatcher.doStorageWithPermissionCheck(this);
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
        ApFragmentPermissionsDispatcher.doCameraWithPermissionCheck(this);
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
        ApFragmentPermissionsDispatcher.doRecordAudioWithPermissionCheck(this);
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
