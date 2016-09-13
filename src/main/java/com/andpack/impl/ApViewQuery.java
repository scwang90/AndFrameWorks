package com.andpack.impl;

import android.view.View;
import android.widget.ImageView;

import com.andframe.feature.AfViewQuery;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 *
 * Created by SCWANG on 2016/8/15.
 */
public class ApViewQuery extends AfViewQuery<ApViewQuery> {

    public ApViewQuery(View view) {
        super(view);
    }

    @Override
    public ApViewQuery image(String url) {
        if (url != null) {
            if (url.startsWith("/")) {
                url = "file://" + url;
            }
            String furl = url;
            return foreach(ImageView.class, (view)-> {
                ImageLoader.getInstance().displayImage(furl,view);
            });
        }
        return self();
    }

}
