package com.andframe.impl.helper;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.adapter.AfHeaderFooterAdapter;
import com.andframe.adapter.AfListAdapter;
import com.andframe.annotation.pager.BindLayout;
import com.andframe.api.ListItem;
import com.andframe.api.ListItemAdapter;
import com.andframe.api.multistatus.MoreFooter;
import com.andframe.api.page.ItemsPager;
import com.andframe.api.page.ItemsPagerHelper;
import com.andframe.api.view.ItemsViewer;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.impl.multistatus.DefaultMoreFooter;
import com.andframe.model.Page;
import com.andframe.task.AfDispatcher;
import com.andframe.task.AfHandlerDataTask;
import com.andframe.task.AfHandlerTask;
import com.andframe.task.AfListViewTask;

import java.util.ArrayList;
import java.util.List;

import static com.andframe.util.java.AfReflecter.getAnnotation;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public class AfItemsPagerHelper<T> implements ItemsPagerHelper<T> {

    protected ItemsPager<T> mItemsPager;

//    protected AbsListView mListView;
    protected ItemsViewer mListView;
    protected ListItemAdapter<T> mAdapter;
    protected MoreFooter<T> mMoreFooter;

    protected String TAG(String tag) {
        if (mItemsPager == null) {
            return "AfItemsPagerHelper(null)." + tag;
        }
        return "AfItemsPagerHelper(" + mItemsPager.getClass().getName() + ")." + tag;
    }

    public AfItemsPagerHelper(ItemsPager<T> ItemsPager) {
        this.mItemsPager = ItemsPager;
    }

    @Override
    public int getLayoutId() {
        BindLayout layout = getAnnotation(mItemsPager.getClass() , BindLayout.class);
        if (layout != null) {
            return layout.value();
        }
        return 0;
    }

    @Override
    public ItemsViewer onViewCreated() {
        if (mAdapter == null) {
            mAdapter = mItemsPager.newAdapter(mItemsPager.getContext(), new ArrayList<>());
            if (mAdapter instanceof AfListAdapter && !(mAdapter instanceof AfHeaderFooterAdapter)) {
                AfHeaderFooterAdapter<T> adapter = new AfHeaderFooterAdapter<>(((AfListAdapter<T>) mAdapter));
                adapter.addFooter(mMoreFooter = mItemsPager.newMoreFooter());
                mMoreFooter.setOnMoreListener(mItemsPager);
                mAdapter = adapter;
            }
        }
        mListView = mItemsPager.findItemsViewer(mItemsPager);
        if (mListView != null) {
            mListView.setOnItemClickListener(mItemsPager);
            mListView.setOnItemLongClickListener(mItemsPager);
            mItemsPager.bindListHeaderAndFooter();
            mItemsPager.bindAdapter(mListView, mAdapter);
        }
        AfDispatcher.dispatch(() -> mItemsPager.postTask(new AbLoadListTask()));
        return mListView;
    }

    @Override
    public boolean onMore() {
        return mItemsPager.postTask(new AbMoreListTask()).prepare();
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
                mItemsPager.showContent();
//            } else {
//                makeToastLong("暂无数据");
            } else {
                mItemsPager.showEmpty();
            }
        } else {
            mItemsPager.showError(task.makeErrorToast("数据加载失败"));
//            mItemsPager.makeToastShort(task.makeErrorToast("数据加载失败"));
        }
    }

    @Override
    public void onTaskMoreLoaded(AfHandlerTask task, List<T> list) {
        // 通知列表刷新完成
        mMoreFooter.finishLoadMore();
        if (task.isFinish()) {
            if (list != null && list.size() > 0) {
                // 更新列表
                mAdapter.addAll(list);
                mListView.smoothScrollToPosition(mAdapter.getCount() + 1);
            }
            if (!mItemsPager.setMoreShow(task, list)) {
                mItemsPager.makeToastShort("数据全部加载完毕！");
            }
        } else {
            mItemsPager.makeToastLong(task.makeErrorToast("获取更多失败！"));
        }
    }

    @Override
    public boolean setMoreShow(AfHandlerTask task, List<T> list) {
        if (list.size() < AfListViewTask.PAGE_SIZE) {
            mMoreFooter.setLoadAllFinish(true);
            return false;
        } else {
            mMoreFooter.setLoadAllFinish(false);
            return true;
        }
    }

    @Override
    public List<T> onTaskLoadList(Page page) {
        return null;
    }

    @Override
    public MoreFooter<T> newMoreFooter() {
        return new DefaultMoreFooter<>();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        if (mListView instanceof ListView) {
            index -= ((ListView) mListView).getHeaderViewsCount();
        }
        if (index >= 0) {
            T model = mAdapter.get(index);
            try {
                mItemsPager.onItemClick(model, index);
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
                return mItemsPager.onItemLongClick(model, index);
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
            return mItemsPager.newListItem();
        }
    }

    protected class AbLoadListTask extends AfHandlerDataTask<List<T>> {
        @Override
        protected void onHandle(List<T> list) {
            mItemsPager.onTaskLoaded(this, list);
        }

        @Override
        protected List<T> onLoadData() throws Exception {
            return mItemsPager.onTaskLoadList(new Page(AfListViewTask.PAGE_SIZE, 0));
        }
    }

    protected class AbMoreListTask extends AfHandlerDataTask<List<T>> {

        @Override
        protected void onHandle(List<T> list) {
            mItemsPager.onTaskMoreLoaded(this, list);
        }

        @Override
        protected List<T> onLoadData() throws Exception {
            return mItemsPager.onTaskLoadList(new Page(AfListViewTask.PAGE_SIZE, mAdapter.size() - 1));
        }

    }
}
