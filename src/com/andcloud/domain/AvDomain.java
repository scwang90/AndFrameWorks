package com.andcloud.domain;

import java.util.List;

import com.andframe.helper.java.AfTimeSpan;
import com.andframe.util.java.AfReflecter;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVQuery.CachePolicy;

public class AvDomain<T extends AVObject> {

	protected AVQuery<T> query = null;

	public AvDomain() {
		// TODO Auto-generated constructor stub
		Class<T> clazz = AfReflecter.getActualTypeArgument(this, AvDomain.class, 0);
		query = AVObject.getQuery(clazz);
//		query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));
		query.setMaxCacheAge(AfTimeSpan.FromDays(1).getTotalMilliseconds());
	}

	public void add(T model) throws AVException{
		model.save();
	}

	public List<T> list() throws AVException{
		return query.find();
	}

	public List<T> list(int skip,int limit) throws AVException{
		query.setSkip(skip);
		query.setLimit(limit);
		return query.find();
	}

	public int count() throws AVException{
		return query.count();
	}

	public void setCachePolicy(CachePolicy policy){
		query.setCachePolicy(policy);
	}
}
