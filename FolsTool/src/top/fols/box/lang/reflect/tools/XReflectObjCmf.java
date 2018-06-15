package top.fols.box.lang.reflect.tools;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import top.fols.box.lang.XClassUtils;
import top.fols.box.lang.reflect.XReflect;
import top.fols.box.statics.XStaticBaseType;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.ArrayListUtils;
import top.fols.box.util.HashMapUtils9;
import top.fols.box.util.XArrays;
public class XReflectObjCmf
{
	
	public static final XReflectObjCmf defaultInstance = new XReflectObjCmf();
	
	private Map<Class,Constructor[]> hashClassConstructor = new HashMapUtils9<>();
	public void clearClassConstructorCache()
	{
		hashClassConstructor.clear();
	}
	public Set<Class> listClassConstructorCache()
	{
		return hashClassConstructor.keySet();
	}
	public void removeClassConstructorCache(Class cls)
	{
		hashClassConstructor.remove(cls);
	}
	public Constructor[] updateClassConstructorCache(Class cls, Constructor[] newCS)
	{
		hashClassConstructor.put(cls, newCS);
		return newCS;
	}
	public Constructor[] getClassConstructorCache(Class cls)
	{
		return hashClassConstructor.get(cls);
	}

	public Constructor getObjInstance(Class<?> cls, Object... objArr)
	{
		return getObjInstance(true,cls,objArr);
	}
	public Constructor getObjInstance(boolean IgnoreOneTheParameterMatchingOfAResult,Class<?> cls, Object... objArr)
	{
		Constructor[] constructors = hashClassConstructor.get(cls);
		if (constructors == null)
			constructors = updateClassConstructorCache(cls, XReflect.getConstructors(cls));
		if (constructors == null)
			return null;
		int forlength = constructors.length;
		if (forlength == 1 && IgnoreOneTheParameterMatchingOfAResult)
			return constructors[0];
		for (int i = 0;i < forlength;i++)
		{
			Constructor constructor2 = constructors[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == objArr.length)
			{
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++)
				{
					if (!XClassUtils.isInstance(objArr[i2], parameterTypes[i2]))
					{
						b = false;
						break;
					}	
				}
				if (b)
				{
					return constructor2;
				}
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (Constructor constructor22 : constructors)
		{
			stringBuilder.append('\t');
			stringBuilder.append(constructor22.toString());
			stringBuilder.append('\n');
		}
		throw new RuntimeException("invalid constructor method call. invalid parameters.\n" + stringBuilder.toString());
	}



	public Constructor[] getObjInstances(Class<?> cls, Object... objArr)
	{
		return getObjInstances(true,cls,objArr);
	}
	public Constructor[] getObjInstances(boolean IgnoreOneTheParameterMatchingOfAResult,Class<?> cls, Object...objArr) 
	{
		Constructor[] constructors = hashClassConstructor.get(cls);
		if (constructors == null)
			constructors = updateClassConstructorCache(cls, XReflect.getConstructors(cls));
		if (constructors == null)
			return null;
		int forlength = constructors.length;
		if (forlength == 1 && IgnoreOneTheParameterMatchingOfAResult)
			return new Constructor[]{constructors[0]};
		else if (forlength == 0)
			return XStaticFixedValue.nullConstructorArray;
		Constructor[] cs2 = null;
		int index = 0;
		for (int i = 0;i < forlength;i++)
		{
			Constructor constructor2 = constructors[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == objArr.length)
			{
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++)
				{
					if (!XClassUtils.isInstance(objArr[i2], parameterTypes[i2]))
					{
						b = false;
						break;
					}	
				}
				if (b)
				{
					if (cs2 == null)
						cs2 = new Constructor[constructors.length];
					cs2[index] = constructor2;
					index++;
				}
			}
		}
		if (cs2 != null)
			return Arrays.copyOf(cs2, index);
		else
			return XStaticFixedValue.nullConstructorArray;
	}




