package com.andframe.impl.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.adapter.AfHeaderFooterAdapter;
import com.andframe.adapter.AfListAdapter;
import com.andframe.annotation.mark.MarkCache;
import com.andframe.annotation.multistatus.MultiItemsViewer;
import com.andframe.api.ListItem;
import com.andframe.api.ListItemAdapter;
import com.andframe.api.multistatus.MoreFooter;
import com.andframe.api.page.ItemsPager;
import com.andframe.api.page.ItemsPagerHelper;
import com.andframe.api.view.ItemsViewer;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.fragment.AfMultiItemsFragment;
import com.andframe.impl.multistatus.DefaultMoreFooter;
import com.andframe.impl.viewer.ItemsGridViewWrapper;
import com.andframe.impl.viewer.ItemsListViewWrapper;
import com.andframe.impl.viewer.ItemsRecyclerViewWrapper;
import com.andframe.model.Page;
import com.andframe.task.AfDispatcher;
import com.andframe.task.AfHandlerDataTask;
import com.andframe.task.AfHandlerTask;
import com.andframe.task.AfListViewTask;
import com.andframe.util.java.AfReflecter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.andframe.util.java.AfReflecter.getAnnotation;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public class AfItemsPagerHelper<T> implements ItemsPagerHelper<T> {

    //<editor-fold desc="属性字段">
    protected ItemsPager<T> mItemsPager;

    protected ItemsViewer mListView;
    protected ListItemAdapter<T> mAdapter;
    protected MoreFooter<T> mMoreFooter;

    //<editor-fold desc="缓存相关">
    /**
     * 缓存使用的 class 对象（json要用到）
     * 设置 并且任务为 TASK_LOAD AfListTask 将自动使用缓存功能
     */
    public Class<T> mCacheClazz = null;
    /**
     * 缓存使用的 KEY_CACHELIST = this.getClass().getName()
     * KEY_CACHELIST 为缓存的标识
     */
    public String KEY_CACHETIME = "KEY_CACHETIME";
    public String KEY_CACHELIST = this.getClass().getName();

    protected int mCacheSpan = AfListViewTask.CACHETIMEOUTSECOND;
    //</editor-fold>
    //</editor-fold>

    protected String TAG(String tag) {
        if (mItemsPager == null) {
            return "AfItemsPagerHelper(null)." + tag;
        }
        return "AfItemsPagerHelper(" + mItemsPager.getClass().getName() + ")." + tag;
    }

    public AfItemsPagerHelper(ItemsPager<T> itemsPager) {
        this.mItemsPager = itemsPager;
    }

    @Override
    public ItemsViewer onViewCreated() {
        mItemsPager.initCache();
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
        if (mCacheClazz != null) {
            AfDispatcher.dispatch(() -> mItemsPager.postTask(new AbLoadListTask()));
        } else {
            AfDispatcher.dispatch(() -> mItemsPager.postTask(new AbRefreshListTask()));
        }
        return mListView;
    }

    @Override
    public boolean onMore() {
        return mItemsPager.postTask(new AbMoreListTask()).prepare();
    }

    @Override
    public boolean onRefresh() {
        return mItemsPager.postTask(new AbRefreshListTask()).prepare();
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
    public void onTaskLoadedCache(AfHandlerTask task, List<T> list) {
        onTaskLoadedRefresh(task, list);
        if (task.isFinish() && list != null && list.size() > 0) {
            //设置上次刷新缓存时间
            mItemsPager.setLastRefreshTime(mItemsPager.getCacheTime());
        }
    }

    @Override
    public void onTaskLoadedRefresh(AfHandlerTask task, List<T> list) {
        if (task.isFinish()) {
            //通知列表刷新完成
            mItemsPager.finishRefresh();
            if (list != null && list.size() > 0) {
                mAdapter.set(list);
                mItemsPager.setMoreShow(task, list);
                mItemsPager.showContent();
            } else {
                mItemsPager.showEmpty();
                mAdapter.set(list == null ? new ArrayList<>() : list);
            }
        } else {
            //通知列表刷新失败
            mItemsPager.finishRefreshFail();
            if (mAdapter != null && mAdapter.getCount() > 0) {
                mItemsPager.showContent();
                mItemsPager.makeToastLong(task.makeErrorToast("刷新失败"));
            } else if (list != null && list.size() > 0) {
                mAdapter.set(list);
                mItemsPager.showContent();
                mItemsPager.makeToastLong(task.makeErrorToast("刷新失败"));
            } else {
                mItemsPager.showError(task.makeErrorToast("刷新失败"));
            }
        }
    }

    @Override
    public void onTaskLoadedMore(AfHandlerTask task, List<T> list) {
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

    //<editor-fold desc="缓存相关">
    @Override
    public void initCache() {
        MarkCache mark = getAnnotation(mItemsPager.getClass(), MarkCache.class);
        if (mark != null) {
            if (mark.value().equals(MarkCache.class)) {
                mCacheClazz = AfReflecter.getActualTypeArgument(mItemsPager, AfMultiItemsFragment.class, 0);
            } else {
                //noinspection unchecked
                mCacheClazz = (Class<T>) mark.value();
            }
            if (TextUtils.isEmpty(mark.key())) {
                KEY_CACHELIST = mItemsPager.getClass().getName();
            } else {
                KEY_CACHELIST = mark.key();
            }
        }
    }
    @Override
    public void putCache() {
        if (mAdapter != null && !mAdapter.isEmpty()) {
            putCache(mAdapter.getList());
        }
    }

    @Override
    public void clearCache() {
        mItemsPager.putCache(new ArrayList<>());
    }

    @Override
    public void putCache(List<T> list) {
        mItemsPager.onTaskPutCache(list);
    }

    @Override
    public Date getCacheTime() {
        return new Date(AfPrivateCaches.getInstance(KEY_CACHELIST).getLong(KEY_CACHETIME, 0));
    }

    @Override
    public void onTaskPushCache(List<T> list) {
        if (mCacheClazz != null) {
            AfPrivateCaches cache = AfPrivateCaches.getInstance(KEY_CACHELIST);
            list.addAll(0, cache.getList(KEY_CACHELIST, mCacheClazz));
            cache.putList(KEY_CACHELIST, list);
        }
    }

    @Override
    public void onTaskPutCache(List<T> list) {
        if (mCacheClazz != null) {
            AfPrivateCaches cache = AfPrivateCaches.getInstance(KEY_CACHELIST);
            cache.putList(KEY_CACHELIST, list);
            cache.put(KEY_CACHETIME, System.currentTimeMillis());
        }
    }

    //</editor-fold>

    @Override
    public boolean setMoreShow(AfHandlerTask task, List<T> list) {
        if (list.size() < AfListViewTask.PAGE_SIZE) {
            mMoreFooter.setAllLoadFinish(true);
            return false;
        } else {
            mMoreFooter.setAllLoadFinish(false);
            return true;
        }
    }

    //<editor-fold desc="组件加载">
    @Override
    public ItemsViewer findItemsViewer(ItemsPager<T> pager) {
        View itemView = null;
        MultiItemsViewer viewer = AfReflecter.getAnnotation(pager.getClass(), AfMultiItemsFragment.class, MultiItemsViewer.class);
        if (viewer != null) {
            itemView = pager.findViewById(viewer.value());
        } else {
            Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(pager.getView()));
            do {
                View view = views.poll();
                if (view != null) {
                    if (view instanceof ListView || view instanceof GridView || view instanceof RecyclerView) {
                        itemView = view;
                    } else if (view instanceof ViewGroup) {
                        ViewGroup group = (ViewGroup) view;
                        for (int j = 0; j < group.getChildCount(); j++) {
                            views.add(group.getChildAt(j));
                        }
                    }
                }
            } while (!views.isEmpty() && itemView == null);
        }

        if (itemView instanceof ListView) {
            return new ItemsListViewWrapper((ListView) itemView);
        } else if (itemView instanceof GridView) {
            return new ItemsGridViewWrapper((GridView) itemView);
        } else if (itemView instanceof RecyclerView) {
            return new ItemsRecyclerViewWrapper((RecyclerView) itemView);
        } else {
            throw new RuntimeException("请重写 findItemsViewer 获取列表控件");
        }

    }

    @Override
    public MoreFooter<T> newMoreFooter() {
        return new DefaultMoreFooter<>();
    }
    //</editor-fold>

    //<editor-fold desc="任务加载">
    @Override
    public List<T> onTaskLoadList(Page page) {
        return null;
    }

    @Override
    public List<T> onTaskLoadCache(boolean isCheckExpired) {
        if (mCacheClazz != null) {
            AfPrivateCaches instance = AfPrivateCaches.getInstance(KEY_CACHELIST);
            long date = instance.getLong(KEY_CACHETIME, (0));
            if (!isCheckExpired || System.currentTimeMillis() - date < (mCacheSpan)) {
                return instance.getList(KEY_CACHELIST, mCacheClazz);
            }
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold desc="原生事件">
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
    //</editor-fold>

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
            mItemsPager.onTaskLoadedCache(this, list);
        }

        @Override
        protected boolean onPrepare() {
            mItemsPager.showProgress();
            return super.onPrepare();
        }

        @Override
        protected List<T> onLoadData() throws Exception {
            List<T> list = mItemsPager.onTaskLoadCache(true);
            if (list != null && list.size() > 0) {
                return list;
            }
            return mItemsPager.onTaskLoadList(new Page(AfListViewTask.PAGE_SIZE, 0));
        }
    }

    /**
     * 刷新数据任务
     */
    protected class AbRefreshListTask extends AfHandlerDataTask<List<T>> {
        @Override
        protected void onHandle(List<T> list) {
            mItemsPager.onTaskLoadedRefresh(this, list);
        }

        @Override
        protected List<T> onLoadData() throws Exception {
            AfDispatcher.dispatch(() -> mItemsPager.showProgress());
            data = mItemsPager.onTaskLoadList(new Page(AfListViewTask.PAGE_SIZE, 0));
            mItemsPager.onTaskPutCache(data);
            return data;
        }

        @Override
        protected void onException(Throwable e) {
            super.onException(e);
            data = mItemsPager.onTaskLoadCache(false);
        }
    }

    /**
     * 获取更多数据任务
     */
    protected class AbMoreListTask extends AfHandlerDataTask<List<T>> {

        @Override
        protected void onHandle(List<T> list) {
            mItemsPager.onTaskLoadedMore(this, list);
        }

        @Override
        protected List<T> onLoadData() throws Exception {
            data =  mItemsPager.onTaskLoadList(new Page(AfListViewTask.PAGE_SIZE, mAdapter.size() - 1));
            mItemsPager.onTaskPushCache(data);
            return data;
        }

    }
}
