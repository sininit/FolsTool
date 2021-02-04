package top.fols.atri.io;

import top.fols.atri.io.buffer.bytes.ByteArrayBuffer;
import top.fols.atri.util.Releasable;

import java.io.IOException;
import java.io.InputStream;


public class ByteBufferInputStream extends InputStream implements Releasable {
	private int mark;
	private ByteArrayBuffer buffer;

	public ByteBufferInputStream(byte[] array) {
		this(array, 0, array.length);
	}
	public ByteBufferInputStream(byte[] array, int offset, int limit) {
		this.buffer = new ByteArrayBuffer(array, offset, limit);
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
		long k = this.buffer.limit() - this.buffer.position();
		if (n < k) {
			k = n < 0 ? 0 : n;
		}
		int ki = (int) k;
		this.buffer.positionSkip(ki);
		return ki;
	}

    public void close() { this.buffer.remove(); }
    public void mark(int readlimit) { this.mark = this.buffer.position(); }
    public void reset() { this.buffer.position(this.mark); }
    public boolean markSupported() { return true; }


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
