package top.fols.box.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import top.fols.box.statics.XStaticSystem;

public class XExceptionTool {
	public static String StackTraceToString(Throwable e) {
		if (null == e) {
			return "";
		}
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
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
