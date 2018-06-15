package top.fols.box.statics;
import java.io.File;
import java.security.Security;
import java.util.List;
import top.fols.box.util.ArrayListUtils;
public class XStaticSystem
{
	public static final String System_Property_TmpDir = "java.io.tmpdir";
	public static String getTmpDir()
	{
		return System.getProperty(System_Property_TmpDir);
	}
	public static File getTmpDirFile()
	{
		return new File(getTmpDir());
	}
	
	
	
	


	public static final String System_Property_OSName = "os.name";
	public static String getOSName()
	{
		return System.getProperty(System_Property_OSName);
	}
	public static class OSName
	{    
		private static String OS = getOSName().toLowerCase();  
		private OSName()
		{}  
		public static boolean isLinux()
		{  
			return OS.indexOf("linux") >= 0;  
		}  
		public static boolean isMacOS()
		{  
			return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") < 0;  
		}  
		public static boolean isMacOSX()
		{  
			return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;  
		}  
		public static boolean isWindows()
		{  
			return OS.indexOf("windows") >= 0;  
		}  
		public static boolean isOS2()
		{  
			return OS.indexOf("os/2") >= 0;  
		}  
		public static boolean isSolaris()
		{  
			return OS.indexOf("solaris") >= 0;  
		}  
		public static boolean isSunOS()
		{  
			return OS.indexOf("sunos") >= 0;  
		}  
		public static boolean isMPEiX()
		{  
			return OS.indexOf("mpe/ix") >= 0;  
		}  
		public static boolean isHPUX()
		{  
			return OS.indexOf("hp-ux") >= 0;  
		}  
		public static boolean isAix()
		{  
			return OS.indexOf("aix") >= 0;  
		}  
		public static boolean isOS390()
		{  
			return OS.indexOf("os/390") >= 0;  
		}  
		public static boolean isFreeBSD()
		{  
			return OS.indexOf("freebsd") >= 0;  
		}  
		public static boolean isIrix()
		{  
			return OS.indexOf("irix") >= 0;  
		}  
		public static boolean isDigitalUnix()
		{  
			return OS.indexOf("digital") >= 0 && OS.indexOf("unix") > 0;  
		}  
		public static boolean isNetWare()
		{  
			return OS.indexOf("netware") >= 0;  
		}  
		public static boolean isOSF1()
		{  
			return OS.indexOf("osf1") >= 0;  
		}  
		public static boolean isOpenVMS()
		{  
			return OS.indexOf("openvms") >= 0;  
		} 
		public static boolean isOthersOs()
		{  
			return !isLinux() && !isMacOS()		&& !isMacOSX()		&& !isWindows()		&& !isOS2()		&& !isSolaris()		&& !isSunOS()		&& !isMPEiX()		&& !isHPUX()		&& !isAix()		&& !isOS390()		&& !isFreeBSD()		&& !isIrix()		&& !isDigitalUnix()		&& !isNetWare()		&& !isOSF1()		&& !isOpenVMS();  
		}  

	}




	public static final String getLineSeparator()
	{
		return System.lineSeparator();
	}
	public static List<String> getMessageDigestAlgorithms()
	{
		List<String> list = new ArrayListUtils<String>();
		for (String Str:Security.getAlgorithms("MessageDigest"))
			list.add(Str);
		return list;
	}
}
