package top.fols.box.io.base;
import java.io.IOException;
import java.io.InputStream;
import top.fols.box.io.base.ns.XNsInputStreamFixedLength;

public class XInputStreamFixedLength extends XNsInputStreamFixedLength
{
	public XInputStreamFixedLength(InputStream inputstream, long MaxReadSize)
	{
		super(inputstream,MaxReadSize);
	}
	public synchronized int read() throws IOException
	{
		return super.read();
	}
    public synchronized int read(byte[] b, int off, int len) throws java.io.IOException
	{
		return super.read(b,off,len);
	}
	
	
	
	
	public synchronized long getFixedLengthFree()
	{
		return super.getFixedLengthFree();
	}
	public synchronized boolean isFixedLengthAvailable()
	{
		return super.isFixedLengthAvailable();
	}
	public synchronized long getFixedLengthUseSize()
	{
		return super.getFixedLengthUseSize();
	}
	public synchronized void resetFixedLengthUseSize()
	{
		 super.resetFixedLengthUseSize();
	}

	public synchronized long getFixedLengthMaxSize()
	{
		return super.getFixedLengthMaxSize();
	}
	public synchronized void setFixedLengthMaxSize(long maxCount)
	{
		super.setFixedLengthMaxSize(maxCount);
	}
	public synchronized void setFixedLength(boolean b)
	{
		super.setFixedLength(b);
	}
	public synchronized boolean getFixedLength()
	{
		return super.getFixedLength();
	}
	
	
	
}
