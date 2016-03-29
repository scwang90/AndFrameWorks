package com.andframe.annotation.interpreter;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.AfViewable;
import com.andframe.annotation.view.BindAfterViews;
import com.andframe.annotation.view.BindCheckedChange;
import com.andframe.annotation.view.BindClick;
import com.andframe.annotation.view.BindItemClick;
import com.andframe.annotation.view.BindItemLongClick;
import com.andframe.annotation.view.BindLayout;
import com.andframe.annotation.view.BindLongClick;
import com.andframe.annotation.view.BindView;
import com.andframe.annotation.view.BindViewModule;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.framework.EventListener;
import com.andframe.layoutbind.AfFrameSelector;
import com.andframe.layoutbind.AfModuleNodata;
import com.andframe.layoutbind.AfModuleNodataImpl;
import com.andframe.layoutbind.AfModuleProgress;
import com.andframe.layoutbind.AfModuleProgressImpl;
import com.andframe.layoutbind.AfModuleTitlebar;
import com.andframe.layoutbind.AfModuleTitlebarImpl;
import com.andframe.layoutbind.framework.AfViewDelegate;
import com.andframe.layoutbind.framework.AfViewModule;
import com.andframe.util.java.AfReflecter;
import com.andframe.view.AfContactsRefreshView;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * 控件绑定器
 *
 * @author 树朾
 */
public class ViewBinder {

    protected static String TAG(Object obj, String tag) {
        if (obj == null) {
            return "ViewBinder." + tag;
        }
        return "LayoutBinder(" + obj.getClass().getName() + ")." + tag;
    }

    public static void doBind(AfViewable root) {
        doBind(root, (AfViewable) root);
    }

    public static void doBind(Object handler, View root) {
        doBind(handler, (AfViewable) new AfView(root));
    }

    public static void doBind(Object handler, AfViewable root) {
        bindClick(handler, root);
        bindLongClick(handler, root);
        bindItemClick(handler, root);
        bindItemLongClick(handler, root);
        bindCheckedChange(handler, root);
        bindView(handler, root);
        bindViewModule(handler, root);
        bindAfterView(handler, root);
    }

    private static Class<?> getStopType(Object handler) {
        if (handler instanceof AfViewDelegate) {
            return AfViewDelegate.class;
        }
        return Object.class;
    }

