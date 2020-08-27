package top.fols.box.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import top.fols.box.io.interfaces.XInterfaceGetOriginStream;
import top.fols.box.time.XTimeTool;

public class XCycleSpeedLimiter {

	private volatile long cycleTime;
	private volatile long nowAccess;
	private volatile long maxAccess;

	private volatile long lastAccessTime;

	private Object lock;



	private boolean limiter = false;
	public XCycleSpeedLimiter limit(boolean b) {
		this.limiter = true;
		return this;
	}
	public boolean isLimit() {
		return this.limiter;
	}

	private static long time() throws RuntimeException {
		long time = XTimeTool.currentTimeMillis();
		return time;
	}

	public XCycleSpeedLimiter() {
		this(0, Long.MAX_VALUE);
	}
	public XCycleSpeedLimiter(long cycle, long max) {
		this.cycleTime = cycle;
		this.nowAccess = 0;
		this.maxAccess = max;

		this.lastAccessTime = this.time();
		this.lock = new Object();
		this.limiter = cycle <= 0;
	}


	public XCycleSpeedLimiter setCycleAccessMax(long maxAccess) throws RuntimeException {
		if (maxAccess <= 0) {
			throw new RuntimeException("<=0");
		}
		this.maxAccess = maxAccess;
		return this;
	}
	public long getCycleAccessMax() {
		return this.maxAccess;
	}

	public boolean isNowCycleEnd() {
		long newTime = this.time();
		if (newTime - this.lastAccessTime >= this.cycleTime) {
			return true;
		}
		return false;
	}
	public long getNowCycleAccess() {
		if (this.isNowCycleEnd()) {
			return 0;
		}
		return this.nowAccess;
	}

	public long getLastCycleTime() {
		return this.lastAccessTime;
	}



	public XCycleSpeedLimiter setCycle(long cycle) throws RuntimeException {
		if (cycle < 0) {
			throw new RuntimeException("<0");
		}
		this.cycleTime = cycle;
		return this;
	}
	public long getCycle() {
		return this.cycleTime;
	}


	public XCycleSpeedLimiter access(long count) throws RuntimeException {
		if (count < 0) {
			throw new RuntimeException("count can't <0");
		}
		if (count > this.maxAccess) {
//			throw new RuntimeException("count can't > cycle max limit count");
			synchronized (this.lock) {
				while (count > 0) {
					long a = count >= this.maxAccess ?this.maxAccess: count;
					count -= a;
					this.access(a);
				}
				return this;
			}
		}
		synchronized (this.lock) {
			long time = this.time();
			if (time - this.lastAccessTime > cycleTime) {
				this.lastAccessTime = time;
				this.nowAccess = 0;
			}
			if (count <= 0) {
				return this;
			}
			if (!this.limiter || this.nowAccess + count <= this.maxAccess) {
				this.nowAccess += count;
				return this;
			} else {
				if (this.nowAccess > this.maxAccess) {
					this.nowAccess = this.maxAccess;
				} else {
					long remainder = this.maxAccess - this.nowAccess;
					this.nowAccess += remainder;
					count -= remainder;
				}

				time = this.time();
				long sleept = (this.lastAccessTime + this.cycleTime) - time;
				if (sleept > 0) {
					try {
						Thread.sleep(sleept - 1);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}

				while (true) {
					time = this.time();
					if (time - this.lastAccessTime > cycleTime) {
						this.lastAccessTime = time;
						this.nowAccess = 0;
						this.nowAccess += count;
						return this;
					}

				}

			}
		}
	}
	/**
	 *
	 */
	public XCycleSpeedLimiter access() {
		this.access(0);
		return this;
	}







	public static <T extends InputStream> SpeedLimiterInputStream<T> wrap(T in, XCycleSpeedLimiter m) {
		return new SpeedLimiterInputStream<T>(in).setSpeedLimiter(m);
	}
	public static <T extends OutputStream> SpeedLimiterOutputStream<T> wrap(T os, XCycleSpeedLimiter m) {
		return new SpeedLimiterOutputStream<T>(os).setSpeedLimiter(m);
	}



	public static class SpeedLimiterInputStream<T extends InputStream> extends InputStream
			implements XInterfaceGetOriginStream<T> {
		private T stream;
		private XCycleSpeedLimiter m;

		public int read() throws java.io.IOException {
			if (null != m) {
				m.access(1);
			}
			return stream.read();
		}

		public int read(byte[] b) throws IOException {
			return read(b, 0, b.length);
		}

		public int read(byte[] b, int off, int len) throws java.io.IOException {
			if (null != m) {
				m.access(null == b ?0: off + len > b.length ?b.length - off: len);
			}
			return stream.read(b);
		}

		public long skip(long n) throws java.io.IOException {
			return stream.skip(n);
		}

		public int available() throws java.io.IOException {
			return stream.available();
		}

		public void close() throws java.io.IOException {
			stream.close();
		}

		public synchronized void mark(int readlimit) {
			stream.mark(readlimit);
		}

		public synchronized void reset() throws java.io.IOException {
			stream.reset();
		}

		public boolean markSupported() {
			return stream.markSupported();
		}

		@Override
		public T getStream() {
			return stream;
		}

		public SpeedLimiterInputStream(T in) {
			this.stream = in;
			this.m = m;
		}

		public XCycleSpeedLimiter getSpeedLimiter() {
			return this.m;
		}
		public SpeedLimiterInputStream<T> setSpeedLimiter(XCycleSpeedLimiter limiter) {
			this.m = limiter;
			return this;
		}

	}

	public static class SpeedLimiterOutputStream<T extends OutputStream> extends OutputStream
			implements XInterfaceGetOriginStream<T> {
		private T stream;
		private XCycleSpeedLimiter m;

		public void write(int p1) throws java.io.IOException {
			if (null != m) {
				m.access(1);
			}
			stream.write(p1);
		}

		public void write(byte[] b) throws java.io.IOException {
			write(b, 0, b.length);
		}

		public void write(byte[] b, int off, int len) throws java.io.IOException {
			if (null != m) {
				m.access(null == b ?0: off + len > b.length ?b.length - off: len);
			}
			stream.write(b, off, len);
		}

		public void flush() throws java.io.IOException {
			stream.flush();
		}

		public void close() throws java.io.IOException {
			stream.close();
		}

		public SpeedLimiterOutputStream(T in) {
			this.stream = in;
			this.m = m;
		}

		@Override
		public T getStream() {
			return stream;
		}

		public XCycleSpeedLimiter getSpeedLimiter() {
			return this.m;
		}
		public SpeedLimiterOutputStream<T> setSpeedLimiter(XCycleSpeedLimiter limiter) {
			this.m = limiter;
			return this;
		}


	}
}
