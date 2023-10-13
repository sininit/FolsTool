package top.fols.box.reflect.re;
import java.util.Iterator;

import top.fols.atri.assist.util.ArrayLists;
import top.fols.atri.lang.Strings;
import top.fols.atri.interfaces.annotations.NotNull;

public class Re_NativeStack {
	public static String toLineAddressString(String fileName, int line) {
		return '(' + fileName + ":" + line + ')';
	}
	public static String toLineAddressString(String funName, String fileName, int line) {
		return funName + '(' + fileName + ":" + line + ')';
	}
	public static ReTraceElement parseLineAddressString(String toLineAddressString) {
		String fun;
		String path;
		int line;
		if (null == toLineAddressString) {
			fun  = "";
			path = "";
			line = 0;
		} else {
			int offsetPath = toLineAddressString.indexOf('(') + 1;
			int offsetLine = toLineAddressString.lastIndexOf(":");
			int offset = offsetPath;
			while ((offset > 0) && !(toLineAddressString.charAt(offset - 1) <= ' ')) {
				offset--;
			}
			fun  = toLineAddressString.substring(offset, offsetPath - 1);

			if (offsetPath == offsetLine) {
				path = "";
			} else {
				path = toLineAddressString.substring(offsetPath, offsetLine);
			}

			line = Integer.parseInt(toLineAddressString.substring(offsetLine + 1, toLineAddressString.lastIndexOf(')')));
		}
		return new ReTraceElement(fun, path, line);
	}



	@SuppressWarnings("SpellCheckingInspection")
	public static final ITraceElement[] 	  	  	  EMPTY_ITRACE_ELEMENTS = {};
	@SuppressWarnings("SpellCheckingInspection")
	public static final ReNativeTraceElement[]  	  EMPTY_NATIVETRACES = {};
	@SuppressWarnings("SpellCheckingInspection")
	public static final ReTraceElement[] 		      EMPTY_TRACEELEMENTS = {};



	@SuppressWarnings({"UnnecessaryModifier", "UnnecessaryInterfaceModifier"})
	public static interface ITraceElement extends Cloneable {
		public String getMethod();
		public String getFilePath();
		public int getLine();

		public ITraceElement clone();
	}

	public static final class ReTraceElement implements Cloneable, ITraceElement {
		protected final String method;
		protected final String filePath;
		protected final int    line;

		public ReTraceElement(Re_NativeStack.ITraceElement iTrace) {
			this.method   = iTrace.getMethod();
			this.filePath = iTrace.getFilePath();
			this.line     = iTrace.getLine();
		}
		public ReTraceElement(String method, String filePath, int line) {
			this.method   = method;
			this.filePath = filePath;
			this.line     = line;
		}

		@Override public String getMethod() { return method; }
		@Override public String getFilePath() { return filePath; }
		@Override public int getLine() { return line; }


		@Override
		public String toString() {
			// TODO: Implement this method
			return toLineAddressString(this.method, this.filePath, this.line);
		}

