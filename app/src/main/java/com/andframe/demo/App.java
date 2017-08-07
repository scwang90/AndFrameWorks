package com.andframe.demo;

import android.app.Application;

import com.andframe.AndFrame;

/**
 * Application 实例扩展
 * Created by SCWANG on 2017/8/7.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndFrame.init(this);
    }
}
