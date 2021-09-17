package top.fols.atri.lang;

import java.lang.reflect.Array;
import java.util.*;

import top.fols.atri.array.ArrayObject;

@SuppressWarnings({"unused", "SpellCheckingInspection", "unchecked", "StatementWithEmptyBody", "rawtypes", "UnnecessaryLocalVariable", "SuspiciousSystemArraycopy"})
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
	public static void set(Object originalArray, int index, Object value)
			throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		Array.set(originalArray, index, value);
	}

	public static Object get(Object originalArray, int index)
			throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		return Array.get(originalArray, index);
	}

	public static int getLength(Object originalArray) {
		return Array.getLength(originalArray);
	}

	public static Object newInstance(java.lang.Class<?> componentType, int length) {
		return Array.newInstance(componentType, length);
	}

	public static Object newInstance(java.lang.Class<?> componentType, int... dimensions)
			throws java.lang.IllegalArgumentException, java.lang.NegativeArraySizeException {
		return Array.newInstance(componentType, dimensions);
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



	public static <T> T[] filter(T[] array, top.fols.atri.lang.Objects.Invoke<Boolean, T> filter) {
		if (null == array) {
			return null;
		}
		int length = Array.getLength(array);
		Class<?> component = array.getClass().getComponentType();
		if (null == filter) {
			T newArray = (T) Array.newInstance(component, length);
			System.arraycopy(array, 0, newArray, 0, length);
			return array;
		} else {
			Object[] newArray = new Object[length];
			int newArraySize  = 0;
			for (T element: array) {
				Boolean filte = filter.invoke(element);
				if (null != filte && filte) {
					newArray[newArraySize++] = element;
				}
			}
			T[] arr = (T[]) Array.newInstance(component, newArraySize);
			System.arraycopy(newArray, 0, arr, 0, arr.length);
			return arr;
		}
	}

	
	
	
	@SuppressWarnings("unchecked")
	public static <T, C> C convert(T array, C convertArrayType) {
		return (C) convert(array, null == convertArrayType ?null: convertArrayType.getClass());
	}
	public static <T, C> C convert(T array, Class<C> convertArrayType) {
		if (null == array) {
			return null;
		}
		if (null == convertArrayType) {
			return null;
		}
		ArrayObject oriArr = ArrayObject.wrap(array);

		Class<?> componentType = convertArrayType.getComponentType();
		ArrayObject newArr = ArrayObject.wrap(Array.newInstance(componentType, Array.getLength(array)));

		oriArr.copy(0, newArr, 0, oriArr.length());

		C inner = (C) newArr.innerArray();

		return inner;
	}

















	/*
	 * try get next, If invoke return null then end
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static abstract class Traverse<T> {
		public abstract Value<T> invoke(Value<T> next);
	}



	/**
	 * traverse all elements, whether it encounters null or not
	 */
	@SuppressWarnings({"unchecked", "ForLoopReplaceableByWhile"})
	public static abstract class TraverseArray<RETURN, ELEMENT> extends Traverse<RETURN> implements Next<RETURN, ELEMENT> {
		Object[] array;
		public TraverseArray(ELEMENT[] array) {
			this.array = null == array ?Finals.EMPTY_OBJECT_ARRAY: array;
		}
		int i = 0;



		public Value<RETURN> end()    { this.i = array.length; return null; }
		public int  index()  { return this.i; }
		public int  index(int newIndex) { return this.i = newIndex;}
		public int  length() { return this.array.length; }

		Value<RETURN> next_result = new Value<>();



		@Override
		public final Value<RETURN> invoke(Value<RETURN> next) {
			for (;i < array.length;) {
				Value<RETURN> n = next(this.next_result, (ELEMENT) array[i++]);
				if (null != n) {
					return  n;
				}
			}
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static abstract class TraverseList<RETURN, ELEMENT> extends Traverse<RETURN> implements Next<RETURN, ELEMENT> {
		List<ELEMENT> array;
		public TraverseList(List<ELEMENT> array) {
			this.array = null == array ?Finals.EMPTY_LIST: array;
		}
		int i = 0;


		public Value<RETURN> end()    { this.i = array.size(); return null; }
		public int  index()  { return this.i; }
		public int  index(int newIndex) { this.i = newIndex; return i;}
		public int  length() { return this.array.size(); }

		Value<RETURN> next_result = new Value<>();



		@Override
		public final Value<RETURN> invoke(Value<RETURN> next) {
			for (int size = array.size(); i < size;) {
				Value<RETURN> n = next(this.next_result, array.get(i++));
				if (null != n) {
					return  n;
				}
			}
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static abstract class TraverseCollection<RETURN, ELEMENT> extends Traverse<RETURN> implements Next<RETURN, ELEMENT> {
		Collection<ELEMENT> array;
		Iterator<ELEMENT>   array_iterator;
		public TraverseCollection(Collection<ELEMENT> array) {
			this.array = null == array ?Finals.EMPTY_LIST: array;
			this.array_iterator = this.array.iterator();
		}

		static final Iterator EMPTY = new Iterator<Object>() {
			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public Object next() {
				return null;
			}
		};


		public Value<RETURN> end()    { this.array_iterator = EMPTY; return null; }
		public int  length() { return this.array.size(); }

		Value<RETURN> next_result = new Value<>();

		@Override
		public final Value<RETURN> invoke(Value<RETURN> next) {
			while  (array_iterator.hasNext()) {
				Value<RETURN> n = next(this.next_result, array_iterator.next());
				if (null != n) {
					return  n;
				}
			}
			return null;
		}
	}





	/**
	 * @param executor If executor returns null, it means the creation process is over
	 */
	public static <RETURN> RETURN[] create(RETURN[] array, Traverse<RETURN> executor) {
		Objects.requireNonNull(array, "array type");
		List<RETURN> list = new ArrayList<>();
		create(list, executor);
		return  list.toArray(array);
	}
	public static <RETURN> Collection<RETURN> create(Collection<RETURN> buffer, Traverse<RETURN> executor) {
		Objects.requireNonNull(buffer, "null buffer");
		if (null == executor) {
		} else {
			Value<RETURN> result = new Value<>();
			while (null != (result = executor.invoke(result))) {
				buffer.add(result.get());
			}
		}
		return buffer;
	}










	public interface Next<RETURN, ELEMENT> {
		Value<RETURN> next(Value<RETURN> next, ELEMENT array_element);
	}


//	public static <RETURN, ELEMENT> RETURN[] filter(Class<RETURN[]> type, Next<RETURN, ELEMENT> executor,
//													ELEMENT[] filter) {
//		return filter(type, executor, filter, 0, null == filter ?0: filter.length);
//	}
//	public static <RETURN, ELEMENT> RETURN[] filter(Class<RETURN[]> type, Next<RETURN, ELEMENT> executor,
//													ELEMENT[] filter, int filter_offset, int filter_count) {
//		return filter((RETURN[]) newInstance(Objects.requireNonNull(type.getComponentType(), "array type"), 0), executor, filter, filter_offset, filter_count);
//	}

	public static <RETURN, ELEMENT> RETURN[] filter(RETURN[] buffer, Next<RETURN, ELEMENT> executor,
													ELEMENT[] filter) {
		return filter(buffer, executor, filter, 0, null == filter ?0: filter.length);
	}
	public static <RETURN, ELEMENT> RETURN[] filter(RETURN[] buffer, Next<RETURN, ELEMENT> executor,
													ELEMENT[] filter, int filter_offset, int filter_count) {
		Objects.requireNonNull(buffer, "buffer type");
		List<RETURN> list = new ArrayList<>();
		filter(list, executor, filter, filter_offset, filter_count);
		return  list.toArray(buffer);
	}

	public static <RETURN, ELEMENT> Collection<RETURN> filter(Collection<RETURN> buffer, Next<RETURN, ELEMENT> executor,
															  ELEMENT[] filter) {
		return filter(buffer, executor, filter, 0, null == filter ?0: filter.length);
	}
	public static <RETURN, ELEMENT> Collection<RETURN> filter(Collection<RETURN> buffer, Next<RETURN, ELEMENT> executor,
															  ELEMENT[] filter, int filter_offset, int filter_count) {
		Objects.requireNonNull(buffer, "null buffer");
		if (null == executor) {
		} else {
			if (null == filter) { return buffer; }
			Value<RETURN> result = new Value<>();
			for (int max = filter_offset + filter_count; filter_offset < max;) {
				Value<RETURN> n = executor.next(result, filter[filter_offset++]);
				if (null != n) {
					buffer.add(n.get());
				}
			}
		}
		return buffer;
	}



	public static <RETURN, ELEMENT> Collection<RETURN> filter(Collection<RETURN> buffer, Next<RETURN, ELEMENT> executor,
															  List<ELEMENT> filter) {
		return filter(buffer, executor, filter, 0, null == filter ?0: filter.size());
	}
	public static <RETURN, ELEMENT> Collection<RETURN> filter(Collection<RETURN> buffer, Next<RETURN, ELEMENT> executor,
															  List<ELEMENT> filter, int filter_offset, int filter_count) {
		Objects.requireNonNull(buffer, "null buffer");
		if (null == executor) {
		} else {
			if (null == filter) { return buffer; }
			Value<RETURN> result = new Value<>();
			for (int max = filter_offset + filter_count; filter_offset < max;) {
				Value<RETURN> n = executor.next(result, filter.get(filter_offset++));
				if (null != n) {
					buffer.add(n.get());
				}
			}
		}
		return buffer;
	}




	public static <RETURN, ELEMENT> Collection<RETURN> filter(Collection<RETURN> buffer, Next<RETURN, ELEMENT> executor,
															  Collection<ELEMENT> filter) {
		Objects.requireNonNull(buffer, "null buffer");
		if (null == executor) {
		} else {
			if (null == filter) { return buffer; }
			Iterator<ELEMENT> array_iterator = filter.iterator();
			Value<RETURN> result = new Value<>();
			while  (array_iterator.hasNext()) {
				Value<RETURN> n = executor.next(result, array_iterator.next());
				if (null != n) {
					buffer.add(n.get());
				}
			}
		}
		return buffer;
	}








	public static <R> Iterator<R> keys(R... array) {
		return Arrays.asList(array).iterator();
	}






	public static <A, B> Object cast(A[] src, int srcPos, B[] dest, int destPos, int length, Objects.Cast<A, B> cast) {
		if (length <= 0) {
			return dest;
		}
		for (int i = 0; i < length; i++) {
			dest[i + destPos] = cast.cast(src[i + srcPos]);
		}
		return dest;
	}
	public static char[] arraycopy(String src, int srcPos, char[] dest, int destPos, int length) {
		if (length <= 0) {
			return dest;
		}
		src.getChars(srcPos, srcPos + length, dest, destPos);
		return dest;
	}
	public static Object arraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
		if (length <= 0) {
			return dest;
		}
		System.arraycopy(src, srcPos, dest, destPos, length);
		return dest;
	}












	public static int space(Object a) {
		if (null == a) {
			return 0;
		} else {
			int space = 0;
			Class type = a.getClass();
			do {
				if (type.isArray()) { space++; }
			} while (null != (type = type.getComponentType()));
			return space;
		}
	}

	public boolean isArray(Object object) {
		return null != object && object.getClass().isArray();
	}
	public boolean isPrimitiveArray(Object object) {
		if (null == object) { return false; }
		Class<?> 		 componentClass = object.getClass().getComponentType();
		return   null != componentClass && componentClass.isPrimitive();
	}
	public boolean isObjectArray(Object object) {
		if (null == object) { return false; }
		Class<?> 		 componentClass = object.getClass().getComponentType();
		return   null != componentClass && componentClass.isPrimitive() == false;
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



}
