package com.andframe.annotation.interpreter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.andframe.annotation.view.BindCheckedChange;
import com.andframe.annotation.view.BindCheckedChangeGroup;
import com.andframe.annotation.view.BindClick;
import com.andframe.annotation.view.BindClickType;
import com.andframe.annotation.view.BindItemClick;
import com.andframe.annotation.view.BindItemLongClick;
import com.andframe.annotation.view.BindLongClick;
import com.andframe.annotation.view.BindLongClickType;
import com.andframe.annotation.view.BindTouch;
import com.andframe.annotation.view.BindTouchType;
import com.andframe.annotation.view.BindView;
import com.andframe.annotation.view.BindViewCreated;
import com.andframe.annotation.view.BindViewModule;
import com.andframe.annotation.view.idname.BindCheckedChange$;
import com.andframe.annotation.view.idname.BindCheckedChangeGroup$;
import com.andframe.annotation.view.idname.BindClick$;
import com.andframe.annotation.view.idname.BindItemClick$;
import com.andframe.annotation.view.idname.BindItemLongClick$;
import com.andframe.annotation.view.idname.BindLongClick$;
import com.andframe.annotation.view.idname.BindTouch$;
import com.andframe.annotation.view.idname.BindView$;
import com.andframe.annotation.view.idname.BindViewModule$;
import com.andframe.api.viewer.ItemsViewer;
import com.andframe.api.viewer.Viewer;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.impl.viewer.ItemsViewerWrapper;
import com.andframe.module.AfFrameSelector;
import com.andframe.module.AfSelectorBottomBar;
import com.andframe.module.AfSelectorBottomBarImpl;
import com.andframe.module.AfSelectorTitleBar;
import com.andframe.module.AfSelectorTitleBarImpl;
import com.andframe.module.AfViewModuler;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.andframe.annotation.interpreter.ReflecterCacher.getFields;
import static com.andframe.annotation.interpreter.ReflecterCacher.getMethods;
import static com.andframe.annotation.interpreter.SmartInvoke.paramAllot;


/**
 * 控件绑定器
 *
 * @author 树朾
 */
@SuppressWarnings("unused")
public class ViewBinder {

    protected static String TAG(Object obj, String tag) {
        if (obj == null) {
            return "ViewBinder." + tag;
        }
        return "LayoutBinder(" + obj.getClass().getName() + ")." + tag;
    }

    public static void doBind(Viewer root) {
        doBind(root, root);
    }

    public static void doBind(Object handler, Viewer root) {

        Field[] fields = getFields(handler);
        for (Field field : fields) {
            bindView(field, handler, root);
            bindViewModule(field, handler, root);

            bindView$(field, handler, root);
            bindViewModule$(field, handler, root);
        }

        Method[] methods = getMethods(handler);
        for (Method method : methods) {
            bindClick(method, handler, root);
            bindTouch(method, handler, root);
            bindLongClick(method, handler, root);
            bindItemClick(method, handler, root);
            bindItemLongClick(method, handler, root);
            bindCheckedChange(method, handler, root);
            bindCheckedChangeGroup(method, handler, root);

            bindClickType(method, handler, root);
            bindTouchType(method, handler, root);
            bindLongClickType(method, handler, root);

            bindClick$(method, handler, root);
            bindTouch$(method, handler, root);
            bindLongClick$(method, handler, root);
            bindItemClick$(method, handler, root);
            bindItemLongClick$(method, handler, root);
            bindCheckedChange$(method, handler, root);
            bindCheckedChangeGroup$(method, handler, root);
        }

        bindViewCreated(methods, handler);
    }

