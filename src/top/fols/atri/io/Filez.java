package top.fols.atri.io;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Set;

import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Mathz;
import top.fols.atri.lang.Objects;
import top.fols.atri.lang.Strings;
import top.fols.atri.util.interfaces.InnerFile;
import top.fols.box.util.XByteEncode;

@SuppressWarnings({"SpellCheckingInspection", "StatementWithEmptyBody"})
public class Filez implements InnerFile {
	public static Filez RUN  = new Filez(".").getCanonical();
	public static Filez TEMP = new Filez(System.getProperty(Finals.Property.JAVA_IO_TEMPDIR, "")).getCanonical();


	public static Filez wrap(Object path) {
		return new Filez(path);
	}




	private final File    fileOrDirectory;
	private boolean isCanonical;


	public Filez(Object path) {
		File file = Objects.requireNonNull(Filex.toFile(path), "path");

		this.fileOrDirectory = file;
	}
	public Filez(Filez path) {
		File file             = Objects.requireNonNull(path, "path").fileOrDirectory;

		this.fileOrDirectory  = file;
		this.isCanonical        = path.isCanonical;

	}
	public Filez(Filez path, String name) {
		File pattern         = Objects.requireNonNull(path, "path").fileOrDirectory;

		this.fileOrDirectory = new File(pattern, Filex.nameCheck(name));
		this.isCanonical       = path.isCanonical;
	}



	public File innerFile() {
		return this.fileOrDirectory;
	}


	public String getPath() {
		return this.fileOrDirectory.getPath();
	}




	private transient String cPath;
	public String getCanonicalPath() {
		String path = this.cPath;
		if (null ==      path) {
			this.cPath = path = isCanonical() ?fileOrDirectory.getPath(): Filex.getCanonicalPath(this.fileOrDirectory);
		}
		return path;
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
		if (this.isCanonical) {
			return this;
		} else {
			String path = this.getCanonicalPath();
			Filez  instance;
			instance = new Filez(path);
			instance.isCanonical = true;
			return instance;
		}
	}
	public boolean isCanonical() {
		return this.isCanonical;
	}



	public Filez getParent() {
		String filePath = this.getParentPath();
		File p = null == filePath ?null: new File(filePath);
		if (null == p) {
			return null;
		} else {
			Filez  instance;
			instance = new Filez(p);
			instance.isCanonical = this.isCanonical;
			return instance;
		}
	}
	
	public String getParentPath() {
		String filePath = this.getPath();
		if ((null != filePath)) {
			int dot = Strings.lastIndexOfChar(filePath, Filex.separatorChars);
			if (dot > -1) {
				filePath = filePath.substring(0, dot + 1);
			} else {
				filePath = null;
			}
		}
		return filePath;
	}
	
	public String getName() {
		String filePath = this.getPath();
		if ((null != filePath)) {
			int dot = Strings.lastIndexOfChar(filePath, Filex.separatorChars);
			if (dot > -1) {
				return filePath.substring(dot + 1, filePath.length());
			} else {
				return filePath;
			}
		}
		return null;
	}

	public String getExtensionName() {
		String filePath = this.getPath();
		if ((null != filePath) && (filePath.length() > 0)) {
			int dot = Strings.lastIndexOfChar(filePath, Filex.FILE_EXTENSION_NAME_SEPARATOR);
			int splitCharDot = Strings.lastIndexOfChar(filePath, Filex.separatorChars);
			if (splitCharDot > -1) {
				if (dot > -1 && dot > splitCharDot) {
					return filePath.substring(dot + 1, filePath.length());
				}
			} else {
				if (dot > -1) {
					return filePath.substring(dot + 1, filePath.length());
				}
			}
		}
		return null;
	}

	public String getNameNoExtension() {
		String filePath = this.getPath();
		if ((null != filePath)) {
			int dot = Strings.lastIndexOfChar(filePath, Filex.separatorChars);
			if (dot > -1) {
				int dot2 = Strings.lastIndexOfChar(filePath, Filex.FILE_EXTENSION_NAME_SEPARATOR);
				if (dot2 > dot) { // dot2 > -1
					return filePath.substring(dot + 1, dot2);
				} else {// dot2 <= -1
					return filePath.substring(dot + 1, filePath.length());
				}
			} else {
				int dot2 = Strings.lastIndexOfChar(filePath, Filex.FILE_EXTENSION_NAME_SEPARATOR);
				if (dot2 > -1) {
					return filePath.substring(0, dot2);
				} else {
					return filePath;
				}
			}
		}
		return null;
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
		if (!(obj instanceof Filez)) {
			return false;
		}
		Filez fz = (Filez) obj;
		return
			Objects.equals(getPath(), fz.getPath()) ||
			Objects.equals(getCanonicalPath(), fz.getCanonicalPath());
	}






