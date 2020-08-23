package top.fols.box.io.base;

import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import top.fols.box.io.XStream;
import top.fols.box.lang.abstracts.XBitsOptionAbstract;
import top.fols.box.lang.reflect.optdeclared.XReflectAccessible;
import top.fols.box.lang.reflect.safety.XReflect;
import top.fols.box.statics.XStaticBaseType;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XByteEncode;


/**
 * no synchronized
 */
public final class XObjectStreams implements Flushable {


    private static Class forClassLoaderLoadClass(ClassLoader cl, String name) throws ClassNotFoundException {
        if (null == cl) {
            return Class.forName(name);
        } else {
            return cl.loadClass(name);
        }
    }



    /**
     * no cache
     * 
     * @return Methods if private method cannot be reflected
     */
    private static Field[] getAllFields(Class cls) {
        if (null == cls) {
            return null;
        } else {
            try {
                Field[] fields = XReflectAccessible.getFieldsAllSetAccessible(cls);
                return fields;
            } catch (Throwable e) {
                return XReflect.getFields(cls);
            }
        }
	}
    private static Constructor getConstructor(Class cls) {
        if (null == cls) {
            return null;
        } else {
            try {
                Constructor constructor = XReflectAccessible.getConstructorSetAccessible(cls, new Class[]{});
                return constructor;
            } catch (Throwable e) {
                return XReflect.getConstructor(cls, new Class[]{});
            }
        }
	}




    /**
     * FAST
     * WEAK_CACHE not need manager
     */
    private static final Map<ClassLoader, Map<String, Class>> FOR_NAME_CACHE = new WeakHashMap<>(); 
    private static Class cacheForName(ClassLoader cl, String name) throws ClassNotFoundException {
        Map<String, Class> classloadermap = FOR_NAME_CACHE.get(cl);
        if (null == classloadermap) {
            FOR_NAME_CACHE.put(cl, classloadermap = new WeakHashMap<String, Class>());
        }
        Class cls = classloadermap.get(name);
        if (null == cls) {
            classloadermap.put(name, cls = forClassLoaderLoadClass(cl, name));
        }
        return cls;
    }
    /**
     * FAST
     * WEAK_CACHE not need manager
     */
    private static final Map<Class, Field[]> GET_FIELDS_CACHE = new WeakHashMap<>();
    private static Field[] cacheGetFields(Class cls) {
        Field[]  fs = GET_FIELDS_CACHE.get(cls);
        if (null == fs) {
            Field[] newFields = cls == XStaticBaseType.Object_class ?XStaticFixedValue.nullFieldArray: 
                getAllFields(cls);
            List<Field> newFieldsList = new ArrayList<>(newFields.length);
            for (int i = 0;i < newFields.length;i++) {
                Field f = newFields[i];
                int mod = f.getModifiers();
                if (Modifier.isTransient(mod) || Modifier.isStatic(mod)) {
                    continue;
                }
                newFieldsList.add(f);
            }
            GET_FIELDS_CACHE.put(cls, fs = newFieldsList.toArray(new Field[newFieldsList.size()]));
            newFieldsList.clear();
        }
        return fs;
    }
    /**
     * FAST
     * WEAK_CACHE not need manager
     */
    private static final Map<Class, Map<String, Field>> GET_FIELD_CACHE = new WeakHashMap<>();
    private static Field cacheGetField(Class cls, String name) {
        Map<String, Field> fm = GET_FIELD_CACHE.get(cls);
        if (null == fm) {
            Map<String, Field> newFm = new WeakHashMap<>();
            Field[] newFields = cls == XStaticBaseType.Object_class ?XStaticFixedValue.nullFieldArray: 
                getAllFields(cls);
            for (Field f: newFields) {
                newFm.put(f.getName(), f);
            }
            GET_FIELD_CACHE.put(cls, fm = newFm);
        }
        Field f = fm.get(name);
        return f;
    }
    /**
     * FAST
     * WEAK_CACHE not need manager
     */
    private static final Map<Class, Constructor> GET_CONSTRUCTOR_CACHE = new WeakHashMap<>();
    private static Constructor cacheGetConstructor(Class cls) throws NoSuchMethodException {
        Constructor con = GET_CONSTRUCTOR_CACHE.get(cls);
        if (null == con) {
            con = getConstructor(cls);
            if (null == con) {
                throw new RuntimeException("no soch available constructor " + cls.getName() + ".<init>()");
            }
            GET_CONSTRUCTOR_CACHE.put(cls, con);
        }
        if (null == con) {
            throw new NoSuchMethodException(cls.getName() + ".<init>()");
        }
        return con;
    }





















    private InputStream is;
    private OutputStream os;

