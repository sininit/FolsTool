package top.fols.box.lang.reflect.optdeclared;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.lang.reflect.XReflectAccessible;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XArrays;

@XAnnotations("cache option")
public class XReflectQuick {
	public static final XReflectQuick defaultInstance = new XReflectQuick();
	private static class ClassArrHash {
		private Class[] clss;
		private int hash;
		public ClassArrHash(Class[] cls) {
			this.hash = getHashValue(this.clss = null == cls ?XStaticFixedValue.nullClassArray: cls);
		}
		@Override
		public int hashCode() {
			// TODO: Implement this method
			return hash;
		}
		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj instanceof ClassArrHash) {
				ClassArrHash newobj = (XReflectQuick.ClassArrHash) obj;
				if (newobj.clss.length == this.clss.length &&
					newobj.hash == this.hash) {
					return XArrays.arrayContentsEquals(this.clss, newobj.clss);
				}
			}
			return false;
		}

		private static int getHashValue(Class[] c) {
			return Arrays.hashCode(c);
		}
	}




	protected Field[] getFields0(Class cls) {
		return XReflectAccessible.getFieldsAllSetAccessibleTrue(cls);
	}
	protected Constructor[] getConstructors0(Class cls) {
		return XReflectAccessible.getConstructorsAllSetAccessibleTrue(cls);
	}
	protected Method[] getMethods0(Class cls) {
		return XReflectAccessible.getMethodsAllSetAccessibleTrue(cls);
	}


	private final Map<Class,reflect> reflects = new HashMap<Class,reflect>();
	public Field getField(Class cls, String Name) {
		return getFieldManager(cls).getField(Name);
	}
	public Constructor getConstructor(Class cls, Class... param) {
		return getConstructorManager(cls).getConstructor(param);
	}
	public Method getMethod(Class cls, String Name, Class... param) {
		return getMethodManager(cls).getMethod(Name, param);
	}

	public field getFieldManager(Class cls) {
		reflect f = reflects.get(cls);
		if (null == f)
			reflects.put(cls, f = new reflect(cls));
		return f.getFieldManager();
	}
	public constructor getConstructorManager(Class cls) {
		reflect f = reflects.get(cls);
		if (null == f)
			reflects.put(cls, f = new reflect(cls));
		return f.getConstructorManager();
	}
	public method getMethodManager(Class cls) {
		reflect f = reflects.get(cls);
		if (null == f)
			reflects.put(cls, f = new reflect(cls));
		return f.getMethodManager();
	}

	public reflect get(Class cls) {
		reflect f = reflects.get(cls);
		if (null == f)
			reflects.put(cls, f = new reflect(cls));
		return f;
	}

	public void clearCache() {
		reflects.clear();
	}
	public void removeCache(Class cls) {
		reflects.remove(cls);
	}
	public void put(Class cls, reflect reflect) {
		reflects.put(cls, reflect);
	}
	
	
	public class reflect {
		private Class cls;
		private field fields;
		private method methods;
		private constructor constructors;

		private reflect(Class cls) {
			if (null == cls)
				throw new NullPointerException();
			this.cls = cls;
			this.fields = new field(cls);
			this.methods = new method(cls);
			this.constructors = new constructor(cls);
		}
		public field getFieldManager() {
			return fields;
		}
		public constructor getConstructorManager() {
			return constructors;
		}
		public method getMethodManager() {
			return methods;
		}

		public Field getField(String Name) {
			return fields.getField(Name);
		}
		public Constructor getConstructor(Class... param) {
			return constructors.getConstructor(param);
		}
		public Method getMethod(String Name, Class... param) {
			return methods.getMethod(Name, param);
		}
	}

	public class field {
		private Class cls;
		// cache
		private Map<String,Field> Hash = new HashMap<String,Field>(); //MethodName ->  ClassKey -> Method
		public Field getField(String name) {
			return Hash.get(name);
		}

		public field(Class cls) {
			if (null == cls)
				throw new NullPointerException();
			setFields(cls, getFields0(cls));
		}
		public field(Class cls, Field[] clsFields) {
			if (null == cls)
				throw new NullPointerException();
			setFields(cls, clsFields);
		}

		public synchronized void setFields(Class cls, Field[] clsFields) {
			if (null == cls)
				throw new NullPointerException();
			if (null == clsFields)
				clsFields = XStaticFixedValue.nullFieldArray;
			this.cls = cls;
			this.Hash.clear();
			for (Field field:clsFields)
				Hash.put(field.getName(), field);
		}
	}

	public class constructor {
		private Class cls;
		// cache
		private Map<ClassArrHash,Constructor> constructorHash = new HashMap<ClassArrHash,Constructor>(); //MethodName ->  ClassKey -> Method

		public Constructor getConstructor(Class[] ParameterTypesClass) {
			return constructorHash.get(new ClassArrHash(ParameterTypesClass));
		}
		public constructor(Class cls) {
			if (null == cls)
				throw new NullPointerException();
			setConstructors(cls, getConstructors0(cls));
		}
		public constructor(Class cls, Constructor[] clsConstructors) {
			if (null == cls)
				throw new NullPointerException();
			setConstructors(cls, clsConstructors);
		}

		public synchronized void setConstructors(Class cls, Constructor[] clsConstructors) {
			if (null == cls)
				throw new NullPointerException();
			if (null == clsConstructors)
				clsConstructors = XStaticFixedValue.nullConstructorArray;
			this.cls = cls;
			this.constructorHash.clear();
			Map<ClassArrHash, Constructor> newCache = new HashMap<ClassArrHash, Constructor>(); 
			for (Constructor constructor:clsConstructors) {
				Class[] param = constructor.getParameterTypes();
				ClassArrHash cah = new ClassArrHash(param);
				newCache.put(cah, constructor);
			}
			this.constructorHash = newCache;
		}
	}



	public class method {
		private Class cls;
		private Map<String,Map<ClassArrHash,Method>> cache = new HashMap<String,Map<ClassArrHash,Method>>();

		public Method getMethod(String methodName, Class[] ParameterTypesClass) {
			Map<ClassArrHash,Method> cache2 = cache.get(methodName);
			if (null == cache2)
				return null;
			Method ms = cache2.get(new ClassArrHash(ParameterTypesClass));
			return ms;
		}

		public method(Class cls) {
			if (null == cls)
				throw new NullPointerException();
			setMethods(cls, getMethods0(cls));
		}
		public method(Class cls, Method[] clsMethods) {
			if (null == cls)
				throw new NullPointerException();
			setMethods(cls, clsMethods);
		}

		public synchronized void setMethods(Class cls, Method[] clsMethods) {
			if (null == cls)
				throw new NullPointerException();
			if (null == clsMethods)
				clsMethods = XStaticFixedValue.nullMethodArray;
			this.cls = cls;
			this.cache.clear();
			Map<String,Map<ClassArrHash,Method>> Hash = new HashMap<String,Map<ClassArrHash,Method>>(); 

			for (Method method:clsMethods) {
				Class[] param = method.getParameterTypes();
				String MethodName = method.getName();
				ClassArrHash cah = new ClassArrHash(param);

				Map<ClassArrHash,Method> xmap = Hash.get(MethodName);
				if (null == xmap) xmap = new HashMap<ClassArrHash,Method>();

				xmap.put(cah, method);
				Hash.put(MethodName, xmap);
			}
			this.cache = Hash;
		}
	}
}

