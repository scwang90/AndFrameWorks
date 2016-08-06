package com.andoffice.domain;

import com.andframe.bean.Page;

import java.util.List;

public interface IDomain<Model>{
    boolean Insert(Model model) throws Exception;
    boolean Update(Model model) throws Exception;
//    boolean Delete(Model model) throws Exception;
    boolean DeleteList(List<Model> list) throws Exception;
//    boolean UpdateList(List<Model> list) throws Exception;
//    boolean DeleteByID(UUID id) throws Exception;
//    boolean Exists(UUID id) throws Exception;
//    Model GetByID(UUID uuid) throws Exception;
//    long GetRecordCount(String where) throws Exception;
    List<Model> GetListByPage(String where ,Page page) throws Exception;
//    List<Model> GetListWhere(String where, String order, String asc) throws Exception;
}
