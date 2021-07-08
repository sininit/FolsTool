package top.fols.box.util;


import java.util.*;

import top.fols.atri.array.ArrayObject;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Objects;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.lang.XClass;

import static top.fols.atri.lang.Arrayz.*;

public class XArray {


	/*
	 * equals Array type 判断数组类型是否相同
	 */
	public static boolean equalsArrayTypeAbs(Object originalArray, Object objectArray) {
		if (!(isArray(originalArray) && isArray((objectArray)))) {
			return false;
		} else {
			return getElementClass(originalArray) == getElementClass(originalArray);
		}
	}

	public static boolean equalsArrayType(Object originalArray, Object objectArray) {
		if (originalArray == objectArray) {
			return true;
		}
		if (null == originalArray || null == objectArray) {
			return false;
		} else if (originalArray instanceof Object[]) {
			return objectArray instanceof Object[];
		} else if (originalArray instanceof byte[]) {
			return objectArray instanceof byte[];
		} else if (originalArray instanceof long[]) {
			return objectArray instanceof long[];
		} else if (originalArray instanceof char[]) {
			return objectArray instanceof char[];
		} else if (originalArray instanceof int[]) {
			return objectArray instanceof int[];
		} else if (originalArray instanceof short[]) {
			return objectArray instanceof short[];
		} else if (originalArray instanceof boolean[]) {
			return objectArray instanceof boolean[];
		} else if (originalArray instanceof double[]) {
			return objectArray instanceof double[];
		} else if (originalArray instanceof float[]) {
			return objectArray instanceof float[];
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static <A extends Object> A add(A originalArray, int index, Object Element) {
		return (A) add(originalArray, index, Element, getElementClass(originalArray));
	}

	@SuppressWarnings("unchecked")
	public static <A extends Object> A addAllTraverse(A originalArray, int index, Object Array) {
		return (A) addAllTraverse(originalArray, index, Array, getElementClass(originalArray));
	}

	@SuppressWarnings("unchecked")
	public static <A extends Object> A addAll(A originalArray, int index, Object Array) {
		return (A) addAll(originalArray, index, Array, getElementClass(originalArray));
	}

	@SuppressWarnings("unchecked")
	public static <A extends Object> A subArray(A originalArray, int start, int end) {
		return (A) subArray(originalArray, start, end, getElementClass(originalArray));
	}

	@SuppressWarnings("unchecked")
	public static <A extends Object> A remove(A originalArray, int start, int end) {
		return (A) remove(originalArray, start, end, getElementClass(originalArray));
	}

	/*
	 * Add All Traversal 遍历添加元素
	 * 
	 * long[] data = new long[]{71, 69, 84, 32, 47, 32, 72, 84, 84}; //{71, 69, 84,
	 * 32, 47, 32, 72, 84, 84} //0 1 2 3 4 5 6 7 8 9 AddAllUsageTraversal(data,0,new
	 * int[]{1,2,3},long.class); >> {1,2,3, 71, 69, 84, 32, 47, 32, 72, 84, 84}
	 * AddAllUsageTraversal(data,1,new long[]{1,2,3},long.class); >> {71, 1,2,3, 69,
	 * 84, 32, 47, 32, 72, 84, 84}
	 */
	public static Object addAllTraverse(Object originalArray, int index, Object Array,
			Class originalArrayElementClass) {
		int originalArrayLength = null == originalArray ? 0 : getLength(originalArray);
		if (index < 0 || index > originalArrayLength) {
			index = originalArrayLength;
		}
		int addElementLength = null == Array ? 0 : getLength(Array);
		Object object = insert(originalArray, index, addElementLength, originalArrayElementClass);
		if (addElementLength != 0) {
			XArrays.arraycopyTraverse(Array, 0, object, index, addElementLength);
		}
		return object;
	}

	/**
	 * Add All 元素类型相同的数组
	 * 
	 * int[] data = new int[]{71, 69, 84, 32, 47, 32, 72, 84, 84}; //{71, 69, 84,
	 * 32, 47, 32, 72, 84, 84} //0 1 2 3 4 5 6 7 8 9 addAll(data,0,new
	 * int[]{1,2,3},int.class); >> {1,2,3, 71, 69, 84, 32, 47, 32, 72, 84, 84}
	 * addAll(data,1,new int[]{1,2,3},int.class); >> {71, 1,2,3, 69, 84, 32, 47, 32,
	 * 72, 84, 84} addAll(data,6,new int[]{1,2,3},int.class); >> {71, 69, 84, 32,
	 * 47, 32, 1,2,3, 72, 84, 84}
	 **/
	public static Object addAll(Object originalArray, int index, Object Array, Class originalArrayElementClass) {
		int originalArrayLength = null == originalArray ? 0 : getLength(originalArray);
		if (index < 0 || index > originalArrayLength) {
			index = originalArrayLength;
		}
		int addElementLength = null == Array ? 0 : getLength(Array);
		Object object = insert(originalArray, index, addElementLength, originalArrayElementClass);
		if (addElementLength != 0) {
			System.arraycopy(Array, 0, object, index, addElementLength);
		}
		return object;
	}

	/**
	 * Insert data in the array
	 *
	 * @param oldArray array
	 * @param offset insert index, >= 0 or >= array.length
	 * @param len insert len
	 * @param oldArrayEleClass originalArrayElementClass
	 */
	public static Object insert(Object oldArray, int offset, int len,  Class oldArrayEleClass) throws ArrayIndexOutOfBoundsException {
		if (null == oldArrayEleClass) { throw new NullPointerException("attempt to read to null class type"); }
		int oldArrayLength = getLength(oldArray);
		if (offset < 0) { throw new ArrayIndexOutOfBoundsException(String.format("offset=%s, len=%s", offset, len)); }
		if (offset > oldArrayLength) { len += (offset - oldArrayLength); offset = oldArrayLength; }

		Object object = newInstance(oldArrayEleClass, oldArrayLength + len);
		if (offset > 0) { System.arraycopy(oldArray, 0, object, 0, offset);}
		if (offset < oldArrayLength) {	System.arraycopy(oldArray, offset, object, offset + len, oldArrayLength - offset); }
		return object;
	}

	/**
	 * Element Will be added to originalArray Element serve Object ElementClass Must
	 * serve Element.class 将Element添加到originalArray
	 * 
	 * int[] data = new int[]{71, 69, 84, 32, 47, 32, 72, 84, 84}; //{71, 69, 84,
	 * 32, 47, 32, 72, 84, 84} //0 1 2 3 4 5 6 7 8 9
	 * <p>
	 * add(data,0,8888,int.class); >> {8888, 71, 69, 84, 32, 47, 32, 72, 84, 84}
	 * <p>
	 * add(data,1,9999,int.class); >> {71, 9999, 69, 84, 32, 47, 32, 72, 84, 84}
	 * <p>
	 * add(data,6,2333,int.class); >> {71, 69, 84, 32, 47, 32, 2333, 72, 84, 84}
	 **/
	public static Object add(Object originalArray, int index, Object Element, Class originalArrayElementClass) {
		if (null == originalArray) {
			throw new NullPointerException("attempt to read from null array");
		}
		int originalArrayLength = getLength(originalArray);
		if (index < 0 || index > originalArrayLength) {
			index = originalArrayLength;
		}
		int addElementLength = 1;
		Object object = insert(originalArray, index, addElementLength, originalArrayElementClass);
		set(object, index, Element);
		return object;
	}

	/**
	 * Sub Array element 取出数组指定位置数据
	 * 
	 * byte[] databyte = new byte[]{71, 69, 84, 32, 47, 32, 72, 84, 84}; //{71, 69,
	 * 84, 32, 47, 32, 72, 84, 84} //0 1 2 3 4 5 6 7 8 9
	 * subArray(databyte,0,8,byte.class); >> {71, 69, 84, 32, 47, 32, 72, 84}
	 * subArray(databyte,1,8,byte.class); >> {69, 84, 32, 47, 32, 72, 84}
	 * subArray(databyte,6,7,byte.class); >> {72}
	 **/
	public static Object subArray(Object originalArray, int start, int end, Class originalArrayElementClass) {
		if (null == originalArray) {
			throw new NullPointerException("attempt to read from null array");
		}
		if (start > end) {
			throw new ArrayIndexOutOfBoundsException(
					"index=" + start + ", end=" + end + ", size=" + getLength(originalArray));
		}
		int newlength = end - start;
		Object object = newInstance(originalArrayElementClass, newlength);
		if (newlength != 0) {
			System.arraycopy(originalArray, start, object, 0, newlength);
		}
		return object;
	}

	/**
	 * remove Array element 删除数组项目
	 * 
	 * remove(new int[]{1,2,3,4,5,6,7,8,9}, 1, 3, int.class); >> {1,4,5,6,7,8,9}
	 * remove(new int[]{1,2,3,4,5,6,7,8,9}, 4, 6, int.class); >> {1,2,3,4,7,8,9}
	 **/
	public static Object remove(Object originalArray, int start, int end, Class originalArrayElementClass) {
		if (null == originalArray) {
			throw new NullPointerException("attempt to read from null array");
		}
		int originalArrayLength = getLength(originalArray);
		if (start > end) {
			throw new ArrayIndexOutOfBoundsException(
					"index=" + start + ", end=" + end + ", size=" + originalArrayLength);
		}
		int newlength = originalArrayLength - (end - start);
		if (newlength == originalArrayLength) {
			return originalArray;
		}
		Object object = newInstance(originalArrayElementClass, newlength);
		if (start != 0) {
			System.arraycopy(originalArray, 0, object, 0, start);
		}
		if (((newlength) - start) != 0) {
			System.arraycopy(originalArray, end, object, start, (newlength) - start);
		}
		return object;
	}

	/**
	 * is Array 判断对象是否为数组
	 * 
	 * isArray(1); >> false isArray(new int[]{1}); >> true isArray(new
	 * String[]{"str"}); >> true
	 **/
	public static boolean isArray(Object originalArray) {
		if (null == originalArray) {
			return false;
		} else if (originalArray.getClass().isArray()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Determines whether the array is Object[] 判断数组是否为Object[]
	 */
	public static boolean isObjectArray(Object originalArray) {
		return originalArray instanceof Object[];
	}

	/**
	 * get Array Dimensional 获取数组纬度
	 * 
	 * getDimensional(int[0]); >> 1 getDimensional(int[0][0]); >> 2
	 **/
	public static int getDimensional(Object Object) {
		return Arrayz.space(Object);
	}

	public static int getDimensionalFromClassCanonicalName(String name) {
		return XClass.getArrayDimensionalFromClassCanonicalName(name);
	}

	/**
	 * array to String
	 * 
	 **/
	public static String toString(Object originalArray) {
		return toString(false, originalArray);
	}

	public static String toString(boolean addCanonicalName, Object originalArray) {
		if (null == originalArray) {
			return null;
		}
		if (!originalArray.getClass().isArray()) {
			throw new ClassCastException(String.format("%s not can cast to array", originalArray.getClass().getName()));
		}
		ArrayObject as = ArrayObject.wrap(originalArray);
		StringBuilder result = new StringBuilder(toString0(addCanonicalName,
				XClass.toAbsCanonicalName(originalArray.getClass()), ArrayObject.wrap(originalArray)));
		if (XArrays.CharSequenceUtil.endWith(result, ",")) {
			result.setLength(result.length() - 1);
		}
		return result.toString();
	}
	private static StringBuilder toString0(boolean addCanonicalName, String canonicalName,
										   ArrayObject originalArray) {
		StringBuilder result = new StringBuilder();
		if (addCanonicalName) {
			result.append(canonicalName);
		}
		result.append('{');
		for (int i = 0; i < originalArray.length(); i++) {
			Object object = originalArray.objectValue(i);
			if (ArrayObject.wrapable(object)) {
				result.append(
						toString0(addCanonicalName, XClass.toAbsCanonicalName(object.getClass()), ArrayObject.wrap(object)));
			} else {
				result.append(object);
				result.append(',');
				continue;
			}
		}
		if (XArrays.CharSequenceUtil.endWith(result, ",")) {
			result.setLength(result.length() - 1);
		}
		result.append('}');
		result.append(',');
		return result;
	}





	/*
	 * get Array Element Class 获取数组元素类
	 * 
	 * getElementClass(new int[0]); >> int.class getElementClass(new int[0][0]); >>
	 * int[].class
	 */
	public static Class getElementClass(Object originalArray) {
		return null == originalArray ? null : originalArray.getClass().getComponentType();
	}

	public static Class getElementClass(Class originalArrayCls) {
		return null == originalArrayCls ? null : originalArrayCls.getComponentType();
	}

	/*
	 * new array val for fill value 创建一个数组 val为填充值
	 */
	public static Object newInstanceFill(Class originalArrayElementClass, int length, Object fiiVal) {
		if (length == 0) {
			return newInstance(originalArrayElementClass, 0);
		}
		Object array = newInstance(originalArrayElementClass, 1);
		set(array, 0, fiiVal);
		if (length == 1) {
			return array;
		}
		Object newArray = repeat(array, length);
		return newArray;
	}

	/*
	 * fast repeat copy array 快速重复复制数组
	 * 
	 * fastRepeat(new int[]{1},int.class,1); >> {1}
	 * fastRepeat("1".toCharArray(),char.class,3); >> {'1','1','1'}
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Object> A repeat(A originalArray, int repeatCount) {
		return (A) repeat(originalArray, getElementClass(originalArray), repeatCount);
	}

	public static Object repeat(Object originalArray, Class originalArrayElementClass, int repeatCount) {
		int secureLength;
		if (null == originalArray) {
			throw new NullPointerException("attempt to read from null array");
		} else if ((secureLength = getLength(originalArray)) == 0) {
			throw new NullPointerException("array length min can't for 0");
		} else if (repeatCount < 1) {
			throw new NumberFormatException("array repeat Count length min can't for 0");
		}
		int nowCumulative = 1;
		int newCumulative;
		Object newArray = originalArray;
		while (true) {
			if (nowCumulative >= repeatCount) {
				break;
			}
			newCumulative = nowCumulative * 2;
			if (newCumulative > repeatCount) {
				Object newArray2 = newInstance(originalArrayElementClass, repeatCount * secureLength);
				int newArraylength = getLength(newArray);
				int newArray2length = getLength(newArray2);
				System.arraycopy(newArray, 0, newArray2, 0, newArraylength);
				System.arraycopy(newArray, 0, newArray2, newArraylength, newArray2length - newArraylength);

				newArray = null;
				return newArray2;
			}
			Object tmp = repeatMultiply0(newArray, originalArrayElementClass);
			newArray = null;
			nowCumulative = newCumulative;
			newArray = tmp;
		}
		originalArray = null;
		return newArray;
	}

	private static Object repeatMultiply0(Object originalArray, Class originalArrayElementClass) {
		int arrayLength = getLength(originalArray);
		Object newArray = newInstance(originalArrayElementClass, arrayLength * 2);
		System.arraycopy(originalArray, 0, newArray, 0, arrayLength);
		System.arraycopy(originalArray, 0, newArray, arrayLength, arrayLength);
		originalArray = null;
		return newArray;
	}

	/*
	 * copyOf array 复制数组
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Object> A copyOf(A originalArray, int length) {
		Class cls = getElementClass(originalArray);
		if (null == cls) {
			throw new ClassCastException(String.format("%s not can cast to array", originalArray.getClass().getName()));
		}
		Object array = newInstance(cls, length);
		if (length != 0) {
			int originalArrayLength = getLength(originalArray);
			System.arraycopy(originalArray, 0, array, 0, length < originalArrayLength ? length : originalArrayLength);
		}
		return (A) array;
	}

	@SuppressWarnings("unchecked")
	public static <A extends Object> A copyOfRange(A originalArray, int from, int to) {
		Class cls = getElementClass(originalArray);
		if (null == cls) {
			throw new ClassCastException(String.format("%s not can cast to array", originalArray.getClass().getName()));
		}
		A newArray = (A) subArray(originalArray, from, to, cls);
		return newArray;
	}

	/*
	 * 将数组转换成别的类型 copyOfConversion(new int[]{},new long[]{}); 将int数组转换为long[]数组
	 * copyOfConversion(new int[]{},long.class);
	 */
	public static <A extends Object> A copyOfConversion(Object originalArray,
			@XAnnotations("new Class[0]") A newArrayNullObj) {
		return copyOfConversion(originalArray, getLength(originalArray), newArrayNullObj);
	}

	@SuppressWarnings("unchecked")
	public static <A extends Object> A copyOfConversion(Object originalArray, int length,
			@XAnnotations("new Class[0]") A newArrayNullObj) {
		return (A) copyOfConversion(originalArray, length, getElementClass(newArrayNullObj));
	}

	public static Object copyOfConversion(Object originalArray, Class<? extends Object> newElementType) {
		return copyOfConversion(originalArray, getLength(originalArray), newElementType);
	}

	public static Object copyOfConversion(Object originalArray, int length, Class<? extends Object> newElementType) {
		return copyOfConversion(originalArray, 0, length, newElementType);
	}

	public static Object copyOfConversion(Object originalArray, int off, int length,
			Class<? extends Object> newElementType) {
		Object newArray = newInstance(newElementType, length);
		int oArrLen = getLength(originalArray);
		if (length != 0) {
			XArrays.arraycopyTraverse(originalArray, off, newArray, 0, Math.min(length, oArrLen));
		}
		return newArray;
	}



	public static <T> void sort(T[] cover, Comparator<T> c) {
		XArray.sort(cover, 0, cover.length, c);
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
	public static <T> T[] filter(T[] origin, Objects.Accept<Boolean, T> fp) {
		return XArray.filter(origin, 0, origin.length, fp);
	}
	public static <T> T[] filter(T[] origin, int off, int len, Objects.Accept<Boolean, T> fp) {
		List<T> newList = new ArrayList<T>();
		for (int i = 0; i < len; i++) {
			T content = origin[off + i];
			if (fp.callback(content)) {
				newList.add(content);
			}
		}
		Object array = newInstance(XArray.getElementClass(origin), newList.size());
		T[] newArray = newList.toArray((T[]) array);
		newList = null;
		return newArray;
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
		Class elementClass = XArray.getElementClass(XArray.getElementClass(arrays));
		T[] newArray = (T[]) newInstance(elementClass, length);
		for (int i = 0; i < len; i++) {
			T[] array = arrays[off + i];
			System.arraycopy(array, 0, newArray, index, array.length);
			index += array.length;
		}
		return newArray;
	}

	/**
	 * 合并去重数组
	 */
	public static <T> T[] mergeDeduplication(T[][] arrays) {
		return null == arrays ? null : mergeDeduplication(arrays, 0, arrays.length);
	}
	public static <T> T[] mergeDeduplication(T[][] arrays, int off, int len) {
		if (null == arrays) {
			return null;
		}

		Map<T, Object> map = new LinkedHashMap<>();
		for (int i = 0; i < len; i++) {
			T[] arraysElement = arrays[off + i];
			if (null == arraysElement) {
				continue;
			}
			for (T aee : arraysElement) {
				map.put(aee, null);
			}
		}
		Class elementClass = XArray.getElementClass(XArray.getElementClass(arrays));
		T[] newArray = map.keySet().toArray((T[]) newInstance(elementClass, map.size()));

		return newArray;
	}





	/**
	 * 去除重复元素
	 */
	public static <T> T[] deduplication(T[] array) {
		return null == array ? null : deduplication(array, 0, array.length);
	}
	public static <T> T[] deduplication(T[] array, int off, int len) {
		if (null == array) {
			return null;
		}
		Map<T, Object> map = new LinkedHashMap<>();
		for (int i = 0; i < len; i++) {
			T element = array[off + i];
			map.put(element, null);
		}
		T[] newArray = map.keySet().toArray((T[]) newInstance(XArray.getElementClass(array), map.size()));
		map = null;
		return newArray;
	}



}
