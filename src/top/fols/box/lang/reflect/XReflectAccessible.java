package top.fols.box.lang.reflect;

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
import top.fols.box.statics.XStaticFixedValue;

public class XReflectAccessible {
	public static <T extends AccessibleObject>T[] setAccessible(boolean b, T...a) {
		if (null == a) return null;
		for (T ai : a) try { if (ai != null) ai.setAccessible(b); } catch (java.lang.SecurityException e) { continue; }
		return a;
	}
	public static <T extends AccessibleObject>T[] setAccessibleTrue(T... a) {
		return setAccessible(true, a);
	}
	public static <T extends AccessibleObject> T  setAccessibleTrueOne(T a) {
		if (null == a) return null;
		try { a.setAccessible(true); } catch (java.lang.SecurityException e) { e = null; }
		return a;
	}
	
	
	
	
	
	public static Constructor[] getDeclaredConstructorsSetAccessibleTrue(Class cls) {
		return setAccessibleTrue(getDeclaredConstructors(cls));
	}
	public static Constructor[] getDeclaredConstructors(Class cls) {
		if (null == cls) return null;
		return cls.getDeclaredConstructors();
	}


	public static Method[] getDeclaredMethodsSetAccessibleTrue(Class cls) {
		return setAccessibleTrue(getDeclaredMethods(cls));
	}
	public static Method[] getDeclaredMethods(Class Cls) {
		if (null == Cls) return null;
		return Cls.getDeclaredMethods();
	}


	public static Field[] getDeclaredFieldsSetAccessibleTrue(Class cls) {
		return setAccessibleTrue(getDeclaredFields(cls));
	}
	public static Field[] getDeclaredFields(Class Cls) {
		if (null == Cls) return null;
		return Cls.getDeclaredFields();
	}



	public static Constructor getConstructorSetAccessibleTrue(Class cls, Class... paramClass) {
		return setAccessibleTrueOne(getConstructor(cls, paramClass));
	}
	public static Constructor getConstructor(Class cls, Class... paramClass) {
		try { return cls.getDeclaredConstructor(paramClass); } catch (NoSuchMethodException e) { e = null; }
		try { return cls.getConstructor(paramClass); } catch (NoSuchMethodException e) { e = null; }
		return null;
	}



	public static Method getMethodSetAccessibleTrue(Class cls, String method, Class... paramClass) {
		return setAccessibleTrueOne(getMethod(cls, method, paramClass));
	}
	public static Method getMethod(Class cls, String method, Class... paramClass) {
		try { return cls.getDeclaredMethod(method, paramClass); } catch (NoSuchMethodException e) { e = null; }
		try { return cls.getMethod(method, paramClass); } catch (NoSuchMethodException e) { e = null; }
		return null;
	}



	public static Field getFieldSetAccessibleTrue(Class cls, String name) {
		return setAccessibleTrueOne(getField(cls, name));
	}
	public static Field getField(Class cls, String name) {
		try { return cls.getDeclaredField(name); } catch (NoSuchFieldException e) { e = null; }
		try { return cls.getField(name); } catch (NoSuchFieldException e) { e = null; }
		return null;
	}
	
	
	public static Constructor[] getConstructorsAllSetAccessibleTrue(Class cls) {
		return setAccessibleTrue(getConstructorsAll(cls));
	}
	public static Constructor[] getConstructorsAll(Class cls) {
		Constructor[] newCs = cls.getDeclaredConstructors();
		return newCs;
	}



	public static Method[] getMethodsAllSetAccessibleTrue(Class cls) {
		return setAccessibleTrue(getMethodsAll(cls));
	}
	public static Method[] getMethodsAll(Class cls) {
		return getMethodsAll0(cls, null);
	}

