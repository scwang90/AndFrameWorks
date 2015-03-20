package com.ontheway.thread;

import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

public class AfSmoothRunnable implements Runnable
{
    public static final int ANIMATION_FPS = 1000 / 60;
    public static final int ANIMATION_DURATION_MS = 190;

    private final int valueto;
    private final int valuefrom;
    private final int duratioin;
    private final Handler handler;
    private final Smoothable smoothable;
    private Interpolator interpolator;

    private int value = -1;
    private long startTime = -1;
    private boolean running = true;

    public static interface Smoothable
    {
    	public void onStart(int from, int to);
    	public void onFinish(int from, int to);
    	//返回false表示中途停止 true 表示继续
        public boolean onSmooth(int value,float percent, int from, int to);
    }

    public AfSmoothRunnable(Looper looper, Smoothable smoothable, int from,
            int to)
    {
        this.valueto = to;
        this.valuefrom = from;
        this.smoothable = smoothable;
        this.duratioin = ANIMATION_DURATION_MS;
        this.handler = new Handler(looper);
        this.interpolator = new AccelerateDecelerateInterpolator();
    }
    
    public AfSmoothRunnable(Looper looper, Smoothable smoothable, int from,
            int to,int duratioin)
    {
        this.valueto = to;
        this.valuefrom = from;
        this.duratioin = duratioin;
        this.smoothable = smoothable;
        this.handler = new Handler(looper);
        this.interpolator = new AccelerateDecelerateInterpolator();
    }
    
    public AfSmoothRunnable(Smoothable smoothable, int from,
            int to,int duratioin)
    {
        this.valueto = to;
        this.valuefrom = from;
        this.duratioin = duratioin;
        this.smoothable = smoothable;
        this.handler = new Handler(Looper.myLooper());
        this.interpolator = new AccelerateDecelerateInterpolator();
    }
    /**
     * 设置加速器
     * @param interpolator
     */
    public void setInterpolator(Interpolator interpolator){
    	if (interpolator != null) {
        	this.interpolator = interpolator; 
		}
    }

    @Override
    public void run()
    {
        /**
         * Only set startTime if this is the first time we're starting, else
         * actually calculate the Y delta
         */
        if (startTime == -1)
        {
            startTime = System.currentTimeMillis();
        	smoothable.onStart(valuefrom, valueto);
        }
        else
        {

            /**
             * We do do all calculations in long to reduce software float
             * calculations. We use 1000 as it gives us good accuracy and
             * small rounding errors
             */
            long normalizedTime = System.currentTimeMillis();
            normalizedTime = 1000 * (normalizedTime - startTime);
            normalizedTime = normalizedTime / duratioin;
            normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);
            
            float interpolation = interpolator.getInterpolation(normalizedTime / 1000f);
            final int delta = Math.round((valuefrom - valueto)* interpolation);
            this.value = valuefrom - delta;
            if (smoothable.onSmooth(value,interpolation,valuefrom, valueto) == false)
            {
                running = false;
                this.handler.removeCallbacks(this);
            }
        }

        // If we're not at the target Y, keep going...
        if (running && valueto != value)
        {
            handler.postDelayed(this, ANIMATION_FPS);
        }
        else
        {
        	smoothable.onFinish(valuefrom, valueto);
        }
    }

    public void stop()
    {
        this.running = false;
        this.handler.removeCallbacks(this);
    }

    public void start()
    {
        this.running = true;
        this.handler.post(this);
    }

}
