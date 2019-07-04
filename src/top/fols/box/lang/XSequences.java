package top.fols.box.lang;

import top.fols.box.annotation.XAnnotations;
import top.fols.box.lang.impl.sequences.XBooleanSequenceImpl;
import top.fols.box.lang.impl.sequences.XByteSequenceImpl;
import top.fols.box.lang.impl.sequences.XCharSequenceImpl;
import top.fols.box.lang.impl.sequences.XDoubleSequenceImpl;
import top.fols.box.lang.impl.sequences.XFloatSequenceImpl;
import top.fols.box.lang.impl.sequences.XIntSequenceImpl;
import top.fols.box.lang.impl.sequences.XLongSequenceImpl;
import top.fols.box.lang.impl.sequences.XObjectSequenceImpl;
import top.fols.box.lang.impl.sequences.XShortSequenceImpl;
import top.fols.box.lang.impl.sequences.XStringSequenceImpl;
import top.fols.box.lang.interfaces.XInterfacesSequence;
/*
 * Rapid low demand development
 * 效率会稍低 开发更简单
 */
public class XSequences {
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
	public static XInterfacesSequence<byte[],Byte> wrapArr(byte[] Array) {
		return new XByteSequenceImpl(Array);
	}
	public static XInterfacesSequence<long[],Long> wrapArr(long[] Array) {
		return new XLongSequenceImpl(Array);
	}
	public static XInterfacesSequence<double[],Double> wrapArr(double[] Array) {
		return new XDoubleSequenceImpl(Array);
	}
	public static XInterfacesSequence<char[],Character> wrapArr(char[] Array) {
		return new XCharSequenceImpl(Array);
	}
	public static XInterfacesSequence<int[],Integer> wrapArr(int[] Array) {
		return new XIntSequenceImpl(Array);
	}
	public static XInterfacesSequence<boolean[],Boolean> wrapArr(boolean[] Array) {
		return new XBooleanSequenceImpl(Array);
	}
	public static XInterfacesSequence<float[],Float> wrapArr(float[] Array) {
		return new XFloatSequenceImpl(Array);
	}
	public static XInterfacesSequence<short[],Short> wrapArr(short[] Array) {
		return new XShortSequenceImpl(Array);
	}

	public static XInterfacesSequence<String[],Object> wrapArr(String[] Array) {
		return new XStringSequenceImpl(Array);
	}
	public static XInterfacesSequence<Object[],Object> wrapArr(Object[] Array) {
		return new XObjectSequenceImpl(Array);
	}
	public static <A> XInterfacesSequence<A,?> wrap(A Array) {
		return wrap(Array, null);
	}
	public static <A,E> XInterfacesSequence<A,E> wrap(A Array, E ArrayElementType) {
		Class arrCls = Array.getClass();
		if (!arrCls.isArray())
			throw new ClassCastException("cannot convert " + arrCls.getCanonicalName() + " to array");
		if (Array instanceof byte[]) { return (XInterfacesSequence<A,E>) wrapArr((byte[])Array); }
		if (Array instanceof long[]) { return (XInterfacesSequence<A,E>) wrapArr((long[])Array); }
		if (Array instanceof double[]) { return (XInterfacesSequence<A,E>) wrapArr((double[])Array); }
		if (Array instanceof char[]) { return (XInterfacesSequence<A,E>) wrapArr((char[])Array); }
		if (Array instanceof int[]) { return (XInterfacesSequence<A,E>) wrapArr((int[])Array); }
		if (Array instanceof boolean[]) { return (XInterfacesSequence<A,E>) wrapArr((boolean[])Array); }
		if (Array instanceof float[]) { return (XInterfacesSequence<A,E>) wrapArr((float[])Array); }
		if (Array instanceof short[]) { return (XInterfacesSequence<A,E>) wrapArr((short[])Array); }

		if (Array instanceof String[]) { return (XInterfacesSequence<A,E>) wrapArr((String[])Array); }
		return (XInterfacesSequence<A,E>) wrapArr((Object[])Array);
	}
	
	
	
	public static boolean equalsRange(XInterfacesSequence originalArray, int originalArrayOff, XInterfacesSequence objectArray, int objectArrayOff, int len) {
		if (originalArray == objectArray &&
			originalArrayOff == objectArrayOff &&
			originalArrayOff + len <= originalArray.length()) 
			return true;
		if (originalArrayOff + len > originalArray.length()) return false;
		if (objectArrayOff + len > objectArray.length()) return false;
		for (int i = 0;i < len;i++) if (!originalArray.equals(i + originalArrayOff, objectArray.get(i + objectArrayOff))) return false;
		return true;
	}
	public static boolean equals(XInterfacesSequence originalArray, XInterfacesSequence objectArray) {
		if (originalArray.length() != objectArray.length()) return false;
		return equalsRange(originalArray, 0, objectArray, 0, originalArray.length());
	}


	public static boolean deepEqualsRange(XInterfacesSequence originalArray, int originalArrayOff, XInterfacesSequence objectArray, int objectArrayOff, int len) {
		if (originalArray == objectArray &&
			originalArrayOff == objectArrayOff &&
			originalArrayOff + len <= originalArray.length()) 
			return true;
		if (originalArrayOff + len > originalArray.length()) return false;
		if (objectArrayOff + len > objectArray.length()) return false;
		for (int i = 0;i < len; i++) {
			if (originalArray.isArray(i + originalArrayOff)) {
				if (!objectArray.isArray(i + objectArrayOff))
					return false;
				Object o = originalArray.get(i + originalArrayOff);
				Object o1 = objectArray.get(i + objectArrayOff);
				if (!deepEquals0(o, o1)) return false;
			} else {
				if (!originalArray.equals(i + originalArrayOff, objectArray.get(i + objectArrayOff)))
					return false;
			}
		}
		return true;
	}
	public static boolean deepEquals(XInterfacesSequence originalArray, XInterfacesSequence objectArray) {
		if (originalArray.length() != objectArray.length()) return false;
		return deepEqualsRange(originalArray, 0, objectArray, 0, originalArray.length());
	}
	private static boolean deepEquals0(Object originalArray, Object objectArray) {
		XInterfacesSequence tmp = wrap(originalArray);
		XInterfacesSequence tmp1 = wrap(objectArray);
		boolean b = deepEquals(tmp, tmp1);
		tmp = null; tmp1 = null;
		return b;
	}




