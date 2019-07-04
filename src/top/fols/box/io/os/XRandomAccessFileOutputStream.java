package top.fols.box.io.os;
import java.io.IOException;
import java.io.RandomAccessFile;
import top.fols.box.io.interfaces.XInterfaceRandomAccessOutputStream;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.annotation.XAnnotations;

@XAnnotations("RandomAccessFile mode rw")
public class XRandomAccessFileOutputStream extends XInterfaceRandomAccessOutputStream {
	private long index;
	private final RandomAccessFile stream;
	public XRandomAccessFileOutputStream(java.lang.String file, java.lang.String mode) throws java.io.FileNotFoundException, IOException {
		this(new RandomAccessFile(file, mode));
	}
    public XRandomAccessFileOutputStream(java.io.File file, java.lang.String mode) throws java.io.FileNotFoundException, IOException {
		this(new RandomAccessFile(file, mode));
	}
	public XRandomAccessFileOutputStream(java.lang.String file) throws java.io.FileNotFoundException, IOException {
		this(new RandomAccessFile(file, XStaticFixedValue.FileOptMode.rw()));
	}
    public XRandomAccessFileOutputStream(java.io.File file) throws java.io.FileNotFoundException, IOException {
		this(new RandomAccessFile(file, XStaticFixedValue.FileOptMode.rw()));
	}
	public XRandomAccessFileOutputStream(RandomAccessFile f) throws IOException {
		this(f, f.length());
	}
	public XRandomAccessFileOutputStream(RandomAccessFile f, long off) throws IOException {
		this.stream = f;
		this.stream.seek(off);
		this.index = 0;
	}


	public void write(int p1) throws java.io.IOException {
		stream.write(p1);
		index++;
	}
    public void write(byte[] b, int off, int len) throws java.io.IOException {
		if (len > -1) {
			stream.write(b, off, len);
			index += len;
		}
	}
    public void flush() {
		//无意义
	}
    public void close() throws java.io.IOException {
		stream.close();
	}
	public long getIndex() {
		return index;
	}
	public void seekIndex(long offset) throws java.io.IOException {
		if (offset < 0)
			throw new RuntimeException("offset=" + offset + ", min=0");
		stream.seek(offset);
		index = offset;
	}
	public long length() throws IOException {
		return stream.length();
	}

	public void setLength(long newLength) throws java.io.IOException {
		stream.setLength(newLength);
	}
}
