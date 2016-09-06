package com.andframe.annotation.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectInit {
    /**
     * 是否要求必须完成和不允许抛出异常
     * 默认false允许异常
     * @return true 不允许异常
     */
    boolean value() default false;
}
