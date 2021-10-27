package top.fols.atri.reflect;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Arrays;

import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ReflectProxy<T, F> implements InvocationHandler, Serializable {
	private static final long serialVersionUID = 1L;

	T 		  value;
	Class<T>  valueClass;
	Class[]   interfaces;
	ReflectProxy(T object, Class<T>  objectClass,
				 Class<?>... interfaces) {
		this.value = object;
		this.valueClass = null == objectClass? (Class<T>) object.getClass():objectClass;
		this.interfaces = null == interfaces ? Finals.EMPTY_CLASS_ARRAY: interfaces;
	}



	public T        getValue() { return value; }
	public Class<T> getValueClass() { return valueClass; }
	public Class[]  getInterfaces() { return interfaces.clone(); }




	protected static ProxyReflectCache cache = new ProxyReflectCache();
	protected static class ProxyReflectCache extends ReflectCache {
		@Override public ClassesList 	 getClassesList(Class cls) 			 	{ return super.getClassesList(cls);      }
		@Override public ConstructorList getConstructorsList(Class cls)  		{ return super.getConstructorsList(cls); }
		@Override public FieldList 		 getFieldsList(Class cls)				{ return super.getFieldsList(cls);       }
		@Override public FieldList 		 getFieldsList(Class cls, String name)  { return super.getFieldsList(cls, name); }
		@Override public MethodList 	 getMethodsList(Class p1, String p2)    { return super.getMethodsList(p1, p2);   }
	}




	@SuppressWarnings("SameParameterValue")
//	protected Method matchMethod(Method[] list,
//								 Class[][] listParameterTypes,
//								 Class returnClass,
//								 String name,
//								 boolean nullObjectCanCastToClass,
//								 Object... paramInstanceArr) {
//		return ReflectMatcher.matchMethod(list, listParameterTypes, returnClass, name, nullObjectCanCastToClass, paramInstanceArr);
//	}


//	Method getMethod(Class cls, String name, Object... paramInstanceArr) {
//		ProxyReflectCache cache = ReflectProxy.cache;
//		ReflectCache.MethodList gets = cache.getMethodsList(cls, name);
//		if (null != gets && gets.list().length != 0) {
//			Method match = matchMethod(gets.list(), gets.parameterTypes(), null, null, true, paramInstanceArr);
//			return Reflects.accessible(match);
//		}
//		return null;
//	}


	Method getMethod(Class cls, Class<?> returnClass, String name, Class<?>... paramInstanceArr) {
		ProxyReflectCache cache = ReflectProxy.cache;
		Method match = cache.method(cls, returnClass, name, paramInstanceArr);
		return Reflects.accessible(match);
	}


	@Override
	public Object invoke(Object p1, Method p2, Object[] p3) throws Throwable {
		// TODO: Implement this method
		Method match = getMethod(valueClass,
				p2.getReturnType(), p2.getName(), p2.getParameterTypes());
		if (null == match) {
			throw new NoSuchMethodException(String.format("cannot from [%s] found [interface %s]", valueClass, p2));
		}
		return match.invoke(value, p3);
	}








	public static <T,F> ReflectProxy<T, F> from(T object, Class<F> proxy) {
		if (null == proxy) {
			return null;
		} else {
			return new ReflectProxy<>(object, (Class<T>) object.getClass(), proxy);
		}
	}
	public static <T> ReflectProxy<T, Object> from(T object, Class<?>... proxy0) {
		if (null == proxy0) {
			return null;
		} else {
			return new ReflectProxy<>(object, (Class<T>) object.getClass(), proxy0);
		}
	}

	public static <T,F> ReflectProxy<T, F> fromClass(Class<T> objectClass, Class<F> proxy) {
		if (null == proxy) {
			return null;
		} else {
			return new ReflectProxy<>(null, objectClass, proxy);
		}
	}
	public static <T> ReflectProxy<T, Object> fromClass(Class<T> objectClass, Class<?>... proxy0) {
		if (null == proxy0) {
			return null;
		} else {
			return new ReflectProxy<>(null, objectClass, proxy0);
		}
	}





	public F newInstance() {
		if (interfaces.length == 0) {
			return null;
		}
		for (Class anInterface : interfaces) {
			try {
				return newInstance(anInterface.getClassLoader());
			} catch (Throwable ignored) {
			}
		}
		return newInstance(getClass().getClassLoader());
	}
	public F newInstance(ClassLoader classLoader) {
		return (F) Proxy.newProxyInstance(
				classLoader,
				this.interfaces,
				this);
	}







	public Field field(Class type, String name) {
		ReflectCache.FieldList fieldsList = cache.getFieldsList(valueClass, name);
		Field field = null;
		if (null == type) {
			field = (null == fieldsList || fieldsList.list().length <= 0) ?null: fieldsList.list()[0];
		} else {
			for (Field field1 : fieldsList.list()) {
				if (field1.getType() == type) {
					field = field1;
					break;
				}
			}
		}
		return field;
	}

	public Field field(String name) {
		return field(null , name);
	}



	public Object get(String name) throws NoSuchFieldException, IllegalAccessException {
		return get(null, name);
	}
	public Object get(Class type, String name) throws IllegalAccessException, NoSuchFieldException {
		Field field = field(type, name);
		if (null == field) {
			throw new NoSuchFieldException((null == type ?"": type.getName() + " ") + name);
		}
		return Reflects.accessible(field).get(value);
	}

	public void set(String name, Object newValue) throws NoSuchFieldException, IllegalAccessException {
		set(null, name, newValue);
	}
	public void set(Class type, String name, Object newValue) throws IllegalAccessException, NoSuchFieldException {
		Field field = field(type, name);
		if (null == field) {
			throw new NoSuchFieldException((null == type ?"": type.getName() + " ") + name);
		}
		Reflects.accessible(field).set(value, newValue);
	}



	public Object opt(String name)  {
		return opt((Class) null, name);
	}
	public Object opt(Class type, String name) {
		Field field = field(type, name);
		try {
			return Reflects.accessible(field).get(value);
		} catch (IllegalAccessException e) {
			return null;
		}
	}



	public void opt(String name, Object newValue) {
		opt(null, name, newValue);
	}
	public void opt(Class type, String name, Object newValue) {
		Field field = field(type, name);
		try {
			Reflects.accessible(field).set(value, newValue);
		} catch (IllegalAccessException ignored) {
		}
	}
}
