package top.fols.box.util.process_guard;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import top.fols.atri.lang.Objects;

public class ProcessGuard implements Closeable {
	public static class BooleanValue { boolean v; public BooleanValue(boolean v) { this.v = v; }  public boolean get() { return v; } }
	public static class ThrowClosedException extends RuntimeException {}


	static class FinalClose extends BooleanValue { FinalClose() { super(true); }}
	BooleanValue asyncMarkClose = new BooleanValue(false);
	public void setAsyncMarkClose(BooleanValue markClose) { 
		boolean b = null != markClose && markClose.get();
		if (b) {
			this.asyncMarkClose = new FinalClose();
		} else {
			if (this.isAsyncMarkClose())
				throw new RuntimeException("already close");
			this.asyncMarkClose = markClose; 
		}
	}
	public boolean isAsyncMarkClose() {
		return null != this.asyncMarkClose && this.asyncMarkClose.get();
	}


	boolean isExited;
	final Object lock = new Object();


	ApplicationStarter applicationStarter;
	public void setApplicationStarter(ApplicationStarter applicationStarter) {
		this.applicationStarter = applicationStarter;
	}
	public ApplicationStarter getApplicationStarter() {
		return applicationStarter;
	}

	protected void startApplication() {
		ApplicationStarter starter = this.applicationStarter;
		if (starter != null) {
			ProcessObjectGroup group = this.getFindProcesser().findProcess();
			if (null == group || group.isEmpty()) {
				starter.start();
			}
		}
	}
	public void start() {
		synchronized (lock) {
			if (null == getFindProcesser())
				throw new RuntimeException("null find processer");
			if (null == getMonitor())
				throw new RuntimeException("null monitor");

			if (isAsyncMarkClose())
				throw new RuntimeException("async closed");

			ProcessMonitor p = this.getMonitor();
			if (p.isMarkClosed())
				throw new RuntimeException("monitor closed");
			if (p.isStarted()) 
				throw new RuntimeException("started");

			this.startApplication();
			p.start();
		}
	}
	public void exit() {
		synchronized (lock) {
			if (!isExited) {

				this.asyncMarkClose = new FinalClose();

				this.callback_stop_monitor();
				this.callback_exit();
				this.callback_exit_findprocesser();
				this.callback_clear_monitor();

				isExited = true;

				ProcessGuard.close(this);
			}
		}
	}
	@Override
	public void close() {
		// TODO: Implement this method
		this.exit();
	}


	void callback_process_start(ProcessObjectGroup.diff diff) {
		ProcessMonitorCallback pmc = getCallback();
		if (null != pmc) 
			try {
				pmc.start(diff);
			} catch (Throwable e) {
				e.printStackTrace();
			}
	}
	void callback_process_diff(ProcessObjectGroup.diff diff) {
		ProcessMonitorCallback pmc = getCallback();
		if (null != pmc)
			try {
				pmc.diff(diff);
			} catch (Throwable e) {
				e.printStackTrace();
			}
	}


	void callback_stop_monitor() {
		ProcessMonitor p = this.getMonitor();
		if (null != p) {
			p.stop();
		}
	}
	void callback_exit() {
		ProcessMonitorCallback pmc = getCallback();
		if (null != pmc) 
			try {
				pmc.exit();
			} catch (Throwable e) {
				e.printStackTrace();
			}
	}

	void callback_exit_findprocesser() {
		FindProcesser fper = getFindProcesser();
		if (null != fper) 
			try {
				fper.exit();
			} catch (Throwable e) {
				e.printStackTrace();
			}
	}

	void callback_clear_monitor() {
		ProcessMonitor p = this.getMonitor();
		if (null != (((p)))) {
			p.lastProcessObjectGroup = null;
		}
	}



	public ProcessObjectGroup getLastProcessObjectGroup() {
		ProcessMonitor p = this.getMonitor();
		if (null != p) {
			return  p.lastProcessObjectGroup;
		}
		return null;
	}




	public static interface FindProcesser {
		ProcessObjectGroup findProcess();
		void kill(ProcessObjectGroup group);
		void exit();
	}

	public static class ProcessMonitor {
		protected final Object lock = new Object();
		protected final ProcessGuard parent;

