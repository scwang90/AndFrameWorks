package com.andframe.impl.helper;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.$;
import com.andframe.R;
import com.andframe.activity.AfItemsActivity;
import com.andframe.adapter.AfAnimatedAdapter;
import com.andframe.adapter.AfHeaderFooterAdapter;
import com.andframe.adapter.AfListAdapter;
import com.andframe.annotation.mark.MarkCache;
import com.andframe.annotation.pager.items.ItemsFooter;
import com.andframe.annotation.pager.items.ItemsHeader;
import com.andframe.annotation.pager.items.ItemsSinglePage;
import com.andframe.annotation.pager.items.ItemsViewerId;
import com.andframe.annotation.pager.items.ItemsViewerOnly;
import com.andframe.annotation.pager.items.idname.ItemsFooter$;
import com.andframe.annotation.pager.items.idname.ItemsHeader$;
import com.andframe.annotation.pager.items.idname.ItemsViewerId$;
import com.andframe.annotation.pager.status.StatusContentViewId;
import com.andframe.annotation.pager.status.StatusContentViewType;
import com.andframe.annotation.pager.status.idname.StatusContentViewId$;
import com.andframe.api.Cacher;
import com.andframe.api.Paging;
import com.andframe.api.adapter.AnimatedAdapter;
import com.andframe.api.adapter.HeaderFooterAdapter;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.adapter.ItemsViewerAdapter;
import com.andframe.api.pager.items.ItemsHelper;
import com.andframe.api.pager.items.ItemsPager;
import com.andframe.api.pager.items.MoreFooter;
import com.andframe.api.pager.items.MoreLayoutManager;
import com.andframe.api.pager.status.StatusLayoutManager;
import com.andframe.api.task.Task;
import com.andframe.api.task.TaskWithPaging;
import com.andframe.api.viewer.ItemsViewer;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.fragment.AfItemsFragment;
import com.andframe.impl.pager.items.MoreFooterLayoutManager;
import com.andframe.impl.viewer.ItemsViewerWrapper;
import com.andframe.impl.viewer.ViewerWrapper;
import com.andframe.model.Page;
import com.andframe.task.AfDispatcher;
import com.andframe.task.AfListViewTask;
import com.andframe.util.java.AfReflecter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.andframe.util.java.AfReflecter.getAnnotation;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
@SuppressWarnings({"WeakerAccess", "unchecked"})
public class AfItemsHelper<T> extends AfStatusHelper<List<T>> implements ItemsHelper<T> {

    //<editor-fold desc="属性字段">
    protected ItemsPager<T> mItemsPager;

    protected MoreLayoutManager mMoreLayoutManager;
    protected MoreFooter mMoreFooter;
    protected com.andframe.api.viewer.ItemsViewer mItemsViewer;
    protected ItemsViewerAdapter<T> mAdapter;

//    protected ViewQuery<? extends ViewQuery> $$ ;
    protected ItemsViewerOnly mItemsViewerOnly;

    protected List<View> mHeaderFooterViews = new ArrayList<>();

