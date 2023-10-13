package top.fols.box.util;


import java.util.Comparator;
import top.fols.atri.assist.util.ArrayLists;
import top.fols.atri.assist.lang.StringBuilders;
import top.fols.atri.io.Delimiter;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Mathz;
import top.fols.atri.util.Lists;

public class StringQueryFactory {
	private final String oldContent;
	private final StringBuilders contentBuilder;

	public StringQueryFactory(String content) {
		if (null == content)
			throw new NullPointerException("content cannot be null");
		this.oldContent     = content;
		this.contentBuilder = new StringBuilders(content);
	}

	public long contentVersion() {
		return contentBuilder.modCount();
	}
	public int length() {
		return contentBuilder.length();
	}
	public CharSequence content() {
		return contentBuilder;
	}

	long queryModCount;

	private static final Group[] EMPTY_ARRAY_GROUP = {};
	public  static class Group {
		private StringQueryFactory root;
		private boolean deprecated = false;
		private long queryModCount;

		private int start;
		private int end;
		private int index = -1;

		private final String oldContent;
		private final int    oldLine;

		private Group(int start, int end, String content, int oldLine) {
			if (end - start != content.length()) {
				throw new UnsupportedOperationException("error str length");
			}
			this.start = start;
			this.end = end;
			this.oldContent = content;
			this.oldLine = oldLine;
		}


		private long contentVersion = 0;
		private int line;

		void update() {
			if (contentVersion != root.contentVersion()) {
				line = new StringLineFinder(root.content()).find(start);
				contentVersion  = root.contentVersion();
			}
		}

		public int findLine() {
			if (deprecated)
				return oldLine;
			if (root.queryModCount != queryModCount)
				return oldLine;
			update();
			return line;
		}
		public String getOriginalString() {
			return oldContent;
		}

		public boolean isDeprecated() { return deprecated; }
		public int start() { return start; }
		public int end() { return end; }

		@Override
		public String toString() {
			// TODO: Implement this method
//			String content = getOriginalString().replaceAll("$\\s*", " ");
//			int maxShow = 96;
//			if (content.length() > maxShow) {
//				content = content.substring(maxShow) + "......";
//			}
			return "{" + "sf=" + (null == root ?"": root).hashCode() + "(Length:" + (null == root ?-1: root.length()) + ")" + ", Line:" + findLine() + ", Index:" + "[" + start() + "-" + end() + "]" + ", Content=" + getOriginalString() + "}";
		}
	}


	private final GroupList groupList = new GroupList();
	public static class GroupList {
		private final ArrayLists<Group> gList = new ArrayLists<Group>(EMPTY_ARRAY_GROUP);
		private long mocCount;

		Group[] toArray() { return gList.toArray(); }

		Group remove(int i) {
			try {
				return gList.remove(i);
			} finally {
				mocCount ++;
			}
		}

		void sort() {
			try {
				Lists.sort(gList, new Comparator<Group>() {
					@Override
					public int compare(StringQueryFactory.Group p1, StringQueryFactory.Group p2) {
						// TODO: Implement this method
						return Mathz.compareAsLeftMinToRightMax(p1.start, p2.start);
					}
				});
			} finally {
				mocCount++;
			}
		}

		public void add(Group gp) {
			try {
				gList.add(gp);
			} finally {
				mocCount++;
			}
		}

		public Group get(int i)    {
			return gList.get(i);
		}
		public int size() {
			return gList.size();
		}

		public Group newGroup(int start, int end,
							  String str, int line) { return new Group(start, end,
				str, line); }
	}
	public GroupList newGroupList() {
		return new GroupList();
	}
	protected void setQueryGroupList(GroupList list) {
		if (null == list) {
			throw new NullPointerException();
		}
		//index sort
		groupList.sort();
		queryModCount++;
		int last = 0;
		for (int i = 0; i < list.size(); i++) {
			Group gp = list.get(i);
			if (gp.root != null) {
				throw new UnsupportedOperationException("group already bind");
			}
			int st = gp.start;
			int ed = gp.end;
			if (st < 0 || ed < 0 || st > ed)
				throw new UnsupportedOperationException("error index");
			if (st >= last) {
				gp.root = this;
				gp.queryModCount = queryModCount;
				gp.index = i;

				last = ed;
				groupList.add(gp);
			} else {
				throw new UnsupportedOperationException("pointer backtracking");
			}
		}
	}




	int findGroup(Group g) {
		int lastIndex = g.index;
		int size = groupList.size();
		if (lastIndex >= 0 && lastIndex < size) {//use cache
			if  (groupList.get(lastIndex) == g) {
				return lastIndex;
			}
		}
		//rewrite cache
		int find = -1;
		for (int i = 0; i < size; i++) {
			Group e;
			if  ((e = groupList.get(i)) == g)
				if (find <= -1)
					find = i;
			e.index = i;
		}
		return find;
	}

