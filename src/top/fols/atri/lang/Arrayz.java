package top.fols.atri.lang;

import java.lang.reflect.Array;
import top.fols.atri.array.ArrayObject;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
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

}
