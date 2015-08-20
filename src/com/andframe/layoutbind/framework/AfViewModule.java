package com.andframe.layoutbind.framework;

import android.view.View;

import com.andframe.activity.framework.AfViewable;
import com.andframe.annotation.inject.interpreter.Injecter;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.AfViewBinder;

public class AfViewModule extends AfViewDelegate implements AfViewable,IViewModule{

	public AfViewModule(AfViewable view, int id) {
		super(new View(view.getContext()));
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
		if(isValid()){
			setVisibility(View.GONE);
		}
	}

	@Override
	public void show() {
		if(isValid()){
			setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean isValid() {
		return target != null;
	}

	@Override
	public boolean isVisibility() {
		if(isValid()){
			return getVisibility() == View.VISIBLE;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends View> T findViewByID(int id) {
		try {
			return (T)target.findViewById(id);
		} catch (Exception e) {
			AfExceptionHandler.handler(e, "AfViewModule.findViewByID");
		}
		return null;
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> clazz) {
		View view = target.findViewById(id);
		if (clazz.isInstance(view)) {
			return clazz.cast(view);
		}
		return null;
	}

}
