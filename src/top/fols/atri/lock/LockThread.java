package top.fols.atri.lock;

import top.fols.atri.util.DoubleLinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class LockThread {
	private final Object linkedLock = new Object();
	private DoubleLinkedList<Lock> linked = new DoubleLinkedList<>();

	public final Lock execute(Runnable runnable) {
		synchronized (linkedLock) {
			if (null == this.linked || null == runnable) { 
				return null; 
			} else {
				Lock lock = new Lock();
				DoubleLinkedList.Element<Lock> element = new DoubleLinkedList.Element<>(lock);
				this.linked.addLast(element);
				lock.element = element;
				lock.runnable.set(runnable);
				lock.start();
				return lock;
			}
		}
	}






	/** 
	 * Will not be interrupted when encountering InterruptedException
	 * 
	 * @return If any thread is has InterruptedException, 
	 *         it will return false
	 */
	public final boolean join() {
		boolean result = true;
		for (;;) {
			DoubleLinkedList.Element<Lock> element;
			synchronized (linkedLock) {
				if (null == (element = linked.getFirst())) {
					break;
				}
			}
			try {
				element.content().join();
			} catch (InterruptedException e) {
				result = false;
			}
		}
		return result;
	}



	/** 
	 * Will not be interrupted when encountering InterruptedException
	 * 
	 * @param  millis overtime
	 * @return If any thread is has InterruptedException, 
	 *         it will return false
	 *           or
	 *         overtime
	 */
	public final boolean join(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

		long base = System.currentTimeMillis();
        long now = 0;
		boolean result = true;

        if (millis == 0) {
			for (;;) {
				DoubleLinkedList.Element<Lock> element;
				synchronized (linkedLock) {
					if (null == (element = linked.getFirst())) {
						break;
					}
				}
				result &= !(element.content().isAlive());
			}
			return result;
        } else {
			for (;;) {
				DoubleLinkedList.Element<Lock> element;
				synchronized (linkedLock) {
					if (null == (element = linked.getFirst())) {
						break;
					}
				}
				long delay = millis - now;
                if (delay <= 0) {
                    return false;
                }
                try {
					element.content().join(delay);
				} catch (InterruptedException e) {
					result = false;
				}
                now = System.currentTimeMillis() - base;
			}
			return result;
        }
    }
	
	
	
	
	
	final void interrupt0() {
		for (DoubleLinkedList.Element<Lock> element = linked.getFirst(); null != element; element = (DoubleLinkedList.Element<LockThread.Lock>) element.getNext()) {
			element.content().interrupt();
		}
	}


	public final void interrupt() {
		synchronized (linkedLock) {
			this.interrupt0();
		}
	}


	/**
	 * release linked and interrupt all
	 */
	public final void release() { 
		synchronized (linkedLock) {
			this.interrupt0();
			this.linked.clear();
			this.linked = null;
		}
	}
	public final boolean isRelease() 	{
		if (null == this.linked) {
			return true;
		} else {
			synchronized (linkedLock) {
				return null == this.linked;
			}
		}
	}



	public class Lock extends Thread {
		DoubleLinkedList.Element<Lock> element;
		AtomicReference<Runnable> runnable = new AtomicReference<>();


		public boolean effective() {
			if (null == element || null == runnable.get()) {
				super.interrupt();
				return false;
			} else {
				return true;
			}
		}



		@Override
		public void start() {
			// TODO: Implement this method
			if (effective()) {
				super.start();
			} else {
				throw new IllegalStateException("removed");
			}
		}

		@Override
		public void run() {
			// TODO: Implement this method
			if (effective()) {
				try {
					runnable.get().run();
				} finally {
					runnable.set(null);
					
					synchronized (linkedLock) {
						element.remove();
						element = null;
					}
				}
			} else {
				throw new IllegalStateException("removed");
			}
		}
	}
}