    public XObjectStreams(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    public void setInputStream(InputStream is) {
        if (this.is != is) {
            this.clearStatus();
        }
        this.is = is;
    }

    public InputStream getInputStream() {
        return this.is;
    }

    public void setOutputStream(OutputStream os) {
        if (this.os != os) {
            this.clearStatus();
        }
        this.os = os;
    }

    public OutputStream getOutputStream() {
        return this.os;
    }


    // 更换流的时候使用
    public void clearStatus() {
        this.isLastReadType = false;
        this.lastType = -1;

        Arrays.fill(this.readCache, (byte) 0);
        Arrays.fill(this.writeCache, (byte) 0);
    }

    public int read() throws IOException {
        return this.is.read();
    }

    public int read(byte[] bytes) throws IOException {
        return this.read(bytes, 0, bytes.length);
    }

    public int read(byte[] bytes, int off, int len) throws IOException {
        return this.is.read(bytes, off, len);
    }

    public void write(int b) throws IOException {
        this.os.write(b);
    }

    public void write(byte[] bytes) throws IOException {
        this.write(bytes, 0, bytes.length);
    }

    public void write(byte[] bytes, int off, int len) throws IOException {
        this.os.write(bytes, off, len);
    }

    @Override
    public void flush() throws IOException {
        this.os.flush();
    }

    public void closeInput() throws IOException {
        if (null == this.is) {
            throw new IOException("no input stream");
        }
        this.is.close();
    }

    public void closeOutput() throws IOException {
        if (null == this.os) {
            throw new IOException("no output stream");
        }
        this.os.close();
    }



    static class ObjectReader {
        Class cls = null;

        public Object read(XObjectStreams s) throws IOException {
            throw new IOException("without this implementation");
        }
    }


    // 不要超过 Byte.MAX_VALUE 个
    static final transient ObjectReader[] OBJECT_READER_LIST = new ObjectReader[] {
        // new ObjectRw(){{ 
        //                //因为byte 默认为值为0所以避免一下
        //                cls = ObjectRw.class; } @Override public Object read(XObjectStreams2 s) throws IOException { 
        //                    throw new IOException("read type that is impossible to read"); }
        //        },

        new ObjectReader(){{ 
                cls = Boolean.class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readBoolean(); }  

        },

        new ObjectReader(){{ 
                cls = Character.class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readChar(); } 

        },

        new ObjectReader(){{ 
                cls = Short.class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readShort(); } 

        },

        new ObjectReader(){{ 
                cls = Integer.class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readInt(); } 

        },

        new ObjectReader(){{ 
                cls = Float.class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readFloat(); } 

        },

        new ObjectReader(){{ 
                cls = Long.class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readLong(); } 

        },

        new ObjectReader(){{ 
                cls = Double.class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readDouble(); } 

        },

        new ObjectReader(){{ 
                cls = Byte.class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readByte(); } 

        },

        new ObjectReader(){{ 
                cls = String.class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readChars(); } 

        },



        new ObjectReader(){{ 
                cls = boolean[].class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readBooleanArray(); } 

        },

        new ObjectReader(){{ 
                cls = char[].class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readCharArray(); } 

        },

        new ObjectReader(){{ 
                cls = short[].class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readShortArray(); } 

        },

        new ObjectReader(){{ 
                cls = int[].class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readIntArray(); } 

        },

        new ObjectReader(){{ 
                cls = float[].class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readFloatArray(); } 

        },

        new ObjectReader(){{ 
                cls = long[].class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readLongArray(); } 

        },

        new ObjectReader(){{ 
                cls = double[].class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readDoubleArray(); } 

        },

        new ObjectReader(){{ 
                cls = byte[].class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readByteArray(); } 

        },

        new ObjectReader(){{ 
                cls = String[].class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readCharsArray(); } 

        },




        new ObjectReader(){{ 
                cls = null; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readNull(); } 

        },


        /**  Object type  */

        new ObjectReader(){{ 
                cls = Class.class; } @Override public Object read(XObjectStreams s) throws IOException { return
                    s.readClass(); } 

        },



        new ObjectReader(){{ 
                cls = Object.class; } @Override public Object read(XObjectStreams s) throws IOException { return
                    s.readClassInstaceObject0(); } 

        },


        new ObjectReader(){{ 
                cls = Object[].class; } @Override public Object read(XObjectStreams s) throws IOException { return 
                    s.readObjectArray(); } 

        },
    };









    static class ObjectWriter {
        Class cls = null;

        public void write(XObjectStreams s, Object value) throws IOException {
            throw new IOException("without this implementation");
        }
    }

    static final Map<Class, ObjectWriter> OBJECT_WRITER_LIST_MAP = new HashMap<>();
    static {
        /**
         * 顺序可以和 OBJECT_READER_LIST 不一样
         */

        ObjectWriter[] OBJECT_WRITER_LIST = new ObjectWriter[] {
            // new ObjectRw(){{ 
            //                //因为byte 默认为值为0所以避免一下
            //                cls = ObjectRw.class; } @Override public void write(XObjectStreams2 s, Object value) throws IOException { 
            //                        throw new IOException("read type that is impossible to read");}
            //        },

            new ObjectWriter(){{ 
                    cls = Boolean.class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeBoolean((boolean)value); }
            },

            new ObjectWriter(){{ 
                    cls = Character.class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeChar((char)value); }
            },

            new ObjectWriter(){{ 
                    cls = Short.class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeShort((short)value); }
            },

            new ObjectWriter(){{ 
                    cls = Integer.class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeInt((int)value); }
            },

            new ObjectWriter(){{ 
                    cls = Float.class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeFloat((float)value); }
            },

            new ObjectWriter(){{ 
                    cls = Long.class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeLong((long)value); }
            },

            new ObjectWriter(){{ 
                    cls = Double.class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeDouble((double)value); }
            },

            new ObjectWriter(){{ 
                    cls = Byte.class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeByte((byte)value); }
            },

            new ObjectWriter(){{ 
                    cls = String.class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeChars((String)value); }
            },



            new ObjectWriter(){{ 
                    cls = boolean[].class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeBooleanArray((boolean[])value); }
            },

            new ObjectWriter(){{ 
                    cls = char[].class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeCharArray((char[])value); }
            },

            new ObjectWriter(){{ 
                    cls = short[].class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeShortArray((short[])value); }
            },

            new ObjectWriter(){{ 
                    cls = int[].class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeIntArray((int[])value); }
            },

            new ObjectWriter(){{ 
                    cls = float[].class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeFloatArray((float[])value); }
            },

            new ObjectWriter(){{ 
                    cls = long[].class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeLongArray((long[])value); }
            },

            new ObjectWriter(){{ 
                    cls = double[].class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeDoubleArray((double[])value); }
            },

            new ObjectWriter(){{ 
                    cls = byte[].class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeByteArray((byte[])value); }
            },

            new ObjectWriter(){{ 
                    cls = String[].class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeCharsArray((String[])value); }
            },




            new ObjectWriter(){{ 
                    cls = null; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeNull(); }
            },


            /**  Object type  */

            new ObjectWriter(){{ 
                    cls = Class.class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeClass((Class)value); }

            },


            new ObjectWriter(){{ 
                    cls = Object.class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeClassInstaceObject0(value); }
            },


            new ObjectWriter(){{ 
                    cls = Object[].class; } 
                @Override public void write(XObjectStreams s, Object value) throws IOException { 
                    s.writeObjectArray((Object[])value); }
            },
        };

        for (ObjectWriter ow: OBJECT_WRITER_LIST) {
            OBJECT_WRITER_LIST_MAP.put(ow.cls, ow);
        }
    }









    static final int objectReaderInterfaceCount() {
        return OBJECT_READER_LIST.length;
    }
    static final ObjectReader atObjectReaderInterface(int index) {
        return OBJECT_READER_LIST[index];
    }
    static final int findObjectReaderInterface(Class cls) {
        for (int i = 0; i < OBJECT_READER_LIST.length; i++) {
            ObjectReader ro = OBJECT_READER_LIST[i];
            if (ro.cls == cls) {
                return i;
            }
        }
        throw new RuntimeException("not found class: " + cls);
    }




    static final ObjectWriter atObjectWriterInterface(Class index) {
        return OBJECT_WRITER_LIST_MAP.get(index);
    }















    static final byte READ_TYPE_BOOLEAN = (byte) findObjectReaderInterface(Boolean.class);
    static final byte READ_TYPE_CHAR = (byte) findObjectReaderInterface(Character.class);
    static final byte READ_TYPE_SHORT = (byte) findObjectReaderInterface(Short.class);
    static final byte READ_TYPE_INT = (byte) findObjectReaderInterface(Integer.class);
    static final byte READ_TYPE_FLOAT = (byte) findObjectReaderInterface(Float.class);
    static final byte READ_TYPE_LONG = (byte) findObjectReaderInterface(Long.class);
    static final byte READ_TYPE_DOUBLE = (byte) findObjectReaderInterface(Double.class);
    static final byte READ_TYPE_BYTE = (byte) findObjectReaderInterface(Byte.class);

    static final byte READ_TYPE_CHARS = (byte) findObjectReaderInterface(String.class);


    static final byte READ_TYPE_BOOLEAN_ARRAY = (byte) findObjectReaderInterface(boolean[].class);
    static final byte READ_TYPE_CHAR_ARRAY = (byte) findObjectReaderInterface(char[].class);
    static final byte READ_TYPE_SHORT_ARRAY = (byte) findObjectReaderInterface(short[].class);
    static final byte READ_TYPE_INT_ARRAY = (byte) findObjectReaderInterface(int[].class);
    static final byte READ_TYPE_FLOAT_ARRAY = (byte) findObjectReaderInterface(float[].class);
    static final byte READ_TYPE_LONG_ARRAY = (byte) findObjectReaderInterface(long[].class);
    static final byte READ_TYPE_DOUBLE_ARRAY = (byte) findObjectReaderInterface(double[].class);
    static final byte READ_TYPE_BYTE_ARRAY = (byte) findObjectReaderInterface(byte[].class);

    static final byte READ_TYPE_CHARS_ARRAY = (byte) findObjectReaderInterface(String[].class);

    static final byte READ_TYPE_NULL = (byte) findObjectReaderInterface(null);

    static final byte READ_TYPE_CLASS = (byte) findObjectReaderInterface(Class.class);



    static final byte READ_TYPE_OBJECT = (byte) findObjectReaderInterface(Object.class);
    static final byte READ_TYPE_OBJECT_ARRAY = (byte) findObjectReaderInterface(Object[].class);




    void writeType0(byte writeType) throws IOException {
        os.write(writeType);
    }

    /**
     * 进行数据读取后记得将 isLastReadType 设置为false
     * 
     * @return writeType0 writeType
     */
    byte readType0() throws IOException {
        if (this.isLastReadType) {
            return this.lastType;
        }
        int read = is.read();
        if (read == -1) {
            return -1;// stream read = -1;
        }
        byte readType = (byte) read;
        this.isLastReadType = true;
        return this.lastType = readType;
    }

    void requireType0(byte readType, byte requireType) throws IOException {
        if (readType != requireType) {
            String requireTypeClassName = (requireType >= 0 && requireType < objectReaderInterfaceCount())
                ? (null == atObjectReaderInterface(requireType).cls ? "null"
                : atObjectReaderInterface(requireType).cls.getName())
                : "unknown";
            String readTypeClassName = (readType >= 0 && readType < objectReaderInterfaceCount())
                ? (null == atObjectReaderInterface(readType).cls ? "null"
                : atObjectReaderInterface(readType).cls.getName())
                : ((int)readType == -1 ? "stream read = -1" : "unknown");
            throw new IOException(
                String.format("requireType=%s, readType=%s", requireTypeClassName, readTypeClassName));
        }
    }

    // 及时写入的数据不存在残留
    transient byte[] writeCache = new byte[XBitsOptionAbstract.BIG_ENDIAN.MAX_DATA_LENGTH()];

    public void writeBoolean(boolean value) throws IOException {
        this.writeType0(READ_TYPE_BOOLEAN);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.boolean_byte_length;
        XBitsOptionAbstract.BIG_ENDIAN.putBytes(this.writeCache, 0, value);
        os.write(this.writeCache, off, length);
    }

    public void writeBooleanArray(boolean[] value) throws IOException {
        this.writeBooleanArray(value, 0, null == value ? 0 : value.length);
    }

    public void writeBooleanArray(boolean[] value, int voff, int vlen) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }
        if (vlen > 0) {
            boolean temp = value[voff + vlen - 1];
        }
        this.writeType0(READ_TYPE_BOOLEAN_ARRAY);
        os.write(XBitsOptionAbstract.BIG_ENDIAN.getBytes(vlen));

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.boolean_byte_length;
        byte[] buf = new byte[length * vlen];
        for (int i = 0; i < vlen; i++) {
            XBitsOptionAbstract.BIG_ENDIAN.putBytes(buf, off, value[voff + i]);
            off += length;
        }
        os.write(buf);
        buf = null;
    }

    public void writeChar(char value) throws IOException {
        this.writeType0(READ_TYPE_CHAR);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.char_byte_length;
        XBitsOptionAbstract.BIG_ENDIAN.putBytes(this.writeCache, 0, value);
        os.write(this.writeCache, off, length);
    }

    public void writeCharArray(char[] value) throws IOException {
        this.writeCharArray(value, 0, null == value ? 0 : value.length);
    }

    public void writeCharArray(char[] value, int voff, int vlen) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }
        if (vlen > 0) {
            char temp = value[voff + vlen - 1];
        }
        this.writeType0(READ_TYPE_CHAR_ARRAY);
        os.write(XBitsOptionAbstract.BIG_ENDIAN.getBytes(vlen));

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.char_byte_length;
        byte[] buf = new byte[length * vlen];
        for (int i = 0; i < vlen; i++) {
            XBitsOptionAbstract.BIG_ENDIAN.putBytes(buf, off, value[voff + i]);
            off += length;
        }
        os.write(buf);
        buf = null;
    }

    public void writeShort(short value) throws IOException {
        this.writeType0(READ_TYPE_SHORT);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.short_byte_length;
        XBitsOptionAbstract.BIG_ENDIAN.putBytes(this.writeCache, 0, value);
        os.write(this.writeCache, off, length);
    }

    public void writeShortArray(short[] value) throws IOException {
        this.writeShortArray(value, 0, null == value ? 0 : value.length);
    }

    public void writeShortArray(short[] value, int voff, int vlen) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }
        if (vlen > 0) {
            short temp = value[voff + vlen - 1];
        }
        this.writeType0(READ_TYPE_SHORT_ARRAY);
        os.write(XBitsOptionAbstract.BIG_ENDIAN.getBytes(vlen));

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.short_byte_length;
        byte[] buf = new byte[length * vlen];
        for (int i = 0; i < vlen; i++) {
            XBitsOptionAbstract.BIG_ENDIAN.putBytes(buf, off, value[voff + i]);
            off += length;
        }
        os.write(buf);
        buf = null;
    }

    public void writeInt(int value) throws IOException {
        this.writeType0(READ_TYPE_INT);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.int_byte_length;
        XBitsOptionAbstract.BIG_ENDIAN.putBytes(this.writeCache, 0, value);
        os.write(this.writeCache, off, length);
    }

    public void writeIntArray(int[] value) throws IOException {
        this.writeIntArray(value, 0, null == value ? 0 : value.length);
    }

    public void writeIntArray(int[] value, int voff, int vlen) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }
        if (vlen > 0) {
            int temp = value[voff + vlen - 1];
        }
        this.writeType0(READ_TYPE_INT_ARRAY);
        os.write(XBitsOptionAbstract.BIG_ENDIAN.getBytes(vlen));

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.int_byte_length;
        byte[] buf = new byte[length * vlen];
        for (int i = 0; i < vlen; i++) {
            XBitsOptionAbstract.BIG_ENDIAN.putBytes(buf, off, value[voff + i]);
            off += length;
        }
        os.write(buf);
        buf = null;
    }

    public void writeFloat(float value) throws IOException {
        this.writeType0(READ_TYPE_FLOAT);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.float_byte_length;
        XBitsOptionAbstract.BIG_ENDIAN.putBytes(this.writeCache, 0, value);
        os.write(this.writeCache, off, length);
    }

    public void writeFloatArray(float[] value) throws IOException {
        this.writeFloatArray(value, 0, null == value ? 0 : value.length);
    }

    public void writeFloatArray(float[] value, int voff, int vlen) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }
        if (vlen > 0) {
            float temp = value[voff + vlen - 1];
        }
        this.writeType0(READ_TYPE_FLOAT_ARRAY);
        os.write(XBitsOptionAbstract.BIG_ENDIAN.getBytes(vlen));

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.float_byte_length;
        byte[] buf = new byte[length * vlen];
        for (int i = 0; i < vlen; i++) {
            XBitsOptionAbstract.BIG_ENDIAN.putBytes(buf, off, value[voff + i]);
            off += length;
        }
        os.write(buf);
        buf = null;
    }

    public void writeLong(long value) throws IOException {
        this.writeType0(READ_TYPE_LONG);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.long_byte_length;
        XBitsOptionAbstract.BIG_ENDIAN.putBytes(this.writeCache, 0, value);
        os.write(this.writeCache, off, length);
    }

    public void writeLongArray(long[] value) throws IOException {
        this.writeLongArray(value, 0, null == value ? 0 : value.length);
    }

    public void writeLongArray(long[] value, int voff, int vlen) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }
        if (vlen > 0) {
            long temp = value[voff + vlen - 1];
        }
        this.writeType0(READ_TYPE_LONG_ARRAY);
        os.write(XBitsOptionAbstract.BIG_ENDIAN.getBytes(vlen));

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.long_byte_length;
        byte[] buf = new byte[length * vlen];
        for (int i = 0; i < vlen; i++) {
            XBitsOptionAbstract.BIG_ENDIAN.putBytes(buf, off, value[voff + i]);
            off += length;
        }
        os.write(buf);
        buf = null;
    }

    public void writeDouble(double value) throws IOException {
        this.writeType0(READ_TYPE_DOUBLE);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.double_byte_length;
        XBitsOptionAbstract.BIG_ENDIAN.putBytes(this.writeCache, 0, value);
        os.write(this.writeCache, off, length);
    }

    public void writeDoubleArray(double[] value) throws IOException {
        this.writeDoubleArray(value, 0, null == value ? 0 : value.length);
    }

    public void writeDoubleArray(double[] value, int voff, int vlen) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }
        if (vlen > 0) {
            double temp = value[voff + vlen - 1];
        }
        this.writeType0(READ_TYPE_DOUBLE_ARRAY);
        os.write(XBitsOptionAbstract.BIG_ENDIAN.getBytes(vlen));

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.double_byte_length;
        byte[] buf = new byte[length * vlen];
        for (int i = 0; i < vlen; i++) {
            XBitsOptionAbstract.BIG_ENDIAN.putBytes(buf, off, value[voff + i]);
            off += length;
        }
        os.write(buf);
        buf = null;
    }

    public void writeByte(byte value) throws IOException {
        this.writeType0(READ_TYPE_BYTE);
        os.write(value);
    }

    public void writeByteArray(byte[] value) throws IOException {
        this.writeByteArray(value, 0, null == value ? 0 : value.length);
    }

    public void writeByteArray(byte[] value, int voff, int vlen) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }
        if (vlen > 0) {
            byte temp = value[voff + vlen - 1];
        }
        this.writeType0(READ_TYPE_BYTE_ARRAY);
        os.write(XBitsOptionAbstract.BIG_ENDIAN.getBytes(vlen));
        os.write(value, voff, vlen);
    }

    public void writeChars(String value) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }
        int charlen = value.length();
        byte[] bytes = new byte[XByteEncode.UnicodeOption.BIG_ENDIAN.charsLenToBytesLen(charlen)];
        XByteEncode.UnicodeOption.BIG_ENDIAN.putCharsToBytes(value, 0, charlen, bytes, 0);

        this.writeType0(READ_TYPE_CHARS);
        os.write(XBitsOptionAbstract.BIG_ENDIAN.getBytes(charlen));// write length
        os.write(bytes);// write data

        bytes = null;
    }

    public void writeCharsArray(String[] value) throws IOException {
        this.writeCharsArray(value, 0, null == value ? 0 : value.length);
    }

    public void writeCharsArray(String[] value, int voff, int vlen) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }
        if (vlen > 0) {
            String temp = value[voff + vlen - 1];
        }
        this.writeType0(READ_TYPE_CHARS_ARRAY);
        os.write(XBitsOptionAbstract.BIG_ENDIAN.getBytes(vlen));
        for (int i = 0; i < vlen; i++) {
            this.writeChars(value[voff + i]);
        }
    }

    public void writeNull() throws IOException {
        this.writeType0(READ_TYPE_NULL);
    }

    public void writeClass(Class value) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }

        this.writeType0(READ_TYPE_CLASS);
        String className = value.getName();
        this.writeChars(className);
    }










    private void _writeCollectionData0(Collection value) throws IOException {
        int count = value.size();
        this.writeInt(count);
        Iterator iterator = value.iterator();
        for (int i = 0;i < count;i++) {
            Object element;
            try {
                element = iterator.next();
            } catch (Throwable e) {
                element = null;
            }
            this.writeObject(element);
        }
    }
    private void _writeMapData0(Map value) throws IOException {
        int count = value.size();
        this.writeInt(count);
        Iterator iterator = value.keySet().iterator();
        for (int i = 0;i < count;i++) {
            Object key;
            try {
                key = iterator.next();
            } catch (Throwable e) {
                key = null;
            }
            this.writeObject(key);
            this.writeObject(value.get(key));
        }
    }
    /**
     * from @method writeObject execute this method
     * write public JavaObject
     * @param value nullable
     */
    private void writeClassInstaceObject0(Object value) throws IOException {
//        if (null == value) {
//            this.writeNull();
//            return;
//      }

        this.writeType0(READ_TYPE_OBJECT);
        Class cls = value.getClass();
        this.writeClass(cls);
        Field[] fields = cacheGetFields(cls);
        this.writeInt(fields.length);
        try {
            for (Field f: fields) {
                String fname = f.getName();
                Object fvalue = f.get(value);
                this.writeChars(fname);
                this.writeObject(fvalue);
            }
        } catch (Throwable e) {
            throw new IOException(e);
        }
        if (value instanceof Map) {
            this._writeMapData0((Map)value);
        } else if (value instanceof Collection) {
            this._writeCollectionData0((Collection)value);
        }
    }






    public void writeObject(Object value) throws IOException {
        // 也可以注解掉 因为findBaseObjectRwInterfaceW 可以搜索null
        if (null == value) {
            this.writeNull();
            return;
        }

        ObjectWriter writer = this.atObjectWriterInterface(value.getClass());
        if (null != writer) {
            writer.write(this, value);
        } else if (value instanceof Object[]) {
            //write instanceOf Object[]
            this.writeObjectArray((Object[]) value);
        } else {
            //write Object
            this.writeClassInstaceObject0(value);
        }
    }

    public void writeObjectArray(Object[] value) throws IOException {
        this.writeObjectArray(value, 0, null == value ? 0 : value.length);
    }

    public void writeObjectArray(Object[] value, int voff, int vlen) throws IOException {
        if (null == value) {
            this.writeNull();
            return;
        }
        if (vlen > 0) {
            Object temp = value[voff + vlen - 1];
        }

        this.writeType0(READ_TYPE_OBJECT_ARRAY);

        Class elementClass = value.getClass().getComponentType();
        int length = value.length;

        this.writeChars(elementClass.getName());// 元素类名
        this.writeInt(length);
        for (int i = 0; i < vlen; i++) {
            this.writeObject(value[voff + i]);
        }
    }


















    transient ClassLoader cl;
    public XObjectStreams setReadObjectInClassLoader(ClassLoader cl) {
        this.cl = cl;
        return this;
    }
    public ClassLoader getReadObjectInClassLoader() {
        return this.cl;
    }







    // 及时读取的数据不存在残留
    transient byte[] readCache = new byte[XBitsOptionAbstract.BIG_ENDIAN.MAX_DATA_LENGTH()];

    // require read data
    int readToCacheFixedLength(InputStream is, int len) throws IOException {
        int read = is.read(this.readCache, 0, len);
        if (read == len) {
            return read;
        }

        if (read == -1) {
            throw new IOException("no data read");
        }
        int off = read;
        int need = len - off;
        int alread = read;
        while (need > 0 && (read = is.read(this.readCache, off, need)) != -1) {
            off += read;
            need -= read;
            alread += read;
        }
        if (alread == len) {
            return len;
        }
        throw new IOException(String.format("requireReadLength=%s, read=%s", len, alread));
    }

    byte[] readFixedLength(InputStream is, int len) throws IOException {
        byte[] buf = new byte[len];
        int read = is.read(buf, 0, len);
        if (read == len) {
            return buf;
        }

        XByteArrayOutputStream out = new XByteArrayOutputStream();
        out.setBuff(buf, read);
        out.seekIndex(read);

        int off = read;
        int need = len - off;
        int alread = read;

        while (need > 0) {
            int read2 = (int) XStream.copyFixedLength(is, out, (long) need);
            if (read2 > 0) {
                off += read2;
                need -= read2;
                alread += read2;
            }
        }

        if (alread == len) {
            byte[] bytes = out.toByteArray();
            out.releaseBuffer();
            return bytes;
        }
        out.releaseBuffer();
        throw new IOException(String.format("requireReadLength=%s, read=%s", len, alread));
    }

    public boolean readBoolean() throws IOException {
        this.requireType0(this.readType0(), READ_TYPE_BOOLEAN);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.boolean_byte_length);
        return XBitsOptionAbstract.BIG_ENDIAN.getBoolean(this.readCache, 0);
    }

    public boolean[] readBooleanArray() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (boolean[]) this.readNull();
        }
        this.requireType0(type, READ_TYPE_BOOLEAN_ARRAY);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.int_byte_length);
        int arrlen = XBitsOptionAbstract.BIG_ENDIAN.getInt(this.readCache, 0);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.boolean_byte_length;
        byte[] bytes = this.readFixedLength(is, arrlen * length);

        boolean[] arr = new boolean[arrlen];
        for (int i = 0; i < arrlen; i++) {
            arr[i] = XBitsOptionAbstract.BIG_ENDIAN.getBoolean(bytes, off);
            off += length;
        }
        bytes = null;
        return arr;
    }

    public char readChar() throws IOException {
        this.requireType0(this.readType0(), READ_TYPE_CHAR);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.char_byte_length);
        return XBitsOptionAbstract.BIG_ENDIAN.getChar(this.readCache, 0);
    }

    public char[] readCharArray() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (char[]) this.readNull();
        }
        this.requireType0(type, READ_TYPE_CHAR_ARRAY);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.int_byte_length);
        int arrlen = XBitsOptionAbstract.BIG_ENDIAN.getInt(this.readCache, 0);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.char_byte_length;
        byte[] bytes = this.readFixedLength(is, arrlen * length);

        char[] arr = new char[arrlen];
        for (int i = 0; i < arrlen; i++) {
            arr[i] = XBitsOptionAbstract.BIG_ENDIAN.getChar(bytes, off);
            off += length;
        }
        bytes = null;
        return arr;
    }

    public short readShort() throws IOException {
        this.requireType0(this.readType0(), READ_TYPE_SHORT);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.short_byte_length);
        return XBitsOptionAbstract.BIG_ENDIAN.getShort(this.readCache, 0);
    }

    public short[] readShortArray() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (short[]) this.readNull();
        }
        this.requireType0(type, READ_TYPE_SHORT_ARRAY);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.int_byte_length);
        int arrlen = XBitsOptionAbstract.BIG_ENDIAN.getInt(this.readCache, 0);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.short_byte_length;
        byte[] bytes = this.readFixedLength(is, arrlen * length);

        short[] arr = new short[arrlen];
        for (int i = 0; i < arrlen; i++) {
            arr[i] = XBitsOptionAbstract.BIG_ENDIAN.getShort(bytes, off);
            off += length;
        }
        bytes = null;
        return arr;
    }

    public int readInt() throws IOException {
        this.requireType0(this.readType0(), READ_TYPE_INT);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.int_byte_length);
        return XBitsOptionAbstract.BIG_ENDIAN.getInt(this.readCache, 0);
    }

    public int[] readIntArray() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (int[]) this.readNull();
        }
        this.requireType0(type, READ_TYPE_INT_ARRAY);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.int_byte_length);
        int arrlen = XBitsOptionAbstract.BIG_ENDIAN.getInt(this.readCache, 0);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.int_byte_length;
        byte[] bytes = this.readFixedLength(is, arrlen * length);

        int[] arr = new int[arrlen];
        for (int i = 0; i < arrlen; i++) {
            arr[i] = XBitsOptionAbstract.BIG_ENDIAN.getInt(bytes, off);
            off += length;
        }
        bytes = null;
        return arr;
    }

    public float readFloat() throws IOException {
        this.requireType0(this.readType0(), READ_TYPE_FLOAT);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.float_byte_length);
        return XBitsOptionAbstract.BIG_ENDIAN.getFloat(this.readCache, 0);
    }

    public float[] readFloatArray() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (float[]) this.readNull();
        }
        this.requireType0(type, READ_TYPE_FLOAT_ARRAY);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.int_byte_length);
        int arrlen = XBitsOptionAbstract.BIG_ENDIAN.getInt(this.readCache, 0);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.float_byte_length;
        byte[] bytes = this.readFixedLength(is, arrlen * length);

        float[] arr = new float[arrlen];
        for (int i = 0; i < arrlen; i++) {
            arr[i] = XBitsOptionAbstract.BIG_ENDIAN.getFloat(bytes, off);
            off += length;
        }
        bytes = null;
        return arr;
    }

    public long readLong() throws IOException {
        this.requireType0(this.readType0(), READ_TYPE_LONG);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.long_byte_length);
        return XBitsOptionAbstract.BIG_ENDIAN.getLong(this.readCache, 0);
    }

    public long[] readLongArray() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (long[]) this.readNull();
        }
        this.requireType0(type, READ_TYPE_LONG_ARRAY);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.int_byte_length);
        int arrlen = XBitsOptionAbstract.BIG_ENDIAN.getInt(this.readCache, 0);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.long_byte_length;
        byte[] bytes = this.readFixedLength(is, arrlen * length);

        long[] arr = new long[arrlen];
        for (int i = 0; i < arrlen; i++) {
            arr[i] = XBitsOptionAbstract.BIG_ENDIAN.getLong(bytes, off);
            off += length;
        }
        bytes = null;
        return arr;
    }

    public double readDouble() throws IOException {
        this.requireType0(this.readType0(), READ_TYPE_DOUBLE);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.double_byte_length);
        return XBitsOptionAbstract.BIG_ENDIAN.getDouble(this.readCache, 0);
    }

    public double[] readDoubleArray() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (double[]) this.readNull();
        }
        this.requireType0(type, READ_TYPE_DOUBLE_ARRAY);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.int_byte_length);
        int arrlen = XBitsOptionAbstract.BIG_ENDIAN.getInt(this.readCache, 0);

        int off = 0, length = XBitsOptionAbstract.BIG_ENDIAN.double_byte_length;
        byte[] bytes = this.readFixedLength(is, arrlen * length);

        double[] arr = new double[arrlen];
        for (int i = 0; i < arrlen; i++) {
            arr[i] = XBitsOptionAbstract.BIG_ENDIAN.getDouble(bytes, off);
            off += length;
        }
        bytes = null;
        return arr;
    }

    public byte readByte() throws IOException {
        this.requireType0(this.readType0(), READ_TYPE_BYTE);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, 1);
        return this.readCache[0];
    }

    public byte[] readByteArray() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (byte[]) this.readNull();
        }
        this.requireType0(type, READ_TYPE_BYTE_ARRAY);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.int_byte_length);
        int arrlen = XBitsOptionAbstract.BIG_ENDIAN.getInt(this.readCache, 0);

        int length = 1;
        byte[] bytes = this.readFixedLength(is, arrlen * length);

        return bytes;
    }

    public String readChars() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (String) this.readNull();
        }
        this.requireType0(type, READ_TYPE_CHARS);
        this.isLastReadType = false;

        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.int_byte_length);
        int charlen = XBitsOptionAbstract.BIG_ENDIAN.getInt(this.readCache, 0);

        char[] chars = new char[charlen];
        byte[] readData = this.readFixedLength(is, XByteEncode.UnicodeOption.BIG_ENDIAN.charsLenToBytesLen(charlen));
        XByteEncode.UnicodeOption.BIG_ENDIAN.putBytesToChars(readData, 0, chars, 0, charlen);
        readData = null;
        String result = new String(chars);
        chars = null;
        return result;
    }

    public String[] readCharsArray() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (String[]) this.readNull();
        }
        this.requireType0(type, READ_TYPE_CHARS_ARRAY);
        this.isLastReadType = false;

        // 获取数组长度
        this.readToCacheFixedLength(is, XBitsOptionAbstract.BIG_ENDIAN.int_byte_length);
        int arrlen = XBitsOptionAbstract.BIG_ENDIAN.getInt(this.readCache, 0);

        String[] arr = new String[arrlen];
        for (int i = 0; i < arrlen; i++) {
            arr[i] = this.readChars();
        }
        return arr;
    }

    public Object readNull() throws IOException {
        this.requireType0(this.readType0(), READ_TYPE_NULL);
        this.isLastReadType = false;

        return null;
    }

    public Class readClass() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (Class) this.readNull();
        }
        this.requireType0(type, READ_TYPE_CLASS);
        this.isLastReadType = false;

        String className = this.readChars();
        try {
            Class forNameClass = cacheForName(this.getReadObjectInClassLoader(), className);
            return forNameClass;
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }



    private void _readCollectionData0(Collection value) throws IOException {
        int count = this.readInt();
        for (int i = 0;i < count;i++) {
            Object element;
            try {
                element = this.readObject();
            } catch (Throwable e) {
                element = null;
            }
            value.add(element);
        }
    }
    private void _readMapData0(Map value0) throws IOException {
        int count = this.readInt();
        for (int i = 0;i < count;i++) {
            Object key;
            try {
                key = this.readObject();
            } catch (Throwable e) {
                key = null;
            }
            Object value;
            try {
                value = this.readObject();
            } catch (Throwable e) {
                value = null;
            }
            value0.put(key, value);
        }
    }
    private void readAdditionalData(Object instance) throws IOException {
        if (instance instanceof Collection) {
            this._readCollectionData0((Collection)instance);
        } else if (instance instanceof Map) {
            this._readMapData0((Map)instance);
        }
    }
    /**
     * from @method readObject acces this method
     * read JavaObject
     */
    private Object readClassInstaceObject0() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return this.readNull();
        }
        this.requireType0(type, READ_TYPE_OBJECT);
        this.isLastReadType = false;

        IOException readClassException = null;

        Class cls = null;
        Object instance = null;
        try {
            cls = this.readClass();
            instance = cacheGetConstructor(cls).newInstance(XStaticFixedValue.nullObjectArray);
        } catch (Throwable e) {
            readClassException = e instanceof IOException ?(IOException)e: new IOException(e);
        }

        try {
            int arrlen = this.readInt();

            for (int i = 0;i < arrlen;i++) {
                String fname = this.readChars();
                Object fValue = null;
                try {
                    fValue = this.readObject();
                } catch (Throwable e) {
                    if (null == readClassException) {
                        readClassException = e instanceof IOException ?(IOException)e: new IOException(e);
                    }
                }
                if (null == readClassException) {
                    Field f = cacheGetField(cls, fname);
                    if (null != f) {
                        f.set(instance, fValue);
                    }
                }
            }
        } catch (Throwable e) {
            if (null == readClassException) {
                readClassException = e instanceof IOException ?(IOException)e: new IOException(e);
            }
        }

        this.readAdditionalData(instance);

        if (null != readClassException) { throw readClassException; }

        return instance;
    }




    public Object readObject() throws IOException {
        byte type = this.readType0();
        if (type == -1) {
            throw new IOException("stream read = -1");
        } else if (!(type >= 0 && type < objectReaderInterfaceCount())) {
            throw new IOException("read unknown type: " + type);
        } 

        // System.out.println(this.ReadObjectInterfaceMap[(int)type].cls);
        ObjectReader ro = this.atObjectReaderInterface((int) type);
        return ro.read(this);
    }






    public Object[] readObjectArray() throws IOException {
        byte type = this.readType0();
        if (type == READ_TYPE_NULL) {
            return (Object[]) this.readNull();
        }
        this.requireType0(type, READ_TYPE_OBJECT_ARRAY);
        this.isLastReadType = false;

        IOException readClassException = null;
        String elementClassName = this.readChars();
        Class elementClass = XStaticBaseType.Object_class;
        try {
            elementClass = cacheForName(this.getReadObjectInClassLoader(), elementClassName);
        } catch (ClassNotFoundException e) {
            readClassException = new IOException(e);
        }
        int length = this.readInt();
        Object[] array = (Object[]) Array.newInstance(elementClass, length);
        for (int i = 0; i < length; i++) {
            try {
                array[i] = this.readObject();
            } catch (Throwable e) {
                if (null == readClassException) {
                    readClassException = e instanceof IOException ?(IOException)e: new IOException(e);
                }
            }
        }
        if (null != readClassException) {
            throw readClassException;
        }
        return array;
    }





    // 防止再次读取数据类型
    boolean isLastReadType = false;
    byte lastType = -1;

    // 获取下个数据类型并缓存
    public byte nextDataType() throws IOException {
        byte type = this.readType0();
        if (type == -1) {
            return -1;// stream read = -1
        }
        if (!(type >= 0 && type < this.objectReaderInterfaceCount())) {
            throw new IOException("read unknown type: " + type);
        }
        return type;
    }

    public Class getDataTypeClass(byte type) throws IOException {
        if (type == -1) {
            throw new IOException("stream read = -1");
        }
        if (!(type >= 0 && type < this.objectReaderInterfaceCount())) {
            throw new IOException("read unknown type: " + type);
        }
        return this.atObjectReaderInterface(type).cls;
    }

    public boolean hasNextDataType() throws IOException {
        byte b = this.nextDataType();
        return b != -1;
    }

}


