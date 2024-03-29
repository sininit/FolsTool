package top.fols.box.io.buffer.bytes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import top.fols.atri.lang.Finals;
import top.fols.box.io.buffer.BufferFilter;
import top.fols.box.io.buffer.BufferOperate;
import top.fols.box.lang.Arrayy;
import java.io.OutputStream;

import static top.fols.atri.lang.Finals.*;

public abstract class ByteBufferOperate extends BufferOperate<byte[]> {
	public ByteBufferOperate() {
		this(EMPTY_BYTE_ARRAY);
	}
	public ByteBufferOperate(byte[] datas) {
		this(datas, 0, datas.length);
	}
	public ByteBufferOperate(byte[] datas, int position, int limit) throws ArrayIndexOutOfBoundsException {
		super(datas, position, limit);
	}
	
	
	public int indexOfBuffer(byte b, int startIndex, int indexRange) {
		return Arrayy.indexOf(buffer, b, startIndex, indexRange);
	}
	public int lastIndexOfBuffer(byte b, int startIndex, int indexRange) {
		return Arrayy.lastIndexOf(buffer, b, startIndex, indexRange);
	}

	@Override public int indexOfBuffer(byte[] b, int startIndex, int indexRange) {
		return Arrayy.indexOf(buffer, b, startIndex, indexRange);
	}
	@Override public int lastIndexOfBuffer(byte[] b, int startIndex, int indexRange) {
		return Arrayy.lastIndexOf(buffer, b, startIndex, indexRange);
	}


	@Override public int hashCode() {
		// TODO: Implement this method
		return Arrays.hashCode(this.buffer) + this.position + this.limit;
	}

	@Override public boolean equals(Object obj) {
		// TODO: Implement this method
		if (!(obj instanceof BufferOperate)) { return false; }
		ByteBufferOperate object = (ByteBufferOperate)obj;
		return 
			Arrays.equals(this.buffer, object.buffer) &&
			this.position == object.position &&
			this.limit == object.limit;
	}

	@Override public String toString() {
		// TODO: Implement this method
		return new String(toArray());
	}
	public String toString(Charset charset) {
		// TODO: Implement this method
		return new String(toArray(), charset);
	}


	@Override public byte[] array_empty() { return EMPTY_BYTE_ARRAY;}
	@Override public byte[] array(int count) { return new byte[count]; }
	
	@Override public int sizeof(byte[] array) { return array.length; }

	public int read() throws IOException {
		int avail = this.available();
		if (avail > 0) {
			byte result = this.buffer[this.position];
			this.positionSkip(1);
			return result & 0xff;
		} else {
			this.buffer_grow(this.limit + this.stream_buffer_size);
			int read = stream_read(this.buffer, this.limit, this.stream_buffer_size);
			if (read == 0) {
				return 0;
			} else if (read > 0) {
				int result = this.buffer[this.limit];
				this.limit += read;
				this.positionSkip(1);
				return result;
			} else {
				return -1;
			}
		}
	}

	public int 			readAvailable() {
		if (this.available() > 0) {
			int result = this.buffer[this.position];
			this.positionSkip(1);
			return result & 0xff;
		} else {
			return -1;
		}
	}

	public void 			append(byte ch) { 
		this.buffer_grow(this.limit + 1);
		this.buffer[this.limit++] = ch;
	}


	public int append_from_stream_read(InputStream reader, int len) throws IOException {
		return this.insert_from_stream_read(this.limit, reader, len);
	}
	public int insert_from_stream_read(int position, InputStream reader, int len) throws IOException {
		if (position < 0 || position > this.limit) { throw new ArrayIndexOutOfBoundsException(
				String.format("position=%s, buffer.limit=%s"
							  , position  
							  , this.limit)); }
		if (len <  0) { throw new ArrayIndexOutOfBoundsException(
				String.format("length=%s"
							  , len)); }
		if (null == reader) { throw new NullPointerException("stream"); }

		this.insert(position, len);
		return reader.read(this.buffer, position, len);
	}
	
	public void writeTo(OutputStream stream) throws IOException{ this.writeTo(stream, this.position, this.available()); }
	public void writeTo(OutputStream stream, int position, int len) throws IOException {
		if (position < 0 || len < 0 || position + len > this.limit) { throw new ArrayIndexOutOfBoundsException(
				String.format("buffer.length=%s, buffer.limit=%s, position=%s, length=%s"
							  , this.buffer_length()
							  , this.limit
							  , position
							  , len)); }
		stream.write(this.buffer, position, len);
	}




	/**
	 * windows or linux or mac os
	 */
	static final ByteBufferFilter READ_LINE_FILTER = new ByteBufferFilter() {{
		byte[][] allSystemLineSeparatorBytes = LineSeparator.getAllSystemLineSeparatorBytesSortedMaxToMin(Finals.Charsets.getOperateSystemCharsetOrDefaultCharset());
		for (byte[] bytes: allSystemLineSeparatorBytes)
			this.addSeparator(bytes);
		}

		@Override
        public boolean accept(int last, int search, byte[] separator, boolean readEnd) {
			return super.accept(last, search, separator, readEnd);
		}
	};
	public static ByteBufferFilter lineFilter() {
		return READ_LINE_FILTER.clone();
	}






	/**
	 * @see top.fols.box.io.buffer.bytes.ByteBufferOperate#readFilterIFEnd(BufferFilter)
	 * @param filter filter
	 * @throws IOException stream.read
	 */
	public boolean readFilterIFEnd(BufferFilter<byte[]> filter) throws IOException {
		int lastFind = this.position;

		filter.setFindResult(null, 0, 0, null, true);

		int maxSize  = filter.getSeparatorMaxSize(), minSize = filter.getSeparatorMinSize();
		int readSize = Math.max(stream_buffer_size, maxSize);

		byte[][] separators = filter.getSeparators();
		while (true) {
			if (lastFind + minSize <= this.limit) {
				boolean isFind = false;
				for (int i = lastFind; i < this.limit; i++) {
					SW: for (byte[] separator : separators) {
						if (this.buffer[i] == separator[0] && i + separator.length <= this.limit) {
							for (int ji = 1; ji < separator.length; ji++) {
								if (!(this.buffer[i + ji] == separator[ji])) {
									continue SW;
								}
							}
							boolean accept = filter.accept(this.position, i, separator, false);
							lastFind = i + sizeof(separator);
							isFind = true;
							if (accept) {
								filter.setFindResult(this, this.position, i, separator, false);
								this.position(lastFind);
								return false;
							}
							break;
						}
					}
				}
				if (!isFind) {
					lastFind = this.limit - maxSize + 1;
				} else {
					continue;
				}
			}
			if (this.append_from_stream_read(readSize) == -1) {
				break;
			}
		}
		if (this.position != this.limit) {
			boolean accept = filter.accept(this.position, this.limit, null, true);
			if (accept) {
				filter.setFindResult(this, this.position, this.limit, null, true);
				this.position(this.limit);
				return false;
			}
		}
		return true;
	}
	public boolean readFilter(BufferFilter<byte[]> filter) throws IOException {
		//noinspection PointlessBooleanExpression
		return false == readFilterIFEnd(filter);
	}
}
