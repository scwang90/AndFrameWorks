package com.ontheway.application;

import android.content.Context;
import android.os.Vibrator;

public class AfVibratorConsole {
	
	private static Vibrator mVibrator;
	
	public static void initialize(Context context) {
		String server = Context.VIBRATOR_SERVICE;
		mVibrator = (Vibrator)context.getSystemService(server);
	}
	
	public static void vibrator(){
		long[] pattern = {100,200,100,200};// 停止 开启 停止 开启   
		mVibrator.vibrate(pattern,-1);     //重复两次上面的pattern 如果只想震动一次，index设为-1
	}
}
