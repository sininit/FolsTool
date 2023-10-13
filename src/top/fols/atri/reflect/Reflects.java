package top.fols.atri.reflect;

import java.lang.reflect.*;
import java.util.*;

import top.fols.atri.interfaces.annotations.Private;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.box.lang.Classx;
import top.fols.box.lang.GenericTypes;
import top.fols.box.reflect.Reflectx;

@SuppressWarnings({"rawtypes", "SpellCheckingInspection", "UnnecessaryContinue"})
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
		return isOverride(parentClass, getDeclaredFields(parentClass), thisClass);
	}
	public static boolean isOverride(Class parentClass,
									 Method thisClass) {
		return isOverride(parentClass, getDeclaredMethods(parentClass), thisClass);
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
	static final Method[]      CLASS_OBJECT_DECLARED_METHOD = getDeclaredMethods(CLASS_OBJECT);
	public static boolean isOverrideObjectClassMethod(Method thisClass) {
		if (Modifier.isPrivate(thisClass.getModifiers()))
			return false;
		return isOverride(CLASS_OBJECT, CLASS_OBJECT_DECLARED_METHOD, thisClass);
	}




	public static class SignatureClassWrap implements Cloneable {
		private final Class value;

		public SignatureClassWrap(Class value) {
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
			if (!(obj instanceof SignatureClassWrap)) { return false; }
			SignatureClassWrap value = (SignatureClassWrap) obj;
			return this.value == value.value; //signature
		}

		@Override
		public SignatureClassWrap clone() {
			// TODO: Implement this method
			return new SignatureClassWrap(this.value);
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return this.value.toString();
		}

		public static SignatureClassWrap wrap(Class value) { return new SignatureClassWrap(value); }
	}

	public static class SignatureConstructorWrap implements Cloneable {
		private final Constructor value;

		private final Class clazz;
		private final Class[] params;
		public SignatureConstructorWrap(Constructor value) {
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
			if (!(obj instanceof SignatureConstructorWrap)) { return false;}

			SignatureConstructorWrap value = (SignatureConstructorWrap) obj;
			return  this.clazz == value.clazz &&
					Arrays.equals(this.params, value.params); //signature
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return this.value.toString();
		}

		@Override
		public SignatureConstructorWrap clone() {
			// TODO: Implement this method
			return new SignatureConstructorWrap(this.value);
		}

		public static SignatureConstructorWrap wrap(Constructor value) { return new SignatureConstructorWrap(value); }
	}

	public static class SignatureFieldWrap {
		private final Field value;

		private final String name;
		private final Class type;

		public SignatureFieldWrap(Field value) {
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
			if (!(obj instanceof SignatureFieldWrap)) { return false;}
			SignatureFieldWrap value = (SignatureFieldWrap) obj;
			return
					this.name.equals(value.name) &&
							this.type == value.type; //signature
		}


		@Override
		public SignatureFieldWrap clone() {
			// TODO: Implement this method
			return new SignatureFieldWrap(this.value);
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return this.value.toString();
		}

		public static SignatureFieldWrap wrap(Field value) { return new SignatureFieldWrap(value);}
	}
	public static class SignatureMethodWrap {
		private final Method value;

		private final Class returnType;
		private final String name;
		private final Class[] params;

		public SignatureMethodWrap(Method value) {
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
			if (!(obj instanceof SignatureMethodWrap)) { return false;}
			SignatureMethodWrap value = (SignatureMethodWrap) obj;
			return
					this.returnType == value.returnType &&
							this.name.equals(value.name) &&
							Arrays.equals(this.params, value.params); //signature
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return this.value.toString();
		}

		@Override
		public SignatureMethodWrap clone() {
			// TODO: Implement this method
			return new SignatureMethodWrap(this.value);
		}


		public static SignatureMethodWrap wrap(Method value) {	return new SignatureMethodWrap(value); }
	}





	static Field[] OBJECT_CLASS_DECLARED_FIELDS;
	static {
		Field[] es = Finals.OBJECT_CLASS.getDeclaredFields();
		List<Field> fields = new ArrayList<>();
		for (Field f: es) {
			int modifier = f.getModifiers();
			if (Modifier.isPrivate(modifier)) {
				continue;
			} else {
				fields.add(f);
			}
		}
		OBJECT_CLASS_DECLARED_FIELDS = fields.toArray(Finals.EMPTY_FIELD_ARRAY);
	}
	public static Class[] getDeclaredClasses(Class clazz) {
		return null != clazz ? clazz.getDeclaredClasses() : null;
	}
	public static Field[] getDeclaredFields(Class clazz) {
		return null != clazz ? (
				clazz != Finals.OBJECT_CLASS
				? clazz.getDeclaredFields()
				: OBJECT_CLASS_DECLARED_FIELDS.clone()) : null;
	}
	public static Constructor[] getDeclaredConstructors(Class clazz) {
		return null != clazz ? clazz.getDeclaredConstructors() : null;
	}
	public static Method[] getDeclaredMethods(Class clazz) {
		return null != clazz ? clazz.getDeclaredMethods() : null;
	}










	/**
	 * deep search
	 */
	public static Class[] classes(Class clazz) {
		if (null == clazz) { return null; }
		Map<SignatureClassWrap, Class> values = new LinkedHashMap<>();
		Class tempClass = clazz;
		while (null != tempClass) {
			Reflects.classesAppend(values, getDeclaredClasses(tempClass));
			tempClass = tempClass.getSuperclass();
		}
		Reflects.classesAppend(values, clazz.getClasses());
		return values.values().toArray(Finals.EMPTY_CLASS_ARRAY);
	}
	private static void classesAppend(Map<SignatureClassWrap, Class> values, Class[] puts) {
		for (Class cf: puts) {
			SignatureClassWrap cacheFieldObject = SignatureClassWrap.wrap(cf);
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
				for (Class c: getDeclaredClasses(tempClass)) {
					if (Classx.findSimpleName(c).equals(name)) {
						return c;
					}
				}
				tempClass = tempClass.getSuperclass();
			}
		} catch (Exception ignored) {}
		try {
			for (Class c: clazz.getClasses()) {
				if (Classx.findSimpleName(c).equals(name)) {
					return c;
				}
			}
		} catch (Exception ignored) {}
		return null;
	}





	public static Constructor[] constructors(Class<?> clazz) {
		if (null == clazz) { return null; }
		Map<SignatureConstructorWrap, Constructor> values = new LinkedHashMap<>();
		Reflects.constructorsAppend(values, getDeclaredConstructors(clazz));
		Reflects.constructorsAppend(values, clazz.getConstructors());
		return values.values().toArray(Finals.EMPTY_CONSTRUCTOR_ARRAY);
	}
	private static void constructorsAppend(Map<SignatureConstructorWrap, Constructor> values, Constructor[] puts) {
		for (Constructor cf: puts) {
			SignatureConstructorWrap cacheFieldObject = SignatureConstructorWrap.wrap(cf);
			Constructor cacheField = values.get(cacheFieldObject);
			if (null == cacheField) {
				values.put(cacheFieldObject, cf);
			}
		}
	}
	public static Constructor constructor(Class<?> clazz, Class... paramClass) {
		if (null == clazz) { return null; }
		try { return clazz.getDeclaredConstructor(paramClass); } catch (NoSuchMethodException ignored) {}
		try { return clazz.getConstructor(paramClass);         } catch (NoSuchMethodException ignored) {}
		return null;
	}



	/**
	 * deep search
	 */
	public static Field[] fields(Class<?> clazz) {
		if (null == clazz) { return null; }
		Map<SignatureFieldWrap, Field> values = new LinkedHashMap<>();
		try {
			Class tempClass = clazz;
			while (null != tempClass) {
				Reflects.fieldAppend(values, getDeclaredFields(tempClass));
				tempClass = tempClass.getSuperclass();
			}
		} catch (Exception ignore) {}
		Reflects.fieldAppend(values, clazz.getFields());
		return values.values().toArray(Finals.EMPTY_FIELD_ARRAY);
	}
	private static void fieldAppend(Map<SignatureFieldWrap, Field> values, Field[] puts) {
		for (Field cf: puts) {
			SignatureFieldWrap cacheFieldObject = SignatureFieldWrap.wrap(cf);
			Field cacheField = values.get(cacheFieldObject);
			if (null == cacheField) {
				values.put(cacheFieldObject, cf);
			}
		}
	}

	/**
	 * deep search
	 */
	public static Field field(Class<?> clazz, String name) {
		if (null == clazz) { return null; }
		Class<?> tempClass = clazz;
		while (null != tempClass) {
			try {
				return tempClass.getDeclaredField(name);
			} catch (NoSuchFieldException ignored) {}
			tempClass = tempClass.getSuperclass();
		}
		try {
			return clazz.getField(name);
		} catch (NoSuchFieldException ignored) {}
		return null;
	}
	/**
	 * deep search
	 */
	public static Field field(Class<?> clazz, Class<?> returnType, String name) {
		if (null == clazz) { return null; }
		if (null == returnType) { return field(clazz, name); }
		Class<?> tempClass = clazz;
		while (null != tempClass) {
			try {
				Field declaredField = tempClass.getDeclaredField(name);
				if (returnType == declaredField.getType()) {
					return declaredField;
				}
			} catch (NoSuchFieldException ignored) {}
			tempClass = tempClass.getSuperclass();
		}
		try {
			Field declaredField = clazz.getField(name);
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
		Map<SignatureMethodWrap, Method> values = new LinkedHashMap<>();
		try {
			Class tempClass = clazz;
			while (null != tempClass) {
				Reflects.methodsAppend(values, getDeclaredMethods(tempClass));
				tempClass = tempClass.getSuperclass();
			}
		} catch (Exception ignore) {}
		Reflects.methodsAppend(values, clazz.getMethods());
		return values.values().toArray(Finals.EMPTY_METHOD_ARRAY);
	}
	private static void methodsAppend(Map<SignatureMethodWrap, Method> values, Method[] puts) {
		for (Method cf: puts) {
			SignatureMethodWrap cacheFieldObject = SignatureMethodWrap.wrap(cf);
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
		Class<?> tempClass = clazz;
		while (null != tempClass) {
			try {
				return tempClass.getDeclaredMethod(name, array);
			} catch (NoSuchMethodException ignored) {}
			tempClass = tempClass.getSuperclass();
		}
		try {
			return clazz.getMethod(name, array);
		} catch (NoSuchMethodException ignored) {}
		return null;
	}

	/**
	 * deep search
	 */
	public static Method method(Class<?> clazz, Class<?> returnType, String name, Class<?>... array) {
		if (null == clazz)      { return null; }
		if (null == returnType) { return method(clazz, name, array); }
		Class<?> tempClass = clazz;
		while (null != tempClass) {
			try {
				Method declaredMethod = tempClass.getDeclaredMethod(name, array);
				if (returnType == declaredMethod.getReturnType()) {
					return declaredMethod;
				}
			} catch (NoSuchMethodException ignored) {}
			tempClass = tempClass.getSuperclass();
		}
		try {
			Method declaredMethod = clazz.getMethod(name, array);
			if (returnType == declaredMethod.getReturnType()) {
				return declaredMethod;
			}
		} catch (NoSuchMethodException ignored) {}
		return null;
	}






	public static Class[] topClassess(Class clazz) {
		if (null == clazz) { return null; }
		Map<SignatureClassWrap, Class> values = new LinkedHashMap<>();
		classesAppend(values, getDeclaredClasses(clazz));
		classesAppend(values, clazz.getClasses());
		return values.values().toArray(Finals.EMPTY_CLASS_ARRAY);
	}
	public static Class topClasses(Class<?> clazz, String name) {
		if (null == clazz) { return null; }
		try {
			for (Class c : getDeclaredClasses(clazz)) {
				if (Classx.findSimpleName(c).equals(name)) {
					return c;
				}
			}
			for (Class c : clazz.getClasses()) {
				if (Classx.findSimpleName(c).equals(name)) {
					return c;
				}
			}
		} catch (Exception ignored) {}
		return null;
	}


	public static Constructor[] topConstructors(Class clazz) { return constructors(clazz); }
	public static Constructor   topConstructor(Class<?> clazz, Class<?>... param) { return constructor(clazz, param); }


	public static Field[] topFields(Class<?> clazz) {
		if (null == clazz) { return null; }

		Map<SignatureFieldWrap, Field> values = new LinkedHashMap<>();
		fieldAppend(values, getDeclaredFields(clazz));
		fieldAppend(values, clazz.getFields());
		return values.values().toArray(Finals.EMPTY_FIELD_ARRAY);
	}

	public static Field topField(Class<?> clazz, String name) {
		if (null == clazz) { return null; }
		try { return clazz.getDeclaredField(name); } catch (NoSuchFieldException ignored) {}
		try { return clazz.getField(name);         } catch (NoSuchFieldException ignored) {}
		return null;
	}

	public static Field topField(Class<?> clazz, Class<?> returnType, String name) {
		if (null == clazz) 		{ return null; }
		if (null == returnType) { return topField(clazz, name); }
		try {
			Field declaredField = clazz.getDeclaredField(name);
			if (returnType == declaredField.getType()) {
				return declaredField;
			}
		} catch (NoSuchFieldException ignored) {}
		try {
			Field declaredField = clazz.getField(name);
			if (returnType == declaredField.getType()) {
				return declaredField;
			}
		} catch (NoSuchFieldException ignored) {}
		return null;
	}


	public static Method[] topMethods(Class<?> clazz) {
		if (null == clazz) { return null; }
		Map<SignatureMethodWrap, Method> values = new LinkedHashMap<>();
		methodsAppend(values, getDeclaredMethods(clazz));
		methodsAppend(values, clazz.getMethods());
		return values.values().toArray(Finals.EMPTY_METHOD_ARRAY);
	}
	public static Method topMethod(Class<?> clazz, String name, Class<?>... param) {
		if (null == clazz) { return null; }
		try { return clazz.getDeclaredMethod(name, param); } catch (NoSuchMethodException ignored) {}
		try { return clazz.getMethod(name, param);         } catch (NoSuchMethodException ignored) {}
		return null;
	}
	public static Method topMethod(Class<?> clazz, Class<?> returnType, String name, Class<?>... param) {
		if (null == clazz) 		{ return null; }
		if (null == returnType) { return topMethod(clazz, name, param); }
		try {
			Method declaredMethod = clazz.getDeclaredMethod(name, param);
			if (returnType == declaredMethod.getReturnType()) {
				return declaredMethod;
			}
		} catch (NoSuchMethodException ignored) {}
		try {
			Method declaredMethod = clazz.getMethod(name, param);
			if (returnType == declaredMethod.getReturnType()) {
				return declaredMethod;
			}
		} catch (NoSuchMethodException ignored) {}
		return null;
	}







	public static <T> GenericTypes.GenericElement getGenericSuperclasses(Class src) {
		return new GenericTypes().getGenericSuperclasses(src);
	}
	public static <T> GenericTypes.GenericElement getGenericsInterfaces(Class src, Class interfax) {
		return new GenericTypes().getGenericsInterfaces(src, interfax);
	}







	static class AccessibleSetter { public void setAccessible(AccessibleObject accessibleObject) { accessibleObject.setAccessible(true); }}
	static AccessibleSetter accessibleSetter;


	public static <T extends AccessibleObject> Collection<T> accessible(Collection<T> aos) {
		if (null != aos) {
			if (null != accessibleSetter) {
				for (T ao : aos)
					try { accessibleSetter.setAccessible(ao); } catch (Throwable ignore) { }
			} else {
				for (T ao : aos)
					try { ao.setAccessible(true); } catch (Throwable ignore) { }
			}
		}
		return aos;
	}

	@SafeVarargs
	public static <T extends AccessibleObject> T[] accessible(T... aos) {
		if (null != aos) {
			if (null != accessibleSetter) {
				for (T ao : aos)
					try { accessibleSetter.setAccessible(ao); } catch (Throwable ignore) { }
			} else {
				for (T ao : aos)
					try { ao.setAccessible(true); } catch (Throwable ignore) { }
			}
		}
		return aos;
	}

	public static <T extends AccessibleObject> T accessible(T ao) {
		if (null != ao) {
			try {
				if (null != accessibleSetter) {
					accessibleSetter.setAccessible(ao);
				} else {
					ao.setAccessible(true);
				}
			} catch (Throwable ignore) {}
			return ao;
		}
		return null;
	}







	@Private
	public static <T> Constructor<T> getEmptyArgsConstructor(Class<T> type){
		try {
			return Reflects.accessible(type.getDeclaredConstructor(Finals.EMPTY_CLASS_ARRAY));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Private
	public static <T> T newInstance(Class<T> type) {
		Constructor<T> con = getEmptyArgsConstructor(type);
		try {
			return (T) con.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Private
	@Deprecated
	public static Field setFinalFieldAccessAble(Field f) throws IllegalAccessException, IllegalArgumentException {
		return Reflectx.setFinalFieldAccessAble(f);
	}



}


