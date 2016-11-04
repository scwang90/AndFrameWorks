package com.andframe.impl.viewer;

import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.andframe.R;
import com.andframe.api.view.ItemsViewer;
import com.andframe.impl.wrapper.RecyclerAdapterWrapper;

/**
 * RecyclerView
 * Created by SCWANG on 2016/9/14.
 */
public class ItemsRecyclerViewWrapper implements ItemsViewer<RecyclerView> {

    protected RecyclerView mItemsView;
    protected AdapterView.OnItemClickListener mOnItemClickListener;
    protected AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    protected View.OnClickListener mOnClickListener;
    protected View.OnLongClickListener mOnLongClickListener;

    public ItemsRecyclerViewWrapper(RecyclerView itemView) {
        this.mItemsView = itemView;
        mOnClickListener = v -> onItemClick(v, mItemsView.getChildAdapterPosition(v), v.getId());
        mOnLongClickListener = v -> onItemLongClick(v, mItemsView.getChildAdapterPosition(v), v.getId());
//        mItemsView.addOnItemTouchListener(setUpItemListener(itemView.getContext()));
    }

//    @Override
//    public void smoothScrollToPosition(int index) {
//        mItemsView.smoothScrollToPosition(index);
//    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof Adapter) {
            if (mItemsView.getLayoutManager() == null) {
                mItemsView.setLayoutManager(new LinearLayoutManager(mItemsView.getContext()));
                DividerItemDecoration dividerLine = new DividerItemDecoration();
                dividerLine.setSize(mItemsView.getResources().getDimensionPixelSize(R.dimen.division_line));
                dividerLine.setColor(mItemsView.getResources().getColor(R.color.colorDivison));
                mItemsView.addItemDecoration(dividerLine);
            }
            //noinspection unchecked
            Adapter<ViewHolder> holderAdapter = (Adapter<ViewHolder>) adapter;
            mItemsView.setAdapter(new RecyclerAdapterWrapper<ViewHolder>(holderAdapter) {
                @Override
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                    if (viewHolder.itemView != null) {
                        if (viewHolder.itemView.getBackground() == null) {
                            TypedValue typedValue = new TypedValue();
                            Resources.Theme theme = viewHolder.itemView.getContext().getTheme();
                            if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
                                viewHolder.itemView.setBackgroundResource(typedValue.resourceId);
                            } else {
                                viewHolder.itemView.setBackgroundResource(R.drawable.af_selector_background);
                            }
                        }
                        if (!viewHolder.itemView.isClickable()) {
                            viewHolder.itemView.setOnClickListener(mOnClickListener);
                            viewHolder.itemView.setOnLongClickListener(mOnLongClickListener);
                        }
                    }
                    return viewHolder;
                }
            });
        }
    }

    @Override
    public boolean addHeaderView(View view) {
        return false;
    }

    @Override
    public boolean addFooterView(View view) {
        return false;
    }

    @Override
    public RecyclerView getItemsView() {
        return mItemsView;
    }

    //<editor-fold desc="点击事件">
//    private RecyclerView.OnItemTouchListener setUpItemListener(Context context) {
//        GestureDetectorCompat gestureDetector = new GestureDetectorCompat(context,
//                new GestureDetector.SimpleOnGestureListener() {
//                    @Override
//                    public boolean onSingleTapUp(MotionEvent e) {
//                        View view = mItemsView.findChildViewUnder(e.getX(), e.getY());
//                        if (view != null) {
//                            onItemClick(view, mItemsView.getChildAdapterPosition(view), view.getId());
//                        }
//                        return true;
//                    }
//
//                    @Override
//                    public void onLongPress(MotionEvent e) {
//                        View view = mItemsView.findChildViewUnder(e.getX(), e.getY());
//                        if (view != null) {
//                            onItemLongClick(view, mItemsView.getChildAdapterPosition(view), view.getId());
//                        }
//                    }
//                });
//        return new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                gestureDetector.onTouchEvent(e);
//                return false;
//            }
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//            }
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//            }
//        };
//    }

    public void onItemClick(View view, int index, long id) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, view, index, id);
        }
    }

    public boolean onItemLongClick(View view, int index, long id) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(null, view, index, id);
        }
        return false;
    }
    //</editor-fold>

}
