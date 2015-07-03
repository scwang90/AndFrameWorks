package com.andoffice.layoutbind;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.andoffice.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.AfLayoutAlpha;

public class ModuleTitlebarSearcher extends AfLayoutAlpha implements OnClickListener, OnEditorActionListener, OnMenuItemClickListener {
	
	@SuppressWarnings("serial")
	public static class SearchOptions extends ArrayList<Entry<String, String>>{
		public SearchOptions() {
			// TODO Auto-generated constructor stub
		}

		public void put(String key, String value) {
			// TODO Auto-generated method stub
			add(new SimpleEntry<String, String>(key,value));
		}
	}

	public interface SearcherListener{
		boolean onSearch(ModuleTitlebarSearcher searcher,String option,String keyword);
	}
	
	private View mVwSearch = null;
	private TextView mTvOption = null;
	private EditText mEtKeyword = null;
	private SearcherListener mListener = null;
	private Entry<String,String> mEyOption = new AbstractMap.SimpleEntry<String,String>("","");
	
	private List<Entry<String, String>> mltOptions = new ArrayList<Entry<String,String>>();

	private AfPageable mPage = null;
	
	public ModuleTitlebarSearcher(AfPageable page) {
		super(page);
		// TODO Auto-generated constructor stub
		mPage = page;
		if(isValid()){
			mVwSearch = page.findViewByID(R.id.titlebar_search_go);
			mTvOption = page.findViewByID(R.id.titlebar_search_option);
			mEtKeyword = page.findViewByID(R.id.titlebar_search_keyword);
			
			mTvOption.setText("搜索");
			mTvOption.setOnClickListener(this);
			mVwSearch.setOnClickListener(this);
			mEtKeyword.setOnEditorActionListener(this);
		}
	}
	
	protected View findLayout(AfViewable view) {
		// TODO Auto-generated method stub
		return view.findViewById(R.id.titlebar_search_layout);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == mTvOption){
			PopupMenu popmenu = new PopupMenu(v.getContext(),v);
			for (int i = 0; i < mltOptions.size(); i++) {
				Entry<String,String> entry = mltOptions.get(i);
				popmenu.getMenu().add(0, i, 0, entry.getValue());
			}
			popmenu.setOnMenuItemClickListener(this);
			popmenu.show();
		}else if(v == mVwSearch){
			String keyword = mEtKeyword.getText().toString();
			if(mListener.onSearch(this, mEyOption.getKey(), keyword)){
				this.hide();
			}else if(!keyword.equals("")){
				mPage.setSoftInputEnable(mEtKeyword, false);
			}
		}
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if(EditorInfo.IME_ACTION_SEARCH == actionId){
			String keyword = mEtKeyword.getText().toString();
			if(mListener.onSearch(this, mEyOption.getKey(), keyword)){
				this.hide();
			}else if(!keyword.equals("")){
				mPage.setSoftInputEnable(mEtKeyword, false);
			}
		}else if(EditorInfo.IME_ACTION_DONE == actionId){
			mPage.makeToastShort("IME_ACTION_DONE");
		}else if(EditorInfo.IME_ACTION_NONE == actionId){
			mPage.makeToastShort("IME_ACTION_NONE");
		}else if(EditorInfo.IME_NULL == actionId){
			mPage.makeToastShort("IME_NULL");
		}
		return true;
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		mEyOption = mltOptions.get(item.getItemId());
		mTvOption.setText(mEyOption.getValue());
		return true;
	}
	
	public void setListener(SearcherListener mListener) {
		// TODO Auto-generated method stub
		this.mListener = mListener;
	}			

	public void setOptions(HashMap<String, String> options){
		// TODO Auto-generated method stub
		if(isValid() && options != null && !options.equals("")){
			mltOptions = new ArrayList<Entry<String,String>>();
			Iterator<Entry<String, String>> iter = options.entrySet().iterator();
			while (iter.hasNext()) {
				mltOptions.add(iter.next());
			}
			mTvOption.setText(mltOptions.get(0).getValue());
			if(mltOptions.size() > 0){
				mEyOption = mltOptions.get(0);
			}
		}
	}
	
	public void setOptions(Collection<Entry<String, String>> options){
		// TODO Auto-generated method stub
		if(isValid() && options != null && !options.equals("")){
			mltOptions = new ArrayList<Entry<String,String>>(options);
			mTvOption.setText(mltOptions.get(0).getValue());
			if(mltOptions.size() > 0){
				mEyOption = mltOptions.get(0);
			}
		}
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		if(isValid()){
			super.show();
			mPage.setSoftInputEnable(mEtKeyword, true);
		}
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		if(isValid()){
			super.hide();
			mPage.setSoftInputEnable(mEtKeyword, false);
		}
	}
}
