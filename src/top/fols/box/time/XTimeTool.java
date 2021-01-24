package top.fols.box.time;


public final class XTimeTool {


	/**
	 * @see java.lang.System#currentTimeMillis()
	 */
	public static final long getSystemCurrentTimeMillis() {
		return System.currentTimeMillis();
	}
	/**
	 * @see java.lang.System#nanoTime()
	 */
	public static final long getSystemNanoTime() {
		return System.nanoTime();
	}






	public static long currentTimeMillis() { return System.currentTimeMillis(); }
	public static long nanoTime() { return System.nanoTime(); }




	/**
	 * Test
	 * Use the difference of nanoTime to calculate the time to achieve currentTimeMillis
	 * Solve the problem of high concurrency and low efficiency of currentTimeMillis
	 * The time must be read once within the data overflow
	 * It is not too precise
	 *
	 * 使用nanoTime的差 来计算时间 实现currentTimeMillis, 解决currentTimeMillis 高并发，低效率的问题
	 * 在数据溢出内必须读取一次时间, 同时设备不可以是休眠状态
	 *
	 * @hide
	 */
	public final static class ConcurrencyCurrentTimeMillis {

		/**
		 * Guarantee only one instance
		 */
		private static final ConcurrencyCurrentTimeMillis DEFAULT_INSTANCE = new ConcurrencyCurrentTimeMillis();
		public static long currentTimeMillis() {
			return DEFAULT_INSTANCE.getCurrentTimeMillis();
		}
		public static long nanoTime() {
			return DEFAULT_INSTANCE.getNanoTime();
		}




		private volatile TimeRecording timeRecording;
		private final class TimeRecording {
			private final long time;
			private final long nanotime;
			private TimeRecording(long time, long nanotime) {
				this.time = time;
				this.nanotime = nanotime;
			}
		}
		protected ConcurrencyCurrentTimeMillis() {
			this.timeRecording = new TimeRecording(
					System.currentTimeMillis(),
					System.nanoTime()
			);
		}





		/**
		 * Because there may be unforeseen problems without synchronized, I don't know.
		 */
		protected long getCurrentTimeMillis() {

			/**
			 * We don’t need to know how it is achieved
			 */
			long currentNanotime = System.nanoTime();


			TimeRecording last = this.timeRecording;
			long lastTime = last.time;
			long lastNanotime = last.nanotime;

			/**
			 * When nanotime overflow
			 */
			if (lastNanotime >= 0) {
				if (currentNanotime < 0) { // currentNanotime:-1 - lastNanotime:1
					this.timeRecording = new TimeRecording(
							lastTime = System.currentTimeMillis(),
							currentNanotime
					);
					return lastTime;
				}
			} else {
				if (currentNanotime >= 0) {// currentNanotime:1 - lastNanotime: -1
					this.timeRecording = new TimeRecording(
							lastTime = System.currentTimeMillis(),
							currentNanotime
					);
					return lastTime;
				}
			}

			long nanotimeDifference;
			if ((nanotimeDifference = (currentNanotime - lastNanotime)) < 0) { // currentNanotime > lastNanotime
				this.timeRecording = new TimeRecording(
						lastTime = System.currentTimeMillis(),
						currentNanotime
				);
				return lastTime;
			}


			/*
			 *   181963650265951 					nanosecond
			 *  	/ 1000000000L 	= 181963 		seconds
			 *  	/    1000000L 	= 181963650 	milliseconds
			 */
			return lastTime + (nanotimeDifference / NANO_TO_MILLISECONDS);
		}
		protected long getNanoTime() {
			return System.nanoTime();
		}

		protected static final long NANO_TO_MILLISECONDS 	= 1000000L;

		/**
		 * Prevent data overflow twice
		 */
		static {
			try {
				new Heartbeat().start();
			} catch (Throwable e) { throw new Error(e); }
		}
		static protected class Heartbeat extends Thread {
			private static final long HEARTBEAT_TIME = XTimeTool.time_1d;
			private Heartbeat() {
				try {
					super.setName(this.getClass().getName());
					super.setPriority(Thread.MIN_PRIORITY);
				} catch (Throwable e) { throw new Error(e); }
			}
			@Override
			public void run() {
				// TODO: Implement this method
				while (true) {
					try {
						ConcurrencyCurrentTimeMillis.currentTimeMillis();
//						System.out.println("heartbeat: " + System.currentTimeMillis());
						super.sleep(HEARTBEAT_TIME);
					} catch (Throwable e) {
					}
				}
			}
		}




	}







	public static final long time_1millisecond = 1L;// 一毫秒
	public static final long time_1s = time_1millisecond * 1000L;// 一秒
	public static final long time_1m = 60L * time_1s;// 一分钟
	public static final long time_1h = 60L * time_1m;// 一小时
	public static final long time_1d = 24L * time_1h;// 一天


	public static long millis(long day, long hour, long minute, long second, long millis) {
		return day * time_1d
				+ hour * time_1h
				+ minute * time_1m
				+ second * time_1s
				+ millis * time_1millisecond;
	}

	public static long getNextSecondStartTime(long nowTime, long s) {
		return time_1s * ((nowTime / time_1s) + s);
	}
	public static long getNextMinuteStartTime(long nowTime, long m) {
		return time_1m * ((nowTime / time_1m) + m);
	}
	public static long getNextHourStartTime(long nowTime, long h) {
		return time_1h * ((nowTime / time_1h) + h);
	}
	public static long getNextSecondTime(long nowTime, long s) {
		return nowTime + time_1s * s;
	}
	public static long getNextMinuteTime(long nowTime, long m) {
		return nowTime + time_1m * m;
	}
	public static long getNextHourTime(long nowTime, long h) {
		return nowTime + time_1h * h;
	}
	public static long getNextDayTime(long nowTime, long d) {
		return nowTime + time_1d * d;
	}


}

