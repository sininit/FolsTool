package top.fols.box.io.base;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.XStream;
import top.fols.box.io.interfaces.XInterfaceGetOriginStream;
import top.fols.box.io.interfaces.XInterfaceLineReaderStream;
import top.fols.box.io.interfaces.XInterfacePrivateBuffOperat;
import top.fols.box.io.interfaces.XInterfereReleaseBufferable;
import top.fols.box.statics.XStaticFixedValue;
public class XReaderLine<T extends Reader> extends Reader implements  XInterfacePrivateBuffOperat<char[]>,XInterfaceLineReaderStream<char[]> ,XInterfereReleaseBufferable,XInterfaceGetOriginStream<T> {
	private T stream = null;
	private char[] buf;

	public XReaderLine(T in) {
		init(in, rLBufSize);
	}
	public XReaderLine(T in, int readLine_BuffSize) {
		init(in, readLine_BuffSize);
	}
	private void init(T in, int buffSize) {
		if (null == in)
			throw new NullPointerException("stream for null");
		if (buffSize < 1)
			throw new RuntimeException("buffSize=" + buffSize + ", min=1");
		stream = in;
		rLrArray = new char[rLBufSize = buffSize];
	}



	@Override
	public boolean ready() throws java.io.IOException {
		return stream.ready();
	}
	@Override
    public void mark(int readlimit) throws IOException {
		stream.mark(readlimit);
	}
	@Override
    public boolean markSupported() {
		return stream.markSupported();
	}
	@Override
	public long skip(long n) throws java.io.IOException {
		if (null != buf && buf.length == 0)
			buf = null;
		if (n <= 0)
			return 0;
		if (null != buf) {
			int length = buf.length;
			int start;
			if (n > Integer.MAX_VALUE)
				start = Integer.MAX_VALUE;
			else
				start = (int)n;
			if (start > length)
				start = length;
			buf = newArray(buf, start, buf.length);
			n = n - start;
		}
		if (n > 0)
			stream.skip(n);
		return n;
	}
	@Override
	public void reset() throws java.io.IOException {
		buf = null;
		stream.reset();
	}
	@Override
	public void close()throws IOException {
		stream.close();
		releaseBuffer();
	}
	@Override
	public  int read() throws IOException {
		isReadComplete = false;
		if (null != buf && buf.length == 0)
			buf = null;
		if (null != buf) {
			if (buf.length > 0) {
				char byteint = buf[0];
				buf = newArray(buf, 1, buf.length);
				return byteint & 0xff;
			}
		}
		int read = stream.read();
		if (read == -1)
			isReadComplete = true;
		return read;
	}
	@Override
	public  int read(char[] b, int off, int len) throws IOException {
		isReadComplete = false;
		if (null != buf && buf.length == 0)
			buf = null;
		if (null == b)
			throw new NullPointerException();
		else if (off < 0 || len < 0 || len > b.length - off)
			throw new IndexOutOfBoundsException();
		int base = 0;
		if (null != buf) {
			int alreadyRead = 0;
			if (len > buf.length) alreadyRead = buf.length;
			else alreadyRead = len;

			System.arraycopy(buf, 0, b, off, alreadyRead);
			buf = newArray(buf, alreadyRead, buf.length);

			base = alreadyRead;
			len -= alreadyRead;
			if (len == 0) {
				return base;
			}
		}
		int read = stream.read(b, off + base, len);
		if (read == -1) {
			isReadComplete = true;
			return base == 0 ?-1: base;
		} else {
			return read + base;
		}
	}




	/*
	 * Get Next Line(Separator)
	 * 获取下一行(Buffered)

	 * new this(new ByteArrayInputStream("abc\n123\n+-*".getBytes())).readLine(); >> "abc\n" "123\n" "+-*"
	 * new this(new ByteArrayInputStream("abc\n123\n".getBytes())).readLine(); >> "abc\n" "123\n"

	 * byte[]{13, 10, 13, 10} >> \r\n\r\n
	 */
	private int rLBufSize = XStream.defaultStreamCharArrBuffSize;
	private char[] rLrArray = null;//缓存
	private XCharArrayWriter rLReturnTmp = new XCharArrayWriter();
	private boolean isReadComplete = false;
	private boolean isReadSeparator = false;
	@Override
	public char[] readLineDefaultSeparator() {
		return Chars_NextLineN;
	}
	@Override
	public char[] readLine() throws IOException {
		return readLine(Chars_NextLineN);
	}
	@Override
	public char[] readLine(char[] rLSplit) throws IOException {
		return readLine(rLSplit, true);
	}
	@Override
	@XAnnotations("this will buffered data until read to separator")
	public  char[] readLine(char[] rLSplit, boolean resultAddSplitChar) throws IOException {
		if (null != buf && buf.length == 0)
			buf = null;
		isReadComplete = false;
		isReadSeparator = false;
		int i = 0;//累计
		int start = -1;
		int read = -1;
		if (null != buf) {
			rLReturnTmp.write(buf, 0, buf.length);
			buf = null;
		}
		if ((start = rLReturnTmp.indexOfBuff(rLSplit, 0, rLReturnTmp.size())) == -1) {
			while (true) {
				if ((read = stream.read(rLrArray)) == -1) {
					isReadComplete = true;
					break;
				}	
				rLReturnTmp.write(rLrArray, 0, read);
				if ((start = rLReturnTmp.indexOfBuff(rLSplit, i - rLSplit.length + 1)) != -1)
					break;
				i += read;
			}
		}
		if (start > -1) {
			char[] Array = rLReturnTmp.getBuff();
			int ArraySize = rLReturnTmp.size();
			if (resultAddSplitChar)
				rLReturnTmp.setBuffSize(start + rLSplit.length);
			else 
				rLReturnTmp.setBuffSize(start);
			buf = newArray(Array, start + rLSplit.length, ArraySize);	
			isReadSeparator = true;

			if (rLReturnTmp.size() == 0 && !resultAddSplitChar && null != buf) {
				rLReturnTmp.releaseBuffer();
				return nullChars;
			}
		}
		char Array[] = rLReturnTmp.toCharArray();
		rLReturnTmp.releaseBuffer();
		return (null != Array && Array.length == 0) ?null: Array;
	}

	@XAnnotations("last read stream result equals -1")
	public  boolean isReadComplete() {
		return isReadComplete;
	}
	@Override
	public boolean isReadLineReadToSeparator() {
		return isReadSeparator;
	}

	
	
	
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		buf = null;
	}
	@Override
	public char[] getBuff() {
		if (null != buf && buf.length == 0)
			buf = null;
		return buf;
	}
	@Override
	public int getBuffSize() {
		return null == buf ?0: buf.length;
	}
	@Override
	public void setBuff(char[] newBuff, int size) {
		// TODO: Implement this method
		this.buf = null == newBuff ?null: (newBuff.length == 0 ?null: newBuff);
		this.setBuffSize(size);
	}
	@Override
	public void setBuffSize(int size) {
		// TODO: Implement this method
		if (size <= 0) {
			buf = null;
			return;
		}
		if (size == (null == buf ?0: buf.length)) {
			return;
		}
		buf = Arrays.copyOf(null == buf ?XStaticFixedValue.nullcharArray: buf, size);
	}









	@Override
	public T getStream() {
		return stream;
	}

	protected static char[] newArray(char[] array, int start, int stop) {
		if (stop - start < 0 || start < 0 || stop < 0 || start > array.length || stop > array.length)
			return null;
		if (stop - start < 1)
			return XCharArrayReader.nullChars;
		return Arrays.copyOfRange(array, start, stop);
	}
}

