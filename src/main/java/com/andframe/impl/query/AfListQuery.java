package com.andframe.impl.query;

import com.andframe.api.query.ListQuery;
import com.andframe.api.query.hindler.FlatMap;
import com.andframe.api.query.hindler.Foreach;
import com.andframe.api.query.hindler.Map;
import com.andframe.api.query.hindler.MapIndex;
import com.andframe.api.query.hindler.Where;

import java.util.ArrayList;
import java.util.List;

/**
 * ListQuery 实现类
 * Created by SCWANG on 2017/5/11.
 */
public class AfListQuery<T> extends ArrayList<T> implements ListQuery<T> {

    public AfListQuery() {
    }

    public AfListQuery(Iterable<T> iterable) {
        for (T anIterable : iterable) add(anIterable);
    }

    @Override
    public ListQuery<T> remove(Where<T> where) {
        for (int i = 0; i < size(); i++) {
            if (where.where(get(i))) {
                remove(i--);
            }
        }
        return this;
    }

    @Override
    public ListQuery<T> where(Where<T> where) {
        AfListQuery<T> query = new AfListQuery<>();
        for (int i = 0; i < size(); i++) {
            T model = get(i);
            if (where.where(model)) {
                query.add(model);
            }
        }
        return query;
    }

    @Override
    public ListQuery<T> foreach(Foreach<T> foreach) {
        for (int i = 0; i < size(); i++) {
            foreach.foreach(i, get(i));
        }
        return this;
    }

    @Override
    public <TT> ListQuery<TT> map(Map<T, TT> map) {
        AfListQuery<TT> query = new AfListQuery<>();
        for (int i = 0; i < size(); i++) {
            query.add(map.map(get(i)));
        }
        return query;
    }

    @Override
    public <TT> ListQuery<TT> mapIndex(MapIndex<T, TT> map) {
        AfListQuery<TT> query = new AfListQuery<>();
        for (int i = 0; i < size(); i++) {
            query.add(map.mapIndex(i, get(i)));
        }
        return query;
    }

    @Override
    public <TT> ListQuery<TT> flatMap(FlatMap<T, TT> map) {
        AfListQuery<TT> query = new AfListQuery<>();
        for (int i = 0; i < size(); i++) {
            query.addAll(map.flatMap(get(i)));
        }
        return query;
    }

    @Override
    public List<T> toList() {
        return this;
    }
}
