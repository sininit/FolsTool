
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import top.fols.box.io.XStream;
import top.fols.box.io.base.XByteArrayInputStream;
import top.fols.box.io.base.XByteArrayOutputStream;
import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.io.base.XInputStreamLine;
import top.fols.box.io.base.XObjectStreams;
import top.fols.box.io.os.XFile;
import top.fols.box.io.os.XRandomAccessFileInputStream;
import top.fols.box.io.os.XRandomAccessFileOutputStream;
import top.fols.box.lang.XClass;
import top.fols.box.lang.XSequences;
import top.fols.box.lang.XString;
import top.fols.box.lang.impl.sequences.XIntSequenceImpl;
import top.fols.box.lang.impl.sequences.XLongSequenceImpl;
import top.fols.box.lang.reflect.XReflectMatcher;
import top.fols.box.lang.reflect.XReflectPeakMatcher;
import top.fols.box.lang.reflect.optdeclared.XReflectAccessible;
import top.fols.box.net.XCookie;
import top.fols.box.net.XURL;
import top.fols.box.net.XURLConnectionMessageHeader;
import top.fols.box.net.XURLConnectionTool;
import top.fols.box.net.XURLParam;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.time.XTiming;
import top.fols.box.util.XArray;
import top.fols.box.util.XArrayPieceIndexManager;
import top.fols.box.util.XArrays;
import top.fols.box.util.XCycleSpeedLimiter;
import top.fols.box.util.XDoubleLinked;
import top.fols.box.util.XObjects;
import top.fols.box.util.json.JSONObject;
import top.fols.box.io.base.XReaderLine;
import top.fols.box.io.base.XCharArrayReader;

abstract class k {

	int df, tt, yh, dd, ghji, ttfgg, fryh, dedcvhu, rrtgv;

	public abstract void v();

	protected void dddd() {
		System.out.println();
	}
}

public class Main extends k {

	@Override
	public void v() {
		// TODO: Implement this method
	}

	@Override
	protected void dddd() {
		// TODO: Implement this method
		super.dddd();
	}

	public Main() {
	}

	public Main(CharSequence[] s, Main t, CharSequence tt, int gggg, XStaticFixedValue ooh) {
	}

	public static void main() {
		System.out.println("=");
	}

	private static int k = 8;

	public static class v2 {
		protected void k() {
		}
	}

	public static class v1 extends v2 {

	}

	public static void name() {
		System.out.println("sb");
	}

	public void name2() {
		System.out.println("sb");
	}

	// public interface b {
	// default void l() {
	//
	// }
	//
	// public static void b() {
	// }
	// }

	// public class a implements b {
	// public void a() {
	// System.out.println("sb");
	// }
	// }

	public class BB {
		public void b() {

		}
	}

	public class AA extends BB {
		public void a() {

		}

		@Override
		public void b() {

		}
	}

	public static class TestA {
		public void a() {
			System.out.println("a");
		}

		public void a2() {
			System.out.println("a");
		}
	}

	public static class TestB extends TestA {
		public void a() {
			System.out.println("b");
		}
	}

	public static class TestC extends TestB {
		public void a() {
			System.out.println("c");
		}
	}

	private int a, b, c, d, e, f, g, h, r, p, esc, eg, v;

	public Main(Integer p, String dd) {
		System.out.println(1);
	}

	public Main(int p, String dd) {
		System.out.println(2);
	}

	public Main(Integer p, CharSequence ff) {
		System.out.println(3);
	}

	public Main(Integer p, Object ff) {
		System.out.println(4);
	}

	public Main(CharSequence[] p) {
		System.out.println(5);
	}

	public Main(String[] p) {
		System.out.println(6);
	}

	public Main(Object[] p) {
		System.out.println(7);
	}

	public static void main(CharSequence ss) {
	}

	public static void main(Object[][] f) {
	}

