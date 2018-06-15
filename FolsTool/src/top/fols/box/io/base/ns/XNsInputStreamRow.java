package top.fols.box.io.base.ns;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.XStream;
import top.fols.box.io.base.ByteArrayInputStreamUtils;
import top.fols.box.io.interfaces.XInterfacePrivateBuffOperat;
import top.fols.box.io.interfaces.XInterfaceStreanNextRow;
import top.fols.box.statics.XStaticFixedValue;
public class XNsInputStreamRow  extends InputStream implements XInterfacePrivateBuffOperat<byte[]>,XInterfaceStreanNextRow<byte[]>
{


	private InputStream stream = null;
	private byte[] buf;
	private int readBreak = XStaticFixedValue.Stream_ReadBreak;
	public XNsInputStreamRow(InputStream in)
	{
		init(in, rLBufSize);
	}
	public XNsInputStreamRow(InputStream in, int readLine_BuffSize)
	{
		init(in, readLine_BuffSize);
	}
	void init(InputStream in, int buffSize)
	{
		if (in == null)
			throw new NullPointerException("stream for null");
		if (buffSize < 1)
			throw new RuntimeException("buffSize=" + buffSize + ", min=1");
		stream = in;
		rLrArray = new byte[rLBufSize = buffSize];
	}
    public void mark(int readlimit)
	{
		stream.mark(readlimit);
	}
    public boolean markSupported()
	{
		return stream.markSupported();
	}
	public  long skip(long n) throws java.io.IOException
	{
		if (buf != null && buf.length == 0)
			buf = null;
		if (n <= 0)
			return 0;
		if (buf != null)
		{
			int length = buf.length;
			int start;
			if (n > Integer.MAX_VALUE)
				start = Integer.MAX_VALUE;
			else
				start = (int)n;
			if (start > length)
				start = length;
			buf = getBytes(buf, start, buf.length);
			n = n - start;
		}
		if (n > 0)
			stream.skip(n);
		return n;
	}
	public void reset() throws java.io.IOException
	{
		buf = null;
		stream.reset();
	}
	public int available()throws IOException
	{
		if (buf != null && buf.length == 0)
			buf = null;
		if (buf != null)
			return buf.length;
		return stream.available();
	}
	public void close()throws IOException
	{
		if (stream != null)
			stream.close();
		clearBuff();
	}
	public  int read() throws IOException
	{
		isReadComplete = false;
		if (buf != null && buf.length == 0)
			buf = null;
		if (buf != null)
		{
			if (buf.length > 0)
			{
				int byteint = buf[0];
				buf = getBytes(buf, 1, buf.length);
				return byteint & 0xff;
			}
		}
		int read = readBreak;
		if (stream != null)
			read = stream.read();
		if (read == readBreak)
			isReadComplete = true;
		return read;
	}
	public  int read(byte[] b, int off, int len) throws IOException
	{
		isReadComplete = false;
		if (buf != null && buf.length == 0)
			buf = null;
		if (b == null)
			throw new NullPointerException();
		else if (off < 0 || len < 0 || len > b.length - off)
			throw new IndexOutOfBoundsException();
		if (buf != null)
		{
			if (len > buf.length)
				len = buf.length;
			System.arraycopy(buf, 0, b, off, len);
			buf = getBytes(buf, len, buf.length);
			return len;
		}
		int read = readBreak;
		if (stream != null)
			read = stream.read(b, off, len);
		if (read == readBreak)
			isReadComplete = true;
		return read;
	}

	/*
	 Get Next Line(Buffered)
	 获取下一行(Buffered)

	 new this(new ByteArrayInputStream("abc\n123\n+-*".getBytes())).readLine(); >> {97, 98, 99, 10}{49, 50, 51, 10}{43, 45, 42}("abc\n","123\n","+-*")
	 new this(new ByteArrayInputStream("abc\n123\n".getBytes())).readLine(); >> {97, 98, 99, 10}{49, 50, 51, 10}("abc\n","123\n")
	 */
	//13 10 13 10 代表\r\n\r\n
	//10 10 代表\n\n
	private int rLBufSize = XStream.default_streamByteArrBuffSize;
	private byte[] rLrArray = null;//缓存
	private XNsByteArrayOutputStreamUtils rLReturn = new XNsByteArrayOutputStreamUtils();
	private boolean isReadComplete = false;
	private boolean isReadSeparator = false;
	@Override
	public byte[] readLineDefaultSplitChar()
	{
		return Bytes_NextLineN;
	}
	public byte[] readLine() throws IOException
	{
		return readLine(Bytes_NextLineN);
	}
	public byte[] readLine(byte[] rLSplit) throws IOException
	{
		return readLine(rLSplit, true);
	}
	@XAnnotations("this will buffered data until read to separator")
	public  byte[] readLine(byte[] rLSplit, boolean resultAddSplitChar) throws IOException
	{
		if (buf != null && buf.length == 0)
			buf = null;
		isReadComplete = false;
		isReadSeparator = false;
		int i = 0;//累计
		int start = -1;
		int read = -1;
		if (buf != null)
		{
			rLReturn.write(buf, 0, buf.length);
			buf = null;
		}
		if ((start = rLReturn.indexOfBuff(rLSplit, 0, rLReturn.size())) == -1)
		{
			while (true)
			{
				if ((read = stream.read(rLrArray)) == readBreak)
				{
					isReadComplete = true;
					break;
				}	
				rLReturn.write(rLrArray, 0, read);
				if ((start = rLReturn.indexOfBuff(rLSplit, i - rLSplit.length + 1)) != -1)
					break;
				i += read;
			}
		}
		if (start > -1)
		{
			byte[] Array = rLReturn.getBuff();
			int ArraySize = rLReturn.size();
			if (resultAddSplitChar)
				rLReturn.setSize(start + rLSplit.length);
			else 
				rLReturn.setSize(start);
			buf = getBytes(Array, start + rLSplit.length, ArraySize);	
			isReadSeparator = true;

			if (rLReturn.size() == 0 && !resultAddSplitChar && buf != null)
			{
				rLReturn.releaseCache();
				return nullBytes;
			}
		}
		byte Array[] = rLReturn.toByteArray();
		rLReturn.releaseCache();
		return (Array != null && Array.length == 0) ?null: Array;
	}




	@XAnnotations("last read stream result equals -1")
	public  boolean isReadComplete()
	{
		return isReadComplete;
	}
	public  boolean readLineIsReadToSeparator()
	{
		return isReadSeparator;
	}

	@Override
	public int getBuffSize()
	{
		return buf == null ?0: buf.length;
	}
	/*
	 Clear Buffered
	 清空缓存区
	 */
	public  void clearBuff()
	{
		buf = null;
	}
	/*
	 Get Buffered 
	 获取缓存区
	 */
	public byte[] getBuff()
	{
		if (buf != null && buf.length == 0)
			buf = null;
		return buf;
	}

	public InputStream getStream()
	{
		return stream;
	}

	protected static byte[] getBytes(byte[] array, int start, int stop) 
	{
		if (stop - start < 0 || start < 0 || stop < 0 || start > array.length || stop > array.length)
			return null;
		if (stop - start < 1)
			return ByteArrayInputStreamUtils.nullBytes;
		return Arrays.copyOfRange(array, start, stop);
	}
}

