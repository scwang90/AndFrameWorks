package com.andframe.widget.popupmenu;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class MenuEntity implements MenuItem{

	private int mId;
	private CharSequence mTitle;
	
	public MenuEntity(int id,CharSequence charSequence) {
		// TODO Auto-generated constructor stub
		mId = id;
		mTitle = charSequence;
	}

	@Override
	public boolean collapseActionView() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean expandActionView() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ActionProvider getActionProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getActionView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getAlphabeticShortcut() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getGroupId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Drawable getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent getIntent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getItemId() {
		// TODO Auto-generated method stub
		return mId;
	}

	@Override
	public ContextMenuInfo getMenuInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getNumericShortcut() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SubMenu getSubMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharSequence getTitle() {
		// TODO Auto-generated method stub
		return mTitle;
	}

	@Override
	public CharSequence getTitleCondensed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasSubMenu() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isActionViewExpanded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCheckable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MenuItem setActionProvider(ActionProvider arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setActionView(View arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setActionView(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setAlphabeticShortcut(char arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setCheckable(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setChecked(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setEnabled(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setIcon(Drawable arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setIcon(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setIntent(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setNumericShortcut(char arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setOnActionExpandListener(OnActionExpandListener arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setShortcut(char arg0, char arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setShowAsAction(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MenuItem setShowAsActionFlags(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setTitle(CharSequence arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setTitle(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setTitleCondensed(CharSequence arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setVisible(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
