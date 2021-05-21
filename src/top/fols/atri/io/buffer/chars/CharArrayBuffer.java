package top.fols.atri.io.buffer.chars;

import static top.fols.atri.lang.Finals.*;
import static top.fols.atri.lang.Finals.EMPTY_CHAR_ARRAY;

public class CharArrayBuffer extends CharBufferOperate {
	public CharArrayBuffer() {
		this(EMPTY_CHAR_ARRAY, 0, 0);
	}
	public CharArrayBuffer(char[] array) {
		this(array, 0, array.length);
	}
	public CharArrayBuffer(char[] buffer, int position, int limit) {
		super(buffer, position, limit);
	}

	@Override public int stream_read(char[] buf, int off, int len) { 
		return -1;
	}
}
