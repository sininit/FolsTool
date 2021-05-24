package top.fols.box.util;

import java.util.concurrent.atomic.AtomicBoolean;
import top.fols.atri.lang.Objects;
import top.fols.atri.util.DoubleLinkedList;

/**
 * It’s a pity that I found that it is similar to Thread.join.
 * @see top.fols.atri.lock.LockThread#
 */
@Deprecated()
public class LockOneTime {
	public final Object linkedLock = new Object();

	private DoubleLinkedList<Lock> linked = new DoubleLinkedList<>();


	/**
	 * please note that the created lock must be used
	 * @return is release return null.
	 */
	public Lock createLock() {
		synchronized (linkedLock) {
			if (null == this.linked) { 
				return null; 
			} else {
				Lock lock = new Lock();
				DoubleLinkedList.Element<Lock> element = new DoubleLinkedList.Element<>(lock);
				this.linked.addLast(element);
				lock.element = element;
				return lock;
			}
		}
	}


	public <T> T execute(LockOneTime.Lock lock, Objects.Executor<T> runnable) {
		if (null == runnable) {
			throw new NullPointerException("runnable");
		} else {
			try {
				synchronized (lock) {
					if (null != this.linked) {
						return runnable.execute();
					} else {
						throw new RuntimeException("lock released.");
					}
				}
			} finally {
				lock.notifyLock();
			}
		}
	}



	/** 
	 * Will not be interrupted when encountering InterruptedException
	 * 
	 * @return If any thread is has InterruptedException, 
	 *         it will return false
	 */
	public boolean waitLock() {
		boolean result = true;
		while (true) {
			Lock first;
			AtomicBoolean effective;
			synchronized (linkedLock) {
				DoubleLinkedList.Element<Lock> element;
				if (null == (element = linked.getFirst()) && null == (element = linked.getLast())) {
					break;
				} else {
					first = element.content();
					effective = first.lock;
				}
			}
			if (effective.get()) {
				result &= first.waitLock();
			} 
		}
		return result;
	}

	public void release() 		{ this.linked = null; }
	public boolean isRelease() 	{ return (this.linked == null);}




	/**
	 * One-time lock
	 */
	public class Lock {
		DoubleLinkedList.Element<Lock> element;
		AtomicBoolean lock = new AtomicBoolean(true);
		Lock() {}


		/**
		 * wait indefinitely
		 *
		 * @return If an InterruptedException occurs, 
		 *         it will return false, 
		 *         otherwise it will wait for completion and return true
		 */
		public boolean waitLock() {
			if (null == element) { return true; }
			while (lock.get()) {
				synchronized (this) {
					if (null == element) { return true ; } //unlocked
					try {
						super.wait();
						return true;
					} catch (InterruptedException ignored) {
						return false; 
					} catch (Throwable ignored) {
					}
				}
			}
			return true;
		}


		/**
		 * finally notify
		 */
		public void notifyLock() {
			// TODO: Implement this method
			this.lock.set(false);
			synchronized (this) {
				super.notifyAll();

				synchronized (linkedLock) {
					this.element.remove();
					this.element = null;
				}
			}
		}
		public boolean isNotified() 	{ return false == this.lock.get() || null == this.element;}

	}

}

