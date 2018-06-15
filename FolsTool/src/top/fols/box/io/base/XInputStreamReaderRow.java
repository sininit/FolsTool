package top.fols.box.io.base;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.XStream;
import top.fols.box.io.base.ns.XNsByteArrayOutputStreamUtils;
import top.fols.box.io.interfaces.XInterfaceRandomAccessInputStream;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XEncodingDetect;
import top.fols.box.util.XEncodingUtils;

public class XInputStreamReaderRow extends XInterfaceRandomAccessInputStream
{
	private XInterfaceRandomAccessInputStream stream;
	private Object sync = new Object();
	private Charset streamEncoding;
	private byte[] rLSplit = XStaticFixedValue.String_NextLineN.getBytes();
	public void setLineSplitChar(char[] originLine)
	{
		synchronized (sync)
		{
			rLSplit = XEncodingUtils.getBytes(originLine, streamEncoding);
		}
	}
	public XInputStreamReaderRow(XInterfaceRandomAccessInputStream in) throws IOException
	{
		this(in, XStream.default_streamByteArrBuffSize);
	}
	public XInputStreamReaderRow(XInterfaceRandomAccessInputStream in, int buffSize) throws IOException
	{
		this(in, buffSize, null);
	}
	public XInputStreamReaderRow(XInterfaceRandomAccessInputStream in, int buffSize, Charset encoding) throws IOException
	{
		if (in == null)
			throw new NullPointerException("stream for null");
		if (buffSize < 1)
			throw new RuntimeException("buffSize=" + buffSize + ", min=1");
		stream = in;
		rLrArray = new byte[rLBufSize = buffSize];

		streamEncoding = encoding == null ? XEncodingDetect.getJavaEncode2Charset(in): encoding;
		in.seekIndex(0);
		setLineSplitChar(XStaticFixedValue.Chars_NextLineN);
	}
	

	public void mark(int readlimit)
	{
		stream.mark(readlimit);
	}
    public boolean markSupported()
	{
		return stream.markSupported();
	}
	public long skip(long n) throws java.io.IOException
	{
		if (n <= 0)
			return 0;
		return stream.skip(n);
	}
	public void reset() throws java.io.IOException
	{
		stream.reset();
	}
	public int available() throws IOException
	{
		return stream.available();
	}
	public void close()throws IOException
	{
		if (stream != null)
			stream.close();
	}
	public int read() throws IOException
	{
		isReadComplete = false;
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
		if (b == null)
			throw new NullPointerException();
		else if (off < 0 || len < 0 || len > b.length - off)
			throw new IndexOutOfBoundsException();
		int read = readBreak;
		if (stream != null)
			read = stream.read(b, off, len);
		if (read == readBreak)
			isReadComplete = true;
		return read;
	}



	private int readBreak = XStaticFixedValue.Stream_ReadBreak;
	private int rLBufSize = 8192;
	private byte[] rLrArray = null;//缓存
	private XNsByteArrayOutputStreamUtils rLReturn = new XNsByteArrayOutputStreamUtils();
	private boolean isReadComplete = false;
	private boolean isReadSeparator = false;
	private int readLineTheByteSize = 0;
	
	public char[] readLine() throws IOException
	{
		return readLine(true);
	}
	@XAnnotations("this will buffered data until read to separator")
	public char[] readLine(boolean resultAddSplitChar) throws IOException
	{
		isReadComplete = false;
		isReadSeparator = false;
		readLineTheByteSize = 0;
		
		int i = 0;//累计
		int start = -1;
		int read = -1;
		long readLength = 0;
		while (true)
		{
			if ((read = stream.read(rLrArray)) == readBreak)
			{
				isReadComplete = true;
				break;
			}	
			rLReturn.write(rLrArray, 0, read);
			readLength += read;
			if ((start = rLReturn.indexOfBuff(rLSplit, i - rLSplit.length + 1)) != -1)
				break;
			i += read;
		}
		if (start > -1)
		{
			readLineTheByteSize = start + rLSplit.length;
			if (resultAddSplitChar)
				rLReturn.setSize(start + rLSplit.length);
			else 
				rLReturn.setSize(start);
			isReadSeparator = true;
			stream.seekIndex(stream.getIndex() - (readLength - start - rLSplit.length));
			
			if (rLReturn.size() == 0 && !resultAddSplitChar)
			{
				rLReturn.releaseCache();
				return XStaticFixedValue.nullcharArray;
			}
		}
		byte Array[] = rLReturn.toByteArray();
		rLReturn.releaseCache();
		if(start < 0)
			readLineTheByteSize = Array.length;
		return XEncodingUtils.getChars((Array != null && Array.length == 0) ?null: Array, streamEncoding);
	}

	@XAnnotations("last read stream result equals -1")
	public boolean isReadComplete()
	{
		return isReadComplete;
	}
	public boolean readLineIsReadToSeparator()
	{
		return isReadSeparator;
	}
	public int getReadLineReadToByteLength()
	{
		return readLineTheByteSize;
	}

	protected static byte[] getBytes(byte[] array, int start, int stop) 
	{
		if (stop - start < 0 || start < 0 || stop < 0 || start > array.length || stop > array.length)
			return null;
		if (stop - start < 1)
			return XStaticFixedValue.nullbyteArray;
		return Arrays.copyOfRange(array, start, stop);
	}
	
	
	
	
	@Override
	public void seekIndex(long offset) throws IOException
	{
		stream.seekIndex(offset);
	}
	@Override
	public long length() throws IOException
	{
		return stream.length();
	}
	public long getIndex()
	{
		return stream.getIndex();
	}
	
	public XInterfaceRandomAccessInputStream getStream()
	{
		return stream;
	}
	public Charset getEncoding()
	{
		return streamEncoding;
	}
}
