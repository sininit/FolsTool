package top.fols.box.io.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XObjects;
import top.fols.box.io.interfaces.XInterfaceGetOriginStream;
import top.fols.box.io.interfaces.XInterfaceGetOriginStream;
import top.fols.box.io.interfaces.XInterfereReleaseBufferable;

public class XHexStreams {
	public static String encode2String(byte[] src) {
		return new String(encode(src));
	}
	public static byte[] encode(byte[] src) {
		return encode(src, 0, src.length);
	}
	public static byte[] encode(byte[] cbuffered, int off, int len) {
		EncoderBuffer encoder = new EncoderBuffer();
		byte[] bytes = encoder.encode(cbuffered, off, len);
		return bytes;
	}

	public static byte[] decode(String src) {
		return decode(src.getBytes());
	}
	public static byte[] decode(byte[] src) {
		return decode(src, 0, src.length);
	}
	public static byte[] decode(byte[] cbuffered, int off, int len) {
		DecoderBuffer decoder = new DecoderBuffer();
		byte[] bytes = decoder.decode(cbuffered, off, len);
		return bytes;
	}

	
	public static class EncoderBuffer {
		public static final byte[] HEX_CHAR_BYTES_LOWERCASE = new byte[]{'0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		public static final byte[] HEX_CHAR_BYTES_UPPERCASE = new byte[]{'0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

		private final byte[] HEX_CHAR;
		public byte[] encode(byte b) {
			int a;
			if (b < 0) 
				a = 256 + b;
			else 
				a = b;
			byte[] one = new byte[2];
			one[0] = HEX_CHAR[a / 16];
			one[1] = HEX_CHAR[a % 16];
			return one;
		}
		public String encode2String(byte[] src) {
			return new String(encode(src));
		}
		public byte[] encode(byte[] src) {
			return encode(src, 0, src.length);
		} 
		public byte[] encode(byte[] cbuffered, int off, int len) {
			if (len < 0 || off + len > cbuffered.length)
				throw new ArrayIndexOutOfBoundsException();
			byte[] bytes = new byte[len * 2];
			int forlength = len;
			int index = 0;
			for (int i = 0;i < forlength;i++) {
				byte b = cbuffered[i + off];
				int a;
				if (b < 0) 
					a = 256 + b;
				else 
					a = b;
				bytes[index++] = HEX_CHAR[a / 16];
				bytes[index++] = HEX_CHAR[a % 16];
			}
			return bytes;
		}


		public EncoderBuffer() {
			this(false);
		}
		public EncoderBuffer(boolean toUpperCase) {
			HEX_CHAR = toUpperCase ?HEX_CHAR_BYTES_UPPERCASE: HEX_CHAR_BYTES_LOWERCASE;
		}
	}
	
