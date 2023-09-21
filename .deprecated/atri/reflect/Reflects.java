package top.fols.atri.reflect;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.box.lang.Classx;

import java.lang.reflect.*;
import java.util.*;

@SuppressWarnings({"UnnecessaryLocalVariable", "rawtypes", "SpellCheckingInspection"})
public class Reflects {


	public static Set<Class> getInherites(Class cls) {
		Set<Class> result = new LinkedHashSet<>();
		if (null == cls)
			return result;
		else
			result.add(cls);
		for (Class inter: cls.getInterfaces()) //public interface x extends a,b,c
			result.addAll(getInherites(inter));
		for (Class inter: new Class[]{cls.getSuperclass()})
			result.addAll(getInherites(inter));
		return result;
	}

	/**
	 * Reflects 使用前自己需要判断 instanceof
	 *
	 * 类型和名字必须一样
	 * //修饰符 相同
	 */
	public static boolean isOverride(Field parentClass,
									 Field thisClass) {
		boolean isEqDeclared = parentClass.getDeclaringClass() == thisClass.getDeclaringClass();
		boolean isEqName     = parentClass.getName().equals(thisClass.getName());
		boolean isEqType     = parentClass.getType() == thisClass.getType();
		if (isEqDeclared) {
			return
					(isEqName && isEqType);
		} else {
//			int parentClassModifiers = parentClass.getModifiers();
//			int thisClassModifiers = thisClass.getModifiers();
//			if (Modifier.isStatic(parentClassModifiers) != Modifier.isStatic(thisClassModifiers))
//				return false;
//
//			return parentClassModifiers == thisClassModifiers &&
//					(isEqName && isEqType);
			return false;
		}
	}
	/**
	 * Reflects 使用前自己需要判断 instanceof
	 *
	 * 参数类型必须一致
	 * 子类方法的返回值类型要和父类方法的返回值类型一样，
	 *    子类方法的返回值类型要是父类方法的返回值类型的子类
	 *    例如父类返回类型是object 子类方法的返回值类型是String
	 * 修饰符 如果父类是private就不能重写
	 *    可以从空权限提升到protected或者public
	 *    可以从protected提升到public
	 *    可以相同
	 */
	public static boolean isOverride(Method parentClass,
									 Method thisClass) {
		boolean isEqDeclared    = parentClass.getDeclaringClass() == thisClass.getDeclaringClass();
		if (isEqDeclared) {
			return (parentClass.getName().equals(thisClass.getName()) &&
					parentClass.getReturnType().isAssignableFrom(thisClass.getReturnType()) &&
					Objects.identityEquals(parentClass.getParameterTypes(), thisClass.getParameterTypes())); //low speed
		} else {
			int parentClassModifiers = parentClass.getModifiers();
			int thisClassModifiers   = thisClass.getModifiers();
			if (Modifier.isStatic(parentClassModifiers) || Modifier.isStatic(thisClassModifiers))
				return false;

			boolean isParentPrivate = Modifier.isPrivate(parentClassModifiers);
			if (!isParentPrivate) {
				return (parentClass.getName().equals(thisClass.getName()) &&
						parentClass.getReturnType().isAssignableFrom(thisClass.getReturnType()) &&
						Objects.identityEquals(parentClass.getParameterTypes(), thisClass.getParameterTypes())); //low speed
			}
		}
		return false;
	}


