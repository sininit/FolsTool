package top.fols.box.io.base.ns;
import java.io.IOException;
import java.io.Writer;
import top.fols.box.io.base.ns.XNsStringWriterUtils;

public class XNsStringWriterUtils extends Writer
{
    private StringBuilder buf;
    public XNsStringWriterUtils()
	{
        buf = new StringBuilder();
        lock = buf;
    }
    public XNsStringWriterUtils(int initialSize)
	{
        if (initialSize < 0)
		{
            throw new IllegalArgumentException("Negative buffer size");
        }
        buf = new StringBuilder(initialSize);
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
    public XNsStringWriterUtils append(CharSequence csq)
	{
        write(String.valueOf(csq));
        return this;
    }
    public XNsStringWriterUtils append(CharSequence csq, int start, int end)
	{
	  	if (csq == null) csq = "null";
        return append(csq.subSequence(start, end));
    }
    public XNsStringWriterUtils append(char c)
	{
        write(c);
        return this;
    }
    public String toString()
	{
        return buf.toString();
    }
    public StringBuilder getBuffer()
	{
        return buf;
    }
    public void flush()
	{
    }
    public void close() throws IOException
	{
    }
	
	
	public void setBuff(StringBuilder buf)
	{
		if(buf == null)
			throw new NullPointerException();
		this.buf = buf;
	}
	
}

