package com.andframe.activity.framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.andframe.feature.AfDensity;

/**
 * AfView 框架视图类
 * @author 树朾
 * 实现了 AfViewable 接口 优化 findViewById 方法
 */
@SuppressWarnings("unused")
public class AfView implements AfViewable {

	protected View mRootView = null;
	protected View mTargetView = null;

	public AfView(View view) {
		mRootView = view;
		mTargetView = view;
	}

	/**
	 * 使AfView 承载的 view 从父视图中脱离出来成为独立的 view 
	 * 	主要用于用于view 的转移
	 * 返回 null 标识 转移失败 否则返回脱离独立的 view
	 */
	public View breakaway() {
		if (mTargetView != null) {
			ViewParent parent = mTargetView.getParent();
			if (parent instanceof ViewGroup) {
				ViewGroup group = ViewGroup.class.cast(parent);
				group.removeView(mTargetView);
				return mTargetView;
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	public AfView getParentView() {
		if (mRootView != null) {
			return new AfView((View)mRootView.getParent());
		}
		return null;
	}

	@Override
	public Context getContext() {
		if (mRootView != null) {
			return mRootView.getContext();
		}
		return null;
	}

	public View getView() {
		return mTargetView;
	}

	@Override
	public View findViewById(int id) {
		if (mRootView != null) {
			return mRootView.findViewById(id);
		}
		return null;
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> clazz) {
		View view = findViewById(id);
		if (clazz.isInstance(view)) {
			return clazz.cast(view);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends View> T findViewByID(int id) {
		return (T)findViewById(id);
	}


	protected AfView self(){
		return this;
	}

	/**
	 * Points the current operating view to the first view found with the id under the root.
	 * @param id the id
	 * @return self
	 */
	public AfView id(int id){
		return id(findViewById(id));
	}

	/**
	 * Points the current operating view to the specified view.
	 * @return self
	 */
	public AfView id(View view){
		this.mTargetView = view;
		return self();
	}


	/**
	 * Set the rating of a RatingBar.
	 *
	 * @param rating the rating
	 * @return self
	 */
	public AfView rating(float rating){

		if(mTargetView instanceof RatingBar){
			RatingBar rb = (RatingBar) mTargetView;
			rb.setRating(rating);
		}
		return self();
	}


	/**
	 * Set the text of a TextView.
	 *
	 * @param resid the resid
	 * @return self
	 */
	public AfView text(int resid){

		if(mTargetView instanceof TextView){
			TextView tv = (TextView) mTargetView;
			tv.setText(resid);
		}
		return self();
	}

	/**
	 * Set the text of a TextView with localized formatted string
	 * from application's package's default string table
	 *
	 * @param resid the resid
	 * @return self
	 * @see Context#getString(int, Object...)
	 */
	public AfView text(int resid, Object... formatArgs) {
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
	public AfView text(CharSequence text){

		if(mTargetView instanceof TextView){
			TextView tv = (TextView) mTargetView;
			tv.setText(text);
		}

		return self();
	}

	/**
	 * Set the text of a TextView. Hide the mTargetView (gone) if text is empty.
	 *
	 * @param text the text
	 * @param goneIfEmpty hide if text is null or length is 0
	 * @return self
	 */

	public AfView text(CharSequence text, boolean goneIfEmpty){

		if(goneIfEmpty && (text == null || text.length() == 0)){
			return gone();
		}else{
			return text(text);
		}
	}



	/**
	 * Set the text of a TextView.
	 *
	 * @param text the text
	 * @return self
	 */
	public AfView text(Spanned text){


		if(mTargetView instanceof TextView){
			TextView tv = (TextView) mTargetView;
			tv.setText(text);
		}
		return self();
	}

	/**
	 * Set the text color of a TextView. Note that it's not a color resource id.
	 *
	 * @param color color code in ARGB
	 * @return self
	 */
	public AfView textColor(int color){

		if(mTargetView instanceof TextView){
			TextView tv = (TextView) mTargetView;
			tv.setTextColor(color);
		}
		return self();
	}

	/**
	 * Set the text color of a TextView from  a color resource id.
	 *
	 * @param id color resource id
	 * @return self
	 */
	public AfView textColorId(int id){
		return textColor(getContext().getResources().getColor(id));
	}


	/**
	 * Set the text typeface of a TextView.
	 *
	 * @param typeface typeface
	 * @return self
	 */
	public AfView typeface(Typeface typeface){

		if(mTargetView instanceof TextView){
			TextView tv = (TextView) mTargetView;
			tv.setTypeface(typeface);
		}
		return self();
	}

	/**
	 * Set the text size (in sp) of a TextView.
	 *
	 * @param size size
	 * @return self
	 */
	public AfView textSize(float size){

		if(mTargetView instanceof TextView){
			TextView tv = (TextView) mTargetView;
			tv.setTextSize(size);
		}
		return self();
	}


	/**
	 * Set the adapter of an AdapterView.
	 *
	 * @param adapter adapter
	 * @return self
	 */

	@SuppressWarnings({"unchecked", "rawtypes" })
	public AfView adapter(Adapter adapter){

		if(mTargetView instanceof AdapterView){
			AdapterView av = (AdapterView) mTargetView;
			av.setAdapter(adapter);
		}

		return self();
	}

	/**
	 * Set the adapter of an ExpandableListView.
	 *
	 * @param adapter adapter
	 * @return self
	 */
	public AfView adapter(ExpandableListAdapter adapter){

		if(mTargetView instanceof ExpandableListView){
			ExpandableListView av = (ExpandableListView) mTargetView;
			av.setAdapter(adapter);
		}

		return self();
	}

	/**
	 * Set the image of an ImageView.
	 *
	 * @param resid the resource id
	 * @return self
	 *
	 */
	public AfView image(int resid){

		if(mTargetView instanceof ImageView){
			ImageView iv = (ImageView) mTargetView;
			if(resid == 0){
				iv.setImageBitmap(null);
			}else{
				iv.setImageResource(resid);
			}
		}

		return self();
	}

	/**
	 * Set the image of an ImageView.
	 *
	 * @param drawable the drawable
	 * @return self
	 *
	 *
	 */
	public AfView image(Drawable drawable){

		if(mTargetView instanceof ImageView){
			ImageView iv = (ImageView) mTargetView;
			iv.setImageDrawable(drawable);
		}

		return self();
	}

	/**
	 * Set the image of an ImageView.
	 *
	 * @param bm Bitmap
	 * @return self
	 *
	 */
	public AfView image(Bitmap bm){

		if(mTargetView instanceof ImageView){
			ImageView iv = (ImageView) mTargetView;
			iv.setImageBitmap(bm);
		}

		return self();
	}


	/**
	 * Set the image of an ImageView.
	 *
	 * @param url Image url.
	 * @return self
	 *
	 */

	public AfView image(String url){
		if (mTargetView instanceof ImageView && url != null) {
			((ImageView) mTargetView).setImageURI(Uri.parse(url));
		}
		return self();
	}


	/**
	 * Set tag object of a mTargetView.
	 * @return self
	 */
	public AfView tag(Object tag){

		if(mTargetView != null){
			mTargetView.setTag(tag);
		}

		return self();
	}

	/**
	 * Set tag object of a mTargetView.
	 *
	 * @return self
	 */
	public AfView tag(int key, Object tag){

		if(mTargetView != null){
			mTargetView.setTag(key, tag);
		}

		return self();
	}

	/**
	 * Enable a mTargetView.
	 *
	 * @param enabled state
	 * @return self
	 */
	public AfView enabled(boolean enabled){

		if(mTargetView != null){
			mTargetView.setEnabled(enabled);
		}

		return self();
	}

	/**
	 * Set checked state of a compound button.
	 *
	 * @param checked state
	 * @return self
	 */
	public AfView checked(boolean checked){

		if(mTargetView instanceof CompoundButton){
			CompoundButton cb = (CompoundButton) mTargetView;
			cb.setChecked(checked);
		}

		return self();
	}

	/**
	 * Get checked state of a compound button.
	 *
	 * @return checked
	 */
	public boolean isChecked(){

		boolean checked = false;

		if(mTargetView instanceof CompoundButton){
			CompoundButton cb = (CompoundButton) mTargetView;
			checked = cb.isChecked();
		}

		return checked;
	}

	/**
	 * Set clickable for a mTargetView.
	 * @return self
	 */
	public AfView clickable(boolean clickable){
		if(mTargetView != null){
			mTargetView.setClickable(clickable);
		}

		return self();
	}


	/**
	 * Set mTargetView visibility to View.GONE.
	 *
	 * @return self
	 */
	public AfView gone(){
		return visibility(View.GONE);
	}

	/**
	 * Set mTargetView visibility to View.INVISIBLE.
	 *
	 * @return self
	 */
	public AfView invisible(){
		return visibility(View.INVISIBLE);
	}

	/**
	 * Set mTargetView visibility to View.VISIBLE.
	 *
	 * @return self
	 */
	public AfView visible(){
		return visibility(View.VISIBLE);
	}

	/**
	 * Set mTargetView visibility, such as View.VISIBLE.
	 *
	 * @return self
	 */
	public AfView visibility(int visibility){

		if(mTargetView != null && mTargetView.getVisibility() != visibility){
			mTargetView.setVisibility(visibility);
		}

		return self();
	}


	/**
	 * Set mTargetView background.
	 *
	 * @param id the id
	 * @return self
	 */
	public AfView background(int id){

		if(mTargetView != null){

			if(id != 0){
				mTargetView.setBackgroundResource(id);
			}else{
				mTargetView.setBackgroundDrawable(null);
			}

		}

		return self();
	}

	/**
	 * Set mTargetView background color.
	 *
	 * @param color color code in ARGB
	 * @return self
	 */
	public AfView backgroundColor(int color){

		if(mTargetView != null){
			mTargetView.setBackgroundColor(color);
		}

		return self();
	}

	/**
	 * Set mTargetView background color.
	 *
	 * @param colorId color code in resource id
	 * @return self
	 */
	public AfView backgroundColorId(int colorId){

		if(mTargetView != null){
			mTargetView.setBackgroundColor(getContext().getResources().getColor(colorId));
		}

		return self();
	}

	/**
	 * Notify a ListView that the data of it's adapter is changed.
	 *
	 * @return self
	 */
	public AfView dataChanged(){

		if(mTargetView instanceof AdapterView){

			AdapterView<?> av = (AdapterView<?>) mTargetView;
			Adapter a = av.getAdapter();

			if(a instanceof BaseAdapter){
				BaseAdapter ba = (BaseAdapter) a;
				ba.notifyDataSetChanged();
			}

		}


		return self();
	}




	/**
	 * Checks if the current mTargetView exist.
	 *
	 * @return true, if is exist
	 */
	public boolean isExist(){
		return mTargetView != null;
	}

	/**
	 * Gets the tag of the mTargetView.
	 *
	 * @return tag
	 */
	public Object getTag(){
		Object result = null;
		if(mTargetView != null){
			result = mTargetView.getTag();
		}
		return result;
	}

	/**
	 * Gets the tag of the mTargetView.
	 * @param id the id
	 *
	 * @return tag
	 */
	public Object getTag(int id){
		Object result = null;
		if(mTargetView != null){
			result = mTargetView.getTag(id);
		}
		return result;
	}

	/**
	 * Gets the current mTargetView as an image mTargetView.
	 *
	 * @return ImageView
	 */
	public ImageView getImageView(){
		return (ImageView) mTargetView;
	}

	/**
	 * Gets the current mTargetView as an Gallery.
	 *
	 * @return Gallery
	 */
	public Gallery getGallery(){
		return (Gallery) mTargetView;
	}



	/**
	 * Gets the current mTargetView as a text mTargetView.
	 *
	 * @return TextView
	 */
	public TextView getTextView(){
		return (TextView) mTargetView;
	}

	/**
	 * Gets the current mTargetView as an edit text.
	 *
	 * @return EditText
	 */
	public EditText getEditText(){
		return (EditText) mTargetView;
	}

	/**
	 * Gets the current mTargetView as an progress bar.
	 *
	 * @return ProgressBar
	 */
	public ProgressBar getProgressBar(){
		return (ProgressBar) mTargetView;
	}

	/**
	 * Gets the current mTargetView as seek bar.
	 *
	 * @return SeekBar
	 */

	public SeekBar getSeekBar(){
		return (SeekBar) mTargetView;
	}

	/**
	 * Gets the current mTargetView as a button.
	 *
	 * @return Button
	 */
	public Button getButton(){
		return (Button) mTargetView;
	}

	/**
	 * Gets the current mTargetView as a checkbox.
	 *
	 * @return CheckBox
	 */
	public CheckBox getCheckBox(){
		return (CheckBox) mTargetView;
	}

	/**
	 * Gets the current mTargetView as a listview.
	 *
	 * @return ListView
	 */
	public ListView getListView(){
		return (ListView) mTargetView;
	}

	/**
	 * Gets the current mTargetView as a ExpandableListView.
	 *
	 * @return ExpandableListView
	 */
	public ExpandableListView getExpandableListView(){
		return (ExpandableListView) mTargetView;
	}

	/**
	 * Gets the current mTargetView as a gridview.
	 *
	 * @return GridView
	 */
	public GridView getGridView(){
		return (GridView) mTargetView;
	}

	/**
	 * Gets the current mTargetView as a RatingBar.
	 *
	 * @return RatingBar
	 */
	public RatingBar getRatingBar(){
		return (RatingBar) mTargetView;
	}

	/**
	 * Gets the current mTargetView as a webview.
	 *
	 * @return WebView
	 */
	public WebView getWebView(){
		return (WebView) mTargetView;
	}

	/**
	 * Gets the current mTargetView as a spinner.
	 *
	 * @return Spinner
	 */
	public Spinner getSpinner(){
		return (Spinner) mTargetView;
	}

	/**
	 * Gets the editable.
	 *
	 * @return the editable
	 */
	public Editable getEditable(){

		Editable result = null;

		if(mTargetView instanceof EditText){
			result = ((EditText) mTargetView).getEditableText();
		}

		return result;
	}

	/**
	 * Gets the text of a TextView.
	 *
	 * @return the text
	 */
	public CharSequence getText(){

		CharSequence result = null;

		if(mTargetView instanceof TextView){
			result = ((TextView) mTargetView).getText();
		}

		return result;
	}

	/**
	 * Gets the selected item if current mTargetView is an adapter mTargetView.
	 *
	 * @return selected
	 */
	public Object getSelectedItem(){

		Object result = null;

		if(mTargetView instanceof AdapterView<?>){
			result = ((AdapterView<?>) mTargetView).getSelectedItem();
		}

		return result;

	}


	/**
	 * Gets the selected item position if current mTargetView is an adapter mTargetView.
	 *
	 * Returns AdapterView.INVALID_POSITION if not valid.
	 *
	 * @return selected position
	 */
	public int getSelectedItemPosition(){

		int result = AdapterView.INVALID_POSITION;

		if(mTargetView instanceof AdapterView<?>){
			result = ((AdapterView<?>) mTargetView).getSelectedItemPosition();
		}

		return result;
	}

	/**
	 * Register a callback method for when the mTargetView is clicked.
	 *
	 * @param listener The callback method.
	 * @return self
	 */
	public AfView clicked(View.OnClickListener listener){

		if(mTargetView != null){
			mTargetView.setOnClickListener(listener);
		}

		return self();
	}

	/**
	 * Register a callback method for when the mTargetView is long clicked.
	 *
	 * @param listener The callback method.
	 * @return self
	 */
	public AfView longClicked(View.OnLongClickListener listener){

		if(mTargetView != null){
			mTargetView.setOnLongClickListener(listener);
		}

		return self();
	}

	/**
	 * Register a callback method for when an item is clicked in the ListView.
	 *
	 * @param listener The callback method.
	 * @return self
	 */
	public AfView itemClicked(AdapterView.OnItemClickListener listener){

		if(mTargetView instanceof AdapterView){

			AdapterView<?> alv = (AdapterView<?>) mTargetView;
			alv.setOnItemClickListener(listener);


		}

		return self();

	}

	/**
	 * Register a callback method for when an item is long clicked in the ListView.
	 *
	 * @param listener The callback method.
	 * @return self
	 */
	public AfView itemLongClicked(AdapterView.OnItemLongClickListener listener){

		if(mTargetView instanceof AdapterView){
			AdapterView<?> alv = (AdapterView<?>) mTargetView;
			alv.setOnItemLongClickListener(listener);
		}

		return self();

	}

	/**
	 * Set selected item of an AdapterView.
	 *
	 * @param position The position of the item to be selected.
	 * @return self
	 */
	public AfView setSelection(int position){

		if(mTargetView instanceof AdapterView){
			AdapterView<?> alv = (AdapterView<?>) mTargetView;
			alv.setSelection(position);
		}

		return self();

	}

	/**
	 * Register a callback method for when a textview text is changed. Method must have signature of method(CharSequence s, int start, int before, int count)).
	 *
	 * @param method The method name of the callback.
	 * @return self
	 */
	public AfView textChanged(TextWatcher method){
		if(mTargetView instanceof TextView){
			TextView tv = (TextView) mTargetView;
			tv.addTextChangedListener(method);
		}
		return self();
	}

	/**
	 * Set the margin of a mTargetView. Notes all parameters are in DIP, not in pixel.
	 *
	 * @param leftDip the left dip
	 * @param topDip the top dip
	 * @param rightDip the right dip
	 * @param bottomDip the bottom dip
	 * @return self
	 */
	@SuppressWarnings("unused")
	public AfView margin(float leftDip, float topDip, float rightDip, float bottomDip){

		if(mTargetView != null){

			ViewGroup.LayoutParams lp = mTargetView.getLayoutParams();

			if(lp instanceof ViewGroup.MarginLayoutParams){

				Context context = getContext();

				int left = AfDensity.dip2px(context, leftDip);
				int top = AfDensity.dip2px(context, topDip);
				int right = AfDensity.dip2px(context, rightDip);
				int bottom = AfDensity.dip2px(context, bottomDip);

				((ViewGroup.MarginLayoutParams) lp).setMargins(left, top, right, bottom);
				mTargetView.setLayoutParams(lp);
			}

		}

		return self();
	}

	/**
	 * Set the width of a mTargetView in dip.
	 * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
	 *
	 * @param dip width in dip
	 * @return self
	 */

	public AfView width(int dip){
		size(true, dip, true);
		return self();
	}

	/**
	 * Set the height of a mTargetView in dip.
	 * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
	 *
	 * @param dip height in dip
	 * @return self
	 */

	public AfView height(int dip){
		size(false, dip, true);
		return self();
	}

	/**
	 * Set the width of a mTargetView in dip or pixel.
	 * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
	 *
	 * @param width width
	 * @param dip dip or pixel
	 * @return self
	 */

	public AfView width(int width, boolean dip){
		size(true, width, dip);
		return self();
	}

	/**
	 * Set the height of a mTargetView in dip or pixel.
	 * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
	 *
	 * @param height height
	 * @param dip dip or pixel
	 * @return self
	 */

	public AfView height(int height, boolean dip){
		size(false, height, dip);
		return self();
	}

	private void size(boolean width, int n, boolean dip){

		if(mTargetView != null){

			ViewGroup.LayoutParams lp = mTargetView.getLayoutParams();

			Context context = getContext();

			if(n > 0 && dip){
				n = AfDensity.dip2px(context, n);
			}

			if(width){
				lp.width = n;
			}else{
				lp.height = n;
			}

			mTargetView.setLayoutParams(lp);

		}

	}

}
