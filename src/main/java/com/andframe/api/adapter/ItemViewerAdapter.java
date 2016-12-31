package com.andframe.api.adapter;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.andframe.adapter.recycler.ViewHolderItem;

import java.util.List;

/**
 * 可以使用 @{@link com.andframe.api.view.ItemsViewer} 填充 列表控件的 适配器接口
 * <br>使用提醒
 * <br>适配器对@{@link List}中的集合操作都会自动触发 @notifyDataSetChanged
 * <br>如果需要连续进行集合操作请先使用 @getList 用返回的List进行操作，再手动触发  @notifyDataSetChanged
 * Created by SCWANG on 2016/9/10.
 */
public interface ItemViewerAdapter<T> extends List<T>, ListAdapter, RecyclerAdapter<ViewHolderItem<T>> {

    //<editor-fold desc="列表扩展">
    /**
     * 获取源@{@link List}
     * <br>如果对返回的@{@link List}进行改变操作，结束之后需要手动触发  @notifyDataSetChanged
     * <br>改变之后不用调用 @set 方法
     */
    @NonNull
    List<T> getList();

    /**
     * 设置新的数据
     * @param list 新的数据
     */
    void set(@NonNull List<T> list);
    //</editor-fold>

    /**
     * 创建新的 列表项视图 @{@link ItemViewer }
     * @param viewType 视图类型
     */
    @NonNull
    ItemViewer<T> newItemViewer(int viewType);

    /**
     *
     * @param item
     * @param parent
     * @return
     */
    View inflateItem(ItemViewer<T> item, ViewGroup parent);
    void bindingItem(View view, ItemViewer<T> item, int index);


    //<editor-fold desc="改变通知">

    /**
     * 与 @{@link android.widget.BaseAdapter}
     */
    void notifyDataSetChanged();
    void notifyDataSetInvalidated();
    void registerDataSetObserver(DataSetObserver observer) ;
    void unregisterDataSetObserver(DataSetObserver observer);
    //</editor-fold>

}