	public static boolean isOverride(Class parentClass,
									 Field thisClass) {
		return isOverride(parentClass, parentClass.getDeclaredFields(), thisClass);
	}
	public static boolean isOverride(Class parentClass,
									 Method thisClass) {
		return isOverride(parentClass, parentClass.getDeclaredMethods(), thisClass);
	}
	static boolean isOverride(Class parentClass, Method[] parentClassDeclaredMethods,
							  Method thisClass) {
		if (thisClass.getDeclaringClass() == parentClass) {
			return true;
		} else {
			for (Method f: parentClassDeclaredMethods) {
				if (isOverride(f, thisClass)) {
					return true;
				}
			}
			return false;
		}
	}
	static boolean isOverride(Class parentClass, Field[] parentClassDeclaredMethods,
							  Field thisClass) {
		if (thisClass.getDeclaringClass() == parentClass) {
			return true;
		} else {
			for (Field f: parentClassDeclaredMethods) {
				if (isOverride(f, thisClass)) {
					return true;
				}
			}
			return false;
		}
	}
	static final Class<Object> CLASS_OBJECT = Finals.OBJECT_CLASS;
	static final Method[]      CLASS_OBJECT_DECLARED_METHOD = CLASS_OBJECT.getDeclaredMethods();
	public static boolean isOverrideObjectClass(Method thisClass) {
		return isOverride(CLASS_OBJECT, CLASS_OBJECT_DECLARED_METHOD, thisClass);
	}
	public static boolean isOverrideObjectClassMethod(Method thisClass) {
		if (Modifier.isPrivate(thisClass.getModifiers()))
			return false;
		return isOverrideObjectClass(thisClass);
	}




	public static class ClassObject implements Cloneable {
		Class value;

		public ClassObject(Class value) {
			this.value = value;
		}


		int hashCode;
		@Override
		public int hashCode() {
			// TODO: Implement this method
			int h = this.hashCode;
			if (h == 0) {
				int h1 = Objects.hashCode(value);
				hashCode = h = (0 == h1 ? 1 : h1);
			}
			return h;
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (!(obj instanceof ClassObject)) { return false; }
			ClassObject value = (ClassObject) obj;
			return this.value == value.value;
		}

		@Override
		public ClassObject clone() {
			// TODO: Implement this method
			return new ClassObject(this.value);
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return this.value.toString();
		}

		public static ClassObject wrap(Class value) { return new ClassObject(value); }
	}
	public static class ConstructorObject implements Cloneable {
		Constructor value;

		private Class clazz;
		private Class[] params;
		public ConstructorObject(Constructor value) {
			this.value = value;

			this.clazz = value.getDeclaringClass();
			this.params = value.getParameterTypes();
		}

		int hashCode;
		@Override
		public int hashCode() {
			// TODO: Implement this method
			int h = this.hashCode;
			if (h == 0) {
				int h1 = Objects.hashCode(clazz) +
						 Objects.hashCode(params);
				hashCode = h = (0 == h1 ? 1 : h1);
			}
			return h;
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (!(obj instanceof ConstructorObject)) { return false;}

			ConstructorObject value = (ConstructorObject) obj;
			return  this.clazz == value.clazz &&
					Arrays.equals(this.params, value.params);
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return this.value.toString();
		}

		@Override
		public ConstructorObject clone() {
			// TODO: Implement this method
			return new ConstructorObject(this.value);
		}

		public static ConstructorObject wrap(Constructor value) { return new ConstructorObject(value); }
	}
	public static class FieldObject {
		Field value;

		private final String name;
		private final Class type;

		public FieldObject(Field value) {
			this.value = value;

			this.name = value.getName();
			this.type = value.getType();
		}

		int hashCode;
		@Override
		public int hashCode() {
			// TODO: Implement this method
			int h = this.hashCode;
			if (h == 0) {
				int h1 = Objects.hashCode(this.name) +
						 Objects.hashCode(type);
				hashCode = h = (0 == h1 ? 1 : h1);
			}
			return h;
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (!(obj instanceof FieldObject)) { return false;}
			FieldObject value = (FieldObject) obj;
			return
				this.name.equals(value.name) &&
				this.type == value.type;
		}


		@Override
		public FieldObject clone() {
			// TODO: Implement this method
			return new FieldObject(this.value);
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return this.value.toString();
		}

		public static FieldObject wrap(Field value) { return new FieldObject(value);}
	}
	public static class MethodObject {
		Method value;

