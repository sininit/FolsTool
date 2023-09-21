package top.fols.atri.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.IdentityHashMap;
import java.util.Map;

import top.fols.atri.interfaces.annotations.ThreadSafe;
import top.fols.atri.io.util.Streams;
import top.fols.atri.io.file.Filex;
import top.fols.atri.lang.Finals;

@ThreadSafe
public class WeakFileCacheDirectoryAsString extends WeakFileCache<File> {
	private final File directory;
	private final Charset charset;

	public WeakFileCacheDirectoryAsString(String dir, Charset charset) {
		this(Filex.newFileInstance(dir), charset);
	}
	public WeakFileCacheDirectoryAsString(File dir, Charset charset) {
		super(createDirectoryFileCacheInterface(dir));

		this.directory = dir;
		this.charset = null == charset ? Finals.Charsets.UTF_8 : charset;
	}

	public File getDirectory() {
		return directory;
	}


	static class LastString {
		byte[]  lastBytes;

		volatile String  lastContent;
	}
	final Map<Cache, LastString> cacheMap = new IdentityHashMap<>();



	public String getCacheStringOrNewString(String path) throws IOException {
		Object  ci = super.openCacheOrInputStream(path);
		if (ci instanceof Cache) {
			Cache cache = (Cache) ci;
			LastString  ls = cacheMap.get(cache);
			if (null == ls) {
				return new String(cache.fileData, charset);
			} else {
				if (ls.lastBytes == cache.fileData) {
					return ls.lastContent;
				}
				return new String(cache.fileData, charset);
			}
		} else if (ci instanceof InputStream) {
			byte[] bytes = Streams.toBytes((InputStream)ci);
			return new String(bytes, charset);
		} else {
			return null;
		}
	}


	@Override
	protected void addCacheBefore(Cache cache) {
		// TODO: Implement this method
		synchronized (this.cacheMap) {
			try {
				LastString newLastString  = new LastString();
				newLastString.lastBytes   = cache.fileData;
				newLastString.lastContent = new String(cache.fileData, charset);

				this.cacheMap.put(cache, newLastString);
			} catch (OutOfMemoryError e) {
				this.cacheMap.remove(cache);

				throw e;
			}
		}
	}


	@Override
	protected void removeCacheBefore(Cache cache) {
		// TODO: Implement this method
		synchronized (this.cacheMap) {
			this.cacheMap.remove(cache);
		}
	}
}

