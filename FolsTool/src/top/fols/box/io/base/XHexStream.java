package top.fols.box.io.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XObjects;
public class XHexStream
{
	public static class EncoderBuffer
	{
		private static final byte[] HEX_CHAR = {'0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		private final byte[] one = new byte[2];
		public byte[] encode(byte b)
		{
			int a = 0;
			if (b < 0) 
				a = 256 + b;
			else 
				a = b;
			one[0] = HEX_CHAR[a / 16];
			one[1] = HEX_CHAR[a % 16];
			return bytes;
		}
		public String encode2String(byte[] src)
		{
			return new String(encode(src));
		}
		public byte[] encode(byte[] src)
		{
			return encode(src, 0, src.length);
		}
		private byte[] bytes = XStaticFixedValue.nullbyteArray;;
		public byte[] encode(byte[] cbuffered, int off, int len)
		{
			if (len < 1 || off + len > cbuffered.length)
				return XStaticFixedValue.nullbyteArray;
			if (bytes.length != len * 2)
				bytes = new byte[len * 2];
			int forlength = len;
			int index = 0;
			for (int i = 0;i < forlength;i++)
			{
				byte b = cbuffered[i + off];
				int a = 0;
				if (b < 0) 
					a = 256 + b;
				else 
					a = b;
				bytes[index++] = HEX_CHAR[a / 16];
				bytes[index++] = HEX_CHAR[a % 16];
			}
			return bytes;
		}
		public void clearBuffer()
		{
			bytes = XStaticFixedValue.nullbyteArray;
		}
	}
	public static class DecoderBuffer
	{
		public byte[] decode(String src)
		{
			return decode(src.getBytes());
		}
		public byte[] decode(byte[] src)
		{
			return decode(src, 0, src.length);
		}
		private byte[] b = XStaticFixedValue.nullbyteArray;
		public byte[] decode(byte[] cbuffered, int off, int len) 
		{
			if (len < 1 || off + len > cbuffered.length)
				return XStaticFixedValue.nullbyteArray;
			if (b.length != len / 2)
				b = new byte[len / 2];
			int forlength = b.length;
			int Pos;
			for (int i = 0; i < forlength; i++)
			{  
				Pos = off + i * 2;
				//b[i] = (byte) ((byte)(DecInputStream.hexString.indexOf(cbuffered[Pos])) << 4 | (byte)(DecInputStream.hexString.indexOf(cbuffered[Pos + 1]))); 
				if (cbuffered[Pos] >= 48 && cbuffered[Pos] <= 57)
					cbuffered[Pos] -= 48;
				else if (cbuffered[Pos] >= 97 && cbuffered[Pos] <= 102)
					cbuffered[Pos] -= 87;
				else
					cbuffered[Pos] = -1;

				if (cbuffered[Pos + 1] >= 48 && cbuffered[Pos + 1] <= 57)
					cbuffered[Pos + 1] -= 48;
				else if (cbuffered[Pos + 1] >= 97 && cbuffered[Pos + 1] <= 102)
					cbuffered[Pos + 1] -= 87;
				else
					cbuffered[Pos + 1] = -1;
				b[i] = (byte)(cbuffered[Pos] << 4 | cbuffered[Pos + 1]);
			}  
			return b;
		}
		public void clearBuffer()
		{
			b = XStaticFixedValue.nullbyteArray;
		}
	}

	private static EncoderBuffer encoder;
	public static String encode2String(byte[] src)
	{
		return new String(encode(src));
	}
	public static byte[] encode(byte[] src)
	{
		return encode(src, 0, src.length);
	}
	public static synchronized byte[] encode(byte[] cbuffered, int off, int len)
	{
		if (encoder == null)
			encoder = new EncoderBuffer();
		byte[] bytes = encoder.encode(cbuffered, off, len);
		encoder.clearBuffer();
		return bytes;
	}

	private static DecoderBuffer decoder;
	public static byte[] decode(String src)
	{
		return decode(src.getBytes());
	}
	public static byte[] decode(byte[] src)
	{
		return decode(src, 0, src.length);
	}
	public static synchronized byte[] decode(byte[] cbuffered, int off, int len) 
	{
		if (decoder == null)
			decoder = new DecoderBuffer();
		byte[] bytes = decoder.decode(cbuffered, off, len);
		decoder.clearBuffer();
		return bytes;
	}




	@XAnnotations("from byte array cast to hex write OutputStream")
	public static class EncOutputStream extends OutputStream
	{
		private final OutputStream stream;
		private final EncoderBuffer encoder;
		public EncOutputStream(OutputStream hexWrite)
		{
			this.stream = XObjects.requireNonNull(hexWrite);
			this.encoder = new EncoderBuffer();
		}

