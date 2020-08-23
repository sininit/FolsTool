package top.fols.box.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import top.fols.box.io.base.XCharArrayWriter;
import top.fols.box.lang.abstracts.XAbstractSequence;
import top.fols.box.util.XArray;
import top.fols.box.util.XObjects;
import top.fols.box.util.XRandom;

public class XString {

	/**
	 * StringJoiner
	 * 
	 * @param array
	 * @param joinString
	 * @return
	 */
	public static String join(Object array, String joinString) {
		return XString.join(array, null, joinString, null);
	}

	public static String join(Object array, String head, String joinString, String end) {
		StringBuilder sb = new StringBuilder();
		if (null != head) {
			sb.append(head);
		}
		if (null == array || !array.getClass().isArray()) {
			sb.append(array);
		} else {
			XAbstractSequence<?, ?> xifs = XSequences.wrap(array);
			int len = xifs.length();
			for (int i = 0; i < len; i++) {
				sb.append(xifs.get(i)).append((i >= len - 1) ? "" : joinString);
			}
			xifs.releaseBuffer();
		}
		if (null != end) {
			sb.append(end);
		}
		return sb.toString();
	}

	public static String join(Collection<?> array, String joinString) {
		return XString.join(array, null, joinString, null);
	}

	public static String join(Collection<?> array, String head, String joinString, String end) {
		StringBuilder sb = new StringBuilder();
		if (null != head) {
			sb.append(head);
		}
		int len = array.size();
		Iterator<?> iterator = array.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			sb.append(iterator.next()).append((i >= len - 1) ? "" : joinString);
			i++;
		}
		if (null != end) {
			sb.append(end);
		}
		return sb.toString();
	}

	public static String join(Map<?, ?> map, String joinString) {
		return XString.join(map, null, "=", joinString, null);
	}

	public static String join(Map<?, ?> map, String valSeparator, String joinString) {
		return XString.join(map, null, valSeparator, joinString, null);
	}

	public static String join(Map<?, ?> map, String head, String valSeparator, String joinString, String end) {
		StringBuilder sb = new StringBuilder();
		if (null != head) {
			sb.append(head);
		}
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
		if (null != end) {
			sb.append(end);
		}
		return sb.toString();
	}

	public static String getRandomString(char[] str, int length) {
		if (length == 0) {
			return "";
		}
		XRandom random = new XRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(str[random.randomInt(0, str.length - 1)]);
		}
		return sb.toString();
	}

	public static String getRandomString(CharSequence str, int length) {
		if (length == 0) {
			return "";
		}
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
		if (XObjects.isEmpty(str) || XObjects.isEmpty(element)) {
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
		if (XObjects.isEmpty(str) || XObjects.isEmpty(find)) {
			return 0;
		}
		int i = 0;
		int elementlen = find.length();
		int indexOf = str.indexOf(find);
		if (indexOf <= -1) {
			return 0;
		}
		do {
			i++;
		} while ((indexOf = str.indexOf(find, indexOf + elementlen)) > -1);
		return i;
	}

	/**
	 * 取文本长度
	 */
	public static int length(String str) {
		return null == str ? 0 : str.length();
	}

	public static String submiddle(String str, String left, String right) {
		return submiddle(str, left, right, 0);
	}

	public static String submiddle(String str, String left, String right, int off) {
		if (XObjects.isEmpty(str) || XObjects.isEmpty(left) || XObjects.isEmpty(right)) {
			return "";
		}
		if (off < 0) {
			off = 0;
		}
		int start = str.indexOf(left, off);
		int end = str.indexOf(right, start + left.length());
		if (start > -1 && end > -1) {
			return str.substring(start + left.length(), end);
		}
		return "";
	}

	public static String subleft(String str, String text, int off) {
		int index = str.indexOf(text, off);
		if (index > -1) {
			return subleft(str, index);
		} else {
			return "";
		}
	}

	public static String subright(String str, String text, int off) {
		int index = str.indexOf(text, off);
		if (index > -1) {
			return subright(str, index + text.length());
		} else {
			return "";
		}
	}

	/**
	 * 取文本左边
	 */
	public static String subleft(String str, int endIndex) {
		return null == str ? null : str.substring(0, endIndex);
	}

	/**
	 * 取文本右边
	 */
	public static String subright(String str, int startIndex) {
		return null == str ? null : str.substring(startIndex, str.length());
	}

	/**
	 * 取文本中间
	 */
	public static String substring(String str, int startIndex, int endIndex) throws NullPointerException {
		if (null == str) {
			throw new NullPointerException("string");
		} else {
			if (endIndex == startIndex) {
				return "";
			}
			int strlen = str.length();
			if (startIndex <= -1 || endIndex <= -1 || startIndex > strlen || endIndex > strlen) {
				throw new ArrayIndexOutOfBoundsException(
						String.format("length=%s, off=%s, end=%s", str.length(), startIndex, endIndex));
			}
			if (endIndex > startIndex) {// 正序文本
				return str.substring(startIndex, endIndex);
			} else {// 倒序文本
				return reverse(str, endIndex, startIndex);
			}
		}
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
		XString.split(str, separator, list);
		return list;
	}

	public static void split(String str, String separator, Collection<String> splits) {
		if (XObjects.isEmpty(str) || XObjects.isEmpty(separator) || str.equals(separator)) {
			return;
		}
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
		if (str.length() == 0) {
			return "";
		}
		char newChar[] = XArray.repeat(str.toCharArray(), repeatLength);
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
		char newChar[] = new char[newLength];
		fillLeft(str.toCharArray(), fillstr, newChar);
		return new String(newChar);
	}

	public static void fillLeft(char[] str, char fillstr, char newChar[]) {
		if (newChar.length == str.length) {
			if (str == newChar) {
				return;
			}
			System.arraycopy(str, 0, newChar, 0, str.length);
			return;
		} else if (str.length == 0 && newChar.length == 0) {
			return;
		} else if (newChar.length == 0) {
			return;
		}
		if (newChar.length < str.length) {
			int strart = str.length - newChar.length;
			int end = str.length > newChar.length ? newChar.length : str.length;
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
		char newChar[] = new char[newLength];
		fillRight(str.toCharArray(), fillstr, newChar);
		return new String(newChar);
	}

	public static void fillRight(char[] str, char fillstr, char newChar[]) {
		if (newChar.length == str.length || (str.length == 0 && newChar.length == 0)) {
			if (str == newChar) {
				return;
			}
			System.arraycopy(str, 0, newChar, 0, str.length);
			return;
		} else if (str.length == 0 && newChar.length == 0) {
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
		int len = end - off;
		if (len <= 0) {
			return "";
		}
		if (end > off) {
			XCharArrayWriter write = new XCharArrayWriter(len);
			for (int i = end - 1; i >= off; i--) {
				write.write(str.charAt(i));
			}
			String r = new String(write.toCharArray());
			write.close();
			return r;
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
	public static String retain(CharSequence str, int off, int len, char[] retain) throws NullPointerException {
		XCharArrayWriter retains = new XCharArrayWriter();
		try {
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
		} finally {
			retains.releaseBuffer();
			retains.close();
		}
	}




	/**
	 * 替换文本
	 * replace string
	 * @param str string
	 * @param target need replace substrings
	 * @param replacement replacement
	 */
	public static CharSequence replace(CharSequence str, CharSequence[] target, CharSequence replacement) {
		if (null == str) { return null; }
		if (str.equals("")) { return ""; }
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
			int st = lastIndex;
			int et = len;
			sb.append(str, st, et);
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
		if (null == str) { return null; }
		if (str.equals("")) { return ""; }
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

						sb.append(str, st, strIndex).append(replacement[targetIndex]);

						lastIndex = et;
						strIndex += target[targetIndex].length();

						strIndex -= 1;// for (offset for self-increment)
					}
				}
			}
		}
		if (lastIndex != len) {
			int st = lastIndex;
			int et = len;
			sb.append(str, st, et);
		}
		return sb.toString();
	}
}
