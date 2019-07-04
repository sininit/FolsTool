package top.fols.box.io.base;
import java.io.Writer;
import top.fols.box.io.interfaces.XInterfacePrivateBuffOperat;
import top.fols.box.io.interfaces.XInterfereReleaseBufferable;

public class XStringWriter extends Writer implements XInterfacePrivateBuffOperat<StringBuilder>,XInterfereReleaseBufferable {
    private StringBuilder buf;
    public XStringWriter() {
        buf = new StringBuilder();
        lock = buf;
    }
    public XStringWriter(int initialSize) {
        if (initialSize < 0) {
            throw new IllegalArgumentException("negative buffer size");
        }
        buf = new StringBuilder(initialSize);
        lock = buf;
    }
	@Override
    public void write(int c) {
        buf.append((char) c);
    }
	@Override
    public void write(char cbuf[], int off, int len) {
        if ((off < 0) || (off > cbuf.length) || (len < 0) ||
            ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        buf.append(cbuf, off, len);
    }
	@Override
    public void write(String str) {
        buf.append(str);
    }
	@Override
    public void write(String str, int off, int len) {
        buf.append(str, off, off + len);
    }
	@Override
    public XStringWriter append(CharSequence csq) {
        write(String.valueOf(csq));
        return this;
    }
	@Override
    public XStringWriter append(CharSequence csq, int start, int end) {
	  	if (null == csq) csq = "null";
        return append(csq.subSequence(start, end));
    }
	@Override
    public XStringWriter append(char c) {
        write(c);
        return this;
    }
	@Override
    public String toString() {
        return buf.toString();
    }
    public StringBuilder getBuffer() {
        return buf;
    }
	@Override
    public void flush() {
		return;
    }
	@Override
    public void close() {
		return;
    }
	public int length(){
		return buf.length();
	}
	
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		setBuff(null,0);
	}
	@Override
	public StringBuilder getBuff() {
		return buf;
	}
	@Override
	public int getBuffSize() {
		// TODO: Implement this method
		return buf.length();
	}
	@Override
	public void setBuff(StringBuilder newBuff, int size) {
		// TODO: Implement this method
		this.buf = null == newBuff ?new StringBuilder(): newBuff;
		this.setBuffSize(size);
	}
	@Override
	public  void setBuffSize(int size) throws ArrayIndexOutOfBoundsException {
		if (size > buf.length())
			throw new ArrayIndexOutOfBoundsException("arrayLen=" + buf.length() + ", setLen=" + size);
		this.buf.setLength(size);
	}
}

