package top.fols.atri.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import top.fols.atri.interfaces.annotations.UnsafeOperate;
import top.fols.atri.interfaces.interfaces.IInnerStream;
import top.fols.atri.io.util.Streams;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Mathz;

import static top.fols.atri.io.BytesInputStreams.SEPARATOR_INDEX_UNSET;

public class BufferInputStreams<T extends InputStream> extends InputStream implements Delimiter.IBytesDelimiterInputStreamIO, IInnerStream<T> {
	static final int DEFAULT_BUFF_SIZE = Streams.DEFAULT_BYTE_BUFF_SIZE;
	static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	final int defaultBufferSize;
	final int defaultBufferSizeD2;
	final int defaultBufferSizeM3;

	InputStream in;
	byte[] buffDef;
	byte[] buff;
	int    buffIndex = 0;
	int    buffCount = 0;//relative index
	int    markPos   = -1;

	/*
	 * 理论上在read后数据才会装载 这时候available会变化，
	 * 假设是阻塞流将这时候的 available 读取完 然后再一次 read(),
	 * 但是实际上已经没有数据了 所以会一直阻塞
	 * 假设 forceRead = false; 那么 读完 available 后就会退出不再读取(BufferedInputStream就是这样的)
	 * 假设 forceRead = true;  那么 将会一直读取直到读取到需要的数据;
	 */
	boolean fread = true;
	@Deprecated
	public boolean isForceRead() { return fread; }
	@Deprecated
	public void setForceRead(boolean b) { this.fread = b; }

	public BufferInputStreams(T in) {
		this(in, DEFAULT_BUFF_SIZE);
	}
	public BufferInputStreams(T in, int size) {
		this.in = in;
		this.defaultBufferSizeD2 = (this.defaultBufferSize = size) / 2;
		this.defaultBufferSizeM3 = size * 3;
		this.buff = this.buffDef = new byte[defaultBufferSize];
	}

	/**
	 * whether the buffer is used up or not, it will be filled
	 */
	int readFill() throws IOException {
		this.clean();
		return this.appendFill();
	}
	void clean() {
		if (markPos < 0) {//no mark
			if (buffCount <= 0) {// no buff
				buffIndex  = 0; //reset pot -> clear buffer
				buffCount  = 0; //reset pot -> clear buffer
				if (buff.length > defaultBufferSize)
					buff = buffDef; //to default buffer size
			} else {
				if (buff.length  >= defaultBufferSizeM3) {
					if (buffCount < defaultBufferSize) {
						byte[] data = new byte[defaultBufferSize];
						Arrayz.arraycopy(buff, buffIndex, data, 0, buffCount);
						this.buff = data;
						this.buffIndex = 0;
					}
				}
			}
		}
	}
	int appendFill() throws IOException {
		int writable = buff.length - (buffIndex + buffCount);
		if (writable <= 0 || //no Writable
				writable < defaultBufferSizeD2) { //Writable < 1/2
			//grow buffer
			int old = buff.length;
			int nsz = old * 2;
			if (nsz < 0 || nsz > MAX_ARRAY_SIZE)
				throw new OutOfMemoryError("Required array size too large");
			byte[] data = new byte[nsz];
			Arrayz.arraycopy(buff, 0, data, 0, old);
			buff = data;
		}
		int read  = -1;
		int pos   = buffIndex + buffCount;
		int need  = buff.length - pos;
		int fill = in.read(buff, pos, need);
		if (fill > -1) {
			buffCount += (read = Mathz.min(fill, need));
		}
		return read;
	}

