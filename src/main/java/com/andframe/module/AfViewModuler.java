package com.andframe.module;

import android.annotation.SuppressLint;
import android.view.View;

import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.annotation.view.BindLayout;
import com.andframe.api.view.ViewQuery;
import com.andframe.api.view.ViewQueryHelper;
import com.andframe.api.view.Viewer;
import com.andframe.api.view.ViewModuler;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.impl.helper.AfViewQueryHelper;
import com.andframe.util.java.AfReflecter;

/**
 * 视图模块实现基类
 */
@SuppressLint("ViewConstructor")
@SuppressWarnings("unused")
public abstract class AfViewModuler extends AfViewWrapper implements Viewer, ViewModuler, ViewQueryHelper {

	public static <T extends AfViewModuler> T init(Class<T> clazz, Viewer viewable, int viewId) {
		T module = null;
		try {
			module = AfReflecter.newUnsafeInstance(clazz);
//            Constructor<?>[] constructors = clazz.getConstructors();
//            for (int i = 0; i < constructors.length && module == null; i++) {
//                Class<?>[] parameterTypes = constructors[i].getParameterTypes();
//                if (parameterTypes.length == 0) {
//                    module = clazz.newInstance();
//                } else if (parameterTypes.length == 1 && Viewer.class.isAssignableFrom(parameterTypes[0])) {
//                    module = (T) constructors[i].newInstance(viewable);
//                }
//            }
			if (module != null && !module.isValid()) {
				AfViewModuler viewModule = module;
				viewModule.setTarget(viewable, viewable.findViewByID(viewId));
			}
			return module;
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AfViewModuler.init");
		}
		return module;
	}

	public static <T extends AfViewModuler> T init(Class<T> clazz, Viewer viewable) {
		BindLayout annotation = AfReflecter.getAnnotation(clazz, AfViewModuler.class, BindLayout.class);
		if (annotation == null) {
			return null;
		}
		return init(clazz, viewable, annotation.value());
	}

	protected AfViewModuler(){
		super(new View(AfApp.get()));
	}

	protected AfViewModuler(View view) {
		super(view);
	}

	protected AfViewModuler(Viewer view) {
		super(new View(view.getContext()));
		BindLayout layout = AfReflecter.getAnnotation(this.getClass(), AfViewModuler.class, BindLayout.class);
		if (layout != null) {
			wrapped = view.findViewById(layout.value());
		} else {
			wrapped = null;
		}
	}

	protected AfViewModuler(Viewer view, int id) {
		super(new View(view.getContext()));
		wrapped = view.findViewById(id);
	}

	/**
	 * 如果不想要采用 注入的形式
	 * 子类构造函数中必须调用这个函数
	 */
	protected void initializeComponent(Viewer viewable){
		BindLayout layout = AfReflecter.getAnnotation(this.getClass(), AfViewModuler.class, BindLayout.class);
		if (wrapped == null && layout != null) {
			wrapped = viewable.findViewById(layout.value());
		}
		setTarget(viewable, wrapped);
	}

	private void setTarget(final Viewer viewable, final View target) {
		if (target != null) {
			this.wrapped = target;
			this.onCreated(viewable, target);
		}
	}

	protected void onCreated(Viewer viewable, View view) {
		if (mViewQueryHelper == null) {
			mViewQueryHelper = new AfViewQueryHelper(this);
		}
		this.doInject();
	}

	protected void doInject(){
		if(isValid()){
			Injecter.doInject(this, getContext());
			ViewBinder.doBind(this, wrapped);
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
		return wrapped != null;
	}

	@Override
	public boolean isVisibility() {
		return isValid() && getVisibility() == View.VISIBLE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends View> T findViewByID(int id) {
		try {
			return (T) wrapped.findViewById(id);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AfViewModule.findViewByID");
		}
		return null;
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> clazz) {
		View view = wrapped.findViewById(id);
		if (clazz.isInstance(view)) {
			return clazz.cast(view);
		}
		return null;
	}

	//<editor-fold desc="ViewQuery 集成">
	ViewQueryHelper mViewQueryHelper = new AfViewQueryHelper(this);
	/**
	 * 开始 ViewQuery 查询
	 * @param id 控件Id
	 */
	@Override
	public ViewQuery $(int... id) {
		return mViewQueryHelper.$(id);
	}
	/**
	 * 开始 ViewQuery 查询
	 * @param view 至少一个 View
	 * @param views 可选的多个 View
	 */
	@Override
	public ViewQuery $(View view, View... views) {
		return mViewQueryHelper.$(view, views);
	}
	//</editor-fold>

}
