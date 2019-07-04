package top.fols.box.io.os;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.XStream;
import top.fols.box.io.base.XCharArrayWriter;
import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.io.base.XOutputStreamFixedLength;
import top.fols.box.io.interfaces.XInterfaceRandomAccessInputStream;
import top.fols.box.io.interfaces.XInterfaceRandomAccessOutputStream;
import top.fols.box.lang.XUnitConversion;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XArrayPieceIndexManager;
import top.fols.box.util.interfaces.sequence.byteutil.XInterfaceSequenceBigByteIO;

public class XFile implements Closeable {

	private final String fileCanonicalPath;
	private File file;
	private RandomAccessFile r;
	public XFile(String file) {
		this(new File(file));
	}
	public XFile(File file) {
		this.file = file;
		try {
			this.fileCanonicalPath = file.getCanonicalPath();
		} catch (IOException e) {
			this.fileCanonicalPath = file.getAbsolutePath();
		}
	}
	private void init()throws IOException {
		if (null == r)
			r = new RandomAccessFile(fileCanonicalPath, XStaticFixedValue.FileOptMode.rw());
	}
	public File getFile() {
		return this.file;
	}
	
	public String getExtensionName() { 
		return XFile.getExtensionName(fileCanonicalPath);
	}
	public String getName() {
		return XFile.getName(fileCanonicalPath);
	}
	public String getNameNoExtension() {
		return XFile.getNameNoExtension(fileCanonicalPath);
	}
	
	
	
	public InputStream getRangeInputStream(long off, long len) throws IOException {
		XRandomAccessFileInputStream in = new XRandomAccessFileInputStream(fileCanonicalPath);
		in.seekIndex(off);
		return new XInputStreamFixedLength(in, len);
	}
	public OutputStream getRangeOutputStream(long off, long len) throws IOException {
		XRandomAccessFileOutputStream out = new XRandomAccessFileOutputStream(fileCanonicalPath);
		out.seekIndex(off);
		return new XOutputStreamFixedLength(out, len);
	}
	
	public String getPath() {
		return fileCanonicalPath;
	}

	public XFile append(String bytes) throws IOException {
		byte[] bytes2 = bytes.getBytes();
		return append(bytes2, 0, bytes2.length);
	}
	public XFile append(String bytes, String encoding) throws IOException {
		byte[] bytes2 = bytes.getBytes(encoding);
		return append(bytes2, 0, bytes2.length);
	}
	public XFile append(byte[] bytes) throws  IOException {
		return append(bytes, 0, bytes.length);
	}
	public XFile append(byte[] bytes, int off, int len) throws IOException {
		init();
		if (len < 0)
			return this;
		r.seek(r.length());
		r.write(bytes, off, len);
		return this;
	}
	public XFile append(InputStream in, long len) throws IOException {
		init();
		if (len < 0)
			return this;
		r.seek(r.length());
		XStream.copyFixedLength(in, new XRandomAccessFileOutputStream(r), len);
		return this;
	}
	public XFile append(int p1) throws FileNotFoundException, IOException {
		init();
		r.seek(r.length());
		r.write(p1);
		return this;
	}





	public XFile empty() throws IOException {
		init();
		r.setLength(0);
		return this;
	}
	public long length() {
		return file.length();
	}
	
