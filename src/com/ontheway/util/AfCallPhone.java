package com.ontheway.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ontheway.application.AfExceptionHandler;

public class AfCallPhone {

	public static void call(Context context, String phone) {
		// TODO Auto-generated method stub
		try {
			AfIntent intent = new AfIntent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + phone));
			context.startActivity(intent);
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();//handled
			AfExceptionHandler.handler(e, "CallPhoneUtil，call 抛出异常 被过滤");
		}
	}

}
