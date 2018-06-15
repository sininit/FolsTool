package top.fols.box.io.base;
import java.io.IOException;
import java.io.Reader;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.base.ns.XNsReaderRow;
public class XReaderRow extends XNsReaderRow
{
	public XReaderRow(Reader in)
	{
		super(in);
	}
	public XReaderRow(Reader in, int readLine_BuffSize)
	{
		super(in, readLine_BuffSize);
	}
	public long skip(long n) throws java.io.IOException
	{
		synchronized (lock)
		{
			return super.skip(n);
		}

	}
	public void reset() throws java.io.IOException
	{
		synchronized (lock)
		{
			super.reset();
		}
	}
	public int read() throws IOException
	{
		synchronized (lock)
		{
			return super.read();
		}
	}
	public int read(char[] b, int off, int len) throws IOException
	{
		synchronized (lock)
		{
			return super.read(b, off, len);
		}
	}

	public char[] readLine() throws IOException
	{
		synchronized (lock)
		{
			return readLine();
		}
	}
	public char[] readLine(char[] rLSplit) throws IOException
	{
		synchronized (lock)
		{
			return readLine(rLSplit);
		}
	}
	@XAnnotations("this will buffered data until read to separator")
	public char[] readLine(char[] rLSplit, boolean resultAddSplitChar) throws IOException
	{
		synchronized (lock)
		{
			return super.readLine(rLSplit, resultAddSplitChar);
		}
	}
	@XAnnotations("last read stream result equals -1")
	public  boolean isReadComplete()
	{
		synchronized (lock)
		{
			return super.isReadComplete();
		}
	}
	public boolean readLineIsReadToSeparator()
	{
		synchronized (lock)
		{
			return super.readLineIsReadToSeparator();
		}
	}
	public void clearBuff()
	{
		synchronized (lock)
		{
			super.clearBuff();
		}
	}
	public char[] getBuff()
	{
		synchronized (lock)
		{
			return super.getBuff();
		}
	}
}
