package top.fols.atri.reflect;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;

public class ReflectProxy<T> implements InvocationHandler, Serializable {
	private static final long serialVersionUID = 1L;

	T 		  value;
	Class<T>  valueClass;

	Class[]   interfaces;
	public ReflectProxy(T object, Class... interfaces) {
		if (null != (this.value = object)) {
			this.valueClass = (Class<T>) object.getClass();
		}
		this.interfaces = interfaces;
	}

	

	public T        getValue() { return value; }
	public Class<T> getValueClass() { return valueClass; }
	public Class[]  getInterfaces() { return interfaces.clone(); }

	static ProxyReflectCache cache = new ProxyReflectCache();
	static class ProxyReflectCache extends ReflectCache {
		@Override
		public MethodList getMethodsList(Class p1, String p2) {
			// TODO: Implement this method
			return super.getMethodsList(p1, p2);
		}
	}

	protected Method matchMethod(Method[] list,
								 Class[][] listParameterTypes,
								 Class returnClass,
								 String name,
								 boolean nullObjectCanCastToClass,
								 Object... paramInstanceArr) {
		return ReflectMatcher.matchMethod(list, listParameterTypes, returnClass, name, nullObjectCanCastToClass, paramInstanceArr);
	}



	Method getMethod(Class cls, String name, Object... paramInstanceArr) {
		ProxyReflectCache cache = this.cache;
		ReflectCache.MethodList gets = cache.getMethodsList(cls, name);
        if (null != gets && gets.list().length != 0) {
        	Method match = matchMethod(gets.list(), gets.parameterTypes(), null, null, true, paramInstanceArr);
            return Reflects.accessible(match);
        }
		return null;
	}



	@Override
	public Object invoke(Object p1, Method p2, Object[] p3) throws Throwable {
		// TODO: Implement this method
		Method match = getMethod(valueClass, p2.getName(), Objects.empty(p3)? Finals.EMPTY_OBJECT_ARRAY: p3);
		return null == match ? null: match.invoke(value, p3);
	}


	public static <T> T newInstance(Object object, Class<T> proxy) {
		return (T) newInstance(object, new Class[]{proxy});
	}
	public static Object newInstance(Object object, Class... proxy) {
		if (Objects.empty(proxy)) {
			return null;
		} else {
			return newInstance(
					object,
					proxy[0].getClassLoader(),
					proxy);
		}
	}
	public static Object newInstance(Object object, ClassLoader classLoader, Class... proxy) {
		if (Objects.empty(proxy)) {
			return null;
		} else {
			ReflectProxy r0 = new ReflectProxy(object, proxy);
			return Proxy.newProxyInstance(
					classLoader,
					proxy,
					r0);
		}
	}

}
