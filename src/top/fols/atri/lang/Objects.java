package top.fols.atri.lang;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class Objects {

    public interface Accept<R, T> { R callback(T param); }
    public interface Cast<P, R> { R cast(P param); }

    public interface CallbackValue<T> { void callback(T param); }
    public interface Callback { void callback(); }

    public interface Executor<T> { T execute();}



    public static <T> T requireNonNull(T obj) {
        if (null == obj) { throw new NullPointerException(); }
        return obj;
    }
    public static <T> T requireNonNull(T obj, String errorMessage) {
        if (null == obj) { throw new NullPointerException(errorMessage); }
        return obj;
    }


    public static void requireTrue(boolean obj) throws RuntimeException {
        if (!obj) { throw new RuntimeException(); }
    }
    public static void requireTrue(boolean obj, String errorMessage) throws RuntimeException {
        if (!obj) { throw new RuntimeException(errorMessage); }
    }


    public static void requireFalse(boolean obj) throws RuntimeException {
        if (obj) { throw new RuntimeException(); }
    }
    public static void requireFalse(boolean obj, String errorMessage) throws RuntimeException {
        if (obj) { throw new RuntimeException(errorMessage); }
    }




    public static <T> Value<T> wrap(T v) { return new Value<>(v); }




    public static boolean equals(Object obj, Object obj2) {
        if (null == obj) {
            return null == obj2;
        } else {
            return obj.equals(obj2);
        }
    }


    public static boolean empty(CharSequence obj) {
        return null == obj || obj.length() == 0;
    }
    public static boolean empty(Collection<?> obj) {
        return null == obj || obj.size() == 0;
    }
    public static boolean empty(Object[] obj) {
        return null == obj || obj.length == 0;
    }

    public static boolean empty(long[] obj) {
        return null == obj || obj.length == 0;
    }
    public static boolean empty(int[] obj) {
        return null == obj || obj.length == 0;
    }
    public static boolean empty(short[] obj) {
        return null == obj || obj.length == 0;
    }
    public static boolean empty(byte[] obj) {
        return null == obj || obj.length == 0;
    }
    public static boolean empty(boolean[] obj) {
        return null == obj || obj.length == 0;
    }
    public static boolean empty(double[] obj) {
        return null == obj || obj.length == 0;
    }
    public static boolean empty(float[] obj) {
        return null == obj || obj.length == 0;
    }
    public static boolean empty(char[] obj) {
        return null == obj || obj.length == 0;
    }

    public static boolean empty(Map obj) {
        return null == obj || obj.size() == 0;
    }

    public static boolean empty(Object obj) { return null == obj; }








    public static String toString(Object obj) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof char[]) {
            return new String((char[]) obj);
        } else {
            return obj.toString();
        }
    }

    public static char toChar(Object obj) {
        if (null == obj) {
            return (char)0;
        }
        if (obj instanceof Character) {
            return (Character) obj;
        }
        String str = obj.toString();
        return 0 == str.length() ?(char)0: str.charAt(0);
    }

    public static boolean toBoolean(Object obj) {
        if (null == obj) {
            return false;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        return Boolean.parseBoolean(obj.toString().trim());
    }

    public static byte toByte(Object obj) {
        if (null == obj) {
            return 0;
        }
        if (obj instanceof Byte) {
            return (Byte) obj;
        }
        return Byte.parseByte(obj.toString().trim());
    }

    public static int toInt(Object obj) {
        if (null == obj) {
            return 0;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        return Integer.parseInt(obj.toString().trim());
    }

    public static long toLong(Object obj) {
        if (null == obj) {
            return 0;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        return Long.parseLong(obj.toString().trim());
    }

    public static short toShort(Object obj) {
        if (null == obj) {
            return 0;
        }
        if (obj instanceof Short) {
            return (Short) obj;
        }
        return Short.parseShort(obj.toString().trim());
    }

    /**
     * xx.xxx
     **/
    public static double toDouble(Object obj) {
        if (null == obj) {
            return 0;
        } else if (obj instanceof Double) {
            return (Double) obj;
        }
        return Double.parseDouble(obj.toString().trim());
    }

    /**
     * xx.xxx
     **/
    public static float toFloat(Object obj) {
        if (null == obj) {
            return 0;
        }
        if (obj instanceof Float) {
            return (Float) obj;
        }
        return Float.parseFloat(obj.toString().trim());
    }











    /**
     * parse value Object[] byte[] long[] double[] char[] int[] boolean[] float[] short[]
     */



    public static boolean parseBoolean(Object value) {
        return null != value && "true".equalsIgnoreCase(value.toString().trim());
    }

    public static byte parseByte(Object value) {
        if (null == value) { return 0; }
        String st = retainNum(st = value.toString(), 0, st.length());
        return st.length() == 0 ? 0 : Byte.parseByte(st);
    }

    public static char parseChar(Object value) {
        String st;
        return (null != value && (st = value.toString()).length() > 0) ?st.charAt(0): (char)0;
    }

    public static short parseShort(Object value) {
        if (null == value) { return 0; }
        String st = retainNum(st = value.toString(), 0, st.length());
        return st.length() == 0 ? 0 : Short.parseShort(st);
    }

    public static int parseInt(Object value) {
        if (null == value) { return 0; }
        String st = retainNum(st = value.toString(), 0, st.length());
        return st.length() == 0 ? 0 : Integer.parseInt(st);
    }

    public static long parseLong(Object value) {
        if (null == value) { return 0; }
        String st = retainNum(st = value.toString(), 0, st.length());
        return st.length() == 0 ? 0 : Long.parseLong(st);
    }


    public static float parseFloat(Object value) {
        if (null == value) { return 0; }
        String st = retainDouble(st = value.toString(), 0, st.length());
        return st.length() == 0 ? 0 : Float.parseFloat(st);
    }

    public static double parseDouble(Object value) {
        if (null == value) { return 0; }
        String st = retainDouble(st = value.toString(), 0, st.length());
        return st.length() == 0 ? 0 : Double.parseDouble(st);
    }








    /**
     *
     * read String first num
     * @see top.fols.box.lang.XString#retain(CharSequence, int, int, char[])
     */
    public static String retainNum(CharSequence str, int off, int len) {
        char[] buf = new char[20];// long max string len = 20
        int bufindex = 0, bufsize = 0;
        char ch;
        for (int i = off; i < off + len; i++) {
            ch = str.charAt(i);
            if (ch == '+' || ch == '-' || (ch >= '0' && ch <= '9')) {
                int minCapacity = bufindex + 1;
                if (minCapacity - buf.length > 0) {
                    int oldCapacity = buf.length;
                    int newCapacity = oldCapacity << 1;
                    if (newCapacity - minCapacity < 0) {
                        newCapacity = minCapacity;
                    }
                    if (newCapacity < 0) {
                        if (minCapacity < 0) {
                            // overflow
                            throw new OutOfMemoryError();
                        }
                        newCapacity = Integer.MAX_VALUE;
                    }
                    buf = Arrays.copyOf(buf, newCapacity);
                }

                buf[bufindex++] = ch;
                bufsize++;
            } else if (bufindex > 0) {// interrupt
                break;
            }
        }
        return new String(buf, 0, bufsize);
    }

    /**
     *
     * read String first num
     * @see top.fols.box.lang.XString#retain(CharSequence, int, int, char[])
     */
    public static String retainDouble(CharSequence str, int off, int len) {
        char[] buf = new char[64];
        int bufindex = 0, bufsize = 0;
        char ch;
        for (int i = off; i < off + len; i++) {
            ch = str.charAt(i);
            if (ch == '+' || ch == '-' || (ch >= '0' && ch <= '9') || ch == 'N' || ch == 'I' || ch == 'x' || ch == 'X'
                    || ch == '.' || ch == 'e' || ch == 'E' || ch == 'f' || ch == 'F' || ch == 'd' || ch == 'D') {

                int minCapacity = bufindex + 1;
                if (minCapacity - buf.length > 0) {
                    int oldCapacity = buf.length;
                    int newCapacity = oldCapacity << 1;
                    if (newCapacity - minCapacity < 0) {
                        newCapacity = minCapacity;
                    }
                    if (newCapacity < 0) {
                        if (minCapacity < 0) {
                            // overflow
                            throw new OutOfMemoryError();
                        }
                        newCapacity = Integer.MAX_VALUE;
                    }
                    buf = Arrays.copyOf(buf, newCapacity);
                }

                buf[bufindex++] = ch;
                bufsize++;
            } else if (bufindex > 0) {// interrupt
                break;
            }
        }
        return new String(buf, 0, bufsize);
    }

}
