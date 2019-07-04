package top.fols.box.util;
import java.util.Random;

public class XRandom {
	public static int getRandomInt(int min , int max) {
	    return (int)nextLong(ints, min, max);
	}
    public static boolean getRandomBoolean() {
		return nextLong(booleans, 0, 1) == 1;
	}
	
	
	
	private static final long KEEP_63_BITS = 0b0111111111111111111111111111111111111111111111111111111111111111L;
	private static final Random ints = new Random();
	private static final Random booleans = new Random();
	private static long nextLong0(Random r, long bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive.");
        }
        while (true) {
            long sample = r.nextLong() & KEEP_63_BITS;  // clear sign bit to make positive
            long result = sample % bound;
            if (sample - result + bound - 1 > 0) {  // confirm sample is from a complete bucket
                return result;
            }
        }
    }
	private static long nextLong(Random r, long least, long bound) {
		if (least == bound)
			return bound;
        if (least < 0 ? (bound * -1 > least * -1): (least > bound))
            throw new IllegalArgumentException(String.format("min=%s, max=%s", least, bound));
        return nextLong0(r, bound - least + 1) + least;
    }
}
