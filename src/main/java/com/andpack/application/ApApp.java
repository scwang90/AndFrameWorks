package com.andpack.application;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.andframe.$;
import com.andframe.api.DialogBuilder;
import com.andframe.api.view.ViewQuery;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.exception.AfToastException;
import com.andpack.impl.ApDialogBuilder;
import com.andpack.impl.ApExceptionHandler;
import com.andpack.impl.ApViewQuery;
import com.andrestful.http.MultiRequestHandler;
import com.lzy.imagepicker.ImagePicker;
import com.nostra13.universalimageloader.cache.disc.impl.ext.ApDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.IOException;

/**
 *
 * Created by SCWANG on 2016/9/6.
 */
public class ApApp extends AfApp {

    public static ApApp getApApp() {
        return (ApApp)get();
    }

    public boolean isUserLogined() {
        throw new AfToastException("App.isUserLogined必须由子类实现");
    }

    //<editor-fold desc="初始化">

    @Override
    protected void initApp() throws Exception {
        super.initApp();
        initImageLoader(this);
        initImagePicker(this);
        MultiRequestHandler.DEBUD = isDebug();
    }

    private void initImagePicker(Context context) {
        ImagePicker picker = ImagePicker.getInstance();
        picker.setImageLoader(new com.lzy.imagepicker.loader.ImageLoader() {
            @Override
            public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
                $.query(imageView).image(path);
            }
            @Override
            public void clearMemoryCache() {
            }
        });
    }

    protected void initImageLoader(Context context) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheOnDisk(true);
        builder.cacheInMemory(true);
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        //config.threadPriority(Thread.NORM_PRIORITY - 2);
        //config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.defaultDisplayImageOptions(builder.build());
        config.memoryCacheSizePercentage(12);
        /**
         * 初始化图片 SD卡缓存
         */
        try {
            File imageDir = new File(getExternalCacheDir(),"uil-image");
            config.diskCache(new ApDiskCache(imageDir, new Md5FileNameGenerator(), 50 * 1024 * 1024));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageLoader.getInstance().init(config.build());
    }
    //</editor-fold>

    //<editor-fold desc="重写组件">
    @Override
    public DialogBuilder newDialogBuilder(Context context) {
        return new ApDialogBuilder(context);
    }

    @Override
    public ViewQuery newViewQuery(View view) {
        return new ApViewQuery(view);
    }

    @Override
    public AfExceptionHandler newExceptionHandler() {
        return new ApExceptionHandler();
    }

    public ApUpdateService getUpdateService() {
        return new ApUpdateService(this) {
            @Override
            protected ServiceVersionInfo infoFromService(String version) throws Exception {
                return null;
            }
        };
    }

    //</editor-fold>

}
