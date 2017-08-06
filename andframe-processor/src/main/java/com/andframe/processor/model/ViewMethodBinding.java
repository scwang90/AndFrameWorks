package com.andframe.processor.model;

import java.util.Collections;
import java.util.List;

/**
 * 方法绑定
 */
public class ViewMethodBinding {

    public final String name;
    public final boolean required;
    public final List<Parameter> parameters;

    public ViewMethodBinding(String name, List<Parameter> parameters, boolean required) {
        this.name = name;
        this.required = required;
        this.parameters = Collections.unmodifiableList(parameters);
    }

    @Override
    public String toString() {
        return "method '" + name + "'";
    }
}