	public static class DecoderBuffer {
		public byte[] decode(String src) {
			return decode(src.getBytes());
		}
		public byte[] decode(byte[] src) {
			return decode(src, 0, src.length);
		}
		public byte[] decode(byte[] cbuffered, int off, int len) {
			if (len < 0 || off + len > cbuffered.length)
				throw new ArrayIndexOutOfBoundsException();
			byte[] b = new byte[len / 2];
			int forlength = b.length;
			int Pos;
			for (int i = 0; i < forlength; i++) {  
				Pos = off + i * 2;
				//b[i] = (byte) ((byte)(DecInputStream.hexString.indexOf(cbuffered[Pos])) << 4 | (byte)(DecInputStream.hexString.indexOf(cbuffered[Pos + 1]))); 
				if (cbuffered[Pos] >= '0' && cbuffered[Pos] <= '9')
					cbuffered[Pos] -= '0';
				else if (cbuffered[Pos] >= 'a' && cbuffered[Pos] <= 'f')
					cbuffered[Pos] -= ('a' - 10);
				else if (cbuffered[Pos] >= 'A' && cbuffered[Pos] <= 'F')
					cbuffered[Pos] -= ('A' - 10);
				else
					cbuffered[Pos] = -1;

				if (cbuffered[Pos + 1] >= '0' && cbuffered[Pos + 1] <= '9')
					cbuffered[Pos + 1] -= '0';
				else if (cbuffered[Pos + 1] >= 'a' && cbuffered[Pos + 1] <= 'f')
					cbuffered[Pos + 1] -= ('a' - 10);
				else if (cbuffered[Pos + 1] >= 'A' && cbuffered[Pos + 1] <= 'F')
					cbuffered[Pos + 1] -= ('A' - 10);
				else
					cbuffered[Pos + 1] = -1;
				b[i] = (byte)(cbuffered[Pos] << 4 | cbuffered[Pos + 1]);
			}  
			return b;
		}
	}


	
	
	
	
	
	
	
	@XAnnotations("from byte array cast to hex write OutputStream")
	public static class EncOutput<T extends OutputStream> extends OutputStream implements XInterfaceGetOriginStream<T>, XInterfereReleaseBufferable{
		private final T stream;
		private final byte[] HEX_CHAR;
		public EncOutput(T hexwriter) {
			this(hexwriter, false);
		}
		public EncOutput(T hexWrite, boolean toUpperCase) {
			this.stream = XObjects.requireNonNull(hexWrite);
			this.HEX_CHAR = HEX_CHAR = toUpperCase ?EncoderBuffer.HEX_CHAR_BYTES_UPPERCASE : EncoderBuffer.HEX_CHAR_BYTES_LOWERCASE;;
		}


		private void write0(byte b) throws IOException {
			// 一个byte为8位，可用两个十六进制位标识
			int a;
			if (b < 0) 
				a = 256 + b;
			else 
				a = b;
			stream.write(HEX_CHAR[a / 16]);
			stream.write(HEX_CHAR[a % 16]);
		}
		private byte[] cbuffered = new byte[0];
		private void write0(byte[] bytes, int off, int len) throws IOException {
			if (cbuffered.length != len * 2)
				cbuffered = new byte[len * 2];
			int index = 0;
			for (int i = 0;i < len;i++) {
				byte b = bytes[i + off];
				int a;
				if (b < 0) 
					a = 256 + b;
				else 
					a = b;
				cbuffered[index++] = HEX_CHAR[a / 16];
				cbuffered[index++] = HEX_CHAR[a % 16];
			}
			stream.write(cbuffered);
		}
		@XAnnotations("write 2 char    = 2byte")
		@Override
		public void write(int p1) throws java.io.IOException {
			write0((byte)p1);
		}
		@Override
		public void write(byte[] b) throws java.io.IOException {
			write0(b, 0, b.length);
		}
		@XAnnotations("write len*2 char    =len*2byte")
		@Override
		public void write(byte[] b, int off, int len) throws java.io.IOException {
			write0(b, off, len);
		}
		@Override
		public void flush() throws java.io.IOException {
			stream.flush();
		}
		@Override
		public void close() throws java.io.IOException {
			stream.close();
		}

		@Override
		public void releaseBuffer() {
			// TODO: Implement this method
			cbuffered = XStaticFixedValue.nullbyteArray;
		}


		
		@Override
		public T getStream() {
			return stream;
		}
	}



