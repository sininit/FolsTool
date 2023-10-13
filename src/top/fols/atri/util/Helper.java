package top.fols.atri.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import top.fols.atri.lock.Locks;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import top.fols.atri.lock.Locks;
import java.nio.file.Files;

public class Helper {
	public static interface IterateElement<T> {
		public T[]     listElement(boolean isRoot, T v);


		public static final IterateElement<File> ITERATE_FILE_ITERABLE_LINK = new IterateElement<File>() {
			final File[] EMPTY = {};

			@Override
			public File[] listElement(boolean isRoot, File v) {
				// TODO: Implement this method
				return null == v ?EMPTY: v.listFiles();
			}
		};
//		public static final IterateElement<File> ITERATE_FILE_IGRENORED_LINK = new IterateElement<File>() {
//			final File[] EMPTY = {};
//
//			@Override
//			public File[] listElement(boolean isRoot, File v) {
//				// TODO: Implement this method
//				if (Files.isSymbolicLink(v.toPath()))
//					return new File[]{};
//				return null == v ?EMPTY: v.listFiles();
//			}
//		};
	}
	public static List<File> iterateDirectoryChildrenFile(File file) {
		ArrayList<File> result;
		result = iterateNode(new ArrayList<File>(), file, IterateElement.ITERATE_FILE_ITERABLE_LINK);
		result.remove(0);
		return result;
	}
	public static List<String> iterateDirectoryChildrenFileRelativePath(final File file) {
		final String path = file.getPath();

		ArrayList<String> result;
		result = iterateNode(new ArrayList<String>(), path, new IterateElement<String>() {
			@Override
			public String[] listElement(boolean isRoot, String v) {
				// TODO: Implement this method
				if (isRoot) {
					File[]      files = file.listFiles();
					if (null != files) {
						String[] names  = new String[files.length];
						for (int i = 0;i < files.length;i++) {
							names[i] = files[i].getName() + (files[i].isDirectory() ?File.separator: "");
						}
						return names;
					}
				} else {
					File f = new File(file.getPath() + File.separator + v);
					File[] files = f.listFiles();
					if (null != files) {
						String[] names  = new String[files.length];
						for (int i = 0;i < files.length;i++) {
							names[i] = v +files[i].getName() + (files[i].isDirectory() ?File.separator: "");
						}
						return names;
					}
				}
				return null;

			}
		});
		result.remove(0);
		return result;
	}



	/**
	 * The returned results include root.
	 * Iterate over all objects under root.
	 */
	public static <T> Collection<T> iterateNode(T root, IterateElement<T> executor) {
		return iterateNode(new ArrayList<T>(), root, executor);
	}
	/**
	 * The returned results include root.
	 * Iterate over all objects under root.
	 */
	public static <T, L extends Collection<T>> L iterateNode(L buffer, T root, IterateElement<T> executor) {
		buffer.add(root);

		Stack<T> stack;
		stack = new Stack<>();
		stack.push(root);

		if (!stack.isEmpty()) {
			T currentFolder = stack.pop();
			T[] elements = executor.listElement(true, currentFolder);
			if (elements != null) {
				for (T element : elements) {
					buffer.add(element);
					stack.push(element);
				}
			}
		}
		while (!stack.isEmpty()) {
			T currentFolder = stack.pop();
			T[] elements = executor.listElement(false, currentFolder);
			if (elements != null) {
				for (T element : elements) {
					buffer.add(element);
					stack.push(element);
				}
			}
		}
		return buffer;
	}





	public static Debounce newDebounce(Runnable runnable, long effectiveTime) {return new Debounce(runnable, effectiveTime);}
	public static class Debounce<T> {
		final Object lock = new Object();

		Runnable runnable;
		long effectiveTime;
		long lastAccessTime;


		public Debounce(Runnable runnable, long effectiveTime) {
			this.setRunnable(runnable != null ? runnable: new Runnable(){@Override public void run() {}});
			this.setEffectiveTime(effectiveTime);
		}

		public Runnable getRunnable() { return this.runnable; }
		public Runnable setRunnable(Runnable r) { Runnable lastRunnable = this.runnable; this.runnable = r; return lastRunnable; }

		public void setEffectiveTime(long time) {
			if (time < 0) {
				time = 0;
			}
			this.effectiveTime = time;
		}
		public long getEffectiveTime() {
			return effectiveTime;
		}

		public void execute() {
			synchronized (lock) {
				long effectiveTime = this.effectiveTime;
				if ((effectiveTime < 0))
					throw new UnsupportedOperationException("error effective time");
				long time = System.currentTimeMillis();
				if  (time - lastAccessTime > effectiveTime) {
					this.runnable.run();
					this.lastAccessTime = time;
				}
			}
		}
	}


	public static QpsLimiter newQpsLimiter(int limit, long toMillis) {
		return new QpsLimiter(limit, toMillis);
	}
	public static class QpsLimiter {
		private long[] accessTime;
		private int limit;
		private int curPosition;
		private long period;


		private final Object LOCK = new Object();

		public QpsLimiter(int limit) {
			this(limit, TimeUnit.SECONDS, 1);
		}
		public QpsLimiter(int limit, long toMillis) {
			this(limit, TimeUnit.MILLISECONDS, toMillis);
		}
		public QpsLimiter(int limit, TimeUnit timeUnit, long period) {
			if (limit < 0) {
				throw new IllegalArgumentException("Illegal Capatity: " + limit);
			}
			this.curPosition = 0;
			this.period = timeUnit.toMillis(period);
			this.limit = limit;

			this.accessTime = new long[limit];
			Arrays.fill(accessTime, 0);
		}


		public void limit() throws InterruptedException {
			synchronized (LOCK) {
				long curTime = System.currentTimeMillis();
				if ((curTime - accessTime[curPosition] < period)) {
					Thread.sleep(period - (curTime - accessTime[curPosition]) + 1);
					curTime = System.currentTimeMillis();
				}
				accessTime[curPosition++] = curTime;
				curPosition = curPosition % limit;
			}
		}

	}



	public static Locks newLockTable() { return new Locks(); }





}

