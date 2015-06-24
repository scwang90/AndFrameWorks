package com.andsoap.domain;

import java.util.List;
import java.util.UUID;

import com.andframe.bean.Page;

public interface IAfDomain<T,Tid> {
    boolean Insert(Tid authid,T model) throws Exception;
    boolean Update(Tid authid,T model) throws Exception;
    boolean Delete(Tid authid,T model) throws Exception;
    boolean DeleteList(Tid authid,List<T> list) throws Exception;
    boolean UpdateList(Tid authid,List<T> list) throws Exception;
    boolean DeleteByID(Tid authid,Tid id) throws Exception;
    long GetRecordCount(String where) throws Exception;
    T GetByID(UUID uuid) throws Exception;
    boolean Exists(UUID id) throws Exception;
    List<T> GetListByPage(String where ,Page page) throws Exception;
    List<T> GetListWhere(String where, String order, String asc) throws Exception;
}