		private final Class returnType;
		private final String name;
		private final Class[] params;

		public MethodObject(Method value) {
			this.value = value;

			this.returnType = value.getReturnType();
			this.name = value.getName();
			this.params = value.getParameterTypes();
		}

		int hashCode;
		@Override
		public int hashCode() {
			// TODO: Implement this method
			int h = this.hashCode;
			if (h == 0) {
				int h1 = Objects.hashCode(this.returnType) +
						 Objects.hashCode(this.name) +
						 Objects.hashCode(this.params);
				hashCode = h = (0 == h1 ? 1 : h1);
			}
			return h;
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (!(obj instanceof MethodObject)) { return false;}
			MethodObject value = (MethodObject) obj;
			return
				this.returnType == value.returnType &&
				this.name.equals(value.name) &&
				Arrays.equals(this.params, value.params);
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return this.value.toString();
		}

		@Override
		public MethodObject clone() {
			// TODO: Implement this method
			return new MethodObject(this.value);
		}


		public static MethodObject wrap(Method value) {	return new MethodObject(value); }
	}





	public static Constructor[] constructors(Class<?> clazz) {
		if (null == clazz) { return null; }
		Map<ConstructorObject, Constructor> values = new LinkedHashMap<>();
		Reflects.constructorsPut(values, clazz.getConstructors());
		Reflects.constructorsPut(values, clazz.getDeclaredConstructors());
		return values.values().toArray(Finals.EMPTY_CONSTRUCTOR_ARRAY);
	}
	private static void constructorsPut(Map<ConstructorObject, Constructor> values, Constructor[] puts) {
		for (Constructor cf: puts) {
			ConstructorObject cacheFieldObject = ConstructorObject.wrap(cf);
			Constructor cacheField = values.get(cacheFieldObject);
			if (null == cacheField) {
				values.put(cacheFieldObject, cf);
			}
		}
	}
	public static Constructor constructor(Class<?> clazz, Class... paramClass) {
		if (null == clazz) { return null; }
		try {
			return clazz.getDeclaredConstructor(paramClass);
		} catch (NoSuchMethodException ignored) {}
		try {
			return clazz.getConstructor(paramClass);
		} catch (NoSuchMethodException ignored) {}
		return null;
	}




	/**
	 * deep search
	 */
	public static Class[] classes(Class clazz) {
        if (null == clazz) { return null; }
		Map<ClassObject, Class> values = new LinkedHashMap<>();
		Class tempClass = clazz;
		while (null != tempClass) {
			Reflects.classesPut(values, tempClass.getDeclaredClasses());
			tempClass = tempClass.getSuperclass();
			//if (temp == OBJECT_CLASS) { break;}
		}
		return values.values().toArray(new Class[]{});
    }
	private static void classesPut(Map<ClassObject, Class> values, Class[] puts) {
		for (Class cf: puts) {
			ClassObject cacheFieldObject = ClassObject.wrap(cf);
			Class cacheField = values.get(cacheFieldObject);
			if (null == cacheField) {
				values.put(cacheFieldObject, cf);
			}
		}
	}

	/**
	 * deep search
	 */
	public static Class classes(Class<?> clazz, String name) {
		if (null == clazz) { return null; }
		try {
			Class tempClass = clazz;
			while (null != tempClass) {
				Class[] declaredClasses = tempClass.getDeclaredClasses();
				for (Class c: declaredClasses) {
					if (Classx.findSimpleName(c).equals(name)) {
						return c;
					}
				}
				tempClass = tempClass.getSuperclass();
				//if (temp == OBJECT_CLASS) { break;}
			}
		} catch (Throwable ignored) {}
		return null;
	}