	/**
	 * * @ProjectName: flactomp3 * @Package: PACKAGE_NAME * @ClassName:
	 * algorithm.Decode * @Author: 吴成昊 * @Description: * @Date: 2019/4/17 17:26
	 * * @Version: 0.1
	 */
	public static class QmcDecode {
		private static class MaskCacl {
			private int x = -1;
			private int y = 8;
			private int dx = 1;
			private int index = -1;
			private int[][] seedMap = { { 0x4a, 0xd6, 0xca, 0x90, 0x67, 0xf7, 0x52 },
					{ 0x5e, 0x95, 0x23, 0x9f, 0x13, 0x11, 0x7e }, { 0x47, 0x74, 0x3d, 0x90, 0xaa, 0x3f, 0x51 },
					{ 0xc6, 0x09, 0xd5, 0x9f, 0xfa, 0x66, 0xf9 }, { 0xf3, 0xd6, 0xa1, 0x90, 0xa0, 0xf7, 0xf0 },
					{ 0x1d, 0x95, 0xde, 0x9f, 0x84, 0x11, 0xf4 }, { 0x0e, 0x74, 0xbb, 0x90, 0xbc, 0x3f, 0x92 },
					{ 0x00, 0x09, 0x5b, 0x9f, 0x62, 0x66, 0xa1 } };

			public int NextMask() {
				int ret;
				index++;
				if (x < 0) {
					dx = 1;
					y = ((8 - y) % 8);
					ret = ((8 - y) % 8);
					ret = 0xc3;
				} else if (x > 6) {
					dx = -1;
					y = 7 - y;
					ret = 0xd8;
				} else {
					ret = seedMap[y][x];
				}
				x += dx;
				if (index == 0x8000 || (index > 0x8000 && (index + 1) % 0x8000 == 0)) {
					return NextMask();
				}
				return ret;
			}
		}

		private static File getNewExtensionNamePath(String filepath, String newExtensionName) {
			File orgFile = new File(filepath);
			File file = new File(XFile.getParent(orgFile.getAbsolutePath()) + XFile.getNameNoExtension(filepath) + "."
					+ newExtensionName);
			return file;
		}

		public static void decodeQmcFile(String filePath) throws FileNotFoundException, IOException {

			String newExName = null;
			String orgExName = XFile.getExtensionName(filePath);
			if ("qmcflac".equals(orgExName)) {
				newExName = "flac";
			} else if ("qmc3".equals(orgExName)) {
				newExName = "mp3";
			} else if ("qmc0".equals(orgExName)) {
				newExName = "mp3";
			} else {
				throw new IOException(
						"cannot decode file type: " + orgExName + ", can decode type: " + "{qmcflac, qmc3, qmc0}");
			}

			QmcDecode.MaskCacl decode = new QmcDecode.MaskCacl();

			File file = new File(filePath);
			long fileLength = file.length();

			File fileDecodePath = QmcDecode.getNewExtensionNamePath(filePath, newExName);

			FileInputStream fis = new FileInputStream(filePath);
			FileOutputStream fos = new FileOutputStream(fileDecodePath);

			int buflen = 8192;

			XArrayPieceIndexManager arrayPieceIndexManager = new XArrayPieceIndexManager(fileLength, buflen);

			byte[] buffer = new byte[buflen];
			for (int i = 0; i < arrayPieceIndexManager.getPieceCount(); i++) {
				int read = fis.read(buffer, 0, (int) arrayPieceIndexManager.getPieceSize(i));

				for (int i2 = 0; i2 < read; i2++) {
					buffer[i2] = (byte) (decode.NextMask() ^ buffer[i2]);
				}
				fos.write(buffer, 0, read);
			}
			fos.flush();
			fos.close();
			fis.close();

			buffer = null;

		}
	}

	//
	//
	//
	//
	//
	public static class XBase64Simple {
		private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
				'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0',
				'1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
		private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0,
				1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1,
				-1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
				49, 50, 51, -1, -1, -1, -1, -1 };

