package top.fols.box.util;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import top.fols.atri.interfaces.interfaces.IFilter;
import top.fols.atri.io.util.Streams;

@SuppressWarnings({"StringOperationCanBeSimplified", "resource"})
public class ZipReader implements AutoCloseable {
	static final String   ROOT_DIRECTORY = Zips.ROOT_DIRECTORY;
	static final String[] EMPTY_STRING_ARRAY = {};


	String  baseDirectory;
	File    file;
	Charset zfilecharset;
	ZipFile zfile;

	public ZipReader(File zip) {
		this(zip, null, null);
	}
	public ZipReader(File zip, Charset zipCharset) {
		this(zip, zipCharset, null);
	}
	public ZipReader(File zipfile, Charset zipCharset,
					 String  innerBaseDirectory) {
		try {
			zipfile = (null == zipfile || !zipfile.isFile() || !zipfile.canRead()) ? null : zipfile;

			this.file         =  zipfile;
			this.baseDirectory = (null == innerBaseDirectory || innerBaseDirectory.length() == 0) ? ROOT_DIRECTORY : Zips.formatPathToDirectory(innerBaseDirectory);
			this.zfilecharset  = (null == zipCharset ? StandardCharsets.UTF_8: zipCharset);
			if (null != zipfile) {
				this.zfile = new ZipFile(this.file, ZipFile.OPEN_READ, this.zfilecharset);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



	@Override
	public void close() throws Exception {
		// TODO: Implement this method
		Streams.close(this.zfile);
	}





	public boolean isRootPath(String formatedPath) {
		return ROOT_DIRECTORY.equals(formatedPath);
	}
	public boolean pathIsDirectory(String formatedPath) {
		return Zips.pathIsDirectory(formatedPath);
	}



	public String getBaseDirectory() {
		return baseDirectory;
	}
	public Charset getCharset() {
		return this.zfilecharset;
	}


	public InputStream getInputStream(ZipEntry entry) {
		if (this.file == null)
			return       null;
		if (entry == null)
			return   null;
		try {
			return zfile.getInputStream(entry);
		} catch (Throwable e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public Enumeration<? extends ZipEntry> entries() {
		if (this.file == null)
			return       null;
		return zfile.entries();
	}

	public String getName(String formatedPath) {
		return Zips.getName(formatedPath);
	}
	public String getParent(String formatedPath) {
		return Zips.getParent(formatedPath);
	}






	private Set<String> initializationMkdirsDirectory(Map<String, Set<String>> fileMap, String formatedDir) {
		Set<String> sf = fileMap.get(formatedDir);
		if (null == sf) {
			fileMap.put(formatedDir, sf = new LinkedHashSet<>());
		}
		String current = formatedDir;
		String parent = getParent(current);
		while (null != parent) {
			Set<String> pf = fileMap.get(parent);
			if (null == pf) {
				fileMap.put(parent, pf = new LinkedHashSet<>());
			}
			pf.add(current);
			current = parent;
			parent = getParent(parent);
		}
		return sf;
	}
	private Map<String, ZipEntry> 	 fileEntryMap;
	private Map<String, Set<String>> directoryFileMap;
	private Set<String> 			 fileSet;

	@SuppressWarnings("unchecked")
	private ZipReader initialization() {
		if (this.file == null)
			return       null;
		if (null == directoryFileMap) {
			Map<String, Set<String>> directoryFileMap = new LinkedHashMap<>();
			Map<String, ZipEntry>    fileEntryMap     = new LinkedHashMap<>();
			Enumeration<ZipEntry> enums = (Enumeration<ZipEntry>) entries();
			while (enums.hasMoreElements()) {
				ZipEntry entry    = enums.nextElement();
				String originPath = entry.getName();
				String formatPath = Zips.formatPath(originPath);
				if (formatPath.startsWith(baseDirectory)) {
					formatPath = formatPath.substring(baseDirectory.length(), formatPath.length());

					fileEntryMap.put(formatPath, entry);

					if (pathIsDirectory(formatPath)) {
						initializationMkdirsDirectory(directoryFileMap, formatPath);
					} else {
						String parent = getParent(formatPath);
						Set<String> sf = initializationMkdirsDirectory(directoryFileMap, parent) ;

						if (!(formatPath.length() == 0)) {
							sf.add(formatPath);
						}
					}
				}
			}
			this.directoryFileMap = directoryFileMap;
			this.fileEntryMap     = fileEntryMap;
			this.fileSet 		  = Collections.unmodifiableSet(fileEntryMap.keySet());
		}
		return this;
	}
	public Set<String> allPath() {
		return initialization().fileSet;
	}
	public String[]    allDirectory() {
		return initialization().directoryFileMap.keySet().toArray(new String[]{});
	}


	public String[] listPathsFromRoot() {
		return listPaths(ROOT_DIRECTORY);
	}
	public String[] listPaths(String dir) {
		Set<String>    s = listPathsSet(dir);
		return null == s ?
				EMPTY_STRING_ARRAY:
				s.toArray(EMPTY_STRING_ARRAY);
	}
	private Set<String> listPathsSet(String dir) {
		if (this.file == null)
			return       null;
		String directory;
		if (ROOT_DIRECTORY.equals(dir)) {
			directory = dir;
		} else {
			directory = Zips.pathToDirectory(Zips.formatPath(dir));
		}
		return initialization().directoryFileMap.get(directory);
	}

	public int getCount(String dir) {
		Set<String> s = listPathsSet(dir);
		if (null == s)
			return 0;
		return s.size();
	}
	public int getCount() {
		return fileEntryMap.size();
	}

	public boolean exists(String path) {
		return null != getEntry(path);
	}
	public ZipEntry getEntry(String path) {
		if (this.file == null)
			return       null;
		Map<String, ZipEntry> file = this.initialization().fileEntryMap;
		ZipEntry    entry = file.get(path);
		if (null != entry) {
			return  entry;
		}
		return file.get(Zips.formatPath(path));
	}




	public FileElement getFileItemAsRoot() {
		return new FileElement(ROOT_DIRECTORY);
	}
	public FileElement getFileItem(String path) {
		return new FileElement(Zips.formatPath(path));
	}
	public FileElement getFileItemAsDirectory(String path) {
		return new FileElement(Zips.pathToDirectory(Zips.formatPath(path)));
	}
	public class FileElement {
		final String formatedPath;
		FileElement(String formatedPath) {
			this.formatedPath = formatedPath;
		}

		public String getPath() {
			return this.formatedPath;
		}

		public boolean isDirectory() {
			return ZipReader.this.pathIsDirectory(this.formatedPath);
		}
		public boolean isFile() {
			return !ZipReader.this.pathIsDirectory(this.formatedPath);
		}
		public boolean exists() {
			return ZipReader.this.exists(getPath());
		}
		public int getCount() {
			return ZipReader.this.getCount(getPath());
		}

		public String getParent() {
			return ZipReader.this.getParent(getPath());
		}
		public FileElement getParentFile() {
			String parent = getParent();
			if (parent == null)
				return    null;
			return getFileItem(parent);
		}

		public InputStream getInputStream() {
			return ZipReader.this.getInputStream(getEntry(getPath()));
		}

		String cacheName;
		public String getName() {
			if (cacheName == null)
				cacheName = ZipReader.this.getName(this.getPath());
			return cacheName;
		}
		@Override
		public String toString() {
			// TODO: Implement this method
			return getPath().toString();
		}



		public String[] listNames() {
			return listNames(null);
		}
		public String[] listNames(IFilter<String> filter) {
			Set<String>    s = listPathsSet(getPath());
			if   (s == null)
				return null;
			if (null == filter) {
				return s.toArray(EMPTY_STRING_ARRAY);
			} else {
				List<String> result = new ArrayList<>();
				for (String cf: s) {
					if (filter.next(cf)) {
						result.add(cf);
					}
				}
				return result.toArray(EMPTY_STRING_ARRAY);
			}
		}
		public FileElement[] listFiles() {
			return listFiles(null);
		}
		public FileElement[] listFiles(IFilter<FileElement> filter) {
			Set<String>    s = listPathsSet(getPath());
			if   (s == null)
				return null;
			String dir = Zips.pathToDirectory(getPath());
			FileElement[] result;
			if (null == filter) {
				result = new FileElement[s.size()];
				Iterator<String>  iterator = s.iterator();
				for (int i = 0;   iterator.hasNext();i++) {
					result[i] = new FileElement(dir + iterator.next());
				}
			} else {
				List<FileElement> buffer = new ArrayList<>();
				Iterator<String>  iterator = s.iterator();
				for (int i = 0;   iterator.hasNext();i++) {
					FileElement cf = new FileElement(dir + iterator.next());
					if (filter.next(cf)) {
						buffer.add(cf);
					}
				}
				result = buffer.toArray(new FileElement[]{});
			}
			return result;
		}
	}







//	public static void main(String[] args) {
//		String pathname ;
//		pathname= "C:\\Users\\78492\\Desktop\\20230706表格处理\\20230725权责清单.zip";
//		try (ZipResources zipResources = new ZipResources(new File(pathname), Charset.forName("GBK"))) {
//			String[] a = zipResources.filterFile(new IFilter<String>() {
//					@Override
//					public boolean next(String param) {
//						return true;
//					}
//				});
//			System.out.println(Arrays.toString(a));
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

}

