package top.fols.box.util;

import top.fols.box.time.XTimeTool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicBoolean;

public class XCycleAvgergeSpeedGet {
    private AtomicLong nowAccess;// 周期时间内的速率
    private AtomicBoolean stop;
    private long cycle;
    private AtomicLong lastAccessTime;

    public XCycleAvgergeSpeedGet() {
        this.nowAccess = new AtomicLong(0);
        this.stop = new AtomicBoolean(false);
        this.lastAccessTime = new AtomicLong(0);
    }

    public void access(long count) {
        this.stop.set(false);
        this.nowAccess.getAndAdd(count);
        long newTime = this.time();
        if (newTime - this.lastAccessTime.get() >= cycle) {
            this.averageSpeedList.left(new Recording(this.nowAccess.get(), newTime));
            this.lastAccessTime.getAndSet(newTime);
            this.nowAccess.set(0);
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
        long time = XTimeTool.currentTimeMillis();
        return time;
    }


    private static class Recording {
        private long speed, time;
        public Recording(long speed, long time) {
            this.speed = speed;
            this.time = time;
        }
    }



    private double lastCycleAverageSpeed = 0;
    private long lastCycleAverageSpeedGetTime = 0;
    private XFixelArrayFill<Recording> averageSpeedList = new XFixelArrayFill<>(6);

    public synchronized double getAvgergeValue() {
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
                for (int i = 0; i < averageSpeedList.length(); i++) {
                    Recording recording = averageSpeedList.get(i);
                    if (null != recording && recording.time > timeRange) {
                        m = m.add(new BigDecimal((double) recording.speed));
                        ++forLength;
                    }
                }
                // System.out.println(averageSpeedList);
                // System.out.println(averageSpeedUpdateTimeList);
                if (forLength == 0) {
                    stop.set(true);
                    return 0;
                }
                m = m.divide(new BigDecimal(forLength), 12, RoundingMode.CEILING);
                lastCycleAverageSpeedGetTime = newTime;
                return lastCycleAverageSpeed = m.doubleValue();
            } else {
                return lastCycleAverageSpeed;
            }
        }
    }

}
