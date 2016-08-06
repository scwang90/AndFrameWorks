package com.andoffice.layoutbind;

import android.view.View;

import com.andframe.activity.framework.AfPageable;
import com.andframe.layoutbind.AfModuleAlpha;
import com.andoffice.R;

public class ModuleBottombar extends AfModuleAlpha {

	public static final int ID_ADD = 0;
	public static final int ID_SELECT = 1;
	public static final int ID_SEARCH = 2;
	public static final int ID_TABLE = 3;
	public static final int ID_CONFIRM = 4;
	public static final int ID_MORE = 5;
	
	protected static final int ID_All = 6;
	
	protected View[] mViews = new View[ID_All];
	
	public ModuleBottombar(AfPageable page) {
		super(page,R.id.bottombar_layout);
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
			wrapped.setVisibility(View.GONE);
		}
	}
	
	public void setFunction(int id ,boolean value){
		if(id >= 0 && id < ID_All){
			if(value && wrapped.getVisibility() != View.VISIBLE){
				wrapped.setVisibility(View.VISIBLE);
			}
			mViews[id].setVisibility(value?View.VISIBLE:View.GONE);
		}
	}
	
	public void setHighLightMode(boolean value){
		if(isValid()){
			if(value){
				wrapped.setBackgroundResource(R.color.theme_titlebar_selcet_dg);
			}else{
				wrapped.setBackgroundResource(R.color.gray_dark);
			}	
		}
	}
	
	public void setListener(OnClickListener listener) {
		for (View view : mViews) {
			view.setOnClickListener(listener);
		}
	}

	public void setIdListener(int id,OnClickListener listener) {
		if(id >= 0 && id < ID_All){
			mViews[id].setOnClickListener(listener);
		}
	}

	public void setSelectListener(OnClickListener listener) {
		mViews[ID_SELECT].setOnClickListener(listener);
	}

	public void setAddListener(OnClickListener listener) {
		mViews[ID_ADD].setOnClickListener(listener);
	}
	public void setSearchListener(OnClickListener listener) {
		mViews[ID_SEARCH].setOnClickListener(listener);
	}

	public void setTableListener(OnClickListener listener) {
		mViews[ID_TABLE].setOnClickListener(listener);
	}
}
