package top.fols.atri.array;
import top.fols.atri.util.Releasable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.List;

public abstract class ArrayObject< T extends Object> implements Releasable {
	protected T 	array;
	protected int   length;

	public ArrayObject(T array) {
		this.array = array;
		this.length = lengthOf(array);
	}



	abstract public byte 	byteValue(int index);
	abstract public long 	longValue(int index);
	abstract public double 	doubleValue(int index);
	abstract public char 	charValue(int index);
	abstract public int 	intValue(int index);
	abstract public boolean booleanValue(int index);
	abstract public float 	floatValue(int index);
	abstract public short 	shortValue(int index);

	abstract public Object objectValue(int index);

	abstract public String stringValue(int index);


	public T 		innerArray() { return this.array; }
	public int 		innerArrayLength() { return Array.getLength(this.array);}
	public Class 	innerArrayType() { return this.array.getClass(); }
	public int 		innerArrayIndex(int index) { return index; }


	abstract public int lengthOf(T array);
	public int length() 		{ return this.length; }

	abstract public void set(int index, Object value);
	abstract public void set(int index, ArrayObject value, int valueIndex);
	abstract public boolean equals(int index, ArrayObject value, int valueIndex);


	abstract public boolean isNull(int index);
	abstract public boolean isPrimitive();


	/**
	 * the contents are equal, regardless of the array type
	 */
	abstract public boolean equals(Object obj);
	public boolean equalsAll(Object obj) { 
		return this.equals(obj) && ArrayObjects.equalsInnerArrayType(this, obj);
	}

	

	
	

	static final String TO_STRING_START = "{";
	static final String TO_STRING_SEPARATOR = ", ";
	static final String TO_STRING_END = "}";

	@Override
	public String toString() {
		// TODO: Implement this method

		StringBuilder builder = new StringBuilder();
		builder.append(ArrayObject.TO_STRING_START);
		for (int i = 0; i < this.length(); i++) {
			builder.append(objectValue(i)).append(ArrayObject.TO_STRING_SEPARATOR);
		}
		if (builder.length() >= ArrayObject.TO_STRING_SEPARATOR.length()) {
			if (builder.substring(builder.length() - ArrayObject.TO_STRING_SEPARATOR.length(), builder.length()).equals(ArrayObject.TO_STRING_SEPARATOR)) {
				builder.setLength(builder.length() - ArrayObject.TO_STRING_SEPARATOR.length());
			}
		}
		builder.append(ArrayObject.TO_STRING_END);
		return builder.toString();
	}

