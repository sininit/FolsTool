package top.fols.atri.lang;

import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.atri.io.*;
import top.fols.atri.io.util.Streams;
import top.fols.box.array.ArrayObject;
import top.fols.box.lang.Arrayx;
import top.fols.box.util.Randoms;

import java.util.*;


@SuppressWarnings("SpellCheckingInspection")
public class Strings {
	public static final String EMPTY = Finals.STRING_EMPTY_VALUE;

	public static String nullToEmpty(String string) {
		return (string == null) ? "" : string;
	}
	public static String emptyToNull(String string) {
		return isNullOrEmpty(string) ? null : string;
	}
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.length() == 0; // string.isEmpty() in Java 6
	}
	public static boolean empty(CharSequence str) {
		return null == str || str.length() == 0;
	}


	public static boolean startsWithIgnoreCase(String v, String start) {
		int vlen  = v.length();
		int stlen = start.length();
		if (stlen > vlen)
			return false;
		return v.regionMatches(true, 0, start, 0, stlen);
	}
	public static boolean endsWithIgnoreCase(String v, String start) {
		int vlen  = v.length();
		int stlen = start.length();
		if (stlen > v.length())
			return false;
		return v.regionMatches(true, vlen - stlen, start, 0, stlen);
	}

	public static String cast(Object object) {
		return null == object ? null : object.toString();
	}

	public static String tabs(String lines) {
		return tabs(lines, "\t");
	}
	public static String tabs(String lines, String tabs) {
		if (null == lines) {
			return tabs + null;
		}
		StringBuilder result = new StringBuilder();
		StringReaders readers = new StringReaders(lines);
		readers.setDelimiterAsLine();
		char[] chars;
		while (null != (chars = readers.readNextLine(true))) {
			result.append(tabs).append(chars);
		}
		Streams.close(readers);
		return result.toString();
	}
	public static String tabsFromSecondLineStart(int firstLinePrefixLength, String reason) {
		return tabsFromSecondLineStart(firstLinePrefixLength, "", reason);
	}
	public static String tabsFromSecondLineStart(int firstLinePrefixLength, String tabs, String reason) {
		StringBuilder sb = new StringBuilder();
		StringReaders readers = new StringReaders(null == reason ? "" : reason);
		readers.setDelimiterAsLine();
		char[] chars;
		if (null != (chars = readers.readNextLine(true))) {
			sb.append(chars);//first line
		}
		StringBuilder prefix = new StringBuilder();
		for (int i = 0; i < firstLinePrefixLength; i++)
			prefix.append(' ');
		prefix.append(tabs);
		while (null != (chars = readers.readNextLine(true))) {
			sb.append(prefix).append(chars);
		}
		return sb.toString();
	}




	public static String line(String... lines) {
		if (null == lines) { return null; }

		StringBuilder result = new StringBuilder();
		for (String line: lines)
			result.append(line).append(Finals.LineSeparator.LINE_SEPARATOR_CHAR_N);
		if (result.length() > 1)
			result.setLength(result.length() - 1);
		return result.toString();
	}




	public static int indexOfChar(String str, char chars) {
		return (null == str)?-1:str.indexOf(chars);
	}
	public static int indexOfChar(String str, char[] chars, int offset) {
		if (!(null == str || null == chars)) {
			for (int i = offset; i < str.length(); i++) {
				char ch = str.charAt(i);
				for (char s: chars) {
					if (ch == s) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	public static int lastIndexOfChar(String str, char chars) {
		return (null == str)?-1:str.lastIndexOf(chars);
	}
	public static int lastIndexOfChar(String str, char[] chars) {
		return lastIndexOfChar(str, chars, str.length()-1);
	}
	public static int lastIndexOfChar(String str, char[] chars, int offset) {
		if (!(null == str || null == chars) && str.length() > 0) {
			for (int i = offset; i >= 0; i--) {
				char ch = str.charAt(i);
				for (char s: chars) {
					if (ch == s) {
						return i;
					}
				}
			}
		}
		return -1;
	}












	public static String random(char[] str, int length) {
		if (null == str) { return  null; }
		if (length == 0) { return Finals.STRING_EMPTY_VALUE; }

		Randoms random = new Randoms();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(str[random.randomInt(0, str.length - 1)]);
		}
		return sb.toString();
	}

	public static String random(CharSequence str, int length) {
		if (null == str) { return  null; }
		if (length == 0) { return Finals.STRING_EMPTY_VALUE; }

		Randoms random = new Randoms();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(str.charAt(random.randomInt(0, str.length() - 1)));
		}
		return sb.toString();
	}



//	public static String submiddle(String str, String left, String right) {
//		return submiddle(str, left, right, 0);
//	}
//	public static String submiddle(String str, String left, String right, int off) {
//		if (Objects.empty(str) || Objects.empty(left) || Objects.empty(right)) { return ""; }
//		if (off < 0) { off = 0; }
//		int start = str.indexOf(left, off);
//		int end = str.indexOf(right, start + left.length());
//		if (start > -1 && end > -1) {
//			return str.substring(start + left.length(), end);
//		}
//		return "";
//	}
//
//	public static String subleft(String str, String text, int off) {
//		int index = str.indexOf(text, off);
//		if (index > -1) {
//			return subleft(str, index);
//		} else {
//			return "";
//		}
//	}
//
//	public static String subright(String str, String text, int off) {
//		int index = str.indexOf(text, off);
//		if (index > -1) {
//			return subright(str, index + text.length());
//		} else {
//			return "";
//		}
//	}
//
//	/**
//	 * 取文本左边
//	 */
//	public static String subleft(String str, int endIndex) {
//		return null == str ? null : str.substring(0, endIndex);
//	}
//
//	/**
//	 * 取文本右边
//	 */
//	public static String subright(String str, int startIndex) {
//		return null == str ? null : str.substring(startIndex, str.length());
//	}




	/**
	 * 取文本中间
	 */
	public static String substring(String str, int startIndex, int endIndex) {
		if (null == str) return null;
		if (startIndex < 0 || endIndex < 0) { return null; }

		return str.substring(startIndex, Mathz.min(str.length(), endIndex));
	}
	public static String subtrim(String str, int start, int limit) {
		if (null == str) return null;
		if (start < 0 || limit < 0) { return null; }

		while ((limit > 0) && (str.charAt(limit - 1) <= ' ')) limit--;
		while ((start < limit) && (str.charAt(start) <= ' ')) start++;
		return str.substring(start, limit);
	}





	public static List<String> splitSpace(String line) {
		List<String> list = new ArrayList<>();
		if (null == line) {
			return list;
		}
		int last = 0, length = line.length();
		for (int i = 0; i < length; i++) {
			if (Character.isSpaceChar(line.charAt(i))) {
				if (i - last > 0) {
					String element = line.substring(last, i);
					list.add(element);
				}
				while (i + 1 < length && Character.isSpaceChar(line.charAt(i + 1))) {
					i++;
				}
				last = i + 1;
			}
		}
		if (last < length) {
			String element = line.substring(last, length);
			list.add(element);
		}
		return list;
	}


	/**
	 * split String
	 * <p>
	 * 分割文本
	 *
	 * split("ab+cd+ef","+"); >> {"ab","cd",ef"}
	 */
	@Nullable
	public static List<String> splitSkipEmpty(String content, String separator) {
		if (null == content) return null;

		List<String> list;
		Strings.splitSkipEmpty(
				content,
				separator,
				list = new ArrayList<>());
		return  list;
	}

	@Nullable
	public static List<String> splitSkipEmpty(String content, String separator, List<String> splits) {
		if (null == content) return null;

		Objects.requireNonNull(separator, "separator");
		Objects.requireNonNull(splits, "buffer");

		int end = 0;
		int off = -separator.length();

		boolean startWith;
		if (startWith = content.startsWith(separator)) {
			end += separator.length();
			off += separator.length();
		}

		if ((end = content.indexOf(separator, end)) > -1) {
			while (end > -1) {
				String substring = content.substring(off + separator.length(), end);
				if (substring.length() > 0) {
					splits.add(substring);
				}
				off = end;
				end = content.indexOf(separator, end + separator.length());
			}
			if (content.endsWith(separator)) {
				return splits;
			}
			String substring = content.substring(off + separator.length(), content.length());
			if (substring.length() > 0) {
				splits.add(substring);
			}
		} else {
			if (startWith) {
				String substring = content.substring(off + separator.length(), content.length());
				if (substring.length() > 0) {
					splits.add(substring);
				}
			}
		}
		return splits;
	}

	@Nullable
	public static List<String> split(String str, String delimiter) {
		if (null == str) return null;

		Objects.requireNonNull(delimiter, "delimiter");
		Objects.requireTrue(!"".equals(delimiter), "delimiter is empty");

		List<String> result = new ArrayList<>();
		int pos;
		int delPos;
		for (pos = 0; (delPos = str.indexOf(delimiter, pos)) != -1; pos = delPos + delimiter.length()) {
			result.add(str.substring(pos, delPos));
		}
		if (str.length() > 0 && pos <= str.length()) {
			result.add(str.substring(pos));
		}
		return result;
	}
	@Nullable
	public static List<String> split(String content, Delimiter.ICharsDelimiter delimiter) {
		if (null == content) return null;

		return Delimiter.splitToStringLists(
				content,
				Objects.requireNonNull(delimiter, "delimiter"));
	}



	@Nullable
	public static String join(Object[] array,
							  String joinString) {
		if (null == array) return null;

		StringBuilder sb = new StringBuilder();
		int length = array.length;
		if (length > 0) {
			sb.append(array[0]);
			for (int i = 1; i < length; i++) {
				sb.append(joinString);
				sb.append(array[i]);
			}
		}
		return sb.toString();
	}




	@Nullable
	public static String join(List<?> array,
							  String joinString) {
		if (null == array) return null;

		StringBuilder sb = new StringBuilder();
		int length = array.size();
		if (length > 0) {
			sb.append(array.get(0));
			for (int i = 1; i < length; i++) {
				sb.append(joinString);
				sb.append(array.get(i));
			}
		}
		return sb.toString();
	}
	@Nullable
	public static String join(Collection<?> array,
							  String joinString) {
		if (null == array) return null;

		StringBuilder sb = new StringBuilder();
		int length = array.size();
		if (length > 0) {
			Iterator<?> iterator = array.iterator();
			if (iterator.hasNext()) {
				sb.append(iterator.next());
				while (iterator.hasNext()) {
					sb.append(joinString);
					sb.append(iterator.next());
				}
			}
		}
		return sb.toString();
	}


	/**
	 * StringJoiner
	 *
	 * @param array
	 * @param joinString
	 * @return
	 */
	@Nullable
	public static String join(Object array,
							  String joinString) {
		if (!ArrayObject.wrapable(array)) return null;

		StringBuilder sb = new StringBuilder();
		ArrayObject<?> xifs = ArrayObject.wrap(array);
		int length = xifs.length();
		if (length > 0) {
			sb.append(xifs.objectValue(0));
			for (int i = 1; i < length; i++) {
				sb.append(joinString);
				sb.append(xifs.objectValue(i));
			}
		}
		return sb.toString();
	}





















	/**
	 * repeat String
	 * <p>
	 * 取重复字符串
	 */
	public static String repeat(String str, int repeatLength) {
		if (null == str) { return null; }
		if (str.length() == 0 || repeatLength == 0) { return Finals.STRING_EMPTY_VALUE; }

		char[] newChar = Arrayx.repeat(str.toCharArray(), repeatLength);
		return new String(newChar);
	}

	/**
	 * fill String left
	 * <p>
	 * 在左边填充文本
	 * 
	 * fillLeft("123456",'0',2)); >> "56"
	 * <p>
	 * fillLeft("123456",'0',8)); >> "00123456"
	 */
	public static String fillLeft(String str, char fillstr, int newLength) {
		if (null == str)    { str = Finals.STRING_EMPTY_VALUE;  }
		if (newLength <= 0) { return Finals.STRING_EMPTY_VALUE; }

		char[] newChar = new char[newLength];
		fillLeft(str.toCharArray(), fillstr, newChar);
		return new String(newChar);
	}

	public static void fillLeft(char[] str, char fillstr, char[] newChar) {
		if (null == newChar) { return; }
		if (null == str)     { str = Finals.EMPTY_CHAR_ARRAY; }

		if (newChar.length == str.length) {
			if (str == newChar) {
				return;
			}
			System.arraycopy(str, 0, newChar, 0, str.length);
			return;
		} else if (newChar.length == 0) {
			return;
		}
		if (newChar.length < str.length) {
			int strart = str.length - newChar.length;
			int end = newChar.length;
			System.arraycopy(str, strart, newChar, 0, end);
		} else {
			int strart = newChar.length - str.length;
			System.arraycopy(str, 0, newChar, strart, str.length);
			Arrays.fill(newChar, 0, strart, fillstr);
		}
	}









	/**
	 * fill String Last
	 * <p>
	 * 在末尾填充文本
	 * 
	 * fillRight("123456",'0',2)); >> "12"
	 * <p>
	 * fillRight("123456",'0',8)); >> "12345600"
	 */
	public static String fillRight(String str, char fillstr, int newLength) {
		if (null == str)    { str =  Finals.STRING_EMPTY_VALUE; }
		if (newLength <= 0) { return Finals.STRING_EMPTY_VALUE; }

		char[] newChar = new char[newLength];
		fillRight(str.toCharArray(), fillstr, newChar);
		return new String(newChar);
	}

	public static void fillRight(char[] str, char fillstr, char[] newChar) {
		if (null == newChar) { return; }
		if (null == str)     { str = Finals.EMPTY_CHAR_ARRAY; }

		if (newChar.length == str.length) {
			if (str == newChar) {
				return;
			}
			System.arraycopy(str, 0, newChar, 0, str.length);
			return;
		} else if (newChar.length == 0) {
			return;
		}
		if (newChar.length < str.length) {
			System.arraycopy(str, 0, newChar, 0, newChar.length);
		} else {
			System.arraycopy(str, 0, newChar, 0, str.length);
			Arrays.fill(newChar, str.length, newChar.length, fillstr);
		}
	}

	/**
	 * reverse string
	 * <p>
	 * 字符串反转
	 * 
	 */
	public static String reverse(String str, int off, int end) throws NullPointerException {
		if (null == str)     		{ return null; }
		if (off < 0 || end < 0)     { return null; }

		if (end > off) {
			char[] chars = new char[str.length()];
			for (int i = end - 1, chi = 0; i >= off; i--) {
				chars[chi++] = (str.charAt(i));
			}
			return new String(chars);
		} else {
			return str.substring(off, end);
		}
	}

	/**
	 * retain chars
	 * <p>
	 * 保留char字符
	 * <p>
	 * 
	 * @return if @param str exist @param @retain element will return these exist
	 * @example retain("123123123321", 0, 12, new char[]{'1','2'}) >> 12121221
	 */
	public static String retain(CharSequence str, int off, int len, char[] retain) {
		if (null == str)     								  { return null; }
		if (null == retain)     							  { return null; }
		if (off < 0 || len < 0)     						  { return null; }

		CharsWriters retains = new CharsWriters();
		for (int i = off; i < off + len; i++) {
			char ch = str.charAt(i);
			for (char c : retain) {
				if (c == ch) {
					retains.write(c);
					break;
				}
			}
		}
		return retains.toString();
	}



	/**
	 * 替换字符串 i代表替换次数
	 */
	public static String replace(String str, CharSequence target, CharSequence replacement) {
		return replace(str, target, replacement, -1);
	}
	public static String replace(String str, CharSequence target, CharSequence replacement, int limiter) {
		if (null == str || null == target || target.length() == 0) { return  str; }
		if (null == replacement) { replacement = Finals.STRING_EMPTY_VALUE; }

		String tgtStr = target.toString();
		String replStr = replacement.toString();
		int j = str.indexOf(tgtStr);
		int tgtLen = tgtStr.length();
		if (j < 0 || limiter == 0 || tgtLen == 0) {
			return str;
		}
		int tgtLen1 = Math.max(tgtLen, 1);
		int length = str.length();
		int newLenHint = length - tgtLen + replStr.length();
		if (newLenHint < 0) {
			throw new OutOfMemoryError();
		}
		StringBuilder sb = new StringBuilder(newLenHint);
		int i = 0;
		if (limiter < 0)
			do {
				sb.append(str, i, j).append(replStr);
				i = j + tgtLen;
			} while (j < length && (j = str.indexOf(tgtStr, j + tgtLen1)) > 0);
		else
			do {
				sb.append(str, i, j).append(replStr);
				i = j + tgtLen;
			} while (--limiter > 0 && j < length && (j = str.indexOf(tgtStr, j + tgtLen1)) > 0);
		return sb.append(str, i, length).toString();
	}


	/**
	 * 替换文本
	 * replace string
	 * @param str string
	 * @param target need replace substrings
	 * @param replacement replacement
	 */
	public static CharSequence replace(CharSequence str, CharSequence[] target, CharSequence replacement) {
		if (null == str) 			{ return null; }
		if (str.length() == 0) 		{ return Finals.STRING_EMPTY_VALUE; }
		if (null == target || target.length == 0) { return str; }

		StringBuilder sb = new StringBuilder();

		int len = str.length();
		int lastIndex = 0;
		for (int strIndex = 0; strIndex < len; strIndex++) {
			char strchar = str.charAt(strIndex);

			for (int targetIndex = 0; targetIndex < target.length; targetIndex++) {
				if (target[targetIndex].charAt(0) == strchar && (strIndex + target[targetIndex].length()) <= len) {
					int c = 1;
					for (int targetElementIndex = 1; targetElementIndex < target[targetIndex].length(); targetElementIndex++) {
						if (target[targetIndex].charAt(targetElementIndex) == str.charAt(strIndex + targetElementIndex)) {
							c++;
						}
					}
					if (c == target[targetIndex].length()) {
						int st = lastIndex;
						int et = strIndex + target[targetIndex].length();

						sb.append(str, st, strIndex).append(replacement);

						lastIndex = et;
						strIndex += target[targetIndex].length();

						strIndex -= 1;// for (offset for self-increment)
					}
				}
			}
		}
		if (lastIndex != len) {
			sb.append(str, lastIndex, len);
		}
		return sb.toString();
	}

	/**
	 * 替换文本
	 * replace string
	 * @param str string
	 * @param target need replace substrings
	 * @param replacement correspond@param target
	 */
	public static CharSequence replace(CharSequence str, CharSequence[] target, CharSequence[] replacement) {
		if (null == str) 							{ return null; }
		if (str.length() == 0) 						{ return Finals.STRING_EMPTY_VALUE; }
		if (null == target || target.length == 0) 	{ return str; }

		StringBuilder sb = new StringBuilder();

		int len = str.length();
		int lastIndex = 0;
		for (int strIndex = 0; strIndex < len; strIndex++) {
			char strchar = str.charAt(strIndex);

			for (int targetIndex = 0; targetIndex < target.length; targetIndex++) {
				if (target[targetIndex].charAt(0) == strchar && (strIndex + target[targetIndex].length()) <= len) {
					int c = 1;
					for (int targetElementIndex = 1; targetElementIndex < target[targetIndex].length(); targetElementIndex++) {
						if (target[targetIndex].charAt(targetElementIndex) == str.charAt(strIndex + targetElementIndex)) {
							c++;
						}
					}
					if (c == target[targetIndex].length()) {
						int st = lastIndex;
						int et = strIndex + target[targetIndex].length();

						sb.append(str, st, strIndex).append(replacement[targetIndex]);

						lastIndex = et;
						strIndex += target[targetIndex].length();

						strIndex -= 1;// for (offset for self-increment)
					}
				}
			}
		}
		if (lastIndex != len) {
			sb.append(str, lastIndex, len);
		}
		return sb.toString();
	}














	/**
	 * 获取str 出现的位置集合
	 */
	public static List<Integer> searchIndex(String str, String element) {
		List<Integer> list = new ArrayList<>();
		if ((null == str     || str.length() == 0) ||
				(null == element || element.length() == 0)) {
			return list;
		}
		int elementlen = element.length();
		int indexOf = str.indexOf(element);
		if (indexOf <= -1) {
			return list;
		}
		do {
			list.add(indexOf);
		} while ((indexOf = str.indexOf(element, indexOf + elementlen)) > -1);
		return list;
	}

	/**
	 * 获取str重复出现的次数
	 */
	public static int 			searchRepeatCount(String str, String find) {
		if ((null == str  || str.length() == 0) ||
				(null == find || find.length() == 0)) {
			return 0;
		}

		int i = 0;
		int elementlen = find.length();
		int indexOf = str.indexOf(find);
		if (indexOf <= -1) { return 0; }
		do {
			i++;
		} while ((indexOf = str.indexOf(find, indexOf + elementlen)) > -1);
		return i;
	}

}
