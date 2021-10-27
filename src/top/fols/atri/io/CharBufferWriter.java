package top.fols.atri.io;

import top.fols.atri.io.buffer.chars.CharArrayBuffer;
import top.fols.atri.lang.Finals;
import top.fols.atri.util.Releasable;

import java.io.IOException;
import java.io.Writer;

public class CharBufferWriter extends Writer implements Releasable {
	private int mark;
	private CharArrayBuffer buffer;

	public CharBufferWriter() { 
		this(Finals.EMPTY_CHAR_ARRAY);
	}
	public CharBufferWriter(char[] array) {
		this(array, 0, array.length);
	}
	public CharBufferWriter(char[] array, int offset, int limit) {
		this.buffer = new CharArrayBuffer(array, offset, limit);
	}




	@Override
	public void write(int c) {
		this.buffer.append((char)c);
	}
    @Override
	public void write(char[] cbuf) {
		this.write(cbuf, 0, cbuf.length);
	}
	@Override
	public void write(char[] p1, int p2, int p3) {
		this.buffer.append(p1, p2, p3);
	}

	@Override
	public void write(String str) {
		this.write(null == str ?str = "null": str, 0, str.length());
	}
    @Override
	public void write(String str, int off, int len) {
		this.buffer.append(str, off, len);
	}

    @Override
	public Writer append(CharSequence csq) {
		this.append(null == csq ?csq = "null": csq, 0, csq.length());
		return this;
	}
    @Override
	public Writer append(CharSequence csq, int start, int end) {
		this.buffer.append(csq, start, end - start);
		return this;
	}
    @Override
	public Writer append(char c) {
		this.buffer.append(c);
		return this;
	}




    @Override
	public void flush() {
		return;
	}
	@Override
	public void close() {
		this.buffer.remove();
	}

	public void mark(int readlimit) {
		this.mark = this.buffer.position();
	}
    public void reset() {
		this.buffer.position(this.mark);
	}


	public void writeTo(Writer out) throws IOException {
		out.write(this.buffer.buffer_inner(), this.buffer.position(), this.limit());
	}

	public char[] toCharArray() {
		return this.buffer.toArray();
	}


	@Override
	public String toString() {
		return new String(this.buffer.buffer_inner(), this.buffer.position(), this.buffer.available());
	}

	
	
	public int available() { return this.buffer.available(); }
	
	public int 	limit() { return this.buffer.available(); }
	public void limit(int limit) throws ArrayIndexOutOfBoundsException {
		this.buffer.limit(limit);
		this.position(Math.min(this.position(), limit));
	}
	
	public CharArrayBuffer buffer() { return this.buffer; }
	public int  position() { return this.buffer.position(); }
	public void position(int position) { this.buffer.position(position); }

	@Override
	public boolean release() {
		this.buffer = null;
		this.mark = -1;
		return true;
	}

	@Override
	public boolean released() {
		return null == this.buffer;
	}
}
