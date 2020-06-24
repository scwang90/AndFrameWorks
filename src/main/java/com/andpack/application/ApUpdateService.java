package com.andpack.application;

import com.andframe.api.pager.Pager;
import com.andframe.application.DefaultUpdateService;
import com.andpack.$;
import com.andpack.api.ApPager;

import java.io.File;

public abstract class ApUpdateService extends DefaultUpdateService {

    private static int count = 0;

    @Override
    public void install(File file) {
        //http://file.teecloud.cn/public/download/app/android/%E8%81%8C%E8%80%83%E9%80%9A.apk
        Pager pager = $.pager().currentPager();
        if (pager == null || pager.isRecycled()) {
            $.pager().startForeground();
            if (count++ < 5) {
                $.dispatch(5000, () -> install(file));
            }
        } else if (pager instanceof ApPager) {
            count = 0;
            ((ApPager) pager).doInstallWithPermissionCheck(()->{
                super.install(file);
            });
        }
    }

}
