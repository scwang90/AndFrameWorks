package com.ontheway.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
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
import android.widget.Toast;

import com.ontheway.activity.framework.AfPageable;
import com.ontheway.application.AfApplication;
import com.ontheway.application.AfDaemonThread;
import com.ontheway.application.AfExceptionHandler;
import com.ontheway.exception.AfException;
import com.ontheway.exception.AfToastException;
import com.ontheway.fragment.AfFragment;
import com.ontheway.thread.AfTask;
import com.ontheway.thread.AfThreadWorker;
import com.ontheway.util.AfIntent;
import com.ontheway.util.AfStackTrace;
/**
 * 框架 Activity
 * @author SCWANG
 *
 *	以下是 Activity 像子类提供的 功能方法
 *
	protected void buildThreadWorker()
	 * 为本页面开启一个独立后台线程 供 postTask 的 任务(AfTask)运行 注意：开启线程之后 postTask
	 * 任何任务都会在该线程中运行。 如果 postTask 前一个任务未完成，后一个任务将等待
	 * 
	protected AfTask postTask(AfTask task)
	 * 抛送任务到Worker执行

	AfPageable 接口中的方法
	public Activity getActivity();
	public void makeToastLong(String tip);
	public void makeToastShort(String tip);
	public void makeToastLong(int resid);
	public void makeToastShort(int resid);
	public boolean getSoftInputStatus();
	public boolean getSoftInputStatus(View view);
	public void setSoftInputEnable(EditText editview, boolean enable);
	public void showProgressDialog(String message);
	public void showProgressDialog(String message, boolean cancel);
	public void showProgressDialog(String message, boolean cancel,int textsize);
	public void showProgressDialog(String message, listener);
	public void showProgressDialog(String message, listener, int textsize);
	public void hideProgressDialog();
	public void startActivity(Class<? extends AfActivity> tclass);
	public void startActivityForResult(Class<AfActivity> tclass,int request);
	
	public void doShowDialog(String title, String message);
	public void doShowDialog(String title, String message,OnClickListener);
	public void doShowDialog(String title, String message,String ,OnClickListener);
	public void doShowDialog(String, String,String,OnClickListener,String,OnClickListener);
	public void doShowDialog(String,String,String,Listener,String,Listener,String,Listener);
	public void doShowDialog(int,String,String,String,OnClickListener,String,OnClickListener);
	public void doShowDialog(int,String,String,String,Listener,String,Listener,String,Listener);
	
	public void doShowViewDialog(title, View view,String positive, OnClickListener );
	public void doShowViewDialog(title, View view,String positive, OnClickListener , String negative,OnClickListener );
	public void doShowViewDialog(title,view,String,Listener,String,Listener,String,Listener);
	public void doShowViewDialog(int iconres, title,  view,String, OnClickListener,String,OnClickListener );
	public void doShowViewDialog(int iconres,title,view,String,Listener,String,Listener,String,Listener);
	
	public void doSelectItem(String title,String[] items,OnClickListener);
	public void doSelectItem(String title,String[] items,OnClickListener,cancel);
	public void doSelectItem(String title,String[] items,OnClickListener,oncancel);
	
	public void doInputText(String title,InputTextListener listener);
	public void doInputText(String title,int type,InputTextListener listener);
	public void doInputText(String title,String defaul,int type,InputTextListener listener);
	
	AfPageListener 接口中的方法
	public void onSoftInputShown();
	public void onSoftInputHiden();
	public void onQueryChanged();
}
 */
