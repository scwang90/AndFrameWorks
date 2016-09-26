package com.andframe.task;

import com.andframe.activity.AfActivity;
import com.andframe.application.AfApp;
import com.andframe.model.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 各种数据加载任务handler分流
 *
 * @param <T>
 */
public abstract class AfListViewTask<T> extends AfHandlerTask {
    // 加载单页条数
    public static int PAGE_SIZE = 15;
    //缓存有效时间（5分钟）
    public static int CACHETIMEOUTSECOND = 5 * 60 * 1000;
    // 枚举任务类型
    public static final int TASK_REFRESH = 100; // 下拉刷新
    public static final int TASK_MORE = 101; // 点击更多

    public int mTask = 0;
    public int mFirstResult = 0;
    public int mPageSize = PAGE_SIZE;
    public List<T> mltData = new ArrayList<>();

    public AfListViewTask(int task) {
        this.mTask = task;
    }

    public AfListViewTask(int task, int first) {
        this.mTask = task;
        mFirstResult = first;
    }

    public AfListViewTask(List<T> list) {
        mTask = list != null ? TASK_MORE : TASK_LOAD;
        if (list != null && list.size() > 0) {
            mFirstResult = list.size();
        }
    }

    /**
     * 数据分页加载（在异步线程中执行，不可以更改页面操作）
     *
     * @param page 分页对象
     * @param task 任务id
     * @return 加载到的数据列表
     * @throws Exception
     */
    protected abstract List<T> onListByPage(Page page, int task) throws Exception;

    /**
     * 任务结束处理
     */
    @Override
    protected void onHandle() {
        boolean dealerror;
        boolean isfinish = isFinish();
        if (mTask == TASK_LOAD) {
            dealerror = this.onLoaded(isfinish, mltData);
        } else if (mTask == TASK_MORE) {
            dealerror = this.onMored(isfinish, mltData, mltData.size() < PAGE_SIZE);
        } else if (mTask == TASK_REFRESH) {
            dealerror = this.onRefreshed(isfinish, mltData);
        } else {
            dealerror = this.onWorked(mTask, isfinish, mltData);
        }
        if (!dealerror && !isfinish) {
            this.onDealError(mTask, mErrors, mException);
        }
    }

    /**
     * 任务执行出错 之后统一（各种任务）处理错误提示
     * 如果在 onRefreshed、onMored、onTaskWorked、onLoaded 返回true 表示已经错误处理提示
     * 矿井唉将不在调用 onDealError
     */
    @SuppressWarnings("UnusedParameters")
    protected void onDealError(int task, String rrrors, Throwable exception) {
        AfActivity activity = AfApp.get().getCurActivity();
        if (activity != null && !activity.isRecycled()) {
            if (exception instanceof IOException) {
                activity.makeToastLong("网络出现异常");
            } else {
                activity.makeToastLong(rrrors);
            }
        }
    }

    /**
     * 刷新任务执行结束 后页面处理工作
     *
     * @param isfinish true 成功执行数据刷新 false 失败
     * @param ltdata   刷新的数据
     * @return 返回 true 表示 isfinish=false 时候 已经做了失败提示处理 将不在调用 onDealError
     */
    protected abstract boolean onRefreshed(boolean isfinish, List<T> ltdata);

    /**
     * 加载更多任务执行结束 后页面处理工作
     *
     * @param isfinish true 成功执行数据刷新 false 失败
     * @param ltdata   刷新的数据
     * @param ended    true 表示是否已经加载完毕 用于控制更多按钮的显示
     * @return 返回 true 表示 isfinish=false 时候 已经做了失败提示处理 将不在调用 onDealError
     */
    protected abstract boolean onMored(boolean isfinish, List<T> ltdata, boolean ended);

    /**
     * 其他任务执行结束 后页面处理工作
     *
     * @param task     任务标识
     * @param isfinish true 成功执行数据刷新 false 失败
     * @param ltdata   刷新的数据
     * @return 返回 true 表示 isfinish=false 时候 已经做了失败提示处理 将不在调用 onDealError
     */
    @SuppressWarnings("UnusedParameters")
    protected boolean onWorked(int task, boolean isfinish, List<T> ltdata) {
        return false;
    }

    /**
     * 加载缓存结束 后页面处理工作
     *
     * @param isfinish true 成功执行数据刷新 false 失败
     * @param ltdata   刷新的数据
     * @return 返回 true 表示 isfinish=false 时候 已经做了失败提示处理 将不在调用 onDealError
     */
    protected boolean onLoaded(boolean isfinish, List<T> ltdata) {
        return onRefreshed(isfinish, ltdata);
    }

    /**
     * 加载缓存
     * @param isCheckExpired 是否检测缓存过期（刷新失败时候可以加载缓存）
     * @return 缓存数据 或 null
     */
    @SuppressWarnings("UnusedParameters")
    protected List<T> onLoad(boolean isCheckExpired) {
        return mltData;
    }


    @SuppressWarnings("UnusedParameters")
    protected boolean onWorking(int task) throws Exception {
        return false;
    }

    @Override
    protected final void onWorking() throws Exception {
        switch (mTask) {
            case TASK_LOAD:
                /**
                 * 为了安全考虑，系统框架规定 onLoad 不能抛出异常
                 * 	onLoad 主要用于 本地数据库加载缓存，就算本地没有数据
                 * 	也可返回空List
                 * 		以防万一使用try catch 阻止异常
                 */
                if ((mltData = onLoad(true)) != null && mltData.size() > 0) {
                    break;
                }
                mTask = TASK_REFRESH;
            case AfListViewTask.TASK_REFRESH:
                mltData = onListByPage(new Page(mPageSize, 0), mTask);
                this.onPutCache(mltData);
                break;
            case AfListViewTask.TASK_MORE:
                mltData = onListByPage(new Page(mPageSize, mFirstResult), mTask);
                this.onPushCache(mltData);
                break;
            default:
                this.onWorking(mTask);
                break;
        }
    }

    /**
     * 缓存追加
     */
    @SuppressWarnings("UnusedParameters")
    protected void onPushCache(List<T> list) {

    }

    /**
     * 缓存覆盖
     */
    @SuppressWarnings("UnusedParameters")
    protected void onPutCache(List<T> list) {

    }

    @Override
    protected void onException(Throwable e) {
        e.printStackTrace();
        if (mTask == TASK_REFRESH) {
            mltData = onLoad(false);
        }
//        if (AfApplication.getNetworkStatus() == AfNetworkEnum.TYPE_NONE
//                && mTask != AfTask.TASK_LOAD) {
//            mErrors = "当前网络不可用！";
//            mException = new AfToastException(mErrors);
//        }
    }

}
