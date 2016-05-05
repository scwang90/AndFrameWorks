package com.andoffice.activity.framework;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.andframe.activity.AfActivity;
import com.andframe.application.AfExceptionHandler;
import com.andframe.bean.Page;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfIntent;
import com.andframe.thread.AfListViewTask;
import com.andframe.thread.AfTask;
import com.andframe.view.multichoice.AfMultiChoiceAdapter;
import com.andframe.view.multichoice.AfMultiChoiceItem;
import com.andframe.view.tableview.AfColumn;
import com.andframe.view.tableview.AfTable;
import com.andoffice.R;
import com.andoffice.activity.TableViewActivity;
import com.andoffice.bean.Permission;
import com.andoffice.domain.IDomain;
import com.andoffice.domain.impl.ImplDomain;
import com.andoffice.layoutbind.ListItem;
import com.andoffice.layoutbind.ModuleBottombar;
import com.andoffice.layoutbind.ModuleBottombarSelector;
import com.andoffice.layoutbind.ModuleTitlebar;
import com.andoffice.layoutbind.ModuleTitlebarSearcher;
import com.andoffice.layoutbind.ModuleTitlebarSearcher.SearchOptions;
import com.andoffice.layoutbind.ModuleTitlebarSearcher.SearcherListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbModeuleListActivity<T> extends AbSuperListViewActivity<T> implements SearcherListener {

    /**
     * 列表选择模式 控制传递常量
     */
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_PERMISSION = "EXTRA_PERMISSION";
    public static final String EXTRA_ISSELECTION = "EXTRA_ISSELECTION";
    public static final String EXTRA_SELECTIONS = "EXTRA_SELECTIONS";
    public static final String EXTRA_ISSINGLE = "EXTRA_SELECTIONS";
    /**
     * 模块列表请求 类型 枚举
     */
    protected static final int REQUEST_ADD = 10;
    protected static final int REQUEST_EDIT = 20;
    protected static final int REQUEST_DELETE = 30;
    protected static final int REQUEST_TABLE = 40;
    protected static final int REQUEST_SEARCH = 50;
    protected static final int REQUEST_FINISH = 60;
    /**
     * 模块列表 任务类型枚举
     */
    protected static final int TASK_DELETE = 10000;
    protected static final int TASK_READ = 20000;
    protected static final int TASK_MODIFY = 30000;

    /**
     * 模块权限对象
     */
    protected Permission mPermission = null;

    /**
     * 当前选择项 用于 删除和阅读任务
     */
    protected T mSelected = null;
    protected List<T> mltDelete = null;
    /**
     *
     */
    protected Class<T> mClazzModel = null;
    protected Class<? extends AfActivity> mClazzAdd = null;
    protected Class<? extends AfActivity> mClazzView = null;

    protected IDomain<T> mDomain = null;

    protected Map<String, String> mSeaechEntry = null;

    protected boolean mIsSingle = false;
    protected boolean mIsSelection = false;
    protected List<T> mltSelection = null;

    public AbModeuleListActivity(Class<T> clazz) {
        mClazzModel = clazz;
        mDomain = getDomain();
    }

    public AbModeuleListActivity(Class<T> clazz,
                                 Class<? extends AfActivity> clazzview) {
        this(clazz);
        mClazzView = clazzview;
        mClazzAdd = clazzview;
    }

    public AbModeuleListActivity(Class<T> clazz,
                                 Class<? extends AfActivity> clazzview,
                                 Class<? extends AfActivity> clazzadd) {
        this(clazz, clazzview);
        mClazzAdd = clazzadd;
    }

    public AbModeuleListActivity(Class<T> clazz, IDomain<T> domain) {
        mClazzModel = clazz;
        if (domain != null) {
            mDomain = domain;
        } else {
            mDomain = getDomain();
        }
    }

    public AbModeuleListActivity(Class<T> clazz, IDomain<T> domain,
                                 Class<? extends AfActivity> clazzview) {
        this(clazz, domain);
        mClazzView = clazzview;
        mClazzAdd = clazzview;
    }

    public AbModeuleListActivity(Class<T> clazz, IDomain<T> domain,
                                 Class<? extends AfActivity> clazzview,
                                 Class<? extends AfActivity> clazzadd) {
        this(clazz, domain, clazzview);
        mClazzAdd = clazzadd;
    }

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        super.onCreate(bundle, intent);
        //根据传递值设置标题
        mTitlebar.setTitle(intent.getString(EXTRA_TITLE, getString(R.string.app_name)));
        //判断调用模式
        if (intent.getBoolean(EXTRA_ISSELECTION, mIsSelection)) {
            mIsSingle = intent.getBoolean(EXTRA_ISSINGLE, mIsSingle);
            mltSelection = intent.getList(EXTRA_SELECTIONS, mClazzModel);
            //设置权限为只读
            mIsSelection = true;
            mPermission = new Permission(true, false, false, false);
            //mModuleListView.getAfRefreshableView().setRefreshable(false);
            //mBottombar.setFunction(ModuleBottombar.ID_CONFIRM, true);
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            map.put("搜索", REQUEST_SEARCH);
            mTitlebarSelector.addMeuns(map);
            mTitlebarSelector.setMenuItemListener(this);

            int imageid = ModuleBottombarSelector.ID_OK;
            String deldetail = ModuleBottombarSelector.DETAIL_OK;
            mBottombarSelector.addFunction(REQUEST_FINISH, imageid, deldetail);
            mBottombarSelector.setMenuItemListener(this);
        } else {
            mPermission = intent.get(EXTRA_PERMISSION, Permission.class);
            if (mPermission == null) {
                mPermission = new Permission(true, true, true, true);
//				throw new AfToastException("没有设置模块权限！");
            } else if (!mPermission.IsRead) {
                //throw new AfToastException("您没有权限浏览本模块！");
                makeToastLong("您已经被管理员取消本模块的使用权限！");
                getActivity().finish();
                return;
            }
        }
        onCreate(bundle, intent, mPermission);
    }

    protected void onCreate(Bundle bundle, AfIntent intent, Permission permission) throws Exception {
        //非选择状态
        if (!mIsSelection) {
            if (mPermission.IsDelete) {
                buildMultiDelete();
                if (mPermission.IsModify) {
                    buildModifyFunction();
                }
            }
            if (mPermission.IsAdd) {
                buildAddFunction();
            }
        }
    }

    protected void buildAddFunction(Class<? extends AfActivity> clazz) {
        mClazzAdd = clazz;
        buildAddFunction();
    }

    protected void buildAddFunction() {
        if (mClazzAdd != null) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            map.put("添加", REQUEST_ADD);
            mTitlebar.addMeuns(map);
            mTitlebar.setMenuItemListener(this);
            mTitlebar.setFunction(ModuleTitlebar.FUNCTION_MENU);

            mBottombar.setFunction(ModuleBottombar.ID_ADD, true);
            mBottombar.setAddListener(this);
        }
    }

    protected void buildSearch(List<Entry<String, String>> options) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("搜索", REQUEST_SEARCH);
        mTitlebar.addMeuns(map);
        mTitlebar.setMenuItemListener(this);
        mTitlebar.setFunction(ModuleTitlebar.FUNCTION_MENU);

        mBottombar.setFunction(ModuleBottombar.ID_SEARCH, true);
        mBottombar.setSearchListener(this);

        mTitlebarSearcher.setOptions(options);
    }

    protected void buildSearch() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("搜索", REQUEST_SEARCH);
        mTitlebar.addMeuns(map);
        mTitlebar.setMenuItemListener(this);
        mTitlebar.setFunction(ModuleTitlebar.FUNCTION_MENU);

        mBottombar.setFunction(ModuleBottombar.ID_SEARCH, true);
        mBottombar.setSearchListener(this);

        mTitlebarSearcher.setOptions(onBuildSearchItem());
    }

    /**
     * 获取搜索选项
     *
     * @return 子类可以重写这个方法自定义搜索字段
     */
    protected SearchOptions onBuildSearchItem() {
        SearchOptions entitys = new SearchOptions();
        entitys.put("Name", "名称");
        return entitys;
    }

    protected void buildTableView() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("表格视图", REQUEST_TABLE);
        mTitlebar.addMeuns(map);
        mTitlebar.setMenuItemListener(this);
        mTitlebar.setFunction(ModuleTitlebar.FUNCTION_MENU);

        mBottombar.setFunction(ModuleBottombar.ID_TABLE, true);
        mBottombar.setTableListener(this);

        getTableColumn();
    }

    protected void buildModifyFunction(Class<? extends AfActivity> clazz) {
        mClazzAdd = clazz;
        buildModifyFunction();
    }

    protected void buildModifyFunction() {
        if (mClazzAdd != null) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            map.put("编辑", REQUEST_EDIT);
            mTitlebarSelector.addMeuns(map);
            mTitlebarSelector.setMenuItemListener(this);

            int delimageid = ModuleBottombarSelector.ID_EDIT;
            String deldetail = ModuleBottombarSelector.DETAIL_EDIT;
            mBottombarSelector.addFunction(REQUEST_EDIT, delimageid, deldetail);
            mBottombarSelector.setMenuItemListener(this);
        }
    }

    protected void buildMultiDelete() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("删除", REQUEST_DELETE);
        mTitlebarSelector.addMeuns(map);
        mTitlebarSelector.setMenuItemListener(this);

        int delimageid = ModuleBottombarSelector.ID_DELETE;
        String deldetail = ModuleBottombarSelector.DETAIL_DELETE;
        mBottombarSelector.addFunction(REQUEST_DELETE, delimageid, deldetail);
        mBottombarSelector.setMenuItemListener(this);
    }

    /**
     * 响应返回选择器
     *
     * @param list
     */
    protected void doResultSelection(List<T> list) {
        if (mIsSelection) {
            AfIntent intent = new AfIntent();
            if (mIsSingle) {
                if (list.size() > 0) {
                    intent.put(EXTRA_RESULT, list.get(0));
                    setResult(RESULT_OK, intent);
                }
            } else {
                intent.putList(EXTRA_RESULT, list);
                setResult(RESULT_OK, intent);
            }
            this.finish();
        }
    }

    private void doSearchHide() {
        mTitlebarSearcher.hide();
        mBottombar.setHighLightMode(false);
        mBottombar.show();
        if (mSeaechEntry != null) {
            mSeaechEntry = null;
            postRefreshTask(true);
        }
    }

    /**
     * 在多选状态下 点击编辑按钮 启动编辑页面
     */
    protected void doRequestEdit() {
        if (mMultiChoiceAdapter.isMultiChoiceMode()) {
            List<T> list = mMultiChoiceAdapter.peekSelectedItems();
            if (list.size() == 0) {
                makeToastLong("还没有选择任何数据喔~");
            } else if (list.size() == 1) {
                mMultiChoiceAdapter.closeMultiChoice();
                doStartEditActivity(mSelected = list.get(0));
            } else {
                makeToastLong("一次只能编辑一条记录喔~");
            }
        }
    }

    /**
     * 点击搜索按钮，显示搜索控件
     */
    protected void doSearchShow() {
        mTitlebarSearcher.show();
        mTitlebarSearcher.setListener(this);
        mBottombar.hide();
        mBottombar.setHighLightMode(true);
    }

    /**
     * 点击删除按钮 提问确认删除
     */
    protected void doReauestDelete() {
        if (mMultiChoiceAdapter.isMultiChoiceMode()) {
            final List<T> list = mMultiChoiceAdapter.peekSelectedItems();
            if (list.size() > 0) {
                Builder builder = new Builder(this);
                builder.setTitle("提问");
                builder.setMessage("确定要删除" + list.size() + "个对象吗？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mSelected = null;
                                mltDelete = list;
                                postTask(TASK_DELETE);
                            }
                        });
                builder.show();
            } else {
                makeToastLong("还没有选择任何项喔~");
            }
        }
    }

    /**
     * 点击表格视图按钮，启动表格页面
     */
    protected void doShowTableView() {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            makeToastLong("现在还没有任何数据喔~");
            return;
        }
        AfTable table = AfTable.buildTable(this, getTableColumn());
        TableViewActivity.setData(table);
        TableViewActivity.setData(mAdapter.getList());
        startActivity(TableViewActivity.class);
    }

    /**
     * 点击添加按钮，启动添加页面
     */
    protected void doStartAddActivity() {
        AfIntent intent = new AfIntent(this, mClazzAdd);
        onPutIntentWhenStartAddOrEdit(intent, null);
        startActivityForResult(intent, REQUEST_ADD);
    }

    /**
     * 点击编辑按钮，启动编辑页面
     *
     * @param model
     */
    protected void doStartEditActivity(T model) {
        AfIntent intent = new AfIntent(this, mClazzView);
        onPutIntentWhenStartAddOrEdit(intent, model);
        startActivityForResult(intent, REQUEST_EDIT);
    }

    /**
     * 当 调用 buildTableView() 会获取 TableView 的配置列
     *
     * @return
     */
    protected AfColumn[] getTableColumn() {
        AfColumn[] list = new AfColumn[]{
                new AfColumn("名称", "Name", 0.333f, AfColumn.STRING),
                new AfColumn("备注", "Remark", 0.333f, AfColumn.STRING),
                new AfColumn("日期", "RegDate", 0.333f, "y-M-d HH:mm")};
        return list;
    }

    protected Object getModel(T model) {
        return model;
    }

    protected IDomain<T> getDomain() {
        return new ImplDomain<T>(mClazzModel);
    }

    protected String getWhere() {
        if (mSeaechEntry != null) {
            String option = mSeaechEntry.get("Key");
            String keyword = mSeaechEntry.get("Value");
            return String.format(" where %s like '%%%s%%'", option, keyword);
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        if (v == null) {
            super.onClick(v);
            return;
        }
        if (v.getId() == ModuleTitlebar.ID_ADD) {
            this.doStartAddActivity();
            return;
        }
        switch (v.getId()) {
            case ModuleBottombar.ID_ADD:
                this.doStartAddActivity();
                break;
            case ModuleBottombar.ID_TABLE:
                this.doShowTableView();
                break;
            case ModuleBottombar.ID_SEARCH:
                if (mTitlebarSearcher.isVisibility()) {
                    this.doSearchHide();
                } else {
                    this.doSearchShow();
                }
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    @Override
    public void onSoftInputHiden() {
        super.onSoftInputHiden();
        mBottombar.show();
        if (mTitlebarSearcher.isVisibility() && mSeaechEntry == null) {
            this.doSearchHide();
        }
    }

    @Override
    public void onSoftInputShown() {
        super.onSoftInputShown();
        mBottombar.hide();
    }

    @Override
    public boolean onSearch(ModuleTitlebarSearcher searcher, String option,
                            String keyword) {
        if (keyword.equals("")) {
            this.doSearchHide();
        } else {
            mSeaechEntry = new HashMap<String, String>();
            mSeaechEntry.put("Key", option);
            mSeaechEntry.put("Value", keyword);
            this.postRefreshTask(true);
        }
        return false;
    }

    @Override
    protected AfMultiChoiceItem<T> getItemLayout(T data) {
        return new ListItem<T>();
    }

    @Override
    protected void onActivityResult(AfIntent intent, int requestcode, int resultcode) throws Exception {
        super.onActivityResult(intent, requestcode, resultcode);
        if (resultcode == RESULT_OK) {
            T value = intent.get(EXTRA_RESULT, mClazzModel);
            switch (requestcode) {
                case REQUEST_ADD:
                    if (value != null) {
                        this.onDataAdded(value, intent);
                    }
                    break;
                case REQUEST_EDIT:
                    if (value != null && value.equals(mSelected)) {
                        this.onDataEdited(mSelected, value, intent);
                    }
                    break;
            }
        }
    }

    protected void onDataEdited(T oldv, T newv, AfIntent intent) {
        List<T> mltArray = mAdapter.getList();
        int index = mltArray.indexOf(oldv);
        if (index >= 0) {
            mltArray.set(index, newv);
            mAdapter.setData(mltArray);
        }
    }

    protected void onDataAdded(T value, AfIntent intent) {
        addData(value);
    }

    protected void onDataDeleted(T model) {
    }

    @Override
    public boolean onMenuItemClick(MenuItem menu) {
        switch (menu.getItemId()) {
            case REQUEST_DELETE:
                this.doReauestDelete();
                break;
            case REQUEST_ADD:
                this.doStartAddActivity();
                break;
            case REQUEST_EDIT:
                this.doRequestEdit();
                break;
            case REQUEST_TABLE:
                this.doShowTableView();
                break;
            case REQUEST_SEARCH:
                this.doSearchShow();
                break;
            case REQUEST_FINISH:
                this.doResultSelection(mMultiChoiceAdapter.peekSelectedItems());
                break;
            default:
                return super.onMenuItemClick(menu);
        }
        return true;
    }

    @Override
    protected boolean onBackKeyPressed() {
        if (mTitlebarSearcher.isVisibility()) {
            this.doSearchHide();
            return true;
        } else {
            return super.onBackKeyPressed();
        }
    }

    @Override
    protected void onItemClick(T model, int index) {
        super.onItemClick(model, index);
        mSelected = model;
        if (mClazzView != null) {
            doStartEditActivity(mSelected);
        }
    }

    /**
     * 在启动添加或者编辑页面时候 向 intent 中Put参数
     *
     * @param intent
     * @param model  当前选择的对象 为null是表示添加页面
     */
    protected void onPutIntentWhenStartAddOrEdit(AfIntent intent, T model) {
        String expermiss = AbModeuleListActivity.EXTRA_PERMISSION;
        intent.put(expermiss, mPermission);
        if (model != null) {
            intent.put(EXTRA_DATA, getModel(model));
        }
    }

    @Override
    protected boolean onTaskPrepare(int task) {
        switch (task) {
            case TASK_DELETE:
                showProgressDialog("正在删除...");
                if (mltDelete == null && mSelected != null) {
                    mltDelete = new ArrayList<T>();
                    mltDelete.add(mSelected);
                }
                return true;
        }
        return super.onTaskPrepare(task);
    }

    @Override
    protected List<T> onTaskLoad(boolean isCheckExpired) {
        if (mltSelection != null) {
            mIsPaging = false;
            return mltSelection;
        }
        return super.onTaskLoad(isCheckExpired);
    }

    /**
     * 异步线程加载数据（使用 Domain）
     */
    @Override
    protected List<T> onTaskListFromDomain(int task, Page page) throws Exception {
        String where = getWhere();
        return mDomain.GetListByPage(where, page);
    }

    @Override
    protected boolean onTaskWorking(int task) throws Exception {
        switch (task) {
            case TASK_READ:
                onTaskRead(mSelected);
                return true;
            case TASK_DELETE:
                if (mltDelete != null && mltDelete.size() > 0) {
                    onTaskDelete(mltDelete);
                } else {
                    String remark = "AbModeuleListActivity.onTaskWorking.TASK_DELETE mltDelete 空 抛出异常\r\n";
                    remark += "class = " + getClass().toString();
                    try {
                        throw new AfToastException("mltDelete 为空");
                    } catch (Throwable e) {
                        AfExceptionHandler.handler(e, remark);
                    }
                }
                return true;
        }
        return super.onTaskWorking(task);
    }

    protected void onTaskRead(T model) throws Exception {
        mDomain.Update(model);
    }

    protected void onTaskDelete(List<T> mltmodel) throws Exception {
        mDomain.DeleteList(mltmodel);
    }

    @Override
    protected boolean onTaskWorked(AbListViewTask task, boolean isfinish, List<T> ltdata) {
        switch (task.mTask) {
            case TASK_DELETE:
                hideProgressDialog();
                if (task.mResult == AfTask.RESULT_FINISH) {
                    makeToastShort("删除成功！");
                    List<T> mltArray = mAdapter.getList();
                    for (T model : mltDelete) {
                        onDataDeleted(model);
                        mltArray.remove(model);
                    }
                    mMultiChoiceAdapter.setData(mltArray);
                    mMultiChoiceAdapter.closeMultiChoice();
                } else {
                    makeToastShort(task.makeErrorToast("删除失败"));
                }
                mltDelete = null;
                return true;
            case TASK_READ:
                if (task.mResult == AfTask.RESULT_FINISH) {
                    mAdapter.notifyDataSetChanged();
                }
                return true;
            case AfListViewTask.TASK_LOAD:
            case AfListViewTask.TASK_REFRESH:
                if (super.onTaskWorked(task, isfinish, ltdata) && mIsSelection) {
                    if (mMultiChoiceAdapter != null) {
                        mMultiChoiceAdapter.setSingle(mIsSingle);
                        mMultiChoiceAdapter.beginMultiChoice(false);
                    }
                }
                return true;
        }
        return super.onTaskWorked(task, isfinish, ltdata);
    }

    @Override
    public void onMultiChoiceClosed(AfMultiChoiceAdapter<T> adapter,
                                    List<T> list) {
        super.onMultiChoiceClosed(adapter, list);
    }
}
