package top.fols.box.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import top.fols.box.time.XTimeTool;

/*
 * 计算周期速度 
 * setPerCycleSize 设置每个周期大小 
 * getAvgergeCycleSpeed 所有周期的平均速度 
 * 
 * 比如周期大小为3秒 上一个3秒周期速度:1000 这个3秒周期速度:2000 那么平均周期速度为1500
 */
public class XAvgergeCycleSpeedCalc {
	private volatile long cyclespeednow;// 周期时间内的速率
	private Object lock = new Object();

	public XAvgergeCycleSpeedCalc() {
		this.cyclespeednow = 0;
	}

	public void addForFreeLong(long freeLength) {
		this.isStop = false;

		synchronized (lock) {
			cyclespeednow += freeLength;
			long newTime = XTimeTool.currentTimeMillis();
			if (newTime - averageSpeedBackNextUpdateTime >= cyclesize) {
				averageSpeedList.left(cyclespeednow);
				averageSpeedUpdateTimeList.left(newTime);
				averageSpeedBackNextUpdateTime = newTime;
				cyclespeednow = 0;
			}
		}
	}

	public long getPerCycleSize() {
		return cyclesize;
	}

	public void setPerCycleSize(long cycle) {
		this.cyclesize = cycle;
	}

	private long cyclesize;
	private double lastCycleAverageSpeed = 0;
	private long lastCycleAverageSpeedGetTime = 0;
	private XFixelArrayFill<Long> averageSpeedList = new XFixelArrayFill<Long>(6);
	private XFixelArrayFill<Long> averageSpeedUpdateTimeList = new XFixelArrayFill<Long>(6);
	private long averageSpeedBackNextUpdateTime;
	private boolean isStop = false;

	public double getAvgergeCycleSpeed() {
		if (isStop) {
			return 0.0D;
		} else {
			long newTime = XTimeTool.currentTimeMillis();
			// 因为是周期速度 频繁的重新计算速度也无意义
			if (newTime - lastCycleAverageSpeedGetTime >= cyclesize) {
				BigDecimal m = BigDecimal.ZERO;
				// 3倍周期内的数据 / 除以3
				long timeRange;
				timeRange = newTime - (cyclesize * 3);

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
				isStop = true;
				return lastCycleAverageSpeed = m.doubleValue();
			} else {
				return lastCycleAverageSpeed;
			}
		}
	}
}
