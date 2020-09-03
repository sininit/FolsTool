package top.fols.box.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Comparator;

import top.fols.box.statics.XStaticFixedValue;

public class XObjects {

	public static abstract interface CastProcess<T extends Object, C extends Object> {
		public C cast(T content);
	}

	public static abstract interface AcceptProcess<T extends Object> {
		public boolean accept(T content);
	}

	public static abstract interface ComparatorProcess<T extends Object> extends Comparator<T> {
		public abstract int compare(T o1, T o2);
	}

	@SuppressWarnings("unchecked")
	public static <S> List<S> keys(Map<S, ?> map) {
		if (null == map || map.size() == 0) {
			return XStaticFixedValue.nullList;
		}
		List<S> list = new ArrayList<S>(map.size());
		Set<S> set = map.keySet();
		for (S key : set) {
			list.add(key);
		}
		return list;
	}

	public static <T> T requireNonNull(T obj) {
		if (null == obj) {
			throw new NullPointerException();
		}
		return obj;
	}

	public static <T> T requireNonNull(T obj, String errorMessage) {
		if (null == obj) {
			throw new NullPointerException(errorMessage);
		}
		return obj;
	}

	public static boolean isEquals(Object obj, Object obj2) {
		if (null == obj) {
			if (null == obj2) {
				return true;
			}
			return false;
		} else {
			if (obj.equals(obj2)) {
				return true;
			}
			return false;
		}
	}

	public static boolean isEmpty(StringBuffer obj) {
		return null == obj || obj.length() == 0;
	}

	public static boolean isEmpty(StringBuilder obj) {
		return null == obj || obj.length() == 0;
	}

	public static boolean isEmpty(Object obj) {
		return null == obj;
	}

	public static boolean isEmpty(Collection<?> obj) {
		return null == obj || obj.size() == 0;
	}

	public static boolean isEmpty(String obj) {
		return null == obj || obj.length() == 0;
	}

	public static boolean isEmpty(Object[] obj) {
		return null == obj || obj.length == 0;
	}

	public static boolean isEmpty(long[] obj) {
		return null == obj || obj.length == 0;
	}

	public static boolean isEmpty(int[] obj) {
		return null == obj || obj.length == 0;
	}

	public static boolean isEmpty(short[] obj) {
		return null == obj || obj.length == 0;
	}

	public static boolean isEmpty(byte[] obj) {
		return null == obj || obj.length == 0;
	}

	public static boolean isEmpty(boolean[] obj) {
		return null == obj || obj.length == 0;
	}

	public static boolean isEmpty(double[] obj) {
		return null == obj || obj.length == 0;
	}

	public static boolean isEmpty(float[] obj) {
		return null == obj || obj.length == 0;
	}

	public static boolean isEmpty(char[] obj) {
		return null == obj || obj.length == 0;
	}






	public static String toString(Object obj) {
		if (null == obj) {
			return null;
		}
		if (obj instanceof char[]) {
			return new String((char[]) obj);
		} else {
			return obj.toString();
		}
	}

	public static char tochar(Object obj) {
		if (null == obj) {
			return (char)0;
		}
		if (obj instanceof Character) {
			return ((Character) obj).charValue();
		}
		String str = obj.toString();
		return 0 == str.length() ?(char)0: str.charAt(0);
	}

	public static boolean toboolean(Object obj) {
		if (null == obj) {
			return false;
		}
		if (obj instanceof Boolean) {
			return ((Boolean) obj).booleanValue();
		}
		return Boolean.parseBoolean(obj.toString().trim());
	}

	public static byte tobyte(Object obj) {
		if (null == obj) {
			return 0;
		}
		if (obj instanceof Byte) {
			return ((Byte) obj).byteValue();
		}
		return Byte.parseByte(obj.toString().trim());
	}

	public static int toint(Object obj) {
		if (null == obj) {
			return 0;
		}
		if (obj instanceof Integer) {
			return ((Integer) obj).intValue();
		}
		return Integer.parseInt(obj.toString().trim());
	}

	public static long tolong(Object obj) {
		if (null == obj) {
			return 0;
		}
		if (obj instanceof Long) {
			return ((Long) obj).longValue();
		}
		return Long.parseLong(obj.toString().trim());
	}

	public static short toshort(Object obj) {
		if (null == obj) {
			return 0;
		}
		if (obj instanceof Short) {
			return ((Short) obj).shortValue();
		}
		return Short.parseShort(obj.toString().trim());
	}

	/**
	 * xx.xxx
	 **/
	public static double todouble(Object obj) {
		if (null == obj) {
			return 0;
		} else if (obj instanceof Double) {
			return ((Double) obj).doubleValue();
		}
		return Double.parseDouble(obj.toString().trim());
	}

	/**
	 * xx.xxx
	 **/
	public static float tofloat(Object obj) {
		if (null == obj) {
			return 0;
		}
		if (obj instanceof Float) {
			return ((Float) obj).floatValue();
		}
		return Float.parseFloat(obj.toString().trim());
	}



	/**
	 * parse value Object[] byte[] long[] double[] char[] int[] boolean[] float[]
	 * short[]
	 *
	 * @param str
	 * @return
	 */



	public static boolean parseBoolean(Object value) {
		return null != value && "true".equalsIgnoreCase(value.toString().trim());
	}

	public static byte parseByte(Object value) {
		if (null == value) { return 0; }
		String st = XObjects.retainNum(st = value.toString(), 0, st.length());
		return st.length() == 0 ? 0 : Byte.parseByte(st);
	}

	public static char parseChar(Object value) {
		String st;
		return (null != value && (st = value.toString()).length() > 0) ?st.charAt(0): (char)0;
	}

