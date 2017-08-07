package com.andframe.processor.model;

import com.andframe.annotation.listener.internal.ListenerClass;
import com.andframe.annotation.listener.internal.ListenerMethod;
import com.andframe.processor.constant.ClassNames;
import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.andframe.processor.util.Utils.isSubtypeOfType;
import static com.google.auto.common.MoreElements.getPackage;

/**
 * 以类为单位的绑定集合
 * Created by SCWANG on 2017/8/7.
 */

public class TypeBinding {

    public final boolean isView;
    public final boolean isActivity;
    public final boolean isDialog;
    public final TypeName targetTypeName;
    public final ClassName bindingClassName;
    public final List<ViewBinding> viewBindings;
    public final List<ViewFieldsBinding> viewFieldsBindings;
    public final List<ResourceBinding> resourceBindings;
    public final TypeLayoutBinding typeLayoutBinding;

    private TypeBinding(TypeName targetTypeName, ClassName bindingClassName,
                       boolean isView, boolean isActivity, boolean isDialog,
                       List<ViewBinding> viewBindings,
                       List<ViewFieldsBinding> viewFieldsBindings,
                       List<ResourceBinding> resourceBindings, TypeLayoutBinding typeLayoutBinding) {
        this.isView = isView;
        this.isDialog = isDialog;
        this.isActivity = isActivity;
        this.targetTypeName = targetTypeName;
        this.bindingClassName = bindingClassName;
        this.viewBindings = viewBindings;
        this.typeLayoutBinding = typeLayoutBinding;
        this.viewFieldsBindings = viewFieldsBindings;
        this.resourceBindings = resourceBindings;
    }


    public static Builder newBuilder(TypeElement enclosingElement) {
        TypeMirror typeMirror = enclosingElement.asType();

        boolean isView = isSubtypeOfType(typeMirror, ClassNames.ANDROID_VIEW);
        boolean isActivity = isSubtypeOfType(typeMirror, ClassNames.ANDROID_ACTIVITY);
        boolean isDialog = isSubtypeOfType(typeMirror, ClassNames.ANDROID_DIALOG);

        TypeName targetType = TypeName.get(typeMirror);
        if (targetType instanceof ParameterizedTypeName) {
            targetType = ((ParameterizedTypeName) targetType).rawType;
        }

        String packageName = getPackage(enclosingElement).getQualifiedName().toString();
        String className = enclosingElement.getQualifiedName().toString().substring(
                packageName.length() + 1).replace('.', '$');
        ClassName bindingClassName = ClassName.get(packageName, className + "$");

        return new Builder(targetType, bindingClassName, isView, isActivity, isDialog);
    }

    public static final class Builder {

        private final TypeName targetTypeName;
        private final ClassName bindingClassName;
        private final boolean isView;
        private final boolean isActivity;
        private final boolean isDialog;

        private TypeLayoutBinding typeLayoutBinding;

        private final Map<Id, ViewBinding.Builder> viewIdMap = new LinkedHashMap<>();
        private final List<ResourceBinding> resourceBindings = new ArrayList<>();
        private final List<ViewFieldsBinding> viewFieldsBindings = new ArrayList<>();


        private Builder(TypeName targetTypeName, ClassName bindingClassName,
                        boolean isView, boolean isActivity, boolean isDialog) {
            this.targetTypeName = targetTypeName;
            this.bindingClassName = bindingClassName;
            this.isView = isView;
            this.isActivity = isActivity;
            this.isDialog = isDialog;
        }

        public void addField(Id id, ViewFieldBinding binding) {
            getOrCreateViewBindings(id).addFieldBindings(binding);
        }

        public boolean addMethod(Id id, ListenerClass listener, ListenerMethod method, ViewMethodBinding binding) {
            ViewBinding.Builder viewBinding = getOrCreateViewBindings(id);
            if (viewBinding.hasMethodBinding(listener, method) && !"void".equals(method.returnType())) {
                return false;
            }
            viewBinding.addMethodBinding(listener, method, binding);
            return true;
        }

        public void addResource(ResourceBinding binding) {
            resourceBindings.add(binding);
        }

        public void addViewFieldsBinding(ViewFieldsBinding binding) {
            this.viewFieldsBindings.add(binding);
        }

        public void setTypeLayoutBinding(TypeLayoutBinding binding) {
            this.typeLayoutBinding = binding;
        }

        public String findExistingBindingName(Id id) {
            ViewBinding.Builder builder = viewIdMap.get(id);
            if (builder == null) {
                return null;
            }
            List<ViewFieldBinding> fieldBindings = builder.fieldBindings;
            if (fieldBindings.size() == 0) {
                return null;
            }
            return fieldBindings.get(0).getName();
        }

        private ViewBinding.Builder getOrCreateViewBindings(Id id) {
            ViewBinding.Builder viewId = viewIdMap.get(id);
            if (viewId == null) {
                viewIdMap.put(id, viewId = new ViewBinding.Builder(id));
            }
            return viewId;
        }

        public TypeBinding build() {
            ImmutableList.Builder<ViewBinding> viewBindings = ImmutableList.builder();
            for (ViewBinding.Builder builder : viewIdMap.values()) {
                viewBindings.add(builder.build());
            }
            return new TypeBinding(targetTypeName, bindingClassName,
                    isView, isActivity, isDialog,
                    viewBindings.build(),
                    Collections.unmodifiableList(viewFieldsBindings),
                    Collections.unmodifiableList(resourceBindings),
                    typeLayoutBinding);
        }

        public void setExtendBinding(TypeBinding binding) {
            this.resourceBindings.addAll(binding.resourceBindings);
            this.viewFieldsBindings.addAll(binding.viewFieldsBindings);
            this.typeLayoutBinding = (typeLayoutBinding==null)?binding.typeLayoutBinding:typeLayoutBinding;
            for (ViewBinding viewBinding : binding.viewBindings) {
                for (ViewFieldBinding viewFieldBinding : viewBinding.fieldBindings) {
                    addField(viewBinding.id, viewFieldBinding);
                }
                for (Map.Entry<ListenerClass, Map<ListenerMethod, Set<ViewMethodBinding>>> entry : viewBinding.methodBindings.entrySet()) {
                    for (Map.Entry<ListenerMethod, Set<ViewMethodBinding>> methodSetEntry : entry.getValue().entrySet()) {
                        for (ViewMethodBinding viewMethodBinding : methodSetEntry.getValue()) {
                            addMethod(viewBinding.id, entry.getKey(), methodSetEntry.getKey(), viewMethodBinding);
                        }
                    }
                }
            }
        }
    }
}
