package top.fols.atri.thread;

import top.fols.atri.util.DoubleLinked;
import top.fols.box.time.XTimeTool;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FixedThreadPool {

	private static class RunInterfaceMessage {
		private volatile SinglyLinked linkedRoot;
		private volatile DoubleLinked<Run> element;
		private volatile int status = STATUS_UNKNOWN;
		private volatile FixedThreadPool.SubThread subThread;

		private static final int STATUS_UNKNOWN = 0;//？？？ null

		private static final int STATUS_WAIT = 1;
		private static final int STATUS_RUNNING = 2;
		private static final int STATUS_STOPPING = 3;

		private static final int STATUS_START_EXCEPTION = 4;//？？？
		private static final int STATUS_END = 5;// run success！
	}
	public static abstract class Run implements Runnable {
		private final RunInterfaceMessage _message = new RunInterfaceMessage();

		private static final void _runBefore(Run runinterface) {
			if (null != runinterface) { runinterface.setRemove(false); }
			try { runinterface.forStartCallback(); } catch (Throwable ignore) {}
		}

		public abstract void run();

		private static final void _completeAfter(Run runinterface) {
			try { if (!runinterface.isRemove()) runinterface.forEndCallback(); } catch (Throwable ignore) {}
		}

		/**
		 * Threads are executed only when they exist in the pool
		 */
		private static void _remove(Run runinterface) {
			//mark end
			try {
				runinterface.setRemove(true);
				runinterface._message.status = RunInterfaceMessage.STATUS_STOPPING;
			} catch (Throwable e) { e = null; }

			//current thread interrupt
			try { runinterface._message.subThread.interrupt(); } catch (Throwable e) { e = null; }

			//interrupt
			try { runinterface.remove(); } catch (Throwable e) { }

			try { runinterface.forStopCallback(); } catch (Throwable ignore) {}
		}

		public void remove() { }



		/**
		 * permanent mark
		 * this not thread status
		 */
		private AtomicBoolean remove0;
		public final boolean isRemove() { return null != remove0 && remove0.get(); }
		final void setRemove(boolean status) { (null == remove0?remove0= new AtomicBoolean():remove0).set(status); }


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



		Set<Callback> startCallback;  //正常 运行 Thread.run 方法
		Set<Callback> stopCallback;   //正常 运行 Thread.run 方法 时 中断线程
		Set<Callback> endCallback;    //正常 运行 Thread.run 方法 结束

		public boolean addStartCallback(Callback callback) 		{
			return null == startCallback?(startCallback = new LinkedHashSet<>()).add(callback):startCallback.add(callback); }
		public boolean removeStartCallback(Callback callback) 	{
			return null != startCallback && startCallback.remove(callback); }
		public boolean hasStartCallback(Callback callback) 		{
			return null != startCallback && startCallback.contains(callback); }
		public boolean forStartCallback() {
			if (null == startCallback) return false;
			for (Callback callback: startCallback)
				try { callback.callback(this); } catch (Throwable ignore) { }
			return true;
		}

		public boolean addStopCallback(Callback callback) 		{
			return null == stopCallback?(stopCallback = new LinkedHashSet<>()).add(callback):stopCallback.add(callback); }
		public boolean removeStopCallback(Callback callback) 	{
			return null != stopCallback && stopCallback.remove(callback); }
		public boolean hasStopCallback(Callback callback) 		{
			return null != stopCallback && stopCallback.contains(callback); }
		public boolean forStopCallback() {
			if (null == stopCallback) return false;
			for (Callback callback: stopCallback)
				try { callback.callback(this); } catch (Throwable ignore) { }
			return true;
		}

		public boolean addEndCallback(Callback callback) 		{
			return null == endCallback?(endCallback = new LinkedHashSet<>()).add(callback):endCallback.add(callback); }
		public boolean removeEndCallback(Callback callback) 	{
			return null != endCallback && endCallback.remove(callback); }
		public boolean hasEndCallback(Callback callback) 		{
			return null != endCallback && endCallback.contains(callback); }
		public boolean forEndCallback() {
			if (null == endCallback) return false;
			for (Callback callback: endCallback)
				try { callback.callback(this); } catch (Throwable ignore) { }
			return true;
		}

	}





	private static class SinglyLinked {
		private final DoubleLinked<Run> linkedRoot = new DoubleLinked<Run>(null);
		private DoubleLinked<Run> linkedTop = linkedRoot;
		private void addToTop(DoubleLinked<Run> element) {
			linkedTop.addNext(element);
			linkedTop = element;

			if (null == this.now) {
				this.now = element;
			}
		}
		private void remove(DoubleLinked<Run> element) throws RuntimeException {
			if (this.linkedRoot == element) {
				throw new RuntimeException("cannot remove linked root");
			}

			if (element == this.now) {
				DoubleLinked<Run> next = this.getNext();
				this.now = next;
			}

			if (this.linkedTop == element) {
				DoubleLinked<Run> prev = element.getPrev();
				element.remove();
				this.linkedTop = prev;
			} else {
				element.remove();
			}
		}



		private DoubleLinked<Run> now = linkedRoot;
		private DoubleLinked<Run> getNext() {
			return (null == now || null == now.getNext()) ?null: now.getNext();
		}
		private DoubleLinked<Run> next() {
			return this.now = this.getNext();
		}
		private DoubleLinked<Run> now() {
			return this.now;
		}
	}

	public boolean is_deal_thread_running() {
		return this.isDealThreadRunning;
	}
	public boolean is_deal_thread_running_wait() {
		return this.isDealThreadWait;
	}





	private final SinglyLinked list = new SinglyLinked();

	private final Object lock = new Object();
	private volatile boolean isDealThreadWait;//处理线程是否正在等待
	private volatile boolean isDealThreadRunning;//是否运行了处理线程

	private volatile int runningCount = 0;
	private volatile int maxRunningCount = 1;

	private volatile int waitCount = 0;

	public FixedThreadPool() {
		this.isDealThreadWait = false;
		this.isDealThreadRunning = false;
	}



	public FixedThreadPool setMaxRunningCount(int maxRuningCount) {
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






	public void post(Run runInterface) throws RuntimeException {
		synchronized (this.lock) {
			if (null != runInterface._message.linkedRoot || null != runInterface._message.element) {
				throw new RuntimeException("already add to thread pool");
			}
			runInterface.setRemove(false);
			runInterface._message.status = RunInterfaceMessage.STATUS_WAIT;
			runInterface._message.element = new DoubleLinked<Run>(runInterface);
			runInterface._message.subThread = null;
			runInterface._message.linkedRoot = this.list;
			this.waitCount++;
			this.list.addToTop(runInterface._message.element);

			this.heartbeatDealThread0();
		}
	}

	/**
	 * If there are threads in the pool, the submitted thread will still run after the pool thread has finished running, even if it is only submitted
	 * 如果 线程池中存在运行的线程 那么该方法可能没有意义
	 *
	 * @param runInterface
	 * @throws RuntimeException
	 */
	public void postOnly(Run runInterface) throws RuntimeException {
		String warning = "If there are threads in the pool, the submitted thread will still run after the pool thread has finished running, even if it is only submitted";

		synchronized (this.lock) {
			if (null != runInterface._message.linkedRoot || null != runInterface._message.element) {
				throw new RuntimeException("already add to thread pool");
			}
			runInterface.setRemove(false);
			runInterface._message.status = RunInterfaceMessage.STATUS_WAIT;
			runInterface._message.element = new DoubleLinked<>(runInterface);
			runInterface._message.subThread = null;
			runInterface._message.linkedRoot = this.list;
			this.waitCount++;
			this.list.addToTop(runInterface._message.element);
		}
	}
	public void heartbeat() throws RuntimeException {
		synchronized (this.lock) {
			this.heartbeatDealThread0();
		}
	}


	public interface Callback { void callback(Run runInterface); }



	private class SubThread extends Thread {
		private Run runInterface;
		private SubThread() {
		}

		@Override
		public void interrupt() {
			// TODO: Implement this method
			super.interrupt();
		}

		@Override
		public void run() {
			try { Run._runBefore(this.runInterface); } catch (Throwable e) { e.printStackTrace(); }

			try { runInterface.run(); } catch (Throwable ignore) { }

			try {
				FixedThreadPool pool = FixedThreadPool.this;

//				System.out.println("--*+");

				synchronized (pool.lock) {
					pool.runningCount--;
					pool.list.remove(runInterface._message.element);

					runInterface._message.status = RunInterfaceMessage.STATUS_END;
					runInterface._message.element = null;
					runInterface._message.subThread = null;
					runInterface._message.linkedRoot = null;
				}

				try { Run._completeAfter(this.runInterface); } catch (Throwable e) { e.printStackTrace(); }

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
			DoubleLinked<Run> now;
			FixedThreadPool pool = FixedThreadPool.this;
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
								st.runInterface = runinterface;

								pool.waitCount--;

								pool.runningCount++;

								runinterface._message.status = RunInterfaceMessage.STATUS_RUNNING;
								runinterface._message.subThread = st;

								try {
									st.start(); //?
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
			DoubleLinked<Run> now = runinterface._message.element;
			if (runinterface._message.status == RunInterfaceMessage.STATUS_WAIT) {
				this.waitCount--;
				this.list.remove(now);
				Run._remove(runinterface);
				runinterface._message.status = RunInterfaceMessage.STATUS_UNKNOWN;
				runinterface._message.element = null;
				runinterface._message.subThread = null;
				runinterface._message.linkedRoot = null;
				return true;
			} else if (
					runinterface._message.status == RunInterfaceMessage.STATUS_RUNNING ||
							runinterface._message.status == RunInterfaceMessage.STATUS_STOPPING) {
				Run._remove(runinterface);
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

			DoubleLinked<Run> now;
			now = this.list.linkedRoot;
			now = now.getNext();
			while (null != now) {
				Run runinterface = now.content();
				DoubleLinked<Run> next = now.getNext();
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



	public FixedThreadPool removeAllAndWaitEnd() { return this.removeAllAndWaitEnd(this.list()); }
	public FixedThreadPool removeAllAndWaitEnd(Collection<Run> tasks) {
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
			DoubleLinked<Run> now;

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
