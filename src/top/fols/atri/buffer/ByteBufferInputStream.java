package top.fols.atri.buffer;

import top.fols.atri.buffer.bytes.ByteArrayBuffer;

import java.io.InputStream;


public class ByteBufferInputStream extends InputStream {
	private int mark;
	private ByteArrayBuffer buffer;

	public ByteBufferInputStream(byte[] array) {
		this(array, 0, array.length);
	}
	public ByteBufferInputStream(byte[] array, int offset, int length) {
		this.buffer = new ByteArrayBuffer(array, offset, length);
	}


	public int read() {
		return this.buffer.readAvailable();
	}
    public int read(byte[] b) {
		return this.read(b, 0, b.length);
	}
    public int read(byte[] b, int off, int len) {
		return this.buffer.readAvailable(b, off, len);
	}
    public long skip(long n) {
		long k = this.buffer.size - this.buffer.position;
		if (n < k) {
			k = n < 0 ? 0 : n;
		}
		int ki = (int) k;
		this.buffer.positionSkip(ki);
		return ki;
	}

    public void close() { this.buffer.remove(); }
    public void mark(int readlimit) { this.mark = this.buffer.position; }
    public void reset() { this.buffer.position(this.mark); }
    public boolean markSupported() { return true; }


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
