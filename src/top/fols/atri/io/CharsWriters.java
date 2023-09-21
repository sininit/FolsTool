package top.fols.atri.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.box.io.interfaces.IPrivateBuffOperat;
import top.fols.box.io.interfaces.IPrivateStreamIndexOperat;


public class CharsWriters extends Writer
		implements IPrivateBuffOperat<char[]>,
		IPrivateStreamIndexOperat, IReleasable {

	private char[] buf;

	private int count;

	public CharsWriters() {
		this(32);
	}

	public CharsWriters(int initialSize) {
		if (initialSize < 0) {
			throw new IllegalArgumentException("Negative initial size: " + initialSize);
		}
		buf = new char[initialSize];
	}

	public char[] trimToSizeAndGetBuffer() {
		if (count == buf.length) {
			return   buf;
		}
		return buf = (count == 0)
				? Finals.EMPTY_CHAR_ARRAY
				: Arrays.copyOf(buf, count);
	}

	@Override
	public void write(int c) {
		int newcount = count + 1;
		// 如果buf存满、则将buf容量扩大1倍、并将原来buf中count字符copy到新的buf中
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		buf[count] = (char) c;
		count = newcount;
	}

    @Override
    public void write(char[] cbuf) {
        this.write(cbuf, 0, cbuf.length);
    }

	@Override
	public void write(char[] c, int off, int len) {
		if ((off < 0) || (off > c.length) || (len < 0) || ((off + len) > c.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		int newcount = count + len;
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		System.arraycopy(c, off, buf, count, len);
		count = newcount;
	}

	@Override
	public void write(String str, int off, int len) {
		int newcount = count + len;
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		str.getChars(off, off + len, buf, count);
		count = newcount;
	}

	@Override
	public void write(String str) {
		write(str, 0, str.length());
	}

	public void writeTo(Writer out) throws IOException {
		out.write(buf, 0, count);
	}

	@Override
	public Writer append(CharSequence csq) {
		String s = (null == csq ? "null" : csq.toString());
		write(s, 0, s.length());
		return this;
	}

	@Override
	public Writer append(CharSequence csq, int start, int end) {
		String s = (null == csq ? "null" : csq).subSequence(start, end).toString();
		write(s, 0, s.length());
		return this;
	}

	@Override
	public Writer append(char c) {
		write(c);
		return this;
	}

	public void reset() {
		count = 0;
	}

	public char[] toCharArray() {
		if (count == 0)
			return Finals.EMPTY_CHAR_ARRAY;
		return Arrays.copyOf(buf, count);
	}

	public int size() {
		return count;
	}

	@Override
	public String toString() {
		return new String(buf, 0, count);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() {
		this.release();
	}



	@Override
	public char[] buffer() {
		return buf;
	}

	@Override
	public int buffer_length() {
		// TODO: Implement this method
		return buf.length;
	}

	@Override
	public void buffer(char[] newBuff, int size) {
		// TODO: Implement this method
		this.buf = null == newBuff ? Finals.EMPTY_CHAR_ARRAY : newBuff;
		this.buffer_length(size);
	}

	@Override
	public void buffer_length(int size) throws ArrayIndexOutOfBoundsException {
		if (size > buf.length) {
			throw new ArrayIndexOutOfBoundsException("arrayLen=" + buf.length + ", setLen=" + size);
		}
		count = size;
	}

	@Override
	public void seekIndex(int index) {
		buffer_length(index);
	}

	@Override
	public int getIndex() {
		return count;
	}





	@Override
	public boolean release() {
		buf = Finals.EMPTY_CHAR_ARRAY;
		count = 0;
		return true;
	}

	@Override
	public boolean released() {
		return null == buf || buf==Finals.EMPTY_CHAR_ARRAY;
	}
}
