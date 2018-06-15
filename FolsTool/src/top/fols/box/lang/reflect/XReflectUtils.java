package top.fols.box.lang.reflect;
import top.fols.box.lang.XString;
import top.fols.box.util.XArrays;
import top.fols.box.statics.XStaticBaseType;
public class XReflectUtils
{
	//把简单的类地址转换为真实的类地址 Main[] >> [LMain;   String[] >> [Ljava.lang.String; byte[] >> [B
	public static String toAbsClassName(String Addres)
	{
		//没有这些地址
		if (XStaticBaseType.isBaseClassName(Addres))
			return Addres;
		//判断数组纬度
		int d = XArrays.getDimensional_ClassCanonicalName(Addres);
		StringBuffer startStr = new StringBuffer();
		for (int i = 0;i < d;i++)
			startStr.append('[');
		int start;
		String F;
		if ((start = Addres.indexOf("[]")) > -1)
		{
			Addres = Addres.substring(0, start);
			if (Addres.equals("byte"))
				F = "B";
			else if (Addres.equals("char"))
				F = "C";
			else if (Addres.equals("double"))
				F = "D";
			else if (Addres.equals("float"))
				F = "F";
			else if (Addres.equals("int"))
				F = "I";
			else if (Addres.equals("long"))
				F = "J";
			else if (Addres.equals("short"))
				F = "S";
			else if (Addres.equals("boolean"))
				F = "Z";
			else if (Addres.equals("void"))//注意 void[].class是无效的！！
				F = "V";
			else
				F = String.format("L%s;", Addres);
			return startStr.append(F).toString();
		}
		return Addres;
	}
	public static String[] toAbsClassName(String...ClsName)
	{
		if (ClsName == null || ClsName.length == 0)
			return null;
		String c[] = new String[ClsName.length];
		for (int i = 0;i < ClsName.length;i++)
			c[i] = ClsName[i] == null ?null: toAbsClassName(ClsName[i]);
		return c;
	}


	public static Class<?> forName(String AddresS,boolean initialize, java.lang.ClassLoader loader) throws ClassNotFoundException
	{
		String Addres = toAbsClassName(AddresS);
		Class c = XStaticBaseType.forName(Addres);
		if (c != null)
			return c;
		return Class.forName(Addres,initialize,loader);
	}
	
	

    //根据类名获取Class  byte[]  >> byte[].class ([B)    java.lang.String[] >> [Ljava.lang.String (String[].class);
	public static Class<?> forName(String AddresS) throws ClassNotFoundException
	{
		String Addres = toAbsClassName(AddresS);
		Class c = XStaticBaseType.forName(Addres);
		if (c != null)
			return c;
		return Class.forName(Addres);
	}
	
}
