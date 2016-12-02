package com.andframe.util.java;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * java 反射工具类
 *
 * @author 树朾
 */
@SuppressWarnings("unused")
public class AfReflecter {


    /**
     * 获取 subobj 相对 父类 supclass 的 第index个泛型参数
     *
     * @param subobj   对象 一般用this 如 class Type<E> 可传入 Type.this
     * @param supclass 父类(模板类) 如 class Type<E> 可传入 Type.class
     * @param index    要获取参数的序列 一般用0
     * @return null 查找失败 否则返回参数类型Class<?>
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getActualTypeArgument(Object subobj, Class<?> supclass, int index) {
        Class<?> subclass = subobj.getClass();
        List<ParameterizedType> ptypes = new ArrayList<>();
        ParameterizedType ptype;
        while (supclass != null && !supclass.equals(subclass)) {
            Type type = subclass.getGenericSuperclass();
            if (type == null) {
                throw new Error("GenericSuperclass not find");
            } else if (type instanceof Class) {
                subclass = (Class<?>) type;
            } else if (type instanceof ParameterizedType) {
                ptype = ParameterizedType.class.cast(type);
                ptypes.add(ptype);
                subclass = (Class<?>) ptype.getRawType();
            } else if (type instanceof GenericArrayType) {
                GenericArrayType gtype = GenericArrayType.class.cast(type);
                subclass = (Class<?>) gtype.getGenericComponentType();
            } else {
                throw new Error("GenericSuperclass not case");
            }
        }
        Type type;
        do {
            type = ptypes.get(ptypes.size() - 1).getActualTypeArguments()[index];
            ptypes.remove(ptypes.size() - 1);
        } while (!(type instanceof Class) && ptypes.size() > 0);
        //noinspection ConstantConditions
        return (Class<T>) type;
    }

    /**
     * 在clazz中获取所有Field(包括父类)
     *
     * @param clazz 开始扫描类型
     * @return Field[] （父类到子类排序好的）
     */
    public static Field[] getField(Class<?> clazz) {
        return getField(clazz, Object.class);
    }

    /**
     * 在clazz中获取所有Field(向父类扫描知道stop停止)
     *
     * @param clazz    开始扫描类型
     * @param stoptype 停止扫描类型 stop 本身不被扫描 必须是 clazz 父类
     * @return Field[] （父类到子类排序好的）
     */
    public static Field[] getField(Class<?> clazz, Class<?> stoptype) {
        List<List<Field>> lists = new ArrayList<>();
        while (clazz != null && !clazz.equals(stoptype)) {
            List<Field> fields = new ArrayList<>();
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
            lists.add(fields);
        }
        List<Field> fields = new ArrayList<>();
        Collections.reverse(lists);
        for (List<Field> tmethods : lists) {
            fields.addAll(tmethods);
        }
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * 在clazz中获取所有标记annot的Field(包括父类)
     *
     * @param clazz 开始扫描类型
     * @param annot Annotation
     * @return Field[] （父类到子类排序好的）
     */
    public static Field[] getFieldAnnotation(Class<?> clazz, Class<? extends Annotation> annot) {
        return getFieldAnnotation(clazz, Object.class, annot);
    }

    /**
     * 在clazz中获取所有标记annot的Field(包括父类)
     *
     * @param type     开始扫描类型
     * @param stoptype 停止扫描类型 stop 本身不被扫描 必须是 clazz 父类
     * @param annot    Annotation
     * @return Field[] （父类到子类排序好的）
     */
    public static Field[] getFieldAnnotation(Class<?> type, Class<?> stoptype, Class<? extends Annotation> annot) {
        Class<?> clazz = type;
        List<List<Field>> lists = new ArrayList<>();
        while (clazz != null && !clazz.equals(stoptype)) {
            List<Field> fields = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(annot)) {
                    fields.add(field);
                }
            }
            lists.add(fields);
            clazz = clazz.getSuperclass();
        }
        List<Field> fields = new ArrayList<>();
        Collections.reverse(lists);
        for (List<Field> tmethods : lists) {
            fields.addAll(tmethods);
        }
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * 在clazz中获取所有Method(包括父类)
     *
     * @param clazz 开始扫描类型
     * @return Method[] （父类到子类排序好的）
     */
    public static Method[] getMethod(Class<?> clazz) {
        return getMethod(clazz, Object.class);
    }

