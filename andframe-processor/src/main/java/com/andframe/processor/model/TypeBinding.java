package com.andframe.processor.model;

import com.andframe.annotation.listener.internal.ListenerClass;
import com.andframe.annotation.listener.internal.ListenerMethod;
import com.andframe.processor.constant.ClassNames;
import com.andframe.processor.constant.ComponentType;
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

    public final ComponentType componentType;
    public final TypeName targetTypeName;
    public final ClassName bindingClassName;
    public final List<ViewBinding> viewBindings;
    public final List<ViewFieldsBinding> viewFieldsBindings;
    public final List<ResourceBinding> resourceBindings;
    public final TypeLayoutBinding layoutBinding;

    private TypeBinding(TypeName targetTypeName, ClassName bindingClassName,
                        ComponentType componentType,
                        List<ViewBinding> viewBindings,
                        List<ViewFieldsBinding> viewFieldsBindings,
                        List<ResourceBinding> resourceBindings, TypeLayoutBinding layoutBinding) {
        this.componentType = componentType;
        this.targetTypeName = targetTypeName;
        this.bindingClassName = bindingClassName;
        this.viewBindings = viewBindings;
        this.layoutBinding = layoutBinding;
        this.viewFieldsBindings = viewFieldsBindings;
        this.resourceBindings = resourceBindings;
    }


    public static Builder newBuilder(TypeElement enclosingElement) {
        TypeMirror typeMirror = enclosingElement.asType();

        TypeName targetType = TypeName.get(typeMirror);
        if (targetType instanceof ParameterizedTypeName) {
            targetType = ((ParameterizedTypeName) targetType).rawType;
        }

        String packageName = getPackage(enclosingElement).getQualifiedName().toString();
        String className = enclosingElement.getQualifiedName().toString().substring(
                packageName.length() + 1).replace('.', '$');
        ClassName bindingClassName = ClassName.get(packageName, className + "$");

        if (isSubtypeOfType(typeMirror, ClassNames.ANDROID_ACTIVITY)) {
            return new Builder(targetType, bindingClassName, ComponentType.Activity);
        }
        return new Builder(targetType, bindingClassName, ComponentType.Fragment);
    }

    public static final class Builder {

        private final TypeName targetName;
        private final ClassName bindingName;
        private final ComponentType componentType;

        private TypeLayoutBinding layoutBinding;

        private final Map<Id, ViewBinding.Builder> viewIdMap = new LinkedHashMap<>();
        private final List<ResourceBinding> resourceBindings = new ArrayList<>();
        private final List<ViewFieldsBinding> viewFieldsBindings = new ArrayList<>();


        private Builder(TypeName targetName, ClassName bindingName, ComponentType componentType) {
            this.targetName = targetName;
            this.bindingName = bindingName;
            this.componentType = componentType;
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

        public void setLayoutBinding(TypeLayoutBinding binding) {
            this.layoutBinding = binding;
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
            return new TypeBinding(targetName, bindingName,
                    componentType,
                    viewBindings.build(),
                    Collections.unmodifiableList(viewFieldsBindings),
                    Collections.unmodifiableList(resourceBindings),
                    layoutBinding);
        }

        public void setExtendBinding(TypeBinding binding) {
            this.resourceBindings.addAll(binding.resourceBindings);
            this.viewFieldsBindings.addAll(binding.viewFieldsBindings);
            this.layoutBinding = (layoutBinding ==null)?binding.layoutBinding : layoutBinding;
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
