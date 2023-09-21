package app.utils.reflects;

import top.fols.atri.lang.Finals;
import top.fols.atri.reflect.ReflectCache;
import top.fols.atri.reflect.Reflects;
import top.fols.atri.util.Throwables;

import java.io.*;
import java.lang.reflect.*;

public class ReflectUtils {


    public static ReflectCache cache = ReflectCache.DEFAULT;



    public static boolean isIgnoreField(Field field) {
        if (null == field) return true;

        int modifier = field.getModifiers();
        return Modifier.isTransient(modifier) || Modifier.isFinal(modifier);
    }



    /**
     * @see #isIgnoreField
     */
    public static <T> T setFieldValue(Object origin, T set) {
        Field[] fields = cache.fields(set.getClass());
        for (Field field: fields) {
            if (isIgnoreField(field)) {
                continue;
            }
            Field originField = cache.field(origin.getClass(), field.getName());
            if (isIgnoreField(originField)) {
                continue;
            }
            try {
                Reflects.accessible(field).set(set, Reflects.accessible(originField).get(origin));
            } catch (IllegalAccessException ignored) {
            }
        }
        return set;
    }




    public static Class<Serializable> CLASS_SERIALIZABLE = Serializable.class;

    public static <T> T clone(T origin) {
        if (null == origin) { return null; }
        Class<?> aClass = origin.getClass();
        try {
            if (aClass.isArray()) { // 。。。 不会clone元素
                return cloneArray(origin);
            }


            try {
                Method cloneM = aClass.getMethod("clone", Finals.EMPTY_CLASS_ARRAY);
                if (cloneM.getDeclaringClass() == aClass) {
                    //noinspection unchecked
                    return (T) cloneM.invoke(origin, Finals.EMPTY_OBJECT_ARRAY);
                }
            } catch (Throwable ignored) {}

            if (origin instanceof Serializable) {
                try {
                    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
                    objectOutputStream.writeObject(origin);
                    objectOutputStream.flush();

                    ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(arrayOutputStream.toByteArray()));
                    @SuppressWarnings("unchecked") T instance =  (T) objectInputStream.readObject();
                    return instance;
                } catch (Throwable ignored) {}
            }

            Constructor<?> constructor = cache.constructor(aClass, Finals.EMPTY_CLASS_ARRAY);
            if (null == constructor) {
                throw new UnsupportedOperationException("not found constructor: () in "+aClass);
            }

            @SuppressWarnings("unchecked") T instance = (T) Reflects.accessible(constructor).newInstance(Finals.EMPTY_OBJECT_ARRAY);
            Field[] fields = cache.fields(aClass);
            for (Field field: fields) {
                if (isIgnoreField(field)) {
                    continue;
                }
                Reflects.accessible(field);

                Class<?> type = field.getType();
                Object fieldValue = field.get(origin);
                Object fieldClone;
                if (type.isPrimitive() || type == Finals.STRING_CLASS) {
                    fieldClone = fieldValue;
                } else {
                    fieldClone = clone(fieldValue);
                }
                field.set(instance, fieldClone);
            }
            return instance;
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw Throwables.convertRuntimeException(e);
        }
    }
    public static <T> T cloneArray(T src) {
        if (null == src) { return null; }
        Class<?> aClass = src.getClass();
        if (aClass.isArray()) {
            if (src instanceof byte[]) {
                return (T) ((byte[])   src).clone();
            } else if (src instanceof short[]) {
                return (T) ((short[])  src).clone();
            } else if (src instanceof int[]) {
                return (T) ((int[])    src).clone();
            } else if (src instanceof long[]) {
                return (T) ((long[])   src).clone();
            } else if (src instanceof char[]) {
                return (T) ((char[])   src).clone();
            } else if (src instanceof float[]) {
                return (T) ((float[])  src).clone();
            } else if (src instanceof double[]) {
                return (T) ((double[]) src).clone();
            } else if (src instanceof boolean[]) {
                return (T) ((boolean[])src).clone();
            } else {
                Object[] strArr = (Object[]) src;
                Object[] valArr = (Object[]) Array.newInstance(aClass.getComponentType(), strArr.length);
                for (int i = 0; i < strArr.length; i++) {
                    valArr[i] = clone(strArr[i]);
                }
                //noinspection unchecked
                return (T) valArr;
            }
        } else {
            throw new UnsupportedOperationException("type: " + aClass);
        }
    }






    public static boolean has(Class<?>[] classes, Class<?> classz) {
        for (Class<?> aClass : classes) {
            if (aClass == classz) { return true; }
        }
        return false;
    }





}
