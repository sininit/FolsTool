package top.fols.atri.regex;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import top.fols.atri.lang.Finals;

public class Regexs {

	/*
	 * 快缓存
	 */

	static final Map<Regex, Pattern> map = new WeakHashMap<>();
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
			return instance.equals(this.regex) && instance.flag == this.flag;
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

		map.put(e, pattern = Pattern.compile(e.regex, e.flag));
		return pattern;
	}






	/**
     * Compiles the given regular expression into a pattern.  </p>
     *
     * @param  regex
     *         The expression to be compiled
     *
     * @throws  PatternSyntaxException
     *          If the expression's syntax is invalid
     */
    public static Pattern compile(String regex) {
        return query(new Regex(regex));
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




	public static Matcher matches(String regex, String content) {
		Pattern pattern = compile(regex);
		return  pattern.matcher(content);
	}
	public static Matcher matches(String regex, int flag, String content) {
		Pattern pattern = compile(regex, flag);
		return  pattern.matcher(content);
	}

















	/**
	 * 一直搜索 并返回所有子模式 () 匹配到的值 
	 */
	public static String[][] subpatterns_all(String content, String regex) {
		return subpatterns_all(content, regex, Pattern.MULTILINE);
	}
	public static String[][] subpatterns_all(String content, String regex, int flag) {
		Pattern pattern = Regexs.compile(regex, flag);
		return subpatterns_all(content, pattern);
	}
	public static String[][] subpatterns_all(String content, Pattern regex) {
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
	 * 只搜索一次 并返回所有子模式 () 匹配到的值 
	 */
	public static String[] subpatterns(String content, String regex) {
		return subpatterns(content, regex, Pattern.MULTILINE);
	}
	public static String[] subpatterns(String content, String regex, int flag) {
		Pattern pattern = Regexs.compile(regex, flag);
		return subpatterns(content, pattern);
	}
	public static String[] subpatterns(String content, Pattern regex) {
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
	 * 一直搜索 并返回所有指定子模式 () 匹配到的值 
	 */
	public static String[] subpattern_all(String content, String regex, int subpattern) {
		return subpattern_all(content, regex, Pattern.MULTILINE, subpattern);
	}
	public static String[] subpattern_all(String content, String regex, int flag, int subpattern) {
		Pattern pattern = Regexs.compile(regex, flag);
		return subpattern_all(content, pattern, subpattern);
	}
	public static String[] subpattern_all(String content, Pattern regex, int subpattern) {
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
	 * 只搜索一次 并返回指定子模式 () 匹配到的值 
	 */
	public static String subpattern(String content, String regex, int subpattern) {
		return subpattern(content, regex, Pattern.MULTILINE, subpattern);
	}
	public static String subpattern(String content, String regex, int flag, int subpattern) {
		Pattern pattern = Regexs.compile(regex, flag);
		return subpattern(content, pattern, subpattern);
	}
	public static String subpattern(String content, Pattern pattern, int subpattern) {
		Matcher matcher = pattern.matcher(content);
		boolean find = matcher.find();
		if (find) {
			int count = matcher.groupCount();
			if (count >= subpattern) {
				return matcher.group(subpattern);
			}
		}
		return null;
	}


	
	
	
	
	/**
	 * 一直搜索 并返回所有group() 匹配到的值 
	 */
	public static String[] group_all(String content, String regex) {
		return group_all(content, regex, Pattern.MULTILINE);
	}
	public static String[] group_all(String content, String regex, int flag) {
		Pattern pattern = Regexs.compile(regex, flag);
		return group_all(content, pattern);
	}
	public static String[] group_all(String content, Pattern regex) {
		List<String> results = new ArrayList<>();
		Matcher matcher = regex.matcher(content);
		while (matcher.find()) {
			results.add(matcher.group());
		}
		return results.toArray(Finals.EMPTY_STRING_ARRAY);
	}


	/**
	 * 只搜索一次 并返回group() 匹配到的值 
	 */
	public static String group(String content, String regex) {
		return group(content, regex, Pattern.MULTILINE);
	}
	public static String group(String content, String regex, int flag) {
		Pattern pattern = Regexs.compile(regex, flag);
		return group(content, pattern);
	}
	public static String group(String content, Pattern pattern) {
		Matcher matcher = pattern.matcher(content);
		boolean find = matcher.find();
		if (find) {
			return matcher.group();
		}
		return null;
	}
}
