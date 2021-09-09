package top.fols.atri.reflect;
import top.fols.atri.lang.Finals;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("UnnecessaryLocalVariable")
public class Reflects {

	public static boolean parameterTypesEquals(Class[] parameterTypes, Class[] parameterTypes2) {
		if (parameterTypes.length != parameterTypes2.length) { return false;}
		for(int i = 0; i<parameterTypes.length; i++){
			if(parameterTypes[i] != parameterTypes2[i]){
				return false;
			}
		}
		return true;
	}






	public static ReflectPoint point(Object object) 	{ return ReflectPoint.from(ReflectMatcher.DEFAULT_INSTANCE, object); }
	public static ReflectPoint point(Class<?> object) 	{ return ReflectPoint.fromClass(ReflectMatcher.DEFAULT_INSTANCE, object); }





	public static class ClassObject implements Cloneable {
		Class value;

		private Class cls;
		public ClassObject(Class value) {
			this.value = value;

			this.cls = value;
		}

		Integer hashCode = null;
		@Override
		public int hashCode() {
			// TODO: Implement this method
			if (null == this.hashCode) {
				this.hashCode = 
					Objects.hashCode(Reflects.name(this.cls));
			}
			return this.hashCode.intValue();
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj instanceof ClassObject == false) { return false;}
			ClassObject value = (ClassObject) obj;
			return 
				this.cls == value.cls;
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

		private Class cls;
		private Class[] params;
		public ConstructorObject(Constructor value) {
			this.value = value;

			this.cls = value.getDeclaringClass();
			this.params = value.getParameterTypes();
		}

		Integer hashCode = null;
		@Override
		public int hashCode() {
			// TODO: Implement this method
			if (null == this.hashCode) {
				this.hashCode = 
					Objects.hash(Reflects.name(this.cls))
					+ Objects.hash((Object[]) Reflects.names(this.params))
					;
			}
			return this.hashCode.intValue();
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj instanceof ConstructorObject == false) { return false;}
			ConstructorObject value = (ConstructorObject) obj;
			return 
				this.cls == value.cls && 
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

//		private Class cls;
		private String name;
		private Class type;

		public FieldObject(Field value) {
			this.value = value;

//			this.cls = value.getDeclaringClass();
			this.name = value.getName();
			this.type = value.getType();
		}

		Integer hashCode = null;
		@Override
		public int hashCode() {
			// TODO: Implement this method
			if (null == this.hashCode) {Objects.hash();
				this.hashCode = 
					Objects.hash(this.name)
					+ Objects.hash(Reflects.name(this.type));
			}
			return this.hashCode.intValue();
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj instanceof FieldObject == false) { return false;}
			FieldObject value = (FieldObject) obj;
			return 
				//this.cls == value.cls &&
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

		private Class returnType;
		private String name;
		private Class[] params;

		public MethodObject(Method value) {
			this.value = value;

			this.returnType = value.getReturnType();
			this.name = value.getName();
			this.params = value.getParameterTypes();
		}

		Integer hashCode = null;
		@Override
		public int hashCode() {
			// TODO: Implement this method
			if (null == this.hashCode) {
				this.hashCode = 
					Objects.hash(Reflects.name(this.returnType))
					+ Objects.hash(this.name)
					+ Objects.hash((Object[]) Reflects.names(this.params));
			}
			return this.hashCode.intValue();
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj instanceof MethodObject == false) { return false;}
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



	public static String[] names(Class clss[]) { 
		String[] names = new String[clss.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = null == clss[i] ?null: clss[i].getName();
		}
		return names;
	}
	public static String name(Class cls) {
		return null == cls ?null: cls.getName();
	}


	public static final Class OBJECT_CLASS = Object.class;


