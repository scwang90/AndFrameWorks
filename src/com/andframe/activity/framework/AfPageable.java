package com.andframe.activity.framework;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.andframe.feature.AfDialogBuilder;
import com.andframe.thread.AfTask;

import java.util.Date;

/**
 * 框架页面接口 AfPageable
 * @author 树朾
 * 继承了 AfViewable 
 */
@SuppressWarnings("unused")
public interface AfPageable extends AfViewable, AfSoftInputListener {

	String EXTRA_DATA = "EXTRA_DATA";//通用数据传递标识
	String EXTRA_INDEX = "EXTRA_INDEX";//通用下标栓地标识
	String EXTRA_RESULT = "EXTRA_RESULT";//通用返回传递标识
	String EXTRA_MAIN = "EXTRA_MAIN";//主要数据传递标识
	String EXTRA_DEPUTY = "EXTRA_DEPUTY";//主要数据传递标识

	int LP_MP = ViewGroup.LayoutParams.MATCH_PARENT;
	int LP_WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	/**
	 * 抛送任务到Worker执行
	 * @param task 任务对象
	 */
	<T extends AfTask> T postTask(T task);
	/**
	 * 判断是否被回收
	 * @return true 已经被回收
	 */
	boolean isRecycled();
	/**
	 * 查询系统数据变动
	 */
	void onQueryChanged();
	/**
	 * 获取页面相关的 Activity
	 * @return 相关的 Activity
	 */
	Activity getActivity();
	
	String getString(int resid);

	void makeToastLong(String tip);

	void makeToastShort(String tip);

	void makeToastLong(String tip, Throwable e);
	/**
	 * 显示 资源ID 为 resid 的 Toast
	 * @param resid
	 */
	void makeToastLong(int resid);
	/**
	 * 显示 资源ID 为 resid 的 Toast
	 * @param resid
	 */
	void makeToastShort(int resid);

	/**
	 * 获取软键盘大打开状态
	 * @return true 打开 false 关闭
	 */
	boolean getSoftInputStatus();
	
	/**
	 * 获取软键盘大打开状态
	 * @param view 关联view
	 * @return true 打开 false 关闭
	 */
	boolean getSoftInputStatus(View view);
	
	/**
	 * 设置软键盘显示和关闭
	 * @param enable 显示或者关闭
	 */
	void setSoftInputEnable(EditText editview, boolean enable);

	/**
	 * 显示 进度对话框
	 * @param message
	 *            消息
	 */
	Dialog showProgressDialog(String message);

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param cancel 是否可取消
	 */
	Dialog showProgressDialog(String message, boolean cancel);
	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param cancel 是否可取消
	 * @param textsize 字体大小
	 */
	Dialog showProgressDialog(String message, boolean cancel,
			int textsize);

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 */
	Dialog showProgressDialog(String message,
			OnCancelListener listener);

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param textsize 字体大小
	 */
	Dialog showProgressDialog(String message,
			OnCancelListener listener, int textsize);

	/**
	 * 隐藏 进度对话框
	 */
	void hideProgressDialog();

	/**
	 * 启动 Activity
	 */
	void startActivity(Intent intent);
	/**
	 * 快速启动 AfActivity
	 * 	省去创建 Intent 的代码
	 */
	void startActivity(Class<? extends Activity> clazz);
	/**
	 * 快速启动 AfActivity
	 * 	省去创建 Intent 的代码
	 */
	void startActivity(Class<? extends Activity> clazz,Object... args);

	/**
	 * 快速启动 AfActivity ForResult
	 * 	省去创建 Intent 的代码
	 */
	void startActivityForResult(Class<? extends Activity> clazz,int request);

	/**
	 * 快速启动 AfActivity ForResult
	 * 	省去创建 Intent 的代码
	 */
	void startActivityForResult(Class<? extends Activity> clazz,int request,Object... args);
	

