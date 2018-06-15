package top.fols.box.io.base;
import top.fols.box.io.base.ns.XNsByteArrayInputStreamUtils;
public class ByteArrayInputStreamUtils extends XNsByteArrayInputStreamUtils
{
	public ByteArrayInputStreamUtils(byte buf[])
	{
		super(buf);
	}
	public ByteArrayInputStreamUtils(byte buf[], int offset, int length)
	{
		super(buf,offset,length);
	}
	public synchronized int read()
	{
		return super.read();
	}
	public synchronized int read(byte b[], int off, int len)
	{
		return super.read(b,off,len);
	}
	public synchronized byte[] readLine()
	{
		return super.readLine();
	}
	public synchronized byte[] readLine(byte[] splitChar)
	{
		return super.readLine(splitChar);
	}
	public synchronized byte[] readLine(byte[] split, boolean resultAddSplitChar)
	{
		return super.readLine(split,resultAddSplitChar);
	}
	public synchronized boolean readLineIsReadToSeparator()
	{
		return super.readLineIsReadToSeparator();
	}
	public synchronized long skip(long n)
	{
		return super.skip(n);
	}
	public synchronized int available()
	{
		return super.available();
	}
	public synchronized void reset()
	{
		super.reset();
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
	public synchronized int getIndex()
	{
		return super.getIndex();
	}
}
