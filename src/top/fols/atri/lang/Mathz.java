package top.fols.atri.lang;

@SuppressWarnings({"SpellCheckingInspection", "ManualMinMaxCalculation"})
public class Mathz {
	/**
     * Returns the tip of the {@code long} argument;
     * throwing an exception if the tip overflows an {@code int}.
     *
     * @param value the long tip
     * @return the argument as an int
     * @throws ArithmeticException if the {@code argument} overflows an int
     * @since 1.8 ...
     *
     */
    public static int toIntExact(long value) {
        if ((int)value != value) {
            throw new ArithmeticException("integer overflow");
        }
        return (int)value;
    }
    public static int toIntExact(double value) {
        if ((int)value != value) {
            throw new ArithmeticException("integer overflow");
        }
        return (int)value;
    }

    public static int  max(int a, int b) {
        return (a >= b) ? a : b;
    }
    public static long max(long a, long b) {
        return (a >= b) ? a : b;
    }
    public static int  min(int a, int b) {
        return (a <= b) ? a : b;
    }
    public static long min(long a, long b) {
        return (a <= b) ? a : b;
    }


    @SuppressWarnings("UseCompareMethod")
    public static int compareAsLeftMinToRightMax(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
    @SuppressWarnings("UseCompareMethod")
    public static int compareAsLeftMaxToRightMin(int x, int y) {
        return (x < y) ? 1 : ((x == y) ? 0 : -1);
    }
}
