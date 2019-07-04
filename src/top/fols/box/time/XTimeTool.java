package top.fols.box.time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
public final class XTimeTool {

	public static final int getTimeZoneRawOffSet() {
		return defaultTimeZoneRawOffset;
	}
	public static final long currentTimeMillis() {
		return System.currentTimeMillis();
	}


	public final static int time_1millisecond = 1;
	public final static int time_1s = 1 * 1000;
	public final static int time_1m = 60 * 1000;//一分钟
	public final static int time_1h = 60 * 60 * 1000;//一小时
	public final static int time_1d = 24 * 60 * 60 * 1000;//一天
	public final static int defaultTimeZoneRawOffset = TimeZone.getDefault().getRawOffset();
	
	
	
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
