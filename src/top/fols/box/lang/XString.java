package top.fols.box.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XArray;
import top.fols.box.util.XObjects;
import top.fols.box.util.XRandom;

public class XString {
	public static final String Number = "0123456789";
	public static final String LowerCaseAlphabet = "abcdefghijklnmopqrstuvwxyz";
	public static final String CapitalAlphabet = "ABCDEFGHIJKLNMOPQRSTUVWXYZ";

	public static String getRandomString(char[] str, int length) {
		if (length == 0) return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) sb.append(str[XRandom.getRandomInt(0, str.length - 1)]);
		return sb.toString();
	}
	public static String getRandomString(CharSequence str, int length) {
		if (length == 0) return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) sb.append(str.charAt(XRandom.getRandomInt(0, str.length() - 1)));
		return sb.toString();
	}
	
	
	
	//替换字符串  i代表替换次数
	public static String replace(String str, CharSequence target, CharSequence replacement) {
		return replace(str, target, replacement, -1);
	}
	public static String replace(String str, CharSequence target, CharSequence replacement, int limiter) {
        String tgtStr = target.toString();
        String replStr = replacement.toString();
        int j = str.indexOf(tgtStr);
        int tgtLen = tgtStr.length();
		if (j < 0 || limiter == 0 || tgtLen == 0)
            return str;
        int tgtLen1 = Math.max(tgtLen, 1);
        int length = str.length();
        int newLenHint = length - tgtLen + replStr.length();
        if (newLenHint < 0)
            throw new OutOfMemoryError();
        StringBuilder sb = new StringBuilder(newLenHint);
        int i = 0;
		if (limiter < 0)
			do {
				sb.append(str, i, j).append(replStr);
				i = j + tgtLen;
			} while (j < length && (j = str.indexOf(tgtStr, j + tgtLen1)) > 0);
		else do {
				sb.append(str, i, j).append(replStr);
				i = j + tgtLen;
			} while (--limiter > 0 && j < length && (j = str.indexOf(tgtStr, j + tgtLen1)) > 0);
        return sb.append(str, i, length).toString();
    }





	//获取str 出现的位置集合
	public static List<Integer> indexOfs(String str, String element) {
		List<Integer> arraylist = new ArrayList<Integer>();
		if (XObjects.isEmpty(str) || XObjects.isEmpty(element))
			return arraylist;
		int strlength = element.length();
		int indexOf = str.indexOf(element);
		if (indexOf <= -1)
			return arraylist;
		do {
			arraylist.add(indexOf);
		} while((indexOf = str.indexOf(element, indexOf + strlength)) > -1);
        return arraylist;
	}
	//获取str重复出现的次数
	public static int getRepeatNum(String Stringx, String str) {
		if (XObjects.isEmpty(Stringx) || XObjects.isEmpty(str))
			return 0;
        int i = 0;
		int strlength = str.length();
		int indexOf = Stringx.indexOf(str);
		if (indexOf <= -1)
			return 0;
		do{
			i++;
		}while((indexOf = Stringx.indexOf(str, indexOf + strlength)) > -1);
        return i;
    }


	public static String submiddle(String str, String left, String right) {
		return submiddle(str, left, right, 0);
	}
	public static String submiddle(String str, String left, String right, int off) {
		if (XObjects.isEmpty(str) || XObjects.isEmpty(left) || XObjects.isEmpty(right))
			return "";
		if (off < 0)
			off = 0;
		int start = str.indexOf(left, off);
		int end = str.indexOf(right, start + left.length());
		if (start > -1 && end > -1)
			return str.substring(start + left.length(), end);
		return "";
	}
	public static String submiddle(String str, int off, int end) {
		if (XObjects.isEmpty(str) || off > end || off < 0 || end < 0 || end > str.length())
			return "";
		return str.substring(off, end);
	}




	public static String subleft(String str, String string, int off) {
		return subleft(str, str.indexOf(string, off));
	}
	public static String subleft(String str, int off) {
		if (XObjects.isEmpty(str) || off < 0 || off > str.length())
			return "";
		return str.substring(0, off);
	}




	public static String subright(String str, String string, int off) {
		int index = str.indexOf(string, off);
		if (index > -1)
			index += string.length();
		return subright(str, index);
	}
	public static String subright(String str, int off) {
		if (XObjects.isEmpty(str) || off < 0 || off > str.length())
			return "";
		return str.substring(off, str.length());
	}





	/*
	 split String
	 分割文本

	 split("ab+cd+ef"); >> {"ab","cd",ef"}
	 */
	public static List<String> split(String Stringx, String str) {
        List<String> arraylist = new ArrayList<String>();
        if (XObjects.isEmpty(Stringx) || XObjects.isEmpty(str) || Stringx.equals(str))
			return arraylist;
		if (Stringx.startsWith(str))
			Stringx = Stringx.substring(str.length(), Stringx.length());
		int end = 0;
		int off = -str.length();
		if ((end = Stringx.indexOf(str, end)) > -1) {
			while (end > -1) {
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
	public static String repeat(String str, int repeatLength) {
		if (null == str || str.length() == 0)
			return "";
		char newChar[] = XArray.repeat(str.toCharArray(), repeatLength);
		return new String(newChar);
	}



	/*
	 fill String
	 填充文本

	 fillLeft("123456",'0',2)); >> "56"
	 fillLeft("123456",'0',8)); >> "00123456"
	 */
	public static String fillLeft(String str, char fillstr, int newLength) {
		char newChar[] = new char[newLength];
		fillLeft(str.toCharArray(), fillstr, newChar);
		return new String(newChar);
	}
	public static void fillLeft(char[] str, char fillstr, char newChar[]) {
		if (newChar.length == str.length) {
			if (str == newChar)
				return;
			System.arraycopy(str, 0, newChar, 0, str.length);
			return;
		} else if (str.length == 0 && newChar.length == 0)
			return;
		else if (newChar.length == 0)
			return;
		if (newChar.length < str.length) {
			int strart = str.length - newChar.length;
			int end = str.length > newChar.length ?newChar.length: str.length;
			System.arraycopy(str, strart, newChar, 0, end);
		} else {
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
	public static String fillRight(String str, char fillstr, int newLength) {
		char newChar[] = new char[newLength];
		fillRight(str.toCharArray(), fillstr, newChar);
		return new String(newChar);
	}
	public static void fillRight(char[] str, char fillstr, char newChar[]) {
		if (newChar.length == str.length || (str.length == 0 && newChar.length == 0)) {
			if (str == newChar)
				return;
			System.arraycopy(str, 0, newChar, 0, str.length);
			return;
		} else if (str.length == 0 && newChar.length == 0)
			return;
		else if (newChar.length == 0)
			return;
		if (newChar.length < str.length) {
			System.arraycopy(str, 0, newChar, 0, newChar.length);
		} else {
			System.arraycopy(str, 0, newChar, 0, str.length);
			Arrays.fill(newChar, str.length, newChar.length, fillstr);
		}
	}
	
}
