package com.andframe.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.andframe.application.AfExceptionHandler;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfIntent;
import com.andframe.util.java.AfStackTrace;
/**
 * 框架 Activity
 * @author SCWANG
 */
public abstract class AfActivity extends com.andframe.activity.framework.AfActivity implements OnItemClickListener {

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
		super.onCreate(bundle);
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

	/**
	 * 转发 onBackPressed 事件给 AfFragment
	 */
	@Override
	public final void onBackPressed() {
		// TODO Auto-generated method stub
		if (AfStackTrace.isLoopCall()) {
			super.onBackPressed();
			return;
		}
		
		if(!this.onBackKeyPressed()){
			super.onBackPressed();
		}
	}

	/**
	 * 转发 onBackPressed 事件给 AfFragment
	 */
	protected boolean onBackKeyPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}
