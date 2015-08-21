package com.andframe.feature;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.AfViewable;
import com.andframe.annotation.view.BindCheckedChange;
import com.andframe.annotation.view.BindClick;
import com.andframe.annotation.view.BindItemClick;
import com.andframe.annotation.view.BindItemLongClick;
import com.andframe.annotation.view.BindLongClick;
import com.andframe.annotation.view.BindView;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.framework.EventListener;
import com.andframe.layoutbind.framework.AfViewDelegate;
import com.andframe.util.java.AfReflecter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 控件绑定器
 * @author 树朾
 */
public class AfViewBinder {

	private Object mHandler;

	public AfViewBinder(Object handler) {
		mHandler = handler;
	}
	
	protected String TAG(String tag) {
		return "AfViewBinder("+mHandler.getClass().getName()+")."+tag;
	}
	
	public void doBind() {
		if (mHandler instanceof AfViewable) {
			this.doBind((AfViewable)(mHandler));
		}
	}
	
	public void doBind(View root) {
		this.doBind(new AfView(root));
	}
	
	public void doBind(AfViewable root) {
		doBindClick(root);
		doBindLongClick(root);
		doBindItemClick(root);
		doBindItemLongClick(root);
		doBindCheckedChange(root);
		doBindView(root);
	}

	private Class<?> getStopType(){
		if (mHandler instanceof AfViewDelegate){
			return AfViewDelegate.class;
		}
		return Object.class;
	}

	public void doBindClick(AfViewable root) {
		for (Method method : AfReflecter.getMethodAnnotation(mHandler.getClass(), getStopType(), BindClick.class)) {
			try {
				BindClick bind = method.getAnnotation(BindClick.class);
				for (int id : bind.value()){
					View view = root.findViewById(id);
					view.setOnClickListener(new EventListener(mHandler).click(method));
				}
			} catch (Exception e) {
				AfExceptionHandler.handler(e,TAG("doBindLongClick.")+ method.getName());
			}
		}
	}

	public void doBindLongClick(AfViewable root) {
		for (Method method : AfReflecter.getMethodAnnotation(mHandler.getClass(), getStopType(), BindLongClick.class)) {
			try {
				BindLongClick bind = method.getAnnotation(BindLongClick.class);
				for (int id : bind.value()){
					View view = root.findViewById(id);
					view.setOnLongClickListener(new EventListener(mHandler).longClick(method));
				}
			} catch (Exception e) {
				AfExceptionHandler.handler(e,TAG("doBindLongClick.")+ method.getName());
			}
		}
	}

	public void doBindItemClick(AfViewable root) {
		for (Method method : AfReflecter.getMethodAnnotation(mHandler.getClass(), getStopType(), BindItemClick.class)) {
			try {
				BindItemClick bind = method.getAnnotation(BindItemClick.class);
				for (int id : bind.value()){
					AdapterView view = root.findViewByID(id);
					if (view != null){
						view.setOnItemClickListener(new EventListener(mHandler).itemClick(method));
					}
				}
			} catch (Exception e) {
				AfExceptionHandler.handler(e,TAG("doBindLongClick.")+ method.getName());
			}
		}
	}

	public void doBindItemLongClick(AfViewable root) {
		for (Method method : AfReflecter.getMethodAnnotation(mHandler.getClass(), getStopType(), BindItemLongClick.class)) {
			try {
				BindItemLongClick bind = method.getAnnotation(BindItemLongClick.class);
				for (int id : bind.value()){
					AdapterView view = root.findViewByID(id);
					if (view != null){
						view.setOnItemLongClickListener(new EventListener(mHandler).itemLongClick(method));
					}
				}
			} catch (Exception e) {
				AfExceptionHandler.handler(e,TAG("doBindLongClick.")+ method.getName());
			}
		}
	}

	public void doBindCheckedChange(AfViewable root) {
		for (Method method : AfReflecter.getMethodAnnotation(mHandler.getClass(), getStopType(), BindCheckedChange.class)) {
			try {
				BindCheckedChange bind = method.getAnnotation(BindCheckedChange.class);
				for (int id : bind.value()){
					CompoundButton view = root.findViewByID(id);
					if (view != null){
						view.setOnCheckedChangeListener(new EventListener(mHandler).checkedChange(method));
					}
				}
			} catch (Exception e) {
				AfExceptionHandler.handler(e,TAG("doBindLongClick.")+ method.getName());
			}
		}
	}

	public void doBindView(AfViewable root) {
		for (Field field : AfReflecter.getFieldAnnotation(mHandler.getClass(), getStopType(), BindView.class)) {
			try {
				BindView bind = field.getAnnotation(BindView.class);
				List<View> list = new ArrayList<View>();
				for (int id : bind.value()){
					int viewId = id;
					View view = root.findViewById(viewId);
					if (view != null){
						if (bind.click() && mHandler instanceof OnClickListener) {
							view.setOnClickListener((OnClickListener) mHandler);
						}
						list.add(view);
					}
				}
				if (list.size() > 0){
					field.setAccessible(true);
					if (field.getType().isArray()){
						Class<?> componentType = field.getType().getComponentType();
						Object[] array = list.toArray((Object[]) Array.newInstance(componentType, list.size()));
						field.set(mHandler, array);
					} else {
						field.set(mHandler, list.get(0));
					}
				}
			} catch (Exception e) {
				AfExceptionHandler.handler(e,TAG("doBindView.")+ field.getName());
			}
		}
	}

}
