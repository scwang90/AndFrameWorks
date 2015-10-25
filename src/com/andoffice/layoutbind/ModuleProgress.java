package com.andoffice.layoutbind;

import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.andoffice.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.layoutbind.framework.IAfLayoutModule;
import com.andframe.thread.AfHandlerTimerTask;

public class ModuleProgress extends AfHandlerTimerTask implements IAfLayoutModule {

	public static final int DELAYMILLIS = 300;

	public View mLayout = null;
	public TextView mTvDescription = null;

	private int mCount = 0;
	private boolean mIsValid = false;
	private Drawable[] mDrawables = null;


	public ModuleProgress(AfPageable page) {
//		Resources resource = page.getResources();
//		mDrawables = new Drawable[] {
//				resource.getDrawable(R.drawable.image_person),
//				resource.getDrawable(R.drawable.image_person),
//				resource.getDrawable(R.drawable.image_person), };
		mTvDescription = page.findViewByID(R.id.module_progress_loadinfo);
		if(mTvDescription != null){
			mIsValid = true;
			//mHandler.post(this);
			//new Timer().schedule(this, DELAYMILLIS);
			mTvDescription.setText("正在加载...");
			mLayout = (View) mTvDescription.getParent();
		}
	}

	public void setDescription(String description) {
		mTvDescription.setText(description);
	}

	public void setDescription(int resid) {
		mTvDescription.setText(resid);
	}

	public View getLayout() {
		return mLayout;
	}

	public void hide() {
		if (mIsValid && mLayout.getVisibility() == View.VISIBLE) {
			mLayout.setVisibility(View.GONE);
		}
	}

	public void show() {
		if (mIsValid && mLayout.getVisibility() != View.VISIBLE) {
			mLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected boolean onHandleTimer(Message msg) {
		Drawable drawable = mDrawables[mCount];
		mTvDescription.setCompoundDrawablesWithIntrinsicBounds(null, drawable,
				null, null);
		mCount = ++mCount % 3;
		return true;
	}

	@Override
	protected void finalize() {
		// add something....................
		mHandler.removeCallbacks(this);
	}

	@Override
	public boolean isValid() {
		return mIsValid;
	}
	
	@Override
	public boolean isVisibility() {
		if(mIsValid){
			return mLayout.getVisibility() == View.VISIBLE;
		}
		return false;
	}
	
	@Override
	public void setEnabled(boolean enabled) {

	}
}
