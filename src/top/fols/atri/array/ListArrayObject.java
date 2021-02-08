package top.fols.atri.array;
import java.util.List;

public class ListArrayObject<T extends Object> extends ArrayObject<List<T>> {
	public ListArrayObject(List<T> list) {
		super(list);
	}


	@Override
	public byte byteValue(int index) {
		// TODO: Implement this method
		Object value = super.array.get(index);
		if (value instanceof Byte) {
			return ((Byte)value);
		} else if (null == value) {
			return 0;
		} else if (value instanceof Number) {
			return ((Number)value).byteValue();
		} else {
			return Byte.parseByte(value.toString());
		}
	}

	@Override
	public long longValue(int index) {
		// TODO: Implement this method
		Object value = super.array.get(index);
		if (value instanceof Long) {
			return ((Long)value);
		} else if (null == value) {
			return 0;
		} else if (value instanceof Number) {
			return ((Number)value).longValue();
		} else {
			return Long.parseLong(value.toString());
		}
	}

	@Override
	public double doubleValue(int index) {
		// TODO: Implement this method
		Object value = super.array.get(index);
		if (value instanceof Double) {
			return ((Double)value);
		} else if (null == value) {
			return 0;
		} else if (value instanceof Number) {
			return ((Number)value).doubleValue();
		} else {
			return Double.parseDouble(value.toString());
		}
	}

	@Override
	public char charValue(int index) {
		// TODO: Implement this method
		Object value = super.array.get(index);
		if (value instanceof Character) {
			return ((Character)value);
		} else if (null == value) {
			return 0;
		} else if (value instanceof Number) {
			return (char) ((Number)value).intValue();
		} else {
			String str = value.toString();
			if (str.length() == 1) {
				return str.charAt(0);
			} else if (str.length() == 0) {
				return 0;
			} else {
				throw new NumberFormatException(str);
			}
		}
	}

	@Override
	public int intValue(int index) {
		// TODO: Implement this method
		Object value = super.array.get(index);
		if (value instanceof Integer) {
			return ((Integer)value);
		} else if (null == value) {
			return 0;
		} else if (value instanceof Number) {
			return ((Number)value).intValue();
		} else {
			return Integer.parseInt(value.toString());
		}
	}

	@Override
	public float floatValue(int index) {
		// TODO: Implement this method
		Object value = super.array.get(index);
		if (value instanceof Float) {
			return ((Float)value);
		} else if (null == value) {
			return 0;
		} else if (value instanceof Number) {
			return ((Number)value).floatValue();
		} else {
			return Float.parseFloat(value.toString());
		}
	}

	@Override
	public short shortValue(int index) {
		// TODO: Implement this method
		Object value = super.array.get(index);
		if (value instanceof Short) {
			return ((Short)value);
		} else if (null == value) {
			return 0;
		} else if (value instanceof Number) {
			return ((Number)value).shortValue();
		} else {
			return Short.parseShort(value.toString());
		}
	}



	@Override
	public boolean booleanValue(int index) {
		// TODO: Implement this method
		Object value = super.array.get(index);
		if (value instanceof Boolean) {
			return ((Boolean)value);
		} else if (null == value) {
			return false;
		} else if (value instanceof Number) {
			return ((Number)value).intValue() != 0;
		} else {
			return Boolean.parseBoolean(value.toString());
		}
	}
	@Override
	public T objectValue(int index) {
		// TODO: Implement this method
		return super.array.get(index);
	}



	@Override public boolean isNull(int index) { return null == super.array.get(index); }
	@Override public boolean isPrimitive() { return false; }

	@Override
	public int lengthOf(List<T> array) {
		// TODO: Implement this method
		return array.size();
	}

	@Override
	public int length() {
		// TODO: Implement this method
		return super.array.size();
	}



	public T get(int index) { return super.array.get(index); }

	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (this == obj) { return true; }
		if (null == obj) { return false; }
		if (false == ArrayObject.wrapable(obj)) { return false; }

		ArrayObject value = ArrayObject.wrap(obj);
		int valueLength = value.length();
		if (this.length() != valueLength) { return false; }
		for (int index = 0; index < valueLength; index++) {
			if (!this.equals(index, value, index)) {
				return false;
			}
		}
		return true;
	}







	@Override
	public void set(int index, Object value) {
		// TODO: Implement this method
		super.array.set(index, (T) value);
	}

	@Override
	public void set(int index, ArrayObject value, int valueIndex) {
		// TODO: Implement this method
		super.array.set(index, (T) value.objectValue(valueIndex));
	}




	@Override
	public boolean equals(int index, ArrayObject value, int index2) {
		// TODO: Implement this method
		T object = super.array.get(index);
		if (null == object) {
			return null == value.objectValue(index2);
		} else {
			Object object2 = value.objectValue(index2);
			if (ArrayObject.wrapable(object)) {
				if (ArrayObject.wrapable(object2)) {
					return ArrayObject.wrap(object).equals(ArrayObject.wrap(object2));
				} else {
					return false;
				}
			} else {
				return object.equals(object2);
			}
		}
	}


	@Override
	public int hashCode() {
		// TODO: Implement this method
		return this.array.hashCode();
	}


	@Override
	public boolean release() {
		super.array  = null;
		super.length = 0;
		return true;
	}
	@Override
	public boolean released() {
		return null == super.array;
	}
}
