package top.fols.box.lang;

import top.fols.box.lang.impl.sequences.XIntSequenceImpl;
import top.fols.box.lang.impl.sequences.XLongSequenceImpl;
import top.fols.box.time.XTiming;
import top.fols.box.util.XArray;
import top.fols.box.util.XArrays;
import java.util.Arrays;

public class XSequencesTest {
    public static void main(String[] args) throws Throwable{
        System.out.println(XSequences.deepIndexOf(XSequences.wrapArr(new int[] { 1, 1, 2, 3 }),
                XSequences.wrapArr(new int[] { 1 }), 1, 2));
        System.out.println(XSequences.deepLastIndexOf(XSequences.wrapArr(new int[] { 1, 1, 2, 3 }),
                XSequences.wrapArr(new int[] { 1, 1 }), 1, 0));

        System.out.println(XSequences.deepIndexOf(XSequences.wrapArr(new int[] { 1, 1, 2, 3 }),
                XSequences.wrapArr(new int[] { 1 }), 0, 2));
        System.out.println(XSequences.deepLastIndexOf(XSequences.wrapArr(new int[] { 1, 1, 2, 3 }),
                XSequences.wrapArr(new int[] { 1, 1 }), 0, 0));
        System.out.println("_____");

        String[] testStrArr = new String[1];
        XArrays.arraycopyTraverse(new Object[] { 8 }, 0, testStrArr, 0, 1);
        System.out.println(Arrays.toString(testStrArr));

        XArray.copyOfConversion(new int[] { 1, 2, 3, 4, 5 }, new long[] {});

        System.out.println(XArrays.equals(new int[] { 4 }, new int[] { 4 }));
        System.out.println(XArrays.equalsRange(new int[] { 4, 5, 7, 9, 10 }, 2, new int[] { 4, 7, 9 }, 1, 2));
        System.out.println(XArrays.deepEqualsRange(new Object[] { new int[] { 4, 7, 9 }, "a", "b" }, 0,
                new Object[] { new int[] { 4, 7, 9 } }, 0, 1));
        System.out.println("_____");

        XTiming ff0 = XTiming.newAndStart();

        System.out.println(XSequences.deepIndexOf(
                XSequences.wrapArr(new Object[] { 0, new Object[] { null, new int[] { 8 } }, new int[] { 123 },
                        new Object[][] { { null, 6 } }, "cxk", 5 }),
                XSequences.wrapArr(new Object[] { new Object[] { null, new int[] { 8 } }, new int[] { 123 } }), 0,
                6));
        System.out.println(XSequences.deepLastIndexOf(
                XSequences.wrapArr(new Object[] { 0, new Object[] { null, new int[] { 8 } }, new int[] { 123 },
                        new Object[][] { { null, 6 } }, "cxk", 5 }),
                XSequences.wrapArr(new Object[] { new Object[] { null, new int[] { 8 } }, new int[] { 123 } }), 6,
                0));

        System.out.println(XSequences.deepIndexOf(XSequences.wrapArr(new int[] { 1, 2, 3 }),
                XSequences.wrapArr(new long[] { 1, 2, 3 }), 0, 3));
        System.out.println(XSequences.deepIndexOf(XSequences.wrapArr(new int[] { 1, 2, 3 }),
                XSequences.wrapArr(new int[] { 1, 2, 3 }), 0, 3));
        System.out.println("&" + ff0.endAndGetEndLessStart());
        System.out.println();

        int testlength = 1000000;
        int[] testObjArr = (int[]) XArray.newInstanceFill(int.class, testlength, 0);
        long[] testLongArr = new long[testlength];

        XTiming ff1 = XTiming.newAndStart();
        XArrays.arraycopyTraverse(testLongArr, 0, testObjArr, 0, testObjArr.length);
        System.out.println("&" + ff1.endAndGetEndLessStart());

        XTiming ff2 = XTiming.newAndStart();
        XIntSequenceImpl testObjArrS = new XIntSequenceImpl(testObjArr);
        XLongSequenceImpl testLongArrS = new XLongSequenceImpl(testLongArr);
        XArrays.arraycopyTraverse(testObjArrS.getArray(), 0, testLongArrS.getArray(), 0, testObjArrS.length());
        System.out.println("&" + ff2.endAndGetEndLessStart());
        testLongArrS = null;
        testObjArrS = null;

    }
}