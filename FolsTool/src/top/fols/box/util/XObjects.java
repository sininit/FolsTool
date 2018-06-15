package top.fols.box.util;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import top.fols.box.lang.XClassUtils;
import top.fols.box.statics.XStaticBaseType;
import top.fols.box.statics.XStaticFixedValue;
public class XObjects
{
	public static <S> List<S> keys(Map<S,?> map)
	{
		if (map.size() == 0)
			return XStaticFixedValue.nullList;
		List<S> list = new ArrayListUtils<>();
		Set<S> set = map.keySet();
		for (S key:set)
			list.add(key);
		return list;
	}
	
	
	
	

	public static <T> T requireNonNull(T obj)
	{
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }
	public static <T> T requireNonNull(T obj, String errorMessage)
	{
        if (obj == null)
            throw new NullPointerException(errorMessage);
        return obj;
    }

	public static <T> T requireArray(T obj)
	{
        if (obj == null || requireNonNull(obj).getClass().isArray() == false)
            throw new RuntimeException((obj == null ?null: obj.getClass().getCanonicalName()) + " cannot cast to array");
        return obj;
    }
	public static <T> T requireArray(T obj, String errorMessage)
	{
        if (obj == null || requireNonNull(obj).getClass().isArray() == false)
            throw new RuntimeException(errorMessage);
        return obj;
    }

	public static <T> T requireInstanceOf(T obj, Class cls)
	{
        if (!XClassUtils.isInstance(obj, cls))
            throw new ClassCastException((obj == null ?null: obj.getClass().getCanonicalName()) + " cannot cast to " + cls.getCanonicalName());
        return obj;
    }
	public static <T> T requireInstanceOf(T obj, Class cls, String errorMessage)
	{

        if (!XClassUtils.isInstance(obj, cls))
            throw new ClassCastException(errorMessage);
        return obj;
    }






	public static RuntimeException notPermittedAccess()
	{
		return new RuntimeException("cannot operate. this object not permitted to access");
	}




	public static boolean isEquals(Object obj, Object obj2)
	{
		if (obj == null)
		{
			if (obj2 == null)
				return true;
			return false;

		}
		else
		{
			if (obj.equals(obj2))
				return true;
			return false;
		}
    }
	public static boolean isEmpty(Object obj)
	{
        return obj == null;
    }
	public static boolean isEmpty(Collection obj)
	{
		return obj == null || obj.size() == 0;
    }
	public static boolean isEmpty(String obj)
	{
        return obj == null || obj.length() == 0;
    }
	public static boolean isEmptyArray(Object[] obj)
	{
        return obj == null || obj.length == 0;
    }
	public static boolean isEmpty(long[] obj)
	{
        return obj == null || obj.length == 0;
    }
	public static boolean isEmpty(int[] obj)
	{
        return obj == null || obj.length == 0;
    }
	public static boolean isEmpty(short[] obj)
	{
        return obj == null || obj.length == 0;
    }
	public static boolean isEmpty(byte[] obj)
	{
        return obj == null || obj.length == 0;
    }
	public static boolean isEmpty(boolean[] obj)
	{
        return obj == null || obj.length == 0;
    }
	public static boolean isEmpty(double[] obj)
	{
        return obj == null || obj.length == 0;
    }
	public static boolean isEmpty(float[] obj)
	{
        return obj == null || obj.length == 0;
    }
	public static boolean isEmpty(char[] obj)
	{
        return obj == null || obj.length == 0;
    }




