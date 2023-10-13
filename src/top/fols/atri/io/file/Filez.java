package top.fols.atri.io.file;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.text.Collator;
import java.util.*;

import top.fols.atri.assist.encode.ByteEncodeDetect;
import top.fols.atri.io.BufferReaders;
import top.fols.atri.io.Delimiter;
import top.fols.atri.io.util.Streams;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Mathz;
import top.fols.atri.lang.Objects;
import top.fols.atri.interfaces.interfaces.IInnerFile;
import top.fols.box.util.encode.ByteEncoders;

@SuppressWarnings({"SpellCheckingInspection", "StatementWithEmptyBody", "UnnecessaryLocalVariable"})
public class Filez implements IInnerFile {
	public static final File[] EMPTY_FILE_ARRAY = {};


	public static Filez RUN  = new Filez(Filex.PATH_CURRENT_DIRECTORY_STRING);
	public static Filez RUN_CANONICAL  = RUN.getCanonical();

	public static Filez TEMP = new Filez(System.getProperty(Finals.Property.JAVA_IO_TEMPDIR, ""));
	public static Filez TEMP_CANONICAL = TEMP.getCanonical();


	public static final Charset CHARSET_UTF8 = Finals.Charsets.UTF_8;

	public static Filez absolute(Object path) {
		return new Filez(Filex.getCanonicalRelativePath(Filex.toFilePath(path)));
	}

	public static Filez wrap(String path) { return new Filez(path); }
	public static Filez wrap(Object path) { return new Filez(path); }



	private final File fileOrDirectory;


	/**
	 * no check
	 */
	public Filez(Object path) {
		this.fileOrDirectory = Objects.requireNonNull(Filex.toFile(path), "path");
	}
	public Filez(Filez path) {
		this.fileOrDirectory = Objects.requireNonNull(path, "path").fileOrDirectory;
		this.canonical       = path.canonical;
	}



	@Override
	public File innerFile() {
		return this.fileOrDirectory;
	}
	public String getPath() {
		return this.fileOrDirectory.getPath();
	}




	private transient String  canonicalPath;
	private transient boolean canonical;

	public String getCanonicalPath() {
		String path = this.canonicalPath;
		if (null ==      path) {
			if (isCanonical()) {
				//路径经过了格式化又创建为了File 实际上它获取到的路径可能已经改变了
				path = this.fileOrDirectory.getPath();
				if (!path.startsWith(Filex.system_separators)) {
					 path = Filex.system_separators + path;
				}
			} else {
				path = getCanonicalPath(this.fileOrDirectory);
			}
			this.canonicalPath = path;
		}
		return path;
	}



	/**
	 * absolutely path
	 */
	public static String getCanonicalPath(Object path) {
		File file = Filex.toFile(path);
		if (null == file) {
			return null;
		} else if (path instanceof Filez) {
			return ((Filez)path).getCanonicalPath();
		} else {
			String paths = new File(Filex.getCanonicalRelativePath(Filex.toLocalCanonicalPath(file), Filex.system_separator)).getPath();
			if (!paths.startsWith(Filex.system_separators)) {
				 paths = Filex.system_separators + paths;
			}
			return paths;
		}
	}
	public String getCanonicalPath(char separator) {
		String path = this.getCanonicalPath();
		if (separator == Filex.system_separator) {
			return path;
		} else {
			return path.replace(Filex.system_separator, separator);
		}
	}
	public Filez getCanonical() {
		if (this.canonical) {
			return this;
		} else {
			String path = this.getCanonicalPath();
			Filez  instance;
			instance = new Filez(path);
			instance.canonical = true;
			return instance;
		}
	}
	//localCanonical
	public boolean isCanonical() {
		return this.canonical;
	}



	public Filez getParent() {
		String filePath = this.getParentPath();
		File p = null == filePath ?null: new File(filePath);
		if (null == p) {
			return null;
		} else {
			Filez  instance;
			instance = new Filez(p);
			instance.canonical = this.canonical;
			return instance;
		}
	}

	public String getParentPath() {
		return Filex.getParent(this.getPath());
	}

	public String getName() {
		return Filex.getName(this.getPath());
	}

	public String getExtensionName() {
		return Filex.getExtensionName(this.getPath());
	}

	public String getNameNoExtension() {
		return Filex.getNameNoExtension(this.getPath());
	}