	@Override
	public String toString() {
		// TODO: Implement this method
		return this.getPath();
	}


	public boolean isFile() {
		return Filex.isFile(this);
	}
	public boolean isDirectory() {
		return Filex.isDir(this);
	}


	public boolean exists() {
		return Filex.exists(this);
	}
	public boolean exists(String name) {
		return Filex.exists(new File(innerFile(), name));
	}


	public boolean createsFile() { 
		return Filex.createsFile(this);
	}
	public Filez   createsFile(String name) {
		Filex.nameCheck(name);
		File file = new File(this.innerFile(), name);
		Filex.createsFile(file);
		Filez  files;
		files = new Filez(file);
		files.isCanonical = this.isCanonical;
		return files;
	}

	public boolean createsDir() {
		return Filex.createsDir(this);
	}
	public Filez   createsDir(String name) {
		Filex.nameCheck(name);
		File file = new File(this.innerFile(), name);
		Filex.createsDir(file);
		Filez  files;
		files = new Filez(file);
		files.isCanonical = this.isCanonical;
		return files;
	}


	public File file(String name) {
		Filex.nameCheck(name);
		File   file = new File(this.innerFile(), name);
		return file;
	}
	public Filez files(String name) {
		File   file = this.file(name);
		Filez  filez;
		filez = new Filez(file);
		filez.isCanonical = this.isCanonical;
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
			list[i] = new Filez(this, names[i]); 
			list[i].isCanonical = this.isCanonical;
		}
		return list;
	}







	public Snapshot snapshot() {
		return snapshot(innerFile());
	}
	public static Snapshot snapshot(Object path) {
		File file = Filex.toFile(path);
		return Snapshot.parse(file);
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
		return Filex.forOutput(this);
	}







	public byte[] readBytes() {
		InputStream stream = null;
		try {
			stream = input();
			byte[] bytes = Streams.toBytes(stream);
			return bytes;
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
			return XByteEncode.bytesToChars(bytes, 0, bytes.length, charset);
		}
	}
	public char[] readUTF8() {
		return readChars(Finals.StandardCharsets.UTF_8);
	}
	public String readUTF8Chars() {
		char[] str = readChars(Finals.StandardCharsets.UTF_8);
		return null == str ?null: new String(str);
	}


	public boolean writeUTF8(String data) {
		return writeChars(data, Finals.StandardCharsets.UTF_8);
	}
	public boolean writeUTF8(char[] data) {
		return writeChars(data, Finals.StandardCharsets.UTF_8);
	}
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
		byte[] bytes = XByteEncode.charsToBytes(data, 0, data.length(), charset);
		return writeBytes(bytes, 0, bytes.length);
	}
	public boolean writeChars(char[] data, int off, int len, Charset charset) {
		if (null == data) {
			throw new NullPointerException("data");
		}
		byte[] bytes = XByteEncode.charsToBytes(data, 0, data.length, charset);
		return writeBytes(bytes, 0, bytes.length);
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






	/**
	 * Copy this file to the specified directory
	 *
	 * @param to to
	 */
	public boolean copyTo(Filez to) {
		try {
			return copySubFilesTo(null, to, true);
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
	public boolean copySubFilesTo(Filez to) {
		try {
			return copySubFilesTo(Snapshot.parse(this.innerFile()), to, true);
		} catch (IOException ignored) {
			return false;
		}
	}

	/**
	 * Copy sub files to the specified directory
	 * A snapshot of the list is saved before copying
	 *
	 * @param thisList file filter   if this file is Directory
	 * @param to to
	 */
	public boolean copySubFilesTo(Snapshot thisList, Filez to, boolean ignoredIoError) throws IOException {
		return clone0(thisList, to, ignoredIoError);
	}



	private boolean clone0(Snapshot thisList, Filez to, boolean ignoredIoError) throws IOException  {
		if (null == to) {
			throw new NullPointerException("to file");
		}
		if (to.exists()) {
			//throw new IOException("existed: " + file);
		}
		boolean result = true;
		if (this.isFile()) {
			to.createsFile();
			try {
				copyAfterClose(input(), output());
			} catch (IOException e) {
				if (ignoredIoError) {
					result = false;
				} else {
					throw e;
				}
			}
		} else if (this.isDirectory()) {
			to.createsDir();

			if (null != thisList) {
				for (String s : thisList.keySet()) {
					final Filez fromFiles = this.files(s);
					if (thisList.isFile(s)) {
						Filez toFiles;
						toFiles = to.files(s);
						toFiles.createsFile();

						try {
							copyAfterClose(fromFiles.input(), toFiles.output());
						} catch (IOException e) {
							if (ignoredIoError) {
								result = false;
							} else {
								throw e;
							}
						}
					} else {
						result = result & fromFiles.clone0(thisList.getFileList(s), to.files(s), ignoredIoError);
					}
				}
			}
		}
		return result;
	}

	@SuppressWarnings("UnusedReturnValue")
	public static long copyAfterClose(InputStream input, OutputStream output) throws IOException {
		long   copy = Streams.copyFixedLength(input, output, Streams.DEFAULT_BYTE_BUFF_SIZE, Streams.COPY_UNLIMIT_COPYLENGTH, true);
		output.flush();

		Streams.close(input);
		Streams.close(output);

		return copy;
	}

	/**
	 * deep file list
	 */
	@SuppressWarnings({"UnnecessaryLocalVariable"})
	public static class Snapshot implements Serializable {
		static final long serialVersionUID = 42L;

		String name;
		LinkedHashMap<String, Object> fileLinkedHashMap = new LinkedHashMap<>();

		private Snapshot(String name) {
			this.name = name;
		}


		private void put(String name, File file) {
			if (null == file) { return; }
			fileLinkedHashMap.put(name, file);
		}
		private void put(String name, Snapshot fileList) {
			if (null ==   fileList) { return; }
			fileLinkedHashMap.put(name, fileList);
		}


		public boolean contains(String name) {
			return fileLinkedHashMap.containsKey(name);
		}
		public boolean remove(String name) {
			return null == fileLinkedHashMap.remove(name);
		}


		public static Snapshot parse(File file) {
			if (null == file) {
				throw new NullPointerException("to file");
			}
			return parse(new Snapshot(file.getName()),  file);
		}
		static Snapshot parse(Snapshot fileList, File file) {
			if (null == file) {
				throw new NullPointerException("to file");
			}
			File[] fs = file.listFiles();
			for (File f: null == fs ?new File[]{}: fs) {
				String name = f.getName();
				if (f.isFile()) {
					fileList.put(name, f);
				} else {
					Snapshot fileL = new Snapshot(name);
					fileList.put(name, fileL);
					parse(fileL, f);
				}
			}
			return fileList;
		}



		public String   getName() {
			return name;
		}
		public String[] listName() {
			return fileLinkedHashMap.keySet().toArray(Finals.EMPTY_STRING_ARRAY);
		}


		public Set<String> keySet() {
			return fileLinkedHashMap.keySet();
		}

		public Object get(String name) {
			Object object = fileLinkedHashMap.get(name);
			return object;
		}

		public boolean isDirectory(String name) {
			return get(name) instanceof Snapshot;
		}
		public boolean isFile(String name) {
			return get(name) instanceof File;
		}

		public Snapshot getFileList(String name) {
			Object object = get(name);
			if (object instanceof Snapshot) { return (Snapshot) object; }
			return null;
		}
		public File 	getFile(String name) {
			Object object = get(name);
			if (object instanceof File)     { return (File) object;     }
			return null;
		}


		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Snapshot)) return false;
			Snapshot snapshot = (Snapshot) o;
			return java.util.Objects.equals(name, snapshot.name) &&
					java.util.Objects.equals(fileLinkedHashMap, snapshot.fileLinkedHashMap);
		}

		@Override
		public int hashCode() {
			return java.util.Objects.hash(name, fileLinkedHashMap);
		}

		@Override
		public String toString() {
			return name+fileLinkedHashMap.toString();
		}
	}
}
