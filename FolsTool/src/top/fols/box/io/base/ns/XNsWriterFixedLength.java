package top.fols.box.io.base.ns;

import java.io.IOException;
import java.io.Writer;
import top.fols.box.io.interfaces.XInterfaceStreamFixedLength;
public class XNsWriterFixedLength extends Writer implements XInterfaceStreamFixedLength<Writer>
{

	private Writer stream;
	private long maxCount;
	private long nowCount;
	private boolean fixed;
	public XNsWriterFixedLength(Writer stream, long maxCount)
	{
		if (stream == null)
			throw new NullPointerException("stream for null");
		if (maxCount < 0)
			maxCount = 0;
		this.stream = stream;
		this.maxCount = maxCount;
		this.nowCount = 0;
		this.fixed = true;
	}

	// 写入一个字节b到“字节数组输出流”中，并将计数+1  
	public void write(int b) throws IOException
	{  
		if (fixed && nowCount + 1 > maxCount)
			return;
		stream.write(b);
		nowCount++;
	}  
	// 写入字节数组b到“字节数组输出流”中。off是“写入字节数组b的起始位置”，len是写入的长度  
	public  void write(char b[], int off, int len) throws IOException
	{
		if (len < 0)
			return;
		else if (fixed)
		{
			if (nowCount + len > maxCount)
			{
				len = (int)(maxCount - nowCount);
				if (len < 1)
					return;
			}
			if (nowCount + len > maxCount)
				return;
		}
		stream.write(b, off, len);
		nowCount += len;
	}
	public void write(java.lang.String str, int off, int len) throws IOException
	{
		if (len < 0)
			return;
		else if (fixed)
		{
			if (nowCount + len > maxCount)
			{
				len = (int)(maxCount - nowCount);
				if (len < 1)
					return;
			}
			if (nowCount + len > maxCount)
				return;
		}
		stream.write(str, off, len);
		nowCount += len;
	}
	public void close() throws IOException
	{
		stream.close();
	}
	public void flush() throws IOException
	{
		stream.flush();
	}




	public long getFixedLengthFree()
	{
		return maxCount - nowCount;
	}
	public boolean isFixedLengthAvailable()
	{
		return !fixed || fixed && nowCount < maxCount;
	}

	public long getFixedLengthUseSize()
	{
		return nowCount;
	}
	public void resetFixedLengthUseSize()
	{
		nowCount = 0;
	}

	public long getFixedLengthMaxSize()
	{
		return maxCount;
	}
	public void setFixedLengthMaxSize(long maxCount)
	{
		this.maxCount = maxCount;
	}

	public void setFixedLength(boolean b)
	{
		this.fixed = b;
	}
	public boolean getFixedLength()
	{
		return fixed;
	}


	public Writer getStream()
	{
		return stream;
	}
}

