package top.fols.box.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import top.fols.atri.lang.Mathz;
import top.fols.atri.lang.Objects;
import top.fols.atri.time.Times;
import top.fols.box.util.CycleDigitalRecorder;
import top.fols.box.util.SizeUnit;

/** 
 * restrict broadband and automatically determine broadband bandwidth
 */
public class BandwidthLimiter {

//	static public void main(String[] args) {
//		test.test(args);
//	}
	static class test {
		static void test(String[] args) {

			int    defaultBandwidthSpeed     = SizeUnit.FILE_SIZE_1024_SIZE_UNIT.parseValue("1MB").intValue(); // 默认最小宽带速度，单位：字节/秒
			double maxBandwidthRatio         = 0.9; // 总宽带占比

			BandwidthLimiter bandwidthLimiter = new BandwidthLimiter(defaultBandwidthSpeed, maxBandwidthRatio);

			final long time  = System.currentTimeMillis() + 10000;
			final long time2 = System.currentTimeMillis() + 20000;
			for (int i = 0; i < 10 ; i++) {
				final InputStream wrappedInputStream = bandwidthLimiter.wrapStream(new InputStream(){
						@Override
						public int read() {
							// TODO: Implement this method
							return '6';
						}

						@Override
						public int read(byte[] b, int off, int len) {
							for (int i = 0; i < len; i++) {
								b[off + i] = '6';
							}
							return len;
						}
					}, null).getInputStream();
				new Thread(){
					@Override
					public void run() {
						try {
							byte[] buffer = new byte[8192];
							int bytesRead;

							while ((bytesRead = wrappedInputStream.read(buffer)) != -1) {
								long t = System.currentTimeMillis();
								if (t > time2) {
								} else if (t > time) {
									Thread.sleep(1);
								}
								String chunk = new String(buffer, 0, bytesRead);
								//System.out.println("read: " + chunk);
							}
							wrappedInputStream.close();
						} catch (Exception e) {}
					}
				}.start();
			}

			long st = System.currentTimeMillis();
			while (true) {
				System.out.println(System.currentTimeMillis() - st);
				System.out.println("currentBandwidthSpeed:"       			  + bandwidthLimiter.getCurrentBandwidthSpeed());
				System.out.println("currentBandwidthLimitSpeed:"       		  + bandwidthLimiter.getCurrentBandwidthSpeedLimit());
				System.out.println("currentBandwidthFillLoadAssertThreshold:" + bandwidthLimiter.dCurrentBandwidthFillLoadAssertThreshold);
				System.out.println("avg: " + bandwidthLimiter.setCurrentBandwidthAvgSpeed());
				System.out.println(Arrays.toString(bandwidthLimiter.recorder.getRecordValues()));
				System.out.println("currentUnlimit:" + bandwidthLimiter.dFillLoadUnlimit);

				Times.sleep(1000);
			}
		}
	}





	private Object lock = new Object();

	private long   defaultBandwidthSpeed;
	private double limitBandwidthRatio;     		    	// 总宽带占比

	private final int    lowerLoadAssertSecond = 6; 
	private final double fillLoadAssertThresholdRatio = .95; // dCurrentBandwidthSpeed * limitBandwidthRatio * fillLoadAssertThresholdRatio


	private boolean 	 autoUpdateBandwidthSpeed;
	private int     	 fillLoadUnlimitSecond; 	         // 已满载的单位时间 达到多少秒解除速度限制      
	private int     	 fillLoadUnlimitSecondCloseSecond;   // 已满载的单位时间 达到多少秒解除速度限制 多少秒关闭解除限速功能 单位：秒


	private long   		dCurrentBandwidthSpeed;          		  // 当前宽带速度，单位：字节/秒
	private long        dCurrentBandwidthSpeedLimit;
	private long   		dCurrentBandwidthFillLoadAssertThreshold; // 满载判断阀值
	private boolean 	dFillLoadUnlimit;
	private long    	dFillLoadUnlimitStartTime;
	private long    	dFillLoadUnlimitEndTime;
    private long    	dLStartTime;
	private long    	dNStartTime;
    private long    	dBytesRead;       // 单位时间已读取字节数
	private long    	dCFillLoadSecond; // 当前已连续满载的单位时间
	private long    	dCLowerLoadSecond;

	private CycleDigitalRecorder recorder;


