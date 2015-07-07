package com.andframe.layoutbind.framework;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class AfViewDelegate extends View{

	protected View target;

	public AfViewDelegate(View view) {
		super(view.getContext());
		// TODO Auto-generated constructor stub
		this.target = view;
		if (target == null) {
			target = this;
		}
	}
	
	@Override
	@TargetApi(16)
	public void setBackground(Drawable background) {
		// TODO Auto-generated method stub
		target.setBackground(background);
	}

	public void addFocusables(ArrayList<View> views, int direction,
			int focusableMode) {
		target.addFocusables(views, direction, focusableMode);
	}

	public void addFocusables(ArrayList<View> views, int direction) {
		target.addFocusables(views, direction);
	}

	public void addTouchables(ArrayList<View> views) {
		target.addTouchables(views);
	}

	public void bringToFront() {
		target.bringToFront();
	}

	public void buildDrawingCache() {
		target.buildDrawingCache();
	}

	public void buildDrawingCache(boolean autoScale) {
		target.buildDrawingCache(autoScale);
	}

	public void cancelLongPress() {
		target.cancelLongPress();
	}

	public boolean checkInputConnectionProxy(View view) {
		return target.checkInputConnectionProxy(view);
	}

	public void clearAnimation() {
		target.clearAnimation();
	}

	public void clearFocus() {
		target.clearFocus();
	}

	public void computeScroll() {
		target.computeScroll();
	}

	public void createContextMenu(ContextMenu menu) {
		target.createContextMenu(menu);
	}

	public void destroyDrawingCache() {
		target.destroyDrawingCache();
	}

	public void dispatchConfigurationChanged(Configuration newConfig) {
		target.dispatchConfigurationChanged(newConfig);
	}

	public void dispatchDisplayHint(int hint) {
		target.dispatchDisplayHint(hint);
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		return target.dispatchKeyEvent(event);
	}

	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		return target.dispatchKeyEventPreIme(event);
	}

	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
		return target.dispatchKeyShortcutEvent(event);
	}

	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
		return target.dispatchPopulateAccessibilityEvent(event);
	}

	public boolean dispatchTouchEvent(MotionEvent event) {
		return target.dispatchTouchEvent(event);
	}

	public boolean dispatchTrackballEvent(MotionEvent event) {
		return target.dispatchTrackballEvent(event);
	}

	public boolean dispatchUnhandledMove(View focused, int direction) {
		return target.dispatchUnhandledMove(focused, direction);
	}

	public void dispatchWindowFocusChanged(boolean hasFocus) {
		target.dispatchWindowFocusChanged(hasFocus);
	}

	public void dispatchWindowVisibilityChanged(int visibility) {
		target.dispatchWindowVisibilityChanged(visibility);
	}

	public void draw(Canvas canvas) {
		target.draw(canvas);
	}

	public boolean equals(Object o) {
		return target.equals(o);
	}

	public View findFocus() {
		return target.findFocus();
	}

	public View focusSearch(int direction) {
		return target.focusSearch(direction);
	}

	public void forceLayout() {
		target.forceLayout();
	}

	public Animation getAnimation() {
		return target.getAnimation();
	}

	public IBinder getApplicationWindowToken() {
		return target.getApplicationWindowToken();
	}

	public Drawable getBackground() {
		return target.getBackground();
	}

	public int getBaseline() {
		return target.getBaseline();
	}

	public CharSequence getContentDescription() {
		return target.getContentDescription();
	}

	public Bitmap getDrawingCache() {
		return target.getDrawingCache();
	}

	public Bitmap getDrawingCache(boolean autoScale) {
		return target.getDrawingCache(autoScale);
	}

	public int getDrawingCacheBackgroundColor() {
		return target.getDrawingCacheBackgroundColor();
	}

	public int getDrawingCacheQuality() {
		return target.getDrawingCacheQuality();
	}

	public void getDrawingRect(Rect outRect) {
		target.getDrawingRect(outRect);
	}

	public long getDrawingTime() {
		return target.getDrawingTime();
	}

	public ArrayList<View> getFocusables(int direction) {
		return target.getFocusables(direction);
	}

	public void getFocusedRect(Rect r) {
		target.getFocusedRect(r);
	}

	public boolean getGlobalVisibleRect(Rect r, Point globalOffset) {
		return target.getGlobalVisibleRect(r, globalOffset);
	}

	public Handler getHandler() {
		return target.getHandler();
	}

	public void getHitRect(Rect outRect) {
		target.getHitRect(outRect);
	}

	public int getHorizontalFadingEdgeLength() {
		return target.getHorizontalFadingEdgeLength();
	}

	public int getId() {
		return target.getId();
	}

	public boolean getKeepScreenOn() {
		return target.getKeepScreenOn();
	}

	public DispatcherState getKeyDispatcherState() {
		return target.getKeyDispatcherState();
	}

	public LayoutParams getLayoutParams() {
		return target.getLayoutParams();
	}

	public void getLocationInWindow(int[] location) {
		target.getLocationInWindow(location);
	}

	public void getLocationOnScreen(int[] location) {
		target.getLocationOnScreen(location);
	}

	public int getNextFocusDownId() {
		return target.getNextFocusDownId();
	}

	public int getNextFocusLeftId() {
		return target.getNextFocusLeftId();
	}

	public int getNextFocusRightId() {
		return target.getNextFocusRightId();
	}

	public int getNextFocusUpId() {
		return target.getNextFocusUpId();
	}

	public OnFocusChangeListener getOnFocusChangeListener() {
		return target.getOnFocusChangeListener();
	}

	public int getPaddingBottom() {
		return target.getPaddingBottom();
	}

	public int getPaddingLeft() {
		return target.getPaddingLeft();
	}

	public int getPaddingRight() {
		return target.getPaddingRight();
	}

	public int getPaddingTop() {
		return target.getPaddingTop();
	}

	public Resources getResources() {
		return target.getResources();
	}

	public View getRootView() {
		return target.getRootView();
	}

	public int getScrollBarStyle() {
		return target.getScrollBarStyle();
	}

	public int getSolidColor() {
		return target.getSolidColor();
	}

	public Object getTag() {
		return target.getTag();
	}

	public Object getTag(int key) {
		return target.getTag(key);
	}

	public TouchDelegate getTouchDelegate() {
		return target.getTouchDelegate();
	}

	public ArrayList<View> getTouchables() {
		return target.getTouchables();
	}

	public int getVerticalFadingEdgeLength() {
		return target.getVerticalFadingEdgeLength();
	}

	public int getVerticalScrollbarWidth() {
		return target.getVerticalScrollbarWidth();
	}

	public ViewTreeObserver getViewTreeObserver() {
		return target.getViewTreeObserver();
	}

	public int getVisibility() {
		return target.getVisibility();
	}

	public IBinder getWindowToken() {
		return target.getWindowToken();
	}

	public int getWindowVisibility() {
		return target.getWindowVisibility();
	}

	public void getWindowVisibleDisplayFrame(Rect outRect) {
		target.getWindowVisibleDisplayFrame(outRect);
	}

	public boolean hasFocus() {
		return target.hasFocus();
	}

	public boolean hasFocusable() {
		return target.hasFocusable();
	}

	public boolean hasWindowFocus() {
		return target.hasWindowFocus();
	}

	public int hashCode() {
		return target.hashCode();
	}

	public void invalidate() {
		target.invalidate();
	}

	public void invalidate(int l, int t, int r, int b) {
		target.invalidate(l, t, r, b);
	}

	public void invalidate(Rect dirty) {
		target.invalidate(dirty);
	}

	public void invalidateDrawable(Drawable drawable) {
		target.invalidateDrawable(drawable);
	}

	public boolean isClickable() {
		return target.isClickable();
	}

	public boolean isDrawingCacheEnabled() {
		return target.isDrawingCacheEnabled();
	}

	public boolean isDuplicateParentStateEnabled() {
		return target.isDuplicateParentStateEnabled();
	}

	public boolean isEnabled() {
		return target.isEnabled();
	}

	public boolean isFocused() {
		return target.isFocused();
	}

	public boolean isHapticFeedbackEnabled() {
		return target.isHapticFeedbackEnabled();
	}

	public boolean isHorizontalFadingEdgeEnabled() {
		return target.isHorizontalFadingEdgeEnabled();
	}

	public boolean isHorizontalScrollBarEnabled() {
		return target.isHorizontalScrollBarEnabled();
	}

	public boolean isInEditMode() {
		return target.isInEditMode();
	}

	public boolean isInTouchMode() {
		return target.isInTouchMode();
	}

	public boolean isLayoutRequested() {
		return target.isLayoutRequested();
	}

	public boolean isLongClickable() {
		return target.isLongClickable();
	}

	public boolean isOpaque() {
		return target.isOpaque();
	}

	public boolean isPressed() {
		return target.isPressed();
	}

	public boolean isSaveEnabled() {
		return target.isSaveEnabled();
	}

	public boolean isScrollbarFadingEnabled() {
		return target.isScrollbarFadingEnabled();
	}

	public boolean isSelected() {
		return target.isSelected();
	}

	public boolean isShown() {
		return target.isShown();
	}

	public boolean isSoundEffectsEnabled() {
		return target.isSoundEffectsEnabled();
	}

	public boolean isVerticalFadingEdgeEnabled() {
		return target.isVerticalFadingEdgeEnabled();
	}

	public boolean isVerticalScrollBarEnabled() {
		return target.isVerticalScrollBarEnabled();
	}

	public void offsetLeftAndRight(int offset) {
		target.offsetLeftAndRight(offset);
	}

	public void offsetTopAndBottom(int offset) {
		target.offsetTopAndBottom(offset);
	}

	public boolean onCheckIsTextEditor() {
		return target.onCheckIsTextEditor();
	}

	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		return target.onCreateInputConnection(outAttrs);
	}

	public void onFinishTemporaryDetach() {
		target.onFinishTemporaryDetach();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return target.onKeyDown(keyCode, event);
	}

	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		return target.onKeyLongPress(keyCode, event);
	}

	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		return target.onKeyMultiple(keyCode, repeatCount, event);
	}

	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		return target.onKeyPreIme(keyCode, event);
	}

	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		return target.onKeyShortcut(keyCode, event);
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return target.onKeyUp(keyCode, event);
	}

	public void onStartTemporaryDetach() {
		target.onStartTemporaryDetach();
	}

	public boolean onTouchEvent(MotionEvent event) {
		return target.onTouchEvent(event);
	}

	public boolean onTrackballEvent(MotionEvent event) {
		return target.onTrackballEvent(event);
	}

	public void onWindowFocusChanged(boolean hasWindowFocus) {
		target.onWindowFocusChanged(hasWindowFocus);
	}

	public boolean performClick() {
		return target.performClick();
	}

	public boolean performHapticFeedback(int feedbackConstant, int flags) {
		return target.performHapticFeedback(feedbackConstant, flags);
	}

	public boolean performHapticFeedback(int feedbackConstant) {
		return target.performHapticFeedback(feedbackConstant);
	}

	public boolean performLongClick() {
		return target.performLongClick();
	}

	public void playSoundEffect(int soundConstant) {
		target.playSoundEffect(soundConstant);
	}

	public boolean post(Runnable action) {
		return target.post(action);
	}

	public boolean postDelayed(Runnable action, long delayMillis) {
		return target.postDelayed(action, delayMillis);
	}

	public void postInvalidate() {
		target.postInvalidate();
	}

	public void postInvalidate(int left, int top, int right, int bottom) {
		target.postInvalidate(left, top, right, bottom);
	}

	public void postInvalidateDelayed(long delayMilliseconds, int left,
			int top, int right, int bottom) {
		target.postInvalidateDelayed(delayMilliseconds, left, top, right,
				bottom);
	}

	public void postInvalidateDelayed(long delayMilliseconds) {
		target.postInvalidateDelayed(delayMilliseconds);
	}

	public void refreshDrawableState() {
		target.refreshDrawableState();
	}

	public boolean removeCallbacks(Runnable action) {
		return target.removeCallbacks(action);
	}

	public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
		return target.requestFocus(direction, previouslyFocusedRect);
	}

	public void requestLayout() {
		target.requestLayout();
	}

	public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
		return target.requestRectangleOnScreen(rectangle, immediate);
	}

	public boolean requestRectangleOnScreen(Rect rectangle) {
		return target.requestRectangleOnScreen(rectangle);
	}

	public void restoreHierarchyState(SparseArray<Parcelable> container) {
		target.restoreHierarchyState(container);
	}

	public void saveHierarchyState(SparseArray<Parcelable> container) {
		target.saveHierarchyState(container);
	}

	public void scheduleDrawable(Drawable who, Runnable what, long when) {
		target.scheduleDrawable(who, what, when);
	}

	public void scrollBy(int x, int y) {
		target.scrollBy(x, y);
	}

	public void scrollTo(int x, int y) {
		target.scrollTo(x, y);
	}

	public void sendAccessibilityEvent(int eventType) {
		target.sendAccessibilityEvent(eventType);
	}

	public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
		target.sendAccessibilityEventUnchecked(event);
	}

	public void setAnimation(Animation animation) {
		target.setAnimation(animation);
	}

	public void setBackgroundColor(int color) {
		target.setBackgroundColor(color);
	}

    /**
     * @deprecated use {@link #setBackground(Drawable)} instead
     */
	public void setBackgroundDrawable(Drawable background) {
		target.setBackgroundDrawable(background);
	}

	public void setBackgroundResource(int resid) {
		target.setBackgroundResource(resid);
	}

	public void setClickable(boolean clickable) {
		target.setClickable(clickable);
	}

	public void setContentDescription(CharSequence contentDescription) {
		target.setContentDescription(contentDescription);
	}

	public void setDrawingCacheBackgroundColor(int color) {
		target.setDrawingCacheBackgroundColor(color);
	}

	public void setDrawingCacheEnabled(boolean enabled) {
		target.setDrawingCacheEnabled(enabled);
	}

	public void setDrawingCacheQuality(int quality) {
		target.setDrawingCacheQuality(quality);
	}

	public void setDuplicateParentStateEnabled(boolean enabled) {
		target.setDuplicateParentStateEnabled(enabled);
	}

	public void setEnabled(boolean enabled) {
		target.setEnabled(enabled);
	}

	public void setFadingEdgeLength(int length) {
		target.setFadingEdgeLength(length);
	}

	public void setFocusable(boolean focusable) {
		target.setFocusable(focusable);
	}

	public void setFocusableInTouchMode(boolean focusableInTouchMode) {
		target.setFocusableInTouchMode(focusableInTouchMode);
	}

	public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
		target.setHapticFeedbackEnabled(hapticFeedbackEnabled);
	}

	public void setHorizontalFadingEdgeEnabled(
			boolean horizontalFadingEdgeEnabled) {
		target.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
	}

	public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
		target.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
	}

	public void setId(int id) {
		target.setId(id);
	}

	public void setKeepScreenOn(boolean keepScreenOn) {
		target.setKeepScreenOn(keepScreenOn);
	}

	public void setLayoutParams(LayoutParams params) {
		target.setLayoutParams(params);
	}

	public void setLongClickable(boolean longClickable) {
		target.setLongClickable(longClickable);
	}

	public void setMinimumHeight(int minHeight) {
		target.setMinimumHeight(minHeight);
	}

	public void setMinimumWidth(int minWidth) {
		target.setMinimumWidth(minWidth);
	}

	public void setNextFocusDownId(int nextFocusDownId) {
		target.setNextFocusDownId(nextFocusDownId);
	}

	public void setNextFocusLeftId(int nextFocusLeftId) {
		target.setNextFocusLeftId(nextFocusLeftId);
	}

	public void setNextFocusRightId(int nextFocusRightId) {
		target.setNextFocusRightId(nextFocusRightId);
	}

	public void setNextFocusUpId(int nextFocusUpId) {
		target.setNextFocusUpId(nextFocusUpId);
	}

	public void setOnClickListener(OnClickListener l) {
		target.setOnClickListener(l);
	}

	public void setOnCreateContextMenuListener(OnCreateContextMenuListener l) {
		target.setOnCreateContextMenuListener(l);
	}

	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		target.setOnFocusChangeListener(l);
	}

	public void setOnKeyListener(OnKeyListener l) {
		target.setOnKeyListener(l);
	}

	public void setOnLongClickListener(OnLongClickListener l) {
		target.setOnLongClickListener(l);
	}

	public void setOnTouchListener(OnTouchListener l) {
		target.setOnTouchListener(l);
	}

	public void setPadding(int left, int top, int right, int bottom) {
		target.setPadding(left, top, right, bottom);
	}

	public void setPressed(boolean pressed) {
		target.setPressed(pressed);
	}

	public void setSaveEnabled(boolean enabled) {
		target.setSaveEnabled(enabled);
	}

	public void setScrollBarStyle(int style) {
		target.setScrollBarStyle(style);
	}

	public void setScrollContainer(boolean isScrollContainer) {
		target.setScrollContainer(isScrollContainer);
	}

	public void setScrollbarFadingEnabled(boolean fadeScrollbars) {
		target.setScrollbarFadingEnabled(fadeScrollbars);
	}

	public void setSelected(boolean selected) {
		target.setSelected(selected);
	}

	public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
		target.setSoundEffectsEnabled(soundEffectsEnabled);
	}

	public void setTag(int key, Object tag) {
		target.setTag(key, tag);
	}

	public void setTag(Object tag) {
		target.setTag(tag);
	}

	public void setTouchDelegate(TouchDelegate delegate) {
		target.setTouchDelegate(delegate);
	}

	public void setVerticalFadingEdgeEnabled(boolean verticalFadingEdgeEnabled) {
		target.setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled);
	}

	public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
		target.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
	}

	public void setVisibility(int visibility) {
		target.setVisibility(visibility);
	}

	public void setWillNotCacheDrawing(boolean willNotCacheDrawing) {
		target.setWillNotCacheDrawing(willNotCacheDrawing);
	}

	public void setWillNotDraw(boolean willNotDraw) {
		target.setWillNotDraw(willNotDraw);
	}

	public boolean showContextMenu() {
		return target.showContextMenu();
	}

	public void startAnimation(Animation animation) {
		target.startAnimation(animation);
	}

	public String toString() {
		return target.toString();
	}

	public void unscheduleDrawable(Drawable who, Runnable what) {
		target.unscheduleDrawable(who, what);
	}

	public void unscheduleDrawable(Drawable who) {
		target.unscheduleDrawable(who);
	}

	public boolean willNotCacheDrawing() {
		return target.willNotCacheDrawing();
	}

	public boolean willNotDraw() {
		return target.willNotDraw();
	}


}
