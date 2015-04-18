package com.andframe.layoutbind;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.widget.popupmenu.OnMenuItemClickListener;
import com.andframe.widget.popupmenu.PopupMenu;


public class AfModuleTitlebar extends AfLayoutModule implements OnClickListener {

	public static final int FUNCTION_NONE = 0;
	public static final int FUNCTION_ADD = 1;
	public static final int FUNCTION_OK = 2;
	public static final int FUNCTION_MENU = 3;

	public static final int ID_GOBACK = R.id.titlebar_other_goback;
	public static final int ID_ADD = R.id.titlebar_other_add;
	public static final int ID_OK = R.id.titlebar_other_ok;
	public static final int ID_MEUN = R.id.titlebar_other_meun;

	private View mBtGoBack = null;
	private View mBtAdd = null;
	private View mBtOK = null;
	private View mBtMenu = null;
	private TextView mTvTitle = null;

	private Map<String, Integer> mMeuns = new HashMap<String, Integer>();
	private OnMenuItemClickListener mListener = null;
	private WeakReference<Activity> mWeakRefActivity = null;

	public AfModuleTitlebar(AfPageable page) {
		// TODO Auto-generated constructor stub
		this(page, FUNCTION_NONE);
	}

	public AfModuleTitlebar(AfPageable page, int function) {
		super(page);
		// TODO Auto-generated constructor stub
		if(isValid()){
			mBtAdd = page.findViewById(R.id.titlebar_other_add);
			mBtGoBack = page.findViewById(R.id.titlebar_other_goback);
			mBtOK = page.findViewById(R.id.titlebar_other_ok);
			mBtMenu = page.findViewById(R.id.titlebar_other_meun);
			mTvTitle = page.findViewByID(R.id.titlebar_other_title);
			mWeakRefActivity = new WeakReference<Activity>(page.getActivity());
			mMeuns = new HashMap<String, Integer>();
			mBtMenu.setOnClickListener(this);
			mBtGoBack.setOnClickListener(this);
			setFunction(function);
		}
	}

	public AfModuleTitlebar(AfPageable page, String title) {
		this(page, FUNCTION_NONE);
		// TODO Auto-generated constructor stub
		setTitle(title);
	}

	@Override
	protected View findLayout(AfViewable view) {
		// TODO Auto-generated method stub
		return view.findViewById(R.id.titlebar_other_layout);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mWeakRefActivity != null) {
			if (v.getId() == R.id.titlebar_other_goback) {
				Activity activity = mWeakRefActivity.get();
				if (activity != null) {
					activity.finish();
				}
			} else if(v.getId() == R.id.titlebar_other_meun){
				PopupMenu pm = new PopupMenu(v.getContext(), v);
				if (pm.isValid()) {
					Iterator<Entry<String, Integer>> iter = mMeuns.entrySet().iterator();  
					while (iter.hasNext()) {  
						Entry<String, Integer> entry = iter.next();  
						pm.getMenu().add(1,entry.getValue(),0,entry.getKey());
					}  
					pm.setOnMenuItemClickListener(mListener);
					pm.show();
				}
			}
		}
	}

	public void setFunction(int function) {
		// TODO Auto-generated constructor stub
		switch (function) {
		default:
		case FUNCTION_NONE:
			mBtAdd.setVisibility(View.GONE);
			mBtOK.setVisibility(View.GONE);
			mBtMenu.setVisibility(View.GONE);
			break;
		case FUNCTION_ADD:
			mBtAdd.setVisibility(View.VISIBLE);
			mBtOK.setVisibility(View.GONE);
			mBtMenu.setVisibility(View.GONE);
			break;
		case FUNCTION_OK:
			mBtOK.setVisibility(View.VISIBLE);
			mBtAdd.setVisibility(View.GONE);
			mBtMenu.setVisibility(View.GONE);
			break;
		case FUNCTION_MENU:
			mBtMenu.setVisibility(View.VISIBLE);
			mBtOK.setVisibility(View.GONE);
			mBtAdd.setVisibility(View.GONE);
			break;
		}
	}

	public void addMeuns(Map<String, Integer> map) {
		this.mMeuns.putAll(map);
	}

	public void setMenuItemListener(OnMenuItemClickListener mListener) {
		this.mListener = mListener;
	}
	
	public void setOnGoBackListener(OnClickListener listener) {
		// TODO Auto-generated constructor stub
		mBtGoBack.setOnClickListener(listener);
	}

	public void setOnAddListener(OnClickListener listener) {
		// TODO Auto-generasted constructor stub
		mBtAdd.setOnClickListener(listener);
	}

	public void setOnOkListener(OnClickListener listener) {
		// TODO Auto-generasted constructor stub
		mBtOK.setOnClickListener(listener);
	}

	public void setTitle(String description) {
		// TODO Auto-generated constructor stub
		mTvTitle.setText(description);
	}

	public void setTitle(int id) {
		// TODO Auto-generated constructor stub
		mTvTitle.setText(id);
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return mTvTitle.getText().toString();
	}

	public void putMenu(String text, int id) {
		// TODO Auto-generated method stub
		this.mMeuns.put(text, id);
	}

}
