package top.fols.box.util;

import java.util.Arrays;

public class XArraysUtils
{
	public static boolean arrayContentsEquals(Object[] a1, Object[] a2)
	{
        if (a1 == null)
            return a2 == null || a2.length == 0;
        if (a2 == null)
            return a1.length == 0;
        if (a1.length != a2.length)
            return false;
        for (int i = 0; i < a1.length; i++)
            if (a1[i] != a2[i])
                return false;
        return true;
    }
	



	/*
	 array type:

	 Object[]
	 byte[]
	 long[]
	 double[]
	 char[]
	 int[]
	 boolean[]
	 float[]
	 short[]
	 //void[]
	 */


	public static Object[] trim(Object[] b, Object[] starttrim, Object[] endtrim)
	{
		if (b == null)
			throw new NullPointerException();

		int start = 0;
		int end = b.length;
		if (starttrim != null && starttrim.length != 0)
			for (int i = 0;i < b.length;i++)
			{
				boolean b2 = false;
				if (b[i] == null)
				{
					for (int i2 = 0;i2 < starttrim.length;i2++)
					{
						if (starttrim[i2] == null)
						{
							b2 = true;
							break;
						}
					}
				}
				else
				{
					for (int i2 = 0;i2 < starttrim.length;i2++)
					{
						if  (b[i].equals(starttrim[i2]))
						{
							b2 = true;
							break;
						}
					}
				}
				if (b2)
					start++;
				else
					break;
			}
		if (endtrim != null && endtrim.length != 0)
			for (int i = b.length - 1 ;i >= 0;i--)
			{
				if (end <= start)
					break;
				boolean b2 = false;
				if (b[i] == null)
				{
					for (int i2 = 0;i2 < endtrim.length;i2++)
					{
						if (endtrim[i2] == null)
						{
							b2 = true;
							break;
						}
					}
				}
				else
				{
					for (int i2 = 0;i2 < endtrim.length;i2++)
					{
						if  (b[i].equals(endtrim[i2]))
						{
							b2 = true;
							break;
						}
					}
				}
				if (b2)
					end--;
				else
					break;
			}
		if (start == 0 && end == b.length)
			return b;
		return Arrays.copyOfRange(b, start, end);

	}

