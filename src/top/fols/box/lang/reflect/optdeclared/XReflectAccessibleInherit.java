package top.fols.box.lang.reflect.optdeclared;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import top.fols.box.lang.XClass;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XArrays;
import top.fols.box.util.XDoubleLinked;
import top.fols.box.util.XKeyMap;

/**
 * XReflectAccessibleInherit
 */
public class XReflectAccessibleInherit {

    /**
     * FAST
     * WEAK_CACHE not need manager
     */
    private static final Map<Class, Method[]> WEAK_CACHE_DECLARED_METHODS = new WeakHashMap<>();
    private static Method[] cacheDeclaredMethods0(Class cls) {
        if (null == cls) {
            return null;
        }
        Method[] cache = WEAK_CACHE_DECLARED_METHODS.get(cls);
        if (null == cache) {
            WEAK_CACHE_DECLARED_METHODS.put(cls, cache = cls.getDeclaredMethods());
        }
        if (WEAK_CACHE_DECLARED_METHODS.size() >= 1024) {
            WEAK_CACHE_DECLARED_METHODS.clear();
        }
        return cache;
    }



    /**
     * no need to use caching
     */ 
//    private static final Map<Class, Field[]> WEAK_CACHE_DECLARED_FIELDS = new WeakHashMap<>();
    private static Field[] cacheDeclaredFields0(Class cls) {
        if (null == cls) {
            return null;
        }
        Field[] cache = cls.getDeclaredFields();
//        Field[] cache = WEAK_CACHE_DECLARED_FIELDS.get(cls);
//        if (null == cache) {
//            WEAK_CACHE_DECLARED_FIELDS.put(cls, cache = cls.getDeclaredFields());
//        }
//        if (WEAK_CACHE_DECLARED_FIELDS.size() >= 1024) {
//            WEAK_CACHE_DECLARED_FIELDS.clear();
//        }
        return cache;
    }

    /* ---- */


    /**
     * 
     */

    public static Constructor getConstructorSetAccessible(Class cls, Class... paramClass) {
        return XReflectAccessible.setAccessibleTrueOne(XReflectAccessible.getConstructor(cls, paramClass));
    }

    public static Constructor getConstructor(Class<?> cls, Class... paramClass) {
        try {
            return cls.getDeclaredConstructor(paramClass);
        } catch (NoSuchMethodException e) {
            e = null;
        }
        try {
            return cls.getConstructor(paramClass);
        } catch (NoSuchMethodException e) {
            e = null;
        }
        return null;
    }

    public static Field getFieldSetAccessible(Class cls, String name) {
        return XReflectAccessible.setAccessibleTrueOne(getField(cls, name));
    }

