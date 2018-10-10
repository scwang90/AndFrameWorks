package com.andframe.impl.viewer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.andframe.api.viewer.ViewQuery;
import com.andframe.api.viewer.Viewer;
import com.andframe.application.AfApp;
import com.andframe.listener.SafeListener;
import com.andframe.util.android.AfMeasure;
import com.andframe.util.java.AfDateFormat;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static android.support.v4.content.ContextCompat.getDrawable;

/**
 * 安卓版 JQuery 实现
 * Created by SCWANG on 2016/8/18.
 */
@SuppressWarnings("WeakerAccess")
public class AfViewQuery<T extends AfViewQuery<T>> implements ViewQuery<T> {

    protected Viewer mRootView ;
    protected View[] mTargetViews ;
    protected SparseArray<View> mCacheArray = null;

    public AfViewQuery(Viewer view) {
        mRootView = view;
        mTargetViews = new View[]{view.getView()};
    }

    //<editor-fold desc="实现基础">

    public View getRootView() {
        return mRootView.getView();
    }

    public View findViewById(int id) {
        if (mCacheArray != null) {
            View view = mCacheArray.get(id);
            if (view != null) {
                return view;
            }
        }
        if (mRootView != null) {
            View view = mRootView.findViewById(id);
            if (view != null && mCacheArray != null) {
                mCacheArray.append(id, view);
            }
            return view;
        }
        return null;
    }

    public Context getContext() {
        if (mRootView != null) {
            return mRootView.getContext();
        }
        return null;
    }

    @Override
    public Viewer rootViewer() {
        return mRootView;
    }

    @Override
    public Viewer rootViewer(@NonNull Viewer viewer) {
        Viewer old = mRootView;
        mRootView = viewer;
        return old;
    }

    protected T self() {
        //noinspection unchecked
        return (T) this;
    }

    protected T redirect() {
        if (mTargetViews.length == 1 && mTargetViews[0] != null) {
            //noinspection unchecked
            return (T) AfApp.get().newViewQuery(new ViewerWrapper(mTargetViews[0]));
        }
        return self();
    }

    //</editor-fold>

    //<editor-fold desc="选择器">

    //<editor-fold desc="基本选择">
    @Override
    public T query(Integer id, int... ids) {
        if (id != null) {
            this.mTargetViews = new View[ids.length + 1];
            this.mTargetViews[0] = findViewById(id);
            for (int i = 0; i < ids.length; i++) {
                mTargetViews[i + 1] = findViewById(ids[i]);
            }
        } else if (ids.length == 0) {
            mTargetViews = new View[]{getRootView()};
        } else {
            this.mTargetViews = new View[ids.length];
            for (int i = 0; i < ids.length; i++) {
                mTargetViews[i] = findViewById(ids[i]);
            }
        }
        return redirect();
    }

