package com.andframe.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andframe.$;
import com.andframe.R;
import com.andframe.api.view.ViewQuery;
import com.andframe.application.AfApp;
import com.andframe.impl.viewer.ViewerWarpper;
import com.andframe.util.java.AfReflecter;
import com.andframe.widget.treeview.AfTreeEstablisher;
import com.andframe.widget.treeview.AfTreeViewAdapter;
import com.andframe.widget.treeview.AfTreeViewItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * View 控件 树形展开适配器
 * Created by SCWANG on 2016/9/12.
 */
public class AfViewTreeAdapter extends AfTreeViewAdapter<View> {

    public AfViewTreeAdapter(View root) {
        super(root.getContext(),
                new ArrayList<>(Collections.singletonList(root)),
                new AfTreeEstablisher<>(model -> Arrays.asList($.query(model).childs())),
                true);
    }

    @Override
    protected AfTreeViewItem<View> newTreeViewItem() {
        return new AfTreeViewItem<View>() {
            @Override
            public View onCreateView(Context context, ViewGroup parent) {
                LinearLayout root = new LinearLayout(context);
                ViewQuery<? extends ViewQuery> $ = AfApp.get().newViewQuery(new ViewerWarpper(root));
                $.backgroundColor(0xfff9f9f9).padding(5f);
                ImageView image = new ImageView(context);
                image.setId(android.R.id.icon);
                $.$(image).height(40f).width(40f).margin(5f);
                LinearLayout ll = new LinearLayout(context);
                ll.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(context);
                title.setId(android.R.id.text1);
                TextView content = new TextView(context);
                content.setId(android.R.id.text2);
                $.$(title).setSingleLine(true).textColorId(R.color.colorTextTitle).textSizeId(R.dimen.textsize_small);
                $.$(content).margin(0,5f,0,0).textColorId(R.color.colorTextContent).textSizeId(R.dimen.textsize_content);
                $.$(ll).padding(5f).addView(title).addView(content);
                $.$(root).addView(image).addView(ll);
                return root;
            }

            @Override
            protected boolean onBinding(View model, int index, int level, boolean isExpanded, SelectStatus status) {
                if (model instanceof TextView) {
                    $(android.R.id.text1).text(String.format("\"%s\" (%s)", ((TextView) model).getText(), model.getClass().getSimpleName()));
                } else {
                    $(android.R.id.text1).text(model.getClass().getSimpleName());
                }
                $(android.R.id.text2).text(buildSizeInfo(model) + "\n" + model.toString());
                if (model instanceof ImageView) {
                    Drawable drawable = AfReflecter.getMemberNoException(model, "mDrawable", Drawable.class);
                    if (drawable != null) {
                        $(android.R.id.icon).image(drawable);
                    } else {
                        $(android.R.id.icon).image(android.R.drawable.sym_def_app_icon);
                    }
                } else {
                    $(android.R.id.icon).image(android.R.drawable.sym_def_app_icon);
                }
                return false;
            }
        };
    }

    protected String buildSizeInfo(View model) {
        ViewGroup.LayoutParams lp = model.getLayoutParams();
        return "params = " + lp.width + "x" + lp.height+"  size = " + model.getWidth()+"x"+model.getHeight();
    }
}