    public static Field getField(Class clazz, String name) {
        try {
            Field declaredField = clazz.getField(name);
            return declaredField;
        } catch (NoSuchFieldException e) {
            e = null;
        }
        while (null != clazz) {
            try {
                Field declaredField = clazz.getDeclaredField(name);
                return declaredField;
            } catch (NoSuchFieldException ex) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    public static Method getMethodSetAccessible(Class cls, String method, Class... paramClass) {
        return XReflectAccessible.setAccessibleTrueOne(getMethod(cls, method, paramClass));
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... array) {
        try {
            Method declaredMethod = clazz.getMethod(name, array);
            return declaredMethod;
        } catch (NoSuchMethodException e) {
            e = null;
        }
        while (null != clazz) {
            try {
                Method declaredMethod = clazz.getDeclaredMethod(name, array);
                return declaredMethod;
            } catch (NoSuchMethodException ex) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }






    /**
     * s Object.class
     * <p>
     * super
     */
    private static XDoubleLinked<Class<?>> getSuperClassLinkedRootToTop0(Class<?> cls) {
        XDoubleLinked<Class<?>> linked = new XDoubleLinked<>(null);
        Class<?> clazz = cls;
        while (null != clazz) {
            linked.addNext(new XDoubleLinked<Class<?>>(clazz));
            clazz = clazz.getSuperclass();
        }
        return linked;
    }

    /**
     * super
     * <p>
     * Object.class
     */
    private static XDoubleLinked<Class<?>> getSuperClassLinkedTopToRoot0(Class<?> cls) {
        XDoubleLinked<Class<?>> linked = new XDoubleLinked<>(null);
        XDoubleLinked<Class<?>> top = linked;
        Class<?> clazz = cls;
        while (null != clazz) {
            XDoubleLinked<Class<?>> c = new XDoubleLinked<Class<?>>(clazz);
            top.addNext(c);
            top = c;
            clazz = clazz.getSuperclass();
        }
        return linked;
    }




    // private static XDoubleLinkedList.VarLinkedList<Class<?>>
    // getInherits0(Class<?> cls) {
    // XDoubleLinkedList.VarLinkedList<Class<?>> linked = new
    // XDoubleLinkedList.VarLinkedList<Class<?>>(null);
    // XDoubleLinkedList.VarLinkedList<Class<?>> top = linked;

    // XDoubleLinkedList.VarLinkedList<Class<?>> supers =
    // getSuperClassLinked00(cls);
    // XDoubleLinkedList.VarLinkedList<Class<?>> now = supers;
    // while (null != now && null != (now =
    // (XDoubleLinkedList.VarLinkedList<Class<?>>) now.getNext())) {
    // Class scls = now.content();
    // Class[] inters = scls.getInterfaces();
    // if (null != inters) {
    // for (Class i : inters) {
    // XDoubleLinkedList.VarLinkedList<Class<?>> element = new
    // XDoubleLinkedList.VarLinkedList<Class<?>>(
    // i);
    // top.addNext(element);
    // top = element;
    // }
    // }
    // XDoubleLinkedList.VarLinkedList<Class<?>> element = new
    // XDoubleLinkedList.VarLinkedList<Class<?>>(scls);
    // top.addNext(element);
    // top = element;
    // }
    // return linked;
    // }

    public static Class[] getClassesAll(Class cls) {
        if (null == cls) {
            return null;
        }
        XKeyMap<Class> methodMap = new XKeyMap<>(new LinkedHashMap<>());
        XDoubleLinked<Class<?>> now = getSuperClassLinkedRootToTop0(cls);
        while (null != now && null != (now = (XDoubleLinked<Class<?>>) now.getNext())) {
            Class[] newms = now.content().getDeclaredClasses();
            methodMap.putAll(newms);
        }
        methodMap.putAll(cls.getClasses());
        Class[] ms = methodMap.keySet().toArray(new Class[methodMap.size()]);
        methodMap = null;
        return ms;
    }

    public static Constructor[] getConstructorsAllSetAccessible(Class<?> cls) {
        if (null == cls) {
            return null;
        }
        return XReflectAccessible.setAccessibleTrue(cls.getDeclaredConstructors());
    }

    public static Constructor[] getConstructorsAll(Class<?> cls) {
        if (null == cls) {
            return null;
        }
        return cls.getDeclaredConstructors();
    }

    public static Field[] getFieldsAllSetAccessible(Class<?> cls) {
        return XReflectAccessible.setAccessibleTrue(getFieldsAll(cls));
    }

    public static Field[] getFieldsAll(Class<?> cls) {
        if (null == cls) {
            return null;
        }
        Map<String, Field> methodMap = new HashMap<>();
        XDoubleLinked<Class<?>> now = getSuperClassLinkedRootToTop0(cls);
        while (null != now && null != (now = (XDoubleLinked<Class<?>>) now.getNext())) {
            Field[] newms = cacheDeclaredFields0(now.content());
            getFieldsAllPutAll0(methodMap, newms);
        }
        getFieldsAllPutAll0(methodMap, cls.getFields());
        Field[] ms = methodMap.values().toArray(new Field[methodMap.size()]);
        methodMap = null;
        return ms;
    }
    private static void getFieldsAllPutAll0(Map<String, Field> methodMap, Field[] newms) {
        for (Field n : newms) {
            methodMap.put(n.getName(), n);
        }
    }

    /**
     * Very slow
     * 
     * @param cls
     * @return cls methods and set accessible for true
     */
    public static Method[] getMethodsAllSetAccessible(Class<?> cls) {
        return XReflectAccessible.setAccessibleTrue(getMethodsAll(cls, null));
    }

    /**
     * Very slow
     * 
     * @param cls
     * @return cls methods
     */
    public static Method[] getMethodsAll(Class<?> cls) {
        return getMethodsAll(cls, null);
    }

    /**
     * Very slow
     * 
     * @param cls
     * @param requestName methodName
     * @return cls methods and set accessible for true
     */
    public static Method[] getMethodsAllSetAccessible(Class<?> cls, String requestName) {
        return XReflectAccessible.setAccessibleTrue(getMethodsAll(cls, requestName));
    }

    /**
     * Very slow
     * <p>
     * from Object.class >> cls.super >> @param cls all methods
     * 
     * @param cls
     * @param requestName methodName
     * @return cls methods
     */
    public static Method[] getMethodsAll(Class<?> cls, String requestName) {
        if (null == cls) {
            return null;
        }
        List<Method> methodMap = new ArrayList<>();
        XDoubleLinked<Class<?>> now = getSuperClassLinkedRootToTop0(cls);
        while (null != now && null != (now = (XDoubleLinked<Class<?>>) now.getNext())) {
            Method[] newms = cacheDeclaredMethods0(now.content());
            getMethodsAllPutAll0(methodMap, newms, requestName);
        }
        getMethodsAllPutAll0(methodMap, cls.getMethods(), requestName);
        Method[] ms = methodMap.toArray(new Method[methodMap.size()]);
        methodMap = null;
        return ms;
    }
    private static void getMethodsAllPutAll0(List<Method> methodMap, Method[] newms, String requestName) {
        for (Method newm : newms) {
            String newName = newm.getName();
            if (null != requestName && !newName.equals(requestName)) {
                continue;
            }
            Class[] pcs = newm.getParameterTypes();
            int indexOf = -1;
            for (int i0 = methodMap.size() - 1; i0 >= 0; i0--) {
                Method orm = methodMap.get(i0);
                Class[] ormpcs = orm.getParameterTypes();
                if (orm.getName().equals(newName) && XArrays.arrayContentsEquals(pcs, ormpcs)) {
                    indexOf = i0;
                    break;
                }
            }
            if (indexOf != -1) {
                methodMap.set(indexOf, newm);
            } else {
                methodMap.add(newm);
            }
        }
    }

    /**
     * 
     * @param cls
     * @return do not process duplicate data
     */
    public static Method[] getMethodsAllFastSetAccessible(Class<?> cls) {
        return XReflectAccessible.setAccessibleTrue(getMethodsAllFast(cls));
    }

    /**
     * from @param cls >> cls.super >> Object.class all methods
     * 
     * @param cls
     * @return do not process duplicate data
     */
    public static Method[] getMethodsAllFast(Class<?> cls) {
        if (null == cls) {
            return null;
        }
        List<Method> methodMap = new ArrayList<>();
        XDoubleLinked<Class<?>> now = getSuperClassLinkedTopToRoot0(cls);
        while (null != now && null != (now = (XDoubleLinked<Class<?>>) now.getNext())) {
            methodMap.addAll(Arrays.asList(cacheDeclaredMethods0(now.content())));
        }
        Method[] ms = methodMap.toArray(new Method[methodMap.size()]);
        methodMap = null;
        return ms;
    }
    /**
     * 
     * @param cls
     * @return do not process duplicate data
     */
    public static Method[] getMethodsAllFastSetAccessible(Class<?> cls, String requestName) {
        return XReflectAccessible.setAccessibleTrue(getMethodsAllFast(cls, requestName));
    }
    /**
     * 
     * @param cls
     * @return do not process duplicate data
     */
    public static Method[] getMethodsAllFast(Class<?> cls, String requestName) {
        Method[] methods = getMethodsAllFast(cls);
        if (null != requestName && null != methods) {
            List<Method> list = new ArrayList<>(methods.length);
            for (Method m : methods) {
                if (requestName.equals(m.getName())) {
                    list.add(m);
                }
            }
            methods = new Method[list.size()];
            list = null;
        }
        return methods;
    }







    /**
     * 
     */
    public static Constructor findConstructor(Class cls, Class... classs) {
        return getConstructorSetAccessible(cls, null == classs ? XStaticFixedValue.nullClassArray : classs);
    }

    public static Object newInstance(Constructor c, Object... object)
    throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException {
        return XReflectAccessible.setAccessibleTrueOne(c)
            .newInstance(null == object ? XStaticFixedValue.nullObjectArray : object);
    }

    public static Method findMethod(Class Cls, String functionName, Class... classs) {
        return getMethodSetAccessible(Cls, functionName, null == classs ? XStaticFixedValue.nullClassArray : classs);
    }

    private static Object execMethod0(Object Instance, Method method, Object... object)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        return XReflectAccessible.setAccessibleTrueOne(method).invoke(Instance,
            null == object ? XStaticFixedValue.nullObjectArray : object);
    }

    public static Field findField(Class cls, String name) throws NoSuchFieldException {
        return XReflectAccessible.setAccessibleTrueOne(getField(cls, name));
    }

    private static Object fieldGetValue0(Field field, Object object)
    throws IllegalAccessException, IllegalArgumentException {
        return XReflectAccessible.setAccessibleTrueOne(field).get(object);
    }

    private static void fieldSetValue0(Field field, Object object, Object newFieldValue)
    throws IllegalAccessException, IllegalArgumentException {
        XReflectAccessible.setAccessibleTrueOne(field).set(object, newFieldValue);
    }

    /**
     * 
     */

    public static Constructor findConstructor(Object instance, Class... classs) {
        return findConstructor(instance.getClass(), classs);
    }

    public static Object execMethod(Object Instance, Class cls, String method, Class[] classs, Object... object)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        return execMethod0(Instance, findMethod(cls, method, classs), object);
    }

    public static Object execMethod(Object Instance, String method, Class[] classs, Object... object)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        return execMethod0(Instance, findMethod(XClass.getClass(Instance), method, classs), object);
    }

