package top.fols.box.util.thread;

import java.util.ArrayList;
import java.util.List;
import top.fols.box.time.XTimeTool;
import top.fols.box.util.XDoubleLinked;

public class XFixedThreadPool {

	private static class RunInterfaceMessage {
		private SinglyLinked linkedRoot;

		private XDoubleLinked<Run> element;

		private int status = STATUS_NO_OPTION; 

		private XFixedThreadPool.SubThread subThread;

		private static final int STATUS_NO_OPTION = 0;
		private static final int STATUS_WAIT = 1;
		private static final int STATUS_RUNING = 2;
		private static final int STATUS_END = 3;
	}
	public static abstract class Run {
		private final RunInterfaceMessage _message = new RunInterfaceMessage();

		public abstract void run();

		private boolean interrupt;
		public void interrupt() {
			this.interrupt = true;
		}

		public boolean isInterrupt() {
			// TODO: Implement this method
			return this.interrupt;
		}

		public boolean checkInterrupt() throws InterruptedException {
			if (interrupt) {
				throw new InterruptedException();
			}
			return false;
		}
	}
	private static class SinglyLinked {
		private final XDoubleLinked<Run> linkedRoot = new XDoubleLinked<Run>(null);
		private XDoubleLinked<Run> linkedTop = linkedRoot;
		private void addToTop(XDoubleLinked<Run> element) {
			linkedTop.addNext(element);
			linkedTop = element;

			if (null == this.now) {
				this.now = element;
			}
		}
		private void remove(XDoubleLinked<Run> element) throws RuntimeException {
			if (this.linkedRoot == element) {
				throw new RuntimeException("cannot remove linked root");
			}

			if (element == this.now) {
				XDoubleLinked<Run> next = this.getNext();
				this.now = next;
			}

			if (this.linkedTop == element) {
				XDoubleLinked<Run> prev = element.getPrev();
				element.remove();
				this.linkedTop = prev;
			} else {
				element.remove();
			}
		}



		private XDoubleLinked<Run> now = linkedRoot;
		private XDoubleLinked<Run> getNext() {
			return (null == now || null == now.getNext()) ?null: now.getNext();
		}
		private XDoubleLinked<Run> next() {
			return this.now = this.getNext();
		}
		private XDoubleLinked<Run> now() {
			return this.now;
		}
	}





	private final SinglyLinked list = new SinglyLinked();

	private Object lock = new Object();
	private volatile boolean isWait;//处理线程是否正在等待
	private volatile boolean isRuningThread;//是否运行了处理线程

	private volatile int runingCount = 0;
	private volatile int maxRuningCount = 1;

	private volatile int waitCount = 0;

	public XFixedThreadPool() {
		this.isWait = false;
		this.isRuningThread = false;
	}

	public XFixedThreadPool setMaxRuningCount(int maxRuningCount) {
		this.maxRuningCount = maxRuningCount;
		return this;
	}

	public int getMaxRuningCount() {
		return maxRuningCount;
	}

	public int getNowRuningCount() {
		return this.runingCount;
	}

	public int getWaitCount() {
		return this.waitCount;
	}


	public boolean isStatusNoOption(Run runinterface) {
		return null != runinterface && runinterface._message.status == RunInterfaceMessage.STATUS_NO_OPTION;
	}
	public boolean isStatusWait(Run runinterface) {
		return null != runinterface && runinterface._message.status == RunInterfaceMessage.STATUS_WAIT;
	}
	public boolean isStatusRuning(Run runinterface) {
		return null != runinterface && runinterface._message.status == RunInterfaceMessage.STATUS_RUNING;
	}
	public boolean isStatusEnd(Run runinterface) {
		return null != runinterface && runinterface._message.status == RunInterfaceMessage.STATUS_END;
	}




