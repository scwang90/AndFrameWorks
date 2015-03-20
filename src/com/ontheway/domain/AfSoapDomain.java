package com.ontheway.domain;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.ontheway.bean.Page;
import com.ontheway.network.AfSoapService;

public class AfSoapDomain<T, Tid> extends AfSoapService<T> implements
		IAfDomain<T, Tid> {

	public AfSoapDomain(Class<T> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	protected boolean CheckReturn(SoapHolder hoder) throws Exception {
		// TODO Auto-generated method stub
		if(hoder.ReturnValue != null){
			if(hoder.getReturnValue().toLowerCase(Locale.ENGLISH).equals("false")){
				throw new Exception("一个内部的错误，攻城师正在努力修复中。。。");
			}
		}
		return false;
	}
	
	public boolean Insert(Tid authid, T model) throws Exception {
		String[][] properties = new String[][]{
				new String[]{"authid", mGson.toJson(authid)},
				new String[]{"model", mGson.toJson(model)}
		};
		return CheckReturn(doFunction("Insert", properties ));
	}

	public boolean Update(Tid authid, T model) throws Exception {
		String[][] properties = new String[][]{
				new String[]{"authid", mGson.toJson(authid)},
				new String[]{"model", mGson.toJson(model)}
		};
		return CheckReturn(doFunction("Update", properties ));
	}

	public boolean Delete(Tid authid, T model) throws Exception {
		String[][] properties = new String[][]{
				new String[]{"authid", mGson.toJson(authid)},
				new String[]{"model", mGson.toJson(model)}
		};
		return CheckReturn(doFunction("Delete", properties ));
	}

	public boolean DeleteByID(Tid authid, Tid id) throws Exception {
		String[][] properties = new String[][]{
				new String[]{"authid", mGson.toJson(authid)},
				new String[]{"id", mGson.toJson(id)}
		};
		return CheckReturn(doFunction("DeleteByID", properties ));
	}

	public boolean DeleteList(Tid authid, List<T> list) throws Exception {
		String[][] properties = new String[][]{
				new String[]{"authid", mGson.toJson(authid)},
				new String[]{"list", mGson.toJson(list)}
		};
		return CheckReturn(doFunction("DeleteList", properties ));
	}

	@Override
	public boolean UpdateList(Tid authid, List<T> list) throws Exception {
		// TODO Auto-generated method stub
		String[][] properties = new String[][]{
				new String[]{"authid", mGson.toJson(authid)},
				new String[]{"list", mGson.toJson(list)}
		};
		return CheckReturn(doFunction("UpdateList", properties ));
	}

	public T GetByID(UUID id) throws Exception {
		String[][] properties = new String[][]{
				new String[]{"id", mGson.toJson(id)}
		};
		return getModel("GetByID", properties);
	}

	public boolean Exists(UUID id) throws Exception {
		String[][] properties = new String[][]{
				new String[]{"id", mGson.toJson(id)}
		};
		SoapHolder holder = doFunction("Exists", properties);
		return mGson.fromJson(holder.getReturnValue(), boolean.class);
	}

	public List<T> GetListByPage(String where, Page page) throws Exception {
		String[][] properties = new String[][]{
				new String[]{"where", where},
				new String[]{"page", mGson.toJson(page)}
		};
		return getList("GetListByPage", properties);
	}

	public long GetRecordCount(String where) throws Exception {
		String[][] properties = new String[][]{
				new String[]{"where", where}
		};
		SoapHolder holder = doFunction("GetRecordCount", properties);
		return mGson.fromJson(holder.getReturnValue(), long.class);
	}

	public List<T> GetListWhere(String where, String order, String asc)
			throws Exception {
		String[][] properties = new String[][]{
				new String[]{"where", where},
				new String[]{"order", order},
				new String[]{"asc", asc}
		};
		return getList("GetListWhere", properties);
	}
}
