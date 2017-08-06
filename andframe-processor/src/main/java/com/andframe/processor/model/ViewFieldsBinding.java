package com.andframe.processor.model;

import com.andframe.processor.constant.ClassNames;
import com.andframe.processor.util.Utils;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.List;


public class ViewFieldsBinding {
    public enum Kind {
        ARRAY("arrayOf"),
        LIST("listOf");
        final String factoryName;
        Kind(String factoryName) {
            this.factoryName = factoryName;
        }
    }

    public final String name;
    private final Kind kind;
    private final List<Id> ids;
    private final TypeName type;
    private final boolean required;

    public ViewFieldsBinding(String name, TypeName type, Kind kind, List<Id> ids, boolean required) {
        this.ids = ids;
        this.name = name;
        this.type = type;
        this.kind = kind;
        this.required = required;
    }

    public CodeBlock render(boolean debuggable) {
        CodeBlock.Builder builder = CodeBlock.builder()
                .add("$L = $T.$L(", name, ClassNames.ANDFRAME_UTILS, kind.factoryName);
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) {
                builder.add(", ");
            }
            builder.add("\n");

            Id id = ids.get(i);
            boolean requiresCast = Utils.requiresCast(type);
            if (!debuggable) {
                if (requiresCast) {
                    builder.add("($T) ", type);
                }
                builder.add("source.findViewById($L)", id.code);
            } else if (!requiresCast && !required) {
                builder.add("source.findViewById($L)", id.code);
            } else {
                builder.add("$T.find", ClassNames.ANDFRAME_UTILS);
                builder.add(required ? "RequiredView" : "OptionalView");
                if (requiresCast) {
                    builder.add("AsType");
                }
                builder.add("(source, $L, \"field '$L'\"", id.code, name);
                if (requiresCast) {
                    TypeName rawType = type;
                    if (rawType instanceof ParameterizedTypeName) {
                        rawType = ((ParameterizedTypeName) rawType).rawType;
                    }
                    builder.add(", $T.class", rawType);
                }
                builder.add(")");
            }
        }
        return builder.add(")").build();
    }
}