    @Override
    public T query(String idValue, String... idValues) {
        if (mRootView == null || mRootView.getView() == null) {
            return self();
        }
        List<Integer> listId = new ArrayList<>(idValues.length + 1);
        Context context = getContext();
        String packageName = context.getPackageName();
        Resources resources = context.getResources();
        if (idValue != null) {
            listId.add(resources.getIdentifier(idValue,"id",packageName));
        }
        if (idValues.length > 0) {
            for (String value : idValues) {
                listId.add(resources.getIdentifier(value, "id", packageName));
            }
        } else if (idValue == null) {
            mTargetViews = new View[]{getRootView()};
            return self();
        }
        int[] ids = new int[listId.size() - 1];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = listId.get(i + 1);
        }
        return query(listId.get(0), ids);
    }

    @Override
    public T query(Class<? extends View> type) {
        if (mRootView == null || mRootView.getView() == null) {
            return self();
        }
        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(mRootView.getView()));
        List<View> list = new ArrayList<>();
        do {
            View view = views.poll();
            if (view != null) {
                if (type != null && type.isInstance(view)) {
                    list.add(view);
                }
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    for (int j = 0; j < group.getChildCount(); j++) {
                        views.add(group.getChildAt(j));
                    }
                }
            }
        } while (!views.isEmpty());
        mTargetViews = list.toArray(new View[list.size()]);
        return redirect();
    }

    @Override@SafeVarargs
    public final T query(Class<? extends View>... types) {
        if (mRootView == null || mRootView.getView() == null) {
            return self();
        }
        List<View> list = new ArrayList<>();
        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(mRootView.getView()));
        while (!views.isEmpty() && types.length > 0) {
            View view = views.poll();
            if (view != null) {
                for (Class<?> clazz : types) {
                    if (clazz.isInstance(view)) {
                        list.add(view);
                        break;
                    }
                }
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    for (int j = 0; j < group.getChildCount(); j++) {
                        views.add(group.getChildAt(j));
                    }
                }
            }
        }
        mTargetViews = list.toArray(new View[list.size()]);
        return redirect();
    }

    @Override
    public T with(View... views) {
        if (views.length == 0) {
            mTargetViews = new View[]{getRootView()};
        } else {
            mTargetViews = views;
        }
        return redirect();
    }

    @Override
    public T with(Collection<View> views) {
        if (views.size() == 0) {
            mTargetViews = new View[]{getRootView()};
        } else {
            mTargetViews = views.toArray(new View[views.size()]);
        }
        return redirect();
    }

    //</editor-fold>

    //<editor-fold desc="选择变换">
    @Override
    public T toPrev() {
        if (mTargetViews != null) {
            for (int i = 0; i < mTargetViews.length; i++) {
                if (mTargetViews[i] != null && mTargetViews[i].getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) mTargetViews[i].getParent();
                    int index = parent.indexOfChild(mTargetViews[i]);
                    if (index > 0) {
                        mTargetViews[i] = parent.getChildAt(index - 1);
                    } else {
                        mTargetViews[i] = null;
                    }
                } else {
                    mTargetViews[i] = null;
                }
            }
        }
        return redirect();
    }

    @Override
    public T toNext() {
        if (mTargetViews != null) {
            for (int i = 0; i < mTargetViews.length; i++) {
                if (mTargetViews[i] != null && mTargetViews[i].getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) mTargetViews[i].getParent();
                    int index = parent.indexOfChild(mTargetViews[i]);
                    if (index < parent.getChildCount()) {
                        mTargetViews[i] = parent.getChildAt(index + 1);
                    } else {
                        mTargetViews[i] = null;
                    }
                } else {
                    mTargetViews[i] = null;
                }
            }
        }
        return redirect();
    }

    @Override
    public T toChild(int... index) {
        if (mTargetViews != null) {
            for (int i = 0, position = index.length == 0 ? 0 : index[0]; i < mTargetViews.length; i++) {
                if (mTargetViews[i] instanceof ViewGroup) {
                    ViewGroup view = (ViewGroup) mTargetViews[i];
                    if (view.getChildCount() > position) {
                        mTargetViews[i] = view.getChildAt(position);
                    } else {
                        mTargetViews[i] = null;
                    }
                } else {
                    mTargetViews[i] = null;
                }
            }
        }
        return redirect();
    }

    @Override
    public T toChildren() {
        return with(children());
    }

    @Override
    public T toChildrenTree() {
        return with(childrenTree());
    }

    @Override
    public T toParent() {
        if (mTargetViews != null) {
            for (int i = 0; i < mTargetViews.length; i++) {
                if (mTargetViews[i] != null) {
                    mTargetViews[i] = (View) mTargetViews[i].getParent();
                }
            }
        }
        return redirect();
    }

    @Override
    public T toRoot() {
        mTargetViews = new View[]{getRootView()};
        return self();
    }
    //</editor-fold>

    //<editor-fold desc="合并选择">
    @Override
    public T mixView(View... views) {
        if (views.length > 0) {
            View[] origins = mTargetViews;
            mTargetViews = new View[origins.length + views.length];
            for (int i = 0; i < mTargetViews.length; i++) {
                if (i < views.length) {
                    mTargetViews[i] = views[i];
                } else {
                    mTargetViews[i] = origins[i - views.length];
                }
            }
        }
        return self();
    }

    @Override
    public T mixPrev() {
        View[] views = Arrays.copyOf(this.mTargetViews, this.mTargetViews.length);
        return toPrev().mixView(views);
    }

    @Override
    public T mixNext() {
        View[] views = Arrays.copyOf(this.mTargetViews, this.mTargetViews.length);
        return toNext().mixView(views);
    }

    @Override
    public T mixChild(int... index) {
        View[] views = Arrays.copyOf(this.mTargetViews, this.mTargetViews.length);
        return toChild(index).mixView(views);
    }

    @Override
    public T mixChildren() {
        View[] views = Arrays.copyOf(this.mTargetViews, this.mTargetViews.length);
        return toChildren().mixView(views);
    }

    @Override
    public T mixChildrenTree() {
        View[] views = Arrays.copyOf(this.mTargetViews, this.mTargetViews.length);
        return toChildrenTree().mixView(views);
    }

    @Override
    public T mixParent() {
        View[] views = Arrays.copyOf(this.mTargetViews, this.mTargetViews.length);
        return toParent().mixView(views);
    }
    //</editor-fold>

    //<editor-fold desc="选择遍历">
    @Override
    public T foreach(ViewIterator<View> iterator) {
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null) {
                    iterator.each(view);
                }
            }
        }
        return self();
    }

    @Override
    public <TT> T foreach(Class<TT> clazz, ViewIterator<TT> iterator) {
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null && clazz.isInstance(view)) {
                    iterator.each(clazz.cast(view));
                }
            }
        }
        return self();
    }

    @Override
    public <TTT> TTT foreach(ViewReturnIterator<View, TTT> iterator) {
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null) {
                    TTT ret = iterator.each(view);
                    if (ret != null) {
                        return ret;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public <TT, TTT> TTT foreach(Class<TT> clazz, ViewReturnIterator<TT, TTT> iterator) {
        return foreach(clazz, iterator, null);
    }

    @Override
    public <TT, TTT> TTT foreach(Class<TT> clazz, ViewReturnIterator<TT, TTT> iterator, TTT defValue) {
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null && clazz.isInstance(view)) {
                    TTT ret = iterator.each(clazz.cast(view));
                    if (ret != null) {
                        return ret;
                    }
                }
            }
        }
        return defValue;
    }

    @Override
    public T cacheIdEnable(boolean enable) {
        if (mCacheArray != null && !enable) {
            mCacheArray.clear();
            mCacheArray = null;
        } else if (enable && mCacheArray == null) {
            mCacheArray = new SparseArray<>();
        }
        return self();
    }

    @Override
    public T clearIdCache() {
        if (mCacheArray != null) {
            mCacheArray.clear();
        }
        return self();
    }
    //</editor-fold>

    //<editor-fold desc="选择获取">

    public boolean exist() {
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int queryCount() {
        int count = 0;
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null) {
                    count++;
                }
            }
        }
        return count;
    }

    public View getView(int... index) {
        if (mTargetViews != null && mTargetViews.length > 0) {
            if (index != null && index.length > 0 && index[0] < mTargetViews.length) {
                return mTargetViews[index[0]];
            } else {
                return mTargetViews[0];
            }
        }
        return null;
    }

    @Nullable
    public <TT extends View> TT  view(int... index) {
        //noinspection unchecked
        return (TT)getView(index);
    }

    @Override
    public View[] views() {
        return mTargetViews == null ? new View[0] : mTargetViews;
    }

    @Override
    public <TT extends View> TT[] views(Class<TT> clazz,TT[] tt) {
        List<TT> list = new ArrayList<>();
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null && clazz.isInstance(view)) {
                    list.add(clazz.cast(view));
                }
            }
        }
        //noinspection unchecked
        return list.toArray(tt);
    }

    @Override
    public <TT extends View> TT view(Class<TT> clazz, int... index) {
        View view = getView(index);
        if (view != null && clazz.isInstance(view)) {
            return clazz.cast(view);
        }
        return null;
    }
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="所有View">

    //<editor-fold desc="基本设置">

    public T id(int id) {
        return foreach((ViewIterator<View>) view -> view.setId(id));
    }

    public T tag(Object tag) {
        return foreach((ViewIterator<View>) (view) -> view.setTag(tag));
    }

    public T tag(int key, Object tag) {
        return foreach((ViewIterator<View>) (view) -> view.setTag(key, tag));
    }

    public T enabled(boolean enabled) {
        return foreach((ViewIterator<View>) (view) -> view.setEnabled(enabled));
    }

    @Override
    public T focusable(boolean focusable) {
        return foreach((ViewIterator<View>) (view) -> view.setFocusable(focusable));
    }

    @Override
    public T focusable(int focusable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return foreach((ViewIterator<View>) (view) -> view.setFocusable(focusable));
        }
        return self();
    }

    @Override
    public T requestFocus() {
        return foreach(View.class, (ViewIterator<View>) View::requestFocus);
    }

    @Override
    public T clearFocus() {
        return foreach(View.class, View::clearFocus);
    }

    @Override
    public T clickable(boolean clickable) {
        return foreach((ViewIterator<View>) (view) -> view.setClickable(clickable));
    }

    @Override
    public T visibility(int visibility) {
        return foreach((ViewIterator<View>) (view) -> view.setVisibility(visibility));
    }

    @Override
    @SuppressLint("ResourceType")
    public T background(@DrawableRes int id) {
        return foreach((view) -> {
            if (id > 0) {
                view.setBackgroundResource(id);
            } else {
                //noinspection deprecation
                view.setBackgroundDrawable(null);
            }
        });
    }

    @Override
    public T background(Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16) {
            //noinspection deprecation
            return foreach((ViewIterator<View>) view -> view.setBackgroundDrawable(drawable));
        } else {
            return foreach((ViewIterator<View>) view -> view.setBackground(drawable));
        }
    }

    @Override
    public T backgroundColor(int color) {
        return foreach((ViewIterator<View>) (view) -> view.setBackgroundColor(color));
    }

    @Override
    public T animation(Animation animation) {
        return foreach((ViewIterator<View>) view -> view.setAnimation(animation));
    }

    @Override
    public T startAnimation(Animation animation) {
        return foreach((ViewIterator<View>) view -> view.startAnimation(animation));
    }

    @Override
    public T layoutParams(LayoutParams params) {
        return foreach((ViewIterator<View>) view -> view.setLayoutParams(params));
    }

    @Override
    public T layoutGravity(int gravity) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof FrameLayout.LayoutParams) {
                ((FrameLayout.LayoutParams) lp).gravity = gravity;
            } else if (lp instanceof DrawerLayout.LayoutParams) {
                ((DrawerLayout.LayoutParams) lp).gravity = gravity;
            } else if (lp instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) lp).gravity = gravity;
            } else if (lp instanceof LinearLayoutCompat.LayoutParams) {
                ((LinearLayoutCompat.LayoutParams) lp).gravity = gravity;
            } else if (lp instanceof android.support.v7.widget.Toolbar.LayoutParams) {
                ((android.support.v7.widget.Toolbar.LayoutParams) lp).gravity = gravity;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (lp instanceof Toolbar.LayoutParams) {
                    ((Toolbar.LayoutParams) lp).gravity = gravity;
                }
            }
            view.setLayoutParams(lp);
        });
    }

    @Override
    public T onKey(View.OnKeyListener listener) {
        return foreach((ViewIterator<View>) view -> view.setOnKeyListener(listener));
    }

    @Override
    public T elevation(float elevation) {
        return foreach((ViewIterator<View>) view -> ViewCompat.setElevation(view, elevation));
    }

    //</editor-fold>

    //<editor-fold desc="基本获取">

    @Override
    public int id() {
        return foreach(View::getId);
    }

    @Override
    public Object tag() {
        return foreach((ViewReturnIterator<View,Object>) View::getTag);
    }

    @Override
    public Object tag(int key) {
        return foreach((ViewReturnIterator<View, Object>) view -> view.getTag(key));
    }

    @Override
    public LayoutParams layoutParams() {
        return foreach(View::getLayoutParams);
    }

    @Override
    public float elevation() {
        return foreach(ViewCompat::getElevation);
    }

    @Override
    public boolean clickable() {
        return foreach(View::isClickable);
    }

    @Override
    public Drawable background() {
        return foreach(View::getBackground);
    }

    //</editor-fold>

    //<editor-fold desc="扩展设置">

    @Override
    public T gone() {
        return visibility(View.GONE);
    }

    @Override
    public T invisible() {
        return visibility(View.INVISIBLE);
    }

    @Override
    public T visible() {
        return visibility(View.VISIBLE);
    }

    @Override
    public T gone(boolean gone) {
        return visibility(gone ? View.GONE : View.VISIBLE);
    }

    @Override
    public T visible(boolean isvisibe) {
        return visibility(isvisibe ? View.VISIBLE : View.GONE);
    }

    @Override
    public T invisible(boolean invisible) {
        return visibility(invisible ? View.INVISIBLE : View.VISIBLE);
    }

    //</editor-fold>

    //<editor-fold desc="扩展获取">

    @Override
    public boolean isVisible() {
        return foreach(view -> view.getVisibility() == View.VISIBLE);
    }

    @Override
    public Point measure() {
        return foreach(AfMeasure::measureView);
    }

    @Override
    public Rect padding() {
        return foreach(view -> {
            return new Rect(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        });
    }

    @Override
    public Rect margin() {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams params = (MarginLayoutParams) lp;
                return new Rect(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
            }
            return null;
        });
    }

    @Override
    public Bitmap screenshot() {
        return foreach(view -> {
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            return bitmap;
        });
    }

    //</editor-fold>

    //<editor-fold desc="尺寸边距">

    //<editor-fold desc="尺寸">
    @Override
    public T width(int width) {
        size(true, width, false);
        return self();
    }

    @Override
    public T width(float dp) {
        size(true, dp, true);
        return self();
    }

    @Override
    public T minWidth(int px) {
        return foreach((ViewIterator<View>) view -> view.setMinimumWidth(px));
    }

    @Override
    public T minWidth(float dp) {
        return foreach(view -> {
            float scale = getContext().getResources().getDisplayMetrics().density;
            int px = (int) (scale * dp + 0.5f);
            view.setMinimumWidth(px);
        });
    }

    @Override
    public T height(int height) {
        return size(false, height, false);
    }

    @Override
    public T height(float dp) {
        size(false, dp, true);
        return self();
    }

    @Override
    public T minHeight(int px) {
        return foreach((ViewIterator<View>) view -> view.setMinimumHeight(px));
    }

    @Override
    public T minHeight(float dp) {
        return foreach(view -> {
            float scale = getContext().getResources().getDisplayMetrics().density;
            int px = (int) (scale * dp + 0.5f);
            view.setMinimumHeight(px);
        });
    }

    @Override
    public T size(int width, int height) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp != null) {
                lp.width = width;
                lp.height = height;
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T size(float width, float height) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp != null) {
                float scale = getContext().getResources().getDisplayMetrics().density;
                lp.width = (int) (scale * width + 0.5f);
                lp.height = (int) (scale * height + 0.5f);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public int width() {
        return foreach(View::getWidth);
    }

    @Override
    public int height() {
        return foreach(View::getHeight);
    }

    @Override
    @TargetApi(16)
    public int minWidth() {
        return foreach(View::getMinimumWidth);
    }

    @Override
    @TargetApi(16)
    public int minHeight() {
        return foreach(View::getMinimumHeight);
    }

    private T size(boolean width, float n, boolean dp) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp != null) {
                Context context = getContext();
                float tn = n;
                if (tn > 0 && dp) {
                    float scale = context.getResources().getDisplayMetrics().density;
                    tn = (int) (scale * tn + 0.5f);
                }
                if (width) {
                    lp.width = (int)tn;
                } else {
                    lp.height = (int)tn;
                }
                view.setLayoutParams(lp);
            }
        });
    }

    //</editor-fold>

    //<editor-fold desc="外边距">
    @Override
    public T margin(Rect rectPx) {
        return margin(rectPx.left, rectPx.top, rectPx.right, rectPx.bottom);
    }

    @Override
    public T margin(RectF rectDp) {
        return margin(rectDp.left, rectDp.top, rectDp.right, rectDp.bottom);
    }

    @Override
    public T margin(float dp) {
        return margin(dp,dp,dp,dp);
    }

    @Override
    public T margin(int px) {
        return margin(px,px,px,px);
    }

    @Override
    public T margin(int left, int top, int right, int bottom) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                ((MarginLayoutParams) lp).setMargins(left, top, right, bottom);
                view.setLayoutParams(lp);
            }
        });
    }
    @Override
    public T margin(float leftDp, float topDp, float rightDp, float bottomDp) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                Context context = getContext();
                float scale = context.getResources().getDisplayMetrics().density;
                int left = (int) (scale * leftDp + 0.5f);
                int top = (int) (scale * topDp + 0.5f);
                int right = (int) (scale * rightDp + 0.5f);
                int bottom = (int) (scale * bottomDp + 0.5f);
                ((MarginLayoutParams) lp).setMargins(left, top, right, bottom);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public int marginLeft() {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                return ((MarginLayoutParams) lp).leftMargin;
            }
            return 0;
        });
    }

    @Override
    public int marginRight() {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                return ((MarginLayoutParams) lp).rightMargin;
            }
            return 0;
        });
    }

    @Override
    public int marginTop() {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                return ((MarginLayoutParams) lp).topMargin;
            }
            return 0;
        });
    }

    @Override
    public int marginBottom() {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                return ((MarginLayoutParams) lp).bottomMargin;
            }
            return 0;
        });
    }

    @Override
    public T marginLeft(int px) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams params = (MarginLayoutParams) lp;
                params.setMargins(px, params.topMargin, params.rightMargin, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginRight(int px) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams params = (MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, params.topMargin, px, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginTop(int px) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams params = (MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, px, params.rightMargin, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginBottom(int px) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams params = (MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, px);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginLeft(float dp) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                float scale = getContext().getResources().getDisplayMetrics().density;
                int px = (int) (scale * dp + 0.5f);
                MarginLayoutParams params = (MarginLayoutParams) lp;
                params.setMargins(px, params.topMargin, params.rightMargin, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginRight(float dp) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                float scale = getContext().getResources().getDisplayMetrics().density;
                int px = (int) (scale * dp + 0.5f);
                MarginLayoutParams params = (MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, params.topMargin, px, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginTop(float dp) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                float scale = getContext().getResources().getDisplayMetrics().density;
                int px = (int) (scale * dp + 0.5f);
                MarginLayoutParams params = (MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, px, params.rightMargin, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginBottom(float dp) {
        return foreach(view -> {
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                float scale = getContext().getResources().getDisplayMetrics().density;
                int px = (int) (scale * dp + 0.5f);
                MarginLayoutParams params = (MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, px);
                view.setLayoutParams(lp);
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="内边距">
    @Override
    public T padding(Rect rectPx) {
        return padding(rectPx.left, rectPx.top, rectPx.right, rectPx.bottom);
    }

    @Override
    public T padding(RectF rectDp) {
        return padding(rectDp.left, rectDp.top, rectDp.right, rectDp.bottom);
    }

    @Override
    public T padding(float dp) {
        return padding(dp,dp,dp,dp);
    }

    @Override
    public T paddingRes(@DimenRes int dimen) {
        return paddingRes(dimen, dimen, dimen, dimen);
    }

    @Override
    public T padding(int px) {
        return padding(px,px,px,px);
    }

    @Override
    public T padding(float leftDp, float topDp, float rightDp, float bottomDp) {
        return foreach(view -> {
            float scale = getContext().getResources().getDisplayMetrics().density;
            int left = (int) (scale * leftDp + 0.5f);
            int top = (int) (scale * topDp + 0.5f);
            int right = (int) (scale * rightDp + 0.5f);
            int bottom = (int) (scale * bottomDp + 0.5f);
            view.setPadding(left, top, right, bottom);
        });
    }

    @Override
    public int paddingLeft() {
        return foreach(View::getPaddingLeft);
    }

    @Override
    public int paddingRight() {
        return foreach(View::getPaddingRight);
    }

    @Override
    public int paddingTop() {
        return foreach(View::getPaddingTop);
    }

    @Override
    public int paddingBottom() {
        return foreach(View::getPaddingBottom);
    }

    @Override
    public T padding(int left, int top, int right, int bottom) {
        return foreach((ViewIterator<View>) view -> view.setPadding(left, top, right, bottom));
    }

    @Override
    public T paddingRes(@DimenRes int left, @DimenRes int top, @DimenRes int right, @DimenRes int bottom) {
        return foreach(view -> {
            Resources resources = view.getResources();
            view.setPadding(resources.getDimensionPixelOffset(left),
                    resources.getDimensionPixelOffset(top),
                    resources.getDimensionPixelOffset(right),
                    resources.getDimensionPixelOffset(bottom));
        });
    }

    @Override
    public T paddingLeft(int px) {
        return foreach((ViewIterator<View>) view -> view.setPadding(px, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()));
    }

    @Override
    public T paddingRight(int px) {
        return foreach((ViewIterator<View>) view -> view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), px, view.getPaddingBottom()));
    }

    @Override
    public T paddingTop(int px) {
        return foreach((ViewIterator<View>) view -> view.setPadding(view.getPaddingLeft(), px, view.getPaddingRight(), view.getPaddingBottom()));
    }

    @Override
    public T paddingBottom(int px) {
        return foreach((ViewIterator<View>) view -> view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), px));
    }

    @Override
    public T paddingLeftRes(@DimenRes int dimen) {
        return foreach(view -> {
            int padding = view.getResources().getDimensionPixelOffset(dimen);
            view.setPadding(padding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        });
    }

    @Override
    public T paddingRightRes(@DimenRes int dimen) {
        return foreach(view -> {
            int padding = view.getResources().getDimensionPixelOffset(dimen);
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), padding, view.getPaddingBottom());
        });
    }
    @Override
    public T paddingTopRes(@DimenRes int dimen) {
        return foreach(view -> {
            int padding = view.getResources().getDimensionPixelOffset(dimen);
            view.setPadding(view.getPaddingLeft(), padding, view.getPaddingRight(), view.getPaddingBottom());
        });
    }
    @Override
    public T paddingBottomRes(@DimenRes int dimen) {
        return foreach(view -> {
            int padding = view.getResources().getDimensionPixelOffset(dimen);
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
        });
    }

    @Override
    public T paddingLeft(float dp) {
        return foreach(view -> {
            float scale = getContext().getResources().getDisplayMetrics().density;
            int px = (int) (scale * dp + 0.5f);
            view.setPadding(px, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        });
    }

    @Override
    public T paddingRight(float dp) {
        return foreach(view -> {
            float scale = getContext().getResources().getDisplayMetrics().density;
            int px = (int) (scale * dp + 0.5f);
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), px, view.getPaddingBottom());
        });
    }

    @Override
    public T paddingTop(float dp) {
        return foreach(view -> {
            float scale = getContext().getResources().getDisplayMetrics().density;
            int px = (int) (scale * dp + 0.5f);
            view.setPadding(view.getPaddingLeft(), px, view.getPaddingRight(), view.getPaddingBottom());
        });
    }

    @Override
    public T paddingBottom(float dp) {
        return foreach(view -> {
            float scale = getContext().getResources().getDisplayMetrics().density;
            int px = (int) (scale * dp + 0.5f);
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), px);
        });
    }
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="视图变换">

    @Override
    public float alpha() {
        return foreach(View.class, View::getAlpha);
    }
    @Override
    public float scaleX() {
        return foreach(View.class, View::getScaleX);
    }
    @Override
    public float getScaleY() {
        return foreach(View.class, View::getScaleY);
    }
    @Override
    public float rotationX() {
        return foreach(View.class, View::getRotationX);
    }
    @Override
    public float rotationY() {
        return foreach(View.class, View::getRotationY);
    }
    @Override
    public float rotation() {
        return foreach(View.class, View::getRotation);
    }

    @Override
    public int x() {
        return foreach(View.class, (View view) -> (int) view.getX());
    }
    @Override
    public int y() {
        return foreach(View.class, (View view) -> (int) view.getY());
    }
    @Override
    public int z() {
        return foreach(View.class, (View view) -> (int) ViewCompat.getZ(view));
    }
    @Override
    public int scrollX() {
        return foreach(View.class, View::getScrollX);
    }
    @Override
    public int scrollY() {
        return foreach(View.class, View::getScrollY);
    }
    @Override
    public int translationX() {
        return foreach(View.class, (View view) -> (int) view.getTranslationX());
    }
    @Override
    public int translationY() {
        return foreach(View.class, (View view) -> (int) view.getTranslationY());
    }
    @Override
    public int translationZ() {
        return foreach(View.class, (View view) -> (int) ViewCompat.getTranslationZ(view));
    }
    @Override
    public int pivotX() {
        return foreach(View.class, (View view) -> (int) view.getPivotX());
    }
    @Override
    public int pivotY() {
        return foreach(View.class, (View view) -> (int) view.getPivotY());
    }


    @Override
    public T scrollX(int x) {
        if (Build.VERSION.SDK_INT >= 14) {
            return foreach((ViewIterator<View>) view -> view.setScrollX(x));
        } else {
            return foreach((ViewIterator<View>) view -> view.scrollTo(x, view.getScrollY()));
        }
    }

    @Override
    public T scrollY(int y) {
        if (Build.VERSION.SDK_INT >= 14) {
            return foreach((ViewIterator<View>) view -> view.setScrollY(y));
        } else {
            return foreach((ViewIterator<View>) view -> view.scrollTo(view.getScrollX(), y));
        }
    }

    @Override
    public T scrollX(float x) {
        if (Build.VERSION.SDK_INT >= 14) {
            return foreach((ViewIterator<View>) view -> view.setScrollX((int)(x * Resources.getSystem().getDisplayMetrics().density + 0.5f)));
        } else {
            return foreach((ViewIterator<View>) view -> view.scrollTo((int)(x * Resources.getSystem().getDisplayMetrics().density + 0.5f), view.getScrollY()));
        }
    }

    @Override
    public T scrollY(float y) {
        if (Build.VERSION.SDK_INT >= 14) {
            return foreach((ViewIterator<View>) view -> view.setScrollY((int)(y * Resources.getSystem().getDisplayMetrics().density + 0.5f)));
        } else {
            return foreach((ViewIterator<View>) view -> view.scrollTo(view.getScrollX(), (int)(y * Resources.getSystem().getDisplayMetrics().density + 0.5f)));
        }
    }

    @Override
    public T scaleX(float x) {
        return foreach((ViewIterator<View>) view -> view.setScaleX(x));
    }

    @Override
    public T scaleY(float y) {
        return foreach((ViewIterator<View>) view -> view.setScaleY(y));
    }

    @Override
    public T translationX(int x) {
        return foreach((ViewIterator<View>) view -> view.setTranslationX(x));
    }

    @Override
    public T translationY(int y) {
        return foreach((ViewIterator<View>) view -> view.setTranslationY(y));
    }

    @Override
    public T translationX(float x) {
        return foreach((ViewIterator<View>) view -> view.setTranslationX(x * Resources.getSystem().getDisplayMetrics().density + 0.5f));
    }

    @Override
    public T translationY(float y) {
        return foreach((ViewIterator<View>) view -> view.setTranslationY(y * Resources.getSystem().getDisplayMetrics().density + 0.5f));
    }

    @Override
    public T translationZ(int y) {
        return foreach((ViewIterator<View>) view -> ViewCompat.setTranslationZ(view, y));
    }

    @Override
    public T translationZ(float y) {
        return foreach((ViewIterator<View>) view -> ViewCompat.setTranslationZ(view, y * Resources.getSystem().getDisplayMetrics().density + 0.5f));
    }

    @Override
    public T x(int x) {
        return foreach((ViewIterator<View>) view -> view.setX(x));
    }

    @Override
    public T y(int y) {
        return foreach((ViewIterator<View>) view -> view.setY(y));
    }

    @Override
    public T z(int z) {
        return foreach((ViewIterator<View>) view -> ViewCompat.setZ(view, z));
    }

    @Override
    public T x(float x) {
        return foreach((ViewIterator<View>) view -> view.setX(x * Resources.getSystem().getDisplayMetrics().density + 0.5f));
    }

    @Override
    public T y(float y) {
        return foreach((ViewIterator<View>) view -> view.setY(y * Resources.getSystem().getDisplayMetrics().density + 0.5f));
    }

    @Override
    public T z(float z) {
        return foreach((ViewIterator<View>) view -> ViewCompat.setZ(view, z * Resources.getSystem().getDisplayMetrics().density + 0.5f));
    }

    @Override
    public T pivotX(int x) {
        return foreach((ViewIterator<View>) view -> view.setPivotX(x));
    }

    @Override
    public T pivotY(int y) {
        return foreach((ViewIterator<View>) view -> view.setPivotY(y));
    }

    @Override
    public T pivotX(float x) {
        return foreach((ViewIterator<View>) view -> view.setPivotX(x * Resources.getSystem().getDisplayMetrics().density + 0.5f));
    }

    @Override
    public T pivotY(float y) {
        return foreach((ViewIterator<View>) view -> view.setPivotY(y * Resources.getSystem().getDisplayMetrics().density + 0.5f));
    }

    @Override
    public T rotationX(float x) {
        return foreach((ViewIterator<View>) view -> view.setRotationX(x));
    }

    @Override
    public T rotationY(float y) {
        return foreach((ViewIterator<View>) view -> view.setRotationY(y));
    }

    @Override
    public T rotation(float rotation) {
        return foreach((ViewIterator<View>) view -> view.setRotation(rotation));
    }

    @Override
    public T alpha(float x) {
        return foreach((ViewIterator<View>) view -> view.setAlpha(x));
    }

    @Override
    public T requestLayout() {
        return foreach(View::requestLayout);
    }

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="TextView">

    //<editor-fold desc="基本设置">
    @Override
    public T text(@StringRes int resId) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(resId));
    }

    @Override
    public T text(@StringRes int resId, Object... formatArgs) {
        Context context = getContext();
        if (context != null) {
            CharSequence text = context.getString(resId, formatArgs);
            text(text);
        }
        return self();
    }

    @Override
    public T text(CharSequence text) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(text));
    }

    @Override
    public T hint(CharSequence hint) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setHint(hint));
    }

    @Override
    public T hint(int hintId) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setHint(hintId));
    }

    @Override
    public T inputType(int type) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setInputType(type));
    }
    @Override
    public T textSizeId(@DimenRes int id) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setTextSize(TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimension(id)));
    }

    @Override
    public T textColor(ColorStateList color) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setTextColor(color));
    }
    @Override
    public T textColorListId(@ColorRes int id) {
        return textColor(ContextCompat.getColorStateList(getContext(), id));
    }

    @Override
    public T textColor(int color) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setTextColor(color));
    }

    @Override
    public T textColorId(@ColorRes int id) {
        return textColor(ContextCompat.getColor(getContext(), id));
    }

    @Override
    public T textSize(float size) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setTextSize(size));
    }

    @Override
    public T textSize(int type, float size) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setTextSize(type,size));
    }

    @Override
    public T maxLines(int lines) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setMaxLines(lines));
    }

    @Override
    public T singleLine(boolean... value) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setSingleLine(value.length == 0 || value[0]));
    }

    @Override
    public T typeface(Typeface typeface) {
        try {
            return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setTypeface(typeface));
        } catch (Throwable e) {
            e.printStackTrace();
            return self();
        }
    }

    @Override
    public T typeface(File typefaceFile) {
        if (typefaceFile.exists()) {
            try {
                Typeface typeface = Typeface.createFromFile(typefaceFile);
                return typeface(typeface);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return self();
    }

    @Override
    public T typeface(String typefacePath) {
        return typeface(new File(typefacePath));
    }

    @Override
    public T shadowLayer(float radius, float dx, float dy, int color) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setShadowLayer(radius, dx, dy, color));
    }

    //</editor-fold>

    //<editor-fold desc="基本获取">

    public String text() {
        return foreach(TextView.class, (ViewReturnIterator<TextView, String>) view -> view.getText().toString());
    }

    public String hint() {
        return foreach(TextView.class, (ViewReturnIterator<TextView, String>) view -> view.getHint().toString());
    }

    //</editor-fold>

    //<editor-fold desc="扩展设置">

    @Override
    public T text(TextConverter converter) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(converter.convert(view.getText().toString())));
    }

    @Override
    public T textGoneIfEmpty(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return gone();
        } else {
            if (!isVisible()) {
                visible();
            }
            return text(text);
        }
    }

    @Override
    public T text(CharSequence format, Object... args) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(String.format(format+"", args)));
    }

    @Override
    public T textElse(CharSequence text, CharSequence defValue) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(TextUtils.isEmpty(text) ? defValue : text));
    }

    @Override
    public T hint(CharSequence format, Object... args) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setHint(String.format(format+"", args)));
    }

    @Override
    public T html(String format, Object... args) {
        if (args.length == 0) {
            //noinspection deprecation
            foreach(WebView.class, (ViewIterator<WebView>) (view) -> view.loadData(format+"","text/html;charset=UTF-8",null));
            return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(Html.fromHtml(format+"")));
        }
        Context context = null;
        for (int i = 0, len = format.length(), index = 0; i < len; i++) {
            if (format.charAt(i) == '%' && i < len - 1) {
                if (format.charAt(i + 1) == 's') {
                    if (index < args.length && args[index] instanceof Integer) {
                        int color = ((Integer) args[index]);
                        try {
                            if (context == null) {
                                context = getContext();
                            }
                            if (context != null) {
                                color = ContextCompat.getColor(context, color);
                            }
                        } catch (Resources.NotFoundException ignored) {
                        }
                        args[index] = Integer.toHexString(0x00FFFFFF & color);
                    }
                }
                i++;
                index++;
            }
        }
        //noinspection deprecation
        foreach(WebView.class, (ViewIterator<WebView>) (view) -> view.loadData(String.format(format+"", args),"text/html;charset=UTF-8",null));
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(Html.fromHtml(String.format(format, args))));
    }

    @Override
    public T textFormat(String format) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(String.format(format+"", view.getText())));
    }
    //</editor-fold>

    //<editor-fold desc="时间设置">
    @Override
    public T time(Date time) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.TIME.format(time)));
    }

    @Override
    public T time(@NonNull String format, Date time) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.format(format, time)));
    }

    @Override
    public T time(@NonNull DateFormat format, Date time) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(time == null ? "" : format.format(time)));
    }

    @Override
    public T timeDay(Date time) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.DAY.format(time)));
    }

    @Override
    public T timeDate(Date time) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.DATE.format(time)));
    }

    @Override
    public T timeFull(Date time) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.FULL.format(time)));
    }

    @Override
    public T timeSimple(Date time) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.SIMPLE.format(time)));
    }

    @Override
    public T timeStandard(Date time) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.STANDARD.format(time)));
    }

    @Override
    public T timeDynamic(Date time) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(AfDateFormat.formatTime(time)));
    }

    @Override
    public T timeDynamicDate(Date time) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(AfDateFormat.formatDate(time)));
    }

    @Override
    public T timeDynamicDateTime(Date time) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(AfDateFormat.formatDateTime(time)));
    }

    @Override
    public T timeSpan(Date start, Date close, @NonNull String split, String... formats) {
        return foreach(TextView.class, (ViewIterator<TextView>) (view) -> view.setText(AfDateFormat.formatTimeSpan(start, close, split, formats)));
    }
    //</editor-fold>

    //<editor-fold desc="复合图片">
    @Override
    public T drawablePadding(int padding) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.setCompoundDrawablePadding(padding));
    }

    @Override
    public T drawablePadding(float padding) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.setCompoundDrawablePadding((int) (getContext().getResources().getDisplayMetrics().density * padding + 0.5f)));
    }

    @Override
    public T drawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.setCompoundDrawables(left, top, right, bottom));
    }

    @Override
    public T drawableLeft(Drawable drawable) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.setCompoundDrawables(drawable, null, null, null));
    }

    @Override
    public T drawableTop(Drawable drawable) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.setCompoundDrawables(null, drawable, null, null));
    }

    @Override
    public T drawableRight(Drawable drawable) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.setCompoundDrawables(null, null, drawable, null));
    }

    @Override
    public T drawableBottom(Drawable drawable) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.setCompoundDrawables(null, null, null, drawable));
    }

    @Override
    public T drawableLeft(@DrawableRes int id) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.setCompoundDrawablesWithIntrinsicBounds(getDrawable(getContext(),id), null, null, null));
    }

    @Override
    public T drawableTop(@DrawableRes int id) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(getContext(),id), null, null));
    }

    @Override
    public T drawableRight(@DrawableRes int id) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(getContext(),id), null));
    }

    @Override
    public T drawableBottom(@DrawableRes int id) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(getContext(),id)));
    }

    @Nullable
    @Override
    public Drawable drawableLeft() {
        return foreach(TextView.class, view -> view.getCompoundDrawables()[0]);
    }

    @Nullable
    @Override
    public Drawable drawableTop() {
        return foreach(TextView.class, view -> view.getCompoundDrawables()[1]);
    }

    @Nullable
    @Override
    public Drawable drawableRight() {
        return foreach(TextView.class, view -> view.getCompoundDrawables()[2]);
    }

    @Nullable
    @Override
    public Drawable drawableBottom() {
        return foreach(TextView.class, view -> view.getCompoundDrawables()[3]);
    }

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="列表控件">

    //<editor-fold desc="基本设置">
    @SuppressWarnings({"unchecked"})
    public T adapter(Adapter adapter) {
        return foreach(AdapterView.class, (ViewIterator<AdapterView>) (view) -> view.setAdapter(adapter));
    }

    public T adapter(ExpandableListAdapter adapter) {
        return foreach(ExpandableListView.class, (ViewIterator<ExpandableListView>) (view) -> view.setAdapter(adapter));
    }

    public T setSelection(int position) {
        return foreach(AdapterView.class, (ViewIterator<AdapterView>) view -> view.setSelection(position));
    }

    public T dataChanged() {
        return foreach(AdapterView.class, (view) -> {
            Adapter a = view.getAdapter();
            if (a instanceof BaseAdapter) {
                ((BaseAdapter) a).notifyDataSetChanged();
            }
        });
    }

    //</editor-fold>

    //<editor-fold desc="基本获取">
    public Object getSelectedItem() {
        return foreach(AdapterView.class, view -> {
            //noinspection Convert2MethodRef
            return view.getSelectedItem();
        });
    }

    public int getSelectedItemPosition() {
        return foreach(AdapterView.class, view -> {
            //noinspection Convert2MethodRef
            return view.getSelectedItemPosition();
        });
    }
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="ImageView">

    public T image(int resId) {
        return foreach(ImageView.class, (view) -> {
            if (resId == 0) {
                view.setImageBitmap(null);
            } else {
                view.setImageResource(resId);
            }
        });
    }

    public T image(Drawable drawable) {
        return foreach(ImageView.class, (ViewIterator<ImageView>) (view) -> view.setImageDrawable(drawable));
    }

    public T image(Bitmap bm) {
        return foreach(ImageView.class, (ViewIterator<ImageView>) (view) -> view.setImageBitmap(bm));
    }

    public T image(String url) {
        if (url != null && url.length() > 0) {
            return foreach(ImageView.class, (ViewIterator<ImageView>) (view) -> view.setImageURI(Uri.parse(url)));
        }
        return self();
    }

    @Override
    public T image(String url, int widthpx, int heightpx) {
        if (url != null && url.length() > 0) {
            return foreach(ImageView.class, (ViewIterator<ImageView>) (view) -> view.setImageURI(Uri.parse(url)));
        }
        return self();
    }

    @Override
    public T image(String url, int resId) {
        return image(resId).image(url);
    }

    @Override
    public T avatar(String url) {
        return image(url);
    }

    @Override
    public T scaleType(ImageView.ScaleType scaleType) {
        return foreach(ImageView.class, (ViewIterator<ImageView>) (view) -> view.setScaleType(scaleType));
    }

    @Override
    public T imageTintColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return foreach(ImageView.class, (ViewIterator<ImageView>) (view) -> view.setImageTintList(ColorStateList.valueOf(color)));
        }
        return self();
    }

    @Override
    public T imageTintColorId(@ColorRes int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return foreach(ImageView.class, (ViewIterator<ImageView>) (view) -> view.setImageTintList(ContextCompat.getColorStateList(getContext(),colorId)));
        }
        return self();
    }

    //</editor-fold>

    //<editor-fold desc="EditText">

    public Editable editable() {
        return foreach(EditText.class, EditText::getEditableText);
    }

    @Override
    public T selection(int start, int stop) {
        return foreach(EditText.class, (ViewIterator<EditText>) view -> view.setSelection(start, stop));
    }

    @Override
    public T selection(int index) {
        return foreach(EditText.class, (ViewIterator<EditText>) view -> view.setSelection(index));
    }

    @Override
    public T selectAll() {
        return foreach(EditText.class, EditText::selectAll);
    }

    @Override
    public T extendSelection(int index) {
        return foreach(EditText.class, (ViewIterator<EditText>) view -> view.extendSelection(index));
    }

    @Override
    public T selectionToEnd() {
        return foreach(EditText.class, editText -> {
            editText.setSelection(editText.getText().length());
        });
    }

    @Override
    public int selectionEnd() {
        return foreach(EditText.class, EditText::getSelectionEnd);
    }

    @Override
    public int selectionStart() {
        return foreach(EditText.class, EditText::getSelectionStart);
    }

    //</editor-fold>

    //<editor-fold desc="RatingBar">
    @Override
    public float rating() {
        return foreach(RatingBar.class, RatingBar::getRating);
    }
    public T rating(float rating) {
        return foreach(RatingBar.class, (ViewIterator<RatingBar>) (view) -> view.setRating(rating));
    }
    //</editor-fold>

    //<editor-fold desc="ProgressBar">
    @Override
    public T progress(int progress) {
        return foreach(ProgressBar.class,(ViewIterator<ProgressBar>) (view) -> view.setProgress(progress));
    }

    @Override
    public int progress() {
        return foreach(ProgressBar.class, ProgressBar::getProgress);
    }

    @Override
    public int max() {
        return foreach(ProgressBar.class, ProgressBar::getMax);
    }

    @Override
    public T secondaryProgress(int secondaryProgress) {
        return foreach(ProgressBar.class,(ViewIterator<ProgressBar>) (view) -> view.setSecondaryProgress(secondaryProgress));
    }

    @Override
    public int secondaryProgress() {
        return foreach(ProgressBar.class, ProgressBar::getSecondaryProgress);
    }

    @Override
    public T max(int max) {
        return foreach(ProgressBar.class,(ViewIterator<ProgressBar>) (view) -> view.setMax(max));
    }
    //</editor-fold>

    //<editor-fold desc="CompoundButton">
    @Override
    public T toggle() {
        return foreach(CompoundButton.class,(ViewIterator<CompoundButton>) (view) -> view.setChecked(!view.isChecked()));
    }

    public T checked(boolean checked) {
        return foreach(CompoundButton.class, (ViewIterator<CompoundButton>) (view) -> view.setChecked(checked));
    }

    @Override
    public boolean isChecked() {
        return foreach(CompoundButton.class, CompoundButton::isChecked);
    }

    //</editor-fold>

    //<editor-fold desc="LinearLayout">
    @Override
    public int orientation() {
        return foreach(LinearLayout.class, LinearLayout::getOrientation);
    }

    @Override
    public T orientation(@LinearLayoutCompat.OrientationMode int orientation) {
        return foreach(LinearLayout.class, (ViewIterator<LinearLayout>) view -> view.setOrientation(orientation));
    }
    //</editor-fold>

    //<editor-fold desc="事件绑定">

    @Override
    public T clicked(View.OnClickListener listener) {
        return foreach((ViewIterator<View>) view -> view.setOnClickListener(listener==null?null:new SafeListener(listener)));
    }

    @Override
    public T clicked(View.OnClickListener listener, int intervalTime) {
        return foreach((ViewIterator<View>) view -> view.setOnClickListener(listener==null?null:new SafeListener(listener, intervalTime)));
    }

    @Override
    public T longClicked(View.OnLongClickListener listener) {
        return foreach((ViewIterator<View>) view -> view.setOnLongClickListener(listener==null?null:new SafeListener(listener)));
    }

    @Override
    public T itemClicked(AdapterView.OnItemClickListener listener) {
        return foreach(AdapterView.class, (ViewIterator<AdapterView>) view -> view.setOnItemClickListener(listener));
    }

    @Override
    public T itemLongClicked(AdapterView.OnItemLongClickListener listener) {
        return foreach(AdapterView.class, (ViewIterator<AdapterView>) view -> view.setOnItemLongClickListener(listener));
    }

    @Override
    public T textChanged(TextWatcher method) {
        return foreach(TextView.class, (ViewIterator<TextView>) view -> view.addTextChangedListener(method));
    }

    @Override
    public T checkChanged(CompoundButton.OnCheckedChangeListener listener) {
        return foreach(CompoundButton.class, (ViewIterator<CompoundButton>) view -> view.setOnCheckedChangeListener(listener));
    }

    @Override
    public T radioChanged(RadioGroup.OnCheckedChangeListener listener) {
        return foreach(RadioGroup.class, (ViewIterator<RadioGroup>) view -> view.setOnCheckedChangeListener(listener));
    }

    //</editor-fold>

    //<editor-fold desc="共用方法">
    @Override
    public int gravity() {
        return foreach(view -> {
            if (view instanceof TextView) {
                return ((TextView) view).getGravity();
            } else if (view instanceof RelativeLayout && Build.VERSION.SDK_INT > 16) {
                return ((RelativeLayout) view).getGravity();
            } else if (view instanceof LinearLayout && Build.VERSION.SDK_INT > 23) {
                return ((LinearLayout) view).getGravity();
            }
            return -1;
        });
    }

    @Override
    public T gravity(int gravity) {
        return foreach(view -> {
            if (view instanceof TextView) {
                ((TextView) view).setGravity(gravity);
            } else if (view instanceof RelativeLayout) {
                ((RelativeLayout) view).setGravity(gravity);
            } else if (view instanceof LinearLayout) {
                ((LinearLayout) view).setGravity(gravity);
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="ViewPager">
    @Override
    public T adapter(PagerAdapter adapter) {
        return foreach(ViewPager.class, (ViewIterator<ViewPager>) (view) -> view.setAdapter(adapter));
    }

    @Override
    public T currentItem(int item) {
        return foreach(ViewPager.class, (ViewIterator<ViewPager>) (view) -> view.setCurrentItem(item));
    }

    @Override
    public T currentItem(int item, boolean smoothScroll) {
        return foreach(ViewPager.class, (ViewIterator<ViewPager>) (view) -> view.setCurrentItem(item, smoothScroll));
    }

    @Override
    public T pageChanged(ViewPager.OnPageChangeListener listener) {
        return foreach(ViewPager.class, (ViewIterator<ViewPager>) (view) -> view.addOnPageChangeListener(listener));
    }

    @Override
    public T pageSelected(OnPageSelectedListener listener) {
        return pageChanged(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                listener.onPageSelected(position);
            }
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public int currentItem() {
        return foreach(ViewPager.class, ViewPager::getCurrentItem);
    }

    //</editor-fold>

    //<editor-fold desc="容器布局">

    //<editor-fold desc="基本操作">

    @Override
    public T removeView(View view) {
        return foreach(ViewGroup.class, (ViewIterator<ViewGroup>) group -> group.removeView(view));
    }

    @Override
    public T removeViewAt(int index) {
        return foreach(ViewGroup.class, (ViewIterator<ViewGroup>) group -> group.removeViewAt(index));
    }

    @Override
    public T removeViews(int start, int count) {
        return foreach(ViewGroup.class, (ViewIterator<ViewGroup>) group -> group.removeViews(start, count));
    }

    @Override
    public T removeViewInLayout(View view) {
        return foreach(ViewGroup.class, (ViewIterator<ViewGroup>) group -> group.removeViewInLayout(view));
    }

    @Override
    public T removeAllViews() {
        return foreach(ViewGroup.class, ViewGroup::removeAllViews);
    }

    @Override
    public T removeViewsInLayout(int start, int count) {
        return foreach(ViewGroup.class, (ViewIterator<ViewGroup>) group -> group.removeViewsInLayout(start, count));
    }

    @Override
    public T removeAllViewsInLayout() {
        return foreach(ViewGroup.class, ViewGroup::removeAllViewsInLayout);
    }

    @Override
    public T clipToPadding(boolean clip) {
        return foreach(ViewGroup.class, (ViewIterator<ViewGroup>) view -> view.setClipToPadding(clip));
    }

    @Override
    public T clipChildren(boolean clip) {
        return foreach(ViewGroup.class, (ViewIterator<ViewGroup>) view -> view.setClipChildren(clip));
    }

    //</editor-fold>

    //<editor-fold desc="添加子控件">
    @Override
    public T addView(View... views) {
        return foreach(ViewGroup.class, group -> {
            for (View view : views) {
                ViewParent parent = view.getParent();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(view);
                }
                group.addView(view);
            }
        });
    }

    @Override
    public T addView(View view, int index) {
        return foreach(ViewGroup.class, group -> {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
            group.addView(view, index);
        });
    }

    @Override
    public T addView(View view, int width, int height) {
        return foreach(ViewGroup.class, group -> {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
            group.addView(view, width, height);
        });
    }

    @Override
    public T addView(View view, float widthDp, float heightDp) {
        final float density = Resources.getSystem().getDisplayMetrics().density;
        final int width = widthDp > 0 ? ((int) (widthDp * density + 0.5f)) : (int)widthDp;
        final int height = heightDp > 0 ? ((int) (heightDp * density + 0.5f)) : (int)heightDp;
        return foreach(ViewGroup.class, group -> {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
            group.addView(view, width, height);
        });
    }

    @Override
    public T addView(View view, LayoutParams params) {
        return foreach(ViewGroup.class, group -> {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
            group.addView(view, params);
        });
    }

    @Override
    public T addView(View view, int index, LayoutParams params) {
        return foreach(ViewGroup.class, group -> {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
            group.addView(view, index, params);
        });
    }
    //</editor-fold>

    //<editor-fold desc="获取子控件">
    @Override
    public int childCount() {
        return foreach(ViewGroup.class, ViewGroup::getChildCount);
    }

    @Override
    public View childAt(int index) {
        return foreach(ViewGroup.class, (ViewReturnIterator<ViewGroup, View>) view -> view.getChildAt(index));
    }

    @Override
    public View[] children() {
        return foreach(ViewGroup.class, view -> {
            View[] views = new View[view.getChildCount()];
            for (int i = 0; i < views.length; i++) {
                views[i] = view.getChildAt(i);
            }
            return views;
        }, new View[0]);
    }

    @Override
    public View[] childrenTree() {
        return foreach(ViewGroup.class, view -> {
            List<View> children = new ArrayList<>();
            Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(view));
            while (!views.isEmpty()) {
                View cview = views.poll();
                if (cview != view) {
                    children.add(cview);
                }
                if (cview instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) cview;
                    for (int j = 0; j < group.getChildCount(); j++) {
                        views.add(group.getChildAt(j));
                    }
                }
            }
            return children.toArray(new View[children.size()]);
        }, new View[0]);
    }

    //</editor-fold>

    //<editor-fold desc="分离操作">
    @Override
    public View[] breakChildren() {
        return with(children()).foreach(view -> {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
        }).views();
    }

    @Nullable
    @Override
    public View breakView() {
        return foreach(view -> {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
                return view;
            }
            return null;
        });
    }

    @Override
    public View[] breakViews() {
        return foreach(view -> {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
        }).views();
    }
    //</editor-fold>

    //<editor-fold desc="替换操作">

    @Override
    public T replace(View target) {
        return foreach(view -> {
            ViewGroup parent = (ViewGroup)view.getParent();
            if (parent != null) {
                ViewParent viewParent = target.getParent();
                if (viewParent instanceof ViewGroup) {
                    ((ViewGroup) viewParent).removeView(target);
                }
                int i = parent.indexOfChild(view);
                parent.removeViewAt(i);
                parent.addView(target, i, view.getLayoutParams());
            }
        });
    }
    //</editor-fold>

    //</editor-fold>

}