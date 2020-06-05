package top.fols.box.lang.reflect.safety;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import top.fols.box.lang.XClass;
import top.fols.box.statics.XStaticFixedValue;

public class XReflect {



	/**
     * 
     */

	public static Constructor getConstructor(Class<?> cls, Class... paramClass) {
		try {
			return cls.getConstructor(paramClass);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	public static Field getField(Class<?> cls, String field) {
		try {
			return cls.getField(field);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	public static Method getMethod(Class<?> cls, String method, Class... paramClass) {
		try {
			return cls.getMethod(method, paramClass);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * 
	 */

    public static Class[] getClasses(Class cls) {
        if (null == cls) {
            return null;
        }
        return cls.getClasses();
    }

    public static Constructor[] getConstructors(Class cls) {
        if (null == cls) {
            return null;
        }
        return cls.getConstructors();
    }

    public static Field[] getFields(Class cls) {
        if (null == cls) {
            return null;
        }
        return cls.getFields();
    }

    public static Method[] getMethods(Class cls) {
        if (null == cls) {
            return null;
        }
        return cls.getMethods();
	}
    
    
    
    
    
    
	public static Method[] getMethodsAll(Class cls, String methodName) {
		if (null == cls) {
            return null;
        }
        List<Method> methods = new ArrayList<Method>();
		Method[] methods0 = cls.getMethods();
		for (Method m : methods0) {
			if (null == methodName || methodName.equals(m.getName())) {
				methods.add(m);
			}
		}
		Method[] newMs = new Method[methods.size()];
		methods.toArray(newMs);
		methods = null;
		return newMs;
	}


	/**
	 * 
	 */

	public static Constructor findConstructor(Class<?> Cls, Class... classs)
    throws NoSuchMethodException, SecurityException {
		return Cls.getConstructor(null == classs ? XStaticFixedValue.nullClassArray : classs);
	}

	public static Object newInstance(Constructor c, Object... object)
    throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException {
		return c.newInstance(null == object ? XStaticFixedValue.nullObjectArray : object);
	}

	public static Method findMethod(Class<?> cls, String functionName, Class... classs)
    throws NoSuchMethodException, SecurityException {
		return cls.getMethod(functionName, null == classs ? XStaticFixedValue.nullClassArray : classs);
	}

	private static Object execMethod0(Object Instance, Method method, Object... object)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
		return method.invoke(Instance, null == object ? XStaticFixedValue.nullObjectArray : object);
	}

	public static Field findField(Class cls, String name) throws NoSuchFieldException {
		return getField(cls, name);
	}

	private static Object fieldGetValue0(Field field, Object object)
    throws IllegalAccessException, IllegalArgumentException {
		return field.get(object);
	}

	private static void fieldSetValue0(Field field, Object object, Object newFieldValue)
    throws IllegalAccessException, IllegalArgumentException {
		field.set(object, newFieldValue);
	}

	/**
	 * 
	 */

	public static Constructor findConstructor(Object Cls, Class... classs)
    throws NoSuchMethodException, SecurityException {
		return findConstructor(XClass.getClass(Cls), classs);
	}

	public static Object execMethod(Object Instance, Class cls, String method, Class[] classs, Object... object)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException,
    SecurityException {
		return execMethod0(Instance, findMethod(cls, method, classs), object);
	}

	public static Object execMethod(Object Instance, String method, Class[] classs, Object... object)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException,
    SecurityException {
		return execMethod0(Instance, findMethod(XClass.getClass(Instance), method, classs), object);
	}

	public static Object execStaticMethod(Object Instance, String method, Class[] classs, Object... object)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException,
    SecurityException {
		return execMethod0(null, findMethod(XClass.getClass(Instance), method, classs), object);
	}

	public static Object execStaticMethod(Class cls, String method, Class[] classs, Object... object)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException,
    SecurityException {
		return execMethod0(null, findMethod(cls, method, classs), object);
	}

	public static Object getFieldValue(Class ClassV, Object Instance, String name)
    throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
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
    throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
		fieldSetValue0(findField(instanceCls, name), Instance, Value);
	}

	public static void setFieldValue(Object Instance, String name, Object Value)
    throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
		fieldSetValue0(findField(Instance.getClass(), name), Instance, Value);
	}

	public static void setStaticFieldValue(Class cls, String name, Object newFieldValue)
    throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
		fieldSetValue0(findField(cls, name), null, newFieldValue);
	}

	public static void setStaticFieldValue(Field field, Object newFieldValue)
    throws IllegalAccessException, IllegalArgumentException {
		fieldSetValue0(field, null, newFieldValue);
	}

	/**
	 *
	 * grow
	 * 
	 */

	// 获取变量类型
	public static Class getFieldType(Field field) {
		return field.getType();
	}

	// 获取方法返回类型
	public static Class getMethodReturnType(Method method) {
		return method.getReturnType();
	}

	// 获取方法名
	public static String getName(Member member) {
		return null == member ? null : member.getName();
	}

	// 获取变量的修饰符
	public static int getModifiers(Member field) {
		return null == field ? -1 : field.getModifiers();
	}

	// 获取修饰符
	public static String getModifiersString(int method) {
		return Modifier.toString(method);
	}

	public static String[] getNames(Class[] classArr) {
		String[] type = new String[classArr.length];
		for (int i = 0; i < classArr.length; i++) {
			type[i] = null == classArr[i] ? null : classArr[i].getName();
		}
		return type;
	}
    
    
    
    
    
    /**
     * @see XReflectAccessible.testPrivateReflect()
    */
    private static boolean testPrivateReflectMethod0() { return true; }
}
