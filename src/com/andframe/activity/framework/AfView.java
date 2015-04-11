package com.andframe.activity.framework;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
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
 * AfView 框架视图类
 * @author SCWANG
 *	实现了 AfViewable 接口 优化 findViewById 方法
 */
public class AfView implements AfViewable {

	private View mRootView = null;

	public AfView(View view) {
		// TODO Auto-generated constructor stub
		mRootView = view;
	}
	/**
	 * 使AfView 承载的 view 从父视图中脱离出来成为独立的 view 
	 * 	主要用于用于view 的转移
	 * 返回 null 标识 转移失败 否则返回脱离独立的 view
	 */
	public View breakaway() {
		if (mRootView != null) {
			ViewParent parent = mRootView.getParent();
			if (parent instanceof ViewGroup) {
				ViewGroup group = ViewGroup.class.cast(parent);
				group.removeView(mRootView);
				return mRootView;
			}
		}
		return null;
	}
	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		if (mRootView != null) {
			return mRootView.getContext();
		}
		return null;
	}

	@Override
	public Resources getResources() {
		// TODO Auto-generated method stub
		if (mRootView != null) {
			return mRootView.getResources();
		}
		return null;
	}

	@Override
	public View findViewById(int id) {
		// TODO Auto-generated method stub
		if (mRootView != null) {
			return mRootView.findViewById(id);
		}
		return null;
	}

	@Override
	public final TextView findTextViewById(int id) {
		View view = findViewById(id);
		if (view instanceof TextView) {
			return (TextView) view;
		}
		return null;
	}

	@Override
	public final ImageView findImageViewById(int id) {
		View view = findViewById(id);
		if (view instanceof ImageView) {
			return (ImageView) view;
		}
		return null;
	}

	@Override
	public final Button findButtonById(int id) {
		View view = findViewById(id);
		if (view instanceof Button) {
			return (Button) view;
		}
		return null;
	}

	@Override
	public final EditText findEditTextById(int id) {
		View view = findViewById(id);
		if (view instanceof EditText) {
			return (EditText) view;
		}
		return null;
	}

	@Override
	public final CheckBox findCheckBoxById(int id) {
		View view = findViewById(id);
		if (view instanceof CheckBox) {
			return (CheckBox) view;
		}
		return null;
	}

	@Override
	public final RadioButton findRadioButtonById(int id) {
		View view = findViewById(id);
		if (view instanceof RadioButton) {
			return (RadioButton) view;
		}
		return null;
	}

	@Override
	public final ListView findListViewById(int id) {
		View view = findViewById(id);
		if (view instanceof ListView) {
			return (ListView) view;
		}
		return null;
	}

	@Override
	public GridView findGridViewById(int id) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (view instanceof GridView) {
			return (GridView) view;
		}
		return null;
	}

	@Override
	public final LinearLayout findLinearLayoutById(int id) {
		View view = findViewById(id);
		if (view instanceof LinearLayout) {
			return (LinearLayout) view;
		}
		return null;
	}

	@Override
	public final FrameLayout findFrameLayoutById(int id) {
		View view = findViewById(id);
		if (view instanceof FrameLayout) {
			return (FrameLayout) view;
		}
		return null;
	}

	@Override
	public final RelativeLayout findRelativeLayoutById(int id) {
		View view = findViewById(id);
		if (view instanceof RelativeLayout) {
			return (RelativeLayout) view;
		}
		return null;
	}

	@Override
	public final ScrollView findScrollViewById(int id) {
		View view = findViewById(id);
		if (view instanceof ScrollView) {
			return (ScrollView) view;
		}
		return null;
	}

	@Override
	public ViewPager findViewPagerById(int id) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (view instanceof ViewPager) {
			return (ViewPager) view;
		}
		return null;
	}

	@Override
	public WebView findWebViewById(int id) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (view instanceof WebView) {
			return (WebView) view;
		}
		return null;
	}

	@Override
	public ProgressBar findProgressBarById(int id) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (view instanceof ProgressBar) {
			return (ProgressBar) view;
		}
		return null;
	}

	@Override
	public RatingBar findRatingBarById(int id) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (view instanceof RatingBar) {
			return (RatingBar) view;
		}
		return null;
	}

	@Override
	public ExpandableListView findExpandableListViewById(int id) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (view instanceof ExpandableListView) {
			return (ExpandableListView) view;
		}
		return null;
	}

	@Override
	public HorizontalScrollView findHorizontalScrollViewById(int id) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (view instanceof HorizontalScrollView) {
			return (HorizontalScrollView) view;
		}
		return null;
	}

	@Override
	public RadioGroup findRadioGroupById(int id) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (view instanceof RadioGroup) {
			return (RadioGroup) view;
		}
		return null;
	}

	@Override
	public DatePicker findDatePickerById(int id) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (view instanceof DatePicker) {
			return (DatePicker) view;
		}
		return null;
	}

	@Override
	public TimePicker findTimePickerById(int id) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (view instanceof TimePicker) {
			return (TimePicker) view;
		}
		return null;
	}

	public View getView() {
		// TODO Auto-generated method stub
		return mRootView;
	}

	@Override
	public <T> T findViewById(int id, Class<T> clazz) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (clazz.isInstance(view)) {
			return clazz.cast(view);
		}
		return null;
	}
}
