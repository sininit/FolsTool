package top.fols.box.lang;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import top.fols.box.util.ArrayListUtils;
import top.fols.box.util.XArrays;
import top.fols.box.statics.XStaticBaseType;
public class XString
{
	public static String getRandomString(char[] str, int length)
	{
		if (length == 0)
			return "";
		Random random = new Random();  
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < length; ++i)
			sb.append(str[random.nextInt(str.length - 1)]);
		return sb.toString();
	}



	public static boolean isEmpty(String str)
	{
        return str == null || str.length() == 0;
    }
	//替换字符串  i代表替换次数
	public static String replace(String text, String searchString, String replacement, int max)
	{
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0)
            return text;
		if (max < 1)
			return text.replace(searchString, replacement);
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1)
            return text;
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= max < 0 ? 16 : max > 64 ? 64 : max;
		final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1)
		{
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0)
                break;
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }




	//获取str 出现的位置集合
	public static List<Integer> indexOfs(String Stringx, String str)
	{
		List<Integer> arraylist = new ArrayListUtils<Integer>();
		if (isEmpty(Stringx) || isEmpty(str))
			return arraylist;
        int i = 0;
		int strlength = str.length();
		int indexOf = Stringx.indexOf(str);
		while (indexOf > -1)
		{
			i++;
			arraylist.add(indexOf);
			indexOf = Stringx.indexOf(str, indexOf + strlength);
		}
        return arraylist;
	}
	//获取str重复出现的次数
	public static int getRepeatNum(String Stringx, String str)
	{
		if (isEmpty(Stringx) || isEmpty(str))
			return 0;
        int i = 0;
		int strlength = str.length();
		int indexOf = Stringx.indexOf(str);
		while (indexOf > -1)
		{
			i++;
			indexOf = Stringx.indexOf(str, indexOf + strlength);
		}
        return i;
    }


	public static String submiddle(String str, String left, String right, int off)
	{
		if (isEmpty(str) || isEmpty(left) || isEmpty(right))
			return "";
		if (off < 0)
			off = 0;
		int start = str.indexOf(left, off);
		int end = str.indexOf(right, start + left.length());
		if (start > -1 && end > -1)
			return str.substring(start + left.length(), end);
		return "";
	}
	public static String submiddle(String str, int off, int end)
	{
		if (isEmpty(str) || off > end || off < 0 || end < 0 || end > str.length())
			return "";
		return str.substring(off, end);
	}

	public static String subleft(String str, String string, int off)
	{
		return subleft(str, str.indexOf(string, off));
	}
	public static String subleft(String str, int off)
	{
		if (isEmpty(str) || off < 0 || off > str.length())
			return "";
		return str.substring(0, off);
	}

	public static String subright(String str, String string, int off)
	{
		int index = str.indexOf(string, off);
		if (index > -1)
			index += string.length();
		return subright(str, index);
	}
	public static String subright(String str, int off)
	{
		if (isEmpty(str) || off < 0 || off > str.length())
			return "";
		return str.substring(off, str.length());
	}





	/*
	 split String
	 分割文本

	 split("ab+cd+ef"); >> {"ab","cd",ef"}
	 */
	public static List<String> split(String Stringx, String str)
	{
        List<String> arraylist = new ArrayListUtils<String>();
        if (isEmpty(Stringx) || isEmpty(str) || Stringx.equals(str))
			return arraylist;
		if (Stringx.startsWith(str))
			Stringx = Stringx.substring(str.length(), Stringx.length());
		int end = 0;
		int off = -str.length();
		if ((end = Stringx.indexOf(str, end)) > -1)
		{
			while (end > -1)
			{
				arraylist.add(Stringx.substring(off + str.length(), end));
				off = end;
				end = Stringx.indexOf(str, end + str.length());
			}
			if (Stringx.endsWith(str))
				return arraylist;
			arraylist.add(Stringx.substring(Stringx.lastIndexOf(str) + str.length(), Stringx.length()));
		}
		return arraylist;
    }











	/*
	 repeat String
	 取重复字符串
	 */
	public static String repeat(String str, int repeatLength)
	{
		if (str == null || str.length() == 0)
			return "";
		char newChar[] = (char[])XArrays.repeat(str.toCharArray(), XStaticBaseType.char_class, repeatLength);
		return new String(newChar);
	}
	/*
	 fill String
	 填充文本

	 fillLeft("123456",'0',2)); >> "56"
	 fillLeft("123456",'0',8)); >> "00123456"
	 */
	public static String fillLeft(String str, char fillstr, int newLength)
	{
		char newChar[] = new char[newLength];
		fillLeft(str.toCharArray(), fillstr, newChar);
		return new String(newChar);
	}
	public static void fillLeft(char[] str, char fillstr, char newChar[])
	{
		if (newChar.length == str.length)
		{
			System.arraycopy(str, 0, newChar, 0, str.length);
			return;
		}
		else if (str.length == 0 && newChar.length == 0)
			return;
		else if (newChar.length == 0)
			return;
		if (newChar.length < str.length)
		{
			int strart = str.length - newChar.length;
			int end = str.length > newChar.length ?newChar.length: str.length;
			System.arraycopy(str, strart, newChar, 0, end);
		}
		else
		{
			int strart = newChar.length - str.length;
			System.arraycopy(str, 0, newChar, strart, str.length);
			Arrays.fill(newChar, 0, strart, fillstr);
		}
	}
	/*
	 fill String Last
	 在末尾填充文本

	 fillRight("123456",'0',2)); >> "12"
	 fillRight("123456",'0',8)); >> "12345600"
	 */
	public static String fillRight(String str, char fillstr, int newLength)
	{
		char newChar[] = new char[newLength];
		fillRight(str.toCharArray(), fillstr, newChar);
		return new String(newChar);
	}
	public static void fillRight(char[] str, char fillstr, char newChar[])
	{
		if (newChar.length == str.length || (str.length == 0 && newChar.length == 0))
		{
			System.arraycopy(str, 0, newChar, 0, str.length);
			return;
		}
		else if (str.length == 0 && newChar.length == 0)
			return;
		else if (newChar.length == 0)
			return;
		if (newChar.length < str.length)
		{
			System.arraycopy(str, 0, newChar, 0, newChar.length);
		}
		else
		{
			System.arraycopy(str, 0, newChar, 0, str.length);
			Arrays.fill(newChar, str.length, newChar.length, fillstr);
		}
	}
}
