package top.fols.box.lang.reflect;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import top.fols.box.statics.XStaticFixedValue;
public class XReflect
{
	public static Constructor[] getConstructors(Class Cls)
	{
		if (Cls == null)
			return null;
		return Cls.getConstructors();
	}
	public static Method[] getMethods(Class Cls)
	{
		if (Cls == null)
			return null;
		return Cls.getMethods();
	}
	public static Field[] getFields(Class Cls)
	{
		if (Cls == null)
			return null;
		return Cls.getFields();
	}

	public static Class getClass(Object obj)
	{
		return obj == null ?null: obj.getClass();
	}


	public static Method findMethod(Class ClassV, String functionName , Class...classs)
	{ 

		if (classs == null)
			classs = XStaticFixedValue.nullClassArray;
		Method m =  XFastReflect.defaultInstance.getMethod(ClassV, functionName, classs);
		return m;
	}
	//执行方法 方法名(functionName)  传入参数(object)的Class(classname) 传入参数(object)
	public static Object execMethod(Method method , Object... object) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, SecurityException, InvocationTargetException
	{
		return execMethod(null, method, object);
	}
	//传入参数(object) 比 传入Class名称(classname) 多时就取 (0,classname.length)
	public static Object execMethod(Object Instance, Method method , Object... object) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, SecurityException, InvocationTargetException
	{
		if (object == null)
			object = XStaticFixedValue.nullObjectArray;
		return method.invoke(Instance, object);
	}



	//搜索构造方法 方法名(name) {可选}指定传入参数(object)的Class(classname)
	public static Constructor findInstance(Object ClassV, Class...classs)
	{
		return findInstance(getClass(ClassV), classs);
	}
	public static Constructor findInstance(Class ClassV, Class...classs)
	{
		if (classs == null)
			classs = XStaticFixedValue.nullClassArray;
		Constructor  c = XFastReflect.defaultInstance.getConstructor(ClassV, classs);
		return c;
	}
	//构建 new 传入参数(object) 传入参数(object)的Class(classname)
	public static Object newInstance(Constructor c , Object... object) throws InstantiationException, InvocationTargetException, SecurityException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException 
	{
		if (object == null || object.length == 0)
			return c.newInstance();
		return c.newInstance(object);
	}




	//获取变量类型
	public static Class getFieldType(Field idF)
	{
		return idF.getType();
	}

	//获取变量值 将会打破封装
	public static Object getFieldValue(Class ClassV, Object Instance, String name) throws IllegalAccessException, IllegalArgumentException 
	{
		//获取id属性
		Field idF = XFastReflect.defaultInstance.getField(ClassV, name);
		return idF.get(Instance);
	}
	public static Object getFieldValue(Object Instance, String name) throws IllegalAccessException, IllegalArgumentException
	{
		return getFieldValue(Instance.getClass(), Instance, name);
	}


	public static void setFieldValue(Object Instance, String name, Object Value) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException 
	{
		setFieldValue(Instance, XFastReflect.defaultInstance.getField(Instance.getClass(), name), Value);
	}


	public static void setFieldValue(Field field , Object newFieldValue) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException 
	{
		setFieldValue(null, field, newFieldValue);
	}
	public static void setFieldValue(Object object, Field field , Object newFieldValue) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException
	{
		field.set(object, newFieldValue);
	}
	public static void setFieldValue(Class cls, String fieldName, Object newFieldValue) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException
	{
		Field field = XFastReflect.defaultInstance.getField(cls, fieldName);
		setFieldValue(field, newFieldValue);
	}






	//获取方法返回类型
	public static Class getMethodReturnType(Method method)
	{
		return method.getReturnType();
	}



	public static String getName(Member object)
	{
		if (object == null)
			return null;
		return object.getName();
	}

	//获取变量的修饰符
	public static int getModifiers(Member idF)
	{
		//打破封装
		if (idF == null)
			return -1;
		return idF.getModifiers();
	}




	public static String getModifiersStr(int method)
	{
		return Modifier.toString(method);
	}
	public static String[] getNames(Class[] typearr)
	{
		String[] type = new String[typearr.length];
		for (int i =0;i < typearr.length;i++)
			type[i] = typearr[i].getName();
		return type;
	}


//	public static class general
//	{
//		
//		public static Map <Class,Boolean>  getCanCastClass(Class c)
//		{
//			Map <Class,Boolean> list = new HashMapUtils <Class,Boolean>();
//			if (c == null)
//				return list;
//			list.put(Object.class, true);
//			list.put(c, true);
//			getSuperClass(list, c);
//			getInterface(list, c);
//			return list;
//		}
//
//		static void getSuperClass(Map <Class,Boolean>list, Class c)
//		{
//			if (c == null)
//				return;
//			Class CS = c.getSuperclass();
//			if (CS == null)
//			{
//				getInterface(list, CS);
//				return;
//			}
//			else
//			{
//				while (true)
//				{
//					list.put(CS, true);
//					getInterface(list, CS);
//					CS = CS.getSuperclass();
//					if (CS == null)
//						break;
//				}
//			}
//		}
//		static void getInterface(Map <Class,Boolean> list, Class c)
//		{
//			if (c == null)
//				return;
//			Class[] CS = c.getInterfaces();
//			if (CS == null || CS.length == 0)
//				return;
//			for (Class cls:CS)
//				list.put(cls, true);
//		}
//	}
}
