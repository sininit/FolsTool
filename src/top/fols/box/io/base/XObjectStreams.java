package top.fols.box.io.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.interfaces.XInterfaceGetOriginStream;
import top.fols.box.lang.XBits;
import top.fols.box.util.XObjects;

/*
 * 简单的序列化工具  simple serialization
 * 单线程不安全 single thread is not safe
 * 没有缓冲 no buffer you can use java.io.BufferInputStream and java.io.BufferOutputStream
 */
public final class XObjectStreams {
	
	/*
	 * null
	 * String
	 * byte
	 * long
	 * double
	 * char
	 * int
	 * boolean
	 * float
	 * short
	 */
	public final static enum Type {
		NULL(Type.VALUE_NULL),

		STRING(Type.VALUE_STRING),
		BYTE(Type.VALUE_BYTE),
		LONG(Type.VALUE_LONG),
		DOUBLE(Type.VALUE_DOUBLE),
		CHAR(Type.VALUE_CHAR),
		INT(Type.VALUE_INT),
		BOOLEAN(Type.VALUE_BOOLEAN),
		FLOAT(Type.VALUE_FLOAT),
		SHORT(Type.VALUE_SHORT),

		STRINGARRAY(Type.VALUE_STRINGARRAY),
		BYTEARRAY(Type.VALUE_BYTEARRAY),
		LONGARRAY(Type.VALUE_LONGARRAY),
		DOUBLEARRAY(Type.VALUE_DOUBLEARRAY),
		CHARARRAY(Type.VALUE_CHARARRAY),
		INTARRAY(Type.VALUE_INTARRAY),
		BOOLEANARRAY(Type.VALUE_BOOLEANARRAY),
		FLOATARRAY(Type.VALUE_FLOATARRAY),
		SHORTARRAY(Type.VALUE_SHORTARRAY);

		private byte b;
		private Type(byte v){
			this.b = v;
		}
		public byte value(){
			return this.b;
		}


		private static final Type[] valueArr = new Type[]{
			Type.NULL,//0

			Type.STRING,//1
			Type.BYTE,//2
			Type.LONG,//3
			Type.DOUBLE,//4
			Type.CHAR,//5
			Type.INT,//6
			Type.BOOLEAN,//7
			Type.FLOAT,//8
			Type.SHORT,//9

			null,//10
			null,//11
			null,//12
			null,//13
			null,//14
			null,//15
			null,//16

			Type.STRINGARRAY,//17
			Type.BYTEARRAY,//18
			Type.LONGARRAY,//19
			Type.DOUBLEARRAY,//20
			Type.CHARARRAY,//21
			Type.INTARRAY,//22
			Type.BOOLEANARRAY,//23
			Type.FLOATARRAY,//24
			Type.SHORTARRAY//25




		};
		public static boolean isData(byte b){
			return b >= 0 && b < valueArr.length && valueArr[b] != null;
		}
		public static boolean isBaseData(byte b){
			return (b == Type.VALUE_NULL) ||
				(b >= Type.VALUE_STRING && b <= Type.VALUE_SHORT);
		}
		public static boolean isArrayData(byte b){
			return (b >= Type.VALUE_STRINGARRAY) &&
				(b <= Type.VALUE_SHORTARRAY);
		}
		public static boolean isNull(byte b){
			return b == Type.VALUE_NULL;
		}
		public static boolean isObject(byte b){
			return (b == Type.VALUE_NULL) || 
				(b == Type.VALUE_STRING) || 
				(b >= Type.VALUE_STRINGARRAY && b <= Type.VALUE_SHORTARRAY);
		}


		public static Type forValue(byte b){
			if (b >= 0 && b < valueArr.length && valueArr[b] != null){
				return valueArr[b];
			}
			throw new RuntimeException("unknown data type: " + b);
		}


		// 不要添加负数字段 不要添加超过127的值的字段
		// do not add negative fields
		public static final byte VALUE_NULL = (byte)0;

		public static final byte VALUE_STRING = (byte)1;

