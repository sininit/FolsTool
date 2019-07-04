package top.fols.box.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import top.fols.box.lang.XClass;
import top.fols.box.statics.XStaticFixedValue;

public class XObjects {
	public static <S> List<S> keys(Map<S,?> map) {
		if (null == map || map.size() == 0)
			return XStaticFixedValue.nullList;
		List<S> list = new ArrayList<S>(map.size());
		Set<S> set = map.keySet();
		for (S key:set)
			list.add(key);
		return list;
	}

	public static <T> T requireNonNull(T obj) {
        if (null == obj)
            throw new NullPointerException();
        return obj;
    }
	public static <T> T requireNonNull(T obj, String errorMessage) {
        if (null == obj)
            throw new NullPointerException(errorMessage);
        return obj;
    }

	public static <T> T requireInstanceOf(T obj, Class cls) {
        if (!XClass.isInstance(obj, cls))
            throw new ClassCastException((null == obj ?null: obj.getClass().getCanonicalName()) + " cannot cast to " + cls.getCanonicalName());
        return obj;
    }
	public static <T> T requireInstanceOf(T obj, Class cls, String errorMessage) {
        if (!XClass.isInstance(obj, cls))
            throw new ClassCastException(errorMessage);
        return obj;
    }

	public static boolean isEquals(Object obj, Object obj2) {
		if (null == obj) {
			if (null == obj2)
				return true;
			return false;
		} else {
			if (obj.equals(obj2))
				return true;
			return false;
		}
    }
	public static Object[] array(Object ... str) { return str; }
	public static String[] array(String ... str) { return str; }

	public static long[] array(long... str) { return str; }
	public static int[] array(int ... str) { return str; }
	public static short[] array(short ... str) { return str; }
	public static byte[] array(byte ... str) { return str;}
	public static boolean[] array(boolean ... str) { return str; }
	public static double[] array(double ... str) { return str; }
	public static float[] array(float ... str) { return str; }
	public static char[] array(char ... str) { return str; }

	public static Long[] array(Long... str) { return str; }
	public static Integer[] array(Integer ... str) { return str; }
	public static Short[] array(Short ... str) { return str; }
	public static Byte[] array(Byte ... str) { return str; }
	public static Boolean[] array(Boolean ... str) { return str; }
	public static Double[] array(Double ... str) { return str; }
	public static Float[] array(Float ... str) { return str; }
	public static Character[] array(Character ... str) { return str; }
	
	
	
	
	
	public static boolean isEmpty(StringBuffer obj) { return null == obj || obj.length() == 0; }
	public static boolean isEmpty(StringBuilder obj) { return null == obj || obj.length() == 0; }
	public static boolean isEmpty(Object obj) { return null == obj; }
	public static boolean isEmpty(Collection obj) { return null == obj || obj.size() == 0; }
	public static boolean isEmpty(String obj) { return null == obj || obj.length() == 0; }
	public static boolean isEmpty(Object[] obj) { return null == obj || obj.length == 0; }
	public static boolean isEmpty(long[] obj) { return null == obj || obj.length == 0; }
	public static boolean isEmpty(int[] obj) { return null == obj || obj.length == 0; }
	public static boolean isEmpty(short[] obj) { return null == obj || obj.length == 0; }
	public static boolean isEmpty(byte[] obj) { return null == obj || obj.length == 0; }
	public static boolean isEmpty(boolean[] obj) { return null == obj || obj.length == 0; }
	public static boolean isEmpty(double[] obj) { return null == obj || obj.length == 0; }
	public static boolean isEmpty(float[] obj) { return null == obj || obj.length == 0; }
	public static boolean isEmpty(char[] obj) { return null == obj || obj.length == 0; }
	
	
	
	/*
	 toString(char[]);
	 toString(byte[]);
	 toString(byte[],String coding);
	 toString(Object);
	 */
	public static String toString(Object objArr,String codeing) {
		if (null == objArr)
			return null;
		if (objArr instanceof String)
			return (String) objArr;
		else if (objArr instanceof char[])
			return new String((char[])objArr);
		else if (objArr instanceof byte[]) {
			return null == codeing
				? new String((byte[]) objArr) 
				: new String((byte[]) objArr, Charset.forName(codeing));
		} else
			return objArr.toString();
    }
	public static char tochar(Object objArr) {
        if (null == objArr)
			return 0;
		if (objArr instanceof Character)
			return ((Character)objArr).charValue();
		throw new ClassCastException(String.format("%s not can cast to array", objArr.getClass().getName()));
    }
	public static boolean toboolean(Object objArr) {
        if (null == objArr)
            return false;
        if (objArr instanceof Boolean)
			return ((Boolean)objArr).booleanValue();
		return Boolean.parseBoolean(objArr.toString().trim());
    }
    public static byte tobyte(Object objArr) {
        if (null == objArr)
			return 0;
		if (objArr instanceof Byte)
			return ((Byte)objArr).byteValue();
		return Byte.parseByte(objArr.toString().trim());
    }
	public static int toint(Object objArr) {
        if (null == objArr)
			return 0;
		if (objArr instanceof Integer)
			return ((Integer)objArr).intValue();
		return Integer.parseInt(objArr.toString().trim());
    }
	public static long tolong(Object objArr) {
        if (null == objArr)
            return 0;
		if (objArr instanceof Long)
			return ((Long)objArr).longValue();
		return Long.parseLong(objArr.toString().trim());
    }
	public static short toshort(Object objArr) {
        if (null == objArr)
			return 0;
		if (objArr instanceof Short)
			return ((Short)objArr).shortValue();
		return Short.parseShort(objArr.toString().trim());
    }
	/**
	 xx.xxx
	 **/
    public static double todouble(Object objArr) {
        if (null == objArr)
			return 0;
		else if (objArr instanceof Double)
			return ((Double)objArr).doubleValue();
		return Double.parseDouble(objArr.toString().trim());
    }
	/**
	 xx.xxx
	 **/
	public static float tofloat(Object objArr) {
        if (null == objArr)
			return 0;
		if (objArr instanceof Float)
			return ((Float)objArr).floatValue();
		return Float.parseFloat(objArr.toString().trim());
    }
}
