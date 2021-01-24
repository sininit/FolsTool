package top.fols.atri.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import top.fols.box.lang.XClass;

import static top.fols.atri.reflect.ReflectCache.*;
import static top.fols.atri.lang.Finals.*;
public class ReflectMatcher {
	ReflectCache cache;
	public ReflectMatcher(ReflectCache cache) {
		this.cache = cache;
	}


	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
	public static Field matchField(Field[] list, Class returnClass, String name) {
        if (null == list) { return null; }
        for (int i = 0; i < list.length; i++) {
            Field element = list[i];
            if ((null == returnClass || returnClass == element.getType())
				&& (null == name || name.equals(element.getName()))) {
				return element;
			}
        }
        return null;
    }

	/**
	 * @param listParameterTypes Nullable
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
	 * @param listParameterTypes Nullable
	 */
	public static Constructor[] matchConstructors(Constructor[] list, Class[][] listParameterTypes,
												  boolean nullObjectCanCastToClass, Class... paramClassArr) {
        if (null == list) {
            return EMPTY_CONSTRUCTOR_ARRAY;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return EMPTY_CONSTRUCTOR_ARRAY;
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
            return EMPTY_CONSTRUCTOR_ARRAY;
        }
    }





	/**
     * @param returnClass        	Nullable
     * @param listParameterTypes 	Nullable
     * @param name         			Nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, Class returnClass, String name,
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
				&& (null == name || name.equals(element.getName()))) {
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
     * @param returnClass        	Nullable
     * @param listParameterTypes 	Nullable
     * @param name         			Nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, 
										boolean nullObjectCanCastToClass, Class... paramClassArr) {
        if (null == list) {
            return EMPTY_METHOD_ARRAY;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return EMPTY_METHOD_ARRAY;
        }
        Method[] result = null;
        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
				: listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length
				&& (null == returnClass || returnClass == element.getReturnType())
				&& (null == name || name.equals(element.getName()))) {
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
            return EMPTY_METHOD_ARRAY;
        }
    }









	/**
     * matcher object type param
     */

    /**
	 * @param listParameterTypes Nullable
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
	 * @param listParameterTypes Nullable
	 */
    public static Constructor[] matchConstructors(Constructor[] list, Class[][] listParameterTypes,
												  boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        if (null == list) {
            return EMPTY_CONSTRUCTOR_ARRAY;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return EMPTY_CONSTRUCTOR_ARRAY;
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
            return EMPTY_CONSTRUCTOR_ARRAY;
        }
    }