	private static void notify(final Object lock) {
		new Thread(){
			@Override
			public void run() {
				synchronized (lock) {
					try {
						lock.notify();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	private void dealThread0() {
		synchronized (this.lock) {
			if (this.isWait) {
				this.notify(this.lock);
				this.isWait = false;
			}
			if (!this.isRuningThread) {
				this.isRuningThread = true;
				this.isWait = false;

				new DealThread().start();
			}
		}
	}







	public void post(Run runinterface) throws RuntimeException {
		synchronized (this.lock) {
			if (null != runinterface._message.linkedRoot || null != runinterface._message.element) {
				throw new RuntimeException("already add to thread pool");
			}
			runinterface._message.linkedRoot = this.list;
			runinterface._message.element = new XDoubleLinked<Run>(runinterface);
			runinterface._message.status = RunInterfaceMessage.STATUS_WAIT;
			runinterface._message.subThread = null;
			this.waitCount++;
			this.list.addToTop(runinterface._message.element);
			this.dealThread0();
		}
	}
	private class SubThread extends Thread {
		private Run runinterface;

		@Override
		public void run() {
			try {
				this.runinterface.run();
			} catch (Throwable e) {
				e.printStackTrace();
			} 
			try {
				XFixedThreadPool pool = XFixedThreadPool.this;
				synchronized (pool.lock) {
					runinterface._message.status = RunInterfaceMessage.STATUS_END;

					pool.runingCount--;
					pool.list.remove(runinterface._message.element);

					runinterface._message.linkedRoot = null;
					runinterface._message.element = null;
					runinterface._message.subThread = null;

					pool.dealThread0();
//						System.out.println(";;");
				}
			} catch (Throwable e) {
				e.printStackTrace();
			} 
		}
	}
	private class DealThread extends Thread {
		@Override
		public void run() {
//				System.out.println("xt");
			XDoubleLinked<Run> now;
			XFixedThreadPool pool = XFixedThreadPool.this;
			synchronized (pool.lock) {
//					System.out.println("startthread");

				while (true) {
					if (pool.runingCount + 1 <= pool.maxRuningCount) {
						if (null != pool.list.now()) {
							if (pool.list.now().isFirst()) {
								pool.list.next();
								continue;
							}
							now = pool.list.now();
							pool.list.next();//下一个线程
							if (null == now.content()) {
								throw new RuntimeException("null thread");
							}

//								System.out.println("get: " + now);
							Run runinterface = now.content();
							try {
								SubThread st = new SubThread();
								st.runinterface = runinterface;
								pool.waitCount--;
								runinterface._message.status = RunInterfaceMessage.STATUS_RUNING;
								runinterface._message.subThread = st;
								pool.runingCount++;
								try {
									st.start();
								} catch (Throwable e) {
									runinterface._message.status = RunInterfaceMessage.STATUS_END;
									runinterface._message.subThread = null;
									pool.runingCount--;
									pool.list.remove(runinterface._message.element);
									throw e;
								}
							} catch (Throwable e) {
								e.printStackTrace();
							}
						} else {
							//没有需要处理的线程了，暂停本线程
							try {
								pool.isWait = true;
//									System.out.println("wait");
								pool.lock.wait(XTimeTool.time_1s);
//									System.out.println("continue");
								pool.isWait = false;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (!(null != pool.list.now())) {
								break;
							}
						}
					} else {
						//运行中的线程 已满足最大运行中线程条件
						try {
							pool.isWait = true;
//								System.out.println("wait");
							pool.lock.wait(XTimeTool.time_1s);
//								System.out.println("continue");
							pool.isWait = false;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (!(pool.runingCount + 1 <= pool.maxRuningCount)) {
							break;
						}
					}
				}
//					System.out.println("stopthread");

				pool.isRuningThread = false;
				pool.isWait = false;
			}
		}
	}

	public void remove(Run runinterface) throws RuntimeException {
		synchronized (this.lock) {
			if (runinterface._message.linkedRoot != this.list) {
				throw new RuntimeException("the thread is not on this thread pool");
			}
			XDoubleLinked<Run> now = runinterface._message.element;
			if (runinterface._message.status == RunInterfaceMessage.STATUS_WAIT) {
				this.list.remove(now);
				this.waitCount--;

				runinterface._message.status = RunInterfaceMessage.STATUS_NO_OPTION;
				runinterface._message.linkedRoot = null;
				runinterface._message.element = null;
				runinterface._message.subThread = null;
			} else if (runinterface._message.status == RunInterfaceMessage.STATUS_RUNING) {
				this.list.remove(now);

				XFixedThreadPool.interrupt(runinterface);
			} 

		}
	}

	private static void interrupt(Run runinterface) {
		try { 
			runinterface.interrupt(); 
			runinterface._message.subThread.interrupt();
		} catch (Throwable e) { 
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	public void removeAll() throws RuntimeException {
		synchronized (this.lock) {
			XDoubleLinked<Run> now;
			now = this.list.linkedRoot;
			now = now.getNext();
			while (null != now) {
				Run runinterface = now.content();
				XDoubleLinked<Run> next = now.getNext();
				this.remove(runinterface);
				now = next;
			}
		}
	}
	
	
	public XFixedThreadPool stopAndWaitComplete(List<Run> tasks) {
		synchronized (this.lock) {
			for (Run r : tasks) {
				if (null != r) {
					this.remove(r);
				}
			}
			for (Run r : tasks) {
				if (null != r) {
					while (this.exist(r)) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e = null;
						}
					}
				}
			}
		}
		return this;
	}
	
	
	
	
	public List<Run> list() {
		List<Run> list = new ArrayList<>();
		synchronized (this.lock) {
			XDoubleLinked<Run> now;

			now = this.list.linkedRoot;
			while (null != (now = now.getNext())) {
				Run runinterface = now.content();
				list.add(runinterface);
			}
		}
		return list;
	}

	public boolean exist(Run runinterface) {
		return null != runinterface && 
			(runinterface._message.linkedRoot == this.list);
	}
}
