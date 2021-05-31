package top.fols.atri.array;
import java.io.IOException;
import java.util.Arrays;

public class BooleanArrayObject extends ArrayObject<boolean[]> {
	public BooleanArrayObject(boolean[] array) { super(array); }



	@Override
	public byte byteValue(int index) {
		// TODO: Implement this method
		return super.array[index] ?(byte)1: (byte)0;
	}

	@Override
	public long longValue(int index) {
		// TODO: Implement this method
		return super.array[index] ?(long)1: (long)0;
	}

	@Override
	public double doubleValue(int index) {
		// TODO: Implement this method
		return super.array[index] ?(double)1: (double)0;
	}

	@Override
	public char charValue(int index) {
		// TODO: Implement this method
		return super.array[index] ?(char)1: (char)0;
	}

	@Override
	public int intValue(int index) {
		// TODO: Implement this method
		return super.array[index] ?(int)1: (int)0;
	}

	@Override
	public float floatValue(int index) {
		// TODO: Implement this method
		return super.array[index] ?(float)1: (float)0;
	}

	@Override
	public short shortValue(int index) {
		// TODO: Implement this method
		return super.array[index] ?(short)1: (short)0;
	}



	@Override
	public boolean booleanValue(int index) {
		// TODO: Implement this method
		return super.array[index];
	}
	@Override
	public Boolean objectValue(int index) {
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
	public int lengthOf(boolean[] array) {
		// TODO: Implement this method
		return array.length;
	}

	@Override
	public int length() {
		// TODO: Implement this method
		return super.array.length;
	}



	public boolean get(int index) { return super.array[index]; }
	public void set(int index, boolean value) { super.array[index] = value; }

	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
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
		super.array[index] = (boolean) value;
	}
	@Override
	public void set(int index, ArrayObject value, int valueIndex) {
		// TODO: Implement this method
		super.array[index] = value.booleanValue(valueIndex);
	}




	@Override
	public boolean equals(int index, ArrayObject value, int index2) {
		// TODO: Implement this method
		return super.array[index] == value.booleanValue(index2);
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
