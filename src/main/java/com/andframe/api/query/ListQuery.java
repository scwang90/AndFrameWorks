package com.andframe.api.query;

import com.andframe.api.query.hindler.FlatMap;
import com.andframe.api.query.hindler.Foreach;
import com.andframe.api.query.hindler.Map;
import com.andframe.api.query.hindler.MapIndex;
import com.andframe.api.query.hindler.Where;

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
    ListQuery<T> append(T t);
    ListQuery<T> append(Collection<T> t);
    ListQuery<T> taken(int count);
    List<T> toList();
    T firstOrNull(Where<? super T> where);
    T firstOrNull();
    T[] toArrays(T... a);
    <TT> ListQuery<TT> map(Map<T,TT> map);
    <TT> ListQuery<TT> mapIndex(MapIndex<T,TT> map);
    <TT> ListQuery<TT> flatMap(FlatMap<T,TT> map);

    String join(CharSequence delimiter);

}
