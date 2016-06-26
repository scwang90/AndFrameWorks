package com.andoffice.activity.framework;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.andframe.activity.AfListViewActivity;
import com.andframe.activity.framework.AfPageable;
import com.andframe.adapter.AfListAdapter;
import com.andframe.bean.Page;
import com.andframe.feature.AfIntent;
import com.andframe.layoutbind.AfFrameSelector;
import com.andframe.layoutbind.AfModuleNodata;
import com.andframe.layoutbind.AfModuleNodataImpl;
import com.andframe.layoutbind.AfModuleProgress;
import com.andframe.layoutbind.AfModuleProgressImpl;
import com.andframe.layoutbind.AfModuleTitlebar;
import com.andframe.layoutbind.AfModuleTitlebarImpl;
import com.andframe.view.multichoice.AfMultiChoiceAdapter;
import com.andframe.view.multichoice.AfMultiChoiceAdapter.MultiChoiceListener;
import com.andframe.view.multichoice.AfMultiChoiceItem;
import com.andframe.widget.popupmenu.OnMenuItemClickListener;
import com.andoffice.R;
import com.andoffice.layoutbind.ModuleBottombar;
import com.andoffice.layoutbind.ModuleBottombarSelector;
import com.andoffice.layoutbind.ModuleTitlebarSearcher;
import com.andoffice.layoutbind.ModuleTitlebarSelector;

import java.util.List;

public abstract class AbSuperListViewActivity<T> extends AfListViewActivity<T>
        implements MultiChoiceListener<T>, OnMenuItemClickListener {

    private static final int REQUEST_SELECT = 1;

    protected AfModuleTitlebar mTitlebar = null;
    protected ModuleBottombar mBottombar = null;
    protected ModuleTitlebarSelector mTitlebarSelector = null;
    protected ModuleTitlebarSearcher mTitlebarSearcher = null;
    protected ModuleBottombarSelector mBottombarSelector = null;

    protected AfMultiChoiceAdapter<T> mMultiChoiceAdapter = null;

    protected abstract List<T> onTaskListFromDomain(int task, Page page) throws Exception;

    protected abstract AfMultiChoiceItem<T> getItemLayout(T data);

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        super.onCreate(bundle, intent);
        mTitlebar = new AfModuleTitlebarImpl(this);
        mBottombar = new ModuleBottombar(this);
        mTitlebarSelector = new ModuleTitlebarSelector(this);
        mTitlebarSearcher = new ModuleTitlebarSearcher(this);
        mBottombarSelector = new ModuleBottombarSelector(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_listview;
    }

    @Override
    protected AfModuleNodata newModuleNodata(AfPageable pageable) {
        return new AfModuleNodataImpl(pageable);
    }

    @Override
    protected AfModuleProgress newModuleProgress(AfPageable pageable) {
        return new AfModuleProgressImpl(pageable);
    }

    @Override
    protected AfFrameSelector newAfFrameSelector(AfPageable pageable) {
        return new AfFrameSelector(pageable,R.id.module_listview_frame);
    }

    @Override
    protected ListView findListView(AfPageable pageable) {
        return pageable.findViewByID(R.id.module_listview);
    }

    @Override
    protected boolean onBackKeyPressed() {
        if (mMultiChoiceAdapter != null &&
                mMultiChoiceAdapter.isMultiChoiceMode()) {
            mMultiChoiceAdapter.closeMultiChoice();
            return true;
        } else {
            return super.onBackKeyPressed();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == REQUEST_SELECT) {
            if (mMultiChoiceAdapter != null) {
                mMultiChoiceAdapter.beginMultiChoice();
            } else {
                makeToastShort("还没有数据喔~");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ModuleBottombar.ID_SELECT) {
            if (mMultiChoiceAdapter != null) {
                mMultiChoiceAdapter.beginMultiChoice();
            } else {
                makeToastShort("还没有数据喔~");
            }
        } else if (v.getId() == AfModuleNodataImpl.ID_BUTTON
                /*|| v.getId() == AfModuleNodataImpl.TEXT_TOREFRESH*/) {
            postRefreshTask(true);
        } else {
            super.onClick(v);
        }
    }

    @Override
    public void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter, int number, int total) {
    }

    @Override
    public void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter, T tag,boolean selected, int number) {
    }

    @Override
    public void onMultiChoiceClosed(AfMultiChoiceAdapter<T> adapter,List<T> list) {
        mBottombar.show();
    }

    @Override
    public void onMultiChoiceStarted(AfMultiChoiceAdapter<T> adapter, int number) {
        mBottombar.hide();
    }

    @Override
    protected AfListAdapter<T> newAdapter(Context context, List<T> ltdata) {
        //获取并创建适配器监听器
        mMultiChoiceAdapter = getMultiChoiceAdapter(ltdata);
        mMultiChoiceAdapter.addListener(this);
        //添加选择按钮状态
        mBottombar.setSelectListener(this);
        mBottombar.setFunction(ModuleBottombar.ID_SELECT, true);

        mTitlebar.setOnMenuItemListener(this);
        mTitlebar.putMenu("选择", REQUEST_SELECT);
        mTitlebar.setFunction(AfModuleTitlebarImpl.FUNCTION_MENU);
        //返回适配器到父类
        return mMultiChoiceAdapter;
    }

    @Override
    protected List<T> onTaskListByPage(Page page, int task) throws Exception {
        return onTaskListFromDomain(task, page);
    }

    protected AfMultiChoiceAdapter<T> getMultiChoiceAdapter(List<T> ltdata) {
        return new AbListViewAdapter(ltdata);
    }

    protected class AbListViewAdapter extends AfMultiChoiceAdapter<T> {

        public AbListViewAdapter(List<T> ltdata) {
            super(getActivity(), ltdata);
        }

        @Override
        protected AfMultiChoiceItem<T> getMultiChoiceItem(T data) {
            return AbSuperListViewActivity.this.getItemLayout(data);
        }
    }

    /**
     * 发送刷新任务
     *
     * @param progress 是否显示正在加载页面
     */
    protected void postRefreshTask(boolean progress) {
        onRefresh();
        if (progress) {
            setLoading();
        }
    }
}
