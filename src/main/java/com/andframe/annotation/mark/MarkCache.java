package com.andframe.annotation.mark;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 缓存标记注解<br>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MarkCache {
    /**
     * 数据Model的类对象（json要用到）
     */
    Class<?> value() default MarkCache.class;
    /**
     * 缓存的KEY标识
     */
    String key() default "";
}

