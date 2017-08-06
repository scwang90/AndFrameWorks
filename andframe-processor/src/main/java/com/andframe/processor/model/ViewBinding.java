package com.andframe.processor.model;


import com.andframe.annotation.listener.internal.ListenerClass;
import com.andframe.annotation.listener.internal.ListenerMethod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 视图绑定
 * Created by SCWANG on 2017/8/6.
 */
public class ViewBinding {

    public final Id id;
    public final Map<ListenerClass, Map<ListenerMethod, Set<ViewMethodBinding>>> methodBindings;
    public final ViewFieldBinding fieldBinding;

    ViewBinding(Id id, Map<ListenerClass, Map<ListenerMethod, Set<ViewMethodBinding>>> methodBindings,
                ViewFieldBinding fieldBinding) {
        this.id = id;
        this.methodBindings = methodBindings;
        this.fieldBinding = fieldBinding;
    }

    public List<Object> getRequiredBindings() {
        List<Object> requiredBindings = new ArrayList<>();
        if (fieldBinding != null && fieldBinding.isRequired()) {
            requiredBindings.add(fieldBinding);
        }
        for (Map<ListenerMethod, Set<ViewMethodBinding>> methodBinding : methodBindings.values()) {
            for (Set<ViewMethodBinding> set : methodBinding.values()) {
                for (ViewMethodBinding binding : set) {
                    if (binding.required) {
                        requiredBindings.add(binding);
                    }
                }
            }
        }
        return requiredBindings;
    }

    public boolean isSingleFieldBinding() {
        return methodBindings.isEmpty() && fieldBinding != null;
    }

    public boolean requiresLocal() {
        if (isBoundToRoot()) {
            return false;
        }
        if (isSingleFieldBinding()) {
            return false;
        }
        return true;
    }

    public boolean isBoundToRoot() {
        return Id.NO_ID.equals(id);
    }

    public static final class Builder {
        private final Id id;
        ViewFieldBinding fieldBinding;
        private final Map<ListenerClass, Map<ListenerMethod, Set<ViewMethodBinding>>> methodBindings =
                new LinkedHashMap<>();

        Builder(Id id) {
            this.id = id;
        }

        public boolean hasMethodBinding(ListenerClass listener, ListenerMethod method) {
            Map<ListenerMethod, Set<ViewMethodBinding>> methods = methodBindings.get(listener);
            return methods != null && methods.containsKey(method);
        }

        public void addMethodBinding(ListenerClass listener, ListenerMethod method, ViewMethodBinding binding) {
            Map<ListenerMethod, Set<ViewMethodBinding>> methods = methodBindings.get(listener);
            Set<ViewMethodBinding> set = null;
            if (methods == null) {
                methods = new LinkedHashMap<>();
                methodBindings.put(listener, methods);
            } else {
                set = methods.get(method);
            }
            if (set == null) {
                set = new LinkedHashSet<>();
                methods.put(method, set);
            }
            set.add(binding);
        }

        public void setFieldBinding(ViewFieldBinding fieldBinding) {
            if (this.fieldBinding != null) {
                throw new AssertionError();
            }
            this.fieldBinding = fieldBinding;
        }

        public ViewBinding build() {
            return new ViewBinding(id, methodBindings, fieldBinding);
        }
    }
}