package com.andframe.api.query;

import com.andframe.api.query.handler.FlatMap;
import com.andframe.api.query.handler.Foreach;
import com.andframe.api.query.handler.ForeachIndexed;
import com.andframe.api.query.handler.Map;
import com.andframe.api.query.handler.MapIndexed;
import com.andframe.api.query.handler.Where;
import com.andframe.api.query.handler.WhereIndexed;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * 列表查询接口
 * Created by SCWANG on 2017/5/11.
 */

public interface ListQuery<T> extends Collection<T> {

    ListQuery<T> rank(Comparator<? super T> comparator);
    ListQuery<T> remove(Where<? super T> where);
    ListQuery<T> where(Where<? super T> where);
    ListQuery<T> foreach(Foreach<? super T> foreach);

    ListQuery<T> removeIndexed(WhereIndexed<? super T> where);
    ListQuery<T> whereIndexed(WhereIndexed<? super T> where);
    ListQuery<T> foreachIndexed(ForeachIndexed<? super T> foreach);

    ListQuery<T> append(T t);
    ListQuery<T> append(Collection<T> t);
    ListQuery<T> taken(int count);
    List<T> toList();
    T firstOrNull(Where<? super T> where);
    T firstOrNull();
    T[] toArrays(T... a);
    <TT> ListQuery<TT> map(Map<T,TT> map);
    <TT> ListQuery<TT> mapIndexed(MapIndexed<T,TT> map);
    <TT> ListQuery<TT> flatMap(FlatMap<T,TT> map);

    String join(CharSequence delimiter);

}
