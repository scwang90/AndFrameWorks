package com.andframe.impl.helper;

import android.database.DataSetObserver;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.adapter.AfListAdapter;
import com.andframe.annotation.mark.MarkCache;
import com.andframe.api.page.MultiListPager;
import com.andframe.api.page.MultiListPagerHelper;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.model.Page;
import com.andframe.module.AfFrameSelector;
import com.andframe.module.AfModuleNodata;
import com.andframe.module.AfModuleProgress;
import com.andframe.task.AfListViewTask;
import com.andframe.util.java.AfReflecter;
import com.andframe.widget.AfGridView;
import com.andframe.widget.AfListView;
import com.andframe.widget.AfRefreshAbsListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.andframe.util.java.AfReflecter.getAnnotation;

/**
 * 多功能列表页面帮助类
 * Created by SCWANG on 2016/9/7.
 */
public class AfMultiListPagerHelper<T> extends AfListPagerHelper<T> implements MultiListPagerHelper<T> {

    protected MultiListPager<T> mMultiListPager;


    protected AfModuleNodata mNodata;
    protected AfModuleProgress mProgress;
    protected AfFrameSelector mSelector;

    protected AfRefreshAbsListView<? extends AbsListView> mListView;
    /**
     * 是否使用分页
     */
    protected boolean mIsPaging = true;
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

    public AfMultiListPagerHelper(MultiListPager<T> multiListPager) {
        super(multiListPager);
        mMultiListPager = multiListPager;

        MarkCache mark = getAnnotation(multiListPager.getClass(), MarkCache.class);
        if (mark != null) {
            if (mark.value().equals(MarkCache.class)) {
                mCacheClazz = AfReflecter.getActualTypeArgument(this, AfMultiListPagerHelper.class, 0);
            } else {
                //noinspection unchecked
                mCacheClazz = (Class<T>) mark.value();
            }
            if (!"".equals(mark.key())) {
                KEY_CACHELIST = mark.key();
            }
        }
    }

    @Override
    public void onAfterViews() {
        super.onAfterViews();
        mNodata = mMultiListPager.newModuleNodata(mMultiListPager);
        mProgress = mMultiListPager.newModuleProgress(mMultiListPager);
        mSelector = mMultiListPager.newAfFrameSelector(mMultiListPager);

//        if (mListView == null) {
//            /**
//             * 在 super.onAfterViews(); 没有成功获取 ListView
//             *
//             */
//            mListView = mMultiListPager.newAfListView(null);
//            mListView.setOnRefreshListener(mMultiListPager);
//            mListView.setOnItemClickListener(mMultiListPager);
//            mListView.setOnItemLongClickListener(mMultiListPager);
//            if (mAdapter != null) {
//                mListView.setAdapter(mAdapter);
//            }
//        }

        mMultiListPager.showLoading();
        /**
         * 父类会执行一个普通的加载任务，可以替代这个 onLoad
         */
//        mMultiListPager.onLoad();
    }

    @Override
    public AfRefreshAbsListView<? extends AbsListView> newAfListView(AbsListView listView) {
        if (listView instanceof ListView) {
            return new AfListView(((ListView) listView));
        } else if (listView instanceof GridView) {
            return new AfGridView(((GridView) listView));
        }
        return new AfListView(mMultiListPager.getContext());
    }

    @Override
    public void showData() {
        mSelector.selectFrame(mListView);
    }

    @Override
    public void showLoading() {
        mProgress.setDescription("正在加载...");
        mSelector.selectFrame(mProgress);
    }

    @Override
    public void showNodata() {
        mNodata.setDescription("抱歉，暂无数据");
        mSelector.selectFrame(mNodata);
        mNodata.setOnRefreshListener(mNodataRefreshListener);
    }

    @Override
    public void showLoadError(Throwable ex) {
        mNodata.setDescription(AfExceptionHandler.tip(ex, "数据加载出现异常"));
        mNodata.setOnRefreshListener(mNodataRefreshListener);
        mSelector.selectFrame(mNodata);
    }

    @Override
    public void onLoad() {
        mMultiListPager.postTask(new AbListViewTask());
    }

