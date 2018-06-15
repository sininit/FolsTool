package top.fols.box.statics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import top.fols.box.util.empty.XEmptyIterator;
import top.fols.box.util.empty.XEmptyList;
import top.fols.box.util.empty.XEmptyListIterator;

public class XStaticFixedValue
{
	public static class FileValue
	{
		public static final String getRandomAccessFile_Mode_R_String()
		{
			return "r";
		}
		public static final String getRandomAccessFile_Mode_RW_String()
		{
			return "rw";
		}
	}
	
	
	
	
	
	public final static String[] nullStringArray = new String[0];
	public final static byte[] nullbyteArray = new byte[0];
	public final static long[] nulllongArray = new long[0];
	public final static double[] nulldoubleArray = new double[0];
	public final static char[] nullcharArray = new char[0];
	public final static int[] nullintArray = new int[0];
	public final static boolean[] nullbooleanArray = new boolean[0];
	public final static float[] nullfloatArray = new float[0];
	public final static short[] nullshortArray = new short[0];
	public final static Object[] nullObjectArray = new Object[0];

	public final static Byte[] nullByteArray = new Byte[0];
	public final static Long[] nullLongArray = new Long[0];
	public final static Double[] nullDoubleArray = new Double[0];
	public final static Character[] nullCharacterArray = new Character[0];
	public final static Integer[] nullIntegerArray = new Integer[0];
	public final static Boolean[] nullBooleanArray = new Boolean[0];
	public final static Float[] nullFloatArray = new Float[0];
	public final static Short[] nullShortArray = new Short[0];
	
	public final static Iterator nullIterator = new XEmptyIterator();
	public final static List nullList = new XEmptyList();
	public final static ListIterator nullListIterator = new XEmptyListIterator();;
	
	public final static Class[] nullClassArray = new Class[0];
	public final static Method[] nullMethodArray = new Method[0];
	public final static Constructor[] nullConstructorArray = new Constructor[0];
	public final static Field[] nullFieldArray = new Field[0];

	public final static byte[] Bytes_NextLineN = new byte[]{10};
	public final static byte[] Bytes_NextLineRN = new byte[]{13,10};
	public final static byte Byte_NextLineN = (byte)10;

	public final static char[] Chars_NextLineN = new char[]{'\n'};
	public final static char[] Chars_NextLineRN = new char[]{'\r','\n'};
	public final static char Char_NextLineN = '\n';

	public final static String String_NextLineN = "\n";
	public final static String String_NextLineRN = "\r\n";
	public final static int Stream_ReadBreak = -1;
		
	
	
}
