package top.fols.atri.lock;

import top.fols.atri.lang.Objects;
import top.fols.atri.util.DoubleLinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class LockOneTime {
	public final Object linkedLock = new Object();

	private DoubleLinkedList<Lock> linked = new DoubleLinkedList<>();
	public Lock createLock() {
		synchronized (linkedLock) {
			Lock lock = new Lock();
			DoubleLinkedList.Element<Lock> element = new DoubleLinkedList.Element<>(lock);
			linked.addLast(element);
			lock.element = element;
			return lock;
		}
	}

	public <T> T execute(Objects.Executor<T> runnable) {
		if (null == runnable) {
			throw new NullPointerException("runnable");
		} else {
			LockOneTime.Lock lock = createLock();
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
	
	public void waitLock() {
		synchronized (linkedLock) {
			for (DoubleLinkedList.Element<Lock> element = linked.getFirst(); null != element; element = (DoubleLinkedList.Element<Lock>) element.getNext()) {
				Lock lock = element.content();
				lock.waitLock();
			}
		}
	}
	
	public void release() { this.linked = null; }
	
	
	
	
	/**
	 * One-time lock
	 */
	public class Lock {
		DoubleLinkedList.Element element;
		AtomicBoolean lock = new AtomicBoolean(true);
		Lock() {}

		public boolean waitLock() {
			if (null == element) { throw new NullPointerException("release"); }
			while (true) {
				synchronized (this) {
					if (!lock.get()) { return true; }
					try {
						this.wait();
						return true;
					} catch (InterruptedException e) {}
				}
			}
		}


		public void notifyLock() {
			// TODO: Implement this method
			this.lock.set(false);
			synchronized (this) {
				this.notifyAll();

				this.element.remove();
				this.element = null;
			}
		}
	}
	
}
