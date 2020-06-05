package top.fols.box.time;

import java.util.TimeZone;

public final class XTimeTool {

	public static final int getTimeZoneRawOffSet() {
		return defaultTimeZoneRawOffset;
	}

	public static final long currentTimeMillis() {
		return System.currentTimeMillis();
	}

	public static final long nanoTime() {
		return System.nanoTime();
	}

	public static final int time_1millisecond = 1;
	public static final int time_1s = time_1millisecond * 1000;
	public static final int time_1m = 60 * time_1s;// 一分钟
	public static final int time_1h = 60 * time_1m;// 一小时
	public static final int time_1d = 24 * time_1h;// 一天
	public static final int defaultTimeZoneRawOffset = TimeZone.getDefault().getRawOffset();

	public static long getNextSecondStartTime(long nowTime, long s) {
		return time_1s * ((nowTime / time_1s) + s);
	}

	public static long getNextMinuteStartTime(long nowTime, long m) {
		return time_1m * ((nowTime / time_1m) + m);
	}

	public static long getNextHourStartTime(long nowTime, long h) {
		return time_1h * ((nowTime / time_1h) + h);
	}

	public static long getNextSecondTime(long nowTime, long s) {
		return nowTime + time_1s * s;
	}

	public static long getNextMinuteTime(long nowTime, long m) {
		return nowTime + time_1m * m;
	}

	public static long getNextHourTime(long nowTime, long h) {
		return nowTime + time_1h * h;
	}

	public static long getNextDayTime(long nowTime, long d) {
		return nowTime + time_1d * d;
	}

}
