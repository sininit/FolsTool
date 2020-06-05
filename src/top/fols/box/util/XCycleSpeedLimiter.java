package top.fols.box.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.interfaces.XInterfaceGetOriginStream;
import top.fols.box.time.XTimeTool;

public class XCycleSpeedLimiter implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private volatile long cycle;
	private volatile long cycleUpdateTime;
	private volatile long cyclemaxspeed;
	private volatile long cyclespeednow;// 周期时间内的速率
	private volatile boolean isLimit;
	private final Object lock = new Object();

	public XCycleSpeedLimiter() {
		this(false);
	}

	public XCycleSpeedLimiter(boolean isLimiter) {
		this.cycle = XTimeTool.time_1s;
		this.cyclespeednow = 0;
		this.cyclemaxspeed = 8192;
		this.cycleUpdateTime = XTimeTool.currentTimeMillis();
		this.isLimit = isLimiter;
		this.averageSpeed.setPerCycleSize(XTimeTool.time_1s);
	}

	public int waitForFreeInt(
			@XAnnotations("max length is preferably waitfor free length multiples") int waiforFreeLength) {
		return (int) waitForFreeLong(waiforFreeLength, true);
	}

	public long waitForFreeLong(
			@XAnnotations("max length is preferably waitfor free length multiples") long waiforFreeLength) {
		return waitForFreeLong(waiforFreeLength, true);
	}

	public long waitForFreeLong(long waiforFreeLength, boolean mustBeTheSpecifiedLength) {
		if (isLimit && (mustBeTheSpecifiedLength && waiforFreeLength > cyclemaxspeed)) {
			throw new NumberFormatException("pieceLength can't > cyclemaxspeed");
		}
		synchronized (lock) {
			while (true) {
				long newTime = XTimeTool.currentTimeMillis();
				if (newTime - cycleUpdateTime >= cycle) {
					cycleUpdateTime = newTime;
					cyclespeednow = 0;
				}
				if (!isLimit || cyclespeednow + waiforFreeLength <= cyclemaxspeed)
					break;
				if (!mustBeTheSpecifiedLength && cyclemaxspeed - cyclespeednow > 0) {
					waiforFreeLength = cyclemaxspeed - cyclespeednow;
					break;
				}
			}
			averageSpeed.addForFreeLong(waiforFreeLength);
			cyclespeednow += waiforFreeLength;
			return waiforFreeLength;
		}
	}

	public long getCycleUseSpeed() {
		if (isCycleProcessEnd()) {
			return 0;
		}
		return this.cyclespeednow;
	}

	public long getCycleMaxSpeed() {
		return this.cyclemaxspeed;
	}

	public XCycleSpeedLimiter setCycleMaxSpeed(long cycleMaxSpeed) throws NumberFormatException {
		if (cycleMaxSpeed < 0) {
			throw new NumberFormatException("size error cycleMaxSpeed " + cycleMaxSpeed);
		}
		this.cyclemaxspeed = cycleMaxSpeed;
		// this.cyclespeednow = 0;
		// this.backTime = currentTimeMillis();
		return this;
	}

	public XCycleSpeedLimiter setCycle(long cycle) {
		if (cycle < 0) {
			throw new NumberFormatException("size error cycle " + cycle);
		}
		this.cycle = cycle;
		// this.cyclespeednow = 0;
		// this.backTime = currentTimeMillis();
		return this;
	}

	public long getCycle() {
		return this.cycle;
	}

	public long getCycleFreeSpeed() {
		if (!isLimit) {
			return cyclemaxspeed;
		}
		long length;
		return (length = getCycleMaxSpeed() - getCycleUseSpeed()) < 0 ? 0 : length;
	}

	public XCycleSpeedLimiter setLimit(boolean b) {
		this.isLimit = b;
		return this;
	}

	public boolean isLimit() {
		return this.isLimit;
	}

	public String toString() {
		return String.format("[cycle=%s, cyclemaxspeed=%s, cyclespeednow=%s, islimit=%s]", getCycle(),
				getCycleMaxSpeed(), getCycleUseSpeed(), isLimit);
	}

	public boolean isCycleProcessEnd() {
		long newTime = XTimeTool.currentTimeMillis();
		if (newTime - cycleUpdateTime >= cycle) {
			return true;
		}
		return false;
	}

	private XAvgergeCycleSpeedCalc averageSpeed = new XAvgergeCycleSpeedCalc();

	public double getAverageSpeed() {
		double speed = averageSpeed.getAvgergeCycleSpeed();
		return speed;
	}

	public long getAverageSpeedUpdateCycleSize() {
		return averageSpeed.getPerCycleSize();
	}

	public XCycleSpeedLimiter setAverageSpeedUpdateCycleSize(long cycle) {
		averageSpeed.setPerCycleSize(cycle);
		return this;
	}

	public static <T extends InputStream> SpeedLimiterInputStream<T> wrap(T in, XCycleSpeedLimiter m) {
		return new SpeedLimiterInputStream<T>(in, m);
	}

	public static <T extends OutputStream> SpeedLimiterOutputStream<T> wrap(T os, XCycleSpeedLimiter m) {
		return new SpeedLimiterOutputStream<T>(os, m);
	}

	public static class SpeedLimiterInputStream<T extends InputStream> extends InputStream
			implements XInterfaceGetOriginStream<T> {
		private T stream;
		private XCycleSpeedLimiter m;

		public int read() throws java.io.IOException {
			if (null != m) {
				m.waitForFreeInt(1);
			}
			return stream.read();
		}

		public int read(byte[] b) throws IOException {
			return read(b, 0, b.length);
		}

		public int read(byte[] b, int off, int len) throws java.io.IOException {
			if (null != m) {
				m.waitForFreeInt(len);
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

		public SpeedLimiterInputStream(T in, XCycleSpeedLimiter m) {
			this.stream = in;
			this.m = m;
		}

		public XCycleSpeedLimiter getSpeedLimiter() {
			return this.m;
		}
	}

	public static class SpeedLimiterOutputStream<T extends OutputStream> extends OutputStream
			implements XInterfaceGetOriginStream<T> {
		private T stream;
		private XCycleSpeedLimiter m;

		public void write(int p1) throws java.io.IOException {
			if (null != m) {
				m.waitForFreeInt(1);
			}
			stream.write(p1);
		}

		public void write(byte[] b) throws java.io.IOException {
			write(b, 0, b.length);
		}

		public void write(byte[] b, int off, int len) throws java.io.IOException {
			if (null != m) {
				m.waitForFreeInt(len);
			}
			stream.write(b, off, len);
		}

		public void flush() throws java.io.IOException {
			stream.flush();
		}

		public void close() throws java.io.IOException {
			stream.close();
		}

		public SpeedLimiterOutputStream(T in, XCycleSpeedLimiter m) {
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
	}
}
