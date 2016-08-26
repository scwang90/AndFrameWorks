package com.andframe.activity.framework;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.andframe.constant.AfPageConstant;
import com.andframe.feature.AfDialogBuilder;

import java.util.Date;

/**
 * 框架页面接口 AfPageable
 * @author 树朾
 * 继承了 AfViewable 
 */
@SuppressWarnings("unused")
public interface AfPageable extends AfViewable, AfPageConstant {

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
	
	void makeToastLong(CharSequence tip);

	void makeToastShort(CharSequence tip);

	void makeToastLong(CharSequence tip, Throwable e);
	/**
	 * 显示 资源ID 为 resid 的 Toast
	 */
	void makeToastLong(int resid);
	/**
	 * 显示 资源ID 为 resid 的 Toast
	 */
	void makeToastShort(int resid);

	/**
	 * 显示 进度对话框
	 * @param message
	 *            消息
	 */
	Dialog showProgressDialog(CharSequence message);

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param cancel 是否可取消
	 */
	Dialog showProgressDialog(CharSequence message, boolean cancel);
	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param cancel 是否可取消
	 * @param textsize 字体大小
	 */
	Dialog showProgressDialog(CharSequence message, boolean cancel,
			int textsize);

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 */
	Dialog showProgressDialog(CharSequence message, OnCancelListener listener);

	/**
	 * 显示 进度对话框
	 * @param message 消息
	 * @param textsize 字体大小
	 */
	Dialog showProgressDialog(CharSequence message, OnCancelListener listener, int textsize);

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
	Dialog doShowDialog(CharSequence title, CharSequence message);
	/**
	 * 显示对话框 并添加默认按钮 "我知道了"
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param lpositive 点击  "我知道了" 响应事件
	 */
	Dialog doShowDialog(CharSequence title, CharSequence message,OnClickListener lpositive);
	/**
	 * 显示对话框 
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 */
	Dialog doShowDialog(CharSequence title, CharSequence message,CharSequence positive,OnClickListener lpositive);
	/**
	 * 显示对话框 
	 * @param title 显示标题
	 * @param message 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	Dialog doShowDialog(CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative) ;

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
	Dialog doShowDialog(int iconres, CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative);

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
	Dialog doShowDialog(CharSequence title, CharSequence message,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral);
	
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
	Dialog doShowDialog(int iconres, CharSequence title, CharSequence message,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral);

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
	Dialog doShowDialog(int theme,int iconres, CharSequence title, CharSequence message,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral);
	
	/**
	 * 显示视图对话框 
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 */
	Dialog doShowViewDialog(CharSequence title, View view,CharSequence positive, OnClickListener lpositive);
	
	/**
	 * 显示视图对话框 
	 * @param title 显示标题
	 * @param view 显示内容
	 * @param positive 确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 * @param negative 按钮显示信息
	 * @param lnegative 点击  拒绝 按钮 响应事件
	 */
	Dialog doShowViewDialog(CharSequence title, View view, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative);
	
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
	Dialog doShowViewDialog(CharSequence title, View view,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) ;
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
	Dialog doShowViewDialog(int iconres, CharSequence title, View view, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative);

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
	Dialog doShowViewDialog(int iconres, CharSequence title, View view,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) ;

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
	Dialog doShowViewDialog(int theme,int iconres, CharSequence title, View view,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) ;

	/**
	 * 显示一个单选对话框 
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 * @param oncancel 取消选择监听器
	 */
	Dialog doSelectItem(CharSequence title,CharSequence[] items,OnClickListener listener, OnCancelListener oncancel);

	/**
	 * 显示一个单选对话框 （默认可取消）
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 */
	Dialog doSelectItem(CharSequence title,CharSequence[] items,OnClickListener listener);
	
	/**
	 * 显示一个单选对话框 （设置可取消）
	 * @param title 对话框标题
	 * @param items 选择菜单项
	 * @param listener 选择监听器
	 * @param cancel 是否可以取消
	 */
	Dialog doSelectItem(CharSequence title,CharSequence[] items,OnClickListener listener,boolean cancel);
	

