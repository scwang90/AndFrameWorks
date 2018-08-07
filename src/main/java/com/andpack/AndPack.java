package com.andpack;

import com.andframe.AndFrame;
import com.andpack.application.ApAppSettings;

public class AndPack extends AndFrame {

    public static ApAppSettings settings() {
        return (ApAppSettings)AndFrame.settings();
    }

}