    /**
     * 在clazz中获取所有Method(向父类扫描知道stop停止)
     *
     * @param clazz    开始扫描类型
     * @param stoptype 停止扫描类型 stop 本身不被扫描 必须是 clazz 父类
     * @return Method[] （父类到子类排序好的）
     */
    public static Method[] getMethod(Class<?> clazz, Class<?> stoptype) {
        List<List<Method>> lists = new ArrayList<>();
        while (clazz != null && !clazz.equals(stoptype)) {
            List<Method> fields = new ArrayList<>();
            Collections.addAll(fields, clazz.getDeclaredMethods());
            clazz = clazz.getSuperclass();
            lists.add(fields);
        }
        List<Method> fields = new ArrayList<>();
        Collections.reverse(lists);
        for (List<Method> tmethods : lists) {
            fields.addAll(tmethods);
        }
        return fields.toArray(new Method[fields.size()]);
    }

    /**
     * 在clazz中获取所有标记 annot 的 Method (包括父类)
     *
     * @param clazz 开始扫描类型
     * @param annot Annotation
     * @return Method[] （父类到子类排序好的）
     */
    public static Method[] getMethodAnnotation(Class<?> clazz, Class<? extends Annotation> annot) {
        return getMethodAnnotation(clazz, Object.class, annot);
    }

