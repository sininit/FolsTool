package top.fols.atri.time;

import top.fols.atri.interfaces.interfaces.ICallbackOneParam;

public class Times {
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }
    public static long nanoTime() {
        return System.nanoTime();
    }

    public static long currentCycleTimeMillis(long nowTime, long cycle) {
        return cycle * ((nowTime / cycle));
    }
    public static long currentCycleTimeMillis(long nowTime, long cycle, long sum) {
        return cycle * ((nowTime / cycle) + sum);
    }


    long current;
    public Times() {
        current = System.currentTimeMillis();
    }

    public long time()   { return currentTimeMillis(); }
    public long passed() { return currentTimeMillis() - this.current; }


    @Override
    public String toString() {
        // TODO: Implement this method
        return String.valueOf(time());
    }





    /**
     * Waits a given number of milliseconds (of uptimeMillis) before returning.
     * Similar to {@link java.lang.Thread#sleep(long)}, but does not throw
     * {@link InterruptedException}; {@link Thread#interrupt()} events are
     * deferred until the next interruptible operation.  Does not return until
     * at least the specified number of milliseconds has elapsed.
     *
     * @param ms to sleep before returning, in milliseconds of uptime.
     */
    public static void sleep(long ms) {
        long start = System.currentTimeMillis();
        long duration = ms;
        boolean interrupted = false;
        do {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                interrupted = true;
            }
            duration = start + ms - System.currentTimeMillis();
        } while (duration > 0);

        if (interrupted) {
            // Important: we don't want to quietly eat an interrupt() event,
            // so we make sure to re-interrupt the thread so that the next
            // call to Thread.sleep() or Object.wait() will be interrupted.
            Thread.currentThread().interrupt();
        }
    }










    public static Timeout timeout = new Timeout();
    public static Timeout.Control setTimeout(ICallbackOneParam<Timeout.Control> runnable, long millisecondsLater) {
        return timeout.setTimeout(runnable, millisecondsLater);
    }

    public static Timeout.Control setInterval(ICallbackOneParam<Timeout.Control> runnable, long millisecondsLater) {
        return timeout.setInterval(runnable, millisecondsLater);
    }

    public static final long TIME_1_MILLISECOND = 1L;// 一毫秒
    public static final long TIME_1_S = TIME_1_MILLISECOND * 1000L;// 一秒
    public static final long TIME_1_M = 60L * TIME_1_S;// 一分钟
    public static final long TIME_1_H = 60L * TIME_1_M;// 一小时
    public static final long TIME_1_D = 24L * TIME_1_H;// 一天


    public static long millis(long day, long hour, long minute, long second, long millis) {
        return day * TIME_1_D
                + hour * TIME_1_H
                + minute * TIME_1_M
                + second * TIME_1_S
                + millis * TIME_1_MILLISECOND;
    }

    public static long getNextSecondStartTime(long nowTime, long s) {
        return TIME_1_S * ((nowTime / TIME_1_S) + s);
    }
    public static long getNextMinuteStartTime(long nowTime, long m) {
        return TIME_1_M * ((nowTime / TIME_1_M) + m);
    }
    public static long getNextHourStartTime(long nowTime, long h) {
        return TIME_1_H * ((nowTime / TIME_1_H) + h);
    }
    public static long getNextSecondTime(long nowTime, long s) {
        return nowTime + TIME_1_S * s;
    }
    public static long getNextMinuteTime(long nowTime, long m) {
        return nowTime + TIME_1_M * m;
    }
    public static long getNextHourTime(long nowTime, long h) {
        return nowTime + TIME_1_H * h;
    }
    public static long getNextDayTime(long nowTime, long d) {
        return nowTime + TIME_1_D * d;
    }
}
