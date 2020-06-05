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
	private long index;
	private final RandomAccessFile stream;

	public XRandomAccessFileOutputStream(String file, String mode) throws FileNotFoundException, IOException {
		this(new RandomAccessFile(file, mode));
	}

	public XRandomAccessFileOutputStream(File file, String mode) throws FileNotFoundException, IOException {
		this(new RandomAccessFile(file, mode));
	}

	public XRandomAccessFileOutputStream(String file) throws FileNotFoundException, IOException {
		this(new RandomAccessFile(file, XStaticFixedValue.FileOptMode.rw()));
	}

	public XRandomAccessFileOutputStream(File file) throws FileNotFoundException, IOException {
		this(new RandomAccessFile(file, XStaticFixedValue.FileOptMode.rw()));
	}

	public XRandomAccessFileOutputStream(RandomAccessFile f) throws IOException {
		this(f, f.length());
	}

	public XRandomAccessFileOutputStream(RandomAccessFile f, long startIndex) throws IOException {
		this.stream = f;
		this.seekIndex(startIndex);
	}


	public void write(int p1) throws IOException {
		this.stream.write(p1);
		index++;
	}

	public void write(byte[] b, int off, int len) throws IOException {
		if (len > -1) {
			this.stream.write(b, off, len);
			index += len;
		}
	}

	public void flush() {
		return;
	}

	public void close() throws IOException {
		this.stream.close();
	}

	public long getIndex() {
		return this.index;
	}

	public void seekIndex(long offset) throws IOException {
		if (offset < 0) {
			throw new RuntimeException("offset=" + offset + ", min=0");
		}
		this.stream.seek(offset);
		this.index = offset;
	}

	public long length() throws IOException {
		return this.stream.length();
	}

	public void setLength(long newLength) throws IOException {
		this.stream.setLength(newLength);
		if (this.index > newLength) {
			this.index = newLength;
		}
	}

	public XRandomAccessFileOutputStream empty() throws IOException {
		this.setLength(0);
		this.seekIndex(0);
		return this;
	}
}
