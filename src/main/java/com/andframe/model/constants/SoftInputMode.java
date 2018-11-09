package com.andframe.model.constants;

import android.view.WindowManager;

public enum SoftInputMode {

    None(0),

    StateUnspecified(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED),
    StateUnchanged(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED),
    StateHidden(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN),
    StateAlwaysHidden(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN),
    StateVisible(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE),
    StateAlwaysVisible(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE),

    AdjustUnspecified(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED),
    AdjustResize(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE),
    AdjustPan(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN),
    AdjustNothing(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING),

    StateAlwaysVisibleAdjustResize(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE),
    StateAlwaysVisibleAdjustPan(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN),
    StateAlwaysVisibleAdjustNothing(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING),

    StateVisibleAdjustResize(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE),
    StateVisibleAdjustPan(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN),
    StateVisibleAdjustNothing(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING),

    StateAlwaysHiddenAdjustResize(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE),
    StateAlwaysHiddenAdjustPan(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN),
    StateAlwaysHiddenAdjustNothing(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING),

    StateHiddenAdjustResize(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE),
    StateHiddenAdjustPan(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN),
    StateHiddenAdjustNothing(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING),

    StateUnchangedAdjustResize(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE),
    StateUnchangedAdjustPan(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN),
    StateUnchangedAdjustNothing(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING),;

    public final int value;

    SoftInputMode(int value) {
        this.value = value;
    }
}
