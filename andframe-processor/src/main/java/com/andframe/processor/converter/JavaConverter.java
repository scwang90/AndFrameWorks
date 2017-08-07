package com.andframe.processor.converter;

import com.andframe.annotation.listener.BindOnTouch;
import com.andframe.annotation.listener.internal.ListenerClass;
import com.andframe.annotation.listener.internal.ListenerMethod;
import com.andframe.processor.constant.ClassNames;
import com.andframe.processor.model.Parameter;
import com.andframe.processor.model.ResourceBinding;
import com.andframe.processor.model.TypeBinding;
import com.andframe.processor.model.TypeLayoutBinding;
import com.andframe.processor.model.ViewBinding;
import com.andframe.processor.model.ViewFieldBinding;
import com.andframe.processor.model.ViewFieldsBinding;
import com.andframe.processor.model.ViewMethodBinding;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.andframe.processor.constant.ClassNames.ANDFRAME_LISTENER;
import static com.andframe.processor.constant.ClassNames.ANDFRAME_UTILS;
import static com.andframe.processor.constant.ClassNames.ANDROID_CONTEXT;
import static com.andframe.processor.constant.ClassNames.ANDROID_RESOURCES;
import static com.andframe.processor.constant.ClassNames.ANDROID_VIEW;
import static com.andframe.processor.constant.ClassNames.SUPPRESS_LINT;
import static com.andframe.processor.util.Utils.requiresCast;
import static java.util.Collections.singletonList;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * 爪哇转换器
 * Created by SCWANG on 2017/8/7.
 */

public class JavaConverter {

    private TypeBinding binding;

    private JavaConverter(TypeBinding binding) {
        this.binding = binding;
    }

    public static JavaFile bindingToJava(TypeBinding binding, int sdk, boolean debug) {
        return JavaFile.builder(binding.bindingClassName.packageName(),
                new JavaConverter(binding).createType(sdk, debug))
                .addFileComment("Generated code from AndFrame. Do not modify!")
                .build();
    }


    //<editor-fold desc="生产代码">

    private TypeSpec createType(int sdk, boolean debuggable) {
        TypeSpec.Builder result = TypeSpec.classBuilder(binding.bindingClassName.simpleName())
                .addModifiers(PUBLIC);

        result.superclass(binding.targetTypeName);

        if (binding.isView) {
            result.addMethod(createBindingConstructorForView());
        } else if (binding.isActivity) {
        } else if (binding.isDialog) {
            result.addMethod(createBindingConstructorForDialog());
        }
        if (binding.typeLayoutBinding != null) {
            result.addMethod(createLayoutBindingMethod(binding.typeLayoutBinding));
        } else if (hasViewBindings()) {
            result.addMethods(createViewBindingMethodInvokers());
        }

        if (hasViewBindings()) {
            result.addMethod(createViewBindingMethod(sdk, debuggable));
            result.addMethod(createBindingUnbindMethod(result));
        }

        return result.build();
    }