	public static short parseShort(Object value) {
		if (null == value) { return 0; }
		String st = XObjects.retainNum(st = value.toString(), 0, st.length());
		return st.length() == 0 ? 0 : Short.parseShort(st);
	}

	public static int parseInt(Object value) {
		if (null == value) { return 0; }
		String st = XObjects.retainNum(st = value.toString(), 0, st.length());
		return st.length() == 0 ? 0 : Integer.parseInt(st);
	}


	public static long parseLong(Object value) {
		if (null == value) { return 0; }
		String st = XObjects.retainNum(st = value.toString(), 0, st.length());
		return st.length() == 0 ? 0 : Long.parseLong(st);
	}


	public static float parseFloat(Object value) {
		if (null == value) { return 0; }
		String st = XObjects.retainDouble(st = value.toString(), 0, st.length());
		return st.length() == 0 ? 0 : Float.parseFloat(st);
	}

	public static double parseDouble(Object value) {
		if (null == value) { return 0; }
		String st = XObjects.retainDouble(st = value.toString(), 0, st.length());
		return st.length() == 0 ? 0 : Double.parseDouble(st);
	}


























	@SuppressWarnings("unchecked")
	public static byte[] tobyteArray(Object originArray) {
		return (byte[]) XArray.copyOfConversion(originArray, XStaticFixedValue.byte_class);
	}

	@SuppressWarnings("unchecked")
	public static long[] tolongArray(Object originArray) {
		return (long[]) XArray.copyOfConversion(originArray, XStaticFixedValue.long_class);
	}

	@SuppressWarnings("unchecked")
	public static double[] todoubleArray(Object originArray) {
		return (double[]) XArray.copyOfConversion(originArray, XStaticFixedValue.double_class);
	}

	@SuppressWarnings("unchecked")
	public static char[] tocharArray(Object originArray) {
		return (char[]) XArray.copyOfConversion(originArray, XStaticFixedValue.char_class);
	}

	@SuppressWarnings("unchecked")
	public static int[] tointArray(Object originArray) {
		return (int[]) XArray.copyOfConversion(originArray, XStaticFixedValue.int_class);
	}

	@SuppressWarnings("unchecked")
	public static boolean[] tobooleanArray(Object originArray) {
		return (boolean[]) XArray.copyOfConversion(originArray, XStaticFixedValue.boolean_class);
	}

	@SuppressWarnings("unchecked")
	public static float[] tofloatArray(Object originArray) {
		return (float[]) XArray.copyOfConversion(originArray, XStaticFixedValue.float_class);
	}

	@SuppressWarnings("unchecked")
	public static short[] toshortArray(Object originArray) {
		return (short[]) XArray.copyOfConversion(originArray, XStaticFixedValue.short_class);
	}

	@SuppressWarnings("unchecked")
	public static String[] toStringArray(Object originArray) {
		return (String[]) XArray.copyOfConversion(originArray, XStaticFixedValue.String_class);
	}

	@SuppressWarnings("unchecked")
	public static Object[] toObjectArray(Object originArray) {
		return (Object[]) XArray.copyOfConversion(originArray, XStaticFixedValue.Object_class);
	}



	/**
	 * 
	 * read String first num
	 * @see top.fols.box.lang.XString#retain(CharSequence, int, int, char[]) 
	 */
	public static String retainNum(CharSequence str, int off, int len) {
		char[] buf = new char[20];// long max string len = 20
		int bufindex = 0, bufsize = 0;
		char ch;
		for (int i = off; i < off + len; i++) {
			ch = str.charAt(i);
			if (ch == '+' || ch == '-' || (ch >= '0' && ch <= '9')) {
				int minCapacity = bufindex + 1;
				if (minCapacity - buf.length > 0) {
					int oldCapacity = buf.length;
					int newCapacity = oldCapacity << 1;
					if (newCapacity - minCapacity < 0) {
						newCapacity = minCapacity;
					}
					if (newCapacity < 0) {
						if (minCapacity < 0) {
							// overflow
							throw new OutOfMemoryError();
						}
						newCapacity = Integer.MAX_VALUE;
					}
					buf = Arrays.copyOf(buf, newCapacity);
				}

				buf[bufindex++] = ch;
				bufsize++;
			} else if (bufindex > 0) {// interrupt
				break;
			}
		}
		return new String(buf, 0, bufsize);
	}

	/**
	 * 
	 * read String first num
	 * @see top.fols.box.lang.XString#retain(CharSequence, int, int, char[]) 
	 */
	public static String retainDouble(CharSequence str, int off, int len) {
		char[] buf = new char[64];
		int bufindex = 0, bufsize = 0;
		char ch;
		for (int i = off; i < off + len; i++) {
			ch = str.charAt(i);
			if (ch == '+' || ch == '-' || (ch >= '0' && ch <= '9') || ch == 'N' || ch == 'I' || ch == 'x' || ch == 'X'
					|| ch == '.' || ch == 'e' || ch == 'E' || ch == 'f' || ch == 'F' || ch == 'd' || ch == 'D') {

				int minCapacity = bufindex + 1;
				if (minCapacity - buf.length > 0) {
					int oldCapacity = buf.length;
					int newCapacity = oldCapacity << 1;
					if (newCapacity - minCapacity < 0) {
						newCapacity = minCapacity;
					}
					if (newCapacity < 0) {
						if (minCapacity < 0) {
							// overflow
							throw new OutOfMemoryError();
						}
						newCapacity = Integer.MAX_VALUE;
					}
					buf = Arrays.copyOf(buf, newCapacity);
				}

				buf[bufindex++] = ch;
				bufsize++;
			} else if (bufindex > 0) {// interrupt
				break;
			}
		}
		return new String(buf, 0, bufsize);
	}

}
