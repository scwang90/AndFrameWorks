package com.ontheway.model.framework;

import java.util.Date;
import java.util.UUID;

public class AfModel implements Comparable<AfModel>{
    
	public UUID ID = UUID.randomUUID();
    
    public String Name = "";
    
    public String Remark = "";
    
    public Date RegDate = new Date();

    public AfModel() {
		// TODO Auto-generated constructor stub
	}
    
	public AfModel(AfModel model) {
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		return String.format("[%s,%s,%s]", Name,ID.toString(),getClass().toString());
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if(o instanceof AfModel){
			return ID.equals(((AfModel)o).ID);
		}
		return super.equals(o);
	}

	@Override
	public int compareTo(AfModel tag) {
		// TODO Auto-generated method stub
		if(RegDate != null && tag.RegDate != null){
			return tag.RegDate.compareTo(RegDate);
		}
		return tag.ID.compareTo(tag.ID);
	}
}
