package top.fols.box.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.fols.atri.assist.json.JSONObject;
import top.fols.atri.assist.lang.StringBuilders;
import top.fols.atri.interfaces.annotations.Private;
import top.fols.atri.lang.Finals;
import top.fols.box.util.StringQueryFactory;
import top.fols.box.util.StringLineFinder;

@SuppressWarnings({"FieldCanBeLocal", "UnnecessaryInterfaceModifier", "unused", "UnusedLabel", "UnnecessaryLabelOnContinueStatement"})
@Deprecated
public class StringRegexFactory  {


	final static class MyStringQueryFactory extends StringQueryFactory {
		MyStringQueryFactory(String content) { super(content); }

		@Override
		protected void setQueryGroupList(StringQueryFactory.GroupList p1) {
			// TODO: Implement this method
			super.setQueryGroupList(p1);
		}
	}
	final MyStringQueryFactory sf;

	public StringRegexFactory(String content) {
		sf = new MyStringQueryFactory(content);
	}


	public long contentVersion() {
		return sf.contentVersion();
	}
	public int length() {
		return sf.length();
	}
	public CharSequence content() {
		return sf.content();
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		return sf.toString();
	}

	public String getOriginalContent() {
		return sf.getOriginalContent();
	}




	public static interface Query  {  }
	public static interface Result {
		public String buildContent();
	}









	public static abstract class ResultBuilder {
		public abstract RegexResult build(RegexQuery query, StringQueryFactory.Group group, String matchLine);
	}
	public static abstract class RegexQuery implements Query {
		int flag;
		public RegexQuery() { this(Regexs.DEFAULT_FLAGS); }
		public RegexQuery(int flag) { this.flag = flag; }

		public int patternFlag() { return flag; }

		Map<String, ResultBuilder> matchRegex = new LinkedHashMap<>();
		public void addMatchLineRegex(String matchRegex, ResultBuilder q) {
			this.matchRegex.put(matchRegex, q);
		}
		public Map<String, ResultBuilder> getMatchLineRegex() {
			return matchRegex;
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return new JSONObject()
					.put("matchRegex", matchRegex)
					.toString();
		}
	}


//	static public ClassProperties defaultFactory() {
//		return DEFAULT_ATTRIBUTE_FACTORY;
//	}
//	static public void defaultFactory(ClassProperties factory) {
//		if (null == factory)
//			throw new NullPointerException("factory");
//		DEFAULT_ATTRIBUTE_FACTORY = factory;
//	}
//	static ClassProperties DEFAULT_ATTRIBUTE_FACTORY = new ClassProperties();


	public static abstract class RegexResult implements Result {
		public RegexResult(RegexQuery query, StringQueryFactory.Group group) {
			if (group.isDeprecated())
				throw new IllegalArgumentException("is deprecated group");

			this.regexQuery = query;
			this.group = group;
		}

		private final StringQueryFactory.Group group;



		private final RegexQuery regexQuery;
		public RegexQuery getRegexQuery() { return regexQuery; }  //@MethodMapping

		public int getLine()  { return group.findLine(); } //@MethodMapping

		public int getStart() { return group.start(); }    //@MethodMapping

		public int getEnd()   { return group.end(); }      //@MethodMapping

		public String getLineContent() { return group.getOriginalString(); }

		@Override
		public String toString() {
			return "RegexResult{" +
					"group=" + group +
					", regexQuery=" + regexQuery +
					", start=" + getStart() +
					", end="   + getEnd() +
					", line="  + getLine() +
					'}';
		}
	}







	static List<RegexResult> updateSfGroupListOrMode(MyStringQueryFactory sf, RegexQuery p) {
		final String oldContent = sf.content().toString();
		final StringLineFinder lineFinder = new StringLineFinder(oldContent);
		final StringQueryFactory.GroupList groupList = sf.newGroupList();

		Map<String, ResultBuilder>  builder = p.getMatchLineRegex();
		String[]  regexs = builder.keySet().toArray(Finals.EMPTY_STRING_ARRAY);
		Pattern[] patterns = new Pattern[regexs.length];
		for (int i = 0; i < regexs.length; i++)
			patterns[i] = Regexs.compile(regexs[i], p.patternFlag());

		List<RegexResult> result = new ArrayList<>();

		Matcher matcher = Regexs.compile(buildOrRegex(regexs), p.patternFlag()).matcher(oldContent);
		FIND: while (matcher.find()) {
			int st = matcher.start();
			int ed = matcher.end();
			int len = ed - st;
			int line = lineFinder.find(st);
			String groupContent = matcher.group();
			MATCH: for (int i = 0; i < patterns.length; i++) {
				Pattern pattern        = patterns[i];
				Matcher patternMatcher = pattern.matcher(groupContent);
				if (patternMatcher.find() && patternMatcher.end() - patternMatcher.start() == len) {
					String lineRegex = regexs[i];
					ResultBuilder sq = builder.get(lineRegex);
					if (sq != null) {
						StringQueryFactory.Group group = groupList.newGroup(st, ed,
								groupContent, line);
						RegexResult myResult = sq.build(p, group, groupContent);
						if (null != myResult) {
							groupList.add(group);
							result.add(myResult);

							continue MATCH;
						}
					}
				}
			}
		}
		sf.setQueryGroupList(groupList);
		return result;
	}

