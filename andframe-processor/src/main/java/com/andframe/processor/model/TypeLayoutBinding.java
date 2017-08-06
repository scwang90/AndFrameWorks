package com.andframe.processor.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class TypeLayoutBinding {
    public Id id;
    public final String name;
    public final TypeName type;

    public TypeLayoutBinding(String name, TypeName type, Id id) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public ClassName getRawType() {
        if (type instanceof ParameterizedTypeName) {
            return ((ParameterizedTypeName) type).rawType;
        }
        return (ClassName) type;
    }

}