		public static final byte VALUE_BYTE = (byte)2;
		public static final byte VALUE_LONG = (byte)3;
		public static final byte VALUE_DOUBLE = (byte)4;
		public static final byte VALUE_CHAR = (byte)5;
		public static final byte VALUE_INT = (byte)6;
		public static final byte VALUE_BOOLEAN = (byte)7;
		public static final byte VALUE_FLOAT = (byte)8;
		public static final byte VALUE_SHORT = (byte)9; 

		public static final byte VALUE_STRINGARRAY = (byte)17;

		public static final byte VALUE_BYTEARRAY = (byte)18;
		public static final byte VALUE_LONGARRAY = (byte)19;
		public static final byte VALUE_DOUBLEARRAY = (byte)20;
		public static final byte VALUE_CHARARRAY = (byte)21;
		public static final byte VALUE_INTARRAY = (byte)22;
		public static final byte VALUE_BOOLEANARRAY = (byte)23;
		public static final byte VALUE_FLOATARRAY = (byte)24;
		public static final byte VALUE_SHORTARRAY = (byte)25;
	}





	private static final byte TRUE = 1;
	private static final byte FALSE = 0;
	
	
	
	
	
	
	
	
	public static final class Input<S extends InputStream> implements XInterfaceGetOriginStream<S> {
		private S is;
		private Charset Unicode = Charset.forName("Unicode");
		@Override
		public S getStream() {
			// TODO: Implement this method
			return this.is;
		}
		public Input(S inp) {
			this.is = XObjects.requireNonNull(inp);
		}
		
		
		
		
		protected static Type requireNextType(InputStream iss, Type need) throws IOException {
			int read = iss.read();
			if (read == -1) {
				throw new IOException("null data");
			} else {
				byte b = (byte)read;
				if (b != need.value()) {
					Type type = Type.forValue(b);
					throw new IOException("cannot cast data type " + type.name() + " cast to " + need.name());
				} else {
					return need;
				}
			}
		}
		protected static Type requireType(Type iss, Type need) throws IOException {
			if (iss != need) {
				if (iss == null) {
					throw new RuntimeException("cannot get data type");
				}
				throw new IOException("cannot cast data type " + iss.name() + " cast to " + need.name());
			} else {
				return need;
			}
		}
		
		
		
		public byte readByte() throws IOException {
			return readByteForce(requireNextType(this.is, Type.BYTE));
		}
		public boolean readBoolean() throws IOException {
			return readBooleanForce(requireNextType(this.is, Type.BOOLEAN));
		}
		public char readChar() throws IOException {
			return readCharForce(requireNextType(this.is, Type.CHAR));
		}
		public short readShort() throws IOException {
			return readShortForce(requireNextType(this.is, Type.SHORT));
		}
		public int readInt() throws IOException {
			return readIntForce(requireNextType(this.is, Type.INT));
		}
		public float readFloat() throws IOException {
			return readFloatForce(requireNextType(this.is, Type.FLOAT));
		}
		public long readLong() throws IOException {
			return readLongForce(requireNextType(this.is, Type.LONG));
		}
		public double readDouble() throws IOException {
			return readDoubleForce(requireNextType(this.is, Type.DOUBLE));
		}
		public String readString() throws IOException {
			return readStringForce(requireNextType(this.is, Type.STRING));
		}
		public Object readNull() throws IOException {
			return readNullForce(requireNextType(this.is, Type.NULL));
		}



