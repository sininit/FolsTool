package top.fols.box.util;

import java.util.Random;

public class XRandom {

    public static final XRandom defaultInstance = new XRandom();

    public static int getRandomInt(int min, int max) {
        return XRandom.defaultInstance.randomInt(min, max);
    }

    public static long getRandomLong(long min, long max) {
        return XRandom.defaultInstance.randomLong(min, max);
    }

    public static boolean getRandomBoolean() {
        return XRandom.defaultInstance.randomBoolean();
    }

    /*
     * GET RANDOM VALUE
     *
     */
    private final Random RANDOM;

    public XRandom() {
        this(new Random());
    }

    public XRandom(Random random) {
        this.RANDOM = random;
    }

    public Random getRandomObject() {
        return this.RANDOM;
    }

    /*
     * nextLong(bound)
     */

    private static final long KEEP_63_BITS = 0b0111111111111111111111111111111111111111111111111111111111111111L;

    public static long nextLong(Random randomInstance, long bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive.");
        }
        while (true) {
            long sample = randomInstance.nextLong() & KEEP_63_BITS; // clear sign bit to make positive
            long result = sample % bound;
            if (sample - result + bound - 1 > 0) { // confirm sample is from a complete bucket
                return result;
            }
        }
    }

    /*
     *
     *
     *
     *
     * GET RANDOM INTEGER VALUE
     * 
     *
     *
     */
    private static int randomInt0(Random randomInstance, int minVal, int maxVal) {
        long min = minVal, max = maxVal;
        if (min == max) {
            return maxVal;
        }
        if (min < 0 ? (max * -1 > min * -1) : (min > max)) {
            throw new IllegalArgumentException(String.format("min=%s, max=%s", min, max));
        }
        return (int) (min + nextLong(randomInstance, max - min + 1));
    }

    public int randomInt(int min, int max) {
        return XRandom.randomInt0(this.RANDOM, min, max);
    }

    /*
     *
     *
     *
     *
     * GET RANDOM LONG VALUE
     * 
     *
     *
     */
    private static long randomLong0(Random randomInstance, long min, long max) {
        if (min == max) {
            return max;
        }
        // if (min < 0 ? (max * -1 > min * -1) : (min > max)) {
        // throw new IllegalArgumentException(String.format("min=%s, max=%s", min,
        // max));
        // }
        return min + (long) ((max - min) * randomInstance.nextDouble()) + (randomInstance.nextBoolean() ? 1 : 0);
    }

    /*
     * 利用Java机制来实现 任意范围随机long 实际上就是多个取随机数...
     */
    public long randomLong(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException(String.format("min=%s, max=%s", min, max));
        }
        long cn = max - min;
        long buf1, buf2, buf3;
        if (cn < 0) {
            /*
             * @max - @min > Long.MAX_VALUE
             * 
             * @cn the probability of getting this value becomes very low
             */
            if (cn - Long.MAX_VALUE >= 0) {
                buf1 = Long.MAX_VALUE;
                buf2 = 0;
                buf3 = cn - Long.MAX_VALUE;
            } else if (cn - Long.MAX_VALUE - Long.MAX_VALUE >= 0) {
                buf1 = Long.MAX_VALUE;
                buf2 = Long.MAX_VALUE;
                buf3 = cn - Long.MAX_VALUE - Long.MAX_VALUE;
            } else {
                throw new ArithmeticException("i don't know why will be wrong... " + "min=" + min + ", max=" + max);
            }
            // System.out.println(String.format("buf1=%s,buf2=%s,buf3=%s",buf1,buf2,buf3));
            return min + XRandom.randomLong0(this.RANDOM, 0, buf1) + XRandom.randomLong0(this.RANDOM, 0, buf2)
                    + XRandom.randomLong0(this.RANDOM, 0, buf3);
        } else {
            return min + XRandom.randomLong0(this.RANDOM, 0, cn);
        }
    }

    /*
     *
     *
     *
     *
     * GET RANDOM BOOLEAN VALUE
     * 
     *
     *
     */
    public boolean randomBoolean() {
        return this.RANDOM.nextBoolean();
    }
}
