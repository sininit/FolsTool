package top.fols.box.io.os;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
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
import top.fols.box.io.abstracts.XAbstractRandomAccessOutputStream;
import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.io.base.XOutputStreamFixedLength;
import top.fols.box.lang.XUnitConversion;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XByteEncodeDetect;
import top.fols.box.util.XDoubleLinked;

public class XFile extends XAbstractRandomAccessOutputStream implements Closeable {

	public static final String FILE_EXTENSION_NAME_SEPARATOR = ".";

	private String filePath;
	private File file;
	private XRandomAccessFileOutputStream raf;

	public XFile(String file) {
		this(new File(file));
	}

	public XFile(File file) {
		this.file = file;

		try {
			this.filePath = file.getCanonicalPath();
		} catch (IOException e) {
			this.filePath = XFile.getCanonicalPath(file.getAbsolutePath(), false, File.separatorChar);
		}
	}

	private void openStream() throws IOException {
		if (null == this.raf) {
			this.raf = new XRandomAccessFileOutputStream(filePath);
		}
	}

	public File getFile() {
		return this.file;
	}

	public String getExtensionName() {
		return XFile.getExtensionName(filePath);
	}

	public String getName() {
		return XFile.getName(filePath);
	}

	public String getNameNoExtension() {
		return XFile.getNameNoExtension(filePath);
	}

	public String getDir() {
		return XFile.getDir(filePath);
	}

	public String getPath() {
		return this.filePath;
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
		this.write(bytes, off, len);
		return this;
	}

	public XFile append(InputStream in, long len) throws IOException {
		if (len < 0) {
			return this;
		}
		XStream.copyFixedLength(in, this, len);
		return this;
	}

	//
	@Override
	public void write(int p1) throws FileNotFoundException, IOException {
		this.openStream();
		this.raf.write(p1);
	}

	@Override
	public void write(byte[] bytes, int off, int len) throws IOException {
		this.openStream();
		this.raf.write(bytes, off, len);
	}

	//
	public XFile empty() throws IOException {
		this.openStream();
		this.raf.empty();
		return this;
	}

	@Override
	public long getIndex() {
		return this.raf.getIndex();
	}

	@Override
	public void seekIndex(long index) throws IOException {
		this.raf.seekIndex(index);
	}

	@Override
	public void setLength(long newLength) throws IOException {
		this.raf.setLength(newLength);
	}

	@Override
	public long length() {
		return this.file.length();
	}

	@Override
	public void flush() throws IOException {
		super.flush();
	}

	@Override
	public void close() {
		// TODO: Implement this method
		try {
			this.raf.close();
		} catch (IOException e) {
			this.raf = null;
		}
	}

	public String detectEncoding() throws IOException, RuntimeException {
		return XByteEncodeDetect.getJavaEncode(this.getFile());
	}