    @Override
    public boolean onMore() {
        return mMultiListPager.postTask(new AbListViewTask(mAdapter)).setListener(mListView).prepare();
    }

    @Override
    public boolean onRefresh() {
        return mMultiListPager.postTask(new AbListViewTask(AfListViewTask.TASK_REFRESH)).setListener(mListView).prepare();
    }

    @Override
    public void putCache() {
        if (mAdapter != null && !mAdapter.isEmpty()) {
            putCache(mAdapter.getList());
        }
    }

    @Override
    public void clearCache() {
        mMultiListPager.putCache(new ArrayList<>());
    }

    @Override
    public void putCache(List<T> list) {
        mMultiListPager.onTaskPutCache(list);
    }

    @Override
    public Date getCacheTime() {
        return new Date(AfPrivateCaches.getInstance(KEY_CACHELIST).getLong(KEY_CACHETIME, 0));
    }

    @Override
    public boolean onTaskPrepare(int task) {
        return true;
    }

    @Override
    public boolean onLoaded(AfListViewTask task, boolean isfinish, List<T> list, Date cachetime) {
        boolean deal = mMultiListPager.onRefreshed(task, isfinish, list);
        if (isfinish && list != null && list.size() > 0) {
            //设置上次刷新缓存时间
            mListView.setLastUpdateTime(cachetime);
        }
        return deal;
    }

    @Override
    public boolean onRefreshed(AfListViewTask task, boolean isfinish, List<T> list) {
        if (isfinish) {
            //通知列表刷新完成
            mListView.finishRefresh();
            if (list != null && list.size() > 0) {
                mAdapter.set(list);
                if (mIsPaging) {
                    mMultiListPager.setMoreShow(task, list);
                }
            } else {
                mMultiListPager.showNodata();
                mAdapter.set(list == null ? new ArrayList<>() : list);
            }
        } else {
            //通知列表刷新失败
            mListView.finishRefreshFail();
            if (mAdapter != null && mAdapter.getCount() > 0) {
                mMultiListPager.showData();
                mMultiListPager.makeToastLong(task.makeErrorToast("刷新失败"));
            } else if (list != null && list.size() > 0) {
                mAdapter.set(list);
                mMultiListPager.showData();
                mMultiListPager.makeToastLong(task.makeErrorToast("刷新失败"));
            } else {
                mMultiListPager.showLoadError(task.mException);
            }
        }
        return true;
    }

    @Override
    public boolean onMored(AfListViewTask task, boolean isfinish, List<T> list) {
        // 通知列表刷新完成
        mListView.finishLoadMore();
        if (isfinish) {
            if (list != null && list.size() > 0) {
                // 更新列表
                mAdapter.addAll(list);
                mListView.smoothScrollToPosition(mAdapter.getCount() + 1);
            }
            if (mIsPaging && !setMoreShow(task, list)) {
                mMultiListPager.makeToastShort("数据全部加载完毕！");
            }
        } else {
            mMultiListPager.makeToastLong(task.makeErrorToast("获取更多失败！"));
        }
        return true;
    }

    @Override
    public boolean setMoreShow(AfListViewTask task, List<T> list) {
        if (list.size() < task.mPageSize) {
            mListView.removeMoreView();
            return false;
        } else {
            mListView.addMoreView();
            return true;
        }
    }

