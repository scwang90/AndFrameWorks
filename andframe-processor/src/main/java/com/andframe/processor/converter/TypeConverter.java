package com.andframe.processor.converter;

import com.andframe.annotation.listener.BindOnTouch;
import com.andframe.annotation.listener.internal.ListenerClass;
import com.andframe.annotation.listener.internal.ListenerMethod;
import com.andframe.processor.constant.ClassNames;
import com.andframe.processor.constant.Methods;
import com.andframe.processor.model.Parameter;
import com.andframe.processor.model.ResourceBinding;
import com.andframe.processor.model.TypeBinding;
import com.andframe.processor.model.ViewBinding;
import com.andframe.processor.model.ViewFieldBinding;
import com.andframe.processor.model.ViewFieldsBinding;
import com.andframe.processor.model.ViewMethodBinding;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.andframe.processor.constant.ClassNames.ANDFRAME_LISTENER;
import static com.andframe.processor.constant.ClassNames.ANDFRAME_UTILS;
import static com.andframe.processor.constant.ClassNames.ANDROID_CONTEXT;
import static com.andframe.processor.constant.ClassNames.ANDROID_RESOURCES;
import static com.andframe.processor.constant.ClassNames.ANDROID_VIEW;
import static com.andframe.processor.constant.ClassNames.SUPPRESS_LINT;
import static com.andframe.processor.util.Utils.asHumanDescription;
import static com.andframe.processor.util.Utils.bestGuess;
import static com.andframe.processor.util.Utils.getListenerMethods;
import static com.andframe.processor.util.Utils.requiresCast;
import static java.util.Collections.singletonList;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * 转换器
 * Created by SCWANG on 2017/8/8.
 */

public abstract class TypeConverter {

    protected final TypeBinding binding;

    protected TypeConverter(TypeBinding binding) {
        this.binding = binding;
    }

    public JavaFile convert(int sdk, boolean debuggable) {
        return JavaFile.builder(binding.bindingClassName.packageName(),createType(sdk, debuggable))
                .addFileComment("Generated code from AndFrame. Do not modify!")
                .addFileComment("本文件由AndFrame生成的代码。不要修改!")
                .build();
    }

    private TypeSpec createType(int minsdk, boolean debuggable) {
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(binding.bindingClassName.simpleName())
                .superclass(binding.targetTypeName)
                .addModifiers(PUBLIC);

        createConstructorSupper(typeSpec, minsdk, debuggable);

        if (binding.layoutBinding != null) {
            createLayoutBindingMethod(typeSpec, minsdk, debuggable);
        }

        if (hasViewBindings()) {
            createViewBindingMethod(typeSpec, minsdk, debuggable);
            createBindingUnbindMethod(typeSpec, minsdk, debuggable);
        }

        return typeSpec.build();
    }

    /**
     * 创建构造函数连接父类
     */
    protected abstract void createConstructorSupper(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable);

    /**
     * 创建绑定布局相关方法
     */
    protected abstract void createLayoutBindingMethod(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable);

    //<editor-fold desc="视图绑定">
    /**
     * 创建绑定视图相关方法
     */
    protected void createViewBindingMethod(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable) {
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(Methods.VIEW_BIND)
                .addParameter(ClassNames.ANDROID_VIEW,"source")
                .addModifiers(PRIVATE);

        if (hasUnqualifiedResourceBindings()) {
            // Aapt can change IDs out from underneath us, just suppress since all will work at runtime.
            methodSpec.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                    .addMember("value", "$S", "ResourceType")
                    .build());
        }

        if (hasOnTouchMethodBindings()) {
            methodSpec.addAnnotation(AnnotationSpec.builder(SUPPRESS_LINT)
                    .addMember("value", "$S", "ClickableViewAccessibility")
                    .build());
        }

        if (hasViewBindings()) {
            if (hasViewLocal()) {
                // Local variable in which all views will be temporarily stored.
                methodSpec.addStatement("$T view", ClassNames.ANDROID_VIEW);
            }
            for (ViewBinding viewBinding : binding.viewBindings) {
                addViewBindingToBindMethod(methodSpec, viewBinding, debuggable);
            }
            for (ViewFieldsBinding viewBinding : binding.viewFieldsBindings) {
                methodSpec.addStatement("$L", viewBinding.render(debuggable));
            }
            if (!binding.resourceBindings.isEmpty()) {
                methodSpec.addCode("\n");
            }
        }

