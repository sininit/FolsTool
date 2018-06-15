package top.fols.box.util;
import java.io.PrintWriter;
import java.io.StringWriter;

public class XExceptionTool
{
	public static String StackTraceToString(Throwable e)
	{
        if (e == null)
			return "";
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
    }

}
