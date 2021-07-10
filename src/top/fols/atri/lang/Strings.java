package top.fols.atri.lang;

import top.fols.atri.array.ArrayObject;
import top.fols.box.io.base.XCharArrayWriter;
import top.fols.box.util.XArray;
import top.fols.box.util.XRandom;

import javax.swing.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("SpellCheckingInspection")
public class Strings {


	public static String tabs(String lines) {
		return tabs(lines, "\t");
	}
	public static String tabs(String lines, String tabs) {
		if (null == lines) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		StringLineReader reader = new StringLineReader(lines);
		while (reader.hasNext()) {
			result.append(tabs).append(reader.next(true));
		}
		return result.toString();
	}
	public static String line(String... lines) {
		if (null == lines) { return null; }
		StringBuilder result = new StringBuilder();
		for (String line: lines) {
			result.append(line).append(Finals.LINE_SEPARATOR_CHAR_N);
		}
		if (result.length() > 1) {
			result.setLength(result.length() - 1);
		}
		return result.toString();
	}






	public static String[][] matchs(String content, String regex) {
		return matchs(content, regex, Pattern.MULTILINE);
	}
	public static String[][] matchs(String content, String regex, int mode) {
		Pattern pattern = Pattern.compile(regex, mode);
		return matchs(content, pattern);
	}
	public static String[][] matchs(String content, Pattern regex) {
		List<String[]> results = new ArrayList<>();
		Matcher matcher = regex.matcher(content);
		while (matcher.find()) {
			String[] result = new String[matcher.groupCount()];
			for (int i = 0; i < result.length;i++) {
				result[i] = matcher.group(i + 1);
			}
			results.add(result);
		}
		return results.toArray(new String[][]{});
	}

	public static String[] match(String content, String regex) {
		return match(content, regex, Pattern.MULTILINE);
	}
	public static String[] match(String content, String regex, int mode) {
		Pattern pattern = Pattern.compile(regex, mode);
		return match(content, pattern);
	}
	public static String[] match(String content, Pattern regex) {
		Matcher matcher = regex.matcher(content);
		if (matcher.find()) {
			String[] result = new String[matcher.groupCount()];
			for (int i = 0; i < result.length;i++) {
				result[i] = matcher.group(i + 1);
			}
			return result;
		}
		return Finals.EMPTY_STRING_ARRAY;
	}









	public static String join(Object[] array, String joinString) {
		return Strings.join(array, null, joinString, null);
	}
	@SuppressWarnings({"ImplicitArrayToString", "ConstantConditions"})
	public static String join(Object[] array, String head, String joinString, String end) {
		StringBuilder sb = new StringBuilder();
		if (null != head) { sb.append(head); }
		if (null == array) {
			sb.append(array);
		} else {
			if (array.length > 0) {
				for (Object o : array) {
					sb.append(o).append(joinString);
				}
				if (sb.length() > joinString.length()) {
					sb.setLength(sb.length() - joinString.length());
				}
			}
		}
		if (null != end) { sb.append(end); }
		return sb.toString();
	}



	public static String join(Collection<?> array, String joinString) {
		return Strings.join(array, null, joinString, null);
	}

	public static String join(Collection<?> array, String head, String joinString, String end) {
		StringBuilder sb = new StringBuilder();
		if (null != head) { sb.append(head); }
		if (null == array) {
			sb.append(array);
		} else {
			if (array.size() > 0) {
				for (Object o : array) {
					sb.append(o).append(joinString);
				}
				if (sb.length() > joinString.length()) {
					sb.setLength(sb.length() - joinString.length());
				}
			}
		}
		if (null != end) { sb.append(end); }
		return sb.toString();
	}


	/**
	 * StringJoiner
	 * 
	 * @param array
	 * @param joinString
	 * @return
	 */
	public static String join(Object array, String joinString) {
		return Strings.join(array, null, joinString, null);
	}

	public static String join(Object array, String head, String joinString, String end) {
		StringBuilder sb = new StringBuilder();
		if (null != head) { sb.append(head); }
		if (!ArrayObject.wrapable(array)) {
			sb.append(array);
		} else {
			ArrayObject<?> xifs = ArrayObject.wrap(array);
			int len = xifs.length();
			if (len > 0) {
				for (int i = 0; i < len; i++) {
					sb.append(xifs.objectValue(i)).append(joinString);
				}
				if (sb.length() > joinString.length()) {
					sb.setLength(sb.length() - joinString.length());
				}
			}
		}
		if (null != end) { sb.append(end); }
		return sb.toString();
	}



