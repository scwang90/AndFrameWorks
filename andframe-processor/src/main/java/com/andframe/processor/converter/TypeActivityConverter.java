package com.andframe.processor.converter;

import com.andframe.processor.constant.ClassNames;
import com.andframe.processor.constant.Methods;
import com.andframe.processor.model.TypeBinding;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import static com.andframe.processor.constant.ClassNames.ANDROID_VIEW;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Activity 类的对应转换器
 * Created by SCWANG on 2017/8/8.
 */

public class TypeActivityConverter extends TypeConverter {

    TypeActivityConverter(TypeBinding binding) {
        super(binding);
    }

    @Override
    protected void createConstructorSupper(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable) {

    }

    @Override
    protected void createLayoutBindingMethod(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable) {
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("onCreate")
                .addAnnotation(Override.class)
                .addParameter(ClassNames.ANDROID_BUNDLE, "bundle")
                .addModifiers(PROTECTED);
        methodSpec.addStatement("setContentView($L)", binding.layoutBinding.id.code);
        if (hasViewBindings()) {
            methodSpec.addStatement("$T view = getWindow().getDecorView()",ClassNames.ANDROID_VIEW);
            methodSpec.beginControlFlow("if (view != null)");
            methodSpec.addStatement("$L(view)", Methods.VIEW_BIND);
            methodSpec.endControlFlow();
        }
        methodSpec.addStatement("super.onCreate(bundle)");
        typeSpec.addMethod(methodSpec.build());
    }

    @Override
    protected void createViewBindingMethod(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable) {
        if (binding.layoutBinding == null) {
            MethodSpec.Builder method1 = MethodSpec.methodBuilder("setContentView")
                    .addModifiers(PUBLIC).addAnnotation(Override.class).returns(TypeName.VOID);
            method1.addParameter(ANDROID_VIEW, "view");
            method1.addStatement("super.setContentView(view)");
            method1.beginControlFlow("if (view != null)");
            method1.addStatement("$L(view)", Methods.VIEW_BIND);
            method1.endControlFlow();
            typeSpec.addMethod(method1.build());

            MethodSpec.Builder method2 = MethodSpec.methodBuilder("setContentView")
                    .addModifiers(PUBLIC).addAnnotation(Override.class).returns(TypeName.VOID);
            method2.addParameter(TypeName.INT, "layoutResID");
            method2.addStatement("super.setContentView(layoutResID)");
            method2.addStatement("$T view = getWindow().getDecorView()",ClassNames.ANDROID_VIEW);
            method2.beginControlFlow("if (view != null)");
            method2.addStatement("$L(view)", Methods.VIEW_BIND);
            method2.endControlFlow();
            typeSpec.addMethod(method2.build());

            MethodSpec.Builder method3 = MethodSpec.methodBuilder("setContentView")
                    .addModifiers(PUBLIC).addAnnotation(Override.class).returns(TypeName.VOID);
            method3.addParameter(ANDROID_VIEW, "view");
            method3.addParameter(ClassNames.VIEWGROUP_LAYOUTPARAMS, "params");
            method3.addStatement("super.setContentView(view, params)");
            method3.beginControlFlow("if (view != null)");
            method3.addStatement("$L(view)", Methods.VIEW_BIND);
            method3.endControlFlow();
            typeSpec.addMethod(method3.build());
        }
        super.createViewBindingMethod(typeSpec, minsdk, debuggable);
    }

    @Override
    protected void createBindingUnbindMethod(TypeSpec.Builder typeSpec, int minsdk, boolean debuggable) {
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("onDestroy")
                .addAnnotation(Override.class)
                .addModifiers(PROTECTED);
        methodSpec.addStatement("super.onDestroy()");
        methodSpec.addStatement("$L()", Methods.VIEW_UNBIND);
        typeSpec.addMethod(methodSpec.build());
        super.createBindingUnbindMethod(typeSpec, minsdk, debuggable);
    }
}
