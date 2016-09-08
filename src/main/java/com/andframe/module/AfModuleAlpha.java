package com.andframe.module;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;

import com.andframe.api.view.Viewer;

@SuppressWarnings("unused")
public abstract class AfModuleAlpha extends AfViewModuler implements AnimationListener {

	private static final long DURATION = 500;

	protected AlphaAnimation mAnimationHide = null;//new AlphaAnimation(1, 0);
	protected AlphaAnimation mAnimationShow = null;//new AlphaAnimation(0, 1);

	protected boolean mHasStarted;

	public AfModuleAlpha() {
	}

	public AfModuleAlpha(Viewer view) {
		super(view);
//		mAnimationHide.setDuration(DURATION);
//		mAnimationShow.setDuration(DURATION);
	}

	public AfModuleAlpha(Viewer view, int viewid) {
		super(view,viewid);
//		mAnimationHide.setDuration(DURATION);
//		mAnimationShow.setDuration(DURATION);
	}

	@SuppressWarnings("unused")
	public void setBackgroundAlpha(int alpha) {
		Drawable drawable = getBackground();
		if (drawable != null) {
			drawable.setAlpha(alpha);
		}
	}

	public void hide(boolean smoothAlpha) {
		if (smoothAlpha) {
			this.hide();
		} else {
			super.hide();
		}
	}

	public void show(boolean smoothAlpha) {
		if (smoothAlpha) {
			this.show();
		} else {
			super.show();
		}
	}

	public void hide() {
		if (wrapped != null && wrapped.getVisibility() == View.VISIBLE) {
			mAnimationHide = new AlphaAnimation(1, 0);
			mAnimationHide.setDuration(DURATION);
			mAnimationHide.setAnimationListener(this);
			AnimationSet animationSet = new AnimationSet(true);
			animationSet.addAnimation(mAnimationHide);
			wrapped.clearAnimation();
			wrapped.setAnimation(animationSet);
		}
	}

	public void show() {
		if (wrapped != null && wrapped.getVisibility() != View.VISIBLE) {
			wrapped.setVisibility(View.VISIBLE);
			mAnimationShow = new AlphaAnimation(0, 1);
			mAnimationShow.setDuration(DURATION);
			mAnimationShow.setAnimationListener(this);
			AnimationSet animationSet = new AnimationSet(true);
			animationSet.addAnimation(mAnimationShow);
			wrapped.clearAnimation();
			wrapped.setAnimation(animationSet);
		}
	}
	
	@Override
	public boolean isVisibility() {
		return !mHasStarted && super.isVisibility();
	}

    @Override
    public void onAnimationStart(Animation animation) {
    	mHasStarted = true;
    }
    
    @Override
    public void onAnimationRepeat(Animation animation) {
    }
    
    @Override
    public void onAnimationEnd(Animation animation) {
    	mHasStarted = false;
    	if(animation == mAnimationHide){
    		wrapped.setVisibility(View.GONE);
//    	}else if(animation == mAnimationShow){
//
		}
    }
}
