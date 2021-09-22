package top.fols.box.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import top.fols.box.time.XTimeTool;

public class XCycleAvgergeSpeedGet {
    private AtomicLong nowAccessCount = new AtomicLong(0);// 周期时间内的速率
    private AtomicLong lastCycleAccessTime = new AtomicLong(0);//上一个周期内最后访问时间

    private AtomicBoolean isIdle = new AtomicBoolean(false);

    private volatile long cycleTime = 0;//周期


    public XCycleAvgergeSpeedGet() {
        super();
    }

    public void access(long count) {
        this.isIdle.set(false);
        long nowAccessCount = this.nowAccessCount.addAndGet(count);
        long newTime = this.time();
        if (newTime - this.lastCycleAccessTime.get() >= cycleTime) {
            this.speeds.left(new Recording(nowAccessCount, newTime));
            this.nowAccessCount.set(0);
            this.lastCycleAccessTime.getAndSet(newTime);
        }
    }

    public long getCycle() {
        return cycleTime;
    }

    public XCycleAvgergeSpeedGet setCycle(long cycle) {
        this.cycleTime = cycle;
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
        @Override
        public String toString() {
            // TODO: Implement this method
            return String.format("speed=%s time=%s", speed, time);
        }
    }


    private AtomicLong lastCycleAverageSpeedGetTime = new AtomicLong(0);

    private volatile double lastCycleAverageSpeed = 0;
    private volatile FinalArray<Recording> speeds = new FinalArray<>(6);
    public double getAvgergeValue() {
        if (this.isIdle.get()) {
            return 0;
        } else {
            long newTime = this.time();
            // 因为是周期速度 频繁的重新计算速度也无意义
            if (newTime - this.lastCycleAverageSpeedGetTime.get() >= this.cycleTime) {
                BigDecimal all = BigDecimal.ZERO;
                // 3倍周期内的数据 / 除以3
                long timeRange = newTime - (this.cycleTime * 3);
                int forLength = 0;
                for (int i = 0; i < this.speeds.length(); i++) {
                    Recording recording = this.speeds.get(i);
                    if (null != recording && recording.time > timeRange) {
                        all = all.add(new BigDecimal((double) recording.speed));
                        ++forLength;
                    }
                }
                //System.out.println(this.speeds);
                if (!(newTime - this.lastCycleAverageSpeedGetTime.get() >= this.cycleTime)) {
                    return this.lastCycleAverageSpeed;
                }
                if (forLength == 0) {
                    this.isIdle.set(true);
                    return 0;
                }
                all = all.divide(new BigDecimal(forLength), 12, RoundingMode.CEILING);
                this.lastCycleAverageSpeedGetTime.set(newTime);
                return this.lastCycleAverageSpeed = all.doubleValue();
            } else {
                return this.lastCycleAverageSpeed;
            }
        }
    }

}