    /**
     * 在clazz中获取所有标记 annot 的 Method (包括父类)
     *
     * @param type     开始扫描类型
     * @param stoptype 停止扫描类型 stop 本身不被扫描 必须是 clazz 父类
     * @param annot    Annotation
     * @return Method[] （父类到子类排序好的）
     */
    public static Method[] getMethodAnnotation(Class<?> type, Class<?> stoptype, Class<? extends Annotation> annot) {
        List<List<Method>> lists = new ArrayList<>();
        while (!type.equals(stoptype)) {
            List<Method> methods = new ArrayList<>();
            for (Method method : type.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annot)) {
                    methods.add(method);
                }
            }
            lists.add(methods);
            type = type.getSuperclass();
        }
        List<Method> methods = new ArrayList<>();
        Collections.reverse(lists);
        for (List<Method> tmethods : lists) {
            methods.addAll(tmethods);
        }
        return methods.toArray(new Method[methods.size()]);
    }

    /**
     * 获取 type 的 method 的 Annotation (包括父类)
     *
     * @param type   对象类型
     * @param method 方法名称
     * @param annot  Annotation
     * @return method or null
     */
    public static <T extends Annotation> T getMethodAnnotation(Class<?> type, String method, Class<T> annot) {
        return getMethodAnnotation(type, Object.class, method, annot);
    }

    /**
     * 获取 type 的 method 的 Annotation (包括父类)
     *
     * @param type   对象类型
     * @param stoptype 停止扫描类型 stop 本身不被扫描 必须是 clazz 父类
     * @param method 方法名称
     * @param annot  Annotation
     * @return method or null
     */
    public static <T extends Annotation> T getMethodAnnotation(Class<?> type, Class<?> stoptype, String method, Class<T> annot) {
        while (type != null && !type.equals(stoptype)) {
            for (Method dmethod : type.getDeclaredMethods()) {
                if (dmethod.getName().equals(method)) {
                    if (dmethod.isAnnotationPresent(annot)) {
                        return dmethod.getAnnotation(annot);
                    }
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    /**
     * 获取类型 type 的 Annotation (包括父类)
     * @param type   对象类型
     * @param annot  Annotation
     * @return Annotation or null
     */
    public static <T extends Annotation> T getAnnotation(Class<?> type, Class<T> annot) {
        return getAnnotation(type, Object.class, annot);
    }

    /**
     * 获取类型 type 的 Annotation (包括父类)
     * @param type   对象类型
     * @param stoptype 停止扫描类型 stop 本身不被扫描 必须是 clazz 父类
     * @param annot  Annotation
     * @return Annotation or null
     */
    public static <T extends Annotation> T getAnnotation(Class<?> type, Class<?> stoptype, Class<T> annot) {
        while (type != null && !type.equals(stoptype)) {
            if (type.isAnnotationPresent(annot)) {
                return type.getAnnotation(annot);
            }
            type = type.getSuperclass();
        }
        return null;
    }

    /**
     * 获取type的method(包括父类)
     *
     * @param type   对象类型
     * @param method 方法名称
     * @return method or null
     */
    public static Method getMethod(Class<?> type, String method) {
        while (type != null && !type.equals(Object.class)) {
            for (Method dmethod : type.getDeclaredMethods()) {
                if (dmethod.getName().equals(method)) {
                    return dmethod;
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    /**
     * 获取type的method(包括父类)
     *
     * @param type   对象类型
     * @param method 方法名称
     * @param args   如果 args 没有null 可以精确查找方法
     * @return method or null
     */
    public static Method getMethod(Class<?> type, String method, Object... args) {
        if (args != null) {
            Class<?>[] parameterTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    parameterTypes[i] = args[i].getClass();
                } else {
                    parameterTypes = null;
                    break;
                }
            }
            if (parameterTypes != null) {
                Method method1 = getMethod(type, method, parameterTypes);
                if (method1 != null) {
                    return method1;
                }
            }
        }
        return getMethod(type, method);
    }

    /**
     * 获取type的method(包括父类)
     *
     * @param type   对象类型
     * @param method 方法名称
     * @return method or null
     */
    public static Method getMethod(Class<?> type, String method, Class<?>[] parameterTypes) {
        while (type != null && !type.equals(Object.class)) {
            try {
                return type.getDeclaredMethod(method, parameterTypes);
            } catch (NoSuchMethodException e) {
//				e.printStackTrace();
            }
            type = type.getSuperclass();
        }
//        new NoSuchMethodException(method).printStackTrace();
        return null;
    }

    /**
     * 获取type的field(包括父类)
     *
     * @param type  对象类型
     * @param field 不支持‘.’路径
     * @return field or null
     */
    public static Field getField(Class<?> type, String field) {
        while (type != null && !type.equals(Object.class)) {
            try {
                return type.getDeclaredField(field);
            } catch (NoSuchFieldException e) {
                //e.printStackTrace();
            }
            type = type.getSuperclass();
        }
        return null;
    }

    /**
     * 深层获取类型type的属性path的Field(包括父类)（内部递归算法）
     */
    private static Field getField(Class<?> type, String[] path, int index) throws Exception {
        Field field = getField(type, path[index]);
        if (field == null) return null;
        if (path.length == index + 1) {
            return field;
        } else if (path.length > 0) {
            return getField(field.getType(), path, index + 1);
        }
        return field;
    }

    /**
     * 获取字段 Field(包括父类)
     *
     * @param model 操作对象
     * @param field 支持‘.’路径 如 person.name
     * @return Field
     * @throws Exception
     */
    public static Field getField(Object model, String field) throws Exception {
        return getField(model.getClass(), field.split("\\."), 0);
    }

    /**
     * 获取字段 Field(包括父类)
     *
     * @param model 操作对象
     * @param field 支持‘.’路径 如 person.name
     * @return Field
     */
    public static Field getFieldNoException(Object model, String field) {
        try {
            return getField(model.getClass(), field.split("\\."), 0);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 获取对象obj的属性path的value （内部递归算法）
     */
    private static void invokeMember(Class<?> type, String[] path, Object obj,
                                     Object value, int index) throws Exception {
        Field field = getField(type, path[index]);
        if (field == null) return;
        field.setAccessible(true);
        if (path.length == index + 1) {
            field.set(obj, value);
        } else if (path.length > 0) {
            obj = field.get(obj);
            invokeMember(obj.getClass(), path, obj, value, index + 1);
        }
    }

    /**
     * 获取对象obj的属性path的value （内部递归算法）
     */
    private static Object invokeMember(Class<?> type, String[] path, Object obj, int index) throws Exception {
        Field field = getField(type, path[index]);
        if (field == null) return null;
        field.setAccessible(true);
        Object value = field.get(obj);
        if (path.length == index + 1) {
            return value;
        } else if (path.length > 0 && value != null) {
            return invokeMember(value.getClass(), path, value, index + 1);
        }
        return value;
    }

    /**
     * 获取 obj 属性 field 的值
     *
     * @param obj   操作对象
     * @param field 支持‘.’路径 如 person.name
     * @throws Exception 数组越界
     */
    public static void setMember(Object obj, String field, Object value) throws Exception {
        invokeMember(getType(obj), field.split("\\."), obj, value, 0);
    }

    /**
     * 获取 obj 属性 field 的值
     *
     * @param obj   操作对象
     * @param field 支持‘.’路径 如 person.name
     * @return 成功失败
     */
    public static boolean setMemberNoException(Object obj, String field, Object value) {
        try {
            invokeMember(getType(obj), field.split("\\."), obj, value, 0);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    private static Class<?> getType(Object obj) {
        return obj instanceof Class ? (Class<?>) obj: obj.getClass();
    }

    //<editor-fold desc="通过类型设置获取">
    /**
     * 通过 类型clazz匹配 设置obj 的对应 Field 的值
     * @param obj 对象 或者 Clazz（可以匹配 statis Field）
     * @param clazz 匹配的clazz （支持子类匹配 如 clazz = List 可以匹配 ArrayList 的 Filed）
     * @return 如果有多个返回第一个 否则null
     */
    public static <T> T getMemberByType(Object obj, Class<T> clazz) throws IllegalAccessException {
        Field field = getFieldByType(obj, clazz);
        if (field != null) {
            field.setAccessible(true);
            return clazz.cast(field.get(obj));
        }
        return null;
    }


    /**
     * 通过 类型clazz匹配 设置obj 的对应 Field
     * @param obj 对象 或者 Clazz（可以匹配 statis Field）
     * @param clazz 匹配的clazz （支持子类匹配 如 clazz = List 可以匹配 ArrayList 的 Filed）
     * @param value 设置的值
     */
    public static void setMemberByType(Object obj, Object value, Class<?> clazz) throws IllegalAccessException {
        Field field = getFieldByType(obj, clazz);
        if (field != null) {
            field.setAccessible(true);
            field.set(obj, value);
        }
    }

    /**
     * 通过 类型clazz匹配 获取 obj 的对应 Field
     * @param obj 对象 或者 Clazz（可以匹配 statis Field）
     * @param clazz 匹配的clazz （支持子类匹配 如 clazz = List 可以匹配 ArrayList 的 Filed）
     * @return 如果有多个返回第一个 否则null
     */
    public static Field getFieldByType(Object obj, Class<?> clazz) {
        Class<?> type = getType(obj);
        Field[] fields = getField(type);
        for (Field field : fields) {
            if (clazz.isAssignableFrom(field.getType())) {
                if (!Modifier.isStatic(field.getModifiers()) || type == obj) {
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * 通过 类型clazz精确匹配  设置obj 的对应 Field 的值
     * 与 getMemberByType 的差别在于 不支持子类匹配 如 class = List 不能匹配 ArrayList 的 Filed
     * @param obj 对象 或者 Clazz（可以匹配 statis Field）
     * @param clazz 匹配的clazz
     * @return 如果有多个返回第一个 否则null
     */
    public static <T> T getPreciseMemberByType(Object obj, Class<T> clazz) throws IllegalAccessException {
        Field field = getPreciseFieldByType(obj, clazz);
        if (field != null) {
            field.setAccessible(true);
            return clazz.cast(field.get(obj));
        }
        return null;
    }

    /**
     * 通过 类型clazz精确匹配  设置obj 的对应 Field 的值
     * 与 getMemberByType 的差别在于 不支持子类匹配 如 class = List 不能匹配 ArrayList 的 Filed
     * @param obj 对象 或者 Clazz（可以匹配 statis Field）
     * @param clazz 匹配的clazz
     * @param type 定义 Field 的 type
     * @return 如果有多个返回第一个 否则null
     */
    public static <T> T getPreciseMemberByType(Object obj, Class<T> clazz, Class<?> type) throws IllegalAccessException {
        Field field = getPreciseFieldByType(obj, clazz, type);
        if (field != null) {
            field.setAccessible(true);
            return clazz.cast(field.get(obj));
        }
        return null;
    }

    /**
     * 通过 类型clazz精确匹配  设置obj 的对应 Field
     * 与 setMemberByType 的差别在于 不支持子类匹配 如 class = List 不能匹配 ArrayList 的 Filed
     * @param obj 对象 或者 Clazz（可以匹配 statis Field）
     * @param clazz 匹配的clazz
     * @param value 设置的值
     */
    public static void setPreciseMemberByType(Object obj, Object value, Class<?> clazz) throws IllegalAccessException {
        Field field = getPreciseFieldByType(obj, clazz);
        if (field != null) {
            field.setAccessible(true);
            field.set(obj, value);
        }
    }


    /**
     * 通过 类型clazz精确匹配  设置obj 的对应 Field
     * 与 setMemberByType 的差别在于 不支持子类匹配 如 class = List 不能匹配 ArrayList 的 Filed
     * @param obj 对象 或者 Clazz（可以匹配 statis Field）
     * @param value 设置的值
     * @param clazz 匹配的clazz
     * @param type 定义 Field 的 type
     */
    public static void setPreciseMemberByType(Object obj, Object value, Class<?> clazz, Class<?> type) throws IllegalAccessException {
        Field field = getPreciseFieldByType(obj, clazz, type);
        if (field != null) {
            field.setAccessible(true);
            field.set(obj, value);
        }
    }

    /**
     * 通过 类型clazz精确匹配 获取 obj 的对应 Field
     * 与 getFieldByType 的差别在于 不支持子类匹配 如 class = List 不能匹配 ArrayList 的 Filed
     * @param obj 对象 或者 Clazz（可以匹配 statis Field）
     * @param clazz 匹配的clazz
     * @return 如果有多个返回第一个 否则null
     */
    public static Field getPreciseFieldByType(Object obj, Class<?> clazz) {
        Class<?> type = getType(obj);
        Field[] fields = getField(type);
        for (Field field : fields) {
            if (clazz.equals(field.getType())) {
                if (!Modifier.isStatic(field.getModifiers()) || type == obj) {
                    return field;
                }
            }
        }
        return null;
    }


    /**
     * 通过 类型clazz精确匹配 获取 obj 的对应 Field
     * 与 getFieldByType 的差别在于 不支持子类匹配 如 class = List 不能匹配 ArrayList 的 Filed
     * @param obj 对象 或者 Clazz（可以匹配 statis Field）
     * @param clazz 匹配的clazz
     * @param type 定义 Field 的 type
     * @return 如果有多个返回第一个 否则null
     */
    public static Field getPreciseFieldByType(Object obj, Class<?> clazz, Class<?> type) {
        Class<?> subtype = getType(obj);
        Field[] fields = getField(subtype);
        for (Field field : fields) {
            if (clazz.equals(field.getType()) && field.getDeclaringClass().equals(type)) {
                if (!Modifier.isStatic(field.getModifiers()) || subtype == obj) {
                    return field;
                }
            }
        }
        return null;
    }
    //</editor-fold>

    /**
     * 获取 obj 属性 field 的值
     *
     * @param obj   操作对象
     * @param field 支持‘.’路径 如 person.name
     * @return 值
     * @throws Exception 数组越界
     */
    public static Object getMember(Object obj, String field) throws Exception {
        return invokeMember(getType(obj), field.split("\\."), obj, 0);
    }

    /**
     * 获取 obj 属性 field 的值
     *
     * @param obj   操作对象
     * @param field 支持‘.’路径 如 person.name
     * @return 值
     */
    public static Object getMemberNoException(Object obj, String field) {
        try {
            return invokeMember(getType(obj), field.split("\\."), obj, 0);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 获取 obj 属性 field 的值
     *
     * @param obj   操作对象
     * @param field 支持‘.’路径 如 person.name
     * @return 值
     * @throws Exception 数组越界
     */
    public static <T> T getMember(Object obj, String field, Class<T> type) throws Exception {
        obj = invokeMember(getType(obj), field.split("\\."), obj, 0);
        if (type.isInstance(obj)) {
            type.cast(obj);
        }
        return null;
    }

    /**
     * 获取 obj 属性 field 的值
     *
     * @param obj   操作对象
     * @param field 支持‘.’路径 如 person.name
     * @return 值
     */
    public static <T> T getMemberNoException(Object obj, String field, Class<T> type) {
        try {
            return type.cast(invokeMember(getType(obj), field.split("\\."), obj, 0));
        } catch (Throwable e) {
            return null;
        }
    }

    public static Object doMethod(Object obj, String smethod, Object... args) {
        Method method = getMethod(obj.getClass(), smethod, args);
        try {
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T doMethod(Object obj, String smethod, Class<T> type, Object... args) {
        Method method = getMethod(obj.getClass(), smethod, args);
        try {
            return type.cast(method.invoke(obj, args));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object doMethod(Object obj, String smethod, Class<?>[] argtype, Object... args) {
        Method method = getMethod(obj.getClass(), smethod, argtype);
        if (method != null) {
            try {
                method.setAccessible(true);
                return method.invoke(obj, args);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> T doMethod(Object obj, String smethod, Class<T> type, Class<?>[] argtype, Object... args) {
        Method method = getMethod(obj.getClass(), smethod, argtype);
        try {
            return type.cast(method != null ? method.invoke(obj, args) : null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object doStaticMethod(Class<?> type, String smethod, Object... args) {
        Method method = getMethod(type, smethod, args);
        return doStaticMethod(type, method, args);
    }


    public static <T> T doStaticMethod(Class<?> type, String smethod, Class<T> rettype, Object... args) {
        Method method = getMethod(type, smethod, args);
        return rettype.cast(doStaticMethod(type, method, args));
    }

    public static <T> Object doStaticMethod(Class<?> type, Method method, Object... args) {
        try {
            return method.invoke(type, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 为 type 创建实例
     *
     * @param type 类型
     * @param <T>  模板参数
     * @return 新的实例 失败会 null （很少会失败）
     */
    public static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (Throwable ignored) {
        }
        try {
            return UnsafeAllocator.create().newInstance(type);
        } catch (Throwable ignored) {
        }
        return null;
    }


    /**
     * 为 type 创建实例
     *
     * @param type 类型
     * @param <T>  模板参数
     * @return 新的实例 失败会 null （很少会失败）
     */
    public static <T> T newUnsafeInstance(Class<T> type) {
        try {
            return UnsafeAllocator.create().newInstance(type);
        } catch (Throwable ignored) {
        }
        return null;
    }


    /**
     * Do sneaky things to allocate objects without invoking their constructors.
     *
     * @author Joel Leitch
     * @author Jesse Wilson
     */
    public static abstract class UnsafeAllocator {
        public abstract <T> T newInstance(Class<T> c) throws Exception;

        public static UnsafeAllocator create() {
            // try JVM
            // public class Unsafe {
            //   public Object allocateInstance(Class<?> type);
            // }
            try {
                Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                Field f = unsafeClass.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                final Object unsafe = f.get(null);
                final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
                return new UnsafeAllocator() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public <T> T newInstance(Class<T> c) throws Exception {
                        return (T) allocateInstance.invoke(unsafe, c);
                    }
                };
            } catch (Exception ignored) {
            }

            // try dalvikvm, post-gingerbread
            // public class ObjectStreamClass {
            //   private static native int getConstructorId(Class<?> c);
            //   private static native Object newInstance(Class<?> instantiationClass, int methodId);
            // }
            try {
                Method getConstructorId = ObjectStreamClass.class
                        .getDeclaredMethod("getConstructorId", Class.class);
                getConstructorId.setAccessible(true);
                final int constructorId = (Integer) getConstructorId.invoke(null, Object.class);
                final Method newInstance = ObjectStreamClass.class
                        .getDeclaredMethod("newInstance", Class.class, int.class);
                newInstance.setAccessible(true);
                return new UnsafeAllocator() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public <T> T newInstance(Class<T> c) throws Exception {
                        return (T) newInstance.invoke(null, c, constructorId);
                    }
                };
            } catch (Exception ignored) {
            }

            // try dalvikvm, pre-gingerbread
            // public class ObjectInputStream {
            //   private static native Object newInstance(
            //     Class<?> instantiationClass, Class<?> constructorClass);
            // }
            try {
                final Method newInstance = ObjectInputStream.class
                        .getDeclaredMethod("newInstance", Class.class, Class.class);
                newInstance.setAccessible(true);
                return new UnsafeAllocator() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public <T> T newInstance(Class<T> c) throws Exception {
                        return (T) newInstance.invoke(null, c, Object.class);
                    }
                };
            } catch (Exception ignored) {
            }

            // give up
            return new UnsafeAllocator() {
                @Override
                public <T> T newInstance(Class<T> c) {
                    throw new UnsupportedOperationException("Cannot allocate " + c);
                }
            };
        }
    }


}
