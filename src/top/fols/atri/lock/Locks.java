package top.fols.atri.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Locks {
	CountDownLatch lock = new CountDownLatch(1);

	public void unlock() {
		try {
			lock.countDown();
		} catch (Throwable ignored) {}
	}

	public boolean isUnlock() {
		return lock.getCount() == 0;
	}
	public boolean isLock() {
		return lock.getCount() != 0;
	}
	
	public void await() throws InterruptedException  {
		lock.await();
	}

	public void await(long time, TimeUnit timeUnit) throws InterruptedException  {
		lock.await(time, timeUnit);
	}
	
	
	
	
}
