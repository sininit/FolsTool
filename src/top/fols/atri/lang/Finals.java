package top.fols.atri.lang;

import top.fols.atri.interfaces.annotations.Help;
import top.fols.atri.io.Delimiter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.security.Security;
import java.util.*;

@SuppressWarnings("rawtypes")
public class Finals {

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    public static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;



    public static final Annotation[]    EMPTY_ANNOTATION_ARRAY  = new Annotation[]{};
    public static final Field[] 		EMPTY_FIELD_ARRAY       = new Field[]{};
    public static final Constructor[] 	EMPTY_CONSTRUCTOR_ARRAY = new Constructor[]{};
    public static final Method[] 		EMPTY_METHOD_ARRAY      = new Method[]{};

    public static final Class[] 		EMPTY_CLASS_ARRAY   = new Class[]{};
    public static final Object[] 		EMPTY_OBJECT_ARRAY  = new Object[]{};


    public static final String[]        EMPTY_STRING_ARRAY = new String[]{};

    public static final byte[]          EMPTY_BYTE_ARRAY = new byte[]{};
    public static final long[]          EMPTY_LONG_ARRAY = new long[]{};
    public static final double[]        EMPTY_DOUBLE_ARRAY = new double[]{};
    public static final char[]          EMPTY_CHAR_ARRAY = new char[]{};
    public static final int[]           EMPTY_INT_ARRAY = new int[]{};
    public static final boolean[]       EMPTY_BOOLEAN_ARRAY = new boolean[]{};
    public static final float[]         EMPTY_FLOAT_ARRAY = new float[]{};
    public static final short[]         EMPTY_SHORT_ARRAY = new short[]{};

    public static final Byte[]          EMPTY_BYTE_PACKAGE_ARRAY    = new Byte[]{};
    public static final Long[]          EMPTY_LONG_PACKAGE_ARRAY    = new Long[]{};
    public static final Double[]        EMPTY_DOUBLE_PACKAGE_ARRAY  = new Double[]{};
    public static final Character[]     EMPTY_CHAR_PACKAGE_ARRAY    = new Character[]{};
    public static final Integer[]       EMPTY_INT_PACKAGE_ARRAY     = new Integer[]{};
    public static final Boolean[]       EMPTY_BOOLEAN_PACKAGE_ARRAY = new Boolean[]{};
    public static final Float[]         EMPTY_FLOAT_PACKAGE_ARRAY   = new Float[]{};
    public static final Short[]         EMPTY_SHORT_PACKAGE_ARRAY   = new Short[]{};


    public static final StackTraceElement[] EMPTY_STACK_TRACE_ELEMENT_ARRAY = new StackTraceElement[]{};





    public static final String TRUE_STRING  = String.valueOf(true);
    public static final String FALSE_STRING = String.valueOf(false);
    public static final String NULL_STRING  = String.valueOf((Object) null);

    public static final Object NULL = null;

    public static final boolean BOOLEAN_TRUE = true;
    public static final boolean BOOLEAN_FALSE = false;

    public static final Boolean BOOLEAN_PACKAGE_TRUE = true;
    public static final Boolean BOOLEAN_PACKAGE_FALSE = false;



    public static final byte    BYTE_DEFAULT_VALUE = 0;
    public static final long    LONG_DEFAULT_VALUE = 0L;
    public static final double  DOUBLE_DEFAULT_VALUE = 0D;
    public static final char    CHAR_DEFAULT_VALUE = 0;
    public static final int     INT_DEFAULT_VALUE = 0;
    public static final boolean BOOLEAN_DEFAULT_VALUE = false;
    public static final float   FLOAT_DEFAULT_VALUE = 0F;
    public static final short   SHORT_DEFAULT_VALUE = 0;
    public static final String  STRING_EMPTY_VALUE = "";

