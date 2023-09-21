package top.fols.atri.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * can only be used once
 */
public class LockAwait {
	CountDownLatch lock = new CountDownLatch(1);

	public void unlock() {
		lock.countDown();
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

	public boolean await(long time, TimeUnit timeUnit) throws InterruptedException  {
		return lock.await(time, timeUnit);
	}
	
}
