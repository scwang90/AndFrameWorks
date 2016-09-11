package com.andframe.adapter.recycler;

import android.database.DataSetObserver;
import android.support.v7.widget.AdapterDataObservableWrapper;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 *
 * Created by SCWANG on 2016/9/10.
 */
public class DataSetObservable extends AdapterDataObservableWrapper {

    public DataSetObservable(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    //<editor-fold desc="原安卓 Observable 的重新实现">
    /**
     * The list of observers.  An observer can be in the list at most
     * once and will never be null.
     */
    protected final ArrayList<DataSetObserver> mObservers = new ArrayList<>();

    /**
     * Adds an observer to the list. The observer cannot be null and it must not already
     * be registered.
     * @param observer the observer to register
     * @throws IllegalArgumentException the observer is null
     * @throws IllegalStateException the observer is already registered
     */
    public void registerObserver(DataSetObserver observer) {
        if (observer != null) {
            synchronized(mObservers) {
                if (!mObservers.contains(observer)) {
                    mObservers.add(observer);
                }
            }
        }
    }

    /**
     * Removes a previously registered observer. The observer must not be null and it
     * must already have been registered.
     * @param observer the observer to unregister
     * @throws IllegalArgumentException the observer is null
     * @throws IllegalStateException the observer is not yet registered
     */
    public void unregisterObserver(DataSetObserver observer) {
        if (observer != null) {
            synchronized(mObservers) {
                int index = mObservers.indexOf(observer);
                if (index > -1) {
                    mObservers.remove(index);
                }
            }
        }
    }

    /**
     * Remove all registered observers.
     */
    public void unregisterAll() {
        synchronized(mObservers) {
            mObservers.clear();
        }
    }
    //</editor-fold>

    //<editor-fold desc="原安卓 DataSetObservable 的重新实现">
    /**
     * Invokes {@link DataSetObserver#onChanged} on each observer.
     * Called when the contents of the data set have changed.  The recipient
     * will obtain the new contents the next time it queries the data set.
     */
    public void notifyChanged() {
        super.notifyChanged();
        synchronized(mObservers) {
            // since onChanged() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }

    /**
     * Invokes {@link DataSetObserver#onInvalidated} on each observer.
     * Called when the data set is no longer valid and cannot be queried again,
     * such as when the data set has been closed.
     */
    public void notifyInvalidated() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onInvalidated();
            }
        }
    }
    //</editor-fold>
}
