package top.fols.box.regex;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import top.fols.atri.lang.Objects;
import top.fols.atri.lang.Finals;

@Deprecated
public class StringRegexGroupsReplacer {
	final Map<String, RegexExpression> map = new LinkedHashMap<>();
	final int flag;

	public StringRegexGroupsReplacer() {
		this(Regexs.DEFAULT_FLAGS);
	}
	public StringRegexGroupsReplacer(int flag) {
		this.flag = flag;
	}
	static class RegexExpression {
		String name;
		Pattern pattern;
		public RegexExpression(String name, Pattern pattern) {
			this.name = name;
			this.pattern = pattern;
		}
	}


	public boolean containsMatch(String name) {
		return map.containsKey(name);
	}
	public void addMatch(String name, String regex) {
		Objects.requireNonNull(name,  "name");
		Objects.requireNonNull(regex, "regex");

		RegexExpression e = map.get(name);
		if (null == e) {
			e = new RegexExpression(name, Regexs.compile(regex, flag));
		}
		map.put(name, e);
	}
	public void removeMatch(String name) {
		map.remove(name);
	}
	public LineMatcher matcher(String content) {
		return new LineMatcher(content, flag, map);
	}


	public static class LineMatcher {
		/**
		 * 性能差 虽然使用没什么问题
		 */
		@Deprecated
		String buildOrRegex() {
			List<String> regexs = new ArrayList<>();
			for (String name: expressionMap.keySet()) {
				RegexExpression re = expressionMap.get(name);
				if (null != re) {
					regexs.add(re.pattern.pattern());
				}
			}
			return StringRegexFactory.buildOrRegex(regexs.toArray(Finals.EMPTY_STRING_ARRAY));
		}


		private final int flag;
		private final Map<String, RegexExpression> expressionMap = new LinkedHashMap<>();
		private final String oldContent;

		public LineMatcher(String content, int flag, Map<String, RegexExpression> map) {
			this.oldContent = content.toString();
			this.flag = flag;
			this.expressionMap.putAll(map);
			this.initialize();
		}

		public class ReplacerResult {
			int start;
			int end;
			final StringRegexSubgroupsReplacer replacer;
			public ReplacerResult(StringRegexSubgroupsReplacer replacer) {
				this.replacer = replacer;
			}

			@Override
			public String toString() {
				// TODO: Implement this method
				String content = Regexs.compile("$\\s*").matcher(oldContent).replaceAll(" ");
				return "Index:[" + start + "-" + end + "], " +
						"Replacer: " + "" + replacer.pattern() + " " + "(Group:" + replacer.getGroupCount() + ")" + " -> " + "'" + replacer.build() + "'" + "(Group:" + replacer.getGroupCount() + ")" + ", " +
						"Mathcer:'" + content;
			}
		}
		public class ReplacerResults {
			private final List<ReplacerResult> list;

			public ReplacerResults() {
				this.list = new ArrayList<>();
			}
			void add(ReplacerResult r) {
				list.add(r);
			}

			public int size() { return list.size(); }
			public StringRegexSubgroupsReplacer getReplacer(int i) {
				return list.get(i).replacer;
			}

			@Override
			public String toString() {
				// TODO: Implement this method
				return list.toString();
			}


			public int getSubgroupCount() {
				if (list.size() == 0) {
					throw new UnsupportedOperationException("not match");
				}
				return list.get(0).replacer.getGroupCount();
			}
		}


		private final List<ReplacerResult>          resultAll = new ArrayList<>();
		private final Map<String, ReplacerResults>  resultMap = new LinkedHashMap<>();
		private void initialize() {
			Pattern p = Regexs.compile(buildOrRegex(), flag);
			Matcher m = p.matcher(oldContent);
			while (m.find()) {
				String group = m.group();

				for (String expressionName: expressionMap.keySet()) {
					RegexExpression expression = expressionMap.get(expressionName);
					StringRegexSubgroupsReplacer replacer = new StringRegexSubgroupsReplacer(group, expression.pattern);
					if (replacer.isMatch()) {
						ReplacerResults list = resultMap.get(expressionName);
						if (null == list) {
							resultMap.put(expressionName, list = new ReplacerResults());
						}

						ReplacerResult r =  new ReplacerResult(replacer);
						r.start = m.start();
						r.end   = m.end();

						list.add(r);
						resultAll.add(r);
					}
				}
			}
		}

		public int getReplacerCount() { return this.resultMap.size(); }
		public ReplacerResults getReplacerResults(String name) {
			ReplacerResults list = this.resultMap.get(name);
			if (null == list) {
				throw new UnsupportedOperationException("no replacer: " + name);
			}
			return list;
		}
		public Set<String> getReplacerNameKeySet() { return this.resultMap.keySet(); }




		private int groupCount() { return resultAll.size(); }
		private int start(int g) { return resultAll.get(g - 1).start; }
		private int end(int g)   { return resultAll.get(g - 1).end;   }
		private String getReplace(int g) {
			StringRegexSubgroupsReplacer r = resultAll.get(g - 1).replacer;
			return r.isChanged() ? r.build() : null;
		}
		private String group(int g) {
			StringRegexSubgroupsReplacer r = resultAll.get(g - 1).replacer;
			return r.oldContent();
		}


		public String build() {
			StringBuilder sb = new StringBuilder();
			String gs;

			int groupCount = groupCount();
			int g = 1;
			sb.append(oldContent, 0, start(g));
			for (; g <= groupCount; g++) {
				String ms =  getReplace(g);
				if (null != ms)
					gs = ms;
				else
					gs = group(g);

				sb.append(gs);

				int next = g + 1;
				if (next <= groupCount) {
					int st = end(g);
					int ed = start(next);
					sb.append(oldContent, st, ed);
				}
			}
			sb.append(oldContent, end(groupCount), oldContent.length());
			return sb.toString();
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return build().toString();
		}
	}
}
