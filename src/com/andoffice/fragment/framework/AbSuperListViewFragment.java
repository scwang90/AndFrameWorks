package com.andoffice.fragment.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;

import com.andframe.adapter.AfListAdapter;
import com.andframe.bean.Page;
import com.andframe.feature.AfBundle;
import com.andframe.model.framework.AfModel;
import com.andframe.thread.AfListViewTask;
import com.andframe.thread.AfTask;
import com.andframe.view.multichoice.AfMultiChoiceAdapter;
import com.andframe.view.multichoice.AfMultiChoiceAdapter.MultiChoiceListener;
import com.andframe.view.multichoice.AfMultiChoiceItem;
import com.andframe.view.multichoice.AfMultiChoiceListView;
import com.andframe.widget.popupmenu.OnMenuItemClickListener;
import com.andoffice.layoutbind.ModuleBottombar;
import com.andoffice.layoutbind.ModuleTitlebar;

/**
 * AbSuperListViewFragment
 * @author 树朾
 * 	在 AbListViewFragment 的基础上 添加多选功能
 * @param <T>
 */
public abstract class AbSuperListViewFragment<T extends AfModel> extends AbListViewFragment<T> implements MultiChoiceListener<T>, OnMenuItemClickListener{

	private static final int REQUEST_SELECT = 1;
	
	protected AfMultiChoiceAdapter<T> mMultiChoiceAdapter = null;
	
	protected abstract List<T> onTaskListFromDomain(int task,Page page)throws Exception;

	protected abstract AfMultiChoiceItem<T> getItemLayout(T data);

	@Override
	protected void onCreate(AfBundle bundle, AfMultiChoiceListView listview)
			throws Exception {
		// TODO Auto-generated method stub
		super.onCreate(bundle, listview);
		mBottombar.setSelectListener(this);
		mBottombar.setFunction(ModuleBottombar.ID_SELECT, true);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("选择", REQUEST_SELECT);
		mTitlebar.addMeuns(map);
		mTitlebar.setMenuItemListener(this);
		mTitlebar.setFunction(ModuleTitlebar.FUNCTION_MENU);
	}
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		if(mMultiChoiceAdapter!=null&&
				mMultiChoiceAdapter.isMultiChoiceMode()){
			mMultiChoiceAdapter.closeMultiChoice();
			return true;
		}else{
			return super.onBackPressed();
		}
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == REQUEST_SELECT){
			if(mMultiChoiceAdapter != null){
				mMultiChoiceAdapter.beginMultiChoice();
			}else{
				makeToastShort("还没有数据喔~");
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == ModuleBottombar.ID_SELECT){
			if(mMultiChoiceAdapter != null){
				mMultiChoiceAdapter.beginMultiChoice();
			}else{
				makeToastShort("还没有数据喔~");
			}
		}else{
			super.onClick(v);
		}
	}
	
	@Override
	public void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter,
			int number, int total) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter, T tag,
			boolean selected, int number) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMultiChoiceClosed(AfMultiChoiceAdapter<T> adapter,
			List<T> list) {
		// TODO Auto-generated method stub
		mBottombar.show();
	}
	@Override
	public void onMultiChoiceStarted(AfMultiChoiceAdapter<T> adapter, int number) {
		// TODO Auto-generated method stub
		mBottombar.hide();
	}
	
	@Override
	protected AfListViewTask<T> getTask(Handler handler, int task) {
		// TODO Auto-generated method stub
		return new AbListViewTask(handler,task);
	}

	@Override
	protected AfListAdapter<T> getAdapter(List<T> ltdata){
		mMultiChoiceAdapter = getMultiChoiceAdapter(ltdata);
		mMultiChoiceAdapter.addListener(this);
		return mMultiChoiceAdapter;
	}

	protected AfMultiChoiceAdapter<T> getMultiChoiceAdapter(List<T> ltdata){
		return new AbListViewAdapter(ltdata);
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		AfListViewTask<?> task = AfListViewTask.getTask(msg);
		if(task != null && this.onTaskTerminate(task)){
			return true;
		}
		return super.handleMessage(msg);
	}
	
	protected class AbListViewAdapter extends AfMultiChoiceAdapter<T>{

		public AbListViewAdapter(List<T> ltdata) {
			super(getActivity(), ltdata);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		protected AfMultiChoiceItem<T> getMultiChoiceItem(T data) {
			// TODO Auto-generated method stub
			return AbSuperListViewFragment.this.getItemLayout(data);
		}
	}
	
	protected class AbListViewTask extends AfListViewTask<T>{

		public AbListViewTask(Handler handler,int task) {
			super(handler, task);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public boolean onPrepare() {
			// TODO Auto-generated method stub
			return onTaskPrepare(mTask);
		}

		@Override
		protected List<T> onLoad() {
			// TODO Auto-generated method stub
			return onTaskLoad();
		}

		@Override
		protected List<T> onListByPage(Page page, int task) throws Exception {
			// TODO Auto-generated method stub
			return onTaskListFromDomain(mTask,page);
		}
		
		@Override
		protected boolean onWorking(int task) throws Exception {
			// TODO Auto-generated method stub
			return onTaskWorking(task);
		}

		@Override
		protected boolean onRefreshed(boolean isfinish, List<T> ltdata) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean onMored(boolean isfinish, List<T> ltdata,
				boolean ended) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	protected boolean onTaskPrepare(int task) {
		// TODO Auto-generated method stub
		return true;
	}
	
	protected List<T> onTaskLoad(){
		// TODO Auto-generated method stub
		return new ArrayList<T>();
	}

	protected List<T> onTaskMore(Page page) throws Exception{
		// TODO Auto-generated method stub
		return new ArrayList<T>();
	}

	protected boolean onTaskWorking(int task) throws Exception{
		// TODO Auto-generated method stub
		return false;
	}

	protected boolean onTaskTerminate(AfListViewTask<?> task) {
		// TODO Auto-generated method stub
		return super.handleMessage(AfTask.putTask(task));
	}
}
