package top.fols.box.lang;
public abstract class XMath {
	
	//获取从左到右数第几位数
	public static long getBit(long num, long bit) {
		num = num < 0 ? -num : num;
		if 		(bit == 1)	return (num % 10L) / 1L;
		else if (bit == 2)	return (num % 100L) / 10L;
		else if (bit == 3)	return (num % 1000L) / 100L;
		else if (bit == 4)	return (num % 10000L) / 1000L;
		else if (bit == 5)	return (num % 100000L) / 10000L;
		else if (bit == 6)	return (num % 1000000L) / 100000L;
		else if (bit == 7)	return (num % 10000000L) / 1000000L;
		else if (bit == 8)	return (num % 100000000L) / 10000000L;
		else if (bit == 9)	return (num % 1000000000L) / 100000000L;
		else if (bit == 10)	return (num % 10000000000L) / 1000000000L;
		else if (bit == 11)	return (num % 100000000000L) / 10000000000L;
		else if (bit == 12)	return (num % 1000000000000L) / 100000000000L;
		else if (bit == 13)	return (num % 10000000000000L) / 1000000000000L;
		else if (bit == 14)	return (num % 100000000000000L) / 10000000000000L;
		else if (bit == 15)	return (num % 1000000000000000L) / 100000000000000L;
		else if (bit == 16)	return (num % 10000000000000000L) / 1000000000000000L;
		else if (bit == 17)	return (num % 100000000000000000L) / 10000000000000000L;
		else if (bit == 18)	return (num % 1000000000000000000L) / 100000000000000000L;
		else 				return (num / 1000000000000000000L);
	}
	public static int getNumLength(long integer) {
		int len = 1;
		long newNum;
		for (;(newNum = integer / 10) != 0;integer = newNum)
			len++;
		return len;
	}
	
	
	
	//整数反转
	public static long reverse(long x) {
        long result = 0;
        while (x != 0) {
            long mod = x % 10;
            x = x / 10;  
            if (result > Long.MAX_VALUE / 10 || result < Long.MIN_VALUE / 10)
                return 0;
            result = result * 10 + mod;  
        }
        return result;
    }
	public static int reverse(int x) {
        int result = 0;
        while (x != 0) {
            int mod = x % 10;
            x = x / 10;  
            if (result > Integer.MAX_VALUE / 10 || result < Integer.MIN_VALUE / 10)
                return 0;
            result = result * 10 + mod;  
        }
        return result;
    }
}
