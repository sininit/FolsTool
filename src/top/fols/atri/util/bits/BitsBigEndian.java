package top.fols.atri.util.bits;

/**
 * Utility methods for packing/unpacking primitive values in/out of byte arrays
 * using big-endian byte ordering.
 *
 * java default using big-endian byte ordering.
 */
import top.fols.atri.interfaces.abstracts.BitsOptions;
import top.fols.atri.interfaces.annotations.Tips;

public class BitsBigEndian extends BitsOptions {

	public static final int BOOLEAN_BYTE_LENGTH = 1;
	public static final int CHAR_BYTE_LENGTH = 2;
	public static final int SHORT_BYTE_LENGTH = 2;
	public static final int INT_BYTE_LENGTH = 4;
	public static final int FLOAT_BYTE_LENGTH = 4;
	public static final int LONG_BYTE_LENGTH = 8;
	public static final int DOUBLE_BYTE_LENGTH = 8;

	public static final int MIN_DATA_LENGTH = 1;
	public static final int MAX_DATA_LENGTH = 8;

	/*
	 * Methods for unpacking primitive values from byte arrays starting at given
	 * offsets.
	 */
	@Tips("occupied byte length: 1")
	@Override
	public boolean getBoolean(byte[] b, int off) {
		return b[off] != 0;
	}

	@Tips("occupied byte length: 2")
	@Override
	public char getChar(byte[] b, int off) {
		return (char) (
			(b[off + 1] & 0xFF) + 
			(b[off] << 8)
			);
	}

	@Tips("occupied byte length: 2")
	@Override
	public short getShort(byte[] b, int off) {
		return (short) (
			(b[off + 1] & 0xFF) +
			(b[off] << 8)
			);
	}

	@Tips("occupied byte length: 4")
	@Override
	public int getInt(byte[] b, int off) {
		return
			((b[off + 3] & 0xFF)) +
			((b[off + 2] & 0xFF) << 8) + 
			((b[off + 1] & 0xFF) << 16) +
			((b[off]) << 24);
	}

	@Tips("occupied byte length: 4")
	@Override
	public float getFloat(byte[] b, int off) {
		return Float.intBitsToFloat(getInt(b, off));
	}

	@Tips("occupied byte length: 8")
	@Override
	public long getLong(byte[] b, int off) {
		return 
			((b[off + 7] & 0xFFL)) +
			((b[off + 6] & 0xFFL) << 8) + 
			((b[off + 5] & 0xFFL) << 16) + 
			((b[off + 4] & 0xFFL) << 24) + 
			((b[off + 3] & 0xFFL) << 32) + 
			((b[off + 2] & 0xFFL) << 40) +
			((b[off + 1] & 0xFFL) << 48) +
			(((long) b[off]) << 56);
	}

	@Tips("occupied byte length: 8")
	@Override
	public double getDouble(byte[] b, int off) {
		return Double.longBitsToDouble(getLong(b, off));
	}






	/*
	 * Methods for packing primitive values into byte arrays starting at given
	 * offsets.
	 */
	@Tips("occupied byte length: 1")
	@Override
	public void putBytes(byte[] b, int off, boolean val) {
		b[off] = (byte) (val ? 1 : 0);
	}
	@Tips("occupied byte length: 1")
	@Override
	public byte[] getBytes(boolean val) {
		byte[] bytes = new byte[BOOLEAN_BYTE_LENGTH];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@Tips("occupied byte length: 2")
	@Override
	public void putBytes(byte[] b, int off, char val) {
		b[off + 1] = (byte) (val);
		b[off] = (byte) (val >>> 8);
	}
	@Tips("occupied byte length: 2")
	@Override
	public byte[] getBytes(char val) {
		byte[] bytes = new byte[CHAR_BYTE_LENGTH];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@Tips("occupied byte length: 2")
	@Override
	public void putBytes(byte[] b, int off, short val) {
		b[off + 1] = (byte) (val);
		b[off] = (byte) (val >>> 8);
	}
	@Tips("occupied byte length: 2")
	@Override
	public byte[] getBytes(short val) {
		byte[] bytes = new byte[SHORT_BYTE_LENGTH];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@Tips("occupied byte length: 4")
	@Override
	public void putBytes(byte[] b, int off, int val) {
		b[off + 3] = (byte) (val);
		b[off + 2] = (byte) (val >>> 8);
		b[off + 1] = (byte) (val >>> 16);
		b[off] = (byte) (val >>> 24);
	}
	@Tips("occupied byte length: 4")
	@Override
	public byte[] getBytes(int val) {
		byte[] bytes = new byte[INT_BYTE_LENGTH];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@Tips("occupied byte length: 4")
	@Override
	public void putBytes(byte[] b, int off, float val) {
		putBytes(b, off, Float.floatToIntBits(val));
	}
	@Tips("occupied byte length: 4")
	@Override
	public byte[] getBytes(float val) {
		byte[] bytes = new byte[FLOAT_BYTE_LENGTH];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@Tips("occupied byte length: 8")
	@Override
	public void putBytes(byte[] b, int off, long val) {
		b[off + 7] = (byte) (val);
		b[off + 6] = (byte) (val >>> 8);
		b[off + 5] = (byte) (val >>> 16);
		b[off + 4] = (byte) (val >>> 24);
		b[off + 3] = (byte) (val >>> 32);
		b[off + 2] = (byte) (val >>> 40);
		b[off + 1] = (byte) (val >>> 48);
		b[off] = (byte) (val >>> 56);
	}
	@Tips("occupied byte length: 8")
	@Override
	public byte[] getBytes(long val) {
		byte[] bytes = new byte[LONG_BYTE_LENGTH];
		putBytes(bytes, 0, val);
		return bytes;
	}


	@Tips("occupied byte length: 8")
	@Override
	public void putBytes(byte[] b, int off, double val) {
		putBytes(b, off, Double.doubleToLongBits(val));
	}
	@Tips("occupied byte length: 8")
	@Override
	public byte[] getBytes(double val) {
		byte[] bytes = new byte[DOUBLE_BYTE_LENGTH];
		putBytes(bytes, 0, val);
		return bytes;
	}




}
