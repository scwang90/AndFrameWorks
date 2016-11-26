package com.andframe.impl.helper;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.$;
import com.andframe.R;
import com.andframe.activity.AfMultiItemsActivity;
import com.andframe.adapter.AfHeaderFooterAdapter;
import com.andframe.adapter.AfListAdapter;
import com.andframe.annotation.mark.MarkCache;
import com.andframe.annotation.multistatus.MultiContentViewId;
import com.andframe.annotation.multistatus.MultiContentViewType;
import com.andframe.annotation.multistatus.MultiItemsFooter;
import com.andframe.annotation.multistatus.MultiItemsHeader;
import com.andframe.annotation.multistatus.MultiItemsViewer;
import com.andframe.annotation.multistatus.MultiItemsViewerOnly;
import com.andframe.api.adapter.ListItem;
import com.andframe.api.adapter.ListItemAdapter;
import com.andframe.api.multistatus.MoreFooter;
import com.andframe.api.multistatus.MoreLayouter;
import com.andframe.api.page.ItemsHelper;
import com.andframe.api.page.ItemsPager;
import com.andframe.api.view.ItemsViewer;
import com.andframe.api.view.ViewQuery;
import com.andframe.api.view.ViewQueryHelper;
import com.andframe.application.AfApp;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.fragment.AfMultiItemsFragment;
import com.andframe.impl.multistatus.MoreFooterLayouter;
import com.andframe.impl.viewer.ItemsGridViewWrapper;
import com.andframe.impl.viewer.ItemsListViewWrapper;
import com.andframe.impl.viewer.ItemsRecyclerViewWrapper;
import com.andframe.impl.viewer.ViewerWarpper;
import com.andframe.model.Page;
import com.andframe.task.AfDispatcher;
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
public class AfItemsPagerHelper<T> extends AfMultiStatusHelper<List<T>> implements ItemsHelper<T> {

    //<editor-fold desc="属性字段">
    protected ItemsPager<T> mItemsPager;

    protected MoreLayouter mMoreLayouter;
    protected MoreFooter mMoreFooter;
    protected ItemsViewer mItemsViewer;
    protected AfHeaderFooterAdapter<T> mAdapter;

    protected ViewQueryHelper mViewQueryHelper ;
    protected MultiItemsViewerOnly mItemsViewerOnly;

    protected List<View> mHeaderFooterViews = new ArrayList<>();

    //<editor-fold desc="缓存相关">
    /**
     * 缓存使用的 class 对象（json要用到）
     * 设置 并且任务为 TASK_LOAD AfListTask 将自动使用缓存功能
     */
    protected Class<T> mCacheClazz = null;
    /**
     * 缓存使用的 KEY_CACHELIST = this.getClass().getName()
     * KEY_CACHELIST 为缓存的标识
     */
    protected String KEY_CACHETIME = "KEY_CACHETIME";
    protected String KEY_CACHELIST = this.getClass().getName();

    protected int mCacheSpan = AfListViewTask.CACHETIMEOUTSECOND;
    //</editor-fold>
    //</editor-fold>

    protected String TAG(String tag) {
        if (mItemsPager == null) {
            return "AfItemsPagerHelper(null)." + tag;
        }
        return "AfItemsPagerHelper(" + mItemsPager.getClass().getName() + ")." + tag;
    }


