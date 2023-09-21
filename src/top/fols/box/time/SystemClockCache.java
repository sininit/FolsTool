package top.fols.box.time;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * update time every second through a thread
 * it is not recommended unless you plan to get time crazily
 */
@Deprecated
@SuppressWarnings("FieldCanBeLocal")
public class SystemClockCache {
//	public static void test() {
//		{
//			System.out.println(System.currentTimeMillis());
//			for (int i = 0; i < 1_000_0000;i++) {
//				long l = System.currentTimeMillis();
//			}
//			System.out.println(System.currentTimeMillis());
//		}
//		{
//			int num = 100_0000;
//			final Runnable runnable = new Runnable() {
//				@Override
//				public void run() {
//					// TODO: Implement this method
//					System.currentTimeMillis();
//				}
//			};
//
//			ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(num));
//			final long[] sum = new long[]{0};
//			//闭锁基于CAS实现，并不适合当前的计算密集型场景，可能导致等待时间较长
//			final CountDownLatch countDownLatch = new CountDownLatch(num);
//			for (int i = 0; i < num; i++) {
//				threadPoolExecutor.submit(new Runnable(){
//						@Override
//						public void run() {
//							// TODO: Implement this method
//							long begin = System.nanoTime();
//							runnable.run();
//							long end = System.nanoTime();
//							//计算复杂型场景更适合使用悲观锁
//							synchronized (SystemClockCache.class) {
//								sum[0] += end - begin;
//							}
//							countDownLatch.countDown();
//						}
//					});
//			}
//			try {
//				countDownLatch.await();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			System.out.println(sum[0] / 1000000L) ;
//		}
//	}


	static final Object lock = new Object();
	static SystemClockCache instance;

	public volatile long timeMillis;

	private final ScheduledExecutorService timer;
	private SystemClockCache() {
		synchronized (lock) {
			if (null == instance) {
				timer = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
						@Override
						public Thread newThread(final Runnable p1) {
							// TODO: Implement this method
							return new Thread(p1);
						}
					});
				timer.scheduleAtFixedRate(new Runnable() {
						@Override
						public void run() {
							timeMillis = System.currentTimeMillis();
						}
					}, 0, 1000, TimeUnit.MILLISECONDS);
				instance = this;
			} else {
				throw new UnsupportedOperationException("initialized");
			}
		}
	}


	public static SystemClockCache getInstance() {
		synchronized (lock) {
			SystemClockCache i = instance;
			if (null == instance) {
				i = instance = new SystemClockCache();
			}
			return i;
		}
	}
}
