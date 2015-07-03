package com.andoffice.domain;

import java.util.List;
import java.util.UUID;

import com.andframe.bean.Page;

public interface IDomain<Template>{
    boolean Insert(Template model) throws Exception;
    boolean Update(Template model) throws Exception;
    boolean Delete(Template model) throws Exception;
    boolean DeleteList(List<Template> list) throws Exception;
    boolean UpdateList(List<Template> list) throws Exception;
    boolean DeleteByID(UUID id) throws Exception;
    boolean Exists(UUID id) throws Exception;
    Template GetByID(UUID uuid) throws Exception;
    long GetRecordCount(String where) throws Exception;
    List<Template> GetListByPage(String where ,Page page) throws Exception;
    List<Template> GetListWhere(String where, String order, String asc) throws Exception;
}
