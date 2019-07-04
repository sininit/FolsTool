package top.fols.box.statics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public final class XStaticFixedValue {
	
	public static final class FileOptMode {
		public static final String r() { return "r"; }
		public static final String rw() { return "rw"; }
		public static final String rws() { return "rws"; }
	}
	
	public final static String[] nullStringArray = new String[0];
	public final static Object[] nullObjectArray = new Object[0];

	public final static byte[] nullbyteArray = new byte[0];
	public final static long[] nulllongArray = new long[0];
	public final static double[] nulldoubleArray = new double[0];
	public final static char[] nullcharArray = new char[0];
	public final static int[] nullintArray = new int[0];
	public final static boolean[] nullbooleanArray = new boolean[0];
	public final static float[] nullfloatArray = new float[0];
	public final static short[] nullshortArray = new short[0];

	public final static Byte[] nullByteArray = new Byte[0];
	public final static Long[] nullLongArray = new Long[0];
	public final static Double[] nullDoubleArray = new Double[0];
	public final static Character[] nullCharacterArray = new Character[0];
	public final static Integer[] nullIntegerArray = new Integer[0];
	public final static Boolean[] nullBooleanArray = new Boolean[0];
	public final static Float[] nullFloatArray = new Float[0];
	public final static Short[] nullShortArray = new Short[0];

	public final static Iterator nullIterator = Collections.emptyIterator();
	public final static List nullList = Collections.emptyList();
	public final static ListIterator nullListIterator = Collections.emptyListIterator();;
	public final static Enumeration nullEnumeration = Collections.emptyEnumeration();;
	public final static Set nullSet = Collections.emptySet();;
	public final static Map nullMap = Collections.emptyMap();;
	
	public final static Class[] nullClassArray = new Class[0];
	public final static Method[] nullMethodArray = new Method[0];
	public final static Constructor[] nullConstructorArray = new Constructor[0];
	public final static Field[] nullFieldArray = new Field[0];
	public static final StackTraceElement[] nullStack = new StackTraceElement[0];
	
	
	
	
	public final static byte Byte_NextLineR = '\r';
	public final static byte Byte_NextLineN = '\n';
	public final static byte[] Bytes_NextLineN = new byte[]{'\n'};
	public final static byte[] Bytes_NextLineRN = new byte[]{'\r','\n'};
	
	public final static char Char_NextLineR = '\r';
	public final static char Char_NextLineN = '\n';
	public final static char[] Chars_NextLineN = new char[]{'\n'};
	public final static char[] Chars_NextLineRN = new char[]{'\r','\n'};

	public final static String String_NextLineR = "\r";
	public final static String String_NextLineN = "\n";
	public final static String String_NextLineRN = "\r\n";
}