		/*
		 * 不检查类型强制读取数据转换
		 */
		private byte[] READ_BYTE_BUF = new byte[1];
		public byte readByteForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.BYTE);
			int read = is.read(READ_BYTE_BUF);
			if (read == -1)
				throw new IOException("unable to get data");
			return READ_BYTE_BUF[0];
		}
		private byte[] READ_BOOLEAN_BUF = new byte[XBits.boolean_byte_length];
		public boolean readBooleanForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.BOOLEAN);
			int read = is.read(READ_BOOLEAN_BUF);
			if (read != READ_BOOLEAN_BUF.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, READ_BOOLEAN_BUF.length));
			return READ_BOOLEAN_BUF[0] == TRUE;
		}
		private byte[] READ_CHAR_BUF = new byte[XBits.char_byte_length];
		public char readCharForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.CHAR);
			int read = is.read(READ_CHAR_BUF);
			if (read != READ_CHAR_BUF.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, READ_CHAR_BUF.length));
			return XBits.getChar(READ_CHAR_BUF, 0);
		}

		private byte[] READ_SHORT_BUF = new byte[XBits.short_byte_length];
		public short readShortForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.SHORT);
			int read = is.read(READ_SHORT_BUF);
			if (read != READ_SHORT_BUF.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, READ_SHORT_BUF.length));
			return XBits.getShort(READ_SHORT_BUF, 0);
		}
		private byte[] READ_INT_BUF = new byte[XBits.int_byte_length];
		public int readIntForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.INT);
			int read = is.read(READ_INT_BUF);
			if (read != READ_INT_BUF.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, READ_INT_BUF.length));
			return XBits.getInt(READ_INT_BUF, 0);
		}
		private byte[] READ_FLOAT_BUF = new byte[XBits.float_byte_length];
		public float readFloatForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.FLOAT);
			int read = is.read(READ_FLOAT_BUF);
			if (read != READ_FLOAT_BUF.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, READ_FLOAT_BUF.length));
			return Float.intBitsToFloat(XBits.getInt(READ_FLOAT_BUF, 0));
		}
		private byte[] READ_LONG_BUF = new byte[XBits.long_byte_length];
		public long readLongForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.LONG);
			int read = is.read(READ_LONG_BUF);
			if (read != READ_LONG_BUF.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, READ_LONG_BUF.length));
			return XBits.getLong(READ_LONG_BUF, 0);
		}
		private byte[] READ_DOUBLE_BUF = new byte[XBits.double_byte_length];
		public double readDoubleForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.DOUBLE);
			int read = is.read(READ_DOUBLE_BUF);
			if (read != READ_DOUBLE_BUF.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, READ_DOUBLE_BUF.length));
			return Double.longBitsToDouble(XBits.getLong(READ_DOUBLE_BUF, 0));
		}
		public String readStringForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.STRING);
			return readStringDirect0();
		}
		/*
		 * 直接 读取长度和数据 创建字符串
		 */
		private final String readStringDirect0() throws IOException {
			int length = readInt();// char length
			if (length >= 0) {
				byte[] bs = new byte[length];
				int read = is.read(bs);
				if (read != bs.length)
					throw new IOException(String.format("incomplete data, length=%s, need=%s", read, bs.length));
				String str = new String(bs, Unicode);
				bs = null;
				return str;
			} else {
				return null;
			}
		}
		public Object readNullForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.NULL);
			return null;
		}









		public byte[] readByteArray() throws IOException {
			return readByteArrayForce(requireNextType(this.is, Type.BYTEARRAY));
		}
		public long[] readLongArray() throws IOException {
			return readLongArrayForce(requireNextType(this.is, Type.LONGARRAY));
		}
		public double[] readDoubleArray() throws IOException {
			return readDoubleArrayForce(requireNextType(this.is, Type.LONGARRAY));
		}
		public char[] readCharArray() throws IOException {
			return readCharArrayForce(requireNextType(this.is, Type.CHARARRAY));
		}
		public int[] readIntArray() throws IOException {
			return readIntArrayForce(requireNextType(this.is, Type.INTARRAY));
		}
		public boolean[] readBooleanArray() throws IOException {
			return readBooleanArrayForce(requireNextType(this.is, Type.BOOLEANARRAY));
		}
		public float[] readFloatArray() throws IOException {
			return readFloatArrayForce(requireNextType(this.is, Type.FLOATARRAY));
		}
		public short[] readShortArray() throws IOException {
			return readShortArrayForce(requireNextType(this.is, Type.SHORTARRAY));
		}
		public String[] readStringArray() throws IOException {
			return readStringArrayForce(requireNextType(this.is, Type.STRINGARRAY));
		}




		public byte[] readByteArrayForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.BYTEARRAY);
			int length = this.readInt();
			if (length == -1) {
				return null;
			}
			byte[] bs = new byte[length];
			int read = this.is.read(bs);
			if (read != bs.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, bs.length));
			return bs;
		}
		public long[] readLongArrayForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.LONGARRAY);
			int length = this.readInt();
			if (length == -1) {
				return null;
			}
			byte[] bs = new byte[XBits.long_byte_length * length];
			int read = this.is.read(bs);
			if (read != bs.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, bs.length));
			long[] arrs = new long[length];
			for (int i = 0;i < length;i++) {
				arrs[i] = XBits.getLong(bs, i * XBits.long_byte_length);
			}
			bs = null;
			return arrs;
		}
		public double[] readDoubleArrayForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.DOUBLEARRAY);
			int length = this.readInt();
			if (length == -1) {
				return null;
			}
			byte[] bs = new byte[XBits.double_byte_length * length];
			int read = this.is.read(bs);
			if (read != bs.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, bs.length));
			double[] arrs = new double[length];
			for (int i = 0;i < length;i++) {
				arrs[i] = XBits.getDouble(bs, i * XBits.long_byte_length);
			}
			bs = null;
			return arrs;
		}
		public char[] readCharArrayForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.CHARARRAY);
			int length = this.readInt();
			if (length == -1) {
				return null;
			}
			byte[] bs = new byte[XBits.char_byte_length * length];
			int read = this.is.read(bs);
			if (read != bs.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, bs.length));
			char[] arrs = new char[length];
			for (int i = 0;i < length;i++) {
				arrs[i] = XBits.getChar(bs, i * XBits.char_byte_length);
			}
			bs = null;
			return arrs;
		}
		public int[] readIntArrayForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.INTARRAY);
			int length = this.readInt();
			if (length == -1) {
				return null;
			}
			byte[] bs = new byte[XBits.int_byte_length * length];
			int read = this.is.read(bs);
			if (read != bs.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, bs.length));
			int[] arrs = new int[length];
			for (int i = 0;i < length;i++) {
				arrs[i] = XBits.getInt(bs, i * XBits.int_byte_length);
			}
			bs = null;
			return arrs;
		}
		public boolean[] readBooleanArrayForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.BOOLEANARRAY);
			int length = this.readInt();
			if (length == -1) {
				return null;
			}
			byte[] bs = new byte[length];
			int read = this.is.read(bs);
			if (read != bs.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, bs.length));
			boolean[] arrs = new boolean[length];
			for (int i = 0;i < length;i++) {
				arrs[i] = bs[i] == TRUE;
			}
			bs = null;
			return arrs;
		}
		public float[] readFloatArrayForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.FLOATARRAY);
			int length = this.readInt();
			if (length == -1) {
				return null;
			}
			byte[] bs = new byte[XBits.float_byte_length * length];
			int read = this.is.read(bs);
			if (read != bs.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, bs.length));
			float[] arrs = new float[length];
			for (int i = 0;i < length;i++) {
				arrs[i] = XBits.getFloat(bs, i * XBits.float_byte_length);
			}
			bs = null;
			return arrs;
		}
		public short[] readShortArrayForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.SHORTARRAY);
			int length = this.readInt();
			if (length == -1) {
				return null;
			}
			byte[] bs = new byte[XBits.short_byte_length * length];
			int read = this.is.read(bs);
			if (read != bs.length)
				throw new IOException(String.format("incomplete data, length=%s, need=%s", read, bs.length));
			short[] arrs = new short[XBits.short_byte_length * length];
			for (int i = 0;i < length;i++) {
				arrs[i] = XBits.getShort(bs, i * XBits.short_byte_length);
			}
			bs = null;
			return arrs;
		}
		public String[] readStringArrayForce(Type typeCheck) throws IOException {
			requireType(typeCheck, Type.STRINGARRAY);
			int length = this.readInt();
			if (length == -1) {
				return null;
			}
			String[] arrs = new String[length];
			for (int i = 0;i < length;i++) {
				arrs[i] = readStringDirect0();
			}
			return arrs;
		}




		public Object readObject() throws IOException {
			return readObjectForce(nextDataType());
		}
		public Object readObjectForce(Type forType) throws IOException {
			/*
			 * 由于Type不可以被外部类创建 我们只需要判断它是否为空即可
			 */
			if (forType == null) {
				throw new IOException("null data type: " + forType);
			} else {
				if (Type.isArrayData(forType.value())) {
					switch (forType.value()) {
						case Type.VALUE_BYTEARRAY:{ return readByteArrayForce(forType); }
						case Type.VALUE_LONGARRAY:{ return readLongArrayForce(forType); }
						case Type.VALUE_DOUBLEARRAY:{ return readDoubleArrayForce(forType); }
						case Type.VALUE_CHARARRAY:{ return readCharArrayForce(forType); }
						case Type.VALUE_INTARRAY:{ return readIntArrayForce(forType); }
						case Type.VALUE_BOOLEANARRAY:{ return readBooleanArrayForce(forType); }
						case Type.VALUE_FLOATARRAY:{ return readFloatArrayForce(forType); }
						case Type.VALUE_SHORTARRAY:{ return readShortArrayForce(forType); }
						case Type.VALUE_STRINGARRAY:{ return readStringArrayForce(forType); }
					}
				} else {
					switch (forType.value()) {
						case Type.VALUE_BYTE:{ return readByteForce(forType); }
						case Type.VALUE_LONG:{ return readLongForce(forType); }
						case Type.VALUE_DOUBLE:{ return readDoubleForce(forType); }
						case Type.VALUE_CHAR:{ return readCharForce(forType); }
						case Type.VALUE_INT:{ return readIntForce(forType); }
						case Type.VALUE_BOOLEAN:{ return readBooleanForce(forType); }
						case Type.VALUE_FLOAT:{ return readFloatForce(forType); }
						case Type.VALUE_SHORT:{ return readShortForce(forType); }
						case Type.VALUE_STRING:{ return readStringForce(forType); }

						case Type.VALUE_NULL:{ return readNullForce(forType); }
					}
				}
				throw new IOException("unknown exception, cannot read data type: " + forType);
			}
		}




		/*
		 * 使用了该方法后必须使用Force方法读取数据, 才能继续读取下一个数据, 否则将会出现严重错误
		 */
		@XAnnotations("after using this method, you must use the Force method to read the data before you can continue reading the next data, otherwise a serious error will occur.")
		public Type nextDataType() throws IOException {
			int read = is.read();
			if (read == -1) {
				throw new IOException("null data");
			} 
			return Type.forValue((byte)read);
		}
	}
	
	
	
	
	
	
	
	public static final class Output<S extends OutputStream> implements XInterfaceGetOriginStream<S> {
		@Override
		public S getStream() {
			// TODO: Implement this method
			return this.os;
		}
		private S os;
		public Output(S os) {
			this.os = os;
		}



		public void writeByte(byte b) throws IOException {
			this.os.write(Type.BYTE.value());
			this.os.write(b);
		}
		private final byte[] WRITE_LONG_BUF = new byte[XBits.long_byte_length];
		public void writeLong(long l) throws IOException {
			this.os.write(Type.LONG.value());
			XBits.putLong(WRITE_LONG_BUF, 0,   l);
			this.os.write(WRITE_LONG_BUF);
		}
		private final byte[] WRITE_DOUBLE_BUF = new byte[XBits.double_byte_length];
		public void writeDouble(double d) throws IOException {
			this.os.write(Type.DOUBLE.value());
			XBits.putDouble(WRITE_DOUBLE_BUF, 0,   d);
			this.os.write(WRITE_DOUBLE_BUF);
		}
		private final byte[] WRITE_CHAR_BUF = new byte[XBits.char_byte_length];
		public void writeChar(char c) throws IOException {
			this.os.write(Type.CHAR.value());
			XBits.putChar(WRITE_CHAR_BUF, 0,   c);
			this.os.write(WRITE_CHAR_BUF);
		}
		private final byte[] WRITE_INT_BUF = new byte[XBits.int_byte_length];
		public void writeInt(int i) throws IOException {
			this.os.write(Type.INT.value());
			XBits.putInt(WRITE_INT_BUF, 0,   i);
			this.os.write(WRITE_INT_BUF);
		}
		public final void writeBoolean(boolean b) throws IOException {
			this.os.write(Type.BOOLEAN.value());
			this.os.write(b ?TRUE: FALSE);//非0即真
		}
		private final byte[] WRITE_FLOAT_BUF = new byte[XBits.float_byte_length];
		public void writeFloat(float f) throws IOException {
			this.os.write(Type.FLOAT.value());
			XBits.putFloat(WRITE_FLOAT_BUF, 0,   f);
			this.os.write(WRITE_FLOAT_BUF);
		}
		private final byte[] WRITE_SHORT_BUF = new byte[XBits.short_byte_length];
		public void writeShort(short s) throws IOException {
			this.os.write(Type.SHORT.value());
			XBits.putShort(WRITE_SHORT_BUF, 0,   s);
			this.os.write(WRITE_SHORT_BUF);
		}
		public void writeString(String str) throws IOException {
			this.os.write(Type.STRING.value());
			this.writeStringDirect0(str);
		}
		/*
		 * 直接 写入字符串 长度和数据
		 * char 即 Unicode
		 */
		private final void writeStringDirect0(String str) throws IOException {
			if (str != null) {
				int length = str.length();// char length
				byte[] unicodeBytes = new byte[XBits.char_byte_length * length];
				for (int i = 0;i < length;i++) {
					XBits.putChar(unicodeBytes, i * XBits.char_byte_length, str.charAt(i));
				}
				this.writeInt(unicodeBytes.length);
				this.os.write(unicodeBytes);
				unicodeBytes = null;
			} else {
				this.writeInt(-1);
			}
		}
		public void writeNull() throws IOException {
			this.os.write(Type.NULL.value());
		}






		public void writeByteArray(byte[] arr) throws IOException {
			if (arr == null) writeByteArray(null, -1, -1);
			else writeByteArray(arr, 0, arr.length);
		}
		public void writeByteArray(byte[] arr, int off, int len) throws IOException {
			this.os.write(Type.BYTEARRAY.value());
			if (arr == null) {
				this.writeInt(-1);
			} else {
				this.writeInt(len);//数组长度
				this.os.write(arr, off, len);
				arr = null;
			}
		}
		public void writeLongArray(long[] arr) throws IOException {
			if (arr == null) writeLongArray(null, -1, -1);
			else writeLongArray(arr, 0, arr.length);
		}
		public void writeLongArray(long[] arr, int off, int len) throws IOException {
			this.os.write(Type.LONGARRAY.value());
			if (arr == null) {
				this.writeInt(-1);
			} else {
				this.writeInt(len);//数组长度
				byte[] bs = new byte[XBits.long_byte_length * len];
				for (int i = 0;i < len;i++) {
					XBits.putLong(bs, i * XBits.long_byte_length, arr[i + off]);
				}
				this.os.write(bs);
				bs = null;
			}
		}
		public void writeDoubleArray(double[] arr) throws IOException {
			if (arr == null) writeDoubleArray(null, -1, -1);
			else writeDoubleArray(arr, 0, arr.length);
		}
		public void writeDoubleArray(double[] arr, int off, int len) throws IOException {
			this.os.write(Type.DOUBLEARRAY.value());
			if (arr == null) {
				this.writeInt(-1);
			} else {
				this.writeInt(len);//数组长度
				byte[] bs = new byte[XBits.double_byte_length * len];
				for (int i = 0;i < len;i++) {
					XBits.putDouble(bs, i * XBits.double_byte_length, arr[i + off]);
				}
				this.os.write(bs);
				bs = null;
			}
		}
		public void writeCharArray(char[] arr) throws IOException {
			if (arr == null) writeCharArray(null, -1, -1);
			else writeCharArray(arr, 0, arr.length);
		}
		public void writeCharArray(char[] arr, int off, int len) throws IOException {
			this.os.write(Type.CHARARRAY.value());
			if (arr == null) {
				this.writeInt(-1);
			} else {
				this.writeInt(len);//数组长度
				byte[] bs = new byte[XBits.char_byte_length * len];
				for (int i = 0;i < len;i++) {
					XBits.putChar(bs, i * XBits.char_byte_length, arr[i + off]);
				}
				this.os.write(bs);
				bs = null;
			}
		}
		public void writeIntArray(int[] arr) throws IOException {
			if (arr == null) writeIntArray(null, -1, -1);
			else writeIntArray(arr, 0, arr.length);
		}
		public void writeIntArray(int[] arr, int off, int len) throws IOException {
			this.os.write(Type.INTARRAY.value());
			if (arr == null) {
				this.writeInt(-1);
			} else {
				this.writeInt(len);//数组长度
				byte[] bs = new byte[XBits.int_byte_length * len];
				for (int i = 0;i < len;i++) {
					XBits.putInt(bs, i * XBits.int_byte_length, arr[i + off]);
				}
				this.os.write(bs);
				bs = null;
			}
		}
		public void writeBooleanArray(boolean[] arr) throws IOException {
			if (arr == null) writeBooleanArray(null, -1, -1);
			else writeBooleanArray(arr, 0, arr.length);
		}
		public void writeBooleanArray(boolean[] arr, int off, int len) throws IOException {
			this.os.write(Type.BOOLEANARRAY.value());
			if (arr == null) {
				this.writeInt(-1);
			} else {
				this.writeInt(len);//数组长度
				byte[] bs = new byte[len];
				for (int i = 0;i < len;i++) {
					bs[i] = arr[i + off] ?TRUE: FALSE;
				}
				this.os.write(bs);
				bs = null;
			}
		}
		public void writeFloatArray(float[] arr) throws IOException {
			if (arr == null) writeFloatArray(null, -1, -1);
			else writeFloatArray(arr, 0, arr.length);
		}
		public void writeFloatArray(float[] arr, int off, int len) throws IOException {
			this.os.write(Type.FLOATARRAY.value());
			if (arr == null) {
				this.writeInt(-1);
			} else {
				this.writeInt(len);//数组长度
				byte[] bs = new byte[XBits.float_byte_length * len];
				for (int i = 0;i < len;i++) {
					XBits.putFloat(bs, i * XBits.float_byte_length, arr[i + off]);
				}
				this.os.write(bs);
				bs = null;
			}
		}
		public void writeShortArray(short[] arr) throws IOException {
			if (arr == null)  writeShortArray(null, -1, -1);
			else writeShortArray(arr, 0, arr.length);
		}
		public void writeShortArray(short[] arr, int off, int len) throws IOException {
			this.os.write(Type.SHORTARRAY.value());
			if (arr == null) {
				this.writeInt(-1);
			} else {
				this.writeInt(len);//数组长度
				byte[] bs = new byte[XBits.short_byte_length * len];
				for (int i = 0;i < len;i++) {
					XBits.putShort(bs, i * XBits.short_byte_length, arr[i + off]);
				}
				this.os.write(bs);
				bs = null;
			}
		}
		public void writeStringArray(String[] arr) throws IOException {
			if (arr == null) writeStringArray(null, -1, -1);
			else writeStringArray(arr, 0, arr.length);
		}
		public void writeStringArray(String[] arr, int off, int len) throws IOException {
			this.os.write(Type.STRINGARRAY.value());
			if (arr == null) {
				this.writeInt(-1);
			} else {
				this.writeInt(len);//数组长度
				for (int i = 0;i < len;i++) {
					this.writeStringDirect0(arr[i + off]);
				}
			}
		}






		public void flush() throws IOException {
			this.os.flush();
		}
	}

}
