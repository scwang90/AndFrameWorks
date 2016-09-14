package com.andframe.impl.helper;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.adapter.AfListAdapter;
import com.andframe.annotation.view.BindLayout;
import com.andframe.api.ListItem;
import com.andframe.api.page.ListPagerHelper;
import com.andframe.api.page.ListPager;
import com.andframe.api.view.ItemsViewer;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.task.AfDispatcher;
import com.andframe.task.AfHandlerTask;

import java.util.ArrayList;
import java.util.List;

import static com.andframe.util.java.AfReflecter.getAnnotation;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public class AfListPagerHelper<T> implements ListPagerHelper<T> {

    protected ListPager<T> mListPager;

//    protected AbsListView mListView;
    protected ItemsViewer mListView;
    protected AfListAdapter<T> mAdapter;

    protected String TAG(String tag) {
        if (mListPager == null) {
            return "AfListPagerHelper(null)." + tag;
        }
        return "AfListPagerHelper(" + mListPager.getClass().getName() + ")." + tag;
    }

    public AfListPagerHelper(ListPager<T> listPager) {
        this.mListPager = listPager;
    }

    @Override
    public int getLayoutId() {
        BindLayout layout = getAnnotation(mListPager.getClass() , BindLayout.class);
        if (layout != null) {
            return layout.value();
        }
        return 0;
    }

    @Override
    public void onAfterViews() {
        if (mAdapter == null) {
            mAdapter = mListPager.newAdapter(mListPager.getContext(), new ArrayList<>());
        }
        mListView = mListPager.findListView(mListPager);
        if (mListView != null) {
            mListView.setOnItemClickListener(mListPager);
            mListView.setOnItemLongClickListener(mListPager);
            mListPager.bindListHeaderAndFooter();
            mListPager.bindAdapter(mListView, mAdapter);
        }
        AfDispatcher.dispatch(() -> mListPager.postTask(new AbLoadListTask()));
    }

    @Override
    public void bindAdapter(ItemsViewer listView, ListAdapter adapter) {
        listView.setAdapter(adapter);
    }

    @Override
    public AfListAdapter<T> newAdapter(Context context, List<T> list) {
        return new AbListAdapter(context, list);
    }

    @Override
    public void onTaskLoaded(AfHandlerTask task, List<T> list) {
        if (task.isFinish()) {
            if (list != null && !list.isEmpty()) {
                mAdapter.set(list);
//            } else {
//                makeToastLong("暂无数据");
            }
        } else {
            mListPager.makeToastShort(task.makeErrorToast("数据加载失败"));
        }
    }

    @Override
    public List<T> onTaskLoadList() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        if (mListView instanceof ListView) {
            index -= ((ListView) mListView).getHeaderViewsCount();
        }
        if (index >= 0) {
            T model = mAdapter.get(index);
            try {
                mListPager.onItemClick(model, index);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG("onItemClick"));
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long id) {
        if (mListView instanceof ListView) {
            index -= ((ListView) mListView).getHeaderViewsCount();
        }
        if (index >= 0) {
            T model = mAdapter.get(index);
            try {
                return mListPager.onItemLongClick(model, index);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG("onItemLongClick"));
            }
        }
        return false;
    }

    /**
     * ListView数据适配器（事件已经转发getItemLayout，无实际处理代码）
     */
    protected class AbListAdapter extends AfListAdapter<T> {

        public AbListAdapter(Context context, List<T> ltdata) {
            super(context, ltdata);
        }

        /**
         * 转发事件到 AfListViewActivity.this.getItemLayout(data);
         */
        @Override
        protected ListItem<T> newListItem(int viewType) {
            return mListPager.newListItem();
        }
    }

    protected class AbLoadListTask extends AfHandlerTask {
        private List<T> list;
        @Override
        protected void onWorking() throws Exception {
            list = mListPager.onTaskLoadList();
        }
        @Override
        protected void onHandle() {
            mListPager.onTaskLoaded(this, list);
        }
    }
}
