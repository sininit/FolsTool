package top.fols.box.statics;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class XStaticBaseType {
	public static final Class byte_class = byte.class;
	public static final Class long_class = long.class;
	public static final Class double_class = double.class;
	public static final Class char_class = char.class;
	public static final Class int_class = int.class;
	public static final Class boolean_class = boolean.class;
	public static final Class float_class = float.class;
	public static final Class short_class = short.class;
	public static final Class void_class = void.class ;

	public static final Class Byte_class = Byte.class;
	public static final Class Long_class = Long.class;
	public static final Class Double_class = Double.class;
	public static final Class Character_class = Character.class;
	public static final Class Integer_class = Integer.class;
	public static final Class Boolean_class = Boolean.class;
	public static final Class Float_class = Float.class;
	public static final Class Short_class = Short.class;
	public static final Class Void_class = Void.class;

	public static final Class Object_class = Object.class;
	public static final Class Class_class = Class.class;
	public static final Class String_class = String.class;
	public static final Class Method_class = Method.class;
	public static final Class Field_class = Field.class;
	public static final Class Constructor_class = Constructor.class;

	public static final String byte_classcanonicalname = byte.class.getCanonicalName();
	public static final String long_classcanonicalname = long.class.getCanonicalName();
	public static final String double_classcanonicalname = double.class.getCanonicalName();
	public static final String char_classcanonicalname = char.class.getCanonicalName();
	public static final String int_classcanonicalname = int.class.getCanonicalName();
	public static final String boolean_classcanonicalname = boolean.class.getCanonicalName();
	public static final String float_classcanonicalname = float.class.getCanonicalName();
	public static final String short_classcanonicalname = short.class.getCanonicalName();
	public static final String void_classcanonicalname = void.class.getCanonicalName();

	public static final String Byte_classcanonicalname = Byte.class.getCanonicalName();
	public static final String Long_classcanonicalname = Long.class.getCanonicalName();
	public static final String Double_classcanonicalname = Double.class.getCanonicalName();
	public static final String Character_classcanonicalname = Character.class.getCanonicalName();
	public static final String Integer_classcanonicalname = Integer.class.getCanonicalName();
	public static final String Boolean_classcanonicalname = Boolean.class.getCanonicalName();
	public static final String Float_classcanonicalname = Float.class.getCanonicalName();
	public static final String Short_classcanonicalname = Short.class.getCanonicalName();
	public static final String Void_classcanonicalname = Void.class.getCanonicalName();

	public final static byte byte_defaultValue = 0;
	public final static long long_defaultValue = 0L;
	public final static double double_defaultValue = 0D;
	public final static char char_defaultValue = 0;
	public final static int int_defaultValue = 0;
	public final static boolean boolean_defaultValue = false;
	public final static float float_defaultValue = 0F;
	public final static short short_defaultValue = 0;

	public final static Byte Byte_defaultValue = 0;
	public final static Long Long_defaultValue = 0L;
	public final static Double Double_defaultValue = 0D;
	public final static Character Character_defaultValue = 0;
	public final static Integer Integer_defaultValue = 0;
	public final static Boolean Boolean_defaultValue = false;
	public final static Float Float_defaultValue = 0F;
	public final static Short Short_defaultValue = 0;
	
	
	public final static Object NULL = null;
	
	public final static boolean boolean_true = true;
	public final static boolean boolean_false = false;

	public final static Boolean Boolean_TRUE = true;
	public final static Boolean Boolean_FALSE = false;
	public static Byte toByte(byte obj) {
		return (Byte)obj;
	}
	public static Long toLong(long obj) {
		return ((Long)obj);
	}
	public static Double toDouble(double obj) {
		return ((Double)obj);
	}
	public static Character toChar(char obj) {
		return ((Character)obj);
	}
	public static Integer toInt(int obj) {
		return ((Integer)obj);
	}
	public static Boolean toBoolean(boolean obj) {
		return ((Boolean)obj);
	}
	public static Float toFloat(float obj) {
		return ((Float)obj);
	}
	public static Short toShort(short obj) {
		return ((Short)obj);
	}




	public static byte tobyte(Object obj) {
		return ((Byte)obj).byteValue();
	}
	public static long tolong(Object obj) {
		return ((Long)obj).longValue();
	}
	public static double todouble(Object obj) {
		return ((Double)obj).doubleValue();
	}
	public static char tochar(Object obj) {
		return ((Character)obj).charValue();
	}
	public static int toint(Object obj) {
		return ((Integer)obj).intValue();
	}
	public static boolean toboolean(Object obj) {
		return ((Boolean)obj).booleanValue();
	}
	public static float tofloat(Object obj) {
		return ((Float)obj).floatValue();
	}
	public static short toshort(Object obj) {
		return ((Short)obj).shortValue();
	}



	
}
