package top.fols.box.io.base;
import java.io.IOException;
import top.fols.box.io.base.ns.XNsCharArrayReaderUtils;

public class CharArrayReaderUtils extends XNsCharArrayReaderUtils
{
	public CharArrayReaderUtils(char buf[])
	{
		super(buf);
	}
	public CharArrayReaderUtils(char buf[], int offset, int length)
	{
		super(buf,offset,length);
	}
	public int read() throws IOException
	{
		synchronized (lock)
		{
			return super.read();
		}
	}
	public int read(char b[], int off, int len) throws IOException
	{
		synchronized (lock)
		{
			return super.read(b,off,len);
		}
	}
	
	
	
	public char[] readLine()
	{
		synchronized (lock)
		{
		return super. readLine();
		}
	}
	public char[] readLine(char[] splitChar)
	{
		synchronized (lock)
		{
		return super.readLine(splitChar);
		}
	}
	public char[] readLine(char[] split, boolean resultAddSplitChar)
	{
		synchronized (lock)
		{
			return super.readLine(split,resultAddSplitChar);
		}
	}
	public boolean readLineIsReadToSeparator()
	{
		synchronized (lock)
		{
			return super.readLineIsReadToSeparator();
		}
	}

	
	
	
	public long skip(long n) throws IOException
	{
		synchronized (lock)
		{
			return super.skip(n);
		}
	}
	public boolean ready() throws IOException
	{
		synchronized (lock)
		{
			return super.ready();
		}
	}
	public void mark(int readAheadLimit) throws IOException
	{
		synchronized (lock)
		{
			 super.mark(readAheadLimit);
		}
	}
	public void reset() throws IOException
	{
		synchronized (lock)
		{
			super.reset();
		}
	}
	public void setSize(int size)
	{
		synchronized (lock)
		{
			super.setSize(size);
		}
	}
	public char[] getBuff()
	{
		synchronized (lock)
		{
			return super.getBuff();
		}
	}
	public void seekIndex(int index)
	{
		synchronized (lock)
		{
			 super.seekIndex(index);
		}
	}
	public  int getIndex()
	{
		synchronized (lock)
		{
			return super.getIndex();
		}
	}

}