	@XAnnotations("from InputStream read hex cast to byte array")
	public static class DecInput<T extends InputStream> extends InputStream implements XInterfaceGetOriginStream<T>,XInterfereReleaseBufferable{
		//private static final String hexString = "0123456789abcdef";
		private final T stream;
		public DecInput(T hexReader) {
			this.stream = XObjects.requireNonNull(hexReader);
		}
		private byte[] oneHex = new byte[2];
		@XAnnotations("read 2 byte")
		@Override
		public int read() throws IOException {
			int read = stream.read(oneHex);
			if (read != 2)
				return -1;
			if (oneHex[0] >= '0' && oneHex[0] <= '9')
				oneHex[0] -= '0';
			else if (oneHex[0] >= 'a' && oneHex[0] <= 'f')
				oneHex[0] -= ('a' - 10);
			else if (oneHex[0] >= 'A' && oneHex[0] <= 'F')
				oneHex[0] -= ('A' - 10);
			else
				oneHex[0] = -1;

			if (oneHex[1] >= '0' && oneHex[1] <= '9')
				oneHex[1] -= '0';
			else if (oneHex[1] >= 'a' && oneHex[1] <= 'f')
				oneHex[1] -= ('a' - 10);
			else if (oneHex[1] >= 'A' && oneHex[1] <= 'F')
				oneHex[1] -= ('A' - 10);
			else
				oneHex[1] = -1;
			return (byte)(oneHex[0] << 4 | oneHex[1]);

			//return (byte)(hexString.indexOf(oneHex[0])) << 4 | (byte)(hexString.indexOf(oneHex[1]));
		}
		@Override
		public int read(byte[] b) throws java.io.IOException {
			return read(b, 0, b.length);
		} 
		private byte[] cbuffered = new byte[0];
		@XAnnotations("read len*2 byte")
		@Override
		public int read(byte[] b, int off, int len) throws java.io.IOException {
			if (cbuffered.length != len * 2)
				cbuffered = new byte[len * 2];
			final int read = stream.read(cbuffered);
			if (read == -1)
				return -1;
			int forlength = read / 2;
			int index = off;
			for (int i = 0; i < forlength; i++) {  
				int Pos = i * 2;
				//b[index++] = (byte) ((byte)(hexString.indexOf(cbuffered[Pos])) << 4 | (byte)(hexString.indexOf(cbuffered[Pos + 1])));  
				if (cbuffered[Pos] >= '0' && cbuffered[Pos] <= '9')
					cbuffered[Pos] -= '0';
				else if (cbuffered[Pos] >= 'a' && cbuffered[Pos] <= 'f')
					cbuffered[Pos] -= ('a' - 10);
				else if (cbuffered[Pos] >= 'A' && cbuffered[Pos] <= 'F')
					cbuffered[Pos] -= ('A' - 10);
				else
					cbuffered[Pos] = -1;

				if (cbuffered[Pos + 1] >= '0' && cbuffered[Pos + 1] <= '9')
					cbuffered[Pos + 1] -= '0';
				else if (cbuffered[Pos + 1] >= 'a' && cbuffered[Pos + 1] <= 'f')
					cbuffered[Pos + 1] -= ('a' - 10);
				else if (cbuffered[Pos + 1] >= 'A' && cbuffered[Pos + 1] <= 'F')
					cbuffered[Pos + 1] -= ('A' - 10);
				else
					cbuffered[Pos + 1] = -1;
				b[index++] = (byte)(cbuffered[Pos] << 4 | cbuffered[Pos + 1]);
			}  
			return forlength;
		}
		@XAnnotations("skip n*2")
		@Override
		public long skip(long n) throws java.io.IOException {
			long skip =  stream.skip(n * 2);//一个 hex 长度为2字节
			skip = skip > 0 ?skip / 2: 0;
			return skip;
		}
		@Override
		public void close() throws java.io.IOException {
			stream.close();
			stream.close();
		}
		@XAnnotations("get available/2")
		@Override
		public int available() throws IOException {
			int available = stream.available();
			available = available > 0 ?available / 2: 0;
			return available;
		}
		@XAnnotations("mark readlimit*2")
		@Override
		public synchronized void mark(int readlimit) {
			stream.mark(readlimit * 2);
		}
		@Override
		public synchronized void reset() throws java.io.IOException {
			stream.reset();
		}
		@Override
		public boolean markSupported() {
			return stream.markSupported();
		}
		
		@Override
		public void releaseBuffer() {
			// TODO: Implement this method
			cbuffered = XStaticFixedValue.nullbyteArray;
		}
		
		@Override
		public T getStream() {
			return stream;
		}
	}









}
