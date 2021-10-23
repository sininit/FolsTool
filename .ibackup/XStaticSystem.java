package top.fols.box.statics;

import java.io.File;
import java.nio.charset.Charset;
import java.security.Security;

import top.fols.atri.util.KeySetMap;

public final class XStaticSystem {

	public static final String SystemPropertyKey_TmpDir = "java.io.tmpdir";

	public static String getTmpDir() {
		return System.getProperty(SystemPropertyKey_TmpDir);
	}

	public static File getTmpDirFile() {
		return new File(XStaticSystem.getTmpDir());
	}

	public static KeySetMap<String> getMessageDigestAlgorithms() {
		KeySetMap<String> list = new KeySetMap<>();
		for (String Str : Security.getAlgorithms("MessageDigest")) {
			list.put(Str);
		}
		return list;
	}


	public static final String WINDOWS_LINE_SEPARATOR = "\r\n";//WINDOWS
    public static final String MAC_LINE_SEPARATOR = "\r";//MAC
	public static final String LINUX_UNIX_LINE_SEPARATOR = "\n";//Linux, Unix
	

	public static String getLineSeparator() {
		return System.lineSeparator();
	}
	public static String[] getAllSystemLineSeparator() {
		return new String[]{
			XStaticSystem.WINDOWS_LINE_SEPARATOR,
			XStaticSystem.MAC_LINE_SEPARATOR,
			XStaticSystem.LINUX_UNIX_LINE_SEPARATOR,
		};
	}

	private static byte[] lineSeparatorBytes;
	public static byte[] getLineSeparatorBytes() {
		return null == lineSeparatorBytes ? lineSeparatorBytes = getLineSeparator().getBytes() : lineSeparatorBytes;
	}

	private static String defaultCharsetName;
	public static String getDefaultCharsetName() {
		return null == defaultCharsetName ? defaultCharsetName = Charset.defaultCharset().name() : defaultCharsetName;
	}

}
