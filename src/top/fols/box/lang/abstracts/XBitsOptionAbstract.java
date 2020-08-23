package top.fols.box.lang.abstracts;

import top.fols.box.lang.XBitsBigEndian;
import top.fols.box.lang.XBitsLittleEndian;

public abstract class XBitsOptionAbstract {
    public abstract int boolean_byte_length();
    public abstract int char_byte_length();
    public abstract int short_byte_length();
    public abstract int int_byte_length();
    public abstract int float_byte_length();
    public abstract int long_byte_length();
    public abstract int double_byte_length();

    public abstract int MIN_DATA_LENGTH();
    public abstract int MAX_DATA_LENGTH();


    public abstract boolean getBoolean(byte[] b, int off);
    public abstract char getChar(byte[] b, int off);
    public abstract short getShort(byte[] b, int off);
    public abstract int getInt(byte[] b, int off);
    public abstract float getFloat(byte[] b, int off);
    public abstract long getLong(byte[] b, int off);
    public abstract double getDouble(byte[] b, int off);


    public abstract void putBytes(byte[] b, int off, boolean val);
    public abstract byte[] getBytes(boolean val);


    public abstract void putBytes(byte[] b, int off, char val);
    public abstract byte[] getBytes(char val);


    public abstract void putBytes(byte[] b, int off, short val);
    public abstract byte[] getBytes(short val);


    public abstract void putBytes(byte[] b, int off, int val);
    public abstract byte[] getBytes(int val);


    public abstract void putBytes(byte[] b, int off, float val);
    public abstract byte[] getBytes(float val);


    public abstract void putBytes(byte[] b, int off, long val);
    public abstract byte[] getBytes(long val);


    public abstract void putBytes(byte[] b, int off, double val);
    public abstract byte[] getBytes(double val);




    public static final XBitsBigEndian BIG_ENDIAN = new XBitsBigEndian();//java default
    public static final XBitsLittleEndian LITTLE_ENDIAN = new XBitsLittleEndian();



}
