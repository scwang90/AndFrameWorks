package com.andcloud.domain;

import com.andcloud.model.AvObject;
import com.andframe.helper.java.AfTimeSpan;
import com.andframe.util.java.AfReflecter;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVQuery.CachePolicy;

import java.util.List;

public class AvDomain<T extends AVObject> {

	protected Class<T> clazz;

	public AvDomain() {
		clazz = AfReflecter.getActualTypeArgument(this, AvDomain.class, 0);
	}

	public AVQuery<T> getQuery(){
		return getQuery(clazz);
	}

	public static <T extends AVObject> AVQuery<T> getQuery(Class<T> clazz){
		AVQuery<T> query = AVObject.getQuery(clazz);
//		query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));
//		query.setMaxCacheAge(AfTimeSpan.FromDays(1).getTotalMilliseconds());
		query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		query.setMaxCacheAge(AfTimeSpan.FromHours(0.5).getTotalMilliseconds());
		return query;
	}

	public void add(T model) throws AVException{
		model.save();
		AVQuery.clearAllCachedResults();
	}

	public void update(T model) throws AVException{
		model.save();
		AVQuery.clearAllCachedResults();
	}

	public void delete(T model) throws AVException{
		model.delete();
		AVQuery.clearAllCachedResults();
	}

	public T findById(String objectId) throws AVException {
		AVQuery<T> query = getQuery();
		query.setSkip(0);
		query.setLimit(1);
		query.whereEqualTo(AvObject.objectId,objectId);
		for (T model : query.find()) {
			return model;
		}
		return null;
	}

	public List<T> list() throws AVException{
		AVQuery<T> query = getQuery();
		return query.find();
	}

	public List<T> list(int skip,int limit) throws AVException{
		AVQuery<T> query = getQuery();
		query.setSkip(skip);
		query.setLimit(limit);
		return query.find();
	}

	public int count() throws AVException{
		AVQuery<T> query = getQuery();
		return query.count();
	}

	public void setCachePolicy(CachePolicy policy){
		AVQuery<T> query = getQuery();
		query.setCachePolicy(policy);
	}
}