	@Override
	public Filez clone() {
		// TODO: Implement this method
		try {
			Filez  instance = (Filez) super.clone();
			return instance;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}



	@Override
	public int hashCode() {
		// TODO: Implement this method
		return this.getCanonicalPath().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (null == obj) { return false; }
		if (obj instanceof Filez) {
			Filez fz = (Filez) obj;
			return Objects.equals(getPath(), fz.getPath()) || Objects.equals(getCanonicalPath(), fz.getCanonicalPath());
		}
		return false;
	}






	@Override
	public String toString() {
		// TODO: Implement this method
		return this.getPath();
	}


	/**
	 * @return 判断是否是本地文件
	 */
	public boolean isFile() {
		return Filex.isFile(this);
	}
	/**
	 * @return 判断是否是本地目录
	 */
	public boolean isDirectory() {
		return Filex.isDir(this);
	}




	/**
	 * @return 判断是否是本地存在
	 */
	public boolean exists() {
		return Filex.exists(this);
	}
	/**
	 * 会检测名称的规范性 不能出现指向 父目录等， 有什么用我也不知道，但是我觉得有用
	 * @see Filex#pathCheck
	 */
	public boolean exists(String name) {
		return Filex.exists(new File(innerFile(), name));
	}

	public boolean createsFile() {
		return Filex.createsFile(this);
	}
	/**
	 * 会检测名称的规范性 不能出现指向 父目录等， 有什么用我也不知道，但是我觉得有用
	 * @see Filex#pathCheck
	 */
	public Filez   createsFile(String name) {
		File file = new File(this.innerFile(), name);
		Filex.createsFile(file);
		Filez  files;
		files = new Filez(file);
		files.canonical = Filex.pathChecked(name) && this.canonical;
		return files;
	}

	public boolean createsDir() {
		return Filex.createsDir(this);
	}
	/**
	 * 会检测名称的规范性 不能出现指向 父目录等， 有什么用我也不知道，但是我觉得有用
	 * @see Filex#pathCheck
	 */
	public Filez   createsDir(String name) {
		File file = new File(this.innerFile(), name);
		Filex.createsDir(file);
		Filez  files;
		files = new Filez(file);
		files.canonical = Filex.pathChecked(name) && this.canonical;
		return files;
	}


	public File file(String name) {
		File   file = new File(this.innerFile(), name);
		return file;
	}
	/**
	 * 会检测名称的规范性 不能出现指向 父目录等， 有什么用我也不知道，但是我觉得有用
	 * @see Filex#pathCheck
	 */
	public Filez files(String name) {
		File   file = this.file(name);
		Filez  filez;
		filez = new Filez(file);
		filez.canonical = this.canonical;
		return filez;
	}


	/**
	 * @param name 将格式化子路径， 格式化后不会再出现 .. .等
	 */
	public File subfile(String name) {
		return new File(this.innerFile(), Filex.getCanonicalRelativePath(name, Filex.system_separator));
	}
	/**
	 * @param name 将格式化子路径， 格式化后不会再出现 .. .等
	 */
	public Filez subfiles(String name) {
		File   file = this.subfile(name);
		Filez  filez;
		filez = new Filez(file);
		filez.canonical = this.canonical;
		return filez;
	}





	public String[] listNames() {
		String[] names = this.fileOrDirectory.list();
		return   names;
	}
	public Filez[]  listFiles() {
		String[] names = this.fileOrDirectory.list();
		if (null == names) {
			return null;
		}
		Filez[] list = new Filez[names.length];
		for (int i = 0; i < names.length; i++) {
			// On some file systems,
			// the file separator of another system can be used as the file name. . . so.  no  use {new File}
			list[i] = new Filez(new File(getPath(), names[i]));
			list[i].canonical = this.canonical;
		}
		return list;
	}






	/**
	 * 会检测名称的规范性 不能出现指向 父目录等，也不能出现分隔符 有什么用我也不知道，但是我觉得有用
	 * @see Filex#pathCheck
	 */
	public boolean refilename(String name) {
		Filex.nameCheck(name);
		String parent = getParentPath();
		File   file = null == parent?new File(name):new File(parent, name);
		return innerFile().renameTo(file);
	}
	public boolean reparent(String directory) {
		String name = getName();
		File   file = null == directory?new File(name):new File(directory, name);
		return innerFile().renameTo(file);
	}
	public boolean renameTo(String path) {
		return innerFile().renameTo(new File(path));
	}



	public Filex.Snapshot snapshot() {
		return snapshot(innerFile());
	}
	public static Filex.Snapshot snapshot(Object path) {
		File file = Filex.toFile(path);
		return Filex.Snapshot.parse(file);
	}


	public boolean delete() {
		return Filex.deletes(this);
	}
	public boolean deleteIfFile() {
		if (this.isFile()) {
			return innerFile().delete();
		}
		return false;
	}
	public boolean deleteIfDirectory() {
		if (this.isDirectory()) {
			return delete();
		}
		return false;
	}




	public int len() {
		return Mathz.toIntExact(length());
	}
	public long length() {
		return this.innerFile().length();
	}


	public FileInputStream  input() throws FileNotFoundException {
		return Filex.forInput(this);
	}
	public FileOutputStream output() throws FileNotFoundException {
		return output(false);
	}
	public FileOutputStream output(boolean append) throws FileNotFoundException {
		return Filex.forOutput(this,  append);
	}



	public RandomAccessFile randomAccess(String mode) throws FileNotFoundException {
		return new RandomAccessFile(innerFile(), mode);
	}




	public byte[] readBytes() {
		InputStream stream = null;
		try {
			stream = input();
			return Streams.toBytes(stream);
		} catch (IOException e) {
			return null;
		} finally {
			Streams.close(stream);
		}
	}
	public char[] readChars(Charset charset) {
		if (null == charset) {
			throw new NullPointerException("charset");
		}
		byte[] bytes = this.readBytes();
		if (null == bytes) {
			return null;
		} else {
			return ByteEncoders.bytesToChars(bytes, 0, bytes.length, charset);
		}
	}

	public String readUTF8String() {
		char[] str = readChars(CHARSET_UTF8);
		return null == str ?null: new String(str);
	}
	public char[] readUTF8Chars() {
		return readChars(CHARSET_UTF8);
	}



	public Charset readDetectCharset() {
		try {
			return ByteEncodeDetect.getJavaEncode2Charset(innerFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public char[] readDetectCharsetChars() {
		byte[] bytes = readBytes();
		if (null == bytes) {
			return null;
		} else {
			try {
				Charset charset = ByteEncodeDetect.getJavaEncode2Charset(bytes);
				return ByteEncoders.bytesToChars(bytes, 0, bytes.length, charset);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	public String readDetectCharsetString() {
		byte[] bytes = readBytes();
		if (null == bytes) {
			return null;
		} else {
			try {
				return new String(bytes, 0, bytes.length, ByteEncodeDetect.getJavaEncode2Charset(bytes));
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}



	public boolean writeUTF8(String data) { return writeChars(data, CHARSET_UTF8); }
	public boolean writeUTF8(char[] data) { return writeChars(data, CHARSET_UTF8); }

	public boolean writeChars(CharSequence data, Charset charset) {
		if (null == data) {
			throw new NullPointerException("data");
		}
		return writeChars(data, 0, data.length(), charset);
	}
	public boolean writeChars(char[] data, Charset charset) {
		if (null == data) {
			throw new NullPointerException("data");
		}
		return writeChars(data, 0, data.length, charset);
	}
	public boolean writeChars(CharSequence data, int off, int len, Charset charset) {
		if (null == data) {
			throw new NullPointerException("data");
		}
		return writeBytes(ByteEncoders.charsToBytes(data, off, len, charset));
	}
	public boolean writeChars(char[] data, int off, int len, Charset charset) {
		if (null == data) {
			throw new NullPointerException("data");
		}
		return writeBytes(ByteEncoders.charsToBytes(data, off, len, charset));
	}

	public boolean writeBytes(byte[] data) {
		if (null == data) {
			throw new NullPointerException("bytes");
		}
		return writeBytes(data, 0, data.length);
	}
	public boolean writeBytes(byte[] data, int off, int len) {
		if (null == data) {
			throw new NullPointerException("data");
		}
		OutputStream stream = null;
		try {
			stream = output();
			stream.write(data, off, len);
			stream.flush();

			return true;
		} catch (IOException e) {
			return false;
		} finally {
			Streams.close(stream);
		}
	}



	public boolean appendUTF8(String data) { return appendChars(data, CHARSET_UTF8); }
	public boolean appendUTF8(char[] data) { return appendChars(data, CHARSET_UTF8); }

	public boolean appendChars(CharSequence data, Charset charset) {
		if (null == data) {
			throw new NullPointerException("data");
		}
		return appendChars(data, 0, data.length(), charset);
	}
	public boolean appendChars(char[] data, Charset charset) {
		if (null == data) {
			throw new NullPointerException("data");
		}
		return appendChars(data, 0, data.length, charset);
	}
	public boolean appendChars(CharSequence data, int off, int len, Charset charset) {
		if (null == data) {
			throw new NullPointerException("data");
		}
		return appendBytes(ByteEncoders.charsToBytes(data, off, len, charset));
	}
	public boolean appendChars(char[] data, int off, int len, Charset charset) {
		if (null == data) {
			throw new NullPointerException("data");
		}
		return appendBytes(ByteEncoders.charsToBytes(data, off, len, charset));
	}

	public boolean appendBytes(byte[] data) {
		if (null == data) {
			throw new NullPointerException("bytes");
		}
		return appendBytes(data, 0, data.length);
	}
	public boolean appendBytes(byte[] data, int off, int len) {
		if (null == data) {
			throw new NullPointerException("data");
		}
		OutputStream stream = null;
		try {
			stream = output(true);
			stream.write(data, off, len);
			stream.flush();

			return true;
		} catch (IOException e) {
			return false;
		} finally {
			Streams.close(stream);
		}
	}



	public BufferReaders<Reader> linesReader(Charset charset) throws FileNotFoundException {
		BufferReaders<Reader> readerBufferReaders = new BufferReaders<>((Reader) new InputStreamReader(input(), charset));
		readerBufferReaders.setDelimiterAsLine();
		return readerBufferReaders;
	}
	public String[] lines(Charset charset) throws IOException {
		BufferReaders<Reader> readerBufferReaders = linesReader(charset);
		List<String> strings = Delimiter.splitToStringLists(readerBufferReaders);
		return strings.toArray(Finals.EMPTY_STRING_ARRAY);
	}


	public Path toPath(){ return innerFile().toPath(); }

	public boolean isSymbolicLink() {
		return Filex.isSymbolicLink(this);
	}
	public boolean copySymbolicLinkTo(Filez to) {
		try {
			return Filex.copySymbolicLinkTo(this, to);
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Copy this file to the specified directory
	 *
	 * @param to to
	 */
	public boolean copyTo(Filez to) {
		try {
			return deepCopyTo(null, to, true);
		} catch (IOException ignored) {
			return false;
		}
	}

	/**
	 * Copy sub files to the specified directory
	 * A snapshot of the list is saved before copying
	 *
	 * @param to to
	 */
	public boolean deepCopyTo(Filez to) {
		try {
			return deepCopyTo(Filex.Snapshot.parse(this.innerFile()), to, true);
		} catch (IOException ignored) {
			return false;
		}
	}

	/**
	 * Copy sub files to the specified directory
	 * A snapshot of the list is saved before copying
	 *
	 * @param subfiles file filter   if this file is Directory
	 * @param to to
	 */
	public boolean deepCopyTo(Filex.Snapshot subfiles, Filez to, boolean ignoredIoError) throws IOException {
		return Filex.copyTo(this, subfiles, to, ignoredIoError);
	}



	//递归搜索文件，假设子文件的子文件中存在一个子文件的链接，是否会造成无限死循环？
	public static List<String> listRelativeFilePath(String filePath, boolean recursion, boolean addDirectory) {
		List<String> List = new ArrayList<>();
		return listRelativeFilePath0(List, new File(filePath), recursion, addDirectory, new StringBuilder());
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
						list.add(baseDir + name + File.separator);
					}
				} else {
					list.add(baseDir + name);
				}
			}
		}
		return list;
	}






	public static List<String> listFilesSort(File filePath, boolean adddir) {
		return listFilesSort(filePath.listFiles(), adddir, Locale.getDefault());
	}
	public static List<String> listFilesSort(File[] files, boolean adddir, Locale locale) {
		List<String> d = new ArrayList<>();
		List<String> f = new ArrayList<>();
		if (null != files) {
			for (File file : files) {
				if (null == file) {
					continue;
				}
				String name = file.getName();
				if (file.isDirectory()) {
					if (adddir) {
						d.add(name + File.separator);
					}
				} else {
					f.add(name);
				}
			}
		}
		Collections.sort(d, Collator.getInstance(locale));
		Collections.sort(f, Collator.getInstance(locale));
		if (adddir) {
			d.addAll(f);
			return d;
		} else {
			return f;
		}
	}


}
