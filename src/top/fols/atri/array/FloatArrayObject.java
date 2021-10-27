package top.fols.atri.array;

import java.util.Arrays;

public class FloatArrayObject extends ArrayObject<float[]> {
	public FloatArrayObject(float[] array){ super(array); }



	@Override
	public byte byteValue(int index) {
		// TODO: Implement this method
		return (byte) super.array[index];
	}

	@Override
	public long longValue(int index) {
		// TODO: Implement this method
		return (long) super.array[index];
	}

	@Override
	public double doubleValue(int index) {
		// TODO: Implement this method
		return (double) super.array[index];
	}

	@Override
	public char charValue(int index) {
		// TODO: Implement this method
		return (char) super.array[index];
	}

	@Override
	public int intValue(int index) {
		// TODO: Implement this method
		return (int) super.array[index];
	}

	@Override
	public float floatValue(int index) {
		// TODO: Implement this method
		return (float) super.array[index];
	}

	@Override
	public short shortValue(int index) {
		// TODO: Implement this method
		return (short) super.array[index];
	}



	@Override
	public boolean booleanValue(int index) {
		// TODO: Implement this method
		return super.array[index] != 0F;
	}
	@Override
	public Float objectValue(int index) {
		// TODO: Implement this method
		return super.array[index];
	}
	@Override
	public String stringValue(int index) {
		return String.valueOf(super.array[index]);
	}



	@Override public boolean isNull(int index) { return false; }
	@Override public boolean isPrimitive() { return true; }
	
	
	@Override
	public int lengthOf(float[] array) {
		// TODO: Implement this method
		return array.length;
	}

	@Override
	public int length() {
		// TODO: Implement this method
		return super.array.length;
	}



	public float get(int index){ return super.array[index]; }
	public void set(int index, float value) { super.array[index] = value; }

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
	public int hashCode() {
		// TODO: Implement this method
		return Arrays.hashCode(this.array);
	}

	




	@Override
	public void set(int index, Object value) {
		// TODO: Implement this method
		super.array[index] = (float) value;
	}
	@Override
	public void set(int index, ArrayObject value, int valueIndex) {
		// TODO: Implement this method
		super.array[index] = value.floatValue(valueIndex);
	}




	@Override
	public boolean equals(int index, ArrayObject value, int index2) {
		// TODO: Implement this method
		return super.array[index] == value.floatValue(index2);
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
