package com.andframe.util;

import com.andframe.helper.android.AfGifHelper;
import com.andframe.helper.android.AfGifHelper.GifFrame;

import java.io.InputStream;

public class GifUtil
{
    /**
     * 解码GIF图片
     * @param is
     * @return GifFrame
     */
    public static GifFrame[] getGif(InputStream is) {
        AfGifHelper gifHelper = new AfGifHelper();
        if (AfGifHelper.STATUS_OK == gifHelper.read(is)) {
            return gifHelper.getFrames();
        }
        return null;
    }
    /**
     * 判断图片是否为GIF格式
     * @param is
     * @return 是否为GIF格式
     */
    public static boolean isGif(InputStream is) {
        AfGifHelper gifHelper = new AfGifHelper();
        return gifHelper.isGif(is);
    }

}
