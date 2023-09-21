package top.fols.atri.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Classz;
import top.fols.box.lang.Classx;

import static top.fols.atri.reflect.ReflectCache.*;
import static top.fols.atri.lang.Finals.*;
@SuppressWarnings({"rawtypes", "ForLoopReplaceableByForEach"})
public class ReflectMatcher<T extends ReflectCache> {
	T cache;
	public ReflectMatcher(T cache) {
		this.cache = cache;
	}




    /**
     * @param returnClass Nullable
     * @param name Nullable
     */
    public Field matchField(ReflectCache.FieldList list, Class returnClass, String name) {
        return matchField(list.list(), returnClass, name);
    }
    public Field matchField(Field[] list, Class returnClass, String name) {
        int forCount = list.length;
        for (int i = 0; i < forCount; i++) {
            Field element = list[i];
            if ((null == returnClass || returnClass == element.getType()) &&
                (null == name || name.equals(element.getName()))) {
                return element;
            }
        }
        return null;
    }



    public Constructor matchConstructorArgumentsTypes(ReflectCache.ConstructorList list, Class... paramClassArr) {
        return matchConstructorArgumentsTypes(list.list(), list.parameterTypes(), true, paramClassArr);
    }
    public Constructor matchConstructorArgumentsTypes(Constructor[] list, Class[][] listParameterTypes,
                                                      boolean nullObjectCanCastToClass, Class... paramClassArr) {
        int forCount = list.length;
        for (int i = 0; i < forCount; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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

    public Constructor[] matchConstructorsArgumentsTypes(ReflectCache.ConstructorList list, Class... paramClassAr) {
        return matchConstructorsArgumentsTypes(list.list(), list.parameterTypes(), true, paramClassAr);
    }
    public Constructor[] matchConstructorsArgumentsTypes(Constructor[] list, Class[][] listParameterTypes,
                                                         boolean nullObjectCanCastToClass, Class... paramClassArr) {
        int forCount = list.length;
        if (forCount == 0) {
            return null;
        }
        Constructor[] result = null;
        int index = 0;
        for (int i = 0; i < forCount; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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
            return null;
        }
    }





    public Method matchMethodArgumentsTypes(ReflectCache.MethodList list, Class returnClass, String name, Class... paramClassArr) {
        return matchMethodArgumentsTypes(list.list(), list.parameterTypes(), returnClass, name, true, paramClassArr);
    }
    /**
     * @param returnClass        	Nullable
     * @param name         			Nullable
     */
    public Method matchMethodArgumentsTypes(Method[] list, Class[][] listParameterTypes, Class returnClass, String name,
                                            boolean nullObjectCanCastToClass, Class... paramClassArr) {
        int forCount = list.length;
        for (int i = 0; i < forCount; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length
                    && (null == returnClass || returnClass == element.getReturnType())
                    && (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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


    public Method[] matchMethodsArgumentsTypes(ReflectCache.MethodList list, Class returnClass, String name, Class... paramClassArr) {
        return matchMethodsArgumentsTypes(list.list(), list.parameterTypes(), returnClass, name, true, paramClassArr);
    }
    /**
     * @param returnClass        	Nullable
     * @param name         			Nullable
     */
    public Method[] matchMethodsArgumentsTypes(Method[] list, Class[][] listParameterTypes, Class returnClass, String name,
                                               boolean nullObjectCanCastToClass, Class... paramClassArr) {
        int forCount = list.length;
        if (forCount == 0) {
            return null;
        }
        Method[] result = null;
        int index = 0;
        for (int i = 0; i < forCount; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length
                    && (null == returnClass || returnClass == element.getReturnType())
                    && (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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
            return null;
        }
    }









    /* matcher object type param */




    public Constructor matchConstructorArguments(ReflectCache.ConstructorList list, Object... paramInstanceArr) {
        return matchConstructorArguments(list.list(), list.parameterTypes(), true, paramInstanceArr);
    }
    public Constructor matchConstructorArguments(Constructor[] list, Class[][] listParameterTypes,
                                                 boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        int forCount = list.length;
        for (int i = 0; i < forCount; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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

    public Constructor[] matchConstructorsArguments(ReflectCache.ConstructorList list, Object... paramInstanceArr) {
        return matchConstructorsArguments(list.list(), list.parameterTypes(), true, paramInstanceArr);
    }
    public Constructor[] matchConstructorsArguments(Constructor[] list, Class[][] listParameterTypes,
                                                    boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        int forCount = list.length;
        if (forCount == 0) {
            return null;
        }
        Constructor[] result = null;
        int index = 0;
        for (int i = 0; i < forCount; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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
            return null;
        }
    }


    public Method matchMethodArguments(ReflectCache.MethodList list, Class returnClass, String name, Object... paramInstanceArr) {
        return matchMethodArguments(list.list(), list.parameterTypes(), returnClass, name, true, paramInstanceArr);
    }
    /**
     * @param returnClass        	Nullable
     * @param name         			Nullable
     */
    public Method matchMethodArguments(Method[] list, Class[][] listParameterTypes, Class returnClass, String name,
                                       boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        int forCount = list.length;
        for (int i = 0; i < forCount; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length
                    && (null == returnClass || returnClass == element.getReturnType())
                    && (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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


    public Method[] matchMethodsArguments(ReflectCache.MethodList list, Class returnClass, String name, Object... paramInstanceArr) {
        return matchMethodsArguments(list.list(), list.parameterTypes(), returnClass, name, true, paramInstanceArr);
    }
    /**
     * @param returnClass        	Nullable
     * @param name         			Nullable
     */
    public Method[] matchMethodsArguments(Method[] list, Class[][] listParameterTypes, Class returnClass,
                                          String name, boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        int forCount = list.length;
        if (forCount == 0) {
            return null;
        }
        Method[] result = null;
        int index = 0;
        for (int i = 0; i < forCount; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length
                    && (null == returnClass || returnClass == element.getReturnType())
                    && (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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
            return null;
        }
    }





    public String buildNoSuchClasses(Class cls, String name) {
        ReflectCache.ClassesList throwList = cacher().getClassesList(cls);
        return buildNoSuchMatch(cls, null == throwList?null:throwList.list(), null, name, (Class[]) null);
    }

    public String buildNoSuchField(Class cls, Class type, String name) {
        ReflectCache.FieldList list = cacher().getFieldList(cls);
        return buildNoSuchMatch(cls, null == list?null:list.list(), type, name, (Class[]) null);
    }

    public String buildNoSuchConstructor(Class<?> cls, Object... paramInstanceArr) {
        ReflectCache.ConstructorList list = cacher().getConstructorList(cls);
        return buildNoSuchMatch(cls, null == list?null:list.list(), null, "", null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr);
    }
    public String buildNoSuchConstructor(Class<?> cls, Class... paramClass) {
        ReflectCache.ConstructorList list = cacher().getConstructorList(cls);
        return buildNoSuchMatch(cls, null == list?null:list.list(), null, "", null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
    }

    public String buildNoSuchMethod(Class cls, Class returnClass, String name, Object... paramInstanceArr) {
        ReflectCache.MethodList list = cacher().getMethodList(cls);
        return buildNoSuchMatch(cls, null == list?null:list.list(), returnClass, name, null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr);
    }
    public String buildNoSuchMethod(Class cls, Class returnClass, String name, Class... paramClass) {
        ReflectCache.MethodList list = cacher().getMethodList(cls);
        return buildNoSuchMatch(cls, null == list?null:list.list(), returnClass, name, null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
    }

    public static String buildNoSuchMatch(Class inClass, Object[] inClassElementList,
                                          Class returnClass, String name,
                                          Object... paramInstanceArr) {
        StringBuilder builder = new StringBuilder();
        String s = null == returnClass ? "" : Classx.getClassGetNameToCanonicalName(returnClass);
        builder.append("cannot found: ").append(s);
        if (null == paramInstanceArr) {
            builder.append(s.length() > 0 ? " " : "").append(name);
        } else {
            builder.append(s.length() > 0 ? " " : "").append(name).append(null == inClassElementList ? "" : Classx.joinParamJavaClassCanonicalName(paramInstanceArr));
        }
        builder.append(" in ");
        if (null != inClass) {
            builder.append(Classx.getClassGetNameToCanonicalName(inClass)).append(" ");
        }
        if (null == inClassElementList) {
            builder.append("[]");
        } else {
            builder.append(Arrayz.toString(inClassElementList));
        }
        return builder.toString();
    }

    public static String buildNoSuchMatch(Class inClass, Object[] inClassElementList,
                                          Class returnClass, String name,
                                          Class... paramClassArr) {
        StringBuilder builder = new StringBuilder();
        String s = null == returnClass ? "" : Classx.getClassGetNameToCanonicalName(returnClass);
        builder.append("cannot found: ").append(s);
        if (null == paramClassArr) {
            builder.append(s.length() > 0 ? " " : "").append(name);
        } else {
            builder.append(s.length() > 0 ? " " : "").append(name).append(null == inClassElementList ? "" : Classx.joinParamJavaClassCanonicalName(paramClassArr));
        }
        builder.append(" in ");
        if (null != inClass) {
            builder.append(Classx.getClassGetNameToCanonicalName(inClass)).append(" ");
        }
        if (null == inClassElementList) {
            builder.append("[]");
        } else {
            builder.append(Arrayz.toString(inClassElementList));
        }
        return builder.toString();
    }




    //****************************
	
	
	public T 	                cacher() { return this.cache; }
    public ReflectMatcher<T>    release() {
        this.cacher().release();
        return this;
    }








    /**
     * @param cls Nullable
     * @param name Nullable
     */
    public Class requireClasses(Class cls, String name) throws RuntimeException {
        Class match = classes(cls, name);
        if (null != match) {
            return  match;
        }
        throw new RuntimeException(buildNoSuchClasses(cls, name));
    }
    public Class classes(Class cls, String name) throws RuntimeException {
        ReflectCache cache = this.cacher();
        return cache.getClasses(cls, name);
    }



    /**
	 * @param type Nullable
	 * @param name Nullable
	 */
	public Field requireField(Class cls, Class type, String name) throws RuntimeException {
        Field match = field(cls, type, name);
        if (null != match) {
            return  match;
        }
        throw new RuntimeException(buildNoSuchField(cls, type, name));
    }
    public Field field(Class cls, Class type, String name) throws RuntimeException {
        ReflectCache.FieldList fieldList = this.cacher().getFieldList(cls, name);
        if (null != fieldList) {
            return matchField(fieldList.list(), type, name);
        }
        return null;
    }





	public Constructor requireConstructor(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        Constructor match = constructor(cls, paramInstanceArr);
        if (null != match) {
            return match;
        }
        throw new RuntimeException(buildNoSuchConstructor(cls, paramInstanceArr));
    }
    public Constructor constructor(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        ConstructorList constructorList = this.cacher().getConstructorList(cls);
        if (null != constructorList) {
            return matchConstructorArguments(constructorList.list(), constructorList.parameterTypes(), true,
                   null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr);
        }
        return null;
    }

    public Constructor requireConstructor(Class<?> cls, Class... paramClass) throws RuntimeException {
        Constructor match = constructor(cls, paramClass);
        if (null != match) {
            return match;
        }
        throw new RuntimeException(buildNoSuchConstructor(cls, paramClass));
    }
    public Constructor constructor(Class<?> cls, Class... paramClass) throws RuntimeException {
        ConstructorList constructorList = this.cacher().getConstructorList(cls);
        if (null != constructorList) {
            return matchConstructorArgumentsTypes(constructorList.list(), constructorList.parameterTypes(), true,
                   null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
        }
        return null;
    }






    public Constructor[] requireConstructors(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        Constructor[] match = constructors(cls, paramInstanceArr);
        if (null != match && match.length > 0) {
            return  match;
        }
        throw new RuntimeException(buildNoSuchConstructor(cls, paramInstanceArr));
    }
    public Constructor[] constructors(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        ReflectCache.ConstructorList constructorList = this.cacher().getConstructorList(cls);
        if (null != constructorList) {
            return matchConstructorsArguments(constructorList.list(), constructorList.parameterTypes(), true,
                   null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr);
        }
        return null;
    }


    public Constructor[] requireConstructors(Class<?> cls, Class... paramClass) throws RuntimeException {
        Constructor[] match = constructors(cls, paramClass);
        if (null != match && match.length > 0) {
            return  match;
        }
        throw new RuntimeException(buildNoSuchConstructor(cls, paramClass));
    }
    public Constructor[] constructors(Class<?> cls, Class... paramClass) throws RuntimeException {
        ReflectCache.ConstructorList constructorList = this.cacher().getConstructorList(cls);
        if (null != constructorList) {
            return  matchConstructorsArgumentsTypes(constructorList.list(), constructorList.parameterTypes(), true,
                    null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
        }
        return null;
    }








	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method requireMethod(Class cls, Class returnClass, String name, Object... paramInstanceArr)
	    throws RuntimeException {
        Method match = method(cls, returnClass, name, paramInstanceArr);
        if (null != match) {
            return match;
        }
        throw new RuntimeException(buildNoSuchMethod(cls, returnClass, name, paramInstanceArr));
    }
    public Method method(Class cls, Class returnClass, String name, Object... paramInstanceArr)
            throws RuntimeException {
        ReflectCache.MethodList methodList = this.cacher().getMethodList(cls, name);
        if (null != methodList) {
            return  matchMethodArguments(methodList.list(), methodList.parameterTypes(), returnClass, null, true, paramInstanceArr);
        }
        return null;
    }


	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method requireMethod(Class cls, Class returnClass, String name, Class... paramClass)
	        throws RuntimeException {
        Method match = method(cls, returnClass, name, paramClass);
        if (null != match) {
            return match;
        }
        throw new RuntimeException(buildNoSuchMethod(cls, returnClass, name, paramClass));
    }
    public Method method(Class cls, Class returnClass, String name, Class... paramClass)
            throws RuntimeException {
        ReflectCache.MethodList methodList = this.cacher().getMethodList(cls, name);
        if (null != methodList) {
            return  matchMethodArgumentsTypes(methodList.list(), methodList.parameterTypes(), returnClass, null, true,
                    null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
        }
        return null;
    }


	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method[] requireMethods(Class cls, Class returnClass, String name, Object... paramInstanceArr)
            throws RuntimeException {
        Method[] match = methods(cls, returnClass, name, paramInstanceArr);
        if (null != match && match.length > 0) {
            return  match;
        }
        throw new RuntimeException(buildNoSuchMethod(cls, returnClass, name, paramInstanceArr));
    }
    public Method[] methods(Class cls, Class returnClass, String name, Object... paramInstanceArr)
            throws RuntimeException {
        MethodList methodList = this.cacher().getMethodList(cls, name);
        if (null != methodList) {
            return  matchMethodsArguments(methodList.list(), methodList.parameterTypes(), returnClass, null, true,
                    null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr);
        }
        return null;
    }
	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method[] requireMethods(Class cls, Class returnClass, String name, Class... paramClass)
	        throws RuntimeException {
        Method[] match = methods(cls, returnClass, name, paramClass);
        if (null != match && match.length > 0) {
            return  match;
        }
        throw new RuntimeException(buildNoSuchMethod(cls, returnClass, name, paramClass));
    }
    public Method[] methods(Class cls, Class returnClass, String name, Class... paramClass)
            throws RuntimeException {
        MethodList methodList = this.cacher().getMethodList(cls, name);
        if (null != methodList) {
            return  matchMethodsArgumentsTypes(methodList.list(), methodList.parameterTypes(), returnClass, null, true,
                    null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
        }
        return null;
    }











    //****************************

    public boolean isDefault() { return this == DEFAULT; }
	public static final ReflectMatcher<ReflectCache> DEFAULT = new ReflectMatcher<ReflectCache>(ReflectCache.DEFAULT){};
}
