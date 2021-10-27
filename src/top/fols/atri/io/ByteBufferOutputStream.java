package top.fols.atri.io;

import top.fols.atri.io.buffer.bytes.ByteArrayBuffer;
import top.fols.atri.lang.Finals;
import top.fols.atri.util.Releasable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class ByteBufferOutputStream extends OutputStream implements Releasable {
	private int mark;
	private ByteArrayBuffer buffer;

	public ByteBufferOutputStream() { 
		this(Finals.EMPTY_BYTE_ARRAY);
	}
	public ByteBufferOutputStream(byte[] array) {
		this(array, 0, array.length);
	}
	public ByteBufferOutputStream(byte[] array, int offset, int limit) { this.buffer = new ByteArrayBuffer(array, offset, limit); }


	@Override
	public void write(int p1) {
		this.buffer.append((byte)p1);
	}

    @Override
	public void write(byte[] b) {
		this.write(b, 0, b.length);
	}

	@Override
	public void write(byte[] b, int off, int len) {
		this.buffer.append(b, off, len);
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
	
	
	public void writeTo(OutputStream out) throws IOException {
		out.write(this.buffer.buffer_inner(), this.buffer.position(), this.limit());
	}

	public byte[] toByteArray() {
		return this.buffer.toArray();
	}
	
	
	@Override
	public String toString() {
		return new String(this.buffer.buffer_inner(), this.buffer.position(), this.buffer.available());
	}
	public String toString(String charsetName) throws UnsupportedEncodingException {
		return new String(this.buffer.buffer_inner(), this.buffer.position(), this.buffer.available(), charsetName);
	}
	public String toString(Charset charset) {
		return new String(this.buffer.buffer_inner(), this.buffer.position(), this.buffer.available(), charset);
	}
	
	public int available() { return this.buffer.available(); }
	
	public int 	limit() { return this.buffer.limit(); }
	public void limit(int limit) throws ArrayIndexOutOfBoundsException {
		this.buffer.limit(limit);
		this.position(Math.min(this.position(), limit));
	}

	public ByteArrayBuffer buffer() { return this.buffer; }

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
