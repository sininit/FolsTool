package top.fols.box.io.base;

import java.io.IOException;
import java.io.Writer;
import top.fols.box.io.base.ns.XNsCharArrayWriterUtils;

/**
 * Writer的一个子类、可将字符写入到自带的一个缓存字符数组buf中、
 * 当buf写满时、会自动扩容。
 */
public class CharArrayWriterUtils extends XNsCharArrayWriterUtils 
{
	public CharArrayWriterUtils()
	{
    	super();
    }
    public CharArrayWriterUtils(int initialSize)
	{
        super(initialSize);
    }
    public void write(int c)
	{
		synchronized (lock)
		{
		    super.write(c);
		}
    }
    public void write(char c[], int off, int len)
	{
		synchronized (lock)
		{
		    super.write(c, off, len);
		}
    }
    public void write(String str, int off, int len)
	{
		synchronized (lock)
		{
		    super.write(str, off, len);
		}
    }
    public void writeTo(Writer out) throws IOException
	{
		synchronized (lock)
		{
		    super.writeTo(out);
		}
    }
    public Writer append(CharSequence csq)
	{
		String s = (csq == null ? "null" : csq.toString());
		write(s, 0, s.length());
		return this;
    }
    public Writer append(CharSequence csq, int start, int end)
	{
		String s = (csq == null ? "null" : csq).subSequence(start, end).toString();
		write(s, 0, s.length());
		return this;
    }
    public Writer append(char c)
	{
		write(c);
		return this;
    }
    public char toCharArray()[]
	{
		synchronized (lock)
		{
			return super.toCharArray();
		}
    }
    public String toString()
	{
		synchronized (lock)
		{
		    return super.toString();
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

	@Override
	public void releaseCache()
	{
		synchronized (lock)
		{

			super.releaseCache();
		}
	}

}