		public static String encode(byte[] data) {
			StringBuilder sb = new StringBuilder();
			int len = data.length;
			int i = 0;
			int b1, b2, b3;
			try {
				while (i < len) {
					b1 = data[i++] & 0xff;
					if (i == len) {
						sb.append(base64EncodeChars[b1 >>> 2]);
						sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
						sb.append("==");
						break;
					}
					b2 = data[i++] & 0xff;
					if (i == len) {
						sb.append(base64EncodeChars[b1 >>> 2]);
						sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
						sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
						sb.append("=");
						break;
					}
					b3 = data[i++] & 0xff;
					sb.append(base64EncodeChars[b1 >>> 2]);
					sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
					sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
					sb.append(base64EncodeChars[b3 & 0x3f]);
				}
			} catch (Exception e) {
				e = null;
			}
			return sb.toString();
		}

		public static byte[] decode(String str) throws UnsupportedEncodingException {
			StringBuilder sb = new StringBuilder();
			byte[] data = str.getBytes("US-ASCII");
			int len = data.length;
			int i = 0;
			int b1, b2, b3, b4;
			try {
				while (i < len) {
					/* b1 */
					do {
						b1 = base64DecodeChars[data[i++]];
					} while (i < len && b1 == -1);
					if (b1 == -1)
						break;

					/* b2 */
					do {
						b2 = base64DecodeChars[data[i++]];
					} while (i < len && b2 == -1);
					if (b2 == -1)
						break;
					sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

					/* b3 */
					do {
						b3 = data[i++];
						if (b3 == 61)
							return sb.toString().getBytes("ISO-8859-1");
						b3 = base64DecodeChars[b3];
					} while (i < len && b3 == -1);
					if (b3 == -1)
						break;
					sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

					/* b4 */
					do {
						b4 = data[i++];
						if (b4 == 61)
							return sb.toString().getBytes("ISO-8859-1");
						b4 = base64DecodeChars[b4];
					} while (i < len && b4 == -1);
					if (b4 == -1)
						break;
					sb.append((char) (((b3 & 0x03) << 6) | b4));
				}
			} catch (Exception e) {
				e = null;
			}
			return sb.toString().getBytes("ISO-8859-1");
		}
	}

	public static class Vx {
		public static List<String> getList() throws IOException {
			XURLConnectionTool.GetRequest get = new XURLConnectionTool.GetRequest(
					"https://mp.weixin.qq.com/s/uzxGzhWKNrNutvATzJzLvQ");
			String source = get.toString();
			Pattern pattern = Pattern.compile("\"http://mp.weixin.qq.com/s\\?__biz=.*?\"", Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(source);
			List<String> list = new ArrayList<>();
			while (matcher.find()) {
				String url;
				url = matcher.group();
				url = url.substring(1, url.length() - 1);
				list.add(url);

				if (list.size() >= 10) {
					break;
				}
			}
			return list;
		}

		private static String getSource(String url, String c) throws IOException {
			XURLConnectionTool.GetRequest get = new XURLConnectionTool.GetRequest(url);
			HttpURLConnection urlcon = (HttpURLConnection) get.getURLConnection();

			XURLConnectionMessageHeader mh = new XURLConnectionMessageHeader();
			mh.putAll("Connection: keep-alive");
			mh.putAll("Cache-Control: max-age=0");
			mh.putAll("Upgrade-Insecure-Requests: 1");
			mh.putAll(
					"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
			mh.putAll("Sec-Fetch-Mode: navigate");
			mh.putAll("Sec-Fetch-User: ?1");
			mh.putAll(
					"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			mh.putAll("Sec-Fetch-Site: none");
			mh.putAll("Accept-Encoding: gzip, deflate, br");
			mh.putAll("Accept-Language: zh-CN,zh;q=0.9");
			mh.putAll("Cookie: " + c);
			mh.setToURLConnection(urlcon);

			Map<String, List<String>> headerFields = urlcon.getHeaderFields();
			String LocationKey = "Location";
			if (null != headerFields.get(LocationKey)) {
				Map<String, String> cookie = XCookie.getHeaderCookieToMap(urlcon);
				return Main.Vx.getSource(headerFields.get(LocationKey).get(0), XCookie.cookieMapToString(cookie));
			}
			System.out.println(XString.join(urlcon.getHeaderFields(), "\n"));
			String source = get.toString();
			return source;
		}

		public static String getName(String url) throws IOException {
			String source = getSource(url, "").toString();

			String source2 = XString.submiddle(source, "<mpvoice ", "</mpvoice>");
			return XString.submiddle(source2, "name=\"", "\"");
		}

		public static String getMediaid(String url) throws IOException {
			String source = getSource(url, "").toString();
			String source2 = XString.submiddle(source, "<mpvoice ", "</mpvoice>");
			return XString.submiddle(source2, "voice_encode_fileid=\"", "\"");
		}

		public static String getDownloadAddres(String voice_encode_fileid) throws IOException {
			return String.format("https://res.wx.qq.com/voice/getvoice?mediaid=%s", voice_encode_fileid);
		}

		public static void download() throws IOException {
			List<String> list = getList();
			File dir = new File("C:\\Users\\Administrator\\Desktop\\新建文件夹");
			for (int i = 0; i < list.size(); i++) {
				String u = list.get(i);

				String name = getName(u);
				String mediaid = getMediaid(u);

				String downloadLink = getDownloadAddres(mediaid);
				System.out.println("name=" + name + ", link=" + u + ", downloadLink=" + downloadLink);
			}
		}
	}




	
	public static void main1(int a) {
	}

	public static void main1(Object args) {
	}

	public static void main1(Object[] args) {
	}

	public static void main1(Object[][] args) {
	}

	public static void main1(int[] args) {
	}

	public static void main1(InputStream[] args) throws Throwable {
	}

	public static void main1(int[][] args) {
	}

	public static void main1(InputStream[][] args) throws Throwable {
	}

	public static void main(String[] args) throws Throwable {




		// System.out.println(XString.join(XReflectAccessibleInherit.getMethodsAll(a.class),
		// "\n"));

		// byte[] bss = "package top.fols.box.util; public class Main\r\n{\n
		// System.out.println(XArrays.toString(new byte[]{66})); }\n"
		// .getBytes();
		// // bss = "1,,,2,,3,,4,,5,,6,,7,,8,,9".getBytes();

		// XByteArrayInputStream input0 = new XByteArrayInputStream(bss);
		// byte[] lines = " ".getBytes();

		// XInputStreamLine<XByteArrayInputStream> input = new
		// XInputStreamLine<XByteArrayInputStream>(input0, 1);

		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());
		// System.out.println("[" + XObjects.toString(new String(input.readLine(lines,
		// true), "UTF-8").getBytes()) + "]");
		// System.out.println("pindex: " + input.readLineSeparatorsIndex());

		//

		// TestA.class.getMethod("a").invoke(new TestA());
		// TestA.class.getMethod("a").invoke(new TestB());
		// TestA.class.getMethod("a").invoke(new TestC());
		// System.out.println();

		// System.out.println(XString.join(XReflectCacheFast.defaultInstance.getAllInheritMethods(TestC.class),
		// "\n"));
		// System.out.println();
		// System.out.println(XString.join(XReflectCacheFast.defaultInstance.getAllInheritMethodsFast(TestC.class),
		// "\n"));

		// System.out.println();
		// System.out.println(XReflectCache.defaultInstance.getMethod(TestC.class,
		// "a"));
		// System.out.println(XReflectCacheFast.defaultInstance.getMethod(TestC.class,
		// "a"));
		// System.out.println();

		// if (true) {
		// return;
		// }

		
		Compiler.start("C:\\Program Files\\Java\\jdk1.8.0_212\\bin", "src", "libs", "top.fols.box.jar");
		if (true) {
			return;
		}

		System.out.println(XReflectPeakMatcher.defaultInstance.getMethod(Main.class, "main1",
				new Class[] { XByteArrayInputStream[].class }));

		if (true) {
			return;
		}
		Class searchClass = Main.class;
		Class[] searchConstructorClass;

		System.out.println("----list---");
		System.out.println(XString
				.join(XReflectPeakMatcher.defaultInstance.getCacher().getConstructors(searchClass).list(), "\n"));

		System.out.println("-------");
		System.out.println("search-list: "
				+ (XClass.joinParamJavaClassCanonicalName(
						searchConstructorClass = new Class[] { int.class, String.class }))
				+ " result="
				+ XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
						"\n"));

		System.out.println("-------");
		System.out.println("search: "
				+ (XClass.joinParamJavaClassCanonicalName(
						searchConstructorClass = new Class[] { Integer.class, String.class }))
				+ " result="
				+ XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
						"\n"));

		System.out.println("-------");
		System.out.println("search: "
				+ (XClass.joinParamJavaClassCanonicalName(searchConstructorClass = new Class[] { Integer.class, null }))
				+ " result="
				+ XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
						"\n"));

		System.out.println("-------");
		System.out.println("search: "
				+ (XClass.joinParamJavaClassCanonicalName(searchConstructorClass = new Class[] { int[][].class }))
				+ " result="
				+ XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
						"\n"));

		System.out.println("-------");
		System.out.println("search: "
				+ (XClass.joinParamJavaClassCanonicalName(
						searchConstructorClass = new Class[] { int.class, CharSequence.class }))
				+ " result="
				+ XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
						"\n"));

		System.out.println("-------");
		System.out.println("search: "
				+ (XClass.joinParamJavaClassCanonicalName(searchConstructorClass = new Class[] { String[].class }))
				+ " result="
				+ XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
						"\n"));

		System.out.println("-------");
		System.out.println("search: "
				+ (XClass.joinParamJavaClassCanonicalName(
						searchConstructorClass = new Class[] { int.class, JSONObject.class }))
				+ " result="
				+ XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
						"\n"));

		System.out.println("-------");
		System.out.println("search: "
				+ (XClass.joinParamJavaClassCanonicalName(
						searchConstructorClass = new Class[] { Integer.class, Object.class }))
				+ " result="
				+ XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
						"\n"));

		System.out.println("-------");
		System.out.println("-------");

		Class returnType = void.class;
		Class mcc = Main.class;
		Class[] pcc = new Class[] { String[].class };
		XTiming jsq00 = XTiming.newAndStart();
		for (int i = 0; i < 100 * 10000; i++) {
			XReflectPeakMatcher.defaultInstance.getConstructor(mcc, pcc);
		}
		System.out.println(jsq00.endAndGetEndLessStart());

		if (true) {
			return;
		}

		/* xprocess xurl */
		// System.out.println("*" + XProcess.execCommand2String(null, new String[] {
		// "cmd" }, new String[] { "echo 影流之主" },
		// Charset.forName("GB2312")) + "*");

		//

		/* test xurl */
		try {
			System.out.println("______test xurl start");

			String urlStr = "http://tester:123456@www.baidu.com?sb?a=b&b=c&c=d#abc";
			URL url = new URL(urlStr);
			String protocol = url.getProtocol();
			String host = url.getHost();
			int port = url.getPort();
			int defaultPort = url.getDefaultPort();
			String query = url.getQuery();
			String ref = url.getRef();
			String user = url.getUserInfo();
			String authority = url.getAuthority();
			String file = url.getFile();
			// Object content = url.getContent();
			System.out.println(url.getPath());

			System.out.println("______");
			String testUrl;
			testUrl = "http://tester:123456@www.baidu.com/s/k?s/sb?a=b  #abc/";
			// testUrl = "http://127.0.0.1:7777/_HM4X/1qw/r/t/b";
			// testUrl = "http://127.0.0.1:7777?/post=4/_HM4X/1qw/r/t/b";
			// testUrl = "http://127.0.0.1:7777/l/..?path=%2F_PhoneFile%2F/listmode=1";
			testUrl = "http://tester:123456@www.baidu.com//a///b///c/..//?sb//../..///?a=b&b=c&c=d//../#cxk";

			System.out.println(testUrl);
			XURL tsx = new XURL(testUrl).absoluteUrl();
			System.out.println(tsx);
			Method[] ms = XURL.class.getMethods();
			for (Method m : ms) {
				if (m.getParameterTypes().length == 0) {
					System.out.println(m.getName() + " ==> " + m.invoke(tsx));
				}
			}

			System.out.println("_________");

			System.out.println("______test xurl end");

		} catch (Exception e) {
			e.printStackTrace();
		}

		/* test xsequence */
		try {
			System.out.println("______test xsequence start");

			// can not be compared to an array type is not the same array
			System.out.println();
			System.out.println(XReflectMatcher.defaultInstance.getField(Main.class, "k"));
			System.out.println(XSequences.deepIndexOf(XSequences.wrapArr(new int[] { 1, 1, 2, 3 }),
					XSequences.wrapArr(new int[] { 1 }), 1, 2));
			System.out.println(XSequences.deepLastIndexOf(XSequences.wrapArr(new int[] { 1, 1, 2, 3 }),
					XSequences.wrapArr(new int[] { 1, 1 }), 1, 0));

			System.out.println(XSequences.deepIndexOf(XSequences.wrapArr(new int[] { 1, 1, 2, 3 }),
					XSequences.wrapArr(new int[] { 1 }), 0, 2));
			System.out.println(XSequences.deepLastIndexOf(XSequences.wrapArr(new int[] { 1, 1, 2, 3 }),
					XSequences.wrapArr(new int[] { 1, 1 }), 0, 0));
			System.out.println("_____");

			String[] testStrArr = new String[1];
			XArrays.arraycopyTraverse(new Object[] { 8 }, 0, testStrArr, 0, 1);
			System.out.println(Arrays.toString(testStrArr));

			XArray.copyOfConversion(new int[] { 1, 2, 3, 4, 5 }, new long[] {});

			System.out.println(XArrays.equals(new int[] { 4 }, new int[] { 4 }));
			System.out.println(XArrays.equalsRange(new int[] { 4, 5, 7, 9, 10 }, 2, new int[] { 4, 7, 9 }, 1, 2));
			System.out.println(XArrays.deepEqualsRange(new Object[] { new int[] { 4, 7, 9 }, "a", "b" }, 0,
					new Object[] { new int[] { 4, 7, 9 } }, 0, 1));
			System.out.println("_____");

			XTiming ff0 = XTiming.newAndStart();

			System.out.println(XSequences.deepIndexOf(
					XSequences.wrapArr(new Object[] { 0, new Object[] { null, new int[] { 8 } }, new int[] { 123 },
							new Object[][] { { null, 6 } }, "cxk", 5 }),
					XSequences.wrapArr(new Object[] { new Object[] { null, new int[] { 8 } }, new int[] { 123 } }), 0,
					6));
			System.out.println(XSequences.deepLastIndexOf(
					XSequences.wrapArr(new Object[] { 0, new Object[] { null, new int[] { 8 } }, new int[] { 123 },
							new Object[][] { { null, 6 } }, "cxk", 5 }),
					XSequences.wrapArr(new Object[] { new Object[] { null, new int[] { 8 } }, new int[] { 123 } }), 6,
					0));

			System.out.println(XSequences.deepIndexOf(XSequences.wrapArr(new int[] { 1, 2, 3 }),
					XSequences.wrapArr(new long[] { 1, 2, 3 }), 0, 3));
			System.out.println(XSequences.deepIndexOf(XSequences.wrapArr(new int[] { 1, 2, 3 }),
					XSequences.wrapArr(new int[] { 1, 2, 3 }), 0, 3));
			System.out.println("&" + ff0.endAndGetEndLessStart());
			System.out.println();

			int testlength = 1000000;
			int[] testObjArr = (int[]) XArray.newInstanceFill(int.class, testlength, 0);
			long[] testLongArr = new long[testlength];

			XTiming ff1 = XTiming.newAndStart();
			XArrays.arraycopyTraverse(testLongArr, 0, testObjArr, 0, testObjArr.length);
			System.out.println("&" + ff1.endAndGetEndLessStart());

			XTiming ff2 = XTiming.newAndStart();
			XIntSequenceImpl testObjArrS = new XIntSequenceImpl(testObjArr);
			XLongSequenceImpl testLongArrS = new XLongSequenceImpl(testLongArr);
			XArrays.arraycopyTraverse(testObjArrS.getArray(), 0, testLongArrS.getArray(), 0, testObjArrS.length());
			System.out.println("&" + ff2.endAndGetEndLessStart());
			testLongArrS = null;
			testObjArrS = null;

			System.out.println("______test xsequence end");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* test reflect */
		try {
			System.out.println("______test xreflectoptdeclared start");

			Class<?> mainclass = Main.class;
			Method[] ms0 = XReflectAccessible.getMethodsAll(mainclass);
			for (Method m : ms0)
				System.out.println(m);
			System.out.println();
			System.out.println(Arrays.toString(mainclass.getMethods()));
			System.out.println(Arrays.toString(mainclass.getDeclaredMethods()));
			System.out.println();

			XReflectMatcher.defaultInstance.getMethod(mainclass, "main", new Object[] { new String[] {} });
			System.out.println(XReflectMatcher.defaultInstance.getField(mainclass, "k"));

			XTiming jsq0 = XTiming.newAndStart();
			for (int i = 0; i < 1000000; i++) {
				XReflectMatcher.defaultInstance.getConstructor(mainclass, new String[] {}, new Main(), "", 1,
						new XStaticFixedValue());
			}
			System.out.println(jsq0.endAndGetEndLessStart());
			System.out.println("______test xreflectoptdeclared end");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* test xfiletool */
		try {
			System.out.println("______test xfiletool start");

			System.out.println(XFile.getCanonicalPath("//XSt/*?:]/tt/////.//./././a/b/v//x//a/v**v//n///...//../../();")
					.equals(new File("//XSt/*?:]/tt/////.//./././a/b/v//x//a/v**v//n///...//../../();")
							.getCanonicalPath()));
			System.out.println(XFile.getCanonicalPath(
					"..//XSt/*?:]/tt/////.//././///////../.././a/b/v//x//a/v**v//n///...//../../();"));
			System.out.println(
					new File("..//XSt/*?:]/tt/////.//././///////../.././a/b/v//x//a/v**v//n///...//../../();"));
			System.out.println(XFile.getCanonicalPath("hhh*/.././//////gggghjj/../..ggg/rrrf"));
			/*
			 * true /XSt/a/b/v/x/a/v**v/();
			 * ../XSt/*?:]/tt/./././../.././a/b/v/x/a/v**v/n/.../../../(); /..ggg/rrrf
			 */
			System.out.println("______test xfiletool start");

		} catch (Exception e) {
			e.printStackTrace();
		}

		/* test xurlparam */
		try {
			System.out.println("______test xurlparam start");

			XURLParam xurlp = new XURLParam("from=1012852s&%E8%94%A1%E5%BE%90%E5%9D%A4=%E8%A1%8C%E4%B8%BA");
			System.out.println(xurlp.get("为什么"));
			xurlp.clear();
			xurlp.put("你", "这么骚");
			xurlp.put("??", "esm");
			System.out.println(xurlp);
			System.out.println(xurlp.toFormatString());
			System.out.println(xurlp.get("你"));
			/*
			 * null {%3F%3F=esm, %E4%BD%A0=%E8%BF%99%E4%B9%88%E9%AA%9A}
			 * ?%3F%3F=esm&%E4%BD%A0=%E8%BF%99%E4%B9%88%E9%AA%9A 这么骚
			 */
			System.out.println("______test xurlparam start");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* test multi thread copy file@ speed limiter */
		try {
			System.out.println("______test multi thread copy file@ speed limiter start");

			Thread.sleep(10000000L);
			long start = System.currentTimeMillis();

			XCycleSpeedLimiter xioread = new XCycleSpeedLimiter();
			xioread.setCycleMaxSpeed(24 * 1024 * 1024);// 限制 每个周期时间内 最多读24M
			xioread.setCycle(500);
			xioread.setLimit(true);

			System.out.println(xioread.isLimit());
			System.out.println(xioread);
			XFileThreadCopy tread = null;
			for (int i = 0; i < 10; i++) {
				tread = new XFileThreadCopy("/sdcard/AppProjects/android-22.jar", "/sdcard/" + i + ".test", xioread);
				tread.start();
			}
			while (true) {
				if (false)
					break;
				// Thread.sleep(1000);
				// System.out.println("当前FileIO读写取速度:" +
				// XFileTool.FileFormatSize(xioread.getCycleUseSpeedEverySecondMax()) + " /s
				// 剩余:" + XFileTool.FileFormatSize(xioread.getCycleFreeSpeed()));
				// if(xioread.getEverySecondAverageSpeed()>0)
				System.out.println("当前平均速度:" + XFile.fileUnitFormat(xioread.getAverageSpeed()) + "/S");
			}

			System.out.println();
			System.out.println("\t耗时:" + (new Date().getTime() - start));

			System.out.println("______test multi thread copy file@ speed limiter end");
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static class XFileThreadCopy extends Thread {
		private String srcPath;// 原文件地址
		private String destPath;// 目标文件地址
		private long start, end;// start指定起始位置，end指定结束位置
		private XCycleSpeedLimiter Xiolimit = null;

		// 构造CopyThread方法
		public XFileThreadCopy(String srcPath, String destPath, long start, long end) {
			this(srcPath, destPath, start, end, null);
		}

		public XFileThreadCopy(String srcPath, String destPath) {
			this(srcPath, destPath, 0, new File(srcPath).length());
		}

		public XFileThreadCopy(String srcPath, String destPath, long start, long end, XCycleSpeedLimiter xio) {
			this.srcPath = srcPath;// 要复制的源文件路径
			this.destPath = destPath;// 复制到的文件路径
			this.start = start;// 复制起始位置
			this.end = end;// 复制结束位置
			this.Xiolimit = xio;// 数据流速度限制器
		}

		public XFileThreadCopy(String srcPath, String destPath, XCycleSpeedLimiter xio) {
			this(srcPath, destPath, 0, new File(srcPath).length(), xio);
		}

		public final static int state_copying = 1;
		public final static int state_copyComplete = 2;
		public final static int state_copyException = 4;
		private double copyPercentage = 0;
		private int state = 0;
		private Exception e;

		public int getCopyState() {
			return state;
		}

		public Exception getCopyException() {
			return e;
		}

		public double getCopyPercentage() {
			return copyPercentage;
		}

		public void run() {
			try {
				state = state_copying;
				copyPercentage = 0;
				e = null;

				// 创建一个只读的随机访问文件
				RandomAccessFile randomIn = new RandomAccessFile(srcPath, "r");
				// 创建一个可读可写的随机访问文件
				RandomAccessFile randomOut = new RandomAccessFile(destPath, "rw");
				randomIn.seek(start);// 将输入跳转到指定位置
				randomOut.seek(start);// 从指定位置开始写
				long copylength = end - start;

				InputStream in;
				in = new XRandomAccessFileInputStream(randomIn);
				in = new XInputStreamFixedLength<InputStream>(in, copylength);
				in = XCycleSpeedLimiter.wrap(in, Xiolimit);

				OutputStream out;
				out = new XRandomAccessFileOutputStream(randomOut, start);
				// out = XCycleSpeedLimiter.wrap(out, Xiolimit);

				byte[] buffer = new byte[8192];
				int read = -1;
				long length = 0;
				while (true) {
					if ((read = in.read(buffer)) == -1)
						break;

					out.write(buffer, 0, read);
					length += read;
					copyPercentage = ((double) length / (double) copylength) * 100;
				}
				out.close();// 从里到外关闭文件
				in.close();// 关闭文件
				state = state_copyComplete;
			} catch (Exception e) {
				this.e = e;
				state = state_copyException;
			}
		}
	}

}
