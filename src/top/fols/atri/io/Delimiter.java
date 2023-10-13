package top.fols.atri.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import top.fols.atri.assist.util.ArrayLists;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Mathz;
import top.fols.atri.lang.Strings;
import top.fols.box.lang.Arrayx;
import top.fols.box.util.encode.ByteEncoders;

@SuppressWarnings({"PointlessArithmeticExpression", "UnnecessaryInterfaceModifier", "UseCompareMethod"})
public class Delimiter {
	public static class HashChars {
		char[] chs; int hash;
		public HashChars(char[] chs) {
			this.chs = chs;
			this.hash = Arrays.hashCode(chs);
		}
		@Override public int     hashCode() { return hash; }
		@Override public boolean equals(Object obj) {
			if (!(obj instanceof HashChars)) return false;
			HashChars t = (HashChars) obj;
			return Arrays.equals(this.chs, t.chs);
		}
	}
	public static class HashBytes {
		byte[] chs; int hash;
		public HashBytes(byte[] chs) {
			this.chs = chs;
			this.hash = Arrays.hashCode(chs);
		}
		@Override public int     hashCode() { return hash; }
		@Override public boolean equals(Object obj) {
			if (!(obj instanceof HashBytes)) return false;
			HashBytes t = (HashBytes) obj;
			return Arrays.equals(this.chs, t.chs);
		}
	}



	public static ICharsDelimiter lineCharDelimit() {
		return Finals.LineSeparator.lineCharDelimit();
	}
	static int compare(int x, int y) {
		return (x < y) ? -1 : ((x == y) ? 0 : 1);
	}

