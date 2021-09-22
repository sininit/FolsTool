package top.fols.atri.util;

import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Value;
import top.fols.atri.reflect.Reflects;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

@SuppressWarnings("all")
public class Throwables {
	public static String toString(Throwable e) {
		if (null == e) {
			return "";
		}
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String str = sw.toString();
		try {
			sw.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		return str;
	}

	public static void print(OutputStream out, Throwable e) {
		if (null == e) {
			return;
		}
		PrintStream sw = new PrintStream(out);
		e.printStackTrace(sw);
		sw = null;
	}

	public static void print(Writer out, Throwable e) {
		if (null == e) {
			return;
		}
		PrintWriter sw = new PrintWriter(out);
		e.printStackTrace(sw);
		sw = null;
	}

	public static void println(OutputStream out, Throwable e) {
		if (null == e) {
			return;
		}
		PrintStream sw = new PrintStream(out);
		e.printStackTrace(sw);
		sw.print(Finals.Separator.SYSTEM_LINE_SEPARATOR_BYTES());
		sw = null;
	}

	public static void println(Writer out, Throwable e) {
		if (null == e) {
			return;
		}
		PrintWriter sw = new PrintWriter(out);
		e.printStackTrace(sw);
		sw.print(Finals.Separator.SYSTEM_LINE_SEPARATOR_BYTES());
		sw = null;
	}











	public static void printlnThrowable(Object... values) {
		if (null == values) {
		} else {
			for (Object value : values) {
				if (value instanceof Throwable) {
					((Throwable) value).printStackTrace();
				}
			}
		}
	}
	public static void throwThrowable(Object... values) throws RuntimeException {
		if (null == values) {
		} else {
			for (Object value : values) {
				if (value instanceof Throwable) {
					throw new RuntimeException((Throwable) value);
				}
			}
		}
	}

	public static boolean hasThrowable(Object... values) {
		if (null != values) {
			for (Object value : values) {
				if (value instanceof Throwable) {
					return true;
				}
			}
		}
		return false;
	}




	static class TestException extends Throwable {
		public TestException(String message) {
			super(message);
		}
	}
	static Value<Field> THROWABLE_FIELD_DETAIL_MESSAGE;
	static Field getThrowableDetailMessageField() {
		if (null == THROWABLE_FIELD_DETAIL_MESSAGE) {
			THROWABLE_FIELD_DETAIL_MESSAGE = new Value<>();
			String temp = Throwables.class.getCanonicalName();
			TestException ex = new TestException(temp);
			Field[] fields = Reflects.accessible(Reflects.fields(RuntimeException.class));
			for (Field field : fields) {
				try {
					Object value = field.get(ex);
					if (value == temp) {
						THROWABLE_FIELD_DETAIL_MESSAGE.set(field);
						break;
					}
				} catch (IllegalAccessException ignored) {}
			}
		}
		if (THROWABLE_FIELD_DETAIL_MESSAGE.isNull()) {
			throw new RuntimeException("cannot found field: message");
		} else {
			return THROWABLE_FIELD_DETAIL_MESSAGE.get();
		}
	}









	public static <O extends Throwable> RuntimeException convertRuntimeException(O originEx) {
		return convert(originEx, new RuntimeException());
	}
	/**
	 *
	 * @param originEx
	 *
	 * @param object:
	 * @see #convert(Throwable, Class)  Class.newInstance
	 *
	 * @param <O>
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <O extends Throwable, T extends Throwable> T convert(O originEx, T object) {
		return (T) convert(originEx, object.getClass());
	}
	@SuppressWarnings("unchecked")
	public static <O extends Throwable, T extends Throwable> T convert(O originEx, Class<T> toEx) {
		if (null == originEx) {
			return null;
		} else if (null == toEx) {
			return null;
		} else if (originEx.getClass() == toEx) {
			return (T) originEx;
		} else {
			T ex = null;
			NEW: {
				try {
					ex = toEx.getConstructor(Finals.STRING_CLASS).newInstance(originEx.getMessage());
					break NEW;
				} catch (Throwable e) {
					ex = null;
				}

				try {
					ex = toEx.getConstructor().newInstance();
					getThrowableDetailMessageField().set(ex, originEx.getMessage());
					break NEW;
				} catch (Throwable e) {
					ex = null;
				}

				if (null == ex) {
					throw new RuntimeException("cannot create exception instance: " + toEx);
				}
			}

			Throwable cause = originEx.getCause();
			if (null != cause) {
				ex.initCause(cause);
			}

			StackTraceElement[] stacks = originEx.getStackTrace();
			if (null != stacks) {
				ex.setStackTrace(stacks);
			}

			Throwable[] throwableList = originEx.getSuppressed();
			if (null != throwableList) {
				for (Throwable throwable : throwableList) {
					ex.addSuppressed(throwable);
				}
			}
			return ex;
		}
	}



	public static Throwable getFirstThrowable(Object... values) {
		if (null != values) {
			for (Object value : values) {
				if (value instanceof Throwable) {
					return (Throwable) value;
				}
			}
		}
		return null;
	}
	public static Throwable[] filterThrowable(Object... values) {
		if (null == values) {
			return new Throwable[]{};
		} else {
			int arrIndex = 0;
			Throwable[] throwableList = new Throwable[values.length];
			for (Object value : values) {
				if (value instanceof Throwable) {
					throwableList[arrIndex++] = (Throwable) value;
				}
			}
			return Arrays.copyOf(throwableList, arrIndex);
		}
	}


	/**
	 *
	 * @param e
	 * @return full exception
	 */
	public static String toStrings(Throwable e) {
		return printStackTrace(e);
	}


	private static final String SUPPRESSED_CAPTION  = "Suppressed: ";
	private static final String CAUSE_CAPTION       = "Caused by: ";
	public static String printStackTrace(Throwable e) {
		if (null == e) { return null; }

		Set<Throwable> dejaVu = Collections.newSetFromMap(new IdentityHashMap<>());
		dejaVu.add(e);

		StringBuilder s = new StringBuilder();

		// Print our stack trace
		s.append(e).append("\n");
		StackTraceElement[] trace = e.getStackTrace();
		for (StackTraceElement traceElement : trace)
			s.append("\tat " + traceElement).append("\n");

		// Print suppressed exceptions, if any
		for (Throwable se : e.getSuppressed())
			printEnclosedStackTrace(s, se, trace, SUPPRESSED_CAPTION, "\t", dejaVu);

		// Print cause, if any
		Throwable ourCause = e.getCause();
		if (ourCause != null)
			printEnclosedStackTrace(s, ourCause, trace, CAUSE_CAPTION, "", dejaVu);

		return s.toString();
	}
	public static void printEnclosedStackTrace(StringBuilder s,
											   Throwable this_,

											   StackTraceElement[] enclosingTrace,
											   String caption,
											   String prefix,
											   Set<Throwable> dejaVu) {
		if (dejaVu.contains(this_)) {
			s.append("\t[CIRCULAR REFERENCE:" + this_ + "]").append("\n");
		} else {
			dejaVu.add(this_);
			// Compute number of frames in common between this and enclosing trace
			StackTraceElement[] trace = this_.getStackTrace();
			int m = trace.length - 1;
//            int n = enclosingTrace.length - 1;
//            while (m >= 0 && n >=0 && trace[m].equals(enclosingTrace[n])) {
//                m--; n--;
//            }
//            int framesInCommon = trace.length - 1 - m;

			// Print our stack trace
			s.append(prefix + caption + this_).append("\n");
			for (int i = 0; i <= m; i++)
				s.append(prefix + "\tat " + trace[i]).append("\n");
//            if (framesInCommon != 0)
//                s.append(prefix + "\t... " + framesInCommon + " more").append("\n");

			// Print suppressed exceptions, if any
			for (Throwable se : this_.getSuppressed())
				printEnclosedStackTrace(s, se, trace, SUPPRESSED_CAPTION,
						prefix + "\t", dejaVu);

			// Print cause, if any
			Throwable ourCause = this_.getCause();
			if (ourCause != null)
				printEnclosedStackTrace(s, ourCause, trace, CAUSE_CAPTION, prefix, dejaVu);
		}
	}
}
