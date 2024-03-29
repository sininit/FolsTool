package top.fols.atri.io.file;

import top.fols.atri.interfaces.annotations.Help;
import top.fols.atri.interfaces.interfaces.IInnerFile;
import top.fols.atri.lang.Finals;

import java.io.*;

@Help("random access file mode r")
public class RandomAccessFileInputStream extends InputStream implements IInnerFile {
	private final File file;
	private RandomAccessFile innerStream;

	private long index = 0;
	private long reOff = 0;
	private boolean closed = false;


	public static final String MODE_R = Finals.FileOptMode.r();

	public RandomAccessFileInputStream(String file) throws FileNotFoundException, IOException {
		this(new File(file));
	}
	public RandomAccessFileInputStream(File file) throws FileNotFoundException, IOException {
		this(file, 0);
	}


	public RandomAccessFileInputStream(String file, long startIndex) throws FileNotFoundException, IOException {
		this(new File(file), startIndex);
	}
	public RandomAccessFileInputStream(File file, long startIndex) throws FileNotFoundException, IOException {
		this(file, MODE_R, startIndex);
	}

	public RandomAccessFileInputStream(File file, String mode, long startIndex) throws java.io.FileNotFoundException, IOException  {
		this.file = file;
		this.innerStream = new RandomAccessFile(file, mode);
		this.index = 0;
		this.reOff = 0;
		this.seekIndex(startIndex);
	}
	void requestOpen() throws IOException{
		if (this.closed){
			throw new IOException("closed");
		}
	}

	@Override
	public File innerFile() {
		return this.file;
	}


	@Override
	public int read() throws IOException {
		this.requestOpen();
		int read = this.innerStream.read();
		if (read != -1) {
			this.index++;
		}
		return read;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		this.requestOpen();
		if (off + len > b.length) {
			throw new ArrayIndexOutOfBoundsException(String.format("arrlen=%s, off=%s, len=%s", b.length, off, len));
		}
		int read = this.innerStream.read(b, off, len);
		if (read != -1) {
			this.index += read;
		}
		return read;
	}

	@Override
	public int available() throws IOException {
		this.requestOpen();
		long length = this.length();
		long available = length - this.index;
		return available > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) available;
	}

	@Override
	public void close() throws IOException {
		if (!this.closed){
			this.closed = true;
			this.innerStream.close();
		}
	}
	public boolean isClosed() {
		return this.closed;
	}


	@Override
	public long skip(long n) throws IOException {
		this.requestOpen();
		if (n == 0) {
			return 0;
		}
		if (n < 0) {
			throw new IOException("skip bytes lengths error:" + n);
		}
		long length = this.length();
		if (n + this.index > length) {
			n = length - this.index;
		}
		this.index += n;
		this.innerStream.seek(this.index);
		return n;
	}

	/**
	 * 这里的readlimit无意义 mark就像书签一样，用于标记，以后再调用reset时就可以再回到这个mark过的地方。
	 * mark方法有个参数，通过这个整型参数，你告诉系统，希望在读出这么多个字符之前，这个mark保持有效。 比如说
	 * :mark(10)，那么在read()10个以内的字符时，reset（）操作后可以重新读取已经读出的数据
	 * ，如果已经读取的数据超过10个，那reset()操作后，就不能正确读取以前的数据了，因为此时mark标记已经失效。
	 */
	@Override
	public synchronized void mark(int readlimit) {
		this.reOff = index;
	}

	@Override
	public void reset() throws IOException {
		this.seekIndex(this.reOff);
	}

	@Override
	public boolean markSupported() {
		return true;
	}


	public long length() {
		return this.innerFile().length();
	}

	public long getIndex() {
		return this.index;
	}
	public void seekIndex(long offset) throws IOException {
		this.requestOpen();
		if (offset < 0) {
			throw new RuntimeException("offset=" + offset + ", min=0");
		}
		this.innerStream.seek(offset);
		this.index = offset;
	}

}
