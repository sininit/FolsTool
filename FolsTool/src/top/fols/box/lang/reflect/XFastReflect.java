package top.fols.box.lang.reflect;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.ArrayListUtils;
import top.fols.box.util.HashMapUtils9;
import top.fols.box.util.XArraysUtils;

public class XFastReflect
{
	public static final XFastReflect defaultInstance = new XFastReflect();
	
	
	
	private final HashMapUtils9<Class,reflect> reflects = new HashMapUtils9<Class,reflect>();
	
	public Field getField(Class cls, String Name)
	{
		return getFieldManager(cls).getField(Name);
	}
	public Constructor getConstructor(Class cls, Class... param)
	{
		return getConstructorManager(cls).getConstructor(param);
	}
	public Method getMethod(Class cls, String Name, Class... param)
	{
		return getMethodManager(cls).getMethod(Name, param);
	}
	
	public field getFieldManager(Class cls)
	{
		reflect f = reflects.get(cls);
		if (f == null)
			reflects.put(cls, f = new reflect(cls));
		return f.getFieldManager();
	}
	public constructor getConstructorManager(Class cls)
	{
		reflect f = reflects.get(cls);
		if (f == null)
			reflects.put(cls, f = new reflect(cls));
		return f.getConstructorManager();
	}
	public method getMethodManager(Class cls)
	{
		reflect f = reflects.get(cls);
		if (f == null)
			reflects.put(cls, f = new reflect(cls));
		return f.getMethodManager();
	}
	
	public reflect get(Class cls)
	{
		reflect f = reflects.get(cls);
		if (f == null)
			reflects.put(cls, f = new reflect(cls));
		return f;
	}
	public void clearCache()
	{
		reflects.clear();
	}
	public void removeCache(Class cls)
	{
		reflects.remove(cls);
	}
	public void put(Class cls,reflect reflect)
	{
		reflects.put(cls,reflect);
	}
	
	
	public static class reflect
	{
		private Class cls;
		private field fields;
		private method methods;
		private constructor constructors;
		private reflect(Class cls)
		{
			if (cls == null)
				throw new NullPointerException();
			this.cls = cls;
			this.fields = new field(cls);
			this.methods = new method(cls);
			this.constructors = new constructor(cls);
		}
		public field getFieldManager()
		{
			return fields;
		}
		public constructor getConstructorManager()
		{
			return constructors;
		}
		public method getMethodManager()
		{
			return methods;
		}

		public Field getField(String Name)
		{
			return fields.getField(Name);
		}
		public Constructor getConstructor(Class... param)
		{
			return constructors.getConstructor(param);
		}
		public Method getMethod(String Name, Class... param)
		{
			return methods.getMethod(Name, param);
		}
	}







	private static Field[] getFields(Class cls)
	{
		return XReflect.getFields(cls);
	}
	private static Constructor[] getConstructors(Class cls)
	{
		return XReflect.getConstructors(cls);
	}
	private static Method[] getMethods(Class cls)
	{
		return XReflect.getMethods(cls);
	}



	private static int toHash(Class[] c)
	{
		return Arrays.hashCode(c);
	}




	public static class field
	{
		private Class cls;
		private HashMapUtils9<String,Field> Hash = new HashMapUtils9<String,Field>(); //MethodName ->  ClassKey -> Method
		public Field getField(String name)
		{
			return Hash.get(name);
		}
		public field(Class cls)
		{
			if (cls == null)
				throw new NullPointerException();
			setFields(cls, getFields(cls));
		}
		public field(Class cls, Field[] clsFields)
		{
			if (cls == null)
				throw new NullPointerException();
			setFields(cls, clsFields);
		}

		public synchronized void setFields(Class cls, Field[] clsFields)
		{
			if (cls == null)
				throw new NullPointerException();
			if(clsFields == null)
				clsFields = XStaticFixedValue.nullFieldArray;
			this.cls = cls;
			this.Hash.clear();
			for (Field field:clsFields)
				Hash.put(field.getName(), field);
		}
	}





	public static class constructor
	{

