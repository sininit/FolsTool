package top.fols.box.time;
import top.fols.box.annotation.XAnnotations;

@XAnnotations("one thread will always run get time")
public final class XTimeTool
{
	public static final long currentTimeMillis()
	{
		return System.currentTimeMillis();
	}
	
	public final static int time_1millisecond = 1;
	public final static int time_1s = 1000;
	public final static int time_1m = 60000;//一分钟
	public final static int time_1h = 3600000;//一小时
	public final static int time_1d = 86400000;//一天
	


	public static long getNextSecondStartTime(long nowTime,long s)
	{
		return time_1s * ((nowTime / time_1s) + s);
	}
	public static long getNextMinuteStartTime(long nowTime,long m)
	{
		return time_1m * ((nowTime / time_1m) + m);
	}
	public static long getNextHourStartTime(long nowTime,long h)
	{
		return time_1h * ((nowTime / time_1h) + h);
	}
	public static long getNextDayStartTime(long nowTime,long d)
	{
		return time_1d * ((nowTime / time_1d) + d) - 8 * time_1h;
	}




	public static long getNextSecondTime(long nowTime,long s)
	{
		return nowTime + time_1s * s;
	}
	public static long getNextMinuteTime(long nowTime,long m)
	{
		return nowTime + time_1m * m;
	}
	public static long getNextHourTime(long nowTime,long h)
	{
		return nowTime + time_1h * h;
	}
	public static long getNextDayTime(long nowTime,long d)
	{
		return nowTime + time_1d * d;
	}



}