		@Override
		public ReTraceElement clone() {
			// TODO: Implement this method
			try {
				return (ReTraceElement) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);//不可能
			}
		}
	}
	protected static final class ReNativeTraceElement implements Cloneable, ITraceElement {
		protected final Re_Executor executor;
		protected final String method;
		protected final String filePath;
		protected int line;

		protected ReNativeTraceElement(Re_Executor executor,
									   String method, String filePath, int line) {
			this.executor = executor;
			this.method   = method;
			this.filePath = filePath;
			this.line = line;
		}


		@Override public String getMethod() { return method; }
		@Override public String getFilePath() {
			return filePath;
		}
		@Override public int getLine() {
			return line;
		}


		@Override
		public String toString() {
			// TODO: Implement this method
			return toLineAddressString(this.method, this.filePath, this.line);
		}

		@Override
		public ReNativeTraceElement clone() {
			// TODO: Implement this method
			try {
				return (ReNativeTraceElement) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);//不可能
			}
		}
		public ReTraceElement toReTraceElement() {
			return new ReTraceElement(this);
		}
	}

	Re re;
	ArrayLists<ReNativeTraceElement>     tracks = new ArrayLists<>(EMPTY_NATIVETRACES);
	boolean 							 throwed;
	Re_PrimitiveClass_exception.Instance throwedException;


	public boolean isEmpty() {
		return tracks.size() == 0;
	}




	private Re_NativeStack(Re_NativeStack beCloneStack) {
		this.re = beCloneStack.re;

		for (ReNativeTraceElement element: beCloneStack.tracks) {
			tracks.add(element.clone());
		}

		this.throwed = beCloneStack.throwed;
		this.throwedException = null == beCloneStack.throwedException ? null : beCloneStack.throwedException.superClone();
	}
	private Re_NativeStack(Re re, ReNativeTraceElement startNativeTrace) {
		this.re = re;

		if (null != startNativeTrace) {
			tracks.add(startNativeTrace.clone());
		}
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public Re_NativeStack clone() {
		// TODO: Implement this method
		return cloneStack(this);
	}

	public Re_PrimitiveClass_exception.Instance createExceptionInstance(String throwReason, Re_NativeStack stack) {
		return Rez.Safes.createInstanceOrThrowEx_exception(re, throwReason, stack);
	}
	static void fillExceptionInstanceTraceElements(Re_PrimitiveClass_exception.Instance instance, Re_NativeStack stack) {
		instance.setStackElements(stack.getReTraceElements());
	}


	protected ReTraceElement[] getReTraceElements() {
		ArrayLists<ReNativeTraceElement> tracks = this.tracks;
		ReTraceElement[] elements = new ReTraceElement[tracks.size()];
		for (int i = elements.length - 1, ri = 0; i >= 0 ; i--, ri++) {
			elements[i] = tracks.get(ri).toReTraceElement();
		}
		return elements;
	}
	protected ReNativeTraceElement[] getReNativeTraceElements() {
		ArrayLists<ReNativeTraceElement> tracks = this.tracks;
		ReNativeTraceElement[] elements = new ReNativeTraceElement[tracks.size()];
		for (int i = elements.length - 1, ri = 0; i >= 0 ; i--, ri++) {
			elements[i] = tracks.get(ri);
		}
		return elements;
	}





	public boolean isThrow() {
		return this.throwed;
	}
	public Re_PrimitiveClass_exception.Instance getThrow() {
		return this.throwedException;
	}


	public void setThrow(String reason) {
		if (throwed) return;//returned

		this.throwed = true;
		this.throwedException = createExceptionInstance(reason, this);
	}
	public void setThrow(Re_PrimitiveClass_exception.Instance reason) {
		if (throwed) return;//returned

		this.throwed = true;
		this.throwedException = reason;
	}


	public void clearThrow() {
		this.throwed = false;
		this.throwedException = null;
	}




	static Re_NativeStack cloneStack(Re_NativeStack stack) {
		return new Re_NativeStack(stack);
	}


	static Re_NativeStack newStack(Re re) {
		return new Re_NativeStack(re, (ReNativeTraceElement) null);
	}
	static Re_NativeStack newThreadStack(Re re, ReNativeTraceElement threadStartNativeTrace) {
		return new Re_NativeStack(re, threadStartNativeTrace);
	}


	public static final int INDEX_REMOVED = -1;

	/**
	 * @return index
	 */
	 int addStackElementAndGetIndex(ReNativeTraceElement current) {
		return tracks.getIndexAndAdd(current);
	}
	/**
	 *
	 * @param index {@link #addStackElementAndGetIndex(ReNativeTraceElement)}
	 * @param lastTrace {@link #addStackElementAndGetIndex(ReNativeTraceElement)}
	 */
	 int removeStackElementFromIndex(int index, ReNativeTraceElement lastTrace) {
		tracks.removeIfIdentityEquals(index, lastTrace); // 理论上每次删除的都是最后一个指针 防止未知情况
		return INDEX_REMOVED;
	}
	/**
	 * 删除某个栈之前的所有栈
	 * @param index {@link #addStackElementAndGetIndex(ReNativeTraceElement)}
	 * @param lastTrace {@link #addStackElementAndGetIndex(ReNativeTraceElement)}
	 */
	@SuppressWarnings({"SpellCheckingInspection", "ListRemoveInLoop"})
	 void disconBeforeFromIndex(int index, ReNativeTraceElement lastTrace) {
		if (tracks.opt(index) == lastTrace) {//防止未知情况
			for (int i = tracks.size() - 1; i > index; i--) {
				tracks.remove(i);
			}
		}
	}



	@SuppressWarnings("SameParameterValue")
	public String asString() {
		return buildExceptionString0("", "", iterable(this));
	}
	static public String buildExceptionString(@NotNull Re_PrimitiveClass_exception.Instance instance) {
		String retype = instance.getReClass().getName();
		String reason = instance.getReason();
		return buildExceptionString0(retype, reason, iterable(instance));
	}

	static String buildExceptionString0(String type, String reason, Iterable<ITraceElement> top) {
		StringBuilder sb = new StringBuilder();
		sb.append("Traceback:" + ' ') .append(Re_CodeLoader.CODE_LINE_SEPARATOR_CHAR);
		for (ITraceElement trace : top) {
			if (null != trace) {
				sb.append("\t" + "in" + ' ').append(trace).append(Re_CodeLoader.CODE_LINE_SEPARATOR_CHAR);
			}
		}
		sb.append("Retype:" + ' ').append(type).append(Re_CodeLoader.CODE_LINE_SEPARATOR_CHAR);
		String ReasonPrefix = "Reason:" + ' ';
		sb.append(ReasonPrefix) .append(Strings.tabsFromSecondLineStart(ReasonPrefix.length(), reason)).append(Re_CodeLoader.CODE_LINE_SEPARATOR_CHAR);
		if (sb.length() > String.valueOf(Re_CodeLoader.CODE_LINE_SEPARATOR_CHAR).length()) {
			sb.setLength(sb.length() - String.valueOf(Re_CodeLoader.CODE_LINE_SEPARATOR_CHAR).length());
		}
		return sb.toString();
	}


	@Override
	public String toString() {
		return asString();
	}


	/**
	 * 字段中的栈不需要倒叙了
	 */
	static Iterable<ITraceElement> iterable(Re_PrimitiveClass_exception.Instance instance) {
		final ReTraceElement[] stackElements = instance.getStackElements();
		return new Iterable<ITraceElement>() {
			@Override
			public Iterator<ITraceElement> iterator() {
				return new Iterator<ITraceElement>() {
					final ITraceElement[] top = stackElements;
					final int size  = top.length;
					int index = 0;

					@Override
					public boolean hasNext() {
						return index < size;
					}

					@Override
					public ITraceElement next() {
						if (hasNext())
							return top[index++];
						return null;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	/**
	 * 反转
	 */
	static Iterable<ITraceElement> iterable(final Re_NativeStack nativeStack) {
		return new Iterable<ITraceElement>() {
			@Override
			public Iterator<ITraceElement> iterator() {
				return new Iterator<ITraceElement>() {
					final ArrayLists<ReNativeTraceElement> top = nativeStack.tracks;
					int index = top.size() - 1;

					@Override
					public boolean hasNext() {
						return index > 0;
					}

					@Override
					public ITraceElement next() {
						if (hasNext())
							return top.value(index--);
						return null;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

}
