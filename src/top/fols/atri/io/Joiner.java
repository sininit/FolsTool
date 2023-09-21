package top.fols.atri.io;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.interfaces.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public class Joiner {
	public static Joiner on(String separator) {
		return new Joiner(separator);
	}



	private final String separator;

	private Joiner(String separator) {
		this.separator = requireNonNull(separator);
	}
	private Joiner(Joiner prototype) {
		this.separator = prototype.separator;
	}




	public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Object> iterable)throws IOException {
		requireNonNull(appendable);
		if (iterable.hasNext()) {
			appendable.append(toString(iterable.next()));
			while (iterable.hasNext()) {
				appendable.append(separator);
				appendable.append(toString(iterable.next()));
			}
		}
		return appendable;
	}
	public <A extends Appendable> A appendTo(A appendable, Iterable<? extends Object> iterable)throws IOException {
		return appendTo(appendable, iterable.iterator());
	}

	public final <A extends Appendable> A appendTo(A appendable, @NotNull Object[] iterable) throws IOException {
		return appendTo(appendable, Arrays.asList(iterable));
	}




	public final StringBuilder appendTo(StringBuilder builder, Iterator<? extends Object> iterable) {
		try {
			appendTo((Appendable) builder, iterable);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return builder;
	}
	public final StringBuilder appendTo(StringBuilder builder, Iterable<? extends Object> iterable) {
		return appendTo(builder, iterable.iterator());
	}

	public final StringBuilder appendTo(StringBuilder builder, @NotNull Object[] iterable) {
		return appendTo(builder, Arrays.asList(iterable));
	}



	public final String join(Iterator<? extends Object> values) {
		return appendTo(new StringBuilder(), values).toString();
	}
	public final String join(Iterable<? extends Object> values) {
		return appendTo(new StringBuilder(), values).toString();
	}
	public final String join(@Nullable Object[] values) {
		return appendTo(new StringBuilder(), values).toString();
	}

	CharSequence toString(@NotNull Object value) {
		requireNonNull(value);
		return (value instanceof CharSequence) ? (CharSequence) value : value.toString();
	}

	public Joiner replaceForNull(final String nullText) {
		requireNonNull(nullText);
		return new Joiner(this) {
			@Override
			CharSequence toString(@NotNull Object value) {
				return (value == null) ? nullText : Joiner.this.toString(value);
			}

			@Override
			public Joiner replaceForNull(String nullText) {
				throw new UnsupportedOperationException("already use replaceForNull");
			}

			@Override
			public Joiner skipNulls() {
				throw new UnsupportedOperationException("already use replaceForNull");
			}
		};
	}

	public Joiner skipNulls() {
		return new Joiner(this) {
			@Override
			public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Object> iterator) throws IOException {
				requireNonNull(appendable, "appendable");
				requireNonNull(iterator, "iterator");
				while (iterator.hasNext()) {
					Object part = iterator.next();
					if (part != null) {
						appendable.append(Joiner.this.toString(part));
						break;
					}
				}
				while (iterator.hasNext()) {
					Object part = iterator.next();
					if (part != null) {
						appendable.append(separator);
						appendable.append(Joiner.this.toString(part));
					}
				}
				return appendable;
			}

			@Override
			public Joiner replaceForNull(String v) {
				throw new UnsupportedOperationException("already use skipNulls");
			}

			@Override
			public JoinerMap withKeyValueSeparator(String kvs) {
				throw new UnsupportedOperationException("map entry cannot skipNulls");
			}
		};
	}


	public JoinerMap withKeyValueSeparator(String keyValueSeparator) {
		return new JoinerMap(this, keyValueSeparator);
	}


	public static final class JoinerMap {
		private final Joiner joiner;
		private final String separator;

		private JoinerMap(Joiner joiner, String keyValueSeparator) {
			this.joiner = joiner; 
			this.separator = requireNonNull(keyValueSeparator);
		}

		public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Entry<?, ?>> parts) throws IOException {
			requireNonNull(appendable);
			if (parts.hasNext()) {
				Entry entry = parts.next();
				appendable.append(joiner.toString(entry.getKey()));
				appendable.append(separator);
				appendable.append(joiner.toString(entry.getValue()));
				while (parts.hasNext()) {
					appendable.append(joiner.separator);
					Entry e = parts.next();
					appendable.append(joiner.toString(e.getKey()));
					appendable.append(separator);
					appendable.append(joiner.toString(e.getValue()));
				}
			}
			return appendable;
		}

		public <A extends Appendable> A appendTo(A appendable, Iterable<? extends Entry<?, ?>> entries) throws IOException {
			return appendTo(appendable, entries.iterator());
		}

		public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException {
			return appendTo(appendable, map.entrySet().iterator());
		}


		public StringBuilder appendTo(StringBuilder builder, Iterator<? extends Entry<?, ?>> entries) {
			try {
				appendTo((Appendable) builder, entries);
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			return builder;
		}
		public StringBuilder appendTo(StringBuilder builder, Iterable<? extends Entry<?, ?>> entries) {
			return appendTo(builder, entries.iterator());
		}
		public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) {
			return appendTo(builder, map.entrySet().iterator());
		}



		public String join(Map<?, ?> map) {
			return appendTo(new StringBuilder(), map).toString();
		}
		public String join(Iterable<? extends Entry<?, ?>> entries) {
			return appendTo(new StringBuilder(), entries.iterator()).toString();
		}
		public String join(Iterator<? extends Entry<?, ?>> entries) {
			return appendTo(new StringBuilder(), entries).toString();
		}

		public JoinerMap useForNull(String v) {
			return new JoinerMap(joiner.replaceForNull(v), separator);
		}
	}


}

