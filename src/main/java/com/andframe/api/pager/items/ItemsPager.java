package com.andframe.api.pager.items;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.andframe.api.Paging;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.pager.status.StatusPager;

import java.util.List;

/**
 * 基本列表页
 * Created by SCWANG on 2016/9/7.
 */
public interface ItemsPager<T> extends StatusPager<List<T>>, ItemsHelper<T> {

    /**
     * 创建新的 列表项视图 @{@link ItemViewer }
     * @param viewType 视图类型
     */
    @NonNull
    ItemViewer<T> newItemViewer(int viewType);

    /**
     * 分页加载数据
     * @param paging 分页数据(null表示不使用分页功能)
     * @throws Exception
     */
    List<T> onTaskLoadList(Paging paging) throws Exception;

    /**
     * 列表点击事件
     * @param model 点击项对应的数据model
     * @param index 点击位置索引
     */
    void onItemClick(@Nullable T model, int index);

    /**
     * 列表长按事件
     * @param model 点击项对应的数据model
     * @param index 点击位置索引
     */
    boolean onItemLongClick(@Nullable T model, int index);

}