    public static final Byte        BYTE_PACKAGE_DEFAULT_VALUE = BYTE_DEFAULT_VALUE;
    public static final Long        LONG_PACKAGE_DEFAULT_VALUE = LONG_DEFAULT_VALUE;
    public static final Double      DOUBLE_PACKAGE_DEFAULT_VALUE = DOUBLE_DEFAULT_VALUE;
    public static final Character   CHAR_PACKAGE_DEFAULT_VALUE = CHAR_DEFAULT_VALUE;
    public static final Integer     INT_PACKAGE_DEFAULT_VALUE = INT_DEFAULT_VALUE;
    public static final Boolean     BOOLEAN_PACKAGE_DEFAULT_VALUE = BOOLEAN_DEFAULT_VALUE;
    public static final Float       FLOAT_PACKAGE_DEFAULT_VALUE = FLOAT_DEFAULT_VALUE;
    public static final Short       SHORT_PACKAGE_DEFAULT_VALUE = SHORT_DEFAULT_VALUE;



    public static final Class BYTE_CLASS    = byte.class;
    public static final Class LONG_CLASS    = long.class;
    public static final Class DOUBLE_CLASS  = double.class;
    public static final Class CHAR_CLASS    = char.class;
    public static final Class INT_CLASS     = int.class;
    public static final Class BOOLEAN_CLASS = boolean.class;
    public static final Class FLOAT_CLASS   = float.class;
    public static final Class SHORT_CLASS   = short.class;
    public static final Class VOID_CLASS    = void.class;

    public static final Class<byte[]>       BYTE_ARRAY_CLASS = byte[].class;
    public static final Class<long[]>       LONG_ARRAY_CLASS = long[].class;
    public static final Class<double[]>     DOUBLE_ARRAY_CLASS = double[].class;
    public static final Class<char[]>       CHAR_ARRAY_CLASS = char[].class;
    public static final Class<int[]>        INT_ARRAY_CLASS = int[].class;
    public static final Class<boolean[]>    BOOLEAN_ARRAY_CLASS = boolean[].class;
    public static final Class<float[]>      FLOAT_ARRAY_CLASS = float[].class;
    public static final Class<short[]>      SHORT_ARRAY_CLASS = short[].class;
  //public static final Class<void[]>       VOID_ARRAY_CLASS = void[].class;

    public static final Class<Byte>         BYTE_PACKAGE_CLASS = Byte.class;
    public static final Class<Long>         LONG_PACKAGE_CLASS = Long.class;
    public static final Class<Double>       DOUBLE_PACKAGE_CLASS = Double.class;
    public static final Class<Character>    CHAR_PACKAGE_CLASS = Character.class;
    public static final Class<Integer>      INT_PACKAGE_CLASS = Integer.class;
    public static final Class<Boolean>      BOOLEAN_PACKAGE_CLASS = Boolean.class;
    public static final Class<Float>        FLOAT_PACKAGE_CLASS = Float.class;
    public static final Class<Short>        SHORT_PACKAGE_CLASS = Short.class;
    public static final Class<Void>         VOID_PACKAGE_CLASS = Void.class;

    public static final Class<Byte[]>       BYTE_PACKAGE_ARRAY_CLASS = Byte[].class;
    public static final Class<Long[]>       LONG_PACKAGE_ARRAY_CLASS = Long[].class;
    public static final Class<Double[]>     DOUBLE_PACKAGE_ARRAY_CLASS = Double[].class;
    public static final Class<Character[]>  CHAR_PACKAGE_ARRAY_CLASS = Character[].class;
    public static final Class<Integer[]>    INT_PACKAGE_ARRAY_CLASS = Integer[].class;
    public static final Class<Boolean[]>    BOOLEAN_PACKAGE_ARRAY_CLASS = Boolean[].class;
    public static final Class<Float[]>      FLOAT_PACKAGE_ARRAY_CLASS = Float[].class;
    public static final Class<Short[]>      SHORT_PACKAGE_ARRAY_CLASS = Short[].class;
    public static final Class<Void[]>       VOID_PACKAGE_ARRAY_CLASS = Void[].class;