	public static Class[] currentClassess(Class clazz) {
		if (null == clazz) { return null; }
		Map<ClassObject, Class> values = new LinkedHashMap<>();
		Reflects.classesPut(values, clazz.getDeclaredClasses());
		Reflects.classesPut(values, clazz.getClasses());
		return values.values().toArray(new Class[]{});
	}
	public static Class currentClasses(Class<?> clazz, String name) {
		if (null == clazz) { return null; }
		try {
			Class[] classes = clazz.getClasses();
			for (Class c : classes) {
				if (Classx.findSimpleName(c).equals(name)) {
					return c;
				}
			}
			Class[] declaredClasses = clazz.getDeclaredClasses();
			for (Class c : declaredClasses) {
				if (Classx.findSimpleName(c).equals(name)) {
					return c;
				}
			}
		} catch (Throwable ignored) {}
		return null;
	}



	/**
	 * deep search
	 */
    public static Field[] fields(Class<?> clazz) {
        if (null == clazz) { return null; }
		Map<FieldObject, Field> values = new LinkedHashMap<>();
		Reflects.fieldPut(values, clazz.getFields());
		try {
			Class tempClass = clazz;
			while (null != tempClass) {
				Reflects.fieldPut(values, tempClass.getDeclaredFields());
				tempClass = tempClass.getSuperclass();
				//if (temp == OBJECT_CLASS) { break;}
			}
		} catch (Throwable ignore) {}
        return values.values().toArray(new Field[]{});
    }
	private static void fieldPut(Map<FieldObject, Field> values, Field[] puts) {
		for (Field cf: puts) {
			FieldObject cacheFieldObject = FieldObject.wrap(cf);
			Field cacheField = values.get(cacheFieldObject);
			if (null == cacheField) {
				values.put(cacheFieldObject, cf);
			} else if (cacheField.getDeclaringClass().isAssignableFrom(cf.getDeclaringClass())) {
				values.put(cacheFieldObject, cf); // cacheField class inherit from cf
			}
		}
	}

