package top.fols.box.util;

import top.fols.atri.lang.Arrayz;
import top.fols.box.statics.XStaticFixedValue;

import java.lang.reflect.Array;

public class XArrays {



	/*
	 * @return a1[i] == a2[i];
	 */
	public static boolean arrayContentsEquals(Object[] a1, Object[] a2) {
        if (null == a1) {
            return null == a2;
        }
        if (null == a2) {
            return false;
        }
        if (a1.length != a2.length) {
            return false;
        }
        int n = a1.length;
        int i = 0;
        while (n-- != 0) {
            if (a1[i] != a2[i]) {
                return false;
            }
            i++;
        }
        return true;
	}






	public static class CharSequenceUtil {

		public static boolean equals(CharSequence originalArray, char[] objectArray) {
			if (null == originalArray || null == objectArray || 
				originalArray.length() != objectArray.length) return false;
			return equalsRange(originalArray, 0, objectArray, 0, originalArray.length());
		}
		public static boolean equals(char[] originalArray, CharSequence objectArray) {
			if (null == originalArray || null == objectArray || 
				originalArray.length != objectArray.length()) return false;
			return equalsRange(originalArray, 0, objectArray, 0, originalArray.length);
		}
		public static boolean equals(CharSequence originalArray, CharSequence objectArray) {
			if (originalArray == objectArray) return true;
			if (null == originalArray || null == objectArray || 
				originalArray.length() != objectArray.length()) return false;
			return equalsRange(originalArray, 0, objectArray, 0, originalArray.length());
		}



		public static boolean equalsRange(CharSequence array, int aoff,
										  char[] b, int boff,
										  int len) {
			if (aoff + len > array.length() || boff + len > b.length) return false;
			for (int i = 0;i < len;i++) if (array.charAt(i + aoff) != b[i + boff]) return false; 
			return true;
		}
		public static boolean equalsRange(char[] array, int aoff,
										  CharSequence b, int boff,
										  int len) {
			if (aoff + len > array.length || boff + len > b.length()) return false;
			for (int i = 0;i < len;i++) if (array[i + aoff] != b.charAt(i + boff)) return false; 
			return true;
		}
		public static boolean equalsRange(CharSequence array, int aoff,
										  CharSequence b, int boff,
										  int len) {
			if (array == b && aoff == boff && aoff + len <= array.length()) return true;
			if (aoff + len > array.length() || boff + len > b.length()) return false;
			for (int i = 0;i < len;i++) if (array.charAt(i + aoff) != b.charAt(i + boff)) return false; 
			return true;
		}

		public static boolean startsWith(char[] b, CharSequence s) { return startsWith(b, s, 0); }
		public static boolean startsWith(CharSequence b, char[] s) { return startsWith(b, s, 0); }
		public static boolean startsWith(CharSequence b, CharSequence s) { return startsWith(b, s, 0); }


		public static boolean endWith(char[] b, CharSequence s) { return startsWith(b, s, b.length - s.length()); }
		public static boolean endWith(CharSequence b, char[] s) { return startsWith(b, s, b.length() - s.length); }
		public static boolean endWith(CharSequence b, CharSequence s) { return startsWith(b, s, b.length() - s.length()); }

		public static boolean startsWith(char[] array, CharSequence b, int off) { return equalsRange(array, off, b, 0, b.length()); }
		public static boolean startsWith(CharSequence array, char[] b, int off) { return equalsRange(array, off, b, 0, b.length); }
		public static boolean startsWith(CharSequence array, CharSequence b, int off) { return equalsRange(array, off, b, 0, b.length()); }


		public static void arraycopyTraverse(CharSequence originArray, int originarrayCopyOffIndex, Object newArray, int off, int len) {
			if (newArray instanceof byte[]) {
				byte[] array = (byte[])newArray;
				for (int i = 0;i < len;i++)array[i + off] = (byte)originArray.charAt(i + originarrayCopyOffIndex);
			} else if (newArray instanceof long[]) {
				long[] array = (long[])newArray;
				for (int i = 0;i < len;i++)array[i + off] = originArray.charAt(i + originarrayCopyOffIndex);
			} else if (newArray instanceof double[]) {
				double[] array = (double[])newArray;
				for (int i = 0;i < len;i++)array[i + off] = originArray.charAt(i + originarrayCopyOffIndex);
			} else if (newArray instanceof char[]) {
				char[] array = (char[])newArray;
				for (int i = 0;i < len;i++)array[i + off] = originArray.charAt(i + originarrayCopyOffIndex);
			} else if (newArray instanceof int[]) {
				System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
			} else if (newArray instanceof boolean[]) {
				throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
			} else if (newArray instanceof float[]) {
				float[] array = (float[])newArray;
				for (int i = 0;i < len;i++)array[i + off] = originArray.charAt(i + originarrayCopyOffIndex);
			} else if (newArray instanceof short[]) {
				short[] array = (short[])newArray;
				for (int i = 0;i < len;i++)array[i + off] = (short)originArray.charAt(i + originarrayCopyOffIndex);
			} else if (newArray instanceof String[]) {
				String[] array = (String[])newArray;
				for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray.charAt(i + originarrayCopyOffIndex));
			} else {
				Object[] array = (Object[])newArray;
				for (int i = 0;i < len;i++)array[i + off] = originArray.charAt(i + originarrayCopyOffIndex);
			}
		}

		public static char[] tocharArray(CharSequence originArray) {
			if (null == originArray) return XStaticFixedValue.nullcharArray;
			char[] newArray = new char[originArray.length()];
			for (int i = 0;i < originArray.length();i++) newArray[i] = originArray.charAt(i);
			return newArray;
		}