    /**
     * @param returnClass        	Nullable
     * @param listParameterTypes 	Nullable
     * @param name         			Nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, Class returnClass, String name,
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
				&& (null == name || name.equals(element.getName()))) {
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
     * @param returnClass        	Nullable
     * @param listParameterTypes 	Nullable
     * @param name         			Nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, Class returnClass,
										String name, boolean nullObjectCanCastToClass, Object... paramInstanceArr) {
        if (null == list) {
            return EMPTY_METHOD_ARRAY;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return EMPTY_METHOD_ARRAY;
        }
        Method[] result = null;
        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes()
				: listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length
				&& (null == returnClass || returnClass == element.getReturnType())
				&& (null == name || name.equals(element.getName()))) {
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
            return EMPTY_METHOD_ARRAY;
        }
    }









	public static String throwNoSuchMatch(Member[] finds
										  , Class returnClass, String name,
										  Object... paramInstanceArr) {
        StringBuilder strbuf = new StringBuilder();
        strbuf.append("cannot found: ").append(null == returnClass ? "" : XClass.toAbsCanonicalName(returnClass)).append(" ")
			.append(name).append(null == finds ? "" : XClass.joinParamJavaClassCanonicalName(paramInstanceArr))
			.append("    ");
        strbuf.append("matching list: ");
        if (null == finds) {
            strbuf.append((String) null);
        } else {
			StringBuilder xsj = new StringBuilder();
            String separator = ",    ";
            for (Member constructor22 : finds) {
                xsj.append(null == constructor22 ? "null" : constructor22.toString()).append(separator);
            }
			String xsjstr = xsj.toString();
			int offset = 0; int end = (xsjstr.endsWith(separator) ?xsjstr.length() - separator.length(): xsjstr.length());
            strbuf.append(xsjstr, offset, end);
        }
        return strbuf.toString();
    }

    public static String throwNoSuchMatch(Member[] finds
										  , Class returnClass, String name,
										  Class... paramClassArr) {
        StringBuilder strbuf = new StringBuilder();
        strbuf.append("cannot found: ").append(null == returnClass ? "" : XClass.toAbsCanonicalName(returnClass)).append(" ")
			.append(name).append(null == finds ? "" : XClass.joinParamJavaClassCanonicalName(paramClassArr))
			.append("    ");
        strbuf.append("matching list: ");
        if (null == finds) {
            strbuf.append((String) null);
        } else {
            StringBuilder xsj = new StringBuilder();
            String separator = ",    ";
            for (Member constructor22 : finds) {
                xsj.append(null == constructor22 ? "null" : constructor22.toString()).append(separator);
            }
			String xsjstr = xsj.toString();
			int offset = 0; int end = (xsjstr.endsWith(separator) ?xsjstr.length() - separator.length(): xsjstr.length());
            strbuf.append(xsjstr, offset, end);
        }
        return strbuf.toString();
    }
	
	
	
	
	public ReflectCache 	cacher() { return this.cache; }
    public ReflectMatcher 	cacherRelease() {
        this.cacher().release();
        return this;
    }





	
	
	
	
	
	
	
	
	

	/**
	 * @param type Nullable
	 * @param name Nullable
	 */
	public Field getField(Class cls, Class type, String name) throws RuntimeException {
        ReflectCache cache = this.cacher();
		ReflectCache.FieldList gets = cache.getFieldsList(cls, name);
        if (null != gets && gets.list.length != 0) {
			Field match = matchField(gets.list(), type, name);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getFieldsList(cls).list(), null, name, (Class[]) null));
    }



	public Constructor getConstructor(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        ReflectCache cache = this.cacher();
		ConstructorList gets = cache.getConstructorsList(cls);
        if (null != gets && gets.list.length != 0) {
            Constructor match = matchConstructor(gets.list(), gets.parameterTypes(), true,
												 null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getConstructorsList(cls).list(), null, "", paramInstanceArr));
    }
    public Constructor getConstructor(Class<?> cls, Class... paramClass) throws RuntimeException {
        ReflectCache cache = this.cacher();
		ConstructorList gets = cache.getConstructorsList(cls);
        if (null != gets && gets.list.length != 0) {
            Constructor match = matchConstructor(gets.list(), gets.parameterTypes(), true,
												 null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getConstructorsList(cls).list(), null, "", paramClass));
    }



    public Constructor[] getConstructors(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        ReflectCache cache = this.cacher();
		ReflectCache.ConstructorList gets = cache.getConstructorsList(cls);
        if (null != gets && gets.list.length != 0) {
            Constructor[] match = matchConstructors(gets.list(), gets.parameterTypes(), true,
													null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getConstructorsList(cls).list(), null, "", paramInstanceArr));
    }
    public Constructor[] getConstructors(Class<?> cls, Class... paramClass) throws RuntimeException {
        ReflectCache cache = this.cacher();
		ReflectCache.ConstructorList gets = cache.getConstructorsList(cls);
        if (null != gets && gets.list.length != 0) {
            Constructor[] match = matchConstructors(gets.list(), gets.parameterTypes(), true,
													null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getConstructorsList(cls).list(), null, "", paramClass));
    }








	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method getMethod(Class cls, Class returnClass, String name, Object... paramInstanceArr)
	throws RuntimeException {
        ReflectCache cache = this.cacher();
		ReflectCache.MethodList gets = cache.getMethodsList(cls, name);
        if (null != gets && gets.list.length != 0) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), returnClass, null, true, paramInstanceArr);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getMethodsList(cls).list(), returnClass, name,
													null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr));
    }
	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method getMethod(Class cls, Class returnClass, String name, Class... paramClass)
	throws RuntimeException {
        ReflectCache cache = this.cacher();
		ReflectCache.MethodList gets = cache.getMethodsList(cls, name);
        if (null != gets && gets.list.length != 0) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), returnClass, null, true,
									   null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getMethodsList(cls).list(), returnClass, name,
													null == paramClass ? EMPTY_CLASS_ARRAY : paramClass));
    }



	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method[] getMethods(Class cls, Class returnClass, String name, Object... paramInstanceArr)
	throws RuntimeException {
        ReflectCache cache = this.cacher();
		MethodList gets = cache.getMethodsList(cls, name);
        if (null != gets && gets.list.length != 0) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), returnClass, null, true,
										  null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getMethodsList(cls).list(), returnClass, name,
													paramInstanceArr));
    }
	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method[] getMethods(Class cls, Class returnClass, String name, Class... paramClass)
	throws RuntimeException {
        ReflectCache cache = this.cacher();
		MethodList gets = cache.getMethodsList(cls, name);
        if (null != gets && gets.list.length != 0) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), returnClass, null, true,
										  null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getMethodsList(cls).list(), returnClass, name,
													paramClass));
    }
	
	
	
	
	
	public Object invoke(Class  cls, String name, Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException { return this.invoke(cls, null, name, args); }
	public Object invoke(Object object, String name, Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException { if (null == object) { throw new NullPointerException("object"); } return this.invoke(object.getClass(), object, null, name, args); }
	public Object invoke(Class cls, Object object, Class returnClass, String name, Object... args) throws NullPointerException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method invoke = this.getMethod(null == cls ?cls = object.getClass(): cls, returnClass, name, args);
		return invoke.invoke(object, args);
	}

	public Object get(Class cls, Class returnClass, String name) throws IllegalArgumentException, IllegalAccessException { return this.get(cls, null, returnClass, name); }
	public Object get(Class cls, String name) throws IllegalArgumentException, IllegalAccessException { return this.get(cls, null, null, name); }
	public Object get(Object object, Class returnClass, String name) throws IllegalArgumentException, IllegalAccessException { if (null == object) { throw new NullPointerException("object"); } return this.get(null, object, returnClass, name); }
	public Object get(Object object, String name) throws IllegalArgumentException, IllegalAccessException { if (null == object) { throw new NullPointerException("object"); } return this.get(null, object, null, name); }
	public Object get(Class cls, Object object, Class returnClass, String name) throws IllegalArgumentException, IllegalAccessException {
		Field invoke = this.getField(null == cls ?cls = object.getClass(): cls, returnClass, name);
		return invoke.get(object);
	}

	public void set(Class cls, Class returnClass, String name, Object value) throws IllegalArgumentException, IllegalAccessException { this.set(cls, null, returnClass, name, value); }
	public void set(Class cls, String name, Object value) throws IllegalArgumentException, IllegalAccessException { this.set(cls, null, null, name, value); }
	public void set(Object object, Class returnClass, String name, Object value) throws IllegalArgumentException, IllegalAccessException { if (null == object) { throw new NullPointerException("object"); } this.set(null, object, returnClass, name, value); }
	public void set(Object object, String name, Object value) throws IllegalArgumentException, IllegalAccessException { if (null == object) { throw new NullPointerException("object"); } this.set(null, object, null, name, value); }
	public void set(Class cls, Object object, Class returnClass, String name, Object value) throws IllegalArgumentException, IllegalAccessException {
		Field invoke = this.getField(null == cls ?cls = object.getClass(): cls, returnClass, name);
		invoke.set(object, value);
	} 


	public Object newInstance(Object object, Object... args) throws IllegalArgumentException, InstantiationException, NullPointerException, IllegalAccessException, InvocationTargetException  { if (null == object) { throw new NullPointerException("object"); } return this.newInstance(object.getClass(), args); }
	public Object newInstance(Class cls, Object... args) throws NullPointerException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		if (null == cls) { throw new NullPointerException("class"); }
		Constructor invoke = this.getConstructor(cls, args);
		return invoke.newInstance(args);
	}


	public ReflectPoint point(Object object) 	{ return ReflectPoint.wrapObjectOption(this, object); }
	public ReflectPoint point(Class object) 	{ return ReflectPoint.wrapStaticOption(this, object); }
	
	
	public boolean isDefault(){ return this == DEFAULT_INSTANCE; }
	public static final ReflectMatcher DEFAULT_INSTANCE = new ReflectMatcher(ReflectCache.DEFAULT_INSTANCE){
	};
}