    public static final String BYTE_CLASS_CANONICAL_NAME        = byte.class.getCanonicalName();
    public static final String LONG_CLASS_CANONICAL_NAME        = long.class.getCanonicalName();
    public static final String DOUBLE_CLASS_CANONICAL_NAME      = double.class.getCanonicalName();
    public static final String CHAR_CLASS_CANONICAL_NAME        = char.class.getCanonicalName();
    public static final String INT_CLASS_CANONICAL_NAME         = int.class.getCanonicalName();
    public static final String BOOLEAN_CLASS_CANONICAL_NAME     = boolean.class.getCanonicalName();
    public static final String FLOAT_CLASS_CANONICAL_NAME       = float.class.getCanonicalName();
    public static final String SHORT_CLASS_CANONICAL_NAME       = short.class.getCanonicalName();
    public static final String VOID_CLASS_CANONICAL_NAME        = void.class.getCanonicalName();

    public static final String BYTE_PACKAGE_CLASS_CANONICAL_NAME    = Byte.class.getCanonicalName();
    public static final String LONG_PACKAGE_CLASS_CANONICAL_NAME    = Long.class.getCanonicalName();
    public static final String DOUBLE_PACKAGE_CLASS_CANONICAL_NAME  = Double.class.getCanonicalName();
    public static final String CHAR_PACKAGE_CLASS_CANONICAL_NAME    = Character.class.getCanonicalName();
    public static final String INT_PACKAGE_CLASS_CANONICAL_NAME     = Integer.class.getCanonicalName();
    public static final String BOOLEAN_PACKAGE_CLASS_CANONICAL_NAME = Boolean.class.getCanonicalName();
    public static final String FLOAT_PACKAGE_CLASS_CANONICAL_NAME   = Float.class.getCanonicalName();
    public static final String SHORT_PACKAGE_CLASS_CANONICAL_NAME   = Short.class.getCanonicalName();
    public static final String VOID_PACKAGE_CLASS_CANONICAL_NAME    = Void.class.getCanonicalName();














    public static final Class<InvocationHandler> INVOCATION_HANDLER_CLASS = InvocationHandler.class;







    public static final Class<Object>           OBJECT_CLASS            = Object.class;
    public static final Class<Class>            CLASS_CLASS             = Class.class;
    public static final Class<String>           STRING_CLASS            = String.class;
    public static final Class<Method>           METHOD_CLASS            = Method.class;
    public static final Class<Field>            FIELD_CLASS             = Field.class;
    public static final Class<Constructor>      CONSTRUCTOR_CLASS       = Constructor.class;

    public static final Class<Object[]>         OBJECT_ARRAY_CLASS      = Object[].class;
    public static final Class<Class[]>          CLASS_ARRAY_CLASS       = Class[].class;
    public static final Class<String[]>         STRING_ARRAY_CLASS      = String[].class;
    public static final Class<Method[]>         METHOD_ARRAY_CLASS      = Method[].class;
    public static final Class<Field[]>          FIELD_ARRAY_CLASS       = Field[].class;
    public static final Class<Constructor[]>    CONSTRUCTOR_ARRAY_CLASS = Constructor[].class;


    @SuppressWarnings("ManualArrayCopy")
    public static final class LineSeparator {
        public static final char LINE_SEPARATOR_CHAR_R = '\r';
        public static final char LINE_SEPARATOR_CHAR_N = '\n';

        public static final String LINE_SEPARATOR_STRING_R = "\r";
        public static final String LINE_SEPARATOR_STRING_N = "\n";
        public static final String LINE_SEPARATOR_STRING_RN = "\r\n";

        public static final String WINDOWS_LINE_SEPARATOR = "\r\n";//WINDOWS
        public static final String MAC_LINE_SEPARATOR = "\r";//MAC
        public static final String LINUX_UNIX_LINE_SEPARATOR = "\n";//Linux, Unix

