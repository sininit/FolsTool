package top.fols.box.io.os;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.XStream;
import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.io.base.XOutputStreamFixedLength;
import top.fols.box.lang.XUnitConversion;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XByteEncodeDetect;
import top.fols.box.util.XDoubleLinked;

public class XFile implements Closeable, Serializable {
	private static final long serialVersionUID = 1L;


	public static final char        UNIX_FILE_SEPARATOR_CHAR = '/';
    public static final String      UNIX_FILE_SEPARATOR_STRING = String.valueOf(UNIX_FILE_SEPARATOR_CHAR);

    public static final char        WINDOWS_FILE_SEPARATOR_CHAR = '\\';
	public static final String      WINDOWS_FILE_SEPARATOR_STRING = String.valueOf(WINDOWS_FILE_SEPARATOR_CHAR);
	
    public static char getSystemFileSeparatorChar() {
        return File.separatorChar;
    }

	public static final String FILE_EXTENSION_NAME_SEPARATOR = ".";







	private String path;

	private transient File file0;
	private transient XRandomAccessFileOutputStream raf0;

	public XFile(CharSequence path) {
		this.path = XFile.normalizePath(path, File.separatorChar);
	}
	public XFile(CharSequence parent, CharSequence subfilepath) {
        this.path = XFile.normalizePath(parent, subfilepath, File.separatorChar);
	}
	public XFile(File file) {
		this(file.getPath());
	}
	public XFile(File file, String subfilepath) {
		this(file.getPath(), subfilepath);
	}





	public String getExtensionName() {
		return XFile.getExtensionName(path);
	}

	public String getName() {
		return XFile.getName(path);
	}

	public String getNameNoExtension() {
		return XFile.getNameNoExtension(path);
	}

	public String getParent() {
		return XFile.getParent(path);
	}

	public String getPath() {
		return this.path;
	}






	private XRandomAccessFileOutputStream openStream() throws IOException {
		if (null == this.raf0) {
			/* seek end index*/
			XRandomAccessFileOutputStream newStream = new XRandomAccessFileOutputStream(this.getFile(), this.getFile().length());
			return this.raf0 = newStream;
		}
		return this.raf0;
	}
	public File getFile() {
		return null == this.file0 ? this.file0 = new File(this.getPath()) : this.file0;
	}



	public XFile append(String bytes) throws IOException {
		byte[] bytes2 = bytes.getBytes();
		return this.append(bytes2, 0, bytes2.length);
	}

	public XFile append(String bytes, String encoding) throws IOException {
		byte[] bytes2 = bytes.getBytes(encoding);
		return this.append(bytes2, 0, bytes2.length);
	}

	public XFile append(byte[] bytes) throws IOException {
		return this.append(bytes, 0, bytes.length);
	}





	public XFile append(byte[] bytes, int off, int len) throws IOException {
		this.openStream().write(bytes, off, len);
		return this;
	}

	public XFile append(InputStream in, long len) throws IOException {
		if (len < 0) {
			return this;
		}
		XStream.copyFixedLength(in, this.openStream(), len);
		return this;
	}

	public XFile empty() throws IOException {
		this.openStream().empty();
		return this;
	}

	public long length() {
		return this.getFile().length();
	}

	@Override
	public void close() {
		// TODO: Implement this method
		try {
			this.openStream().close();
			this.raf0 = null;
		} catch (IOException e) {
		}
	}







	public String detectEncoding() throws IOException, RuntimeException {
		return XByteEncodeDetect.getJavaEncode(this.getFile());
	}

