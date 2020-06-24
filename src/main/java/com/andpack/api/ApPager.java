package com.andpack.api;

import android.content.Intent;

import com.andframe.api.pager.Pager;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public interface ApPager extends Pager {

    void postEvent(Object event);
    void doStorageWithPermissionCheck(Runnable runnable);
    void doCameraWithPermissionCheck(Runnable runnable);
    void doDeviceWithPermissionCheck(Runnable runnable);
    void doLocationWithPermissionCheck(Runnable runnable);
    void doInstallWithPermissionCheck(Runnable runnable);
    void doRecordAudioWithPermissionCheck(Runnable runnable);
    void doInstall();//安装有点特殊，用户需要查看更新信息之后点击更新才会更新，回调安装方法
    void startActivityForResult(Intent intent, int requestCode);

}
