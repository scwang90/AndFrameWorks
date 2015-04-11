package com.andframe.activity.framework;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
/**
 * AfViewable 框架视图接口
 * @author SCWANG
 *	主要用于 优化 传统的 findViewById 方法
 */
public interface AfViewable {

	public Context getContext();

	public Resources getResources();

	public View findViewById(int id);

	public TextView findTextViewById(int id);

	public ImageView findImageViewById(int id);

	public Button findButtonById(int id);

	public EditText findEditTextById(int id);

	public CheckBox findCheckBoxById(int id);

	public RadioButton findRadioButtonById(int id);

	public ListView findListViewById(int id);

	public LinearLayout findLinearLayoutById(int id);

	public FrameLayout findFrameLayoutById(int id);

	public RelativeLayout findRelativeLayoutById(int id);

	public ScrollView findScrollViewById(int id);

	public GridView findGridViewById(int id);

	public ViewPager findViewPagerById(int id);

	public WebView findWebViewById(int id);

	public ProgressBar findProgressBarById(int id);

	public RadioGroup findRadioGroupById(int id);

	public RatingBar findRatingBarById(int id);
	
	public DatePicker findDatePickerById(int id);
	
	public TimePicker findTimePickerById(int id);

	public ExpandableListView findExpandableListViewById(int id);

	public HorizontalScrollView findHorizontalScrollViewById(int id);

	public <T> T findViewById(int id,Class<T> clazz);
}
