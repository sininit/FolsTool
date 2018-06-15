package top.fols.box.util;
import java.util.Random;

public class XRandom
{
	protected static Random getRandomIntRandom = new Random();
	public static int getRandomInt(int min , int max)
	{
		if (max == min)
			return min;
		if (min == 0)
			return getRandomIntRandom.nextInt(max + 1);
		return getRandomIntRandom.nextInt(max) % (max - min + 1) + min;
	}
}
