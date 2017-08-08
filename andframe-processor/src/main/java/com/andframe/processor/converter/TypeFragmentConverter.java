package com.andframe.processor.converter;

import com.andframe.processor.constant.ClassNames;
import com.andframe.processor.constant.Methods;
import com.andframe.processor.model.TypeBinding;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Fragment 类的对应转换器
 * Created by SCWANG on 2017/8/8.
 */

public class TypeFragmentConverter extends TypeConverter {

    protected TypeFragmentConverter(TypeBinding binding) {
        super(binding);
    }

    @Override
    protected void createConstructorSupper(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable) {

    }

    @Override
    protected void createLayoutBindingMethod(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable) {
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("onCreateView")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .returns(ClassNames.ANDROID_VIEW)
                .addParameter(ClassNames.ANDROID_INFLATER, "inflater")
                .addParameter(ClassNames.ANDROID_VIEWGROUP, "container")
                .addParameter(ClassNames.ANDROID_BUNDLE, "bundle");
        methodSpec.addStatement("$T view = inflater.inflate($L, container, false)",ClassNames.ANDROID_VIEW, binding.layoutBinding.id.code);
        if (hasViewBindings()) {
            methodSpec.beginControlFlow("if (view != null)");
            methodSpec.addStatement("$L(view)", Methods.VIEW_BIND);
            methodSpec.endControlFlow();
        }
        methodSpec.addStatement("return view");
        typeSpec.addMethod(methodSpec.build());
    }

    @Override
    protected void createViewBindingMethod(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable) {
        if (binding.layoutBinding == null) {
            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("onViewCreated")
                    .addAnnotation(Override.class)
                    .addModifiers(PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(ClassNames.ANDROID_VIEW, "view")
                    .addParameter(ClassNames.ANDROID_BUNDLE, "bundle");
            methodSpec.beginControlFlow("if (view != null)");
            methodSpec.addStatement("$L(view)", Methods.VIEW_BIND);
            methodSpec.endControlFlow();
            methodSpec.addStatement("super.onViewCreated(view, bundle)");
            typeSpec.addMethod(methodSpec.build());
        }
        super.createViewBindingMethod(typeSpec, minsdk, debuggable);
    }

    @Override
    protected void createBindingUnbindMethod(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable) {
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("onDestroy")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC);
        methodSpec.addStatement("super.onDestroy()");
        methodSpec.addStatement("$L()", Methods.VIEW_UNBIND);
        typeSpec.addMethod(methodSpec.build());
        super.createBindingUnbindMethod(typeSpec, minsdk, debuggable);
    }
}
