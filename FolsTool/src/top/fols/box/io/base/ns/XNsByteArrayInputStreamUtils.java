package top.fols.box.io.base.ns;

import java.io.IOException;
import java.io.InputStream;
import top.fols.box.io.interfaces.XInterfacePrivateBuffOperat;
import top.fols.box.io.interfaces.XInterfacePrivateByteArrayBuffSearchOperat;
import top.fols.box.io.interfaces.XInterfacePrivateFixedStreamIndexOperat;
import top.fols.box.io.interfaces.XInterfacePrivateFixedStreamSizeOperat;
import top.fols.box.io.interfaces.XInterfaceStreanNextRow;
import top.fols.box.util.XArraysUtils;
/**
 @java.io.ByteArrayInputStream
 **/
public class XNsByteArrayInputStreamUtils extends InputStream implements  XInterfacePrivateBuffOperat<byte[]>,XInterfaceStreanNextRow<byte[]>,XInterfacePrivateByteArrayBuffSearchOperat,XInterfacePrivateFixedStreamSizeOperat,XInterfacePrivateFixedStreamIndexOperat
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
		// TODO: Implement this method
		return buf.length;
	}



	// 保存字节输入流数据的字节数组
	private byte buf[];
	// 下一个会被读取的字节的索引
	private int pos;
	// 标记的索引
	private int mark = 0;
	// 字节流的长度
	private int count;
	// 构造函数：创建一个内容为buf的字节流
	public XNsByteArrayInputStreamUtils(byte buf[])
	{
		// 初始化“字节流对应的字节数组为buf”
		this.buf = buf;
		// 初始化“下一个要被读取的字节索引号为0”
		this.pos = 0;
		// 初始化“字节流的长度为buf的长度”
		this.count = buf.length;
	}
	// 构造函数：创建一个内容为buf的字节流，并且是从offset开始读取数据，读取的长度为length
	public XNsByteArrayInputStreamUtils(byte buf[], int offset, int length)
	{
		// 初始化“字节流对应的字节数组为buf”
		this.buf = buf;
		// 初始化“下一个要被读取的字节索引号为offset”
		this.pos = offset;
		// 初始化“字节流的长度”
		this.count = Math.min(offset + length, buf.length);
		// 初始化“标记的字节流读取位置”
		this.mark = offset;
	}
	// 读取下一个字节
	public  int read()
	{
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}
	// 将“字节流的数据写入到字节数组b中”
	// off是“字节数组b的偏移地址”，表示从数组b的off开始写入数据
	// len是“写入的字节长度”
	public  int read(byte b[], int off, int len)
	{
		if (b == null)
		{
			throw new NullPointerException();
		}
		else if (off < 0 || len < 0 || len > b.length - off)
		{
			throw new IndexOutOfBoundsException();
		}
		if (pos >= count)
		{
			return -1;
		}
		int avail = count - pos;
		if (len > avail)
		{
			len = avail;
		}
		if (len <= 0)
		{
			return 0;
		}
		System.arraycopy(buf, pos, b, off, len);
		pos += len;
		return len;
	}

	@Override
	public byte[] readLineDefaultSplitChar()
	{
		return Bytes_NextLineN;
	}
	private boolean isReadSeparator = false;
	public byte[] readLine()
	{
		return readLine(Bytes_NextLineN, true);
	}
	public byte[] readLine(byte[] splitChar)
	{
		return readLine(splitChar, true);
	}
	public byte[] readLine(byte[] split, boolean resultAddSplitChar)
	{
		isReadSeparator = false;
		if (pos >= count)
			return null;
		int index = XArraysUtils.indexOf(buf, split, pos, count);
		if (index == -1)
		{
			byte[] newArray = new byte[count - pos];
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
				byte[] newArray = new byte[index - pos + split.length];
				System.arraycopy(buf, pos, newArray, 0, newArray.length);

				pos = index + split.length;
				return newArray;
			}
			else
			{
				if (index - pos <  1)
				{
					pos = index + split.length;
					return nullBytes;
				}	
				byte[] newArray = new byte[index - pos];
				System.arraycopy(buf, pos, newArray, 0, newArray.length);

				pos = index + split.length;
				return newArray;
			}
		}
	}
	public  boolean readLineIsReadToSeparator()
	{
		return isReadSeparator;
	}




	// 跳过“字节流”中的n个字节。
	public  long skip(long n)
	{
		long k = count - pos;
		if (n < k)
		{
			k = n < 0 ? 0 : n;
		}
		pos += k;
		return k;
	}
	// “能否读取字节流的下一个字节”
	public  int available()
	{
		return count - pos;
	}
	// 是否支持“标签”
	public boolean markSupported()
	{
		return true;
	}
	// 保存当前位置。readAheadLimit在此处没有任何实际意义
	public void mark(int readAheadLimit)
	{
		mark = pos;
	}
	// 重置“字节流的读取索引”为“mark所标记的位置”
	public  void reset()
	{
		pos = mark;
	}
	public void close() throws IOException
	{
	}


	public  void setSize(int size)
	{
		if (size > buf.length)
			size = buf.length;
		count = size;
	}
	public  byte[] getBuff()
	{
		return buf;
	}
	public  void seekIndex(int index)
	{
		if (!(index > -1 && index <= count))
			throw new ArrayIndexOutOfBoundsException("can't set pos index=" + index + " length=" + count);
		pos = index;
	}
	public  int getIndex()
	{
		return pos;
	}



	public int indexOfBuff(byte b, int start)
	{
		return XArraysUtils.indexOf(buf,b,start,buf.length);
	}
	public int indexOfBuff(byte[] b, int start)
	{
		return XArraysUtils.indexOf(buf,b,start,buf.length);
	}
	public int indexOfBuff(byte b, int start, int end)
	{
		return XArraysUtils.indexOf(buf,b,start,end);
	}
	public int indexOfBuff(byte[] b, int start, int end)
	{
		return XArraysUtils.indexOf(buf,b,start,end);
	}


	public int lastIndexOfBuff(byte b, int start)
	{
		return XArraysUtils.lastIndexOf(buf,b,0,start);
	}
	public int lastIndexOfBuff(byte[] b, int start)
	{
		return XArraysUtils.lastIndexOf(buf,b,0,start);
	}
	public int lastIndexOfBuff(byte b, int start, int end)
	{
		return XArraysUtils.lastIndexOf(buf,b,start,end);
	}
	public int lastIndexOfBuff(byte[] b, int start, int end)
	{
		return XArraysUtils.lastIndexOf(buf,b,start,end);
	}

	public int size()
	{
		return buf.length;
	}




}

