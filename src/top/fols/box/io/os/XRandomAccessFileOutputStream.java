package top.fols.box.io.os;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.abstracts.XAbstractRandomAccessOutputStream;

@XAnnotations("random access file mode rw, default seek to file end index")
public class XRandomAccessFileOutputStream extends XAbstractRandomAccessOutputStream {
	private final RandomAccessFile innerStream;
	private File file;
	private long index;
	private boolean closed = false;


	public static final String MODE_RW = XStaticFixedValue.FileOptMode.rw();

	public XRandomAccessFileOutputStream(String file) throws FileNotFoundException, IOException {
		this(new File(file));
	}
	public XRandomAccessFileOutputStream(File file) throws FileNotFoundException, IOException {
		this(file, file.length());
	}


	public XRandomAccessFileOutputStream(String file, long startIndex) throws FileNotFoundException, IOException {
		this(new File(file), startIndex);
	}
	public XRandomAccessFileOutputStream(File file, long startIndex) throws FileNotFoundException, IOException {
		this(file, MODE_RW, startIndex);
	}


	public XRandomAccessFileOutputStream(File file,  String mode, long startIndex) throws FileNotFoundException, IOException {
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


	public File getFile() {
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
	public void flush() {
		return;
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
	public long getIndex() {
		return this.index;
	}


	@Override
	public long length() {
		return this.getFile().length();
	}

	@Override
	public void setLength(long newLength) throws IOException {
		this.requestOpen();
		this.innerStream.setLength(newLength);
		this.index = this.innerStream.getFilePointer();
	}

	public XRandomAccessFileOutputStream empty() throws IOException {
		this.setLength(0);
		return this;
	}




	@Override
	public void seekIndex(long offset) throws IOException {
		this.requestOpen();
		if (offset < 0) {
			throw new RuntimeException("offset=" + offset + ", min=0");
		}
		this.innerStream.seek(offset);
		this.index = offset;
	}




}