	/**
	 *         Collection<String> split;
	 *         split = Strings.split(",2,", ",");
	 *         System.out.println(split);
	 *         split = Strings.split("2", ",");
	 *         System.out.println(split);
	 *         split = Strings.split(",2", ",");
	 *         System.out.println(split);
	 *         split = Strings.split(",2,", ",");
	 *         System.out.println(split);
	 *         split = Strings.split(",2,,", ",");
	 *         System.out.println(split);
	 *         split = Strings.split("", ",");
	 *         System.out.println(split);
	 *
	 *         //[, 2, ]
	 *         //[2]
	 *         //[, 2]
	 *         //[, 2, ]
	 *         //[, 2, , ]
	 *         //1
	 */
	public static List<String> splitToStringLists(String content, Delimiter.ICharsDelimiter delimiter) {
		if (content.length() > 0) {
			Delimiter.ICharsDelimiterReader reader = new StringReaders(content);
			reader.setDelimiter(delimiter);
			return splitToStringLists(reader);
		} else {
			return new ArrayList<>();
		}
	}
	public static List<String> splitToStringLists(Delimiter.ICharsDelimiterReader reader) {
		try {
			return splitToStringLists((ICharsDelimiterReaderIO) reader);
		} catch (IOException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	public static List<String> splitToStringLists(Delimiter.ICharsDelimiterReaderIO reader) throws IOException {
		List<String> list = new ArrayList<>();
		String next;
		while (true) {
			boolean lastIsReadReadSeparator = reader.lastIsReadReadSeparator();
			if (null == (next = reader.readNextLineAsString(false))) {
				if (lastIsReadReadSeparator) {
					list.add(Strings.EMPTY);
				}
				break;
			}
			list.add(next);
		}
		return list;
	}

	static void ________________________(){}

	//------------------------------------------------------------------------

	public static char[] convertAsChars(byte[] data, Charset charset) {
		return ByteEncoders.bytesToChars(data, 0, data.length, charset);
	}
	public static char[][] convertAsChars(byte[][] element, Charset charset) {
		char[][] result = new char[element.length][];
		for (int i = 0; i < element.length; i++) {
			result[i] = ByteEncoders.bytesToChars(element[i], 0, element[i].length, charset);
		}
		return result;
	}

	public static List<char[]> split(char[] bytes, Delimiter.ICharsDelimiter delimiter) {
		return split(bytes, 0, bytes.length, delimiter);
	}
	public static List<char[]> split(char[] bytes, int off, int len, Delimiter.ICharsDelimiter delimiter) {
		if (len > 0) {
			Delimiter.ICharsDelimiterReader reader = new CharsReaders(bytes, off, len);
			reader.setDelimiter(delimiter);
			return split(reader);
		} else {
			return new ArrayList<>();
		}
	}
	public static List<char[]> split(Delimiter.ICharsDelimiterReader reader) {
		try {
			return split((ICharsDelimiterReaderIO) reader);
		} catch (IOException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	public static List<char[]> split(Delimiter.ICharsDelimiterReaderIO reader) throws IOException {
		ArrayLists<char[]> list = new ArrayLists<>(EMPTY_CHAR_ARRAY_2D);
		char[] next;
		while (true) {
			boolean lastIsReadReadSeparator = reader.lastIsReadReadSeparator();
			if (null == (next = reader.readNextLine(false))) {
				if (lastIsReadReadSeparator) {
					list.add(Finals.EMPTY_CHAR_ARRAY);
				}
				break;
			}
			list.add(next);
		}
		return list;
	}

	static final char[][] EMPTY_CHAR_ARRAY_2D = {};
	static final int CHAR_INDEX_OFFSET = 0 - Character.MIN_VALUE;
	public static final int CHAR_COUNT = Character.MAX_VALUE + 1;

	@SuppressWarnings("ConstantConditions")
	public static class CharsDelimiterHeads {
		static final int MAPPING_INDEX_OFFSET = CHAR_INDEX_OFFSET;
		static final int MAPPING_COUNT        = CHAR_COUNT;

		static class Head {
			int minLength, maxLength;
			int count;
		}
		Head[] separatorListMapping = new Head[MAPPING_COUNT];

		public static CharsDelimiterHeads extractHead(char[][] s) {
			CharsDelimiterHeads instance = new CharsDelimiterHeads();
			for (char[] chars: s) {
				if (chars.length == 0)
					throw new UnsupportedOperationException("contains empty array");
				int mappingIndex = MAPPING_INDEX_OFFSET + chars[0];
				Head h = instance.separatorListMapping[mappingIndex];
				if (null == h)
					h = instance.separatorListMapping[mappingIndex] = new Head();
				h.minLength = h.minLength == 0 ? chars.length: Mathz.min(h.minLength, chars.length);
				h.maxLength = Mathz.max(h.maxLength, chars.length);
				h.count ++;
			}
			return instance;
		}
		public boolean isHead(char ch) 		 { return null != this.separatorListMapping[MAPPING_INDEX_OFFSET + ch]; }
		public int headMatchMinLength(char ch) { return this.separatorListMapping[MAPPING_INDEX_OFFSET + ch].minLength; }
		public int headMatchMaxLength(char ch) { return this.separatorListMapping[MAPPING_INDEX_OFFSET + ch].maxLength; }
	}


	public static interface ICharsDelimiterReaderIO {
		public ICharsDelimiter getDelimiter();
		public void setDelimiter(ICharsDelimiter delimiter);
		public void setDelimiter(char[][] delimiter);
		public void setDelimiter(String[] delimiter);
		public void setDelimiterAsLine();

		/**
		 * 如果最后一次next返回的是分隔符 则return >= 0
		 */
		public boolean lastIsReadReadSeparator();
		public int     lastReadSeparatorIndex();
		public int     lastReadSeparatorLength();
		public char[]  lastReadSeparator();


		public boolean findNext() throws IOException;

		/**
		 * 返回分隔符 或者分隔符之前的内容
		 */
		public char[] readNext() throws IOException;
		public String readNextAsString() throws IOException;

		//------------copy---------
		//content + (resultAddSeparator?separator:[])
		public char[] readNextLine(boolean resultAddSeparator) throws IOException;

		//content + (resultAddSeparator?separator:[])
		public String readNextLineAsString(boolean resultAddSeparator) throws IOException;
	}
	public static interface ICharsDelimiterReader extends ICharsDelimiterReaderIO {
		@Override public boolean findNext();
		@Override public char[] readNext();
		@Override public String readNextAsString() ;
		@Override public char[] readNextLine(boolean resultAddSeparator) ;
		@Override public String readNextLineAsString(boolean resultAddSeparator) ;
	}

	public static abstract class ICharsDelimiter {
		public abstract boolean  	isHead(char ch);
		public abstract int 		headMatchMaxLength(char ch);
		public abstract int 		headMatchMinLength(char ch);

		public abstract int      assertSeparator(char[] data,       int dataOffset, int dataLimit);
		public abstract int      assertSeparator(CharSequence data, int dataOffset, int dataLimit);

		public char[]   cloneSeparator(int index) { return innerSeparator(index).clone(); }
		public char[][] cloneSeparators() { return Delimiter.clone(innerSeparators()); }

		protected abstract char[]   innerSeparator(int index);
		protected abstract char[][] innerSeparators();

	}

	public static char[][] clone(char[][] separatorCache) {
		int len = separatorCache.length;
		char[][] data = new char[len][];
		for (int i = 0;i < len;i++) {
			data[i] = separatorCache[i].clone();
		}
		return data;
	}

	public static ICharsDelimiter build(String[] separators) {
		int length = separators.length;
		if (length == 1 && separators[0].length() == 1) {
			return singleCharDelimit(separators[0].charAt(0));
		}
		if (length < 4) {
			CharsSimpleDelimiterBuilder simple = new CharsSimpleDelimiterBuilder();
			simple.addAll(separators);
			return simple.build();
		} else {
			CharsMappingDelimiterBuilder a = new CharsMappingDelimiterBuilder();
			a.addAll(separators);
			return a.build();
		}
	}
	public static ICharsDelimiter build(char[][] separators) {
		int length = separators.length;
		if (length == 1 && separators[0].length == 1) {
			return singleCharDelimit(separators[0][0]);
		}
		if (length < 4) {
			CharsSimpleDelimiterBuilder simple = new CharsSimpleDelimiterBuilder();
			simple.addAll(separators);
			return simple.build();
		} else {
			CharsMappingDelimiterBuilder a = new CharsMappingDelimiterBuilder();
			a.addAll(separators);
			return a.build();
		}
	}

	public static ICharsDelimiter singleCharDelimit(final char separator) {
		return new ICharsDelimiter() {
			final char[][] SEPARATOR = {{separator}};
			final int minLength = 1, maxLength = 1;

			@Override
			public boolean isHead(char ch) {
				// TODO: Implement this method
				return ch == separator;
			}
			@Override public int headMatchMinLength(char ch) { return 1; }
			@Override public int headMatchMaxLength(char ch) { return 1; }

			@Override
			public int assertSeparator(char[] data, int dataOffset, int dataLimit) {
				return data[dataOffset] == separator ? 0 : -1;
			}
			@Override
			public int assertSeparator(CharSequence data, int dataOffset, int dataLimit) {
				return data.charAt(dataOffset) == separator ? 0 : -1;
			}

			@Override
			public char[] cloneSeparator(int index) {
				return SEPARATOR[index].clone();
			}
			@Override
			public char[][] cloneSeparators() {
				return Delimiter.clone(SEPARATOR);
			}

			@Override
			protected char[] innerSeparator(int index) {
				// TODO: Implement this method
				return SEPARATOR[index];
			}
			@Override
			protected char[][] innerSeparators() {
				// TODO: Implement this method
				return SEPARATOR;
			}
		};
	}

	public static char[][] sortSeparatorsLeftMaxRightMin(char[][] separators) {
        if (null == separators)
            return  separators;
        Arrays.sort(separators, new Comparator<char[]>() {
				@Override
				public int compare(char[] o1, char[] o2) {
					return Delimiter.compare(o2.length, o1.length);
				}
			});
        return separators;
    }


    static void _______________________(){}

	public static abstract class ICharsDelimiterBuilder {
		public abstract void add(char[]  chars);
		public void addAll(char[][] chars) {
			for (char[] aChar : chars)
				add(aChar);
		}

		public abstract int find(char[] chars);
		public abstract int count();
		public abstract char[]   cloneSeparator(int index);
		public abstract char[][] cloneSeparators();
		public abstract ICharsDelimiter build();

		public void add(String chars) {
			add(chars.toCharArray());
		}
		public void addAll(String[] chars) {
			for (String aChar : chars)
				add(aChar);
		}
	}

	/**
	 * simple faster
	 */
	public static class CharsMappingDelimiterBuilder extends ICharsDelimiterBuilder {
		static final int MAPPING_INDEX_OFFSET = CHAR_INDEX_OFFSET;
		static final int MAPPING_COUNT        = CHAR_COUNT;
		static Separator[] EMPTY_SEPARATOR_ARRAY = {};


		static class Instance extends ICharsDelimiter {
			char[][] separatorCache;
			SeparatorList[] separatorListMapping;
			Instance(CharsMappingDelimiterBuilder limiter) {
				this.separatorCache       = Delimiter.clone(limiter.separatorCache.toArray(new char[][]{}));
				this.separatorListMapping = CharsMappingDelimiterBuilder.clone(limiter.separatorListMapping);
			}

			@Override public boolean isHead(char ch) { return null != separatorListMapping[MAPPING_INDEX_OFFSET + ch]; }
			@Override public int headMatchMinLength(char ch) { return separatorListMapping[MAPPING_INDEX_OFFSET + ch].minLength; }
			@Override public int headMatchMaxLength(char ch) { return separatorListMapping[MAPPING_INDEX_OFFSET + ch].maxLength; }

			@Override
			public int assertSeparator(char[] data, int dataOffset, int dataLimit) {
				SeparatorList list = this.separatorListMapping[MAPPING_INDEX_OFFSET + data[dataOffset]];
				if (null !=   list && dataOffset + list.minLength <= dataLimit) {
					int len = dataLimit - dataOffset;
					F: for (Separator separator: list.list) {
						if (len >= separator.length) {
							int separatorIndex =              separator.compareOffset;
							int dataIndex      = dataOffset + separator.compareOffset;
							for (; separatorIndex < separator.length; separatorIndex++, dataIndex++) {
								if (separator.compareData[separatorIndex] !=  data[dataIndex]) {
									continue F;
								}
							}
							return separator.separatorsIndex;
						}
					}
				}
				return -1;
			}

			//------------copy---------
			@Override
			public int assertSeparator(CharSequence data, int dataOffset, int dataLimit) {
				SeparatorList list = this.separatorListMapping[MAPPING_INDEX_OFFSET + data.charAt(dataOffset)];
				if (null !=   list && dataOffset + list.minLength <= dataLimit) {
					int len = dataLimit - dataOffset;
					F: for (Separator separator: list.list) {
						if (len >= separator.length) {
							int separatorIndex =              separator.compareOffset;
							int dataIndex      = dataOffset + separator.compareOffset;
							for (; separatorIndex < separator.length; separatorIndex++, dataIndex++) {
								if (separator.compareData[separatorIndex] !=  data.charAt(dataIndex)) {
									continue F;
								}
							}
							return separator.separatorsIndex;
						}
					}
				}
				return -1;
			}
			//------------copy---------


			@Override
			public char[] cloneSeparator(int index) {
				// TODO: Implement this method
				return separatorCache[index].clone();
			}
			@Override
			public char[][] cloneSeparators() {
				// TODO: Implement this method
				return Delimiter.clone(separatorCache);
			}

			@Override
			protected char[] innerSeparator(int index) {
				// TODO: Implement this method
				return separatorCache[index];
			}
			@Override
			protected char[][] innerSeparators() {
				// TODO: Implement this method
				return separatorCache;
			}

			@Override
			public String toString() {
				// TODO: Implement this method
				StringBuilder sb = new StringBuilder();
				for (int i = 0;   i < separatorListMapping.length; i++) {
					SeparatorList c = separatorListMapping[i];
					if (c == null || c.list.length == 0) continue;

					char v = (char) (i - MAPPING_INDEX_OFFSET);
					sb.append("[" + i + "]" + String.valueOf(v) + "=" + c).append("\n");
				}
				return sb.toString();
			}
		}

		@Override
		public ICharsDelimiter build() {
			if (count() == 0)
				throw new UnsupportedOperationException("empty separator");
			return new Instance(this);
		}


		static SeparatorList[] clone(SeparatorList[] ms) {
			SeparatorList[] sl = new SeparatorList[ms.length];
			for (int i =0; i < ms.length; i++) {
				if (null == ms[i])
					continue;
				sl[i] = ms[i].clone();
			}
			return sl;
		}

		static class SeparatorList implements Cloneable {
			Separator[] list = EMPTY_SEPARATOR_ARRAY;
			int minLength, maxLength;
			int count;

			@Override
			public SeparatorList clone() {
				// TODO: Implement this method
				try {
					SeparatorList i = (SeparatorList) super.clone();
					Separator[] nl, ol = this.list;
					nl = i.list = new Separator[ol.length];
					for (int k = 0; k < ol.length; k++) {
						nl[k] = ol[k].clone();
					}
					return i;
				} catch (CloneNotSupportedException e) {
					throw new UnsupportedOperationException(e);
				}
			}



			void add(Separator newElement) {
				Arrays.sort(list = Arrayx.add(list, list.length,    newElement), new Comparator<Separator>() {
						@Override
						public int compare(Separator o1, Separator o2) {
							return Mathz.compareAsLeftMinToRightMax(o2.compareData.length, o1.compareData.length);//left max, right min
						}
					});
				maxLength = list[0].compareData.length;
				minLength = list[list.length - 1].compareData.length;
				count++;
			}

			@Override
			public String toString() {
				// TODO: Implement this method
				return "{min=" + minLength + ", max=" + maxLength + ", count=" + count + "}" + Arrayz.toString(list);
			}
		}
		class Separator implements Cloneable {
			int    compareOffset;
			char[] compareData;

			int    length;
			int    separatorsIndex;

			Separator(int bindIndex,
					  char[] compareData, int compareOffset) {
				this.separatorsIndex = bindIndex;

				this.compareData   = compareData;
				this.compareOffset = compareOffset;

				this.length = compareData.length;
			}
			Separator(char[] chars) {
				this.compareData = chars;
			}


			private int hashCode;
			@Override
			public int hashCode() {
				// TODO: Implement this method
				int h = this.hashCode;
				if (h == 0) {
					int h1 = Arrays.hashCode(compareData);
					hashCode = h = (0 == h1 ? 1 : h1);
				}
				return h;
			}

			@Override
			public Separator clone() {
				// TODO: Implement this method
				try {
					Separator s = (Separator) super.clone();
					s.compareData = s.compareData.clone();
					return s;
				} catch (CloneNotSupportedException e) {
					throw new UnsupportedOperationException(e);
				}
			}


			@Override
			public boolean equals(Object obj) {
				// TODO: Implement this method
				if (obj == this) { return true; }
				if (!(obj instanceof Separator)) { return false; }

				Separator value = (Separator) obj;
				return Arrays.equals(this.compareData, value.compareData);
			}

			@Override
			public String toString() {
				// TODO: Implement this method
				return "[" + separatorsIndex + "]" + new String(compareData);
			}
		}

		private int             separatorListMappingCount;
		private SeparatorList[] separatorListMapping = new SeparatorList[MAPPING_COUNT];
		private List<char[]>    separatorCache       = new ArrayList<>();
		private Map<HashChars, Integer> separatorCacheSet = new HashMap<>();
		private int             minLength = 0, maxLength = 0;

		public int getMappingCount() {
			return separatorListMappingCount;
		}
		public int getSeparatorCount() {
			return separatorCache.size();
		}

		public CharsMappingDelimiterBuilder() {}

		private int findSeparator(char[] chars) {
			Integer index = separatorCacheSet.get(new HashChars(chars));
			return null == index ? -1: index;
		}
		private int addSeparatorAndBindIndex(char[] chars) {
			int index = separatorCache.size();
			this.separatorCache.add(chars);
			this.separatorCacheSet.put(new HashChars(chars), index);
			return index;
		}
		@Override
		public void add(char[] chars) {
			if (null == chars || chars.length == 0)
				return;

			int find = findSeparator(chars);
			if (find != -1) {
				return;
			} else {
				chars = chars.clone();

				int mappingIndex = MAPPING_INDEX_OFFSET + chars[0];
				SeparatorList list = this.separatorListMapping[mappingIndex];
				if (null ==   list) {
					this.separatorListMapping[mappingIndex] = list = new SeparatorList();
					this.separatorListMappingCount++;
				}

				int index = this.addSeparatorAndBindIndex(chars);
				int offset = 1;
				list.add(new Separator(index, chars, offset));

				if (minLength == 0 || chars.length < minLength) {
					minLength = chars.length;
				}
				if (chars.length > maxLength) {
					maxLength = chars.length;
				}
			}
		}
		@Override
		public int find(char[] chars) {
			// TODO: Implement this method
			return findSeparator(chars);
		}

		@Override
		public int count() {
			// TODO: Implement this method
			return getSeparatorCount();
		}


		@Override
		public char[]   cloneSeparator(int index) {
			return this.separatorCache.get(index).clone();
		}
		@Override
		public char[][] cloneSeparators() {
			return Delimiter.clone(this.separatorCache.toArray(new char[][]{}));
		}





		@Override
		public String toString() {
			// TODO: Implement this method
			StringBuilder sb = new StringBuilder();
			for (int i = 0;   i < separatorListMapping.length; i++) {
				SeparatorList c = separatorListMapping[i];
				if (c == null || c.list.length == 0) continue;

				char v = (char) (i - MAPPING_INDEX_OFFSET);
				sb.append("[" + i + "]" + String.valueOf(v) + "=" + c).append("\n");
			}
			return sb.toString();
		}

	}

	/**
	 * simple
	 */
	public static class CharsSimpleDelimiterBuilder extends ICharsDelimiterBuilder {
		static class Instance extends ICharsDelimiter {
			char[][]  separatorCache;
			CharsDelimiterHeads heads;
			Instance(CharsSimpleDelimiterBuilder limiter) {
				this.separatorCache = sortSeparatorsLeftMaxRightMin(Delimiter.clone(limiter.separatorCache.toArray(new char[][]{})));
				this.heads = CharsDelimiterHeads.extractHead(this.separatorCache);
			}
			@Override public boolean isHead(char ch) { return this.heads.isHead(ch); }
			@Override public int headMatchMinLength(char ch) { return heads.headMatchMinLength(ch); }
			@Override public int headMatchMaxLength(char ch) { return heads.headMatchMaxLength(ch); }
			
			@Override
			public int assertSeparator(char[] data, int dataOffset, int dataLimit) {
				int ii = 0;
				int offIndex = dataOffset;
				int endIndex = dataLimit;

				char b1 = data[offIndex + ii];
				for (int ii2 = 0; ii2 < separatorCache.length; ii2++) {
					if (separatorCache[ii2][0] == b1 && (offIndex + ii + separatorCache[ii2].length) <= endIndex) {
						int j = 1;
						for (int ii3 = 1; ii3 < separatorCache[ii2].length; ii3++) {
							if (separatorCache[ii2][ii3] == data[offIndex + ii + ii3]) {
								j++;
							} else break;
						}
						if (j == separatorCache[ii2].length) {
							return ii2;
						}
					}
				}
				return -1;
			}

			//------------copy---------
			@Override
			public int assertSeparator(CharSequence data, int dataOffset, int dataLimit) {
				int ii = 0;
				int offIndex = dataOffset;
				int endIndex = dataLimit;

				char b1 = data.charAt(offIndex + ii);
				for (int ii2 = 0; ii2 < separatorCache.length; ii2++) {
					if (separatorCache[ii2][0] == b1 && (offIndex + ii + separatorCache[ii2].length) <= endIndex) {
						int j = 1;
						for (int ii3 = 1; ii3 < separatorCache[ii2].length; ii3++) {
							if (separatorCache[ii2][ii3] == data.charAt(offIndex + ii + ii3)) {
								j++;
							} else break;
						}
						if (j == separatorCache[ii2].length) {
							return ii2;
						}
					}
				}
				return -1;
			}
			//------------copy---------

			@Override
			public char[] cloneSeparator(int index) {
				// TODO: Implement this method
				return separatorCache[index].clone();
			}
			@Override
			public char[][] cloneSeparators() {
				// TODO: Implement this method
				return Delimiter.clone(separatorCache);
			}

			@Override
			protected char[] innerSeparator(int index) {
				// TODO: Implement this method
				return separatorCache[index];
			}
			@Override
			protected char[][] innerSeparators() {
				// TODO: Implement this method
				return separatorCache;
			}

			@Override
			public String toString() {
				// TODO: Implement this method
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < separatorCache.length; i++) {
					char[] c = separatorCache[i];
					if (c == null || c.length == 0) continue;
					sb.append("[" + i + "]" +  "=" + new String(c)).append("\n");
				}
				return sb.toString();
			}
		}

		@Override
		public ICharsDelimiter build() {
			// TODO: Implement this method
			if (count() == 0)
				throw new UnsupportedOperationException("empty separator");
			return new Instance(this);
		}

		public CharsSimpleDelimiterBuilder(){}


		private List<char[]> 		   separatorCache = new ArrayList<>();
		private Map<HashChars, Integer> separatorCacheSet = new HashMap<>();

		private int findSeparator(char[] chars) {
			Integer index = separatorCacheSet.get(new HashChars(chars));
			return null == index ? -1: index;
		}
		private int addSeparatorAndBindIndex(char[] chars) {
			int index = separatorCache.size();
			this.separatorCache.add(chars);
			this.separatorCacheSet.put(new HashChars(chars), index);
			return index;
		}
		@Override
		public void add(char[] chars) {
			if (null == chars || chars.length == 0)
				return ;

			int find = findSeparator(chars);
			if (find != -1) {
				return;
			} else {
				chars = chars.clone();

				this.addSeparatorAndBindIndex(chars);
			}
		}
		@Override
		public int find(char[] chars) {
			// TODO: Implement this method
			return findSeparator(chars);
		}

		@Override
		public int count() {
			// TODO: Implement this method
			return separatorCache.size();
		}

		@Override
		public char[]   cloneSeparator(int index) {
			return this.separatorCache.get(index).clone();
		}
		@Override
		public char[][] cloneSeparators() {
			return Delimiter.clone(this.separatorCache.toArray(new char[][]{}));
		}


		@Override
		public String toString() {
			// TODO: Implement this method
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < separatorCache.size(); i++) {
				char[] c = separatorCache.get(i);
				if (c == null || c.length == 0) continue;
				sb.append("[" + i + "]" +  "=" + new String(c)).append("\n");
			}
			return sb.toString();
		}
	}




	//--------<copy>-------//
	public static byte[] convertAsBytes(char[] data, Charset charset) {
		return ByteEncoders.charsToBytes(data, 0, data.length, charset);
	}
	public static byte[][] convertAsBytes(char[][] element, Charset charset) {
		byte[][] result = new byte[element.length][];
		for (int i = 0; i < element.length; i++) {
			result[i] =  ByteEncoders.charsToBytes(element[i], 0, element[i].length, charset);
		}
		return result;
	}

	public static List<byte[]> split(byte[] bytes, Delimiter.IBytesDelimiter delimiter) {
		return split(bytes, 0, bytes.length, delimiter);
	}
	public static List<byte[]> split(byte[] bytes, int off, int len, Delimiter.IBytesDelimiter delimiter) {
		if (len > 0) {
			Delimiter.IBytesDelimiterInputStream reader = new BytesInputStreams(bytes, off, len);
			reader.setDelimiter(delimiter);
			return split(reader);
		} else {
			return new ArrayList<>();
		}
	}
	public static List<byte[]> split(Delimiter.IBytesDelimiterInputStream reader) {
		try {
			return split((IBytesDelimiterInputStreamIO) reader);
		} catch (IOException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	public static List<byte[]> split(Delimiter.IBytesDelimiterInputStreamIO reader) throws IOException {
		ArrayLists<byte[]> list = new ArrayLists<>(EMPTY_BYTE_ARRAY_2D);
		byte[] next;
		while (true) {
			boolean lastIsReadReadSeparator = reader.lastIsReadReadSeparator();
			if (null == (next = reader.readNextLine(false))) {
				if (lastIsReadReadSeparator) {
					list.add(Finals.EMPTY_BYTE_ARRAY);
				}
				break;
			}
			list.add(next);
		}
		return list;
	}

	static final byte[][] EMPTY_BYTE_ARRAY_2D = {};
	static final int BYTE_INDEX_OFFSET = 0 - Byte.MIN_VALUE;
	public static final int BYTE_COUNT = (BYTE_INDEX_OFFSET + Byte.MAX_VALUE) + 1;

	public static class BytesDelimiterHeads {
		static final int MAPPING_INDEX_OFFSET = BYTE_INDEX_OFFSET;
		static final int MAPPING_COUNT        = BYTE_COUNT;
		static class Head {
			int minLength, maxLength;
			int count;
		}

		Head[] separatorListMapping = new Head[MAPPING_COUNT];

		public static BytesDelimiterHeads extractHead(byte[][] s) {
			BytesDelimiterHeads instance = new BytesDelimiterHeads();
			for (byte[] chars : s) {
				if (chars.length == 0)
					throw new UnsupportedOperationException("contains empty array");
				int mappingIndex = MAPPING_INDEX_OFFSET + chars[0];
				Head h = instance.separatorListMapping[mappingIndex];
				if (null == h)
					h = instance.separatorListMapping[mappingIndex] = new Head();
				h.minLength = h.minLength == 0 ? chars.length : Mathz.min(h.minLength, chars.length);
				h.maxLength = Mathz.max(h.maxLength, chars.length);
				h.count++;
			}
			return instance;
		}

		public boolean isHead(byte ch)       { return null != this.separatorListMapping[MAPPING_INDEX_OFFSET + ch]; }
		public int headMatchMinLength(byte ch) { return this.separatorListMapping[MAPPING_INDEX_OFFSET + ch].minLength; }
		public int headMatchMaxLength(byte ch) { return this.separatorListMapping[MAPPING_INDEX_OFFSET + ch].maxLength; }
	}

	public static interface IBytesDelimiterInputStreamIO {
		public IBytesDelimiter getDelimiter();
		public void setDelimiter(IBytesDelimiter delimiter);
		public void setDelimiter(byte[][] delimiter);

		public void setDelimiterAndSetCharset(char[][] delimiter, Charset charset);
		public void setDelimiterAsStringCharset(Charset charset);
		public Charset getDelimiterAsStringCharset();

		/**
		 * 如果最后一次next返回的是分隔符 则return >= 0
		 */
		public boolean lastIsReadReadSeparator();
		public int     lastReadSeparatorIndex();
		public int     lastReadSeparatorLength();
		public byte[]  lastReadSeparator();


		public boolean findNext() throws IOException;

		/**
		 * 返回分隔符 或者分隔符之前的内容
		 */
		public byte[] readNext() throws IOException;
		public String readNextAsString() throws IOException;

		//------------copy---------
		//content + (resultAddSeparator?separator:[])
		public byte[] readNextLine(boolean resultAddSeparator) throws IOException;

		//content + (resultAddSeparator?separator:[])
		public String readNextLineAsString(boolean resultAddSeparator) throws IOException;
	}
	public static interface IBytesDelimiterInputStream extends IBytesDelimiterInputStreamIO {
		@Override public boolean findNext();
		@Override public byte[] readNext();
		@Override public String readNextAsString() ;
		@Override public byte[] readNextLine(boolean resultAddSeparator) ;
		@Override public String readNextLineAsString(boolean resultAddSeparator) ;
	}

	public static abstract class IBytesDelimiter {
		public abstract boolean  isHead(byte c);
		public abstract int      headMatchMaxLength(byte c);
		public abstract int      headMatchMinLength(byte c);

		public abstract int      assertSeparator(byte[] data, int dataOffset, int dataLimit);

		public byte[]   cloneSeparator(int index) { return innerSeparator(index).clone(); }
		public byte[][] cloneSeparators() { return Delimiter.clone(innerSeparators()); }

		protected abstract byte[]   innerSeparator(int index);
		protected abstract byte[][] innerSeparators();
	}

	public static byte[][] clone(byte[][] separatorCache) {
		int len = separatorCache.length;
		byte[][] data = new byte[len][];
		for (int i = 0;i < len;i++) {
			data[i] = separatorCache[i].clone();
		}
		return data;
	}
	public static IBytesDelimiter build(String[] separators, Charset charset) {
		int length = separators.length;
		if (length < 4) {
			BytesSimpleDelimiterBuilder simple = new BytesSimpleDelimiterBuilder();
			simple.addAll(separators, charset);
			return simple.build();
		} else {
			BytesMappingDelimiterBuilder a = new BytesMappingDelimiterBuilder();
			a.addAll(separators, charset);
			return a.build();
		}
	}
	public static IBytesDelimiter build(char[][] separators, Charset charset) {
		int length = separators.length;
		if (length < 4) {
			BytesSimpleDelimiterBuilder simple = new BytesSimpleDelimiterBuilder();
			simple.addAll(separators, charset);
			return simple.build();
		} else {
			BytesMappingDelimiterBuilder a = new BytesMappingDelimiterBuilder();
			a.addAll(separators, charset);
			return a.build();
		}
	}
	public static IBytesDelimiter build(byte[][] separators) {
		int length = separators.length;
		if (length == 1 && separators[0].length == 1) {
			return singleByteDelimit(separators[0][0]);
		}
		if (length < 4) {
			BytesSimpleDelimiterBuilder simple = new BytesSimpleDelimiterBuilder();
			simple.addAll(separators);
			return simple.build();
		} else {
			BytesMappingDelimiterBuilder a = new BytesMappingDelimiterBuilder();
			a.addAll(separators);
			return a.build();
		}
	}
	public static IBytesDelimiter singleByteDelimit(final byte separator) {
		return new IBytesDelimiter() {
			final byte[][] SEPARATOR = {{separator}};
			final int minLength = 1, maxLength = 1;

			@Override
			public boolean isHead(byte c) {
				// TODO: Implement this method
				return c == separator;
			}
			@Override public int headMatchMinLength(byte ch) { return 1; }
			@Override public int headMatchMaxLength(byte ch) { return 1; }

			@Override
			public int assertSeparator(byte[] data, int dataOffset, int dataLimit) {
				// TODO: Implement this method
				return data[dataOffset] == separator ? 0 : -1;
			}

			@Override
			public byte[] cloneSeparator(int index) {
				// TODO: Implement this method
				return SEPARATOR[index].clone();
			}
			@Override
			public byte[][] cloneSeparators() {
				// TODO: Implement this method
				return Delimiter.clone(SEPARATOR);
			}

			@Override
			protected byte[] innerSeparator(int index) {
				// TODO: Implement this method
				return SEPARATOR[index];
			}
			@Override
			protected byte[][] innerSeparators() {
				// TODO: Implement this method
				return SEPARATOR;
			}
		};
	}
	public static byte[][] sortSeparatorsLeftMaxRightMin(byte[][] separators) {
        if (null == separators)
            return  separators;
        Arrays.sort(separators, new Comparator<byte[]>() {
				@Override
				public int compare(byte[] o1, byte[] o2) {
					return Delimiter.compare(o2.length, o1.length);
				}
			});
        return separators;
    }


	public static abstract class IBytesDelimiterBuilder {
		public abstract void add(byte[]  chars);
		public void addAll(byte[][] chars) {
			for (byte[] aChar : chars)
				add(aChar);
		}


		public abstract int find(byte[] chars);
		public abstract int count();
		public abstract byte[]   cloneSeparator(int index);
		public abstract byte[][] cloneSeparators();
		public abstract IBytesDelimiter build();

		public void add(String chars, Charset charset) {
			add(chars.getBytes(charset));
		}
		public void addAll(String[] chars, Charset charset) {
			for (String aChar : chars)
				add(aChar.getBytes(charset));
		}

		public void add(char[] chars, Charset charset) {
			add(ByteEncoders.charsToBytes(chars, 0, chars.length, charset));
		}
		public void addAll(char[][] chars, Charset charset) {
			for (char[] aChar : chars)
				add(ByteEncoders.charsToBytes(aChar, 0, aChar.length, charset));
		}
	}

	/**
	 * simple faster
	 */
	public static class BytesMappingDelimiterBuilder extends IBytesDelimiterBuilder {
		static final int MAPPING_INDEX_OFFSET = BYTE_INDEX_OFFSET;
		static final int MAPPING_COUNT        = BYTE_COUNT;
		static Separator[] EMPTY_SEPARATOR_ARRAY = {};


		static class Instance extends IBytesDelimiter {
			byte[][] 		separatorCache;
			SeparatorList[] separatorListMapping;
			Instance(BytesMappingDelimiterBuilder limiter) {
				this.separatorCache       = Delimiter.clone(limiter.separatorCache.toArray(new byte[][]{}));
				this.separatorListMapping = BytesMappingDelimiterBuilder.clone(limiter.separatorListMapping);
			}

			@Override public boolean isHead(byte c) { return null != this.separatorListMapping[MAPPING_INDEX_OFFSET + c]; }
			@Override public int headMatchMinLength(byte ch) { return separatorListMapping[MAPPING_INDEX_OFFSET + ch].minLength; }
			@Override public int headMatchMaxLength(byte ch) { return separatorListMapping[MAPPING_INDEX_OFFSET + ch].maxLength; }

			@Override
			public int assertSeparator(byte[] data, int dataOffset, int dataLimit) {
				SeparatorList list = this.separatorListMapping[MAPPING_INDEX_OFFSET + data[dataOffset]];
				if (null !=   list && dataOffset + list.minLength <= dataLimit) {
					int len = dataLimit - dataOffset;
					F: for (Separator separator: list.list) {
						if (len >= separator.length) {
							int separatorIndex =              separator.compareOffset;
							int dataIndex      = dataOffset + separator.compareOffset;
							for (; separatorIndex < separator.length; separatorIndex++, dataIndex++) {
								if (separator.compareData[separatorIndex] !=  data[dataIndex]) {
									continue F;
								}
							}
							return separator.separatorsIndex;
						}
					}
				}
				return -1;
			}

			@Override
			public byte[] cloneSeparator(int index) {
				// TODO: Implement this method
				return separatorCache[index].clone();
			}
			@Override
			public byte[][] cloneSeparators() {
				// TODO: Implement this method
				return Delimiter.clone(separatorCache);
			}

			@Override
			protected byte[] innerSeparator(int index) {
				// TODO: Implement this method
				return separatorCache[index];
			}
			@Override
			protected byte[][] innerSeparators() {
				// TODO: Implement this method
				return separatorCache;
			}

			@Override
			public String toString() {
				// TODO: Implement this method
				StringBuilder sb = new StringBuilder();
				for (int i = 0;   i < separatorListMapping.length; i++) {
					SeparatorList c = separatorListMapping[i];
					if (c == null || c.list.length == 0) continue;

					char v = (char) (i - MAPPING_INDEX_OFFSET);
					sb.append("[" + i + "]" + String.valueOf(v) + "=" + c).append("\n");
				}
				return sb.toString();
			}
		}

		@Override
		public IBytesDelimiter build() {
			if (count() == 0)
				throw new UnsupportedOperationException("empty separator");
			return new Instance(this);
		}


		static SeparatorList[] clone(SeparatorList[] ms) {
			SeparatorList[] sl = new SeparatorList[ms.length];
			for (int i =0; i < ms.length; i++) {
				if (null == ms[i])
					continue;
				sl[i] = ms[i].clone();
			}
			return sl;
		}

		static class SeparatorList implements Cloneable {
			Separator[] list = EMPTY_SEPARATOR_ARRAY;
			int minLength, maxLength;
			int count;

			@Override
			public SeparatorList clone() {
				// TODO: Implement this method
				try {
					SeparatorList i = (SeparatorList) super.clone();
					Separator[] nl, ol = this.list;
					nl = i.list = new Separator[ol.length];
					for (int k = 0; k < ol.length; k++) {
						nl[k] = ol[k].clone();
					}
					return i;
				} catch (CloneNotSupportedException e) {
					throw new UnsupportedOperationException(e);
				}
			}



			void add(Separator newElement) {
				Arrays.sort(list = Arrayx.add(list, list.length,    newElement), new Comparator<Separator>() {
						@Override
						public int compare(Separator o1, Separator o2) {
							return Mathz.compareAsLeftMinToRightMax(o2.compareData.length, o1.compareData.length); //left max, right min
						}
					});
				maxLength = list[0].compareData.length;
				minLength = list[list.length - 1].compareData.length;
				count++;
			}


			@Override
			public String toString() {
				// TODO: Implement this method
				return "{min=" + minLength + ", max=" + maxLength + ", count=" + count + "}" + Arrayz.toString(list);
			}
		}
		class Separator implements Cloneable {
			int    compareOffset;
			byte[] compareData;

			int    length;
			int    separatorsIndex;

			Separator(int bindIndex,
					  byte[] compareData, int compareOffset) {
				this.separatorsIndex = bindIndex;

				this.compareData   = compareData;
				this.compareOffset = compareOffset;

				this.length = compareData.length;
			}

			Separator(byte[] chars) {
				this.compareData = chars;
			}


			private int hashCode;
			@Override
			public int hashCode() {
				// TODO: Implement this method
				int h = this.hashCode;
				if (h == 0) {
					int h1 = Arrays.hashCode(compareData);
					hashCode = h = (0 == h1 ? 1 : h1);
				}
				return h;
			}

			@Override
			public Separator clone() {
				// TODO: Implement this method
				try {
					Separator s = (Separator) super.clone();
					s.compareData = s.compareData.clone();
					return s;
				} catch (CloneNotSupportedException e) {
					throw new UnsupportedOperationException(e);
				}
			}


			@Override
			public boolean equals(Object obj) {
				// TODO: Implement this method
				if (obj == this) { return true; }
				if (!(obj instanceof Separator)) { return false; }

				Separator value = (Separator) obj;
				return Arrays.equals(this.compareData, value.compareData);
			}

			@Override
			public String toString() {
				// TODO: Implement this method
				return "[" + separatorsIndex + "]" + new String(compareData);
			}
		}

		private int             separatorListMappingCount;
		private SeparatorList[] separatorListMapping = new SeparatorList[MAPPING_COUNT];
		private List<byte[]> 	separatorCache       = new ArrayList<>();
		private Map<HashBytes, Integer> separatorCacheSet = new HashMap<>();
		private int             minLength = 0, maxLength = 0;

		public int getMappingCount() {
			return separatorListMappingCount;
		}
		public int getSeparatorCount() {
			return separatorCache.size();
		}

		public BytesMappingDelimiterBuilder() {}

		private int findSeparator(byte[] chars) {
			Integer index = separatorCacheSet.get(new HashBytes(chars));
			return null == index ? -1: index;
		}
		private int addSeparatorAndBindIndex(byte[] chars) {
			int index = separatorCache.size();
			this.separatorCache.add(chars);
			this.separatorCacheSet.put(new HashBytes(chars), index);
			return index;
		}
		@Override
		public void add(byte[] chars) {
			if (null == chars || chars.length == 0)
				return;

			int find = findSeparator(chars);
			if (find != -1) {
				return;
			} else {
				chars = chars.clone();

				int mappingIndex = MAPPING_INDEX_OFFSET + chars[0];
				SeparatorList list = this.separatorListMapping[mappingIndex];
				if (null ==   list) {
					this.separatorListMapping[mappingIndex] = list = new SeparatorList();
					this.separatorListMappingCount++;
				}

				int index = this.addSeparatorAndBindIndex(chars);
				int offset = 1;
				list.add(new Separator(index, chars, offset));

				if (minLength == 0 || chars.length < minLength) {
					minLength = chars.length;
				}
				if (chars.length > maxLength) {
					maxLength = chars.length;
				}
			}
		}
		@Override
		public int find(byte[] chars) {
			// TODO: Implement this method
			return findSeparator(chars);
		}

		@Override
		public int count() {
			// TODO: Implement this method
			return getSeparatorCount();
		}


		@Override
		public byte[]   cloneSeparator(int index) {
			return this.separatorCache.get(index).clone();
		}
		@Override
		public byte[][] cloneSeparators() {
			return Delimiter.clone(this.separatorCache.toArray(new byte[][]{}));
		}





		@Override
		public String toString() {
			// TODO: Implement this method
			StringBuilder sb = new StringBuilder();
			for (int i = 0;   i < separatorListMapping.length; i++) {
				SeparatorList c = separatorListMapping[i];
				if (c == null || c.list.length == 0) continue;

				byte v = (byte) (i - MAPPING_INDEX_OFFSET);
				sb.append("[" + i + "]" + String.valueOf(v) + "=" + c).append("\n");
			}
			return sb.toString();
		}

	}

	/**
	 * simple
	 */
	public static class BytesSimpleDelimiterBuilder extends IBytesDelimiterBuilder {
		static class Instance extends IBytesDelimiter {
			byte[][]  separatorCache;
			BytesDelimiterHeads heads;
			Instance(BytesSimpleDelimiterBuilder limiter) {
				this.separatorCache = sortSeparatorsLeftMaxRightMin(Delimiter.clone(limiter.separatorCache.toArray(new byte[][]{})));
				this.heads = BytesDelimiterHeads.extractHead(this.separatorCache);
			}
			@Override public boolean isHead(byte ch) { return this.heads.isHead(ch); }
			@Override public int headMatchMinLength(byte ch) { return heads.headMatchMinLength(ch); }
			@Override public int headMatchMaxLength(byte ch) { return heads.headMatchMaxLength(ch); }
			
			@Override
			public int assertSeparator(byte[] data, int dataOffset, int dataLimit) {
				int ii = 0;
				int offIndex = dataOffset;
				int endIndex = dataLimit;

				byte b1 = data[offIndex + ii];
				for (int ii2 = 0; ii2 < separatorCache.length; ii2++) {
					if (separatorCache[ii2][0] == b1 && (offIndex + ii + separatorCache[ii2].length) <= endIndex) {
						int j = 1;
						for (int ii3 = 1; ii3 < separatorCache[ii2].length; ii3++) {
							if (separatorCache[ii2][ii3] == data[offIndex + ii + ii3]) {
								j++;
							} else break;
						}
						if (j == separatorCache[ii2].length) {
							return ii2;
						}
					}
				}
				return -1;
			}

			@Override
			public byte[] cloneSeparator(int index) {
				// TODO: Implement this method
				return separatorCache[index].clone();
			}
			@Override
			public byte[][] cloneSeparators() {
				// TODO: Implement this method
				return Delimiter.clone(separatorCache);
			}

			@Override
			protected byte[] innerSeparator(int index) {
				// TODO: Implement this method
				return separatorCache[index];
			}
			@Override
			protected byte[][] innerSeparators() {
				// TODO: Implement this method
				return separatorCache;
			}

			@Override
			public String toString() {
				// TODO: Implement this method
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < separatorCache.length; i++) {
					byte[] c = separatorCache[i];
					if (c == null || c.length == 0) continue;
					sb.append("[" + i + "]" +  "=" + new String(c)).append("\n");
				}
				return sb.toString();
			}
		}

		@Override
		public IBytesDelimiter build() {
			// TODO: Implement this method
			if (count() == 0)
				throw new UnsupportedOperationException("empty separator");
			return new Instance(this);
		}


		public BytesSimpleDelimiterBuilder(){}

		private List<byte[]> 						  separatorCache = new ArrayList<>();
		private Map<HashBytes, Integer> separatorCacheSet = new HashMap<>();

		private int findSeparator(byte[] chars) {
			Integer index = separatorCacheSet.get(new HashBytes(chars));
			return null == index ? -1: index;
		}
		private int addSeparatorAndBindIndex(byte[] chars) {
			int index = separatorCache.size();
			this.separatorCache.add(chars);
			this.separatorCacheSet.put(new HashBytes(chars), index);
			return index;
		}
		@Override
		public void add(byte[] chars) {
			if (null == chars || chars.length == 0)
				return;

			int find = findSeparator(chars);
			if (find != -1) {
				return;
			} else {
				chars = chars.clone();
				this.addSeparatorAndBindIndex(chars);
			}
		}
		@Override
		public int find(byte[] chars) {
			// TODO: Implement this method
			return findSeparator(chars);
		}

		@Override
		public int count() {
			// TODO: Implement this method
			return separatorCache.size();
		}

		@Override
		public byte[]   cloneSeparator(int index) {
			return this.separatorCache.get(index).clone();
		}
		@Override
		public byte[][] cloneSeparators() {
			return Delimiter.clone(this.separatorCache.toArray(new byte[][]{}));
		}


		@Override
		public String toString() {
			// TODO: Implement this method
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < separatorCache.size(); i++) {
				byte[] c = separatorCache.get(i);
				if (c == null || c.length == 0) continue;
				sb.append("[" + i + "]" +  "=" + new String(c)).append("\n");
			}
			return sb.toString();
		}
	}

}

