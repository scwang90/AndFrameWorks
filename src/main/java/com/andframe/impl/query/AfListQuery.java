package com.andframe.impl.query;

import android.text.TextUtils;

import com.andframe.api.query.ListQuery;
import com.andframe.api.query.hindler.FlatMap;
import com.andframe.api.query.hindler.Foreach;
import com.andframe.api.query.hindler.Map;
import com.andframe.api.query.hindler.MapIndex;
import com.andframe.api.query.hindler.Where;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * ListQuery 实现类
 * Created by SCWANG on 2017/5/11.
 */
@SuppressWarnings("ALL")
public class AfListQuery<T> extends ArrayList<T> implements ListQuery<T> {

    public AfListQuery() {
    }

    public AfListQuery(Iterable<T> iterable) {
        if (iterable != null) {
            for (T anIterable : iterable) add(anIterable);
        }
    }

    public AfListQuery(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public ListQuery<T> rank(Comparator<? super T> comparator) {
        super.sort(comparator);
        return this;
    }

    @Override
    public ListQuery<T> remove(Where<? super T> where) {
        for (int i = 0; i < size(); i++) {
            if (where.where(get(i))) {
                remove(i--);
            }
        }
        return this;
    }

    @Override
    public ListQuery<T> where(Where<? super T> where) {
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
    public ListQuery<T> foreach(Foreach<? super T> foreach) {
        for (int i = 0; i < size(); i++) {
            foreach.foreach(i, get(i));
        }
        return this;
    }

    @Override
    public ListQuery<T> append(T t) {
        AfListQuery<T> query = new AfListQuery<>(size() + 1);
        query.addAll(this);
        query.add(t);
        return query;
    }

    @Override
    public ListQuery<T> append(Collection<T> t) {
        if (t != null) {
            AfListQuery<T> query = new AfListQuery<>(size() + 1);
            query.addAll(this);
            query.addAll(t);
            return query;
        }
        return this;
    }

    @Override
    public ListQuery<T> taken(int count) {
        return new AfListQuery<>(subList(0,Math.min(count,size())));
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

    @Override
    public T firstOrNull() {
        if (this.size() > 0) {
            return this.get(0);
        }
        return null;
    }

    @Override
    public T firstOrNull(Where<? super T> where) {
        return where(where).firstOrNull();
    }

    @Override
    public T[] toArrays(T... a) {
        return toArray(a);
    }

    @Override
    public String join(CharSequence delimiter) {
        return TextUtils.join(delimiter, this);
    }

}
