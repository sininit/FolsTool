package top.fols.atri.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import top.fols.atri.interfaces.interfaces.ICaller;

import static top.fols.atri.io.BytesInputStreams.SEPARATOR_INDEX_UNSET;

@SuppressWarnings("UnnecessaryInterfaceModifier")
public class Splitter {
	ISplitterMatcherBuilder matcherBuilder;

	private Splitter() {}
	private Splitter(ISplitterMatcherBuilder delimiter) {
		this.matcherBuilder = delimiter;
	}


	//undefined cache; 

	public static Splitter on(final String... separators) {
		return new Splitter(new ISplitterMatcherBuilder() {
				final Delimiter.ICharsDelimiter delimiter = Delimiter.build(separators);
				
				@Override
				public Splitter.ISplitterMatcher matcher(CharSequence str) {
					// TODO: Implement this method
					return new SplitterDelimiterMatcher(delimiter, str);
				}
			});
	}

	public Iterable<String> split(CharSequence str) {
		return createIterable(str);
	}

	public List<String>  splitToList(CharSequence str) {
		return splitToList(str, new ArrayList<String>());
	}
	public List<String>  splitToList(CharSequence str, List<String> result) {
		for (String content: createIterable(str))
			result.add(content);
		return result;
	}



	protected Iterable<String> createIterable(CharSequence str) {
		return new SplitIterable(str, matcherBuilder);
	}
	protected static class WrapFilterSplitter extends Splitter {
		final ICaller<String, String> filter;
		final Splitter superz;

		public WrapFilterSplitter(ICaller<String, String> filter,
								  Splitter superz) {
			this.filter = filter;
			this.superz = superz;
		}
		
