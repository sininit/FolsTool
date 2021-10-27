package top.fols.box.io.base;

import java.io.IOException;
import java.io.Reader;

import top.fols.atri.lang.Finals;
import top.fols.box.io.interfaces.XInterfaceLineReaderStream;
import top.fols.box.io.interfaces.XInterfacePrivateBuffOperat;
import top.fols.box.io.interfaces.XInterfacePrivateCharArrayBuffSearchOperat;
import top.fols.box.io.interfaces.XInterfacePrivateFixedStreamIndexOperat;
import top.fols.box.io.interfaces.XInterfaceReleaseBufferable;
import top.fols.box.util.XArrays;

public class XStringReader extends Reader implements XInterfacePrivateBuffOperat<String>,
XInterfaceLineReaderStream<char[]>, XInterfacePrivateCharArrayBuffSearchOperat,
XInterfacePrivateFixedStreamIndexOperat, XInterfaceReleaseBufferable {
	private String buf;
	private int count;
	private int pos = 0;
	private int mark = 0;

	/**
	 * Creates a new string reader.
	 *
	 * @param s String providing the character stream.
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
	 * @return The character read, or -1 if the end of the stream has been reached
	 *
	 * @exception IOException If an I/O error occurs
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
	 * @param cbuf Destination buffer
	 * @param off  Offset at which to start writing characters
	 * @param len  Maximum number of characters to read
	 *
	 * @return The number of characters read, or -1 if the end of the stream has
	 *         been reached
	 *
	 * @exception IOException               If an I/O error occurs
	 * @exception IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public int read(char cbuf[], int off, int len) throws IOException {
		ensureOpen();
		if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
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
	 * Skips the specified number of characters in the stream. Returns the number of
	 * characters that were skipped.
	 *
	 * <p>
	 * The <code>ns</code> parameter may be negative, even though the
	 * <code>skip</code> method of the {@link Reader} superclass throws an exception
	 * in this case. Negative values of <code>ns</code> cause the stream to skip
	 * backwards. Negative return values indicate a skip backwards. It is not
	 * possible to skip backwards past the beginning of the string.
	 *
	 * <p>
	 * If the entire string has been read or skipped, then this method has no effect
	 * and always returns 0.
	 *
	 * @exception IOException If an I/O error occurs
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
	 * @exception IOException If the stream is closed
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
	 * Marks the present position in the stream. Subsequent calls to reset() will
	 * reposition the stream to this point.
	 *
	 * @param readAheadLimit Limit on the number of characters that may be read
	 *                       while still preserving the mark. Because the stream's
	 *                       input comes from a string, there is no actual limit, so
	 *                       this argument must not be negative, but is otherwise
	 *                       ignored.
	 *
	 * @exception IllegalArgumentException If {@code readAheadLimit < 0}
	 * @exception IOException              If an I/O error occurs
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
	 * Resets the stream to the most recent mark, or to the beginning of the string
	 * if it has never been marked.
	 *
	 * @exception IOException If an I/O error occurs
	 */
	@Override
	public void reset() throws IOException {
		ensureOpen();
		pos = mark;
	}

	/**
	 * Closes the stream and releases any system resources associated with it. Once
	 * the stream has been closed, further read(), ready(), mark(), or reset()
	 * invocations will throw an IOException. Closing a previously closed stream has
	 * no effect. This method will block while there is another thread blocking on
	 * the reader.
	 */
	@Override
	public void close() {
		buf = null;
	}
	
	@Override
	public char[] readLine(char[] splitChar) {
		return readLine(splitChar, true);
	}

	private int readLineDefaultSeparatorIndex = -1;

	/**
	 * Get Next Line
	 * 
	 * @param separators         lineSeparator
	 * @param resultAddSeparator if read to separator, add return or not?
	 * 
	 * @return if the content is not read, null will be returned
	 */
	@Override
	public char[] readLine(char[] separators, boolean resultAddSeparator) {
		this.readLineDefaultSeparatorIndex = -1;
		if (this.pos >= this.count) {
			return null;
		}

		int findindex = -1;
		char[] split = Finals.EMPTY_CHAR_ARRAY;
		int search = XArrays.CharSequenceUtil.indexOf(buf, separators, this.pos, this.count);
		if (search != -1 && (findindex == -1 || search < findindex)) {
			this.readLineDefaultSeparatorIndex = 0;
			findindex = search;
			split = separators;
		}

		if (findindex == -1) {
			char[] newArray = new char[this.count - this.pos];
			XArrays.CharSequenceUtil.arraycopyTraverse(buf, this.pos, newArray, 0, newArray.length);
			this.pos = this.count;
			return newArray;
		} else {
			int arrlen = findindex - this.pos;
			if (resultAddSeparator) {
				arrlen += split.length;
			}
			char[] newArray = new char[arrlen];
			XArrays.CharSequenceUtil.arraycopyTraverse(buf, this.pos, newArray, 0, newArray.length);

			this.pos = findindex + split.length;
			return newArray;
		}
	}

	@Override
	public char[] readLine(char[][] separators, boolean resultAddSeparator) {
		this.readLineDefaultSeparatorIndex = -1;
		if (this.pos >= this.count) {
			return null;
		}

		int endIndex = this.buf.length();
		int offIndex = this.pos;
		CharSequence data = this.buf;

		int len = endIndex - offIndex;
		int lastIndex = this.pos;
		for (int ii = 0; ii < len; ii++) {
			char b1 = data.charAt(offIndex + ii);

			for (int ii2 = 0; ii2 < separators.length; ii2++) {
				if (separators[ii2][0] == b1 && (offIndex + ii + separators[ii2].length) <= endIndex) {
					int j = 1;
					for (int ii3 = 1; ii3 < separators[ii2].length; ii3++) {
						if (separators[ii2][ii3] == data.charAt(offIndex + ii + ii3)) {
							j++;
						}
					}
					if (j == separators[ii2].length) {
						this.readLineDefaultSeparatorIndex = ii2;

						int st = lastIndex;
						int et = offIndex + ii + separators[ii2].length;
						int l = et - st;
						char[] array = new char[l - (resultAddSeparator ? 0 : separators[ii2].length)];
						XArrays.CharSequenceUtil.arraycopyTraverse(data, st, array, 0, array.length);

						lastIndex = et;
						ii += separators[ii2].length;

						ii -= 1;// for (offset for self-increment)

						this.pos = lastIndex;
						return array;
					}
				}
			}
		}
		if (lastIndex != endIndex) {
			int st = lastIndex;
			int et = endIndex;
			// System.out.println(st + "," + et);
			int l = et - st;
			char[] array = new char[l];
			XArrays.CharSequenceUtil.arraycopyTraverse(data, st, array, 0, array.length);
			this.pos = endIndex;
			return array;
		} else {
			return null;
		}
	}

	@Override
	public int readLineSeparatorsIndex() {
		return this.readLineDefaultSeparatorIndex;
	}

	@Override
	public boolean isReadLineReadToSeparator() {
		return this.readLineDefaultSeparatorIndex != -1;
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
		this.buf = null == newBuff ? "" : newBuff;
		this.setBuffSize(size);
	}

	@Override
	public void setBuffSize(int size) throws ArrayIndexOutOfBoundsException {
		if (size > buf.length()) {
			throw new ArrayIndexOutOfBoundsException("arrayLen=" + buf.length() + ", setLen=" + size);
		}
		count = size;
	}

	@Override
	public void seekIndex(int index) {
		if (!(index > -1 && index <= count)) {
			throw new ArrayIndexOutOfBoundsException("can't set pos index=" + index + " length=" + count);
		}
		pos = index;
	}

	@Override
	public int getIndex() {
		return pos;
	}

	@Override
	public int indexOfBuff(char b, int startIndex, int indexRangeindexRange) {
		return XArrays.CharSequenceUtil.indexOf(buf, b, startIndex, indexRangeindexRange);
	}

	@Override
	public int indexOfBuff(char[] b, int startIndex, int indexRange) {
		return XArrays.CharSequenceUtil.indexOf(buf, b, startIndex, indexRange);
	}

	@Override
	public int lastIndexOfBuff(char b, int startIndex, int indexRange) {
		return XArrays.CharSequenceUtil.lastIndexOf(buf, b, startIndex, indexRange);
	}

	@Override
	public int lastIndexOfBuff(char[] b, int startIndex, int indexRange) {
		return XArrays.CharSequenceUtil.lastIndexOf(buf, b, startIndex, indexRange);
	}

	public int size() {
		return count;
	}

    
    
    
}


