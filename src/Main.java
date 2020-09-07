
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Arrays;
import top.fols.box.io.base.XByteArrayInputStream;
import top.fols.box.io.os.XFile;
import top.fols.box.lang.XClass;
import top.fols.box.lang.XString;
import top.fols.box.lang.reflect.XReflectMatcher;
import top.fols.box.lang.reflect.XReflectPeakMatcher;
import top.fols.box.lang.reflect.optdeclared.XReflectAccessible;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.time.XTimeConsum;
import top.fols.box.util.XArrayPieceIndexManager;
import top.fols.box.util.json.JSONObject;

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



		
		Compiler.start("C:\\Program Files\\Java\\jdk1.8.0_212\\bin", "src", "libs", "top.fols.box.jar");
		if (true) {
			return;
		}








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
		XTimeConsum jsq00 = XTimeConsum.newAndStart();
		for (int i = 0; i < 100 * 10000; i++) {
			XReflectPeakMatcher.defaultInstance.getConstructor(mcc, pcc);
		}
		System.out.println(jsq00.endAndGetEndLessStart());

		if (true) {
			return;
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

			XTimeConsum jsq0 = XTimeConsum.newAndStart();
			for (int i = 0; i < 1000000; i++) {
				XReflectMatcher.defaultInstance.getConstructor(mainclass, new String[] {}, new Main(), "", 1,
						new XStaticFixedValue());
			}
			System.out.println(jsq0.endAndGetEndLessStart());
			System.out.println("______test xreflectoptdeclared end");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
