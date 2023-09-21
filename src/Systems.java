
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import top.fols.atri.reflect.Reflects;

@SuppressWarnings("UnnecessaryInterfaceModifier")
public class Systems {
	static void findRoFieldValue(Map map, Set<IReplaceOutputStream> set, Object object) {
		if (null == object) {
			return;
		}
		Class cls = object.getClass();
		Field[] fields = Reflects.accessible(Reflects.fields(cls));
		for (Field field: fields) {
			Class typeClass = field.getType();
			try {
				Object value = field.get(object);
				if (map.containsKey(value) || field.getType().isPrimitive()) {
					continue;
				} else {
					map.put(value, null);
				}
				if (value instanceof IReplaceOutputStream) {
					set.add((IReplaceOutputStream)value);
				} else if (value instanceof OutputStream ||
						   value instanceof Writer) {
					findRoFieldValue(map, set, value);
				}
			} catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {}
		}
	}

	static OutputStream findRootStream(OutputStream s) {
		if (s instanceof  IReplaceOutputStream) {
			if (null != ((IReplaceOutputStream)s).getRoot()) {
				return  ((IReplaceOutputStream)s).getRoot();
			} else {
				OutputStream result = s;
				while (result instanceof IReplaceOutputStream) {
					result = ((IReplaceOutputStream)result).getParent();
				}
				return result;
			}
		} else {
			Map<Object, Object> identity = new IdentityHashMap<>();
			Set<IReplaceOutputStream>  set = new HashSet<>();
			findRoFieldValue(identity, set, s);
			switch (set.size()) {
				case 0: return s;
				case 1: {
						OutputStream g = findRootStream((OutputStream)set.iterator().next());
						return g;
					}
				default:{
						return s;
					}
			}
		}
	}

	static interface IReplaceOutputStream {
		OutputStream getRoot();
		OutputStream getParent();
	}
	static final class  ReplacePrintStream extends PrintStream implements IReplaceOutputStream {
		@Override
		public OutputStream getRoot() {
			// TODO: Implement this method
			return root;
		}

		@Override
		public OutputStream getParent() {
			// TODO: Implement this method
			return replaceOutputStream;
		}
		OutputStream root;
		ReplaceOutputStream replaceOutputStream;

		public ReplacePrintStream(ReplaceOutputStream replace) {
			super(replace);
			this.replaceOutputStream = replace;
			this.root                = findRootStream(replace);
		}
	}
	public static final class ReplaceOutputStream extends OutputStream implements IReplaceOutputStream {
		@Override
		public OutputStream getRoot() {
			// TODO: Implement this method
			return root;
		}

		@Override
		public OutputStream getParent() {
			// TODO: Implement this method
			return parent;
		}

		OutputStream parent, root;
		OutputStream write;
		OutputStream additional;
		ReplaceOutputStream(OutputStream parent, boolean writeToRootStream,
							OutputStream additional) {
			if (null == parent) {
				throw new NullPointerException();
			}
			this.parent = parent;
			this.root   = findRootStream(parent);
			this.write  = writeToRootStream ? root: parent;

			this.additional = additional;
		}


		OutputStream getRootOutputStream() {
			return root;
		}
		OutputStream getWriteStream() {
			return write;
		}
		OutputStream getAdditionalStream() { return additional; }



		public void write(int p1) throws java.io.IOException {
			this.write.write(p1);
			this.additional.write(p1);
		}
		public void write(byte[] b) throws java.io.IOException {
			this.write(b, 0, b.length);
		}
		public void write(byte[] b, int off, int len) throws java.io.IOException {
			this.write.write(b, off, len);
			this.additional.write(b, off, len);
		}

		public void flush() throws java.io.IOException {
			this.write.flush();
			this.additional.flush();
		}

		public void close() {}



		static final Class lock = ReplaceOutputStream.class;

		/**
		 * Redirect system flow to Debugger
		 */
		public static void writeToRootSout(byte[] content, int off, int len) {
			OutputStream stream = findRootStream(System.out);
			try {
				stream.write(content, off, len);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 替换系统输出流，将内容额外的输出到 additional 中
		 */
		public static ReplaceOutputStream replaceSout(OutputStream additional) {
			synchronized (lock) {
				OutputStream sout = System.out;
				OutputStream stream = findRootStream(sout);
				if (!isBootClassLoader(stream.getClass())) {
					throw new UnsupportedOperationException("unhandled stream, unknown classloader: " + stream.getClass().getClassLoader());
				}
				ReplaceOutputStream  proxy = new ReplaceOutputStream(stream, true,
																	 additional);
				System.setOut(new ReplacePrintStream(proxy));
				return proxy;
			}
		}

		public static ReplaceOutputStream replaceSerr(OutputStream additional) {
			synchronized (lock) {
				OutputStream sout = System.err;
				OutputStream stream = findRootStream(sout);
				if (!isBootClassLoader(stream.getClass())) {
					throw new UnsupportedOperationException("unhandled stream, unknown classloader: " + stream.getClass().getClassLoader());
				}
				ReplaceOutputStream  proxy = new ReplaceOutputStream(stream, true,
																	 additional);
				System.setErr(new ReplacePrintStream(proxy));
				return proxy;
			}
		}
	}

	public static boolean isBootClassLoader(Class cls) {
		return Object.class.getClassLoader() == cls.getClassLoader();
	}



}
