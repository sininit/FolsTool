package top.fols.box.io.base;

import java.io.IOException;
import java.io.Reader;
import top.fols.box.io.interfaces.XInterfaceLineReaderStream;
import top.fols.box.io.interfaces.XInterfacePrivateBuffOperat;
import top.fols.box.io.interfaces.XInterfacePrivateCharArrayBuffSearchOperat;
import top.fols.box.io.interfaces.XInterfacePrivateFixedStreamIndexOperat;
import top.fols.box.io.interfaces.XInterfereReleaseBufferable;
import top.fols.box.util.XArray;
import top.fols.box.util.XArrays;

public class XStringReader extends Reader implements  XInterfacePrivateBuffOperat<String>,XInterfaceLineReaderStream<char[]>,XInterfacePrivateCharArrayBuffSearchOperat,XInterfacePrivateFixedStreamIndexOperat,XInterfereReleaseBufferable {
    private String buf;
    private int count;
    private int pos = 0;
    private int mark = 0;

    /**
     * Creates a new string reader.
     *
     * @param s  String providing the character stream.
     */
    public XStringReader(String s) {
        this.buf = s;
        this.count = s.length();
    }

    /** Check to make sure that the stream has not been closed */
    private void ensureOpen() throws IOException {
        if (null == buf)
            throw new IOException("Stream closed");
    }

