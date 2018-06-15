package top.fols.box.io.base.ns;

import java.io.IOException;
import java.io.Reader;
import top.fols.box.io.interfaces.XInterfacePrivateBuffOperat;
import top.fols.box.io.interfaces.XInterfacePrivateCharArrayBuffSearchOperat;
import top.fols.box.io.interfaces.XInterfacePrivateFixedStreamIndexOperat;
import top.fols.box.io.interfaces.XInterfacePrivateFixedStreamSizeOperat;
import top.fols.box.io.interfaces.XInterfaceStreanNextRow;
import top.fols.box.util.XArraysUtils;

public class XNsCharArrayReaderUtils extends Reader implements  XInterfacePrivateBuffOperat<char[]>,XInterfaceStreanNextRow<char[]>,XInterfacePrivateCharArrayBuffSearchOperat,XInterfacePrivateFixedStreamSizeOperat,XInterfacePrivateFixedStreamIndexOperat
{

	@Override
	public int getSize()
	{
		// TODO: Implement this method
		return count;
	}

	@Override
	public int getBuffSize()
	{
		return buf == null ?0: buf.length;
	}


	public int size()
	{
		return buf.length;
	}

	/**
	 * 字符数组输入流
	 */

	/** 自带字符数组 */
	private char buf[];

	/** buf中下一个要被读取的字符位置 */
	private int pos;

	/** buf中被mark的字符下标 */
	private int markedPos = 0;

	/** 
	 * 字符数组中总数、buf中索引为count和下一个都没有字符存在。
	 */
	private int count;

	/**
	 * 使用传入的buf构造CharArrayReader、并初始化CharArrayReader的buf、以及buf中将要被读取的字符的下标及总数。
	 */
	public XNsCharArrayReaderUtils(char buf[])
	{
		this.buf = buf;
		this.pos = 0;
		this.count = buf.length;
	}

	/**
	 * 使用传入的buf构造CharArrayReader、并初始化CharArrayReader的buf、以及buf中将要被读取的字符的下标及总数。
	 */
	public XNsCharArrayReaderUtils(char buf[], int offset, int length)
	{
		if ((offset < 0) || (offset > buf.length) || (length < 0) ||
			((offset + length) < 0))
		{
			throw new IllegalArgumentException();
		}
		this.buf = buf;
		this.pos = offset;
		this.count = Math.min(offset + length, buf.length);
		this.markedPos = offset;
	}

	/** 检测此流是否关闭、看此流的close()方法就能明白这个方法*/
	private void ensureOpen() throws IOException
	{
		if (buf == null)
			throw new IOException("Stream closed");
	}

	/**
	 * 读取单个字符
	 */
	public int read() throws IOException
	{
		ensureOpen();
		if (pos >= count)
			return -1;
		else
			return buf[pos++];
	}

	/**
	 * 将buf中len个字符读取到下标从off开始的b中、返回读取的字符个数。
	 */
	public int read(char b[], int off, int len) throws IOException
	{
		ensureOpen();
		if ((off < 0) || (off > b.length) || (len < 0) ||
			((off + len) > b.length) || ((off + len) < 0))
		{
			throw new IndexOutOfBoundsException();
		}
		else if (len == 0)
		{
			return 0;
		}

		//buf中没有字符    
		if (pos >= count)
		{
			return -1;
		}
		//buf中字符不够len个
		if (pos + len > count)
		{
			len = count - pos;
		}
		//传入的len<=0、返回0
		if (len <= 0)
		{
			return 0;
		}
		System.arraycopy(buf, pos, b, off, len);
		pos += len;
		return len;
	}

	
	@Override
	public char[] readLineDefaultSplitChar()
	{
		return Chars_NextLineN;
	}
	private boolean isReadSeparator = false;
	public char[] readLine()
	{
		return readLine(Chars_NextLineN, true);
	}
	public char[] readLine(char[] splitChar)
	{
		return readLine(splitChar, true);
	}
	public char[] readLine(char[] split, boolean resultAddSplitChar)
	{
		isReadSeparator = false;
		if (pos >= count)
			return null;
		int index = XArraysUtils.indexOf(buf, split, pos, count);
		if (index == -1)
		{
			char[] newArray = new char[count - pos];
			System.arraycopy(buf, pos, newArray, 0, newArray.length);
			pos = count;
			return newArray;
		}
		else
		{
			isReadSeparator  = true;
			if (resultAddSplitChar)
			{
				if (index - pos + split.length < 1)
				{
					pos = index + split.length;
					return null;
				}	
				char[] newArray = new char[index - pos + split.length];
				System.arraycopy(buf, pos, newArray, 0, newArray.length);

				pos = index + split.length;
				return newArray;
			}
			else
			{
				if (index - pos <  1)
				{
					pos = index + split.length;
					return nullChars;
				}	
				char[] newArray = new char[index - pos];
				System.arraycopy(buf, pos, newArray, 0, newArray.length);

				pos = index + split.length;
				return newArray;
			}
		}

	}
	public boolean readLineIsReadToSeparator()
	{
		return isReadSeparator;
	}



