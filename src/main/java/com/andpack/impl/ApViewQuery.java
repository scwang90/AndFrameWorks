package com.andpack.impl;

import android.widget.ImageView;

import com.andframe.api.viewer.Viewer;
import com.andframe.impl.viewer.DefaultViewQuery;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 *
 * Created by SCWANG on 2016/8/15.
 */
public class ApViewQuery extends DefaultViewQuery<ApViewQuery> {

    public ApViewQuery(Viewer view) {
        super(view);
    }

    @Override
    public ApViewQuery image(String url) {
        if (url != null && url.length() > 0) {
            if (url.startsWith("/")) {
                url = "file://" + url;
            }
            String furl = url;
            return foreach(ImageView.class, (view)-> {
                ImageLoader.getInstance().displayImage(furl, view);
            });
        }
        return self();
    }

    @Override
    public ApViewQuery image(String url, int widthPx, int heightPx) {
        if (url != null && url.length() > 0) {
            if (url.startsWith("/")) {
                url = "file://" + url;
            }
            String furl = url;
            return foreach(ImageView.class, (view)-> {
                ImageLoader.getInstance().displayImage(furl, view, new ImageSize(widthPx, heightPx));
            });
        }
        return self();
    }
}
