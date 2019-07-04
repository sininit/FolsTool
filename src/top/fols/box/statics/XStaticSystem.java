package top.fols.box.statics;
import java.io.File;
import java.nio.charset.Charset;
import java.security.Security;
import top.fols.box.util.XMapKeyCheck;
public final class XStaticSystem {
	public static final String SystemProperty_Key_TmpDir = "java.io.tmpdir";
	public static String getTmpDir() {
		return System.getProperty(SystemProperty_Key_TmpDir);
	}
	public static File getTmpDirFile() {
		return new File(getTmpDir());
	}
	
	public static final String SystemProperty_Key_OSName = "os.name";
	public static String getOSName() {
		return System.getProperty(SystemProperty_Key_OSName);
	}
	
	
	public static XMapKeyCheck<String> getMessageDigestAlgorithms() {
		XMapKeyCheck<String> list = new XMapKeyCheck<String>();
		for (String Str: Security.getAlgorithms("MessageDigest"))
			list.put(Str);
		return list;
	}
	
	
	
	public static String getLineSeparator() {
		return System.lineSeparator();
	}
	private static byte[] lineSeparatorBytes;
	public static byte[] getLineSeparatorBytes() {
		return null == lineSeparatorBytes ? lineSeparatorBytes = getLineSeparator().getBytes(): lineSeparatorBytes;
	}
	
	
	private static String default_charset_encoding_name;
	public static String getDefaultCharsetName() {
		return null == default_charset_encoding_name
		? default_charset_encoding_name = Charset.defaultCharset().name()
		: default_charset_encoding_name;
	}
	
}
