package com.andframe.activity.framework;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
/**
 * AfViewable 框架视图接口
 * @author 树朾
 * 主要用于 优化 传统的 findViewById 方法
 */
public interface AfViewable {

	Context getContext();

	Resources getResources();

	View findViewById(int id);

	<T extends View> T findViewByID(int id);

	<T extends View> T findViewById(int id,Class<T> clazz);
}
