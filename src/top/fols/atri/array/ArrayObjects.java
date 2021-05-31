package top.fols.atri.array;



public class ArrayObjects {

	public static boolean equalsType(Object oldArray, Object writeArray) {
		return null == oldArray ?null == writeArray: null != writeArray && oldArray.getClass() == writeArray.getClass(); 
	}
	public static boolean equalsInnerArrayType(Object oldArray, Object writeArray) { 
		if (oldArray == writeArray) { return true;}
		if (!(ArrayObject.wrapable(oldArray) && ArrayObject.wrapable(writeArray))) { return false; }
		ArrayObject a = ArrayObject.wrap(oldArray);
		ArrayObject b = ArrayObject.wrap(writeArray);
		return 
			a.innerArray() == b.innerArray() ||
			a.innerArrayType() == b.innerArrayType();
	}






	/**
	 * Object[]
	 * 
	 * byte[] 
	 * long[] 
	 * double[] 
	 * char[] 
	 * int[] 
	 * boolean[] 
	 * float[] 
	 * short[]
	 */
	 
	public static <B> B arraycopy(Object oldArray, int oldArrayIndex, ArrayObject<B> writeArray, int writeIndex, int count) {
		return arraycopy(oldArray, oldArrayIndex, writeArray.innerArray(), writeIndex, count);
	}
	public static <B> B arraycopy(Object oldArray, int oldArrayIndex, B writeArray, int writeIndex, int count) {
		if (oldArray == writeArray && oldArrayIndex == writeIndex) { return writeArray;}
		if (!ArrayObject.wrapable(oldArray) || !ArrayObject.wrapable(writeArray)) {
			throw new ClassCastException(
				String.format("unresolved: %s or %s"
							  , null == oldArray ?null: oldArray.getClass().getName()
							  , null == writeArray ?null: writeArray.getClass().getName())
			);
		}
		ArrayObject a = ArrayObject.wrap(oldArray);
		ArrayObject<B> b = ArrayObject.wrap(writeArray);
		if (a.innerArray() == b.innerArray() && oldArrayIndex == writeIndex) { return writeArray;}
		if (oldArrayIndex < 0
			|| writeIndex < 0
			|| count < 0
			|| oldArrayIndex + count > a.length() 
			|| writeIndex + count > b.length()) {

			throw new ArrayIndexOutOfBoundsException(
				String.format("src.length=%s, srcPos=%s, dst.length=%s, dstPos=%s, length=%s"
							  , a.length()
							  , oldArrayIndex
							  , b.length()
							  , writeIndex
							  , count
							  )
			);
		}
		if (a.innerArrayType() == b.innerArrayType()) {
			System.arraycopy(a.array, a.innerArrayIndex(oldArrayIndex), b.array, b.innerArrayIndex(writeIndex), count);
		} else if (b.innerArrayType().isAssignableFrom(a.innerArrayType())) {
			// a instance b
			System.arraycopy(a.array, a.innerArrayIndex(oldArrayIndex), b.array, b.innerArrayIndex(writeIndex), count);
		} else {
			int oldOffset = oldArrayIndex;
			int writeOffset = writeIndex;
			for (int i = 0; i < count; i++) {
				b.set(oldOffset++, a, writeOffset++);
			}
		}
		return b.innerArray();
	}














