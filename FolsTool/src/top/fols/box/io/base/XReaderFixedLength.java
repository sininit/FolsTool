package top.fols.box.io.base;
import java.io.IOException;
import java.io.Reader;
import top.fols.box.io.base.ns.XNsReaderFixedLength;

public class XReaderFixedLength extends XNsReaderFixedLength
{
	public XReaderFixedLength(Reader reader, long MaxReadSize)
	{
		super(reader, MaxReadSize);
	}
	public int read() throws IOException
	{
		synchronized (lock)
		{
			return super.read();
		}
	}
    public int read(char[] b, int off, int len) throws java.io.IOException
	{
		synchronized (lock)
		{
			return super.read(b, off, len);
		}
	}









	public long getFixedLengthFree()
	{
		synchronized (lock)
		{
			return super.getFixedLengthFree();
		}
	}
	public boolean isFixedLengthAvailable()
	{
		synchronized (lock)
		{
			return super.isFixedLengthAvailable();
		}
	}
	public long getFixedLengthUseSize()
	{
		synchronized (lock)
		{
			return super.getFixedLengthUseSize();
		}
	}
	public void resetFixedLengthUseSize()
	{
		synchronized (lock)
		{
			super.resetFixedLengthUseSize();
		}
	}

	public long getFixedLengthMaxSize()
	{
		synchronized (lock)
		{
			return super.getFixedLengthMaxSize();
		}
	}
	public void setFixedLengthMaxSize(long maxCount)
	{
		synchronized (lock)
		{
			super.setFixedLengthMaxSize(maxCount);
		}
	}
	public void setFixedLength(boolean b)
	{
		synchronized (lock)
		{
			super.setFixedLength(b);
		}
	}
	public boolean getFixedLength()
	{
		synchronized (lock)
		{
			return super.getFixedLength();
		}
	}

}
