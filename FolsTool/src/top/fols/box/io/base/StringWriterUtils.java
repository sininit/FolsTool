package top.fols.box.io.base;
import java.io.Writer;
import java.io.IOException;

public class StringWriterUtils extends Writer
{
    private StringBuffer buf;
    public StringWriterUtils()
	{
        buf = new StringBuffer();
        lock = buf;
    }
    public StringWriterUtils(int initialSize)
	{
        if (initialSize < 0)
		{
            throw new IllegalArgumentException("Negative buffer size");
        }
        buf = new StringBuffer(initialSize);
        lock = buf;
    }
    public void write(int c)
	{
        buf.append((char) c);
    }
    public void write(char cbuf[], int off, int len)
	{
        if ((off < 0) || (off > cbuf.length) || (len < 0) ||
            ((off + len) > cbuf.length) || ((off + len) < 0))
		{
            throw new IndexOutOfBoundsException();
        }
		else if (len == 0)
		{
            return;
        }
        buf.append(cbuf, off, len);
    }
    public void write(String str)
	{
        buf.append(str);
    }
    public void write(String str, int off, int len)
	{
        buf.append(str, off, off + len);
    }
    public StringWriterUtils append(CharSequence csq)
	{
        write(String.valueOf(csq));
        return this;
    }
    public StringWriterUtils append(CharSequence csq, int start, int end)
	{
	  	if (csq == null) csq = "null";
        return append(csq.subSequence(start, end));
    }
    public StringWriterUtils append(char c)
	{
        write(c);
        return this;
    }
    public String toString()
	{
        return buf.toString();
    }
    public StringBuffer getBuffer()
	{
        return buf;
    }
    public void flush()
	{
    }
    public void close() throws IOException
	{
    }
}