	public static boolean equalsContent(Object oldArray, Object writeArray) {
		if (oldArray == writeArray) { return true;}
		if (!ArrayObject.wrapable(oldArray) || !ArrayObject.wrapable(writeArray)) {
			throw new ClassCastException(
				String.format("unresolved: %s or %s"
							  , null == oldArray ?null: oldArray.getClass().getName()
							  , null == writeArray ?null: writeArray.getClass().getName())
			);
		}
		ArrayObject a = ArrayObject.wrap(oldArray);
		ArrayObject b = ArrayObject.wrap(writeArray);
		if (a.innerArray() == b.innerArray()) { return true; }
		if (a.length() != b.length()) { return false; }

		int count = a.length();
		for (int index = 0; index < count; index++) {
			if (!a.equals(index, b, index)) {
				return false;
			}
		}
		return true;
	}
	public static boolean equalsContent(Object oldArray, int oldArrayIndex, Object writeArray, int writeIndex, int count) {
		if (oldArray == writeArray && oldArrayIndex == writeIndex) { return true;}
		if (!ArrayObject.wrapable(oldArray) || !ArrayObject.wrapable(writeArray)) {
			throw new ClassCastException(
				String.format("unresolved: %s or %s"
							  , null == oldArray ?null: oldArray.getClass().getName()
							  , null == writeArray ?null: writeArray.getClass().getName())
			);
		}
		ArrayObject a = ArrayObject.wrap(oldArray);
		ArrayObject b = ArrayObject.wrap(writeArray);
		if (a.innerArray() == b.innerArray()) { return true; }
		if (oldArrayIndex < 0
			|| writeIndex < 0
			|| count < 0
			|| oldArrayIndex + count > a.length() 
			|| writeIndex + count > b.length()) {

			throw new ArrayIndexOutOfBoundsException(
				String.format("src.length=%s, srcPos=%s, dst.length=%s, dstPos=%s, length=%s"
							  , a.length()
							  , oldArrayIndex
							  , b.length()
							  , writeIndex
							  , count
							  )
			);
		}
		int oldOffset = oldArrayIndex;
		int writeOffset = writeIndex;
		for (int index = 0; index < count; index++) {
			if (!a.equals(oldOffset++, b, writeOffset++)) {
				return false;
			}
		}
		return true;
	}




	public static boolean startsWithContent(Object oldArray, Object writeArray) {
		if (oldArray == writeArray) { return true;}
		if (!ArrayObject.wrapable(oldArray) || !ArrayObject.wrapable(writeArray)) {
			throw new ClassCastException(
				String.format("unresolved: %s or %s"
							  , null == oldArray ?null: oldArray.getClass().getName()
							  , null == writeArray ?null: writeArray.getClass().getName())
			);
		}
		ArrayObject a = ArrayObject.wrap(oldArray);
		ArrayObject b = ArrayObject.wrap(writeArray);
		if (a.innerArray() == b.innerArray()) { return true; }
		if (b.length() > a.length()) { return false; }

		int count = b.length();
		for (int index = 0; index < count; index++) {
			if (!a.equals(index, b, index)) {
				return false;
			}
		}
		return true;
	}
	public static boolean endWithContent(Object oldArray, Object writeArray) {
		if (oldArray == writeArray) { return true;}
		if (!ArrayObject.wrapable(oldArray) || !ArrayObject.wrapable(writeArray)) {
			throw new ClassCastException(
				String.format("unresolved: %s or %s"
							  , null == oldArray ?null: oldArray.getClass().getName()
							  , null == writeArray ?null: writeArray.getClass().getName())
			);
		}
		ArrayObject a = ArrayObject.wrap(oldArray);
		ArrayObject b = ArrayObject.wrap(writeArray);
		if (a.innerArray() == b.innerArray()) { return true; }
		if (b.length() > a.length()) { return false; }

		int aIndex = a.length() - 1;
		int bIndex = b.length() - 1;
		int count = Math.min(a.length(), b.length());
		for (int i = 0; i < count; i++) {
			if (!a.equals(aIndex--, b, bIndex--)) {
				return false;
			}
		}
		return true;
	}




