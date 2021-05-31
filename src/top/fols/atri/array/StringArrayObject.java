package top.fols.atri.array;

public class StringArrayObject<T extends String> extends ArrayObject<String[]> {
	public StringArrayObject(T[] array) { 
		super(array); 
	}




	@Override
	public byte byteValue(int index) {
		// TODO: Implement this method
		return Byte.parseByte(super.array[index]);
	}

	@Override
	public long longValue(int index) {
		// TODO: Implement this method
		return Long.parseLong(super.array[index]);
	}

	@Override
	public double doubleValue(int index) {
		// TODO: Implement this method
		return Double.parseDouble(super.array[index]);
	}

	@Override
	public char charValue(int index) {
		// TODO: Implement this method
		String str = super.array[index];
		if (str.length() == 1) {
			return str.charAt(0);
		} else if (str.length() == 0) {
			return 0;
		} else {
			throw new NumberFormatException(str);
		}
	}

	@Override
	public int intValue(int index) {
		// TODO: Implement this method
		return Integer.parseInt(super.array[index]);
	}

	@Override
	public float floatValue(int index) {
		// TODO: Implement this method
		return Float.parseFloat(super.array[index]);
	}

	@Override
	public short shortValue(int index) {
		// TODO: Implement this method
		return Short.parseShort(super.array[index]);
	}



	@Override
	public boolean booleanValue(int index) {
		// TODO: Implement this method
		return Boolean.parseBoolean(super.array[index]);
	}
	@Override
	public T objectValue(int index) {
		// TODO: Implement this method
		return (T) super.array[index];
	}
	@Override
	public T stringValue(int index) {
		// TODO: Implement this method
		return (T) super.array[index];
	}



	@Override public boolean isNull(int index) { return null == super.array[index]; }
	@Override public boolean isPrimitive() { return false; }


	@Override
	public int lengthOf(String[] array) {
		return 0;
	}
	@Override
	public int length() {
		// TODO: Implement this method
		return super.array.length;
	}



	public T get(int index) { return (T) super.array[index]; }

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
		super.array[index] = (T) null == value?null:value.toString();
	}
	@Override
	public void set(int index, ArrayObject value, int valueIndex) {
		// TODO: Implement this method
		super.array[index] = (T) value.stringValue(valueIndex);
	}




	@Override
	public boolean equals(int index, ArrayObject value, int index2) {
		// TODO: Implement this method
		T object = (T) super.array[index];
		if (null == object) {
			return null == value.stringValue(index2);
		} else {
			Object object2 = value.stringValue(index2);
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




	/**
	 * @see top.fols.atri.array.ObjectArrayObject#equals(Object, ArrayObject, int)
	 */
	public static boolean equals(Object object, ArrayObject value, int index2) {
		// TODO: Implement this method
		if (null == object) {
			return null == value.stringValue(index2);
		} else {
			Object object2 = value.stringValue(index2);
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

