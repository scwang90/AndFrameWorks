package com.andoffice.activity.framework.cominfo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.andframe.activity.framework.AfActivity;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.AfDensity;
import com.andframe.feature.AfIntent;
import com.andframe.layoutbind.AfLayoutCheckBox;
import com.andframe.layoutbind.AfModuleTitlebarImpl;
import com.andframe.model.framework.AfModel;
import com.andframe.thread.AfHandlerTask;
import com.andframe.util.android.AfFileSelector;
import com.andframe.util.android.AfMeasure;
import com.andoffice.R;
import com.andoffice.activity.framework.AbModeuleListActivity;
import com.andoffice.application.AbApplication;
import com.andoffice.bean.Permission;
import com.andoffice.domain.IDomain;
import com.andoffice.domain.impl.ImplDomain;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class AbCominfoActivity extends AfActivity implements
		View.OnClickListener, OnCheckedChangeListener{

	protected static final int TASK_ADD = 10;
	protected static final int TASK_MODIFY = 20;
	
	protected static final int ID_DATE = 7;
	protected static final int ID_TIME = 8;
	protected static final int ID_DATETIME = 9;
	
	protected static final String TEXT_PASSWORD = "******";
	
	protected static final int REQUEST_FILECHOOSER = 1;

	
	public enum Mode {
		ADD,VIEW,EDIT
	}

	/** 标题控件 */
	protected AfModuleTitlebarImpl mTitlebar = null;

	/** 原控件样本 */
	protected View mViewDivider = null;
	protected Button mBtFinish = null;
	protected TextView mTvProjectName = null;
	protected TextView mTvItemName = null;
	protected TextView mTvItemValue = null;
	protected EditText mEtMultiline = null;
	protected EditText mEtSingleLine = null;
	protected CheckBox mCbItemCheck = null;
	protected LinearLayout mLayout = null;
	protected LinearLayout mLayoutProject = null;
	protected RelativeLayout mLayoutItem = null;

	protected Mode[] mMode = new Mode[1];
	/** 布局资源 */
	protected Integer type = TypedValue.COMPLEX_UNIT_PX;
	/** 布局数据接口 */
	protected List<DynamicProject> mltDynamic = new ArrayList<>();

	protected Item requestitem;
	protected TextView requesttext;
	protected Permission mPermission;	

	protected abstract void onCreate(AfIntent bundle, List<Project> list,Mode[] mode) throws Exception;

	/**
	 * 主要用于 附件等附带属性绑定 ModelID
	 */
	protected AfModel getModel() {
		return new AfModel();
	}

	/**
	 * 获取当前的模式 添加 编辑 查看
	 */
	public Mode getMode(){
		return mMode[0];
	}

	protected abstract void onSubmit(List<Project> ltproject,Mode mode);

	protected void onSubmitProject(Project project,Button bt, Mode mode) {
		bt.setEnabled(false);
	}

	@Override
	@SuppressLint("HandlerLeak")
	protected final void onCreate(Bundle bundle, AfIntent intent) throws Exception {
		super.onCreate(bundle, intent);
		if (mRootView == null) {
			setContentView(R.layout.layout_commonpanel);
		}

		mMode[0] = Mode.EDIT;
		mTitlebar = newModuleTitlebar();

		mBtFinish = findViewByID(R.id.button_finish);
		mLayout = findViewByID(R.id.commonpanel_layout);
		mViewDivider = findViewByID(R.id.commonpanel_divider);
		mLayoutProject = findViewByID(R.id.commonpanel_project);
		mTvProjectName = findViewByID(R.id.commonpanel_projectname);
		mLayoutItem = findViewByID(R.id.commonpanel_item);
		mTvItemName = findViewByID(R.id.commonpanel_itemname);
		mTvItemValue = findViewByID(R.id.commonpanel_itemvalue);
		mCbItemCheck = findViewByID(R.id.commonpanel_itemcheckbox);
		mEtMultiline = findViewByID(R.id.commonpanel_edit_multiline);
		mEtSingleLine = findViewByID(R.id.commonpanel_edit_singleline);
		
		mBtFinish.setText("完成");

		List<Project> projects = new ArrayList<>();
		onCreate(intent, projects, mMode);
		
		//根据权限把编辑改为查看
		String extra = AbModeuleListActivity.EXTRA_PERMISSION;
		mPermission = intent.get(extra,Permission.class);
		if(mPermission != null){
			if(!mPermission.IsModify && mMode[0] == Mode.EDIT){
				mMode[0] = Mode.VIEW;
			}
		}

		if (projects.size() > 0) {
			doBuildLayout(projects);
		}

		if (mMode[0] != Mode.VIEW && mBtFinish.getVisibility() == View.VISIBLE) {
			mTitlebar.setFunction(AfModuleTitlebarImpl.FUNCTION_OK);
			mTitlebar.setOnOkListener(this);
		}
	}

	@NonNull
	protected AfModuleTitlebarImpl newModuleTitlebar() {
		return new AfModuleTitlebarImpl(this);
	}

	/**
	 * 不可编辑的 如果是编辑模式的话 返回 Item.DISABLE
	 */
	protected int DisEdit(int type) {
		if(mMode[0] == Mode.EDIT){
			return Item.DISABLE;
		}
		return type;
	}

	/**
	 * 不可编辑的 如果是编辑模式的话 返回 Item.DISABLE
	 */
	protected Item DisEdit(Item item) {
		if(mMode[0] == Mode.EDIT){
			item.type = Item.DISABLE;
		}
		return item;
	}


	protected DynamicProject getDynamicProject(Project project) {
		for (DynamicProject dynpro : mltDynamic) {
			if(dynpro.mProject == project){
				return dynpro;
			}
		}
		return null;
	}
	
	protected void putProject(Project project){
		if (mMode[0] != Mode.VIEW) {
			mLayout.removeView(mBtFinish);
		}
		doBuildProject(project, mLayout);
		if (mMode[0] != Mode.VIEW) {
			mLayout.addView(mBtFinish);
		}
	}

	protected void doBuildLayout(List<Project> projects) {
		mLayout.removeAllViews();
		for (Project project : projects) {
			doBuildProject(project, mLayout);
		}
		if (mMode[0] != Mode.VIEW) {
			mLayout.addView(mBtFinish);
		}
	}

	protected void doBuildProject(Project project, LinearLayout layout) {
		DynamicProject dynamic = new DynamicProject(project);
		dynamic.mTvTitle = new TextView(this);
		dynamic.mTvTitle.setText(project.name);
		dynamic.mTvTitle.setTextColor(mTvProjectName.getTextColors());
		dynamic.mTvTitle.setTextSize(type, mTvProjectName.getTextSize());
		dynamic.mTvTitle.setLayoutParams(mTvProjectName.getLayoutParams());
		layout.addView(dynamic.mTvTitle);
		if((project.items.size() == 0 && project.custom == null) || project.name.equals("")){
			dynamic.mTvTitle.setVisibility(View.GONE);
		}
		
		dynamic.mLayout = new LinearLayout(this);
		dynamic.mLayout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(dynamic.mLayout);
		if (project.type == Project.TEXTBOX) {
			Item[] items = project.items.toArray(new Item[project.items.size()]);
			for (Item item : items) {
				doBuildTextBoxItem(item, dynamic.mLayout);
			}
		}else if (project.type == Project.CUSTOM) {
			if(project.mLayoutParams != null){
				project.custom.setLayoutParams(project.mLayoutParams);
			}
			dynamic.mLayout.addView(project.custom);
		}else/* if (project.type == Project.SELECTBAR) */{
			dynamic.mLayout.setBackgroundResource(R.drawable.frame_selectbar);
			dynamic.mLayout.setLayoutParams(mLayoutProject.getLayoutParams());

			Item[] items = project.items.toArray(new Item[project.items.size()]);
			for (Item item : items) {
				if (item.type == Item.BUTTON) {
					Button bt = new Button(this);
					bt.setText(item.value);
					bt.setContentDescription(item.name);
					bt.setLayoutParams(mBtFinish.getLayoutParams());
					bt.setBackgroundResource(R.drawable.selector_button_finish);
					bt.setOnClickListener(this);
					bt.setTextColor(mBtFinish.getTextColors());
					bt.setTextSize(type, mBtFinish.getTextSize());
					bt.setTag(project);
					layout.addView(bt);
					return;
				} else {
					doBuildSelectbarItem(item, dynamic.mLayout);
				}
			}
		}
		mltDynamic.add(dynamic);
	}

	private boolean doUninstallSelectbarItem(Project project,Item item, LinearLayout layout) {
		//排除不能删除的项
		if(item.type != Item.BUTTON && item.type < Item.TEXTBOXSINGLE){
			int index = project.items.indexOf(item);
			if(index >= 0){
				if(index > 0){
					/**
					 * index 		view（divider/itemview）
					 * 0 		- 		0
					 * 1 		- 		1/2
					 * 2 		- 		3/4
					 * 3 		- 		5/6
					 * 4 		- 		7/8
					 */
					layout.removeViewAt(2*index-1);//先删除分割线
					layout.removeViewAt(2*index-1);//删除条目（删除分割线之后上移了）
				}else if(project.items.size() > 1){
					layout.removeViewAt(0);//先删除条目
					layout.removeViewAt(0);//再删除分割线（删除分条目之后上移了）
				}else{
					layout.removeViewAt(0);
				}
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	protected void doBuildTextBoxItem(Item item, LinearLayout layout) {
		if(mMode[0] == Mode.VIEW || item.type == Item.DISABLE){
			item.mTextView = new TextView(this);
		}else{
			item.mTextView = new EditText(this);
		}
		
		TextView textview = item.mTextView;
		textview.setId(item.id);
		textview.setHint(item.name);
		textview.setFocusableInTouchMode(true);
		if(mMode[0] != Mode.ADD){
			textview.setText(item.value);
		}
		
		if(item.type != Item.TEXTBOXSINGLE){
			int left = mEtMultiline.getPaddingLeft();
			int top = mEtMultiline.getPaddingTop();
			int right = mEtMultiline.getPaddingRight();
			int bottom = mEtMultiline.getPaddingBottom();

			textview.setSingleLine(false);
			textview.setPadding(left, top, right, bottom);
			textview.setGravity(mEtMultiline.getGravity());
			textview.setTextSize(type,mEtMultiline.getTextSize());
			textview.setLayoutParams(mEtMultiline.getLayoutParams());
			textview.setMinimumHeight(AfDensity.dip2px(this, 120));
			textview.setBackgroundDrawable(mEtMultiline.getBackground());
		}else{
			int left = mEtSingleLine.getPaddingLeft();
			int top = mEtSingleLine.getPaddingTop();
			int right = mEtSingleLine.getPaddingRight();
			int bottom = mEtSingleLine.getPaddingBottom();
			
			textview.setSingleLine(true);
			textview.setPadding(left, top, right, bottom);
			textview.setGravity(mEtSingleLine.getGravity());
			textview.setTextSize(type,mEtSingleLine.getTextSize());
			textview.setLayoutParams(mEtSingleLine.getLayoutParams());
			textview.setBackgroundDrawable(mEtSingleLine.getBackground());
		}
		if(!item.isEmpty() && mMode[0] == Mode.EDIT){
			this.onItemInputFinish(item, textview, true);
		}
		layout.addView(textview);
	}

	@SuppressWarnings("deprecation")
	protected void doBuildSelectbarItem(Item item, LinearLayout projectlayout) {
		if (projectlayout.getChildCount() > 0) {
			View divider = new View(this);
			divider.setBackgroundDrawable(mViewDivider.getBackground());
			projectlayout.addView(divider,mViewDivider.getLayoutParams());
		}
		RelativeLayout itemlayout = new RelativeLayout(this);
		itemlayout.setLayoutParams(mLayoutItem.getLayoutParams());
		itemlayout.setBackgroundDrawable(mLayoutItem.getBackground());
		projectlayout.addView(itemlayout);
		TextView itemname = new TextView(this);
		itemname.setHint(item.name);
		itemname.setSingleLine(true);
		itemname.setGravity(mTvItemName.getGravity());
		itemname.setTextColor(mTvItemName.getTextColors());
		itemname.setLayoutParams(mTvItemName.getLayoutParams());
		itemname.setTextSize(type, mTvItemName.getTextSize());
		if (item.type != Item.CHECK && item.type != Item.CHECKPOWER) {
			TextView itemvalue = item.mTextView = new TextView(this);
			itemvalue.setSingleLine(true);
			itemvalue.setGravity(mTvItemValue.getGravity());
			itemvalue.setTextColor(mTvItemValue.getTextColors());
			itemvalue.setLayoutParams(mTvItemValue.getLayoutParams());
			itemvalue.setTextSize(type, mTvItemValue.getTextSize());
			if(item.notnull){
				itemvalue.setHint("未设置");
			}
			itemvalue.setTag(item);
			itemvalue.setId(item.id);
			///
			if ((item.value != null && item.value.trim().length() > 0) 
					&& (mMode[0] != Mode.ADD || item.type == Item.DISABLE || !item.notnull)
				/*	|| (item.id != Item.ID_DEFAULT && itemvalue.getText().length() == 0)*/) {
				itemvalue.setText(item.value);
				if(item.type == Item.PASSWORD && !item.value.equals("")){
					itemvalue.setText(TEXT_PASSWORD);
				}
				if(!item.isEmpty()){
					this.onItemInputFinish(item, itemvalue, true);
				}
			}
			if ((mMode[0] != Mode.VIEW && item.type != Item.DISABLE)
					|| item.id != Item.ID_DEFAULT) {
				itemvalue.setOnClickListener(this);
				itemvalue.setBackgroundResource(R.drawable.selector_background);
			}
			///
			int left = AfMeasure.measureView(itemname).x;
			int right = mTvItemValue.getPaddingRight();
			itemvalue.setPadding(left + right, 0, right, 0);
			itemlayout.addView(itemvalue);
		}else{
			CheckBox checkbox = new CheckBox(this);
			checkbox.setChecked(item.blvalue);
			checkbox.setLayoutParams(mCbItemCheck.getLayoutParams());
//			checkbox.setBackgroundDrawable(mCbItemCheck.getBackground());
//			checkbox.setButtonDrawable(mCbItemCheck.getBackground());
//			checkbox.setOnClickListener(this);
			checkbox.setTag(item);
			checkbox.setOnCheckedChangeListener(this);
			if(item.type == Item.CHECKPOWER){
				checkbox.setButtonDrawable(R.drawable.selector_checkbox);
				checkbox.setBackgroundResource(R.drawable.selector_checkbox);
			}
			itemlayout.addView(checkbox);
			itemlayout.setBackgroundResource(R.drawable.selector_background);
			// 帮助点击 外布局 改变 checkbox 状态
			new AfLayoutCheckBox(this,checkbox);
		}
		itemlayout.addView(itemname);
		if(mTvItemName.getText().toString().length() < item.name.length()){
			mTvItemName.setText(item.name);
		}
	}
	
	@Override
	public void onClick(View v) {
		if (v != null &&(v.getId() == mTitlebar.getRightImgId()
				|| v.getId() == R.id.button_finish)) {
			doSubmit();
			return;
		}
		if(v instanceof Button && v.getTag() instanceof Project){
			Project project = (Project)v.getTag();
			try {
				project.doCheckNotnull();
				this.onSubmitProject(project,(Button)v,mMode[0]);
			} catch (Throwable e) {
				makeToastLong("",e);
			}
		}else if (v instanceof TextView && v.getTag() instanceof Item) {
			Item item = (Item) v.getTag();
			TextView textview = (TextView)v;
			if (!onItemClick(item, textview)) {
			}
		}
	}

	protected void doSubmit() {
		try {
			for (Project project : mltDynamic) {
				project.doCheckNotnull();
			}
		} catch (Throwable e) {
			makeToastLong("",e);
			return;
		}
		this.onSubmit(new ArrayList<Project>(mltDynamic),mMode[0]);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		if(view instanceof CheckBox && view.getTag() instanceof Item){
			Item item = (Item) view.getTag();
			item.blvalue = isChecked;
			item.binding(isChecked);
		}
	}
	
	@Override
	protected void onActivityResult(AfIntent intent, int requestcode,
			int resultcode) throws Exception {
		super.onActivityResult(intent, requestcode, resultcode);
		if (resultcode == RESULT_OK) {
			switch (requestcode) {
			case REQUEST_FILECHOOSER:
				String path = AfFileSelector.onActivityFileResult(this,resultcode, intent);
				File file = new File(path);
				if (file.exists() && file.isFile()) {
				}
				break;
			}
		}
		requestitem = null;
		requesttext = null;
	}

	protected boolean onItemClick(Item item, TextView textview) {
		if(item.type == Item.SELECTOR_DATE){
			doSelectDate(item,textview,ID_DATE);
		}else if(item.type == Item.SELECTOR_TIME){
			doSelectTime(item,textview,ID_TIME);
		}else if(item.type == Item.SELECTOR_DATETIME){
			doSelectDateTime(item,textview,ID_DATETIME);
		}else if (item.values != null && item.values.length > 0) {
			doSelectItem(item, textview);
		} else {
			doInputText(item,textview);
		}
		return true;
	}

	/**
	 * 当Item设置完成之后
	 * @param item 对应 Item
	 * @param view 对应 TextView
	 * @param init 标记是否初始化时候
	 */
	protected void onItemInputFinish(Item item, TextView view,boolean init) {
	}

	/**
	 * 弹出选择对话框
	 * @param item 对应 Item
	 * @param textview 对应 TextView
	 */
	protected void doSelectItem(final Item item, final TextView textview) {
		final String[] items = item.values;
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请选择" + item.name);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				item.ivalue = which;
				item.blvalue = which > 0;
				item.value = items[which];
				textview.setText(items[which]);
				if(!item.binding(which) && !item.binding(items[which]))
				{
					item.binding(item.blvalue);
				}
				onItemInputFinish(item, textview, false);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	protected void doInputText(final Item item, final TextView textview) {
		final EditText input = new EditText(this);
		input.setText(item.value);
		input.clearFocus();
		if (item.type == Item.NUMBER) {
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
		} else if (item.type == Item.PHONE) {
			input.setInputType(InputType.TYPE_CLASS_PHONE);
		} else if (item.type == Item.PASSWORD) {
			input.setText("");
			input.setInputType(InputType.TYPE_CLASS_TEXT 
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		Builder builder = new AlertDialog.Builder(this);
		builder.setView(input);
		builder.setCancelable(false);
		builder.setTitle("请输入" + item.name);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setSoftInputEnable(input, false);
				String text = input.getText().toString();
				if (!text.equals("")) {
					item.value = text;
					textview.setText(text);
					if(item.type == Item.NUMBER){
						try {
							item.dvalue = Double.valueOf(text);
							item.ivalue = (int) item.dvalue;
							item.fvalue = (float) item.dvalue;
						} catch (Throwable ex) {
							AfExceptionHandler.handle(ex,"AbCommonActivity.doInputText.onClick.valueOf 出现异常");
						}
					}else if(item.type == Item.PASSWORD){
						item.value = AbApplication.getApp().encryptionPassword(text);
						textview.setText(TEXT_PASSWORD);
					}
					Object[] values = new Object[]{text,item.dvalue,item.fvalue,item.lvalue,item.ivalue};
					for (Object object : values) {
						if(item.binding(object)){
							break;
						}
					}
					onItemInputFinish(item, textview, false);
				}
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setSoftInputEnable(input, false);
				dialog.dismiss();
			}
		});
		final AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			public void onShow(DialogInterface dialog) {
				setSoftInputEnable(input, true);
				if(item.type != Item.PASSWORD){
					input.setSelection(0,item.value.length());
				}
			}
		});
		dialog.show();
	}

	protected void doSelectDateTime(Item item, TextView textview, int id) {
		final Item fitem = item;
		final TextView ftext = textview;
		final Calendar calender = Calendar.getInstance();
		calender.setTime(item.vdate);
		int year = calender.get(Calendar.YEAR);
		int month = calender.get(Calendar.MONTH);
		int day = calender.get(Calendar.DAY_OF_MONTH);
		final Dialog tDialog = new DatePickerDialog(this, new OnDateSetListener() {
			private boolean fdealwith = false;
			@Override
			public void onDateSet(DatePicker view, final int year, final int month,final int day) {
				if(fdealwith){
					return ;
				}
				fdealwith = true;
				int hour = calender.get(Calendar.HOUR_OF_DAY);
				int minute = calender.get(Calendar.MINUTE);
				Dialog tDialog = new TimePickerDialog(getActivity(), new OnTimeSetListener() {
					private boolean fdealwith = false;
					@Override
					public void onTimeSet(TimePicker view, int hour, int minute) {
						if(fdealwith){
							return ;
						}
						fdealwith = true;
						onItemDateTimeSet(fitem,ftext, year, month,day, hour, minute);
					}
				}, hour, minute, true);
				tDialog.show();
				tDialog.setCancelable(true);
			}
		}, year, month, day);
		tDialog.show();
		tDialog.setCancelable(true);
	}
	
	protected void doSelectTime(Item item, TextView textview, int id) {
		final Item fitem = item;
		final TextView ftext = textview;
		Calendar calender = Calendar.getInstance();
		calender.setTime(item.vdate);
		int hour = calender.get(Calendar.HOUR_OF_DAY);
		int minute = calender.get(Calendar.MINUTE);
		Dialog tDialog = new TimePickerDialog(this, new OnTimeSetListener() {
			private boolean fdealwith = false;
			@Override
			public void onTimeSet(TimePicker view, int hour, int minute) {
				if(fdealwith){
					return ;
				}
				fdealwith = true;
				onItemTimeSet(fitem,ftext,view, hour, minute);
			}
		}, hour, minute, true);
		tDialog.show();
		tDialog.setCancelable(true);
	}

	protected void doSelectDate(Item item, TextView textview, int id) {
		final Item fitem = item;
		final TextView ftext = textview;
		Calendar calender = Calendar.getInstance();
		calender.setTime(item.vdate);
		int year = calender.get(Calendar.YEAR);
		int month = calender.get(Calendar.MONTH);
		int day = calender.get(Calendar.DAY_OF_MONTH);
		Dialog tDialog = new DatePickerDialog(this, new OnDateSetListener() {
			private boolean fdealwith = false;
			@Override
			public void onDateSet(DatePicker view, final int year, final int month,final int day) {
				if(fdealwith){
					return ;
				}
				fdealwith = true;
				onItemDateSet(fitem,ftext,view, year, month,day);
			}
		}, year, month, day);
		tDialog.show();
		tDialog.setCancelable(true);
	}

	protected void onItemDateSet(Item item, TextView text, DatePicker view,
			int year, int month, int day) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(item.vdate);
		calender.set(Calendar.YEAR, year);
		calender.set(Calendar.MONTH, month);
		calender.set(Calendar.DAY_OF_MONTH, day);
		item.vdate = calender.getTime();
		item.formatdate(item.vdate);
		text.setText(item.value);
		item.binding(item.vdate);
	}
	
	protected void onItemTimeSet(Item item, TextView text, TimePicker view,
			int hour, int minute) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(item.vdate);
		calender.set(Calendar.HOUR_OF_DAY, hour);
		calender.set(Calendar.MINUTE,minute);
		item.vdate = calender.getTime();
		item.formattime(item.vdate);
		text.setText(item.value);
		item.binding(item.vdate);
	}

	protected void onItemDateTimeSet(Item item, TextView text, int year, int month, int day,int hour, int minute) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(item.vdate);
		calender.set(Calendar.YEAR, year);
		calender.set(Calendar.MONTH, month);
		calender.set(Calendar.DAY_OF_MONTH, day);
		calender.set(Calendar.HOUR_OF_DAY, hour);
		calender.set(Calendar.MINUTE,minute);
		item.vdate = calender.getTime();
		item.formatdatetime(item.vdate);
		text.setText(item.value);
		item.binding(item.vdate);
	}

	protected void onItemSelected(Item item,Object model) {
		if (model instanceof AfModel) {
			item.setValue(AfModel.class.cast(model).Name);
		}else if(model != null){
			item.setValue(model.toString());
		}else {
			item.setValue("");
		}
		if (item.mTextView != null) {
			onItemInputFinish(item, item.mTextView, false);
		}
	}


	protected class DynamicProject extends Project{
	
		public Project mProject;
		public TextView mTvTitle;
		public LinearLayout mLayout;

		public boolean mIsShown = true;

		public DynamicProject(Project project) {
			super(project);
			mProject = project;
		}
	
		public void dynHide() {
			if(mIsShown){
				mIsShown = false;
				mLayout.setVisibility(View.GONE);
				mTvTitle.setVisibility(View.GONE);
			}
		}
		
		public void dynShow() {
			if(!mIsShown){
				mIsShown = true;
				mLayout.setVisibility(View.VISIBLE);
				setFocusableInTouchMode(mLayout,true);
				if(mTvTitle.getText().length() > 0){
					mTvTitle.setVisibility(View.VISIBLE);
				}
			}
		}
		public void dynClear() {
			items.clear();
			mLayout.removeAllViews();
			mTvTitle.setVisibility(View.GONE);
		}
	
		public void dynSetTitle(String title) {
			mTvTitle.setText(title);
			if(mTvTitle.getVisibility() != View.VISIBLE){
				if(!title.equals("")){
					mTvTitle.setVisibility(View.VISIBLE);
				}
			}
		}
	
		public void dynPut(Item item) {
			items.add(item);
			doBuildSelectbarItem(item, mLayout);
			if(mTvTitle.getVisibility() != View.VISIBLE){
				if(mTvTitle.getText().length() > 0){
					mTvTitle.setVisibility(View.VISIBLE);
				}
			}
		}
		/**
		 * 动态的 在页面中删除对应的 Item
		 * @param item 删除对应的 Item
		 * @return 是否成功删除
		 */
		public boolean dynUninstall(Item item) {
			int index = items.indexOf(item);
			if(index >= 0 && doUninstallSelectbarItem(this,item, mLayout)){
				items.remove(index);
				return true;
			}
			return false;
		}
		/**
		 * 检查内部 Item 是否全部为非空 
		 */
		@Override
		public void doCheckNotnull() throws Exception {
			if(!mIsShown){//如果已经被隐藏，则忽略检查，默认全部非空并通过
				return;
			}
			super.doCheckNotnull();
		}
		
		protected void setFocusableInTouchMode(View view, boolean mode) {
			if (view instanceof ViewGroup) {
				ViewGroup parent = (ViewGroup) view;
				int count = parent.getChildCount();
				for (int i = 0; i < count; i++) {
					setFocusableInTouchMode(parent.getChildAt(i), mode);
				}
			} else if (view instanceof TextView) {
				TextView textview = (TextView) view;
				textview.setFocusableInTouchMode(true);
			}
		}

	}

	protected class AbCommonTask<T> extends AfHandlerTask {

		protected T mModel;
		protected Class<T> mClass;
		protected int mTask;
		
		@SuppressWarnings("unchecked")
		public AbCommonTask(T model,int task) {
//			super(task);
			mTask = task;
			mModel = model;
			mClass = (Class<T>)model.getClass();
		}
		
		@Override
		public boolean onPrepare() {
			showProgressDialog("正在提交请求...");
			if(getSoftInputStatus()){
				setSoftInputEnable(null, false);
			}
			return super.onPrepare();
		}

		@Override
		protected void onWorking(/*Message msg*/) throws Exception {
			IDomain<T> domain = new ImplDomain<>(mClass);
			switch (mTask) {
			case TASK_ADD:
				domain.Insert(mModel);
				break;
			case TASK_MODIFY:
				domain.Update(mModel);
				break;
			}
		}

		@Override
		protected boolean onHandle(/*Message msg*/) {
			hideProgressDialog();
			if (mResult == RESULT_FINISH) {// TASK_ADD and TASK_MODIFY
				makeToastShort("操作成功完成！");
				AfIntent intent = new AfIntent();
				if (onWriteBack(intent,mModel)) {
					setResult(RESULT_OK,intent);
					finish();
				}
			} else {
				makeToastShort(makeErrorToast(""));
			}
			return true;
		}

		/**
		 * 任务成功执行完成之后 进行数据回写 并关闭当前页面
		 * @param intent 回写载体 
		 * @param model 关联的 数据Model
		 * @return 返回true 成功写回（即将关闭页面）放回false 放弃回写将放弃关闭页面
		 */
		protected boolean onWriteBack(AfIntent intent,T model) {
			intent.put(EXTRA_RESULT, model);
			return true;
		}
	}
}