	public static String toString(Object... objArr) throws UnsupportedEncodingException
	{
		if (isEmpty(objArr) || objArr[0] == null)
			return null;
		if (objArr[0] instanceof String)
			return (String) objArr[0];
		else if (objArr[0] instanceof char[])
			return new String((char[])objArr[0]);
		else if (objArr[0] instanceof byte[])
		{
			String codeing = (objArr.length < 2 || !(objArr[1] instanceof String)) ? null : (String)objArr[1];
			return codeing == null ? new String((byte[]) objArr[0]) : new String((byte[]) objArr[0], codeing);
		}
		else
			return objArr[0].toString();
    }
	public static char toChar(Object objArr) 
	{
        if (objArr == null)
			return 0;
		if (objArr instanceof Character)
			return ((Character)objArr).charValue();
		throw new ClassCastException(String.format("%s not can cast to array", objArr.getClass().getName()));
    }
	public static boolean toBoolean(Object objArr) 
	{
        if (objArr == null)
            return false;
        if (objArr instanceof Boolean)
			return ((Boolean)objArr).booleanValue();
		return Boolean.parseBoolean(objArr.toString().trim());
    }
    public static byte toByte(Object objArr) 
	{
        if (objArr == null)
			return 0;
		if (objArr instanceof Byte)
			return ((Byte)objArr).byteValue();
		return Byte.parseByte(objArr.toString().trim());
    }
	public static int toInt(Object objArr) 
	{
        if (objArr == null)
			return 0;
		if (objArr instanceof Integer)
			return ((Integer)objArr).intValue();
		return Integer.parseInt(objArr.toString().trim());
    }
	public static long toLong(Object objArr) 
	{
        if (objArr == null)
            return 0;
		if (objArr instanceof Long)
			return ((Long)objArr).longValue();
		return Long.parseLong(objArr.toString().trim());
    }
	public static short toShort(Object objArr) 
	{
        if (objArr == null)
			return 0;
		if (objArr instanceof Short)
			return ((Short)objArr).shortValue();
		return Short.parseShort(objArr.toString().trim());
    }
	/**
	 xx.xxx
	 **/
    public static double toDouble(Object objArr) 
	{
        if (objArr == null)
			return 0;
		else if (objArr instanceof Double)
			return ((Double)objArr).doubleValue();
		return Double.parseDouble(objArr.toString().trim());
    }
	/**
	 xx.xxx
	 **/
	public static float toFloat(Object objArr) 
	{
        if (objArr == null)
			return 0;
		if (objArr instanceof Float)
			return ((Float)objArr).floatValue();
		return Float.parseFloat(objArr.toString().trim());
    }


	public static byte[] toByteArray(Object originArray)
	{
		if (originArray instanceof Collection)
			return toByteArray(((Collection)originArray).toArray());
		return (byte[])XArrays.copyOf(originArray, XStaticBaseType.byte_class);
	}
	public static long[] toLongArray(Object originArray)
	{
		if (originArray instanceof Collection)
			return toLongArray(((Collection)originArray).toArray());
		return (long[])XArrays.copyOf(originArray, XStaticBaseType.long_class);
	}
	public static double[] toDoubleArray(Object originArray)
	{
		if (originArray instanceof Collection)
			return toDoubleArray(((Collection)originArray).toArray());
		return (double[])XArrays.copyOf(originArray, XStaticBaseType.double_class);
	}
	public static char[] toCharArray(Object originArray)
	{
		if (originArray instanceof Collection)
			return toCharArray(((Collection)originArray).toArray());
		return (char[])XArrays.copyOf(originArray, XStaticBaseType.char_class);
	}
	public static int[] toIntArray(Object originArray)
	{
		if (originArray instanceof Collection)
			return toIntArray(((Collection)originArray).toArray());
		return (int[])XArrays.copyOf(originArray, XStaticBaseType.int_class);
	}
	public static boolean[] toBooleanArray(Object originArray)
	{
		if (originArray instanceof Collection)
			return toBooleanArray(((Collection)originArray).toArray());
		return (boolean[])XArrays.copyOf(originArray, XStaticBaseType.boolean_class);
	}
	public static float[] toFloatArray(Object originArray)
	{
		if (originArray instanceof Collection)
			return toFloatArray(((Collection)originArray).toArray());
		return (float[])XArrays.copyOf(originArray, XStaticBaseType.float_class);
	}
	public static short[] toShortArray(Object originArray)
	{
		if (originArray instanceof Collection)
			return toShortArray(((Collection)originArray).toArray());
		return (short[])XArrays.copyOf(originArray, XStaticBaseType.short_class);
	}
	public static String[] toStringArray(Object originArray)
	{
		if (originArray instanceof Collection)
			return toStringArray(((Collection)originArray).toArray());
		return (String[])XArrays.copyOf(originArray, XStaticBaseType.String_class);
	}
	public static Object[] toObjectArray(Object originArray)
	{
		if (originArray instanceof Collection)
			return toObjectArray(((Collection)originArray).toArray());
		return (Object[])XArrays.copyOf(originArray, XStaticBaseType.Object_class);
	}


}