    /**
     * Reads a single character.
     *
     * @return     The character read, or -1 if the end of the stream has been
     *             reached
     *
     * @exception  IOException  If an I/O error occurs
     */
	@Override
    public int read() throws IOException {
        ensureOpen();
		if (pos >= count)
			return -1;
		return buf.charAt(pos++);
	}
    /**
     * Reads characters into a portion of an array.
     *
     * @param      cbuf  Destination buffer
     * @param      off   Offset at which to start writing characters
     * @param      len   Maximum number of characters to read
     *
     * @return     The number of characters read, or -1 if the end of the
     *             stream has been reached
     *
     * @exception  IOException  If an I/O error occurs
     * @exception  IndexOutOfBoundsException {@inheritDoc}
     */
	@Override
    public int read(char cbuf[], int off, int len) throws IOException {
		ensureOpen();
		if ((off < 0) || (off > cbuf.length) || (len < 0) ||
			((off + len) > cbuf.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}
		if (pos >= count)
			return -1;
		int n = Math.min(count - pos, len);
		buf.getChars(pos, pos + n, cbuf, off);
		pos += n;
		return n;
    }

    /**
     * Skips the specified number of characters in the stream. Returns
     * the number of characters that were skipped.
     *
     * <p>The <code>ns</code> parameter may be negative, even though the
     * <code>skip</code> method of the {@link Reader} superclass throws
     * an exception in this case. Negative values of <code>ns</code> cause the
     * stream to skip backwards. Negative return values indicate a skip
     * backwards. It is not possible to skip backwards past the beginning of
     * the string.
     *
     * <p>If the entire string has been read or skipped, then this method has
     * no effect and always returns 0.
     *
     * @exception  IOException  If an I/O error occurs
     */
	@Override
    public long skip(long ns) throws IOException {
		ensureOpen();
		if (pos >= count)
			return 0;
		// Bound skip by beginning and end of the source
		long n = Math.min(count - pos, ns);
		n = Math.max(-pos, n);
		pos += n;
		return n;
	}
    /**
     * Tells whether this stream is ready to be read.
     *
     * @return True if the next read() is guaranteed not to block for input
     *
     * @exception  IOException  If the stream is closed
     */
	@Override
    public boolean ready() throws IOException {
		ensureOpen();
		return true;
    }

    /**
     * Tells whether this stream supports the mark() operation, which it does.
     */
	@Override
    public boolean markSupported() {
        return true;
    }

    /**
     * Marks the present position in the stream.  Subsequent calls to reset()
     * will reposition the stream to this point.
     *
     * @param  readAheadLimit  Limit on the number of characters that may be
     *                         read while still preserving the mark.  Because
     *                         the stream's input comes from a string, there
     *                         is no actual limit, so this argument must not
     *                         be negative, but is otherwise ignored.
     *
     * @exception  IllegalArgumentException  If {@code readAheadLimit < 0}
     * @exception  IOException  If an I/O error occurs
     */
	@Override
    public void mark(int readAheadLimit) throws IOException {
        if (readAheadLimit < 0) {
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
		ensureOpen();
		mark = pos;
	}


    /**
     * Resets the stream to the most recent mark, or to the beginning of the
     * string if it has never been marked.
     *
     * @exception  IOException  If an I/O error occurs
     */
	@Override
    public void reset() throws IOException {
		ensureOpen();
		pos = mark;
    }

    /**
     * Closes the stream and releases any system resources associated with
     * it. Once the stream has been closed, further read(),
     * ready(), mark(), or reset() invocations will throw an IOException.
     * Closing a previously closed stream has no effect. This method will block
     * while there is another thread blocking on the reader.
     */
	@Override
    public void close() {
		buf = null;
    }










	@Override
	public char[] readLineDefaultSeparator() {
		return Chars_NextLineN;
	}
	private boolean isReadSeparator = false;
	@Override
	public char[] readLine() {
		return readLine(Chars_NextLineN, true);
	}
	@Override
	public char[] readLine(char[] splitChar) {
		return readLine(splitChar, true);
	}
	@Override
	public char[] readLine(char[] split, boolean resultAddSplitChar) {
		isReadSeparator = false;
		if (pos >= count)
			return null;
		int index = XArrays.CharSequenceUtil.indexOf(buf, split, pos, count);
		if (index == -1) {
			char[] newArray = new char[count - pos];
			XArray.arraycopy(buf, pos, newArray, 0, newArray.length);
			pos = count;
			return newArray;
		} else {
			isReadSeparator  = true;
			if (resultAddSplitChar) {
				if (index - pos + split.length < 1) {
					pos = index + split.length;
					return null;
				}	
				char[] newArray = new char[index - pos + split.length];
				XArray.arraycopy(buf, pos, newArray, 0, newArray.length);

				pos = index + split.length;
				return newArray;
			} else {
				if (index - pos <  1) {
					pos = index + split.length;
					return nullChars;
				}	
				char[] newArray = new char[index - pos];
				XArray.arraycopy(buf, pos, newArray, 0, newArray.length);

				pos = index + split.length;
				return newArray;
			}
		}
	}


	@Override
	public boolean isReadLineReadToSeparator() {
		return isReadSeparator;
	}


	public int size() {
		return null == buf ?0: buf.length();
	}


	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		setBuff(null, 0);
	}
	@Override
	public String getBuff() {
		return buf;
	}
	@Override
	public int getBuffSize() {
		// TODO: Implement this method
		return buf.length();
	}
	@Override
	public void setBuff(String newBuff, int size) {
		// TODO: Implement this method
		this.buf = null == newBuff ?"": newBuff;
		this.setBuffSize(size);
	}
	@Override
	public  void setBuffSize(int size) throws ArrayIndexOutOfBoundsException {
		if (size > buf.length())
			throw new ArrayIndexOutOfBoundsException("arrayLen=" + buf.length() + ", setLen=" + size);
		count = size;
	}


	@Override
	public void seekIndex(int index) {
		if (!(index > -1 && index <= count))
			throw new ArrayIndexOutOfBoundsException("can't set pos index=" + index + " length=" + count);
		pos = index;
	}
	@Override
	public int getIndex() {
		return pos;
	}
	
	
	
	
	@Override
	public int indexOfBuff(char b, int start) {
		return XArrays.CharSequenceUtil.indexOf(buf, b, start, size());
	}
	@Override
	public int indexOfBuff(char[] b, int start) {
		return XArrays.CharSequenceUtil.indexOf(buf, b, start, size());
	}
	@Override
	public int indexOfBuff(char b, int start, int end) {
		return XArrays.CharSequenceUtil.indexOf(buf, b, start, end);
	}
	@Override
	public int indexOfBuff(char[] b, int start, int end) {
		return XArrays.CharSequenceUtil.indexOf(buf, b, start, end);
	}
	@Override
	public int lastIndexOfBuff(char b, int start) {
		return XArrays.CharSequenceUtil.lastIndexOf(buf, b, 0, start);
	}
	@Override
	public int lastIndexOfBuff(char[] b, int start) {
		return XArrays.CharSequenceUtil.lastIndexOf(buf, b, 0, start);
	}
	@Override
	public int lastIndexOfBuff(char b, int startIndex, int indexRange) {
		return XArrays.CharSequenceUtil.lastIndexOf(buf, b, startIndex, indexRange);
	}
	@Override
	public int lastIndexOfBuff(char[] b, int startIndex, int indexRange) {
		return XArrays.CharSequenceUtil.lastIndexOf(buf, b, startIndex, indexRange);
	}

}


