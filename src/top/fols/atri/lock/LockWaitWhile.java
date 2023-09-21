package top.fols.atri.lock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import top.fols.atri.interfaces.interfaces.ICallbackOneParam;


public class LockWaitWhile {
//	public static void test() {
//		final AWaitWhile w = new AWaitWhile();
//		final Thread a = new Thread() {
//			@Override
//			public void run() {
//				try {
//					final long st = System.currentTimeMillis();
//					w.although(new Objects.ICallbackOneParam<AWaitWhile>(){
//							@Override
//							public void callback(AWaitWhile p1) {
//								// TODO: Implement this method
//								System.out.println(System.currentTimeMillis() - st);
//							}
//						});
//				} catch (Throwable e) {
//					e.printStackTrace();
//				}
//			}
//		};
//		a.start();
//
//		new Thread() {
//			@Override
//			public void run() {
//				try {
//					while (true)	{
//						Thread.sleep(100);
//						w.signal();
//					}
//				} catch (Throwable e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
//
//	}
	
	
	
	
	
	boolean break0;
	boolean unlock0;
	ReentrantLock rl = new ReentrantLock();
	Condition     rlc = rl.newCondition();
	AtomicBoolean although = new AtomicBoolean();
	boolean isWait;
	
	public LockWaitWhile() {}


	public boolean isBreak() {
		return break0;
	}
	public boolean isWait() {
		return isWait;
	}
	
	
	public void signal() {
		try {
			this.rl.lock();
			this.unlock0 = true;
			this.rlc.signal();
		} finally {
			this.rl.unlock();
		}
	}
	public void breaked() {
		try {
			this.rl.lock();
			this.break0 = true;
			this.rlc.signal();
		} finally {
			this.rl.unlock();
		}
	}

	public void although(ICallbackOneParam<LockWaitWhile> call) throws InterruptedException {
		if (although.getAndSet(true))
			throw new UnsupportedOperationException("started");
		F: while (true) {
			try {
				rl.lock();

				if (break0) {
					break0 = false;
					break;
				}
				if (unlock0) {
					unlock0 = false;
				}
			} finally {
				rl.unlock();
			}

			call.callback(this);

			while (true) {
				try {
					rl.lock();
					if (break0) {
						break0 = false;
						break F;
					}
					if (unlock0) {
						unlock0 = false;
						continue F;
					}

					this.isWait = true;
					rlc.await();//prevent signal() from being called before this
					this.isWait = false;
				} finally {
					rl.unlock();
				}
			}
		} 
	}
}
