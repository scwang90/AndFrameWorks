package com.andoffice.layoutbind;

import android.view.View;
import android.view.View.OnClickListener;

import com.andoffice.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.AfLayoutAlpha;

public class ModuleBottombar extends AfLayoutAlpha{

	public static final int ID_ADD = 0;
	public static final int ID_SELECT = 1;
	public static final int ID_SEARCH = 2;
	public static final int ID_TABLE = 3;
	public static final int ID_CONFIRM = 4;
	public static final int ID_MORE = 5;
	
	protected static final int ID_All = 6;
	
	protected View[] mViews = new View[ID_All];
	
	public ModuleBottombar(AfPageable page) {
		super(page);
		// TODO Auto-generated constructor stub
		if(isValid()){
			int[] ids = new int[]{
					R.id.bottombar_add,
					R.id.bottombar_select,
					R.id.bottombar_search,
					R.id.bottombar_table,
					R.id.bottombar_ok,
					R.id.bottombar_more
			};
			for (int i = 0; i < ids.length; i++) {
				mViews[i] = page.findViewById(ids[i]);
				mViews[i].setVisibility(View.GONE);
				mViews[i].setId(i);
			}
			mLayout.setVisibility(View.GONE);
		}
	}
	
	public void setFunction(int id ,boolean value){
		if(id >= 0 && id < ID_All){
			if(value && mLayout.getVisibility() != View.VISIBLE){
				mLayout.setVisibility(View.VISIBLE);
			}
			mViews[id].setVisibility(value?View.VISIBLE:View.GONE);
		}
	}
	
	public void setHighLightMode(boolean value){
		if(isValid()){
			if(value){
				mLayout.setBackgroundResource(R.color.theme_titlebar_selcet_dg);
			}else{
				mLayout.setBackgroundResource(R.color.gray_dark);
			}	
		}
	}
	
	@Override
	protected View findLayout(AfViewable view) {
		// TODO Auto-generated method stub
		return view.findViewById(R.id.bottombar_layout);
	}

	public void setListener(OnClickListener listener) {
		// TODO Auto-generated constructor stub
		for (View view : mViews) {
			view.setOnClickListener(listener);
		}
	}

	public void setIdListener(int id,OnClickListener listener) {
		// TODO Auto-generated constructor stub
		if(id >= 0 && id < ID_All){
			mViews[id].setOnClickListener(listener);
		}
	}

	public void setSelectListener(OnClickListener listener) {
		// TODO Auto-generated constructor stub
		mViews[ID_SELECT].setOnClickListener(listener);
	}

	public void setAddListener(OnClickListener listener) {
		// TODO Auto-generasted constructor stub
		mViews[ID_ADD].setOnClickListener(listener);
	}
	public void setSearchListener(OnClickListener listener) {
		// TODO Auto-generasted constructor stub
		mViews[ID_SEARCH].setOnClickListener(listener);
	}

	public void setTableListener(OnClickListener listener) {
		// TODO Auto-generasted constructor stub
		mViews[ID_TABLE].setOnClickListener(listener);
	}
}