	@Override
	public int read() throws IOException {
		// TODO: Implement this method
		if (buffCount <= 0) {
			if (readFill() <= 0)
				return -1;
		}
		byte b = buff[buffIndex++];
		buffCount--;
		return b & 0xff;
	}

	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}
	@Override
	public int read(byte[] b, int offset, int len) throws IOException {
		if ((offset | len | (offset + len) | (b.length - (offset + len))) < 0)
			throw new ArrayIndexOutOfBoundsException("b.length=" + b.length + ", offset=" + offset + ", len=" + len);
		if (len == 0)
			return 0;
		if (len <= buffCount) {
			Arrayz.arraycopy(buff, buffIndex,
					b, offset, len);
			buffIndex += len;
			buffCount -= len;
			return len;
		} else {
			int off   = offset;
			int limit = offset + len;
			if (buffCount > 0) {
				Arrayz.arraycopy(buff, buffIndex,
						b, off, buffCount);
				off += buffCount;
				buffIndex += buffCount;
				buffCount -= buffCount;

				return off - offset;//try not to read the stream
			}

			if (markPos < 0) {
				int read = in.read(b, off, limit - off);
				if (read > -1) {
					off += read;

					if (isStreamAvailable()) {
						readFill();
					}
				}
			} else {
				if (readFill() > 0) {
					int v = Mathz.min(limit - off, buffCount);
					Arrayz.arraycopy(buff, buffIndex,
							b, off, v);
					off  += v;
					buffIndex += v;
					buffCount -= v;
				}
			}
			return off == offset ? -1 : off - offset;
		}
	}


	@Override
	public long skip(long n) throws IOException {
		// TODO: Implement this method
		if (n <= 0)
			return 0;

		if (buffCount >= n) {
			buffIndex += n;
			buffCount -= n;
			return n;
		} else {
			int s = buffCount;
			long skiped  = buffCount;
			buffIndex += skiped;
			buffCount -= skiped;
			if (s > 0) {
				return skiped;
			}

			if (markPos < 0) {
				return in.skip(n);
			} else {
				if (readFill() > 0) {
					long v = Mathz.min(n - skiped, buffCount);
					skiped += v;

					buffIndex += v;
					buffCount -= v;
				}
				return skiped;
			}
		}
	}

	@Override
	public int available() throws IOException {
		// TODO: Implement this method
		int n = buffCount;
		int avail = in.available();
		return n > (Integer.MAX_VALUE - avail)
				? Integer.MAX_VALUE
				: n + avail;
	}

	@Override
	public void close() throws IOException {
		// TODO: Implement this method
		InputStream in = this.in;
		if (null != in)
			in.close();
		in = null;
		buff = null;
		buffCount = 0;
	}




	@Override
	public synchronized void mark(int readlimit) {
		if (readlimit >= 0)
			throw new UnsupportedOperationException("readlimit >= 0, use [readlimit=-1] and markCancel()");
		this.markPos = buffIndex;
	}
	public void markCancel() {
		this.markPos = -1;
		this.clean();
	}
	@Override
	public synchronized void reset() throws IOException {
		if (this.markPos < 0)
			throw new IOException("no mark");
		int limit = buffIndex + buffCount;
		this.buffIndex = markPos;
		this.buffCount = limit - markPos;
		this.markPos = -1;
	}
	@Override
	public boolean markSupported() {
		return true;
	}


	@Override
	public T getInnerStream() {
		return (T) in;
	}

	public boolean isStreamAvailable() throws IOException {
		return in.available() > 0;
	}

