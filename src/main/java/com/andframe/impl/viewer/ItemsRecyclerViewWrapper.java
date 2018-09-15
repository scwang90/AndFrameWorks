package com.andframe.impl.viewer;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.andframe.R;
import com.andframe.adapter.RecyclerInteractionAdapter;
import com.andframe.api.pager.items.OnScrollToBottomListener;
import com.andframe.api.viewer.ItemsViewer;

/**
 * RecyclerView
 * Created by SCWANG on 2016/9/14.
 */
public class ItemsRecyclerViewWrapper implements ItemsViewer<RecyclerView>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    protected RecyclerView mItemsView;
    protected AdapterView.OnItemClickListener mOnItemClickListener;
    protected AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    protected LinearLayoutManager mLinearLayoutManager;
    protected RecyclerInteractionAdapter mInteractionAdapter;
    protected boolean mDivisionEnable = true;
    protected boolean mDrawEndDivider = true;

    public ItemsRecyclerViewWrapper(RecyclerView itemView) {
        this.mItemsView = itemView;
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
    public void setOnScrollToBottomListener(OnScrollToBottomListener listener) {
        mItemsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE && mLinearLayoutManager != null){
                    int lastVisiblePosition = mLinearLayoutManager.findLastVisibleItemPosition();
                    if(lastVisiblePosition >= mLinearLayoutManager.getItemCount() - 1){
                        listener.onScrollToBottom();
                    }
                }
            }
        });
    }

    @Override
    public void setDrawEndDivider(boolean draw) {
        this.mDrawEndDivider = draw;
    }

    @Override
    public void setDivisionEnable(boolean enable) {
        mDivisionEnable = enable;
    }

    @Override
    public void setNestedScrollingEnabled(boolean enable) {
        mItemsView.setNestedScrollingEnabled(enable);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof Adapter) {
            RecyclerView.LayoutManager layoutManager = mItemsView.getLayoutManager();
            if (layoutManager == null) {
                mLinearLayoutManager = newLayoutManager();
                mItemsView.setLayoutManager(mLinearLayoutManager);
                if (mDivisionEnable) {
                    RecycleViewDivider dividerLine = new RecycleViewDivider(mItemsView.getContext());
                    dividerLine.drawFooter(mDrawEndDivider);
                    dividerLine.sizeId(R.dimen.dimenDivisionLine).colorId(R.color.colorDivision);
                    mItemsView.addItemDecoration(dividerLine);
                }
            }
            if (layoutManager instanceof LinearLayoutManager) {
                mLinearLayoutManager = ((LinearLayoutManager) layoutManager);
                if (mItemsView.getItemAnimator() == null) {
                    mItemsView.setItemAnimator(new DefaultItemAnimator());
                }
            }
            //noinspection unchecked
            Adapter<ViewHolder> holderAdapter = (Adapter<ViewHolder>) adapter;
            mInteractionAdapter = new RecyclerInteractionAdapter(holderAdapter);
            mInteractionAdapter.setOnItemClickListener(this);
            mInteractionAdapter.setOnItemLongClickListener(this);
            mItemsView.setAdapter(mInteractionAdapter);
        }
    }

    @NonNull
    protected LinearLayoutManager newLayoutManager() {
        return new LinearLayoutManager(mItemsView.getContext());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, view, position, id);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnItemLongClickListener != null) {
            return mOnItemLongClickListener.onItemLongClick(null, view, position, id);
        }
        return false;
    }

    @Override
    public int getFirstVisiblePosition() {
        final RecyclerView.LayoutManager layoutManager = mItemsView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        return 0;
    }

    @Override
    public void setSelection(int index) {
        mItemsView.scrollToPosition(index);
    }

    @Override
    public int getLastVisiblePosition() {
        final RecyclerView.LayoutManager layoutManager = mItemsView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
        return 0;
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
    //</editor-fold>

}
