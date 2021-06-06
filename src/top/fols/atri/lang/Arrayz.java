package top.fols.atri.lang;

import java.lang.reflect.Array;
import java.util.*;

import top.fols.atri.array.ArrayObject;

@SuppressWarnings({"unused", "SpellCheckingInspection", "unchecked", "StatementWithEmptyBody", "rawtypes", "UnnecessaryLocalVariable", "SuspiciousSystemArraycopy"})
public class Arrayz {

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
		public abstract Value<T> invoke(
				Value<T> next
		);


		/**
		 * @param executor If executor returns null, it means the creation process is over
		 */
		public static <CAST> CAST[] creates(Class<CAST[]> type,   Traverse<CAST> executor) {
			return creates((CAST[]) newInstance(Objects.requireNonNull(type.getComponentType(), "array type"), 0), executor);
		}
		public static <CAST> CAST[] creates(CAST[] array, 		Traverse<CAST> executor) {
			Objects.requireNonNull(array, "array type");
			List<CAST> list = new ArrayList<>();
			creates(list, executor);
			return  list.toArray(array);
		}
		public static <CAST> Collection<CAST> creates(Collection<CAST> buffer, Traverse<CAST> executor) {
			Objects.requireNonNull(buffer, "null buffer");
			if (null == executor) {
			} else {
				Value<CAST> result = new Value<>();
				while (null != (result = executor.invoke(result))) {
					buffer.add(result.get());
				}
			}
			return buffer;
		}


	}



	/**
	 * traverse all elements, whether it encounters null or not
	 */
	@SuppressWarnings({"unchecked", "ForLoopReplaceableByWhile"})
	public static abstract class TraverseArray<CAST, ELEMENT> extends Traverse<CAST> implements Next<CAST, ELEMENT> {
		Object[] array;
		public TraverseArray(ELEMENT[] array) {
			this.array = null == array ?Finals.EMPTY_OBJECT_ARRAY: array;
		}
		int i = 0;



		public void end()    { this.i = array.length; }
		public int  index()  { return this.i; }
		public int  index(int newIndex) { this.i = newIndex; return i;}
		public int  length() { return this.array.length; }

		Value<CAST> next_result = new Value<>();



		@Override
		public final Value<CAST> invoke(Value<CAST> next) {
			for (;i < array.length;) {
				Value<CAST> n = next(this.next_result, (ELEMENT) array[i++]);
				if (null != n) {
					next.set(n.get());
					return next;
				}
			}
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static abstract class TraverseList<CAST, ELEMENT> extends Traverse<CAST> implements Next<CAST, ELEMENT> {
		List<ELEMENT> array;
		public TraverseList(List<ELEMENT> array) {
			this.array = null == array ?Finals.EMPTY_LIST: array;
		}
		int i = 0;


		public void end()    { this.i = array.size(); }
		public int  index()  { return this.i; }
		public int  index(int newIndex) { this.i = newIndex; return i;}
		public int  length() { return this.array.size(); }

		Value<CAST> next_result = new Value<>();



		@Override
		public final Value<CAST> invoke(Value<CAST> next) {
			for (int size = array.size(); i < size;) {
				Value<CAST> n = next(this.next_result, array.get(i++));
				if (null != n) {
					next.set(n.get());
					return next;
				}
			}
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static abstract class TraverseCollection<CAST, ELEMENT> extends Traverse<CAST> implements Next<CAST, ELEMENT> {
		Collection<ELEMENT> array;
		Iterator<ELEMENT>   array_iterator;
		public TraverseCollection(Collection<ELEMENT> array) {
			this.array = null == array ?Finals.EMPTY_LIST: array;
			this.array_iterator = this.array.iterator();
		}
		int i = 0;



		public void end()    { this.array_iterator = null; }
		public int  length() { return this.array.size(); }

		Value<CAST> next_result = new Value<>();

		@Override
		public final Value<CAST> invoke(Value<CAST> next) {
			while  (null != array_iterator && array_iterator.hasNext()) {
				Value<CAST> n = next(this.next_result, array_iterator.next());
				if (null != n) {
					next.set(n.get());
					return next;
				}
			}
			return null;
		}
	}









	public interface Next<CAST, ELEMENT> {
		Value<CAST> next(Value<CAST> next, ELEMENT array_element);
	}

	public static <CAST, ELEMENT> CAST[] create(Class<CAST[]> type,   Next<CAST, ELEMENT> executor,
												ELEMENT[] filter) {
		return create(type, executor, filter, 0, null == filter ?0: filter.length);
	}
	public static <CAST, ELEMENT> CAST[] create(Class<CAST[]> type,   Next<CAST, ELEMENT> executor,
												ELEMENT[] filter, int filter_offset, int filter_count) {
		return create((CAST[]) newInstance(Objects.requireNonNull(type.getComponentType(), "array type"), 0), executor, filter, filter_offset, filter_count);
	}

	public static <CAST, ELEMENT> CAST[] create(CAST[] buffer, Next<CAST, ELEMENT> executor,
												ELEMENT[] filter) {
		return create(buffer, executor, filter, 0, null == filter ?0: filter.length);
	}
	public static <CAST, ELEMENT> CAST[] create(CAST[] buffer, Next<CAST, ELEMENT> executor,
												ELEMENT[] filter, int filter_offset, int filter_count) {
		Objects.requireNonNull(buffer, "buffer type");
		List<CAST> list = new ArrayList<>();
		create(list, executor, filter, filter_offset, filter_count);
		return  list.toArray(buffer);
	}

	public static <CAST, ELEMENT> Collection<CAST> create(Collection<CAST> buffer, Next<CAST, ELEMENT> executor,
														  ELEMENT[] filter) {
		return create(buffer, executor, filter, 0, null == filter ?0: filter.length);
	}
	public static <CAST, ELEMENT> Collection<CAST> create(Collection<CAST> buffer, Next<CAST, ELEMENT> executor,
														  ELEMENT[] filter, int filter_offset, int filter_count) {
		Objects.requireNonNull(buffer, "null buffer");
		if (null == executor) {
		} else {
			if (null == filter) { return buffer; }
			Value<CAST> result = new Value<>();
			for (int max = filter_offset + filter_count; filter_offset < max;) {
				Value<CAST> n = executor.next(result, filter[filter_offset++]);
				if (null != n) {
					buffer.add(n.get());
				}
			}
		}
		return buffer;
	}



	public static <CAST, ELEMENT> Collection<CAST> create(Collection<CAST> buffer, Next<CAST, ELEMENT> executor,
														  List<ELEMENT> filter) {
		return create(buffer, executor, filter, 0, null == filter ?0: filter.size());
	}
	public static <CAST, ELEMENT> Collection<CAST> create(Collection<CAST> buffer, Next<CAST, ELEMENT> executor,
														  List<ELEMENT> filter, int filter_offset, int filter_count) {
		Objects.requireNonNull(buffer, "null buffer");
		if (null == executor) {
		} else {
			if (null == filter) { return buffer; }
			Value<CAST> result = new Value<>();
			for (int max = filter_offset + filter_count; filter_offset < max;) {
				Value<CAST> n = executor.next(result, filter.get(filter_offset++));
				if (null != n) {
					buffer.add(n.get());
				}
			}
		}
		return buffer;
	}




	public static <CAST, ELEMENT> Collection<CAST> create(Collection<CAST> buffer, Next<CAST, ELEMENT> executor,
														  Collection<ELEMENT> filter) {
		Objects.requireNonNull(buffer, "null buffer");
		if (null == executor) {
		} else {
			if (null == filter) { return buffer; }
			Iterator<ELEMENT> array_iterator = filter.iterator();
			Value<CAST> result = new Value<>();
			while  (array_iterator.hasNext()) {
				Value<CAST> n = executor.next(result, array_iterator.next());
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



	public static Object newInstance(Class<?> componentType, int length) {
		return Array.newInstance(componentType, length);
	}

	public static Object newInstance(Class<?> componentType, int... dimensions)
			throws java.lang.IllegalArgumentException, java.lang.NegativeArraySizeException {
		return Array.newInstance(componentType, dimensions);
	}










}