	/**
	 * inputText 的监听器
	 */
	interface InputTextListener{
		boolean onInputTextComfirm(EditText input, String value);
	}

	/**
	 * 可取消的 InputTextListener
	 */
	abstract class InputTextCancelable implements InputTextListener{
		public void onInputTextCancel(EditText input) {}
	}

	/**
	 * 密码输入类型
	 */
	int INPUTTYPE_PASSWORD =  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
	
	/**
	 * 弹出一个文本输入框
	 * @param title 标题
	 * @param listener 监听器
	 */
	Dialog doInputText(CharSequence title, InputTextListener listener);

	/**
	 * 弹出一个文本输入框
	 * @param title 标题
	 * @param type android.text.InputType
	 * @param listener 监听器
	 */
	Dialog doInputText(CharSequence title,int type,InputTextListener listener);

	/**
	 * 弹出一个文本输入框
	 *
	 * @param title    标题
	 * @param defaul   默认值
	 * @param listener 监听器
	 */
	Dialog doInputText(CharSequence title, CharSequence defaul, InputTextListener listener);

	/**
	 * 弹出一个文本输入框
	 * @param title 标题
	 * @param defaul 默认值
	 * @param type android.text.InputType
	 * @param listener 监听器
	 */
	Dialog doInputText(CharSequence title,CharSequence defaul,int type,InputTextListener listener);

	/**
	 * 显示对话框 并添加默认按钮 "我知道了"
	 *
	 * @param key     不再显示KEY
	 * @param title   显示标题
	 * @param message 显示内容
	 */
	Dialog doShowDialog(String key, CharSequence title, CharSequence message) ;

	/**
	 * 显示对话框
	 *
	 * @param key       不再显示KEY
	 * @param title     显示标题
	 * @param message   显示内容
	 * @param positive  确认 按钮显示信息
	 * @param lpositive 点击  确认 按钮 响应事件
	 */
	Dialog doShowDialog(String key, CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive) ;

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
							 CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative) ;

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
							 CharSequence title, CharSequence message,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) ;

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
							 int iconres, CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative) ;

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
							 int iconres, CharSequence title, CharSequence message,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral);


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
							 CharSequence title, CharSequence message,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral);


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
	Dialog doSelectDateTime(CharSequence title, AfDialogBuilder.OnDateTimeSetListener listener);

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
	Dialog doSelectDateTime(final CharSequence title, final Date value, final AfDialogBuilder.OnDateTimeSetListener listener);

	/**
	 * 选择时间
	 * @param listener 监听器
	 */
	Dialog doSelectTime(TimePickerDialog.OnTimeSetListener listener);

	/**
	 * 选择时间
	 * @param title 标题
	 * @param listener 监听器
	 */
	Dialog doSelectTime(CharSequence title, TimePickerDialog.OnTimeSetListener listener);

	/**
	 * 选择时间
	 * @param value 默认时间
	 * @param listener 监听器
	 */
	Dialog doSelectTime(Date value, TimePickerDialog.OnTimeSetListener listener);

	/**
	 * 选择时间
	 * @param title 标题
	 * @param value 默认时间
	 * @param listener 监听器
	 */
	Dialog doSelectTime(CharSequence title, Date value, final TimePickerDialog.OnTimeSetListener listener);

	/**
	 * 选择日期
	 * @param listener 监听器
	 */
	Dialog doSelectDate(DatePickerDialog.OnDateSetListener listener);

	/**
	 * 选择日期
	 * @param title 标题
	 * @param listener 监听器
	 */
	Dialog doSelectDate(CharSequence title, DatePickerDialog.OnDateSetListener listener);

	/**
	 * 选择日期
	 * @param value 默认时间
	 * @param listener 监听器
	 */
	Dialog doSelectDate(Date value, DatePickerDialog.OnDateSetListener listener);

	/**
	 * 选择日期
	 * @param title 标题
	 * @param value 默认时间
	 * @param listener 监听器
	 */
	Dialog doSelectDate(CharSequence title, Date value, DatePickerDialog.OnDateSetListener listener);
}