	public static int indexOf(XInterfacesSequence originalArray, Object value, int index) {
		if (index < 0)
			index = 0;
		int length = originalArray.length();
		for (int i = index; i < length; i++) {
			if (originalArray.equals(i, value))
				return i;
		}
		return -1;
	}
	public static int lastIndexOf(XInterfacesSequence originalArray, Object value, int index) {
		for (int i = index; i > -1; i--) {
			if (originalArray.equals(i, value))
				return i;
		}
		return -1;
	}
	public static int indexOf(XInterfacesSequence originalArray, XInterfacesSequence value, int start, int indexRange) {
		if (null == originalArray || originalArray.length() == 0 || start > indexRange || null == value || value.length() > originalArray.length() || value.length() == 0 || indexRange - start < value.length())
			return -1;
		if (indexRange > originalArray.length())
			indexRange = originalArray.length();
		int i, i2;
		for (i = start; i < indexRange; i++) {
			if (originalArray.equals(i, value.get(0))) {
				if (indexRange - i < value.length())
					break;
				for (i2 = 1; i2 < value.length(); i2++)
					if (!originalArray.equals(i + i2, value.get(i2)))
						break;
				if (i2 == value.length())
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(XInterfacesSequence originalArray, XInterfacesSequence value , int startIndex, int indexRange) {
		if (null == originalArray || originalArray.length() == 0 || indexRange > startIndex || null == value || value.length() > originalArray.length() || value.length() == 0 || startIndex - indexRange < value.length())
			return -1;
		if (startIndex > originalArray.length())
			startIndex = originalArray.length();
		int i, i2;
		for (i = startIndex == originalArray.length() ?originalArray.length() - 1: startIndex; i >= indexRange; i--) {
			if (originalArray.equals(i, value.get(0))) {
				if (i + value.length() > startIndex)
					continue;
				for (i2 = 1; i2 < value.length(); i2++)
					if (!originalArray.equals(i + i2, value.get(i2)))
						break;
				if (i2 == value.length())
					return i;
			}
		}
		return -1;
	}




	public static int deepIndexOf(XInterfacesSequence originalArray, Object value, int index) {
		if (index < 0)
			index = 0;
		int length = originalArray.length();
		boolean valueIsArray = null == value ?false: value.getClass().isArray();
		for (int i = index; i < length; i++) {
			if (originalArray.isArray(i)) {
				if (valueIsArray) {
					if (deepEquals0(originalArray.get(i), value)) {
						return i;
					}
				}
			} else if (originalArray.equals(i, value))
				return i;
		}
		return -1;
	}
	public static int deepLastIndexOf(XInterfacesSequence originalArray, Object value, int index) {
		boolean valueIsArray = null == value ?false: value.getClass().isArray();
		for (int i = index; i > -1; i--) {
			if (originalArray.isArray(i)) {
				if (valueIsArray) {
					if (deepEquals0(originalArray.get(i), value)) {
						return i;
					}
				}
			} else if (originalArray.equals(i, value))
				return i;
		}
		return -1;
	}
	public static int deepIndexOf(XInterfacesSequence originalArray, XInterfacesSequence value, int start, int indexRange) {
		if (null == originalArray || originalArray.length() == 0 || start > indexRange || null == value || value.length() > originalArray.length() || value.length() == 0 || indexRange - start < value.length())
			return -1;
		if (indexRange > originalArray.length())
			indexRange = originalArray.length();
		int i;
		for (i = start; i < indexRange; i++) {
			if (indexRange - i < value.length())
				break;
			if (deepEqualsRange(originalArray, i, value, 0, value.length()))
				return i;
		}
		return -1;
	}
	public static int deepLastIndexOf(XInterfacesSequence originalArray, XInterfacesSequence value , int startIndex, int indexRange) {
		if (null == originalArray || originalArray.length() == 0 || indexRange > startIndex || null == value || value.length() > originalArray.length() || value.length() == 0 || startIndex - indexRange < value.length())
			return -1;
		if (startIndex > originalArray.length())
			startIndex = originalArray.length();
		int i;
		for (i = startIndex == originalArray.length() ?originalArray.length() - 1: startIndex; i >= indexRange; i--) {
			if (i + value.length() > startIndex)
				continue;
			if (deepEqualsRange(originalArray, i, value, 0, value.length()))
				return i;
		}
		return -1;
	}




	public static boolean startsWith(XInterfacesSequence originalArray, XInterfacesSequence s) { 
		return equalsRange(originalArray, 0, s, 0, s.length());
	}
	public static boolean endWith(XInterfacesSequence originalArray, XInterfacesSequence s) {
		return equalsRange(originalArray, originalArray.length() - s.length(), s, 0, s.length());
	}
	
	
	
	public static boolean deepStartsWith(XInterfacesSequence originalArray, XInterfacesSequence s) { 
		return deepEqualsRange(originalArray, 0, s, 0, s.length());
	}
	public static boolean deepEndWith(XInterfacesSequence originalArray, XInterfacesSequence s) {
		return deepEqualsRange(originalArray, originalArray.length() - s.length(), s, 0, s.length());
	}
}
