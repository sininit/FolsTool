package top.fols.box.util;

import top.fols.atri.interfaces.interfaces.IFilter;
import top.fols.atri.io.file.Filex;
import top.fols.atri.io.util.Streams;


import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
@SuppressWarnings({"StringOperationCanBeSimplified", "resource"})
public class ZipResources implements AutoCloseable {
	static final String   ROOT_DIRECTORY = Zips.ROOT_DIRECTORY;
	static final String[] EMPTY_STRING_ARRAY = {};


	String  base;

	String path;
	File file;

	Charset zfilecharset;
	ZipFile zfile;

	public ZipResources(File zip) {
		this(zip, null, null);
	}
	public ZipResources(File zip, Charset zipCharset) {
		this(zip, zipCharset, null);
	}
	public ZipResources(File zip, Charset zipCharset,
						String  innerBaseDirectory) {
		try {
			zip = (null == zip || !zip.isFile() || !zip.canRead()) ? null : zip;
			this.file   =  zip;
			this.base    = (null == innerBaseDirectory || innerBaseDirectory.length() == 0) ? ROOT_DIRECTORY : Zips.formatPathToDirectory(innerBaseDirectory);
			this.zfilecharset = null == zipCharset ? StandardCharsets.UTF_8: zipCharset;

			if (null != zip) {
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

	protected String formatSubPath(String subpath) {
		String f = base + Zips.formatPath(subpath);
		return f;
	}

	public String getEntryPath(String subpath) {
		String p = formatSubPath(subpath);
		return p;
	}

	public String getBaseDirectory() {
		return base;
	}

	public Charset getCharset() {
		return this.zfilecharset;
	}

	public ZipEntry getEntry(String subpath) {
		if (null == this.file)
			return null;
		String s = getEntryPath(subpath);
		return this.loadFiles().fileEntryMap.get(s);
	}
	public boolean exists(String subpath) {
		return null  != getEntry(subpath);
	}
	public InputStream getInputStream(ZipEntry entry) {
		if (null == this.file)
			return null;
		if (null == entry) {
			return null;
		}
		try {
			return zfile.getInputStream(entry);
		} catch (Exception ignored) {}
		return null;
	}

	public Enumeration<? extends ZipEntry> entries() {
		if (null == this.file)
			return null;
		return zfile.entries();
	}

	public String getParent(String file) {
		if (file == null)
			return null;

		if (ROOT_DIRECTORY.equals(file))
			return null;

		String parent = Filex.getParent(file);
		if (null == parent && file.length() > 0)
			return ROOT_DIRECTORY;
		return parent;
	}
	public boolean isRootPath(String file) {
		return ROOT_DIRECTORY.equals(file);
	}


	public boolean pathIsDirectory(String file) {
		return isRootPath(file) || Zips.pathIsDirectory(file);
	}

	Set<String> loadFilesMkdirs(Map<String, Set<String>> files, String dir) {
		Set<String> sf = files.get(dir);
		if (null == sf) {
			files.put(dir, sf = new LinkedHashSet<>());
		}

		String current = dir;
		String parent = getParent(current);
		while (null != parent) {
			Set<String> pf = files.get(parent);
			if (null == pf) {
				files.put(parent, pf = new LinkedHashSet<>());
			}
			pf.add(current);
			current = parent;
			parent = getParent(parent);
		}
		return sf;
	}

	Map<String, ZipEntry> 	fileEntryMap;


	Map<String, Set<String>> directoryFileMap;
	Set<String> 			 fileSet;
	@SuppressWarnings("unchecked")
	ZipResources loadFiles() {
		if (null == this.file)
			return null;
		if (null == directoryFileMap) {
			Map<String, Set<String>> directoryFileMap = new LinkedHashMap<>();
			Map<String, ZipEntry>    fileEntryMap     = new LinkedHashMap<>();
			Enumeration<ZipEntry> enums = (Enumeration<ZipEntry>) entries();
			while (enums.hasMoreElements()) {
				ZipEntry entry    = enums.nextElement();
				String originPath = entry.getName();
				String formatPath = Zips.formatPath(originPath);
				if (formatPath.startsWith(base)) {
					formatPath = formatPath.substring(base.length(), formatPath.length());

					fileEntryMap.put(formatPath, entry);

					if (pathIsDirectory(formatPath)) {
						loadFilesMkdirs(directoryFileMap, formatPath);
					} else {
						String parent = getParent(formatPath);
						Set<String> sf = loadFilesMkdirs(directoryFileMap, parent) ;

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
	public Set<String> allPaths() {
		return loadFiles().fileSet;
	}


	public String[] listRootPaths() {
		return listPaths(ROOT_DIRECTORY);
	}
	public String[] listPaths(String dir) {
		if (null == this.file)
			return null;
		String d = Zips.formatPath(dir);
		Set<String>    s = loadFiles().directoryFileMap.get(d);
		return null == s ?
				EMPTY_STRING_ARRAY:
				s.toArray(EMPTY_STRING_ARRAY);
	}


	/**
	 * only filter file, no dir
	 */
	public String[] filterFile(IFilter<String> filter) {
		List<String> result = new ArrayList<>();
		Map<String, Set<String>> stringSetMap = loadFiles().directoryFileMap;
		for (String dir : stringSetMap.keySet()) {
			if (isRootPath(dir)){
				Set<String> files = stringSetMap.get(dir);
				for (String s : files) {
					if (!pathIsDirectory(s)) {
						if (filter.next(s)) {
							result.add(s);
						}
					}
				}
			} else {
				Set<String> files = stringSetMap.get(dir);
				for (String s : files) {
					if (!pathIsDirectory(s)) {
						if (filter.next(s)) {
							result.add(s);
						}
					}
				}
			}
		}
		return result.toArray(EMPTY_STRING_ARRAY);
	}




	public static void main(String[] args) {
		String pathname ;
		pathname= "C:\\Users\\78492\\Desktop\\20230706表格处理\\20230725权责清单.zip";
		try (ZipResources zipResources = new ZipResources(new File(pathname), Charset.forName("GBK"))) {
			String[] a = zipResources.filterFile(new IFilter<String>() {
				@Override
				public boolean next(String param) {
					return true;
				}
			});
			System.out.println(Arrays.toString(a));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
