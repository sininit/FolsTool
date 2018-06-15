package top.fols.box.util;

import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.interfaces.Destroyable;
import top.fols.box.statics.XStaticFixedValue;
public final class XStackUtils
{
	private static final StackTraceElement[] nullStack = new StackTraceElement[0];
	@XAnnotations("the stack is the reverse order")
	protected static class StackList implements Destroyable
	{
		@Override
		public void destroyData()
		{
			list = nullStack;
			index = -1;
			originIndex = -1;
		}
		private StackTraceElement[] list;
		private int index;
		private int originIndex;
		private StackList(StackTraceElement[] list, int start)
		{
			this.list = list == null ?nullStack: list;
			this.index = start;
			this.originIndex = index;
		}
		public int getDefaultStackIndex()
		{
			return originIndex;
		}
		public StackList toDefaultStack()
		{
			index = originIndex;
			return this;
		}

		public boolean hasNextStack()
		{
			return index - 1 >= 0 && index != -1;
		}
		public boolean hasPreviousStack()
		{
			return index + 1 < list.length && index != -1;
		}
		public StackTraceElement nextStack()
		{
			if (hasNextStack())
				return list[--index];
			return null;
		}
		public StackTraceElement previousStack()
		{
			if (hasPreviousStack())
				return list[++index];
			return null;
		}

		public StackTraceElement getStackBase()
		{
			if (list.length > 0)
				return list[list.length - 1];
			return null;
		}

		public StackTraceElement getStackTop()
		{
			if (list.length > 0)
				return list[0];
			return null;
		}


		public StackTraceElement getNowIndexStack()
		{
			return list[index];
		}

		public int size()
		{
			return list.length;
		}
		public int getNowStackIndex()
		{
			return index;
		}
		public StackTraceElement getStack(int index)
		{
			return list[index];
		}

		public StackTraceElement[] nextsStack()
		{
			if (index <= 1)
				return nullStack;
			StackTraceElement[] ss = new StackTraceElement[index];
			int i2 = 0;
			for (int i = index - 1;i >= 0;i--)
				ss[i2++] = list[i];
			return ss;
		}
		public StackTraceElement[] previoussStack()
		{
			int size = list.length - index - 1;
			if (size <= 0)
				return nullStack;
			StackTraceElement[] ss = new StackTraceElement[size];
			int i2 = 0;
			for (int i = index + 1;i < list.length;i++)
				ss[i2++] = list[i];
			return ss;
		}

	}

	
	
	
	private static final String TName = XStackUtils.class.getCanonicalName();
	private static final int findNowStackIndex(StackTraceElement[] t, String classname, String methodname)
	{
		if (t == null)
			throw new NullPointerException();
		int index = -1;
		for (StackTraceElement s:t)
		{
			++index;
			if (classname.equals(s.getClassName()) && methodname.equals(s.getMethodName()))
				break;
		}
		if (index == -1 || index >= t.length)
			return -1;
		return index;
	}
	public static final StackTraceElement getNowStack()
	{
		/*
		 dalvik.system.VMStack.getThreadStackTrace(Native Method)
		 java.lang.Thread.getStackTrace(Thread.java:1565)
		 top.fols.java.Strack.getThisClass(Strack.java:11)
		 Main.main(Main.java:31)
		 java.lang.reflect.Method.invoke(Native Method)
		 com.aide.ui.build.java.RunJavaActivity$1.run(SourceFile:108)
		 java.lang.Thread.run(Thread.java:760)
		 */
		StackTraceElement[] t = Thread.currentThread().getStackTrace();
		int index =  findNowStackIndex(t, TName, "getNowStack");
		if (index == -1)
			return null;
		return t[++index];
	}
	public static final StackList getNowStacks()
	{
		StackTraceElement[] t = Thread.currentThread().getStackTrace();
		int index =  findNowStackIndex(t, TName, "getNowStacks");
		if (index == -1)
			return null;
		StackList limit = new StackList(t, ++index);
		return limit;
	}
}

