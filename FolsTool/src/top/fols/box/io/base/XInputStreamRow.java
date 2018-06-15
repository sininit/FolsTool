package top.fols.box.io.base;
import java.io.IOException;
import java.io.InputStream;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.base.ns.XNsInputStreamRow;
public class XInputStreamRow  extends XNsInputStreamRow
{
	public XInputStreamRow(InputStream in)
	{
		super(in);
	}
	public XInputStreamRow(InputStream in, int readLine_BuffSize)
	{
		super(in, readLine_BuffSize);
	}
	public synchronized long skip(long n) throws java.io.IOException
	{
		return super.skip(n);
	}
	public synchronized  void reset() throws java.io.IOException
	{
		super.reset();
	}
	public synchronized int available()throws IOException
	{
		return super.available();
	}
	public synchronized int read() throws IOException
	{
		return super.read();
	}
	public synchronized int read(byte[] b, int off, int len) throws IOException
	{
		return super.read(b,off,len);
	}
	public synchronized byte[] readLine() throws IOException
	{
		return super.readLine();
	}
	public synchronized byte[] readLine(byte[] rLSplit) throws IOException
	{
		return super.readLine(rLSplit);
	}
	public synchronized byte[] readLine(byte[] rLSplit, boolean resultAddSplitChar) throws IOException
	{
		return super.readLine(rLSplit,resultAddSplitChar);
	}
	@XAnnotations("last read stream result equals -1")
	public synchronized boolean isReadComplete()
	{
		return super.isReadComplete();
	}
	public synchronized boolean readLineIsReadToSeparator()
	{
		return super.readLineIsReadToSeparator();
	}
	public synchronized void clearBuff()
	{
		super.clearBuff();
	}
	public synchronized  byte[] getBuff()
	{
		return super.getBuff();
	}
}
