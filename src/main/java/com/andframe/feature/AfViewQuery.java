package com.andframe.feature;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
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

import com.andframe.api.view.ViewQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 安卓版 JQuery 实现
 * Created by SCWANG on 2016/8/18.
 */
public class AfViewQuery<T extends AfViewQuery<T>> implements ViewQuery<T> {

    protected View mRootView = null;
    protected View[] mTargetViews = null;

    public AfViewQuery(View view) {
        mRootView = view;
        mTargetViews = new View[]{view};
    }

    public Context getContext() {
        if (mRootView != null) {
            return mRootView.getContext();
        }
        return null;
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
        return null;
    }

    @Override
    public T $(int... id) {
        return id(id);
    }

    @Override
    public T $(String idvalue, String... idvalues) {
        if (mRootView == null) {
            return self();
        }
        List<Integer> listId = new ArrayList<>(idvalues.length + 1);
        String packageName = getContext().getPackageName();
        Resources resources = mRootView.getResources();
        if (idvalue != null) {
            listId.add(resources.getIdentifier(idvalue,"id",packageName));
        }
        if (idvalues.length > 0) {
            for (String value : idvalues) {
                listId.add(resources.getIdentifier(value,"id",packageName));
            }
        }
        int[] ids = new int[listId.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = listId.get(i);
        }
        return id(ids);
    }

