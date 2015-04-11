package com.andframe.feature;

import android.os.Message;
import android.widget.ImageView;

import com.andframe.helper.android.AfGifHelper.GifFrame;
import com.andframe.thread.AfTask;

public class AfGifPlayer extends AfTask{

    private int index = 0;
    private ImageView image;
    private GifFrame[] frames;

    public AfGifPlayer(ImageView iv, GifFrame[] frames) {
        this.image = iv;
        this.frames = frames;
    }

    public void start() {
        image.post(this);
    }
    
    public void stop() {
        if(null != image) image.removeCallbacks(this);
        image = null;
        if(null != frames) {
            for(GifFrame frame : frames) {
                if(frame.image != null && !frame.image.isRecycled()) {
                    frame.image.recycle();
                    frame.image = null;
                }
            }
            frames = null;
        }
    }
    
	@Override
	protected void onWorking(Message msg) throws Exception {
		// TODO Auto-generated method stub
        if (!frames[index].image.isRecycled()) {
            image.setImageBitmap(frames[index].image);
        }
        image.postDelayed(this, frames[index++].delay);
        index %= frames.length;
	}

}
