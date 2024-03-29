package top.fols.box.io.buffer.bytes;

import static top.fols.atri.lang.Finals.*;
public class ByteArrayBuffer extends ByteBufferOperate {
	public ByteArrayBuffer() {
		this(EMPTY_BYTE_ARRAY, 0, 0);
	}
	public ByteArrayBuffer(byte[] array) {
		this(array, 0, array.length);
	}
	public ByteArrayBuffer(byte[] buffer, int position, int limit) {
		super(buffer, position, limit);
	}

	@Override public int stream_read(byte[] buf, int off, int len) { 
		return -1;
	}
}