        if (!binding.resourceBindings.isEmpty()) {
            if (hasResourceBindingsNeedingResource(minsdk)) {
                methodSpec.addStatement("$T context = source.getContext()", ANDROID_CONTEXT);
                methodSpec.addStatement("$T res = context.getResources()", ANDROID_RESOURCES);
            }
            for (ResourceBinding resourceBinding : binding.resourceBindings) {
                methodSpec.addStatement("$L", resourceBinding.render(minsdk));
            }
        }

        typeSpec.addMethod(methodSpec.build());
    }

    /**
     * 在绑定视图方法中添加指定的视图绑定
     */
    private void addViewBindingToBindMethod(MethodSpec.Builder methodSpec, ViewBinding viewBinding, boolean debuggable) {
        if (viewBinding.isSingleFieldBinding()) {
            // Optimize the common case where there's a single binding directly to a field.
            for (ViewFieldBinding fieldBinding : viewBinding.fieldBindings){
                CodeBlock.Builder builder = CodeBlock.builder()
                        .add("$L = ", fieldBinding.getName());
                boolean requiresCast = requiresCast(fieldBinding.getType());
                if (!debuggable || (!requiresCast && !fieldBinding.isRequired())) {
                    builder.add("source.findViewById($L)", viewBinding.id.code);
                } else {
                    builder.add("$T.find", ANDFRAME_UTILS);
                    builder.add(fieldBinding.isRequired() ? "RequiredView" : "OptionalView");
                    if (requiresCast) {
                        builder.add("AsType");
                    }
                    builder.add("(source, $L", viewBinding.id.code);
                    if (fieldBinding.isRequired() || requiresCast) {
                        builder.add(", $S", asHumanDescription(singletonList(fieldBinding)));
                    }
                    if (requiresCast) {
                        builder.add(", $T.class", fieldBinding.getRawType());
                    }
                    builder.add(")");
                }
                methodSpec.addStatement("$L", builder.build());
            }
            return;
        }

        List<?> requiredBindings = viewBinding.getRequiredBindings();
        if (!debuggable || requiredBindings.isEmpty()) {
            methodSpec.addStatement("view = source.findViewById($L)", viewBinding.id.code);
        } else if (!viewBinding.isBoundToRoot()) {
            methodSpec.addStatement("view = $T.findRequiredView(source, $L, $S)", ANDFRAME_UTILS,
                    viewBinding.id.code, asHumanDescription(requiredBindings));
        }

        addFieldBinding(methodSpec, viewBinding, debuggable);
        addMethodBindings(methodSpec, viewBinding, debuggable);
    }

    private void addFieldBinding(MethodSpec.Builder result, ViewBinding viewBinding, boolean debuggable) {
        for (ViewFieldBinding fieldBinding : viewBinding.fieldBindings) {
            if (fieldBinding != null) {
                if (requiresCast(fieldBinding.getType())) {
                    if (debuggable) {
                        result.addStatement("$L = $T.castView(view, $L, $S, $T.class)",
                                fieldBinding.getName(), ANDFRAME_UTILS, viewBinding.id.code,
                                asHumanDescription(singletonList(fieldBinding)), fieldBinding.getRawType());
                    } else {
                        result.addStatement("$L = ($T) view", fieldBinding.getName(), fieldBinding.getType());
                    }
                } else {
                    result.addStatement("$L = view", fieldBinding.getName());
                }
            }
        }
    }

    private void addMethodBindings(MethodSpec.Builder result, ViewBinding viewBinding,
                                   boolean debuggable) {
        Map<ListenerClass, Map<ListenerMethod, Set<ViewMethodBinding>>> classMethodBindings =
                viewBinding.methodBindings;
        if (classMethodBindings.isEmpty()) {
            return;
        }

        // We only need to emit the null check if there are zero required bindings.
        boolean needsNullChecked = viewBinding.getRequiredBindings().isEmpty();
        if (needsNullChecked) {
            result.beginControlFlow("if (view != null)");
        }

        // Add the view reference to the binding.
        String fieldName = "viewSource";
        String bindName = "source";
        if (!viewBinding.isBoundToRoot()) {
            fieldName = "view" + viewBinding.id.value;
            bindName = "view";
        }
        result.addStatement("$L = $N", fieldName, bindName);

        for (Map.Entry<ListenerClass, Map<ListenerMethod, Set<ViewMethodBinding>>> e
                : classMethodBindings.entrySet()) {
            ListenerClass listener = e.getKey();
            Map<ListenerMethod, Set<ViewMethodBinding>> methodBindings = e.getValue();

            TypeSpec.Builder callback = TypeSpec.anonymousClassBuilder("")
                    .superclass(ClassName.bestGuess(listener.type()));

            for (ListenerMethod method : getListenerMethods(listener)) {
                MethodSpec.Builder callbackMethod = MethodSpec.methodBuilder(method.name())
                        .addAnnotation(Override.class)
                        .addModifiers(PUBLIC)
                        .returns(bestGuess(method.returnType()));
                String[] parameterTypes = method.parameters();
                for (int i = 0, count = parameterTypes.length; i < count; i++) {
                    callbackMethod.addParameter(bestGuess(parameterTypes[i]), "p" + i);
                }

                boolean hasReturnType = !"void".equals(method.returnType());
                CodeBlock.Builder builder = CodeBlock.builder();
                if (hasReturnType) {
                    builder.add("return ");
                }

                if (methodBindings.containsKey(method)) {
                    for (ViewMethodBinding methodBinding : methodBindings.get(method)) {
                        builder.add("$T.this.$L(", binding.bindingClassName, methodBinding.name);
                        List<Parameter> parameters = methodBinding.parameters;
                        String[] listenerParameters = method.parameters();
                        for (int i = 0, count = parameters.size(); i < count; i++) {
                            if (i > 0) {
                                builder.add(", ");
                            }

                            Parameter parameter = parameters.get(i);
                            int listenerPosition = parameter.index;

                            if (parameter.requiresCast(listenerParameters[listenerPosition])) {
                                if (debuggable) {
                                    builder.add("$T.castParam(p$L, $S, $L, $S, $L, $T.class)", ANDFRAME_UTILS,
                                            listenerPosition, method.name(), listenerPosition, methodBinding.name, i,
                                            parameter.type);
                                } else {
                                    builder.add("($T) p$L", parameter.type, listenerPosition);
                                }
                            } else {
                                builder.add("p$L", listenerPosition);
                            }
                        }
                        builder.add(");\n");
                    }
                } else if (hasReturnType) {
                    builder.add("$L;\n", method.defaultReturn());
                }
                callbackMethod.addCode(builder.build());
                callback.addMethod(callbackMethod.build());
            }

            boolean requiresRemoval = listener.remover().length() != 0;
            String listenerField = null;
            if (requiresRemoval) {
                TypeName listenerClassName = bestGuess(listener.type());
                listenerField = fieldName + ((ClassName) listenerClassName).simpleName();
                result.addStatement("$L = $L", listenerField, callback.build());
            }

            if (!ANDROID_VIEW.toString().equals(listener.targetType())) {
                result.beginControlFlow("if ($N instanceof $T)", bindName, bestGuess(listener.targetType()));
                result.addStatement("(($T) $N).$L(new $T($L))", bestGuess(listener.targetType()), bindName,
                        listener.setter(), ANDFRAME_LISTENER, requiresRemoval ? listenerField : callback.build());
                result.endControlFlow();
            } else {
                result.addStatement("$N.$L($L)", bindName, listener.setter(),
                        requiresRemoval ? listenerField : callback.build());
            }
        }

        if (needsNullChecked) {
            result.endControlFlow();
        }
    }
    //</editor-fold>

    //<editor-fold desc="视图解绑">
    /**
     * 创建解绑相关方法
     */
    protected void createBindingUnbindMethod(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable){
        MethodSpec.Builder result = MethodSpec.methodBuilder(Methods.VIEW_UNBIND)
                .addModifiers(PRIVATE);

        if (hasTargetField()) {
            for (ViewBinding binding : binding.viewBindings) {
                for (ViewFieldBinding fieldBinding : binding.fieldBindings) {
                    result.addStatement("$L = null", fieldBinding.getName());
                }
            }
            for (ViewFieldsBinding binding : binding.viewFieldsBindings) {
                result.addStatement("$L = null", binding.name);
            }
        }

        if (hasMethodBindings()) {
            result.addCode("\n");
            for (ViewBinding binding : binding.viewBindings) {
                addFieldAndUnbindStatement(typeSpec, result, binding);
            }
        }
        result.addCode("\n");
        typeSpec.addMethod(result.build());
    }

    private void addFieldAndUnbindStatement(TypeSpec.Builder typeSpec, MethodSpec.Builder unbindMethod,
                                            ViewBinding bindings) {
        // Only add fields to the binding if there are method bindings.
        Map<ListenerClass, Map<ListenerMethod, Set<ViewMethodBinding>>> classMethodBindings =
                bindings.methodBindings;
        if (classMethodBindings.isEmpty()) {
            return;
        }

        String fieldName = bindings.isBoundToRoot() ? "viewSource" : "view" + bindings.id.value;
        typeSpec.addField(ANDROID_VIEW, fieldName, PRIVATE);

        // We only need to emit the null check if there are zero required bindings.
        boolean needsNullChecked = bindings.getRequiredBindings().isEmpty();
        if (needsNullChecked) {
            unbindMethod.beginControlFlow("if ($N != null)", fieldName);
        }

        for (ListenerClass listenerClass : classMethodBindings.keySet()) {
            // We need to keep a reference to the listener
            // in case we need to unbind it via a remove method.
            boolean requiresRemoval = !"".equals(listenerClass.remover());
            String listenerField = "null";
            if (requiresRemoval) {
                TypeName listenerClassName = bestGuess(listenerClass.type());
                listenerField = fieldName + ((ClassName) listenerClassName).simpleName();
                typeSpec.addField(listenerClassName, listenerField, PRIVATE);
            }

            if (!ANDROID_VIEW.toString().equals(listenerClass.targetType())) {
                unbindMethod.beginControlFlow("if ($N instanceof $T)", fieldName, bestGuess(listenerClass.targetType()));
                unbindMethod.addStatement("(($T) $N).$N($N)", bestGuess(listenerClass.targetType()),
                        fieldName, removerOrSetter(listenerClass, requiresRemoval), listenerField);
                unbindMethod.endControlFlow();
            } else {
                unbindMethod.addStatement("$N.$N($N)", fieldName,
                        removerOrSetter(listenerClass, requiresRemoval), listenerField);
            }

            if (requiresRemoval) {
                unbindMethod.addStatement("$N = null", listenerField);
            }
        }

        unbindMethod.addStatement("$N = null", fieldName);

        if (needsNullChecked) {
            unbindMethod.endControlFlow();
        }
    }

    private String removerOrSetter(ListenerClass listenerClass, boolean requiresRemoval) {
        return requiresRemoval
                ? listenerClass.remover()
                : listenerClass.setter();
    }
    //</editor-fold>

    //<editor-fold desc="基本判断">
    /** True when this type's bindings require a view hierarchy. */
    protected boolean hasViewBindings() {
        return !binding.viewBindings.isEmpty() || !binding.viewFieldsBindings.isEmpty();
    }

    /** True when this type's bindings use raw integer values instead of {@code R} references. */
    protected boolean hasUnqualifiedResourceBindings() {
        for (ResourceBinding resourceBinding : binding.resourceBindings) {
            if (!resourceBinding.id().qualifed) {
                return true;
            }
        }
        return false;
    }

    /** True when this type's bindings use Resource directly instead of Context. */
    protected boolean hasResourceBindingsNeedingResource(int sdk) {
        for (ResourceBinding resourceBinding : binding.resourceBindings) {
            if (resourceBinding.requiresResources(sdk)) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasMethodBindings() {
        for (ViewBinding bindings : binding.viewBindings) {
            if (!bindings.methodBindings.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasTargetField() {
        return hasFieldBindings() || hasMethodBindings();
    }

    protected boolean hasOnTouchMethodBindings() {
        for (ViewBinding bindings : binding.viewBindings) {
            if (bindings.methodBindings.containsKey(BindOnTouch.class.getAnnotation(ListenerClass.class))) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasFieldBindings() {
        for (ViewBinding bindings : binding.viewBindings) {
            if (bindings.fieldBindings.size() > 0) {
                return true;
            }
        }
        return !binding.viewFieldsBindings.isEmpty();
    }

    protected boolean hasViewLocal() {
        for (ViewBinding bindings : binding.viewBindings) {
            if (bindings.requiresLocal()) {
                return true;
            }
        }
        return false;
    }
    //</editor-fold>
}