    public static Object execStaticMethod(Object Instance, String method, Class[] classs, Object... object)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        return execMethod0(null, findMethod(XClass.getClass(Instance), method, classs), object);
    }

    public static Object execStaticMethod(Class cls, String method, Class[] classs, Object... object)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        return execMethod0(null, findMethod(cls, method, classs), object);
    }

    public static Object getFieldValue(Class ClassV, Object Instance, String name)
    throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        return fieldGetValue0(findField(ClassV, name), Instance);
    }

    public static Object getFieldValue(Object Instance, String name)
    throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        return fieldGetValue0(findField(Instance.getClass(), name), Instance);
    }

    public static Object getStaticFieldValue(Class cls, String name)
    throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        return fieldGetValue0(findField(cls, name), null);
    }

    public static Object getStaticFieldValue(Field field) throws IllegalAccessException, IllegalArgumentException {
        return fieldGetValue0(field, null);
    }

    public static void setFieldValue(Class instanceCls, Object Instance, String name, Object Value)
    throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        fieldSetValue0(findField(instanceCls, name), Instance, Value);
    }

    public static void setFieldValue(Object Instance, String name, Object Value)
    throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        fieldSetValue0(findField(Instance.getClass(), name), Instance, Value);
    }

    public static void setStaticFieldValue(Class cls, String name, Object newFieldValue)
    throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        fieldSetValue0(findField(cls, name), null, newFieldValue);
    }

    public static void setStaticFieldValue(Field field, Object newFieldValue)
    throws IllegalAccessException, IllegalArgumentException {
        fieldSetValue0(field, null, newFieldValue);
    }

}
