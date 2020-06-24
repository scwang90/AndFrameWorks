package com.andframe.impl.wrapper;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.$;
import com.andframe.api.DialogBuilder;
import com.andframe.api.pager.Pager;
import com.andframe.api.query.ViewQuery;
import com.andframe.api.task.Task;
import com.andframe.api.viewer.Viewer;
import com.andframe.feature.AfIntent;

/**
 * PagerWrapper 实现 pager 和 viewer 的分离
 * Created by SCWANG on 2016/12/22.
 */
@SuppressWarnings("unused")
public class PagerWrapper implements Pager {

    protected Pager pager;
    protected Viewer viewer;
    protected Activity activity;

    public PagerWrapper(@NonNull Activity activity) {
        this.activity = activity;
    }

    public PagerWrapper(@NonNull Pager pager) {
        this.pager = pager;
        this.viewer = pager;
    }

    public PagerWrapper(@NonNull Pager pager, @NonNull Viewer viewer) {
        this.pager = pager;
        this.viewer = viewer;
    }

    @Override
    public void finish() {
        if (pager != null) {
            pager.finish();
        } else if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void setResultOk(Object... args) {
        if (pager != null) {
            pager.setResultOk(args);
        } else if (activity != null) {
            activity.setResult(Activity.RESULT_OK, new AfIntent().putKeyVaules(args));
        }
    }

    @Override
    public Context getContext() {
        if (viewer != null) {
            return viewer.getContext();
        } else if (activity != null) {
            return activity;
        }
        return null;
    }

    @Override
    public View getView() {
        if (viewer != null) {
            return viewer.getView();
        } else if (activity != null) {
            return activity.findViewById(android.R.id.content);
        }
        return null;
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (viewer != null) {
            return viewer.findViewById(id);
        } else if (activity != null) {
            return activity.findViewById(id);
        }
        return null;
    }

    @Override
    public <T extends View> T findViewById(int id, Class<T> clazz) {
        if (viewer != null) {
            return viewer.findViewById(id, clazz);
        } else if (activity != null) {
            View viewById = activity.findViewById(id);
            if (clazz.isInstance(viewById)) {
                return clazz.cast(viewById);
            }
        }
        return null;
    }

    @Override
    public boolean isRecycled() {
        if (pager != null) {
            return pager.isRecycled();
        } else if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            activity.isDestroyed();
        }
        return activity == null;
    }

    @Override
    public boolean isFinishing() {
        if (pager != null) {
            return pager.isFinishing();
        } else if (activity != null) {
            return activity.isFinishing();
        }
        return false;
    }

    @Override
    public boolean isShowing() {
        if (pager != null) {
            return pager.isShowing();
        }
        return activity != null;
    }

    @Override
    public Activity getActivity() {
        if (pager != null) {
            return pager.getActivity();
        } else {
            return activity;
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (pager != null) {
            pager.startActivity(intent);
        } else if (activity != null) {
            activity.startActivity(intent);
        }
    }

    @Override
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        if (pager != null) {
            pager.startFragment(clazz, args);
        } else {
            $.pager().startFragment(clazz, args);
        }
    }

    @Override
    public void startActivity(Class<? extends Activity> clazz, Object... args) {
        if (pager != null) {
            pager.startActivity(clazz, args);
        } else {
            $.pager().startActivity(clazz, args);
        }
    }

    @Override
    public void startService(Class<? extends Service> clazz, Object... args) {
        if (pager != null) {
            pager.startService(clazz, args);
        } else {
            $.pager().startService(clazz, args);
        }
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        if (pager != null) {
            pager.startFragmentForResult(clazz, request, args);
        } else {
            $.pager().startFragmentForResult(clazz, request, args);
        }
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> clazz, int request, Object... args) {
        if (pager != null) {
            pager.startActivityForResult(clazz, request, args);
        } else {
            $.pager().startActivityForResult(clazz, request, args);
        }
    }

    @Override
    public boolean startPager(Class clazz, Object... args) {
        if (pager != null) {
            return pager.startPager(clazz, args);
        } else {
            return $.pager().startPager(clazz, args);
        }
    }

    @Override
    public void toast(CharSequence tip) {
        if (pager != null) {
            pager.toast(tip);
        } else {
            $.toaster().toast(tip);
        }
    }

    @Override
    public void toast(CharSequence tip, Throwable e) {
        if (pager != null) {
            pager.toast(tip, e);
        } else {
            $.toaster().error(tip, e);
        }
    }

    @Override
    public <T extends Task> T postTask(T task) {
        if (pager != null) {
            return pager.postTask(task);
        } else if (activity != null) {
            return $.task().postTask(task);
        }
        return null;
    }

    private DialogBuilder dialogBuilder;
    @Override
    public Dialog showProgressDialog(CharSequence message) {
        if (pager != null) {
            return pager.showProgressDialog(message);
        } else if (activity != null) {
            dialogBuilder = $.dialog(activity);
            return dialogBuilder.showProgressDialog(message);
        }
        return null;
    }

    @Override
    public void setProgressDialogText(CharSequence text) {
        if (pager != null) {
            pager.setProgressDialogText(text);
        } else if (dialogBuilder != null) {
            dialogBuilder.setProgressDialogText(text);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (pager != null) {
            pager.hideProgressDialog();
        } else if (dialogBuilder != null) {
            dialogBuilder.hideProgressDialog();
            dialogBuilder = null;
        }
    }

    @Override
    public boolean isProgressDialogShowing() {
        if (pager != null) {
            return pager.isProgressDialogShowing();
        } else if (dialogBuilder != null) {
            return dialogBuilder.isProgressDialogShowing();
        }
        return false;
    }

    @Override
    public void setViewQuery(ViewQuery<? extends ViewQuery> viewQuery) {
        if (pager != null) {
            pager.setViewQuery(viewQuery);
        }
    }

    @Override
    public ViewQuery<? extends ViewQuery> getViewQuery() {
        if (pager != null) {
            return pager.getViewQuery();
        }
        return null;
    }
}
