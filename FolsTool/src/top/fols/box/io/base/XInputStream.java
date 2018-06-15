package top.fols.box.io.base;
import java.io.InputStream;
import java.io.IOException;
import top.fols.box.util.XObjects;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.annotation.XAnnotations;

public class XInputStream extends InputStream
{
	public static XInputStream wrap(InputStream in)
	{
		return new XInputStream(in);
	}
	
	private int readBreak = XStaticFixedValue.Stream_ReadBreak;
	public int read() throws java.io.IOException
	{
		isReadcomplete = false;
		int read = stream.read();
		if (read == readBreak)
			isReadcomplete = true;
		return read;
	}
    public int read(byte[] b) throws java.io.IOException
	{
		return read(b, 0, b.length);
	}
    public int read(byte[] b, int off, int len) throws java.io.IOException
	{
		isReadcomplete = false;
		int read = stream.read(b, off, len);
		if (read == readBreak)
			isReadcomplete = true;
		return read;
	}
    public long skip(long n) throws java.io.IOException
	{
		return stream.skip(n);
	}
    public int available() throws java.io.IOException
	{
		return stream.available();
	}
    public void close() throws java.io.IOException
	{
		stream.close();
	}
    public synchronized void mark(int readlimit)
	{
		stream.mark(readlimit);
	}
    public synchronized void reset() throws java.io.IOException
	{
		stream.reset();
	}
    public boolean markSupported()
	{
		return stream.markSupported();
	}





	public byte[] readLine() throws IOException
	{
		return readLine(Byte_NextLineN);
	}
	private boolean isReadcomplete = false;
	private boolean isReadSeparator = false;
	@XAnnotations("this no buffered")
	public byte[] readLine(byte separator) throws IOException
	{
		isReadcomplete = false;
		isReadSeparator = false;
		readLine_byteReturn.reset();
		int readByte;
		long i = 0;
		do 
		{
			readByte = stream.read();
			if (readByte == readBreak)
			{
				isReadcomplete = true;
				if (i == 0)
					return null;
				break;
			}
			readLine_byteReturn.write(readByte);
			i++;
		}
		while (readByte != separator);//读取一行10代表\n
		byte[] bytearr = readLine_byteReturn.toByteArray();
		if (bytearr.length != 0)
			isReadSeparator = bytearr[bytearr.length - 1] == separator;
		return (bytearr != null && bytearr.length == 0) ? null: bytearr;
	}
	@XAnnotations("last read stream result equals -1")
	public synchronized boolean isReadComplete()
	{
		return isReadcomplete;
	}
	public synchronized boolean readLineIsReadToSeparator()
	{
		return isReadSeparator;
	}
	public InputStream getStream()
	{
		return stream;
	}



	public static byte Byte_NextLineN = XStaticFixedValue.Byte_NextLineN;
	private final InputStream stream;
	private final ByteArrayOutputStreamUtils readLine_byteReturn = new ByteArrayOutputStreamUtils();

	public XInputStream(InputStream inputstream)
	{
		this.stream = XObjects.requireNonNull(inputstream);
	}


}