	public String toString() {
		try {
			byte[] bs = getBytes();
			if (null == bs)
				return null;
			return new String(bs);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public String toString(String encoding) throws IOException {
		return new String(getBytes(), encoding);
	}
	
	public byte[] getBytes() throws IOException {
		if (!file.exists())
			return null;
		InputStream in = new XRandomAccessFileInputStream(fileCanonicalPath);
		byte[] b = XStream.inputstream.toByteArray(in);
		in.close();
		return b;
	}
	public byte[] getBytes(long off, int len) throws IOException {
		if (!file.exists())
			return null;
		InputStream in = getRangeInputStream(off, len);
		byte[] b = XStream.inputstream.toByteArray(in);
		in.close();
		return b;
	}
	public XFile.Input toFileEditReadOption() throws FileNotFoundException, IOException {
		return new XFile.Input(fileCanonicalPath);
	}
	public XFile.Output toFileEditWriteOption() throws FileNotFoundException, IOException {
		return new XFile.Output(fileCanonicalPath);
	}
	
	public InputStream toInputStream() throws IOException {
		return new XRandomAccessFileInputStream(fileCanonicalPath);
	}
	public OutputStream toOutputStream() throws IOException {
		return new XRandomAccessFileOutputStream(fileCanonicalPath);
	}
	
	
	
	@Override
	public void close() {
		// TODO: Implement this method
		try {
			this.r.close();
		} catch (Exception e) {
			this.r = null;
		}
	}
	
	
	public XFile copyTo(XFile f) throws IOException {
		return copyTo(f.fileCanonicalPath);
	}
	public XFile copyTo(File f) throws IOException {
		return copyTo(f.getPath());
	}
	public XFile copyTo(String f) throws IOException {
		return copyTo(f, true);
	}
	public XFile copyTo(String f, boolean check) throws IOException {
		File originFile = new File(fileCanonicalPath);
		File copyToFile = new File(f);
		if (check && copyToFile.exists())
			throw new IOException("file exist");
		
		XRandomAccessFileOutputStream out = new XRandomAccessFileOutputStream(f);
		out.setLength(0);
		out.seekIndex(0);

		XRandomAccessFileInputStream in = new XRandomAccessFileInputStream(originFile);
		XStream.copy(in, out);
		in.close();
		out.close();
		return this;
	}





	
	
	
	public static void openFile(File file) throws IOException {
		if (null == file)
			throw new NullPointerException("file for null");
		try {
			if (!file.exists())
				if (!file.createNewFile())
					throw new IOException("canot new file:" + file);
			if (!file.canRead())
				throw new IOException("canot read file:" + file);
			if (!file.canWrite())
				throw new IOException("canot write file:" + file);
		} catch (IOException e) {
			file = null;
			throw e;
		}
	}
	public static byte[] readFile(File file) {
		if (null == file)
			throw new NullPointerException("file for null");
		if (!file.exists())
			return XStaticFixedValue.nullbyteArray;
		RandomAccessFile randomFile = null;
		byte[] fileBytes;
		long length;
		try { 
			randomFile = new RandomAccessFile(file, XStaticFixedValue.FileOptMode.r());
			length = randomFile.length();
			if (length > Integer.MAX_VALUE)
				throw new OutOfMemoryError("overflow memory file length > " + Integer.MAX_VALUE);
			fileBytes = new byte[(int)length];
			randomFile.read(fileBytes);
			return fileBytes;
		} catch (Exception e) {
			return XStaticFixedValue.nullbyteArray;
		} finally {
			try {
				if (null != randomFile)
					randomFile.close();
			} catch (Exception e) {
				randomFile = null;
			}
		}
	}
	public static void reCreateFile(File file) throws IOException {
		if (null == file)
			throw new NullPointerException("file for null");
		if (!file.exists()) {
			openFile(file);
		} else {
			file.delete();
			openFile(file);
		}

	}
	public static boolean saveFile(File file, byte[] bytes) throws IOException {
		if (null == file)
			throw new NullPointerException("file for null");
		openFile(file);
		if (null == bytes || bytes.length == 0) {
			reCreateFile(file);
			return true;
		}
		RandomAccessFile randomFile = null;
		try { 
			randomFile = new RandomAccessFile(file, XStaticFixedValue.FileOptMode.rws());
			randomFile.write(bytes);
			randomFile.setLength(bytes.length);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (null != randomFile)
					randomFile.close();
			} catch (Exception e) {
				randomFile = null;
			}
		}
	}
	public static void writeNullDataFile(File file, long length) throws IOException {
		writeNullDataFile(file, 0, length);
	}
	public static void writeNullDataFile(File file, long start, long length) throws IOException {
		XRandomAccessFileOutputStream out = new XRandomAccessFileOutputStream(file);
		writeNullDataFile0(out, start, length);
		out.close();
	}
	private static void writeNullDataFile0(XRandomAccessFileOutputStream out, long start, long length) throws IOException {
		out.setLength(start + length);
		out.seekIndex(start);
		int bufs = XStream.defaultStreamByteArrBuffSize;
		if (length <= bufs) {
			byte[] bf = new byte[(int)length];
			out.write(bf);
			bf = null;
		} else {
			byte[] bs = new byte[bufs];
			long needWriter = length;
			while (true) {
				if (needWriter < bufs) {
					byte[] bf = new byte[(int)needWriter];
					out.write(bf);
					bf = null;
					needWriter = 0;
					break;
				} else {
					out.write(bs);
					needWriter -= bufs;
					if (needWriter <= 0)
						break;
				}
			}
		}
	}







	@XAnnotations("1B = 8bit")
	public static final String[] fileUnit = new String[]{
		"B","KB","MB",
		"GB","TB","PB",
		"EB","ZB","YB",
		"BB","NB","DB",
		"CB"
	};
	public static final BigDecimal[] fileUnitSize = new BigDecimal[]{
		new BigDecimal(1024D),new BigDecimal(1024D),new BigDecimal(1024D),
		new BigDecimal(1024D),new BigDecimal(1024D),new BigDecimal(1024D),
		new BigDecimal(1024D),new BigDecimal(1024D),new BigDecimal(1024D),
		new BigDecimal(1024D),new BigDecimal(1024D),new BigDecimal(1024D),
		new BigDecimal(1024D)
	};
	public static String fileUnitFormat(String bytelength) {
		return fileUnitFormat(bytelength, true);
	}
	public static String fileUnitFormat(String bytelength, boolean round) {
		return XUnitConversion.unitCalc(bytelength, fileUnit, 0, fileUnitSize, round, 2);
	}
	public static String fileUnitFormat(double size) {
		return fileUnitFormat(size, true, 2);
	}
	public static String fileUnitFormat(double size, boolean round, int scale) {
		return XUnitConversion.unitCalc(size, fileUnit, 0, 1024, round, scale);
	}




	/*
	 * 文件获取绝对地址
	 * getFormatPath("//XSt/*?:]/tt/////.//./././a/b/v//x//a/v**v//n///...//../../();").equals(new File("//XSt/*?:]/tt/////.//./././a/b/v//x//a/v**v//n///...//../../();").getCanonicalPath()); >> true
	 * getFormatPath("//XSt/*?:]/tt/////.//./././a/b/v//x//a/v**v//n///...//../../();"); >> "/XSt/*?:]/tt/a/b/v/x/a/v**v/();"
	 */
	public static String getFormatPath(String dir, String file) {
		return getFormatPath(dir, file, File.separatorChar);
	}
	public static String getFormatPath(String dir, String file, char separatorChar) {
		return getFormatPath((dir == null ?"": dir) + separatorChar + (file == null ?"": file));
	}



	public static String getFormatPath(String orginPath) {
		return getFormatPath(orginPath, File.separatorChar);
	}
	public static String getFormatPath(String orginPath, char separatorChar) {
		if (orginPath.length() == 0)
			return Character.toString(separatorChar);
		XCharArrayWriter buf  = new XCharArrayWriter();

		StringBuilder newPath;
		newPath = new StringBuilder();
		newPath.append(separatorChar);
		int pathsize = orginPath.length();
		boolean lastSplitChar = true;
		int i = 0;
		if (orginPath.charAt(0) == separatorChar)
			i = 1;
		for (;i < pathsize;i++) {
			if (orginPath.charAt(i) == separatorChar) {
				if (!lastSplitChar)
					newPath.append(separatorChar);
				lastSplitChar = true;
				continue;
			} else {
				lastSplitChar = false;
				newPath.append(orginPath.charAt(i));
			}
		}
		int length = newPath.length();
		int absInd = 0;
		for (int ind = 0;ind < length;) {
			char c = newPath.charAt(ind);
			if (c == separatorChar) {
				if (ind + 2 < length && 
					newPath.charAt(ind + 1) == '.' &&
					newPath.charAt(ind + 2) == separatorChar) {
					//	/./
					ind += 2;
					continue;
				} else if (ind + 3 < length && 
						   newPath.charAt(ind + 1) == '.'  && 
						   newPath.charAt(ind + 2) == '.' && 
						   newPath.charAt(ind + 3) == separatorChar) {
					//	/../
					int last = buf.lastIndexOfBuff(separatorChar, absInd - 1, 0);
					//System.out.println(buf.toString() + "(" + absInd + ")" + "-" + "(" + last + ")");
					if (last >= 0) {
						buf.seekIndex(last);
						absInd = last;
					} else {
						buf.seekIndex(0);
						absInd = 0;
					}
					ind += 3;//
					continue;
				} 
			} 
			buf.write(c);
			ind += 1;
			absInd += 1;
		}
		int size = buf.size();
		if (size >= 3 &&
			buf.getBuff()[size - 3] == separatorChar  &&
			buf.getBuff()[size - 2] == '.' &&
			buf.getBuff()[size - 1] == '.') {
			int last = buf.lastIndexOfBuff(separatorChar, size - 3 - 1, 0);
			if (last > -1) {
				buf.setBuffSize(last + 1);
			} else {
				buf.setBuffSize(0);
				buf.write(separatorChar);
			}
		} else if (size >= 2 &&
				   buf.getBuff()[size - 2] == separatorChar  &&
				   buf.getBuff()[size - 1] == '.') {
			buf.setBuffSize(buf.size() - 1);
		} 
		String result;
		if (buf.size() >= 1 && buf.getBuff()[0] != separatorChar) {
			result = new StringBuilder(separatorChar)
				.append(buf.getBuff(), 0, buf.size())
				.toString();
		} else {
			result = new String(buf.getBuff(), 0, buf.size());
		}
		buf.releaseBuffer();
		newPath = null;
		return result;
	}





	/*
	 获得 文件名 带后缀
	 */
	public static String getName(String filePath, String pathSeparator) { 
        if ((null != filePath) && (filePath.length() > 0)) { 
            int dot = filePath.lastIndexOf(pathSeparator);
			if (dot > -1) { 
				return filePath.substring(dot + pathSeparator.length(), filePath.length()); 
            }
        } 
        return null; 
    } 
	public static String getName(String filePath) { 
		return getName(filePath, File.separator);
	}


	/*
	 得到扩展名
	 */
	public static String getExtensionName(String filePath) { 
		return getExtensionName(filePath, File.separator, ".");
	}
	public final static String getExtensionName(String filePath, String pathSeparator, String extensionNameSeparator) { 
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
	public final static String getNameNoExtension(String filePath, String pathSeparator, String extensionNameSeparator) { 
		if ((null != filePath) && (filePath.length() > 0)) { 
            int dot = filePath.lastIndexOf(pathSeparator);
			if (dot > -1) { 
				int dot2 = filePath.lastIndexOf(extensionNameSeparator);
				if (dot2 > dot) {
					if (dot2 > -1) {
						return filePath.substring(dot + pathSeparator.length(), dot2);
					} else {
						return filePath.substring(dot + pathSeparator.length(), filePath.length());
					}
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
	/*
	 获得 文件名 不带带后缀
	 */
	public static String getNameNoExtension(String fileCanonicalPath) {
		return getNameNoExtension(fileCanonicalPath, File.separator, ".");
	}






	/*
	 Get Dir File List
	 获取文件夹文件列表
	 Parameter:filePath 路径,  recursion 递增搜索, adddir 列表是否添加文件夹
	 */
	public static List<String> getFileList(String filePath, boolean recursion, boolean adddir) {
		List<String> List = new ArrayList<String>();
		return getFilesList(List, new File(filePath) , recursion, adddir, new StringBuilder());
    }
	private static List<String> getFilesList(List<String> list, File filePath, boolean recursion, boolean adddir, StringBuilder baseDir) {
		File[] files = filePath.listFiles();
		if (null != files)
			for (File file : files) {
				if (null == file)
					continue;
				String name = file.getName();
				if (file.isDirectory()) {
					if (recursion) {
						getFilesList(list, file, true, adddir, new StringBuilder(baseDir).append(name).append(File.separator));
					}	
					if (adddir) {
						list.add(new StringBuilder(baseDir).append(name).append(File.separator).toString());
					}
				} else {
					list.add(new StringBuilder(baseDir).append(name).toString()) ;
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
		if (null != files)
			for (File file : files) {
				if (null == file)
					continue;
				String name = file.getName();
				if (file.isDirectory()) {
					if (adddir) {
						d.add(new StringBuilder(name).append(File.separator).toString());
					}
				} else {
					f.add(name) ;
				}
			}
		files = null;
		Collections.sort(d, Collator.getInstance(locale));
		Collections.sort(f, Collator.getInstance(locale));
		if (adddir) {
			d.addAll(f);
			f.clear();
			return d;
		} else {
			d.clear();
			return f;
		}
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static class Input implements Closeable, XInterfaceSequenceBigByteIO {
		public static Input wrap(File file) throws IOException {
			return new Input(file);
		}
		private int pieceBufSize;//BufferSize
		private long nowpiece = -1;//当前块
		private byte[] nowpieceData;
		private XArrayPieceIndexManager xpm;
		private XInterfaceRandomAccessInputStream randomFileStream;
		private long length;

		private Input() {}
		public Input(XInterfaceRandomAccessInputStream randomFileStream) throws IOException {
			this(randomFileStream, XStream.defaultStreamByteArrBuffSize);
		}
		public Input(XInterfaceRandomAccessInputStream randomFileStream, int PieceBufSize) throws IOException {
			if (PieceBufSize < 1)
				throw new RuntimeException("pieceSize=" + PieceBufSize + ", min=1");
			this.randomFileStream = randomFileStream;
			this.pieceBufSize = PieceBufSize;
			this.nowpieceData = new byte[PieceBufSize];
			this.xpm = new XArrayPieceIndexManager(randomFileStream.length(), PieceBufSize);
			this.length = xpm.length();
		}
		public Input(File file, int PieceBufSize) throws FileNotFoundException, IOException {
			this(new XRandomAccessFileInputStream(file), PieceBufSize);
		}
		public Input(File file) throws FileNotFoundException, IOException {
			this(new XRandomAccessFileInputStream(file), XStream.defaultStreamByteArrBuffSize);
		}
		public Input(String file) throws FileNotFoundException, IOException {
			this(new File(file));
		}

		public long getPieceBuffSize() {
			return xpm.getPieceBufSize();
		}
		public long getIndexPiece(long newPiece) {
			return xpm.getIndexPiece(newPiece);
		}
		public long getPieceLength(long newPiece) {
			return xpm.getPieceLength(newPiece);
		}
		public long getPieceIndexEnd(long newPiece) {
			return xpm.getPieceIndexEnd(newPiece);
		}
		public long getPieceIndexStart(long newPiece) {
			return xpm.getPieceIndexStart(newPiece);
		}
		public long getPieceCount() {
			return xpm.getPieceNumber();
		}
		public long length() {
			return length;
		}


		public byte[] getPieceBytes(long newPiece) throws IOException {
			long off = xpm.getPieceIndexStart(newPiece);
			int len = (int)xpm.getPieceLength(newPiece);
			return getBytes(off, len);
		}
		public void seekPiece(long newPiece) throws IOException {
			grow(xpm.getPieceIndexStart(newPiece));
		}
		public byte[] getNowPieceBytes() {
			return nowpieceData;
		}
		public int getNowPieceLength() {
			return this.nowpieceData.length;
		}
		public long getNowPiece() {
			return this.nowpiece;
		}
		private long nowPieceStart = -1;
		private long nowPieceEnd = -1;
		private void grow(long index) throws IOException {
			long newPiece = xpm.getIndexPiece(index);
			if (newPiece != nowpiece) {
				long start = xpm.getPieceIndexStart(newPiece);
				long end = xpm.getPieceIndexEnd(newPiece);
				randomFileStream.seekIndex(start);
				int len = (int)xpm.getPieceLength(newPiece);
				if (nowpieceData.length != len)
					nowpieceData = new byte[len];
				randomFileStream.read(nowpieceData, 0, nowpieceData.length);
				this.nowpiece = newPiece;

				nowPieceStart = start;
				nowPieceEnd = end;
			}
		}


//		if (index < 0 || index >= length)
//			throw new ArrayIndexOutOfBoundsException(String.format("index=%s filelength=%s", index, length));
		public byte byteAt(long index) throws IOException {
			if (!(nowPieceStart >= index && nowPieceEnd <= index))
				grow(index);
			return nowpieceData[(int)(index % pieceBufSize)];
		}
		public byte[] getBytes(long index, int len) throws IOException {
			if (index + len > length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s len=%s filelength=%s", index, len, length));
			byte[] bytes;
			randomFileStream.seekIndex(index);
			randomFileStream.read(bytes = new byte[len]);
			return bytes;
		}



		public long indexOf(byte b, long startIndex, long indexRange) throws IOException {
			startIndex = startIndex < 0 ? 0: startIndex;
			indexRange = indexRange < length ? indexRange : length;
			for (long i = startIndex; i < indexRange; i++)
				if (byteAt(i) == b)
					return i;
			return -1;
		}
		public long indexOf(byte[] b, long startIndex, long indexRange) throws IOException {
			if (length == 0 || startIndex > indexRange || null == b || b.length > length || b.length == 0 || indexRange - startIndex + 1 < b.length)
				return -1;
			startIndex = startIndex < 0 ? 0: startIndex;
			indexRange = indexRange < length ? indexRange : length;
			int i2;
			for (long i = startIndex; i < indexRange; i++) {
				if (byteAt(i) == b[0]) {
					if (indexRange - i < b.length)
						break;
					for (i2 = 1; i2 < b.length; i2++)
						if (byteAt(i + i2) != b[i2])
							break;
					if (i2 == b.length)
						return i;
				}
			}
			return -1;
		}
		public long lastIndexOf(byte b, long startIndex, long indexRange) throws IOException {
			if (length == 0 || indexRange > startIndex)
				return -1;
			indexRange = indexRange < 0 ? 0: indexRange;
			if (startIndex > length - 1) 
				startIndex = length - 1;
			while (startIndex >= indexRange) {
				if (byteAt(startIndex) == b)
					return startIndex;
				startIndex--;
			}
			return -1;
		}
		public long lastIndexOf(byte[] b, long startIndex, long indexRange) throws IOException {
			if (length == 0 || indexRange > startIndex || null == b || b.length > length || b.length == 0 || startIndex - indexRange + 1 < b.length)
				return -1;
			indexRange = indexRange < 0 ? 0: indexRange;
			if (startIndex > length) 
				startIndex = length;
			long i;
			int i2;
			for (i = startIndex == length ?length - 1: startIndex; i >= indexRange; i--) {
				if (byteAt(i) == b[0]) {
					if (i + b.length > startIndex)
						continue;
					for (i2 = 1; i2 < b.length; i2++)
						if (byteAt(i + i2) != b[i2])
							break;
					if (i2 == b.length)
						return i;
				}
			}
			return -1;
		}

		public int read(long index)throws IOException {
			if (index < 0 || index >= length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s filelength=%s", index, length));
			if (!(nowPieceStart >= index && nowPieceEnd <= index))
				grow(index);
			return nowpieceData[(int)(index % pieceBufSize)];
		}
		public int read(long index , byte[] b) throws IOException {
			return read(index, b, 0, b.length);
		}
		public int read(long index , byte[] b, int boff, int blen) throws IOException {
			if (index < 0 || index >= length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s filelength=%s", index, length));
			if (index + blen > length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s off=%s len=%s filelength=%s", index, boff, blen, length));
			randomFileStream.seekIndex(index);
			return randomFileStream.read(b, boff, blen);
		}
		public void close() throws IOException {
			randomFileStream.close();
		}
	}

	@XAnnotations()
	public static class Output implements Closeable {
		public static Output wrap(File file) throws IOException {
			return new Output(file);
		}

		private int pieceBufSize;//BufferSize
		private XArrayPieceIndexManager xpm;
		private XInterfaceRandomAccessOutputStream randomFileStream;
		private long length;

		public Output(XInterfaceRandomAccessOutputStream randomFileStream, int PieceBufSize) throws IOException {
			if (PieceBufSize < 1)
				PieceBufSize = XStream.defaultStreamByteArrBuffSize;
			this.randomFileStream = randomFileStream;
			this.pieceBufSize = PieceBufSize;
			this.xpm = new XArrayPieceIndexManager(randomFileStream.length(), PieceBufSize);
			this.length = xpm.length();
		}
		public Output(File file) throws FileNotFoundException, IOException {
			this(new XRandomAccessFileOutputStream(file), XStream.defaultStreamByteArrBuffSize);
		}
		public Output(String file) throws FileNotFoundException, IOException {
			this(new File(file));
		}

		public void setPieceBytes(long newPiece, byte[] bytes, int boff, int blen) throws IOException {
			if (newPiece > xpm.getPieceNumber())
				throw new IndexOutOfBoundsException(String.format("need piece=%s, max piece=%s", newPiece, xpm.getPieceNumber()));
			if (blen != pieceBufSize)
				throw new IndexOutOfBoundsException(String.format("b.length=%s, piece.length=%s", bytes.length, pieceBufSize)); 
			long off = xpm.getPieceIndexStart(newPiece);
			write(off, bytes, boff, blen);
		}
		public void writePieceBytes(long newPiece, byte[] bytes, int boff, int blen) throws IOException {
			long off = xpm.getPieceIndexStart(newPiece);
			long end = xpm.getPieceIndexEnd(newPiece);
			if (blen + off >= end)
				blen = (int)xpm.getPieceLength(newPiece);
			write(off, bytes, boff, blen);
		}


		public int write(long index, int b) throws IOException {
			if (index >= length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s, filelength=%s", index, length));
			randomFileStream.seekIndex(index);
			randomFileStream.write(b);
			return 1;
		}
		public int write(long index, byte[] b) throws IOException {
			return write(index, b, 0, b.length);
		}
		public int write(long index, byte[] b, int boff, int blen) throws IOException {
			if (index + blen > length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s, b.length=%s, off=%s, len=%s, filelength=%s", index, b.length, boff, blen, length));
			randomFileStream.seekIndex(index);
			randomFileStream.write(b, boff, blen);
			return blen;
		}

		public void close() throws IOException {
			randomFileStream.close();
		}

		public void setLength(long length) throws IOException {
			this.randomFileStream.setLength(length);
			this.xpm.updatepPieceInfo(length, pieceBufSize);
			this.length = length;
		}
		public long getPieceBuffSize() {
			return xpm.getPieceBufSize();
		}
		public long getIndexPiece(long newPiece) {
			return xpm.getIndexPiece(newPiece);
		}
		public long getPieceLength(long newPiece) {
			return xpm.getPieceLength(newPiece);
		}
		public long getPieceIndexEnd(long newPiece) {
			return xpm.getPieceIndexEnd(newPiece);
		}
		public long getPieceIndexStart(long newPiece) {
			return xpm.getPieceIndexStart(newPiece);
		}
		public long getPieceCount() {
			return xpm.getPieceNumber();
		}



		public long length() {
			return length;
		}
	}
	
}