	@Override
	public String toString() throws RuntimeException {
		try {
			byte[] bs = XFile.readFile(this.getFile());
			if (null == bs) {
				return null;
			}
			return new String(bs);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String toString(String encoding) throws IOException {
		return new String(XFile.readFile(this.getFile()), encoding);
	}

	public String toString(Charset encoding) throws IOException {
		return new String(XFile.readFile(this.getFile()), encoding);
	}

	public static InputStream getRangeInputStream(File file, long off, long len) throws IOException {
		XRandomAccessFileInputStream in = new XRandomAccessFileInputStream(file);
		in.seekIndex(off);
		return new XInputStreamFixedLength<XRandomAccessFileInputStream>(in, len);
	}

	public static OutputStream getRangeOutputStream(File file, long off, long len) throws IOException {
		XRandomAccessFileOutputStream out = new XRandomAccessFileOutputStream(file);
		out.seekIndex(off);
		return new XOutputStreamFixedLength<XRandomAccessFileOutputStream>(out, len);
	}

	

	public static byte[] readFile(File file) throws IOException {
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			throw new OutOfMemoryError("overflow memory file length > " + Integer.MAX_VALUE);
		}
		return XFile.readFile(file, 0, (int) length);
	}

	public static byte[] readFile(File file, long off, int len) throws IOException {
		if (null == file) {
			throw new NullPointerException("file for null");
		}
		if (!file.exists()) {
			return XStaticFixedValue.nullbyteArray;
		}
		RandomAccessFile randomFile = null;
		byte[] fileBytes;
		long filelength = file.length();
		try {
			randomFile = new RandomAccessFile(file, XStaticFixedValue.FileOptMode.r());
			randomFile.seek(off);
			if (off + len > filelength) {
				throw new IOException(String.format(
						"read length exceeds total file length, filelen=%s, off=%s, readlen=%s", filelength, off, len));
			}
			fileBytes = new byte[len];
			randomFile.read(fileBytes);
			return fileBytes;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (null != randomFile) {
					randomFile.close();
				}
			} catch (Exception e) {
				randomFile = null;
			}
		}
	}

