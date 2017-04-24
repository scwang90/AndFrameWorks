package com.andframe.api.viewer;

import android.content.Context;
import android.view.View;

/**
 * Viewer 框架视图接口
 * @author 树朾
 */
public interface Viewer {

	Context getContext();

	View getView();

	View findViewById(int id);

	<T extends View> T findViewByID(int id);

	<T extends View> T findViewById(int id, Class<T> clazz);
}