	public BandwidthLimiter(double maxBandwidthRatio) {
		this(8192 * 1024,
			 maxBandwidthRatio);
	}
	public BandwidthLimiter(long defaultBandwidthSpeed, double maxBandwidthRatio) {
		this(defaultBandwidthSpeed, 
			 maxBandwidthRatio,
			 true, 6, 3);
	}
    public BandwidthLimiter(long defaultBandwidthSpeed, double maxBandwidthRatio,
							boolean fillLoadAutoSpeedUp,
							int 	fillLoadUnlimitSecond,
							int 	fillLoadUnlimitOverSecond) {
		Objects.requireGreater(defaultBandwidthSpeed, 0, "default bandwidth speed");

		this.defaultBandwidthSpeed = defaultBandwidthSpeed;
		this.limitBandwidthRatio = maxBandwidthRatio;

		this.setAutoUpdateBandwidthSpeedParameter(fillLoadAutoSpeedUp,
												  fillLoadUnlimitSecond, fillLoadUnlimitOverSecond);
		this.reset();
    }

	public void setCurrentBandwidthSpeed(long b) {
		this.dCurrentBandwidthSpeed                   = b;
		this.dCurrentBandwidthSpeedLimit              = (long) (limitBandwidthRatio * b);
		this.dCurrentBandwidthFillLoadAssertThreshold = (long) (dCurrentBandwidthSpeedLimit * fillLoadAssertThresholdRatio); //fillLoad assert
	}
	public void setLimitBandwidthRatio(double ratio) {
		this.limitBandwidthRatio = ratio;
	}
	public void  setAutoUpdateBandwidthSpeedParameter(boolean fillLoadAutoSpeedUp,
													  int 	 fillLoadUnlimitSecond,
													  int 	 fillLoadUnlimitOverSecond) {
		if (this.autoUpdateBandwidthSpeed = fillLoadAutoSpeedUp) {
			this.fillLoadUnlimitSecond            = Objects.requireGreater(fillLoadUnlimitSecond,     0, "fillLoadUnlimitSecond");
			this.fillLoadUnlimitSecondCloseSecond = Objects.requireGreater(fillLoadUnlimitOverSecond, 0, "fillLoadUnlimitOverSecond");
		}
	}

	public long   getCurrentBandwidthSpeed()    		   { return dCurrentBandwidthSpeed; }
	public long   getCurrentBandwidthSpeedLimit()    	   { return dCurrentBandwidthSpeedLimit; }
	public double getCurrentLimitBandwidthRatio() 		   { return limitBandwidthRatio;}
	public long   getCurrentLimitBandwidthSpeedThreshold() { return dCurrentBandwidthFillLoadAssertThreshold; }

	public boolean isAutoUpdateBandwidthSpeed() { return autoUpdateBandwidthSpeed; }


	public void reset() {
		this.setCurrentBandwidthSpeed(defaultBandwidthSpeed);
		this.resetRecord(System.currentTimeMillis());
		this.recorder = new CycleDigitalRecorder(1000, 7);
	}
	public void resetRecord(long currentTime) {
		this.dLStartTime       = Times.getNextSecondStartTime(currentTime, 0);
		this.dNStartTime       = Times.getNextSecondStartTime(currentTime, 1);
		this.dBytesRead        = 0;
		this.dCFillLoadSecond  = 0;
		this.dCLowerLoadSecond = 0;
	}
	public void close() {
		this.lock = null;
	}


	public long setCurrentBandwidthAvgSpeed() {
		return recorder.getRecordValueAvg();
	}









