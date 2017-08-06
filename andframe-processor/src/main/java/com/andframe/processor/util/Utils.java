package com.andframe.processor.util;

import com.andframe.annotation.Optional;
import com.andframe.processor.constant.ClassNames;
import com.squareup.javapoet.TypeName;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.andframe.processor.constant.Constants.NULLABLE_ANNOTATION_NAME;
import static javax.lang.model.element.ElementKind.INTERFACE;

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

    public static boolean isInterface(TypeMirror typeMirror) {
        return typeMirror instanceof DeclaredType && ((DeclaredType) typeMirror).asElement().getKind() == INTERFACE;
    }

    public static boolean isFieldRequired(Element element) {
        return !hasAnnotationWithName(element, NULLABLE_ANNOTATION_NAME);
    }

    public static boolean isListenerRequired(ExecutableElement element) {
        return element.getAnnotation(Optional.class) == null;
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

    /** Returns the first duplicate element inside an array, null if there are no duplicates. */
    public static Integer findDuplicate(int[] array) {
        Set<Integer> seenElements = new LinkedHashSet<>();

        for (int element : array) {
            if (!seenElements.add(element)) {
                return element;
            }
        }

        return null;
    }

    public static AnnotationMirror getMirror(Element element, Class<? extends Annotation> annotation) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals(annotation.getCanonicalName())) {
                return annotationMirror;
            }
        }
        return null;
    }
}
