package top.fols.box.io.base;

import java.io.Reader;
import java.io.IOException;
import top.fols.box.util.XObjects;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.annotation.XAnnotations;

public class XReader extends Reader
{
	public static XReader wrap(Reader in)
	{
		return new XReader(in);
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
    public int read(char[] b) throws java.io.IOException
	{
		return read(b, 0, b.length);
	}
    public int read(char[] b, int off, int len) throws java.io.IOException
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
    public void close() throws java.io.IOException
	{
		stream.close();
	}
    public synchronized void mark(int readlimit) throws IOException
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
	public boolean ready() throws java.io.IOException
	{
		return stream.ready();
	}






	public char[] readLine() throws IOException
	{
		return readLine(char_NextLineN);
	}
	private boolean isReadcomplete = false;
	private boolean isReadSeparator = false;
	@XAnnotations("this no buffered")
	public char[] readLine(char separator) throws IOException
	{
		isReadcomplete = false;
		isReadSeparator = false;
		readLine_charReturn.reset();
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
			readLine_charReturn.write(readByte);
			i++;
		}
		while (readByte != separator);//读取一行10代表\n
		char[] bytearr = readLine_charReturn.toCharArray();
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
	public Reader getStream()
	{
		return stream;
	}



	public static char char_NextLineN = XStaticFixedValue.Char_NextLineN;
	private final Reader stream;
	private final CharArrayWriterUtils readLine_charReturn = new CharArrayWriterUtils();

	public XReader(Reader inputstream)
	{
		this.stream = XObjects.requireNonNull(inputstream);
	}


}