		private static final byte[] HEX_CHAR = {'0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

		private byte[] oneHex = new byte[2];
		private void write0(byte b) throws IOException
		{
			oneHex = encoder.encode(b);
			// 一个byte为8位，可用两个十六进制位标识
			int a = 0;
			if (b < 0) 
				a = 256 + b;
			else 
				a = b;
			stream.write(HEX_CHAR[a / 16]);
			stream.write(HEX_CHAR[a % 16]);
		}
		private byte[] cbuffered = new byte[0];
		private void write0(byte[] bytes, int off, int len) throws IOException
		{
			if (cbuffered.length != len * 2)
				cbuffered = new byte[len * 2];
			int index = 0;
			for (int i = 0;i < len;i++)
			{
				byte b = bytes[i + off];
				int a = 0;
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
		public void write(int p1) throws java.io.IOException
		{
			write0((byte)p1);
		}
		public void write(byte[] b) throws java.io.IOException
		{
			write0(b, 0, b.length);
		}
		@XAnnotations("write len*2 char    =len*2byte")
		public void write(byte[] b, int off, int len) throws java.io.IOException
		{
			write0(b, off, len);
		}
		public void flush() throws java.io.IOException
		{
			stream.flush();
		}
		public void close() throws java.io.IOException
		{
			stream.close();
		}

		public void clearBuffer()
		{
			cbuffered = XStaticFixedValue.nullbyteArray;
		}

		public OutputStream getStream()
		{
			return stream;
		}
	}



	@XAnnotations("from InputStream read hex cast to byte array")
	public static class DecInputStream extends InputStream
	{
		//private static final String hexString = "0123456789abcdef";
		private final InputStream stream;
		public DecInputStream(InputStream hexReader)
		{
			this.stream = XObjects.requireNonNull(hexReader);
		}
		private byte[] oneHex = new byte[2];
		@XAnnotations("read 2 byte")
		public int read() throws IOException
		{
			int read = stream.read(oneHex);
			if (read != 2)
				return -1;
			if (oneHex[0] >= 48 && oneHex[0] <= 57)
				oneHex[0] -= 48;
			else if (oneHex[0] >= 97 && oneHex[0] <= 102)
				oneHex[0] -= 87;
			else
				oneHex[0] = -1;

			if (oneHex[1] >= 48 && oneHex[1] <= 57)
				oneHex[1] -= 48;
			else if (oneHex[1] >= 97 && oneHex[1] <= 102)
				oneHex[1] -= 87;
			else
				oneHex[1] = -1;
			return (byte)(oneHex[0] << 4 | oneHex[1]);

			//return (byte)(hexString.indexOf(oneHex[0])) << 4 | (byte)(hexString.indexOf(oneHex[1]));
		}
		public int read(byte[] b) throws java.io.IOException
		{
			return read(b, 0, b.length);
		} 
		private byte[] cbuffered = new byte[0];
		@XAnnotations("read len*2 byte")
		public int read(byte[] b, int off, int len) throws java.io.IOException
		{
			if (cbuffered.length != len * 2)
				cbuffered = new byte[len * 2];
			final int read = stream.read(cbuffered);
			if (read == -1)
				return -1;
			int forlength = read / 2;
			int index = off;
			for (int i = 0; i < forlength; i++)
			{  
				int Pos = i * 2;
				//b[index++] = (byte) ((byte)(hexString.indexOf(cbuffered[Pos])) << 4 | (byte)(hexString.indexOf(cbuffered[Pos + 1])));  
				if (cbuffered[Pos] >= 48 && cbuffered[Pos] <= 57)
					cbuffered[Pos] -= 48;
				else if (cbuffered[Pos] >= 97 && cbuffered[Pos] <= 102)
					cbuffered[Pos] -= 87;
				else
					cbuffered[Pos] = -1;

				if (cbuffered[Pos + 1] >= 48 && cbuffered[Pos + 1] <= 57)
					cbuffered[Pos + 1] -= 48;
				else if (cbuffered[Pos + 1] >= 97 && cbuffered[Pos + 1] <= 102)
					cbuffered[Pos + 1] -= 87;
				else
					cbuffered[Pos + 1] = -1;
				b[index++] = (byte)(cbuffered[Pos] << 4 | cbuffered[Pos + 1]);
			}  
			return forlength;
		}
		@XAnnotations("skip n*2")
		public long skip(long n) throws java.io.IOException
		{
			long skip =  stream.skip(n * 2);//一个 hex 长度为2字节
			skip = skip > 0 ?skip / 2: 0;
			return skip;
		}
		public void close() throws java.io.IOException
		{
			stream.close();
			stream.close();
		}
		@XAnnotations("get available/2")
		public int available() throws IOException
		{
			int available = stream.available();
			available = available > 0 ?available / 2: 0;
			return available;
		}
		@XAnnotations("mark readlimit*2")
		public synchronized void mark(int readlimit)
		{
			stream.mark(readlimit * 2);
		}
		public synchronized void reset() throws java.io.IOException
		{
			stream.reset();
		}
		public boolean markSupported()
		{
			return stream.markSupported();
		}

		public void clearBuffer()
		{
			cbuffered = XStaticFixedValue.nullbyteArray;
		}


		public InputStream getStream()
		{
			return stream;
		}
	}









}
