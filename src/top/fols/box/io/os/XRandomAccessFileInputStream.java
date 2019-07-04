package top.fols.box.io.os;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.interfaces.XInterfaceRandomAccessInputStream;
import top.fols.box.statics.XStaticFixedValue;

@XAnnotations("RandomAccessFile mode r")
public class XRandomAccessFileInputStream extends XInterfaceRandomAccessInputStream {
	@Override
	public int hashCode() {
		// TODO: Implement this method
		return stream.hashCode();
	}
	@Override
	public String toString() {
		// TODO: Implement this method
		return stream.toString();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		return stream.equals(obj);
	}
	private final RandomAccessFile stream;
	private long index = 0;
	private long reOff = 0;
	public XRandomAccessFileInputStream(String file, String mode) throws java.io.FileNotFoundException, IOException {
		this(new RandomAccessFile(file, mode));
	}
    public XRandomAccessFileInputStream(File file, String mode) throws java.io.FileNotFoundException, IOException {
		this(new RandomAccessFile(file, mode));
	}
	public XRandomAccessFileInputStream(String file) throws java.io.FileNotFoundException, IOException {
		this(new RandomAccessFile(file, XStaticFixedValue.FileOptMode.r()));
	}
    public XRandomAccessFileInputStream(File file) throws java.io.FileNotFoundException, IOException {
		this(new RandomAccessFile(file, XStaticFixedValue.FileOptMode.r()));
	}
	public XRandomAccessFileInputStream(RandomAccessFile f) {
		this.stream = f;
		this.index = 0;
		this.reOff = 0;
	}
	@Override
	public int read() throws IOException {
		int read = stream.read();
		if (read != -1)
			index++;
		return read;
	}
	public int read(byte[] b, int off, int len) throws java.io.IOException {
		int read = stream.read(b, off, len);
		if (read != -1)
			index += read;
		return read;
	}
	public int available() throws IOException {
		long length = length();
		long available = length - index;
		return available > Integer.MAX_VALUE ?Integer.MAX_VALUE: (int)available; 
	}	
	public void close() throws java.io.IOException {
		stream.close();
	}
	public long skip(long n) throws java.io.IOException {
		if (n == 0)
			return 0;
		if (n < 0)
			throw new IOException("skip bytes lengths error:" + n);
		long length = length();
		if (n + index > length)
			n = length - index;
		index += n;
		stream.seek(index);
		return n;
	}
	//这里的readlimit无意义
	/*
	 mark就像书签一样，用于标记，以后再调用reset时就可以再回到这个mark过的地方。
	 mark方法有个参数，通过这个整型参数，你告诉系统，希望在读出这么多个字符之前，这个mark保持有效。
	 比如说
	 :mark(10)，那么在read()10个以内的字符时，reset（）操作后可以重新读取已经读出的数据
	 ，如果已经读取的数据超过10个，那reset()操作后，就不能正确读取以前的数据了，因为此时mark标记已经失效。     
	 */
	public synchronized void mark(int readlimit) {
		reOff = index;
	}
	public void reset() throws java.io.IOException {
		seekIndex(reOff);
	}
	public boolean markSupported() {
		return true;
	}



	public long getIndex() {
		return index;
	}
	public long length() throws IOException {
		return stream.length();
	}
	public void seekIndex(long offset) throws java.io.IOException {
		if (offset < 0)
			throw new RuntimeException("offset=" + offset + ", min=0");
		stream.seek(offset);
		index = offset;
	}

}