    protected boolean mIsNeedPaging = true;
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
            return "AfItemsHelper(null)." + tag;
        }
        return "AfItemsHelper(" + mItemsPager.getClass().getName() + ")." + tag;
    }

    //<editor-fold desc="初始化">
    public AfItemsHelper(ItemsPager<T> itemsPager) {
        super(itemsPager);
        this.mItemsPager = itemsPager;
        this.mItemsPager.setViewQuery(AfViewQueryHelper.newHelper(new ViewerWrapper(itemsPager) {
            @Override
            public View findViewById(int id) {
                View view = mItemsPager.findViewById(id);
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
        }));
        Class<?> stop = mPager instanceof Activity ? AfItemsActivity.class : AfItemsFragment.class;
        ItemsSinglePage singlePage = AfReflecter.getAnnotation(mItemsPager.getClass(), stop, ItemsSinglePage.class);
        if (singlePage != null) {
            mIsNeedPaging = false;
        }
    }

    @Override
    public void onViewCreated() {
        mLoadOnViewCreated = false;
        super.onViewCreated();
        mItemsPager.initCache();
        mItemsPager.initAdapter();
        mItemsViewer.setOnItemClickListener(mItemsPager);
        mItemsViewer.setOnItemLongClickListener(mItemsPager);
        mItemsPager.bindAdapter(mItemsViewer, mAdapter);

        if (mAdapter != null && mAdapter.size() > 0) {
            mItemsPager.showStatus(StatusLayoutManager.Status.content);
        } else if (mCacheClazz != null) {
            AfDispatcher.dispatch(() -> mItemsPager.postTask(new AbLoadListTask()));
        } else {
            mItemsPager.showStatus(StatusLayoutManager.Status.progress);
            AfDispatcher.dispatch(() -> mItemsPager.postTask(new AbRefreshListTask()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHeaderFooterViews.clear();
    }

    @Override
    public View findContentView() {
        View contentView = super.findContentView();
        mItemsViewer = mItemsPager.findItemsViewer(contentView);
        Class<?> stop = mPager instanceof Activity ? AfItemsActivity.class : AfItemsFragment.class;
        mItemsViewerOnly = AfReflecter.getAnnotation(mItemsPager.getClass(), stop, ItemsViewerOnly.class);
        if (mItemsViewerOnly == null) {
            if (contentView != null && contentView != mItemsViewer.getItemsView()) {
                StatusContentViewId id = AfReflecter.getAnnotation(mPager.getClass(), stop, StatusContentViewId.class);
                StatusContentViewId$ id$ = AfReflecter.getAnnotation(mPager.getClass(), stop, StatusContentViewId$.class);
                StatusContentViewType type = AfReflecter.getAnnotation(mPager.getClass(), stop, StatusContentViewType.class);
                if (id != null && id.value() == contentView.getId()) {
                    return contentView;
                }
                if (id$ != null) {
                    Context context = getContext();
                    if (context != null) {
                        int idv = context.getResources().getIdentifier(id$.value(), "id", context.getPackageName());
                        if (idv > 0 && idv == contentView.getId()) {
                            return contentView;
                        }
                    }
                }
                if (type != null && type.value().isInstance(contentView)) {
                    return contentView;
                }
            }
        } else {
            return null;
        }
        return mItemsViewer.getItemsView();
    }
    //</editor-fold>

    //<editor-fold desc="任务发送">
    @Override
    public boolean onMore() {
        return mItemsPager.postTask(new AbMoreListTask()).status() != Task.Status.canceled;
    }

    @Override
    public boolean onRefresh() {
        return mItemsPager.postTask(new AbRefreshListTask()).status() != Task.Status.canceled;
    }
    //</editor-fold>

    //<editor-fold desc="适配器">
    @Override
    public void bindAdapter(@NonNull com.andframe.api.viewer.ItemsViewer itemsViewer, @NonNull ListAdapter adapter) {
        itemsViewer.setAdapter(adapter);
    }

    @NonNull
    @Override
    public ItemsViewerAdapter<T> initAdapter() {
        mAdapter = mItemsPager.newAdapter(mItemsPager.getContext(), new ArrayList<>());
        AfHeaderFooterAdapter<T> headerFooterAdapter = new AfHeaderFooterAdapter<T>(mAdapter){
            @Override
            public int getViewTypeCount() {
                return super.getViewTypeCount() + 1;
            }
        };
        mItemsPager.initHeaderAndFooter(headerFooterAdapter);
        AfAnimatedAdapter<T> animatedAdapter = new AfAnimatedAdapter<>(headerFooterAdapter);
        mItemsPager.initItemsAnimated(animatedAdapter);
        animatedAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                mItemsPager.onDataChanged();
            }
        });
        return mAdapter = animatedAdapter;
    }

    @NonNull
    @Override
    public AfListAdapter<T> newAdapter(@NonNull Context context, @NonNull List<T> list) {
        return new AbViewerAdapter(list);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onDataChanged() {
        if (mItemsPager.isEmpty(mAdapter.getList())) {
            mItemsPager.showStatus(StatusLayoutManager.Status.empty);
        } else {
            mItemsPager.showStatus(StatusLayoutManager.Status.content);
        }
    }

    //</editor-fold>

    //<editor-fold desc="任务结束">
    @Override
    public void onTaskLoadedCache(@NonNull TaskWithPaging task, List<T> list) {
        onTaskLoadedRefresh(task, list);
        if (task.success() && list != null && list.size() > 0) {
            //设置上次刷新缓存时间
            mItemsPager.setLastRefreshTime(mItemsPager.getCacheTime());
        }
    }

    @Override
    public void onTaskLoadedRefresh(@NonNull TaskWithPaging task, List<T> list) {
        final boolean success = task.success();
        mItemsPager.finishRefresh(success);
        if (success) {
            //通知列表刷新完成
//            mItemsPager.finishRefresh();
            //noinspection StatementWithEmptyBody
            if (!mItemsPager.isEmpty(list)) {
//                mItemsPager.showStatus(StatusLayoutManager.Status.content) mAdapter.set 会触发showContent
                mItemsPager.setMoreShow(task, list);
            } else {
//                mItemsPager.showStatus(StatusLayoutManager.Status.empty);mAdapter.set 会触发showEmpty
            }
            mAdapter.set(list == null ? new ArrayList<>() : list);
        } else {
            //通知列表刷新失败
//            mItemsPager.finishRefreshFail();
            if (mAdapter != null && mAdapter.size() > 0) {
                mItemsPager.showStatus(StatusLayoutManager.Status.content);
                mItemsPager.makeToastLong(task.errorToast(getContext().getString(R.string.items_refresh_fail)));
            } else if (!mItemsPager.isEmpty(list)) {
//                mItemsPager.showStatus(StatusLayoutManager.Status.content) mAdapter.set 会触发showContent
                mAdapter.set(list == null ? new ArrayList<>() : list);
                mItemsPager.makeToastLong(task.errorToast(getContext().getString(R.string.items_refresh_fail)));
            } else {
                mItemsPager.showStatus(StatusLayoutManager.Status.error,task.errorToast(getContext().getString(R.string.items_refresh_fail)));
            }
        }
    }

    private Context getContext() {
        Context context = mPager.getContext();
        if (context == null) {
            return AfApp.get();
        }
        return context;
    }

    @Override
    public void onTaskLoadedMore(@NonNull TaskWithPaging task, List<T> list) {
        // 通知列表刷新完成
        mMoreLayoutManager.finishLoadMore();
        if (task.success()) {
            if (list != null && list.size() > 0) {
                // 更新列表
                mAdapter.addAll(list);
//                mItemsViewer.smoothScrollToPosition(mAdapter.getCount() + 1);
            }
            if (!mItemsPager.setMoreShow(task, list)) {
                mItemsPager.makeToastShort(getContext().getString(R.string.items_loading_all));
            }
        } else {
            mItemsPager.makeToastLong(task.errorToast(getContext().getString(R.string.items_loading_fail)));
        }
    }
    //</editor-fold>

    //<editor-fold desc="缓存相关">
    @Override
    public void initCache() {
        MarkCache mark = getAnnotation(mItemsPager.getClass(), MarkCache.class);
        if (mark != null) {
            if (MarkCache.class.equals(mark.value())) {
                Class<?> stop = mPager instanceof Activity ? AfItemsActivity.class : AfItemsFragment.class;
                mCacheClazz = AfReflecter.getActualTypeArgument(mItemsPager, stop, 0);
            } else {
                //noinspection unchecked
                mCacheClazz = (Class<T>) mark.value();
            }
            KEY_CACHELIST = mItemsPager.getCacheKey(mark);
            KEY_CACHETIME = KEY_CACHELIST + "_TIME";
        }
    }

    @NonNull
    @Override
    public String getCacheKey(MarkCache mark) {
        if (TextUtils.isEmpty(mark.key())) {
            KEY_CACHELIST = mItemsPager.getClass().getName();
        } else {
            KEY_CACHELIST = mark.key();
        }
        return KEY_CACHELIST;
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
    public void putCache(@NonNull List<T> list) {
        mItemsPager.onTaskPutCache(list);
    }

    @NonNull
    @Override
    public Date getCacheTime() {
        return new Date($.cache(KEY_CACHELIST).getLong(KEY_CACHETIME, 0));
    }

    @Override
    public void onTaskPushCache(List<T> list) {
        if (mCacheClazz != null && list != null && list.size() > 0) {
            Cacher cache = $.cache(KEY_CACHELIST);
            List<T> cacheList = cache.getList(KEY_CACHELIST, mCacheClazz);
            cacheList.addAll(0, list);
            cache.putList(KEY_CACHELIST, cacheList);
        }
    }

    @Override
    public void onTaskPutCache(List<T> list) {
        if (mCacheClazz != null) {
            Cacher cache = $.cache(KEY_CACHELIST);
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
    public boolean setMoreShow(@NonNull TaskWithPaging task, List<T> list) {
        if (task.getPaging() == null) {
            return false;
        }
        boolean loadFinish = list.size() >= task.getPaging().pageSize();
        mItemsPager.setLoadMoreEnabled(loadFinish);
        return loadFinish;
    }

    @Nullable
    @Override
    public Paging newPaging(int size, int start) {
        return mIsNeedPaging ? new Page(size, start) : null;
    }

    @Override
    public void setLoadMoreEnabled(boolean enabled) {
        mMoreLayoutManager.setLoadMoreEnabled(enabled);
    }

    @Override
    public void showProgress() {
        if (mItemsViewerOnly == null) {
            super.showProgress();
        }
    }

    @Override
    public void showContent(@NonNull List<T> model) {
        mAdapter.set(model);
    }

    @Override
    public void finishRefresh(boolean success) {
        if (mRefreshLayoutManager != null && mRefreshLayoutManager.isRefreshing()) {
            mRefreshLayoutManager.finishRefresh(success);
            if (success) {
                mRefreshLayoutManager.setLastRefreshTime(new Date());
            }
        } else if (mStatusLayoutManager == null || !mStatusLayoutManager.isProgress()) {
            mItemsPager.hideProgressDialog();
        }
    }

//    @Override
//    public void finishRefresh() {
//        if (mRefreshLayoutManager != null && mRefreshLayoutManager.isRefreshing()) {
//            mRefreshLayoutManager.setRefreshComplete();
//            mRefreshLayoutManager.setLastRefreshTime(new Date());
//        } else if (mStatusLayoutManager == null || !mStatusLayoutManager.isProgress()) {
//            mItemsPager.hideProgressDialog();
//        }
//    }
//
//    @Override
//    public void finishRefreshFail() {
//        if (mRefreshLayoutManager != null && mRefreshLayoutManager.isRefreshing()) {
//            mRefreshLayoutManager.setRefreshFailed();
//        } else if (mStatusLayoutManager == null || !mStatusLayoutManager.isProgress()) {
//            mItemsPager.hideProgressDialog();
//        }
//    }

    @Override
    public void initItemsAnimated(AnimatedAdapter<T> animatedAdapter) {
        animatedAdapter.setEnableAnimated(true);
    }

    @Override
    public void initHeaderAndFooter(@NonNull HeaderFooterAdapter<T> adapter) {
        if (mRefreshLayoutManager instanceof MoreLayoutManager) {
            mMoreLayoutManager = ((MoreLayoutManager) mRefreshLayoutManager);
        } else {
            mMoreFooter = mItemsPager.newMoreFooter();
            mMoreLayoutManager = new MoreFooterLayoutManager<>(mMoreFooter, adapter, mItemsViewer);
        }
        mMoreLayoutManager.setOnMoreListener(mItemsPager);
        mMoreLayoutManager.setLoadMoreEnabled(false);

        Class<?> stop = mPager instanceof Activity ? AfItemsActivity.class : AfItemsFragment.class;
        ItemsHeader headers = AfReflecter.getAnnotation(mPager.getClass(), stop, ItemsHeader.class);
        if (headers != null) {
            for (int id : headers.value()) {
                addHeaderView(adapter, $.query(mPager).query(id).breakView());
            }
        } else {
            Context context = getContext();
            ItemsHeader$ headers$ = AfReflecter.getAnnotation(mPager.getClass(), stop, ItemsHeader$.class);
            if (headers$ != null && context != null) {
                for (String id$ : headers$.value()) {
                    int id = context.getResources().getIdentifier(id$, "id", context.getPackageName());
                    if (id > 0) {
                        addHeaderView(adapter, $.query(mPager).query(id).breakView());
                    }
                }
            }
        }
        ItemsFooter footers = AfReflecter.getAnnotation(mPager.getClass(), stop, ItemsFooter.class);
        if (footers != null) {
            for (int id : footers.value()) {
                addFooterView(adapter, $.query(mPager).query(id).breakView());
            }
        } else {
            Context context = getContext();
            ItemsFooter$ footers$ = AfReflecter.getAnnotation(mPager.getClass(), stop, ItemsFooter$.class);
            if (footers$ != null && context != null) {
                for (String id$ : footers$.value()) {
                    int id = context.getResources().getIdentifier(id$, "id", context.getPackageName());
                    if (id > 0) {
                        addFooterView(adapter, $.query(mPager).query(id).breakView());
                    }
                }
            }
        }

    }

    protected void addHeaderView(HeaderFooterAdapter<T> adapter, View view) {
        if (view != null) {
            if (!mItemsViewer.addHeaderView(view)) {
                adapter.addHeaderView(view);
            }
            mHeaderFooterViews.add(view);
        } else {
            AfExceptionHandler.handle("ItemsHeader指定View为null","AfItemsHelper.addHeaderView");
        }
    }

    protected void addFooterView(HeaderFooterAdapter<T> adapter, View view) {
        if (view != null) {
            if (!mItemsViewer.addFooterView(view)) {
                adapter.addFooterView(view);
            }
            mHeaderFooterViews.add(view);
        } else {
            AfExceptionHandler.handle("ItemsFooter指定View为null","AfItemsHelper.addFooterView");
        }
    }

    //</editor-fold>

    //<editor-fold desc="组件加载">
    @NonNull
    @Override
    public ItemsViewer findItemsViewer(View contentView) {
        View itemView = null;
        Class<?> stop = mPager instanceof Activity ? AfItemsActivity.class : AfItemsFragment.class;
        ItemsViewerId viewer = AfReflecter.getAnnotation(mPager.getClass(), stop, ItemsViewerId.class);
        ItemsViewerId$ viewer$ = AfReflecter.getAnnotation(mPager.getClass(), stop, ItemsViewerId$.class);
        ItemsViewerOnly viewerOnly = AfReflecter.getAnnotation(mPager.getClass(), stop, ItemsViewerOnly.class);
        if (viewer != null) {
            itemView = mPager.findViewById(viewer.value());
        } else if (viewer$ != null) {
            Context context = getContext();
            int id = context.getResources().getIdentifier(viewer$.value(), "id", context.getPackageName());
            if (id > 0) {
                itemView = mPager.findViewById(id);
            }
        } else if (viewerOnly != null && viewerOnly.value() != 0) {
            itemView = mPager.findViewById(viewerOnly.value());
        } else {
            if (ItemsViewerWrapper.isWrapped(contentView)) {
                itemView = contentView;
            } else {
                itemView = ItemsViewerWrapper.searchItemsView(mPager);
            }
        }

        ItemsViewerWrapper wrapper = new ItemsViewerWrapper(itemView);
        if (!wrapper.isWrapped()) throw new RuntimeException("请重写 findItemsViewer 获取列表控件");
        return wrapper;
    }

    @Override
    public MoreFooter newMoreFooter() {
        return AfApp.get().newMoreFooter();
    }
    //</editor-fold>

    //<editor-fold desc="任务加载">
//    @Override
//    public List<T> onTaskLoadList(Paging paging) {
//        return null;
//    }

    @Override
    public List<T> onTaskLoadCache(boolean isCheckExpired) {
        if (mCacheClazz != null) {
            Cacher instance = $.cache(KEY_CACHELIST);
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
        if (mItemsViewer.getItemsView() instanceof ListView) {
            index -= ((ListView) mItemsViewer.getItemsView()).getHeaderViewsCount();
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
    protected class AbViewerAdapter extends AfListAdapter<T> {

        public AbViewerAdapter(List<T> list) {
            super(list);
        }

        /**
         * 转发事件到 AfListViewActivity.this.getItemLayout(data);
         */
        @NonNull
        @Override
        public ItemViewer<T> newItemViewer(int viewType) {
            return mItemsPager.newItemViewer(viewType);
        }

        @Override
        public int getViewTypeCount() {
            return mItemsPager.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position) {
            return mItemsPager.getItemViewType(position);
        }
    }

    //<editor-fold desc="加载任务">

    protected class AbLoadListTask extends AbLoadTask implements TaskWithPaging {

        private Paging paging;

        @Nullable
        @Override
        public Paging getPaging() {
            return paging;
        }

        @Override
        protected void onHandle(List<T> list) {
            super.onHandle(list);
            mItemsPager.onTaskLoadedCache(this, list);
        }

        @Override
        protected boolean onPrepare() {
            mItemsPager.showStatus(StatusLayoutManager.Status.progress);
            return super.onPrepare();
        }

        @Override
        protected List<T> onLoadData() {
            List<T> list = mItemsPager.onTaskLoadCache(true);
            if (list != null && list.size() > 0) {
                return list;
            }
            try {
                data = mItemsPager.onTaskLoadList(paging = mItemsPager.newPaging(AfListViewTask.PAGE_SIZE, 0));
                mItemsPager.onTaskPutCache(data);
                return data;
            } catch (Throwable e) {
                return mItemsPager.onTaskLoadCache(false);
            }
        }
    }

    /**
     * 刷新数据任务
     */
    protected class AbRefreshListTask extends AbLoadTask implements TaskWithPaging {

        private Paging paging;

        public AbRefreshListTask() {
        }

        @Nullable
        @Override
        public Paging getPaging() {
            return paging;
        }

        @Override
        protected void onHandle(List<T> list) {
            super.onHandle(list);
            mItemsPager.onTaskLoadedRefresh(this, list);
        }

        @Override
        protected List<T> onLoadData() throws Exception {
            data = mItemsPager.onTaskLoadList(paging = mItemsPager.newPaging(AfListViewTask.PAGE_SIZE, 0));
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
    protected class AbMoreListTask extends AbLoadTask implements TaskWithPaging  {

        private Paging paging;

        @Nullable
        @Override
        public Paging getPaging() {
            return paging;
        }

        @Override
        protected void onHandle(List<T> list) {
            super.onHandle(list);
            mItemsPager.onTaskLoadedMore(this, list);
        }

        @Override
        protected List<T> onLoadData() throws Exception {
            data =  mItemsPager.onTaskLoadList(paging = mItemsPager.newPaging(AfListViewTask.PAGE_SIZE, mAdapter.size()));
            mItemsPager.onTaskPushCache(data);
            return data;
        }

    }
    //</editor-fold>
    //</editor-fold>

}
