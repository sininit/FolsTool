package top.fols.box.io;

import top.fols.atri.interfaces.interfaces.IInnerStream;
import top.fols.box.io.interfaces.IFixedLengthStream;

import java.io.IOException;
import java.io.Reader;

public class ReaderFixedLengths<T extends Reader> extends Reader
		implements IFixedLengthStream, IInnerStream<T> {
	private T stream;
	private long maxCount;
	private long nowCount;
	private boolean fixed;

	public ReaderFixedLengths(T stream, long maxCount) {
		if (null == stream) {
			throw new NullPointerException("stream for null");
		}
		if (maxCount < 0) {
			maxCount = 0;
		}
		this.stream = stream;
		this.maxCount = maxCount;
		this.nowCount = 0;
		this.fixed = true;
	}

	@Override
	public int read() throws IOException {
		if (fixed && nowCount + 1 > maxCount) {
			return -1;
		}
		int read = stream.read();
		if (read != -1) {
			nowCount++;
		}
		return read;
	}

	@Override
	public int read(char[] b, int off, int len) throws java.io.IOException {
		if (len < 0) {
			return -1;
		} else if (fixed) {
			if (nowCount + len > maxCount) {
				len = (int) (maxCount - nowCount);
				if (len < 1) {
					return -1;
				}
			}
			if (nowCount + len > maxCount) {
				return -1;
			}
		}
		int read = stream.read(b, off, len);
		if (read != -1) {
			nowCount += read;
		}
		return read;
	}

	@Override
	public boolean ready() throws java.io.IOException {
		return stream.ready();
	}

	@Override
	public long skip(long n) throws java.io.IOException {
		return stream.skip(n);
	}

	@Override
	public void reset() throws java.io.IOException {
		stream.reset();
	}

	@Override
	public boolean markSupported() {
		return stream.markSupported();
	}

	@Override
	public void close() throws java.io.IOException {
		stream.close();
	}

	@Override
	public void mark(int readlimit) throws IOException {
		stream.mark(readlimit);
	}

	@Override
	public long getFreeLength() {
		return maxCount - nowCount;
	}

	@Override
	public boolean isAvailable() {
		return !fixed || fixed && nowCount < maxCount;
	}

	@Override
	public long getUseLength() {
		return nowCount;
	}

	@Override
	public void resetUseLength() {
		nowCount = 0;
	}

	@Override
	public long getMaxUseLength() {
		return maxCount;
	}

	@Override
	public void setMaxUseLength(long maxCount) {
		this.maxCount = maxCount;
	}

	@Override
	public void fixed(boolean b) {
		this.fixed = b;
	}

	@Override
	public boolean isFixed() {
		return fixed;
	}

	@Override
	public T getInnerStream() {
		return stream;
	}

}
