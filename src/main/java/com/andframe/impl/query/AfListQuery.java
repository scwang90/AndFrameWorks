package com.andframe.impl.query;

import android.text.TextUtils;

import com.andframe.api.query.ListQuery;
import com.andframe.api.query.handler.FlatMap;
import com.andframe.api.query.handler.Foreach;
import com.andframe.api.query.handler.ForeachIndexed;
import com.andframe.api.query.handler.Map;
import com.andframe.api.query.handler.MapIndexed;
import com.andframe.api.query.handler.Filter;
import com.andframe.api.query.handler.FilterIndexed;

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
    public ListQuery<T> remove(Filter<? super T> filter) {
        for (int i = 0; i < size(); i++) {
            if (filter.filter(get(i))) {
                remove(i--);
            }
        }
        return this;
    }
    @Override
    public ListQuery<T> removeIndexed(FilterIndexed<? super T> where) {
        for (int i = 0; i < size(); i++) {
            if (where.filter(i, get(i))) {
                remove(i--);
            }
        }
        return this;
    }

    @Override
    public ListQuery<T> where(Filter<? super T> filter) {
        AfListQuery<T> query = new AfListQuery<>();
        for (int i = 0; i < size(); i++) {
            T model = get(i);
            if (filter.filter(model)) {
                query.add(model);
            }
        }
        return query;
    }

    @Override
    public ListQuery<T> whereIndexed(FilterIndexed<? super T> where) {
        AfListQuery<T> query = new AfListQuery<>();
        for (int i = 0; i < size(); i++) {
            T model = get(i);
            if (where.filter(i, model)) {
                query.add(model);
            }
        }
        return query;
    }

    @Override
    public ListQuery<T> foreach(Foreach<? super T> foreach) {
        for (int i = 0; i < size(); i++) {
            foreach.foreach(get(i));
        }
        return this;
    }


    @Override
    public ListQuery<T> foreachIndexed(ForeachIndexed<? super T> foreach) {
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
    public <TT> ListQuery<TT> mapIndexed(MapIndexed<T, TT> map) {
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
    public T firstOrNull(Filter<? super T> filter) {
        return where(filter).firstOrNull();
    }

    @Override
    public T[] toArrays(T... a) {
        return toArray(a);
    }

    @Override
    public String join(CharSequence delimiter) {
        return TextUtils.join(delimiter, this);
    }

    @Override
    public boolean any(Filter<? super T> filter) {
        for (int i = 0; i < size(); i++) {
            if (filter.filter(get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean anyIndexed(FilterIndexed<? super T> filter) {
        for (int i = 0; i < size(); i++) {
            if (filter.filter(i, get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean all(Filter<? super T> filter) {
        for (int i = 0; i < size(); i++) {
            if (!filter.filter(get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean allIndexed(FilterIndexed<? super T> filter) {
        for (int i = 0; i < size(); i++) {
            if (!filter.filter(i, get(i))) {
                return false;
            }
        }
        return true;
    }
}
