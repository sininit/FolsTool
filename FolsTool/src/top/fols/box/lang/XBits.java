package top.fols.box.lang;
import top.fols.box.annotation.XAnnotations;
public class XBits
{

    /*
     * Methods for unpacking primitive values from byte arrays starting at
     * given offsets.
     */
	@XAnnotations("need length 1")
	public static boolean getBoolean(byte[] b, int off)
	{
        return b[off] != 0;
    }
	@XAnnotations("need length 2")
	public static char getChar(byte[] b, int off)
	{
        return (char) ((b[off + 1] & 0xFF) +
			(b[off] << 8));
    }
	@XAnnotations("need length 2")
	public static short getShort(byte[] b, int off)
	{
        return (short) ((b[off + 1] & 0xFF) +
			(b[off] << 8));
    }
	@XAnnotations("need length 4")
	public static int getInt(byte[] b, int off)
	{
        return ((b[off + 3] & 0xFF)) +
			((b[off + 2] & 0xFF) <<  8) +
			((b[off + 1] & 0xFF) << 16) +
			((b[off]) << 24);
    }
	@XAnnotations("need length 4")
	public static float getFloat(byte[] b, int off)
	{
        return Float.intBitsToFloat(getInt(b, off));
    }
	@XAnnotations("need length 8")
	public static long getLong(byte[] b, int off)
	{
        return ((b[off + 7] & 0xFFL)) +
			((b[off + 6] & 0xFFL) <<  8) +
			((b[off + 5] & 0xFFL) << 16) +
			((b[off + 4] & 0xFFL) << 24) +
			((b[off + 3] & 0xFFL) << 32) +
			((b[off + 2] & 0xFFL) << 40) +
			((b[off + 1] & 0xFFL) << 48) +
			(((long) b[off])      << 56);
    }
	@XAnnotations("need length 8")
	public static double getDouble(byte[] b, int off)
	{
        return Double.longBitsToDouble(getLong(b, off));
    }

    /*
     * Methods for packing primitive values into byte arrays starting at given
     * offsets.
     */
	@XAnnotations("need length 1")
	public static void putBoolean(byte[] b, int off, boolean val)
	{
        b[off] = (byte) (val ? 1 : 0);
    }
	@XAnnotations("need length 2")
	public static void putChar(byte[] b, int off, char val)
	{
        b[off + 1] = (byte) (val);
        b[off] = (byte) (val >>> 8);
    }
	@XAnnotations("need length 2")
	public static void putShort(byte[] b, int off, short val)
	{
        b[off + 1] = (byte) (val);
        b[off] = (byte) (val >>> 8);
    }
	@XAnnotations("need length 4")
	public static void putInt(byte[] b, int off, int val)
	{
        b[off + 3] = (byte) (val);
        b[off + 2] = (byte) (val >>>  8);
        b[off + 1] = (byte) (val >>> 16);
        b[off] = (byte) (val >>> 24);
    }
	@XAnnotations("need length 4")
	public static void putFloat(byte[] b, int off, float val)
	{
        putInt(b, off,  Float.floatToIntBits(val));
    }
	@XAnnotations("need length 8")
	public static void putLong(byte[] b, int off, long val)
	{
        b[off + 7] = (byte) (val);
        b[off + 6] = (byte) (val >>>  8);
        b[off + 5] = (byte) (val >>> 16);
        b[off + 4] = (byte) (val >>> 24);
        b[off + 3] = (byte) (val >>> 32);
        b[off + 2] = (byte) (val >>> 40);
        b[off + 1] = (byte) (val >>> 48);
        b[off] = (byte) (val >>> 56);
    }
	@XAnnotations("need length 8")
	public static void putDouble(byte[] b, int off, double val)
	{
        putLong(b, off, Double.doubleToLongBits(val));
    }
}