		/*
		 * char sequence
		 */
		public static int indexOf(CharSequence str, char b, int start, int indexRange) {
			int strlength;
			if (null == str || (strlength = str.length()) == 0 || start >= indexRange)
				return -1;
			if (indexRange > strlength)
				indexRange = strlength;
			while (start < indexRange) {
				if (str.charAt(start) == b) return start;
				start++;
			}
			return -1;
		}
		/*
		 * char sequence
		 */
		public static int lastIndexOf(CharSequence str, char b, int startIndex, int indexRange) {
			int strlength;
			if (null == str || (strlength = str.length()) == 0 || indexRange > startIndex)
				return -1;
			if (startIndex > strlength - 1)
				startIndex = strlength - 1 ;
			while (startIndex >= indexRange) {
				if (str.charAt(startIndex) == b) {
					return startIndex;
				}
				startIndex--;
			}
			return -1;
		}

		/*
		 * char sequence
		 */
		public static int indexOf(CharSequence str, char[] b, int start, int indexRange) {
			int strlength;
			if (null == str || (strlength = str.length()) == 0 || start > indexRange || null == b || b.length > strlength || b.length == 0 || indexRange - start < b.length)
				return -1;
			if (indexRange > strlength)
				indexRange = strlength;
			int i, i2;
			for (i = start; i < indexRange; i++) {
				if (str.charAt(i) == b[0]) {
					if (indexRange - i < b.length)
						break;
					for (i2 = 1; i2 < b.length; i2++)
						if (str.charAt(i + i2) != b[i2])
							break;
					if (i2 == b.length)
						return i;
				}
			}
			return -1;
		}
		public static int indexOf(char[] str, CharSequence b, int start, int indexRange) {
			int blength;
			if (null == str || str.length == 0 || start > indexRange || null == b || (blength = b.length()) > str.length || blength == 0 || indexRange - start < blength)
				return -1;
			if (indexRange > str.length)
				indexRange = str.length;
			int i, i2;
			for (i = start; i < indexRange; i++) {
				if (str[i] == b.charAt(0)) {
					if (indexRange - i < blength)
						break;
					for (i2 = 1; i2 < blength; i2++)
						if (str[i + i2] != b.charAt(i2))
							break;
					if (i2 == blength)
						return i;
				}
			}
			return -1;
		}
		public static int indexOf(CharSequence str, CharSequence b, int start, int indexRange) {
			int blength;
			if (null == str || str.length() == 0 || start > indexRange || null == b || (blength = b.length()) > str.length() || blength == 0 || indexRange - start < blength)
				return -1;
			if (indexRange > str.length())
				indexRange = str.length();
			int i, i2;
			for (i = start; i < indexRange; i++) {
				if (str.charAt(i) == b.charAt(0)) {
					if (indexRange - i < blength)
						break;
					for (i2 = 1; i2 < blength; i2++)
						if (str.charAt(i + i2) != b.charAt(i2))
							break;
					if (i2 == blength)
						return i;
				}
			}
			return -1;
		}
		/*
		 * char sequence
		 */
		public static int lastIndexOf(CharSequence array , char[] b, int startIndex, int indexRange) {
			int arraylength;
			if (null == array || (arraylength = array.length()) == 0 || indexRange > startIndex || null == b || b.length > arraylength || b.length == 0 || startIndex - indexRange < b.length)
				return -1;
			if (startIndex > arraylength)
				startIndex = arraylength;
			int i, i2;
			for (i = startIndex == arraylength ?arraylength - 1: startIndex; i >= indexRange; i--) {
				if (array.charAt(i) == b[0]) {
					if (i + b.length > startIndex)
						continue;
					for (i2 = 1; i2 < b.length; i2++)
						if (array.charAt(i + i2) != b[i2])
							break;
					if (i2 == b.length)
						return i;
				}
			}
			return -1;
		}
		public static int lastIndexOf(char[] array , CharSequence b, int startIndex, int indexRange) {
			int blength;
			if (null == array || array.length == 0 || indexRange > startIndex || null == b || (blength = b.length()) > array.length || blength == 0 || startIndex - indexRange < blength)
				return -1;
			if (startIndex > array.length)
				startIndex = array.length;
			int i, i2;
			for (i = startIndex == array.length ?array.length - 1: startIndex; i >= indexRange; i--) {
				if (array[i] == b.charAt(0)) {
					if (i + blength > startIndex)
						continue;
					for (i2 = 1; i2 < blength; i2++)
						if (array[i + i2] != b.charAt(i2))
							break;
					if (i2 == blength)
						return i;
				}
			}
			return -1;
		}
		public static int lastIndexOf(CharSequence array , CharSequence b, int startIndex, int indexRange) {
			int blength;
			if (null == array || array.length() == 0 || indexRange > startIndex || null == b || (blength = b.length()) > array.length() || blength == 0 || startIndex - indexRange < blength)
				return -1;
			if (startIndex > array.length())
				startIndex = array.length();
			int i, i2;
			for (i = startIndex == array.length() ?array.length() - 1: startIndex; i >= indexRange; i--) {
				if (array.charAt(i) == b.charAt(0)) {
					if (i + blength > startIndex)
						continue;
					for (i2 = 1; i2 < blength; i2++)
						if (array.charAt(i + i2) != b.charAt(i2))
							break;
					if (i2 == blength)
						return i;
				}
			}
			return -1;
		}
	}






	/*
	 * all array type:

	 * Object[]
	 * byte[]
	 * long[]
	 * double[]
	 * char[]
	 * int[]
	 * boolean[]
	 * float[]
	 * short[]

	 * void[] unrealistic
	 */
	public static boolean equalsRange(byte[] array, int aoff,
									  byte[] b, int boff,
									  int len) {
		if (array == b && aoff == boff && aoff + len <= array.length) return true;
		if (aoff + len > array.length || boff + len > b.length) return false;
		for (int i = 0;i < len;i++) if (array[i + aoff] != b[i + boff]) return false;
		return true;
	}
	public static boolean equalsRange(long[] array, int aoff,
									  long[] b, int boff,
									  int len) {
		if (array == b && aoff == boff && aoff + len <= array.length) return true;
		if (aoff + len > array.length || boff + len > b.length) return false;
		for (int i = 0;i < len;i++) if (array[i + aoff] != b[i + boff]) return false;
		return true;
	}
	public static boolean equalsRange(char[] array, int aoff,
									  char[] b, int boff,
									  int len) {
		if (array == b && aoff == boff && aoff + len <= array.length) return true;
		if (aoff + len > array.length || boff + len > b.length) return false;
		for (int i = 0;i < len;i++) if (array[i + aoff] != b[i + boff]) return false;
		return true;
	}
	public static boolean equalsRange(int[] array, int aoff,
									  int[] b, int boff,
									  int len) {
		if (array == b && aoff == boff && aoff + len <= array.length) return true;
		if (aoff + len > array.length || boff + len > b.length) return false;
		for (int i = 0;i < len;i++) if (array[i + aoff] != b[i + boff]) return false;
		return true;
	}
	public static boolean equalsRange(short[] array, int aoff,
									  short[] b, int boff,
									  int len) {
		if (array == b && aoff == boff && aoff + len <= array.length) return true;
		if (aoff + len > array.length || boff + len > b.length) return false;
		for (int i = 0;i < len;i++) if (array[i + aoff] != b[i + boff]) return false;
		return true;
	}
	public static boolean equalsRange(boolean[] array, int aoff,
									  boolean[] b, int boff,
									  int len) {
		if (array == b && aoff == boff && aoff + len <= array.length) return true;
		if (aoff + len > array.length || boff + len > b.length) return false;
		for (int i = 0;i < len;i++) if (array[i + aoff] != b[i + boff]) return false;
		return true;
	}
	public static boolean equalsRange(double[] array, int aoff,
									  double[] b, int boff,
									  int len) {
		if (array == b && aoff == boff && aoff + len <= array.length) return true;
		if (aoff + len > array.length || boff + len > b.length) return false;
		for (int i = 0;i < len;i++) if (array[i + aoff] != b[i + boff]) return false;
		return true;
	}
	public static boolean equalsRange(float[] array, int aoff,
									  float[] b, int boff,
									  int len) {
		if (array == b && aoff == boff && aoff + len <= array.length) return true;
		if (aoff + len > array.length || boff + len > b.length) return false;
		for (int i = 0;i < len;i++) if (array[i + aoff] != b[i + boff]) return false;
		return true;
	}
	public static boolean equalsRange(Object[] array, int aoff,
									  Object[] b, int boff,
									  int len) {
		if (array == b && aoff == boff && aoff + len <= array.length) return true;
		if (aoff + len > array.length || boff + len > b.length) return false;
		for (int i = 0;i < len;i++) {
			if (null == array[i + aoff]) {
				if (null != b[i + boff]) {
					return false;
				}
			} else if (!array[i + aoff].equals(b[i + boff])) {
				return false;
			}
		} 
		return true;
	}
	public static boolean equalsRange(Object originalArray, int originalArrayOff, Object objectArray, int objectArrayOff, int len) {
		if (originalArray == objectArray && originalArrayOff == objectArrayOff && originalArrayOff + len <= Arrayz.getLength(originalArray)) return true;
		if (null == originalArray || 
			null == objectArray || 
			! XArray.equalsArrayType(originalArray, objectArray)) return false;
		else if (originalArray instanceof Object[]) return equalsRange((Object[])originalArray, originalArrayOff, (Object[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof byte[]) return equalsRange((byte[])originalArray, originalArrayOff, (byte[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof long[]) return equalsRange((long[])originalArray, originalArrayOff, (long[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof char[]) return equalsRange((char[])originalArray, originalArrayOff, (char[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof int[]) return equalsRange((int[])originalArray, originalArrayOff, (int[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof short[]) return equalsRange((short[])originalArray, originalArrayOff, (short[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof boolean[]) return equalsRange((boolean[])originalArray, originalArrayOff, (boolean[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof double[]) return equalsRange((double[])originalArray, originalArrayOff, (double[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof float[]) return equalsRange((float[])originalArray, originalArrayOff, (float[])objectArray, objectArrayOff, len);
		else return false;
	}




	public static boolean equals(byte[] originalArray, byte[] objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray || 
			originalArray.length != objectArray.length) return false;
		return equalsRange(originalArray, 0, objectArray, 0, originalArray.length);
	}
	public static boolean equals(long[] originalArray, long[] objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray ||
			originalArray.length != objectArray.length) return false;
		return equalsRange(originalArray, 0, objectArray, 0, originalArray.length);
	}
	public static boolean equals(char[] originalArray, char[] objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray ||
			originalArray.length != objectArray.length) return false;
		return equalsRange(originalArray, 0, objectArray, 0, originalArray.length);
	}
	public static boolean equals(int[] originalArray, int[] objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray || 
			originalArray.length != objectArray.length) return false;
		return equalsRange(originalArray, 0, objectArray, 0, originalArray.length);
	}
	public static boolean equals(short[] originalArray, short[] objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray ||
			originalArray.length != objectArray.length) return false;
		return equalsRange(originalArray, 0, objectArray, 0, originalArray.length);
	}
	public static boolean equals(boolean[] originalArray, boolean[] objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray ||
			originalArray.length != objectArray.length) return false;
		return equalsRange(originalArray, 0, objectArray, 0, originalArray.length);
	}
	public static boolean equals(double[] originalArray, double[] objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray || 
			originalArray.length != objectArray.length) return false;
		return equalsRange(originalArray, 0, objectArray, 0, originalArray.length);
	}
	public static boolean equals(float[] originalArray, float[] objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray || 
			originalArray.length != objectArray.length) return false;
		return equalsRange(originalArray, 0, objectArray, 0, originalArray.length);
	}
	public static boolean equals(Object[] originalArray, Object[] objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray || 
			originalArray.length != objectArray.length) return false;
		return equalsRange(originalArray, 0, objectArray, 0, originalArray.length);
	}
	public static boolean equals(Object originalArray, Object objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray ||
			null == objectArray || 
			! XArray.equalsArrayType(originalArray, objectArray)) return false;
		else if (originalArray instanceof Object[]) return equals((Object[])originalArray, (Object[])objectArray);
		else if (originalArray instanceof byte[]) return equals((byte[])originalArray, (byte[])objectArray);
		else if (originalArray instanceof long[]) return equals((long[])originalArray, (long[])objectArray);
		else if (originalArray instanceof char[]) return equals((char[])originalArray, (char[])objectArray);
		else if (originalArray instanceof int[]) return equals((int[])originalArray, (int[])objectArray);
		else if (originalArray instanceof short[]) return equals((short[])originalArray, (short[])objectArray);
		else if (originalArray instanceof boolean[]) return equals((boolean[])originalArray, (boolean[])objectArray);
		else if (originalArray instanceof double[]) return equals((double[])originalArray, (double[])objectArray);
		else if (originalArray instanceof float[]) return equals((float[])originalArray, (float[])objectArray);
		else return false;
	}



	public static boolean deepEqualsRange(Object[] originalArray, int originArrayOff, Object[] objectArray, int objectArrayOff, int len) {
		if (originalArray == objectArray && originArrayOff == objectArrayOff && originArrayOff + len <= originalArray.length) return true;
		if (originArrayOff + len > originalArray.length || objectArrayOff + len > objectArray.length) return false;
		for (int i = 0;i < len;i++) {
			Object o = originalArray[i + originArrayOff];
			Object o1 = objectArray[i + objectArrayOff];
			if (XArray.isArray(o)) {
				if (!deepEquals(o, o1))
					return false;
			} else if (null == o) {
				if (null != o1)
					return false;
			} else {
				if (!o.equals(o1))
					return false;
			}
		}
		return true;
	}
	public static boolean deepEquals(Object[] originalArray, Object[] objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray || 
			originalArray.length != objectArray.length) return false;
		return deepEqualsRange(originalArray, 0, objectArray, 0, objectArray.length);
	}

	public static boolean deepEqualsRange(Object originalArray, int originalArrayOff, Object objectArray, int objectArrayOff, int len) {
		if (originalArray == objectArray && originalArrayOff == objectArrayOff && originalArrayOff + len <= Arrayz.getLength(originalArray)) return true;
		if (null == originalArray ||
			null == objectArray || 
			! XArray.equalsArrayType(originalArray, objectArray)) return false;
		else if (originalArray instanceof Object[]) return deepEqualsRange((Object[])originalArray, originalArrayOff, (Object[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof byte[]) return equalsRange((byte[])originalArray, originalArrayOff, (byte[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof long[]) return equalsRange((long[])originalArray, originalArrayOff, (long[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof char[]) return equalsRange((char[])originalArray, originalArrayOff, (char[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof int[]) return equalsRange((int[])originalArray, originalArrayOff, (int[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof short[]) return equalsRange((short[])originalArray, originalArrayOff, (short[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof boolean[]) return equalsRange((boolean[])originalArray, originalArrayOff, (boolean[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof double[]) return equalsRange((double[])originalArray, originalArrayOff, (double[])objectArray, objectArrayOff, len);
		else if (originalArray instanceof float[]) return equalsRange((float[])originalArray, originalArrayOff, (float[])objectArray, objectArrayOff, len);
		else return false;
	}
	public static boolean deepEquals(Object originalArray, Object objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray || 
			! XArray.equalsArrayType(originalArray, objectArray)) return false;
		else if (originalArray instanceof Object[]) return deepEquals((Object[])originalArray, (Object[])objectArray);
		else if (originalArray instanceof byte[]) return equals((byte[])originalArray, (byte[])objectArray);
		else if (originalArray instanceof long[]) return equals((long[])originalArray, (long[])objectArray);
		else if (originalArray instanceof char[]) return equals((char[])originalArray, (char[])objectArray);
		else if (originalArray instanceof int[]) return equals((int[])originalArray, (int[])objectArray);
		else if (originalArray instanceof short[]) return equals((short[])originalArray, (short[])objectArray);
		else if (originalArray instanceof boolean[]) return equals((boolean[])originalArray, (boolean[])objectArray);
		else if (originalArray instanceof double[]) return equals((double[])originalArray, (double[])objectArray);
		else if (originalArray instanceof float[]) return equals((float[])originalArray, (float[])objectArray);
		else return false;
	}









	public static boolean startsWith(byte[] b, byte[] s) { return startsWith(b, s, 0); }
	public static boolean startsWith(long[] b, long[] s) { return startsWith(b, s, 0); }
	public static boolean startsWith(double[] b, double[] s) { return startsWith(b, s, 0); }
	public static boolean startsWith(char[] b, char[] s) { return startsWith(b, s, 0); }
	public static boolean startsWith(int[] b, int[] s) { return startsWith(b, s, 0); }
	public static boolean startsWith(boolean[] b, boolean[] s) { return startsWith(b, s, 0); }
	public static boolean startsWith(float[] b, float[] s) { return startsWith(b, s, 0); }
	public static boolean startsWith(short[] b, short[] s) { return startsWith(b, s, 0); }
	public static boolean startsWith(Object[] b, Object[] s) { return startsWith(b, s, 0); }
	public static boolean startsWith(Object originalArray, Object objectArray) { 
		if (originalArray == objectArray) return true;
		if (null == originalArray ||
			null == objectArray || 
			! XArray.equalsArrayType(originalArray, objectArray)) return false;
		else if (originalArray instanceof Object[]) return startsWith(((Object[])originalArray), ((Object[])objectArray));
		else if (originalArray instanceof byte[]) return startsWith(((byte[])originalArray), ((byte[])objectArray));
		else if (originalArray instanceof long[]) return startsWith(((long[])originalArray), ((long[])objectArray));
		else if (originalArray instanceof char[]) return startsWith(((char[])originalArray), ((char[])objectArray));
		else if (originalArray instanceof int[]) return startsWith(((int[])originalArray), ((int[])objectArray));
		else if (originalArray instanceof short[]) return startsWith(((short[])originalArray), ((short[])objectArray));
		else if (originalArray instanceof boolean[]) return startsWith(((boolean[])originalArray), ((boolean[])objectArray));
		else if (originalArray instanceof double[]) return startsWith(((double[])originalArray), ((double[])objectArray));
		else if (originalArray instanceof float[]) return startsWith(((float[])originalArray), ((float[])objectArray));
		else return false;
	}


	public static boolean endWith(byte[] b, byte[] s) { return startsWith(b, s, b.length - s.length); }
	public static boolean endWith(long[] b, long[] s) { return startsWith(b, s, b.length - s.length); }
	public static boolean endWith(double[] b, double[] s) { return startsWith(b, s, b.length - s.length); }
	public static boolean endWith(char[] b, char[] s) { return startsWith(b, s, b.length - s.length); }
	public static boolean endWith(int[] b, int[] s) { return startsWith(b, s, b.length - s.length); }
	public static boolean endWith(boolean[] b, boolean[] s) { return startsWith(b, s, b.length - s.length); }
	public static boolean endWith(float[] b, float[] s) { return startsWith(b, s, b.length - s.length); }
	public static boolean endWith(short[] b, short[] s) { return startsWith(b, s, b.length - s.length); }
	public static boolean endWith(Object[] b, Object[] s) { return startsWith(b, s, b.length - s.length); }
	public static boolean endWith(Object originalArray, Object objectArray) { 
		if (originalArray == objectArray) return true;
		if (null == originalArray ||
			null == objectArray || 
			! XArray.equalsArrayType(originalArray, objectArray)) return false;
		else if (originalArray instanceof Object[]) return endWith(((Object[])originalArray), ((Object[])objectArray));
		else if (originalArray instanceof byte[]) return endWith(((byte[])originalArray), ((byte[])objectArray));
		else if (originalArray instanceof long[]) return endWith(((long[])originalArray), ((long[])objectArray));
		else if (originalArray instanceof char[]) return endWith(((char[])originalArray), ((char[])objectArray));
		else if (originalArray instanceof int[]) return endWith(((int[])originalArray), ((int[])objectArray));
		else if (originalArray instanceof short[]) return endWith(((short[])originalArray), ((short[])objectArray));
		else if (originalArray instanceof boolean[]) return endWith(((boolean[])originalArray), ((boolean[])objectArray));
		else if (originalArray instanceof double[]) return endWith(((double[])originalArray), ((double[])objectArray));
		else if (originalArray instanceof float[]) return endWith(((float[])originalArray), ((float[])objectArray));
		else return false;
	}


	public static boolean startsWith(byte[] array, byte[] b, int off) { return equalsRange(array, off, b, 0, b.length); }
	public static boolean startsWith(long[] array, long[] b, int off) { return equalsRange(array, off, b, 0, b.length); }
	public static boolean startsWith(char[] array, char[] b, int off) { return equalsRange(array, off, b, 0, b.length); }
	public static boolean startsWith(int[] array, int[] b, int off) { return equalsRange(array, off, b, 0, b.length); }
	public static boolean startsWith(short[] array, short[] b, int off) { return equalsRange(array, off, b, 0, b.length); }
	public static boolean startsWith(boolean[] array, boolean[] b, int off) { return equalsRange(array, off, b, 0, b.length); }
	public static boolean startsWith(double[] array, double[] b, int off) { return equalsRange(array, off, b, 0, b.length); }
	public static boolean startsWith(float[] array, float[] b, int off) { return equalsRange(array, off, b, 0, b.length); }
	public static boolean startsWith(Object[] array, Object[] b, int off) { return equalsRange(array, off, b, 0, b.length); }
	public static boolean startsWith(Object originalArray, Object objectArray, int off) { 
		if (originalArray == objectArray && off < Arrayz.getLength(originalArray))return true;
		if (null == originalArray ||
			null == objectArray || 
			! XArray.equalsArrayType(originalArray, objectArray)) return false;
		else if (originalArray instanceof Object[]) return startsWith(((Object[])originalArray), ((Object[])objectArray), off);
		else if (originalArray instanceof byte[]) return startsWith(((byte[])originalArray), ((byte[])objectArray), off);
		else if (originalArray instanceof long[]) return startsWith(((long[])originalArray), ((long[])objectArray), off);
		else if (originalArray instanceof char[]) return startsWith(((char[])originalArray), ((char[])objectArray), off);
		else if (originalArray instanceof int[]) return startsWith(((int[])originalArray), ((int[])objectArray), off);
		else if (originalArray instanceof short[]) return startsWith(((short[])originalArray), ((short[])objectArray), off);
		else if (originalArray instanceof boolean[]) return startsWith(((boolean[])originalArray), ((boolean[])objectArray), off);
		else if (originalArray instanceof double[]) return startsWith(((double[])originalArray), ((double[])objectArray), off);
		else if (originalArray instanceof float[]) return endWith(((float[])originalArray), ((float[])objectArray));
		else return false;
	}

	/*
	 indexOf Array
	 找数组

	 [1, 2, 3, 4, 5, 6, 7]
	 0  1  2  3  4  5  6  7

	 indexOf(new int[]{1,2,3,4,5,6,7},1  ,0,1); >> 0
	 indexOf(new int[]{1,2,3,4,5,6,7},4  ,0,5); >> 3  //array[0] >> array[5-1]
	 */
	public static int indexOf(byte[] array, byte b, int start, int indexRange) {
		if (null == array || array.length == 0 || start >= indexRange)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange) {
            if (array[start] == b) return start;
            start++;
        }
        return -1;
    }
	public static int indexOf(long[] array, long b, int start, int indexRange) {
		if (null == array || array.length == 0 || start >= indexRange)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange) {
            if (array[start] == b) return start;
            start++;
        }
        return -1;
    }
	public static int indexOf(char[] array, char b, int start, int indexRange) {
		if (null == array || array.length == 0 || start >= indexRange)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange) {
            if (array[start] == b) return start;
            start++;
        }
        return -1;
    }
	public static int indexOf(int[] array, int b, int start, int indexRange) {
		if (null == array || array.length == 0 || start >= indexRange)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange) {
            if (array[start] == b) return start;
            start++;
        }
        return -1;
    }
	public static int indexOf(short[] array, short b, int start, int indexRange) {
		if (null == array || array.length == 0 || start >= indexRange)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange) {
            if (array[start] == b) return start;
            start++;
        }
        return -1;
    }
	public static int indexOf(boolean[] array, boolean b, int start, int indexRange) {
		if (null == array || array.length == 0 || start >= indexRange)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange) {
            if (array[start] == b) return start;
            start++;
        }
        return -1;
    }
	public static int indexOf(double[] array, double b, int start, int indexRange) {
		if (null == array || array.length == 0 || start >= indexRange)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange) {
            if (array[start] == b) return start;
            start++;
        }
        return -1;
    }
	public static int indexOf(float[] array, float b, int start, int indexRange) {
		if (null == array || array.length == 0 || start >= indexRange)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange) {
            if (array[start] == b) return start;
            start++;
        }
        return -1;
    }
	public static int indexOf(Object[] array, Object b, int start, int indexRange) {
		if (null == array || array.length == 0 || start >= indexRange)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
		if (null == b) {
			while (start < indexRange) {
				if (null == array[start]) return start;
				start++;
			}
		} else {
			while (start < indexRange) {
				if (b.equals(array[start])) return start;
				start++;
			}
		}
        return -1;
    }

	/*
	 last indexOf Array
	 倒找数组

	 [1, 2, 3, 4, 5, 6, 7]
	 0  1  2  3  4  5  6  7

	 lastIndexOf(new int[]{1,2,3,4,5,6,7},2  ,3,0); >> 1  //array[3] >> array[0]
	 */
	public static int lastIndexOf(byte[] array, byte b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex)
			return -1;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange) {
            if (array[startIndex] == b) return startIndex;
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(long[] array, long b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex)
			return -1;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange) {
            if (array[startIndex] == b) return startIndex;
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(char[] array, char b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex)
			return -1;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange) {
            if (array[startIndex] == b) return startIndex;
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(int[] array, int b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex)
			return -1;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange) {
            if (array[startIndex] == b) return startIndex;
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(short[] array, short b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex)
			return -1;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange) {
            if (array[startIndex] == b) return startIndex;
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(boolean[] array, boolean b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex)
			return -1;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange) {
            if (array[startIndex] == b) return startIndex;
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(double[] array, double b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex)
			return -1;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange) {
            if (array[startIndex] == b) return startIndex;
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(float[] array, float b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex)
			return -1;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange) {
            if (array[startIndex] == b) return startIndex;
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(Object[] array, Object b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex)
			return -1;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
		if (null == b) {
			while (startIndex >= indexRange) {
				if (null == array[startIndex]) {
					return startIndex;
				}
				startIndex--;
			}
		} else {
			while (startIndex >= indexRange) {
				if (b.equals(array[startIndex])) {
					return startIndex;
				}
				startIndex--;
			}
		}
        return -1;
    }














	/*
	 indexOf Array
	 找数组

	 indexOf(new byte[]{0,0,2,0,0,2,0,0,1,3,5,9},new byte[]{2,0,0},3,12); >> 5   array[3] >> array[12-1] 必须匹配b[0]然后再匹配后面的元素
	 */
	public static int indexOf(byte[] array, byte[] b, int start, int indexRange) {
		if (null == array || array.length == 0 || start > indexRange || null == b || b.length > array.length || b.length == 0 || indexRange - start < b.length)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++) {
			if (array[i] == b[0]) {
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(long[] array, long[] b, int start, int indexRange) {
		if (null == array || array.length == 0 || start > indexRange || null == b || b.length > array.length || b.length == 0 || indexRange - start < b.length)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++) {
			if (array[i] == b[0]) {
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(char[] array, char[] b, int start, int indexRange) {
		if (null == array || array.length == 0 || start > indexRange || null == b || b.length > array.length || b.length == 0 || indexRange - start < b.length)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++) {
			if (array[i] == b[0]) {
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(int[] array, int[] b, int start, int indexRange) {
		if (null == array || array.length == 0 || start > indexRange || null == b || b.length > array.length || b.length == 0 || indexRange - start < b.length)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++) {
			if (array[i] == b[0]) {
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(short[] array, short[] b, int start, int indexRange) {
		if (null == array || array.length == 0 || start > indexRange || null == b || b.length > array.length || b.length == 0 || indexRange - start < b.length)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++) {
			if (array[i] == b[0]) {
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(boolean[] array, boolean[] b, int start, int indexRange) {
		if (null == array || array.length == 0 || start > indexRange || null == b || b.length > array.length || b.length == 0 || indexRange - start < b.length)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++) {
			if (array[i] == b[0]) {
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(double[] array, double[] b, int start, int indexRange) {
		if (null == array || array.length == 0 || start > indexRange || null == b || b.length > array.length || b.length == 0 || indexRange - start < b.length)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++) {
			if (array[i] == b[0]) {
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(float[] array, float[] b, int start, int indexRange) {
		if (null == array || array.length == 0 || start > indexRange || null == b || b.length > array.length || b.length == 0 || indexRange - start < b.length)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++) {
			if (array[i] == b[0]) {
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(Object[] array, Object[] b, int start, int indexRange) {
		if (null == array || array.length == 0 || start > indexRange || null == b || b.length > array.length || b.length == 0 || indexRange - start < b.length)
			return -1;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++) {
			if (null == array[i]) {
				if (null == b[0]) {
					if (indexRange - i < b.length)
						break;
					for (i2 = 1; i2 < b.length; i2++) {
						if (null == array[i + i2]) {
							if (null != b[i2])
								break;
						} else {
							if (!array[i + i2].equals(b[i2]))
								break;
						}
					}
					if (i2 == b.length)
						return i;
				}
			} else if (array[i].equals(b[0])) {
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++) {
					if (null == array[i + i2]) {
						if (null != b[i2])
							break;
					} else if (!array[i + i2].equals(b[i2]))
						break;
				}
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}














	/*
	 last indexOf Array
	 倒找数组

	 [1, 2, 3, 4, 5, 6, 7]
	 0  1  2  3  4  5  6  7

	 lastIndexOf(new byte[]{0,0,2,0,0,2,0,0,1,3,5,9},new byte[]{2,0,0},3,0); >> 2 //array[3] >> array[0]
	 lastIndexOf(new byte[]{0,0,2,0,0,2,0,0,1,3,5,9},new byte[]{5,9},11,0)); >> 10  //array[11] >> array[0]   array[11] >> array[0] 必须匹配b[0]然后再匹配后面的元素
	 */
	public static int lastIndexOf(byte[] array, byte[] b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex || null == b || b.length > array.length || b.length == 0 || startIndex - indexRange < b.length)
			return -1;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length ?array.length - 1: startIndex; i >= indexRange; i--) {
			if (array[i] == b[0]) {
				if (i + b.length > startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(long[] array, long[] b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex || null == b || b.length > array.length || b.length == 0 || startIndex - indexRange < b.length)
			return -1;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length ?array.length - 1: startIndex; i >= indexRange; i--) {
			if (array[i] == b[0]) {
				if (i + b.length > startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(char[] array, char[] b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex || null == b || b.length > array.length || b.length == 0 || startIndex - indexRange < b.length)
			return -1;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length ?array.length - 1: startIndex; i >= indexRange; i--) {
			if (array[i] == b[0]) {
				if (i + b.length > startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(int[] array, int[] b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex || null == b || b.length > array.length || b.length == 0 || startIndex - indexRange < b.length)
			return -1;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length ?array.length - 1: startIndex; i >= indexRange; i--) {
			if (array[i] == b[0]) {
				if (i + b.length > startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(short[] array, short[] b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex || null == b || b.length > array.length || b.length == 0 || startIndex - indexRange < b.length)
			return -1;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length ?array.length - 1: startIndex; i >= indexRange; i--) {
			if (array[i] == b[0]) {
				if (i + b.length > startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(boolean[] array, boolean[] b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex || null == b || b.length > array.length || b.length == 0 || startIndex - indexRange < b.length)
			return -1;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length ?array.length - 1: startIndex; i >= indexRange; i--) {
			if (array[i] == b[0]) {
				if (i + b.length > startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(double[] array, double[] b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex || null == b || b.length > array.length || b.length == 0 || startIndex - indexRange < b.length)
			return -1;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length ?array.length - 1: startIndex; i >= indexRange; i--) {
			if (array[i] == b[0]) {
				if (i + b.length > startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(float[] array, float[] b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex || null == b || b.length > array.length || b.length == 0 || startIndex - indexRange < b.length)
			return -1;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length ?array.length - 1: startIndex; i >= indexRange; i--) {
			if (array[i] == b[0]) {
				if (i + b.length > startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(Object[] array, Object[] b, int startIndex, int indexRange) {
		if (null == array || array.length == 0 || indexRange > startIndex || null == b || b.length > array.length || b.length == 0 || startIndex - indexRange < b.length)
			return -1;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length ?array.length - 1: startIndex; i >= indexRange; i--) {
			if (null == array[i]) {
				if (null == b[0]) {
					if (i + b.length > startIndex)
						continue;
					for (i2 = 1; i2 < b.length; i2++) {
						if (null == array[i + i2]) {
							if (null != b[i2]) {
								break;
							}
						} else if (!array[i + i2].equals(b[i2]))
							break;
					}
					if (i2 == b.length)
						return i;
				}
			} else if (array[i].equals(b[0])) {
				if (i + b.length > startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++) {
					if (null == array[i + i2]) {
						if (null != b[i2]) {
							break;
						}
					} else if (!array[i + i2].equals(b[i2]))
						break;
				}
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}















	/*
	 * copy array traverse
	 * 使用遍历方式复制数组
	 * 参数与arraycopy同
	 */
	public static void arraycopyTraverse(byte[]originArray, int originarrayCopyOffIndex, Object newArray, int off, int len) {
		if (newArray instanceof byte[]) {
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		} else if (newArray instanceof long[]) {
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof double[]) {
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof char[]) {
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (char)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof int[]) {
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof boolean[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof float[]) {
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof short[]) {
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof String[]) {
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		} else {
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(long[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len) {
		if (newArray instanceof byte[]) {
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof long[]) {
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		} else if (newArray instanceof double[]) {
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof char[]) {
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (char)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof int[]) {
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (int)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof boolean[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof float[]) {
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof short[]) {
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (short)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof String[]) {
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		} else {
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(double[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len) {
		if (newArray instanceof byte[]) {
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof long[]) {
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (long)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof double[]) {
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		} else if (newArray instanceof char[]) {
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (char)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof int[]) {
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (int)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof boolean[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof float[]) {
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (float)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof short[]) {
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (short)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof String[]) {
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		} else {
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(char[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len) {
		if (newArray instanceof byte[]) {
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof long[]) {
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof double[]) {
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof char[]) {
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		} else if (newArray instanceof int[]) {
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof boolean[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof float[]) {
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof short[]) {
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (short)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof String[]) {
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		} else {
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(int[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len) {
		if (newArray instanceof byte[]) {
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof long[]) {
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof double[]) {
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof char[]) {
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ( char)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof int[]) {
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		} else if (newArray instanceof boolean[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof float[]) {
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof short[]) {
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (short)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof String[]) {
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		} else {
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(boolean[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len) {
		if (newArray instanceof byte[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof long[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof double[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof char[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof int[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof boolean[]) {
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		} else if (newArray instanceof float[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof short[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof String[]) {
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		} else {
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(float[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len) {
		if (newArray instanceof byte[]) {
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof long[]) {
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (long)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof double[]) {
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof char[]) {
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (char)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof int[]) {
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (int)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof boolean[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof float[]) {
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		} else if (newArray instanceof short[]) {
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (short)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof String[]) {
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		} else {
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(short[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len) {
		if (newArray instanceof byte[]) {
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (byte)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof long[]) {
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof double[]) {
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof char[]) {
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = (char)originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof int[]) {
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof boolean[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof float[]) {
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		} else if (newArray instanceof short[]) {
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		} else if (newArray instanceof String[]) {
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		} else {
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(String[]originArray, int originarrayCopyOffIndex, java.lang.Object newArray, int off, int len) {
		if (newArray instanceof byte[]) {
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Byte.parseByte(originArray[i + originarrayCopyOffIndex]);
		} else if (newArray instanceof long[]) {
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Long.parseLong(originArray[i + originarrayCopyOffIndex]);
		} else if (newArray instanceof double[]) {
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Double.parseDouble(originArray[i + originarrayCopyOffIndex]);
		} else if (newArray instanceof char[]) {
			throw new ClassCastException(String.format("%s not can copy to %s", originArray.getClass().getName(), newArray.getClass().getName()));
		} else if (newArray instanceof int[]) {
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Integer.parseInt(originArray[i + originarrayCopyOffIndex]);
		} else if (newArray instanceof boolean[]) {
			boolean[] array = (boolean[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = "true".equals(originArray[i + originarrayCopyOffIndex]);
		} else if (newArray instanceof float[]) {
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Float.parseFloat(originArray[i + originarrayCopyOffIndex]);
		} else if (newArray instanceof short[]) {
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = Short.parseShort(originArray[i + originarrayCopyOffIndex]);
		} else if (newArray instanceof String[]) {
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		} else {
			Object[] array = (Object[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = originArray[i + originarrayCopyOffIndex];
		}
	}
	public static void arraycopyTraverse(Object[]originArray, int originarrayCopyOffIndex, Object newArray, int off, int len) {
		if (newArray instanceof byte[]) {
			byte[] array = (byte[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Byte)originArray[i + originarrayCopyOffIndex]).byteValue();
		} else if (newArray instanceof long[]) {
			long[] array = (long[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Long)originArray[i + originarrayCopyOffIndex]).longValue();
		} else if (newArray instanceof double[]) {
			double[] array = (double[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Double)originArray[i + originarrayCopyOffIndex]).doubleValue();
		} else if (newArray instanceof char[]) {
			char[] array = (char[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Character)originArray[i + originarrayCopyOffIndex]).charValue();
		} else if (newArray instanceof int[]) {
			int[] array = (int[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Integer)originArray[i + originarrayCopyOffIndex]).intValue();
		} else if (newArray instanceof boolean[]) {
			boolean[] array = (boolean[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Boolean)originArray[i + originarrayCopyOffIndex]).booleanValue();
		} else if (newArray instanceof float[]) {
			float[] array = (float[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Float)originArray[i + originarrayCopyOffIndex]).floatValue();
		} else if (newArray instanceof short[]) {
			short[] array = (short[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = ((Short)originArray[i + originarrayCopyOffIndex]).shortValue();
		} else if (newArray instanceof String[]) {
			String[] array = (String[])newArray;
			for (int i = 0;i < len;i++)array[i + off] = String.valueOf(originArray[i + originarrayCopyOffIndex]);
		} else {
			System.arraycopy(originArray, originarrayCopyOffIndex, newArray, off, len);
		}
	}
	public static Object arraycopyTraverse(Object originalArray, int originarrayCopyOffIndex, Object newArray, int off, int len) {
		if (originalArray instanceof byte[]) arraycopyTraverse((byte[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof long[]) arraycopyTraverse((long[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof double[]) arraycopyTraverse((double[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof char[]) arraycopyTraverse((char[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof int[]) arraycopyTraverse((int[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof boolean[]) arraycopyTraverse((boolean[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof float[]) arraycopyTraverse((float[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof short[]) arraycopyTraverse((short[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else if (originalArray instanceof String[]) arraycopyTraverse((String[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		else arraycopyTraverse((Object[])originalArray, originarrayCopyOffIndex, newArray, off, len);
		return newArray;
	}
}