		protected GuardThread thread;
		protected boolean markClosed;

		protected ProcessMonitor(ProcessGuard pg) {
			this.parent = Objects.requireNonNull(pg);
		}


		public boolean isStarted() {
			synchronized (lock) {
				return null != this.thread;
			}
		}

		protected void start() {
			synchronized (lock) {
				if (this.isStarted()) 
					throw new RuntimeException("started");
				(this.thread = newGuardThread()).start();
			}
		}
		protected void stop() {
			synchronized (lock) {
				this.markClosed = true;

				if (this.thread != null)
					this.thread.interrupt();
			}
		}


		public void killProcess() {
			try {
				parent.getFindProcesser().kill(lastProcessObjectGroup);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		public void exit() {
			this.killProcess();
			this.parent.exit();
		}
		public boolean isMarkClosed() { 
			return this.markClosed || parent.isAsyncMarkClose();
		}
		public void throwExceptionIfMarkClosed() throws ThrowClosedException {
			if (this.isMarkClosed())
				throw new ThrowClosedException();
		}


		protected GuardThread newGuardThread() {
			return new GuardThreadImpl();
		}

		protected boolean started;
		protected ProcessObjectGroup lastProcessObjectGroup;

		protected GuardThread getGuardThread() { return thread; }

		/**
		 * yyyy-MM-dd HH:mm:ss
		 */
		static String getTime_yyyy_MM_dd_HH_mm_ss_toString(Long time) {
			if (null == time) return null;

			String strDateFormat = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
			return sdf.format(time);
		}



		public static abstract class GuardThread extends Thread {
			static AtomicLong id = new AtomicLong(0);
			static String idNext() {
				return String.valueOf(GuardThread.id.addAndGet(1));
			}

			long startOvertime = TimeUnit.MINUTES.toMillis(3); //启动超时
			long startCheckTime = TimeUnit.SECONDS.toMillis(3); //启动检测延迟

			long runtimeCheckTime = startCheckTime; //启动成功后的检测延迟

			public void setStartOvertime(long startOvertime) { this.startOvertime = startOvertime; }
			public long getStartOvertime() { return startOvertime; }

			public void setStartCheckTime(long startCheckTime) { this.startCheckTime = startCheckTime; }
			public long getStartCheckTime() { return startCheckTime; }

			public long getRuntimeCheckTime() {return runtimeCheckTime;}
			public void setRuntimeCheckTime(long runtimeCheckTime) {this.runtimeCheckTime = runtimeCheckTime;}

			@Override public abstract void run();
		}

		protected class GuardThreadImpl extends GuardThread {
			public GuardThreadImpl() { }

			long 					lastGetTime;

			@Override
			public void run() {
				try {
					String id = idNext();
					boolean interrupt = false;

					System.out.println(
						getTime_yyyy_MM_dd_HH_mm_ss_toString(System.currentTimeMillis()) + ": " + "{tid: " + id + "}" + "[monitor/start-thread]"
					);
					lastGetTime   = System.currentTimeMillis();

					ProcessMonitor.this.lastProcessObjectGroup = null;

					boolean over = false;
					boolean stop  = false;
					while (true) {
						throwExceptionIfMarkClosed();

						ProcessObjectGroup prev    = ProcessMonitor.this.lastProcessObjectGroup;
						ProcessObjectGroup current = parent.findProcessGroup();

						ProcessObjectGroup.diff diff = 
							ProcessObjectGroup.diff(prev,
													current,
													ProcessMonitor.this.started);

						boolean isNonEmpty = null != current && !current.isEmpty();
						if (isNonEmpty) {
							ProcessMonitor.this.lastProcessObjectGroup = current;
						}
						try {
							if (ProcessMonitor.this.started) {
								if (diff.isClosed()) {
									stop = true;
									break;
								}
								if (!diff.isEquals) {
									parent.callback_process_diff(diff);
								}
								this.lastGetTime = System.currentTimeMillis();
							} else {
								if (isNonEmpty && current.isStart(diff)) {
									diff.isStarted = ProcessMonitor.this.started = true;
									parent.callback_process_start(diff);
									this.lastGetTime = System.currentTimeMillis();

									System.out.println(
											getTime_yyyy_MM_dd_HH_mm_ss_toString(System.currentTimeMillis()) + ": " + "{tid: " + id + "}" + "[monitor/open-process]" +
													"[started: " + started + "]" +
													"[list: " + (null==lastProcessObjectGroup?"[]":lastProcessObjectGroup) + "]"
									);
								} else {
									long c = System.currentTimeMillis();
									if ((c - lastGetTime >= startOvertime)) {
										over = true;
										break;
									}
								}
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
						throwExceptionIfMarkClosed();

						try {
							//noinspection BusyWait
							Thread.sleep(!isStarted() ? startCheckTime : runtimeCheckTime);
						} catch (InterruptedException interruptedException) {
							interrupt = true;
							break;
						}

						if (isMarkClosed()) { break; }
					}

					System.out.println(
						getTime_yyyy_MM_dd_HH_mm_ss_toString(System.currentTimeMillis()) + ": " + "{tid: " + id + "}" + "[monitor/stop-thread]" +
						"[close: " + isMarkClosed() + ", sleep-interrupt: " + interrupt + ", start_over: " + over + ", started: " + started + ", process_exit: " + stop + "]" +
						"[list: " + (null==lastProcessObjectGroup?"[]":lastProcessObjectGroup) + "]"
					);
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					exit();
					thread = null;
				}
			}


		}
	}
	public interface ProcessMonitorCallback {
		void start(ProcessObjectGroup.diff diff);
		void diff(ProcessObjectGroup.diff diff);
		void exit();
	}


	public static class ProcessObjectGroup  {
		FindProcesser f;
		Set<ProcessObject> list;
		public ProcessObjectGroup(FindProcesser f) {
			this.f = Objects.requireNonNull(f, "<FindProcesser>");
		}

		public void setList(Set<ProcessObject> list) {
			if (null != this.list) {
				throw new UnsupportedOperationException("repeat set list");
			}
			this.list = new LinkedHashSet<>(list);
		}
		public Set<ProcessObject> getList() {
			return list;
		}
		public boolean isEmpty() {
			return getList().isEmpty();
		}

		public boolean isStart(diff diff) {
			ProcessObjectGroup current = diff.getCurrent();
			return (null != current && current.isEmpty());
		}

//		boolean isClose;
//		public void    setClosed(boolean b) { this.isClose = b; }
//		public boolean isClosed() {
//			return isClose;
//		}

		public static class diff {
			Set<ProcessObject> reserve = new LinkedHashSet<>();
			Set<ProcessObject> remoed = new LinkedHashSet<>();
			Set<ProcessObject> append = new LinkedHashSet<>();
			public Set<ProcessObject> getReserve() { return reserve;}
			public Set<ProcessObject> getRemoed()  { return remoed; }
			public Set<ProcessObject> getAppend()  { return append; }

			boolean isStarted;
			public boolean isStarted() { return isStarted; }
			boolean isEquals;
			public boolean isEquals() { return isEquals; }
			boolean isClosed;
			public boolean isClosed() { return /* (null != current && current.isClosed()) || */ isClosed; }

			ProcessObjectGroup prev, current;
			public ProcessObjectGroup getPrev()    {return prev;}
			public ProcessObjectGroup getCurrent() {return current;}

			@Override
			public String toString() {
				// TODO: Implement this method
				Map<String, String> to = new LinkedHashMap<>();
				to.put("reserve",  reserve.toString());
				to.put("remove",   remoed.toString());
				to.put("append",   append.toString());

				to.put("isStarted",  String.valueOf(isStarted));
				to.put("isEquals",   String.valueOf(isEquals));
				to.put("isClosed",   String.valueOf(isClosed));
				return to.toString();
			}
		}
		public static diff diff(ProcessObjectGroup prev, 
								ProcessObjectGroup current,
								boolean isStarted) {
			diff diff = new diff();
			diff.prev    = prev;
			diff.current = current;

			if (null == prev || prev.isEmpty()) {
				if (null != current && !current.isEmpty()) {
					diff.append.addAll(current.getList());
				} else {
					diff.isEquals = true;
				}
			} else {
				if (null == current || current.isEmpty()) {
					diff.remoed.addAll(prev.getList());
					diff.isClosed = true;
				} else {
					Collection<ProcessObject> prevList    = prev.getList();
					Collection<ProcessObject> currentList = current.getList();
					for (ProcessObject obj : currentList) {
						if (prevList.contains(obj)) {
							diff.reserve.add(obj);
						} else {
							diff.append.add(obj);
						}
					}
					for (ProcessObject obj : prevList) {
						if (!currentList.contains(obj)) {
							diff.remoed.add(obj);
						}
					}
					diff.isEquals = diff.reserve.size() == prevList.size();
				}
			}
			diff.isStarted = isStarted;
			return diff;
		}

		Integer h = 0;
		@Override
		public int hashCode() {
			// TODO: Implement this method
			Integer hash = h;
			if (hash == null) {
				int cache = 0;
				for (ProcessObject po: getList())
					cache += po.hashCode();
				this.h = hash = cache;
			}
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj == this) return true;
			if (obj instanceof ProcessObjectGroup) {
				ProcessObjectGroup b = (ProcessObjectGroup) obj;

				return Objects.equals(getList(), b.getList());
			}
			return false;
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return getList().toString();
		}

		public FindProcesser getFindProcesser() { return f; }
		public ProcessObject newProcessObject() { return new ProcessObject(this); }
	}
	public static class ProcessObject  {
		ProcessObjectGroup pog;
		protected ProcessObject(ProcessObjectGroup pog) {
			this.pog = Objects.requireNonNull(pog, "parameter");
		}

		String ppid;
		public void   setPpid(String ppid) { this.ppid = ppid; }
		public String getPpid() { return ppid; }

		String pid;
		public void   setPid(String pid) { this.pid = pid; }
		public String getPid() { return pid;}

		@Override
		public int hashCode() {
			// TODO: Implement this method
			return null == pid ?0: pid.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj == this) return true;
			if (obj instanceof ProcessObject) {
				ProcessObject b = (ProcessGuard.ProcessObject) obj;

				return  
					Objects.equals(getPpid(), b.getPpid()) &&
					Objects.equals(getPid(),  b.getPid());
			}
			return false;
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return "<" + getPpid() + "," + getPid() + ">";
		}

		public ProcessObjectGroup getGroup() { return pog; }
	}


	FindProcesser processChecker;
	public void          setFindProcesser(FindProcesser p) { this.processChecker = Objects.requireNonNull(p, "parameter"); }
	public FindProcesser getFindProcesser() { return processChecker; }


	ProcessMonitor monitor;
	public void setMonitor(ProcessMonitor monitor) {
		this.monitor = monitor;
	}
	public ProcessMonitor getMonitor() {
		return monitor;
	}

	ProcessMonitorCallback processMonitorCallback;
	public void setCallback(ProcessMonitorCallback processMonitorCallback) {
		this.processMonitorCallback = processMonitorCallback;
	}
	public ProcessMonitorCallback getCallback() {
		return processMonitorCallback;
	}

	public ProcessObjectGroup findProcessGroup() {
		if (null == processChecker)
			throw new NullPointerException("process checker");
		return processChecker.findProcess();
	}



	private ProcessGuard() {}

	static final Set<ProcessGuard> pgs = new LinkedHashSet<>();
	static public ProcessGuard newProcessGuard() {
		synchronized (pgs) {
			ProcessGuard instance;
			instance = new ProcessGuard();
			instance.setMonitor(new ProcessGuard.ProcessMonitor(instance));

			pgs.add(instance);

			return instance;
		}
	}
	static public int getInstanceCount() {
		synchronized (pgs) {
			return pgs.size();
		}
	}
	static public void closeAll() {
		synchronized (pgs) {
			for (ProcessGuard pg: pgs) {
				close(pg);
			}
		}
	}
	static public void close(ProcessGuard pg) {
		if (pg != null) {
			synchronized (pgs) {
				if (pgs.contains(pg)) {
					pg.exit();
					pgs.remove(pg);
				} else {
					throw new RuntimeException("not found: " + pg.toString());
				}
			}
		}
	}


}
