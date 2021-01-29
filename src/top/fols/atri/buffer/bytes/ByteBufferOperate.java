package top.fols.atri.buffer.bytes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import top.fols.atri.buffer.BufferOperate;
import top.fols.box.util.XArrays;
import java.io.OutputStream;

import static top.fols.atri.lang.Finals.*;

public abstract class ByteBufferOperate extends BufferOperate<byte[]> {
	public ByteBufferOperate() {
		this(EMPTY_BYTE_BUFFER);
	}
	public ByteBufferOperate(byte[] datas) {
		this(datas, 0, datas.length);
	}
	public ByteBufferOperate(byte[] datas, int position, int limit) throws ArrayIndexOutOfBoundsException {
		super(datas, position, limit);
	}
	
	
	public int indexOfBuffer(byte b, int startIndex, int indexRange) { return XArrays.indexOf(buffer, b, startIndex, indexRange); }
	public int lastIndexOfBuffer(byte b, int startIndex, int indexRange) { return XArrays.lastIndexOf(buffer, b, startIndex, indexRange); }

	@Override public int indexOfBuffer(byte[] b, int startIndex, int indexRange) { return XArrays.indexOf(buffer, b, startIndex, indexRange); }
	@Override public int lastIndexOfBuffer(byte[] b, int startIndex, int indexRange) { return XArrays.lastIndexOf(buffer, b, startIndex, indexRange); }


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


	@Override public byte[] array_empty() { return EMPTY_BYTE_BUFFER;}
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
		int 	read = reader.read(this.buffer, position, len);//Never cross the line
		return 	read;
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

}
