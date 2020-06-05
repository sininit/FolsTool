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
		public static final String r() {
			return "r";
		}

		public static final String rw() {
			return "rw";
		}

		public static final String rws() {
			return "rws";
		}
	}

	public static final String[] nullStringArray = new String[0];
	public static final Object[] nullObjectArray = new Object[0];

	public static final byte[] nullbyteArray = new byte[0];
	public static final long[] nulllongArray = new long[0];
	public static final double[] nulldoubleArray = new double[0];
	public static final char[] nullcharArray = new char[0];
	public static final int[] nullintArray = new int[0];
	public static final boolean[] nullbooleanArray = new boolean[0];
	public static final float[] nullfloatArray = new float[0];
	public static final short[] nullshortArray = new short[0];

	public static final Byte[] nullByteArray = new Byte[0];
	public static final Long[] nullLongArray = new Long[0];
	public static final Double[] nullDoubleArray = new Double[0];
	public static final Character[] nullCharacterArray = new Character[0];
	public static final Integer[] nullIntegerArray = new Integer[0];
	public static final Boolean[] nullBooleanArray = new Boolean[0];
	public static final Float[] nullFloatArray = new Float[0];
	public static final Short[] nullShortArray = new Short[0];

	public static final Iterator nullIterator = Collections.emptyIterator();
	public static final List nullList = Collections.emptyList();
	public static final ListIterator nullListIterator = Collections.emptyListIterator();;
	public static final Enumeration nullEnumeration = Collections.emptyEnumeration();;
	public static final Set nullSet = Collections.emptySet();;
	public static final Map nullMap = Collections.emptyMap();;

	public static final Class[] nullClassArray = new Class[0];
	public static final Method[] nullMethodArray = new Method[0];
	public static final Constructor[] nullConstructorArray = new Constructor[0];
	public static final Field[] nullFieldArray = new Field[0];
	public static final StackTraceElement[] nullStack = new StackTraceElement[0];

	public static final byte Byte_NextLineR = '\r';
	public static final byte Byte_NextLineN = '\n';
	public static final byte[] Bytes_NextLineN(){
		return new byte[] { '\n' };
	}
	public static final byte[] Bytes_NextLineR(){
		return new byte[] { '\r' };
	}
	public static final byte[] Bytes_NextLineRN(){
		return new byte[] { '\r', '\n' };
	}

	public static final char Char_NextLineR = '\r';
	public static final char Char_NextLineN = '\n';
	public static final char[] Chars_NextLineN(){
		return new char[] { '\n' };
	}
	public static final char[] Chars_NextLineR(){
		return new char[] { '\r' };
	} 
	public static final char[] Chars_NextLineRN(){
		return new char[] { '\r', '\n' };
	}

	public static final String String_NextLineR = "\r";
	public static final String String_NextLineN = "\n";
	public static final String String_NextLineRN = "\r\n";
}
