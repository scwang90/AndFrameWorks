package com.andframe.model.constants;

import android.view.WindowManager;

public enum SoftInputAdjust {

    None(0),
    AdjustUnspecified(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED),
    AdjustResize(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE),
    AdjustPan(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN),
    AdjustNothing(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

    public final int value;

    SoftInputAdjust(int value) {
        this.value = value;
    }
}
