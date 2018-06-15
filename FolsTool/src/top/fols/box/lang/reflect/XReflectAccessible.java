package top.fols.box.lang.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import top.fols.box.util.ArrayListUtils;
import top.fols.box.util.HashMapUtils9;
import top.fols.box.util.XArraysUtils;

public class XReflectAccessible
{
	public static void setAccessible(boolean b, AccessibleObject...a)
	{
		if (a == null)
			return ;
		for (AccessibleObject ai : a)
			try
			{
				ai.setAccessible(b);
			}
			catch (Exception e)
			{
				continue;
			}
	}
	public static void setAccessibleTrue(AccessibleObject... a)
	{
		setAccessible(true, a);
	}


	
	
	public static Constructor[] getDeclaredConstructors(Class Cls)
	{
		if (Cls == null)
			return null;
		return Cls.getDeclaredConstructors();
	}
	public static Method[] getDeclaredMethods(Class Cls)
	{
		if (Cls == null)
			return null;
		return Cls.getDeclaredMethods();
	}
	public static Field[] getDeclaredFields(Class Cls)
	{
		if (Cls == null)
			return null;
		return Cls.getDeclaredFields();
	}

	
	
	
	public static Field[] getFieldsAll(Class cls)
	{
		Field[] Methods = cls.getFields();
		HashMapUtils9<String,Field> hash = new HashMapUtils9<>();
		for (Field f:Methods)
			if (!hash.containsKey(f.getName()))
				hash.put(f.getName(), f);

		Field[] Fields = cls.getDeclaredFields();
		for (Field f:Fields)
			hash.put(f.getName(), f);

		Collection List = hash.values();
		Field[] t = new Field[List.size()];
		List.toArray(t);
		return t;
	}
	public static Constructor[] getConstructorsAll(Class cls)
	{
		Constructor[] t = cls.getDeclaredConstructors();
		return t;
	}
	public static Method[] getMethodsAll(Class cls)
	{
		List<Method> Methods = new ArrayListUtils<>();

		Method[] Methods0 = cls.getMethods();
		for (Method c:Methods0)
			Methods.add(c);

		Method[] Fields = cls.getDeclaredMethods();
		for (int i = 0;i < Fields.length;i++)
		{
			Method f = Fields[i];

			int ik = -1;
			for (int index = 0;index < Methods.size();index++)
			{
				Method f2 = Methods.get(index);
				if (f.getName().equals(f2.getName())  && XArraysUtils.arrayContentsEquals(f.getParameterTypes(), f2.getParameterTypes()))
				{
					ik = index;
					break;
				}
			}	
			if (ik != -1)
				Methods.set(ik, f);
			else
				Methods.add(f);
		}

		return Methods.toArray(new Method[Methods.size()]);
	}
	public static Method[] getMethodsAll(Class cls, String methodName)
	{
		if (cls == null || methodName == null)
			return null;
		List<Method> list = new ArrayListUtils<Method>();
		Method[] m = getMethodsAll(cls);
		for (int i = 0;i < m.length;i++)
			if (methodName.equals(m[i].getName()))
				list.add(m[i]);
		return list.toArray(new Method[list.size()]);
	}

	
	
	
	public static void setFieldValue(Object object, Field field , Object newFieldValue) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException
	{
		setAccessibleTrue(field);
		/*
		 Final 属性 的 变量 不能是内联变量
		 Final attribute variables cannot be inline

		 static final String a0 = null;						//可修改(Can modify)		// 非内联变量
		 static final String a = "a";						//不可修改(Can't modify)	// 内联变量
		 static final String a2 = new String("a");			//可修改(Can modify)		// 非内联变量
		 final String firstName = "Mike";					//不可修改(Can't modify)	// 内联变量
		 final String firstName2 = new String("Jordan");	//可修改(Can modify)		// 非内联变量
		 final float age = 50.5f;							//不可修改(Can't modify)	// 内联变量
		 final float age2 = new Float(50.5f);				//可修改(Can modify)		// 非内联变量
		 final String city;									//可修改(Can modify)		// 非内联变量
		 */
		if (Modifier.isFinal(field.getModifiers()))
		{
			Field modifiersField = getFinalModifiersField();
			XReflectAccessible.setAccessibleTrue(modifiersField);
			if (Modifier.isFinal(modifiersField.getInt(field)))
				modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		}
		field.set(object, newFieldValue);
	}
	
	
	
	
	private static Field getFinalModifiersField;
	public static Field getFinalModifiersField() throws NoSuchFieldException
	{
		if (getFinalModifiersField != null)
		{
			return getFinalModifiersField;
		}
		Field f = null;
		try
		{
			f = Field.class.getDeclaredField("accessFlags");
		}
		catch (NoSuchFieldException e)
		{
			try
			{
				f = Field.class.getDeclaredField("modifiers");
			}
			catch (NoSuchFieldException e1)
			{
				throw e;
			}
		}
		getFinalModifiersField = f;
		return f;
	}
	
	
}
