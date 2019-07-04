package top.fols.box.lang;
import top.fols.box.statics.XStaticBaseType;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XArray;

public class XClass {


	/*
	 * public boolean isInstance(Object obj) {
	 * 	if (null == obj) { 
	 * 		return false;
	 * 	}
	 * 	return isAssignableFrom(obj.getClass());
	 * }
	 */
	public static boolean isInstance(Object obj, Class instanceCls) {
		if (null == instanceCls) return null == obj;
		if (instanceCls.isPrimitive() && null == obj) return false;
		if (!instanceCls.isPrimitive() && null == obj) return true;//数据强转 //((String)null)

		if (instanceCls.isPrimitive()) {
			if (instanceCls == XStaticBaseType.byte_class) return obj instanceof Byte;
			if (instanceCls == XStaticBaseType.long_class) return obj instanceof Long;
			if (instanceCls == XStaticBaseType.double_class) return obj instanceof Double;
			if (instanceCls == XStaticBaseType.char_class) return obj instanceof Character;
			if (instanceCls == XStaticBaseType.int_class) return obj instanceof Integer;
			if (instanceCls == XStaticBaseType.boolean_class) return obj instanceof Boolean;
			if (instanceCls == XStaticBaseType.float_class) return obj instanceof Float;
			if (instanceCls == XStaticBaseType.short_class) return obj instanceof Short;

			return false;
		} else {
//			if (instanceCls == XStaticBaseType.Byte_class) return obj instanceof Byte;
//			if (instanceCls == XStaticBaseType.Long_class) return obj instanceof Long;
//			if (instanceCls == XStaticBaseType.Double_class) return obj instanceof Double;
//			if (instanceCls == XStaticBaseType.Character_class) return obj instanceof Character;
//			if (instanceCls == XStaticBaseType.Integer_class) return obj instanceof Integer;
//			if (instanceCls == XStaticBaseType.Boolean_class) return obj instanceof Boolean;
//			if (instanceCls == XStaticBaseType.Float_class) return obj instanceof Float;
//			if (instanceCls == XStaticBaseType.Short_class) return obj instanceof Short;
		}
		return instanceCls.isInstance(obj); 
	}



	public static boolean isInstance(Class objcls, Class instanceCls) {
		if (null == instanceCls) return null == objcls;
		if (instanceCls.isPrimitive() && null == objcls) return false;
		if (!instanceCls.isPrimitive() && null == objcls) return true;//数据强转 //((String)null)

		if (instanceCls == XStaticBaseType.byte_class || instanceCls == XStaticBaseType.Byte_class) return objcls == XStaticBaseType.byte_class || objcls == XStaticBaseType.Byte_class;
		if (instanceCls == XStaticBaseType.long_class || instanceCls == XStaticBaseType.Long_class) return objcls == XStaticBaseType.long_class || objcls == XStaticBaseType.Long_class;
		if (instanceCls == XStaticBaseType.double_class || instanceCls == XStaticBaseType.Double_class) return objcls == XStaticBaseType.double_class || objcls == XStaticBaseType.Double_class;
		if (instanceCls == XStaticBaseType.char_class || instanceCls == XStaticBaseType.Character_class) return objcls == XStaticBaseType.char_class || objcls == XStaticBaseType.Character_class;
		if (instanceCls == XStaticBaseType.int_class || instanceCls == XStaticBaseType.Integer_class) return objcls == XStaticBaseType.int_class || objcls == XStaticBaseType.Integer_class;
		if (instanceCls == XStaticBaseType.boolean_class || instanceCls == XStaticBaseType.Boolean_class) return objcls == XStaticBaseType.boolean_class || objcls == XStaticBaseType.Boolean_class;
		if (instanceCls == XStaticBaseType.float_class || instanceCls == XStaticBaseType.Float_class) return objcls == XStaticBaseType.float_class || objcls == XStaticBaseType.Float_class;
		if (instanceCls == XStaticBaseType.short_class || instanceCls == XStaticBaseType.Short_class) return objcls == XStaticBaseType.short_class || objcls == XStaticBaseType.Short_class;

		return instanceCls.isAssignableFrom(objcls);
	}
	public static Class[] getObjArrClss(Object[] objs) {
		if (null == objs || objs.length == 0) return XStaticFixedValue.nullClassArray;
		Class[] newClass = new Class[objs.length];
		int ind = 0;
		for (Object oi: objs) newClass[ind++] = null == oi ?null: oi.getClass();
		return newClass;
	}
	
	
	
