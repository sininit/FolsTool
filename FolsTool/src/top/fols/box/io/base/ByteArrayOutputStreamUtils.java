package top.fols.box.io.base;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import top.fols.box.io.base.ns.XNsByteArrayOutputStreamUtils;
public class ByteArrayOutputStreamUtils extends XNsByteArrayOutputStreamUtils
{
	public ByteArrayOutputStreamUtils()
	{  
		super();  
	}  
	public ByteArrayOutputStreamUtils(int size)
	{  
		super(size);
	}  
	public synchronized void write(int b)
	{  
		super.write(b);
	}  
	public synchronized void write(byte b[], int off, int len)
	{  
		super.write(b,off,len);
	}  
	public synchronized void writeTo(OutputStream out) throws IOException
	{  
		super.writeTo(out);  
	} 
	public synchronized void reset()
	{  
		super.reset();  
	}  
	public synchronized byte toByteArray()[]
	{  
		return super.toByteArray();  
	}  
	public synchronized int size()
	{  
		return super.size();  
	}  
	public synchronized String toString()
	{  
		return super.toString();  
	}  
	public synchronized String toString(String charsetName) throws UnsupportedEncodingException  
	{  
		return super.toString(charsetName);  
	}  
	@Deprecated 
	public synchronized String toString(int hibyte)
	{  
		return super.toString(hibyte);  
	}  
	
	public synchronized void setSize(int size)
	{
		super.setSize(size);
	}
	public synchronized byte[] getBuff()
	{
		return super.getBuff();
	}
	public synchronized void seekIndex(int index)
	{
		super.seekIndex(index);
	}

	public synchronized void releaseCache()
	{
		// TODO: Implement this method
		super.releaseCache();
	}

	
	
	
	
	
	
}
