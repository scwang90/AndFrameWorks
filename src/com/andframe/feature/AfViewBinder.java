package com.andframe.feature;

import java.lang.reflect.Field;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;

import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.AfViewable;
import com.andframe.annotation.view.BindView;
import com.andframe.annotation.view.Select;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.framework.EventListener;
import com.andframe.util.java.AfReflecter;
import com.andframe.util.java.AfStringUtil;
/**
 * ¿Ø¼þ°ó¶¨Æ÷
 * @author SCWANG
 */
public class AfViewBinder {

	private Object mHandler;

	public AfViewBinder(Object handler) {
		// TODO Auto-generated constructor stub
		mHandler = handler;
	}
	
	protected String TAG(String tag) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		for (Field field : AfReflecter.getFieldAnnotation(mHandler.getClass(),BindView.class)) {
            try {
            	BindView bind = field.getAnnotation(BindView.class);
            	int viewId = bind.id();
            	boolean clickLis = bind.click();
                View view = root.findViewById(viewId);
                
                if (clickLis && mHandler instanceof OnClickListener) {
                	view.setOnClickListener((OnClickListener)mHandler);
                }
                
                setOnClickListener(view,bind.onClick());
				setLongClickListener(view,bind.longClick());
				setItemClickListener(view,bind.itemClick());
				setItemLongClickListener(view,bind.itemLongClick());
				
				Select select = bind.select();
				if(AfStringUtil.isNotEmpty(select.selected())){
					setViewSelectListener(view,select.selected(),select.noSelected());
				}
				field.setAccessible(true);
                field.set(mHandler, view);
            } catch (Exception e) {
               AfExceptionHandler.handler(e,TAG("initBindView.")+ field.getName());
            }
		}
	}

	private void setViewSelectListener(View view, String selected,String noSelected) {
		// TODO Auto-generated method stub
		if(view instanceof AbsListView){
			((AbsListView)view).setOnItemSelectedListener(new EventListener(this).select(selected).noSelect(noSelected));
		}
	}

	private void setItemLongClickListener(View view, String method) {
		// TODO Auto-generated method stub
		if (AfStringUtil.isNotEmpty(method) && view instanceof AbsListView) {
			AbsListView listView = AbsListView.class.cast(view);
			listView.setOnItemLongClickListener(new EventListener(mHandler).itemLongClick(method));
		}
	}

	private void setItemClickListener(View view, String method) {
		// TODO Auto-generated method stub
		if (AfStringUtil.isNotEmpty(method) && view instanceof AbsListView) {
			AbsListView listView = AbsListView.class.cast(view);
			listView.setOnItemClickListener(new EventListener(mHandler).itemClick(method));
		}
	}

	private void setLongClickListener(View view, String method) {
		// TODO Auto-generated method stub
		if (AfStringUtil.isNotEmpty(method)) {
			view.setOnLongClickListener(new EventListener(mHandler).longClick(method));
		}
	}

	private void setOnClickListener(View view, String method) {
		// TODO Auto-generated method stub
		if (AfStringUtil.isNotEmpty(method)) {
			view.setOnClickListener(new EventListener(mHandler).click(method));
		}
	}

	
}
