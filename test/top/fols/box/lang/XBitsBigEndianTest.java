package top.fols.box.lang;

import top.fols.box.lang.abstracts.XBitsOptionAbstract;

import java.util.Arrays;

public class XBitsBigEndianTest {
    public static void main(String[] args) {
        XBitsOptionAbstract be = new XBitsBigEndian();
        System.out.println(be.getBoolean(be.getBytes(true), 0));
        System.out.println(be.getChar(be.getBytes((char)10086), 0) == (char)10086);
        System.out.println(be.getShort(be.getBytes((short)18525), 0) == (short)18525);
        System.out.println(be.getInt(be.getBytes(Integer.MAX_VALUE), 0) == (int)Integer.MAX_VALUE);
        System.out.println(be.getLong(be.getBytes((long)77777), 0) == (long)77777);
        System.out.println(be.getFloat(be.getBytes((float)10086.521), 0) == (float)10086.521);
        System.out.println(be.getDouble(be.getBytes((double)(5566355D + 0.4)), 0));
        System.out.println(Arrays.toString(XBitsOptionAbstract.BIG_ENDIAN.getBytes(666D)));
    }
}