	/**
	 * 显示对话框 并添加默认按钮 "我知道了"
	 * @param title 显示标题
	 * @param message 显示内容
	 */
	Dialog doShowDialog(String title, String message);
	/**
	 * 显示对话框 并添加默认按钮 "我知道了"
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param lpositive 点击  "我知道了" 响应事件
	 */
	Dialog doShowDialog(String title, String message,OnClickListener lpositive);
	/**
	 * 显示对话框 
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 */
	Dialog doShowDialog(String title, String message,String positive,OnClickListener lpositive);
	/**
	 * 显示对话框 
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	Dialog doShowDialog(String title, String message, String negative, OnClickListener lnegative, String positive, OnClickListener lpositive) ;

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
	Dialog doShowDialog(int iconres, String title, String message, String negative, OnClickListener lnegative, String positive, OnClickListener lpositive);

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
	Dialog doShowDialog(String title, String message, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral, String positive, OnClickListener lpositive);
	
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
	Dialog doShowDialog(int iconres, String title, String message, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral, String positive, OnClickListener lpositive);

	/**
	 * 显示对话框 
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
	Dialog doShowDialog(int theme,int iconres, String title, String message, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral, String positive, OnClickListener lpositive);
	
	/**
	 * 显示视图对话框 
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 */
	Dialog doShowViewDialog(String title, View view,String positive, OnClickListener lpositive);
	
	/**
	 * 显示视图对话框 
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	Dialog doShowViewDialog(String title, View view, String negative, OnClickListener lnegative, String positive, OnClickListener lpositive);
	
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
	Dialog doShowViewDialog(String title, View view, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral, String positive, OnClickListener lpositive) ;
	/**
	 * 显示视图对话框 
	 * @param iconres 对话框图标
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 按钮显示信息
	 * @param lpositive 点击  positive 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  negative 按钮 响应事件
	 */
	Dialog doShowViewDialog(int iconres, String title, View view, String negative, OnClickListener lnegative, String positive, OnClickListener lpositive);

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
	Dialog doShowViewDialog(int iconres, String title, View view, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral, String positive, OnClickListener lpositive) ;

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
	Dialog doShowViewDialog(int theme,int iconres, String title, View view, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral, String positive, OnClickListener lpositive) ;

	/**
	 * 显示一个单选对话框 
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 * @param oncancel 取消选择监听器
	 */
	Dialog doSelectItem(String title,String[] items,OnClickListener listener,OnClickListener oncancel);

	/**
	 * 显示一个单选对话框 （默认可取消）
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 */
	Dialog doSelectItem(String title,String[] items,OnClickListener listener);
	
	/**
	 * 显示一个单选对话框 （设置可取消）
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 * @param cancel 是否可以取消
	 */
	Dialog doSelectItem(String title,String[] items,OnClickListener listener,boolean cancel);
	

	/**
	 * doInputText 的监听器
	 * @author 树朾
	 */
	interface InputTextListener{
		void onInputTextComfirm(EditText input);
	}

	/**
	 * 可取消的 InputTextListener
	 * @author 树朾
	 */
	interface InputTextCancelable extends InputTextListener{
		void onInputTextCancel(EditText input);
	}

	/**
	 * 密码输入类型
	 */
	static final int INPUTTYPE_PASSWORD =  InputType.TYPE_CLASS_TEXT 
	| InputType.TYPE_TEXT_VARIATION_PASSWORD;
	
	/**
	 * 弹出一个文本输入框
	 * @param title 标题
	 * @param listener 监听器
	 */
	void doInputText(String title,InputTextListener listener);

	/**
	 * 弹出一个文本输入框
	 * @param title 标题
	 * @param type android.text.InputType
	 * @param listener 监听器
	 */
	Dialog doInputText(String title,int type,InputTextListener listener);

	/**
	 * 弹出一个文本输入框
	 * @param title 标题
	 * @param defaul 默认值
	 * @param type android.text.InputType
	 * @param listener 监听器
	 */
	Dialog doInputText(String title,String defaul,int type,InputTextListener listener);

	/**
	 * 显示对话框 并添加默认按钮 "我知道了"
	 *
	 * @param key     不再显示KEY
	 * @param title   显示标题
	 * @param message 显示内容
	 */
	Dialog doShowDialog(String key, String title, String message) ;

	/**
	 * 显示对话框
	 *
	 * @param key       不再显示KEY
	 * @param title     显示标题
	 * @param message   显示内容
	 * @param positive  确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 */
	Dialog doShowDialog(String key, String title, String message, String positive, OnClickListener lpositive) ;

