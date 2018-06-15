package top.fols.box.net.base.ftp9;
import top.fols.box.util.XMap;
import top.fols.box.annotation.XAnnotations;
import java.io.PrintStream;

@XAnnotations("this is false class")
public class PlatformLogger
{
	public PrintStream print = System.out;
	private static XMap<PlatformLogger> list = new XMap<PlatformLogger>();
	public void finest(String response)
	{
		print.println(response);
	}
	public static PlatformLogger getLogger(String p0)
	{
		PlatformLogger logger = list.get(p0);
		if (logger == null)
			list.put(p0, logger = new PlatformLogger());
		return logger;
	}}
