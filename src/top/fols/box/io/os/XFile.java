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

import top.fols.atri.io.Filex;
import top.fols.atri.io.Streams;
import top.fols.atri.lang.Finals;
import top.fols.box.annotation.BaseAnnotations;
import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.io.base.XOutputStreamFixedLength;
import top.fols.box.lang.XUnitConversion;
import top.fols.box.util.XByteEncodeDetect;

@Deprecated
public class XFile implements Closeable, Serializable {
	private static final long serialVersionUID = 1L;







	private String path;

	private transient File file0;
	private transient XRandomAccessFileOutputStream raf0;

	public XFile(CharSequence path) {
		this.path = Filex.normalizePath(path, File.separatorChar);
	}
	public XFile(CharSequence parent, CharSequence subfilepath) {
        this.path = Filex.normalizePath(parent, subfilepath, File.separatorChar);
	}
	public XFile(File file) {
		this(file.getPath());
	}
	public XFile(File file, String subfilepath) {
		this(file.getPath(), subfilepath);
	}





	public String getExtensionName() {
		return Filex.getExtensionName(path);
	}

	public String getName() {
		return Filex.getName(path);
	}

	public String getNameNoExtension() {
		return Filex.getNameNoExtension(path);
	}

	public String getParent() {
		return Filex.getParent(path);
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
		Streams.copyFixedLength(in, this.openStream(), len);
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
			return Finals.EMPTY_BYTE_ARRAY;
		}
		RandomAccessFile randomFile = null;
		byte[] fileBytes;
		long filelength = file.length();
		try {
			randomFile = new RandomAccessFile(file, Finals.FileOptMode.r());
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
			bytes = Finals.EMPTY_BYTE_ARRAY;
		}
		if (!file.exists()) {
			if (!file.createNewFile()) {
				throw new IOException("canot new file:" + file);
			}
		}
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(file, Finals.FileOptMode.rws());
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

	@BaseAnnotations("1B = 8bit")
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
