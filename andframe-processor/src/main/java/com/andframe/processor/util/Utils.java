package com.andframe.processor.util;

import com.andframe.processor.constant.ClassNames;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.andframe.processor.constant.Constants.NULLABLE_ANNOTATION_NAME;

/**
 * 工具集
 * Created by SCWANG on 2017/8/7.
 */

public class Utils {

    public static boolean requiresCast(TypeName type) {
        return !ClassNames.ANDROID_VIEW.equals(type);
    }

    public static boolean hasAnnotationWithName(Element element, String simpleName) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            String annotationName = mirror.getAnnotationType().asElement().getSimpleName().toString();
            if (simpleName.equals(annotationName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFieldRequired(Element element) {
        return !hasAnnotationWithName(element, NULLABLE_ANNOTATION_NAME);
    }
    /**
     * typeMirror 和 otherType 是否是同类型
     */
    public static boolean isTypeEqual(TypeMirror typeMirror, String otherType) {
        return otherType.equals(typeMirror.toString());
    }

    /**
     * typeMirror 是否是 otherType 的子类
     */
    public static boolean isSubtypeOfType(TypeMirror typeMirror, TypeName otherType) {
        return isSubtypeOfType(typeMirror, otherType.toString());
    }

    /**
     * typeMirror 是否是 otherType 的子类
     */
    public static boolean isSubtypeOfType(TypeMirror typeMirror, String otherType) {
        if (isTypeEqual(typeMirror, otherType)) {
            return true;
        }
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return false;
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (typeArguments.size() > 0) {
            StringBuilder typeString = new StringBuilder(declaredType.asElement().toString());
            typeString.append('<');
            for (int i = 0; i < typeArguments.size(); i++) {
                if (i > 0) {
                    typeString.append(',');
                }
                typeString.append('?');
            }
            typeString.append('>');
            if (typeString.toString().equals(otherType)) {
                return true;
            }
        }
        Element element = declaredType.asElement();
        if (!(element instanceof TypeElement)) {
            return false;
        }
        TypeElement typeElement = (TypeElement) element;
        TypeMirror superType = typeElement.getSuperclass();
        if (isSubtypeOfType(superType, otherType)) {
            return true;
        }
        for (TypeMirror interfaceType : typeElement.getInterfaces()) {
            if (isSubtypeOfType(interfaceType, otherType)) {
                return true;
            }
        }
        return false;
    }

}
