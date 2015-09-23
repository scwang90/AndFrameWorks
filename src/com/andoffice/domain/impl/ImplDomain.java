package com.andoffice.domain.impl;

import com.andframe.application.AfApplication;
import com.andframe.bean.Page;
import com.andframe.helper.java.AfSQLHelper;
import com.andframe.model.framework.AfModel;
import com.andframe.util.java.AfReflecter;
import com.andoffice.domain.IDomain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

//import com.andsoap.domain.AfSoapDomain;

public class ImplDomain<T> 
		/*extends AfSoapDomain<T, UUID> */implements IDomain<T> {

	protected UUID AuthIdValue = UUID.fromString("eb74b4b4-4607-486b-9f78-c46e31dee30a");//UUIDUtil.Empty;
	protected static LinkedHashMap<String, Object> mltTestMap = new LinkedHashMap<String, Object>();

	protected List<T> buildTestData(){
		return new ArrayList<T>();
	}
	protected Class<T> mClass;

	public ImplDomain(){
		mClass = (Class<T>)AfReflecter.getActualTypeArgument(this,ImplDomain.class,0);
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> getTestData(){
		List<T>ltTest = (List<T>)mltTestMap.get(this.getClass().toString()+mClass.toString()); 
		if(ltTest == null){
			ltTest = new ArrayList<T>();
			ltTest.addAll(buildTestData());
			mltTestMap.put(mClass.toString(),ltTest);
		}
		return ltTest;
	}

	protected List<T> getTestData(String where){
		List<T>ltTest = getTestData();
		if(where != null && !where.equals("") && where.indexOf("%") > 0){
			List<String> lttoken = new ArrayList<String>();
			String[] tokens = where.split(" ");
			for (String token : tokens) {
				if(token.length() > 0){
					lttoken.add(token);
				}
			}
			tokens = lttoken.toArray(new String[0]);
			for (int i = 0; i < tokens.length; i++) {
				String string = tokens[i];
				if(string.equals("like") || string.equals("LIKE")){
					String field = tokens[i-1];
					String key = tokens[i+1].substring(1, tokens[i+1].length()-1);
					List<T> list = new ArrayList<T>();
					for (T model : ltTest) {
						String value = AfReflecter.getMemberNoException(model, field,String.class);
						if(value != null && value.contains(key)){
							list.add(mClass.cast(model));
						}
					}
					return list;
				}
			}
		}
		List<T> list = new ArrayList<T>();
		for (T model : ltTest) {
			list.add(mClass.cast(model));
		}
		return list;
	}

	protected List<T> getTestData(String where, Page page) {
		List<T> list = getTestData(where);
		if(list.size() <= page.FirstResult){
			return new ArrayList<T>();
		}
		int end = page.FirstResult + page.MaxResult;
		if(end >= list.size()){
			end = list.size();
		}
		return list.subList(page.FirstResult, end);
	}
	

	public ImplDomain(Class<T> clazz) {
		mClass = clazz;
	}

	@Override
	public boolean Insert(T model) throws Exception {
		if(AfApplication.getDebugMode() == AfApplication.DEBUG_TESTDATA){
			Thread.sleep(1000);
			getTestData().add(model);
			return true;
		}
		return false;//super.Insert(AuthIdValue, model);
	}

	@Override
	public boolean Update(T model) throws Exception {
		if(AfApplication.getDebugMode() == AfApplication.DEBUG_TESTDATA){
			Thread.sleep(1000);
			List<T> ltTest =  getTestData();
			for (int i = 0; i < ltTest.size(); i++) {
				T tmodel = ltTest.get(i);
				if (tmodel instanceof AfModel) {
					AfModel amodel = AfModel.class.cast(model);
					if(amodel.ID.equals(AfModel.class.cast(model).ID)){
						ltTest.set(i, model);
						return true;
					}
				}
			}
			return false;
		}
		return false;//super.Update(AuthIdValue, model);
	}

	@Override
	public boolean Delete(T model) throws Exception {
		if(AfApplication.getDebugMode() == AfApplication.DEBUG_TESTDATA){
			Thread.sleep(1000);
			List<T> ltTest =  getTestData();
			for (int i = 0; i < ltTest.size(); i++) {
				T tmodel = ltTest.get(i);
				if (tmodel instanceof AfModel) {
					AfModel amodel = AfModel.class.cast(model);
					if(amodel.ID.equals(AfModel.class.cast(model).ID)){
						ltTest.remove(i);
						return true;
					}
				}
			}
			return false;
		}
		return false;//super.Delete(AuthIdValue, model);
	}

	@Override
	public boolean DeleteList(List<T> list) throws Exception {
		if(AfApplication.getDebugMode() == AfApplication.DEBUG_TESTDATA){
			Thread.sleep(1000);
			List<T> tlist = new ArrayList<T>(list);
			List<T> ltTest =  getTestData();
			for (int i = 0; i < ltTest.size() && tlist.size() > 0; i++) {
				T model = ltTest.get(i);
				if (model instanceof AfModel) {
					AfModel amodel = AfModel.class.cast(model);
					for (int j = 0; j < tlist.size(); j++) {
						AfModel date = AfModel.class.cast(tlist.get(j));
						if(amodel.ID.equals(date.ID)){
							ltTest.remove(i--);
							tlist.remove(j);
							return true;
						}
					}
				}
			}
			return false;
		}
		return false;//super.DeleteList(AuthIdValue, list);
	}

	@Override
	public boolean UpdateList(List<T> list) throws Exception {
		if(AfApplication.getDebugMode() == AfApplication.DEBUG_TESTDATA){
			Thread.sleep(1000);
			List<T> tlist = new ArrayList<T>(list);
			List<T> ltTest =  getTestData();
			for (int i = 0; i < ltTest.size() && tlist.size() > 0; i++) {
				T model = ltTest.get(i);
				if (model instanceof AfModel) {
					AfModel amodel = AfModel.class.cast(model);
					for (int j = 0; j < tlist.size(); j++) {
						AfModel date = AfModel.class.cast(tlist.get(j));
						if(amodel.ID.equals(date.ID)){
							ltTest.set(i, tlist.get(j));
							tlist.remove(j);
						}
					}
				}
			}
			return true;
		}
		return false;//super.UpdateList(AuthIdValue, list);
	}

	@Override
	public boolean DeleteByID(UUID id) throws Exception {
		if(AfApplication.getDebugMode() == AfApplication.DEBUG_TESTDATA){
			Thread.sleep(1000);
			List<T> ltTest =  getTestData();
			for (int i = 0; i < ltTest.size(); i++) {
				T model = ltTest.get(i);
				if (model instanceof AfModel) {
					AfModel amodel = AfModel.class.cast(model);
					if(amodel.ID.equals(id)){
						ltTest.remove(i);
						return true;
					}
				}
			}
			return false;
		}
		return false;//super.DeleteByID(AuthIdValue, id);
	}

	public T GetByID(UUID id) throws Exception {
		if(AfApplication.getDebugMode() == AfApplication.DEBUG_TESTDATA){
			Thread.sleep(1000);
			for (T model : getTestData( "")) {
				if (model instanceof AfModel) {
					AfModel amodel = AfModel.class.cast(model);
					if(amodel.ID.equals(id)){
						return model;
					}
				}
			}
			return null;
		}
//		LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
//		properties.put("authid", mGson.toJson(AuthIdValue));
//		properties.put("id", mGson.toJson(id));

//		String[][] properties = new String[][] {
//				new String[] { "authid", mGson.toJson(AuthIdValue)},
//				new String[] {"id", mGson.toJson(id)} };
		
		return null;//getModel("GetByID", properties);
	}

	public boolean Exists(UUID id) throws Exception {
		if(AfApplication.getDebugMode() == AfApplication.DEBUG_TESTDATA){
			Thread.sleep(1000);
			for (T model : getTestData( "")) {
				if (model instanceof AfModel) {
					AfModel amodel = AfModel.class.cast(model);
					if(amodel.ID.equals(id)){
						return true;
					}
				}
			}
			return false;
		}
//		LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
//		properties.put("authid", mGson.toJson(AuthIdValue));
//		properties.put("id", mGson.toJson(id));
		return false;//getBoolean("Exists", properties);
	}

	public List<T> GetListByPage(String where, Page page) throws Exception {
		if(AfApplication.getDebugMode() == AfApplication.DEBUG_TESTDATA){
			Thread.sleep(1000);
			return getTestData( where,page);
		}
//		LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
//		properties.put("authid", mGson.toJson(AuthIdValue));
//		properties.put("where", where);
//		properties.put("page", mGson.toJson(page));
		return new ArrayList<T>();//getList("GetListByPage", properties);
	}

	public long GetRecordCount(String where) throws Exception {
		if(AfApplication.getDebugMode() == AfApplication.DEBUG_TESTDATA){
			Thread.sleep(1000);
			return getTestData( where).size();
		}
//		LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
//		properties.put("authid", mGson.toJson(AuthIdValue));
//		properties.put("where", where);
		return 0;//getLong("GetRecordCount", properties);
	}

	public List<T> GetListWhere(String where, String order, String asc)
			throws Exception {
		if(AfApplication.getDebugMode() == AfApplication.DEBUG_TESTDATA){
			Thread.sleep(1000);
			return getTestData( where);
		}
//		LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
//		properties.put("authid", mGson.toJson(AuthIdValue));
//		properties.put("where", where);
//		properties.put("order", order);
//		properties.put("asc", asc);
		return new ArrayList<T>();//getList("GetListWhere", properties);
	}
	
	protected String where(String where) {
		return AfSQLHelper.Where(where);
	}

	protected String andwhere(String where) {
		return AfSQLHelper.andWhere(where);
	}

	protected String orwhere(String where) {
		return AfSQLHelper.orWhere(where);
	}
}
