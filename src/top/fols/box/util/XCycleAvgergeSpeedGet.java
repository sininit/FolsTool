package top.fols.box.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import top.fols.box.time.XTimeTool;
import top.fols.box.util.XFixelArrayFill;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicBoolean;

public class XCycleAvgergeSpeedGet {
    private Object lock = new Object();

    private AtomicLong nowAccess;// 周期时间内的速率
    private AtomicBoolean stop;
    private long cycle;
    private AtomicLong lastAccessTime;

    public XCycleAvgergeSpeedGet() {
        this.nowAccess = new AtomicLong(0);
        this.stop = new AtomicBoolean(false);
    }

    public void access(long count) {
        this.stop.set(false);
        this.nowAccess.getAndAdd(count);
        long newTime = this.time();
        if (newTime - this.lastAccessTime.get() >= cycle) {
            synchronized (lock) {
                this.averageSpeedList.left(nowAccess.get());
                this.averageSpeedUpdateTimeList.left(newTime);
                this.lastAccessTime.getAndSet(newTime);
                this.nowAccess.set(0);
            }
        }
    }

    public long getCycle() {
        return cycle;
    }

    public XCycleAvgergeSpeedGet setCycle(long cycle) {
        this.cycle = cycle;
        return this;
    }

    private static long time() throws RuntimeException {
        long time = System.currentTimeMillis();
        if (time <= 0) {
            throw new RuntimeException("system time error");
        }
        return time;
    }


    private double lastCycleAverageSpeed = 0;
    private long lastCycleAverageSpeedGetTime = 0;
    private XFixelArrayFill<Long> averageSpeedList = new XFixelArrayFill<Long>(6);
    private XFixelArrayFill<Long> averageSpeedUpdateTimeList = new XFixelArrayFill<Long>(6);

    public double getAvgergeValue() {
        if (stop.get()) {
            return 0.0D;
        } else {
            long newTime = this.time();
            // 因为是周期速度 频繁的重新计算速度也无意义
            if (newTime - lastCycleAverageSpeedGetTime >= cycle) {
                BigDecimal m = BigDecimal.ZERO;
                // 3倍周期内的数据 / 除以3
                long timeRange;
                timeRange = newTime - (cycle * 3);

                int forLength = 0;
                for (int i = 0; i < averageSpeedUpdateTimeList.length(); i++) {
                    Long lastUpdate = averageSpeedUpdateTimeList.get(i);
                    if (null != lastUpdate && lastUpdate.longValue() > timeRange) {
                        m = m.add(new BigDecimal((double) averageSpeedList.get(i)));
                        ++forLength;
                    }
                }
                // System.out.println(averageSpeedList);
                // System.out.println(averageSpeedUpdateTimeList);
                if (forLength == 0) {
                    return 0;
                }
                m = m.divide(new BigDecimal(forLength), 12, RoundingMode.CEILING);
                lastCycleAverageSpeedGetTime = newTime;
                stop.set(true);
                return lastCycleAverageSpeed = m.doubleValue();
            } else {
                return lastCycleAverageSpeed;
            }
        }
    }

}
