package top.fols.box.lang;

/**
 * Utility methods for packing/unpacking primitive values in/out of byte arrays
 * using little-endian byte ordering.
 */
import top.fols.box.annotation.BaseAnnotations;
import top.fols.box.lang.abstracts.XBitsOptionAbstract;

public class XBitsLittleEndian extends XBitsOptionAbstract {
	
	public static final int boolean_byte_length = 1;
	public static final int char_byte_length = 2;
	public static final int short_byte_length = 2;
	public static final int int_byte_length = 4;
	public static final int float_byte_length = 4;
	public static final int long_byte_length = 8;
	public static final int double_byte_length = 8;

	public static final int MIN_DATA_LENGTH = 1;
	public static final int MAX_DATA_LENGTH = 8;



	@Override
	public int boolean_byte_length() {
		return boolean_byte_length;
	}
	@Override
	public int char_byte_length() {
		return char_byte_length;
	}
	@Override
	public int short_byte_length() {
		return short_byte_length;
	}
	@Override
	public int int_byte_length() {
		return int_byte_length;
	}
	@Override
	public int float_byte_length() {
		return float_byte_length;
	}
	@Override
	public int long_byte_length() {
		return long_byte_length;
	}
	@Override
	public int double_byte_length() {
		return double_byte_length;
	}
	@Override
	public int MIN_DATA_LENGTH() {
		return MIN_DATA_LENGTH;
	}
	@Override
	public int MAX_DATA_LENGTH() {
		return MAX_DATA_LENGTH;
	}



	/*
	 * Methods for unpacking primitive values from byte arrays starting at given
	 * offsets.
	 */
	@BaseAnnotations("occupied byte length: 1")
	public boolean getBoolean(byte[] b, int off) {
		return b[off] != 0;
	}

	@BaseAnnotations("occupied byte length: 2")
	public char getChar(byte[] b, int off) {
		return (char) (
			(b[off] & 0xFF) + 
			(b[off + 1] << 8)
			);
	}

	@BaseAnnotations("occupied byte length: 2")
	public short getShort(byte[] b, int off) {
		return (short) (
			(b[off] & 0xFF) +
			(b[off + 1] << 8)
			);
	}

	@BaseAnnotations("occupied byte length: 4")
	public int getInt(byte[] b, int off) {
		return
			((b[off] & 0xFF)) +
			((b[off + 1] & 0xFF) << 8) + 
			((b[off + 2] & 0xFF) << 16) +
			((b[off + 3]) << 24);
	}

	@BaseAnnotations("occupied byte length: 4")
	public float getFloat(byte[] b, int off) {
		return Float.intBitsToFloat(getInt(b, off));
	}

	@BaseAnnotations("occupied byte length: 8")
	public long getLong(byte[] b, int off) {
		return
			((b[off] & 0xFFL)) +
			((b[off + 1] & 0xFFL) << 8) + 
			((b[off + 2] & 0xFFL) << 16) + 
			((b[off + 3] & 0xFFL) << 24) + 
			((b[off + 4] & 0xFFL) << 32) + 
			((b[off + 5] & 0xFFL) << 40) +
			((b[off + 6] & 0xFFL) << 48) +
			(((long) b[off + 7]) << 56);
//		return
//			(0xffL & 				(long)b[off]) +
//			(0xff00L & 				((long)b[off + 1] << 8)) +
//			(0xff0000L & 			((long)b[off + 2] << 16)) +
//			(0xff000000L & 			((long)b[off + 3] << 24)) +
//			(0xff00000000L & 		((long)b[off + 4] << 32)) +
//			(0xff0000000000L & 		((long)b[off + 5] << 40)) +
//			(0xff000000000000L & 	((long)b[off + 6] << 48)) +
//			(0xff00000000000000L & 	((long)b[off + 7] << 56));
	}

	@BaseAnnotations("occupied byte length: 8")
	public double getDouble(byte[] b, int off) {
		return Double.longBitsToDouble(getLong(b, off));
	}






	/*
	 * Methods for packing primitive values into byte arrays starting at given
	 * offsets.
	 */
	@BaseAnnotations("occupied byte length: 1")
	public void putBytes(byte[] b, int off, boolean val) {
		b[off] = (byte) (val ? 1 : 0);
	}
	@BaseAnnotations("occupied byte length: 1")
	public byte[] getBytes(boolean val) {
		byte[] bytes = new byte[boolean_byte_length];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@BaseAnnotations("occupied byte length: 2")
	public void putBytes(byte[] b, int off, char val) {
		b[off] = (byte) (val);
		b[off + 1] = (byte) (val >>> 8);
	}
	@BaseAnnotations("occupied byte length: 2")
	public byte[] getBytes(char val) {
		byte[] bytes = new byte[char_byte_length];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@BaseAnnotations("occupied byte length: 2")
	public void putBytes(byte[] b, int off, short val) {
		b[off] = (byte) (val);
		b[off + 1] = (byte) (val >>> 8);
	}
	@BaseAnnotations("occupied byte length: 2")
	public byte[] getBytes(short val) {
		byte[] bytes = new byte[short_byte_length];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@BaseAnnotations("occupied byte length: 4")
	public void putBytes(byte[] b, int off, int val) {
		b[off] = (byte) (val);
		b[off + 1] = (byte) (val >>> 8);
		b[off + 2] = (byte) (val >>> 16);
		b[off + 3] = (byte) (val >>> 24);
	}
	@BaseAnnotations("occupied byte length: 4")
	public byte[] getBytes(int val) {
		byte[] bytes = new byte[int_byte_length];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@BaseAnnotations("occupied byte length: 4")
	public void putBytes(byte[] b, int off, float val) {
		putBytes(b, off, Float.floatToIntBits(val));
	}
	@BaseAnnotations("occupied byte length: 4")
	public byte[] getBytes(float val) {
		byte[] bytes = new byte[float_byte_length];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@BaseAnnotations("occupied byte length: 8")
	public void putBytes(byte[] b, int off, long val) {
		b[off] = (byte) (val);
		b[off + 1] = (byte) (val >>> 8);
		b[off + 2] = (byte) (val >>> 16);
		b[off + 3] = (byte) (val >>> 24);
		b[off + 4] = (byte) (val >>> 32);
		b[off + 5] = (byte) (val >>> 40);
		b[off + 6] = (byte) (val >>> 48);
		b[off + 7] = (byte) (val >>> 56);
	}
	@BaseAnnotations("occupied byte length: 8")
	public byte[] getBytes(long val) {
		byte[] bytes = new byte[long_byte_length];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@BaseAnnotations("occupied byte length: 8")
	public void putBytes(byte[] b, int off, double val) {
		putBytes(b, off, Double.doubleToLongBits(val));
	}
	@BaseAnnotations("occupied byte length: 8")
	public byte[] getBytes(double val) {
		byte[] bytes = new byte[double_byte_length];
		putBytes(bytes, 0, val);
		return bytes;
	}




}