	public static boolean saveFile(File file, byte[] bytes) throws IOException {
		if (null == file) {
			throw new NullPointerException("file for null");
		}
		if (null == bytes) {
			bytes = XStaticFixedValue.nullbyteArray;
		}
		if (!file.exists()) {
			if (!file.createNewFile()) {
				throw new IOException("canot new file:" + file);
			}
		}
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(file, XStaticFixedValue.FileOptMode.rws());
			randomFile.write(bytes);
			randomFile.setLength(bytes.length);
			return true;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (null != randomFile) {
					randomFile.close();
				}
			} catch (Exception e) {
				randomFile = null;
			}
		}
	}

	@XAnnotations("1B = 8bit")
	private static final XUnitConversion fileUnit1024 = new XUnitConversion(new LinkedHashMap<String, BigDecimal>() {
		/**
		*
		*/
		private static final long serialVersionUID = 1L;

		{
			double base = 1024;
			put("B", new BigDecimal(1));
			put("KB", new BigDecimal(base).pow(1));
			put("MB", new BigDecimal(base).pow(2));
			put("GB", new BigDecimal(base).pow(3));
			put("TB", new BigDecimal(base).pow(4));
			put("PB", new BigDecimal(base).pow(5));
			put("EB", new BigDecimal(base).pow(6));
			put("ZB", new BigDecimal(base).pow(7));
			put("YB", new BigDecimal(base).pow(8));
			put("BB", new BigDecimal(base).pow(9));
			put("NB", new BigDecimal(base).pow(10));
			put("DB", new BigDecimal(base).pow(11));
			put("CB", new BigDecimal(base).pow(12));
		}
	});

	public static String fileUnitFormat(String bytelength) {
		return fileUnit1024.format(new XUnitConversion.Num(bytelength), 2);
	}

	public static String fileUnitFormat(String bytelength, int scale) {
		return fileUnit1024.format(new XUnitConversion.Num(bytelength), scale);
	}

	public static String fileUnitFormat(double bytelength) {
		return fileUnit1024.format(new XUnitConversion.Num(bytelength), 2);
	}

	public static String fileUnitFormat(double bytelength, int scale) {
		return fileUnit1024.format(new XUnitConversion.Num(bytelength), scale);
	}










	/**
	 * Check if the file has an invalid path. Currently, the inspection of
	 * a file path is very limited, and it only covers Nul character check.
	 * Returning true means the path is definitely invalid/garbage. But
	 * returning false does not guarantee that the path is valid.
	 *
	 * @return true if the file path is invalid.
	 */
	public static final boolean isInvalid(String path) {
		return path.indexOf('\u0000') >= 0;
	}





















	/**
	 * Normalize all system separators
	 * 归一化所有系统分隔符修改为目标分隔符
	 *
	 * if @param separator for /
	 * "\a/\b/\/c\d"		return "/a//b///c/d"
	 *
	 */
	public static String toFileSystemSeparator(String path, char separator) {
		StringBuilder sb = new StringBuilder(path.length());
		for (int i = 0;i < path.length();i++) {
			char ch = path.charAt(i);
			if (ch == WINDOWS_FILE_SEPARATOR_CHAR ||
					ch == UNIX_FILE_SEPARATOR_CHAR) {
				ch = separator;
			}
			sb.append(ch);
		}
		return sb.toString();
	}









	/**
	 * Convert path to standard path(Remove duplicate separators)
	 * If the suffix is ​​separator, it will be deleted (unless separator is the only string)
	 * 将路径转换为标准路径(删除重复的分隔符)
	 * 如果字尾为separator将会删除(除非separator是唯一的字符串)
	 *
	 * if @param separator for /
	 * ""			return ""
	 * "/"			return "/"
	 * "a"			return "a"
	 * "a/"			return "a"
	 * "a/b/c/"		return "a/b/c"
	 * "/a/b/c/"	return "/a/b/c"
	 */
	public static String normalizePath(CharSequence path, char separator) {
		StringBuilder ppath = new StringBuilder(path.length());

		int plen = path.length();
		if (plen > 0) {
			char plast = path.charAt(0);
			ppath.append(plast);
			int i;
			for (i = 1;i < plen;i++) {
				char ch = path.charAt(i);
				if (ch == separator) {
					if (plast == separator) {
						continue;
					}
				}
				ppath.append(plast = ch);
			}
			if (plast == separator && ppath.length() > 1) {
				/**
				 * path "a/" "a/b/" ...
				 */
				ppath.setLength(ppath.length() - 1);
			} else {
				/**
				 * path ""  "/"  "a"  "/a"  ...
				 */
			}
		}
		return ppath.toString();
	}


	/**
	 * Convert path to standard path(Remove duplicate separators)
	 * If the suffix is ​​separator, it will be deleted (unless separator is the only string)
	 * 将路径转换为标准路径(删除重复的分隔符)
	 * 如果字尾为separator将会删除(除非separator是唯一的字符串)
	 *
	 * if @param separator for /
	 * "",		""		return ""
	 * "/",		""		return "/"
	 * "/a",	""		return "/a"
	 * "/a",	"/"		return "/a"
	 * "a",		"/"		return "a"
	 * "",		"/a"	return "/a"
	 * "/"      "a/b"	return "/a/b"
	 * "/a",	"/b"	return "/a/b"
	 * "a",		"b"		return "a/b"
	 * "a/",	"b"		return "a/b"
	 */
	public static String normalizePath(CharSequence parent, CharSequence subfilepath, char separator) {
		StringBuilder ppath = new StringBuilder(parent.length());

		int plen = parent.length();
		if (plen > 0) {
			char plast = parent.charAt(0);
			ppath.append(plast);
			int i;
			for (i = 1;i < plen;i++) {
				char ch = parent.charAt(i);
				if (ch == separator) {
					if (plast == separator) {
						continue;
					}
				}
				ppath.append(plast = ch);
			}
			if (plast == separator && ppath.length() > 1) {
				/**
				 * path "a/" "a/b/" ...
				 */
				ppath.setLength(ppath.length() - 1);
			} else {
				/**
				 * path ""  "/"  "a"  "/a"  ...
				 */
			}
		}


		/**
		 * sstart == 4: ////a//////
		 * sstart == 1: /a/////
		 * sstart == 0: a////
		 */
		int sstart = 0;
		int slen = subfilepath.length();
		while (sstart < slen && subfilepath.charAt(sstart) == separator) {
			sstart++;
		}

		/**
		 * There are characters other than delimiters
		 * 存在分隔符以外的字符
		 */
		if (slen - sstart > 0) {
			/**
			 * Determine whether there is content in the character buffer stream
			 * and determine whether the last character of the buffer stream is @param separator If not, add @param separator
			 * 判断字符缓冲流是否存在内容 and 则判断缓冲流最后一个字符是否为 @param separator 如果不是则添加 @param separator
			 */
			if ((ppath.length() > 0 && ppath.charAt(ppath.length() - 1) == separator) == false) {
				ppath.append(separator);
			}

			char slast = subfilepath.charAt(sstart);
			for (int i = sstart;i < slen;i++) {
				char ch = subfilepath.charAt(i);
				if (ch == separator) {
					if (slast == separator) {
						continue;
					}
				}
				ppath.append(slast = ch);
			}

			/**
			 * @param subfilepath a a/ /a/
			 */
			if (slast == separator && ppath.length() > 1) {
				ppath.setLength(ppath.length() - 1);
			}
		} else {
			/**
			 * There are no characters other than the separator,
			 * check whether the character buffer stream is empty,
			 * if it is empty, add a separator
			 * 不存在分隔符以外的字符, 判断一下 字符缓冲流 是否为空，为空则加入一个分隔符
			 */
			if (ppath.length() == 0 && sstart > 0) {
				ppath.append(separator);
			}
		}
		return ppath.toString();
	}






	/**
	 * Handling relative paths that can be handled . or ..
	 * Unrestricted if the prefix is ​​a file separator (Because this is an absolute path)
	 * It will not process drive letters or ~ (Windows DRIVE Letter or Unix ~)
	 *
	 * if @param separator for /
	 * "a/b/.."				return "a/"
	 * "a/../../a/"			return "../a/"
	 * "/../../a/"			return "/a/"
	 * "../../a/"			return "../../a/"
	 * ""					return ""
	 * "/.."				return "/"
	 * ".."                 return ".."
	 */
	public static String dealRelativePath(String path, char separator) {
		int length = path.length();
		if (length == 0) {
			return "";
		}
		String separatorString = String.valueOf(separator);

		XDoubleLinked<String> p = new XDoubleLinked<>(null);
		XDoubleLinked<String> bottom = p;
		XDoubleLinked<String> top = p;

		int lastIndex = 0;
		for (int i = 0; i < length; i++) {
			char ch = path.charAt(i);
			if (ch == separator) {
				String t = path.substring(lastIndex, i);
				XDoubleLinked<String> element;

				if (i > 0) {
					element = new XDoubleLinked<String>(t);
					top.addNext(element);
					top = element;
				}

				element = new XDoubleLinked<String>(separatorString);
				top.addNext(element);
				top = element;

				lastIndex = i + 1;
			}
		}
		if (lastIndex != length) {
			String t = path.substring(lastIndex, length);
			XDoubleLinked<String> element = new XDoubleLinked<String>(t);
			top.addNext(element);
			top = element;
		}
		/**
		 * after >>
		 * /a/b/c//  =>  {null, /, a, /, b, /, c, /, /}
		 * a/b/c//  =>  {null, a, /, b, /, c, /, /}
		 * a/b/c  =>  {null, a, /, b, /, c}
		 */




		XDoubleLinked now = bottom.getNext();
		XDoubleLinked limit = null;
		while (true) {
			if (XDoubleLinked.equalsContent(".", now)) {// 处理 .
				XDoubleLinked next = now.getNext();
				XDoubleLinked prev = now.getPrev();
				now.remove();
				now = prev;

				if (XDoubleLinked.equalsContent(separatorString, next)) {// 实际上必定为true 除非不存在下一个元素
					next.remove();
				}
			} else if (XDoubleLinked.equalsContent("..", now)) {// 处理 ..
				XDoubleLinked prev = now.getPrev();//分隔符 或者为 null

				XDoubleLinked first = bottom.getNext();
				if (prev == bottom) { // 前面不存在分隔符
					/*   ..   */
					XDoubleLinked next = now.getNext();
					limit = XDoubleLinked.equalsContent(separatorString, next) ?next: limit; // 实际上必定为true 除非不存在下一个元素
					now = next;
					continue;
				} if (prev == first) { // 分隔符是在第一个元素 此为绝对路径
					/*   /..   */
					XDoubleLinked next = now.getNext();
					XDoubleLinked last = now.getPrev();
					now.remove();
					now = last;

					if (XDoubleLinked.equalsContent(separatorString, next)) {// 实际上必定为true 除非不存在下一个元素
						next.remove();
					}
					limit = first;
					continue;
				} else {
					if (prev == limit) {// 无法返回上一层
						XDoubleLinked next = now.getNext();
						limit = XDoubleLinked.equalsContent(separatorString, next) ?next: limit;// 实际上必定为true 除非不存在下一个元素
						now = next;
						continue;
					}
				}

				//   a/.. /a/b/../ /a/..  ../a/../ ../../  ../../a/../..
				// 必定不是分隔符 或者为 null
				XDoubleLinked prev_prev = null == prev ? null : prev.getPrev();
				prev_prev = prev_prev == bottom ? null : prev_prev;

				if (null != prev) {
					prev.remove();
				}
				if (null != prev_prev) {
					prev_prev.remove();
				}

				XDoubleLinked next = now.getNext();
				XDoubleLinked last = now.getPrev();
				now.remove();
				now = last;

				if (XDoubleLinked.equalsContent(separatorString, next)) { // 实际上必定为true 除非不存在下一个元素
					next.remove();
				}
			}

			if (!(null != now && null != (now = now.getNext()))) {
				break;
			}
		}

		XDoubleLinked absbottom = bottom.getNext();
		bottom.remove();

		if (null == absbottom) {
			return "";
		} else {
			StringBuilder right = new StringBuilder();
			XDoubleLinked x = absbottom;
			do {
				right.append(x);
			} while (null != (x = x.getNext()));
//			if (right.length() > 1 && right.charAt(right.length() - 1) == separator) { right.setLength(right.length() - 1); }
			return right.toString();
		}
	}


















	/**
	 * Nothing to do with the file system
	 * Treat the path as an absolute path
	 * The delimiter at the end of the suffix will be deleted.
	 * The prefix of the return path must be the @param delimiter
	 * 与文件系统无关
	 * 将路径作为绝对路径处理
	 * 字尾的 @param separator 将被删除 返回路径字首必定为 @param separator
	 *
	 * @param path
	 * @param changeAllFileSystemSeparator if ture will format @param path file
	 *                     separator to @param separator
	 * @param separator
	 * @return
	 */
	public static String getCanonicalRelativePath(String path) {
		return XFile.getCanonicalRelativePath(path, true, File.separatorChar);
	}

	public static String getCanonicalRelativePath(String path, char separator) {
		return XFile.getCanonicalRelativePath(path, true, separator);
	}

	public static String getCanonicalRelativePath(String path, boolean changeAllFileSystemSeparator, char separator) {
		String separatorString = String.valueOf(separator);

		String dealPath = changeAllFileSystemSeparator ?
				XFile.normalizePath(XFile.toFileSystemSeparator(XFile.toAbsolutePath(path, separator), separator), separator):
				XFile.normalizePath(XFile.toAbsolutePath(path, separator), separator);

		String newPath = XFile.dealRelativePath(dealPath, separator);

		if (newPath.startsWith(separatorString) == false) { // path ""
			newPath = new StringBuilder().append(separator).append(newPath).toString();
		}

		return newPath;
	}




	public static String getCanonicalRelativePath(String dir, String subpath) {
		return XFile.getCanonicalRelativePath(dir, subpath, true, File.separatorChar);
	}

	public static String getCanonicalRelativePath(String dir, String subpath, char separator) {
		return XFile.getCanonicalRelativePath(dir, subpath, true, separator);
	}

	public static String getCanonicalRelativePath(String dir, String subpath, boolean changeAllFileSystemSeparator, char separator) {
		String separatorString = String.valueOf(separator);

		String dealPath = changeAllFileSystemSeparator ?
				XFile.normalizePath(XFile.toFileSystemSeparator(XFile.toAbsolutePath(dir, separator), separator), XFile.toFileSystemSeparator(subpath, separator), separator):
				XFile.normalizePath(XFile.toAbsolutePath(dir, separator), subpath, separator);

		String newPath = XFile.dealRelativePath(dealPath, separator);

		if (newPath.startsWith(separatorString) == false) { // path ""
			newPath = new StringBuilder().append(separator).append(newPath).toString();
		}

		return newPath;
	}






	private static String toAbsolutePath(String path, char separator) {
		return new StringBuilder().append(separator).append(path).toString();
	}

	/**
	 * is absolute path
	 * 判断是否为绝对路径
	 *
	 * if @param separator for /
	 * "/a"				return true
	 * "/../../"		return true
	 * "./"				return false
	 * "../"			return false
	 */
	public static boolean isAbsolute(String path, char separator) {
		int n = path.length();
		if (n == 0) return false;
		char c0 = path.charAt(0);
//        char c1 = (n > 1) ? path.charAt(1) : 0;
		if (c0 == separator) {
//            if (c1 == separator) return true;  /* Absolute UNC pathname "\\\\foo" */
			return true;                   /* Drive-relative "\\foo" */
		}
//		if (((c0 >= 'a') && (c0 <= 'z')) || ((c0 >= 'A') && (c0 <= 'Z')) && (c1 == ':')) {
//            if ((n > 2) && (path.charAt(2) == separator))
//                return true;               /* Absolute local pathname "z:\\foo" */
//            return true;                   /* Directory-relative "z:foo" */
//        }
		return false;
	}












	public static String getRunningDir() { return getCanonicalRelativePath(new File(".").getAbsolutePath()); }

	/*
	 * 获得 文件名 带后缀
	 */
	public static String getName(String filePath) {
		return getName(filePath, File.separator);
	}

	/*
	 * 获得 文件名 带后缀
	 */
	public static String getName(String filePath, String pathSeparator) {
		if ((null != filePath)) {
			int dot = filePath.lastIndexOf(pathSeparator);
			if (dot > -1) {
				return filePath.substring(dot + pathSeparator.length(), filePath.length());
			} else {
				return filePath;
			}
		}
		return null;
	}

	/*
	 * 获得 文件目录
	 */
	public static String getParent(String filePath) {
		return getParent(filePath, File.separator);
	}

	/*
	 * 获得 文件目录
	 */
	public static String getParent(String filePath, String pathSeparator) {
		if ((null != filePath)) {
			int dot = filePath.lastIndexOf(pathSeparator);
			if (dot > -1) {
				return filePath.substring(0, dot + pathSeparator.length());
			}
		}
		return null;
	}

	/*
	 * 得到扩展名
	 */
	public static String getExtensionName(String filePath) {
		return getExtensionName(filePath, File.separator, XFile.FILE_EXTENSION_NAME_SEPARATOR);
	}

	public static final String getExtensionName(String filePath, String pathSeparator, String extensionNameSeparator) {
		if ((null != filePath) && (filePath.length() > 0)) {
			int dot = filePath.lastIndexOf(extensionNameSeparator);
			int splitCharDot = filePath.lastIndexOf(pathSeparator);
			if (splitCharDot > -1) {
				if (dot > -1 && dot > splitCharDot) {
					return filePath.substring(dot + extensionNameSeparator.length(), filePath.length());
				}
			} else {
				if (dot > -1) {
					return filePath.substring(dot + extensionNameSeparator.length(), filePath.length());
				}
			}
		}
		return null;
	}

	/*
	 * 获得 文件名 不带带后缀
	 */
	public static String getNameNoExtension(String fileCanonicalPath) {
		return getNameNoExtension(fileCanonicalPath, File.separator, XFile.FILE_EXTENSION_NAME_SEPARATOR);
	}

	/*
	 * 获得 文件名 不带带后缀
	 */
	public static final String getNameNoExtension(String filePath, String pathSeparator,
                                                  String extensionNameSeparator) {
		if ((null != filePath)) {
			int dot = filePath.lastIndexOf(pathSeparator);
			if (dot > -1) {
				int dot2 = filePath.lastIndexOf(extensionNameSeparator);
				if (dot2 > dot) { // dot2 > -1
					return filePath.substring(dot + pathSeparator.length(), dot2);
				} else {// dot2 <= -1
					return filePath.substring(dot + pathSeparator.length(), filePath.length());
				}
			} else {
				int dot2 = filePath.lastIndexOf(extensionNameSeparator);
				if (dot2 > -1) {
					return filePath.substring(0, dot2);
				} else {
					return filePath;
				}
			}
		}
		return null;
	}











	

	public static long indexOf(File file, byte b, long startIndex, long indexRange) throws IOException {
		XFileByteAt fba = new XFileByteAt(file);
		return XFileByteAt.indexOf(fba, b, startIndex, indexRange);
	}

	public static long indexOf(File file, byte[] b, long startIndex, long indexRange) throws IOException {
		XFileByteAt fba = new XFileByteAt(file);
		return XFileByteAt.indexOf(fba, b, startIndex, indexRange);
	}

	public static long lastIndexOf(File file, byte b, long startIndex, long indexRange) throws IOException {
		XFileByteAt fba = new XFileByteAt(file);
		return XFileByteAt.lastIndexOf(fba, b, startIndex, indexRange);
	}

	public static long lastIndexOf(File file, byte[] b, long startIndex, long indexRange) throws IOException {
		XFileByteAt fba = new XFileByteAt(file);
		return XFileByteAt.lastIndexOf(fba, b, startIndex, indexRange);
	}

	/**
	 * 
	 * 
	 * 
	 * ex
	 * 
	 * 
	 * 
	 * 
	 */

	/*
	 * Get Dir File List 获取文件夹文件列表 Parameter:filePath 路径, recursion 递增搜索, adddir
	 * 列表是否添加文件夹
	 */
	public static List<String> listRelativeFilePath(String filePath, boolean recursion, boolean adddir) {
		List<String> List = new ArrayList<String>();
		return listRelativeFilePath0(List, new File(filePath), recursion, adddir, new StringBuilder());
	}
	private static List<String> listRelativeFilePath0(List<String> list, File filePath, boolean recursion, boolean adddir,
													  StringBuilder baseDir) {
		File[] files = filePath.listFiles();
		if (null != files) {
			for (File file : files) {
				if (null == file)
					continue;
				String name = file.getName();
				if (file.isDirectory()) {
					if (recursion) {
						listRelativeFilePath0(list, file, true, adddir,
                            new StringBuilder(baseDir).append(name).append(File.separator));
					}
					if (adddir) {
						list.add(new StringBuilder(baseDir).append(name).append(File.separator).toString());
					}
				} else {
					list.add(new StringBuilder(baseDir).append(name).toString());
				}
			}
		}
		return list;
	}






	public static List<String> listFilesSort(File filePath, boolean adddir) {
		return listFilesSort(filePath.listFiles(), adddir, java.util.Locale.CHINA);
	}
	public static List<String> listFilesSort(File[] files, boolean adddir, Locale locale) {
		List<String> d = new ArrayList<String>();
		List<String> f = new ArrayList<String>();
		if (null != files) {
			for (File file : files) {
				if (null == file) {
					continue;
				}
				String name = file.getName();
				if (file.isDirectory()) {
					if (adddir) {
						d.add(new StringBuilder(name).append(File.separator).toString());
					}
				} else {
					f.add(name);
				}
			}
		}
		files = null;
		Collections.sort(d, Collator.getInstance(locale));
		Collections.sort(f, Collator.getInstance(locale));
		if (adddir) {
			d.addAll(f);
			f = null;
			return d;
		} else {
			d = null;
			return f;
		}
	}

}
