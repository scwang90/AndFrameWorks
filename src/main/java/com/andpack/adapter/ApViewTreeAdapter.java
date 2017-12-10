package com.andpack.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andframe.$;
import com.andframe.R;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.util.java.AfReflecter;
import com.andframe.widget.treeview.AfTreeEstablisher;
import com.andframe.widget.treeview.AfTreeViewAdapter;
import com.andframe.widget.treeview.AfTreeViewItemViewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static android.R.drawable.sym_def_app_icon;
import static android.R.id.icon;
import static android.R.id.text1;
import static android.R.id.text2;
import static android.widget.LinearLayout.VERTICAL;
import static com.andframe.R.color.colorTextContent;
import static com.andframe.R.color.colorTextTitle;

/**
 * View 控件 树形展开适配器
 * Created by SCWANG on 2016/9/12.
 */
public class ApViewTreeAdapter extends AfTreeViewAdapter<View> {

    public ApViewTreeAdapter(View root) {
        super(new ArrayList<>(Collections.singletonList(root)),
              new AfTreeEstablisher<>(model -> Arrays.asList($.query(model).children())),
                true);
    }

    @Override
    protected AfTreeViewItemViewer<View> newTreeViewItem(int viewType) {
        return new AfTreeViewItemViewer<View>(android.R.layout.activity_list_item) {
            @Override
            public View onCreateView(ViewGroup parent, Context context) {
                LinearLayout root = new LinearLayout(context);
                ViewQuery<? extends ViewQuery> $$ = $.query(root);
                TextView title = new TextView(context);
                TextView content = new TextView(context);
                ImageView image = new ImageView(context);
                LinearLayout ll = new LinearLayout(context);
                $$.$(image).id(icon).size(40f, 40f).margin(5f);
                $$.$(title).id(text1).singleLine(true).textColorId(colorTextTitle).textSizeId(R.dimen.textsize_small);
                $$.$(content).id(text2).margin(0,5f,0,0).textColorId(colorTextContent).textSizeId(R.dimen.textsize_content);
                $$.$(ll).orientation(VERTICAL).padding(5f).addView(title).addView(content);
                $$.$(root).backgroundColor(0xfff9f9f9).padding(5f).addView(image).addView(ll);
                return root;
            }

            @Override
            protected boolean onBinding(View model, int index, int level, boolean isExpanded, SelectStatus status) {
                if (model instanceof TextView) {
                    $(text1).text(String.format("\"%s\" (%s)", ((TextView) model).getText(), model.getClass().getSimpleName()));
                } else {
                    $(text1).text(model.getClass().getSimpleName());
                }
                $(text2).text(buildSizeInfo(model) + "\n" + model.toString());
                if (model instanceof ImageView) {
                    Drawable drawable = AfReflecter.getMemberNoException(model, "mDrawable", Drawable.class);
                    if (drawable != null) {
                        $(icon).image(drawable);
                    } else {
                        $(icon).image(sym_def_app_icon);
                    }
                } else {
                    $(icon).image(sym_def_app_icon);
                }
                return false;
            }
        };
    }

    protected String buildSizeInfo(View model) {
        ViewGroup.LayoutParams lp = model.getLayoutParams();
        return "params = " + lp.width + "x" + lp.height + "  size = " + model.getWidth() + "x" + model.getHeight();
    }
}