	/**
	 * deep search
	 */
	public static Field field(Class<?> clazz, String name) {
		if (null == clazz) { return null; }
        try {
            Field declaredField = clazz.getField(name);
            return declaredField;
        } catch (NoSuchFieldException ignored) {}
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
	/**
	 * deep search
	 */
	public static Field field(Class<?> clazz, Class<?> returnType, String name) {
		if (null == clazz) { return null; }
		if (null == returnType) { return field(clazz, name); }
		try {
			Field declaredField = clazz.getField(name);
			if (returnType == declaredField.getType()) {
				return declaredField;
			}
		} catch (NoSuchFieldException ignored) {}
		while (null != clazz) {
			try {
				Field declaredField = clazz.getDeclaredField(name);
				if (returnType == declaredField.getType()) {
					return declaredField;
				} else {
					clazz = clazz.getSuperclass();
				}
			} catch (NoSuchFieldException ex) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}

	public static Field[] currentFields(Class<?> clazz) {
		if (null == clazz) { return null; }
		Map<FieldObject, Field> values = new LinkedHashMap<>();
		Reflects.fieldPut(values, clazz.getDeclaredFields());
		Reflects.fieldPut(values, clazz.getFields());
		return values.values().toArray(new Field[]{});
	}


	public static Field currentField(Class<?> clazz, String name) {
		if (null == clazz) { return null; }
		try {
			Field declaredField = clazz.getField(name);
			return declaredField;
		} catch (NoSuchFieldException ignored) {}
		try {
			Field declaredField = clazz.getDeclaredField(name);
			return declaredField;
		} catch (NoSuchFieldException ignored) {}
		return null;
	}
	public static Field currentField(Class<?> clazz, Class<?> returnType, String name) {
		if (null == clazz) { return null; }
		if (null == returnType) { return currentField(clazz, name); }
		try {
			Field declaredField = clazz.getField(name);
			if (returnType == declaredField.getType()) {
				return declaredField;
			}
		} catch (NoSuchFieldException ignored) {}
		try {
			Field declaredField = clazz.getDeclaredField(name);
			if (returnType == declaredField.getType()) {
				return declaredField;
			}
		} catch (NoSuchFieldException ignored) {}
		return null;
	}








	/**
	 * deep search
	 */
	public static Method[] methods(Class<?> clazz) {
		if (null == clazz) { return null; }
		Map<MethodObject, Method> values = new LinkedHashMap<>();
		Reflects.methodsPut(values, clazz.getMethods());
		try {
			Class tempClass = clazz;
			while (null != tempClass) {
				Reflects.methodsPut(values, tempClass.getDeclaredMethods());
				tempClass = tempClass.getSuperclass();
				//if (temp == OBJECT_CLASS) { break;}
			}
		} catch (Throwable ignore) {}
        return values.values().toArray(Finals.EMPTY_METHOD_ARRAY);
	}
	private static void methodsPut(Map<MethodObject, Method> values, Method[] puts) {
		for (Method cf: puts) {
			MethodObject cacheFieldObject = MethodObject.wrap(cf);
			Method cacheField = values.get(cacheFieldObject);
			if (null == cacheField) {
				values.put(cacheFieldObject, cf);
			}
		}
	}


	/**
	 * deep search
	 */
	public static Method method(Class<?> clazz, String name, Class<?>... array) {
		if (null == clazz) { return null; }
        try {
            Method declaredMethod = clazz.getMethod(name, array);
            return declaredMethod;
        } catch (NoSuchMethodException ignored) {}
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
	 * deep search
	 */
	public static Method method(Class<?> clazz, Class<?> returnType, String name, Class<?>... array) {
		if (null == clazz) { return null; }
		if (null == returnType) { return method(clazz, name, array); }
		try {
			Method declaredMethod = clazz.getMethod(name, array);
			if (returnType == declaredMethod.getReturnType()){
				return declaredMethod;
			}
		} catch (NoSuchMethodException ignored) {}
		while (null != clazz) {
			try {
				Method declaredMethod = clazz.getDeclaredMethod(name, array);
				if (returnType == declaredMethod.getReturnType()) {
					return declaredMethod;
				} else {
					clazz = clazz.getSuperclass();
				}
			} catch (NoSuchMethodException ex) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}


	public static Method[] currentMethods(Class<?> clazz) {
		if (null == clazz) { return null; }
		Map<MethodObject, Method> values = new LinkedHashMap<>();
		Reflects.methodsPut(values, clazz.getMethods());
		Reflects.methodsPut(values, clazz.getDeclaredMethods());
		return values.values().toArray(Finals.EMPTY_METHOD_ARRAY);
	}
	public static Method currentMethod(Class<?> clazz, String name, Class<?>... array) {
		if (null == clazz) { return null; }
		try {
			Method declaredMethod = clazz.getMethod(name, array);
			return declaredMethod;
		} catch (NoSuchMethodException ignored) {}
		try {
			Method declaredMethod = clazz.getDeclaredMethod(name, array);
			return declaredMethod;
		} catch (NoSuchMethodException ignored) {}
		return null;
	}
	public static Method currentMethod(Class<?> clazz, Class<?> returnType, String name, Class<?>... array) {
		if (null == clazz) { return null; }
		if (null == returnType) { return currentMethod(clazz, name, array); }
		try {
			Method declaredMethod = clazz.getMethod(name, array);
			if (returnType == declaredMethod.getReturnType()) {
				return declaredMethod;
			}
		} catch (NoSuchMethodException ignored) {}
		try {
			Method declaredMethod = clazz.getDeclaredMethod(name, array);
			if (returnType == declaredMethod.getReturnType()) {
				return declaredMethod;
			}
		} catch (NoSuchMethodException ignored) {}
		return null;
	}






	public static class SetAccessibleObject {
		public void setAccessible(AccessibleObject accessibleObject) {
			accessibleObject.setAccessible(true);
		}
	}
	static SetAccessibleObject setAccessibleObject;


	public static <T extends AccessibleObject> Collection<T> accessible(Collection<T> aos) {
		if (null == aos) { return null; }
		if (null == setAccessibleObject) {
			try {
				for (T obj: aos) {
					obj.setAccessible(true);
				}
			} catch (Throwable ignore) {}
		} else {
			try {
				for (T obj : aos) {
					setAccessibleObject.setAccessible(obj);
				}
			} catch (Throwable ignore) {}
		}
		return aos;
	}
	@SafeVarargs
	public static <T extends AccessibleObject> T[] accessible(T... aos) {
		if (null == aos) { return null; }
		if (null == setAccessibleObject) {
			for (T ao : aos) {
				try {
					ao.setAccessible(true);
				} catch (Throwable ignore) {}
			}
		} else {
			try {
				for (T obj : aos) {
					setAccessibleObject.setAccessible(obj);
				}
			} catch (Throwable ignore) {}
		}
		return aos;
	}

	public static <T extends AccessibleObject> T accessible(T aos) {
		if (null == aos) { return null; }
		if (null == setAccessibleObject) {
			try {
				aos.setAccessible(true);
			} catch (Throwable ignore) {}
		} else {
			try {
				setAccessibleObject.setAccessible(aos);
			} catch (Throwable ignore) {}
		}
		return aos;
	}







	static Field[] fieldFields;
	static Field[] getFieldFields() {
		if (null == fieldFields) {
			return  fieldFields = accessible(Finals.FIELD_CLASS.getDeclaredFields());
		} else {
			return fieldFields;
		}
	}

	@Deprecated
	public static Field setFinalFieldAccessAble(Field f) throws IllegalAccessException, IllegalArgumentException {
		int modifier = f.getModifiers();
		Field accessFlags;
		Object value;
		int intValue;
		for (Field fi : getFieldFields()) {
			if ((value = fi.get(f)) instanceof Integer && (intValue = (Integer) value) == modifier) {
				accessFlags = fi;
				accessFlags.setInt(f, intValue & ~Modifier.FINAL);
			}
		}
		return f;
	}


	public static <T> T newInstance(Class<T> type) {
		try {
			Constructor<T> con = Reflects.accessible(type.getDeclaredConstructor(Finals.EMPTY_CLASS_ARRAY));
			return (T) con.newInstance();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}










	private static final ReflectPermission suppressAccessChecksReflectPermission = new ReflectPermission(
			"suppressAccessChecks");
	/**
	 * Checks whether can control member accessible.
	 *
	 * @return If you can control member accessible, it return {@literal true}
	 */
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






	static private Object unsafe;
	public static Object getUnsafe() {
		if (null == unsafe) {
			try {
				Class unsafeClass = Class.forName("sun.misc.Unsafe");
				//通过反射获取Unsafe的成员变量theUnsafe
				Field field = Reflects.accessible(unsafeClass.getDeclaredField("theUnsafe"));
				unsafe = field.get(null);
				if (null == unsafe)
					throw new RuntimeException("cannot get unsafe object");
				return unsafe;
			} catch (RuntimeException e) {
				throw e;
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		return unsafe;
	}

	static private Method allocateInstanceMethod;
	public static Method getAllocateInstanceMethod() {
		if (null == allocateInstanceMethod) {
			try {
				allocateInstanceMethod = Class.forName("sun.misc.Unsafe")
						.getMethod("allocateInstance", Class.class);
			} catch (RuntimeException e) {
				throw e;
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		return allocateInstanceMethod;
	}

	public static <T> T allocateInstance(Class<T> type) {
		if (type.isInterface())
			throw new RuntimeException("cannot create interface instance");
		try {
			return (T) getAllocateInstanceMethod().invoke(getUnsafe(), type);
		} catch (RuntimeException e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}



}
