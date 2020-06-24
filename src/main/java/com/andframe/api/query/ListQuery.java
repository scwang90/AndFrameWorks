package com.andframe.api.query;

import com.andframe.api.query.handler.FlatMap;
import com.andframe.api.query.handler.Foreach;
import com.andframe.api.query.handler.ForeachIndexed;
import com.andframe.api.query.handler.Map;
import com.andframe.api.query.handler.MapIndexed;
import com.andframe.api.query.handler.Filter;
import com.andframe.api.query.handler.FilterIndexed;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * 列表查询接口
 * Created by SCWANG on 2017/5/11.
 */

public interface ListQuery<T> extends Collection<T> {

    ListQuery<T> rank(Comparator<? super T> comparator);
    ListQuery<T> remove(Filter<? super T> filter);
    ListQuery<T> where(Filter<? super T> filter);
    ListQuery<T> foreach(Foreach<? super T> foreach);

    ListQuery<T> removeIndexed(FilterIndexed<? super T> where);
    ListQuery<T> whereIndexed(FilterIndexed<? super T> where);
    ListQuery<T> foreachIndexed(ForeachIndexed<? super T> foreach);

    ListQuery<T> append(T t);
    ListQuery<T> append(Collection<T> t);
    ListQuery<T> taken(int count);
    List<T> toList();
    T firstOrNull(Filter<? super T> filter);
    T firstOrNull();
    T[] toArrays(T... a);
    <TT> ListQuery<TT> map(Map<T,TT> map);
    <TT> ListQuery<TT> mapIndexed(MapIndexed<T,TT> map);
    <TT> ListQuery<TT> flatMap(FlatMap<T,TT> map);

    String join(CharSequence delimiter);


    boolean any(Filter<? super T> filter);
    boolean anyIndexed(FilterIndexed<? super T> where);
    boolean all(Filter<? super T> filter);
    boolean allIndexed(FilterIndexed<? super T> where);

}