    public int accessLength(int len) {
		if (len < 0) throw new UnsupportedOperationException("len: " + len);

		synchronized (lock) {
			long currentTime  = System.currentTimeMillis();

			U: if (autoUpdateBandwidthSpeed) {
				if (!dFillLoadUnlimit && dCFillLoadSecond >= fillLoadUnlimitSecond) {
					dFillLoadUnlimit          = true;
					dFillLoadUnlimitStartTime = Times.getNextSecondStartTime(currentTime, 0);
					dFillLoadUnlimitEndTime   = Times.getNextSecondStartTime(currentTime, fillLoadUnlimitSecondCloseSecond);

					this.recorder.addCurrentRecordValue(dBytesRead);
					this.resetRecord(currentTime); 
				}
				if (dFillLoadUnlimit) {
					if (currentTime >= this.dNStartTime) {
						this.recorder.addCurrentRecordValue(dBytesRead);
						this.resetRecord(currentTime);

						if ((currentTime >= dFillLoadUnlimitEndTime)) {
							dFillLoadUnlimit     = false;
							break U;
						}
					}
					//speed up
					if ((dBytesRead += len) > dCurrentBandwidthSpeed) 
						setCurrentBandwidthSpeed(dBytesRead);
					return len;
				} 
			}
			D: while (true) {
				if (currentTime >= this.dNStartTime) {
					this.recorder.addCurrentRecordValue(dBytesRead);

					if (dBytesRead < dCurrentBandwidthFillLoadAssertThreshold) {
						this.dCLowerLoadSecond++;
						this.dCFillLoadSecond = 0;

						//deceleration
						Assert: 
						if (dCLowerLoadSecond >= lowerLoadAssertSecond) {
							if (autoUpdateBandwidthSpeed) {
								long[] dBuffer = recorder.getRecordValues();
								long sum = 0;
								boolean hasZero = false;
								for (long i: dBuffer) {
									sum     += i;
									hasZero |= i == 0;
								}
								if (!hasZero) {
									long avg = sum / dBuffer.length;
									if ((avg > 0)) { 
										long avgThreshold = this.dCurrentBandwidthFillLoadAssertThreshold;
										if ((avgThreshold > 0)) {
											for (long i: dBuffer) 
												if (i > avgThreshold) 
													break Assert;
											setCurrentBandwidthSpeed(avg);
										}
									}
								}
								dCLowerLoadSecond = 0;
							}
						}
					} else {
						this.dCLowerLoadSecond = 0;
						this.dCFillLoadSecond++;
					}
					this.dLStartTime  = Times.getNextSecondStartTime(currentTime, 0);
					this.dNStartTime  = Times.getNextSecondStartTime(currentTime, 1);
					this.dBytesRead   = 0;
				}
				long readed   = dBytesRead;
				long readnext = readed + len;
				if ((readnext <= dCurrentBandwidthSpeedLimit)) {
					dBytesRead = readnext;
					return len;
				} else {
					long a = dCurrentBandwidthSpeedLimit - readed;
					if  (a > 0) {
						dBytesRead += a;
						return Mathz.toIntExact(a);
					} 
				}
				sleep(dNStartTime - currentTime);
				currentTime = System.currentTimeMillis();
			}
		}
    }


	public void sleep(long time) {
		try {
			if (time > 0)
				Thread.sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}



	
	
	
	
	
	




	public InOutStream wrapStream(InputStream input, OutputStream output) {
		if (null == input &&
			null == output)
			return null;

		if (null == input) 
			return new InOutStream(null, new WrapOutputStream(output));
		if (null == output) 
			return new InOutStream(new WrapInputStream(input), null);

		return new InOutStream(new WrapInputStream(input), output);
	}
	public static class InOutStream {
		private final InputStream  inputStream;
		private final OutputStream outputStream;
		private InOutStream(InputStream inputStream, OutputStream stream) {
			this.inputStream = inputStream;
			this.outputStream      = stream;
		}

		public InputStream getInputStream() {
			return inputStream;
		}
		public OutputStream getOutputStream() {
			return outputStream;
		}
	}




    private class WrapInputStream extends InputStream {
        private final InputStream inputStream;
        public WrapInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int read() throws IOException {
			accessLength(1);
            return inputStream.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
//			if (off < 0 || len < 0 || off + len > b.length) 
//				throw new IOException("length=" + b.length + ", off=" + off + ", count=" + len);
			int request = accessLength(len);
            return inputStream.read(b, off, request);
        }

		@Override
		public long skip(long n) throws java.io.IOException { return inputStream.skip(n); }

		@Override
        public int available() throws java.io.IOException { return inputStream.available(); }

		@Override
        public synchronized void mark(int readlimit) { inputStream.mark(readlimit); }

		@Override
        public synchronized void reset() throws java.io.IOException { inputStream.reset(); }

		@Override
        public boolean markSupported() {return inputStream.markSupported(); }

        @Override
        public void close() throws IOException {
            inputStream.close();
        }
    }


    private class WrapOutputStream extends OutputStream {
        private final OutputStream stream;
        public WrapOutputStream(OutputStream inputStream) {
            this.stream = inputStream;
        }

		@Override
        public void write(int p1) throws java.io.IOException {
			accessLength(1);
			stream.write(p1);
		}

		@Override
        public void write(byte[] b) throws java.io.IOException {
			write(b, 0, b.length);
		}

		@Override
        public void write(byte[] b, int off, int len) throws java.io.IOException {
//			if (off < 0 || len < 0 || off + len > b.length) 
//				throw new IOException("length=" + b.length + ", off=" + off + ", count=" + len);
			while (len > 0) {
				int request = accessLength(len);
				stream.write(b, off, request);
				off += request;
				len -= request;
			}
		}

		@Override
        public void flush() throws java.io.IOException {
			stream.flush();
		}

        @Override
        public void close() throws IOException {
            stream.close();
        }
    }


}