	public static Method[] getMethodsAllSetAccessibleTrue(Class cls, String methodName) {
		return setAccessibleTrue(getMethodsAll(cls, methodName));
	}
	public static Method[] getMethodsAll(Class cls, String methodName) {
		if (null == cls || null == methodName) return null;
		return getMethodsAll0(cls, methodName);
	}
	/*
	 * getMethods(),返回某个类的所有公用（public）方法包括其继承类的公用方法，当然也包括它所实现接口的方法。
	 * getDeclaredMethods(),对象表示的类或接口声明的所有方法，包括公共、保护、默认（包）访问和私有方法，但不包括继承的方法。当然也包括它所实现接口的方法。
	 */
	private static Method[] getMethodsAll0(Class cls, String methodName) {
		List<Method> methods = new ArrayList<Method>();
		Method[] methods0 = cls.getMethods();
		for (Method m:methods0)
			if (null == methodName || methodName.equals(m.getName()))
				methods.add(m);

		Method[] methods2 = cls.getDeclaredMethods();
		for (int i = 0;i < methods2.length;i++) {
			Method m = methods2[i];
			if (Modifier.isPublic(m.getModifiers())) continue;

			String mName = m.getName();
			if ((null != methodName && !methodName.equals(mName)))
				continue;
//			int ik = methods.indexOf(m);
//			if (ik != -1) methods.set(ik, m);
//			else 
			methods.add(m);
		}
		Method[] newMs = new Method[methods.size()];
		methods.toArray(newMs);
		methods.clear();
		return newMs;
	}



	public static Field[] getFieldsAllSetAccessibleTrue(Class cls) {
		return setAccessibleTrue(getFieldsAll(cls));
	}
	public static Field[] getFieldsAll(Class cls) {
		Map<String,Field> hashmap = new HashMap<String,Field>();

		Field[] fs0 = cls.getFields();
		for (Field f:fs0) if (!hashmap.containsKey(f.getName())) hashmap.put(f.getName(), f);

		Field[] fs2 = cls.getDeclaredFields();
		for (Field f:fs2) hashmap.put(f.getName(), f);

		Field[] newFs = new Field[hashmap.size()];
		int ind = 0;
		for (String key:hashmap.keySet())
			newFs[ind++] = hashmap.get(key);
		return newFs;
	}
	
	
	
	
	
	/**
	 * Checks whether can control member accessible.
	 * @return If can control member accessible, it return {@literal true}
	 */
	private static final ReflectPermission suppressAccessChecksReflectPermission = new ReflectPermission("suppressAccessChecks");
	public static boolean canSetAccessible() {
		try {
			SecurityManager securityManager = System.getSecurityManager();
			if (null != securityManager)
				securityManager.checkPermission(suppressAccessChecksReflectPermission);
		} catch (SecurityException e) {
			return false;
		}
		return true;
	}
	
	
	
	
	
	//搜索构造方法 方法名(name) {可选}指定传入参数(object)的Class(classname)
	public static Constructor findConstructor(Object Cls, Class...classs) {
		return findConstructor(XClass.getClass(Cls), classs);
	}
	public static Constructor findConstructor(Class Cls, Class...classs) {
		if (null == classs) classs = XStaticFixedValue.nullClassArray;
		return getConstructorSetAccessibleTrue(Cls, classs);
	}

	public static Object newInstance(Constructor c , Object... object) throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException  {
		if (null == object) object = XStaticFixedValue.nullObjectArray;
		return setAccessibleTrueOne(c).newInstance(object);
	}

	public static Method findMethod(Class Cls, String functionName, Class...classs) { 
		if (null == classs) classs = XStaticFixedValue.nullClassArray;
		return getMethodSetAccessibleTrue(Cls, functionName, classs);
	}
	public static Object execMethod(Object Instance, Class cls, String method , Class[] classs, 
									Object... object) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException  {
		return execMethod0(Instance, findMethod(cls, method, classs), object);
	}
	public static Object execMethod(Object Instance, String method , Class[] classs,
									Object... object) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException  {
		return execMethod0(Instance, findMethod(XClass.getClass(Instance), method, classs), object);
	}

	public static Object execStaticMethod(Object Instance, String method, Class[] classs, 
										  Object... object) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException  {
		return execMethod0(null, findMethod(XClass.getClass(Instance), method, classs), object);
	}
	public static Object execStaticMethod(Class cls, String method, Class[] classs, 
										  Object... object) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException  {
		return execMethod0(null, findMethod(cls, method, classs), object);
	}
	private static Object execMethod0(Object Instance, Method method ,
									  Object... object) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException  {
		if (null == object) object = XStaticFixedValue.nullObjectArray;
		return setAccessibleTrueOne(method).invoke(Instance, object);
	}

