package top.fols.box.lang;

import top.fols.atri.io.util.Streams;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Value;
import top.fols.atri.reflect.Reflects;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class Throwables {
    public static void throwRuntimeException(String message) {
        throw new RuntimeException(message); }

    public static String toString(Throwable e) {
        if (null == e) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter s = new PrintWriter(sw);
        e.printStackTrace(s);
        String str = sw.toString();
        Streams.close(sw);
        Streams.close(s);
        return str;
    }


    public static class EmptyStackException extends RuntimeException {
        public EmptyStackException(String message) {
            super(message, null, true, false);
        }
        public EmptyStackException(Throwable cause) {
            super(null == cause ? "" : cause.getMessage(), cause, true, false);
        }
    }
    public static class MessageException extends Throwables.EmptyStackException {
        protected String message;
        public MessageException() {
            super("");
            this.message = null;
        }
        public MessageException(String msg) {
            super("");
            this.message = msg;
        }
        @Override
        public String toString() {
            // TODO: Implement this method
            return String.valueOf((Object) message);
        }
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



    public static String throwableOrObjectAsString(Object object) {
        if (null == object) {
            return String.valueOf((Object) null);
        } else if (object instanceof Throwable) {
            return Throwables.toString((Throwable) object);
        }
        return object.toString();
    }





    static class TestException extends Throwable { public TestException(String message) { super(message); }}
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
                    if (temp.equals(value)) {
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
                    ex = Reflects.newInstance(toEx);
                    getThrowableDetailMessageField()
                            .set(ex, originEx.getMessage());
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

        Set<Throwable> dejaVu = Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>());
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
//                s.w(prefix + "\t... " + framesInCommon + " more").w("\n");

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
