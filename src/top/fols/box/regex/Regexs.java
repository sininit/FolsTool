package top.fols.box.regex;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.atri.lang.Finals;

/**
 * default flag {@code Pattern.MULTILINE | Pattern.DOTALL}
 */
public class Regexs {
	static final Map<Regex, Pattern> map = new WeakHashMap<>();//写锁
	public static final int DEFAULT_FLAGS = Pattern.MULTILINE | Pattern.DOTALL;

	static class Regex {
		String regex; 
		int flag;

		public Regex(String regex) {
			this.regex = regex;
		}
		public Regex(String regex, int flag) {
			this.regex = regex;
			this.flag = flag;
		}

		@Override
		public int hashCode() {
			// TODO: Implement this method
			return regex.hashCode() + flag;
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (!(obj instanceof Regex)) {
				return false;
			}
			Regex  instance = (Regexs.Regex) obj;
			return  instance.regex.equals(this.regex) &&
					instance.flag == this.flag;
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return "" + regex;
		}


	}
	static Pattern query(Regex e) {
		Pattern pattern = map.get(e);
		if (null != pattern) 
			return  pattern;

		synchronized (map) {
			map.put(e, pattern = Pattern.compile(e.regex, e.flag));
		}
		return pattern;
	}






	/**
     * Compiles the given regular expression into a pattern.  </p>
     *
     * @param  regex
     *         The expression to be compiled
     */
    public static Pattern compile(String regex) {
        return compile(regex, DEFAULT_FLAGS);
    }

    /**
     * Compiles the given regular expression into a pattern with the given
     * flags.  </p>
     *
     * @param  regex
     *         The expression to be compiled
     *
     * @param  flags
     *         Match flags, a bit mask that may include
     *         {@link Pattern#CASE_INSENSITIVE}, {@link Pattern#MULTILINE}, {@link Pattern#DOTALL},
     *         {@link Pattern#UNICODE_CASE}, {@link Pattern#CANON_EQ}, {@link Pattern#UNIX_LINES},
     *         {@link Pattern#LITERAL}, {@link Pattern#UNICODE_CHARACTER_CLASS}
     *         and {@link Pattern#COMMENTS}
     *
     * @throws  IllegalArgumentException
     *          If bit values other than those corresponding to the defined
     *          match flags are set in <tt>flags</tt>
     *
     * @throws  java.util.regex.PatternSyntaxException
     *          If the expression's syntax is invalid
     */
    public static Pattern compile(String regex, int flags) {
        return query(new Regex(regex, flags));
    }




	public static Matcher matches(String content, String regex) {
		return  matches(content, regex, DEFAULT_FLAGS);
	}
	public static Matcher matches(String content, String regex, int flag) {
		Pattern pattern = compile(regex, flag);
		return  pattern.matcher(content);
	}

















	/**
	 * 只搜索一次 并返回所有子模式 () 匹配到的值
	 * subpatterns("12-3,12-3,45-6,4-56,"
	 , "(.*?)-(.*?),")
	 * [[12, 3]]
	 */
	public static String[] subgroups(String content, String regex) {
		return subgroups(content, Regexs.compile(regex, DEFAULT_FLAGS));
	}
	public static String[] subgroups(String content, Pattern regex) {
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

	/**
	 * 一直搜索 并返回所有子模式 () 匹配到的值 
	 * 
	 * subpatterns_all("12-3,12-3,45-6,4-56,"
		    , "(.*?)-(.*?),")
	 * [[12, 3], [12, 3], [45, 6], [4, 56]]
	 */
	public static String[][] subgroupsAll(String content, String regex) {
		return subgroupsAll(content, Regexs.compile(regex, DEFAULT_FLAGS));
	}
	public static String[][] subgroupsAll(String content, Pattern regex) {
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








	/**
	 * 只搜索一次 并返回指定子模式 () 匹配到的值
	 * subpattern("12-3,12-3,45-6,4-56,"
	 , "(.*?)-(.*?),",1)
	 * "12"
	 */
	public static String subgroup(String content, String regex, int subpattern) {
		return subgroup(content, Regexs.compile(regex, DEFAULT_FLAGS), subpattern);
	}
	public static String subgroup(String content, Pattern pattern, int subpattern) {
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			int count = matcher.groupCount();
			if (count >= subpattern) {
				return matcher.group(subpattern);
			}
		}
		return null;
	}


	/**
	 * 一直搜索 并返回所有指定子模式 () 匹配到的值 
	 * subpattern_all("12-3,12-3,45-6,4-56,"
		    , "(.*?)-(.*?),",1)
	 * [12, 12, 45, 4]
	 */
	public static String[] subgroupAll(String content, String regex, int subpattern) {
		return subgroupAll(content, Regexs.compile(regex, DEFAULT_FLAGS), subpattern);
	}
	public static String[] subgroupAll(String content, Pattern regex, int subpattern) {
		List<String> results = new ArrayList<>();
		Matcher matcher = regex.matcher(content);
		while (matcher.find()) {
			int count = matcher.groupCount();
			if (count >= subpattern) {
				results.add(matcher.group(subpattern));
			}
		}
		return results.toArray(Finals.EMPTY_STRING_ARRAY);
	}







	/**
	 * 只搜索一次 并返回group() 匹配到的值
	 * group("12-3,12-3,45-6,4-56,"
	 , "(.*?)-(.*?),")
	 * "12-3,"
	 */
	public static String group(String content, String regex) {
		return group(content, Regexs.compile(regex, DEFAULT_FLAGS));
	}
	public static String group(String content, Pattern pattern) {
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}
	/**
	 * 一直搜索 并返回所有group() 匹配到的值 
	 * group_all("12-3,12-3,45-6,4-56,"
		    , "(.*?)-(.*?),")
	 *["12-3,", "12-3,", "45-6,", "4-56,"]
	 */
	public static String[] groupAll(String content, String regex) {
		return groupAll(content, Regexs.compile(regex, DEFAULT_FLAGS));
	}
	public static String[] groupAll(String content, Pattern regex) {
		List<String> results = new ArrayList<>();
		Matcher matcher = regex.matcher(content);
		while (matcher.find()) {
			results.add(matcher.group());
		}
		return results.toArray(Finals.EMPTY_STRING_ARRAY);
	}




	/**
	 * @return match group
	 */
	@Nullable
	public static String removeSubgroup(StringBuilder content, String regex, int subpattern) {
		return removeSubgroup(content, Regexs.compile(regex, DEFAULT_FLAGS), subpattern);
	}
	@Nullable
	public static String removeSubgroup(StringBuilder content, Pattern pattern, int subpattern) {
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			String old = matcher.group(subpattern);
			content.replace(matcher.start(subpattern), matcher.end(subpattern), "");
			return old;
		}
		return null;
	}

	/**
	 * @return match group
	 */
	@Nullable
	public static String removeGroup(StringBuilder content, String regex) {
		return removeGroup(content, Regexs.compile(regex, DEFAULT_FLAGS));
	}
	@Nullable
	public static String removeGroup(StringBuilder content, Pattern pattern) {
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			String old = matcher.group();
			content.replace(matcher.start(), matcher.end(), "");
			return old;
		}
		return null;
	}
}