    private static void bindClick(Object handler, AfViewable root) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindClick.class)) {
            try {
                BindClick bind = method.getAnnotation(BindClick.class);
                for (int id : bind.value()) {
                    View view = root.findViewById(id);
                    view.setOnClickListener(new EventListener(handler).click(method));
                }
            } catch (Throwable e) {
                AfExceptionHandler.handler(e, TAG(handler, "doBindClick.") + method.getName());
            }
        }
    }

    private static void bindLongClick(Object handler, AfViewable root) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindLongClick.class)) {
            try {
                BindLongClick bind = method.getAnnotation(BindLongClick.class);
                for (int id : bind.value()) {
                    View view = root.findViewById(id);
                    view.setOnLongClickListener(new EventListener(handler).longClick(method));
                }
            } catch (Throwable e) {
                AfExceptionHandler.handler(e, TAG(handler, "doBindLongClick.") + method.getName());
            }
        }
    }

    private static void bindItemClick(Object handler, AfViewable root) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindItemClick.class)) {
            try {
                BindItemClick bind = method.getAnnotation(BindItemClick.class);
                for (int id : bind.value()) {
                    AdapterView<?> view = root.findViewByID(id);
                    if (view != null) {
                        view.setOnItemClickListener(new EventListener(handler).itemClick(method));
                    }
                }
            } catch (Throwable e) {
                AfExceptionHandler.handler(e, TAG(handler, "doBindLongClick.") + method.getName());
            }
        }
    }

    private static void bindItemLongClick(Object handler, AfViewable root) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindItemLongClick.class)) {
            try {
                BindItemLongClick bind = method.getAnnotation(BindItemLongClick.class);
                for (int id : bind.value()) {
                    AdapterView<?> view = root.findViewByID(id);
                    if (view != null) {
                        view.setOnItemLongClickListener(new EventListener(handler).itemLongClick(method));
                    }
                }
            } catch (Throwable e) {
                AfExceptionHandler.handler(e, TAG(handler, "doBindLongClick.") + method.getName());
            }
        }
    }

    private static void bindCheckedChange(Object handler, AfViewable root) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindCheckedChange.class)) {
            try {
                BindCheckedChange bind = method.getAnnotation(BindCheckedChange.class);
                for (int id : bind.value()) {
                    CompoundButton view = root.findViewByID(id);
                    if (view != null) {
                        view.setOnCheckedChangeListener(new EventListener(handler).checkedChange(method));
                    }
                }
            } catch (Throwable e) {
                AfExceptionHandler.handler(e, TAG(handler, "doBindLongClick.") + method.getName());
            }
        }
    }

    private static void bindView(Object handler, AfViewable root) {
        for (Field field : AfReflecter.getFieldAnnotation(handler.getClass(), getStopType(handler), BindView.class)) {
            try {
                BindView bind = field.getAnnotation(BindView.class);
                List<View> list = new ArrayList<>();
                for (int id : bind.value()) {
                    View view = root.findViewById(id);
                    if (view != null) {
                        if (bind.click() && handler instanceof OnClickListener) {
                            view.setOnClickListener((OnClickListener) handler);
                        }
                        list.add(view);
                    }
                }
                if (list.size() > 0) {
                    field.setAccessible(true);
                    if (field.getType().isArray()) {
                        Class<?> componentType = field.getType().getComponentType();
                        Object[] array = list.toArray((Object[]) Array.newInstance(componentType, list.size()));
                        field.set(handler, array);
                    } else if (List.class.equals(field.getType())) {
                        field.set(handler, list);
                    } else {
                        field.set(handler, list.get(0));
                    }
                }
            } catch (Throwable e) {
                AfExceptionHandler.handler(e, TAG(handler, "doBindView.") + field.getName());
            }
        }
    }

    private static void bindViewModule(Object handler, AfViewable root) {
        for (Field field : AfReflecter.getFieldAnnotation(handler.getClass(), getStopType(handler), BindViewModule.class)) {
            try {
                Class<?> clazz = field.getType();
                BindViewModule bind = field.getAnnotation(BindViewModule.class);
                List<Object> list = new ArrayList<>();
                for (int id : bind.value()) {
                    Object value = null;
                    if (clazz.equals(AfModuleTitlebar.class) && root != null) {
                        value = new AfModuleTitlebarImpl(root);
                    } else if (clazz.equals(AfFrameSelector.class) && root != null) {
                        value = new AfFrameSelector(root, id);
                    } else if (clazz.equals(AfModuleNodata.class) && root != null) {
                        value = new AfModuleNodataImpl(root);
                    } else if (clazz.equals(AfModuleProgress.class) && root != null) {
                        value = new AfModuleProgressImpl(root);
                    } else if (clazz.equals(AfContactsRefreshView.class) && root != null) {
                        value = new AfContactsRefreshView(root,bind.value()[0]);
                    } else if (root != null
                            && (field.getType().isAnnotationPresent(BindLayout.class) || id > 0)
                            /*&& AfViewModule.class.isAssignableFrom(field.getType())*/) {
                        if (id <= 0) {
                            id = field.getType().getAnnotation(BindLayout.class).value();
                        }
                        //Class<? extends AfViewModule> type = (Class<? extends AfViewModule>) field.getType();
                        if (field.getType().isArray()) {
                            Class<?> type = field.getType().getComponentType();
                            value = AfViewModule.init((Class<? extends AfViewModule>) type, root, id);
                        } else if (List.class.isAssignableFrom(field.getType())) {
                            Type generic = field.getGenericType();
                            ParameterizedType parameterized = (ParameterizedType) generic;
                            Class<?> type = (Class<?>) parameterized.getActualTypeArguments()[0];
                            value = AfViewModule.init((Class<? extends AfViewModule>) type, root, id);
                        } else {
                            value = AfViewModule.init((Class<? extends AfViewModule>) field.getType(), root, id);
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
                    } else {
                        field.set(handler, list.get(0));
                    }
                }
            } catch (Throwable e) {
                AfExceptionHandler.handler(e, TAG(handler, "doBindViewModule.") + field.getName());
            }
        }
    }

    public static void bindAfterView(Object handler, AfViewable root) {
        List<SimpleEntry> methods = new ArrayList<SimpleEntry>();
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindAfterViews.class)) {
            BindAfterViews annotation = method.getAnnotation(BindAfterViews.class);
            methods.add(new SimpleEntry(method, annotation));
        }
        Collections.sort(methods, new Comparator<SimpleEntry>() {
            @Override
            public int compare(SimpleEntry lhs, SimpleEntry rhs) {
                return lhs.getValue().value() - rhs.getValue().value();
            }
        });
        for (SimpleEntry entry : methods) {
            try {
                invokeMethod(handler, entry.getKey());
            } catch (Throwable e) {
                e.printStackTrace();
                if (!entry.getValue().exception()) {
                    throw new RuntimeException("调用视图初始化失败", e);
                }
                AfExceptionHandler.handler(e, TAG(handler, "doBindView.") + entry.getKey().getName());
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

    public static class SimpleEntry implements Map.Entry<Method, BindAfterViews> {

        private final Method key;
        private BindAfterViews value;

        public SimpleEntry(Method theKey, BindAfterViews theValue) {
            key = theKey;
            value = theValue;
        }

        public Method getKey() {
            return key;
        }

        public BindAfterViews getValue() {
            return value;
        }

        public BindAfterViews setValue(BindAfterViews object) {
            BindAfterViews result = value;
            value = object;
            return result;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

}
