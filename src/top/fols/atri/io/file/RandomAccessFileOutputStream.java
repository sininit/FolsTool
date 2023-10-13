package top.fols.atri.io.file;

import top.fols.atri.interfaces.interfaces.IInnerFile;
import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.annotations.Help;

import java.io.*;

@Help("random access file mode rw, default seek to file end index")
public class RandomAccessFileOutputStream extends OutputStream implements IInnerFile {
	private final File file;
	private RandomAccessFile innerStream;

	private long index;
	private boolean closed = false;


	public static final String MODE_RW = Finals.FileOptMode.rw();

	public RandomAccessFileOutputStream(String file) throws FileNotFoundException, IOException {
		this(new File(file));
	}
	public RandomAccessFileOutputStream(File file) throws FileNotFoundException, IOException {
		this(file, file.length());
	}


	public RandomAccessFileOutputStream(String file, long startIndex) throws FileNotFoundException, IOException {
		this(new File(file), startIndex);
	}
	public RandomAccessFileOutputStream(File file, long startIndex) throws FileNotFoundException, IOException {
		this(file, MODE_RW, startIndex);
	}


	public RandomAccessFileOutputStream(File file, String mode, long startIndex) throws FileNotFoundException, IOException {
		this.file = file;
		this.innerStream = new RandomAccessFile(file, mode);
		this.index = 0;
		this.seekIndex(startIndex);
	}
	void requestOpen() throws IOException{
		if (this.closed) {
			throw new IOException("closed");
		}
	}

	@Override
	public File innerFile() {
		return this.file;
	}


	@Override
	public void write(int p1) throws IOException {
		this.requestOpen();
		this.innerStream.write(p1);
		this.index++;
	}
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		this.requestOpen();
		if (off + len > b.length) {
			throw new ArrayIndexOutOfBoundsException(String.format("arrlen=%s, off=%s, len=%s", b.length, off, len));
		}
		if (len > -1) {
			this.innerStream.write(b, off, len);
			this.index += len;
		}
	}

	@Override
	public void flush() {}

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


	public long length() {
		return this.innerFile().length();
	}
	public void setLength(long newLength) throws IOException {
		this.requestOpen();
		this.innerStream.setLength(newLength);
		this.index = this.innerStream.getFilePointer();
	}

	public RandomAccessFileOutputStream clear() throws IOException {
		this.setLength(0);
		return this;
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