	public static byte[] trim(byte[] b, byte[] starttrim, byte[] endtrim)
	{
		if (b == null)
			throw new NullPointerException();

		int start = 0;
		int end = b.length;
		if (starttrim != null && starttrim.length != 0)
			for (int i = 0;i < b.length;i++)
			{
				boolean b2 = false;
				for (int i2 = 0;i2 < starttrim.length;i2++)
					if (b[i] == starttrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					start++;
				else
					break;
			}
		if (endtrim != null && endtrim.length != 0)
			for (int i = b.length - 1 ;i >= 0;i--)
			{
				if (end <= start)
					break;
				boolean b2 = false;
				for (int i2 = 0;i2 < endtrim.length;i2++)
					if (b[i] == endtrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					end--;
				else
					break;
			}
		if (start == 0 && end == b.length)
			return b;
		return Arrays.copyOfRange(b, start, end);

	}
	public static long[] trim(long[] b, long [] starttrim, long[] endtrim)
	{
		if (b == null)
			throw new NullPointerException();

		int start = 0;
		int end = b.length;
		if (starttrim != null && starttrim.length != 0)
			for (int i = 0;i < b.length;i++)
			{
				boolean b2 = false;
				for (int i2 = 0;i2 < starttrim.length;i2++)
					if (b[i] == starttrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					start++;
				else
					break;
			}
		if (endtrim != null && endtrim.length != 0)
			for (int i = b.length - 1 ;i >= 0;i--)
			{
				if (end <= start)
					break;
				boolean b2 = false;
				for (int i2 = 0;i2 < endtrim.length;i2++)
					if (b[i] == endtrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					end--;
				else
					break;
			}
		if (start == 0 && end == b.length)
			return b;
		return Arrays.copyOfRange(b, start, end);

	}

	public static double[] trim(double[] b, double [] starttrim, double[] endtrim)
	{
		if (b == null)
			throw new NullPointerException();

		int start = 0;
		int end = b.length;
		if (starttrim != null && starttrim.length != 0)
			for (int i = 0;i < b.length;i++)
			{
				boolean b2 = false;
				for (int i2 = 0;i2 < starttrim.length;i2++)
					if (b[i] == starttrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					start++;
				else
					break;
			}
		if (endtrim != null && endtrim.length != 0)
			for (int i = b.length - 1 ;i >= 0;i--)
			{
				if (end <= start)
					break;
				boolean b2 = false;
				for (int i2 = 0;i2 < endtrim.length;i2++)
					if (b[i] == endtrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					end--;
				else
					break;
			}
		if (start == 0 && end == b.length)
			return b;
		return Arrays.copyOfRange(b, start, end);

	}

	public static char[] trim(char[] b, char[] starttrim, char[] endtrim)
	{
		if (b == null)
			throw new NullPointerException();

		int start = 0;
		int end = b.length;
		if (starttrim != null && starttrim.length != 0)
			for (int i = 0;i < b.length;i++)
			{
				boolean b2 = false;
				for (int i2 = 0;i2 < starttrim.length;i2++)
					if (b[i] == starttrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					start++;
				else
					break;
			}
		if (endtrim != null && endtrim.length != 0)
			for (int i = b.length - 1 ;i >= 0;i--)
			{
				if (end <= start)
					break;
				boolean b2 = false;
				for (int i2 = 0;i2 < endtrim.length;i2++)
					if (b[i] == endtrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					end--;
				else
					break;
			}
		if (start == 0 && end == b.length)
			return b;
		return Arrays.copyOfRange(b, start, end);

	}
	public static int[] trim(int[] b, int[] starttrim, int[] endtrim)
	{
		if (b == null)
			throw new NullPointerException();

		int start = 0;
		int end = b.length;
		if (starttrim != null && starttrim.length != 0)
			for (int i = 0;i < b.length;i++)
			{
				boolean b2 = false;
				for (int i2 = 0;i2 < starttrim.length;i2++)
					if (b[i] == starttrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					start++;
				else
					break;
			}
		if (endtrim != null && endtrim.length != 0)
			for (int i = b.length - 1 ;i >= 0;i--)
			{
				if (end <= start)
					break;
				boolean b2 = false;
				for (int i2 = 0;i2 < endtrim.length;i2++)
					if (b[i] == endtrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					end--;
				else
					break;
			}
		if (start == 0 && end == b.length)
			return b;
		return Arrays.copyOfRange(b, start, end);

	}
	public static boolean[] trim(boolean[] b, boolean[] starttrim, boolean[] endtrim)
	{
		if (b == null)
			throw new NullPointerException();

		int start = 0;
		int end = b.length;
		if (starttrim != null && starttrim.length != 0)
			for (int i = 0;i < b.length;i++)
			{
				boolean b2 = false;
				for (int i2 = 0;i2 < starttrim.length;i2++)
					if (b[i] == starttrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					start++;
				else
					break;
			}
		if (endtrim != null && endtrim.length != 0)
			for (int i = b.length - 1 ;i >= 0;i--)
			{
				if (end <= start)
					break;
				boolean b2 = false;
				for (int i2 = 0;i2 < endtrim.length;i2++)
					if (b[i] == endtrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					end--;
				else
					break;
			}
		if (start == 0 && end == b.length)
			return b;
		return Arrays.copyOfRange(b, start, end);

	}
	public static float[] trim(float[] b, float[] starttrim, float[] endtrim)
	{
		if (b == null)
			throw new NullPointerException();

		int start = 0;
		int end = b.length;
		if (starttrim != null && starttrim.length != 0)
			for (int i = 0;i < b.length;i++)
			{
				boolean b2 = false;
				for (int i2 = 0;i2 < starttrim.length;i2++)
					if (b[i] == starttrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					start++;
				else
					break;
			}
		if (endtrim != null && endtrim.length != 0)
			for (int i = b.length - 1 ;i >= 0;i--)
			{
				if (end <= start)
					break;
				boolean b2 = false;
				for (int i2 = 0;i2 < endtrim.length;i2++)
					if (b[i] == endtrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					end--;
				else
					break;
			}
		if (start == 0 && end == b.length)
			return b;
		return Arrays.copyOfRange(b, start, end);

	}
	public static short[] trim(short[] b, short[] starttrim, short[] endtrim)
	{
		if (b == null)
			throw new NullPointerException();

		int start = 0;
		int end = b.length;
		if (starttrim != null && starttrim.length != 0)
			for (int i = 0;i < b.length;i++)
			{
				boolean b2 = false;
				for (int i2 = 0;i2 < starttrim.length;i2++)
					if (b[i] == starttrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					start++;
				else
					break;
			}
		if (endtrim != null && endtrim.length != 0)
			for (int i = b.length - 1 ;i >= 0;i--)
			{
				if (end <= start)
					break;
				boolean b2 = false;
				for (int i2 = 0;i2 < endtrim.length;i2++)
					if (b[i] == endtrim[i2])
					{
						b2 = true;
						break;
					}
				if (b2)
					end--;
				else
					break;
			}
		if (start == 0 && end == b.length)
			return b;
		return Arrays.copyOfRange(b, start, end);

	}













	public static boolean startsWith(char[] b, CharSequence s)
	{
		return startsWith(b, s, 0);
	}
	public static boolean endWith(char[] b, CharSequence s)
	{
		return startsWith(b, s, b.length - s.length());
	}
	public static boolean startsWith(CharSequence b, char[] s)
	{
		return startsWith(b, s, 0);
	}
	public static boolean endWith(CharSequence b, char[] s)
	{
		return startsWith(b, s, b.length() - s.length);
	}
	
	
	
	public static boolean startsWith(Object[] b, Object[] s)
	{
		return startsWith(b, s, 0);
	}
	public static boolean endWith(Object[] b, Object[] s)
	{
		return startsWith(b, s, b.length - s.length);
	}

	public static boolean startsWith(byte[] b, byte[] s)
	{
		return startsWith(b, s, 0);
	}
	public static boolean endWith(byte[] b, byte[] s)
	{
		return startsWith(b, s, b.length - s.length);
	}

	public static boolean startsWith(long[] b, long[] s)
	{
		return startsWith(b, s, 0);
	}
	public static boolean endWith(long[] b, long[] s)
	{
		return startsWith(b, s, b.length - s.length);
	}

	public static boolean startsWith(double[] b, double[] s)
	{
		return startsWith(b, s, 0);
	}
	public static boolean endWith(double[] b, double[] s)
	{
		return startsWith(b, s, b.length - s.length);
	}

	public static boolean startsWith(char[] b, char[] s)
	{
		return startsWith(b, s, 0);
	}
	public static boolean endWith(char[] b, char[] s)
	{
		return startsWith(b, s, b.length - s.length);
	}

	public static boolean startsWith(int[] b, int[] s)
	{
		return startsWith(b, s, 0);
	}
	public static boolean endWith(int[] b, int[] s)
	{
		return startsWith(b, s, b.length - s.length);
	}

	public static boolean startsWith(boolean[] b, boolean[] s)
	{
		return startsWith(b, s, 0);
	}
	public static boolean endWith(boolean[] b, boolean[] s)
	{
		return startsWith(b, s, b.length - s.length);
	}


	public static boolean startsWith(float[] b, float[] s)
	{
		return startsWith(b, s, 0);
	}
	public static boolean endWith(float[] b, float[] s)
	{
		return startsWith(b, s, b.length - s.length);
	}

	public static boolean startsWith(short[] b, short[] s)
	{
		return startsWith(b, s, 0);
	}
	public static boolean endWith(short[] b, short[] s)
	{
		return startsWith(b, s, b.length - s.length);
	}





	public static boolean startsWith(byte[] array, byte[] b, int off)
	{
		if (array.length == 0 || b.length == 0 || off < 0 || b.length > array.length || off + b.length > array.length)
		{
			return false;
		}
		int length = off + b.length;
		for (int i = off;i < length;i++)
		{
			if (array[i] != b[i - off])return false;
		}
		return true;
	}
	public static boolean startsWith(long[] array, long[] b, int off)
	{
		if (array.length == 0 || b.length == 0 || off < 0 || b.length > array.length || off + b.length > array.length)
		{
			return false;
		}
		int length = off + b.length;
		for (int i = off;i < length;i++)
		{
			if (array[i] != b[i - off])return false;
		}
		return true;
	}
	public static boolean startsWith(char[] array, char[] b, int off)
	{
		if (array.length == 0 || b.length == 0 || off < 0 || b.length > array.length || off + b.length > array.length)
		{
			return false;
		}
		int length = off + b.length;
		for (int i = off;i < length;i++)
		{
			if (array[i] != b[i - off])return false;
		}
		return true;
	}
	public static boolean startsWith(int[] array, int[] b, int off)
	{
		if (array.length == 0 || b.length == 0 || off < 0 || b.length > array.length || off + b.length > array.length)
		{
			return false;
		}
		int length = off + b.length;
		for (int i = off;i < length;i++)
		{
			if (array[i] != b[i - off])return false;
		}
		return true;
	}
	public static boolean startsWith(short[] array, short[] b, int off)
	{
		if (array.length == 0 || b.length == 0 || off < 0 || b.length > array.length || off + b.length > array.length)
		{
			return false;
		}
		int length = off + b.length;
		for (int i = off;i < length;i++)
		{
			if (array[i] != b[i - off])return false;
		}
		return true;
	}
	public static boolean startsWith(boolean[] array, boolean[] b, int off)
	{
		if (array.length == 0 || b.length == 0 || off < 0 || b.length > array.length || off + b.length > array.length)
		{
			return false;
		}
		int length = off + b.length;
		for (int i = off;i < length;i++)
		{
			if (array[i] != b[i - off])return false;
		}
		return true;
	}
	public static boolean startsWith(double[] array, double[] b, int off)
	{
		if (array.length == 0 || b.length == 0 || off < 0 || b.length > array.length || off + b.length > array.length)
		{
			return false;
		}
		int length = off + b.length;
		for (int i = off;i < length;i++)
		{
			if (array[i] != b[i - off])return false;
		}
		return true;
	}
	public static boolean startsWith(float[] array, float[] b, int off)
	{
		if (array.length == 0 || b.length == 0 || off < 0 || b.length > array.length || off + b.length > array.length)
		{
			return false;
		}
		int length = off + b.length;
		for (int i = off;i < length;i++)
		{
			if (array[i] != b[i - off])return false;
		}
		return true;
	}
	public static boolean startsWith(Object[] array, Object[] b, int off)
	{
		if (array.length == 0 || b.length == 0 || off < 0 || b.length > array.length || off + b.length > array.length)
		{
			return false;
		}
		int length = off + b.length;
		for (int i = off;i < length;i++)
		{
			if (array[i] == null)
			{
				if (null != b[i - off])
				{
					return false;
				}
			}
			else if (!array[i].equals(b[i - off]))
			{
				return false;
			}
		}
		return true;
	}


	public static boolean startsWith(CharSequence array, char[] b, int off)
	{
		int arraylength = array.length();
		if (arraylength == 0 || b.length == 0 || off < 0 || b.length > arraylength || off + b.length > arraylength)
		{
			return false;
		}
		int length = off + b.length;
		for (int i = off;i < length;i++)
		{
			if (array.charAt(i) != b[i - off])return false;
		}
		return true;
	}
	public static boolean startsWith(char[] array, CharSequence b, int off)
	{
		int blength = b.length();
		if (array.length == 0 || blength == 0 || off < 0 || blength > array.length || off + blength > array.length)
		{
			return false;
		}
		int length = off + blength;
		for (int i = off;i < length;i++)
		{
			if (array[i] != b.charAt(i - off))return false;
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	/*
	 indexOf Array
	 找数组

	 [1, 2, 3, 4, 5, 6, 7]
	 0  1  2  3  4  5  6  7

	 indexOf(new int[]{1,2,3,4,5,6,7},1  ,0,1); >> 0
	 indexOf(new int[]{1,2,3,4,5,6,7},4  ,0,5); >> 3  //array[0] >> array[5-1]
	 */
	public static int indexOf(byte[] array, byte b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start >= indexRange)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange)
		{
            if (array[start] == b)
			{
                return start;
            }
            start++;
        }
        return -1;
    }
	public static int indexOf(long[] array, long b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start >= indexRange)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange)
		{
            if (array[start] == b)
			{
                return start;
            }
            start++;
        }
        return -1;
    }
	public static int indexOf(char[] array, char b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start >= indexRange)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange)
		{
            if (array[start] == b)
			{
                return start;
            }
            start++;
        }
        return -1;
    }
	public static int indexOf(int[] array, int b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start >= indexRange)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange)
		{
            if (array[start] == b)
			{
                return start;
            }
            start++;
        }
        return -1;
    }
	public static int indexOf(short[] array, short b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start >= indexRange)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange)
		{
            if (array[start] == b)
			{
                return start;
            }
            start++;
        }
        return -1;
    }
	public static int indexOf(boolean[] array, boolean b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start >= indexRange)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange)
		{
            if (array[start] == b)
			{
                return start;
            }
            start++;
        }
        return -1;
    }
	public static int indexOf(double[] array, double b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start >= indexRange)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange)
		{
            if (array[start] == b)
			{
                return start;
            }
            start++;
        }
        return -1;
    }
	public static int indexOf(float[] array, float b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start >= indexRange)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
        while (start < indexRange)
		{
            if (array[start] == b)
			{
                return start;
            }
            start++;
        }
        return -1;
    }
	public static int indexOf(Object[] array, Object b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start >= indexRange)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
		if (b == null)
		{
			while (start < indexRange)
			{
				if (array[start] == null)
				{
					return start;
				}
				start++;
			}
		}
		else
		{
			while (start < indexRange)
			{
				if (b.equals(array[start]))
				{
					return start;
				}
				start++;
			}
		}
        return -1;
    }
	public static int indexOf(CharSequence str, char b, int start, int indexRange)
	{
		int strlength;
		if (str == null || (strlength = str.length()) == 0 || start >= indexRange)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > strlength)
			indexRange = strlength;
        while (start < indexRange)
		{
            if (str.charAt(start) == b)
			{
                return start;
            }
            start++;
        }
        return -1;
    }
	/*
	 last indexOf Array
	 倒找数组

	 [1, 2, 3, 4, 5, 6, 7]
	 0  1  2  3  4  5  6  7

	 lastIndexOf(new int[]{1,2,3,4,5,6,7},2  ,3,0); >> 1  //array[3] >> array[0]
	 */
	public static int lastIndexOf(byte[] array, byte b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange)
		{
            if (array[startIndex] == b)
			{
                return startIndex;
            }
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(long[] array, long b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange)
		{
            if (array[startIndex] == b)
			{
                return startIndex;
            }
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(char[] array, char b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange)
		{
            if (array[startIndex] == b)
			{
                return startIndex;
            }
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(int[] array, int b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange)
		{
            if (array[startIndex] == b)
			{
                return startIndex;
            }
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(short[] array, short b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange)
		{
            if (array[startIndex] == b)
			{
                return startIndex;
            }
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(boolean[] array, boolean b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange)
		{
            if (array[startIndex] == b)
			{
                return startIndex;
            }
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(double[] array, double b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange)
		{
            if (array[startIndex] == b)
			{
                return startIndex;
            }
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(float[] array, float b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
        while (startIndex >= indexRange)
		{
            if (array[startIndex] == b)
			{
                return startIndex;
            }
            startIndex--;
        }
        return -1;
    }
	public static int lastIndexOf(Object[] array, Object b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length - 1)
			startIndex = array.length - 1 ;
		if (b == null)
		{
			while (startIndex >= indexRange)
			{
				if (array[startIndex] == null)
				{
					return startIndex;
				}
				startIndex--;
			}
		}
		else
		{
			while (startIndex >= indexRange)
			{
				if (b.equals(array[startIndex]))
				{
					return startIndex;
				}
				startIndex--;
			}
		}
        return -1;
    }
	public static int lastIndexOf(CharSequence str, char b, int startIndex, int indexRange)
	{
		int strlength;
		if (str == null || (strlength = str.length()) == 0 || indexRange > startIndex)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > strlength - 1)
			startIndex = strlength - 1 ;
        while (startIndex >= indexRange)
		{
            if (str.charAt(startIndex) == b)
			{
                return startIndex;
            }
            startIndex--;
        }
        return -1;
    }
	
	
	
	
	
	
	/*
	 indexOf Array
	 找数组

	 indexOf(new byte[]{0,0,2,0,0,2,0,0,1,3,5,9},new byte[]{2,0,0},3,12); >> 5   array[3] >> array[12-1] 必须匹配b[0]然后再匹配后面的元素
	 */
	public static int indexOf(byte[] array, byte[] b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start > indexRange || b == null || b.length > array.length || b.length == 0 || indexRange - start + 1 < b.length)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++)
		{
			if (array[i] == b[0])
			{
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(long[] array, long[] b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start > indexRange || b == null || b.length > array.length || b.length == 0 || indexRange - start + 1 < b.length)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++)
		{
			if (array[i] == b[0])
			{
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(char[] array, char[] b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start > indexRange || b == null || b.length > array.length || b.length == 0 || indexRange - start + 1 < b.length)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++)
		{
			if (array[i] == b[0])
			{
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(int[] array, int[] b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start > indexRange || b == null || b.length > array.length || b.length == 0 || indexRange - start + 1 < b.length)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++)
		{
			if (array[i] == b[0])
			{
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(short[] array, short[] b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start > indexRange || b == null || b.length > array.length || b.length == 0 || indexRange - start + 1 < b.length)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++)
		{
			if (array[i] == b[0])
			{
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(boolean[] array, boolean[] b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start > indexRange || b == null || b.length > array.length || b.length == 0 || indexRange - start + 1 < b.length)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++)
		{
			if (array[i] == b[0])
			{
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(double[] array, double[] b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start > indexRange || b == null || b.length > array.length || b.length == 0 || indexRange - start + 1 < b.length)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++)
		{
			if (array[i] == b[0])
			{
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(float[] array, float[] b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start > indexRange || b == null || b.length > array.length || b.length == 0 || indexRange - start + 1 < b.length)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++)
		{
			if (array[i] == b[0])
			{
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(Object[] array, Object[] b, int start, int indexRange)
	{
		if (array == null || array.length == 0 || start > indexRange || b == null || b.length > array.length || b.length == 0 || indexRange - start + 1 < b.length)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > array.length)
			indexRange = array.length;
		int i, i2;
		for (i = start; i < indexRange; i++)
		{
			if (array[i] == null)
			{
				if (null == b[0])
				{
					if (indexRange - i < b.length)
						break;
					for (i2 = 1; i2 < b.length; i2++)
					{
						if (array[i + i2] == null)
						{
							if (null != b[i2])
								break;
						}
						else
						{
							if (!array[i + i2].equals(b[i2]))
								break;
						}
					}
					if (i2 == b.length)
						return i;
				}
			}
			else if (array[i].equals(b[0]))
			{
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
				{
					if (array[i + i2] == null)
					{
						if (null != b[i2])
							break;
					}
					else if (!array[i + i2].equals(b[i2]))
						break;
				}
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}

	public static int indexOf(CharSequence str, char[] b, int start, int indexRange)
	{
		int strlength;
		if (str == null || (strlength = str.length()) == 0 || start > indexRange || b == null || b.length > strlength || b.length == 0 || indexRange - start + 1 < b.length)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > strlength)
			indexRange = strlength;
		int i, i2;
		for (i = start; i < indexRange; i++)
		{
			if (str.charAt(i) == b[0])
			{
				if (indexRange - i < b.length)
					break;
				for (i2 = 1; i2 < b.length; i2++)
					if (str.charAt(i + i2) != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int indexOf(char[] str, CharSequence b, int start, int indexRange)
	{
		int blength;
		if (str == null || str.length == 0 || start > indexRange || b == null || (blength = b.length()) > str.length || blength == 0 || indexRange - start + 1 < blength)
			return -1;
		else if (start < 0)
			start = 0;
		if (indexRange > str.length)
			indexRange = str.length;
		int i, i2;
		for (i = start; i < indexRange; i++)
		{
			if (str[i] == b.charAt(0))
			{
				if (indexRange - i < blength)
					break;
				for (i2 = 1; i2 < blength; i2++)
					if (str[i + i2] != b.charAt(i2))
						break;
				if (i2 == blength)
					return i;
			}
		}
		return -1;
	}

	
	
	





	







	/*
	 last indexOf Array
	 倒找数组

	 [1, 2, 3, 4, 5, 6, 7]
	 0  1  2  3  4  5  6  7

	 lastIndexOf(new byte[]{0,0,2,0,0,2,0,0,1,3,5,9},new byte[]{2,0,0},3,0); >> 2 //array[3] >> array[0]
	 lastIndexOf(new byte[]{0,0,2,0,0,2,0,0,1,3,5,9},new byte[]{5,9},11,0)); >> 10  //array[11] >> array[0]   array[11] >> array[0] 必须匹配b[0]然后再匹配后面的元素
	 */
	public static int lastIndexOf(byte[] array, byte[] b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex || b == null || b.length > array.length || b.length == 0 || startIndex - indexRange + 1 < b.length)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length?array.length-1:startIndex; i >= indexRange; i--)
		{
			if (array[i] == b[0])
			{
				if (i+b.length>startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(long[] array, long[] b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex || b == null || b.length > array.length || b.length == 0 || startIndex - indexRange + 1 < b.length)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length?array.length-1:startIndex; i >= indexRange; i--)
		{
			if (array[i] == b[0])
			{
				if (i+b.length>startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(char[] array, char[] b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex || b == null || b.length > array.length || b.length == 0 || startIndex - indexRange + 1 < b.length)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length?array.length-1:startIndex; i >= indexRange; i--)
		{
			if (array[i] == b[0])
			{
				if (i+b.length>startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(int[] array, int[] b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex || b == null || b.length > array.length || b.length == 0 || startIndex - indexRange + 1 < b.length)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length?array.length-1:startIndex; i >= indexRange; i--)
		{
			if (array[i] == b[0])
			{
				if (i+b.length>startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(short[] array, short[] b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex || b == null || b.length > array.length || b.length == 0 || startIndex - indexRange + 1 < b.length)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length?array.length-1:startIndex; i >= indexRange; i--)
		{
			if (array[i] == b[0])
			{
				if (i+b.length>startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(boolean[] array, boolean[] b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex || b == null || b.length > array.length || b.length == 0 || startIndex - indexRange + 1 < b.length)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length?array.length-1:startIndex; i >= indexRange; i--)
		{
			if (array[i] == b[0])
			{
				if (i+b.length>startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(double[] array, double[] b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex || b == null || b.length > array.length || b.length == 0 || startIndex - indexRange + 1 < b.length)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length?array.length-1:startIndex; i >= indexRange; i--)
		{
			if (array[i] == b[0])
			{
				if (i+b.length>startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(float[] array, float[] b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex || b == null || b.length > array.length || b.length == 0 || startIndex - indexRange + 1 < b.length)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length?array.length-1:startIndex; i >= indexRange; i--)
		{
			if (array[i] == b[0])
			{
				if (i+b.length>startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array[i + i2] != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(Object[] array, Object[] b, int startIndex, int indexRange)
	{
		if (array == null || array.length == 0 || indexRange > startIndex || b == null || b.length > array.length || b.length == 0 || startIndex - indexRange + 1 < b.length)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length)
			startIndex = array.length;
		int i, i2;
		for (i = startIndex == array.length?array.length-1:startIndex; i >= indexRange; i--)
		{
			if (array[i] == null)
			{
				if (null == b[0])
				{
					if (i+b.length>startIndex)
						continue;
					for (i2 = 1; i2 < b.length; i2++)
					{
						if (array[i + i2] == null)
						{
							if (null != b[i2])
							{
								break;
							}
						}
						else if (!array[i + i2].equals(b[i2]))
							break;
					}
					if (i2 == b.length)
						return i;
				}
			}
			else if (array[i].equals(b[0]))
			{
				if (i+b.length>startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
				{
					if (array[i + i2] == null)
					{
						if (null != b[i2])
						{
							break;
						}
					}
					else if (!array[i + i2].equals(b[i2]))
						break;
				}
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(CharSequence array , char[] b, int startIndex, int indexRange)
	{
		int arraylength;
		if (array == null || (arraylength = array.length()) == 0 || indexRange > startIndex || b == null || b.length > arraylength || b.length == 0 || startIndex - indexRange + 1 < b.length)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > arraylength)
			startIndex = arraylength;

		int i, i2;
		for (i = startIndex == arraylength?arraylength-1:startIndex; i >= indexRange; i--)
		{
			if (array.charAt(i) == b[0])
			{
				if (i+b.length>startIndex)
					continue;
				for (i2 = 1; i2 < b.length; i2++)
					if (array.charAt(i + i2) != b[i2])
						break;
				if (i2 == b.length)
					return i;
			}
		}
		return -1;
	}
	public static int lastIndexOf(char[] array , CharSequence b, int startIndex, int indexRange)
	{
		int blength;
		if (array == null || array.length == 0 || indexRange > startIndex || b == null || (blength = b.length()) > array.length || blength == 0 || startIndex - indexRange + 1 < blength)
			return -1;
		else if (indexRange < 0)
			indexRange = 0;
		if (startIndex > array.length)
			startIndex = array.length;

		int i, i2;
		for (i = startIndex == array.length?array.length-1:startIndex; i >= indexRange; i--)
		{
			if (array[i] == b.charAt(0))
			{
				if (i+blength>startIndex)
					continue;
				for (i2 = 1; i2 < blength; i2++)
					if (array[i + i2] != b.charAt(i2))
						break;
				if (i2 == blength)
					return i;
			}
		}
		return -1;
	}







	
	

}
