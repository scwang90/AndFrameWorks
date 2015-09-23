package com.andframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.andframe.feature.AfIntent;
/**
 * 框架 Activity
 * @author 树朾
 */
public abstract class AfActivity extends com.andframe.activity.framework.AfActivity implements OnItemClickListener {

	/**
	 * final 原始 onCreate(Bundle bundle)
	 * 子类只能重写 onCreate(Bundle bundle,AfIntent intent)
	 */
	@Override
	protected final void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	/**
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 * final 重写 onActivityResult 使用 try-catch 调用 
	 * 		onActivityResult(AfIntent intent, int requestcode,int resultcode)
	 * @see AfActivity#onActivityResult(AfIntent intent, int requestcode,int resultcode)
	 * {@link AfActivity#onActivityResult(AfIntent intent, int requestcode,int resultcode)}
	 */
	@Override
	protected final void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);
	}

	/**
	 *  final 包装 onItemClick 事件处理 防止抛出异常崩溃
	 * @author 树朾
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public final void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
	}

	/**
	 * 转发 onBackPressed 事件给 AfFragment
	 */
	@Override
	public final void onBackPressed() {
		super.onBackPressed();
	}

}
