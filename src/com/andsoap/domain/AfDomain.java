package com.andsoap.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.ksoap2.serialization.SoapObject;

import com.andframe.bean.Page;
import com.andframe.exception.AfToastException;
import com.andsoap.AfSoapServiceOld;

public class AfDomain<T, Tid> extends AfSoapServiceOld<T> implements
		IAfDomain<T, Tid> {

	public AfDomain(Class<? extends T> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	public boolean Insert(Tid authid, T model) throws Exception {
		SoapObject request = this.getRequest("Insert");
		request.addProperty("authid", mGson.toJson(authid));
		request.addProperty("model", mGson.toJson(model));
		Object response = this.getResponse("Insert", request);
		if (response.toString().toLowerCase(Locale.ENGLISH).equals("false")) {
			throw new AfToastException("一个内部的错误，攻城师正在努力修复中。。。");
		}
		return true;
	}

	public boolean Update(Tid authid, T model) throws Exception {
		SoapObject request = this.getRequest("Update");
		request.addProperty("authid", mGson.toJson(authid));
		request.addProperty("model", mGson.toJson(model));
		Object response = this.getResponse("Update", request);
		return mGson.fromJson(response.toString(), boolean.class);
	}

	public boolean Delete(Tid authid, T model) throws Exception {
		SoapObject request = this.getRequest("Delete");
		request.addProperty("authid", mGson.toJson(authid));
		request.addProperty("model", mGson.toJson(model));
		Object response = this.getResponse("Delete", request);
		return mGson.fromJson(response.toString(), boolean.class);
	}

	public boolean DeleteByID(Tid authid, Tid id) throws Exception {
		SoapObject request = this.getRequest("DeleteByID");
		request.addProperty("authid", mGson.toJson(authid));
		request.addProperty("id", mGson.toJson(id));
		Object response = this.getResponse("DeleteByID", request);
		return mGson.fromJson(response.toString(), boolean.class);
	}

	public boolean DeleteList(Tid authid, List<T> list) throws Exception {
		SoapObject request = this.getRequest("DeleteList");
		request.addProperty("authid", mGson.toJson(authid));
		request.addProperty("list", mGson.toJson(list));
		Object response = this.getResponse("DeleteList", request);
		return mGson.fromJson(response.toString(), boolean.class);
	}

	@Override
	public boolean UpdateList(Tid authid, List<T> list) throws Exception {
		// TODO Auto-generated method stub
		SoapObject request = this.getRequest("UpdateList");
		request.addProperty("authid", mGson.toJson(authid));	
		request.addProperty("list", mGson.toJson(list));
		Object response = this.getResponse("UpdateList", request);
		return mGson.fromJson(response.toString(), boolean.class);
	}

	@SuppressWarnings("unchecked")
	public T GetByID(UUID id) throws Exception {
		SoapObject request = this.getRequest("GetByID");
		request.addProperty("id", mGson.toJson(id));
		Object response = this.getResponse("GetByID", request);
		T model = (T) mGson.fromJson(response.toString(), mClass);
		return model;
	}

	public boolean Exists(UUID id) throws Exception {
		SoapObject request = this.getRequest("Exists");
		request.addProperty("id", mGson.toJson(id));
		Object response = this.getResponse("Exists", request);
		return mGson.fromJson(response.toString(), boolean.class);
	}

	public List<T> GetListByPage(String where, Page page) throws Exception {
		String strpage = mGson.toJson(page);
		SoapObject request = this.getRequest("GetListByPage");
		request.addProperty("where", where);
		request.addProperty("page", strpage);
		SoapObject response = (SoapObject) this.getResponse(
				"GetListByPage", request);
		int length = response.getPropertyCount();
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < length; i++) {
			@SuppressWarnings("unchecked")
			T model = (T) mGson.fromJson(
					response.getProperty(i).toString(), mClass);
			list.add(model);
		}
		return list;
	}

	public long GetRecordCount(String where) throws Exception {
		SoapObject request = this.getRequest("GetRecordCount");
		request.addProperty("where", where);
		Object response = this.getResponse("GetRecordCount", request);
		return mGson.fromJson(response.toString(), long.class);
	}

	public List<T> GetListWhere(String where, String order, String asc)
			throws Exception {
		SoapObject request = this.getRequest("GetListWhere");
		request.addProperty("where", where);
		request.addProperty("order", order);
		request.addProperty("asc", asc);
		SoapObject response = (SoapObject) this.getResponse("GetListWhere",
				request);
		int length = response.getPropertyCount();
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < length; i++) {
			@SuppressWarnings("unchecked")
			T model = (T) mGson.fromJson(
					response.getProperty(i).toString(), mClass);
			list.add(model);
		}
		return list;
	}
	// public List<T> GetAll() throws Exception {
	// try {
	// SoapObject response = (SoapObject) this.getResponse("GetAll",
	// this.getRequest("GetAll"));
	// int length = response.getPropertyCount();
	// List<T> list = new ArrayList<T>();
	// for (int i = 0; i < length; i++) {
	// @SuppressWarnings("unchecked")
	// T model = (T) gson.fromJson(
	// response.getProperty(i).toString(), clazz);
	// list.add(model);
	// }
	// return list;
	// } catch (Throwable ex) {
	// throw ex;
	// } catch (Error er) {
	// throw new Exception(er.getMessage(),er);
	// }
	// }

	// public boolean DeleteListByID(Tid authid,List<T> ids) throws Exception {
	// try {
	// SoapObject request = this.getRequest("DeleteListByID");
	// String s = gson.toJson(ids);
	// request.addProperty("ids", s);
	// Object response = this.getResponse("DeleteListByID", request);
	//
	// return gson.fromJson(response.toString(), boolean.class);
	// } catch (Error er) {
	// throw new Exception(er.getMessage(),er);
	// }
	// }
}
