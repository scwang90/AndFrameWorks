package com.andframe.activity.framework;


import android.app.Activity;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.andframe.thread.AfTask;
/**
 * 框架页面接口 AfPageable
 * @author 树朾
 * 继承了 AfViewable 
 */
public interface AfPageable extends AfViewable,AfSoftInputPageListener {

	/**
	 * 抛送任务到Worker执行
	 * @param task 任务对象
	 */
	AfTask postTask(AfTask task);
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
	void showProgressDialog(String message);

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param cancel 是否可取消
	 */
	void showProgressDialog(String message, boolean cancel);
	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param cancel 是否可取消
	 * @param textsize 字体大小
	 */
	void showProgressDialog(String message, boolean cancel,
			int textsize);

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 */
	void showProgressDialog(String message,
			OnCancelListener listener);

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param textsize 字体大小
	 */
	void showProgressDialog(String message,
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
	 * @param tclass
	 * 	省去创建 Intent 的代码
	 */
	void startActivity(Class<? extends AfActivity> tclass);

	/**
	 * 快速启动 AfActivity ForResult
	 * @param tclass
	 * @param request
	 * 	省去创建 Intent 的代码
	 */
	void startActivityForResult(Class<? extends AfActivity> tclass,int request);
	

	/**
	 * 显示对话框 并添加默认按钮 "我知道了"
	 * @param title 显示标题
	 * @param message 显示内容
	 */
	void doShowDialog(String title, String message);
	/**
	 * 显示对话框 并添加默认按钮 "我知道了"
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param lpositive 点击  "我知道了" 响应事件
	 */
	void doShowDialog(String title, String message,OnClickListener lpositive);
	/**
	 * 显示对话框 
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 */
	void doShowDialog(String title, String message,String positive,OnClickListener lpositive);
	/**
	 * 显示对话框 
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	void doShowDialog(String title, String message,
			String positive, OnClickListener lpositive, String negative,
			OnClickListener lnegative) ;

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
	void doShowDialog(int iconres, String title, String message,
			String positive, OnClickListener lpositive, String negative,
			OnClickListener lnegative);

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
	void doShowDialog(String title, String message,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative);
	
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
	void doShowDialog(int iconres, String title, String message,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative);

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
	void doShowDialog(int theme,int iconres, String title, String message,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative);
	
	/**
	 * 显示视图对话框 
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 */
	void doShowViewDialog(String title, View view,String positive, OnClickListener lpositive);
	
	/**
	 * 显示视图对话框 
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	void doShowViewDialog(String title, View view,
			String positive, OnClickListener lpositive, String negative,
			OnClickListener lnegative);
	
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
	void doShowViewDialog(String title, View view,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) ;
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
	void doShowViewDialog(int iconres, String title, View view,
			String positive, OnClickListener lpositive, String negative,
			OnClickListener lnegative);

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
	void doShowViewDialog(int iconres, String title, View view,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) ;

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
	void doShowViewDialog(int theme,int iconres, String title, View view,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) ;

	/**
	 * 显示一个单选对话框 
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 * @param oncancel 取消选择监听器
	 */
	void doSelectItem(String title,String[] items,OnClickListener listener,OnClickListener oncancel);

	/**
	 * 显示一个单选对话框 （默认可取消）
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 */
	void doSelectItem(String title,String[] items,OnClickListener listener);
	
	/**
	 * 显示一个单选对话框 （设置可取消）
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 * @param cancel 是否可以取消
	 */
	void doSelectItem(String title,String[] items,OnClickListener listener,boolean cancel);
	

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
	void doInputText(String title,int type,InputTextListener listener);

	/**
	 * 弹出一个文本输入框
	 * @param title 标题
	 * @param defaul 默认值
	 * @param type android.text.InputType
	 * @param listener 监听器
	 */
	void doInputText(String title,String defaul,int type,InputTextListener listener);

	/**
	 * 显示对话框 并添加默认按钮 "我知道了"
	 *
	 * @param key     不再显示KEY
	 * @param title   显示标题
	 * @param message 显示内容
	 */
	void doShowDialog(String key, String title, String message) ;

	/**
	 * 显示对话框
	 *
	 * @param key       不再显示KEY
	 * @param title     显示标题
	 * @param message   显示内容
	 * @param positive  确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 */
	void doShowDialog(String key, String title, String message, String positive, OnClickListener lpositive) ;

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
	void doShowDialog(String key, int defclick,
							 String title, String message,
							 String positive, OnClickListener lpositive,
							 String negative, OnClickListener lnegative) ;

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
	void doShowDialog(String key, int defclick,
							 String title, String message,
							 String positive, OnClickListener lpositive,
							 String neutral, OnClickListener lneutral,
							 String negative, OnClickListener lnegative) ;

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
	void doShowDialog(String key, int defclick,
							 int iconres, String title, String message,
							 String positive, OnClickListener lpositive,
							 String negative, OnClickListener lnegative) ;

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
	void doShowDialog(String key, int defclick,
							 int iconres, String title, String message,
							 String positive, OnClickListener lpositive,
							 String neutral, OnClickListener lneutral,
							 String negative, OnClickListener lnegative);


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
	void doShowDialog(String key, int defclick,
							 int theme, int iconres,
							 String title, String message,
							 String positive, OnClickListener lpositive,
							 String neutral, OnClickListener lneutral,
							 String negative, OnClickListener lnegative);
}