    @Override
    public ViewQuery<? extends ViewQuery> $(int id, int... ids) {
        return mViewQueryHelper.$(id, ids);
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(View... views) {
        return mViewQueryHelper.$(views);
    }

    //<editor-fold desc="初始化">
    public AfItemsPagerHelper(ItemsPager<T> itemsPager) {
        super(itemsPager);
        this.mItemsPager = itemsPager;
        this.mViewQueryHelper= new AfViewQueryHelper(new ViewerWarpper(itemsPager.getView()) {
            @Override
            public Context getContext() {
                return itemsPager.getContext();
            }
            @Override
            public View getView() {
                return itemsPager.getView();
            }
            @Override
            public View findViewById(int id) {
                View view = itemsPager.findViewById(id);
                if (view == null && mAdapter != null) {
                    for (View HeaderFooterView : mHeaderFooterViews) {
                        view = HeaderFooterView.findViewById(id);
                        if (view != null) {
                            return view;
                        }
                    }
                }
                return view;
            }
        });
    }

    @Override
    public void onViewCreated() throws Exception{
        mLoadOnViewCreated = false;
        super.onViewCreated();
        mItemsPager.initCache();
        mItemsPager.initAdapter();

//        mItemsViewer = mItemsPager.findItemsViewer(mItemsPager);

//        if (mItemsViewer != null) {
            mItemsPager.bindListHeaderAndFooter(mAdapter);

            mItemsViewer.setOnItemClickListener(mItemsPager);
            mItemsViewer.setOnItemLongClickListener(mItemsPager);
            mItemsPager.bindAdapter(mItemsViewer, mAdapter);

            if (mAdapter != null && mAdapter.size() > 0) {
                AfDispatcher.dispatch(() -> mItemsPager.postTask(new AbRefreshListTask(mAdapter.getList())));
            } else if (mCacheClazz != null) {
                AfDispatcher.dispatch(() -> mItemsPager.postTask(new AbLoadListTask()));
            } else {
                AfDispatcher.dispatch(() -> mItemsPager.postTask(new AbRefreshListTask()));
            }
//        } else {
//            throw new RuntimeException("findItemsViewer 返回null");
//        }
    }

    @Override
    public View findContentView() {
        View contentView = super.findContentView();
        mItemsViewer = mItemsPager.findItemsViewer(mItemsPager, contentView);
        if (mItemsViewer != null) {
            Class<?> stop = mPager instanceof Activity ? AfMultiItemsActivity.class : AfMultiItemsFragment.class;
            mItemsViewerOnly = AfReflecter.getAnnotation(mItemsPager.getClass(), stop, MultiItemsViewerOnly.class);
            if (mItemsViewerOnly == null) {
                if (contentView != null && contentView != mItemsViewer.getItemsView()) {
                    MultiContentViewId id = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiContentViewId.class);
                    MultiContentViewType type = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiContentViewType.class);
                    if (id != null && id.value() == contentView.getId()) {
                        return contentView;
                    }
                    if (type != null && type.value().isInstance(contentView)) {
                        return contentView;
                    }
                }
            } else {
                return null;
            }
            return mItemsViewer.getItemsView();
        } else {
            throw new RuntimeException("findItemsViewer 返回null");
        }
//        View view = super.findContentView();
//        if (view != null) {
//            return view;
//        }
//        return mItemsViewer != null ? mItemsViewer.getItemsView() : null;
    }
    //</editor-fold>

    //<editor-fold desc="任务发送">
    @Override
    public boolean onMore() {
        return mItemsPager.postTask(new AbMoreListTask()).prepare();
    }

    @Override
    public boolean onRefresh() {
        return mItemsPager.postTask(new AbRefreshListTask()).prepare();
    }
    //</editor-fold>

    //<editor-fold desc="适配器">
    @Override
    public void bindAdapter(ItemsViewer listView, ListAdapter adapter) {
        listView.setAdapter(adapter);
    }

    @Override
    public ListItemAdapter<T> initAdapter() {
        if (mAdapter == null) {
            mAdapter = new AfHeaderFooterAdapter<T>(mItemsPager.newAdapter(mItemsPager.getContext(), new ArrayList<>())){
                @Override
                public int getViewTypeCount() {
                    return super.getViewTypeCount() + 1;
                }
            };
        } else {
            mAdapter.clearHeader();
            mAdapter.clearFooter();
            mHeaderFooterViews.clear();
        }
        return mAdapter;
    }

    @Override
    public AfListAdapter<T> newAdapter(Context context, List<T> list) {
        return new AbListAdapter(context, list);
    }
    //</editor-fold>

    //<editor-fold desc="任务执行结束">
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
            if (mAdapter != null && mAdapter.size() > 0) {
                mItemsPager.showContent();
                mItemsPager.makeToastLong(task.makeErrorToast(mPager.getContext().getString(R.string.items_refresh_fail)));
            } else if (list != null && list.size() > 0) {
                mAdapter.set(list);
                mItemsPager.showContent();
                mItemsPager.makeToastLong(task.makeErrorToast(mPager.getContext().getString(R.string.items_refresh_fail)));
            } else {
                mItemsPager.showError(task.makeErrorToast(mPager.getContext().getString(R.string.items_refresh_fail)));
            }
        }
    }

    @Override
    public void onTaskLoadedMore(AfHandlerTask task, List<T> list) {
        // 通知列表刷新完成
        mMoreLayouter.finishLoadMore();
        if (task.isFinish()) {
            if (list != null && list.size() > 0) {
                // 更新列表
                mAdapter.addAll(list);
//                mItemsViewer.smoothScrollToPosition(mAdapter.getCount() + 1);
            }
            if (!mItemsPager.setMoreShow(task, list)) {
                mItemsPager.makeToastShort(mPager.getContext().getString(R.string.items_loadmore_all));
            }
        } else {
            mItemsPager.makeToastLong(task.makeErrorToast(mPager.getContext().getString(R.string.items_loadmore_fail)));
        }
    }
    //</editor-fold>

    //<editor-fold desc="缓存相关">
    @Override
    public void initCache() {
        MarkCache mark = getAnnotation(mItemsPager.getClass(), MarkCache.class);
        if (mark != null) {
            if (mark.value().equals(MarkCache.class)) {
                Class<?> stop = mPager instanceof Activity ? AfMultiItemsActivity.class : AfMultiItemsFragment.class;
                mCacheClazz = AfReflecter.getActualTypeArgument(mItemsPager, stop, 0);
            } else {
                //noinspection unchecked
                mCacheClazz = (Class<T>) mark.value();
            }
            KEY_CACHELIST = mItemsPager.getCacheKey();
            if (TextUtils.isEmpty(KEY_CACHELIST)) {
                if (TextUtils.isEmpty(mark.key())) {
                    KEY_CACHELIST = mItemsPager.getClass().getName();
                } else {
                    KEY_CACHELIST = mark.key();
                }
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
    public void finishRefresh() {
        if (mRefreshLayouter != null && mRefreshLayouter.isRefreshing()) {
            mRefreshLayouter.setRefreshComplete();
            mRefreshLayouter.setLastRefreshTime(new Date());
        } else if (mStatusLayouter == null || !mStatusLayouter.isProgress()) {
            mItemsPager.hideProgressDialog();
        }
    }

    @Override
    public void finishRefreshFail() {
        if (mRefreshLayouter != null && mRefreshLayouter.isRefreshing()) {
            mRefreshLayouter.setRefreshComplete();
        } else if (mStatusLayouter == null || !mStatusLayouter.isProgress()) {
            mItemsPager.hideProgressDialog();
        }
    }

    @Override
    public void setLastRefreshTime(Date time) {
        if (mRefreshLayouter != null) {
            mRefreshLayouter.setLastRefreshTime(time);
        }
    }

    @Override
    public void bindListHeaderAndFooter(AfHeaderFooterAdapter<T> adapter) {
        if (mRefreshLayouter instanceof MoreLayouter) {
            mMoreLayouter = ((MoreLayouter) mRefreshLayouter);
        } else {
            mMoreFooter = mItemsPager.newMoreFooter();
            mMoreLayouter = new MoreFooterLayouter<>(mMoreFooter, adapter, mItemsViewer);
        }
        mMoreLayouter.setOnMoreListener(mItemsPager);
        mMoreLayouter.setLoadMoreEnabled(false);

        Class<?> stop = mPager instanceof Activity ? AfMultiItemsActivity.class : AfMultiItemsFragment.class;
        MultiItemsHeader headers = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiItemsHeader.class);
        if (headers != null) {
            for (int id : headers.value()) {
                addHeaderView(adapter, $.query(mPager).$(id).breakView());
            }
        }
        MultiItemsFooter footers = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiItemsFooter.class);
        if (footers != null) {
            for (int id : footers.value()) {
                addFooterView(adapter, $.query(mPager).$(id).breakView());
            }
        }

    }

    protected void addHeaderView(AfHeaderFooterAdapter<T> adapter, View view) {
        if (view != null) {
            if (!mItemsViewer.addHeaderView(view)) {
                adapter.addHeaderView(view);
            }
            mHeaderFooterViews.add(view);
        } else {
            AfExceptionHandler.handle("MultiItemsHeader指定View为null","AfItemsPagerHelper.addHeaderView");
        }
    }

    protected void addFooterView(AfHeaderFooterAdapter<T> adapter, View view) {
        if (view != null) {
            if (!mItemsViewer.addFooterView(view)) {
                adapter.addFooterView(view);
            }
            mHeaderFooterViews.add(view);
        } else {
            AfExceptionHandler.handle("MultiItemsFooter指定View为null","AfItemsPagerHelper.addFooterView");
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

    //<editor-fold desc="显示控制">
    /**
     * 根据加载的数据判断是否可以加载更多
     * @return false 数据加载完毕，关闭加载更多功能 true 数据还未加载完，开启加载功能功能
     */
    @Override
    public boolean setMoreShow(AfHandlerTask task, List<T> list) {
        boolean loadFinish = list.size() >= AfListViewTask.PAGE_SIZE;
        mMoreLayouter.setLoadMoreEnabled(loadFinish);
        return loadFinish;
    }

    @Override
    public void showProgress() {
        if (mItemsViewerOnly == null) {
            super.showProgress();
        }
    }
    //</editor-fold>

    //<editor-fold desc="组件加载">
    @Override
    public ItemsViewer findItemsViewer(ItemsPager<T> pager, View contentView) {
        View itemView = null;
        Class<?> stop = mPager instanceof Activity ? AfMultiItemsActivity.class : AfMultiItemsFragment.class;
        MultiItemsViewer viewer = AfReflecter.getAnnotation(pager.getClass(), stop, MultiItemsViewer.class);
        MultiItemsViewerOnly viewerOnly = AfReflecter.getAnnotation(pager.getClass(), stop, MultiItemsViewerOnly.class);
        if (viewer != null) {
            itemView = pager.findViewById(viewer.value());
        } else if (viewerOnly != null && viewerOnly.value() > 0) {
            itemView = pager.findViewById(viewerOnly.value());
        } else {
            View view = contentView;
            if (view instanceof ListView || view instanceof GridView || view instanceof RecyclerView) {
                itemView = view;
            }

            Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(pager.getView()));
            while (!views.isEmpty() && itemView == null) {
                view = views.poll();
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
            }
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
    public MoreFooter newMoreFooter() {
        return AfApp.get().newMoreFooter();
    }
    //</editor-fold>

    //<editor-fold desc="任务加载">
//    @Override
//    public List<T> onTaskLoadList(Page page) {
//        return null;
//    }

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
        if (mItemsViewer instanceof ListView) {
            index -= ((ListView) mItemsViewer).getHeaderViewsCount();
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
        if (mItemsViewer instanceof ListView) {
            index -= ((ListView) mItemsViewer).getHeaderViewsCount();
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

    //<editor-fold desc="内部类">
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
        public ListItem<T> newListItem(int viewType) {
            return mItemsPager.newListItem();
        }
    }

    //<editor-fold desc="加载任务">

    protected class AbLoadListTask extends AbStatusTask {
        @Override
        protected void onHandle(List<T> list) {
            super.onHandle(list);
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
            data = mItemsPager.onTaskLoadList(new Page(AfListViewTask.PAGE_SIZE, 0));
            mItemsPager.onTaskPutCache(data);
            return data;
        }
    }

    /**
     * 刷新数据任务
     */
    protected class AbRefreshListTask extends AbStatusTask {

        private List<T> mList;

        public AbRefreshListTask() {
        }

        public AbRefreshListTask(List<T> list) {
            mList = list;
        }

        @Override
        protected void onHandle(List<T> list) {
            super.onHandle(list);
            mItemsPager.onTaskLoadedRefresh(this, list);
        }

        @Override
        protected List<T> onLoadData() throws Exception {
            AfDispatcher.dispatch(() -> mItemsPager.showProgress());
            if (mList != null && mList.size() > 0) {
                return mList;
            }
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
    protected class AbMoreListTask extends AbStatusTask {

        @Override
        protected void onHandle(List<T> list) {
            super.onHandle(list);
            mItemsPager.onTaskLoadedMore(this, list);
        }

        @Override
        protected List<T> onLoadData() throws Exception {
            data =  mItemsPager.onTaskLoadList(new Page(AfListViewTask.PAGE_SIZE, mAdapter.size()));
            mItemsPager.onTaskPushCache(data);
            return data;
        }

    }
    //</editor-fold>
    //</editor-fold>

}