    private MethodSpec createBindingConstructorForView() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC)
                .addParameter(binding.targetTypeName, "target");
        if (constructorNeedsView()) {
            builder.addStatement("this(target, target)");
        } else {
            builder.addStatement("this(target, getContext())");
        }
        return builder.build();
    }

    private MethodSpec createBindingConstructorForDialog() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC)
                .addParameter(binding.targetTypeName, "target");
        if (constructorNeedsView()) {
            builder.addStatement("this(target, getWindow().getDecorView())");
        } else {
            builder.addStatement("this(target, getContext())");
        }
        return builder.build();
    }

    private MethodSpec createLayoutBindingMethod(TypeLayoutBinding typeLayoutBinding) {
        MethodSpec.Builder result = MethodSpec.methodBuilder("onCreate")
                .addAnnotation(Override.class)
                .addParameter(ClassName.get("android.os", "Bundle"), "bundle")
                .addModifiers(PROTECTED);
        result.addStatement("setContentView($L)", typeLayoutBinding.id.code);
        if (hasViewBindings()) {
            result.addStatement("bindViews$$()");
        }
        result.addStatement("super.onCreate(bundle)");
        return result.build();
    }

    private MethodSpec createViewBindingMethod(int sdk, boolean debuggable) {
        MethodSpec.Builder constructor = MethodSpec.methodBuilder("bindViews$")
                .addModifiers(PRIVATE);

        if (hasUnqualifiedResourceBindings()) {
            // Aapt can change IDs out from underneath us, just suppress since all will work at runtime.
            constructor.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                    .addMember("value", "$S", "ResourceType")
                    .build());
        }

        if (hasOnTouchMethodBindings()) {
            constructor.addAnnotation(AnnotationSpec.builder(SUPPRESS_LINT)
                    .addMember("value", "$S", "ClickableViewAccessibility")
                    .build());
        }

        if (hasViewBindings()) {
            if (hasViewLocal()) {
                // Local variable in which all views will be temporarily stored.
                constructor.addStatement("$T view,source = getWindow().getDecorView()", ClassNames.ANDROID_VIEW);
            } else {
                constructor.addStatement("$T source = getWindow().getDecorView()", ClassNames.ANDROID_VIEW);
            }
            for (ViewBinding viewBinding : binding.viewBindings) {
                addViewBinding(constructor, viewBinding, debuggable);
            }
            for (ViewFieldsBinding viewBinding : binding.viewFieldsBindings) {
                constructor.addStatement("$L", viewBinding.render(debuggable));
            }
            if (!binding.resourceBindings.isEmpty()) {
                constructor.addCode("\n");
            }
        }

        if (!binding.resourceBindings.isEmpty()) {
            if (constructorNeedsView()) {
                constructor.addStatement("$T context = source.getContext()", ANDROID_CONTEXT);
            }
            if (hasResourceBindingsNeedingResource(sdk)) {
                constructor.addStatement("$T res = context.getResources()", ANDROID_RESOURCES);
            }
            for (ResourceBinding resourceBinding : binding.resourceBindings) {
                constructor.addStatement("$L", resourceBinding.render(sdk));
            }
        }

        return constructor.build();
    }

    private MethodSpec createBindingUnbindMethod(TypeSpec.Builder bindingClass) {
        MethodSpec.Builder result = MethodSpec.methodBuilder("onDestroy")
                .addAnnotation(Override.class)
                .addModifiers(PROTECTED);

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
                addFieldAndUnbindStatement(bindingClass, result, binding);
            }
        }
        result.addCode("\n");
        result.addStatement("super.onDestroy()");
        return result.build();
    }


    private Iterable<MethodSpec> createViewBindingMethodInvokers() {
        List<MethodSpec> list = new ArrayList<>();
        MethodSpec.Builder method1 = MethodSpec.methodBuilder("setContentView")
                .addModifiers(PUBLIC).addAnnotation(Override.class).returns(TypeName.VOID);
        method1.addParameter(ANDROID_VIEW, "view");
        method1.addStatement("super.setContentView(view)");
        method1.addStatement("bindViews$$()");
        list.add(method1.build());

        MethodSpec.Builder method2 = MethodSpec.methodBuilder("setContentView")
                .addModifiers(PUBLIC).addAnnotation(Override.class).returns(TypeName.VOID);
        method2.addParameter(TypeName.INT, "layoutResID");
        method2.addStatement("super.setContentView(layoutResID)");
        method2.addStatement("bindViews$$()");
        list.add(method2.build());

        MethodSpec.Builder method3 = MethodSpec.methodBuilder("setContentView")
                .addModifiers(PUBLIC).addAnnotation(Override.class).returns(TypeName.VOID);
        method3.addParameter(ANDROID_VIEW, "view");
        method3.addParameter(ClassNames.VIEWGROUP_LAYOUTPARAMS, "params");
        method3.addStatement("super.setContentView(view, params)");
        method3.addStatement("bindViews$$()");
        list.add(method3.build());
        return list;
    }

    private void addFieldAndUnbindStatement(TypeSpec.Builder result, MethodSpec.Builder unbindMethod,
                                            ViewBinding bindings) {
        // Only add fields to the binding if there are method bindings.
        Map<ListenerClass, Map<ListenerMethod, Set<ViewMethodBinding>>> classMethodBindings =
                bindings.methodBindings;
        if (classMethodBindings.isEmpty()) {
            return;
        }

        String fieldName = bindings.isBoundToRoot() ? "viewSource" : "view" + bindings.id.value;
        result.addField(ANDROID_VIEW, fieldName, PRIVATE);

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
                result.addField(listenerClassName, listenerField, PRIVATE);
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

    //<editor-fold desc="代码实现">

    private void addViewBinding(MethodSpec.Builder result, ViewBinding binding, boolean debuggable) {
        if (binding.isSingleFieldBinding()) {
            // Optimize the common case where there's a single binding directly to a field.
            for (ViewFieldBinding fieldBinding : binding.fieldBindings){
                CodeBlock.Builder builder = CodeBlock.builder()
                        .add("$L = ", fieldBinding.getName());
                boolean requiresCast = requiresCast(fieldBinding.getType());
                if (!debuggable || (!requiresCast && !fieldBinding.isRequired())) {
                    builder.add("source.findViewById($L)", binding.id.code);
                } else {
                    builder.add("$T.find", ANDFRAME_UTILS);
                    builder.add(fieldBinding.isRequired() ? "RequiredView" : "OptionalView");
                    if (requiresCast) {
                        builder.add("AsType");
                    }
                    builder.add("(source, $L", binding.id.code);
                    if (fieldBinding.isRequired() || requiresCast) {
                        builder.add(", $S", asHumanDescription(singletonList(fieldBinding)));
                    }
                    if (requiresCast) {
                        builder.add(", $T.class", fieldBinding.getRawType());
                    }
                    builder.add(")");
                }
                result.addStatement("$L", builder.build());
            }
            return;
        }

        List<?> requiredBindings = binding.getRequiredBindings();
        if (!debuggable || requiredBindings.isEmpty()) {
            result.addStatement("view = source.findViewById($L)", binding.id.code);
        } else if (!binding.isBoundToRoot()) {
            result.addStatement("view = $T.findRequiredView(source, $L, $S)", ANDFRAME_UTILS,
                    binding.id.code, asHumanDescription(requiredBindings));
        }

        addFieldBinding(result, binding, debuggable);
        addMethodBindings(result, binding, debuggable);
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

//    private void addViewBinding(MethodSpec.Builder result, ViewBinding viewBinding, boolean debuggable) {
//        if (viewBinding.isSingleFieldBinding()) {
//            // Optimize the common case where there's a single viewBinding directly to a field.
//            for (ViewFieldBinding fieldBinding : viewBinding.fieldBindings) {
//                CodeBlock.Builder builder = CodeBlock.builder().add("$L = ", fieldBinding.getName());
//
//                boolean requiresCast = requiresCast(fieldBinding.getType());
//                if (!debuggable || (!requiresCast && !fieldBinding.isRequired())) {
//                    builder.add("source.findViewById($L)", viewBinding.id.code);
//                } else {
//                    builder.add("$T.find", ANDFRAME_UTILS);
//                    builder.add(fieldBinding.isRequired() ? "RequiredView" : "OptionalView");
//                    if (requiresCast) {
//                        builder.add("AsType");
//                    }
//                    builder.add("(source, $L", viewBinding.id.code);
//                    if (fieldBinding.isRequired() || requiresCast) {
//                        builder.add(", $S", asHumanDescription(Collections.singletonList(fieldBinding)));
//                    }
//                    if (requiresCast) {
//                        builder.add(", $T.class", fieldBinding.getRawType());
//                    }
//                    builder.add(")");
//                }
//                result.addStatement("$L", builder.build());
//            }
//            return;
//        }
//
//        List<?> requiredBindings = viewBinding.getRequiredBindings();
//        if (!debuggable || requiredBindings.isEmpty()) {
//            result.addStatement("view = source.findViewById($L)", viewBinding.id.code);
//        } else if (!viewBinding.isBoundToRoot()) {
//            result.addStatement("view = $T.findRequiredView(source, $L, $S)", ANDFRAME_UTILS,
//                    viewBinding.id.code, asHumanDescription(requiredBindings));
//        }
//
//        addFieldBinding(result, viewBinding, debuggable);
//        addMethodBindings(result, viewBinding, debuggable);
//    }
//
//    private void addFieldBinding(MethodSpec.Builder result, ViewBinding viewBinding, boolean debuggable) {
//        for (ViewFieldBinding fieldBinding : viewBinding.fieldBindings) {
//            if (fieldBinding != null) {
//                if (requiresCast(fieldBinding.getType())) {
//                    if (debuggable) {
//                        result.addStatement("$L = $T.castView(view, $L, $S, $T.class)",
//                                fieldBinding.getName(), ANDFRAME_UTILS, viewBinding.id.code,
//                                asHumanDescription(singletonList(fieldBinding)), fieldBinding.getRawType());
//                    } else {
//                        result.addStatement("$L = ($T) view", fieldBinding.getName(), fieldBinding.getType());
//                    }
//                } else {
//                    result.addStatement("$L = view", fieldBinding.getName());
//                }
//            }
//        }
//    }
//
//    private void addMethodBindings(MethodSpec.Builder result, ViewBinding viewBinding,
//                                   boolean debuggable) {
//        Map<ListenerClass, Map<ListenerMethod, Set<ViewMethodBinding>>> classMethodBindings =
//                viewBinding.methodBindings;
//        if (classMethodBindings.isEmpty()) {
//            return;
//        }
//
//        // We only need to emit the null check if there are zero required bindings.
//        boolean needsNullChecked = viewBinding.getRequiredBindings().isEmpty();
//        if (needsNullChecked) {
//            result.beginControlFlow("if (view != null)");
//        }
//
//        // Add the view reference to the viewBinding.
//        String fieldName = "viewSource";
//        String bindName = "source";
//        if (!viewBinding.isBoundToRoot()) {
//            fieldName = "view" + viewBinding.id.value;
//            bindName = "view";
//        }
////        result.addStatement("$L = $N", fieldName, bindName);
//
//        for (Map.Entry<ListenerClass, Map<ListenerMethod, Set<ViewMethodBinding>>> e
//                : classMethodBindings.entrySet()) {
//            ListenerClass listener = e.getKey();
//            Map<ListenerMethod, Set<ViewMethodBinding>> methodBindings = e.getValue();
//
//            TypeSpec.Builder callback = TypeSpec.anonymousClassBuilder("")
//                    .superclass(ClassName.bestGuess(listener.type()));
//
//            for (ListenerMethod method : getListenerMethods(listener)) {
//                MethodSpec.Builder callbackMethod = MethodSpec.methodBuilder(method.name())
//                        .addAnnotation(Override.class)
//                        .addModifiers(PUBLIC)
//                        .returns(bestGuess(method.returnType()));
//                String[] parameterTypes = method.parameters();
//                for (int i = 0, count = parameterTypes.length; i < count; i++) {
//                    callbackMethod.addParameter(bestGuess(parameterTypes[i]), "p" + i);
//                }
//
//                boolean hasReturnType = !"void".equals(method.returnType());
//                CodeBlock.Builder builder = CodeBlock.builder();
//                if (hasReturnType) {
//                    builder.add("return ");
//                }
//
//                if (methodBindings.containsKey(method)) {
//                    for (ViewMethodBinding methodBinding : methodBindings.get(method)) {
//                        builder.add("$T.this.$L(", binding.bindingClassName, methodBinding.name);
//                        List<Parameter> parameters = methodBinding.parameters;
//                        String[] listenerParameters = method.parameters();
//                        for (int i = 0, count = parameters.size(); i < count; i++) {
//                            if (i > 0) {
//                                builder.add(", ");
//                            }
//
//                            Parameter parameter = parameters.get(i);
//                            int listenerPosition = parameter.index;
//
//                            if (parameter.requiresCast(listenerParameters[listenerPosition])) {
//                                if (debuggable) {
//                                    builder.add("$T.castParam(p$L, $S, $L, $S, $L, $T.class)", ANDFRAME_UTILS,
//                                            listenerPosition, method.name(), listenerPosition, methodBinding.name, i,
//                                            parameter.type);
//                                } else {
//                                    builder.add("($T) p$L", parameter.type, listenerPosition);
//                                }
//                            } else {
//                                builder.add("p$L", listenerPosition);
//                            }
//                        }
//                        builder.add(");\n");
//                    }
//                } else if (hasReturnType) {
//                    builder.add("$L;\n", method.defaultReturn());
//                }
//                callbackMethod.addCode(builder.build());
//                callback.addMethod(callbackMethod.build());
//            }
//
//            boolean requiresRemoval = listener.remover().length() != 0;
//            String listenerField = null;
//            if (requiresRemoval) {
//                TypeName listenerClassName = bestGuess(listener.type());
//                listenerField = fieldName + ((ClassName) listenerClassName).simpleName();
//                result.addStatement("$L = new $T($L)", listenerField, ANDFRAME_LISTENER, callback.build());
//            }
//
//            if (!ANDROID_VIEW.toString().equals(listener.targetType())) {
//                result.beginControlFlow("if ($N instanceof $T)", bindName, bestGuess(listener.targetType()));
//                result.addStatement("(($T) $N).$L(new $T($L))", bestGuess(listener.targetType()), bindName,
//                        listener.setter(), ANDFRAME_LISTENER, requiresRemoval ? listenerField : callback.build());
//                result.endControlFlow();
//            } else {
//                result.addStatement("$N.$L(new $T($L))", bindName, listener.setter(), ANDFRAME_LISTENER,
//                        requiresRemoval ? listenerField : callback.build());
//            }
//        }
//
//        if (needsNullChecked) {
//            result.endControlFlow();
//        }
//    }

    private static List<ListenerMethod> getListenerMethods(ListenerClass listener) {
        if (listener.method().length == 1) {
            return Arrays.asList(listener.method());
        }

        try {
            List<ListenerMethod> methods = new ArrayList<>();
            Class<? extends Enum<?>> callbacks = listener.callbacks();
            for (Enum<?> callbackMethod : callbacks.getEnumConstants()) {
                Field callbackField = callbacks.getField(callbackMethod.name());
                ListenerMethod method = callbackField.getAnnotation(ListenerMethod.class);
                if (method == null) {
                    throw new IllegalStateException(String.format("@%s's %s.%s missing @%s annotation.",
                            callbacks.getEnclosingClass().getSimpleName(), callbacks.getSimpleName(),
                            callbackMethod.name(), ListenerMethod.class.getSimpleName()));
                }
                methods.add(method);
            }
            return methods;
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }

    static String asHumanDescription(Collection<?> bindings) {
        Iterator<?> iterator = bindings.iterator();
        switch (bindings.size()) {
            case 1:
                return iterator.next().toString();
            case 2:
                return iterator.next().toString() + " and " + iterator.next().toString();
            default:
                StringBuilder builder = new StringBuilder();
                for (int i = 0, count = bindings.size(); i < count; i++) {
                    if (i != 0) {
                        builder.append(", ");
                    }
                    if (i == count - 1) {
                        builder.append("and ");
                    }
                    builder.append(iterator.next().toString());
                }
                return builder.toString();
        }
    }

    private static TypeName bestGuess(String type) {
        switch (type) {
            case "void": return TypeName.VOID;
            case "boolean": return TypeName.BOOLEAN;
            case "byte": return TypeName.BYTE;
            case "char": return TypeName.CHAR;
            case "double": return TypeName.DOUBLE;
            case "float": return TypeName.FLOAT;
            case "int": return TypeName.INT;
            case "long": return TypeName.LONG;
            case "short": return TypeName.SHORT;
            default:
                int left = type.indexOf('<');
                if (left != -1) {
                    ClassName typeClassName = ClassName.bestGuess(type.substring(0, left));
                    List<TypeName> typeArguments = new ArrayList<>();
                    do {
                        typeArguments.add(WildcardTypeName.subtypeOf(Object.class));
                        left = type.indexOf('<', left + 1);
                    } while (left != -1);
                    return ParameterizedTypeName.get(typeClassName,
                            typeArguments.toArray(new TypeName[typeArguments.size()]));
                }
                return ClassName.bestGuess(type);
        }
    }

    /** True when this type's bindings require a view hierarchy. */
    private boolean hasViewBindings() {
        return !binding.viewBindings.isEmpty() || !binding.viewFieldsBindings.isEmpty();
    }

    /** True when this type's bindings use raw integer values instead of {@code R} references. */
    private boolean hasUnqualifiedResourceBindings() {
        for (ResourceBinding resourceBinding : binding.resourceBindings) {
            if (!resourceBinding.id().qualifed) {
                return true;
            }
        }
        return false;
    }

    /** True when this type's bindings use Resource directly instead of Context. */
    private boolean hasResourceBindingsNeedingResource(int sdk) {
        for (ResourceBinding resourceBinding : binding.resourceBindings) {
            if (resourceBinding.requiresResources(sdk)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasMethodBindings() {
        for (ViewBinding bindings : binding.viewBindings) {
            if (!bindings.methodBindings.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasTargetField() {
        return hasFieldBindings() || hasMethodBindings();
    }

    private boolean hasOnTouchMethodBindings() {
        for (ViewBinding bindings : binding.viewBindings) {
            if (bindings.methodBindings.containsKey(BindOnTouch.class.getAnnotation(ListenerClass.class))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasFieldBindings() {
        for (ViewBinding bindings : binding.viewBindings) {
            if (bindings.fieldBindings.size() > 0) {
                return true;
            }
        }
        return !binding.viewFieldsBindings.isEmpty();
    }

    private boolean hasViewLocal() {
        for (ViewBinding bindings : binding.viewBindings) {
            if (bindings.requiresLocal()) {
                return true;
            }
        }
        return false;
    }

    /** True if this binding requires a view. Otherwise only a context is needed. */
    private boolean constructorNeedsView() {
        return hasViewBindings();
    }
    //</editor-fold>


}
