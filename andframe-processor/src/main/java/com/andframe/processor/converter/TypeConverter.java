package com.andframe.processor.converter;

import com.andframe.processor.model.TypeBinding;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * 转换器
 * Created by SCWANG on 2017/8/8.
 */

public abstract class TypeConverter {

    protected final int minsdk;
    protected final boolean debug;
    protected final TypeBinding typeBinding;

    protected TypeConverter(TypeBinding typeBinding, int minsdk, boolean debug) {
        this.debug = debug;
        this.minsdk = minsdk;
        this.typeBinding = typeBinding;
    }

    public JavaFile convert(int sdk, boolean debuggable) {
        return JavaFile.builder(typeBinding.bindingClassName.packageName(),createType(sdk, debuggable))
                .addFileComment("Generated code from AndFrame. Do not modify!")
                .addFileComment("本文件由AndFrame生成的代码。不要修改!")
                .build();
    }

    private TypeSpec createType(int minsdk, boolean debuggable) {
        TypeSpec.Builder result = TypeSpec.classBuilder(typeBinding.bindingClassName.simpleName())
                .addModifiers(PUBLIC);

        result.superclass(typeBinding.targetTypeName);

        result.addMethods(createConstructorSupper());

        if (typeBinding.typeLayoutBinding != null) {
            result.addMethod(createLayoutBindingMethod(typeBinding.typeLayoutBinding));
        } else if (hasViewBindings()) {
            result.addMethods(createViewBindingMethodInvokers());
        }

        if (hasViewBindings()) {
            result.addMethod(createViewBindingMethod(minsdk, debuggable));
            result.addMethod(createBindingUnbindMethod(result));
        }

        return result.build();
    }

    /**
     * 创建构造函数连接父类
     */
    protected abstract Iterable<MethodSpec> createConstructorSupper();

}
