package top.fols.box.statics;

import top.fols.atri.lang.Finals;

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

/**
 * @see Finals
 */
@Deprecated
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
	public static final Object[] nullObjectArray = Finals.EMPTY_OBJECT_ARRAY;

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
	public static final ListIterator nullListIterator = Collections.emptyListIterator();
	public static final Enumeration nullEnumeration = Collections.emptyEnumeration();
	public static final Set nullSet = Collections.emptySet();
	public static final Map nullMap = Collections.emptyMap();

	public static final Class[] nullClassArray = Finals.EMPTY_CLASS_ARRAY;
	public static final Method[] nullMethodArray = Finals.EMPTY_METHOD_ARRAY;
	public static final Constructor[] nullConstructorArray = Finals.EMPTY_CONSTRUCTOR_ARRAY;
	public static final Field[] nullFieldArray = Finals.EMPTY_FIELD_ARRAY;
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



	public static final Class byte_class = byte.class;
	public static final Class long_class = long.class;
	public static final Class double_class = double.class;
	public static final Class char_class = char.class;
	public static final Class int_class = int.class;
	public static final Class boolean_class = boolean.class;
	public static final Class float_class = float.class;
	public static final Class short_class = short.class;
	public static final Class void_class = void.class;

	public static final Class byte_array_class = byte[].class;
	public static final Class long_array_class = long[].class;
	public static final Class double_array_class = double[].class;
	public static final Class char_array_class = char[].class;
	public static final Class int_array_class = int[].class;
	public static final Class boolean_array_class = boolean[].class;
	public static final Class float_array_class = float[].class;
	public static final Class short_array_class = short[].class;
	// public static final Class void_array_class = void[].class;

	public static final Class Byte_class = Byte.class;
	public static final Class Long_class = Long.class;
	public static final Class Double_class = Double.class;
	public static final Class Character_class = Character.class;
	public static final Class Integer_class = Integer.class;
	public static final Class Boolean_class = Boolean.class;
	public static final Class Float_class = Float.class;
	public static final Class Short_class = Short.class;
	public static final Class Void_class = Void.class;

	public static final Class Byte_array_class = Byte[].class;
	public static final Class Long_array_class = Long[].class;
	public static final Class Double_array_class = Double[].class;
	public static final Class Character_array_class = Character[].class;
	public static final Class Integer_array_class = Integer[].class;
	public static final Class Boolean_array_class = Boolean[].class;
	public static final Class Float_array_class = Float[].class;
	public static final Class Short_array_class = Short[].class;
	public static final Class Void_array_class = Void[].class;

	public static final Class Object_class = Object.class;
	public static final Class Class_class = Class.class;
	public static final Class String_class = String.class;
	public static final Class Method_class = Method.class;
	public static final Class Field_class = Field.class;
	public static final Class Constructor_class = Constructor.class;

	public static final Class Object_array_class = Object[].class;
	public static final Class Class_array_class = Class[].class;
	public static final Class String_array_class = String[].class;
	public static final Class Method_array_class = Method[].class;
	public static final Class Field_array_class = Field[].class;
	public static final Class Constructor_array_class = Constructor[].class;

	public static final String byte_classcanonicalname = byte.class.getCanonicalName();
	public static final String long_classcanonicalname = long.class.getCanonicalName();
	public static final String double_classcanonicalname = double.class.getCanonicalName();
	public static final String char_classcanonicalname = char.class.getCanonicalName();
	public static final String int_classcanonicalname = int.class.getCanonicalName();
	public static final String boolean_classcanonicalname = boolean.class.getCanonicalName();
	public static final String float_classcanonicalname = float.class.getCanonicalName();
	public static final String short_classcanonicalname = short.class.getCanonicalName();
	public static final String void_classcanonicalname = void.class.getCanonicalName();

	public static final String byte_array_classcanonicalname = byte[].class.getCanonicalName();
	public static final String long_array_classcanonicalname = long[].class.getCanonicalName();
	public static final String double_array_classcanonicalname = double[].class.getCanonicalName();
	public static final String char_array_classcanonicalname = char[].class.getCanonicalName();
	public static final String int_array_classcanonicalname = int[].class.getCanonicalName();
	public static final String boolean_array_classcanonicalname = boolean[].class.getCanonicalName();
	public static final String float_array_classcanonicalname = float[].class.getCanonicalName();
	public static final String short_array_classcanonicalname = short[].class.getCanonicalName();
	// public static final String void_array_classcanonicalname = void[].class.getCanonicalName();

	public static final String Byte_classcanonicalname = Byte.class.getCanonicalName();
	public static final String Long_classcanonicalname = Long.class.getCanonicalName();
	public static final String Double_classcanonicalname = Double.class.getCanonicalName();
	public static final String Character_classcanonicalname = Character.class.getCanonicalName();
	public static final String Integer_classcanonicalname = Integer.class.getCanonicalName();
	public static final String Boolean_classcanonicalname = Boolean.class.getCanonicalName();
	public static final String Float_classcanonicalname = Float.class.getCanonicalName();
	public static final String Short_classcanonicalname = Short.class.getCanonicalName();
	public static final String Void_classcanonicalname = Void.class.getCanonicalName();

	public static final String Byte_array_classcanonicalname = Byte[].class.getCanonicalName();
	public static final String Long_array_classcanonicalname = Long[].class.getCanonicalName();
	public static final String Double_array_classcanonicalname = Double[].class.getCanonicalName();
	public static final String Character_array_classcanonicalname = Character[].class.getCanonicalName();
	public static final String Integer_array_classcanonicalname = Integer[].class.getCanonicalName();
	public static final String Boolean_array_classcanonicalname = Boolean[].class.getCanonicalName();
	public static final String Float_array_classcanonicalname = Float[].class.getCanonicalName();
	public static final String Short_array_classcanonicalname = Short[].class.getCanonicalName();
	public static final String Void_array_classcanonicalname = Void[].class.getCanonicalName();

	public static final byte 		byte_defaultValue = Finals.BYTE_DEFAULT_VALUE;
	public static final long 		long_defaultValue = Finals.LONG_DEFAULT_VALUE;
	public static final double 		double_defaultValue = Finals.DOUBLE_DEFAULT_VALUE;
	public static final char 		char_defaultValue = Finals.CHAR_DEFAULT_VALUE;
	public static final int 		int_defaultValue = Finals.INT_DEFAULT_VALUE;
	public static final boolean 	boolean_defaultValue = Finals.BOOLEAN_DEFAULT_VALUE;
	public static final float	 	float_defaultValue = Finals.FLOAT_DEFAULT_VALUE;
	public static final short 		short_defaultValue = Finals.SHORT_DEFAULT_VALUE;

	public static final Byte 		Byte_defaultValue = Finals.BYTE_DEFAULT_VALUE;
	public static final Long 		Long_defaultValue = Finals.LONG_DEFAULT_VALUE;
	public static final Double 		Double_defaultValue = Finals.DOUBLE_DEFAULT_VALUE;
	public static final Character 	Character_defaultValue = Finals.CHAR_DEFAULT_VALUE;
	public static final Integer 	Integer_defaultValue = Finals.INT_DEFAULT_VALUE;
	public static final Boolean 	Boolean_defaultValue = Finals.BOOLEAN_DEFAULT_VALUE;
	public static final Float 		Float_defaultValue = Finals.FLOAT_DEFAULT_VALUE;
	public static final Short 		Short_defaultValue = Finals.SHORT_DEFAULT_VALUE;





	public static final Object NULL = Finals.NULL;

	public static final boolean boolean_true = Finals.BOOLEAN_TRUE;
	public static final boolean boolean_false = Finals.BOOLEAN_FALSE;

	public static final Boolean Boolean_TRUE = Finals.BOOLEAN_PACKAGE_TRUE;
	public static final Boolean Boolean_FALSE = Finals.BOOLEAN_PACKAGE_FALSE;


	public static final String TRUE_STRING = Finals.TRUE_STRING;
	public static final String FALSE_STRING = Finals.FALSE_STRING;
	public static final String NULL_STRING = Finals.NULL_STRING;
}
