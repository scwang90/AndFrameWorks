package com.andframe.layoutbind.framework;

import android.view.View;

import com.andframe.activity.framework.AfViewable;
import com.andframe.annotation.inject.interpreter.Injecter;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.AfViewBinder;

public class AfViewModule extends AfViewDelegate implements AfViewable,IViewModule{

	public AfViewModule(AfViewable view, int id) {
		super(new View(view.getContext()));
		// TODO Auto-generated constructor stub
		target = view.findViewById(id);
	}
	
	protected void doInject(){
		if(isValid()){
			AfViewBinder binder = new AfViewBinder(this);
			binder.doBind(target);
			Injecter injecter = new Injecter(this);
			injecter.doInject(getContext());
		}
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		if(isValid()){
			setVisibility(View.GONE);
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		if(isValid()){
			setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return target != null;
	}

	@Override
	public boolean isVisibility() {
		// TODO Auto-generated method stub
		if(isValid()){
			return getVisibility() == View.VISIBLE;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends View> T findViewByID(int id) {
		// TODO Auto-generated method stub
		try {
			return (T)target.findViewById(id);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AfViewModule.findViewByID");
		}
		return null;
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> clazz) {
		// TODO Auto-generated method stub
		View view = target.findViewById(id);
		if (clazz.isInstance(view)) {
			return clazz.cast(view);
		}
		return null;
	}

}
