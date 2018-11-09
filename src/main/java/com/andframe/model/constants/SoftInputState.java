package com.andframe.model.constants;

import android.view.WindowManager;

public enum SoftInputState {

    None(0),
    StateUnspecified(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED),
    StateUnchanged(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED),
    StateHidden(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN),
    StateAlwaysHidden(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN),
    StateVisible(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE),
    StateAlwaysVisible(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    public final int value;

    SoftInputState(int value) {
        this.value = value;
    }
}
