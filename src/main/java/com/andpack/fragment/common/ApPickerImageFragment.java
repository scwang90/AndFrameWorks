package com.andpack.fragment.common;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.$;
import com.andframe.annotation.view.BindViewCreated;
import com.andframe.api.pager.Pager;
import com.andframe.feature.AfIntent;
import com.andpack.R;
import com.andpack.fragment.ApFragment;
import com.andpack.impl.ApCommonBarBinder;
import com.scwang.smartrefresh.layout.util.SmartUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        selectedListener = null;
    }

    @BindViewCreated
    public void onCreate() {
        if (selectedListener == null) {
            finish();
            return;
        }

        doCameraWithPermissionCheck(()-> Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(9)
                .theme(R.style.Matisse_Dracula)
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .gridExpectedSize(SmartUtil.dp2px(120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new ApCommonBarBinder.ImageLoaderEngine())
                .forResult(REQUEST_IMAGE));
//        ImagePicker imagePicker = ImagePicker.getInstance();
//        imagePicker.setMultiMode(selectedListener instanceof MultiSelectedListner);
//        startActivityForResult(ImageGridActivity.class,REQUEST_IMAGE);

    }
    /**
     * 页面跳转数据返回接受和处理
     * @param intent      Intent 的子类 支持对象持久化
     * @param requestCode 请求码
     * @param resultCode  返回码
     */
    @Override
    protected void onActivityResult(final AfIntent intent, int requestCode, int resultCode) {
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                List<String> images = Matisse.obtainPathResult(intent);
                if (images != null && images.size() > 0) {
                    if (selectedListener instanceof MultiSelectedListner) {
                        ((MultiSelectedListner) selectedListener).onSelected($.query(images).map(i -> new File(i)));
                    } else if (selectedListener instanceof SingleSelectedListener){
                        ((SingleSelectedListener) selectedListener).onSelected(new File(images.get(0)));
                    }
                } else {
                    toast("没有选择数据");
                }
            } else {
                toast("没有选择数据");
            }
        }
    }

}
