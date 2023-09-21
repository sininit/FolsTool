package top.fols.atri.lang;

import java.lang.reflect.Array;
import java.util.*;

import top.fols.atri.interfaces.interfaces.ICaller;
import top.fols.atri.interfaces.interfaces.IFilter;
import top.fols.box.array.ArrayObject;
import top.fols.box.lang.Arrayx;

import static java.lang.reflect.Array.newInstance;

@SuppressWarnings({"unused", "SpellCheckingInspection", "unchecked", "rawtypes", "UnnecessaryLocalVariable", "SuspiciousSystemArraycopy"})
public class Arrayz {
	/**
	 * The maximum size of array to allocate. Some VMs reserve some header words in
	 * an array. Attempts to allocate larger arrays may result in OutOfMemoryError:
	 * Requested array size exceeds VM limit
	 */
	public static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	/*
	 * all array type:
	 *
	 * Object[] byte[] long[] double[] char[] int[] boolean[] float[] short[]
	 *
	 * void[] unrealistic
	 */


	public static char[] arraycopy(String src, int srcPos, char[] dest, int destPos, int length) {
		src.getChars(srcPos, srcPos + length, dest, destPos);
		return dest;
	}
	public static Object arraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
		return dest;
	}


	public static int dimension(Object a) {
		if (null == a) {
			return 0;
		} else {
			Class type = a.getClass();
			if (type.isArray()) {
				int space = 0;
				while (null != (type = type.getComponentType())) {
					space++;
				}
				return space;
			} else {
				return 0;
			}
		}
	}
	public static int dimension(Class type) {
		if (null == type) {
			return 0;
		} else {
			if (type.isArray()) {
				int space = 0;
				while (null != (type = type.getComponentType())) {
					space++;
				}
				return space;
			} else {
				return 0;
			}
		}
	}

	public static Class dimensionClass(Class<?> type, int dimension) {
		if (null == type) { return null; }
		return Array.newInstance(type, new int[dimension]).getClass();
	}



	public static Class elevationDimensionClass(Class type) {
		if (null == type) { return null; }
		return Array.newInstance(type, 0).getClass();
	}
	public static Class reductionDimensionClass(Class type) {
		if (null == type) { return null; }
		return type.getComponentType();
	}

	public static Class getRootComponentType(Class type) {
		if (null == type) {
			return null;
		} else {
			Class ret = null;
			if (type.isArray()) {
				while (null != (type = type.getComponentType())) {
					ret = type;
				}
				return ret;
			} else {
				return null;
			}
		}
	}



	public static boolean isArray(Object object) {
		return null != object && object.getClass().isArray();
	}
	public static boolean isPrimitiveArray(Object object) {
		if (null == object) { return false; }
		Class<?> 		 componentClass = object.getClass().getComponentType();
		return   null != componentClass && componentClass.isPrimitive();
	}
	public static boolean isObjectArray(Object object) {
		if (null == object) { return false; }
		Class<?> 		 componentClass = object.getClass().getComponentType();
		return   null != componentClass && !componentClass.isPrimitive();
	}


	public static <T> T clear(T src) {
		if (null == src) { return null; }
		Class aClass = src.getClass();
		if (aClass.isArray()) {
			if (src instanceof byte[]) {
				Arrays.fill((byte[])   src, (byte)0);
				return src;
			} else if (src instanceof short[]) {
				Arrays.fill((short[])   src, (short)0);
				return src;
			} else if (src instanceof int[]) {
				Arrays.fill((int[])   src, 0);
				return src;
			} else if (src instanceof long[]) {
				Arrays.fill((long[])   src, 0);
				return src;
			} else if (src instanceof char[]) {
				Arrays.fill((char[])   src, (char)0);
				return src;
			} else if (src instanceof float[]) {
				Arrays.fill((float[])  src, (float)0);
				return src;
			} else if (src instanceof double[]) {
				Arrays.fill((double[])  src, 0);
				return src;
			} else if (src instanceof boolean[]) {
				Arrays.fill((boolean[]) src, false);
				return src;
			} else {
				Arrays.fill((Object[])  src, null);
				return src;
			}
		} else {
			return src;
		}
	}

	public static <T> T copyOf(T src) {
		if (null == src) { return null; }
		Class aClass = src.getClass();
		if (aClass.isArray()) {
			if (src instanceof byte[]) {
				return (T) ((byte[])   src).clone();
			} else if (src instanceof short[]) {
				return (T) ((short[])  src).clone();
			} else if (src instanceof int[]) {
				return (T) ((int[])    src).clone();
			} else if (src instanceof long[]) {
				return (T) ((long[])   src).clone();
			} else if (src instanceof char[]) {
				return (T) ((char[])   src).clone();
			} else if (src instanceof float[]) {
				return (T) ((float[])  src).clone();
			} else if (src instanceof double[]) {
				return (T) ((double[]) src).clone();
			} else if (src instanceof boolean[]) {
				return (T) ((boolean[])src).clone();
			} else {
				Object[] a =  (Object[]) src;
				Object[] na = (Object[]) Array.newInstance(aClass.getComponentType(), a.length);
				for (int i = 0; i < a.length; i++) {
					na[i] = copyOf(a[i]);
				}
				return (T) na;
			}
		} else {
			return src;
		}
	}

	public static String toString(Object a) {
		if (a == null)
			return "null";
		Class aClass = a.getClass();
		if (aClass.isArray()) {
			if (aClass == byte[].class) {
				return (Arrays.toString((byte[]) a));
			} else if (aClass == short[].class) {
				return (Arrays.toString((short[]) a));
			} else if (aClass == int[].class) {
				return (Arrays.toString((int[]) a));
			} else if (aClass == long[].class) {
				return (Arrays.toString((long[]) a));
			} else if (aClass == char[].class) {
				return (Arrays.toString((char[]) a));
			} else if (aClass == float[].class) {
				return (Arrays.toString((float[]) a));
			} else if (aClass == double[].class) {
				return (Arrays.toString((double[]) a));
			} else if (aClass == boolean[].class) {
				return (Arrays.toString((boolean[]) a));
			} else { // a is an array of object references
				return Arrays.deepToString((Object[])a);
			}
		} else {  // a is non-null and not an array
			return String.valueOf(a);
		}
	}

	public static int hashCode(Object src) {
		if (null == src)
			return 0;
		if (src.getClass().isArray()) {
			if (src instanceof byte[]) {
				return Arrays.hashCode((byte[])   src);
			} else if (src instanceof short[]) {
				return Arrays.hashCode((short[])  src);
			} else if (src instanceof int[]) {
				return Arrays.hashCode((int[])    src);
			} else if (src instanceof long[]) {
				return Arrays.hashCode((long[])   src);
			} else if (src instanceof char[]) {
				return Arrays.hashCode((char[])   src);
			} else if (src instanceof float[]) {
				return Arrays.hashCode((float[])  src);
			} else if (src instanceof double[]) {
				return Arrays.hashCode((double[]) src);
			} else if (src instanceof boolean[]) {
				return Arrays.hashCode((boolean[])src);
			} else {
				return Arrays.deepHashCode((Object[]) src);
			}
		} else {
			return src.hashCode();
		}
	}


	@SuppressWarnings("unchecked")
	public static <C> C convert(Object array, C convertArrayType) {
		return (C) convert(array, 0,  Array.getLength(array), convertArrayType.getClass());
	}
	public static <C> C convert(Object array, Class<C> convertArrayType) {
		return convert(array, 0,  Array.getLength(array), convertArrayType);
	}
	public static <C> C convert(Object array, int offset, int len, Class<C> convertArrayType) {
		if (null == array) return null;

		ArrayObject oriArr = ArrayObject.wrap(array);
		ArrayObject newArr = ArrayObject.wrap(Array.newInstance(convertArrayType.getComponentType(), len));

		oriArr.copy(offset, newArr, 0, newArr.length());

		return (C) newArr.innerArray();
	}

	public static Object convertElement(Object array, Class<?> elementType) {
		return convertElement(array, 0,  Array.getLength(array), elementType);
	}
	public static Object convertElement(Object array, int offset, int len, Class<?> elementType) {
		if (null == array) return null;

		ArrayObject oriArr = ArrayObject.wrap(array);
		ArrayObject newArr = ArrayObject.wrap(Array.newInstance(elementType, len));

		oriArr.copy(offset, newArr, 0, newArr.length());

		return newArr.innerArray();
	}

















	public static void leftMove(long[] data, int position, int limit,
								int leftMove) {
		if (leftMove >= (limit - position)) {
			for (int i = position; i < limit; i++) {
				data[i] = 0;
			}
		} else {
			int ind = limit - leftMove;
			if (ind - position > 0) {
				System.arraycopy(data, limit - ind + position, data, position, ind - position);
			}
			for (int i = ind, ed = ind + leftMove; i < ed; i++) {
				data[i] = 0;
			}
		}
	}
	public static void rightMove(long[] data, int position, int limit,
								 int rightMove) {
		int len = limit - position;
		if (rightMove >= len) {
			for (int i = position; i < limit; i++) {
				data[i] = 0;
			}
		} else {
			if (len - rightMove > 0) {
				System.arraycopy(data, position, data, position + rightMove, len - rightMove);
			}
			for (int i = position, ed = position + rightMove; i < ed; i++) {
				data[i] = 0;
			}
		}
	}






	public static <R> Iterator<R> keys(R... array) {
		return Arrays.asList(array).iterator();
	}




	public static <T> T[] filter(T[] origin, IFilter<T> fp) {
		if (null == origin)
			return null;
		return filter(origin, 0, origin.length, fp);
	}
	public static <T> T[] filter(T[] origin, int off, int len, IFilter<T> filter) {
		int length = Array.getLength(origin);
		Object[] buff = new Object[length];
		int      buffCount  = 0;
		for (int i = 0; i < len; i++) {
			T element = origin[off + i];
			if (filter.next(element)) {
				buff[buffCount++] = element;
			}
		}
		T[] arr = (T[]) Array.newInstance(origin.getClass().getComponentType(), buffCount);
		System.arraycopy(buff, 0, arr, 0, arr.length);
		return arr;
	}

	public static <O, T> T[] cast(O[] origin, T[] to, ICaller<O, T> filter) {
		return cast(origin, 0, origin.length,
				to, filter);
	}
	public static <O, T> T[] cast(O[] origin, int off, int len, T[] to, ICaller<O, T> filter) {
		T[] arr = (T[]) Array.newInstance(to.getClass().getComponentType(), len);
		for (int i = 0; i < len; i++)
			arr[i] = filter.next(origin[off + i]);
		return arr;
	}
	public static <TO, FROM> Object cast(FROM[] src, int srcPos, TO[] dest, int destPos, int length, ICaller<FROM, TO> cast) {
		if (length <= 0) {
			return dest;
		}
		for (int i = 0; i < length; i++) {
			dest[i + destPos] = cast.next(src[i + srcPos]);
		}
		return dest;
	}


	public static <T> void sort(T[] cover, Comparator<T> c) {
		sort(cover, 0, cover.length, c);
	}

	public static <T> void sort(T[] cover, int off, int ed, Comparator<T> c) {
		int i, j;
		for (i = off; i < ed - 1; i++) {
			for (j = off; j < ed - 1 - i + off; j++) {
				if (c.compare(cover[j], cover[j + 1]) > 0) {
					T temp = cover[j];
					cover[j] = cover[j + 1];
					cover[j + 1] = temp;
				}
			}
		}
	}

	/**
	 * 合并数组
	 */
	public static <T> T[] merge(T[][] arrays) {
		return null == arrays ? null : merge(arrays, 0, arrays.length);
	}

	public static <T> T[] merge(T[][] arrays, int off, int len) {
		if (null == arrays) {
			return null;
		}
		int length = 0;
		for (int i = 0; i < len; i++) {
			T[] array = arrays[off + i];
			length += (null == array ? 0 : array.length);
		}
		int index = 0;
		T[] newArray = (T[]) newInstance(Arrayx.getElementClass(Arrayx.getElementClass(arrays)), length);
		for (int i = 0; i < len; i++) {
			T[] array = arrays[off + i];
			System.arraycopy(array, 0, newArray, index, array.length);
			index += array.length;
		}
		return newArray;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <L> L[] marge(L[] left, L[] right) {
		if (null == left) {
			if (null == right) {
				return null;
			} else {
				return java.util.Arrays.copyOf(right, right.length);
			}
		} else {
			if (null == right) {
				return java.util.Arrays.copyOf(left, left.length);
			} else {
				Class componentType = left.getClass().getComponentType();
				int length = left.length + right.length;
				L[] newInstance = (L[]) java.lang.reflect.Array.newInstance(componentType, length);
				System.arraycopy(left, 0, newInstance, 0, left.length);
				System.arraycopy(right, 0, newInstance, left.length, right.length);
				return newInstance;
			}
		}
	}


}