	public static int indexOfs(Object array, int leftIndexRange, int rightIndexRange, Object elements) {
		if (array == elements) { return 0;}

		ArrayObject arrayObject = ArrayObject.wrap(array);
		if (leftIndexRange < 0
			|| rightIndexRange < 0
			|| leftIndexRange > rightIndexRange
			|| rightIndexRange > arrayObject.length()) {

			throw new ArrayIndexOutOfBoundsException(
				String.format("src.length=%s, offset=%s, end=%s"
							  , arrayObject.length()
							  , leftIndexRange
							  , rightIndexRange
							  )
			);
		}
		if (ArrayObject.wrapable(elements)) {
			ArrayObject elementObject = ArrayObject.wrap(elements);
			int elementsCount = elementObject.length();
			if (elementsCount == 0) { return leftIndexRange; }
			for (int i = leftIndexRange; i < rightIndexRange && elementsCount <= (rightIndexRange - i) ;i++) {
				WHERE: if (arrayObject.equals(i, elementObject, 0)) {
					for (int c = 1; c < elementObject.length(); c++) {
						if (!arrayObject.equals(i + c, elementObject, c)) {
							break WHERE; 
						}
					}
					return i;
				}
			}
		} else {
//			for (int i = leftIndexRange; i < rightIndexRange;i++) {
//				Object aELe = arrayObject.objectValue(i); 
//				if (null == aELe) {
//					if (null == element) {
//						return i;
//					}
//				} else if (aELe.equals(element)) {
//					return i;
//				}
//			}
			throw new ClassCastException(String.format("%s not is a array", (null == elements) ?null: elements.getClass().getName()));
		}
		return -1;
	}
	public static int lastIndexOfs(Object array, int leftIndexRange, int rightIndexRange, Object elements) {
		if (array == elements) { return 0;}

		ArrayObject arrayObject = ArrayObject.wrap(array);
		if (leftIndexRange < 0
			|| rightIndexRange < 0
			|| leftIndexRange > rightIndexRange
			|| rightIndexRange > arrayObject.length()) {

			throw new ArrayIndexOutOfBoundsException(
				String.format("src.length=%s, offset=%s, end=%s"
							  , arrayObject.length()
							  , leftIndexRange
							  , rightIndexRange
							  )
			);
		}
		if (ArrayObject.wrapable(elements)) {
			ArrayObject elementObject = ArrayObject.wrap(elements);
			int elementsCount = elementObject.length();
			if (elementsCount == 0) { return rightIndexRange; }
			for (int i = rightIndexRange - elementsCount; i >= leftIndexRange ;i--) {
				WHERE: if (arrayObject.equals(i, elementObject, 0)) {
					for (int c = 1; c < elementObject.length(); c++) {
						if (!arrayObject.equals(i + c, elementObject, c)) {
							break WHERE; 
						}
					}
					return i;
				}
			}
		} else {
//			for (int i = rightIndexRange - 1; i >= leftIndexRange;i++) {
//				Object aELe = arrayObject.objectValue(i); 
//				if (null == aELe) {
//					if (null == element) {
//						return i;
//					}
//				} else if (aELe.equals(element)) {
//					return i;
//				}
//			}
			throw new ClassCastException(String.format("%s not is a array", (null == elements) ?null: elements.getClass().getName()));
		}
		return -1;
	}



	
	
	
	
	/**
	 * Object[]
	 * 
	 * byte[] 
	 * long[] 
	 * double[] 
	 * char[] 
	 * int[] 
	 * boolean[] 
	 * float[] 
	 * short[]
	 */
	public static boolean[] toBooleanArray(Object array) 	{ return ArrayObject.toBooleanArrayObject(array).innerArray();}
	public static byte[] 	toByteArray(Object array) 		{ return ArrayObject.toByteArrayObject(array).innerArray(); }
	public static char[] 	toCharArray(Object array) 		{ return ArrayObject.toCharArrayObject(array).innerArray(); }
	public static double[] 	toDoubleArray(Object array) 	{ return ArrayObject.toDoubleArrayObject(array).innerArray(); }
	public static float[] 	toFloatArray(Object array) 		{ return ArrayObject.toFloatArrayObject(array).innerArray(); }
	public static int[] 	toIntArray(Object array) 		{ return ArrayObject.toIntArrayObject(array).innerArray(); }
	public static long[] 	toLongArray(Object array) 		{ return ArrayObject.toLongArrayObject(array).innerArray(); }
	public static short[] 	toShortArray(Object array) 		{ return ArrayObject.toShortArrayObject(array).innerArray(); }


	public static String[] 	toStringArray(Object array) 		{ return ArrayObject.toStringArrayObject(array).innerArray(); }

	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Object array, T[] array2) { return (T[]) ArrayObject.toArrayObject(array, array2).innerArray(); }
	public static Object toArray(Object array, Object array2) { return ArrayObject.toArrayObject(array, array2).innerArray(); }
}