public abstract class AfActivity extends FragmentActivity implements
		AfPageable, OnGlobalLayoutListener,OnItemClickListener {

	public static final String EXTRA_DATA = "EXTRA_DATA";
	public static final String EXTRA_INDEX = "EXTRA_INDEX";
	public static final String EXTRA_RESULT = "EXTRA_RESULT";

	public static final int LP_MP = LayoutParams.MATCH_PARENT;
	public static final int LP_WC = LayoutParams.WRAP_CONTENT;

	protected View mRoot = null;;
	protected ProgressDialog mProgress;
	protected AfThreadWorker mWorker = null;

	protected boolean mIsRecycled = false;

	/**
	 * @Description: 获取LOG日志 TAG 是 AfActivity 的方法
	 * 用户也可以重写自定义TAG,这个值AfActivity在日志记录时候会使用
	 * 子类实现也可以使用
	 * @Author: scwang
	 * @Version: V1.0, 2015-2-14 上午10:58:00
	 * @Modified: 初次创建TAG方法
	 * @return
	 */
	protected String TAG() {
		// TODO Auto-generated method stub
		return "AfActivity("+getClass().getName()+")";
	}
	protected String TAG(String tag) {
		// TODO Auto-generated method stub
		return "AfFragment("+getClass().getName()+")."+tag;
	}
	/**
	 * final 原始 onCreate(Bundle bundle)
	 * 子类只能重写 onCreate(Bundle bundle,AfIntent intent)
	 */
	@Override
	protected final void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		try {
			if (AfStackTrace.isLoopCall()) {
				//System.out.println("递归检测");
				return;
			}
			super.onCreate(bundle);
			this.onCreate(bundle, new AfIntent(getIntent()));
		} catch (final Throwable e) {
			// TODO: handle exception
			//handler 可能会根据 Activity 弹窗提示错误信息
			//当前 Activity 即将关闭，提示窗口也会关闭
			//用定时器 等到原始 Activity 再提示弹窗
			if (!(e instanceof AfToastException)) {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						AfExceptionHandler.handler(e, TAG()+".onCreate");
					}
				},500);
			}
			makeToastLong("页面启动失败",e);
			this.finish();
		}
	}

	/**
	 * 新的 onCreate 实现
	 * @param bundle
	 * @param intent 
	 * @throws Exception 安全异常
	 * 	重写的 时候 一般情况下请 调用
	 * 		super.onCreate(bundle,intent);
	 */
	protected void onCreate(Bundle bundle,AfIntent intent) throws Exception{
		if(bundle != null){
			AfApplication.getApp().onRestoreInstanceState();
		}
		AfApplication.getApp().setCurActivity(this, this);
	}
	
	/**
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 * final 重写 onActivityResult 使用 try-catch 调用 
	 * 		onActivityResult(AfIntent intent, int questcode,int resultcode)
	 * @see AfActivity#onActivityResult(AfIntent intent, int questcode,int resultcode)
	 * {@link AfActivity#onActivityResult(AfIntent intent, int questcode,int resultcode)}
	 */
	@Override
	protected final void onActivityResult(int questcode, int resultcode, Intent data) {
		// TODO Auto-generated method stub
		try {
			if (AfStackTrace.isLoopCall()) {
				//System.out.println("递归检测");
				return;
			}
			onActivityResult(new AfIntent(data), questcode, resultcode);
		} catch (Throwable e) {
			// TODO: handle exception
			if (!(e instanceof AfToastException)) {
				AfExceptionHandler.handler(e, TAG()+".onActivityResult");
			}
			makeToastLong("反馈信息读取错误！",e);
		}
	}
	/**
	 * @Description: final 包装 onItemClick 事件处理 防止抛出异常崩溃
	 * @Author: scwang
	 * @Version: V1.0, 2015-2-14 上午10:34:56
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public final void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try {
			if (AfStackTrace.isLoopCall()) {
				//System.out.println("递归检测");
				return;
			}
			this.onItemClick(parent,view,id,position);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, TAG()+".onItemClick");
		}
	}

	/**
	 * @Description: 
	 * 安全onItemClick框架会捕捉异常防止崩溃
	 * @Author: scwang
	 * @Version: V1.0, 2015-2-14 上午10:38:56
	 * @Modified: 初次创建onItemClick方法
	 * @param parent
	 * @param item
	 * @param id
	 * @param index
	 */
	protected void onItemClick(AdapterView<?> parent, View item, long id,int index) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 安全 onActivityResult(AfIntent intent, int questcode,int resultcode) 
	 * 在onActivityResult(int questCode, int resultCode, Intent data) 中调用
	 * 并使用 try-catch 提高安全性，子类请重写这个方法 
	 * @see AfActivity#onActivityResult(int, int, android.content.Intent)
	 * {@link AfActivity#onActivityResult(int, int, android.content.Intent)}
	 * @param intent
	 * @param questcode
	 * @param resultcode
	 */
	protected void onActivityResult(AfIntent intent, int questcode,int resultcode) {
		// TODO Auto-generated method stub
		super.onActivityResult(questcode, resultcode, intent);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		AfApplication.getApp().onSaveInstanceState();
		super.onSaveInstanceState(outState);
	}
	/**
	 * 获取 Application 的 AfApplication实例
	 * @return 如果 Application 不是 AfApplication 返回 null 
	 */
	public AfApplication getAfApplication(){
		Application app = getApplication();
		if (app instanceof AfApplication) {
			return AfApplication.class.cast(app);
		}
		return null;
	}
	
	/**
	 * 判断是否被回收
	 * @return true 已经被回收
	 */
	@Override
	public boolean isRecycled() {
		// TODO Auto-generated method stub
		return mIsRecycled;
	}

	/**
	 * 为了实现对软键盘输入法显示和隐藏 的监听重写了 setContentView
	 * 	子类在对 setContentView 重写的时候请调用 
	 * 		super.setContentView(res);
	 * 	否则不能对软键盘进行监听
	 */
	@Override
	public void setContentView(int res) {
		// TODO Auto-generated method stub
		setContentView(LayoutInflater.from(this).inflate(res, null));
	}

	/**
	 * 为了实现对软键盘输入法显示和隐藏 的监听重写了 setContentView
	 * 	子类在对 setContentView 重写的时候请调用 
	 * 		super.setContentView(view);
	 * 	否则不能对软键盘进行监听
	 */
	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		setContentView(view, new LayoutParams(LP_MP, LP_MP));
	}

	/**
	 * 为了实现对软键盘输入法显示和隐藏 的监听重写了 setContentView
	 * 	子类在对 setContentView 重写的时候请调用 
	 * 		super.setContentView(view,params);
	 * 	否则不能对软键盘进行监听
	 */
	@Override
	public void setContentView(View view, LayoutParams params) {
		// TODO Auto-generated method stub
		mRoot = view;
		super.setContentView(view, params);
		view.getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	/**
	 * 实现 onGlobalLayout 
	 * 	用于计算 软键盘的弹出和隐藏
	 * 	子类在对 onGlobalLayout 重写的时候请调用 
	 * 		super.onGlobalLayout();
	 * 	否则不能对软键盘进行监听
	 */
	private int lastdiff = -1;
	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		if(mRoot != null){
			int diff = mRoot.getRootView().getHeight() - mRoot.getHeight();
			if(lastdiff > -1){
				if(lastdiff < diff){
					this.onSoftInputShown();
				}else if(lastdiff > diff){
					this.onSoftInputHiden();
				}
			}
			lastdiff = diff;	
		}
//		if (diff > 100) {
//			// 大小超过100时，一般为显示虚拟键盘事件
//			this.onSoftInputShown();
//		} else {
//			// 大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
//			this.onSoftInputHiden();
//		}
	}

	/**
	 * onGlobalLayout 对软键盘的舰艇结果
	 * 当软键盘显示
	 */
	public void onSoftInputShown() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onGlobalLayout 对软键盘的舰艇结果
	 * 当面软键盘收起
	 */
	public void onSoftInputHiden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AfApplication.getApp().setCurActivity(this, this);
		this.onQueryChanged();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mIsRecycled = true;
		if (mWorker != null) {
			mWorker.quit();
		}
	}

	/**
	 * 查询系统数据变动
	 */
	public void onQueryChanged() {
		// TODO Auto-generated method stub
	}

	/**
	 * 为本页面开启一个独立后台线程 供 postTask 的 任务(AfTask)运行 注意：开启线程之后 postTask
	 * 任何任务都会在该线程中运行。 如果 postTask 前一个任务未完成，后一个任务将等待
	 */
	protected void buildThreadWorker() {
		// TODO Auto-generated method stub
		if (mWorker == null) {
			mWorker = new AfThreadWorker(this.getClass().getSimpleName());
		}
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void startActivity(Class<? extends AfActivity> tclass) {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, tclass));
	}

	@Override
	public void startActivityForResult(Class<? extends AfActivity> tclass,
			int request) {
		// TODO Auto-generated method stub
		startActivityForResult(new Intent(this, tclass), request);
	}
	
	@Override
	public boolean getSoftInputStatus() {
		// TODO Auto-generated method stub
		InputMethodManager imm = null;
		String Server = Context.INPUT_METHOD_SERVICE;
		imm = (InputMethodManager) getSystemService(Server);
		return imm.isActive();
	}
	
	@Override
	public boolean getSoftInputStatus(View view) {
		// TODO Auto-generated method stub
		InputMethodManager imm = null;
		String Server = Context.INPUT_METHOD_SERVICE;
		imm = (InputMethodManager) getSystemService(Server);
		return imm.isActive(view);
	}

	@Override
	public void setSoftInputEnable(EditText editview, boolean enable) {
		// TODO Auto-generated method stub
		InputMethodManager imm = null;
		String Server = Context.INPUT_METHOD_SERVICE;
		imm = (InputMethodManager) getSystemService(Server);
		if (enable) {
			editview.setFocusable(true);
			editview.setFocusableInTouchMode(true);
			editview.requestFocus();
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		} else {
			IBinder token;
			if(editview != null){
				token = editview.getWindowToken();
			}else{
				View focus = getCurrentFocus();
				if(focus!=null){
					token = focus.getWindowToken();
				}else{
					return;
				}
			}
			imm.hideSoftInputFromWindow(token, 0);
		}
	}

	@Override
	public void makeToastLong(int resid) {
		// TODO Auto-generated method stub
		Toast.makeText(this, resid, Toast.LENGTH_LONG).show();
	}

	@Override
	public void makeToastShort(int resid) {
		// TODO Auto-generated method stub
		Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void makeToastLong(String tip) {
		// TODO Auto-generated method stub
		Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
	}

	@Override
	public void makeToastShort(String tip) {
		// TODO Auto-generated method stub
		Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void makeToastLong(String tip,Throwable e) {
		// TODO Auto-generated method stub
		tip = AfException.handle(e, tip);
		Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
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
	public <T> T findViewById(int id, Class<T> clazz) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (clazz.isInstance(view)) {
			return clazz.cast(view);
		}
		return null;
	}

	/**
	 * 抛送任务到Worker执行
	 * @param task
	 */
	public AfTask postTask(AfTask task) {
		// TODO Auto-generated method stub
		if (mWorker != null) {
			return mWorker.postTask(task);
		}
		return AfDaemonThread.postTask(task);
	}

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 */
	public void showProgressDialog(String message) {
		// TODO Auto-generated method stub
		showProgressDialog(message, false, 25);
	}

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param cancel 是否可取消
	 */
	public void showProgressDialog(String message, boolean cancel) {
		// TODO Auto-generated method stub
		showProgressDialog(message, cancel, 25);
	}

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param cancel 是否可取消
	 * @param textsize 字体大小
	 */
	public void showProgressDialog(String message, boolean cancel,
			int textsize) {
		// TODO Auto-generated method stub
		mProgress = new ProgressDialog(this);
		mProgress.setMessage(message);
		mProgress.setCancelable(cancel);
		mProgress.setOnCancelListener(null);
		mProgress.show();

		setDialogFontSize(mProgress, textsize);
	}

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param cancel 是否可取消
	 * @param textsize 字体大小
	 */
	public void showProgressDialog(String message,
			OnCancelListener listener) {
		// TODO Auto-generated method stub
		mProgress = new ProgressDialog(this);
		mProgress.setMessage(message);
		mProgress.setCancelable(true);
		mProgress.setOnCancelListener(listener);
		mProgress.show();

		setDialogFontSize(mProgress, 25);
	}

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param cancel 是否可取消
	 * @param textsize 字体大小
	 */
	public void showProgressDialog(String message,
			OnCancelListener listener, int textsize) {
		// TODO Auto-generated method stub
		mProgress = new ProgressDialog(this);
		mProgress.setMessage(message);
		mProgress.setCancelable(true);
		mProgress.setOnCancelListener(listener);
		mProgress.show();

		setDialogFontSize(mProgress, textsize);
	}

	/**
	 * 隐藏 进度对话框
	 */
	public void hideProgressDialog() {
		// TODO Auto-generated method stub
		if (mProgress != null && !isRecycled()) {
			mProgress.dismiss();
			mProgress = null;
		}
	}

	/**
	 * 显示对话框 并添加默认按钮 "我知道了"
	 * @param title 显示标题
	 * @param message 显示内容
	 */
	public void doShowDialog(String title, String message) {
		doShowDialog(0,title,message,"我知道了", null, "", null);
	}
	/**
	 * 显示对话框 并添加默认按钮 "我知道了"
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param lpositive 点击  "我知道了" 响应事件
	 */
	public void doShowDialog(String title, String message,OnClickListener lpositive) {
		doShowDialog(0,title,message,"我知道了", lpositive, "", null);
	}
	/**
	 * 显示对话框 
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 */
	public void doShowDialog(String title, String message,String positive,OnClickListener lpositive) {
		doShowDialog(0,title,message,positive, lpositive, "", null);
	}
	/**
	 * 显示对话框 
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	public void doShowDialog(String title, String message,
			String positive, OnClickListener lpositive, String negative,
			OnClickListener lnegative) {
		doShowDialog(0,title,message,positive, lpositive,negative,lnegative);
	}
	/**
	 * 显示对话框 
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param neutral 详细 按钮显示信息
	 * @param lneutral 点击  详细 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	@Override
	public void doShowDialog(String title, String message,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative){
		doShowDialog(0, title, message,positive, lpositive, neutral, lneutral, negative,lnegative);
	}
	
	/**
	 * 显示对话框 
	 * @param iconres 对话框图标
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	public void doShowDialog(int iconres, String title, String message,
			String positive, OnClickListener lpositive, String negative,
			OnClickListener lnegative) {
		doShowDialog(iconres, title, message,positive, lpositive, "", null, negative,lnegative);
	}

	/**
	 * 显示对话框 
	 * @param iconres 对话框图标
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param neutral 详细 按钮显示信息
	 * @param lneutral 点击  详细 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	public void doShowDialog(int iconres, String title, String message,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) {
		// TODO Auto-generated method stub
		doShowDialog(-1, iconres, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
	}

	/**
	 * 显示视图对话框 
	 * @param theme 主题
	 * @param iconres 对话框图标
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param neutral 详细 按钮显示信息
	 * @param lneutral 点击  详细 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	@Override
	@SuppressLint("NewApi")
	public void doShowDialog(int theme, int iconres, 
			String title,String message, 
			String positive, OnClickListener lpositive,
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) {
		// TODO Auto-generated method stub
		Builder builder = null;
		try {
			builder = new Builder(getActivity());
		} catch (Throwable ex) {
			// TODO: handle exception
			return;
		}
		if (theme > 0) {
			try {
				builder = new Builder(getActivity(), theme);
			} catch (Throwable e) {
				// TODO: handle exception
				builder = new Builder(getActivity());
			}
		}
		builder.setTitle(title);
		builder.setMessage(message);
		if (iconres > 0) {
			builder.setIcon(iconres);
		}
		if (positive != null && positive.length() > 0) {
			builder.setPositiveButton(positive, lpositive);
		}
		if (neutral != null && neutral.length() > 0) {
			builder.setNeutralButton(neutral, lneutral);
		}
		if (negative != null && negative.length() > 0) {
			builder.setNegativeButton(negative, lnegative);
		}
		builder.setCancelable(false);
		builder.create();
		builder.show();
	}
	/**
	 * 显示视图对话框 
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 */
	@Override
	public void doShowViewDialog(String title, View view, String positive,
			OnClickListener lpositive) {
		// TODO Auto-generated method stub
		doShowViewDialog(title, view, positive, lpositive,"",null);
	}

	/**
	 * 显示视图对话框 
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	@Override
	public void doShowViewDialog(String title, View view, String positive,
			OnClickListener lpositive, String negative,
			OnClickListener lnegative) {
		// TODO Auto-generated method stub
		doShowViewDialog(0,title,view,positive, lpositive,negative,lnegative);
	}
	/**
	 * 显示视图对话框 
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param neutral 详细 按钮显示信息
	 * @param lneutral 点击  详细 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	@Override
	public void doShowViewDialog(String title, View view,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) {
		doShowViewDialog(0,title,view,positive, lpositive,neutral,lneutral,negative,lnegative);
	}
	/**
	 * 显示视图对话框 
	 * @param iconres 对话框图标
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	@Override
	public void doShowViewDialog(int iconres, String title, View view,
			String positive, OnClickListener lpositive, 
			String negative,OnClickListener lnegative) {
		doShowViewDialog(0,title,view,positive, lpositive,"",null,negative,lnegative);
	}
	/**
	 * 显示视图对话框 
	 * @param iconres 对话框图标
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param neutral 详细 按钮显示信息
	 * @param lneutral 点击  详细 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	@Override
	public void doShowViewDialog(int iconres, String title, View view,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) {
		// TODO Auto-generated method stub
		doShowViewDialog(-1, iconres, title, view, positive, lpositive, neutral, lneutral, negative, lnegative);
	}

	/**
	 * 显示视图对话框 
	 * @param theme 主题
	 * @param iconres 对话框图标
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param neutral 详细 按钮显示信息
	 * @param lneutral 点击  详细 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	@SuppressLint("NewApi")
	@Override
	public void doShowViewDialog(int theme, 
			int iconres, String title,View view, 
			String positive, OnClickListener lpositive,
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) {
		// TODO Auto-generated method stub
		Builder builder = null;
		try {
			builder = new Builder(getActivity());
		} catch (Throwable ex) {
			// TODO: handle exception
			return;
		}
		if (theme > 0) {
			try {
				builder = new Builder(getActivity(), theme);
			} catch (Throwable e) {
				// TODO: handle exception
				builder = new Builder(getActivity());
			}
		}
		builder.setTitle(title);
		RelativeLayout.LayoutParams lp = null;
		lp = new RelativeLayout.LayoutParams(LP_WC,LP_WC);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		RelativeLayout layout = new RelativeLayout(getActivity());
		layout.addView(view, lp);
		builder.setView(layout);
		if (iconres > 0) {
			builder.setIcon(iconres);
		}
		if (positive != null && positive.length() > 0) {
			builder.setPositiveButton(positive, lpositive);
		}
		if (neutral != null && neutral.length() > 0) {
			builder.setNeutralButton(neutral, lneutral);
		}
		if (negative != null && negative.length() > 0) {
			builder.setNegativeButton(negative, lnegative);
		}
		builder.setCancelable(false);
		builder.create();
		builder.show();
	}
	/**
	 * 显示一个单选对话框 （设置可取消）
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 * @param cancel 取消选择监听器
	 */
	public void doSelectItem(String title,String[] items,OnClickListener listener,
			boolean cancel){
		Builder dialog = new Builder(getActivity());
		dialog.setItems(items,listener);
		if(title != null){
			dialog.setTitle(title);
			dialog.setCancelable(false);
			if(cancel){
				dialog.setNegativeButton("取消", null);
			}
		}else{
			dialog.setCancelable(cancel);
		}
		dialog.show();
	}

	/**
	 * 显示一个单选对话框 
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 * @param oncancel 取消选择监听器
	 */
	public void doSelectItem(String title,String[] items,OnClickListener listener,
			final OnClickListener oncancel) {
		// TODO Auto-generated method stub
		Builder dialog = new Builder(getActivity());
		if(title != null){
			dialog.setTitle(title);
			dialog.setCancelable(false);
			dialog.setNegativeButton("取消", oncancel);
		}else if(oncancel != null){
			dialog.setCancelable(true);
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					oncancel.onClick(dialog, 0);
				}
			});
		}
		dialog.setItems(items,listener);
		dialog.show();
	}

	/**
	 * 显示一个单选对话框 （默认可取消）
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 */
	public void doSelectItem(String title,String[] items,OnClickListener listener) {
		// TODO Auto-generated method stub
		doSelectItem(title, items, listener, null);
	}

	/**
	 * 弹出一个文本输入框
	 * @param title 标题
	 * @param listener 监听器
	 */
	public void doInputText(String title,InputTextListener listener) {
		doInputText(title, "", InputType.TYPE_CLASS_TEXT, listener);
	}

	/**
	 * 弹出一个文本输入框
	 * @param title 标题
	 * @param type android.text.InputType
	 * @param listener 监听器
	 */
	public void doInputText(String title,int type,InputTextListener listener) {
		doInputText(title, "", type, listener);
	}

	/**
	 * 弹出一个文本输入框
	 * @param title 标题
	 * @param defaul 默认值
	 * @param type android.text.InputType
	 * @param listener 监听器
	 */
	public void doInputText(String title,String defaul,int type,InputTextListener listener) {
		final EditText input = new EditText(this);
		final int defaullength = defaul.length();
		final InputTextListener flistener = listener;
		input.setText(defaul);
		input.clearFocus();
		input.setInputType(type);
		Builder builder = new AlertDialog.Builder(this);
		builder.setView(input);
		builder.setCancelable(false);
		builder.setTitle(title);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				setSoftInputEnable(input, false);
				dialog.dismiss();
				flistener.onInputTextComfirm(input);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				setSoftInputEnable(input, false);
				dialog.dismiss();
				if(flistener instanceof InputTextCancelable){
					InputTextCancelable cancel = (InputTextCancelable)flistener;
					cancel.onInputTextCancel(input);
				}
			}
		});
		final AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			public void onShow(DialogInterface dialog) {
				setSoftInputEnable(input, true);
				input.setSelection(0,defaullength);
			}
		});
		dialog.show();
	}
	
	protected void setProgressDialogText(ProgressDialog dialog, String text) {
		Window window = dialog.getWindow();
		View view = window.getDecorView();
		setViewFontText(view, text);
	}

	private void setViewFontText(View view, String text) {
		// TODO Auto-generated method stub
		if (view instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) view;
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				setViewFontText(parent.getChildAt(i), text);
			}
		} else if (view instanceof TextView) {
			TextView textview = (TextView) view;
			textview.setText(text);
		}
	}

	protected void setDialogFontSize(Dialog dialog, int size) {
		Window window = dialog.getWindow();
		View view = window.getDecorView();
		setViewFontSize(view, size);
	}

	protected void setViewFontSize(View view, int size) {
		if (view instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) view;
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				setViewFontSize(parent.getChildAt(i), size);
			}
		} else if (view instanceof TextView) {
			TextView textview = (TextView) view;
			textview.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
		}
	}

	/**
	 * 转发 onKeyLongPress 事件给 AfFragment
	 */
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onKeyLongPress(keyCode,event) || isHandled;
			}
		}
		if(isHandled){
			return true;
		}
		return super.onKeyLongPress(keyCode, event);
	}

	/**
	 * 转发 onKeyShortcut 事件给 AfFragment
	 */
	@Override
	@SuppressLint("NewApi")
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onKeyShortcut(keyCode,event) || isHandled;
			}
		}
		if(isHandled){
			return true;
		}
		return super.onKeyShortcut(keyCode, event);
	}
	/**
	 * 转发 onKeyMultiple 事件给 AfFragment
	 */
	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onKeyMultiple(keyCode,repeatCount,event) || isHandled;
			}
		}
		if(isHandled){
			return true;
		}
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}

	/**
	 * 转发 onKeyUp 事件给 AfFragment
	 */
	@Override
	@SuppressLint("NewApi")
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onKeyUp(keyCode,event) || isHandled;
			}
		}
		if(isHandled){
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 转发 onKeyDown 事件给 AfFragment
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onKeyDown(keyCode,event) || isHandled;
			}
		}
		if(isHandled){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 转发 onBackPressed 事件给 AfFragment
	 */
	@Override
	public final void onBackPressed() {
		// TODO Auto-generated method stub
//		int index = 1;
//		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
//		StackTraceElement stack = stacks[index];
//		if (!stack.getClassName().equals(AfActivity.class.getName())) {
//			for (int i = 0; i < stacks.length; i++) {
//				if (AfActivity.class.getName().equals(stacks[i].getClassName())) {
//					index = i;
//					stack = stacks[i];
//					break;
//				}
//			}
//		}
//		for (int i = index + 1; i < stacks.length; i++) {
//			StackTraceElement element = stacks[i];
//			if (element.getClassName().equals(stack.getClassName())
//					&& element.getMethodName().equals(stack.getMethodName())) {
//				//System.out.println("递归检测");
//				super.onBackPressed();
//				return;
//			}
//		}
		if (AfStackTrace.isLoopCall()) {
			//System.out.println("递归检测");
			super.onBackPressed();
			return;
		}
		
		if(!this.onBackKeyPressed()){
			super.onBackPressed();
		}
//		boolean isHandled = false;
//		List<Fragment> fragments = getSupportFragmentManager().getFragments();
//		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
//		for (Fragment fragment : fragments) {
//			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
//				AfFragment afment = (AfFragment)fragment;
//				isHandled = afment.onBackPressed() || isHandled;
//			}
//		}
//		if(!isHandled){
//			super.onBackPressed();
//		}
	}

	/**
	 * 转发 onBackPressed 事件给 AfFragment
	 */
	protected boolean onBackKeyPressed() {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onBackPressed() || isHandled;
			}
		}
//		if (!isHandled) {
//			super.onBackPressed();
//			isHandled = true;
//		}
		return isHandled;
	}

}
