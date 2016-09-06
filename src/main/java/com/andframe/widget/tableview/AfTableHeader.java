package com.andframe.widget.tableview;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AfTableHeader extends LinearLayout {

	protected static final int TYPEVALUE = TypedValue.COMPLEX_UNIT_SP;
	protected static final int PARENT = LayoutParams.MATCH_PARENT;
	protected static final int CONTENT = LayoutParams.WRAP_CONTENT;

	protected TextView[] mTextViews;

	public AfTableHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AfTableHeader(Context context, AfTable table) {
		super(context);
		setOrientation(HORIZONTAL);
		setBackground(this, table.BackgroundIdHeader);
		setLayoutParams(new LayoutParams(CONTENT, table.HeaderHeight));

		mTextViews = new TextView[table.Column];
		for (int i = 0; i < mTextViews.length; i++) {
			 mTextViews[i] = new TextView(context);
			 mTextViews[i].setSingleLine(true);
			 mTextViews[i].setGravity(Gravity.CENTER);
			 mTextViews[i].setText(table.getColumns()[i].Header);
			 mTextViews[i].setTextColor(table.TextcolorHeader);
			 mTextViews[i].setTextSize(TYPEVALUE, table.TextsizeHeader);
			 if((i & 1) == 1){
			 setBackground(mTextViews[i],table.BackgroundIdHeaderCell);
			 }
			 LayoutParams lpar = new LayoutParams(table.ltParam[i]);
			 lpar.height = table.HeaderHeight;
			 this.addView(mTextViews[i],lpar);
		}
	}

	public static void setBackground(View view, int back) {
		 try {
			 view.setBackgroundResource(back);
		 } catch (NotFoundException e) {
			 view.setBackgroundColor(back);
		 }
	}
}