	@Override
	public String toString() throws RuntimeException {
		try {
			byte[] bs = XFile.getBytes(this.getFile());
			if (null == bs) {
				return null;
			}
			return new String(bs);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String toString(String encoding) throws IOException {
		return new String(XFile.getBytes(this.getFile()), encoding);
	}

	public String toString(Charset encoding) throws IOException {
		return new String(XFile.getBytes(this.getFile()), encoding);
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

	public static byte[] getBytes(File file) throws IOException, FileNotFoundException {
		if (!file.exists()) {
			throw new FileNotFoundException(file.toString());
		}
		InputStream in = new XRandomAccessFileInputStream(file);
		byte[] b = XStream.InputStreamTool.toByteArray(in);
		in.close();
		return b;
	}

	public static byte[] getBytes(File file, long off, int len) throws IOException, FileNotFoundException {
		if (!file.exists()) {
			throw new FileNotFoundException(file.toString());
		}
		InputStream in = XFile.getRangeInputStream(file, off, len);
		byte[] b = XStream.InputStreamTool.toByteArray(in);
		in.close();
		return b;
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

	/*
	 * will {/,\} cast to param@separator
	 */
	public static String formatPath(String path, char separator) {
		StringBuilder fPath = new StringBuilder();
		boolean lastSeparator = false;
		int length = path.length();
		for (int i = 0; i < length; i++) {
			char ch = path.charAt(i);
			if (ch == '\\' || ch == '/') {
				ch = separator;
			}
			if (ch == separator) {
				if (!lastSeparator) {
					fPath.append(ch);
					lastSeparator = true;
				}
			} else {
				fPath.append(ch);
				lastSeparator = false;
			}
		}
		return fPath.toString();
	}

	/*
	 * deal relative path ../ ./
	 */
	public static String dealRelativePath(String path, char separator) {
		String separatorString = String.valueOf(separator);
		int length = path.length();
		if (length == 0) {
			return separatorString;
		}

		XDoubleLinked.VarLinked<String> p = new XDoubleLinked.VarLinked<>(null);
		XDoubleLinked bottom = p;
		XDoubleLinked top = p;

		int lastIndex = 0;
		for (int i = 0; i <= length; i++) {
			if (i == length) {
				if (i - lastIndex > 0) {
					String t = path.substring(lastIndex, i);

					XDoubleLinked.VarLinked<String> p1 = new XDoubleLinked.VarLinked<>(t);
					top.addNext(top = p1);
				}
			} else {
				char ch = path.charAt(i);
				if (ch == separator) {
					if (i - lastIndex > 0) {
						String t = path.substring(lastIndex, i);

						XDoubleLinked.VarLinked<String> p1 = new XDoubleLinked.VarLinked<>(t);
						top.addNext(top = p1);
					}
					XDoubleLinked.VarLinked<String> p1 = new XDoubleLinked.VarLinked<>(separatorString);
					top.addNext(top = p1);

					lastIndex = i + 1;
				}
			}
		}

		// "/a/b/ff/3/3/7/" >> {/,a,b,ff,3,3,7}

		/*
		 * ../a/b/c ./a/b/c /../a/b/c /a/../c
		 * 
		 * /a/b/c/.. /a/b/c/. /a/b/c
		 * 
		 * ...
		 * 
		 */
		XDoubleLinked now = bottom.getNext();

		while (true) {
			if (XDoubleLinked.VarLinked.equals(".", now)) {
				// 处理 ./

				XDoubleLinked next = now.getNext();
				// boolean isBottom = now.getLast() == bottom;
				bottom.remove(now);

				if (XDoubleLinked.VarLinked.equals(separatorString, next)) {
					bottom.remove(next);
				}
			} else if (XDoubleLinked.VarLinked.equals("..", now)) {
				// 处理 ../
				XDoubleLinked last1 = now.getLast();
				last1 = last1 == bottom ? null : last1;
				XDoubleLinked last2 = null == last1 ? null : last1.getLast();
				last2 = last2 == bottom ? null : last2;
				if (null != last1) {
					bottom.remove(last1);
				}
				if (null != last2) {
					bottom.remove(last2);
				}

				XDoubleLinked next = now.getNext();
				// boolean isBottom = now.getLast() == bottom;
				bottom.remove(now);

				if (XDoubleLinked.VarLinked.equals(separatorString, next)) {
					bottom.remove(next);
				}
			}

			if (null == (now = now.getNext())) {
				break;
			}
		}

		XDoubleLinked absbottom = bottom.getNext();
		top.remove(bottom);

		if (null == absbottom) {
			return "";
		} else {
			StringBuilder right = new StringBuilder();
			XDoubleLinked x = absbottom;
			do {
				right.append(x);
			} while (null != (x = x.getNext()));
			return right.toString();
		}
	}

	public static String getCanonicalPath(String path) {
		return getCanonicalPath(path, false, File.separatorChar);
	}

	public static String getCanonicalPath(String path, char separator) {
		return getCanonicalPath(path, false, separator);
	}

	/**
	 * 
	 * 
	 * @param path
	 * @param noFormatPath if ture noFormatPath, flase format @param path file
	 *                     separator to @param separator
	 * @param separator
	 * @return
	 */
	public static String getCanonicalPath(String path, boolean noFormatPath, char separator) {
		String separatorString = String.valueOf(separator);
		String dealPath = !noFormatPath ? formatPath(path, separator) : path;
		String newPath = dealRelativePath(dealPath, separator);
		if (!newPath.startsWith(separatorString)) {
			return new StringBuilder(separatorString).append(newPath).toString();
		} else {
			return newPath;
		}
	}

	/*
	 * no deal dir relative path
	 */
	public static String getCanonicalPath(String dir, String path) {
		return getCanonicalPath(dir, path, false, File.separatorChar);
	}

	public static String getCanonicalPath(String dir, String path, char separator) {
		return getCanonicalPath(dir, path, false, separator);
	}

	public static String getCanonicalPath(String dir, String path, boolean noFormatPath, char separator) {
		StringBuilder newPath = new StringBuilder();
		newPath.append(getCanonicalPath(dir, noFormatPath, separator));
		newPath.append(getCanonicalPath(path, noFormatPath, separator));
		if (!noFormatPath) {
			return formatPath(newPath.toString(), separator);
		} else {
			return newPath.toString();
		}
	}

	public static String getRunningDir() {
		return getCanonicalPath(new File(".").getAbsolutePath());
	}

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
	public static String getDir(String filePath) {
		return getDir(filePath, File.separator);
	}

	/*
	 * 获得 文件目录
	 */
	public static String getDir(String filePath, String pathSeparator) {
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

	public static File setFilePermission(File file, Boolean readable, Boolean writeable, Boolean executeable,
                                         Boolean ownerOnly) {
		if (null == ownerOnly) {
			if (null != readable) {
				file.setReadable(readable);
			}
			if (null != writeable) {
				file.setWritable(writeable);
			}
			if (null != executeable) {
				file.setExecutable(executeable);
			}
		} else {
			if (null != readable) {
				file.setReadable(readable, ownerOnly);
			}
			if (null != writeable) {
				file.setWritable(writeable, ownerOnly);
			}
			if (null != executeable) {
				file.setExecutable(executeable, ownerOnly);
			}
		}
		return file;
	}

	@SuppressWarnings("unchecked")
	public static File setFilePermissions(File file, Boolean readable, Boolean writeable, Boolean executeable,
                                          Boolean ownerOnly) {
		file = file.getAbsoluteFile();

		XDoubleLinked.VarLinked<File> files = new XDoubleLinked.VarLinked<>(null);
		File pn = file;
		while (null != (pn = pn.getParentFile())) {
			files.addNext(new XDoubleLinked.VarLinked<>(pn));
		}
		XDoubleLinked.VarLinked<File> now = files;
		while (null != now && null != (now = (XDoubleLinked.VarLinked<File>) now.getNext())) {
			File f = now.content();
			setFilePermission(f, readable, writeable, executeable, ownerOnly);
		}
		setFilePermission(file, readable, writeable, executeable, ownerOnly);
		return file;
	}

	public static boolean mkdirs(File file) {
		return XFile.mkdirs(file, true);
	}

	@SuppressWarnings("unchecked")
	public static boolean mkdirs(File file, Boolean ownerOnly) {
		file = file.getAbsoluteFile();
		if (file.exists()) {
			XFile.setFilePermissions(file, true, true, true, ownerOnly);
			return true;
		} else {
			boolean b = false;
			XDoubleLinked.VarLinked<File> files = new XDoubleLinked.VarLinked<>(null);
			files.addNext(new XDoubleLinked.VarLinked<>(file));

			File pn = file;
			while (null != (pn = pn.getParentFile())) {
				files.addNext(new XDoubleLinked.VarLinked<>(pn));
			}
			XDoubleLinked.VarLinked<File> now = files;
			while (null != now && null != (now = (XDoubleLinked.VarLinked<File>) now.getNext())) {
				File dir = now.content();
				if (!dir.exists()) {
					boolean result = dir.mkdir();
					b = b & result;
				}
				XFile.setFilePermission(dir, true, true, true, ownerOnly);
			}
			return b;
		}
	}

	public static File openFile(File file) {
		return XFile.openFile(file, true);
	}

	public static File openFile(File file, Boolean ownerOnly) {
		return XFile.setFilePermissions(file, true, true, true, ownerOnly);
	}

	public static boolean createNewFile(File file) throws IOException {
		return XFile.createNewFile(file, true);
	}

	public static boolean createNewFile(File file, Boolean ownerOnly) throws IOException {
		file = file.getAbsoluteFile();
		try {
			boolean b0 = false;
			try {
				b0 = file.createNewFile();
			} catch (IOException e) {
				b0 = false;
			}
			if (b0) {
				return true;
			} else {
				File parent = file.getParentFile();
				if (null != parent) {
					XFile.mkdirs(parent, ownerOnly);
					return new File(parent, file.getName()).createNewFile();
				} else {
					return file.createNewFile();
				}
			}
		} finally {
			XFile.setFilePermission(file, true, true, true, ownerOnly);
		}
	}

	public static boolean delete(File file) {
		return XFile.delete(file, true);
	}

	public static boolean delete(File file, Boolean ownerOnly) {
		boolean b0 = file.delete();
		if (b0) {
			return true;
		} else {
			XFile.setFilePermissions(file, true, true, true, ownerOnly);
			if (!file.exists()) {
				return false;
			} else {
				if (file.isFile()) {
					boolean result = file.delete();
					return result;
				} else {
					boolean b1 = file.delete();
					if (b1) {
						return true;
					}
					boolean b = delete0(file, ownerOnly) & file.delete();
					return b;
				}
			}
		}
	}

	private static boolean delete0(File file, Boolean ownerOnly) {
		boolean b = true;
		File[] files = file.listFiles();
		if (null != files) {
			for (File f : files) {
				XFile.setFilePermission(f, true, true, true, ownerOnly);
				if (f.isFile()) {
					b = b & f.delete();
				} else if (f.isDirectory()) {
					b = b & delete0(f, ownerOnly) & f.delete();
				} else {
					b = false;
				}
			}
		}
		return b;
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
	public static List<String> getFileList(String filePath, boolean recursion, boolean adddir) {
		List<String> List = new ArrayList<String>();
		return getFilesList(List, new File(filePath), recursion, adddir, new StringBuilder());
	}

	private static List<String> getFilesList(List<String> list, File filePath, boolean recursion, boolean adddir,
                                             StringBuilder baseDir) {
		File[] files = filePath.listFiles();
		if (null != files) {
			for (File file : files) {
				if (null == file)
					continue;
				String name = file.getName();
				if (file.isDirectory()) {
					if (recursion) {
						getFilesList(list, file, true, adddir,
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
				if (null == file)
					continue;
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