        public static char[] getCharsLineSeparatorN()  { return new char[] { '\n' }; }
        public static char[] getCharsLineSeparatorR()  { return new char[] { '\r' }; }
        public static char[] getCharsLineSeparatorRN() { return new char[] { '\r', '\n' }; }


        public static String getSystemLineSeparator() {
            return System.lineSeparator();
        }


        static final String[] ALL_SYSTEM_STRING;
        static final char[][] ALL_SYSTEM_CHARS;
        static {
            ALL_SYSTEM_STRING = new LinkedHashSet<String>() {{
                add(WINDOWS_LINE_SEPARATOR);
                add(MAC_LINE_SEPARATOR);
                add(LINUX_UNIX_LINE_SEPARATOR);
                add(getSystemLineSeparator());
            }}.toArray(Finals.EMPTY_STRING_ARRAY);
            Arrays.sort(ALL_SYSTEM_STRING, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Mathz.compareAsLeftMinToRightMax(o2.length(), o1.length());//left max, right min
                }
            });

            ALL_SYSTEM_CHARS = new char[ALL_SYSTEM_STRING.length][];
            for (int i = 0; i < ALL_SYSTEM_STRING.length; i++) {
                String s = ALL_SYSTEM_STRING[i];
                ALL_SYSTEM_CHARS[i] = s.toCharArray();
            }
        }


        /**
         * sorted [max length,..., min length]
         */
        @Help("sorted [max length,..., min length]")
        public static String[] getAllSystemLineSeparatorSortedMaxToMin() {
            String[] chars = new String[ALL_SYSTEM_STRING.length];
            for (int i = 0; i < chars.length; i++) {
                chars[i] = ALL_SYSTEM_STRING[i];
            }
            return chars;
        }
        /**
         * sorted [max length,..., min length]
         */
        @Help("sorted [max length,..., min length]")
        public static char[][] getAllSystemLineSeparatorCharsSortedMaxToMin() {
            char[][] chars = new char[ALL_SYSTEM_CHARS.length][];
            for (int i = 0; i < chars.length; i++) {
                chars[i] = ALL_SYSTEM_CHARS[i].clone();
            }
            return chars;
        }




        /**
         * sorted [max length,..., min length]
         */
        @Help("sorted [max length,..., min length]")
        public static byte[][] getAllSystemLineSeparatorBytesSortedMaxToMin(Charset charset) {
            byte[][] chars = new byte[ALL_SYSTEM_STRING.length][];
            for (int i = 0; i < chars.length; i++) {
                chars[i] = ALL_SYSTEM_STRING[i].getBytes(charset);
            }
            return chars;
        }


        static final Delimiter.ICharsDelimiter LINE_CHAR_DELIMITER =  new Delimiter.ICharsDelimiter() {
            final char[][] SEPARATOR = {
                    "\r\n".toCharArray(),
                    "\r".toCharArray(),
                    "\n".toCharArray()
            };
            final Delimiter.CharsDelimiterHeads heads = Delimiter.CharsDelimiterHeads.extractHead(SEPARATOR);

            @Override public boolean isHead(char ch) {
                return ch == '\r' ||
                       ch == '\n';
            }
            @Override public int headMatchMinLength(char ch) { return heads.headMatchMinLength(ch); }
            @Override public int headMatchMaxLength(char ch) { return heads.headMatchMaxLength(ch); }

            @Override
            public int assertSeparator(char[] data, int dataOffset, int dataLimit) {
                // TODO: Implement this method
                char ch = data[dataOffset];
                if (ch == '\r') return dataOffset + 1 < dataLimit && data[dataOffset + 1] == '\n' ? 0 : 1;
                if (ch == '\n') return 2;
                return -1;
            }
            @Override
            public int assertSeparator(CharSequence data, int dataOffset, int dataLimit) {
                // TODO: Implement this method
                char ch = data.charAt(dataOffset);
                if (ch == '\r') return dataOffset + 1 < dataLimit && data.charAt(dataOffset + 1) == '\n' ? 0 : 1;
                if (ch == '\n') return 2;
                return -1;
            }

            @Override
            public char[] cloneSeparator(int index) {
                // TODO: Implement this method
                return SEPARATOR[index].clone();
            }

            @Override
            public char[][] cloneSeparators() {
                // TODO: Implement this method
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
        //LineSeparator.lineCharDelimit
        public static Delimiter.ICharsDelimiter lineCharDelimit() {
            return LINE_CHAR_DELIMITER;
        }
    }


    @SuppressWarnings("CharsetObjectCanBeUsed")
    public static final class Charsets {
        private Charsets() {
            throw new AssertionError("No instances for you!");
        }

        /**
         * Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the
         * Unicode character set
         */
        public static final Charset US_ASCII = Charset.forName("US-ASCII");
        /**
         * ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
         */
        public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
        /**
         * Eight-bit UCS Transformation Format
         */
        public static final Charset UTF_8 = Charset.forName("UTF-8");

        public static final Charset UNICODE = Charset.forName("UNICODE");


        /**
         * Sixteen-bit UCS Transformation Format, big-endian byte order
         */
        static  public final Charset UTF_16BE;
        static {
            UTF_16BE = Charset.forName("UTF-16BE");
        }
        /**
         * Sixteen-bit UCS Transformation Format, little-endian byte order
         */
        static public final Charset UTF_16LE;
        static {
            UTF_16LE = Charset.forName("UTF-16LE");
        }
        /**
         * Sixteen-bit UCS Transformation Format, byte order identified by an
         * optional byte-order mark
         */
        static public final Charset UTF_16;
        static {
            UTF_16 = Charset.forName("UTF-16");
        }

        /**
         * Operating system default encoding
         * sun.jnu.encoding refers to the default encoding of the operating system, and file.encoding refers to the encoding of JAVA files (remember, not class files, all class files are encoded in UTF-8), so, in the same operating system The sun.jnu.encoding of the JAVA application running on the computer is exactly the same, and even if the file.encoding is in the same JAVA application, the encoding of the JAVA file can be different.
         *       * In most cases, sun.jnu.encoding is transparent to us.
         *
         */
        public static String getOperateSystemCharset() {
            return System.getProperty("sun.jnu.encoding");
        }

        public static String getOperateSystemCharsetOrDefaultCharsetName() {
            return getOperateSystemCharsetOrDefaultCharset().name();
        }
        public static Charset getOperateSystemCharsetOrDefaultCharset() {
            String charset = getOperateSystemCharset();
            return null != charset ? Charset.forName(charset)
                    : defaultCharset();
        }


        public static String  defaultCharsetName() {
            return defaultCharset().name();
        }
        public static Charset defaultCharset() {
            return Charset.defaultCharset();
        }
    }

    public static class Property {
        @SuppressWarnings("SpellCheckingInspection")
        public static final String JAVA_IO_TEMPDIR = "java.io.tmpdir";
    }

    public static final class FileOptMode {
        public static String r() {
            return "r";
        }
        public static String rw() {
            return "rw";
        }
        public static String rws() {
            return "rws";
        }
    }












    public static Set<String> getMessageDigestAlgorithms() {
        Set<String> list = new HashSet<>();
        for (String Str : Security.getAlgorithms("MessageDigest")) {
            list.add(Str);
        }
        return list;
    }
    public static String[] getOperateSystemEnvironment() {
        Map<String, String> env = System.getenv();
        String[] environments = new String[env.size()];
        int environmentsIndex = 0;
        for (String key: env.keySet()) {
            environments[environmentsIndex++] = key + '=' + env.get(key);
        }
        return environments;
    }




}
