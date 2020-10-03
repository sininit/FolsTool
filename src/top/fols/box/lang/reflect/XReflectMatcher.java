package top.fols.box.lang.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import top.fols.box.lang.XClass;
import top.fols.box.lang.reflect.XReflectCache.ConstructorList;
import top.fols.box.lang.reflect.XReflectCache.MethodList;
import top.fols.box.lang.reflect.optdeclared.XReflectAccessible;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XObjects;
import top.fols.box.util.XStringJoiner;

public class XReflectMatcher {

    /**
     * matcher Class type param
     */

    /**
     * @param paramTypeClass nullable
     */
    public static Constructor matchConstructor(Constructor[] list, Class[][] listParameterTypes,
            boolean nullObjectCanCastToClass, Class... paramClassArr) {
        if (null == list) {
            return null;
        }
        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * @param paramTypeClass nullable
     */
    public static Constructor[] matchConstructors(Constructor[] list, Class[][] listParameterTypes,
            boolean nullObjectCanCastToClass, Class... paramClassArr) {
        if (null == list) {
            return XStaticFixedValue.nullConstructorArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullConstructorArray;
        }
        Constructor[] result = null;
        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    if (null == result) {
                        result = new Constructor[list.length];
                    }
                    result[index] = element;
                    index++;
                }
            }
        }
        if (null != result) {
            return Arrays.copyOf(result, index);
        } else {
            return XStaticFixedValue.nullConstructorArray;
        }
    }




    
    /**
     * @param returnClass        nullable
     * @param listParameterTypes nullable
     * @param methodName         nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, Class returnClass, String methodName,
            boolean nullObjectCanCastToClass, Class... paramClassArr) {
        if (null == list) {
            return null;
        }
        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length
                    && (null == returnClass || returnClass == element.getReturnType())
                    && (null == methodName || methodName.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * @param returnClass        nullable
     * @param listParameterTypes nullable
     * @param methodName         nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, Class returnClass,
            String methodName, boolean nullObjectCanCastToClass, Class... paramClassArr) {
        if (null == list) {
            return XStaticFixedValue.nullMethodArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullMethodArray;
        }
        Method[] result = null;
        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length
                    && (null == returnClass || returnClass == element.getReturnType())
                    && (null == methodName || methodName.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    if (null == result) {
                        result = new Method[list.length];
                    }
                    result[index] = element;
                    index++;
                }
            }
        }
        if (null != result) {
            return Arrays.copyOf(result, index);
        } else {
            return XStaticFixedValue.nullMethodArray;
        }
    }

    /**
     * matcher object type param
     */

    /**
     * @param paramTypeClass nullable
     */
    public static Constructor matchConstructor(Constructor[] list, Class[][] listParameterTypes,
            boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        if (null == list) {
            return null;
        }
        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * @param paramTypeClass nullable
     */
    public static Constructor[] matchConstructors(Constructor[] list, Class[][] listParameterTypes,
            boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        if (null == list) {
            return XStaticFixedValue.nullConstructorArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullConstructorArray;
        }
        Constructor[] result = null;
        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    if (null == result) {
                        result = new Constructor[list.length];
                    }
                    result[index] = element;
                    index++;
                }
            }
        }
        if (null != result) {
            return Arrays.copyOf(result, index);
        } else {
            return XStaticFixedValue.nullConstructorArray;
        }
    }

    /**
     * @param returnClass        nullable
     * @param listParameterTypes nullable
     * @param methodName         nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, Class returnClass, String methodName,
            boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        if (null == list) {
            return null;
        }
        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length
                    && (null == returnClass || returnClass == element.getReturnType())
                    && (null == methodName || methodName.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * @param returnClass        nullable
     * @param listParameterTypes nullable
     * @param methodName         nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, Class returnClass,
            String methodName, boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        if (null == list) {
            return XStaticFixedValue.nullMethodArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullMethodArray;
        }
        Method[] result = null;
        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length
                    && (null == returnClass || returnClass == element.getReturnType())
                    && (null == methodName || methodName.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    if (null == result) {
                        result = new Method[list.length];
                    }
                    result[index] = element;
                    index++;
                }
            }
        }
        if (null != result) {
            return Arrays.copyOf(result, index);
        } else {
            return XStaticFixedValue.nullMethodArray;
        }
    }










    /******* Class == Class time-consuming ********/
    /******* Copy matchMethod remove returnClass validation ********/
    /**
     * @param paramTypeClass nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, String methodName,
            boolean nullObjectCanCastToClass, Class... paramClassArr) {
        if (null == list) {
            return null;
        }
        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length
                    && (null == methodName || methodName.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * @param paramTypeClass nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, String methodName,
            boolean nullObjectCanCastToClass, Class... paramClassArr) {
        if (null == list) {
            return XStaticFixedValue.nullMethodArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullMethodArray;
        }
        Method[] result = null;
        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length
                    && (null == methodName || methodName.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    if (null == result) {
                        result = new Method[list.length];
                    }
                    result[index] = element;
                    index++;
                }
            }
        }
        if (null != result) {
            return Arrays.copyOf(result, index);
        } else {
            return XStaticFixedValue.nullMethodArray;
        }
    }

    /**
     * @param paramTypeClass nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, String methodName,
            boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        if (null == list) {
            return null;
        }
        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length
                    && (null == methodName || methodName.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * @param paramTypeClass nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, String methodName,
            boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        if (null == list) {
            return XStaticFixedValue.nullMethodArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullMethodArray;
        }
        Method[] result = null;
        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
                    : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length
                    && (null == methodName || methodName.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    if (null == result) {
                        result = new Method[list.length];
                    }
                    result[index] = element;
                    index++;
                }
            }
        }
        if (null != result) {
            return Arrays.copyOf(result, index);
        } else {
            return XStaticFixedValue.nullMethodArray;
        }
    }

    /******* delete ReturnType ********/























    public static final XReflectMatcher defaultInstance = new XReflectMatcher(XReflectCacheFast.defaultInstance);

    public XReflectMatcher() {
        this(null);
    }

    public XReflectMatcher(XReflectCache cache) {
        this.rc = null == cache ? new XReflectCacheFast() : cache;
    }

    /**
     * fast cache
     */
    protected XReflectCache rc;

    public static RuntimeException newThrowNoSuchMatchException(Member[] list, Class returnClass, String name,
            Object... paramInstanceArr) {
        StringBuilder strbuf = new StringBuilder();
        strbuf.append("cannot found: ").append(null == returnClass ? "" : XClass.toAbsCanonicalName(returnClass)).append(" ")
                .append(name).append(null == list ? "" : XClass.joinParamJavaClassCanonicalName(paramInstanceArr))
                .append("    ");
        strbuf.append("matching list: ");
        if (null == list) {
            strbuf.append((String) null);
        } else {
            XStringJoiner xsj = new XStringJoiner(",    ");
            for (Member constructor22 : list) {
                xsj.add(null == constructor22 ? "null" : constructor22.toString());
            }
            strbuf.append(xsj);
        }
        return new RuntimeException(strbuf.toString());
    }

    public static RuntimeException newThrowNoSuchMatchException(Member[] list, Class returnClass, String name,
            Class... paramClassArr) {
        StringBuilder strbuf = new StringBuilder();
        strbuf.append("cannot found: ").append(null == returnClass ? "" : XClass.toAbsCanonicalName(returnClass)).append(" ")
                .append(name).append(null == list ? "" : XClass.joinParamJavaClassCanonicalName(paramClassArr))
                .append("    ");
        strbuf.append("matching list: ");
        if (null == list) {
            strbuf.append((String) null);
        } else {
            XStringJoiner xsj = new XStringJoiner(",    ");
            for (Member constructor22 : list) {
                xsj.add(null == constructor22 ? "null" : constructor22.toString());
            }
            strbuf.append(xsj);
        }
        return new RuntimeException(strbuf.toString());
    }

    public XReflectCache getCacher() {
        return this.rc;
    }

    public XReflectMatcher clearCacher() {
        this.rc.clear();
        return this;
    }

    /** extension option */
    /**
     * @see @return
     */
    public XReflectPoint newPoint(Class<?> cls) {
        return new XReflectPoint(new XReflectPoint.StaticOption(cls), this);
    }

    /**
     * @see @return
     */
    public XReflectPoint newPoint(Object obj) {
        return new XReflectPoint(obj, this);
    }



    public Constructor getConstructor(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        ConstructorList gets = this.rc.getConstructors0(cls);
        if (!XObjects.isEmpty(gets)) {
            Constructor match = matchConstructor(gets.list(), gets.parameterTypes(), true,
                    null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        ConstructorList throwList = this.rc.getConstructors0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, "", paramInstanceArr);
    }
    public Constructor getConstructor(Class<?> cls, Class... paramClass) throws RuntimeException {
        ConstructorList gets = this.rc.getConstructors0(cls);
        if (!XObjects.isEmpty(gets)) {
            Constructor match = matchConstructor(gets.list(), gets.parameterTypes(), true,
                    null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        ConstructorList throwList = this.rc.getConstructors0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, "", paramClass);
    }



    public Constructor[] getConstructors(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        ConstructorList gets = this.rc.getConstructors0(cls);
        if (!XObjects.isEmpty(gets)) {
            Constructor[] match = matchConstructors(gets.list(), gets.parameterTypes(), true,
                    null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        ConstructorList throwList = this.rc.getConstructors0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, "", paramInstanceArr);
    }
    public Constructor[] getConstructors(Class<?> cls, Class... paramClass) throws RuntimeException {
        ConstructorList gets = this.rc.getConstructors0(cls);
        if (!XObjects.isEmpty(gets)) {
            Constructor[] match = matchConstructors(gets.list(), gets.parameterTypes(), true,
                    null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        ConstructorList throwList = this.rc.getConstructors0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, "", paramClass);
    }



    public Field getField(Class cls, String name) throws RuntimeException {
        Field match = this.rc.getField(cls, name);
        if (!XObjects.isEmpty(match)) {
            return XReflectAccessible.setAccessibleTrueOne(match);
        }
        throw newThrowNoSuchMatchException(this.rc.getFields0(cls).list(), null, name, (Class[]) null);
    }



    public Method getMethod(Class cls, String methodName, Object... paramInstanceArr) throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), null, true,
                    null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, methodName,
                paramInstanceArr);
    }
    public Method getMethod(Class cls, Class returnClass, String methodName, Object... paramInstanceArr)
            throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), returnClass, null, true, paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), returnClass, methodName,
                null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
    }



    
    public Method getMethod(Class cls, String methodName, Class... paramClass) throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), null, true,
                    null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, methodName, paramClass);
    }
    public Method getMethod(Class cls, Class returnClass, String methodName, Class... paramClass)
            throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), returnClass, null, true,
                    null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), returnClass, methodName,
                paramClass);
    }



    public Method[] getMethods(Class cls, String methodName, Object... paramInstanceArr) throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), null, true,
                    null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, methodName,
                paramInstanceArr);
    }
    public Method[] getMethods(Class cls, Class returnClass, String methodName, Object... paramInstanceArr)
            throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), returnClass, null, true,
                    null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), returnClass, methodName,
                paramInstanceArr);
    }



    public Method[] getMethods(Class cls, String methodName, Class... paramClass) throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), null, true,
                    null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, methodName, paramClass);
    }
    public Method[] getMethods(Class cls, Class returnClass, String methodName, Class... paramClass)
            throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), returnClass, null, true,
                    null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), returnClass, methodName,
                paramClass);
    }












    public Object newInstance(Class cls, Object... paramInstanceArr) throws InvocationTargetException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        Constructor con = this.getConstructor(cls, paramInstanceArr);
        return con.newInstance(paramInstanceArr);
    }

    public Object newInstance(Object instance, Object... paramInstanceArr) throws InvocationTargetException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        Constructor con = this.getConstructor(instance.getClass(), paramInstanceArr);
        return con.newInstance(paramInstanceArr);
    }

    public Object getFieldValue(Class instanceCls, Object instance, String name)
            throws IllegalArgumentException, IllegalAccessException, RuntimeException {
        Field field = this.getField(instanceCls, name);
        return field.get(instance);
    }

    public Object setFieldValue(Class instanceCls, Object instance, String name, Object newValue)
            throws IllegalArgumentException, IllegalAccessException, RuntimeException {
        Field field = this.getField(instanceCls, name);
        field.set(instance, newValue);
        return newValue;
    }

    public Object getStaticFieldValue(Class instanceCls, String name)
            throws IllegalArgumentException, IllegalAccessException, RuntimeException {
        return this.getFieldValue(instanceCls, null, name);
    }

    public Object setStaticFieldValue(Class instanceCls, String name, Object newValue)
            throws IllegalArgumentException, IllegalAccessException, RuntimeException {
        return this.setFieldValue(instanceCls, null, name, newValue);
    }

    public Object execMethod(Class instanceCls, Object instance, String method, Object... paramInstanceArr)
            throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        Method con = this.getMethod(instanceCls, method, paramInstanceArr);
        return con.invoke(instance, paramInstanceArr);
    }

    public Object execMethod(Object instance, String method, Object... paramInstanceArr)
            throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        return execMethod(instance.getClass(), instance, method, paramInstanceArr);
    }

    public Object execStaticMethod(Class cls, String method, Object... paramInstanceArr)
            throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        return execMethod(cls, null, method, paramInstanceArr);
    }

    public Object execStaticMethod(Object instance, String method, Object... paramInstanceArr)
            throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        return execMethod(instance.getClass(), null, method, paramInstanceArr);
    }

}
