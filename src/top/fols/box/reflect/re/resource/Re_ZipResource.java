package top.fols.box.reflect.re.resource;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import top.fols.atri.io.file.Filez;
import top.fols.atri.io.util.Streams;
import top.fols.atri.io.file.Filex;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.box.reflect.re.Re_NativeClassLoader;
import top.fols.box.util.Jars;
import top.fols.box.util.Zips;

/**
 * .
 * .. 
 */
@SuppressWarnings({"UnnecessaryLocalVariable", "SpellCheckingInspection"})
public class Re_ZipResource implements Closeable, Re_IReResource {
	static final String ROOT_DIRECTORY = Zips.ROOT_DIRECTORY;


	File    file; File absolute;
	String  base;
	Charset zfilecharset;
	ZipFile zfile;


	Charset contentCharset;

	public Re_ZipResource(File zip) {
		this(zip, null);
	}
	public Re_ZipResource(File zip,
						  String  innerBaseDirectory) {
		this(zip, null, innerBaseDirectory);
	}
	public Re_ZipResource(File zip, Charset zipCharset,
						  String innerBaseDirectory) {
		try {
			zip = (null == zip || !zip.isFile() || !zip.canRead()) ? null : zip;
			this.file   =  zip;
			if (null != zip) {
				this.absolute = Filez.wrap(zip).getCanonical().innerFile();
			}
			this.base    = Objects.isEmpty(innerBaseDirectory) ? ROOT_DIRECTORY : Zips.formatPathToDirectory(innerBaseDirectory);
			this.zfilecharset = null == zipCharset ? Finals.Charsets.UTF_8: zipCharset;

			if (null != zip) {
				this.zfile = new ZipFile(this.file, ZipFile.OPEN_READ, this.zfilecharset);
			}
			this.contentCharset(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void contentCharset(Charset charset) {
		this.contentCharset = null == charset ?DEFAULT_CONTENT_CHARSET: charset;
	}
	@Override
	public Charset contentCharset() {
		// TODO: Implement this method
		return contentCharset;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Re_ZipResource)) return false;

		Re_ZipResource that = (Re_ZipResource) o;

		if (!java.util.Objects.equals(file, that.file)) return false;
		if (!java.util.Objects.equals(base, that.base)) return false;
		if (!java.util.Objects.equals(zfilecharset, that.zfilecharset)) return false;
		if (!java.util.Objects.equals(contentCharset, that.contentCharset))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = file != null ? file.hashCode() : 0;
		result = 31 * result + (base != null ? base.hashCode() : 0);
		result = 31 * result + (zfilecharset != null ? zfilecharset.hashCode() : 0);
		result = 31 * result + (contentCharset != null ? contentCharset.hashCode() : 0);
		return result;
	}

	URL url;
	@Override
	public URL getURL() {
		if (null == this.file)
			return null;
		if (null != url)
			return  url;
		try {
			return this.url = Jars.getJarURL(file);
		} catch (MalformedURLException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	URL getURL(String path) {
		URL url = getURL();
		if (url == null)
			return null;
		try {
			return new URL(url, Zips.formatPathToFile(path));
		} catch (MalformedURLException e) {
			throw new UnsupportedOperationException(e);
		}
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

	@Override
	public void close() {
		// TODO: Implement this method
		Streams.close(this.zfile);
	}

	public ZipEntry getEntry(String subpath) {
		if (null == this.file)
			return null;
		String s       = getEntryPath(subpath);
		return zfile.getEntry(s);
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

	Map<String, Set<String>> files;
	@SuppressWarnings("unchecked")
	Map<String, Set<String>> loadFiles() {
		if (null == this.file)
			return null;
		if (null == files) {
			Map<String, Set<String>> loads = new HashMap<>();
			Enumeration<ZipEntry> enums = (Enumeration<ZipEntry>) entries();
			while (enums.hasMoreElements()) {
				ZipEntry entry    = enums.nextElement();
				String originPath = entry.getName();
				String subPath = Zips.formatPath(originPath);
				if (subPath.startsWith(base)) {
					subPath = subPath.substring(base.length(), subPath.length());
					String parent = Filex.getParent(subPath);
					if (null == parent) {
						parent = ROOT_DIRECTORY;
					}

					Set<String> sf = loads.get(parent);
					if (null == sf) {
						loads.put(parent, sf = new HashSet<>());
					}

					if (!Objects.isEmpty(subPath)) {
						sf.add(subPath);
					}

				}
			}

			this.files = loads;
		}
		return this.files;
	}

	public String[] listFilePaths(String dir) {
		if (null == this.file)
			return null;
		String d = Zips.formatPath(dir);
		Set<String>    s = loadFiles().get(d);
		return null == s ? 
			Finals.EMPTY_STRING_ARRAY:
			s.toArray(Finals.EMPTY_STRING_ARRAY);
	}
	public String[] listRootPaths() {
		return listFilePaths(ROOT_DIRECTORY);
	}





	@Override
	public Re_IReResourceFile getFileResource(String relativePath) {
		// TODO: Implement this method
		if (null == this.file)
			return null;
		String entryPath = getEntryPath(relativePath);
		ZipEntry entry = zfile.getEntry(entryPath);
		if (null == entry) {
			return null;
		}
		URL url = getURL(relativePath);
		return new Re_ZipResourceFile(this,
				url, relativePath,
				entryPath, relativePath,
				contentCharset);
	}
	@Override
	public Long getFileSize(String relativePath) {
		if (null == this.file)
			return null;
		String entryPath = getEntryPath(relativePath);
		ZipEntry entry = zfile.getEntry(entryPath);
		if (null == entry) {
			return null;
		}
		return entry.getSize();
	}
	@Override
	public Long getFileLastModified(String relativePath) {
		if (null == this.file)
			return null;
		String entryPath = getEntryPath(relativePath);
		ZipEntry entry = zfile.getEntry(entryPath);
		if (null == entry) {
			return null;
		}
		return entry.getTime();
	}
	@Override
	public InputStream getFileInputStream(String relativePath) {
		if (null == this.file)
			return null;
		String entryPath = getEntryPath(relativePath);
		ZipEntry entry = zfile.getEntry(entryPath);
		if (null == entry) {
			return null;
		}
		return getInputStream(entry);
	}



	@Override
	public Re_IReResourceFile findClassResource(String className) {
		// TODO: Implement this method
		if (null == this.file)
			return null;
		String relativePath = Re_NativeClassLoader.classNameToPath(className, Zips.SEPARATOR);
		if (null == relativePath)
			return null;
		String entryPath = getEntryPath(relativePath);
		ZipEntry entry = zfile.getEntry(entryPath);
		if (null == entry) {
			return null;
		}
		URL url = getURL(relativePath);
		return new Re_ZipResourceFile(this,
				url, relativePath,
				entryPath, relativePath,
				contentCharset);
	}



	public static class Re_ZipResourceFile extends Re_IReResourceFile {
		String name;
		String entryPath;
		String relativePath;
		Charset charset;

		Re_ZipResourceFile(Re_ZipResource resource, URL url,
						   String name,
						   String entryPath,
						   String relativePath,
						   Charset charset) {
			super(resource, url);

			this.name = name;
			this.entryPath = entryPath;
			this.relativePath = relativePath;
			this.charset = charset;
		}

		@Override
		public Re_ZipResource getResources() {
			return (Re_ZipResource) super.getResources();
		}

		@Override
		public String name() {
			return name;
		}


		@Override
		public String getRelativePth() {
			return relativePath;
		}

		@Override
		public Charset charset() {
			return charset;
		}



		ZipEntry getEntry(){ return getResources().zfile.getEntry(entryPath); }
		@Override
		public InputStream asStream() {
			ZipEntry entry = getEntry();
			return null == entry ? null : getResources().getInputStream(entry);
		}
	}
	
}
