package com.andoffice.bean;

import com.andframe.model.framework.AfModel;

@SuppressWarnings("serial")
public class Permission extends AfModel{

	public boolean IsRead;
	public boolean IsAdd;
	public boolean IsModify;
	public boolean IsDelete;

	public Permission() {
		this.IsRead = true;
		this.IsAdd = true;
		this.IsModify = true;
		this.IsDelete = true;
	}

	public Permission(Permission model) {
		super(model);
		this.IsRead = model.IsRead;
		this.IsAdd = model.IsAdd;
		this.IsModify = model.IsModify;
		this.IsDelete = model.IsDelete;
	}

	public Permission(boolean read, boolean add, boolean del, boolean edit) {
		this();
		this.IsRead = read;
		this.IsAdd = add;
		this.IsDelete = del;
		this.IsModify = edit;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Permission){
			boolean equal = true;
			Permission obj = (Permission)o;
			equal = equal && ID.equals(obj.ID);
			equal = equal && IsAdd == obj.IsAdd;
			equal = equal && IsRead == obj.IsRead;
			equal = equal && IsModify == obj.IsModify;
			equal = equal && IsDelete == obj.IsDelete;
			return equal;
		}
		return false;
	}
	
}
