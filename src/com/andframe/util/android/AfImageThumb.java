package com.andframe.util.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.widget.ImageView;

import com.andframe.application.AfApplication;
import com.andframe.caches.AfImageCaches;
import com.andframe.feature.AfDensity;
import com.andframe.thread.AfHandlerTask;

import java.io.IOException;
/**
 * 图片压缩处理类
 * @author 树朾
 */
public class AfImageThumb {

	/**
	 *  绑定 image 到 ImageView
	 * 	使用文件缓存，和多线程，提高绑定数据书记杜
	 *  图片大小适配屏幕，防止内存异常
	 * @author 树朾
	 * @param iv
	 * @param image
	 */
	public static void bindImage(final ImageView iv, final String image) {
		AfImageCaches caches = AfImageCaches.getInstance();
		Bitmap bitmap = caches.get(image);
		if (bitmap == null) {
			AfApplication.postTask(new AfHandlerTask() {
				Bitmap bitmap = null;
				@Override
				protected void onWorking() throws Exception {
					AfImageCaches caches = AfImageCaches.getInstance();
					bitmap = revitionImageSize(image);
					caches.put(image, bitmap);
				}
				@Override
				protected boolean onHandle() {
					if (bitmap != null) {
						iv.setImageBitmap(bitmap);
					}
					return false;
				}
			});
		}else {
			iv.setImageBitmap(bitmap);
		}
	}

	/**
	 * 从文件加载一张不大于 屏幕/3 的图片
	 * 如果大于将按缩小到不 大于屏幕/3 (2的倍数缩小，原比例)
	 * @author 树朾
	 * @param path 文件路径
	 * @return
	 * @throws IOException
	 */
	public static Bitmap revitionImageSize(String path) {
		AfDensity density = new AfDensity(AfApplication.getApp());
		int maxw = density.getWidthPixels()/3;
		int maxh = density.getHeightPixels()/3;
		return revitionImageSize(path,maxw,maxh);
	}

	/**
	 * 从文件加载一张不大于 screenscale 的图片
	 * 如果大于将按缩小到不大于screenscale (2的倍数缩小，原比例)
	 * @author 树朾
	 * @param path 文件路径
	 * @param screenscale 最大宽度（相对手机屏幕，如0.3倍屏幕）
	 * @return
	 * @throws IOException
	 */
	public static Bitmap revitionImageSize(String path,float screenscale) {
		AfDensity density = new AfDensity(AfApplication.getApp());
		int maxw = (int) (density.getWidthPixels()*screenscale);
		int maxh = (int) (density.getHeightPixels()*screenscale);
		return revitionImageSize(path,maxw,maxh);
	}

	/**
	 * 从文件加载一张不大于 maxw maxh 的图片
	 * 如果大于将按缩小到不大于maxw maxh (2的倍数缩小，原比例)
	 * @author 树朾
	 * @param path 文件路径
	 * @param maxw 最大宽度
	 * @param maxh 最大高度
	 * @return
	 * @throws IOException
	 */
	public static Bitmap revitionImageSize(String path,int maxw,int maxh) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		for (int i = 0; i < 10 ; i++) {
			if ((options.outWidth >> i <= maxw)
					&& (options.outHeight >> i <= maxh)) {
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(path, options);
				break;
			}
		}
		return bitmap;
	}
	


	/**
	 *  可用于生成缩略图。（默认回收原图）
	 * @author 树朾
	 * @param source 原图
	 * @param width 剪裁大小
	 * @param height 剪裁大小
	 * @return 缩略图
	 */
	public static Bitmap extractMiniThumb(Bitmap source, int width, int height) {
		return extractMiniThumb(source, width, height, true);
	}

	/**
	 *  可用于生成缩略图。
	 * @author 树朾
	 * @param source 原图
	 * @param width 剪裁大小
	 * @param height 剪裁大小
	 * @param recycle 是否回收原图
	 * @return 缩略图
	 */
	public static Bitmap extractMiniThumb(Bitmap source, int width, int height,
			boolean recycle) {
		if (source == null) {
			return null;
		}

		float scale;
		if (source.getWidth() < source.getHeight()) {
			scale = width / (float) source.getWidth();
		} else {
			scale = height / (float) source.getHeight();
		}
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap miniThumbnail = transform(matrix, source, width, height, false);

		if (recycle && miniThumbnail != source) {
			source.recycle();
		}
		return miniThumbnail;
	}

	public static Bitmap transform(Matrix scaler, Bitmap source,
			int targetWidth, int targetHeight, boolean scaleUp) {
		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
					+ Math.min(targetWidth, source.getWidth()), deltaYHalf
					+ Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
					- dstY);
			c.drawBitmap(source, src, dst, null);
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}

		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), scaler, true);
		} else {
			b1 = source;
		}

		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
				targetHeight);

		if (b1 != source) {
			b1.recycle();
		}

		return b2;
	}
}