    @Override
    public T $(Class<? extends View> type, Class<? extends View>... types) {
        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(mRootView));
        List<View> list = new ArrayList<>();
        do {
            View cview = views.poll();
            if (cview != null) {
                if (type != null && type.isAssignableFrom(cview.getClass())) {
                    list.add(cview);
                } else if (types.length > 0) {
                    for (Class<? extends View> ttype : types) {
                        if (ttype.isAssignableFrom(cview.getClass())) {
                            list.add(cview);
                            break;
                        }
                    }
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
    public T $(View view, View... views) {
        return id(view, views);
    }

    public View view(int... indexs) {
        return getView(indexs);
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
    public int gravity() {
        return foreach(view -> {
            if (view instanceof TextView) {
                return ((TextView) view).getGravity();
            } else if (view instanceof LinearLayout && Build.VERSION.SDK_INT > 23) {
                return ((LinearLayout) view).getGravity();
            }
            return null;
        });
    }

    @Override
    public T gravity(int gravity) {
        foreach(view -> {
            if (view instanceof TextView) {
                ((TextView) view).setGravity(gravity);
            } else if (view instanceof LinearLayout && Build.VERSION.SDK_INT > 23) {
                ((LinearLayout) view).setGravity(gravity);
            }
        });
        return self();
    }

    @Override
    public T margin(float Dip) {
        return margin(Dip,Dip,Dip,Dip);
    }

    @Override
    public T padding(float Dip) {
        return padding(Dip,Dip,Dip,Dip);
    }

    @Override
    public T padding(float leftDip, float topDip, float rightDip, float bottomDip) {
        return foreach(TextView.class, view -> {
            float scale = getContext().getResources().getDisplayMetrics().density;
            int left = (int) (scale * leftDip + 0.5f);
            int top = (int) (scale * topDip + 0.5f);
            int right = (int) (scale * rightDip + 0.5f);
            int bottom = (int) (scale * bottomDip + 0.5f);
            view.setPadding(left, top, right, bottom);
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
    public T progress(int progress) {
        return foreach(ProgressBar.class,(ViewEacher<ProgressBar>) (view) -> view.setProgress(progress));
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
    //</editor-fold>
    /**
     * Points the current operating view to the first view found with the id under the root.
     *
     * @param id the id
     * @return self
     */
    public T id(int... id) {
        if (id.length == 0) {
            mTargetViews = new View[]{mRootView};
        } else {
            this.mTargetViews = new View[id.length];
            for (int i = 0; i < id.length; i++) {
                mTargetViews[i] = findViewById(id[i]);
            }
        }
        return self();
    }

    /**
     * Points the current operating view to the specified view.
     *
     * @return self
     */
    public T id(View view, View... views) {
        if (view == null) {
            this.mTargetViews = views;
        } else {
            this.mTargetViews = new ArrayList<View>(Arrays.asList(views)){{add(view);}}.toArray(new View[views.length+1]);
        }
        return self();
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
        //noinspection deprecation
        return textColor(getContext().getResources().getColor(id));
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
//        boolean checked = false;
//        if (mTargetViews != null) {
//            for (View view : mTargetViews) {
//                if (view instanceof CompoundButton) {
//                    CompoundButton cb = (CompoundButton) view;
//                    checked = checked || cb.isChecked();
//                }
//            }
//        }
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
            if (id != 0) {
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
     * Set mTargetViews background color.
     *
     * @param colorId color code in resource id
     * @return self
     */
    public T backgroundColorId(int colorId) {
        //noinspection deprecation
        return foreach((ViewEacher<View>) (view) -> view.setBackgroundColor(getContext().getResources().getColor(colorId)));
    }

    /**
     * Notify a ListView that the data of it's adapter is changed.
     *
     * @return self
     */
    public T dataChanged() {
        return foreach(AdapterView.class, (view) -> {
            AdapterView<?> av = (AdapterView<?>) view;
            Adapter a = av.getAdapter();
            if (a instanceof BaseAdapter) {
                BaseAdapter ba = (BaseAdapter) a;
                ba.notifyDataSetChanged();
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
//        Object result = null;
//        if (mTargetViews != null) {
//            for (View view : mTargetViews) {
//                if (view != null) {
//                    result = view.getTag();
//                }
//            }
//        }
        return foreach((ViewReturnEacher<View,Object>) View::getTag);
    }

    /**
     * Gets the tag of the mTargetViews.
     *
     * @param id the id
     * @return tag
     */

    public Object getTag(int id) {
//        Object result = null;
//        if (mTargetViews != null) {
//            for (View view : mTargetViews) {
//                if (view != null) {
//                    result = view.getTag(id);
//                }
//            }
//        }
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
//        Editable result = null;
//        if (mTargetViews != null) {
//            for (View view : mTargetViews) {
//                if (view instanceof EditText) {
//                    result = ((EditText) view).getEditableText();
//                }
//            }
//        }
        return foreach(EditText.class, EditText::getEditableText);
    }

    /**
     * Gets the text of a TextView.
     *
     * @return the text
     */
    public CharSequence getText() {
//        CharSequence result = null;
//        if (mTargetViews != null) {
//            for (View view : mTargetViews) {
//                if (view instanceof TextView) {
//                    result = ((TextView) view).getText();
//                }
//            }
//        }
        return foreach(TextView.class, TextView::getText);
    }

    /**
     * Gets the selected item if current mTargetViews is an adapter mTargetViews.
     *
     * @return selected
     */
    public Object getSelectedItem() {
//        Object result = null;
//        if (mTargetViews != null) {
//            for (View view : mTargetViews) {
//                if (view instanceof AdapterView<?>) {
//                    result = ((AdapterView<?>) view).getSelectedItem();
//                }
//            }
//        }
        return foreach(AdapterView.class, (ViewReturnEacher<AdapterView, Object>) view -> view.getSelectedItem());
    }


    /**
     * Gets the selected item position if current mTargetViews is an adapter mTargetViews.
     * <p>
     * Returns AdapterView.INVALID_POSITION if not valid.
     *
     * @return selected position
     */
    public int getSelectedItemPosition() {
//        int result = AdapterView.INVALID_POSITION;
//        if (mTargetViews != null) {
//            for (View view : mTargetViews) {
//                if (view instanceof AdapterView<?>) {
//                    result = ((AdapterView<?>) view).getSelectedItemPosition();
//                }
//            }
//        }
        return foreach(AdapterView.class, (ViewReturnEacher<AdapterView, Integer>) view -> view.getSelectedItemPosition());
    }

    /**
     * Register a callback method for when the mTargetViews is clicked.
     *
     * @param listener The callback method.
     * @return self
     */
    public T clicked(View.OnClickListener listener) {
        return foreach((ViewEacher<View>) view -> view.setOnClickListener(listener));
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
    @SuppressWarnings("unused")
    public T margin(float leftDip, float topDip, float rightDip, float bottomDip) {
        return foreach(TextView.class, view -> {
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

    public T width(int dip) {
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

    public T height(int dip) {
        size(false, dip, true);
        return self();
    }

    /**
     * Set the width of a mTargetViews in dip or pixel.
     * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
     *
     * @param width width
     * @param dip   dip or pixel
     * @return self
     */

    public T width(int width, boolean dip) {
        size(true, width, dip);
        return self();
    }

    /**
     * Set the height of a mTargetViews in dip or pixel.
     * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
     *
     * @param height height
     * @param dip    dip or pixel
     * @return self
     */

    public T height(int height, boolean dip) {
        size(false, height, dip);
        return self();
    }

    private T size(boolean width, int n, boolean dip) {
        return foreach(view -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            Context context = getContext();
            int tn = n;
            if (tn > 0 && dip) {
                float scale = context.getResources().getDisplayMetrics().density;
                tn = (int) (scale * tn + 0.5f);
            }
            if (width) {
                lp.width = tn;
            } else {
                lp.height = tn;
            }
            view.setLayoutParams(lp);
        });
    }
}