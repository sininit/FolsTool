package top.fols.box.util;

import java.util.Arrays;

import top.fols.atri.lang.Finals;
import top.fols.atri.util.Releasable;

public class FinalArray<K> implements Releasable {
	private Object[] array;
	private int length;

	public FinalArray(int len) {
		if (len < 1) {
			throw new NumberFormatException("len need > 0");
		}
		array = new Object[len];
		length = len;
	}

	public FinalArray(K[] arr) {
		if (null == arr) {
			throw new NullPointerException();
		}
		if (arr.length < 1) {
			throw new NumberFormatException("len need > 0");
		}
		array = arr;
		length = arr.length;
	}

	// <- 将数据向左移动
	private void left0(Object val) {
		for (int i = 0; i < length - 1; i++) {
			array[i] = array[i + 1];
		}
		array[length - 1] = val;
	}

	public void left(K val) {
		left0(val);
	}

	public void left(K[] val) {
		if (val.length >= length) {
			for (int i = 0; i < length; i++) {
				array[i] = val[val.length - length + i];
			}
		} else {
			int ind = length - val.length;
			for (int i = 0; i < ind; i++) {
				array[i] = array[length - ind + i];
			}
			for (int i = 0; i < val.length; i++) {
				array[ind + i] = val[i];
			}
		}
	}

	// -> 将数据向右移动
	private void right0(Object val) {
		for (int i = length - 1; i >= 1; i--) {
			array[i] = array[i - 1];
		}
		array[0] = val;
	}

	public void right(K val) {
		right0(val);
	}

	public void right(K[] val) {
		if (val.length >= length) {
			for (int i = 0; i < length; i++) {
				array[i] = val[i];
			}
		} else {
			int ind = val.length;
			for (int i = 0; i < length - val.length; i++) {
				array[(length - 1) - i] = array[(length - 1) - ind - i];
			}
			for (int i = 0; i < ind; i++) {
				array[i] = val[i];
			}
		}
	}

	public int length() {
		return length;
	}

	@Override
	public boolean release() {
		array = Finals.EMPTY_OBJECT_ARRAY;
		length = 0;
		return true;
	}

	@Override
	public boolean released() {
		return array == Finals.EMPTY_OBJECT_ARRAY;
	}










	@SuppressWarnings("unchecked")
	public K get(int i) {
		return (K) array[i];
	}

	public void set(int i, K val) {
		array[i] = val;
	}

	public Object[] getArray() {
		return array;
	}

	public void toArray(K[] arr) {
		for (int i = 0; i < length(); i++) {
			arr[i] = get(i);
		}
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		return Arrays.toString(array);
	}
}