	/**
	 * 丢弃buf中n个字符、返回实际丢弃的字符个数。
	 */
	public long skip(long n) throws IOException
	{
		ensureOpen();
		//如果buf中剩余字符不够n个、丢弃buf中现有所有字符
		if (pos + n > count)
		{
			n = count - pos;
		}
		//传入的n为负、不丢弃。
		if (n < 0)
		{
			return 0;
		}
		pos += n;
		return n;
	}

	/**
	 * 查看CharArrayReader是否可读。判断条件是buf中是否还有字符存在。
	 */
	public boolean ready() throws IOException
	{
		ensureOpen();
		return (count - pos) > 0;
	}

	/**
	 * 是否支持mark？是
	 */
	public boolean markSupported()
	{
		return true;
	}

	/**
	 * 标记当前buf中下一个将要被读取的字符下标。
	 * 传入的readAheadLimit同ByteArrayInputStream一样、无效。
	 */
	public void mark(int readAheadLimit) throws IOException
	{
		ensureOpen();
		markedPos = pos;
	}

	/**
	 * 将此流开始位置重置到最后一次调用mark是流的读取位置。
	 */
	public void reset() throws IOException
	{
		ensureOpen();
		pos = markedPos;
	}

	/**
	 * 关闭、清空buf。
	 */
	public void close()
	{
		buf = null;
	}




	public void setSize(int size)
	{
		if (size > buf.length)
			size = buf.length;
		count = size;
	}
	public char[] getBuff()
	{
		return buf;
	}
	public void seekIndex(int index)
	{
		if (!(index > -1 && index <= count))
			throw new ArrayIndexOutOfBoundsException("can't set pos index=" + index + " length=" + count);
		pos = index;
	}
	public  int getIndex()
	{
		return pos;
	}






	public int indexOfBuff(char b, int start)
	{
		return XArraysUtils.indexOf(buf, b, start, buf.length);
	}
	public int indexOfBuff(char[] b, int start)
	{
		return XArraysUtils.indexOf(buf, b, start, buf.length);
	}
	public int indexOfBuff(char b, int start, int end)
	{
		return XArraysUtils.indexOf(buf, b, start, end);
	}
	public int indexOfBuff(char[] b, int start, int end)
	{
		return XArraysUtils.indexOf(buf, b, start, end);
	}


	public int lastIndexOfBuff(char b, int start)
	{
		return XArraysUtils.lastIndexOf(buf, b, 0, start);
	}
	public int lastIndexOfBuff(char[] b, int start)
	{
		return XArraysUtils.lastIndexOf(buf, b, 0, start);
	}
	public int lastIndexOfBuff(char b, int start, int end)
	{
		return XArraysUtils.lastIndexOf(buf, b, start, end);
	}
	public int lastIndexOfBuff(char[] b, int start, int end)
	{
		return XArraysUtils.lastIndexOf(buf, b, start, end);
	}

}