    @Override
    public List<T> onTaskLoad(boolean isCheckExpired) {
        if (mCacheClazz != null) {
            AfPrivateCaches instance = AfPrivateCaches.getInstance(KEY_CACHELIST);
            long date = instance.getLong(KEY_CACHETIME, (0));
            if (!isCheckExpired || System.currentTimeMillis() - date < (mCacheSpan)) {
                return instance.getList(KEY_CACHELIST, mCacheClazz);
            }
        }
        return null;
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

    @Override
    public boolean onTaskWorking(int task) throws Exception {
        return task == -1;
    }

    @Override
    public boolean onTaskWorked(AfListViewTask abListViewTask, boolean isfinish, List<T> list) {
        return false;
    }

    @Override
    public void bindAdapter(AbsListView listView, ListAdapter adapter) {
        adapter.registerDataSetObserver(mDataSetObserver);
        mListView = mMultiListPager.newAfListView(listView);
        mListView.setAdapter(adapter);
        mListView.setOnRefreshListener(mMultiListPager);
        mListView.setOnItemClickListener(mMultiListPager);
        mListView.setOnItemLongClickListener(mMultiListPager);
    }

    @Override
    public List<T> onTaskLoadList() {
        AbListViewTask task = new AbListViewTask();
        task.run();
        return super.onTaskLoadList();
    }

    @Override
    public AfListViewTask postTask(int task) {
        return mMultiListPager.postTask(new AbListViewTask(task));
    }

    @Override
    public void addData(T value) {
        if (mAdapter != null) {
            mAdapter.add(0, value);
        }
    }

    @Override
    public void onDestroy() {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
    }

    /**
     * 监听适配器改变，自动更新页面显示切换（子类要重写请重新赋值新对象）
     */
    protected DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            if (mAdapter == null || mAdapter.getCount() == 0) {
                mMultiListPager.showNodata();
            } else {
                mMultiListPager.showData();
            }
        }
    };

    /**
     * 空数据页面刷新监听器
     * 子类需要重写监听器的话可以对
     * mNodataRefreshListener 重新赋值
     */
    protected View.OnClickListener mNodataRefreshListener = v -> {
        mMultiListPager.onRefresh();
        mMultiListPager.showLoading();
    };

    /**
     * 数据加载内部任务类（数据加载事件已经转发，无实际处理代码）
     *
     * @author 树朾
     */
    protected class AbListViewTask extends AfListViewTask<T> {

        public AbListViewTask() {
            super(TASK_LOAD);
        }

        /**
         * 可以触发加载更多（分页）任务 （传空null可以触发刷新任务）
         *
         * @param adapter 适配器，用于统计当前条数计算分页（传空null可以触发刷新任务）
         */
        public AbListViewTask(AfListAdapter<T> adapter) {
            super(adapter);
        }

        /**
         * 自定义任务 触发 onWorking 和 onTaskWorking
         *
         * @param task 任务标识
         */
        public AbListViewTask(int task) {
            super(task);
        }

        @Override
        protected boolean onPrepare() {
            return mMultiListPager.onTaskPrepare(mTask);
        }

        @Override
        protected List<T> onLoad(boolean isCheckExpired) {
            List<T> list = mMultiListPager.onTaskLoad(isCheckExpired);
            if (list != null && list.size() > 0) {
                return list;
            }
            return super.onLoad(isCheckExpired);
        }

        @Override
        protected void onPutCache(List<T> list) {
            mMultiListPager.onTaskPutCache(list);
        }

        @Override
        protected void onPushCache(List<T> list) {
            mMultiListPager.onTaskPushCache(list);
        }

        //事件转发 参考 AfListViewFragment.onListByPage
        @Override
        protected List<T> onListByPage(Page page, int task) throws Exception {
            return mMultiListPager.onTaskListByPage(page, task);
        }

        @Override
        protected boolean onWorking(int task) throws Exception {
            return mMultiListPager.onTaskWorking(task);
        }

        //事件转发 参考 AfListViewFragment.onLoaded
        @Override
        protected boolean onLoaded(boolean isfinish, List<T> list) {
            return mMultiListPager.onLoaded(this, isfinish, list, getCacheTime());
        }

        //事件转发 参考 AfListViewFragment.onRefreshed
        @Override
        protected boolean onRefreshed(boolean isfinish, List<T> list) {
            return mMultiListPager.onRefreshed(this, isfinish, list);
        }

        //事件转发 参考 AfListViewFragment.onMored
        @Override
        protected boolean onMored(boolean isfinish, List<T> list,
                                  boolean ended) {
            return mMultiListPager.onMored(this, isfinish, list);
        }

        @Override
        protected boolean onWorked(int task, boolean isfinish, List<T> list) {
            return mMultiListPager.onTaskWorked(this, isfinish, list);
        }
    }
}
