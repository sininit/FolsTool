package top.fols.atri.buffer;
import top.fols.atri.buffer.chars.CharArrayBuffer;

import java.io.Reader;

public class CharBufferReader extends Reader {
	private int mark;
	private CharArrayBuffer buffer;

	public CharBufferReader(char[] array) {
		this(array, 0, array.length);
	}
	public CharBufferReader(char[] array, int offset, int limit) { this.buffer = new CharArrayBuffer(array, offset, limit); }
	
    public int read(java.nio.CharBuffer target) throws java.io.IOException {
		return super.read(target);
	}
    public int read() {
		return this.buffer.readAvailable();
	}
    public int read(char[] cbuf) {
		return this.read(cbuf, 0, cbuf.length);
	}
    public int read(char[] p1, int p2, int p3){
		return this.buffer.readAvailable(p1,p2,p3);
	}

    public long skip(long n) {
		long k = this.buffer.limit - this.buffer.position;
		if (n < k) {
			k = n < 0 ? 0 : n;
		}
		int ki = (int) k;
		this.buffer.positionSkip(ki);
		return ki;
	}
	
	
    public boolean ready() { return this.buffer.available() > 0; }
    public boolean markSupported() { return true; }
    public void mark(int readAheadLimit) { this.mark = this.buffer.position; }
    public void reset() { this.buffer.position(this.mark); }
    public void close() { this.buffer.remove(); }
	
	
	
	public int available() { return this.buffer.available(); }
	
	public int 	limit() { return this.buffer.limit; }
	public void limit(int limit) throws ArrayIndexOutOfBoundsException {
		this.buffer.limit(limit);
		this.position(this.position() > limit?limit:this.position());
	}

	public CharArrayBuffer buffer() { return this.buffer; }
	public void releaseBuffer() {
		// TODO: Implement this method
		this.buffer.remove();
	}

	public int  position() { return this.buffer.position; }
	public void position(int position) { this.buffer.position(position); }
	
}
