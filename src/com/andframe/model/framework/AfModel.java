package com.andframe.model.framework;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@SuppressWarnings("serial")
public class AfModel  implements Serializable , Comparable<AfModel>{
    
	public UUID ID = UUID.randomUUID();
    
    public String Name = "";
    
    public String Remark = "";
    
    public Date RegDate = new Date();

    public AfModel() {
	}
    
	public AfModel(AfModel model) {
		this.ID = model.ID;
		this.Name = model.Name;
		this.Remark = model.Remark;
		this.RegDate = model.RegDate;
	}

	public UUID getID() {
		return ID;
	}
	
	public void setID(UUID iD) {
		ID = iD;
	}
	
	@Override
	public String toString() {
		return String.format("[%s,%s,%s]", Name,ID.toString(),getClass().toString());
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof AfModel){
			return ID.equals(((AfModel)o).ID);
		}
		return super.equals(o);
	}

	@Override
	public int compareTo(@NonNull AfModel tag) {
		if(RegDate != null && tag.RegDate != null){
			return tag.RegDate.compareTo(RegDate);
		}
		return tag.ID.compareTo(tag.ID);
	}
}