	public static String join(Map<?, ?> map, String joinString) {
		return Strings.join(map, null, "=", joinString, null);
	}
	public static String join(Map<?, ?> map, String valSeparator, String joinString) {
		return Strings.join(map, null, valSeparator, joinString, null);
	}
	public static String join(Map<?, ?> map, String head, String valSeparator, String joinString, String end) {
		StringBuilder sb = new StringBuilder();
		if (null != head) { sb.append(head); }
		if (null == map) {
			sb.append(map);
		} else {
			int len = map.size();
			Set<?> set = map.keySet();
			int i = 0;
			for (Object k : set) {
				sb.append(k).append(valSeparator).append(map.get(k)).append((i >= len - 1) ? "" : joinString);
				i++;
			}
		}
		if (null != end) { sb.append(end); }
		return sb.toString();
	}




	public static String marge(Object... values) {
		StringBuilder concat = new StringBuilder();
		if (null == values || values.length == 0) {
		} else {
			for (Object value: values) {
				concat.append(Objects.toString(value));
			}
		}
		return concat.toString();
	}





	public static String random(char[] str, int length) {
		if (null == str) { return  null; }
		if (length == 0) { return Finals.STRING_EMPTY_VALUE; }

		XRandom random = new XRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(str[random.randomInt(0, str.length - 1)]);
		}
		return sb.toString();
	}

	public static String random(CharSequence str, int length) {
		if (null == str) { return  null; }
		if (length == 0) { return Finals.STRING_EMPTY_VALUE; }

		XRandom random = new XRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(str.charAt(random.randomInt(0, str.length() - 1)));
		}
		return sb.toString();
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
	 * 获取str 出现的位置集合
	 */
	public static List<Integer> search(String str, String element) {
		List<Integer> list = new ArrayList<Integer>();
		if (Objects.empty(str) || Objects.empty(element)) {
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
	public static int getRepeatCount(String str, String find) {
		if (Objects.empty(str) || Objects.empty(find)) { return 0; }

		int i = 0;
		int elementlen = find.length();
		int indexOf = str.indexOf(find);
		if (indexOf <= -1) { return 0; }
		do {
			i++;
		} while ((indexOf = str.indexOf(find, indexOf + elementlen)) > -1);
		return i;
	}



	/**
	 * 取文本长度
	 */
	public static int length(String str) { return null == str ? 0 : str.length(); }



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
		if (null == str || (startIndex < 0 || endIndex < 0)) { return null; }

		return str.substring(startIndex, endIndex);
	}
	public static String subtrim(String spec, int start, int limit) {
		if (null == spec || (start < 0 || limit < 0)) { return null; }

		while ((limit > 0) && (spec.charAt(limit - 1) <= ' ')) limit--;
		while ((start < limit) && (spec.charAt(start) <= ' ')) start++;
		return spec.substring(start, limit);
	}




	/**
	 * 寻找文本
	 */
	public static int indexOf(String str, String find, int off) {
		return (null == str || null == find) ? -1 : str.indexOf(find, off);
	}

	public static int indexOf(String str, char find, int off) {
		return (null == str) ? -1 : str.indexOf(find, off);
	}

	public static int indexOf(String str, int find, int off) {
		return (null == str) ? -1 : str.indexOf(find, off);
	}

	public static int lastIndexOf(String str, String find, int off) {
		return (null == str || null == find) ? -1 : str.lastIndexOf(find, off);
	}

	public static int lastIndexOf(String str, char find, int off) {
		return (null == str) ? -1 : str.lastIndexOf(find, off);
	}

	public static int lastIndexOf(String str, int find, int off) {
		return (null == str) ? -1 : str.lastIndexOf(find, off);
	}





	/**
	 * split String
	 * <p>
	 * 分割文本
	 * 
	 * split("ab+cd+ef","+"); >> {"ab","cd",ef"}
	 */
	public static List<String> split(String str, String separator) {
		List<String> list = new ArrayList<>();
		Strings.split(str, separator, list);
		return list;
	}

	public static void split(String str, String separator, Collection<String> splits) {
		if (Objects.empty(str) || Objects.empty(separator) || str.equals(separator) || null == splits) { return; }

		int end = 0;
		int off = -separator.length();

		boolean startWith;
		if (startWith = str.startsWith(separator)) {
			end += separator.length();
			off += separator.length();
		}

		if ((end = str.indexOf(separator, end)) > -1) {
			while (end > -1) {
				splits.add(str.substring(off + separator.length(), end));
				off = end;
				end = str.indexOf(separator, end + separator.length());
			}
			if (str.endsWith(separator)) {
				return;
			}
			splits.add(str.substring(off + separator.length(), str.length()));
		} else {
			if (startWith) {
				splits.add(str.substring(off + separator.length(), str.length()));
			}
		}
	}

	/**
	 * repeat String
	 * <p>
	 * 取重复字符串
	 */
	public static String repeat(String str, int repeatLength) {
		if (null == str) { return null; }
		if (str.length() == 0 || repeatLength == 0) { return Finals.STRING_EMPTY_VALUE; }

		char[] newChar = XArray.repeat(str.toCharArray(), repeatLength);
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

		XCharArrayWriter retains = new XCharArrayWriter();
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
}
