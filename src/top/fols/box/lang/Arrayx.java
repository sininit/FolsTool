package top.fols.box.lang;


import java.lang.reflect.Array;
import java.util.*;

import top.fols.atri.lang.Finals;
import top.fols.box.array.ArrayObject;
import top.fols.atri.lang.Arrayz;

import static java.lang.reflect.Array.*;

@SuppressWarnings({"SpellCheckingInspection", "BooleanMethodIsAlwaysInverted", "SuspiciousSystemArraycopy", "rawtypes", "unchecked"})
public class Arrayx {


	/*
	 * equals Array type 判断数组类型是否相同
	 */
	public static boolean equalsArrayClass(Object originalArray, Object objectArray) {
		return isArray(originalArray) && isArray(objectArray) && originalArray.getClass() == objectArray.getClass();
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
	public static <A> A add(A originalArray, int index, Object Element) {
		return (A) add(originalArray, index, Element, getElementClass(originalArray));
	}

	@SuppressWarnings("unchecked")
	public static <A> A addAllTraverse(A originalArray, int index, Object Array) {
		return (A) addAllTraverse(originalArray, index, Array, getElementClass(originalArray));
	}

	@SuppressWarnings("unchecked")
	public static <A> A addAll(A originalArray, int index, Object Array) {
		return (A) addAll(originalArray, index, Array, getElementClass(originalArray));
	}

	@SuppressWarnings("unchecked")
	public static <A> A subArray(A originalArray, int start, int end) {
		return (A) subArray(originalArray, start, end, getElementClass(originalArray));
	}

	@SuppressWarnings("unchecked")
	public static <A> A remove(A originalArray, int start, int end) {
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
	public static Object addAllTraverse(Object originalArray, int index, Object array,
			Class originalArrayElementClass) {
		int originalArrayLength = null == originalArray ? 0 : getLength(originalArray);
		if (index < 0 || index > originalArrayLength) {
			index = originalArrayLength;
		}
		int addElementLength = null == array ? 0 : getLength(array);
		Object object = insert(originalArray, index, addElementLength, originalArrayElementClass);
		if (addElementLength != 0) {
			Arrayy.arraycopyTraverse(array, 0, object, index, addElementLength);
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
		return null != originalArray && originalArray.getClass().isArray();
	}

	/*
	 * Determines whether the array is Object[] 判断数组是否为Object[]
	 */
	public static boolean isObjectArray(Object originalArray) {
		return null != originalArray &&
				originalArray.getClass() == Finals.OBJECT_ARRAY_CLASS;
	}
	public static boolean instanceofObjectArray(Object originalArray) {
		return originalArray instanceof Object[];
	}

	/**
	 * get Array Dimensional 获取数组纬度
	 * 
	 * getDimensional(int[0]); >> 1 getDimensional(int[0][0]); >> 2
	 **/
	public static int getDimensional(Object Object) {
		return Arrayz.dimension(Object);
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
		Class<?> aClass = originalArray.getClass();
		if (!aClass.isArray()) {
			throw new ClassCastException(String.format("%s not can cast to array", aClass.getName()));
		}
		StringBuilder result = new StringBuilder(toString0(addCanonicalName,
				Classx.getClassGetNameToCanonicalName(aClass), ArrayObject.wrap(originalArray)));
		if (Arrayy.CharSequenceUtil.endWith(result, ",")) {
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
						toString0(addCanonicalName, Classx.getClassGetNameToCanonicalName(object.getClass()), ArrayObject.wrap(object)));
			} else {
				result.append(object);
				result.append(',');
			}
		}
		if (Arrayy.CharSequenceUtil.endWith(result, ",")) {
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
	 * new array val for fill tip 创建一个数组 val为填充值
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
		return repeat(array, length);
	}

	/*
	 * fast repeat copy array 快速重复复制数组
	 * 
	 * fastRepeat(new int[]{1},int.class,1); >> {1}
	 * fastRepeat("1".toCharArray(),char.class,3); >> {'1','1','1'}
	 */
	@SuppressWarnings("unchecked")
	public static <A> A repeat(A originalArray, int repeatCount) {
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
		while (nowCumulative < repeatCount) {
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
	@SuppressWarnings({"unchecked", "ManualMinMaxCalculation"})
	public static <A> A copyOf(A originalArray, int length) {
		Class<?> cls = getElementClass(originalArray);
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
	public static <A> A copyOfRange(A originalArray, int from, int to) {
		Class<?> cls = getElementClass(originalArray);
		if (null == cls) {
			throw new ClassCastException(String.format("%s not can cast to array", originalArray.getClass().getName()));
		}
		return (A) subArray(originalArray, from, to, cls);
	}



	public static <C> C convert(Object originalArray, C newArrayNullObj) {
		return (C) convert(originalArray, 0, getLength(originalArray), newArrayNullObj.getClass());
	}
	public static <C> C convert(Object originalArray, Class<C> convertArrayType) {
		return convert(originalArray, 0, getLength(originalArray), convertArrayType);
	}
	public static <C> C convert(Object originalArray, int off, int length, Class<C> convertArrayType) {
		Object newArray = newInstance(convertArrayType.getComponentType(), length);
		if (length != 0)
			Arrayy.arraycopyTraverse(originalArray, off, newArray, 0, length);
		return (C) newArray;
	}

	/*
	 * 将数组转换成别的类型 convertElement(new int[]{}, long.class);
	 */
	public static Object convertElement(Object originalArray, Class<?> elementType) {
		return convertElement(originalArray, 0, getLength(originalArray), elementType);
	}
	public static Object convertElement(Object originalArray, int off, int length, Class<?> elementType) {
		Object newArray = newInstance(elementType, length);
		if (length != 0)
			Arrayy.arraycopyTraverse(originalArray, off, newArray, 0, length);
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
		return map.keySet().toArray((T[]) newInstance(Arrayx.getElementClass(Arrayx.getElementClass(arrays)), 0));
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
		return map.keySet().toArray((T[]) newInstance(Arrayx.getElementClass(array), 0));
	}




	public static List toArrayEditor(Object object) {
		if (null == object) return null;

		Class objectClass = object.getClass();
		if   (objectClass.isArray()) {
			Class component = objectClass.getComponentType();
			if   (component.isPrimitive()) {
				if (component == int.class)   	return new PrimitiveIntArrayEditList((int[]) object);
				if (component == long.class) 	return new PrimitiveLongArrayEditList((long[]) object);
				if (component == byte.class) 	return new PrimitiveByteArrayEditList((byte[]) object);
				if (component == short.class) 	return new PrimitiveShortArrayEditList((short[]) object);
				if (component == char.class) 	return new PrimitiveCharArrayEditList((char[]) object);
				if (component == boolean.class) return new PrimitiveBooleanArrayEditList((boolean[]) object);
				if (component == float.class) 	return new PrimitiveFloatArrayEditList((float[]) object);
				if (component == double.class) 	return new PrimitiveDoubleArrayEditList((double[]) object);
			}
			return new ObjectArrayEditList((Object[]) object);
		}
		throw new UnsupportedOperationException("cannot from " + object.getClass().getName() + " convert to " + Collection.class.getName());
	}

	private static class PrimitiveIntArrayEditList<E extends Integer> extends AbstractList<E>
			implements RandomAccess, java.io.Serializable {
		private static final long serialVersionUID = -2764017481108945198L;
		private final int[] a;
		private int size;

		PrimitiveIntArrayEditList(int[] array) {
			a    = Objects.requireNonNull(array);
			size = array.length;
		}

		@Override public int size()          { return size; }
		@Override public Integer[] toArray() { return Arrayx.convert(a, 0, size, Integer[].class); }

		@Override public E get(int index) { return (E) ((Integer)a[index]);}
		@Override public boolean contains(Object o) { return indexOf(o) != -1; }

		@Override
		@SuppressWarnings("unchecked")
		public <T> T[] toArray(T[] a) {
			int size = size();
			if (a.length < size) {
				Class<? extends T[]> newArrayComponentType = (Class<? extends T[]>) a.getClass().getComponentType();
				T[] newArray = (T[]) Array.newInstance(newArrayComponentType, size);
				Arrayy.arraycopyTraverse(this.a, 0, newArray, 0, size);
				return newArray;
			}
			Arrayy.arraycopyTraverse(this.a, 0, a, 0, size);
			if (a.length > size)
				a[size] = null;
			return a;
		}

		@Override
		public E set(int index, E element) {
			E oldValue = (E) ((Integer)a[index]);
			a[index] = element;
			return oldValue;
		}

		@Override
		public int indexOf(Object o) {
			int[] a = this.a;
			if (o == null) {
				for (int i = 0; i < size; i++)
					if (a[i] == 0)
						return i;
			} else {
				for (int i = 0; i < size; i++)
					if (o.equals(a[i]))
						return i;
			}
			return -1;
		}
	}

	private static class PrimitiveLongArrayEditList<E extends Long> extends AbstractList<E>
			implements RandomAccess, java.io.Serializable {
		private static final long serialVersionUID = -2764017481108945198L;
		private final long[] a;

		PrimitiveLongArrayEditList(long[] array) {
			a = Objects.requireNonNull(array);
		}

		@Override
		public int size() {
			return a.length;
		}

		@Override
		public Long[] toArray() {
			return Arrayx.convert(a.clone(), new Long[]{});
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			int size = size();
			if (a.length < size) {
				Class<? extends T[]> newArrayComponentType = (Class<? extends T[]>) a.getClass().getComponentType();
				T[] newArray = (T[]) Array.newInstance(newArrayComponentType, size);
				Arrayy.arraycopyTraverse(this.a, 0, newArray, 0, size);
				return newArray;
			}
			Arrayy.arraycopyTraverse(this.a, 0, a, 0, size);
			if (a.length > size)
				a[size] = null;
			return a;
		}

		@Override
		public E get(int index) {
			return (E) ((Long) a[index]);
		}

		@Override
		public boolean contains(Object o) {
			return indexOf(o) != -1;
		}

		@Override
		public E set(int index, E element) {
			E oldValue = (E) ((Long) a[index]);
			a[index] = element;
			return oldValue;
		}

		@Override
		public int indexOf(Object o) {
			if (o == null) {
				for (int i = 0; i < a.length; i++)
					if (a[i] == 0)
						return i;
			} else if (o instanceof Long) {
				long value = (Long) o;
				for (int i = 0; i < a.length; i++)
					if (a[i] == value)
						return i;
			}
			return -1;
		}
	}


	private static class PrimitiveByteArrayEditList<E extends Byte> extends AbstractList<E>
			implements RandomAccess, java.io.Serializable {
		private static final long serialVersionUID = -2764017481108945198L;
		private final byte[] a;

		PrimitiveByteArrayEditList(byte[] array) { a = Objects.requireNonNull(array); }

		@Override public int size()          { return a.length; }
		@Override public Byte[] toArray()    { return Arrayx.convert(a.clone(), new Byte[]{}); }

		@Override public E get(int index) { return (E) ((Byte)a[index]);}
		@Override public boolean contains(Object o) { return indexOf(o) != -1; }

		@Override
		@SuppressWarnings("unchecked")
		public <T> T[] toArray(T[] a) {
			int size = size();
			if (a.length < size) {
				Class<? extends T[]> newArrayComponentType = (Class<? extends T[]>) a.getClass().getComponentType();
				T[] newArray = (T[]) Array.newInstance(newArrayComponentType, size);
				Arrayy.arraycopyTraverse(this.a, 0, newArray, 0, size);
				return newArray;
			}
			Arrayy.arraycopyTraverse(this.a, 0, a, 0, size);
			if (a.length > size)
				a[size] = null;
			return a;
		}

		@Override
		public E set(int index, E element) {
			E oldValue = (E) ((Byte)a[index]);
			a[index] = element;
			return oldValue;
		}

		@Override
		public int indexOf(Object o) {
			byte[] a = this.a;
			if (o == null) {
				for (int i = 0; i < a.length; i++)
					if (a[i] == 0)
						return i;
			} else {
				for (int i = 0; i < a.length; i++)
					if (o.equals(a[i]))
						return i;
			}
			return -1;
		}
	}

	private static class PrimitiveShortArrayEditList<E extends Short> extends AbstractList<E>
			implements RandomAccess, java.io.Serializable {
		private static final long serialVersionUID = -2764017481108945198L;
		private final short[] a;

		PrimitiveShortArrayEditList(short[] array) { a = Objects.requireNonNull(array); }

		@Override public int size()          { return a.length; }
		@Override public Short[] toArray()   { return Arrayx.convert(a.clone(), new Short[]{}); }

		@Override public E get(int index) { return (E) ((Short)a[index]);}
		@Override public boolean contains(Object o) { return indexOf(o) != -1; }

		@Override
		@SuppressWarnings("unchecked")
		public <T> T[] toArray(T[] a) {
			int size = size();
			if (a.length < size) {
				Class<? extends T[]> newArrayComponentType = (Class<? extends T[]>) a.getClass().getComponentType();
				T[] newArray = (T[]) Array.newInstance(newArrayComponentType, size);
				Arrayy.arraycopyTraverse(this.a, 0, newArray, 0, size);
				return newArray;
			}
			Arrayy.arraycopyTraverse(this.a, 0, a, 0, size);
			if (a.length > size)
				a[size] = null;
			return a;
		}

		@Override
		public E set(int index, E element) {
			E oldValue = (E) ((Short)a[index]);
			a[index] = element;
			return oldValue;
		}

		@Override
		public int indexOf(Object o) {
			short[] a = this.a;
			if (o == null) {
				for (int i = 0; i < a.length; i++)
					if (a[i] == 0)
						return i;
			} else {
				for (int i = 0; i < a.length; i++)
					if (o.equals(a[i]))
						return i;
			}
			return -1;
		}
	}

	private static class PrimitiveBooleanArrayEditList<E extends Boolean> extends AbstractList<E>
			implements RandomAccess, java.io.Serializable {
		private static final long serialVersionUID = 2985313098253296775L;
		private final boolean[] a;

		PrimitiveBooleanArrayEditList(boolean[] array) { a = Objects.requireNonNull(array); }

		@Override public int size() { return a.length; }
		@Override public Boolean[] toArray() { return Arrayx.convert(a.clone(), new Boolean[]{}); }

		@Override public E get(int index) { return (E) Boolean.valueOf(a[index]); }
		@Override public boolean contains(Object o) { return indexOf(o) != -1; }

		@Override
		@SuppressWarnings("unchecked")
		public <T> T[] toArray(T[] a) {
			int size = size();
			if (a.length < size) {
				Class<? extends T[]> newArrayComponentType = (Class<? extends T[]>) a.getClass().getComponentType();
				T[] newArray = (T[]) Array.newInstance(newArrayComponentType, size);
				Arrayy.arraycopyTraverse(this.a, 0, newArray, 0, size);
				return newArray;
			}
			Arrayy.arraycopyTraverse(this.a, 0, a, 0, size);
			if (a.length > size)
				a[size] = null;
			return a;
		}

		@Override
		public E set(int index, E element) {
			E oldValue = (E) Boolean.valueOf(a[index]);
			a[index] = element;
			return oldValue;
		}

		@Override
		public int indexOf(Object o) {
			if (o instanceof Boolean) {
				boolean value = (Boolean) o;
				for (int i = 0; i < a.length; i++)
					if (a[i] == value)
						return i;
			}
			return -1;
		}
	}

	private static class PrimitiveFloatArrayEditList<E extends Float> extends AbstractList<E>
			implements RandomAccess, java.io.Serializable {
		private static final long serialVersionUID = -2764017481108945198L;
		private final float[] a;

		PrimitiveFloatArrayEditList(float[] array) {
			a = Objects.requireNonNull(array);
		}

		@Override
		public int size() {
			return a.length;
		}

		@Override
		public Float[] toArray() {
			return Arrayx.convert(a.clone(), new Float[]{});
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			int size = size();
			if (a.length < size) {
				Class<? extends T[]> newArrayComponentType = (Class<? extends T[]>) a.getClass().getComponentType();
				T[] newArray = (T[]) Array.newInstance(newArrayComponentType, size);
				Arrayy.arraycopyTraverse(this.a, 0, newArray, 0, size);
				return newArray;
			}
			Arrayy.arraycopyTraverse(this.a, 0, a, 0, size);
			if (a.length > size)
				a[size] = null;
			return a;
		}

		@Override
		public E get(int index) {
			return (E) ((Float) a[index]);
		}

		@Override
		public boolean contains(Object o) {
			return indexOf(o) != -1;
		}

		@Override
		public E set(int index, E element) {
			E oldValue = (E) ((Float) a[index]);
			a[index] = element;
			return oldValue;
		}

		@Override
		public int indexOf(Object o) {
			if (o == null) {
				for (int i = 0; i < a.length; i++)
					if (a[i] == 0)
						return i;
			} else if (o instanceof Float) {
				float value = (Float) o;
				for (int i = 0; i < a.length; i++)
					if (Float.compare(a[i], value) == 0)
						return i;
			}
			return -1;
		}
	}

	private static class PrimitiveDoubleArrayEditList<E extends Double> extends AbstractList<E>
			implements RandomAccess, java.io.Serializable {
		private static final long serialVersionUID = -2764017481108945198L;
		private final double[] a;

		PrimitiveDoubleArrayEditList(double[] array) {
			a = Objects.requireNonNull(array);
		}

		@Override
		public int size() {
			return a.length;
		}

		@Override
		public Double[] toArray() {
			return Arrayx.convert(a.clone(), new Double[]{});
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			int size = size();
			if (a.length < size) {
				Class<? extends T[]> newArrayComponentType = (Class<? extends T[]>) a.getClass().getComponentType();
				T[] newArray = (T[]) Array.newInstance(newArrayComponentType, size);
				Arrayy.arraycopyTraverse(this.a, 0, newArray, 0, size);
				return newArray;
			}
			Arrayy.arraycopyTraverse(this.a, 0, a, 0, size);
			if (a.length > size)
				a[size] = null;
			return a;
		}

		@Override
		public E get(int index) {
			return (E) ((Double) a[index]);
		}

		@Override
		public boolean contains(Object o) {
			return indexOf(o) != -1;
		}

		@Override
		public E set(int index, E element) {
			E oldValue = (E) ((Double) a[index]);
			a[index] = element;
			return oldValue;
		}

		@Override
		public int indexOf(Object o) {
			if (o == null) {
				for (int i = 0; i < a.length; i++)
					if (a[i] == 0)
						return i;
			} else if (o instanceof Double) {
				double value = (Double) o;
				for (int i = 0; i < a.length; i++)
					if (Double.compare(a[i], value) == 0)
						return i;
			}
			return -1;
		}
	}

	private static class PrimitiveCharArrayEditList<E extends Character> extends AbstractList<E>
			implements RandomAccess, java.io.Serializable {
		private static final long serialVersionUID = -2764017481108945198L;
		private final char[] a;

		PrimitiveCharArrayEditList(char[] array) {
			a = Objects.requireNonNull(array);
		}

		@Override
		public int size() {
			return a.length;
		}

		@Override
		public Character[] toArray() {
			return Arrayx.convert(a.clone(), new Character[]{});
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			int size = size();
			if (a.length < size) {
				Class<? extends T[]> newArrayComponentType = (Class<? extends T[]>) a.getClass().getComponentType();
				T[] newArray = (T[]) Array.newInstance(newArrayComponentType, size);
				Arrayy.arraycopyTraverse(this.a, 0, newArray, 0, size);
				return newArray;
			}
			Arrayy.arraycopyTraverse(this.a, 0, a, 0, size);
			if (a.length > size)
				a[size] = null;
			return a;
		}

		@Override
		public E get(int index) {
			return (E) ((Character) a[index]);
		}

		@Override
		public boolean contains(Object o) {
			return indexOf(o) != -1;
		}

		@Override
		public E set(int index, E element) {
			E oldValue = (E) ((Character) a[index]);
			a[index] = element;
			return oldValue;
		}

		@Override
		public int indexOf(Object o) {
			if (o == null) {
				for (int i = 0; i < a.length; i++)
					if (a[i] == 0)
						return i;
			} else if (o instanceof Character) {
				char value = (Character) o;
				for (int i = 0; i < a.length; i++)
					if (a[i] == value)
						return i;
			}
			return -1;
		}
	}

	/**
	 * @serial include
	 */
	private static class ObjectArrayEditList<E> extends AbstractList<E>
			implements RandomAccess, java.io.Serializable {
		private static final long serialVersionUID = -2764017481108945198L;
		private final E[] a;

		ObjectArrayEditList(E[] array) { a = Objects.requireNonNull(array); }

		@Override public int size() { return a.length; }
		@Override public Object[] toArray() { return a.clone(); }

		@Override public E get(int index) { return a[index];}
		@Override public boolean contains(Object o) { return indexOf(o) != -1; }

		@Override
		@SuppressWarnings("unchecked")
		public <T> T[] toArray(T[] a) {
			int size = size();
			if (a.length < size)
				return Arrays.copyOf(this.a, size,
						(Class<? extends T[]>) a.getClass());
			System.arraycopy(this.a, 0, a, 0, size);
			if (a.length > size)
				a[size] = null;
			return a;
		}

		@Override
		public E set(int index, E element) {
			E oldValue = a[index];
			a[index] = element;
			return oldValue;
		}

		@Override
		public int indexOf(Object o) {
			E[] a = this.a;
			if (o == null) {
				for (int i = 0; i < a.length; i++)
					if (a[i] == null)
						return i;
			} else {
				for (int i = 0; i < a.length; i++)
					if (o.equals(a[i]))
						return i;
			}
			return -1;
		}
	}


}
