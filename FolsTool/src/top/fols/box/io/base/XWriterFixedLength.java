package top.fols.box.io.base;
import java.io.IOException;
import java.io.Writer;
import top.fols.box.io.base.ns.XNsWriterFixedLength;
public class XWriterFixedLength extends XNsWriterFixedLength
{
	public XWriterFixedLength(Writer writer, long MaxWriteSize)
	{
		super(writer, MaxWriteSize);
	}
	public void write(int b) throws IOException
	{  
		synchronized (lock)
		{
			super.write(b);
		}
	}  
	public  void write(char b[], int off, int len) throws IOException
	{
		synchronized (lock)
		{
			super.write(b, off, len);
		}
	}
	public  void write(java.lang.String str, int off, int len) throws IOException
	{
		synchronized (lock)
		{
			super.write(str, off, len);
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