    //<editor-fold desc="事件绑定">
    private static void bindTouch(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindTouch.class)) {
            BindTouch bind = method.getAnnotation(BindTouch.class);
            for (int id : bind.value()) {
                View view = root.findViewById(id);
                if (view != null) {
                    view.setOnTouchListener(new EventListener(handler).touch(method));
                } else {
                    notFindView(handler, id, TAG(handler, "bindTouch.") + method.getName());
                }
            }
        }
    }

    private static void bindClick(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindClick.class)) {
            BindClick bind = method.getAnnotation(BindClick.class);
            for (int id : bind.value()) {
                View view = root.findViewById(id);
                if (view != null) {
                    view.setOnClickListener(new EventListener(handler).click(method, bind.intervalTime()));
                } else {
                    notFindView(handler, id, TAG(handler, "doBindClick.") + method.getName());
                }
            }
        }
    }

    private static void bindLongClick(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindLongClick.class)) {
            BindLongClick bind = method.getAnnotation(BindLongClick.class);
            for (int id : bind.value()) {
                View view = root.findViewById(id);
                if (view != null) {
                    view.setOnLongClickListener(new EventListener(handler).longClick(method));
                } else {
                    notFindView(handler, id, TAG(handler, "doBindLongClick.") + method.getName());
                }
            }
        }
    }

    private static void bindItemClick(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindItemClick.class)) {
            BindItemClick bind = method.getAnnotation(BindItemClick.class);
            if (bind.value().length == 0) {
                View[] views = findViewByType(root.getView(), AdapterView.class);
                for (View view : views) {
                    AdapterView.class.cast(view).setOnItemClickListener(new EventListener(handler).itemClick(method, bind.intervalTime()));
                }
            } else for (int id : bind.value()) {
                View view = root.findViewById(id);
                if (view instanceof AdapterView) {
                    ((AdapterView) view).setOnItemClickListener(new EventListener(handler).itemClick(method, bind.intervalTime()));
                } else {
                    notFindView(handler, id, TAG(handler, "bindItemClickLongClick.") + method.getName());
                }
            }
        }
    }

    private static void bindItemLongClick(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindItemLongClick.class)) {
            BindItemLongClick bind = method.getAnnotation(BindItemLongClick.class);
            if (bind.value().length == 0) {
                View[] views = findViewByType(root.getView(), AdapterView.class);
                for (View view : views) {
                    AdapterView.class.cast(view).setOnItemLongClickListener(new EventListener(handler).itemLongClick(method));
                }
            } else for (int id : bind.value()) {
                View view = root.findViewById(id);
                if (view instanceof AdapterView) {
                    ((AdapterView) view).setOnItemLongClickListener(new EventListener(handler).itemLongClick(method));
                } else {
                    notFindView(handler, id, TAG(handler, "bindItemLongClick.") + method.getName());
                }
            }
        }
    }

    private static void bindCheckedChange(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindCheckedChange.class)) {
            BindCheckedChange bind = method.getAnnotation(BindCheckedChange.class);
            for (int id : bind.value()) {
                View view = root.findViewById(id);
                if (view instanceof CompoundButton) {
                    ((CompoundButton) view).setOnCheckedChangeListener(new EventListener(handler).checkedChange(method));
                } else {
                    notFindView(handler, id, TAG(handler, "bindCheckedChange.") + method.getName());
                }
            }
        }
    }

    private static void bindCheckedChangeGroup(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindCheckedChangeGroup.class)) {
            BindCheckedChangeGroup bind = method.getAnnotation(BindCheckedChangeGroup.class);
            for (int id : bind.value()) {
                View view = root.findViewById(id);
                if (view instanceof RadioGroup) {
                    ((RadioGroup) view).setOnCheckedChangeListener(new EventListener(handler).checkedChangeGroup(method));
                } else {
                    notFindView(handler, id, TAG(handler, "bindCheckedChangeGroup.") + method.getName());
                }
            }
        }
    }


    private static void bindTouchType(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindTouchType.class)) {
            BindTouchType bind = method.getAnnotation(BindTouchType.class);
            for (Class<? extends View> type : bind.value()) {
                View[] views = findViewByType(root.getView(), type);
                if (views.length > 0) {
                    for (View view : views) {
                        view.setOnTouchListener(new EventListener(handler).touch(method));
                    }
                } else {
                    notFindView(handler, type.toString(), TAG(handler, "bindTouch.") + method.getName());
                }
            }
        }
    }

    private static void bindClickType(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindClickType.class)) {
            BindClickType bind = method.getAnnotation(BindClickType.class);
            for (Class<? extends View> type : bind.value()) {
                View[] views = findViewByType(root.getView(), type);
                if (views.length > 0) {
                    for (View view : views) {
                        view.setOnClickListener(new EventListener(handler).click(method, bind.intervalTime()));
                    }
                } else {
                    notFindView(handler, type.toString(), TAG(handler, "doBindClick.") + method.getName());
                }
            }
        }
    }

    private static void bindLongClickType(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindLongClickType.class)) {
            BindLongClickType bind = method.getAnnotation(BindLongClickType.class);
            for (Class<? extends View> type : bind.value()) {
                View[] views = findViewByType(root.getView(), type);
                if (views.length > 0) {
                    for (View view : views) {
                        view.setOnLongClickListener(new EventListener(handler).longClick(method));
                    }
                } else {
                    notFindView(handler, type.toString(), TAG(handler, "doBindLongClick.") + method.getName());
                }
            }
        }
    }

    //</editor-fold>

    //<editor-fold desc="事件绑定-兼容">
    private static void bindTouch$(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindTouch$.class)) {
            BindTouch$ bind = method.getAnnotation(BindTouch$.class);
            for (int id : ids(root, bind.value())) {
                View view = root.findViewById(id);
                if (view != null) {
                    view.setOnTouchListener(new EventListener(handler).touch(method));
                } else {
                    notFindView(handler, id, TAG(handler, "bindTouch$.") + method.getName());
                }
            }
        }
    }

    private static void bindClick$(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindClick$.class)) {
            BindClick$ bind = method.getAnnotation(BindClick$.class);
            for (int id : ids(root, bind.value())) {
                View view = root.findViewById(id);
                if (view != null) {
                    view.setOnClickListener(new EventListener(handler).click(method, bind.intervalTime()));
                } else {
                    notFindView(handler, id, TAG(handler, "doBindClick$.") + method.getName());
                }
            }
        }
    }

    private static void bindLongClick$(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindLongClick$.class)) {
            BindLongClick$ bind = method.getAnnotation(BindLongClick$.class);
            for (int id : ids(root, bind.value())) {
                View view = root.findViewById(id);
                if (view != null) {
                    view.setOnLongClickListener(new EventListener(handler).longClick(method));
                } else {
                    notFindView(handler, id, TAG(handler, "doBindLongClick$.") + method.getName());
                }
            }
        }
    }

    private static void bindItemClick$(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindItemClick$.class)) {
            BindItemClick$ bind = method.getAnnotation(BindItemClick$.class);
            if (bind.value().length == 0) {
                View[] views = findViewByType(root.getView(), AdapterView.class);
                for (View view : views) {
                    AdapterView.class.cast(view).setOnItemClickListener(new EventListener(handler).itemClick(method, bind.intervalTime()));
                }
            } else for (int id : ids(root, bind.value())) {
                View view = root.findViewById(id);
                if (view instanceof AdapterView) {
                    ((AdapterView) view).setOnItemClickListener(new EventListener(handler).itemClick(method, bind.intervalTime()));
                } else {
                    notFindView(handler, id, TAG(handler, "bindItemClickLongClick$.") + method.getName());
                }
            }
        }
    }

    private static void bindItemLongClick$(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindItemLongClick$.class)) {
            BindItemLongClick$ bind = method.getAnnotation(BindItemLongClick$.class);
            if (bind.value().length == 0) {
                View[] views = findViewByType(root.getView(), AdapterView.class);
                for (View view : views) {
                    AdapterView.class.cast(view).setOnItemLongClickListener(new EventListener(handler).itemLongClick(method));
                }
            } else for (int id : ids(root, bind.value())) {
                View view = root.findViewById(id);
                if (view instanceof AdapterView) {
                    ((AdapterView) view).setOnItemLongClickListener(new EventListener(handler).itemLongClick(method));
                } else {
                    notFindView(handler, id, TAG(handler, "bindItemLongClick$.") + method.getName());
                }
            }
        }
    }

    private static void bindCheckedChange$(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindCheckedChange$.class)) {
            BindCheckedChange$ bind = method.getAnnotation(BindCheckedChange$.class);
            for (int id : ids(root, bind.value())) {
                View view = root.findViewById(id);
                if (view instanceof CompoundButton) {
                    ((CompoundButton) view).setOnCheckedChangeListener(new EventListener(handler).checkedChange(method));
                } else {
                    notFindView(handler, id, TAG(handler, "bindCheckedChange$.") + method.getName());
                }
            }
        }
    }

    private static void bindCheckedChangeGroup$(Method method, Object handler, Viewer root) {
        if (method.isAnnotationPresent(BindCheckedChangeGroup$.class)) {
            BindCheckedChangeGroup$ bind = method.getAnnotation(BindCheckedChangeGroup$.class);
            for (int id : ids(root, bind.value())) {
                View view = root.findViewById(id);
                if (view instanceof RadioGroup) {
                    ((RadioGroup) view).setOnCheckedChangeListener(new EventListener(handler).checkedChangeGroup(method));
                } else {
                    notFindView(handler, id, TAG(handler, "bindCheckedChangeGroup$.") + method.getName());
                }
            }
        }
    }

    //</editor-fold>

    private static void bindView(Field field, Object handler, Viewer root) {
        if (field.isAnnotationPresent(BindView.class)) {
            try {
                BindView bind = field.getAnnotation(BindView.class);
                bindView(field, handler, root, bind.value());
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindView.") + field.getName());
            }
        }
    }

    private static void bindView(Field field, Object handler, Viewer root, int[] ids) throws IllegalAccessException {
        List<View> list = new ArrayList<>();
        for (int id : ids) {
            View view = null;
            if (id > 0) {
                view = root.findViewById(id);
            } else if (ids.length > 1 || (!field.getType().isArray() && !Iterable.class.isAssignableFrom(field.getType()))) {
                View[] receiveViews = findViewByType(root.getView(), field.getType(), 1);
                if (receiveViews.length > 0) {
                    view = receiveViews[0];
                }
            } else {
                field.setAccessible(true);
                Object original = field.get(handler);
                View[] receiveViews;
                if (original != null && field.getType().isArray()) {
                    Object[] objects = (Object[]) original;
                    receiveViews = findViewByType(root.getView(), field.getType(), objects.length);
                } else {
                    receiveViews = findViewByType(root.getView(), field.getType(), 0);
                }
                list.addAll(Arrays.asList(receiveViews));
            }
            if (view != null) {
                list.add(view);
            }
        }
        if (list.size() > 0) {
            field.setAccessible(true);
            if (field.getType().isArray()) {
                Class<?> componentType = field.getType().getComponentType();
                Object[] array = list.toArray((Object[]) Array.newInstance(componentType, list.size()));
                field.set(handler, array);
            } else if (Iterable.class.isAssignableFrom(field.getType())) {
                if (List.class.isAssignableFrom(field.getType())) {
                    field.set(handler, list);
                } else {
                    AfExceptionHandler.handle("BindView多个View只支持List和Array", TAG(handler, "doBindView.") + field.getName());
                }
            } else if (field.getType().isInstance(list.get(0))) {
                field.set(handler, list.get(0));
            } else {
                notFindView(handler, ids[0], TAG(handler, "doBindView.") + field.getName());
            }
        }
    }

    private static void bindView$(Field field, Object handler, Viewer root) {
        if (field.isAnnotationPresent(BindView$.class)) {
            try {
                BindView$ bind = field.getAnnotation(BindView$.class);
                bindView(field, handler, root, ids(root, bind.value()));
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindView.") + field.getName());
            }
        }
    }

    private static void bindViewModule(Field field, Object handler, @NonNull Viewer root) {
        if (field.isAnnotationPresent(BindViewModule.class)) {
            try {
                BindViewModule bind = field.getAnnotation(BindViewModule.class);
                bindViewModule(field, handler, root, bind.value());
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindViewModule.") + field.getName());
            }
        }
    }

    private static void bindViewModule(Field field, Object handler, @NonNull Viewer root, int[] ids) throws IllegalAccessException {
        Class<?> type = field.getType();
        List<Object> list = new ArrayList<>();
        for (int id : ids) {
            Object value = null;
            if (Fragment.class.isAssignableFrom(type)) {
                if (handler instanceof Fragment) {
                    value = ((Fragment) handler).getChildFragmentManager().findFragmentById(id);
                } else if (handler instanceof FragmentActivity) {
                    value = ((FragmentActivity) handler).getSupportFragmentManager().findFragmentById(id);
                }
            } else if (type.equals(AfSelectorTitleBar.class)) {
                value = new AfSelectorTitleBarImpl(root);
            } else if (type.equals(AfSelectorBottomBar.class)) {
                value = new AfSelectorBottomBarImpl(root);
            } else if (type.equals(AfFrameSelector.class)) {
                value = new AfFrameSelector(root, id);
            } else if (type.equals(ItemsViewerWrapper.class) || type.equals(ItemsViewer.class)) {
                if (id > 0) {
                    value = new ItemsViewerWrapper((View)root.findViewById(id));
                } else {
                    value = new ItemsViewerWrapper(root);
                }
            } else /*if ((field.getType().isAnnotationPresent(BindLayout.class) || id > 0))*/ {
                if (type.isArray()) {
                    type = field.getType().getComponentType();
                } else if (List.class.isAssignableFrom(type)) {
                    Type generic = field.getGenericType();
                    ParameterizedType parameterized = (ParameterizedType) generic;
                    type = (Class<?>) parameterized.getActualTypeArguments()[0];
                }
                if (id <= 0) {
                    id = LayoutBinder.getBindLayoutId(field.getType(), root.getContext());
                }
                if (id <= 0) {
                    AfExceptionHandler.handle("ViewModuler("+type.getSimpleName()+") 必须指定BindLayout",TAG(handler, "doBindViewModule.") + field.getName());
                } else if (AfViewModuler.class.isAssignableFrom(type)) {
                    //noinspection unchecked
                    value = AfViewModuler.init(handler, (Class<? extends AfViewModuler>) type, root, id);
                } else {
                    AfExceptionHandler.handle("BindViewModule的类型必须继承AfViewModuler",TAG(handler, "doBindViewModule.") + field.getName());
                }
            }
            if (value != null) {
                list.add(value);
            }
        }

        if (list.size() > 0) {
            field.setAccessible(true);
            if (field.getType().isArray()) {
                Class<?> componentType = field.getType().getComponentType();
                Object[] array = list.toArray((Object[]) Array.newInstance(componentType, list.size()));
                field.set(handler, array);
            } else if (List.class.isAssignableFrom(field.getType())) {
                field.set(handler, list);
            } else if (list.get(0) != null) {
                field.set(handler, list.get(0));
            }
        }
    }

    private static void bindViewModule$(Field field, Object handler, @NonNull Viewer root) {
        if (field.isAnnotationPresent(BindViewModule$.class)) {
            try {
                BindViewModule$ bind = field.getAnnotation(BindViewModule$.class);
                bindViewModule(field, handler, root, ids(root, bind.value()));
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindViewModule.") + field.getName());
            }
        }
    }

    private static void bindViewCreated(Method[] methods, Object handler) {
        List<SimpleEntry> entries = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(BindViewCreated.class)) {
                BindViewCreated annotation = method.getAnnotation(BindViewCreated.class);
                entries.add(new SimpleEntry(method, annotation));
            }
        }
        Collections.sort(entries, (lhs, rhs) -> lhs.getValue().value() - rhs.getValue().value());
        for (SimpleEntry entry : entries) {
            try {
                invokeMethod(handler, entry.getKey());
            } catch (Throwable e) {
                if (!entry.getValue().exception()) {
                    throw new RuntimeException("调用视图初始化失败", e);
                }
                AfExceptionHandler.handle(e, TAG(handler, "bindViewCreated.") + entry.getKey().getName());
            }
        }
    }

    private static Object invokeMethod(Object handler, Method method, Object... params) throws Exception {
        if (handler != null && method != null) {
            method.setAccessible(true);
            return method.invoke(handler, params);
        }
        return null;
    }

    public static class SimpleEntry {

        private final Method key;
        private BindViewCreated value;

        public SimpleEntry(Method theKey, BindViewCreated theValue) {
            key = theKey;
            value = theValue;
        }

        public Method getKey() {
            return key;
        }

        public BindViewCreated getValue() {
            return value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }


    public static class EventListener implements OnClickListener,
            View.OnLongClickListener,
            AdapterView.OnItemClickListener,
            AdapterView.OnItemLongClickListener,
            CompoundButton.OnCheckedChangeListener,
            RadioGroup.OnCheckedChangeListener, View.OnTouchListener {

        private Object handler;

        private int clickIntervalTime = 1000;
        private long lastClickTime = 0;

        private Method clickMethod;
        private Method touchMethod;
        private Method longClickMethod;
        private Method itemClickMethod;
        private Method itemLongClickMehtod;
        private Method checkedChangedMehtod;
        private Method checkedChangedMehtodGroup;

        public EventListener(Object handler) {
            this.handler = handler;
        }

        public OnClickListener click(Method method) {
            clickMethod = method;
            return this;
        }

        public OnClickListener click(Method method, int intervalTime) {
            clickMethod = method;
            clickIntervalTime = intervalTime;
            return this;
        }

        public View.OnTouchListener touch(Method method) {
            touchMethod = method;
            return this;
        }

        public View.OnLongClickListener longClick(Method method) {
            this.longClickMethod = method;
            return this;
        }

        public AdapterView.OnItemClickListener itemClick(Method method) {
            this.itemClickMethod = method;
            return this;
        }

        public AdapterView.OnItemClickListener itemClick(Method method, int intervalTime) {
            this.itemClickMethod = method;
            this.clickIntervalTime = intervalTime;
            return this;
        }

        public AdapterView.OnItemLongClickListener itemLongClick(Method method) {
            this.itemLongClickMehtod = method;
            return this;
        }

        public CompoundButton.OnCheckedChangeListener checkedChange(Method method) {
            this.checkedChangedMehtod = method;
            return this;
        }

        public RadioGroup.OnCheckedChangeListener checkedChangeGroup(Method method) {
            this.checkedChangedMehtodGroup = method;
            return this;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return Boolean.valueOf(true).equals(invokeMethod(handler, touchMethod, v, event));
        }

        public void onClick(View v) {
            long timeMillis = System.currentTimeMillis();
            if (timeMillis - lastClickTime > clickIntervalTime) {
                lastClickTime = timeMillis;
                invokeMethod(handler, clickMethod, v);
            }
        }

        public boolean onLongClick(View v) {
            return Boolean.valueOf(true).equals(invokeMethod(handler, longClickMethod, v));
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            long timeMillis = System.currentTimeMillis();
            if (timeMillis - lastClickTime > clickIntervalTime) {
                lastClickTime = timeMillis;
                invokeMethod(handler, itemClickMethod, parent, view, position, id);
            }
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return Boolean.valueOf(true).equals(invokeMethod(handler, itemLongClickMehtod, parent, view, position, id));
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            invokeMethod(handler, checkedChangedMehtod, buttonView, isChecked);
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            invokeMethod(handler, checkedChangedMehtodGroup, radioGroup, i);
        }

        private Object invokeMethod(Object handler, Method method, Object... params) {
            if (handler != null && method != null) {
                try {
                    method.setAccessible(true);
                    return method.invoke(handler, paramAllot(method, params));
                } catch (Throwable e) {
                    e.printStackTrace();
                    AfExceptionHandler.handle(e, "EventListener.invokeMethod");
                }
            }
            return null;
        }

    }


    //<editor-fold desc="转换辅助">
    private static int[] ids(Viewer root, String... value) {
        int[] ids = new int[value.length];
        Resources resources = root.getView().getResources();
        for (int i = 0; i < value.length; i++) {
            ids[i] = resources.getIdentifier(value[i], "id", root.getContext().getPackageName());
        }
        return ids;
    }

    private static View[] findViewByType(View rootview, Class<?> type) {
        return findViewByType(rootview, type, 0);
    }

    private static View[] findViewByType(View rootview, Class<?> type, int count) {
        if (type.isArray()) {
            type = type.getComponentType();
        }
        count = count <= 0 ? Integer.MAX_VALUE : count;

        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(rootview));
        List<View> list = new ArrayList<>(count == Integer.MAX_VALUE ? 0 : count);
        do {
            View cview = views.poll();
            if (cview != null && type.isInstance(cview)) {
                list.add(cview);
            } else {
                if (cview instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) cview;
                    for (int j = 0; j < group.getChildCount(); j++) {
                        views.add(group.getChildAt(j));
                    }
                }
            }
        } while (!views.isEmpty() && list.size() < count);

        return list.toArray(new View[list.size()]);
    }

    //</editor-fold>

    //<editor-fold desc="异常处理">
    private static void notFindView(Object handler, int id, String tag) {
        try {
            String name = AfApp.get().getResources().getResourceName(id);
            notFindView(handler, name, tag);
        } catch (Resources.NotFoundException e) {
            notFindView(handler, null, tag);
        }
    }

    private static void notFindView(Object handler, String name, String tag) {
        if (name == null) {
            AfExceptionHandler.handle("无效ID，无法绑定", tag);
        } else {
            AfExceptionHandler.handle("为["+handler.getClass().getSimpleName()+"]匹配View的时候找不到" + name + "，无法绑定", tag);
        }
    }
    //</editor-fold>
}