	public static boolean isBaseClassName(String ClassCanonicalName) {
		if (ClassCanonicalName.equals(XStaticBaseType.byte_classcanonicalname)
			|| ClassCanonicalName.equals(XStaticBaseType.char_classcanonicalname) 
			|| ClassCanonicalName.equals(XStaticBaseType.double_classcanonicalname)
			|| ClassCanonicalName.equals(XStaticBaseType.float_classcanonicalname) 
			|| ClassCanonicalName.equals(XStaticBaseType.int_classcanonicalname)
			|| ClassCanonicalName.equals(XStaticBaseType.long_classcanonicalname)
			|| ClassCanonicalName.equals(XStaticBaseType.short_classcanonicalname)
			|| ClassCanonicalName.equals(XStaticBaseType.boolean_classcanonicalname)
			|| ClassCanonicalName.equals(XStaticBaseType.void_classcanonicalname)
			)
			return true;
		return false;
	}
	public static Class baseClassForName(String ClassCanonicalName) {
		if (ClassCanonicalName.equals(XStaticBaseType.byte_classcanonicalname))
			return XStaticBaseType.byte_class;
		else if (ClassCanonicalName.equals(XStaticBaseType.char_classcanonicalname))
			return XStaticBaseType.char_class;
		else if (ClassCanonicalName.equals(XStaticBaseType.double_classcanonicalname))
			return XStaticBaseType.double_class;
		else if (ClassCanonicalName.equals(XStaticBaseType.float_classcanonicalname))
			return XStaticBaseType.float_class;
		else if (ClassCanonicalName.equals(XStaticBaseType.int_classcanonicalname))
			return XStaticBaseType.int_class;
		else if (ClassCanonicalName.equals(XStaticBaseType.long_classcanonicalname))
			return XStaticBaseType.long_class;
		else if (ClassCanonicalName.equals(XStaticBaseType.short_classcanonicalname))
			return XStaticBaseType.short_class;
		else if (ClassCanonicalName.equals(XStaticBaseType.boolean_classcanonicalname))
			return XStaticBaseType.boolean_class;
		else if (ClassCanonicalName.equals(XStaticBaseType.void_classcanonicalname))
			return XStaticBaseType.void_class;
		return null;
	}
	public static Class baseClassToJavaClass(Class baseClass) {
		if (baseClass == boolean.class)
			return Boolean.class;
		else if (baseClass == byte.class)
			return Byte.class;
		else if (baseClass == char.class)
			return Character.class;
		else if (baseClass == double.class)
			return Double.class;
		else if (baseClass == float.class)
			return Float.class;
		else if (baseClass == int.class)
			return Integer.class;
		else if (baseClass == long.class)
			return Long.class;
		else if (baseClass == short.class)
			return Short.class;
		return baseClass;
	}
	
	
	
	// 把简单的类地址转换为真实的类地址 Main[] >> [LMain;   String[] >> [Ljava.lang.String;      byte[] >> [B
	public static String toAbsClassName(String ClassName) {
		//没有这些地址
		if (XClass.isBaseClassName(ClassName))
			return ClassName;
		//判断数组纬度
		int d = XArray.getDimensionalFromClassCanonicalName(ClassName);
		StringBuilder startStr = new StringBuilder();
		for (int i = 0;i < d;i++)
			startStr.append('[');
		int start;
		String F;
		if ((start = ClassName.indexOf("[]")) > -1) {
			ClassName = ClassName.substring(0, start);
			if (ClassName.equals("byte"))
				F = "B";
			else if (ClassName.equals("char"))
				F = "C";
			else if (ClassName.equals("double"))
				F = "D";
			else if (ClassName.equals("float"))
				F = "F";
			else if (ClassName.equals("int"))
				F = "I";
			else if (ClassName.equals("long"))
				F = "J";
			else if (ClassName.equals("short"))
				F = "S";
			else if (ClassName.equals("boolean"))
				F = "Z";
			else if (ClassName.equals("void"))//注意 void[].class是无效的！！
				F = "V";
			else
				F = String.format("L%s;", ClassName);
			return startStr.append(F).toString();
		}
		return ClassName;
	}
	public static String[] toAbsClassName(String... ClassNames) {
		if (null == ClassNames || ClassNames.length == 0)
			return null;
		String c[] = new String[ClassNames.length];
		for (int i = 0;i < ClassNames.length;i++)
			c[i] = null == ClassNames[i] ?null: toAbsClassName(ClassNames[i]);
		return c;
	}




	public static Class forName(String AddresS, boolean initialize, java.lang.ClassLoader loader) throws ClassNotFoundException {
		String addres = toAbsClassName(AddresS);
		Class c = XClass.baseClassForName(addres);
		if (null != c)
			return c;
		return Class.forName(addres, initialize, loader);
	}
    //根据类名获取Class  byte[]  >> byte[].class ([B)    java.lang.String[] >> [Ljava.lang.String (String[].class);
	public static Class forName(String AddresS) throws ClassNotFoundException {
		String addres = toAbsClassName(AddresS);
		Class c = XClass.baseClassForName(addres);
		if (null != c)
			return c;
		return Class.forName(addres);
	}
	
	
	
	public static Class getClass(Object obj) {
		return null == obj ?null: obj.getClass();
	}
}
