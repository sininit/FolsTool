package top.fols.box.io.base;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import top.fols.atri.io.Streams;
import top.fols.box.annotation.BaseAnnotations;
import top.fols.box.io.interfaces.XInterfaceGetInnerStream;
import top.fols.box.io.interfaces.XInterfaceLineReaderStream;
import top.fols.box.io.interfaces.XInterfacePrivateBuffOperat;
import top.fols.box.io.interfaces.XInterfaceReleaseBufferable;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.statics.XStaticSystem;

public class XInputStreamLine<T extends InputStream> extends InputStream implements XInterfacePrivateBuffOperat<byte[]>,
XInterfaceLineReaderStream<byte[]>, XInterfaceReleaseBufferable, XInterfaceGetInnerStream<T> {
	private T is;
	private byte[] buf;// readLine superfluous data

	public XInputStreamLine(T is) {
		this(is, Streams.DEFAULT_BYTE_BUFF_SIZE);
	}

	public XInputStreamLine(T is, int lineReadBufferedSize) {
		this.is = is;
		this.buf = null;
		this.linereadbuf = new byte[lineReadBufferedSize];
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
				byte b = this.buf[0];
				this.buf = XInputStreamLine.copyOfRange(this.buf, 1, this.buf.length);
				int re = b & 0xff;
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
	public int read(byte[] b, int off, int len) throws IOException {
		try {
			this.isReadComplete = false;

			int br = this.getBuffSize();
			if (br >= len) {
				System.arraycopy(this.buf, 0, b, off, len);
				this.buf = XInputStreamLine.copyOfRange(this.buf, len, this.buf.length);
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
				this.buf = XInputStreamLine.copyOfRange(buf, start, this.buf.length);

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
	public int available() throws IOException {
		long available = 0;
		if (null != this.buf) {
			available += this.buf.length;
		}
		long tmp = this.is.available();
		if (tmp > 0) {
			available += tmp;
		}
		return available > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) available;
	}

	@Override
	public void close() throws IOException {
		this.releaseBuffer();
		this.is.close();
		this.linereadbuf = null;
	}

	@Override
	public void mark(int readlimit) {
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
	private byte[] linereadbuf = null;// read data buffered
	private XByteArrayOutputStream lineoutbuf = new XByteArrayOutputStream();

	/**
	 * Get Next Line
	 * 
	 * @param separator          lineSeparator
	 * @param resultAddSeparator if read to separator, add return or not?
	 * 
	 * @return if the content is not read, null will be returned
	 */
	@Override
	public byte[] readLine(byte[] separator, boolean resultAddSeparator) throws IOException {
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
			byte[] split = XStaticFixedValue.nullbyteArray;
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
				byte[] array = lineoutbuf.getBuff();
				int arraysize = lineoutbuf.size();
				if (resultAddSeparator) {
					this.lineoutbuf.setBuffSize(findindex + split.length);
				} else {
					this.lineoutbuf.setBuffSize(findindex);
				}
				this.buf = XInputStreamLine.copyOfRange(array, findindex + split.length, arraysize);

				if (this.lineoutbuf.size() == 0 && !resultAddSeparator && null != buf) {
					this.lineoutbuf.releaseBuffer();
					return XStaticFixedValue.nullbyteArray;
				}
			}
			byte array[] = this.lineoutbuf.toByteArray();
			this.lineoutbuf.releaseBuffer();
			return (null != array && array.length == 0) ? null : array;
		} finally {
			this.clearBuffered();
		}
	}

	/**
	 * @param offset buf search offset
	 */
	private byte[] nextBufferedLine0(int offset, byte[][] separators, boolean resultAddSeparator) throws IOException {
		this.readLineDefaultSeparatorIndex = -1;

		if (null != this.buf) {
			int endIndex = this.buf.length;
			int offIndex = offset;
			byte[] data = this.buf;

			int len = endIndex - offIndex;
			int lastIndex = 0;
			for (int ii = 0; ii < len; ii++) {
				byte b1 = data[offIndex + ii];

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
							byte[] array = new byte[arrlen];
							if (arrlen > 0) {
								System.arraycopy(data, st, array, 0, array.length);
							}

							lastIndex = et;
							ii += separators[ii2].length;
							ii -= 1;// for (offset for self-increment)

							// System.out.println("*1" + st + "," + et);
							this.buf = copyOfRange(data, et, data.length);
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
	private byte[] nextLineOutLine0(int offset, byte[][] separators, boolean resultAddSeparator) throws IOException {
		this.readLineDefaultSeparatorIndex = -1;

		int endIndex = this.lineoutbuf.size();
		int offIndex = offset;
		byte[] data = this.lineoutbuf.getBuff();

		int len = endIndex - offIndex;
		int lastIndex = 0;
		for (int ii = 0; ii < len; ii++) {
			byte b1 = data[offIndex + ii];

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
						byte[] array = new byte[arrlen];
						if (arrlen > 0) {
							System.arraycopy(data, st, array, 0, array.length);
						}

						lastIndex = et;
						ii += separators[ii2].length;
						ii -= 1;// for (offset for self-increment)

						// System.out.println("*1 " + st + "," + et);
						this.buf = copyOfRange(data, et, this.lineoutbuf.size());
						return array;
					}
				}
			}
		}
		return null; // delimiter not found from cache
	}

	@Override
	public byte[] readLine(byte[][] separators, boolean resultAddSeparator)
    throws UnsupportedOperationException, IOException {
		try {
			this.readLineDefaultSeparatorIndex = -1;
			this.isReadComplete = false;

			byte[] readBuffered = this.nextBufferedLine0(0, separators, resultAddSeparator);
			if (null != readBuffered) {
				return readBuffered;
			} else {
				if (null != this.buf) {
					this.lineoutbuf.setBuff(this.buf, this.buf.length);
					this.buf = null;
				}

				int maxSeparator = 0;
				for (byte[] s : separators) {
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
			byte[] result = this.lineoutbuf.size() == 0 ? null : this.lineoutbuf.toByteArray();
			return result;
		} finally {
			this.lineoutbuf.releaseBuffer();// ******
			this.clearBuffered();
		}
	}

	@Override
	public byte[] readLine(byte[] splitChar) throws IOException {
		return this.readLine(splitChar, true);
	}





    protected static final String[] ALL_LINE_SEPARATOR = XStaticSystem.getAllSystemLineSeparator();
    private static final Map<String, byte[][]> ALL_LINE_SEPARATOR_CACHE = new HashMap<>();
    private static final byte[][] getAllLineSeparatorBytes(Charset charset) {
        String cn = null == charset ?Charset.defaultCharset().name(): charset.name();
        byte[][] ls = ALL_LINE_SEPARATOR_CACHE.get(cn);
        if (null == ls) {
			ls = lineSeparatorsToBytes(ALL_LINE_SEPARATOR, charset);
            ALL_LINE_SEPARATOR_CACHE.put(cn, ls);
        }
        return ls;
    }
    public static byte[][] lineSeparatorsToBytes(String[] lineSeparator, Charset charset) {
        if (null == charset) { charset = Charset.defaultCharset(); }
        byte[][] result = new byte[lineSeparator.length][];
        for (int i = 0;i < lineSeparator.length;i++) {
            result[i] = lineSeparator[i].getBytes(charset);
        }
        return result;
    }
    public static final byte[][] getAllSystemLineSeparatorBytes(Charset charset) {
        return lineSeparatorsToBytes(ALL_LINE_SEPARATOR, charset);
    }
    
	/**
	 * @param charset nullable
	 */
	public String readLineToString(String[] lineSeparator, Charset charset, boolean resultAddSeparator)
			throws UnsupportedOperationException, IOException {
		if (null == charset) {
			charset = Charset.defaultCharset();
		}
		byte[] bytes = this.readLine(this.lineSeparatorsToBytes(lineSeparator, charset), resultAddSeparator);
		return null == bytes ? null : new String(bytes, charset);
	}

	/**
	 * @param charset nullable
	 */
	public String readLineToString(Charset charset, boolean resultAddSeparator)
			throws UnsupportedOperationException, IOException {
		if (null == charset) {
			charset = Charset.defaultCharset();
		}
		byte[] bytes = this.readLine(this.getAllLineSeparatorBytes(charset), resultAddSeparator);
		return null == bytes ? null : new String(bytes, charset);
	}
    /**
     * @param charset nullable
     */
    public String readLineToString(Charset charset) throws UnsupportedOperationException, IOException {
        return this.readLineToString(charset, true);
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

	@BaseAnnotations("last read stream result equals -1")
	public boolean isReadComplete() {
		return this.isReadComplete;
	}

	@Override
	public byte[] getBuff() {
		return this.buf;
	}

	@Override
	public int getBuffSize() {
		return null == this.buf ? 0 : this.buf.length;
	}

	@Override
	public void setBuff(byte[] newBuff, int size) {
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
			this.buf = null == this.buf ? null : XInputStreamLine.copyOf(this.buf, size);
		}
	}

	@Override
	public T getInnerStream() {
		return this.is;
	}

	protected static byte[] copyOf(byte[] array, int size) {
		byte[] newArray = new byte[size];
		if (null != array) {
			System.arraycopy(array, 0, newArray, 0, Math.min(size, array.length));
		}
		return newArray;
	}

	protected static byte[] copyOfRange(byte[] array, int offset, int end) {
		if (end - offset < 0 || offset < 0 || end < 0 || offset > array.length || end > array.length) {
			return null;
		}
		if (end - offset < 1) {
			return XStaticFixedValue.nullbyteArray;
		}
		return Arrays.copyOfRange(array, offset, end);
	}

}
