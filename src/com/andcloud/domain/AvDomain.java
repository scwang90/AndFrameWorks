package com.andcloud.domain;

import com.andcloud.model.AvObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVQuery.CachePolicy;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AvDomain<T extends AVObject> {

	protected Class<T> clazz;

	public AvDomain() {
		clazz = getActualTypeArgument(this, AvDomain.class, 0);
	}

	public AVQuery<T> getQuery(){
		return getQuery(clazz);
	}

	public static <T extends AVObject> AVQuery<T> getQuery(Class<T> clazz){
		AVQuery<T> query = AVObject.getQuery(clazz);
//		query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));
//		query.setMaxCacheAge(AfTimeSpan.FromDays(1).getTotalMilliseconds());
		query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		query.setMaxCacheAge(30 * 60 * 1000);
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

	public static <T> Class<T> getActualTypeArgument(Object subobj, Class<?> supclass, int index) {
		Class<?> subclass = subobj.getClass();
		List<ParameterizedType> ptypes = new ArrayList<>();
		ParameterizedType ptype;
		while (supclass != null && !supclass.equals(subclass)) {
			Type type = subclass.getGenericSuperclass();
			if (type == null) {
				throw new Error("GenericSuperclass not find");
			} else if (type instanceof Class) {
				subclass = (Class<?>) type;
			} else if (type instanceof ParameterizedType) {
				ptype = ParameterizedType.class.cast(type);
				ptypes.add(ptype);
				subclass = (Class<?>) ptype.getRawType();
			} else if (type instanceof GenericArrayType) {
				GenericArrayType gtype = GenericArrayType.class.cast(type);
				subclass = (Class<?>) gtype.getGenericComponentType();
			} else {
				throw new Error("GenericSuperclass not case");
			}
		}
		Type type;
		do {
			type = ptypes.get(ptypes.size() - 1).getActualTypeArguments()[index];
			ptypes.remove(ptypes.size() - 1);
		} while (!(type instanceof Class) && ptypes.size() > 0);
		//noinspection ConstantConditions
		return (Class<T>) type;
	}
}