//----------delimiter---------
//  this may be a blocking flow
//	do {
//		byte[] next = buff.readNextLine(false);
//		if (null == next)
//			break;
//		System.out.println(new String(next));
//	} while (buff.hasNext());

	@UnsafeOperate
	@Deprecated
	@Override
	public boolean findNext() throws IOException {
		return   buffCount > 0 ||
				isStreamAvailable() ||
				readFill() > 0;
	}

	private Delimiter.IBytesDelimiter delimiter;
	private byte[][] separators;
	private int separatorIndex = SEPARATOR_INDEX_UNSET;

	@Override
	public Delimiter.IBytesDelimiter getDelimiter() { return this.delimiter; }
	@Override
	public void setDelimiter(Delimiter.IBytesDelimiter delimiter) {
		this.delimiter  = delimiter;
		this.separators = this.delimiter.innerSeparators();
	}
	@Override
	public void setDelimiter(byte[][] delimiter) {
		setDelimiter(Delimiter.build(delimiter));
	}
	@Override
	public void setDelimiterAndSetCharset(char[][] delimiter, Charset charset) {
		setDelimiter(Delimiter.convertAsBytes(delimiter, charset));
		setDelimiterAsStringCharset(charset);
	}
	Charset charset = Charset.defaultCharset();
	@Override
	public void setDelimiterAsStringCharset(Charset charset) {
		this.charset = null == charset ? Charset.defaultCharset(): charset;
	}
	@Override
	public Charset getDelimiterAsStringCharset() {
		return this.charset = null == charset ? Charset.defaultCharset(): charset;
	}

	/**
	 * 如果最后一次next返回的是分隔符 则return >= 0
	 */
	@Override
	public boolean lastIsReadReadSeparator() {
		return SEPARATOR_INDEX_UNSET != this.separatorIndex;
	}
	@Override
	public int     lastReadSeparatorIndex() {
		return this.separatorIndex;
	}
	public int     lastReadSeparatorLength() {
		return separatorIndex == SEPARATOR_INDEX_UNSET ? 0: separators[separatorIndex].length;
	}
	@Override
	public byte[]  lastReadSeparator() {
		return separatorIndex == SEPARATOR_INDEX_UNSET ? null: separators[separatorIndex].clone();
	}




	/**
	 * 返回分隔符 或者分隔符之前的内容
	 */
	@Override
	public byte[] readNext() throws IOException {
		this.clean();
		this.separatorIndex = SEPARATOR_INDEX_UNSET;

		Delimiter.IBytesDelimiter delimiter = this.delimiter;
		byte[] data    = this.buff;
		int last       = this.buffIndex, offset    = last, limit     = last + this.buffCount;
		boolean readed = this.buffCount > 0;
		for (;;) {
			while (offset < limit) {
				byte datum = data[offset];
				if (delimiter.isHead(datum)) {
					int minLength = delimiter.headMatchMinLength(datum), maxLength = delimiter.headMatchMaxLength(datum);
					//fill data
					while (offset + maxLength > limit) {
						int read;
						if (((readed && (!fread && !isStreamAvailable())) || (read = appendFill()) < 0)) {
							readed = true;
							break;
						}
						readed = true;
						data   = this.buff;
						limit += read;
					}
					if (offset + minLength > limit)
						break;

					int sIndex = delimiter.assertSeparator(data, offset, limit);
					if (sIndex > -1) {
						//is separator
						if (offset == last) {
							byte[] array = separators[sIndex].clone();
							this.separatorIndex = sIndex;
							int v = (offset - last) + array.length;//0+
							this.buffIndex += v;
							this.buffCount -= v;
							return array;
						} else {
							byte[] array = new byte[offset - last];
							Arrayz.arraycopy(data, last, array, 0, array.length);
							int v = (offset - last);
							this.buffIndex += v;
							this.buffCount -= v;
							return array;
						}
					}
				}
				offset++;
			}
			//no data
			int read;
			if (((readed && (!fread && !isStreamAvailable())) || (read = appendFill()) < 0)) {
				readed = true;
				break;
			}
			readed = true;
			data   = this.buff;
			limit += read;
		}
		if (last == offset) {
			return null;
		} else {
			//no avail data
			byte[] array = new byte[limit - last];
			Arrayz.arraycopy(data, last, array, 0, array.length);
			int v = array.length;
			this.buffIndex += v;
			this.buffCount -= v;
			return array;
		}
	}
	//content + (resultAddSeparator?separator:[])
	@Override
	public byte[] readNextLine(boolean resultAddSeparator) throws IOException {
		this.clean();
		this.separatorIndex = SEPARATOR_INDEX_UNSET;

		Delimiter.IBytesDelimiter delimiter = this.delimiter;
		byte[] data    = this.buff;
		int last       = this.buffIndex, offset    = last, limit     = last + this.buffCount;
		boolean readed = this.buffCount > 0;
		for (;;) {
			while (offset < limit) {
				byte datum = data[offset];
				if (delimiter.isHead(datum)) {
					int minLength = delimiter.headMatchMinLength(datum), maxLength = delimiter.headMatchMaxLength(datum);
					//fill data
					while (offset + maxLength > limit) {
						int read;
						if (((readed && (!fread && !isStreamAvailable())) || (read = appendFill()) < 0)) {
							readed = true;
							break;
						}
						readed = true;
						data   = this.buff;
						limit += read;
					}
					if (offset + minLength > limit)
						break;

					int sIndex = delimiter.assertSeparator(data, offset, limit);
					if (sIndex > -1) {
						//is separator
						int v = (offset - last) + separators[sIndex].length;
						byte[] array = new byte[(offset - last) + (resultAddSeparator ? separators[sIndex].length: 0)];
						if (array.length > 0)
							Arrayz.arraycopy(data, last, array, 0, array.length);
						this.separatorIndex = sIndex;
						this.buffIndex += v;
						this.buffCount -= v;
						return array;
					}
				}
				offset++;
			}
			//no data
			int read;
			if (((readed && (!fread && !isStreamAvailable())) || (read = appendFill()) < 0)) {
				readed = true;
				break;
			}
			readed = true;
			data   = this.buff;
			limit += read;
		}
		if (last == offset) {
			return null;
		} else {
			//no avail data
			byte[] array = new byte[limit - last];
			Arrayz.arraycopy(data, last, array, 0, array.length);
			int v = array.length;
			this.buffIndex += v;
			this.buffCount -= v;
			return array;
		}
	}




	//------------copy---------
	@Override
	public String readNextAsString() throws IOException {
		byte[] bytes = readNext();
		return null == bytes ? null : newString(bytes);
	}
	//content + (resultAddSeparator?separator:[])
	@Override
	public String readNextLineAsString(boolean resultAddSeparator) throws IOException {
		byte[] bytes = readNextLine(resultAddSeparator);
		return null == bytes ? null : newString(bytes);
	}
	public String newString(byte[] data) {
		return new String(data, charset);
	}
//------------copy---------
}


