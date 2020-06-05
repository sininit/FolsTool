package top.fols.box.lang;

import top.fols.box.lang.abstracts.XAbstractSequence;
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

/*
 * Rapid low demand development
 * 效率会稍低 开发更简单
 */
public class XSequences {
	/*
	 * all array type:
	 * 
	 * Object[] byte[] long[] double[] char[] int[] boolean[] float[] short[]
	 * 
	 * void[] unrealistic
	 */
	public static XAbstractSequence<byte[], Byte> wrapArr(byte[] Array) {
		return new XByteSequenceImpl(Array);
	}

	public static XAbstractSequence<long[], Long> wrapArr(long[] Array) {
		return new XLongSequenceImpl(Array);
	}

	public static XAbstractSequence<double[], Double> wrapArr(double[] Array) {
		return new XDoubleSequenceImpl(Array);
	}

	public static XAbstractSequence<char[], Character> wrapArr(char[] Array) {
		return new XCharSequenceImpl(Array);
	}

	public static XAbstractSequence<int[], Integer> wrapArr(int[] Array) {
		return new XIntSequenceImpl(Array);
	}

	public static XAbstractSequence<boolean[], Boolean> wrapArr(boolean[] Array) {
		return new XBooleanSequenceImpl(Array);
	}

	public static XAbstractSequence<float[], Float> wrapArr(float[] Array) {
		return new XFloatSequenceImpl(Array);
	}

	public static XAbstractSequence<short[], Short> wrapArr(short[] Array) {
		return new XShortSequenceImpl(Array);
	}

	public static XAbstractSequence<String[], Object> wrapArr(String[] Array) {
		return new XStringSequenceImpl(Array);
	}

	public static XAbstractSequence<Object[], Object> wrapArr(Object[] Array) {
		return new XObjectSequenceImpl(Array);
	}

	public static <A> XAbstractSequence<A, ?> wrap(A Array) {
		return wrap(Array, null);
	}

	@SuppressWarnings("unchecked")
	public static <A, E> XAbstractSequence<A, E> wrap(A Array, E ArrayElementType) {
		Class<?> arrCls = Array.getClass();
		if (null == arrCls || !arrCls.isArray()) {
			throw new ClassCastException("cannot convert " + arrCls.getCanonicalName() + " to array");
		}
		if (Array instanceof byte[]) {
			return (XAbstractSequence<A, E>) wrapArr((byte[]) Array);
		}
		if (Array instanceof long[]) {
			return (XAbstractSequence<A, E>) wrapArr((long[]) Array);
		}
		if (Array instanceof double[]) {
			return (XAbstractSequence<A, E>) wrapArr((double[]) Array);
		}
		if (Array instanceof char[]) {
			return (XAbstractSequence<A, E>) wrapArr((char[]) Array);
		}
		if (Array instanceof int[]) {
			return (XAbstractSequence<A, E>) wrapArr((int[]) Array);
		}
		if (Array instanceof boolean[]) {
			return (XAbstractSequence<A, E>) wrapArr((boolean[]) Array);
		}
		if (Array instanceof float[]) {
			return (XAbstractSequence<A, E>) wrapArr((float[]) Array);
		}
		if (Array instanceof short[]) {
			return (XAbstractSequence<A, E>) wrapArr((short[]) Array);
		}

		if (Array instanceof String[]) {
			return (XAbstractSequence<A, E>) wrapArr((String[]) Array);
		}
		return (XAbstractSequence<A, E>) wrapArr((Object[]) Array);
	}