	public boolean delete(Group g) {
		if (g.deprecated)
			throw new UnsupportedOperationException("deprecated group");
		if (g.root == this && g.queryModCount == this.queryModCount) {
			int index = findGroup(g);
			if (index > -1) {
				int length = (g.end - g.start);
				if (groupList.remove(index) != null) {
					g.deprecated = true;
					for (int i = index; i < groupList.size(); i++) {
						Group g2 = groupList.get(i);
						g2.start -= length;
						g2.end   -= length;
						g2.index--;
					}
				} else {
					throw new UnsupportedOperationException("remove group error");
				}
				contentBuilder.delete(g.start, g.end);
				return true;
			}
			return false;
		} else {
			throw new UnsupportedOperationException("query changed");
		}
	}
	public boolean replace(Group g, String newContent) {
		if (g.deprecated)
			throw new UnsupportedOperationException("deprecated group");
		if (g.root == this && g.queryModCount == this.queryModCount) {
			int index = findGroup(g);
			if (index > -1) {
				if (g.getOriginalString().equals(newContent)) {
					return true;
				} else {
					int length = newContent.length() - (g.end - g.start);
					if (groupList.remove(index) != null) {
						g.deprecated = true;
						for (int i = index; i < groupList.size(); i++) {
							Group g2 = groupList.get(i);
							g2.start += length;
							g2.end   += length;
							g2.index--;
						}
					} else {
						throw new UnsupportedOperationException("remove group error");
					}
					contentBuilder.replace(g.start, g.end, newContent);
					return true;
				}
			}
			return false;
		} else {
			throw new UnsupportedOperationException("query changed");
		}
	}


	public boolean insertToHead(String newContent) {
		contentBuilder.insert(0, newContent);
		int length = newContent.length();
		if (length > 0) {
			for (int i = 0; i < groupList.size(); i++) {
				Group g2 = groupList.get(i);
				g2.start += length;
				g2.end   += length;
			}
		}
		return true;
	}
	public boolean insertToTail(String build) {
		contentBuilder.insert(contentBuilder.length(), build);
		return true;
	}

	public boolean insertAfter(Group g, String newContent) {
		if (g.deprecated)
			throw new UnsupportedOperationException("deprecated group");
		if (g.root == this && g.queryModCount == this.queryModCount) {
			int index = findGroup(g);
			if (index > -1) {
				int insert = g.end;
				int length = newContent.length();
				if (length > 0) {
					for (int i = index + 1; i < groupList.size(); i++) {
						Group g2 = groupList.get(i);
						g2.start += length;
						g2.end   += length;
					}
					contentBuilder.insert(insert, newContent);
				}
				return true;
			}
			return false;
		} else {
			throw new UnsupportedOperationException("query changed");
		}
	}
	public boolean insertBefore(Group g, String newContent) {
		if (g.deprecated)
			throw new UnsupportedOperationException("deprecated group");
		if (g.root == this && g.queryModCount == this.queryModCount) {
			int index = findGroup(g);
			if (index > -1) {
				int insert = g.start;
				int length = newContent.length();
				if (length > 0) {
					for (int i = index; i < groupList.size(); i++) {
						Group g2 = groupList.get(i);
						g2.start += length;
						g2.end   += length;
					}
					contentBuilder.insert(insert, newContent);
				}
				return true;
			}
			return false;
		} else {
			throw new UnsupportedOperationException("query changed");
		}
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		return contentBuilder.toString();
	}

	public String getOriginalContent() {
		return oldContent;
	}

	public GroupList lines() {
		return updateSfGroupListForLine(this);
	}

	static final Delimiter.ICharsDelimiter lineDelimiter = Finals.LineSeparator.lineCharDelimit();
	static final char[][] lineDelimiterSeparators = lineDelimiter.cloneSeparators();
	public static GroupList updateSfGroupListForLine(StringQueryFactory sf) {
		final String oldContent = sf.content().toString();
		final StringBuilders cb = new StringBuilders(oldContent);
		final StringQueryFactory.GroupList groupList = sf.newGroupList();

		StringLineFinder lineFinder = new StringLineFinder(cb);
		int length = cb.length();
		int last = 0;
		for (int i = 0; i < length; i++) {
			int sIndex = lineDelimiter.assertSeparator(cb, i, length);
			if (sIndex != -1) {
				int st = last;
				int ed = i;
				last = ed + lineDelimiterSeparators[sIndex].length;

				int line = lineFinder.find(st);
				String group2 = cb.subSequence(st, ed).toString();
				StringQueryFactory.Group group = groupList.newGroup(st, ed,
						group2, line);
				groupList.add(group);
			}
		}
		if (last != length) {
			int st = last;
			int ed = length;

			int line = lineFinder.find(st);
			String group2 = cb.subSequence(st, ed).toString();
			StringQueryFactory.Group group = groupList.newGroup(st, ed,
					group2, line);
			groupList.add(group);
		}
		sf.setQueryGroupList(groupList);
		return sf.groupList;
	}


}
