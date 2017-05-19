package com.andpack.fragment.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andframe.$;
import com.andframe.annotation.lifecycle.OnDestroy;
import com.andframe.annotation.view.BindViewCreated;
import com.andframe.api.pager.Pager;
import com.andframe.feature.AfIntent;
import com.andpack.fragment.ApFragment;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 通用图片选择页面
 * Created by SCWANG on 2017/5/18.
 */
public class ApPickerImageFragment extends ApFragment {


    private static final int REQUEST_IMAGE = 100;

    //<editor-fold desc="页面传递">
    public interface SelectedListener {
    }
    public interface SingleSelectedListener extends SelectedListener {
        void onSelected(File item);
    }
    public interface MultiSelectedListner extends SelectedListener {
        void onSelected(Collection<File> items);
    }

    private static SelectedListener selectedListener;

    public static void select(Pager pager, SingleSelectedListener listener) {
        selectedListener = listener;
        pager.startFragment(ApPickerImageFragment.class);
    }
    public static void selectMulti(Pager pager, MultiSelectedListner listener) {
        selectedListener = listener;
        pager.startFragment(ApPickerImageFragment.class);
    }
    //</editor-fold>

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return new View(inflater.getContext());
    }

    @OnDestroy
    public void onDestory() {
        selectedListener = null;
    }

    @BindViewCreated
    public void onCreate() {
        if (selectedListener == null) {
            finish();
            return;
        }
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setMultiMode(selectedListener instanceof MultiSelectedListner);
        if (imagePicker.getImageLoader() == null) {
            imagePicker.setImageLoader(new ImageLoader() {
                public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
                    $(imageView).image(path, width, height);
                }
                public void clearMemoryCache() {}
            });
        }
        startActivityForResult(ImageGridActivity.class,REQUEST_IMAGE);
    }
    /**
     * 页面跳转数据返回接受和处理
     * @param intent      Intent 的子类 支持对象持久化
     * @param requestcode 请求码
     * @param resultcode  返回码
     */
    @Override
    protected void onActivityResult(final AfIntent intent, int requestcode, int resultcode) {
        if (requestcode == REQUEST_IMAGE) {
            if (intent != null) {
                //noinspection unchecked
                List<ImageItem> images = (ArrayList<ImageItem>) intent.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    if (selectedListener instanceof MultiSelectedListner) {
                        ((MultiSelectedListner) selectedListener).onSelected($.query(images).map(i -> new File(i.path)));
                    } else if (selectedListener instanceof SingleSelectedListener){
                        ((SingleSelectedListener) selectedListener).onSelected(new File(images.get(0).path));
                    }
                } else {
                    makeToastShort("没有选择数据");
                }
            } else {
                makeToastShort("没有选择数据");
            }
            finish();
            finish();
        }
    }

}
