package top.fols.box.util.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.util.XObjects;
import top.fols.box.util.interfaces.XInterfaceInterruptable;

public class XFixedThreadPool {
	/*
	 * 运行区 等待区.... 用户post到等待区。。。 根据maxRunningCount 判断等待区任务是否可以添加到运行区
	 */
	private int maxRunningCount = 1;// 设置最大运行数
	private int nowRunningCount = 0;
	private int nowWaitCount = 0;// 这个是无限制的
	private List<ThreadMessage> postData = new ArrayList<ThreadMessage>();
	private List<Run> allrm = Collections.synchronizedList(new ArrayList<Run>());
	private Object sync = new Object();

	private static class RunnablemThread extends Thread {
		private ThreadMessage tm;

		private RunnablemThread(ThreadMessage tm) {
			this.tm = tm;
		}

		@Override
		public void run() {
			// TODO: Implement this method
			try {
				tm.rm.checkInterrupt();
				tm.rm.run();
			} catch (Exception e) {
				e = null;// 无视异常
			}
			if (null != this.tm.rm) {
				this.tm.rm.runComplete(this.tm.rm);
			}
		}
	}

	@XAnnotations("will use thread execute this class")
	public static abstract class Run implements XInterfaceInterruptable, Runnable {
		private XFixedThreadPool pool;
		private ThreadMessage tm;

		public ThreadMessage getThreadMessage() {
			return this.tm;
		}

		private boolean runComplete;
		private boolean interrupt;

		@Override
		public boolean checkInterrupt() throws InterruptedException {
			if (interrupt)
				throw new InterruptedException();
			return false;
		}

		@Override
		public boolean isInterrupt() {
			// TODO: Implement this method
			return this.interrupt;
		}

		@Override
		public void interrupt() {
			// TODO: Implement this method
			this.interrupt = true;
		}

		public abstract void run();

		// 运行完成后必须运行这个方法
		private void runComplete(Run This) {
			synchronized (pool.sync) {
				this.runComplete = true;
				this.pool.remove(This);
				this.pool.deal();
			}
		}

	}

	private static class ThreadMessage {
		public static enum stateType {
			running, waiting;
		}

		private stateType state = null;
		private Run rm;
		private RunnablemThread rmt = null;

		public ThreadMessage(Run t) {
			this.rm = XObjects.requireNonNull(t);
		}
	}

	public XFixedThreadPool setMaxRunningCount(int count) {
		this.maxRunningCount = count <= 0 ? 0 : count;
		return this;
	}

	public int getMaxRunningCount() {
		return this.maxRunningCount;
	}

	public int getNowRunningCount() {
		return this.nowRunningCount;
	}

	public int getNowWaitCount() {
		return this.nowWaitCount;
	}

	public XFixedThreadPool post(Run run) {
		synchronized (sync) {
			if (null == run) {
				throw new NullPointerException();
			}
			int index = postData.indexOf(run);
			if (index < 0) {
				ThreadMessage tm;
				tm = new ThreadMessage(run);
				tm.state = ThreadMessage.stateType.waiting;
				tm.rm = run;
				tm.rm.pool = this;
				tm.rm.tm = tm;

				this.postData.add(tm);
				this.allrm.add(run);
				this.nowWaitCount++;
			}
		}
		this.deal();

		return this;
	}

	public XFixedThreadPool postAll(List<Run> runs) {
		synchronized (sync) {
			if (null == runs) {
				throw new NullPointerException();
			}
			for (Run run : runs) {
				int index = postData.indexOf(run);
				if (index < 0) {
					ThreadMessage tm;
					tm = new ThreadMessage(run);
					tm.state = ThreadMessage.stateType.waiting;
					tm.rm = run;
					tm.rm.pool = this;
					tm.rm.tm = tm;

					this.postData.add(tm);
					this.allrm.add(run);
					this.nowWaitCount++;
				}
			}
		}
		this.deal();

		return this;
	}

	public boolean exist(Run task) {
		synchronized (sync) {
			return this.allrm.contains(task);
		}
	}

	public XFixedThreadPool stopAndWaitComplete(List<Run> tasks) {
		synchronized (sync) {
			for (Run r : tasks) {
				if (null != r) {
					r.interrupt();
					if (null != r.tm.rmt)
						r.tm.rmt.interrupt();
				}
			}
			for (Run r : tasks) {
				if (null != r) {
					while (!r.runComplete) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e = null;
						}
						continue;
					}
				}
			}
		}
		return this;
	}

	public List<Run> getRunnablems() {
		return this.allrm;
	}

	public XFixedThreadPool remove(Run run) {
		synchronized (sync) {
			// System.out.println("origin: " + this.postData + "\n" + this.allrm);
			// System.out.println("remove: " + run);

			int index = this.postData.indexOf(run.tm);
			if (index >= 0) {
				if (null != run.tm.rm)
					run.tm.rm.interrupt();
				if (null != run.tm.rmt)
					run.tm.rmt.interrupt();
				this.postData.remove(index);
				this.allrm.remove(run);
				if (null != run.tm) {
					run.tm.rm = null;
					run.tm.rmt = null;
					run.tm = null;
				}
				this.nowRunningCount--;
			}
			// System.out.println("new: " + this.postData + "\n" + this.allrm);
			// System.out.println();
		}
		return this;
	}

	public XFixedThreadPool remove(List<Run> runs) {
		synchronized (sync) {
			for (Run run : runs) {
				int index = this.postData.indexOf(run.tm);
				if (index >= 0) {
					if (null != run.tm.rm) {
						run.tm.rm.interrupt();
					}
					if (null != run.tm.rmt) {
						run.tm.rmt.interrupt();
					}
					this.postData.remove(index);
					this.nowRunningCount--;
					if (null != run.tm) {
						run.tm.rm = null;
						run.tm.rmt = null;
						run.tm = null;
					}
				}
			}
			this.allrm.removeAll(runs);
		}
		return this;
	}

	public XFixedThreadPool removeAll() {
		synchronized (sync) {
			for (ThreadMessage tn : this.postData) {
				Run run = tn.rm;
				if (null == run) {
					continue;
				}
				int index = this.postData.indexOf(run.tm);
				if (index >= 0) {
					if (null != run.tm.rm)
						run.tm.rm.interrupt();
					if (null != run.tm.rmt)
						run.tm.rmt.interrupt();
					if (null != run.tm) {
						run.tm.rm = null;
						run.tm.rmt = null;
						run.tm = null;
					}
					run = null;
				}
			}
			this.postData.clear();
			this.allrm.clear();
			this.nowRunningCount = 0;
		}
		return this;
	}

	protected void deal() {
		synchronized (sync) {
			while (this.nowRunningCount + 1 <= this.maxRunningCount && this.nowWaitCount >= 1) {
				ThreadMessage tm = null;
				for (ThreadMessage t : postData) {
					if (t.state == ThreadMessage.stateType.waiting) {
						tm = t;
						break;
					}
				}
				if (null != tm) {
					tm.state = ThreadMessage.stateType.running;
					tm.rmt = new RunnablemThread(tm);
					tm.rmt.start();

					this.nowRunningCount++;
					this.nowWaitCount--;
				}
			}
		}
	}
}
