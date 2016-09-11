package android.support.v7.widget;

import android.database.Observable;

import com.andframe.exception.AfExceptionHandler;

import java.lang.reflect.Field;

/**
 *
 * Created by SCWANG on 2016/9/10.
 */
public class AdapterDataObservableWrapper extends RecyclerView.AdapterDataObservable {

    RecyclerView.AdapterDataObservable wrapped = new RecyclerView.AdapterDataObservable();

    public AdapterDataObservableWrapper(RecyclerView.Adapter adapter) {
        bindAdapter(adapter);
    }

    private boolean bindAdapter(RecyclerView.Adapter adapter) {
        Field[] fields = RecyclerView.Adapter.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (Observable.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    wrapped = (RecyclerView.AdapterDataObservable) field.get(adapter);
                    field.set(adapter,this);
                    return true;
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e,"AdapterDataObservableWrapper.bindAdapter");
            }
        }
        return false;
    }

    @Override
    public void notifyChanged() {
        wrapped.notifyChanged();
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        wrapped.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
        wrapped.notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    @Override
    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        wrapped.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        wrapped.notifyItemRangeRemoved(positionStart, itemCount);
    }

    @Override
    public void notifyItemMoved(int fromPosition, int toPosition) {
        wrapped.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void registerObserver(RecyclerView.AdapterDataObserver observer) {
        wrapped.registerObserver(observer);
    }

    @Override
    public void unregisterObserver(RecyclerView.AdapterDataObserver observer) {
        wrapped.unregisterObserver(observer);
    }

    @Override
    public void unregisterAll() {
        wrapped.unregisterAll();
    }

    @Override
    public boolean hasObservers() {
        return wrapped.hasObservers();
    }
}
