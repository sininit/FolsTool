package top.fols.box.io.base.ns;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import top.fols.box.io.interfaces.ReleasableCache;
import top.fols.box.io.interfaces.XInterfacePrivateBuffOperat;
import top.fols.box.io.interfaces.XInterfacePrivateByteArrayBuffSearchOperat;
import top.fols.box.io.interfaces.XInterfacePrivateFixedStreamIndexOperat;
import top.fols.box.io.interfaces.XInterfacePrivateFixedStreamSizeOperat;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XArraysUtils;

/**
 @java.io.ByteArrayOutputStream
 **/
public class XNsByteArrayOutputStreamUtils extends OutputStream implements  XInterfacePrivateBuffOperat<byte[]>,XInterfacePrivateByteArrayBuffSearchOperat,XInterfacePrivateFixedStreamSizeOperat,XInterfacePrivateFixedStreamIndexOperat,ReleasableCache
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



	// 保存“字节数组输出流”数据的数组  
	private byte buf[];  
	// “字节数组输出流”的计数  
	private int count;  
	// 构造函数：默认创建的字节数组大小是32。  
	public XNsByteArrayOutputStreamUtils()
	{  
		this(32);  
	}  
	// 构造函数：创建指定数组大小的“字节数组输出流”  
	public XNsByteArrayOutputStreamUtils(int size)
	{  
		if (size < 0)
		{  
			throw new IllegalArgumentException("Negative initial size: " + size);  
		}  
		buf = new byte[size];  
	}  
	// 确认“容量”。  
	// 若“实际容量 < minCapacity”，则增加“字节数组输出流”的容量  
	private void ensureCapacity(int minCapacity)
	{  
		// overflow-conscious code  
		if (minCapacity - buf.length > 0)  
			grow(minCapacity);  
	}  
	// 增加“容量”。  
	private void grow(int minCapacity)
	{  
		int oldCapacity = buf.length;  
		// “新容量”的初始化 = “旧容量”x2  
		int newCapacity = oldCapacity << 1;  
		// 比较“新容量”和“minCapacity”的大小，并选取其中较大的数为“新的容量”。  
		if (newCapacity - minCapacity < 0)  
			newCapacity = minCapacity;  
		if (newCapacity < 0)
		{  
			if (minCapacity < 0) // overflow  
				throw new OutOfMemoryError();  
			newCapacity = Integer.MAX_VALUE;  
		}  
		buf = Arrays.copyOf(buf, newCapacity);  
	}  
	// 写入一个字节b到“字节数组输出流”中，并将计数+1  
	public  void write(int b)
	{  
		ensureCapacity(count + 1);  
		buf[count] = (byte) b;  
		count += 1;  
	}  
	// 写入字节数组b到“字节数组输出流”中。off是“写入字节数组b的起始位置”，len是写入的长度  
	public  void write(byte b[], int off, int len)
	{  
		if ((off < 0) || (off > b.length) || (len < 0) ||  
			((off + len) - b.length > 0))
		{  
			throw new IndexOutOfBoundsException();  
		}  
		ensureCapacity(count + len);  
		System.arraycopy(b, off, buf, count, len);  
		count += len;  
	}  
	// 写入输出流outb到“字节数组输出流”中。  
	public  void writeTo(OutputStream out) throws IOException
	{  
		out.write(buf, 0, count);  
	}  
	// 重置“字节数组输出流”的计数。  
	public  void reset()
	{  
		count = 0;  
	}  
	// 将“字节数组输出流”转换成字节数组。  
	public  byte toByteArray()[]
	{  
		if(count == 0)
			return XStaticFixedValue.nullbyteArray;
		return Arrays.copyOf(buf, count);  
	}  
	// 返回“字节数组输出流”当前计数值  
	public  int size()
	{  
		return count;  
	}  
	public  String toString()
	{  
		return new String(buf, 0, count);  
	}  
	public  String toString(String charsetName) throws UnsupportedEncodingException  
	{  
		return new String(buf, 0, count, charsetName);  
	}  
	@Deprecated 
	public  String toString(int hibyte)
	{  
		return new String(buf, hibyte, 0, count);  
	}  
	public void close() throws IOException
	{
	}

	
	
	public void releaseCache()
	{
		buf = XStaticFixedValue.nullbyteArray;
		count = 0;
	}
	
	
	public void setSize(int size)
	{
		if (size > buf.length)
			size = buf.length;
		count = size;
	}
	public byte[] getBuff()
	{
		return buf;
	}


	@Override
	public void seekIndex(int index)
	{
		setSize(index);
	}

	@Override
	public int getIndex()
	{
		return count;
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

}