		private Class cls;
		private HashMapUtils9<Integer,Constructor[]> constructorHash = new HashMapUtils9<>(); //MethodName ->  ClassKey -> Method
		public Constructor getConstructor(Class[] ParameterTypesClass)
		{
			Constructor[] cs = constructorHash.get(toHash(ParameterTypesClass));
			if (cs == null)
				return null;
			if (cs.length == 1)
				return cs[0];

			for (Constructor m:cs)
				if (XArraysUtils.arrayContentsEquals(m.getParameterTypes(), ParameterTypesClass))
					return m;
			return null;
		}
		public constructor(Class cls)
		{
			if (cls == null)
				throw new NullPointerException();
			setConstructors(cls, getConstructors(cls));
		}
		public constructor(Class cls, Constructor[] clsConstructors)
		{
			if (cls == null)
				throw new NullPointerException();
			setConstructors(cls, clsConstructors);
		}


		public synchronized void setConstructors(Class cls, Constructor[] clsConstructors)
		{
			if (cls == null)
				throw new NullPointerException();
			if(clsConstructors == null)
				clsConstructors = XStaticFixedValue.nullConstructorArray;
			this.cls = cls;
			this.constructorHash.clear();
			HashMapUtils9<Integer,List<Constructor>> Hash = new HashMapUtils9<>(); 
			for (Constructor constructor:clsConstructors)
			{
				Class[] param = constructor.getParameterTypes();
				int clssKEY = toHash(param);

				List<Constructor>methods = Hash.get(clssKEY);
				if (methods == null)
					methods = new ArrayListUtils<>();
				methods.add(constructor);

				Hash.put(clssKEY, methods);
			}
			HashMapUtils9<Integer,Constructor[]> h2 = new HashMapUtils9<>();
			for (Integer ii :Hash.keySet())
			{
				List<Constructor> list = Hash.get(ii);
				Constructor[] newMethod = new Constructor[list == null ?0: list.size()];
				if (list != null)
					list.toArray(newMethod);
				h2.put(ii, newMethod);
			}
			this.constructorHash = h2;
		}
	}










	public static class method 
	{
		private Class cls;
		private HashMapUtils9<String,HashMapUtils9<Integer,Method[]>> methodHashMap = new HashMapUtils9<>(); //MethodName ->  ClassKey -> Method
		public Method getMethod(String MethodName, Class[] ParameterTypesClass)
		{
			HashMapUtils9<Integer,Method[]> Hash2 = methodHashMap.get(MethodName);
			if (Hash2 == null)
				return null;
			Method[] ms = Hash2.get(toHash(ParameterTypesClass));
			if (ms == null)
				return null;
			if (ms.length == 1)
				return ms[0];

			for (Method m:ms)
				if (XArraysUtils.arrayContentsEquals(m.getParameterTypes(), ParameterTypesClass))
					return m;
			return null;
		}
		public method(Class cls)
		{
			if (cls == null)
				throw new NullPointerException();
			setMethods(cls, getMethods(cls));
		}
		public method(Class cls, Method[] clsMethods)
		{
			if (cls == null)
				throw new NullPointerException();
			setMethods(cls, clsMethods);
		}

		public synchronized void setMethods(Class cls, Method[] clsMethods)
		{
			if (cls == null)
				throw new NullPointerException();
			if(clsMethods == null)
				clsMethods = XStaticFixedValue.nullMethodArray;
			this.cls = cls;
			this.methodHashMap.clear();
			HashMapUtils9<String,HashMapUtils9<Integer,List<Method>>> Hash = new HashMapUtils9<>(); 

			for (Method method:clsMethods)
			{
				Class[] param = method.getParameterTypes();
				int clssKEY = toHash(param);
				String MethodName = method.getName();

				HashMapUtils9<Integer,List<Method>> xmap = Hash.get(MethodName);
				if (xmap == null)
					xmap = new HashMapUtils9<Integer,List<Method>>();

				List<Method>methods = xmap.get(clssKEY);
				if (methods == null)
					methods = new ArrayListUtils<>();
				methods.add(method);

				xmap.put(clssKEY, methods);

				Hash.put(MethodName, xmap);
			}
			for (String name:Hash.keySet())
			{
				HashMapUtils9<Integer,List<Method>> h = Hash.get(name);

				HashMapUtils9<Integer,Method[]> h2 = new HashMapUtils9<>();
				for (Integer ii :h.keySet())
				{
					List<Method> list = h.get(ii);
					Method[] newMethod = new Method[list == null ?0: list.size()];
					if (list != null)
						list.toArray(newMethod);
					h2.put(ii, newMethod);
				}
				this.methodHashMap.put(name, h2);
			}
		}
	}
}

