package top.fols.box.util;
import java.math.BigDecimal;
import java.math.RoundingMode;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.time.XTimeTool;
public class XCycleSpeedLimiter
{
	protected static long currentTimeMillis()
	{
		return XTimeTool.currentTimeMillis();
	}
	private volatile long cycle;
	private volatile long cycleUpdateTime;
	private volatile long cyclemaxspeed;
	private volatile long cyclespeednow ;//周期时间内的速率
	private volatile boolean isLimit;
	private final Object lock = new Object();
	public XCycleSpeedLimiter()
	{
		this.cycle = XTimeTool.time_1s;
		this.cyclespeednow = 0;
		this.cyclemaxspeed = 8192 * 512;
		this.cycleUpdateTime = currentTimeMillis();
		this.isLimit = true;

		this.averageSpeedList = new long[3];
	}
	public int waitForFreeInt(@XAnnotations("max length is preferably waitfor free length multiples")int waiforFreeLength)
	{
		return (int)waitForFreeLong(waiforFreeLength);
	}
	public long waitForFreeLong(@XAnnotations("max length is preferably waitfor free length multiples")long waiforFreeLength)
	{
		if (waiforFreeLength > cyclemaxspeed)
			throw new NumberFormatException("pieceLength can't > cyclemaxspeed");
		synchronized (lock)
		{
			while (true)
			{
				if (!isLimit || cyclespeednow + waiforFreeLength <= cyclemaxspeed)
					break;
				else
					while (true)//waitForToNextCycle
					{
						if (!isLimit)
							break;
						long newTime = currentTimeMillis();
						if (newTime - averageSpeedBackNextUpdateTime >= XTimeTool.time_1s)
						{
							averageSpeedList[averageSpeedNowIndex++] = cyclespeednow;
							if (averageSpeedNowIndex >= averageSpeedList.length)
								averageSpeedNowIndex = 0;
							averageSpeedBackNextUpdateTime = newTime;
						}
						if (newTime - cycleUpdateTime >= cycle)
						{
							cycleUpdateTime = newTime;
							cyclespeednow = 0;
							break;
						}
					}
			}
			if (!isLimit)
			{
				long newTime = currentTimeMillis();
				if (newTime - averageSpeedBackNextUpdateTime >= XTimeTool.time_1s)
				{
					averageSpeedList[averageSpeedNowIndex++] = cyclespeednow;
					if (averageSpeedNowIndex >= averageSpeedList.length)
						averageSpeedNowIndex = 0;
					averageSpeedBackNextUpdateTime = newTime;
				}

				if (newTime - cycleUpdateTime >= cycle)
				{
					cycleUpdateTime = newTime;
					cyclespeednow = 0;
				}
			}
			cyclespeednow += waiforFreeLength;
			return waiforFreeLength;
		}
	}
	public long getCycleUseSpeed()
	{
		if (isCycleProcessEnd())
			return 0;
		return this.cyclespeednow;
	}
	public double getCycleUseSpeedEverySecondMax()
	{
		return ((double)XTimeTool.time_1s / (double)getCycle()) * (double)getCycleUseSpeed(); 
	}
	public long getCycleMaxSpeed()
	{
		return this.cyclemaxspeed;
	}
	public void setCycleMaxSpeed(long cycleMaxSpeed)throws NumberFormatException
	{
		if (cycleMaxSpeed < 1)
			throw new NumberFormatException("size error cycleMaxSpeed " + cycleMaxSpeed);
		this.cyclemaxspeed = cycleMaxSpeed;
		//this.cyclespeednow = 0;
		//this.backTime = currentTimeMillis();
	}	
	public void setCycle(long cycle)
	{
		if (cycle < 1)
			throw new NumberFormatException("size error cycle " + cycle);
		this.cycle = cycle;
		//this.cyclespeednow = 0;
		//this.backTime = currentTimeMillis();
	}
	public long getCycle()
	{
		return this.cycle;
	}
	public long getCycleFreeSpeed()
	{
		long length = getCycleMaxSpeed() - getCycleUseSpeed();
		return length > 0 ?length: 0;
	}
	public boolean setLimit(boolean b)
	{
		return this.isLimit = b;
	}
	public boolean isLimit()
	{
		return this.isLimit;
	}
	public String toString()
	{
		return String.format("[cycle=%s, cyclemaxspeed=%s, cyclespeednow=%s, islimit=%s]", getCycle(), getCycleMaxSpeed(), getCycleUseSpeed(), isLimit);
	}
	
	
	
	
	private boolean isCycleProcessEnd()
	{
		long newTime = currentTimeMillis();
		if (newTime - cycleUpdateTime >= cycle)
			return true;
		return false;
	}
	private volatile long averageSpeedBackNextUpdateTime = currentTimeMillis();
	private int averageSpeedNowIndex = 0;
	private long[] averageSpeedList;
	public double getEverySecondAverageSpeed()
	{
		if (isCycleProcessEnd())
			return 0;
		long forLength = averageSpeedNowIndex + 1;
		BigDecimal m = BigDecimal.ZERO;
		for (int i = 0;i < forLength;i++)
			m = m.add(new BigDecimal((double)averageSpeedList[i]));
		m = m.divide(new BigDecimal(forLength), 12, RoundingMode.CEILING);
		m = m.multiply(new BigDecimal(XTimeTool.time_1s).divide(new BigDecimal(getCycle()), 12, RoundingMode.CEILING));
		return m.doubleValue();
	}
}