	private Map<Class,Map<String,Method[]>>hashClassMethod = new HashMapUtils9<>();
	public void clearClassMethodCache()
	{
		hashClassMethod.clear();
	}
	public Set<Class> listClassMethodCache()
	{
		return hashClassMethod.keySet();
	}
	public void removeClassMethodCache(Class cls)
	{
		hashClassMethod.remove(cls);
	}
	public Map<String,Method[]> updateClassMethodCache(Class cls, Method[] newMethods)
	{
		Map<String,Method[]> Methods = new HashMapUtils9<>();

		Map<String,List<Method>> Methods2 = new HashMapUtils9<>();
		Method[] ms =  newMethods;
		for (Method m : ms)
		{
			List<Method> list = Methods2.get(m.getName());
			if (list == null)
				list = new ArrayListUtils<>();
			list.add(m);
			Methods2.put(m.getName(), list);
		}
		for (String m:Methods2.keySet())
		{
			Methods.put(m, (Method[])XArrays.copyOf(Methods2.get(m).toArray(), XStaticBaseType.Method_class));
		}
		Methods2.clear();
		hashClassMethod.put(cls, Methods);
		return Methods;
	}
	public Method[] getClassMethodCache(Class cls, String MethodName)
	{
		return hashClassMethod.get(cls).get(MethodName);
	}
	public Set<String> getClassMethodCacheNameKeys(Class cls)
	{
		
		return hashClassMethod.get(cls).keySet();
	}

	public Method getObjMethod(Class<?> cls, String MethodName, Object... objArr) 
	{
		return getObjMethod(true,cls,MethodName,objArr);
	}
	public Method getObjMethod(boolean IgnoreOneTheParameterMatchingOfAResult,Class<?> cls, String MethodName, Object... objArr) 
	{
		if (MethodName == null)
			throw new NullPointerException();
		Map<String,Method[]> Methods = hashClassMethod.get(cls);
		if (Methods == null)
			Methods = updateClassMethodCache(cls, XReflect.getMethods(cls));
		Method[] constructors = Methods.get(MethodName);
		if (constructors == null)
			return null;
		int forlength = constructors.length;
		if (forlength == 1 && IgnoreOneTheParameterMatchingOfAResult)
			return constructors[0];
		for (int i = 0;i < forlength;i++)
		{
			Method constructor2 = constructors[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == objArr.length)
			{
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++)
				{
					if (!XClassUtils.isInstance(objArr[i2], parameterTypes[i2]))
					{
						b = false;
						break;
					}	
				}
				if (b)
				{
					return constructor2;
				}
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (Method constructor22 : constructors)
		{
			stringBuilder.append('\t');
			stringBuilder.append(constructor22.toString());
			stringBuilder.append('\n');
		}
		throw new RuntimeException("invalid method call. invalid parameters.\n" + stringBuilder.toString());
	}





	public Method[] getObjMethods(Class<?> cls, String MethodName, Object... objArr) 
	{
		return getObjMethods(true,cls,MethodName,objArr);
	}
	public Method[] getObjMethods(boolean IgnoreOneTheParameterMatchingOfAResult,Class<?> cls, String MethodName, Object... objArr)
	{
		if (MethodName == null)
			throw new NullPointerException();
		Map<String,Method[]> Methods = hashClassMethod.get(cls);
		if (Methods == null)
			Methods = updateClassMethodCache(cls, XReflect.getMethods(cls));
		Method[] constructors = Methods.get(MethodName);
		if (constructors == null)
			return null;
		int forlength = constructors.length;
		if (forlength == 1 && IgnoreOneTheParameterMatchingOfAResult)
			return new Method[]{constructors[0]};
		else if (forlength == 0)
			return XStaticFixedValue.nullMethodArray;
		Method[] cs2 = null;
		int index = 0;

		for (int i = 0;i < forlength;i++)
		{
			Method constructor2 = constructors[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == objArr.length)
			{
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++)
				{
					if (!XClassUtils.isInstance(objArr[i2], parameterTypes[i2]))
					{
						b = false;
						break;
					}	
				}
				if (b)
				{
					if (cs2 == null)
						cs2 = new Method[constructors.length];
					cs2[index] = constructor2;
					index++;
				}
			}
		}
		if (cs2 != null)
			return Arrays.copyOf(cs2, index);
		else
			return XStaticFixedValue.nullMethodArray;
	}
}



