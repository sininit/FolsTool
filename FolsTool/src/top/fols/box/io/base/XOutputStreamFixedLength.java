package top.fols.box.io.base;
import java.io.IOException;
import java.io.OutputStream;
import top.fols.box.io.base.ns.XNsOutputStreamFixedLength;

/**
 @java.io.OutputStream
 constraint outputstream max write size
 **/
public class XOutputStreamFixedLength extends XNsOutputStreamFixedLength 
{
	

	public XOutputStreamFixedLength(OutputStream outputstream, long MaxWriteSize)
	{
		super(outputstream,MaxWriteSize);
	}
	public synchronized void write(int b) throws IOException
	{  
		super.write(b);
	}  
	public synchronized void write(byte b[], int off, int len) throws IOException
	{
		super.write(b, off, len);
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
