package top.fols.box.lang.reflect.optdeclared;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ReflectPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import top.fols.box.lang.XClass;
import top.fols.box.lang.reflect.safety.XReflect;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XKeyMap;

public class XReflectAccessible {

	@SuppressWarnings("unchecked")
	public static <T extends AccessibleObject> T[] setAccessible(boolean b, T... a) {
		if (null == a) {
			return null;
		}
		for (T ai : a) {
			try {
				if (null != ai) {
					ai.setAccessible(b);
				}
			} catch (Throwable e) {
				e = null;
			}
		}
		return a;
	}

	@SuppressWarnings("unchecked")
	public static <T extends AccessibleObject> T[] setAccessibleTrue(T... a) {
		return setAccessible(true, a);
	}

	public static <T extends AccessibleObject> T setAccessibleTrueOne(T a) {
		if (null == a) {
			return null;
		}
		try {
			a.setAccessible(true);
		} catch (Throwable e) {
			e = null;
		}
		return a;
	}



	/**
     * 
     */

	public static Constructor getConstructorSetAccessible(Class cls, Class... paramClass) {
		return XReflectAccessible.setAccessibleTrueOne(getConstructor(cls, paramClass));
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
		try {
			Field declaredField = clazz.getDeclaredField(name);
			return declaredField;
		} catch (NoSuchFieldException e) {
			e = null;
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
		try {
			Method declaredMethod = clazz.getDeclaredMethod(name, array);
			return declaredMethod;
		} catch (NoSuchMethodException e) {
			e = null;
		}
		return null;
	}




	/**
	 * 
	 */
    public static Constructor[] getDeclaredConstructorsSetAccessible(Class cls) {
        return setAccessibleTrue(getDeclaredConstructors(cls));
    }

    public static Constructor[] getDeclaredConstructors(Class cls) {
        if (null == cls) {
            return null;
        }
        return cls.getDeclaredConstructors();
    }

    public static Field[] getDeclaredFieldsSetAccessible(Class cls) {
        return setAccessibleTrue(getDeclaredFields(cls));
    }

    public static Field[] getDeclaredFields(Class Cls) {
        if (null == Cls) {
            return null;
        }
        return Cls.getDeclaredFields();
    }

    public static Method[] getDeclaredMethodsSetAccessible(Class cls) {
        return setAccessibleTrue(getDeclaredMethods(cls));
    }

    public static Method[] getDeclaredMethods(Class Cls) {
        if (null == Cls) {
            return null;
        }
        return Cls.getDeclaredMethods();
    }




    public static Class[] getClassesAll(Class cls) {
        if (null == cls) {
            return null;
        }
        return getClassesAll0(cls);
    }
    private static Class[] getClassesAll0(Class cls) {
        XKeyMap<Class> classesMap = new XKeyMap<>();
        Class[] classes = cls.getDeclaredClasses();
        if (null != classes) {
            classesMap.putAll(classes);
            for (Class c : classes) {
                getClassesAll0Put(classesMap, c);
            }
        }
        classesMap.putAll(cls.getClasses());
        Class[] newMs = classesMap.keySet().toArray(new Class[classesMap.size()]);
        classesMap = null;
        return newMs;
    }
    private static void getClassesAll0Put(XKeyMap<Class> classesMap, Class cls) {
        Class[] classes = cls.getDeclaredClasses();
        if (null != classes) {
            classesMap.putAll(classes);
            for (Class cls0 : classes) {
                getClassesAll0Put(classesMap, cls0);
            }
        }
    }

	public static Constructor[] getConstructorsAllSetAccessible(Class cls) {
		return setAccessibleTrue(getConstructorsAll(cls));
	}

	public static Constructor[] getConstructorsAll(Class cls) {
        if (null == cls) {
            return null;
        }
		Constructor[] newCs = cls.getDeclaredConstructors();
		return newCs;
	}

	public static Field[] getFieldsAllSetAccessible(Class cls) {
		return setAccessibleTrue(getFieldsAll(cls));
	}

	public static Field[] getFieldsAll(Class cls) {
        if (null == cls) {
            return null;
        }
		Map<String, Field> hashmap = new HashMap<String, Field>();

		Field[] fs0 = cls.getFields();
		for (Field f : fs0) {
			if (!hashmap.containsKey(f.getName())) {
				hashmap.put(f.getName(), f);
			}
		}

		Field[] fs2 = cls.getDeclaredFields();
		for (Field f : fs2) {
			hashmap.put(f.getName(), f);
		}

		Field[] newFs = new Field[hashmap.size()];
		int ind = 0;
		for (String key : hashmap.keySet()) {
			newFs[ind++] = hashmap.get(key);
		}
		return newFs;
	}

	public static Method[] getMethodsAllSetAccessible(Class cls) {
		return setAccessibleTrue(getMethodsAll(cls));
	}

	public static Method[] getMethodsAll(Class cls) {
		return getMethodsAll0(cls, null);
	}

	public static Method[] getMethodsAllSetAccessible(Class cls, String methodName) {
		return setAccessibleTrue(getMethodsAll(cls, methodName));
	}

	public static Method[] getMethodsAll(Class cls, String methodName) {
		if (null == cls || null == methodName) {
			return null;
		}
		return getMethodsAll0(cls, methodName);
	}
	/*
	 * getMethods(),返回某个类的所有公用（public）方法包括其继承类的公用方法，当然也包括它所实现接口的方法。
	 * getDeclaredMethods(),对象表示的类或接口声明的所有方法，包括公共、保护、默认（包）访问和私有方法，但不包括继承的方法。
	 * 当然也包括它所实现接口的方法。
	 */
	private static Method[] getMethodsAll0(Class cls, String methodName) {
		List<Method> methods = new ArrayList<Method>();
		Method[] methods0 = cls.getMethods();
		for (Method m : methods0) {
			if (null == methodName || methodName.equals(m.getName())) {
				methods.add(m);
			}
		}

		Method[] methods2 = cls.getDeclaredMethods();
		for (int i = 0; i < methods2.length; i++) {
			Method m = methods2[i];
			if (Modifier.isPublic(m.getModifiers())) {
				continue;
			}

			String mName = m.getName();
			if ((null != methodName && !methodName.equals(mName))) {
				continue;
			}
			// int ik = methods.indexOf(m);
			// if (ik != -1) methods.set(ik, m);
			// else
			methods.add(m);
		}
		Method[] newMs = new Method[methods.size()];
		methods.toArray(newMs);
		methods = null;
		return newMs;
	}




	/**
	 * 
	 */

	/**
	 * Checks whether can control member accessible.
	 * 
	 * @return If can control member accessible, it return {@literal true}
	 */
	private static final ReflectPermission suppressAccessChecksReflectPermission = new ReflectPermission(
        "suppressAccessChecks");

	public static boolean canSetAccessible() {
		try {
			SecurityManager securityManager = System.getSecurityManager();
			if (null != securityManager) {
				securityManager.checkPermission(suppressAccessChecksReflectPermission);
			}
		} catch (SecurityException e) {
			return false;
		}
		return true;
	}

	public static boolean testPrivateReflect() {
		try {
			Method method = XReflectAccessible.getMethodSetAccessible(
                XReflect.class, "testPrivateReflectMethod0");
            setAccessibleTrueOne(method);
			return ((Boolean)method.invoke(null)).booleanValue() 
                && XReflectAccessible.canSetAccessible();
		} catch (Throwable e) {
			return false;
		}
	}
    
    
	/**
	 * 
	 */

	public static Field setFinalFieldAccessAble(Field f) throws IllegalAccessException, IllegalArgumentException {
		int modifier = f.getModifiers();
		if (Modifier.isStatic(modifier)) {
			Field[] fs = XReflectAccessible.setAccessibleTrue(XStaticFixedValue.Field_class.getDeclaredFields());
			Field accessFlags;
			Object value;
			int intValue;
			for (Field fi : fs) {
				if ((value = fi.get(f)) instanceof Integer && (intValue = ((Integer) value).intValue()) == modifier) {
					accessFlags = fi;
					accessFlags.set(f, intValue | Modifier.FINAL);
				}
			}
		}
		return f;
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
