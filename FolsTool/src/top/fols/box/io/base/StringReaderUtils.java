package top.fols.box.io.base;

import java.io.IOException;
import top.fols.box.io.base.ns.XNsStringReaderUtils;

public class StringReaderUtils extends XNsStringReaderUtils
{
	public StringReaderUtils(String s)
	{
        super(s);
    }
    public int read() throws IOException
	{
        synchronized (lock)
		{
            return super.read();
        }
    }
    public int read(char cbuf[], int off, int len) throws IOException
	{
        synchronized (lock)
		{
            return super.read(cbuf, off, len);
        }
    }
    public long skip(long ns) throws IOException
	{
        synchronized (lock)
		{
            return super.skip(ns);
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
    public void close()
	{
        synchronized (lock)
		{
            super.close();
        }
    }











	public char[] readLine()
	{
		synchronized (lock)
		{
			return super.readLine();
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
			return super.readLine(split, resultAddSplitChar);
		}
	}


	public boolean readLineIsReadToSeparator()
	{
		synchronized (lock)
		{
			return super.readLineIsReadToSeparator();
		}
	}

	public void setSize(int size)
	{
		synchronized (lock)
		{
			super.setSize(size);
		}
	}
	public int size()
	{
		synchronized (lock)
		{
			return super.size();
		}
	}
	public void seekIndex(int index)
	{
		synchronized (lock)
		{
			super.seekIndex(index);
		}
	}
	public int getIndex()
	{
		synchronized (lock)
		{
			return super.getIndex();
		}
	}
}