	public <T extends Object> ArrayObject copy(int index, ArrayObject toArray, int toArrayIndex, int toArrayCount) {
		if (index + toArrayCount > this.length() || index < 0 || toArrayCount < 0) { throw new ArrayIndexOutOfBoundsException(String.format("src.length=%s, index=%s, to.length=%s, to.index=%s, size=%s", this.length(), index, toArray.length(), toArrayIndex, toArrayCount)); }
		for (int i = 0;i < toArrayCount;i++) {
			toArray.set(toArrayIndex++, this, index++);
		}
		return toArray;
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
	public static ArrayObject<boolean[]> toBooleanArrayObject(Object array) {
		ArrayObject ao = ArrayObject.wrap(array);
		ArrayObject no = ArrayObject.wrap(new boolean[ao.length()]);
		ao.copy(0, no, 0, ao.length());
		return no;
	}
	public ArrayObject<boolean[]> toBooleanArrayObject() { return toBooleanArrayObject(this); }

	public static ArrayObject<byte[]> toByteArrayObject(Object array) {
		ArrayObject ao = ArrayObject.wrap(array);
		ArrayObject no = ArrayObject.wrap(new byte[ao.length()]);
		ao.copy(0, no, 0, ao.length());
		return no;
	}
	public ArrayObject<byte[]> toByteArrayObject() { return toByteArrayObject(this); }

	public static ArrayObject<char[]> toCharArrayObject(Object array) {
		ArrayObject ao = ArrayObject.wrap(array);
		ArrayObject no = ArrayObject.wrap(new char[ao.length()]);
		ao.copy(0, no, 0, ao.length());
		return no;
	}
	public ArrayObject<char[]> toCharArrayObject() { return toCharArrayObject(this); }



	public static ArrayObject<double[]> toDoubleArrayObject(Object array) {
		ArrayObject ao = ArrayObject.wrap(array);
		ArrayObject no = ArrayObject.wrap(new double[ao.length()]);
		ao.copy(0, no, 0, ao.length());
		return no;
	}
	public ArrayObject<double[]> toDoubleArrayObject() { return toDoubleArrayObject(this); }

	public static ArrayObject<float[]> toFloatArrayObject(Object array) {
		ArrayObject ao = ArrayObject.wrap(array);
		ArrayObject no = ArrayObject.wrap(new float[ao.length()]);
		ao.copy(0, no, 0, ao.length());
		return no;
	}
	public ArrayObject<float[]> toFloatArrayObject() { return toFloatArrayObject(this); }


	public static ArrayObject<int[]> toIntArrayObject(Object array) {
		ArrayObject ao = ArrayObject.wrap(array);
		ArrayObject no = ArrayObject.wrap(new int[ao.length()]);
		ao.copy(0, no, 0, ao.length());
		return no;
	}
	public ArrayObject<int[]> toIntArrayObject() { return toIntArrayObject(this); }




	public static ArrayObject<long[]> toLongArrayObject(Object array) {
		ArrayObject ao = ArrayObject.wrap(array);
		ArrayObject no = ArrayObject.wrap(new long[ao.length()]);
		ao.copy(0, no, 0, ao.length());
		return no;
	}
	public ArrayObject<long[]> toLongArrayObject() { return toLongArrayObject(this); }


	public static ArrayObject<short[]> toShortArrayObject(Object array) {
		ArrayObject ao = ArrayObject.wrap(array);
		ArrayObject no = ArrayObject.wrap(new short[ao.length()]);
		ao.copy(0, no, 0, ao.length());
		return no;
	}
	public ArrayObject<short[]> toShortArrayObject() { return toShortArrayObject(this); }


	public static ArrayObject<String[]> toStringArrayObject(Object array) {
		ArrayObject ao = ArrayObject.wrap(array);
		ArrayObject no = ArrayObject.wrap(new String[ao.length()]);
		ao.copy(0, no, 0, ao.length());
		return no;
	}
	public ArrayObject<String[]> toStringArrayObject() { return toStringArrayObject(this); }


	public static ArrayObject toArrayObject(Object old, Object array) {
		ArrayObject ao = ArrayObject.wrap(old);
		ArrayObject no = ArrayObject.wrap(array);
		ao.copy(0, no, 0, ao.length());
		return no;
	}
	public ArrayObject toArrayObject(Object to) { return toArrayObject(this, to); }




	public static boolean wrapable(Object object) {
		if (null == object) { return false; }

		return
					object.getClass().isArray() ||
					object instanceof ArrayObject ||

					object instanceof AbstractList
//					object instanceof CharSequence ||
//					object instanceof StringBuilder
					;
	}

	public static ArrayObject<boolean[]> 				wrap(boolean[] object) { return null == object ?null: new BooleanArrayObject(object); }
	public static ArrayObject<byte[]> 					wrap(byte[] object) { return null == object ?null: new ByteArrayObject(object); }
	public static ArrayObject<char[]> 					wrap(char[] object) { return null == object ?null: new CharArrarObject(object); }
	public static ArrayObject<double[]> 				wrap(double[] object) { return null == object ?null: new DoubleArrayObject(object); }
	public static ArrayObject<float[]> 					wrap(float[] object) { return null == object ?null: new FloatArrayObject(object); }
	public static ArrayObject<int[]> 					wrap(int[] object) { return null == object ?null: new IntArrayObject(object); }
	public static ArrayObject<long[]> 					wrap(long[] object) { return null == object ?null: new LongArrayObject(object); }
	public static ArrayObject<short[]> 					wrap(short[] object) { return null == object ?null: new ShortArrayObject(object); }
	public static ArrayObject<String[]> 				wrap(String[] object) { return null == object ?null: new StringArrayObject(object); }
	public static <T extends Object> ArrayObject<T[]> 	wrap(T[] object) { return null == object ?null: new ObjectArrayObject<T> (object); }




	public static <T extends Object> ArrayObject<List<T>> 				wrap(List<T> object) 			{ return null == object ?null: new ListArrayObject<> (object); }
//	public static <T extends StringBuilder> ArrayObject<StringBuilder> 	wrap(StringBuilder object) 		{ return null == object ?null: new StringBuilderObject<> (object); }
//	public static <T extends CharSequence>  ArrayObject<CharSequence> 	wrap(CharSequence object) 		{ return null == object ?null: new CharSequenceObject<> (object); }


	public static ArrayObject 							wrap(Object object) {
		if (null  == object) { return null; }

		Class objectClass = object.getClass();
		if (objectClass.isArray()) {
			@SuppressWarnings("rawtypes")
			Class component = objectClass.getComponentType();
			if (component.isPrimitive()) {
				/**
				 * base opt
				 */
				if (object instanceof boolean[]) 	{ return wrap((boolean[])object); }
				if (object instanceof byte[]) 	 	{ return wrap((byte[])object); }
				if (object instanceof char[])   	{ return wrap((char[])object); }
				if (object instanceof double[]) 	{ return wrap((double[])object); }
				if (object instanceof float[])  	{ return wrap((float[])object); }
				if (object instanceof int[]) 	 	{ return wrap((int[])object);}
				if (object instanceof long[])   	{ return wrap((long[])object); }
				if (object instanceof short[])  	{ return wrap((short[])object); }
			} else {
				if (object instanceof String[]) 	{ return wrap((String[])object); }
				if (object instanceof Object[]) 	{ return wrap((Object[])object); }
			}
		} else {
			/**
			 * don't deal with
			 */
			if (object instanceof ArrayObject) { return (ArrayObject) object; }

			if (object instanceof AbstractList) 	{ return wrap((AbstractList) object); }
//			if (object instanceof StringBuilder) 	{ return wrap((StringBuilder) object); }
//			if (object instanceof CharSequence) 	{ return wrap((CharSequence) object); }
		}
		throw new ClassCastException(object.getClass().getName() + " cannot parsing");
	}


	public abstract boolean release();
}