	public static boolean equalsRange(XAbstractSequence originalArray, int originalArrayOff,
			XAbstractSequence objectArray, int objectArrayOff, int len) {
		if (originalArray == objectArray && originalArrayOff == objectArrayOff
				&& originalArrayOff + len <= originalArray.length()) {
			return true;
		}
		if (originalArrayOff + len > originalArray.length()) {
			return false;
		}
		if (objectArrayOff + len > objectArray.length()) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if (!originalArray.equals(i + originalArrayOff, objectArray.get(i + objectArrayOff))) {
				return false;
			}
		}
		return true;
	}

	public static boolean equals(XAbstractSequence originalArray, XAbstractSequence objectArray) {
		if (originalArray.length() != objectArray.length()) {
			return false;
		}
		return equalsRange(originalArray, 0, objectArray, 0, originalArray.length());
	}

	public static boolean deepEqualsRange(XAbstractSequence originalArray, int originalArrayOff,
			XAbstractSequence objectArray, int objectArrayOff, int len) {
		if (originalArray == objectArray && originalArrayOff == objectArrayOff
				&& originalArrayOff + len <= originalArray.length()) {
			return true;
		}
		if (originalArrayOff + len > originalArray.length()) {
			return false;
		}
		if (objectArrayOff + len > objectArray.length()) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if (originalArray.isArray(i + originalArrayOff)) {
				if (!objectArray.isArray(i + objectArrayOff)) {
					return false;
				}
				Object o = originalArray.get(i + originalArrayOff);
				Object o1 = objectArray.get(i + objectArrayOff);
				if (!deepEquals0(o, o1)) {
					return false;
				}
			} else {
				if (!originalArray.equals(i + originalArrayOff, objectArray.get(i + objectArrayOff))) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean deepEquals(XAbstractSequence originalArray, XAbstractSequence objectArray) {
		if (originalArray.length() != objectArray.length()) {
			return false;
		}
		return deepEqualsRange(originalArray, 0, objectArray, 0, originalArray.length());
	}

	private static boolean deepEquals0(Object originalArray, Object objectArray) {
		XAbstractSequence tmp = wrap(originalArray);
		XAbstractSequence tmp1 = wrap(objectArray);
		boolean b = deepEquals(tmp, tmp1);
		tmp = null;
		tmp1 = null;
		return b;
	}

	public static int indexOf(XAbstractSequence originalArray, Object value, int index) {
		if (index < 0) {
			index = 0;
		}
		int length = originalArray.length();
		for (int i = index; i < length; i++) {
			if (originalArray.equals(i, value)) {
				return i;
			}
		}
		return -1;
	}

	public static int lastIndexOf(XAbstractSequence originalArray, Object value, int index) {
		for (int i = index; i > -1; i--) {
			if (originalArray.equals(i, value)) {
				return i;
			}
		}
		return -1;
	}

	public static int indexOf(XAbstractSequence originalArray, XAbstractSequence value, int start, int indexRange) {
		if (null == originalArray || originalArray.length() == 0 || start > indexRange || null == value
				|| value.length() > originalArray.length() || value.length() == 0
				|| indexRange - start < value.length()) {
			return -1;
		}
		if (indexRange > originalArray.length()) {
			indexRange = originalArray.length();
		}
		int i, i2;
		for (i = start; i < indexRange; i++) {
			if (originalArray.equals(i, value.get(0))) {
				if (indexRange - i < value.length()) {
					break;
				}
				for (i2 = 1; i2 < value.length(); i2++) {
					if (!originalArray.equals(i + i2, value.get(i2))) {
						break;
					}
				}
				if (i2 == value.length()) {
					return i;
				}
			}
		}
		return -1;
	}

	public static int lastIndexOf(XAbstractSequence originalArray, XAbstractSequence value, int startIndex,
			int indexRange) {
		if (null == originalArray || originalArray.length() == 0 || indexRange > startIndex || null == value
				|| value.length() > originalArray.length() || value.length() == 0
				|| startIndex - indexRange < value.length()) {
			return -1;
		}
		if (startIndex > originalArray.length()) {
			startIndex = originalArray.length();
		}
		int i, i2;
		for (i = startIndex == originalArray.length() ? originalArray.length() - 1 : startIndex; i >= indexRange; i--) {
			if (originalArray.equals(i, value.get(0))) {
				if (i + value.length() > startIndex) {
					continue;
				}
				for (i2 = 1; i2 < value.length(); i2++) {
					if (!originalArray.equals(i + i2, value.get(i2))) {
						break;
					}
				}
				if (i2 == value.length()) {
					return i;
				}
			}
		}
		return -1;
	}

	public static int deepIndexOf(XAbstractSequence originalArray, Object value, int index) {
		if (index < 0) {
			index = 0;
		}
		int length = originalArray.length();
		boolean valueIsArray = null == value ? false : value.getClass().isArray();
		for (int i = index; i < length; i++) {
			if (originalArray.isArray(i)) {
				if (valueIsArray) {
					if (deepEquals0(originalArray.get(i), value)) {
						return i;
					}
				}
			} else if (originalArray.equals(i, value)) {
				return i;
			}
		}
		return -1;
	}

	public static int deepLastIndexOf(XAbstractSequence originalArray, Object value, int index) {
		boolean valueIsArray = null == value ? false : value.getClass().isArray();
		for (int i = index; i > -1; i--) {
			if (originalArray.isArray(i)) {
				if (valueIsArray) {
					if (deepEquals0(originalArray.get(i), value)) {
						return i;
					}
				}
			} else if (originalArray.equals(i, value)) {
				return i;
			}
		}
		return -1;
	}

	public static int deepIndexOf(XAbstractSequence originalArray, XAbstractSequence value, int start,
			int indexRange) {
		if (null == originalArray || originalArray.length() == 0 || start > indexRange || null == value
				|| value.length() > originalArray.length() || value.length() == 0
				|| indexRange - start < value.length()) {
			return -1;
		}
		if (indexRange > originalArray.length()) {
			indexRange = originalArray.length();
		}
		int i;
		for (i = start; i < indexRange; i++) {
			if (indexRange - i < value.length()) {
				break;
			}
			if (deepEqualsRange(originalArray, i, value, 0, value.length())) {
				return i;
			}
		}
		return -1;
	}

	public static int deepLastIndexOf(XAbstractSequence originalArray, XAbstractSequence value, int startIndex,
			int indexRange) {
		if (null == originalArray || originalArray.length() == 0 || indexRange > startIndex || null == value
				|| value.length() > originalArray.length() || value.length() == 0
				|| startIndex - indexRange < value.length()) {
			return -1;
		}
		if (startIndex > originalArray.length()) {
			startIndex = originalArray.length();
		}
		int i;
		for (i = startIndex == originalArray.length() ? originalArray.length() - 1 : startIndex; i >= indexRange; i--) {
			if (i + value.length() > startIndex) {
				continue;
			}
			if (deepEqualsRange(originalArray, i, value, 0, value.length())) {
				return i;
			}
		}
		return -1;
	}

	public static boolean startsWith(XAbstractSequence originalArray, XAbstractSequence s) {
		return equalsRange(originalArray, 0, s, 0, s.length());
	}

	public static boolean endWith(XAbstractSequence originalArray, XAbstractSequence s) {
		return equalsRange(originalArray, originalArray.length() - s.length(), s, 0, s.length());
	}

	public static boolean deepStartsWith(XAbstractSequence originalArray, XAbstractSequence s) {
		return deepEqualsRange(originalArray, 0, s, 0, s.length());
	}

	public static boolean deepEndWith(XAbstractSequence originalArray, XAbstractSequence s) {
		return deepEqualsRange(originalArray, originalArray.length() - s.length(), s, 0, s.length());
	}

}
