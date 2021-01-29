package top.fols.atri.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;

public class Finals {

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    public static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;


    public static final Field[] 		EMPTY_FIELD_ARRAY = new Field[]{};
    public static final Constructor[] 	EMPTY_CONSTRUCTOR_ARRAY = new Constructor[]{};
    public static final Method[] 		EMPTY_METHOD_ARRAY = new Method[]{};

    public static final Class[] 		EMPTY_CLASS_ARRAY = new Class[]{};
    public static final Object[] 		EMPTY_OBJECT_ARRAY = new Object[]{};



    public static final char[] EMPTY_CHAR_BUFFER = new char[]{};
    public static final byte[] EMPTY_BYTE_BUFFER = new byte[]{};


    public static final String TRUE_STRING = String.valueOf(true);
    public static final String FALSE_STRING = String.valueOf(false);
    public static final String NULL_STRING = String.valueOf((Object) null);

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
  //public static final Class<int[]>        VOID_ARRAY_CLASS = void[].class;

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

    public static final Class<Object>           OBJECT_CLASS = Object.class;
    public static final Class<Class>            CLASS_CLASS = Class.class;
    public static final Class<String>           STRING_CLASS = String.class;
    public static final Class<Method>           METHOD_CLASS = Method.class;
    public static final Class<Field>            FIELD_CLASS = Field.class;
    public static final Class<Constructor>      CONSTRUCTOR_CLASS = Constructor.class;

    public static final Class<Object[]>         OBJECT_ARRAY_CLASS = Object[].class;
    public static final Class<Class[]>          CLASS_ARRAY_CLASS = Class[].class;
    public static final Class<String[]>         STRING_ARRAY_CLASS = String[].class;
    public static final Class<Method[]>         METHOD_ARRAY_CLASS = Method[].class;
    public static final Class<Field[]>          FIELD_ARRAY_CLASS = Field[].class;
    public static final Class<Constructor[]>    CONSTRUCTOR_ARRAY_CLASS = Constructor[].class;




    public static final String SYSTEM_LINE_SEPARATOR = System.lineSeparator(); //write command line spearator


    public static String[] getOperateSystemEnvironment() {
        Map<String, String> env = System.getenv();
        String[] environments = new String[env.size()];
        int environmentsIndex = 0;
        for (String key: env.keySet()) {
            environments[environmentsIndex++] = key + '=' + env.get(key);
        }
        return environments;
    }



    /**
     * Operating system default encoding
     * sun.jnu.encoding refers to the default encoding of the operating system, and file.encoding refers to the encoding of JAVA files (remember, not class files, all class files are encoded in UTF-8), so, in the same operating system The sun.jnu.encoding of the JAVA application running on the computer is exactly the same, and even if the file.encoding is in the same JAVA application, the encoding of the JAVA file can be different.
     *       * In most cases, sun.jnu.encoding is transparent to us.
     *
     * LINUX 系统默认编码utf-8
     * WINDOWS 默认编码GBK
     * @return
     */
    public static String getOperateSystemCharset() {
        String charset = System.getProperty("sun.jnu.encoding");
        return charset;
    }
    public static String getOperateSystemCharsetOrDefaultCharset() {
        String charset = Finals.getOperateSystemCharset();
        return null != charset ? charset
                : Finals.defaultCharsetName();
    }


    public static String defaultCharsetName() {
        return Finals.defaultCharset().name();
    }
    public static Charset defaultCharset() {
        return Charset.defaultCharset();
    }
}
