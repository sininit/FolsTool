package top.fols.box.util;
import java.lang.reflect.Array;
import java.util.Collection;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.lang.XSequences;
import top.fols.box.lang.interfaces.XInterfacesSequence;

public class XArray {
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
	public static void set(Object originalArray, int index, Object value) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		Array.set(originalArray, index, value);
	}
	public static Object get(Object originalArray, int index) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		return Array.get(originalArray, index);
	}
	public static int getLength(Object originalArray) {
		return Array.getLength(originalArray);
	}
	public static Object newInstance(java.lang.Class<?> componentType, int length) {
		return Array.newInstance(componentType, length);
	}
	public static Object newInstance(java.lang.Class<?> componentType, int... dimensions) throws java.lang.IllegalArgumentException, java.lang.NegativeArraySizeException {
		return Array.newInstance(componentType, dimensions);
	}


	public static char[] arraycopy(String originStr, int originStrCopyOffIndex, char[] toarray, int off, int len) {
		if (len <= 0)
			return toarray;
		originStr.getChars(originStrCopyOffIndex, originStrCopyOffIndex + len, toarray, off);
		return toarray;
	}
	public static Object arraycopy(Object originarray, int originarrayCopyOffIndex, Object toarray, int off, int len) {
		if (len <= 0)
			return toarray;
		System.arraycopy(originarray, originarrayCopyOffIndex, toarray, off, len);
		return toarray;
	}


	/*
	 equals Array type
	 判断数组类型是否相同
	 */
	public static boolean equalsArrayTypeAbs(Object originalArray, Object objectArray) {
		if (!(isArray(originalArray) && isArray((objectArray)))) return false;
		else return getElementClass(originalArray) == getElementClass(originalArray);
	}
	public static boolean equalsArrayType(Object originalArray, Object objectArray) {
		if (originalArray == objectArray) return true;
		if (null == originalArray || null == objectArray) return false;
		else if (originalArray instanceof Object[]) return objectArray instanceof Object[];
		else if (originalArray instanceof byte[]) return objectArray instanceof byte[];
		else if (originalArray instanceof long[]) return objectArray instanceof long[];
		else if (originalArray instanceof char[]) return objectArray instanceof char[];
		else if (originalArray instanceof int[]) return objectArray instanceof int[];
		else if (originalArray instanceof short[]) return objectArray instanceof short[];
		else if (originalArray instanceof boolean[]) return objectArray instanceof boolean[];
		else if (originalArray instanceof double[]) return objectArray instanceof double[];
		else if (originalArray instanceof float[]) return objectArray instanceof float[];
		else return false;
	}



	public static <A extends Object> A add(A originalArray, int index, Object Element) {
		return (A) add(originalArray, index, Element, getElementClass(originalArray));
	}
	public static <A extends Object> A addAllTraverse(A originalArray, int index, Object Array) {   
		return (A) addAllTraverse(originalArray, index, Array, getElementClass(originalArray));
	}
	public static <A extends Object> A addAll(A originalArray, int index, Object Array) {       
		return (A) addAll(originalArray, index, Array, getElementClass(originalArray));
	}
	public static <A extends Object> A subArray(A originalArray, int start, int end) {
		return (A) subArray(originalArray, start, end, getElementClass(originalArray));
	}
	public static <A extends Object> A remove(A originalArray, int start, int end) {
		return (A) remove(originalArray, start, end, getElementClass(originalArray));
	}


	/*
	 Add All Traversal
	 遍历添加元素

	 long[] data = new long[]{71, 69, 84, 32, 47, 32, 72, 84, 84};
	 //{71, 69, 84, 32, 47, 32, 72, 84, 84}
	 //0  1   2   3   4   5   6   7   8   9 
	 AddAllUsageTraversal(data,0,new int[]{1,2,3},long.class); >> {1,2,3, 71, 69, 84, 32, 47, 32, 72, 84, 84}
	 AddAllUsageTraversal(data,1,new long[]{1,2,3},long.class); >> {71, 1,2,3, 69, 84, 32, 47, 32, 72, 84, 84}
	 */
	public static Object addAllTraverse(Object originalArray, int index, Object Array, Class originalArrayElementClass) {   
		int originalArrayLength = null == originalArray ?0: getLength(originalArray);
		if (index < 0 || index > originalArrayLength)
			index = originalArrayLength;
		int addElementLength = null == Array ?0: getLength(Array);
		Object object = copyOutsideScope(originalArray, index, addElementLength, originalArrayElementClass);
		if (addElementLength != 0)
			XArrays.arraycopyTraverse(Array, 0, object, index, addElementLength);
		return object;
	}


	/**
	 Add All
	 元素类型相同的数组

	 int[] data = new int[]{71, 69, 84, 32, 47, 32, 72, 84, 84};
	 //{71, 69, 84, 32, 47, 32, 72, 84, 84}
	 //0  1   2   3   4   5   6   7   8   9 
	 addAll(data,0,new int[]{1,2,3},int.class); >> {1,2,3, 71, 69, 84, 32, 47, 32, 72, 84, 84}
	 addAll(data,1,new int[]{1,2,3},int.class); >> {71, 1,2,3, 69, 84, 32, 47, 32, 72, 84, 84}
	 addAll(data,6,new int[]{1,2,3},int.class); >> {71, 69, 84, 32, 47, 32, 1,2,3, 72, 84, 84}
	 **/  
	public static Object addAll(Object originalArray, int index, Object Array, Class originalArrayElementClass) {       
		int originalArrayLength = null == originalArray ?0: getLength(originalArray);
		if (index < 0 || index > originalArrayLength)
			index = originalArrayLength;
		int addElementLength = null == Array ?0: getLength(Array);
		Object object = copyOutsideScope(originalArray, index, addElementLength, originalArrayElementClass);
		if (addElementLength != 0)
			System.arraycopy(Array, 0, object, index, addElementLength);
		return object;
	} 
	// Insert null data
	private static Object copyOutsideScope(Object originalArray, int needAddDataIndexOf, int needAddLength, Class originalArrayElementClass) {   
		if (null == originalArrayElementClass)
			throw new NullPointerException("attempt to read to null class type");
		int originalArrayLength = getLength(originalArray);
		Object object = newInstance(originalArrayElementClass, originalArrayLength + needAddLength);
		if (originalArrayLength != 0)
			System.arraycopy(originalArray, 0, object, 0, originalArrayLength);
		if (!(needAddDataIndexOf == originalArrayLength) && needAddLength != 0)
			System.arraycopy(object, needAddDataIndexOf, object, needAddDataIndexOf + needAddLength, originalArrayLength - needAddDataIndexOf);
		return object;
	}

	/**
	 Element Will be added to originalArray
	 Element serve Object
	 ElementClass Must serve Element.class
	 将Element添加到originalArray

	 int[] data = new int[]{71, 69, 84, 32, 47, 32, 72, 84, 84};
	 //{71, 69, 84, 32, 47, 32, 72, 84, 84}
	 //0  1   2   3   4   5   6   7   8   9 
	 add(data,0,8888,int.class); >> {8888, 71, 69, 84, 32, 47, 32, 72, 84, 84}
	 add(data,1,9999,int.class); >> {71, 9999, 69, 84, 32, 47, 32, 72, 84, 84}
	 add(data,6,2333,int.class); >> {71, 69, 84, 32, 47, 32, 2333, 72, 84, 84}
	 **/
	public static Object add(Object originalArray, int index, Object Element, Class originalArrayElementClass) {       
		if (null == originalArray)
			throw new NullPointerException("attempt to read from null array");
		int originalArrayLength= getLength(originalArray);
		if (index < 0 || index > originalArrayLength)
			index = originalArrayLength;
		int addElementLength = 1;
		Object object = copyOutsideScope(originalArray, index, addElementLength, originalArrayElementClass);
		set(object, index, Element);
		return object;
	}
	/**
	 Sub Array element
	 取出数组指定位置数据

	 byte[] databyte = new byte[]{71, 69, 84, 32, 47, 32, 72, 84, 84};
	 //{71, 69, 84, 32, 47, 32, 72, 84, 84}
	 //0  1   2   3   4   5   6   7   8   9 
	 subArray(databyte,0,8,byte.class); >> {71, 69, 84, 32, 47, 32, 72, 84}
	 subArray(databyte,1,8,byte.class); >> {69, 84, 32, 47, 32, 72, 84}
	 subArray(databyte,6,7,byte.class); >> {72}
	 **/
	public static Object subArray(Object originalArray, int start, int end, Class originalArrayElementClass) {       
		if (null == originalArray)
			throw new NullPointerException("attempt to read from null array");
		if (start > end)
			throw new ArrayIndexOutOfBoundsException("index=" + start + ", end=" + end + ", size=" + getLength(originalArray));
		int newlength = end - start;
		Object object = newInstance(originalArrayElementClass, newlength);
		if (newlength != 0)
			System.arraycopy(originalArray, start, object, 0, newlength);
		return object;
	}
	/**
	 remove Array element
	 删除数组项目

	 remove(new int[]{1,2,3,4,5,6,7,8,9}, 1, 3, int.class); >> {1,4,5,6,7,8,9}
	 remove(new int[]{1,2,3,4,5,6,7,8,9}, 4, 6, int.class); >> {1,2,3,4,7,8,9}
	 **/
	public static Object remove(Object originalArray, int start, int end, Class originalArrayElementClass) {       
		if (null == originalArray)
			throw new NullPointerException("attempt to read from null array");
		int originalArrayLength = getLength(originalArray);
		if (start > end)
			throw new ArrayIndexOutOfBoundsException("index=" + start + ", end=" + end + ", size=" + originalArrayLength);
		int newlength = originalArrayLength - (end - start);
		if (newlength == originalArrayLength)
			return originalArray;

		Object object = newInstance(originalArrayElementClass, newlength);
		if (start != 0)
			System.arraycopy(originalArray, 0, object, 0, start);
		if (((newlength) - start) != 0)
			System.arraycopy(originalArray, end, object, start,  (newlength) - start);
		return object;
	}
	/**
	 is Array
	 判断对象是否为数组

	 isArray(1); >> false
	 isArray(new int[]{1}); >> true
	 isArray(new String[]{"str"}); >> true
	 **/
	public static boolean isArray(Object originalArray) {
		if (null == originalArray)
			return false;
		else if (originalArray.getClass().isArray())
			return true;
		return false;
	}

	/*
	 Determines whether the array is Object[]  
	 判断数组是否为Object[]
	 */
	public static boolean isObjectArray(Object originalArray) {
		return originalArray instanceof Object[];
	}


	/**
	 get Array Dimensional    
	 获取数组纬度

	 getDimensional(int[0]); >> 1
	 getDimensional(int[0][0]); >> 2
	 **/
	public static int getDimensional(Object Object) {
		Class originalArray = Object.getClass();
		if (!originalArray.isArray())
			return 0;
		return getDimensionalFromClassCanonicalName(originalArray.getCanonicalName());
	}
	public static int getDimensionalFromClassCanonicalName(String name) {
		if (null == name)
			return 0;
		int index = name.indexOf("["); //java.lang.System[][]
		if (index == -1)
			return 0;
		return (name.length() - index) / 2;//[][]
	}
	
	
	
	
	
	/**
	 Array To String    
	 数组到字符串

	 **/
	public static String toString(Object originalArray) {
		if (null == originalArray)
			throw new NullPointerException("attempt to read from null array");
		if (originalArray instanceof Collection)
			return toString(((Collection)originalArray).toArray());
		if (!XArray.isArray(originalArray))
			throw new ClassCastException(String.format("%s not can cast to array", originalArray.getClass().getName()));
		StringBuilder result = new StringBuilder(toStringProcess(originalArray));
		if (result.substring(result.length() - 1, result.length()).equals(","))
			result.delete(result.length() - 1, result.length());
		return result.toString();
	}
    private static StringBuilder toStringProcess(Object originalArray) {
		Object Object;
		StringBuilder result = new StringBuilder();
		result.append(originalArray.getClass().getCanonicalName());
		result.append('{');
		XInterfacesSequence newArray = XSequences.wrap(originalArray);
		for (int i = 0; i < newArray.length(); i++) {
			Object = newArray.get(i);
			if (XArray.isArray(Object))
				result.append(toStringProcess(Object));
			else {
				result.append(Object);
				result.append(',');
				continue;
			}
		}
		newArray.releaseBuffer();
		if (result.substring(result.length() - 1, result.length()).equals(","))
			result.delete(result.length() - 1, result.length());
		result.append('}');
		result.append(',');
		return result;
	}





	/*
	 get Array Element Class
	 获取数组元素类

	 getElementClass(new int[0]); >> int.class
	 getElementClass(new int[0][0]); >> int[].class
	 */
	public static Class getElementClass(Object originalArray) {
		return null == originalArray?null:originalArray.getClass().getComponentType();
	}
	public static Class getElementClass(Class originalArrayCls) {
		return null == originalArrayCls?null:originalArrayCls.getComponentType();
	}



	/*
	 new array val for fill value
	 创建一个数组 val为填充值
	 */
	public static Object newInstanceFill(Class originalArrayElementClass, int length, Object fiiVal) {
		if (length == 0)
			return newInstance(originalArrayElementClass, 0);
		Object array = newInstance(originalArrayElementClass, 1);
		set(array, 0, fiiVal);
		if (length == 1)
			return array;
		Object newArray = repeat(array, length);
		return newArray;
	}

    /*
	 fast repeat copy array
	 快速重复复制数组

	 fastRepeat(new int[]{1},int.class,1); >> {1}
	 fastRepeat("1".toCharArray(),char.class,3); >> {'1','1','1'}
	 */
	public static <A extends Object> A repeat(A originalArray, int repeatCount) {
		return (A)repeat(originalArray, getElementClass(originalArray), repeatCount);
	}
	public static Object repeat(Object originalArray, Class originalArrayElementClass, int repeatCount) {
		int secureLength;
		if (null == originalArray)
			throw new NullPointerException("attempt to read from null array");
		else if ((secureLength = getLength(originalArray)) == 0)
			throw new NullPointerException("array length min can't for 0");
		else if (repeatCount < 1)
			throw new NumberFormatException("array repeat Count length min can't for 0");
		int nowCumulative = 1;
		int newCumulative;
		Object newArray = originalArray;
		while (true) {
			if (nowCumulative >= repeatCount)
				break;
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
	 * copyOf array
	 * 复制数组
	 */
	public static <A extends Object> A copyOf(A originalArray, int length) {
		Class cls = getElementClass(originalArray);
		if (null == cls)
			throw new ClassCastException(String.format("%s not can cast to array", originalArray.getClass().getName()));
		Object array = newInstance(cls, length);
		if (length != 0) {
			int originalArrayLength = getLength(originalArray);
			System.arraycopy(originalArray, 0, array, 0, length < originalArrayLength ? length: originalArrayLength);
		}
		return (A) array;
	}
	public static <A extends Object> A copyOfRange(A originalArray, int from, int to) {
		Class cls = getElementClass(originalArray);
		if (null == cls)
			throw new ClassCastException(String.format("%s not can cast to array", originalArray.getClass().getName()));
		A newArray = (A) subArray(originalArray, from, to, cls);
		return newArray;
	}
	
	
	
	
	
	
	/*
	 * 将数组转换成别的类型
	 * copyOfConversion(new int[]{},new long[]{}); 将int数组转换为long[]数组
	 * copyOfConversion(new int[]{},long.class); 
	 */
	public static <A extends Object> A copyOfConversion(Object originalArray, @XAnnotations("new Class[0]") A newArrayNullObj) {
		return copyOfConversion(originalArray, getLength(originalArray), newArrayNullObj);
	}
	public static <A extends Object> A copyOfConversion(Object originalArray, int length, @XAnnotations("new Class[0]")A newArrayNullObj) {
		return (A) copyOfConversion(originalArray, length, getElementClass(newArrayNullObj));
	}
	public static Object copyOfConversion(Object originalArray, Class <? extends Object> newElementType) {
		return copyOfConversion(originalArray, getLength(originalArray), newElementType);
	}
	public static Object copyOfConversion(Object originalArray, int length, Class <? extends Object> newElementType) {
		Object newArray = newInstance(newElementType, length);
		int oArrLen = getLength(originalArray);
		if (length != 0)
			XArrays.arraycopyTraverse(originalArray, 0, newArray, 0, Math.min(length, oArrLen));
		return newArray;
	}
}
