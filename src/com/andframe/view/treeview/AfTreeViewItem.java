package com.andframe.view.treeview;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.andframe.activity.framework.AfView;
import com.andframe.feature.AfDensity;
import com.andframe.view.multichoice.AfMultiChoiceItem;


public abstract class AfTreeViewItem<T> extends AfMultiChoiceItem<T>{

	public static final int LP_MP = LayoutParams.MATCH_PARENT;
	public static final int LP_WC = LayoutParams.WRAP_CONTENT;
	
	protected int retract = 35;
	protected AfTreeNode<T> mNode = null;
	protected View mTreeViewContent = null;
	protected ImageView mTreeViewExpanded = null;
	protected LinearLayout mTreeViewLayout = null;
	protected AfTreeViewAdapter<T> mTreeViewAdapter = null;
	
	public AfTreeViewItem() {
		// TODO Auto-generated constructor stub
	}

	public AfTreeViewItem(int layoutId) {
		super(layoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onHandle(AfView view) {
		// TODO Auto-generated method stub
		retract = AfDensity.dip2px(view.getContext(), 20);
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public View inflateLayout(View view,AfTreeViewAdapter<T> adapter) {
		// TODO Auto-generated method stub
		//设置适配器
		mTreeViewAdapter = adapter;
		//创建布局
		mTreeViewContent = view;
		mTreeViewContent.setFocusable(false);
		mTreeViewLayout = new LinearLayout(view.getContext());
		mTreeViewLayout.setOrientation(LinearLayout.HORIZONTAL);
		mTreeViewLayout.setGravity(Gravity.CENTER_VERTICAL);
//		mTreeViewLayout.setOnClickListener(this);
		//设置背景
		if(VERSION.SDK_INT < 16){
			mTreeViewLayout.setBackgroundDrawable(view.getBackground());
		}else{
			mTreeViewLayout.setBackground(view.getBackground());
		}
		view.setBackgroundResource(android.R.color.transparent);
		//包装View
		LayoutParams lpView = new LayoutParams(LP_MP,LP_MP);
		mTreeViewLayout.addView(view,lpView);
		
		return mTreeViewLayout;
	}

	@Override
	protected final boolean onBinding(T model,int index,SelectStatus status) {
		// TODO Auto-generated method stub
		if (mTreeViewLayout != null && mNode != null) {
			mTreeViewLayout.setPadding(mNode.level*retract, 0, 0, 0);
			return onBinding(mNode.value,mNode.level,mNode.isExpanded,status);
		}
		return onBinding(model,0,false,status);
	}
	
	public void setNode(AfTreeNode<T> node){
		mNode = node;
	}

	/**
	 * @param value
	 * @param level 所在树的层数（树根为0）
	 * @param isExpanded 树节点是否张开
	 * @param status 选择状态{NONE,UNSELECT,SELECTED}
	 * @return 绘制 选择状态 返回 TRUE 否则 FALSE
	 */
	protected abstract boolean onBinding(T model,int level,boolean isExpanded,
			SelectStatus status);
	
}
