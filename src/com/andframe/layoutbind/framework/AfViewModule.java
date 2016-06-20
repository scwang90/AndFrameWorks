package com.andframe.layoutbind.framework;

import android.view.LayoutInflater;
import android.view.View;

import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.AfViewable;
import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.annotation.view.BindLayout;
import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.util.java.AfReflecter;

import java.lang.reflect.Constructor;

public class AfViewModule extends AfViewDelegate implements AfViewable, IViewModule{

	public static <T extends AfViewModule> T init(Class<T> clazz,AfViewable viewable,int viewId){
		try {
			T module = null;
			Constructor<?>[] constructors = clazz.getConstructors();
			for (int i = 0; i < constructors.length && module == null; i++) {
				Class<?>[] parameterTypes = constructors[i].getParameterTypes();
				if (parameterTypes.length == 0) {
					module = clazz.newInstance();
				} else if (parameterTypes.length == 1 && AfViewable.class.isAssignableFrom(parameterTypes[0])) {
					module = (T)constructors[i].newInstance(viewable);
				}
			}
			if (module != null) {
				AfViewModule viewModule = module;
				viewModule.setTarget(viewable,viewable.findViewByID(viewId));
			}
			return module;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static <T extends AfViewModule> T init(Class<T> clazz, AfViewable viewable) {
		BindLayout annotation = AfReflecter.getAnnotation(clazz, AfViewModule.class, BindLayout.class);
		if (annotation == null) {
			return null;
		}
		try {
			T module = null;
			int id = annotation.value();
			View view = LayoutInflater.from(viewable.getContext()).inflate(id, null);
			Constructor<?>[] constructors = clazz.getConstructors();
			for (int i = 0; i < constructors.length && module == null; i++) {
				Class<?>[] parameterTypes = constructors[i].getParameterTypes();
				if (parameterTypes.length == 0) {
					module = clazz.newInstance();
				} else if (parameterTypes.length == 1 && AfViewable.class.isAssignableFrom(parameterTypes[0])) {
					module = (T) constructors[i].newInstance(new AfView(view));
				}
			}
			if (module != null) {
				AfViewModule viewModule = module;
				viewModule.setTarget(viewable, view);
			}
			return module;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	protected AfViewModule(){
		super(new View(AfApplication.getApp()));
	}

	public AfViewModule(View view) {
		super(view);
	}

	protected AfViewModule(AfViewable view) {
		super(new View(view.getContext()));
		BindLayout layout = AfReflecter.getAnnotation(this.getClass(), AfViewModule.class, BindLayout.class);
		if (layout != null) {
			target = view.findViewById(layout.value());
		}
	}

	protected AfViewModule(AfViewable view, int id) {
		super(new View(view.getContext()));
		target = view.findViewById(id);
	}

	/**
	 * 如果不想要采用 注入的形式
	 * 子类构造函数中必须调用这个函数
	 */
	protected void InitializeComponent(AfViewable viewable){
		BindLayout layout = AfReflecter.getAnnotation(this.getClass(), AfViewModule.class, BindLayout.class);
		if (layout != null) {
			target = viewable.findViewById(layout.value());
		}
		setTarget(viewable,target);
	}

	private void setTarget(AfViewable viewable, View target) {
		this.target = target;
		this.onCreated(viewable, target);
	}

	protected void onCreated(AfViewable viewable, View view) {
		this.doInject();
	}

	protected void doInject(){
		if(isValid()){
			Injecter.doInject(this, getContext());
			ViewBinder.doBind(this, target);
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
		} catch (Throwable e) {
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
