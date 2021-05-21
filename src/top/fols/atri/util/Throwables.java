package top.fols.atri.util;

import top.fols.box.statics.XStaticSystem;

import java.io.*;

@SuppressWarnings("all")
public class Throwables {
	public static String toString(Throwable e) {
		if (null == e) {
			return "";
		}
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String str = sw.toString();
		try {
			sw.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		return str;
	}

	public static void print(OutputStream out, Throwable e) {
		if (null == e) {
			return;
		}
		PrintStream sw = new PrintStream(out);
		e.printStackTrace(sw);
		sw = null;
	}

	public static void print(Writer out, Throwable e) {
		if (null == e) {
			return;
		}
		PrintWriter sw = new PrintWriter(out);
		e.printStackTrace(sw);
		sw = null;
	}

	public static void println(OutputStream out, Throwable e) {
		if (null == e) {
			return;
		}
		PrintStream sw = new PrintStream(out);
		e.printStackTrace(sw);
		sw.print(XStaticSystem.getLineSeparatorBytes());
		sw = null;
	}

	public static void println(Writer out, Throwable e) {
		if (null == e) {
			return;
		}
		PrintWriter sw = new PrintWriter(out);
		e.printStackTrace(sw);
		sw.print(XStaticSystem.getLineSeparatorBytes());
		sw = null;
	}

}
