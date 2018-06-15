package top.fols.box.util;
import java.lang.reflect.Array;
import java.util.Collection;
import top.fols.box.statics.XStaticBaseType;
public class XArrays
{
	public static void set(Object originalArray, int index, Object value) throws java.lang.IllegalArgumentException, java.lang.ArrayIndexOutOfBoundsException
	{
		Array.set(originalArray, index, value);
	}
	public static Object get(Object originalArray, int index) throws java.lang.IllegalArgumentException, java.lang.ArrayIndexOutOfBoundsException
	{
		return Array.get(originalArray, index);
	}
	public static int getLength(Object originalArray)
	{
		return Array.getLength(originalArray);
	}
	public static Object newInstance(java.lang.Class<?> componentType, int length)
	{
		return Array.newInstance(componentType, length);
	}
	public static java.lang.Object newInstance(java.lang.Class<?> componentType, int...dimensions) throws java.lang.IllegalArgumentException, java.lang.NegativeArraySizeException
	{
		return Array.newInstance(componentType, dimensions);
	}








	/*
	 array type:

	 Object[]
	 byte[]
	 long[]
	 double[]
	 char[]
	 int[]
	 boolean[]
	 float[]
	 short[]
	 //void[]
	 */

	public static void arraycopy(String originStr, int originStrCopyOffIndex, char[] toarray, int off, int len)
	{
		if (originStr == null)
			return;
		originStr.getChars(originStrCopyOffIndex, originStrCopyOffIndex + len, toarray, off);
	}
	public static Object arraycopy(Object originarray, int originarrayCopyOffIndex, Object toarray, int off, int len)
	{
		if (len <= 0)
			return toarray;
		System.arraycopy(originarray, originarrayCopyOffIndex, toarray, off, len);
		return toarray;
	}
	/*
	 copy array traverse
	 使用遍历方式复制数组
	 参数与arraycopy同
	 */
	public static void arraycopyTraverse(byte[]originArray, int originarrayCopyOffIndex, Object newArray, int off, int len)
	{
		if (newArray instanceof byte[])
		{
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		}
		else if (newArray instanceof long[])
		{
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof double[])
		{
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof char[])
		{
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (char)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof int[])
		{
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof boolean[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof float[])
		{
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof short[])
		{
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof String[])
		{
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		}
		else
		{
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(long[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len)
	{
		if (newArray instanceof byte[])
		{
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof long[])
		{
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		}
		else if (newArray instanceof double[])
		{
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof char[])
		{
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (char)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof int[])
		{
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (int)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof boolean[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof float[])
		{
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof short[])
		{
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (short)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof String[])
		{
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		}
		else
		{
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(double[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len)
	{
		if (newArray instanceof byte[])
		{
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof long[])
		{
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (long)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof double[])
		{
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		}
		else if (newArray instanceof char[])
		{
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (char)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof int[])
		{
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (int)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof boolean[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof float[])
		{
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (float)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof short[])
		{
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (short)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof String[])
		{
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		}
		else
		{
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(char[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len)
	{
		if (newArray instanceof byte[])
		{
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof long[])
		{
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof double[])
		{
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof char[])
		{
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		}
		else if (newArray instanceof int[])
		{
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof boolean[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof float[])
		{
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof short[])
		{
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (short)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof String[])
		{
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		}
		else
		{
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(int[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len)
	{
		if (newArray instanceof byte[])
		{
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof long[])
		{
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof double[])
		{
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof char[])
		{
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ( char)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof int[])
		{
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		}
		else if (newArray instanceof boolean[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof float[])
		{
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof short[])
		{
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (short)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof String[])
		{
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		}
		else
		{
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(boolean[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len)
	{
		if (newArray instanceof byte[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof long[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof double[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof char[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof int[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof boolean[])
		{
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		}
		else if (newArray instanceof float[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof short[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof String[])
		{
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		}
		else
		{
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(float[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len)
	{
		if (newArray instanceof byte[])
		{
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof long[])
		{
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (long)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof double[])
		{
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof char[])
		{
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (char)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof int[])
		{
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (int)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof boolean[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof float[])
		{
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		}
		else if (newArray instanceof short[])
		{
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (short)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof String[])
		{
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		}
		else
		{
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(short[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len)
	{
		if (newArray instanceof byte[])
		{
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof long[])
		{
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof double[])
		{
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof char[])
		{
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (char)originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof int[])
		{
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof boolean[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof float[])
		{
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
		else if (newArray instanceof short[])
		{
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		}
		else if (newArray instanceof String[])
		{
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		}
		else
		{
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(String[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len)
	{
		if (newArray instanceof byte[])
		{
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Byte.parseByte(originArray[i + originarrayCopyOffIndex]);
		}
		else if (newArray instanceof long[])
		{
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Long.parseLong(originArray[i + originarrayCopyOffIndex]);
		}
		else if (newArray instanceof double[])
		{
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Double.parseDouble(originArray[i + originarrayCopyOffIndex]);
		}
		else if (newArray instanceof char[])
		{
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		}
		else if (newArray instanceof int[])
		{
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Integer.parseInt(originArray[i + originarrayCopyOffIndex]);
		}
		else if (newArray instanceof boolean[])
		{
			boolean[] array = (boolean[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = "true".equals(originArray[i + originarrayCopyOffIndex]);
		}
		else if (newArray instanceof float[])
		{
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Float.parseFloat(originArray[i + originarrayCopyOffIndex]);
		}
		else if (newArray instanceof short[])
		{
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Short.parseShort(originArray[i + originarrayCopyOffIndex]);
		}
		else if (newArray instanceof String[])
		{
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		}
		else
		{
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(Object[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len)
	{
		if (newArray instanceof byte[])
		{
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Byte)originArray[i + originarrayCopyOffIndex]).byteValue();
		}
		else if (newArray instanceof long[])
		{
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Long)originArray[i + originarrayCopyOffIndex]).longValue();
		}
		else if (newArray instanceof double[])
		{
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Double)originArray[i + originarrayCopyOffIndex]).doubleValue();
		}
		else if (newArray instanceof char[])
		{
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Character)originArray[i + originarrayCopyOffIndex]).charValue();
		}
		else if (newArray instanceof int[])
		{
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Integer)originArray[i + originarrayCopyOffIndex]).intValue();
		}
		else if (newArray instanceof boolean[])
		{
			boolean[] array = (boolean[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Boolean)originArray[i + originarrayCopyOffIndex]).booleanValue();
		}
		else if (newArray instanceof float[])
		{
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Float)originArray[i + originarrayCopyOffIndex]).floatValue();
		}
		else if (newArray instanceof short[])
		{
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Short)originArray[i + originarrayCopyOffIndex]).shortValue();
		}
		else if (newArray instanceof String[])
		{
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		}
		else
		{
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		}
	}
	public static Object arraycopyTraverse(java.lang.Object originalArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len)
	{
		if (originalArray instanceof byte[])
			arraycopyTraverse((byte[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof long[])
			arraycopyTraverse((long[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof double[])
			arraycopyTraverse((double[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof char[])
			arraycopyTraverse((char[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof int[])
			arraycopyTraverse((int[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof boolean[])
			arraycopyTraverse((boolean[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof float[])
			arraycopyTraverse((float[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof short[])
			arraycopyTraverse((short[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof String[])
			arraycopyTraverse((String[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else
			arraycopyTraverse((Object[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		return newArray;
	}








	/**
	 Traversal recursive all elements
	 判断数组是否相等 如果数组为多维数组就会自动递归判断    也就是说会递归遍历所有元素来判断

	 equals(new int[][]{{0},{0},{0},{0,0,0,0,0,0,0,0}},new int[][]{{0},{0},{0},{0,0,0,0,0,0,0,0}}); >> true
	 equals(new int[][]{{0},{0},{0},{0,0,0,0,0,0,0,0}},new int[][]{{0},{0},{0},{0,0,9527,0,0,0,0,666}}); >> false
	 equals(new int[]{0,0,0,0,0,0,0,0},new int[]{0,0,9527,0,0,0,0,666}); >> false
     equals(new long[]{0,0,0,0,0,0,0,0},new int[]{0,0,0,0,0,0,0,0}); >> false
	 equals(new Object[]{new int[]{1,2,3}},new int[][]{new int[]{1,2,3}}); >> true

	 Main m[] = new Main[]{new Main()};
	 equals(new Object[]{new Object[]{1,2,3,4},new Object[]{new Object[]{m,new long[]{1,2,3,4}}}}  ,  new Object[]{new Object[]{1,2,3,4},new Object[]{new Object[]{m,new long[]{1,2,3,4}}}}); >> true
	 equals(new Object[]{new Object[]{1,2,3,4},new Object[]{new Object[]{new Main[]{new Main()},new long[]{1,2,3,4}}}}  ,  new Object[]{new Object[]{1,2,3,4},new Object[]{new Object[]{new Main[]{new Main()},new long[]{1,2,3,4}}}}); >> false
	 **/


	public static boolean equals(byte[] originalArray, byte[] objectArray)
	{
		if (originalArray == objectArray)
			return true;
		if (originalArray == null || objectArray == null)
			return false;
		if (originalArray.length != objectArray.length) 
			return false;
		for (int i = 0;i < originalArray.length;i++)
			if (originalArray[i] != objectArray[i])
				return false;
		return true;
	}
	public static boolean equals(long[] originalArray, long[] objectArray)
	{
		if (originalArray == objectArray)
			return true;
		if (originalArray == null || objectArray == null)
			return false;
		if (originalArray.length != objectArray.length) 
			return false;
		for (int i = 0;i < originalArray.length;i++)
			if (originalArray[i] != objectArray[i])
				return false;
		return true;
	}
	public static boolean equals(char[] originalArray, char[] objectArray)
	{
		if (originalArray == objectArray)
			return true;
		if (originalArray == null || objectArray == null)
			return false;
		if (originalArray.length != objectArray.length) 
			return false;
		for (int i = 0;i < originalArray.length;i++)
			if (originalArray[i] != objectArray[i])
				return false;
		return true;
	}
	public static boolean equals(int[] originalArray, int[] objectArray)
	{
		if (originalArray == objectArray)
			return true;
		if (originalArray == null || objectArray == null)
			return false;
		if (originalArray.length != objectArray.length) 
			return false;
		for (int i = 0;i < originalArray.length;i++)
			if (originalArray[i] != objectArray[i])
				return false;
		return true;
	}
	public static boolean equals(short[] originalArray, short[] objectArray)
	{
		if (originalArray == objectArray)
			return true;
		if (originalArray == null || objectArray == null)
			return false;
		if (originalArray.length != objectArray.length) 
			return false;
		for (int i = 0;i < originalArray.length;i++)
			if (originalArray[i] != objectArray[i])
				return false;
		return true;
	}
	public static boolean equals(boolean[] originalArray, boolean[] objectArray)
	{
		if (originalArray == objectArray)
			return true;
		if (originalArray == null || objectArray == null)
			return false;
		if (originalArray.length != objectArray.length) 
			return false;
		for (int i = 0;i < originalArray.length;i++)
			if (originalArray[i] != objectArray[i])
				return false;
		return true;
	}
	public static boolean equals(double[] originalArray, double[] objectArray)
	{
		if (originalArray == objectArray)
			return true;
		if (originalArray == null || objectArray == null)
			return false;
		if (originalArray.length != objectArray.length) 
			return false;
		for (int i = 0;i < originalArray.length;i++)
			if (originalArray[i] != objectArray[i])
				return false;
		return true;
	}
	public static boolean equals(float[] originalArray, float[] objectArray)
	{
		if (originalArray == objectArray)
			return true;
		if (originalArray == null || objectArray == null)
			return false;
		if (originalArray.length != objectArray.length) 
			return false;
		for (int i = 0;i < originalArray.length;i++)
			if (originalArray[i] != objectArray[i])
				return false;
		return true;
	}
	public static boolean equals(Object originalArray, Object objectArray)
	{
		if (originalArray == null || objectArray == null || !equalsArrayType(originalArray, objectArray))
			return false;
		else if (originalArray instanceof Object[])
			return equals((Object[])originalArray, (Object[])objectArray);
		else if (originalArray instanceof byte[])
			return equals((byte[])originalArray, (byte[])objectArray);
		else if (originalArray instanceof long[])
			return equals((long[])originalArray, (long[])objectArray);
		else if (originalArray instanceof char[])
			return equals((char[])originalArray, (char[])objectArray);
		else if (originalArray instanceof int[])
			return equals((int[])originalArray, (int[])objectArray);
		else if (originalArray instanceof short[])
			return equals((short[])originalArray, (short[])objectArray);
		else if (originalArray instanceof boolean[])
			return equals((boolean[])originalArray, (boolean[])objectArray);
		else if (originalArray instanceof double[])
			return equals((double[])originalArray, (double[])objectArray);
		else if (originalArray instanceof float[])
			return equals((float[])originalArray, (float[])objectArray);
		else
			return false;
	}
	private static boolean equals(Object[] originalArray, Object[] objectArray)
	{
		if (originalArray == objectArray)
			return true;
		if (originalArray.length != objectArray.length)
			return false;
		Object o,o1;
		for (int i = 0;i < originalArray.length;i++)
		{
			o = originalArray[i];
			o1 = objectArray[i];
			if (isArray(o))
			{
				if (o instanceof Object[])
				{
					if (!(o1 instanceof Object[]) || !equals((Object[])o, (Object[])o1))
						return false;
				}
				else if (!equals(o, o1))
					return false;
			}
			else if (o == null)
			{
				if (o1 != null)
					return false;
			}
			else
			{
				if (!o.equals(o1))
					return false;
			}
		}
		return true;
	}


	/*
	 equals Array type
	 判断数组类型是否相同
	 */
	public static boolean equalsArrayType2(Object originalArray, Object objectArray)
	{
		if (!(isArray(originalArray) && isArray((objectArray))))
			return false;
		else
			return originalArray.getClass().getComponentType() == objectArray.getClass().getComponentType();
	}
	public static boolean equalsArrayType(Object originalArray, Object objectArray)
	{
		if (originalArray == null || objectArray == null)
			return false;
		else if (originalArray instanceof Object[])
		{
			if (!(objectArray instanceof Object[]))
				return false;
		}
		else if (originalArray instanceof byte[])
		{
			if (!(objectArray instanceof byte[]))
				return false;
		}
		else if (originalArray instanceof long[])
		{
			if (!(objectArray instanceof long[]))
				return false;
		}
		else if (originalArray instanceof char[])
		{
			if (!(objectArray instanceof char[]))
				return false;
		}
		else if (originalArray instanceof int[])
		{
			if (!(objectArray instanceof int[]))
				return false;
		}
		else if (originalArray instanceof short[])
		{
			if (!(objectArray instanceof short[]))
				return false;
		}
		else if (originalArray instanceof boolean[])
		{
			if (!(objectArray instanceof boolean[]))
				return false;
		}
		else if (originalArray instanceof double[])
		{
			if (!(objectArray instanceof double[]))
				return false;
		}
		else if (originalArray instanceof float[])
		{
			if (!(objectArray instanceof float[]))
				return false;
		}
		else
			return false;
		return true;
	}








    /*
	 Array lastIndexOf Object
	 倒序查找数值指针

	 long[] data = new long[]{71, 69, 84, 32, 47, 32, 72, 84, 84};
	 //{71, 69, 84, 32, 47, 32, 72, 84, 84}
	 //0  1   2   3   4   5   6   7   8   9
	 lastIndexOf(data,6,(long)32); >> 5
	 lastIndexOf(data,4,(long)32); >> 3
	 lastIndexOf(data,6,(long)71); >> 0
	 */
	public static int lastIndexOf(Object originalArray, int index, Object value)
	{
		if (value == null)
		{
			for (int i = index; i > -1; i--)
			{
				if (null == get(originalArray, i))
					return i;
			}
		}
		else
		{
			for (int i = index; i > -1; i--)
			{
				if (value.equals(get(originalArray, i)))
					return i;
			}
		}
		return -1;
	}
    /*
	 Array indexOf Object
	 查找数值指针

	 long[] data = new long[]{71, 69, 84, 32, 47, 32, 72, 84, 84};
	 //{71, 69, 84, 32, 47, 32, 72, 84, 84}
	 //0  1   2   3   4   5   6   7   8   9
	 indexOf(data,0,(long)32); >> 3
	 indexOf(data,4,(long)32); >> 5
	 */
	public static int indexOf(Object originalArray, int index, Object value)
	{
		if (index < 0)
			index = 0;
		int length = getLength(originalArray);
		if (value == null)
		{
			for (int i = index; i < length; i++)
			{
				if (null == get(originalArray, i))
					return i;
			}
		}
		else
		{
			for (int i = index; i < length; i++)
			{
				if (value.equals(get(originalArray, i)))
					return i;
			}
		}
		return -1;
	}









	public static Object add(Object originalArray, int index, Object Element)
	{
		return add(originalArray, index, Element, getElementClass(originalArray));
	}
	public static Object addAllTraverse(Object originalArray, int index, Object Array)
	{   
		return addAllTraverse(originalArray, index, Array, getElementClass(originalArray));
	}
	public static Object addAll(Object originalArray, int index, Object Array)
	{       
		return addAll(originalArray, index, Array, getElementClass(originalArray));
	}
	public static Object subArray(Object originalArray, int start, int end)
	{
		return subArray(originalArray, start, end, getElementClass(originalArray));
	}
	public static Object remove(Object originalArray, int start, int end)
	{
		return remove(originalArray, start, end, getElementClass(originalArray));
	}



	/*
	 Add All Traversal
	 遍历添加元素

	 long[] data = new long[]{71, 69, 84, 32, 47, 32, 72, 84, 84};
	 //{71, 69, 84, 32, 47, 32, 72, 84, 84}
	 //0  1   2   3   4   5   6   7   8   9 
	 AddAllUsageTraversal(data,0,new int[]{1,2,3},long.class); >> {1,2,3, 71, 69, 84, 32, 47, 32, 72, 84, 84}
	 AddAllUsageTraversal(data,1,new long[]{1,2,3},long.class); >> {71, 1,2,3, 69, 84, 32, 47, 32, 72, 84, 84}
	 */
	public static Object addAllTraverse(Object originalArray, int index, Object Array, Class originalArrayElementClass)
	{   
		int originalArrayLength = originalArray == null ?0: getLength(originalArray);
		if (index < 0 || index > originalArrayLength)
			index = originalArrayLength;
		int addElementLength = Array == null ?0: getLength(Array);
		Object object = copyOutsideScope(originalArray, index, addElementLength, originalArrayElementClass);
		if (addElementLength != 0)
			arraycopyTraverse(Array, 0, object, index, addElementLength);
		return object;
	}
	/**
	 Add All
	 元素类型相同的数组

	 int[] data = new int[]{71, 69, 84, 32, 47, 32, 72, 84, 84};
	 //{71, 69, 84, 32, 47, 32, 72, 84, 84}
	 //0  1   2   3   4   5   6   7   8   9 
	 addAll(data,0,new int[]{1,2,3},int.class); >> {1,2,3, 71, 69, 84, 32, 47, 32, 72, 84, 84}
	 addAll(data,1,new int[]{1,2,3},int.class); >> {71, 1,2,3, 69, 84, 32, 47, 32, 72, 84, 84}
	 addAll(data,6,new int[]{1,2,3},int.class); >> {71, 69, 84, 32, 47, 32, 1,2,3, 72, 84, 84}
	 **/  
	public static Object addAll(Object originalArray, int index, Object Array, Class originalArrayElementClass)
	{       
		int originalArrayLength = originalArray == null ?0: getLength(originalArray);
		if (index < 0 || index > originalArrayLength)
			index = originalArrayLength;
		int addElementLength = Array == null ?0: getLength(Array);
		Object object = copyOutsideScope(originalArray, index, addElementLength, originalArrayElementClass);
		if (addElementLength != 0)
			System.arraycopy(Array, 0, object, index, addElementLength);
		return object;
	} 
	private static Object copyOutsideScope(Object originalArray, int off, int addlength, Class originalArrayElementClass)
	{   
		if (originalArrayElementClass == null)
			throw new NullPointerException("attempt to read to null class type");
		int originalArrayLength = getLength(originalArray);
		Object object = newInstance(originalArrayElementClass, originalArrayLength + addlength);
		if (originalArrayLength != 0)
			System.arraycopy(originalArray, 0, object, 0, originalArrayLength);
		if (!(off == originalArrayLength) && addlength != 0)
			System.arraycopy(object, off, object, off + addlength, originalArrayLength - off);
		return object;
	}
	
	/**
	 Element Will be added to originalArray
	 Element serve Object
	 ElementClass Must serve Element.class
	 将Element添加到originalArray

	 int[] data = new int[]{71, 69, 84, 32, 47, 32, 72, 84, 84};
	 //{71, 69, 84, 32, 47, 32, 72, 84, 84}
	 //0  1   2   3   4   5   6   7   8   9 
	 add(data,0,8888,int.class); >> {8888, 71, 69, 84, 32, 47, 32, 72, 84, 84}
	 add(data,1,9999,int.class); >> {71, 9999, 69, 84, 32, 47, 32, 72, 84, 84}
	 add(data,6,2333,int.class); >> {71, 69, 84, 32, 47, 32, 2333, 72, 84, 84}
	 **/
	public static Object add(Object originalArray, int index, Object Element, Class originalArrayElementClass)
	{       
		if (originalArray == null)
			throw new NullPointerException("attempt to read from null array");
		int originalArrayLength= getLength(originalArray);
		if (index < 0 || index > originalArrayLength)
			index = originalArrayLength;
		int addElementLength = 1;
		Object object = copyOutsideScope(originalArray, index, addElementLength, originalArrayElementClass);

		set(object, index, Element);
		return object;
	}
	/**
	 Sub Array element
	 取出数组指定位置数据

	 byte[] databyte = new byte[]{71, 69, 84, 32, 47, 32, 72, 84, 84};
	 //{71, 69, 84, 32, 47, 32, 72, 84, 84}
	 //0  1   2   3   4   5   6   7   8   9 
	 subArray(databyte,0,8,byte.class); >> {71, 69, 84, 32, 47, 32, 72, 84}
	 subArray(databyte,1,8,byte.class); >> {69, 84, 32, 47, 32, 72, 84}
	 subArray(databyte,6,7,byte.class); >> {72}
	 **/
	public static Object subArray(Object originalArray, int start, int end, Class originalArrayElementClass)
	{       
		if (originalArray == null)
			throw new NullPointerException("attempt to read from null array");
		int originalArrayLength = getLength(originalArray);
		if (start < 0)
			start = 0;
		if (end > originalArrayLength)
			end = originalArrayLength;
		if (start > end)
			throw new ArrayIndexOutOfBoundsException("index=" + start + ", end=" + end + ", size=" + originalArrayLength);
		int newlength = end - start;

		Object object = newInstance(originalArrayElementClass, newlength);
		if (newlength != 0)
			System.arraycopy(originalArray, start, object, 0, newlength);
		return object;
	}
	/**
	 remove Array element
	 删除数组项目

	 remove(new int[]{1,2,3,4,5,6,7,8,9}, 1, 3, int.class); >> {1,4,5,6,7,8,9}
	 remove(new int[]{1,2,3,4,5,6,7,8,9}, 4, 6, int.class); >> {1,2,3,4,7,8,9}
	 **/
	public static Object remove(Object originalArray, int start, int end, Class originalArrayElementClass)
	{       
		if (originalArray == null)
			throw new NullPointerException("attempt to read from null array");
		int originalArrayLength = getLength(originalArray);
		if (start < 0)
			start = 0;
		if (end > originalArrayLength)
			end = originalArrayLength;
		if (start > end)
			throw new ArrayIndexOutOfBoundsException("index=" + start + ", end=" + end + ", size=" + originalArrayLength);
		int newlength = originalArrayLength - (end - start);
		if (newlength == originalArrayLength)
			return originalArray;
		Object object = newInstance(originalArrayElementClass, newlength);
		if (start != 0)
			System.arraycopy(originalArray, 0, object, 0, start);
		if (((newlength) - start) != 0)
			System.arraycopy(originalArray, end, object, start,  (newlength) - start);
		return object;
	}
	/**
	 is Array
	 判断对象是否为数组

	 isArray(1); >> false
	 isArray(new int[]{1}); >> true
	 isArray(new String[]{"str"}); >> true
	 **/
	public static boolean isArray(Object originalArray)
	{
		if (originalArray == null)
			return false;
		else if (originalArray.getClass().isArray())
			return true;
		return false;
	}

	/*
	 Determines whether the array is Object[]  
	 判断数组是否为Object[]
	 */
	public static boolean isObjectArray(Object originalArray)
	{
		return originalArray instanceof Object[];
	}


	/**
	 get Array Dimensional    
	 获取数组纬度

	 getDimensional(int[0]); >> 1
	 getDimensional(int[0][0]); >> 2
	 **/
	public static int getDimensional(Object Object)
	{
		Class originalArray = Object.getClass();
		if (!originalArray.isArray())
			return 0;
		return getDimensional_ClassCanonicalName(originalArray.getCanonicalName());
	}
	public static int getDimensional_ClassCanonicalName(String Addres)
	{
		if (Addres == null)
			return 0;
		int index = Addres.indexOf("["); //java.lang.System[][]
		if (index == -1)
			return 0;
		return (Addres.length() - index) / 2;//[][]
	}


	/**
	 Array To String    
	 数组到字符串

	 **/
	public static String toString(Object originalArray)
	{
		if (originalArray == null)
			throw new NullPointerException("attempt to read from null array");
		if (originalArray instanceof Collection)
			return toString(((Collection)originalArray).toArray());
		if (!XArrays.isArray(originalArray))
			throw new ClassCastException(String.format("%s not can cast to array", originalArray.getClass().getName()));
		StringBuilder result = new StringBuilder(toStringProcess(originalArray));
		if (result.substring(result.length() - 1, result.length()).equals(","))
			result.delete(result.length() - 1, result.length());
		return result.toString();
	}
    private static StringBuilder toStringProcess(Object originalArray)
	{
		Object Object;
		StringBuilder result = new StringBuilder();
		result.append(originalArray.getClass().getCanonicalName());
		result.append('{');
		Object[] newArray = (Object[])copyOf(originalArray, XStaticBaseType.Object_class);
		for (int i=0; i < newArray.length; i++)
		{
			Object = newArray[i];
			if (XArrays.isArray(Object))
				result.append(toStringProcess(Object));
			else
			{
				result.append(Object);
				result.append(',');
				continue;
			}
		}
		if (result.substring(result.length() - 1, result.length()).equals(","))
			result.delete(result.length() - 1, result.length());
		result.append('}');
		result.append(',');
		return result;
	}





	/*
	 get Array Element Class
	 获取数组元素类

	 getElementClass(new int[0]); >> int.class
	 getElementClass(new int[0][0]); >> int[].class
	 */
	public static Class getElementClass(Object originalArray)
	{
		return originalArray.getClass().getComponentType();
	}

	/*
	 new array val for fill value
	 创建一个数组 val为填充值
	 */
	public static Object newInstance(Class originalArrayElementClass, int length, Object fiiVal)
	{
		if (length == 0)
			return newInstance(originalArrayElementClass, 0);
		Object array = newInstance(originalArrayElementClass, 1);
		set(array, 0, fiiVal);
		if (length == 1)
			return array;
		return repeat(array, length);
	}

    /*
	 fast repeat copy array
	 快速重复复制数组

	 fastRepeat(new int[]{1},int.class,1); >> {1}
	 fastRepeat("1".toCharArray(),char.class,3); >> {'1','1','1'}
	 */
	public static Object repeat(Object originalArray, int repeatCount)
	{
		return repeat(originalArray, getElementClass(originalArray), repeatCount);
	}
	public static Object repeat(Object originalArray, Class originalArrayElementClass, int repeatCount)
	{
		int secureLength;
		if (originalArray == null)
			throw new NullPointerException("attempt to read from null array");
		else if ((secureLength = getLength(originalArray)) == 0)
			throw new NullPointerException("array length min can't for 0");
		else if (repeatCount < 1)
			throw new NumberFormatException("array repeat Count length min can't for 0");
		int cumulative = 1;
		Object newArray = originalArray;
		int newcumulative;
		while (true)
		{
			if (cumulative >= repeatCount)
				break;
			newcumulative = cumulative * 2;
			if (newcumulative > repeatCount)
			{
				Object newArray2 = newInstance(originalArrayElementClass, repeatCount * secureLength);
				int newArraylength = getLength(newArray);
				int newArray2length = getLength(newArray2);
				System.arraycopy(newArray, 0, newArray2, 0, newArraylength);
				System.arraycopy(newArray, 0, newArray2, newArraylength, newArray2length - newArraylength);
				return newArray2;
			}
			newArray = repeatX2(newArray, originalArrayElementClass);
			cumulative = newcumulative;
		}
		return newArray;
	}
	private static Object repeatX2(Object originalArray, Class originalArrayElementClass)
	{
		int arrayLength = getLength(originalArray);
		Object newArray = newInstance(originalArrayElementClass, arrayLength * 2);
		System.arraycopy(originalArray, 0, newArray, 0, arrayLength);
		System.arraycopy(originalArray, 0, newArray, arrayLength, arrayLength);
		return newArray;
	}



	/*
	 copyOf array
	 复制数组
	 */
	public static Object copyOf(Object originalArray, int newLength)
	{
		if (!isArray(originalArray))
			throw new ClassCastException(String.format("%s not can cast to array", originalArray.getClass().getName()));
		Class cls = getElementClass(originalArray);
		int originalArrayLength = getLength(originalArray);
		Object array = newInstance(cls, newLength);
		if (newLength != 0)
			System.arraycopy(originalArray, 0, array, 0, newLength < originalArrayLength ? newLength: originalArrayLength);
		return array;
	}
	public static Object copyOfRange(Object originalArray, int from, int to)
	{
		return subArray(originalArray, from, to);
	}



	public static Object copyOf(Object originalArray, Class <? extends Object> newElementType)
	{
		return copyOf(originalArray, getLength(originalArray), newElementType);
	}
	public static Object copyOf(Object originalArray, int newLength, Class <? extends Object> newElementType)
	{
		Object newArray = newInstance(newElementType, newLength);
		if (newLength != 0)
			arraycopyTraverse(originalArray, 0, newArray, 0, Math.min(newLength, getLength(originalArray)));
		return newArray;
	}
}
