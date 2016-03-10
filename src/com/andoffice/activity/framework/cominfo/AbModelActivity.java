package com.andoffice.activity.framework.cominfo;

import android.os.Message;
import android.widget.TextView;

import com.andframe.feature.AfIntent;
import com.andframe.model.framework.AfModel;
import com.andoffice.activity.framework.AbModeuleListActivity;
import com.andoffice.domain.IDomain;
import com.andoffice.domain.impl.ImplDomain;

import java.util.ArrayList;
import java.util.List;

public abstract class AbModelActivity<T> extends AbCominfoActivity{

	protected static final int REQUEST_CHOOSERMODEL = 10;
	
	protected List<ModelSelector> mltSelector = new ArrayList<ModelSelector>();
	
	protected abstract T onSubmitModel(List<Project> ltproject, Mode mode);
	
	@Override
	protected void onSubmit(List<Project> ltproject, Mode mode) {
		T model = onSubmitModel(ltproject,mode);
		if(model != null){
			switch (mode) {
			case ADD:
				postTask(new AbCommodelTask(model,TASK_ADD));
				break;
			case EDIT:
				postTask(new AbCommodelTask(model,TASK_MODIFY));
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 任务成功执行完成之后 进行数据回写 并关闭当前页面
	 * 如果之类需要传递更多参数可以重写这个方法
	 * @param intent 回写载体 
	 * @param model 关联的 数据Model
	 * @return 返回true 成功写回（即将关闭页面）放回false 放弃回写将放弃关闭页面
	 */
	protected boolean onTaskWriteBack(AfIntent intent, T model) {
		intent.put(EXTRA_RESULT, model);
		return true;
	}

	/**
	 * 任务添加 执行
	 * 如果之类需要 更多操作 可以重写这个方法
	 * @param domain
	 * @param model
	 * @throws Exception
	 */
	protected void onTaskAdd(IDomain<T> domain, T model) throws Exception {
		domain.Insert(model);
	}

	/**
	 * 编辑任务 执行
	 * 如果之类需要 更多操作 可以重写这个方法
	 * @param domain
	 * @param model
	 * @throws Exception
	 */
	protected void onTaskEdit(IDomain<T> domain, T model) throws Exception {
		domain.Update(model);
	}
	
	protected class AbCommodelTask extends AbCommonTask<T>{

		public AbCommodelTask(T model, int task) {
			super(model, task);
		}
		
		@Override
		protected void onWorking(Message msg) throws Exception {
			IDomain<T> domain = getDomain(mClass);
			switch (mTask) {
			case TASK_ADD:
				onTaskAdd(domain,mModel);
				break;
			case TASK_MODIFY:
				onTaskEdit(domain,mModel);
				break;
			}
		}
		
		@Override
		protected boolean onWriteBack(AfIntent intent, T model) {
			return onTaskWriteBack(intent, model);
		}
	}

	protected <TM> Item buildSelectItem(Item item,Class<TM> clazz,Class<? extends AbModeuleListActivity<? extends TM>> activity) {
		mltSelector.add(new ModelSelector(item,clazz,activity));
		return item;
	}
	
	public IDomain<T> getDomain(Class<T> clazz) {
		return new ImplDomain<T>(clazz);
	}

	protected <TM> Item buildSelectItem(Item item,boolean single,Class<TM> clazz,Class<? extends AbModeuleListActivity<? extends TM>> activity) {
		mltSelector.add(new ModelSelector(item,single,clazz,activity));
		return item;
	}
	
	protected <TM> Item buildSelectItem(Item item,String title,boolean single,Class<TM> clazz,Class<? extends AbModeuleListActivity<? extends TM>> activity) {
		mltSelector.add(new ModelSelector(item,title,single,clazz,activity));
		return item;
	}
	
	@Override
	protected boolean onItemClick(Item item, TextView textview) {
		for (int i = 0; i < mltSelector.size(); i++) {
			ModelSelector selector = mltSelector.get(i);
			if(selector.item == item){
				AfIntent intent = new AfIntent(this,selector.selector);
				intent.put(AbModeuleListActivity.EXTRA_TITLE, selector.title);
				intent.put(AbModeuleListActivity.EXTRA_ISSINGLE, selector.single);
				intent.put(AbModeuleListActivity.EXTRA_ISSELECTION, true);
				if(selector.list != null && selector.list.size() > 0){
					intent.put(AbModeuleListActivity.EXTRA_SELECTIONS, selector.list);
				}
				startActivityForResult(intent, REQUEST_CHOOSERMODEL+i);
				return true;
			}
		}
		return super.onItemClick(item, textview);
	}
	
	@Override
	protected void onActivityResult(AfIntent intent, int requestcode,int resultcode) throws Exception {
		if(resultcode == RESULT_OK){
			for (int i = 0; i < mltSelector.size(); i++) {
				if(requestcode == REQUEST_CHOOSERMODEL + i){
					ModelSelector selector = mltSelector.get(i);
					if(selector.single){
						Object model = intent.get(EXTRA_RESULT, selector.clazz);
						if(model != null){
							onItemSelected(selector.item,model);
						}
					}else{
						List<?> ltmodel = intent.getList(EXTRA_RESULT, selector.clazz);
						if(ltmodel != null && ltmodel.size() > 0){
							onItemSelected(selector.item,ltmodel);
						}
					}
					return;
				}
			}
		}
		super.onActivityResult(intent, requestcode, resultcode);
	}

	/**
	 * 子类 调用 doBuildSelectorItem 并设置多选 之后，选择 完成的回调函数
	 * @param item 
	 * @param ltmodel 选择的多条数据
	 */
	protected void onItemSelected(Item item, List<?> ltmodel) {
		String text = "";
		for (Object model : ltmodel) {
			if (model instanceof AfModel) {
				text += AfModel.class.cast(model).Name + ",";
			}else {
				text += model + ",";
			}
		}
		text = text.substring(0, text.length() - 1);
		item.setValue(text);
		if (item.mTextView != null) {
			onItemInputFinish(item, item.mTextView, false);
		}
	}

	protected static class ModelSelector{
		public ModelSelector(Item item,Class<?> clazz,Class<?> activity) {
			this(item,"选择" + item.name,true,clazz,activity);
		}
		public ModelSelector(Item item, boolean single,Class<?> clazz,Class<?> activity) {
			this(item,"选择" + item.name,single,clazz,activity);
		}
		public ModelSelector(Item item, String title, boolean single,Class<?> clazz,Class<?> activity) {
			this.title = title;
			this.clazz = clazz;
			this.item = item;
			this.single = single;
			this.selector  = activity;
		}
		public Item item;
		public String title;
		public List<?> list;
		public Class<?> selector;
		public Class<? > clazz;
		public boolean single = true;
	}
}
