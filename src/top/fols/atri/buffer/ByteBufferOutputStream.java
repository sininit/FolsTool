package top.fols.atri.buffer;

import top.fols.atri.buffer.bytes.ByteArrayBuffer;
import top.fols.atri.lang.Finals;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class ByteBufferOutputStream extends OutputStream {
	private int mark;
	private ByteArrayBuffer buffer;

	public ByteBufferOutputStream() { 
		this(Finals.EMPTY_BYTE_BUFFER);
	}
	public ByteBufferOutputStream(byte[] array) {
		this(array, 0, array.length);
	}
	public ByteBufferOutputStream(byte[] array, int offset, int length) {
		this.buffer = new ByteArrayBuffer(array, offset, length);
	}


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
		this.mark = this.buffer.position;
	}
    public void reset() {
		this.buffer.position(this.mark);
	}
	
	
	public void writeTo(OutputStream out) throws IOException {
		out.write(this.buffer.buffer, this.buffer.position, this.size());
	}

	public byte[] toByteArray() {
		return this.buffer.toArray();
	}
	
	
	@Override
	public String toString() {
		return new String(this.buffer.buffer, this.buffer.position, this.buffer.available());
	}
	public String toString(String charsetName) throws UnsupportedEncodingException {
		return new String(this.buffer.buffer, this.buffer.position, this.buffer.available(), charsetName);
	}
	
	
	public int available() { return this.buffer.available(); }
	
	public int  size() { return this.buffer.size; }
	public void size(int size) throws ArrayIndexOutOfBoundsException {
		this.buffer.size(size);
		this.position(this.position() > size?size:this.position());
	}

	public ByteArrayBuffer buffer() { return this.buffer; }
	public void releaseBuffer() {
		// TODO: Implement this method
		this.buffer.remove();
	}

	public int  position() { return this.buffer.position; }
	public void position(int position) { this.buffer.position(position); }
}