	public static Class[] classes(Class cls) {
        if (null == cls) { return null; }
		Map<ClassObject, Class> values = new LinkedHashMap<>();
		Class tempClass = cls;
		while (null != tempClass) {
			Reflects.classesPut(values, cls.getClasses());
			tempClass = tempClass.getSuperclass();
			//if (temp == OBJECT_CLASS) { break;}
		}
		return values.values().toArray(new Class[values.size()]);
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



    public static Field[] fields(Class<?> cls) {
        if (null == cls) { return null; }
		Map<FieldObject, Field> values = new LinkedHashMap<>();
		Reflects.fieldPut(values, cls.getFields());
		try {
			Class tempClass = cls;
			while (null != tempClass) {
				Reflects.fieldPut(values, tempClass.getDeclaredFields());
				tempClass = tempClass.getSuperclass();
				//if (temp == OBJECT_CLASS) { break;}
			}
		} catch (Throwable ignore) {}
        return values.values().toArray(new Field[values.size()]);
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
	public static Field field(Class<?> clazz, String name) {
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
	public static Field field(Class<?> clazz, Class<?> returnType, String name) {
		if (null == returnType) { return field(clazz, name); }
		try {
			Field declaredField = clazz.getField(name);
			if (returnType == declaredField.getType()) {
				return declaredField;
			}
		} catch (NoSuchFieldException e) {
			e = null;
		}
		while (null != clazz) {
			try {
				Field declaredField = clazz.getDeclaredField(name);
				if (returnType == declaredField.getType()) {
					return declaredField;
				}
			} catch (NoSuchFieldException ex) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}



	public static Constructor[] constructors(Class<?> cls) {
		if (null == cls) { return null; }
		Map<ConstructorObject, Constructor> values = new LinkedHashMap<>();
		Reflects.constructorsPut(values, cls.getConstructors());
		Reflects.constructorsPut(values, cls.getDeclaredConstructors());
//		try {
//			Class tempClass = cls;
//			while (null != tempClass) {
//				Reflects.constructorsPut(values, cls.getDeclaredConstructors());
//				tempClass = tempClass.getSuperclass();
//				//if (temp == OBJECT_CLASS) { break;}
//			}
//		} catch (Throwable ignore) {}
        return values.values().toArray(new Constructor[values.size()]);
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
	public static Constructor constructor(Class<?> cls, Class... paramClass) {
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






	public static Method[] methods(Class<?> cls) {
		if (null == cls) { return null; }
		Map<MethodObject, Method> values = new LinkedHashMap<>();
		Reflects.methodsPut(values, cls.getMethods());
		try {
			Class tempClass = cls;
			while (null != tempClass) {
				Reflects.methodsPut(values, cls.getDeclaredMethods());
				tempClass = tempClass.getSuperclass();
				//if (temp == OBJECT_CLASS) { break;}
			}
		} catch (Throwable ignore) {}
        return values.values().toArray(new Method[values.size()]);
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
	public static Method method(Class<?> clazz, String name, Class<?>... array) {
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
	public static Method method(Class<?> clazz, Class<?> returnType, String name, Class<?>... array) {
		if (null == returnType) { return method(clazz, name, array); }
		try {
			Method declaredMethod = clazz.getMethod(name, array);
			if (returnType == declaredMethod.getReturnType()){
				return declaredMethod;
			}
		} catch (NoSuchMethodException e) {
			e = null;
		}
		while (null != clazz) {
			try {
				Method declaredMethod = clazz.getDeclaredMethod(name, array);
				if (returnType == declaredMethod.getReturnType()) {
					return declaredMethod;
				}
			} catch (NoSuchMethodException ex) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}










	public static <T extends AccessibleObject> Collection<T> accessible(Collection<T> aos) {
		for (T obj: aos) { accessible(obj); }
		return aos;
	}
	public static <T extends AccessibleObject> T[] accessible(T... aos) {
		try {
			AccessibleObject.setAccessible(aos, true);
		} catch (Throwable ignore) { ignore.printStackTrace(); }
		return aos;
	}
	public static <T extends AccessibleObject> T accessible(T aos) {
		try {
			aos.setAccessible(true);
		} catch (Throwable ignore) { ignore.printStackTrace(); }
		return aos;
	}



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



	/**
	 *
	 */

	public static Field setFinalFieldAccessAble(Field f) throws IllegalAccessException, IllegalArgumentException {
		int modifier = f.getModifiers();
		if (Modifier.isStatic(modifier)) {
			Field[] fs = accessible(Finals.FIELD_CLASS.getDeclaredFields());
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

}