	/**
	 * 显示对话框
	 *
	 * @param key       不再显示KEY
	 * @param defclick  不再显示之后默认执行index
	 * @param title     显示标题
	 * @param message   显示内容
	 * @param positive  确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative  按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	Dialog doShowDialog(String key, int defclick,
							 String title, String message, String negative, OnClickListener lnegative, String positive, OnClickListener lpositive) ;

	/**
	 * 显示对话框
	 *
	 * @param key       不再显示KEY
	 * @param defclick  不再显示之后默认执行index
	 * @param title     显示标题
	 * @param message   显示内容
	 * @param positive  确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param neutral   详细 按钮显示信息
	 * @param lneutral  点击  详细 按钮 响应事件
	 * @param negative  按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	Dialog doShowDialog(String key, int defclick,
							 String title, String message, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral, String positive, OnClickListener lpositive) ;

	/**
	 * 显示对话框
	 *
	 * @param key       不再显示KEY
	 * @param defclick  不再显示之后默认执行index
	 * @param iconres   对话框图标
	 * @param title     显示标题
	 * @param message   显示内容
	 * @param positive  确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative  按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	Dialog doShowDialog(String key, int defclick,
							 int iconres, String title, String message, String negative, OnClickListener lnegative, String positive, OnClickListener lpositive) ;

	/**
	 * 显示对话框
	 *
	 * @param key       不再显示KEY
	 * @param defclick  不再显示之后默认执行index
	 * @param iconres   对话框图标
	 * @param title     显示标题
	 * @param message   显示内容
	 * @param positive  确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param neutral   详细 按钮显示信息
	 * @param lneutral  点击  详细 按钮 响应事件
	 * @param negative  按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	Dialog doShowDialog(String key, int defclick,
							 int iconres, String title, String message, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral, String positive, OnClickListener lpositive);


	/**
	 * 显示视图对话框
	 *
	 * @param key       不再显示KEY
	 * @param defclick  不再显示之后默认执行index
	 * @param theme     主题
	 * @param iconres   对话框图标
	 * @param title     显示标题
	 * @param message   显示内容
	 * @param positive  确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param neutral   详细 按钮显示信息
	 * @param lneutral  点击  详细 按钮 响应事件
	 * @param negative  按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	Dialog doShowDialog(String key, int defclick,
							 int theme, int iconres,
							 String title, String message, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral, String positive, OnClickListener lpositive);


	/**
	 * 选择日期时间
	 * @param listener 监听器
	 */
	Dialog doSelectDateTime(AfDialogBuilder.OnDateTimeSetListener listener);

	/**
	 * 选择日期时间
	 * @param title 标题
	 * @param listener 监听器
	 */
	Dialog doSelectDateTime(String title, AfDialogBuilder.OnDateTimeSetListener listener);

	/**
	 * 选择日期时间
	 * @param value 默认时间
	 * @param listener 监听器
	 */
	Dialog doSelectDateTime(Date value, AfDialogBuilder.OnDateTimeSetListener listener);

	/**
	 * 选择日期时间
	 * @param title 标题
	 * @param value 默认时间
	 * @param listener 监听器
	 */
	Dialog doSelectDateTime(final String title, final Date value, final AfDialogBuilder.OnDateTimeSetListener listener);

	/**
	 * 选择时间
	 * @param listener 监听器
	 */
	Dialog doSelectTime(AfDialogBuilder.OnTimeSetListener listener);

	/**
	 * 选择时间
	 * @param title 标题
	 * @param listener 监听器
	 */
	Dialog doSelectTime(String title, AfDialogBuilder.OnTimeSetListener listener);

	/**
	 * 选择时间
	 * @param value 默认时间
	 * @param listener 监听器
	 */
	Dialog doSelectTime(Date value, AfDialogBuilder.OnTimeSetListener listener);

	/**
	 * 选择时间
	 * @param title 标题
	 * @param value 默认时间
	 * @param listener 监听器
	 */
	Dialog doSelectTime(String title, Date value, final AfDialogBuilder.OnTimeSetListener listener);

	/**
	 * 选择日期
	 * @param listener 监听器
	 */
	Dialog doSelectDate(AfDialogBuilder.OnDateSetListener listener);

	/**
	 * 选择日期
	 * @param title 标题
	 * @param listener 监听器
	 */
	Dialog doSelectDate(String title, AfDialogBuilder.OnDateSetListener listener);

	/**
	 * 选择日期
	 * @param value 默认时间
	 * @param listener 监听器
	 */
	Dialog doSelectDate(Date value, AfDialogBuilder.OnDateSetListener listener);

	/**
	 * 选择日期
	 * @param title 标题
	 * @param value 默认时间
	 * @param listener 监听器
	 */
	Dialog doSelectDate(String title, Date value, AfDialogBuilder.OnDateSetListener listener);
}
