package top.fols.atri.interfaces.abstracts;

import top.fols.atri.util.bits.BitsBigEndian;
import top.fols.atri.util.bits.BitsLittleEndian;

public abstract class BitsOptions {
    public abstract boolean getBoolean(byte[] b, int off);
    public abstract char    getChar(byte[] b, int off);
    public abstract short   getShort(byte[] b, int off);
    public abstract int     getInt(byte[] b, int off);
    public abstract float   getFloat(byte[] b, int off);
    public abstract long    getLong(byte[] b, int off);
    public abstract double  getDouble(byte[] b, int off);


    public abstract void    putBytes(byte[] b, int off, boolean val);
    public abstract byte[]  getBytes(boolean val);


    public abstract void    putBytes(byte[] b, int off, char val);
    public abstract byte[]  getBytes(char val);


    public abstract void    putBytes(byte[] b, int off, short val);
    public abstract byte[]  getBytes(short val);


    public abstract void    putBytes(byte[] b, int off, int val);
    public abstract byte[]  getBytes(int val);


    public abstract void    putBytes(byte[] b, int off, float val);
    public abstract byte[]  getBytes(float val);


    public abstract void    putBytes(byte[] b, int off, long val);
    public abstract byte[]  getBytes(long val);


    public abstract void    putBytes(byte[] b, int off, double val);
    public abstract byte[]  getBytes(double val);


    /**
     * java is big endian
     */
    public static final BitsBigEndian    BIG_ENDIAN     = new BitsBigEndian();//java default
    public static final BitsLittleEndian LITTLE_ENDIAN  = new BitsLittleEndian();

}