	public static Field findField(Class cls, String name) throws NoSuchFieldException {
		return setAccessibleTrueOne(cls.getField(name));
	}
	public static Object getFieldValue(Class ClassV, Object Instance, String name) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException  {
		return fieldGetValue0(findField(ClassV, name), Instance);
	}
	public static Object getFieldValue(Object Instance, String name) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException  {
		return fieldGetValue0(findField(Instance.getClass(), name), Instance);
	}
	public static Object getStaticFieldValue(Class cls, String name) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException   {
		return fieldGetValue0(findField(cls, name), null);
	}
	public static Object getStaticFieldValue(Field field) throws IllegalAccessException, IllegalArgumentException   {
		return fieldGetValue0(field, null);
	}
	private static Object fieldGetValue0(Field field, Object object) throws IllegalAccessException, IllegalArgumentException   {
		return setAccessibleTrueOne(field).get(object);
	}

	public static void setFieldValue(Class instanceCls, Object Instance, String name, Object Value) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException  {
		fieldSetValue0(findField(instanceCls, name), Instance, Value);
	}
	public static void setFieldValue(Object Instance, String name, Object Value) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException  {
		fieldSetValue0(findField(Instance.getClass(), name), Instance, Value);
	}
	public static void setStaticFieldValue(Class cls, String name , Object newFieldValue) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException   {
		fieldSetValue0(findField(cls, name), null, newFieldValue);
	}
	public static void setStaticFieldValue(Field field , Object newFieldValue) throws IllegalAccessException, IllegalArgumentException   {
		fieldSetValue0(field, null, newFieldValue);
	}
	private static void fieldSetValue0(Field field , Object object,  Object newFieldValue) throws IllegalAccessException, IllegalArgumentException   {
		setAccessibleTrueOne(field).set(object, newFieldValue);
	}



	/*
	 * Final 属性 的 变量 不能是内联变量
	 * Final attribute variables cannot be inline

	 * static final String a0 = null;						//可修改(Can modify)		// 非内联变量
	 * static final String a = "a";						//不可修改(Can't modify)	// 内联变量
	 * static final String a2 = new String("a");			//可修改(Can modify)		// 非内联变量
	 * final String firstName = "Mike";					//不可修改(Can't modify)	// 内联变量
	 * final String firstName2 = new String("Jordan");	//可修改(Can modify)		// 非内联变量
	 * final float age = 50.5f;							//不可修改(Can't modify)	// 内联变量
	 * final float age2 = new Float(50.5f);				//可修改(Can modify)		// 非内联变量
	 * final String city;									//可修改(Can modify)		// 非内联变量

	 public static void setFieldValue(Object instance, String fieldName , Object newFieldValue) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
	 setFieldValue0(instance, instance.getClass(), fieldName, newFieldValue);
	 }
	 public static void setStaticFieldValue(Class cls, String fieldName , Object newFieldValue) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
	 setFieldValue0(null, cls, fieldName, newFieldValue);
	 }
	 private static void setFieldValue0(Object instance, Class cls, String fieldName , Object newFieldValue) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
	 Field field = getFieldAccessibleTrue(cls, fieldName);
	 if (Modifier.isFinal(field.getModifiers())) {
	 Field modifiersField = XReflectAccessible.setAccessibleTrue1(getFinalFieldModifiersField());
	 if (Modifier.isFinal(modifiersField.getInt(field)))
	 modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	 }
	 field.set(instance, newFieldValue);
	 }
	 private static Field finalModifiersField;
	 private static Field getFinalFieldModifiersField() throws NoSuchFieldException {
	 Field f;
	 if (null != finalModifiersField) return finalModifiersField;
	 // android 
	 if ((f = getField(XStaticBaseType.Field_class, "accessFlags")) != null) { return finalModifiersField = f; }
	 // default
	 if ((f = getField(XStaticBaseType.Field_class, "modifiers")) != null) { return finalModifiersField = f; }
	 throw new NoSuchFieldException();
	 }
	 */
}
