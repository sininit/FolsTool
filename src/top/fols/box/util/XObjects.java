package top.fols.box.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Comparator;

import top.fols.box.statics.XStaticBaseType;
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

	/**
	 * will data cast to String toString(char[]); toString(byte[]);
	 * toString(byte[],String coding); toString(Object);
	 * 
	 * @param objArr
	 * @return
	 */
	public static String toString(Object objArr) {
		return XObjects.toString(objArr, null);
	}

	public static String toString(Object objArr, String codeing) {
		if (null == objArr) {
			return null;
		}
		if (objArr instanceof String) {
			return (String) objArr;
		} else if (objArr instanceof char[]) {
			return new String((char[]) objArr);
		} else if (objArr instanceof byte[]) {
			byte[] bytes = (byte[]) objArr;
			return null == codeing ? new String(bytes) : new String(bytes, Charset.forName(codeing));
		} else {
			return objArr.toString();
		}
	}

	public static char tochar(Object objArr) {
		if (null == objArr) {
			return 0;
		}
		if (objArr instanceof Character) {
			return ((Character) objArr).charValue();
		}
		throw new ClassCastException(String.format("%s not can cast to array", objArr.getClass().getName()));
	}

	public static boolean toboolean(Object objArr) {
		if (null == objArr) {
			return false;
		}
		if (objArr instanceof Boolean) {
			return ((Boolean) objArr).booleanValue();
		}
		return Boolean.parseBoolean(objArr.toString().trim());
	}

	public static byte tobyte(Object objArr) {
		if (null == objArr) {
			return 0;
		}
		if (objArr instanceof Byte) {
			return ((Byte) objArr).byteValue();
		}
		return Byte.parseByte(objArr.toString().trim());
	}

	public static int toint(Object objArr) {
		if (null == objArr) {
			return 0;
		}
		if (objArr instanceof Integer) {
			return ((Integer) objArr).intValue();
		}
		return Integer.parseInt(objArr.toString().trim());
	}

	public static long tolong(Object objArr) {
		if (null == objArr) {
			return 0;
		}
		if (objArr instanceof Long) {
			return ((Long) objArr).longValue();
		}
		return Long.parseLong(objArr.toString().trim());
	}

	public static short toshort(Object objArr) {
		if (null == objArr) {
			return 0;
		}
		if (objArr instanceof Short) {
			return ((Short) objArr).shortValue();
		}
		return Short.parseShort(objArr.toString().trim());
	}

	/**
	 * xx.xxx
	 **/
	public static double todouble(Object objArr) {
		if (null == objArr) {
			return 0;
		} else if (objArr instanceof Double) {
			return ((Double) objArr).doubleValue();
		}
		return Double.parseDouble(objArr.toString().trim());
	}

	/**
	 * xx.xxx
	 **/
	public static float tofloat(Object objArr) {
		if (null == objArr) {
			return 0;
		}
		if (objArr instanceof Float) {
			return ((Float) objArr).floatValue();
		}
		return Float.parseFloat(objArr.toString().trim());
	}

	@SuppressWarnings("unchecked")
	public static byte[] tobyteArray(Object originArray) {
		return (byte[]) XArray.copyOfConversion(originArray, XStaticBaseType.byte_class);
	}

	@SuppressWarnings("unchecked")
	public static long[] tolongArray(Object originArray) {
		return (long[]) XArray.copyOfConversion(originArray, XStaticBaseType.long_class);
	}

	@SuppressWarnings("unchecked")
	public static double[] todoubleArray(Object originArray) {
		return (double[]) XArray.copyOfConversion(originArray, XStaticBaseType.double_class);
	}

	@SuppressWarnings("unchecked")
	public static char[] tocharArray(Object originArray) {
		return (char[]) XArray.copyOfConversion(originArray, XStaticBaseType.char_class);
	}

	@SuppressWarnings("unchecked")
	public static int[] tointArray(Object originArray) {
		return (int[]) XArray.copyOfConversion(originArray, XStaticBaseType.int_class);
	}

	@SuppressWarnings("unchecked")
	public static boolean[] tobooleanArray(Object originArray) {
		return (boolean[]) XArray.copyOfConversion(originArray, XStaticBaseType.boolean_class);
	}

	@SuppressWarnings("unchecked")
	public static float[] tofloatArray(Object originArray) {
		return (float[]) XArray.copyOfConversion(originArray, XStaticBaseType.float_class);
	}

	@SuppressWarnings("unchecked")
	public static short[] toshortArray(Object originArray) {
		return (short[]) XArray.copyOfConversion(originArray, XStaticBaseType.short_class);
	}

	@SuppressWarnings("unchecked")
	public static String[] toStringArray(Object originArray) {
		return (String[]) XArray.copyOfConversion(originArray, XStaticBaseType.String_class);
	}

	@SuppressWarnings("unchecked")
	public static Object[] toObjectArray(Object originArray) {
		return (Object[]) XArray.copyOfConversion(originArray, XStaticBaseType.Object_class);
	}

	/**
	 * parse value Object[] byte[] long[] double[] char[] int[] boolean[] float[]
	 * short[]
	 * 
	 * @param str
	 * @return
	 */

	public static byte parseByte(String str) {
		String newstr = retainNum(str, 0, str.length());
		return newstr.length() == 0 ? 0 : Byte.parseByte(newstr);
	}

	public static long parseLong(String str) {
		String newstr = retainNum(str, 0, str.length());
		return newstr.length() == 0 ? 0L : Long.parseLong(newstr);
	}

	public static double parseDouble(String str) {
		String newstr = retainDouble(str, 0, str.length());
		return newstr.length() == 0 ? 0D : Double.parseDouble(newstr);
	}

	public static char parseChar(String str) {
		String newstr = retainNum(str, 0, str.length());
		return newstr.length() == 0 ? 0 : newstr.charAt(0);
	}

	public static int parseInt(String str) {
		String newstr = retainNum(str, 0, str.length());
		return newstr.length() == 0 ? 0 : Integer.parseInt(newstr);
	}

	/**
	 * Get the first boolean value
	 * 
	 * @param str
	 * @return
	 */
	public static boolean parseBoolean(String str) {
		return null == str ? false : str.equalsIgnoreCase("true");
	}

	public static float parseFloat(String str) {
		String newstr = retainDouble(str, 0, str.length());
		return newstr.length() == 0 ? 0F : Float.parseFloat(newstr);
	}

	public static short parseShort(String str) {
		String newstr = retainNum(str, 0, str.length());
		return newstr.length() == 0 ? 0 : Short.parseShort(newstr);
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