	public List<RegexResult> lookup(RegexQuery p) {
		// TODO: Implement this method
		return updateSfGroupListOrMode(sf, p);
	}


	public boolean delete(RegexResult g) {
		return sf.delete(g.group);
	}
	public boolean replace(RegexResult g, String newContent) {
		return sf.replace(g.group, newContent);
	}


	public boolean insertToHead(String newContent) {
		return sf.insertToHead(newContent);
	}
	public boolean insertToTail(String build) {
		return sf.insertToTail(build);
	}

	public boolean insertAfter(RegexResult g, String newContent) {
		return sf.insertAfter(g.group, newContent);
	}
	public boolean insertBefore(RegexResult g, String newContent) {
		return sf.insertBefore(g.group, newContent);
	}





	@Deprecated
	static String buildOrRegex(String... regexList) {
		StringBuilder sb = new StringBuilder();
		String st = "(", ed = ")", se = "|";
		for (String pattern: regexList) {
			sb.append(st).append(pattern).append(ed).append(se);
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - se.length());
		}
		return sb.toString();
	}



	/**
	 * no longer use the least efficient method
	 */
	@Deprecated
	static final char UNABLE_CHAR = '\0';//i hope the regular expression doesn't find it
	@Private
	@Deprecated
	static List<RegexResult> updateSfGroupListPriorityMode(MyStringQueryFactory sf, RegexQuery p) {
		final String oldContent = sf.content().toString();
		final StringBuilders cb = new StringBuilders(oldContent);
		final boolean[] unables = new boolean[cb.length()];
		final StringQueryFactory.GroupList groupList = sf.newGroupList();
		final StringLineFinder lineFinder = new StringLineFinder(cb);

		List<RegexResult> result = new ArrayList<>();
		Map<String, ResultBuilder> builder = p.getMatchLineRegex();
		for (String lineRegex: builder.keySet()) {
			Matcher matcher = Regexs.compile(lineRegex, p.patternFlag()).matcher(cb);
			while  (matcher.find()) {
				ResultBuilder sq = builder.get(lineRegex);
				if (sq != null) {
					int st = matcher.start();
					int ed = matcher.end();
					int len1 = ed - st;
					String group1 = matcher.group();

					//allowed trim
					while ((st < ed) && (unables[st]))
						st++;
					while ((st < ed) && (unables[ed - 1]))
						ed--;
					boolean check = true;
					for (int i = st; i < ed;i++) {
						if (unables[st]) {
							check = false;//the middle has been used!
						}
					}
					if (check) {
						int len2 = ed - st;
						String group2 = len1 == len2 ? group1 : cb.subSequence(st, ed).toString();//isString
						if (len1 == 0 || len2 != 0) {
							int line = lineFinder.find(st);
							StringQueryFactory.Group group = groupList.newGroup(st, ed,
									group2, line);
							RegexResult myResult = sq.build(p, group, group2);
							if (null != myResult) {
								groupList.add(group);

								//set states
								cb.replace(st, ed,
										UNABLE_CHAR);
								Arrays.fill(unables, st, ed,
										true);
								result.add(myResult);
							}
						}
					}
				}
			}
		}
		sf.setQueryGroupList(groupList);
		return result;
	}









}



//
//
//	public static void test() {
//		System.out.println("=========");
//		String content = Filez.wrap("/sdcard/1.jsp").readDetectCharsetString();
//
//		StringQueryFactory sf = new StringQueryFactory(content);
//		StringQueryFactory.GroupList  groupList = sf.lines();
//		System.out.println(Arrays.asList(groupList.toArray()));
//		sf.replace(groupList.get(0), groupList.get(0).getOriginalString() + "你好");
////		System.out.println(sf.delete(sf.groupList.get(0)));
////		System.out.println(sf.delete(sf.groupList.get(1)));
////		System.out.println(sf.replace(sf.groupList.get(0), "666"));
//		System.out.println(Arrays.asList(groupList.toArray()));
//		sf.insertAfter(groupList.get(0), "777");
//		System.out.println(Arrays.asList(groupList.toArray()));
//		sf.insertToTail("666");
//		System.out.println(Arrays.asList(groupList.toArray()));
//
//		System.out.println(sf);
//	}
