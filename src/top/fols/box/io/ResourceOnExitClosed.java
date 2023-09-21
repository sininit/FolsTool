package top.fols.box.io;

import top.fols.atri.interfaces.interfaces.ICaller;
import top.fols.atri.util.DoubleLinkedList;
import top.fols.box.lang.Result;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import top.fols.atri.io.util.Streams;
import java.io.InputStream;

public class ResourceOnExitClosed {
	private ResourceOnExitClosed() {}


	public static class Locks {
		private final Object linkedLock = new Object();
		private DoubleLinkedList<Lock> linked = new DoubleLinkedList<>();

		/**
		 * @param runnable executor
		 * @param <R> invalid
		 * @return Create Lock Thread And Start()
		 */
		@Deprecated
		public final <R> Lock execute(ICaller<Lock, R> runnable) {
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
		 * Create Lock Thread And Wait Execute Result
		 *
		 * @param runnable
		 * @param <R> Create Lock Thread And Start()
		 * @return Execute Result
		 */
		public final <R> R executeAndJoins(final ICaller<Lock, R> runnable) {
			final Result<R, Throwable> result = new Result<>();
			ICaller<Lock, R> lockExecutorValue = new ICaller<Lock, R>() {
				@Override
				public R next(Lock param) {
					try {
						return result.set(runnable.next(param)).get();
					} catch (Throwable e) {
						result.setError(e);
						return null;
					}
				}
			};
			Lock lock = execute(lockExecutorValue);
			if (null != lock) {
				lock.joins();
			}
			if (result.isReturn()) {
				return result.get();
			} else {
				throw new RuntimeException(result.getError());
			}
		}






		/** 
		 * Will not be interrupted when encountering InterruptedException
		 * 
		 * @return If any thread is has InterruptedException, 
		 *         it will return false
		 */
		public final boolean joins() {
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
		public final boolean joins(long millis) {
			if (millis < 0) {
				throw new IllegalArgumentException("timeout tip is negative");
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
			for (DoubleLinkedList.Element<Lock> element = linked.getFirst(); null != element; element = (DoubleLinkedList.Element<Locks.Lock>) element.getNext()) {
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
			AtomicReference<ICaller<Lock, ?>> runnable = new AtomicReference<>();


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
						runnable.get().next(this);
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


			/**
			 * Will not be interrupted when encountering InterruptedException
			 *
			 * @return If any thread is has InterruptedException,
			 *         it will return false
			 */
			public boolean joins() {
				boolean result = true;
				try {
					this.join();
				} catch (InterruptedException e) {
					result = false;
				}
				return result;
			}
		}
	}

	static { initialize(); }
	static final Locks FILE_LOCK = new Locks();
	static void exit() {
		FILE_LOCK.joins();
	}


	public static void initialize() {
		Shutdown.initialize();
	}
	public static void addShutdownHook(Runnable runnable) {
		Runtime.getRuntime().addShutdownHook(new Thread(runnable));
	}

	public static boolean isHookConsoleKillHandler() {
		return Shutdown.isHookConsoleKillHandler();
	}

	/**
	 * Create Lock Thread And Wait Execute Result
	 *
	 * @param runnable
	 * @param <R> Create Lock Thread And Start()
	 * @return Execute Result
	 */
	public static <R> R apply(final ICaller<Locks.Lock, R> runnable) {
		return FILE_LOCK.<R>executeAndJoins(runnable);
	}


	public static boolean writeFile(final File file, final InputStream bytes) {
		return apply(new ICaller<Locks.Lock, Boolean>(){
				@Override
				public Boolean next(ResourceOnExitClosed.Locks.Lock p1) {
					// TODO: Implement this method
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(file);
						Streams.copy(bytes, fos);
						fos.flush();
					} catch (IOException e) {
						throw new RuntimeException(e);
					} finally {
						Streams.close(fos);
					}
					return true;
				}
			});
	}
	public static boolean writeFile(final File file, final byte[] bytes) {
		return apply(new ICaller<Locks.Lock, Boolean>(){
				@Override
				public Boolean next(ResourceOnExitClosed.Locks.Lock p1) {
					// TODO: Implement this method
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(file);
						fos.write(bytes);
						fos.flush();
					} catch (IOException e) {
						throw new RuntimeException(e);
					} finally {
						Streams.close(fos);
					}
					return true;
				}
			});
	}


	static class Shutdown {
		static final Object lock = new Object();
		static boolean isHookConsoleKillHandler;
		static void initialize() {
			synchronized (lock) {
				if (!isHookConsoleKillHandler) {
					try {
						hookConsoleKillHandler();
						isHookConsoleKillHandler = true;

						Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO: Implement this method
								ResourceOnExitClosed.exit();
							}
						}));
					} catch (Throwable e) {
						throw new UnsupportedOperationException(e);
					}
				}
			}
		}
		public static boolean isHookConsoleKillHandler() {
			synchronized (lock) {
				return Shutdown.isHookConsoleKillHandler;
			}
		}

		static void hookConsoleKillHandler() {
			new MqKillHandler().registerSignal("TERM");
			new MqKillHandler().registerSignal(System.getProperties().getProperty("os.name").toLowerCase().startsWith("win") ? "INT" : "USR2");
		}
		static class MqKillHandler  implements sun.misc.SignalHandler {
			public MqKillHandler() { }

			/**
			 * 注册信号
			 * @param signalName name
			 */
			public void registerSignal(String signalName) {
				sun.misc.Signal signal = new sun.misc.Signal(signalName);
				sun.misc.Signal.handle(signal, this);
			}
			@Override
			public void handle(sun.misc.Signal signal) {
				try {
					System.exit(0);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}
}
