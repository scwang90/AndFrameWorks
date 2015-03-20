package com.ontheway.layoutbind;

import com.ontheway.activity.framework.AfViewable;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;

public abstract class AfLayoutAlpha extends AfLayoutModule implements AnimationListener {

	private static final long DURATION = 500;

	protected AlphaAnimation mAnimationHide = new AlphaAnimation(1, 0);
	protected AlphaAnimation mAnimationShow = new AlphaAnimation(0, 1);

	protected boolean mHasStarted;
	
	public AfLayoutAlpha(AfViewable view) {
		super(view);
		// TODO Auto-generated constructor stub
		mAnimationHide.setDuration(DURATION);
		mAnimationShow.setDuration(DURATION);
	}

	public void hide() {
		// TODO Auto-generated constructor stub
		if (mLayout != null && mLayout.getVisibility() == View.VISIBLE) {
			mAnimationHide = new AlphaAnimation(1, 0);
			mAnimationHide.setDuration(DURATION);
			mAnimationHide.setAnimationListener(this);
			AnimationSet animationSet = new AnimationSet(true);
			animationSet.addAnimation(mAnimationHide);
			mLayout.clearAnimation();
			mLayout.setAnimation(animationSet);
		}
	}

	public void show() {
		// TODO Auto-generated constructor stub
		if (mLayout != null && mLayout.getVisibility() != View.VISIBLE) {
			mLayout.setVisibility(View.VISIBLE);
			mAnimationShow = new AlphaAnimation(0, 1);
			mAnimationShow.setDuration(DURATION);
			mAnimationShow.setAnimationListener(this);
			AnimationSet animationSet = new AnimationSet(true);
			animationSet.addAnimation(mAnimationShow);
			mLayout.clearAnimation();
			mLayout.setAnimation(animationSet);
		}
	}
	
	@Override
	public boolean isVisibility() {
		// TODO Auto-generated method stub
		if(!mHasStarted){
			return super.isVisibility();
		}else{
			return false;
		}
	}
	

    @Override
    public void onAnimationStart(Animation animation) {
    	// TODO Auto-generated method stub
    	mHasStarted = true;
    }
    
    @Override
    public void onAnimationRepeat(Animation animation) {
    	// TODO Auto-generated method stub
    }
    
    @Override
    public void onAnimationEnd(Animation animation) {
    	// TODO Auto-generated method stub
    	mHasStarted = false;
    	if(animation == mAnimationHide){
    		mLayout.setVisibility(View.GONE);
    	}else if(animation == mAnimationShow){
			
		}
    }
}
