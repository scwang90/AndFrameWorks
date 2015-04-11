package com.andframe.helper.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
/**
 * @Description: 图片处理
 * @Author: scwang
 */
public class AfImageHelper {

	/**
	 * @Description: Drawable 转换 Bitmap
	 * @param drawable
	 * @return
	 */
	public Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? 
								Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	/**
	 * 转换图片灰色
	 * @param context
	 * @param bitmap 传入BitmapDrawable对象
	 * @return 直接返回原有 BitmapDrawable 不会复制新的 BitmapDrawable
	 */
	public BitmapDrawable toGrayBitmap(Context context,
			BitmapDrawable bitmap) {
		bitmap.mutate();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
		bitmap.setColorFilter(cf);
		return bitmap;
	}

	/**
	 * 转换图片成圆形
	 * @param bitmap  传入Bitmap对象
	 * @param recycle 是否释放bitmap对象
	 * @return 返回新的bitmap
	 */
	public Bitmap toRoundBitmap(Bitmap bitmap,boolean recycle) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		if (recycle) {
			bitmap.recycle();
		}
		return output;
	}

	/**
	 * 转换图片成圆角形
	 * @param bitmap 传入Bitmap对象
	 * @param recycle 是否释放bitmap对象
	 * @return 返回新的bitmap
	 */
	public Bitmap toRoundCornerBitmap(Bitmap bitmap,boolean recycle) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 8;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 8;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		if (recycle) {
			bitmap.recycle();
		}
		return output;
	}

	public BitmapDrawable toRoundBitmap(Context context,
			BitmapDrawable bitmap,boolean recycle) {
		// TODO Auto-generated method stub
		return new BitmapDrawable(context.getResources(), toRoundBitmap(bitmap.getBitmap(),recycle));
	}

	public BitmapDrawable toRoundCornerBitmap(Context context,
			BitmapDrawable bitmap,boolean recycle) {
		// TODO Auto-generated method stub
		return new BitmapDrawable(context.getResources(), toRoundCornerBitmap(bitmap.getBitmap(),recycle));
	}
}
