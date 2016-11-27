package com.andframe.api.task;

/**
 * 任务
 * Created by SCWANG on 2016/10/13.
 */

public interface Task extends Runnable {

    /**
     * 任务状态
     */
    enum Status {
        none,       //初始态
        prepared,   //已准备
        canceld,    //已取消
        runing,     //正在运行
        finished,   //已完成
        failed,     //已失败
    }

    /**
     * 准备任务
     * @return true 状态变为 prepared 即将被执行 false 状态变为 canceld 将不会被执行
     */
    boolean prepare();

    /**
     * 取消任务
     * 状态变为 canceld 将不会被执行
     */
    void cancel();

    /**
     * 重置状态
     * 状态变为 none 可以被重新执行
     */
    Task reset();

    /**
     * 获取任务状态
     */
    Status status();

    /**
     * 获取异常
     * 状态为 failed 时返回异常信息
     */
    Throwable exception();

}
