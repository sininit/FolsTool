package top.fols.box.io.base;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.XStream;
import top.fols.box.io.interfaces.XInterfaceGetInnerStream;
import top.fols.box.io.interfaces.XInterfaceLineReaderStream;
import top.fols.box.io.interfaces.XInterfacePrivateBuffOperat;
import top.fols.box.io.interfaces.XInterfaceReleaseBufferable;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.statics.XStaticSystem;

public class XReaderLine<T extends Reader> extends Reader implements XInterfacePrivateBuffOperat<char[]>,
XInterfaceLineReaderStream<char[]>, XInterfaceReleaseBufferable, XInterfaceGetInnerStream<T> {
	private T is;
	private char[] buf;// readLine superfluous data

	public XReaderLine(T is) {
		this(is, XStream.DEFAULT_BYTE_BUFF_SIZE);
	}

	public XReaderLine(T is, int lineReadBufferedSize) {
		this.is = is;
		this.buf = null;
		this.linereadbuf = new char[lineReadBufferedSize];
	}

	private void clearBuffered() {
		if (null != this.buf && this.buf.length == 0) {
			this.buf = null;
		}
	}

	@Override
	public int read() throws IOException {
		try {
			this.isReadComplete = false;

			if (null != this.buf) {
				char b = this.buf[0];
				this.buf = XReaderLine.copyOfRange(this.buf, 1, this.buf.length);
				int re = b;
				return re;
			}
			int read = this.is.read();
			if (read == -1) {
				this.isReadComplete = true;
			}
			return read;
		} finally {
			this.clearBuffered();
		}
	}

	@Override
	public int read(char[] b, int off, int len) throws IOException {
		try {
			this.isReadComplete = false;

			int br = this.getBuffSize();
			if (br >= len) {
				System.arraycopy(this.buf, 0, b, off, len);
				this.buf = XReaderLine.copyOfRange(this.buf, len, this.buf.length);
				return len;
			} else {
                if (null != this.buf) {
					System.arraycopy(this.buf, 0, b, off, br);
					this.buf = null;
					len -= br;
				}
                int r = this.is.read(b, off + br, len);
                if (r == -1) {
					this.isReadComplete = true;
					return br == 0 ?-1: br;
				} else {
					return br + r;
				}
			}
		} finally {
			this.clearBuffered();
		}
	}

	@Override
	public long skip(long n) throws IOException {
		try {
			if (n <= 0) {
				return 0;
			}
			long n2 = 0;
			if (null != this.buf) {
				int length = this.buf.length;
				int start;
				if (n > Integer.MAX_VALUE) {
					start = Integer.MAX_VALUE;
				} else {
					start = (int) n;
				}
				if (start > length) {
					start = length;
				}
				this.buf = XReaderLine.copyOfRange(buf, start, this.buf.length);

				n = n - start;
				n2 += start;
			}
			if (n > 0) {
				n2 += this.is.skip(n);
			}
			return n2;
		} finally {
			this.clearBuffered();
		}
	}

	@Override
	public boolean ready() throws IOException {
		// TODO Auto-generated method stub
		return this.is.ready();
	}

	@Override
	public void close() throws IOException {
		this.releaseBuffer();
		this.is.close();
		this.linereadbuf = null;
	}

	@Override
	public void mark(int readlimit) throws IOException {
		this.is.mark(readlimit);
	}

	@Override
	public void reset() throws IOException {
		this.is.reset();
	}

	@Override
	public boolean markSupported() {
		return this.is.markSupported();
	}

	private int readLineDefaultSeparatorIndex = -1;
	private boolean isReadComplete = false;
	private char[] linereadbuf = null;// read data buffered
	private XCharArrayWriter lineoutbuf = new XCharArrayWriter();

	/**
	 * Get Next Line
	 * 
	 * @param separator          lineSeparator
	 * @param resultAddSeparator if read to separator, add return or not?
	 * 
	 * @return if the content is not read, null will be returned
	 */
	@Override
	public char[] readLine(char[] separator, boolean resultAddSeparator) throws IOException {
		try {
			this.readLineDefaultSeparatorIndex = -1;
			this.isReadComplete = false;

			if (null != this.buf) {
				this.lineoutbuf.setBuff(this.buf, this.buf.length);
				this.buf = null;
			}

			int i = 0;// 累计
			int read = -1;
			int searchOffset;

			int findindex = -1;
			char[] split = XStaticFixedValue.nullcharArray;
			int search = this.lineoutbuf.indexOfBuff(separator, 0, lineoutbuf.size());
			if (search != -1 && (findindex == -1 || search < findindex)) {
				this.readLineDefaultSeparatorIndex = 0;
				findindex = search;
				split = separator;
			}

			if (findindex == -1) {
top: while (true) {
					if ((read = this.is.read(this.linereadbuf)) == -1) {
						this.isReadComplete = true;
						break;
					}
					this.lineoutbuf.write(this.linereadbuf, 0, read);

					searchOffset = i - separator.length + 1;
					search = this.lineoutbuf.indexOfBuff(separator, searchOffset < 0 ? 0 : searchOffset,
                        this.lineoutbuf.size());
					if (search != -1 && (findindex == -1 || search < findindex)) {
						this.readLineDefaultSeparatorIndex = 0;
						findindex = search;
						split = separator;
						break top;
					}
					i += read;
				}
			}

			if (findindex > -1) {
				char[] array = lineoutbuf.getBuff();
				int arraysize = lineoutbuf.size();
				if (resultAddSeparator) {
					this.lineoutbuf.setBuffSize(findindex + split.length);
				} else {
					this.lineoutbuf.setBuffSize(findindex);
				}
				this.buf = XReaderLine.copyOfRange(array, findindex + split.length, arraysize);

				if (this.lineoutbuf.size() == 0 && !resultAddSeparator && null != buf) {
					this.lineoutbuf.releaseBuffer();
					return XStaticFixedValue.nullcharArray;
				}
			}
			char array[] = this.lineoutbuf.toCharArray();
			this.lineoutbuf.releaseBuffer();
			return (null != array && array.length == 0) ? null : array;
		} finally {
			this.clearBuffered();
		}
	}

	/**
	 * @param offset buf search offset
	 */
	private char[] nextBufferedLine0(int offset, char[][] separators, boolean resultAddSeparator) throws IOException {
		this.readLineDefaultSeparatorIndex = -1;

		if (null != this.buf) {
			int endIndex = this.buf.length;
			int offIndex = offset;
			char[] data = this.buf;

			int len = endIndex - offIndex;
			int lastIndex = 0;
			for (int ii = 0; ii < len; ii++) {
				char b1 = data[offIndex + ii];

				for (int ii2 = 0; ii2 < separators.length; ii2++) {
					if (separators[ii2][0] == b1 && (offIndex + ii + separators[ii2].length) <= endIndex) {
						int j = 1;
						for (int ii3 = 1; ii3 < separators[ii2].length; ii3++) {
							if (separators[ii2][ii3] == data[offIndex + ii + ii3]) {
								j++;
							}
						}
						if (j == separators[ii2].length) {
							this.readLineDefaultSeparatorIndex = ii2;

							int st = lastIndex;
							int et = offIndex + ii + separators[ii2].length;
							int l = et - st;

							int arrlen = l - (resultAddSeparator ? 0 : separators[ii2].length);
							char[] array = new char[arrlen];
							if (arrlen > 0) {
								System.arraycopy(data, st, array, 0, array.length);
							}

							lastIndex = et;
							ii += separators[ii2].length;
							ii -= 1;// for (offset for self-increment)

							// System.out.println("*1" + st + "," + et);
							this.buf = XReaderLine.copyOfRange(data, et, data.length);
							return array;
						}
					}
				}
			}
		}
		return null; // delimiter not found from cache
	}

	/**
	 * @param offset buf search offset
	 */
	private char[] nextLineOutLine0(int offset, char[][] separators, boolean resultAddSeparator) throws IOException {
		this.readLineDefaultSeparatorIndex = -1;

		int endIndex = this.lineoutbuf.size();
		int offIndex = offset;
		char[] data = this.lineoutbuf.getBuff();

		int len = endIndex - offIndex;
		int lastIndex = 0;
		for (int ii = 0; ii < len; ii++) {
			char b1 = data[offIndex + ii];

			for (int ii2 = 0; ii2 < separators.length; ii2++) {
				if (separators[ii2][0] == b1 && (offIndex + ii + separators[ii2].length) <= endIndex) {
					int j = 1;
					for (int ii3 = 1; ii3 < separators[ii2].length; ii3++) {
						if (separators[ii2][ii3] == data[offIndex + ii + ii3]) {
							j++;
						}
					}
					if (j == separators[ii2].length) {
						this.readLineDefaultSeparatorIndex = ii2;

						int st = lastIndex;
						int et = offIndex + ii + separators[ii2].length;
						int l = et - st;

						int arrlen = l - (resultAddSeparator ? 0 : separators[ii2].length);
						char[] array = new char[arrlen];
						if (arrlen > 0) {
							System.arraycopy(data, st, array, 0, array.length);
						}

						lastIndex = et;
						ii += separators[ii2].length;
						ii -= 1;// for (offset for self-increment)

						// System.out.println("*1 " + st + "," + et);
						this.buf = XReaderLine.copyOfRange(data, et, this.lineoutbuf.size());
						return array;
					}
				}
			}
		}
		return null; // delimiter not found from cache
	}

	@Override
	public char[] readLine(char[][] separators, boolean resultAddSeparator)
    throws UnsupportedOperationException, IOException {
		try {
			this.readLineDefaultSeparatorIndex = -1;
			this.isReadComplete = false;

			char[] readBuffered = this.nextBufferedLine0(0, separators, resultAddSeparator);
			if (null != readBuffered) {
				return readBuffered;
			} else {
				if (null != this.buf) {
					this.lineoutbuf.setBuff(this.buf, this.buf.length);
					this.buf = null;
				}

				int maxSeparator = 0;
				for (char[] s : separators) {
					if (s.length > maxSeparator) {
						maxSeparator = s.length;
					}
				}

				int i = 0;
				int read = -1;
				int searchOffset;
top: while (true) {
					if ((read = this.is.read(this.linereadbuf)) == -1) {
						this.isReadComplete = true;
						break;
					}
					this.lineoutbuf.write(this.linereadbuf, 0, read);

					searchOffset = i - maxSeparator + 1;
					searchOffset = searchOffset < 0 ? 0 : searchOffset;
					readBuffered = this.nextLineOutLine0(searchOffset, separators, resultAddSeparator);
					if (null != readBuffered) {
						return readBuffered;
					}
					i += read;
				}
			}
			char[] result = this.lineoutbuf.size() == 0 ? null : this.lineoutbuf.toCharArray();
			return result;
		} finally {
			this.lineoutbuf.releaseBuffer();// ******
			this.clearBuffered();
		}
	}

	@Override
	public char[] readLine(char[] splitChar) throws IOException {
		return this.readLine(splitChar, true);
	}







	private static final char[][] ALL_LINE_SEPARATOR_CHARS = lineSeparatorsToChars(XStaticSystem.getAllSystemLineSeparator());
	public static char[][] lineSeparatorsToChars(String[] lineSeparator) {
		char[][] result = new char[lineSeparator.length][];
		for (int i = 0; i < lineSeparator.length; i++) {
			result[i] = lineSeparator[i].toCharArray();
		}
		return result;
	}
	public static final char[][] getAllSystemLineSeparatorChars() {
        return lineSeparatorsToChars(XStaticSystem.getAllSystemLineSeparator());
    }
	
	public String readLineToString(String[] lineSeparator, boolean resultAddSeparator)
			throws UnsupportedOperationException, IOException {
		char[] bytes = this.readLine(lineSeparatorsToChars(lineSeparator), resultAddSeparator);
		return null == bytes ? null : new String(bytes);
	}

	public String readLineToString(boolean resultAddSeparator) throws UnsupportedOperationException, IOException {
		char[] bytes = this.readLine(XReaderLine.ALL_LINE_SEPARATOR_CHARS, resultAddSeparator);
		return null == bytes ? null : new String(bytes);
	}
	public String readLineToString() throws UnsupportedOperationException, IOException {
		return this.readLineToString(true);
	}





	@Override
	public boolean isReadLineReadToSeparator() {
		return this.readLineDefaultSeparatorIndex != -1;
	}

	@Override
	public int readLineSeparatorsIndex() {
		// TODO Auto-generated method stub
		return this.readLineDefaultSeparatorIndex;
	}

	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.buf = null;
	}

	@XAnnotations("last read stream result equals -1")
	public boolean isReadComplete() {
		return this.isReadComplete;
	}

	@Override
	public char[] getBuff() {
		return this.buf;
	}

	@Override
	public int getBuffSize() {
		return null == this.buf ? 0 : this.buf.length;
	}

	@Override
	public void setBuff(char[] newBuff, int size) {
		// TODO: Implement this method
		this.buf = null == newBuff ? null : (newBuff.length == 0 ? null : newBuff);
		this.setBuffSize(size);
	}

	@Override
	public void setBuffSize(int size) {
		// TODO: Implement this method
		if (size <= 0) {
			this.buf = null;
			return;
		}
		if (size == (null == this.buf ? 0 : this.buf.length)) {
			return;
		} else {
			this.buf = null == this.buf ? null : XReaderLine.copyOf(this.buf, size);
		}
	}

	@Override
	public T getInnerStream() {
		return this.is;
	}

	protected static char[] copyOf(char[] array, int size) {
		char[] newArray = new char[size];
		if (null != array) {
			System.arraycopy(array, 0, newArray, 0, Math.min(size, array.length));
		}
		return newArray;
	}

	protected static char[] copyOfRange(char[] array, int offset, int end) {
		if (end - offset < 0 || offset < 0 || end < 0 || offset > array.length || end > array.length) {
			return null;
		}
		if (end - offset < 1) {
			return XStaticFixedValue.nullcharArray;
		}
		return Arrays.copyOfRange(array, offset, end);
	}

}
