package com.andframe.feature;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.andframe.adapter.AfListLayoutItemAdapter;
import com.andframe.api.view.ViewQuery;
import com.andframe.api.view.Viewer;
import com.andframe.listener.SafeListener;
import com.andframe.util.android.AfMeasure;
import com.andframe.util.java.AfDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
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
public class AfViewQuery<T extends AfViewQuery<T>> implements ViewQuery<T> {

    protected Viewer mRootView = null;
    protected View[] mTargetViews = null;

    public AfViewQuery(Viewer view) {
        mRootView = view;
        mTargetViews = new View[]{view.getView()};
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

    public View getRootView() {
        return mRootView.getView();
    }

    public View findViewById(int id) {
        if (mRootView != null) {
            return mRootView.findViewById(id);
        }
        return null;
    }

    protected T self() {
        //noinspection unchecked
        return (T) this;
    }

    /**
     * Points the current operating view to the first view found with the id under the root.
     *
     * @param id the id
     * @return self
     */
    public T id(int id) {
        return foreach((ViewEacher<View>) view -> view.setId(id));
    }

    /**
     * Set the rating of a RatingBar.
     *
     * @param rating the rating
     * @return self
     */
    public T rating(float rating) {
        return foreach(RatingBar.class, (ViewEacher<RatingBar>) (view) -> view.setRating(rating));
    }


    /**
     * Set the text of a TextView.
     *
     * @param resid the resid
     * @return self
     */
    public T text(int resid) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(resid));
    }

    /**
     * Set the text of a TextView with localized formatted string
     * from application's package's default string table
     *
     * @param resid the resid
     * @return self
     * @see Context#getString(int, Object...)
     */
    public T text(int resid, Object... formatArgs) {
        Context context = getContext();
        if (context != null) {
            CharSequence text = context.getString(resid, formatArgs);
            text(text);
        }
        return self();
    }

    /**
     * Set the text of a TextView.
     *
     * @param text the text
     * @return self
     */
    public T text(CharSequence text) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(text));
    }

    /**
     * Set the text of a TextView. Hide the mTargetViews (gone) if text is empty.
     *
     * @param text        the text
     * @param goneIfEmpty hide if text is null or length is 0
     * @return self
     */

    public T text(CharSequence text, boolean goneIfEmpty) {
        if (goneIfEmpty && (text == null || text.length() == 0)) {
            return gone();
        } else {
            return text(text);
        }
    }

    /**
     * Set the text of a TextView.
     *
     * @param text the text
     * @return self
     */
    public T text(Spanned text) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(text));
    }

    /**
     * Set the text color of a TextView. Note that it's not a color resource id.
     *
     * @param color color code in ARGB
     * @return self
     */
    public T textColor(int color) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setTextColor(color));
    }

    /**
     * Set the text color of a TextView from  a color resource id.
     *
     * @param id color resource id
     * @return self
     */
    public T textColorId(int id) {
        return textColor(ContextCompat.getColor(getContext(), id));
    }


    /**
     * Set the text typeface of a TextView.
     *
     * @param typeface typeface
     * @return self
     */
    public T typeface(Typeface typeface) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setTypeface(typeface));
    }

    /**
     * Set the text size (in sp) of a TextView.
     *
     * @param size size
     * @return self
     */
    public T textSize(float size) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setTextSize(size));
    }


    /**
     * Set the adapter of an AdapterView.
     *
     * @param adapter adapter
     * @return self
     */

    @SuppressWarnings({"unchecked"})
    public T adapter(Adapter adapter) {
        return foreach(AdapterView.class, (ViewEacher<AdapterView>) (view) -> view.setAdapter(adapter));
    }

    /**
     * Set the adapter of an ExpandableListView.
     *
     * @param adapter adapter
     * @return self
     */
    public T adapter(ExpandableListAdapter adapter) {
        return foreach(ExpandableListView.class, (ViewEacher<ExpandableListView>) (view) -> view.setAdapter(adapter));
    }

    /**
     * Set the image of an ImageView.
     *
     * @param resid the resource id
     * @return self
     */
    public T image(int resid) {
        return foreach(ImageView.class, (view) -> {
            if (resid == 0) {
                view.setImageBitmap(null);
            } else {
                view.setImageResource(resid);
            }
        });
    }

    /**
     * Set the image of an ImageView.
     *
     * @param drawable the drawable
     * @return self
     */
    public T image(Drawable drawable) {
        return foreach(ImageView.class, (ViewEacher<ImageView>) (view) -> view.setImageDrawable(drawable));
    }

    /**
     * Set the image of an ImageView.
     *
     * @param bm Bitmap
     * @return self
     */
    public T image(Bitmap bm) {
        return foreach(ImageView.class, (ViewEacher<ImageView>) (view) -> view.setImageBitmap(bm));
    }


    /**
     * Set the image of an ImageView.
     *
     * @param url Image url.
     * @return self
     */

    public T image(String url) {
        return foreach(ImageView.class, (ViewEacher<ImageView>) (view) -> view.setImageURI(Uri.parse(url)));
    }


    @Override
    public T image(String url, int widthpx, int heightpx) {
        return foreach(ImageView.class, (ViewEacher<ImageView>) (view) -> view.setImageURI(Uri.parse(url)));
    }

    /**
     * Set tag object of a mTargetViews.
     *
     * @return self
     */
    public T tag(Object tag) {
        return foreach((ViewEacher<View>) (view) -> view.setTag(tag));
    }

    /**
     * Set tag object of a mTargetViews.
     *
     * @return self
     */
    public T tag(int key, Object tag) {
        return foreach((ViewEacher<View>) (view) -> view.setTag(key, tag));
    }

    /**
     * Enable a mTargetViews.
     *
     * @param enabled state
     * @return self
     */
    public T enabled(boolean enabled) {
        return foreach((ViewEacher<View>) (view) -> view.setEnabled(enabled));
    }

    /**
     * Set checked state of a compound button.
     *
     * @param checked state
     * @return self
     */
    public T checked(boolean checked) {
        return foreach(CompoundButton.class, (ViewEacher<CompoundButton>) (view) -> view.setChecked(checked));
    }

    /**
     * Get checked state of a compound button.
     *
     * @return checked
     */
    public boolean isChecked() {
        return foreach(CompoundButton.class, CompoundButton::isChecked);
    }

    /**
     * Set clickable for a mTargetViews.
     *
     * @return self
     */
    public T clickable(boolean clickable) {
        return foreach((ViewEacher<View>) (view) -> view.setClickable(clickable));
    }

    /**
     * Set mTargetViews visibility to View.GONE.
     *
     * @return self
     */
    public T gone() {
        return visibility(View.GONE);
    }

    /**
     * Set mTargetViews visibility to View.INVISIBLE.
     *
     * @return self
     */
    public T invisible() {
        return visibility(View.INVISIBLE);
    }

    /**
     * Set mTargetViews visibility to View.VISIBLE.
     *
     * @return self
     */
    public T visible() {
        return visibility(View.VISIBLE);
    }

    /**
     * Set mTargetViews visibility, such as View.VISIBLE.
     *
     * @return self
     */
    public T visibility(int visibility) {
        return foreach((ViewEacher<View>) (view) -> view.setVisibility(visibility));
    }


    /**
     * Set mTargetViews background.
     *
     * @param id the id
     * @return self
     */
    public T background(int id) {
        return foreach((view) -> {
            if (id > 0) {
                view.setBackgroundResource(id);
            } else {
                //noinspection deprecation
                view.setBackgroundDrawable(null);
            }
        });
    }

    /**
     * Set mTargetViews background color.
     *
     * @param color color code in ARGB
     * @return self
     */
    public T backgroundColor(int color) {
        return foreach((ViewEacher<View>) (view) -> view.setBackgroundColor(color));
    }

    /**
     * Notify a ListView that the data of it's adapter is changed.
     *
     * @return self
     */
    public T dataChanged() {
        return foreach(AdapterView.class, (view) -> {
            Adapter a = view.getAdapter();
            if (a instanceof BaseAdapter) {
                ((BaseAdapter) a).notifyDataSetChanged();
            }
        });
    }

    /**
     * Checks if the current mTargetViews exist.
     *
     * @return true, if is exist
     */
    public boolean isExist() {
        return mTargetViews != null && mTargetViews.length > 0;
    }

    /**
     * Gets the tag of the mTargetViews.
     *
     * @return tag
     */
    public Object getTag() {
        return foreach((ViewReturnEacher<View,Object>) View::getTag);
    }

    /**
     * Gets the tag of the mTargetViews.
     *
     * @param id the id
     * @return tag
     */

    public Object getTag(int id) {
        return foreach((ViewReturnEacher<View, Object>) view -> view.getTag(id));
    }

    /**
     * Gets the current mTargetViews as an image mTargetViews.
     *
     * @return ImageView
     */

    public ImageView getImageView() {
        return foreach(ImageView.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as an Gallery.
     *
     * @return Gallery
     */
    @SuppressWarnings("deprecation")
    public Gallery getGallery() {
        return foreach(Gallery.class, view -> view);
    }


    /**
     * Gets the current mTargetViews as a text mTargetViews.
     *
     * @return TextView
     */
    public TextView getTextView() {
        return foreach(TextView.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as an edit text.
     *
     * @return EditText
     */
    public EditText getEditText() {
        return foreach(EditText.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as an progress bar.
     *
     * @return ProgressBar
     */
    public ProgressBar getProgressBar() {
        return foreach(ProgressBar.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as seek bar.
     *
     * @return SeekBar
     */

    public SeekBar getSeekBar() {
        return foreach(SeekBar.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as a button.
     *
     * @return Button
     */
    public Button getButton() {
        return foreach(Button.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as a checkbox.
     *
     * @return CheckBox
     */
    public CheckBox getCheckBox() {
        return foreach(CheckBox.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as a listview.
     *
     * @return ListView
     */
    public ListView getListView() {
        return foreach(ListView.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as a ExpandableListView.
     *
     * @return ExpandableListView
     */
    public ExpandableListView getExpandableListView() {
        return foreach(ExpandableListView.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as a gridview.
     *
     * @return GridView
     */
    public GridView getGridView() {
        return foreach(GridView.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as a RatingBar.
     *
     * @return RatingBar
     */
    public RatingBar getRatingBar() {
        return foreach(RatingBar.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as a webview.
     *
     * @return WebView
     */
    public WebView getWebView() {
        return foreach(WebView.class, view -> view);
    }

    /**
     * Gets the current mTargetViews as a spinner.
     *
     * @return Spinner
     */
    public Spinner getSpinner() {
        return foreach(Spinner.class, view -> view);
    }

    /**
     * Gets the editable.
     *
     * @return the editable
     */
    public Editable getEditable() {
        return foreach(EditText.class, EditText::getEditableText);
    }

    /**
     * Gets the text of a TextView.
     *
     * @return the text
     */
    public CharSequence getText() {
        return foreach(TextView.class, TextView::getText);
    }

    /**
     * Gets the selected item if current mTargetViews is an adapter mTargetViews.
     *
     * @return selected
     */
    public Object getSelectedItem() {
        return foreach(AdapterView.class, view -> {
            //noinspection Convert2MethodRef
            return view.getSelectedItem();
        });
    }

    /**
     * Gets the selected item position if current mTargetViews is an adapter mTargetViews.
     * <p>
     * Returns AdapterView.INVALID_POSITION if not valid.
     *
     * @return selected position
     */
    public int getSelectedItemPosition() {
        return foreach(AdapterView.class, view -> {
            //noinspection Convert2MethodRef
            return view.getSelectedItemPosition();
        });
    }

    /**
     * Register a callback method for when the mTargetViews is clicked.
     *
     * @param listener The callback method.
     * @return self
     */
    public T clicked(View.OnClickListener listener) {
        return foreach((ViewEacher<View>) view -> view.setOnClickListener(new SafeListener(listener)));
    }

    /**
     * Register a callback method for when the mTargetViews is long clicked.
     *
     * @param listener The callback method.
     * @return self
     */
    public T longClicked(View.OnLongClickListener listener) {
        return foreach((ViewEacher<View>) view -> view.setOnLongClickListener(listener));
    }

    /**
     * Register a callback method for when an item is clicked in the ListView.
     *
     * @param listener The callback method.
     * @return self
     */
    public T itemClicked(AdapterView.OnItemClickListener listener) {
        return foreach(AdapterView.class, (ViewEacher<AdapterView>) view -> view.setOnItemClickListener(listener));
    }

    /**
     * Register a callback method for when an item is long clicked in the ListView.
     *
     * @param listener The callback method.
     * @return self
     */
    public T itemLongClicked(AdapterView.OnItemLongClickListener listener) {
        return foreach(AdapterView.class, (ViewEacher<AdapterView>) view -> view.setOnItemLongClickListener(listener));

    }

    /**
     * Set selected item of an AdapterView.
     *
     * @param position The position of the item to be selected.
     * @return self
     */
    public T setSelection(int position) {
        return foreach(AdapterView.class, (ViewEacher<AdapterView>) view -> view.setSelection(position));
    }

    /**
     * Register a callback method for when a textview text is changed. Method must have signature of method(CharSequence s, int start, int before, int count)).
     *
     * @param method The method name of the callback.
     * @return self
     */
    public T textChanged(TextWatcher method) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.addTextChangedListener(method));
    }

    /**
     * Set the margin of a mTargetViews. Notes all parameters are in DIP, not in pixel.
     *
     * @param leftDip   the left dip
     * @param topDip    the top dip
     * @param rightDip  the right dip
     * @param bottomDip the bottom dip
     * @return self
     */
    @Override
    public T margin(float leftDip, float topDip, float rightDip, float bottomDip) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                Context context = getContext();
                float scale = context.getResources().getDisplayMetrics().density;
                int left = (int) (scale * leftDip + 0.5f);
                int top = (int) (scale * topDip + 0.5f);
                int right = (int) (scale * rightDip + 0.5f);
                int bottom = (int) (scale * bottomDip + 0.5f);
                ((ViewGroup.MarginLayoutParams) lp).setMargins(left, top, right, bottom);
                view.setLayoutParams(lp);
            }
        });
    }

    /**
     * Set the width of a mTargetViews in dip.
     * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
     *
     * @param dip width in dip
     * @return self
     */

    @Override
    public T width(float dip) {
        size(true, dip, true);
        return self();
    }

    /**
     * Set the height of a mTargetViews in dip.
     * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
     *
     * @param dip height in dip
     * @return self
     */

    @Override
    public T height(float dip) {
        size(false, dip, true);
        return self();
    }

    private T size(boolean width, float n, boolean dip) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp != null) {
                Context context = getContext();
                float tn = n;
                if (tn > 0 && dip) {
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

    //<editor-fold desc="自定义">
    @Override
    public T foreach(ViewEacher<View> eacher) {
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null) {
                    eacher.each(view);
                }
            }
        }
        return self();
    }

    @Override
    public <TT> T foreach(Class<TT> clazz, ViewEacher<TT> eacher) {
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null && clazz.isInstance(view)) {
                    eacher.each(clazz.cast(view));
                }
            }
        }
        return self();
    }

    @Override
    public <TTT> TTT foreach(ViewReturnEacher<View, TTT> eacher) {
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null) {
                    TTT ret = eacher.each(view);
                    if (ret != null) {
                        return ret;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public <TT, TTT> TTT foreach(Class<TT> clazz, ViewReturnEacher<TT, TTT> eacher) {
        return foreach(clazz, eacher, null);
    }

    @Override
    public <TT, TTT> TTT foreach(Class<TT> clazz, ViewReturnEacher<TT, TTT> eacher, TTT defvalue) {
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null && clazz.isInstance(view)) {
                    TTT ret = eacher.each(clazz.cast(view));
                    if (ret != null) {
                        return ret;
                    }
                }
            }
        }
        return defvalue;
    }

    @Override
    public T $(Integer id, int... ids) {
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
        return self();
    }

    @Override
    public T $(String idvalue, String... idvalues) {
        if (mRootView == null) {
            return self();
        }
        List<Integer> listId = new ArrayList<>(idvalues.length + 1);
        Context context = getContext();
        String packageName = context.getPackageName();
        Resources resources = context.getResources();
        if (idvalue != null) {
            listId.add(resources.getIdentifier(idvalue,"id",packageName));
        }
        if (idvalues.length > 0) {
            for (String value : idvalues) {
                listId.add(resources.getIdentifier(value,"id",packageName));
            }
        }
        int[] ids = new int[listId.size() - 1];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = listId.get(i + 1);
        }
        return $(listId.get(0), ids);
    }

    @Override
    public T $(Class<? extends View> type) {
        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(mRootView.getView()));
        List<View> list = new ArrayList<>();
        do {
            View cview = views.poll();
            if (cview != null) {
                if (type != null && type.isInstance(cview)) {
                    list.add(cview);
                }
                if (cview instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) cview;
                    for (int j = 0; j < group.getChildCount(); j++) {
                        views.add(group.getChildAt(j));
                    }
                }
            }
        } while (!views.isEmpty());
        mTargetViews = list.toArray(new View[list.size()]);
        return self();
    }

    @Override
    @SafeVarargs
    public final T $(Class<? extends View>... types) {
        List<View> list = new ArrayList<>();
        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(mRootView.getView()));
        while (!views.isEmpty() && types.length > 0) {
            View cview = views.poll();
            if (cview != null) {
                for (Class<?> ttype : types) {
                    if (ttype.isInstance(cview)) {
                        list.add(cview);
                        break;
                    }
                }
                if (cview instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) cview;
                    for (int j = 0; j < group.getChildCount(); j++) {
                        views.add(group.getChildAt(j));
                    }
                }
            }
        }
        mTargetViews = list.toArray(new View[list.size()]);
        return self();
    }

    @Override
    public T $(View... views) {
        if (views.length == 0) {
            mTargetViews = new View[]{getRootView()};
        } else {
            mTargetViews = views;
        }
        return self();
    }

    public <TT extends View> TT  view(int... indexs) {
        //noinspection unchecked
        return (TT)getView(indexs);
    }

    @Override
    public View[] views() {
        return mTargetViews == null ? new View[0] : mTargetViews;
    }

    @Override
    public <TT extends View> TT[] views(Class<TT> clazz) {
        List<TT> list = new ArrayList<>();
        if (mTargetViews != null) {
            for (View view : mTargetViews) {
                if (view != null && clazz.isInstance(view)) {
                    list.add(clazz.cast(view));
                }
            }
        }
        //noinspection unchecked
        return (TT[])list.toArray();
    }

    @Override
    public <TT extends View> TT view(Class<TT> clazz, int... indexs) {
        View view = getView(indexs);
        if (view != null && clazz.isInstance(view)) {
            return clazz.cast(view);
        }
        return null;
    }

    @Override
    public ScrollView getScrollView() {
        return foreach(ScrollView.class, view -> view);
    }

    @Override
    public boolean isVisible() {
        return foreach(view -> view.getVisibility() == View.VISIBLE);
    }

    @Override
    public float rating() {
        return foreach(RatingBar.class, RatingBar::getRating);
    }

    @Override
    public int orientation() {
        return foreach(LinearLayout.class, LinearLayout::getOrientation);
    }

    @Override
    public int gravity() {
        return foreach(view -> {
            if (view instanceof TextView) {
                return ((TextView) view).getGravity();
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
            } else if (view instanceof LinearLayout && Build.VERSION.SDK_INT > 23) {
                ((LinearLayout) view).setGravity(gravity);
            }
        });
    }

    @Override
    public T maxLines(int lines) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setMaxLines(lines));
    }

    @Override
    public T singleLine(boolean... value) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setSingleLine(value.length == 0 || value[0]));
    }

    @Override
    public T orientation(@LinearLayoutCompat.OrientationMode int orientation) {
        return foreach(LinearLayout.class, (ViewEacher<LinearLayout>) view -> view.setOrientation(orientation));
    }

    @Override
    public T width(int width) {
        size(true, width, false);
        return self();
    }

    @Override
    public T height(int height) {
        return size(false, height, false);
    }

    @Override
    public T size(int width, int height) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
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
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp != null) {
                float scale = getContext().getResources().getDisplayMetrics().density;
                lp.width = (int) (scale * width + 0.5f);
                lp.height = (int) (scale * height + 0.5f);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T margin(Rect padding) {
        return margin(padding.left, padding.top, padding.right, padding.bottom);
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
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) lp).setMargins(left, top, right, bottom);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginLeft(int px) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                params.setMargins(px, params.topMargin, params.rightMargin, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginRight(int px) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, params.topMargin, px, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginTop(int px) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, px, params.rightMargin, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginBottom(int px) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, px);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginLeft(float dp) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                float scale = getContext().getResources().getDisplayMetrics().density;
                int px = (int) (scale * dp + 0.5f);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                params.setMargins(px, params.topMargin, params.rightMargin, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginRight(float dp) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                float scale = getContext().getResources().getDisplayMetrics().density;
                int px = (int) (scale * dp + 0.5f);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, params.topMargin, px, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginTop(float dp) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                float scale = getContext().getResources().getDisplayMetrics().density;
                int px = (int) (scale * dp + 0.5f);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, px, params.rightMargin, params.bottomMargin);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T marginBottom(float dp) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                float scale = getContext().getResources().getDisplayMetrics().density;
                int px = (int) (scale * dp + 0.5f);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, px);
                view.setLayoutParams(lp);
            }
        });
    }

    @Override
    public T padding(Rect padding) {
        return padding(padding.left, padding.top, padding.right, padding.bottom);
    }

    @Override
    public T padding(float dp) {
        return padding(dp,dp,dp,dp);
    }

    @Override
    public T padding(int px) {
        return padding(px,px,px,px);
    }

    @Override
    public T padding(float leftDip, float topDip, float rightDip, float bottomDip) {
        return foreach(view -> {
            float scale = getContext().getResources().getDisplayMetrics().density;
            int left = (int) (scale * leftDip + 0.5f);
            int top = (int) (scale * topDip + 0.5f);
            int right = (int) (scale * rightDip + 0.5f);
            int bottom = (int) (scale * bottomDip + 0.5f);
            view.setPadding(left, top, right, bottom);
        });
    }

    @Override
    public T padding(int left, int top, int right, int bottom) {
        return foreach((ViewEacher<View>) view -> view.setPadding(left, top, right, bottom));
    }

    @Override
    public T paddingLeft(int px) {
        return foreach((ViewEacher<View>) view -> view.setPadding(px, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()));
    }

    @Override
    public T paddingRight(int px) {
        return foreach((ViewEacher<View>) view -> view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), px, view.getPaddingBottom()));
    }

    @Override
    public T paddingTop(int px) {
        return foreach((ViewEacher<View>) view -> view.setPadding(view.getPaddingLeft(), px, view.getPaddingRight(), view.getPaddingBottom()));
    }

    @Override
    public T paddingBottom(int px) {
        return foreach((ViewEacher<View>) view -> view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), px));
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

    @Override
    public T visibility(boolean isvisibe) {
        return visibility(isvisibe ? View.VISIBLE : View.GONE);
    }

    @Override
    public T textSizeId(int id) {
        return foreach(TextView.class,(ViewEacher<TextView>) (view) -> view.setTextSize(TypedValue.COMPLEX_UNIT_PX,view.getResources().getDimension(id)));
    }

    @Override
    public T animation(Animation animation) {
        return foreach((ViewEacher<View>) view -> view.setAnimation(animation));
    }

    @Override
    public T rotation(float rotation) {
        return foreach((ViewEacher<View>) view -> view.setRotation(rotation));
    }

    @Override
    public T background(Drawable drawable) {
        //noinspection deprecation
        return foreach((ViewEacher<View>) view -> view.setBackgroundDrawable(drawable));
    }

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
    public T addView(View view, ViewGroup.LayoutParams params) {
        return foreach(ViewGroup.class, group -> {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
            group.addView(view, params);
        });
    }

    @Override
    public T addView(View view, int index, ViewGroup.LayoutParams params) {
        return foreach(ViewGroup.class, group -> {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
            group.addView(view, index, params);
        });
    }

    @Override
    public T progress(int progress) {
        return foreach(ProgressBar.class,(ViewEacher<ProgressBar>) (view) -> view.setProgress(progress));
    }

    @Override
    public T toggle() {
        return foreach(CompoundButton.class,(ViewEacher<CompoundButton>) (view) -> view.setChecked(!view.isChecked()));
    }

    @Override
    public T textFormat(String format) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(String.format(format, view.getText())));
    }

    @Override
    public T text(String format, Object... args) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(String.format(format, args)));
    }

    @Override
    public T html(String format, Object... args) {
        if (args.length == 0) {
            //noinspection deprecation
            return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(Html.fromHtml(format)));
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
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(Html.fromHtml(String.format(format, args))));
    }

    @Override
    public T hint(CharSequence hint) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setHint(hint));
    }

    @Override
    public T hint(String format, Object... args) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setHint(String.format(format, args)));
    }

    @Override
    public T hint(int hintId) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setHint(hintId));
    }

    @Override
    public T time(Date time) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.TIME.format(time)));
    }

    @Override
    public T time(Date time, String format) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.format(format, time)));
    }

    @Override
    public T timeDay(Date time) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.DAY.format(time)));
    }

    @Override
    public T timeDate(Date time) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.DATE.format(time)));
    }

    @Override
    public T timeFull(Date time) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.FULL.format(time)));
    }

    @Override
    public T timeStandard(Date time) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.FULL.format(time)));
    }

    @Override
    public T timeDynamic(Date time) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setText(time == null ? "" : AfDateFormat.formatTime(time)));
    }

    @Override
    public T textColor(ColorStateList color) {
        return foreach(TextView.class, (ViewEacher<TextView>) (view) -> view.setTextColor(color));
    }

    @Override
    public T textColorListId(int id) {
        return textColor(ContextCompat.getColorStateList(getContext(), id));
    }

    @Override
    public View childAt(int index) {
        return foreach(ViewGroup.class, (ViewReturnEacher<ViewGroup, View>) view -> view.getChildAt(index));
    }

    @Override
    public View[] childs() {
        return foreach(ViewGroup.class, view -> {
            View[] views = new View[view.getChildCount()];
            for (int i = 0; i < views.length; i++) {
                views[i] = view.getChildAt(i);
            }
            return views;
        }, new View[0]);
    }

    @Override
    public View[] breakChilds() {
        return $(childs()).foreach(view -> {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
        }).views();
    }

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
        return self();
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
        return self();
    }

    @Override
    public T toChild(int index) {
        if (mTargetViews != null) {
            for (int i = 0; i < mTargetViews.length; i++) {
                if (mTargetViews[i] instanceof ViewGroup) {
                    ViewGroup view = (ViewGroup) mTargetViews[i];
                    if (view.getChildCount() > index) {
                        mTargetViews[i] = view.getChildAt(index);
                    } else {
                        mTargetViews[i] = null;
                    }
                } else {
                    mTargetViews[i] = null;
                }
            }
        }
        return self();
    }

    @Override
    public T toChilds() {
        return $(childs());
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
        return self();
    }

    @Override
    public T toRoot() {
        mTargetViews = new View[]{getRootView()};
        return self();
    }

    @Override
    public T mixView(View... views) {
        if (views.length > 0) {
            View[] orgins = mTargetViews;
            mTargetViews = new View[orgins.length + views.length];
            for (int i = 0; i < mTargetViews.length; i++) {
                if (i < views.length) {
                    mTargetViews[i] = views[i];
                } else {
                    mTargetViews[i] = orgins[i - views.length];
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
    public T mixChild(int index) {
        View[] views = Arrays.copyOf(this.mTargetViews, this.mTargetViews.length);
        return toChild(index).mixView(views);
    }

    @Override
    public T mixChilds() {
        View[] views = Arrays.copyOf(this.mTargetViews, this.mTargetViews.length);
        return toChilds().mixView(views);
    }


    @Override
    public int childCount() {
        return foreach(ViewGroup.class, ViewGroup::getChildCount);
    }

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
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                return new Rect(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
            }
            return null;
        });
    }

    public View getView(int... indexs) {
        if (mTargetViews != null && mTargetViews.length > 0) {
            if (indexs != null && indexs.length > 0 && indexs[0] < mTargetViews.length) {
                return mTargetViews[indexs[0]];
            } else {
                return mTargetViews[0];
            }
        }
        return null;
    }

    @Override
    public T drawablePadding(int padding) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.setCompoundDrawablePadding(padding));
    }

    @Override
    public T drawablePadding(float padding) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.setCompoundDrawablePadding((int) (getContext().getResources().getDisplayMetrics().density * padding + 0.5f)));
    }

    @Override
    public T drawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.setCompoundDrawables(left, top, right, bottom));
    }

    @Override
    public T drawableLeft(Drawable drawable) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.setCompoundDrawables(drawable, null, null, null));
    }

    @Override
    public T drawableTop(Drawable drawable) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.setCompoundDrawables(null, drawable, null, null));
    }

    @Override
    public T drawableRight(Drawable drawable) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.setCompoundDrawables(null, null, drawable, null));
    }

    @Override
    public T drawableBottom(Drawable drawable) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.setCompoundDrawables(null, null, null, drawable));
    }

    @Override
    public T drawableLeft(@DrawableRes int id) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.setCompoundDrawablesWithIntrinsicBounds(getDrawable(getContext(),id), null, null, null));
    }

    @Override
    public T drawableTop(@DrawableRes int id) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(getContext(),id), null, null));
    }

    @Override
    public T drawableRight(@DrawableRes int id) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(getContext(),id), null));
    }

    @Override
    public T drawableBottom(@DrawableRes int id) {
        return foreach(TextView.class, (ViewEacher<TextView>) view -> view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(getContext(),id)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TT> T adapter(@LayoutRes int id, List<TT> list, AdapterItemer<TT> itemer) {
        return foreach(AdapterView.class, (ViewEacher<AdapterView>) (view) -> view.setAdapter(new AfListLayoutItemAdapter<TT>(id,view.getContext(),list) {
            @Override
            protected void onBinding(ViewQuery<? extends ViewQuery> $, TT model, int index) {
                itemer.onBinding($, model, index);
            }
        }));
    }

    //</editor-fold>

}