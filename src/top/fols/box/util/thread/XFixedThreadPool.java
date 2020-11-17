package top.fols.box.util.thread;

import top.fols.box.time.XTimeTool;
import top.fols.box.util.XDoubleLinked;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XFixedThreadPool {

	private static class RunInterfaceMessage {
		private volatile SinglyLinked linkedRoot;
		private volatile XDoubleLinked<Run> element;
		private volatile int status = STATUS_UNKNOWN;
		private volatile XFixedThreadPool.SubThread subThread;

		private static final int STATUS_UNKNOWN = 0;//null

		private static final int STATUS_WAIT = 1;
		private static final int STATUS_RUNNING = 2;
		private static final int STATUS_STOPPING = 3;

		private static final int STATUS_START_EXCEPTION = 4;
		private static final int STATUS_END = 5;
	}
	public static abstract class Run implements Runnable {
		private final RunInterfaceMessage _message = new RunInterfaceMessage();



		private static final void _runBefore(Run runinterface) {
			if (null != runinterface) { runinterface.remove = false; }
			try { runinterface.run(); } catch (Throwable e) { e = null; }
		}
		public abstract void run();



		private static final void _completeBefore(Run runinterface) {
			try { runinterface.complete(); } catch (Throwable e) { e = null; }
		}
		/**
		 * Run() has finished executing or stopped while executing run()
		 */
		public abstract void complete();




		private static final void _removeBefore(Run runinterface) {
			//mark end
			try {
				runinterface.remove = true;
				runinterface._message.status = RunInterfaceMessage.STATUS_STOPPING;
			} catch (Throwable e) { e = null; }

			//current thread interrupt
			try { runinterface._message.subThread.interrupt(); } catch (Throwable e) { e = null; }

			//interrupt
			try { runinterface.remove(); } catch (Throwable e) { e = null; }
		}

		/**
		 * This method only attempts to end the process running, and will not be executed after pool remove is successful
		 */
		public void remove() { }



		/**
		 * permanent mark
		 * this not thread status
		 */
		private boolean remove;
		public final boolean isRemove() {
			// TODO: Implement this method
			return this.remove;
		}
		public final boolean requireNoRemove() throws InterruptedException {
			if (this.isRemove()) {
				throw new InterruptedException();
			}
			return false;
		}



		/**
		 * Unexecuted and executed result is false
		 */
		public final boolean isThreadRunning() {
			return null != this._message.subThread;
		}
		public final Thread getThread() throws RuntimeException {
			if (!this.isThreadRunning()) { throw new RuntimeException("no start thread"); }
			return this._message.subThread;
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




	boolean is_deal_thread_running() {
		return this.isDealThreadRunning;
	}
	boolean is_deal_thread_running_wait() {
		return this.isDealThreadWait;
	}





	private final SinglyLinked list = new SinglyLinked();

	private volatile Object lock = new Object();
	private volatile boolean isDealThreadWait;//处理线程是否正在等待
	private volatile boolean isDealThreadRunning;//是否运行了处理线程

	private volatile int runningCount = 0;
	private volatile int maxRunningCount = 1;

	private volatile int waitCount = 0;

	public XFixedThreadPool() {
		this.isDealThreadWait = false;
		this.isDealThreadRunning = false;
	}



	public XFixedThreadPool setMaxRunningCount(int maxRuningCount) {
		this.maxRunningCount = maxRuningCount;
		return this;
	}

	public int getMaxRunningCount() {
		return maxRunningCount;
	}

	public int getNowRunningCount() {
		return this.runningCount;
	}

	public int getWaitCount() {
		return this.waitCount;
	}


	public boolean isEmpty() {
		return this.waitCount == 0 && this.runningCount == 0;
	}


	public boolean isStatusNull(Run runinterface) {
		return null != runinterface && runinterface._message.status == RunInterfaceMessage.STATUS_UNKNOWN;
	}
	public boolean isStatusWait(Run runinterface) {
		return null != runinterface && runinterface._message.status == RunInterfaceMessage.STATUS_WAIT;
	}
	public boolean isStatusRunning(Run runinterface) {
		return null != runinterface && runinterface._message.status == RunInterfaceMessage.STATUS_RUNNING;
	}
	public boolean isStatusStopping(Run runinterface) {
		return null != runinterface && runinterface._message.status == RunInterfaceMessage.STATUS_STOPPING;
	}
	public boolean isStatusEnd(Run runinterface) {
		return null != runinterface && runinterface._message.status == RunInterfaceMessage.STATUS_END;
	}
	public boolean isStatusException(Run runinterface) {
		return null != runinterface && runinterface._message.status == RunInterfaceMessage.STATUS_START_EXCEPTION;
	}
	public int getStatus(Run runinterface) {
		return null != runinterface ?  runinterface._message.status: RunInterfaceMessage.STATUS_UNKNOWN;
	}



	private static void heartbeatNotifyDealThread00(final Object lock) {
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
	private void heartbeatDealThread0() {
		synchronized (this.lock) {
			if (this.isDealThreadWait) {
				this.heartbeatNotifyDealThread00(this.lock);
				this.isDealThreadWait = false;
			}
			if (!this.isDealThreadRunning) {
				this.isDealThreadRunning = true;
				this.isDealThreadWait = false;
				new DealThread().start();
			}
		}
	}







	public void post(Run runinterface) throws RuntimeException {
		synchronized (this.lock) {
			if (null != runinterface._message.linkedRoot || null != runinterface._message.element) {
				throw new RuntimeException("already add to thread pool");
			}
			runinterface.remove = false;
			runinterface._message.status = RunInterfaceMessage.STATUS_WAIT;
			runinterface._message.element = new XDoubleLinked<Run>(runinterface);
			runinterface._message.subThread = null;
			runinterface._message.linkedRoot = this.list;
			this.waitCount++;
			this.list.addToTop(runinterface._message.element);

			this.heartbeatDealThread0();
		}
	}
	private class SubThread extends Thread {
		private Run runinterface;
		private SubThread() {
		}

		@Override
		public void interrupt() {
			// TODO: Implement this method
			super.interrupt();
		}

		@Override
		public void run() {

			try { Run._runBefore(this.runinterface); } catch (Throwable e) { e.printStackTrace(); }

			try {
				XFixedThreadPool pool = XFixedThreadPool.this;

//				System.out.println("--*+");

				synchronized (pool.lock) {
					pool.runningCount--;
					pool.list.remove(runinterface._message.element);

					runinterface._message.status = RunInterfaceMessage.STATUS_END;
					runinterface._message.element = null;
					runinterface._message.subThread = null;
					runinterface._message.linkedRoot = null;
				}

				try { Run._completeBefore(this.runinterface); } catch (Throwable e) { e.printStackTrace(); }

				synchronized (pool.lock) {
					pool.heartbeatDealThread0();
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
//				System.out.println("startthread");

				while (true) {
					if (pool.runningCount + 1 <= pool.maxRunningCount) {
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

//							System.out.println("get: " + now);
							Run runinterface = now.content();
							try {
								SubThread st = new SubThread();
								st.runinterface = runinterface;

								pool.waitCount--;

								pool.runningCount++;

								runinterface._message.status = RunInterfaceMessage.STATUS_RUNNING;
								runinterface._message.subThread = st;

								try {
									st.start();
								} catch (Throwable e) {

									pool.runningCount--;
									pool.list.remove(runinterface._message.element);

									runinterface._message.status = RunInterfaceMessage.STATUS_START_EXCEPTION;
									runinterface._message.element = null;
									runinterface._message.subThread = null;
									runinterface._message.linkedRoot = null;

									throw e;
								}

							} catch (Throwable e) {
								e.printStackTrace();
							}
						} else {
							//没有需要处理的线程了，暂停本线程
							try {
								pool.isDealThreadWait = true;
//								System.out.println("wait");
								pool.lock.wait(XTimeTool.time_1s);
//								System.out.println("continue");
								pool.isDealThreadWait = false;
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
							pool.isDealThreadWait = true;
//							System.out.println("wait");
							pool.lock.wait(XTimeTool.time_1s);
//							System.out.println("continue");
							pool.isDealThreadWait = false;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (!(pool.runningCount + 1 <= pool.maxRunningCount)) {
							break;
						}
					}
				}

//				System.out.println("stopthread");

				pool.isDealThreadRunning = false;
				pool.isDealThreadWait = false;
			}
		}
	}

	/**
	 * This will interrupt() the thread and execute Run.remove(),
	 * Run.isRemove marked as true
	 */
	public boolean remove(Run runinterface) throws RuntimeException {
		synchronized (this.lock) {
			if (null == runinterface._message.linkedRoot) {
				//not in list
				return false;
			}
			if (runinterface._message.linkedRoot != this.list) {
//				throw new RuntimeException("the thread is not on this thread pool");
				//not in list
				// return false;
			}
			XDoubleLinked<Run> now = runinterface._message.element;
			if (runinterface._message.status == RunInterfaceMessage.STATUS_WAIT) {
				this.waitCount--;
				this.list.remove(now);
				Run._removeBefore(runinterface);
				runinterface._message.status = RunInterfaceMessage.STATUS_UNKNOWN;
				runinterface._message.element = null;
				runinterface._message.subThread = null;
				runinterface._message.linkedRoot = null;
				return true;
			} else if (
					runinterface._message.status == RunInterfaceMessage.STATUS_RUNNING ||
							runinterface._message.status == RunInterfaceMessage.STATUS_STOPPING) {
				Run._removeBefore(runinterface);
				return true;
			}  else {
				//not in list
				return false;
			}
		}
	}



	public boolean removeAll() throws RuntimeException {
		synchronized (this.lock) {
			boolean result = true;

			XDoubleLinked<Run> now;
			now = this.list.linkedRoot;
			now = now.getNext();
			while (null != now) {
				Run runinterface = now.content();
				XDoubleLinked<Run> next = now.getNext();
				result &= this.remove(runinterface);
				now = next;
			}

			return result;
		}
	}
	public boolean removeAll(Collection<Run> tasks) throws RuntimeException {
		synchronized (this.lock) {
			boolean result = true;

			for (Run task : tasks) {
				if (null != task) {
					result &= this.remove(task);
				}
			}

			return result;
		}
	}



	public XFixedThreadPool removeAllAndWaitEnd() { return this.removeAllAndWaitEnd(this.list()); }
	public XFixedThreadPool removeAllAndWaitEnd(Collection<Run> tasks) {
		synchronized (this.lock) {
			for (Run task : tasks) {
				if (null != task) {
					this.remove(task);
				}
			}
		}
		for (Run task : tasks) {
			if (null != task) {
				for (boolean running = this.exist(task) ; running ; running = this.exist(task)) {
					this.remove(task);
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