		static <T> Iterable<T> filter(final Iterable<T> iterable, final ICaller<T, T> filter) {
			return new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					// TODO: Implement this method
					final Iterator<T> iterator = iterable.iterator();
					return new Iterator<T>() {
						private T v;
						private boolean geted = true;

						@Override
						public boolean hasNext() {
							// TODO: Implement this method
							if (geted) {
								while (iterator.hasNext())	{
									T f = filter.next(iterator.next());
									if (null  != f) {
										this.v = f;
										return true;
									}
								}
								return false;
							} else {
								return true;
							}
						}
						@Override
						public T next() {
							// TODO: Implement this method
							geted = true;
							return v;
						}

						@Override
						public void remove() {
							// TODO: Implement this method
							iterator.remove();
						}
					};
				}
			};
		}
		
		@Override
		protected Iterable<String> createIterable(CharSequence str) {
			return filter(superz.createIterable(str), filter);
		}
	}
	static final ICaller<String, String> FILTER_OMIT_EMPTY_STRINGS = new ICaller<String, String>(){
		@Override
		public String next(String p1) {
			// TODO: Implement this method
			return p1.isEmpty() ? null : p1;
		}
	};
	public Splitter omitEmptyStrings() {
		return addFilter(FILTER_OMIT_EMPTY_STRINGS);
	}

	static final ICaller<String, String> FILTER_TRIM = new ICaller<String, String>(){
		@Override
		public String next(String p1) {
			// TODO: Implement this method
			return p1.trim();
		}
	};
	public Splitter trimResults() {
		return addFilter(FILTER_TRIM);
	}



	public Splitter addFilter(ICaller<String, String> filter) {
		if (null == filter)
			return this;
		return new WrapFilterSplitter(filter, this);
	}

	public static class SplitIterable implements Iterable<String> {
		final CharSequence content;
		final ISplitterMatcherBuilder matcherBuilder;
		public SplitIterable(CharSequence s, ISplitterMatcherBuilder matcherBuilder) {
			this.content = s;
			this.matcherBuilder = matcherBuilder;
		}

		@Override
		public Iterator<String> iterator() {
			// TODO: Implement this method
			return new SplitIterator(matcherBuilder.matcher(content), content);
		}

		static class SplitIterator implements Iterator<String> {
			final ISplitterMatcher matcher;
			final CharSequence content;
			public SplitIterator(ISplitterMatcher matcher, CharSequence content) {
				this.matcher = matcher;
				this.content = content;
			}

			@Override
			public boolean hasNext() {
				// TODO: Implement this method
				return matcher.has();
			}

			@Override
			public String next() {
				// TODO: Implement this method
				matcher.find();
				return content.subSequence(matcher.start(), matcher.end()).toString();
			}

			@Override
			public void remove() {
				// TODO: Implement this method
				throw new UnsupportedOperationException();
			}
		}
	}

	public static interface ISplitterMatcherBuilder {
		public ISplitterMatcher matcher(CharSequence str);
	}
	public static interface ISplitterMatcher {
		public boolean has();
		public boolean find(); 
		public int start();
		public int end();
	}
	
	public static class SplitterDelimiterMatcher implements ISplitterMatcher {
		final Delimiter.ICharsDelimiter delimiter;
		final char[][] separators;

		private CharSequence buf;
		private int pos;
		private int count;
		private int separatorIndex = SEPARATOR_INDEX_UNSET;
		private int start = -1;
		private int end =-1;



		public SplitterDelimiterMatcher(Delimiter.ICharsDelimiter delimiter,
										CharSequence content) {
			this.delimiter  = delimiter;
			this.separators = delimiter.innerSeparators();

			this.buf        = content;
			this.pos = 0;
			this.count = buf.length();
		}

		/**
		 * 如果最后一次next返回的是分隔符 则return >= 0
		 */
		public boolean lastIsReadReadSeparator() {
			return SEPARATOR_INDEX_UNSET != this.separatorIndex;
		}
		public int     lastReadSeparatorIndex() {
			return this.separatorIndex;
		}
		public char[]  lastReadSeparator() {
			return separatorIndex == SEPARATOR_INDEX_UNSET ? null: separators[separatorIndex].clone();
		}
		public int     lastReadSeparatorLength() {
			return separatorIndex == SEPARATOR_INDEX_UNSET ? 0: separators[separatorIndex].length;
		}

		@Override
		public boolean has() {
			return !(this.pos >= this.count);
		}

		@Override
		public boolean find() {
			if (this.pos >= this.count) {
				this.separatorIndex = SEPARATOR_INDEX_UNSET;
				this.start = -1;
				this.end   = -1;
				return false;
			} else {
				Delimiter.ICharsDelimiter delimiter = this.delimiter;
				CharSequence data  = this.buf;
				int last = this.pos;
				if (separatorIndex != SEPARATOR_INDEX_UNSET)
					last += separators[separatorIndex].length;
				int offset = last, limit = this.count;
				int sIndex;
				for (;offset < limit; offset++) {
					if ((sIndex = delimiter.assertSeparator(data, offset, limit)) != -1) {
						int nextIndex = offset;
						this.separatorIndex = sIndex;
						this.pos = nextIndex;
						this.start = last;
						this.end = pos;
						return true;
					}
				}
				this.separatorIndex = SEPARATOR_INDEX_UNSET;
				this.pos = offset;
				this.start = last;
				this.end = pos;
				return true;
			}
		}

		@Override
		public int start() {
			if (start < 0) 
				throw new UnsupportedOperationException("not found");
			return start;
		}
		@Override
		public int end() {
			if (end < 0) 
				throw new UnsupportedOperationException("not found");
			return end;
		}

		public int position() {return pos; }
		public void seek(int index) {
			if (index < 0)
				index = 0;
			else if (index > count)
				index = count;
			this.pos = index;
		}

		public CharSequence content() {
			return buf;
		}

		public CharSequence subSequence(int st, int ed) {
			return buf.subSequence(st, ed);
		}
		public String substring(int st, int ed) {
			return buf.subSequence(st, ed).toString();
		}
	}



	public SplitterMap withKeyValueSeparator(String... separators) {
		return new SplitterMap(this, 
							   new KeyValueSplitterDelimiterMatcher(Delimiter.build(separators)));
	}
	public static interface IKeyValueSplitterDelimiterMatcher {
		public void append(Map<String, String> map, String element);
	}
	public static class KeyValueSplitterDelimiterMatcher implements IKeyValueSplitterDelimiterMatcher {
		final Delimiter.ICharsDelimiter delimiter;
		final char[][] separators;

		public KeyValueSplitterDelimiterMatcher(Delimiter.ICharsDelimiter delimiter) {
			this.delimiter  = delimiter;
			this.separators = delimiter.innerSeparators();
		}

		@Override
		public void append(Map<String, String> map, String buf) {
			// TODO: Implement this method

			Delimiter.ICharsDelimiter delimiter = this.delimiter;
			CharSequence data  = buf;
			int offset = 0, limit = buf.length();
			int sIndex;
			for (;offset < limit; offset++) {
				if ((sIndex = delimiter.assertSeparator(data, offset, limit)) != -1) {
					String k = buf.substring(0, offset);
					String v = buf.substring(offset + separators[sIndex].length, buf.length());
					map.put(k, v);
					return;
				}
			}
			String k = buf;
			String v = null;
			map.put(k, v);
			return;
		}
	}
	public static class SplitterMap {
		final IKeyValueSplitterDelimiterMatcher kvs;

		Splitter splitter;
		SplitterMap(Splitter splitter, IKeyValueSplitterDelimiterMatcher kvs) {
			this.splitter  = splitter;
			this.kvs = kvs;
		}


		public Map<String, String> split(CharSequence str) {
			return split(str, new LinkedHashMap<String, String>());
		}
		public Map<String, String> split(CharSequence str, Map<String, String> map) {
			for (String i: splitter.createIterable(str))
			    kvs.append(map, i);
			return map;
		}
	}
}
