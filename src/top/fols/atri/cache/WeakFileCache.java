package top.fols.atri.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.atri.interfaces.annotations.ThreadSafe;
import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.atri.io.BytesInputStreams;
import top.fols.atri.io.file.Filex;
import top.fols.atri.io.util.Streams;
import top.fols.atri.lang.Objects;
import top.fols.atri.net.URLs;
import top.fols.atri.util.DoubleLinkedList;
import top.fols.box.net.URLConnections;

@ThreadSafe
@SuppressWarnings("UnnecessaryInterfaceModifier")
public class WeakFileCache<T> implements IReleasable {
    @Override
    public boolean release() {
        clearCache();
        return true;
    }
    @Override
    public boolean released() {
        return getUseCacheSize() == 0;
    }


//	public static void test() throws IOException	{
//		String file1 = "/storage/emulated/0/360安全云盘下载的文件/我的文件/Programming/JAVA/fols/folsTool/src/top/fols/atri/util/DoubleLinkedList.java";
//		String file2 = "/storage/emulated/0/360安全云盘下载的文件/我的文件/Programming/JAVA/fols/folsTool/src/top/fols/atri/io/BytesInputStreams.java";
//		String file3 = "/storage/emulated/0/360安全云盘下载的文件/我的文件/Programming/JAVA/fols/folsTool/src/top/fols/atri/io/StringWriters.java";
//		WeakFileCache fc = new WeakFileCache(createDirectoryIFileCache(new File("/")),
//											 WeakFileCache.CACHE_TIME());
//
//		System.out.println(System.currentTimeMillis());
//		for (int i =0; i < 1_0000_0000;i++) {
//			fc.getCacheBytesOrReadBytes(file2);
//		}
//		System.out.println(System.currentTimeMillis());
//		fc.clearCache();
//
//
//		fc.getCacheBytesOrReadBytes(file1);
//		fc.getCacheBytesOrReadBytes(file2);
//		fc.getCacheBytesOrReadBytes(file3);
//
//		System.out.println(Arrays.toString(fc.getCaches()));
//		fc.cleanExpiredCacheFromSize(10000000);
//		System.out.println(Arrays.toString(fc.getCaches()));
//
//		Times.sleep(1000);
//
//		fc.cleanExpiredCacheFromSize(1);
//		System.out.println(Arrays.toString(fc.getCaches()));
//
//		fc.getCacheAsStreamOrOpenStream(file2);
//
//		fc.cleanExpiredCacheFromSize(1);
//		System.out.println(Arrays.toString(fc.getCaches()));
//
//		fc.cleanExpiredCacheFromSize(1);
//		System.out.println(Arrays.toString(fc.getCaches()));
//
//		//[1+2+3] [1+2+3] [1+2] [2] [2]
//
//		System.out.println("==============");
//		System.out.println(fc.getVersion(file1));
//		Times.sleep(1000);
//		System.out.println(fc.getVersion(file1));
//		System.out.println(new File(file1).setLastModified(System.currentTimeMillis()));
//
//	}

    public interface IFileCache<F> {
        public F findFile(String path);

        public Version      getFileVersion(F file);
        public long         getFileSize(F file);

        public InputStream  getFileInputStream(F file) throws IOException;
        public OutputStream getFileOutputStream(String path, Boolean append) throws IOException;
    }
    public interface ITimer {
        public long time();
        public long millisToTime(long millis);
        public long timeToMillis(long time);
    }



    public static ITimer SYSTEM_NANO_TIME()  {
        return new ITimer() {
            @Override
            public long time() {
                return System.nanoTime();//no sync
            }
            @Override
            public long millisToTime(long millis) {
                return millis * 1000000L;
            }
            @Override
            public long timeToMillis(long time) {
                return time / 1000000L;
            }
        };
    }







    public static class Version {
        long lastModified;
        long length;

        protected Version() {}

        public Version(long lastModified, long length) {
            this.lastModified = lastModified;
            this.length = length;
        }

        @Override
        public int hashCode() {
            // TODO: Implement this method
            return (int) (lastModified + length);
        }

        @Override
        public boolean equals(Object obj) {
            // TODO: Implement this method
            if (!(obj instanceof Version)) return false;
            Version c = (WeakFileCache.Version) obj;
            return
                    this.lastModified == c.lastModified &&
                            this.length       == c.length;
        }
    }



    static class Cache {
        public final String  filePath; public final byte[]  fileData;

        public volatile Version version;

        public volatile long lastReadTime;

        private DoubleLinkedList.Element<Cache> linkedElement;

        public Cache(String path, byte[] bytes) {
            this.filePath = path;
            this.fileData = bytes;
        }

        @Override
        public String toString() {
            // TODO: Implement this method
            return filePath + "[" + version + "]";
        }
    }


    public static WeakFileCache<File> createDirectoryFileCache(File dir) {
        return new WeakFileCache<>(createDirectoryFileCacheInterface(dir));
    }
    public static DirectoryIFileCache createDirectoryFileCacheInterface(File dir) {
        return new DirectoryIFileCache(dir);
    }
    public static class DirectoryIFileCache implements IFileCache<File> {
        File directory;
        public DirectoryIFileCache(final File dir) {
            this.directory = Filex.newFileInstance(dir);
        }

        public File getDirectory() {
            return directory;
        }

        @Override
        public File findFile(String path) {
            // TODO: Implement this method
            File f = new File(directory, path);
            return f.canRead() ? f : null;
        }

        @Override
        public Version getFileVersion(@NotNull File f) {
            // TODO: Implement this method
            return new Version(f.lastModified(), f.length());
        }

        @Override
        public long getFileSize(@NotNull File f) {
            // TODO: Implement this method
            return f.length();
        }

        @Override
        public InputStream getFileInputStream(@NotNull File f) throws FileNotFoundException {
            // TODO: Implement this method
            return new FileInputStream(f);
        }

        @Override
        public OutputStream getFileOutputStream(String path, Boolean append) throws IOException {
            // TODO: Implement this method
            File f = new File(directory, path);
            return new FileOutputStream(f, Objects.nvl(append, false));
        }
    }


    public static WeakFileCache<URLIFileCache.URLFile> createURLFileCache(URL url) {
        WeakFileCache<URLIFileCache.URLFile> urlFileWeakFileCache = new WeakFileCache<>(createURLFileCacheInterface(url));
        urlFileWeakFileCache.setCacheMaxTimeFromMillis(URLConnections.DEFAULT_READ_TIMEOUT);
        return urlFileWeakFileCache;
    }
    public static WeakFileCache<URLIFileCache.URLFile> createURLFileCache(URL url, long cacheMaxTime, TimeUnit timeUnit) {
        WeakFileCache<URLIFileCache.URLFile> urlFileWeakFileCache = new WeakFileCache<>(createURLFileCacheInterface(url));
        urlFileWeakFileCache.setCacheMaxTimeFromMillis(timeUnit.toMillis(cacheMaxTime));
        return urlFileWeakFileCache;
    }
    public static URLIFileCache createURLFileCacheInterface(URL url) {
        return new URLIFileCache(url);
    }
    public static class URLIFileCache implements IFileCache<URLIFileCache.URLFile> {
        URL url;
        public URLIFileCache(final URL url) {
            this.url = url;
        }

        @Nullable
        URLConnection openRelativePathURLConnection(String path) {
            return URLs.openRelativeURLConnection(url, path);
        }

        public static class URLFile {
            URL file;
            public URLFile(URL file) {
                this.file = file;
            }

            public URLConnection open() {
                try {
                    return getURL().openConnection();
                } catch (IOException e) {
                    return null;
                }
            }
            public URL getURL(){
                return file;
            }
        }

        @Override
        public URLFile findFile(String path) {
            // TODO: Implement this method
            URLConnection  urlConnection = openRelativePathURLConnection(path);
            if (null !=    urlConnection) {
                try {
                    return new URLFile(urlConnection.getURL());
                } finally {
                    Streams.close(urlConnection);
                }
            }
            return null;
        }

        @Override
        public Version getFileVersion(@NotNull URLFile f) {
            // TODO: Implement this method
            URLConnection urlConnection = f.open();
            if (null !=   urlConnection) {
                try {
                    return new Version(urlConnection.getLastModified(), urlConnection.getContentLength());
                } catch (Exception ignored) {
                } finally {
                    Streams.close(urlConnection);
                }
            }
            return null;
        }

        @Override
        public long getFileSize(@NotNull URLFile f) {
            // TODO: Implement this method
            URLConnection urlConnection = f.open();
            if (null !=   urlConnection) {
                try {
                    return urlConnection.getContentLengthLong();
                } catch (Exception ignored) {
                } finally {
                    Streams.close(urlConnection);
                }
            }
            return -1;
        }

        @Override
        public InputStream getFileInputStream(@NotNull URLFile f) {
            // TODO: Implement this method
            URLConnection urlConnection = f.open();
            if (null !=   urlConnection) {
                try {
                    return urlConnection.getInputStream();
                } catch (Exception ignored) {
                    Streams.close(urlConnection);
                }
            }
            return null;
        }

        @Override
        public OutputStream getFileOutputStream(@NotNull String path, Boolean append) throws IOException {
            // TODO: Implement this method
            if (null != append) {
                throw new UnsupportedOperationException("append is not supported: " + getClass().getName());
            }
            URLConnection urlConnection = openRelativePathURLConnection(path);
            if (null !=   urlConnection) {
                try {
                    return urlConnection.getOutputStream();
                } catch (Exception ignored) {
                    Streams.close(urlConnection);
                }
            }
            return null;
        }
    }



    //	public static final long DEFAULT_AUTO_CLEAN_EXPIRED_CACHE_TIME = DEFAULT_CACHE_MAX_TIME * 10L;

    public static final long UNLIMIT_SIZE = -1;

    public static final long DEFAULT_CACHE_MAX_TIME = 100L;
    public static final long DEFAULT_CACHE_MAX_SIZE = UNLIMIT_SIZE;


    protected Cache[] getCaches()	{ return this.currentCacheLinked.toArray(new Cache[]{}); }



    private IFileCache<T> ifc;
    private ITimer        itm;
    private long cacheMaxTime;
    private long cacheMaxSize;
//	private long cacheAutoCleanExpiredTime  = DEFAULT_AUTO_CLEAN_EXPIRED_CACHE_TIME;

    private long                          currentCacheSize   = 0;
    private final Map<String, Cache>      currentCache       = new HashMap<>();
    private final DoubleLinkedList<Cache> currentCacheLinked = new DoubleLinkedList<>();






    public WeakFileCache(IFileCache<T> ifc) {
        this(ifc, null);
    }
    public WeakFileCache(IFileCache<T> ifc, ITimer timer) {
        this.ifc = Objects.requireNonNull(ifc, "interface");
        this.itm = null == timer ? SYSTEM_NANO_TIME() : timer;

        this.setCacheMaxTimeFromMillis(DEFAULT_CACHE_MAX_TIME);
        this.setCacheMaxSize(DEFAULT_CACHE_MAX_SIZE);
    }


    public long getCacheMaxTimeToMillis() {
        return itm.timeToMillis(cacheMaxTime);
    }
    public void setCacheMaxTimeFromMillis(long millis) {
        if (millis >= 0) {
            long newTime =  itm.millisToTime(millis);
            if  (newTime < 0) {
                throw new IllegalArgumentException("overflow, newTime=" + newTime);
            }
            this.cacheMaxTime = newTime;
        } else {
            throw new IllegalArgumentException("millis=" + millis);
        }
    }

//  he is not suitable for high-frequency interview
//	public long getAutoCleanExpiredCacheTime() {
//		return timeToMillis(cacheAutoCleanExpiredTime);
//	}
//	public void setAutoCleanExpiredCacheTime(long millis){
//		if (millis >= 0) {
//			this.cacheAutoCleanExpiredTime = millisToTime(millis);
//		} else {
//			throw new IllegalArgumentException("millis=" + millis);
//		}
//	}
//


    public long getCacheMaxSize() {
        return cacheMaxSize;
    }
    public void setCacheMaxSize(long size) {
        if (size >= 0) {
            this.cacheMaxSize = size;
        } else if (size == UNLIMIT_SIZE) {
            this.cacheMaxSize = size;
        } else {
            throw new IllegalArgumentException("size=" + size);
        }
    }






    public int  getUseCacheCount() { return currentCache.size(); }
    public long getUseCacheSize()  { return currentCacheSize;    }
    public void clearCache() {
        synchronized (this.currentCache) {
            this.currentCacheSize = 0;
            this.currentCache.clear();
            this.currentCacheLinked.clear();
        }
    }
    public void removeCache(String path) {
        synchronized (this.currentCache) {
            removeCache0(getCache0(path));
        }
    }





    /**
     * the returned stream is not necessarily a cache stream
     * if you keep getting new files, it will slow down
     * @return BytesInputStreams or InputStream
     */
    public InputStream getCacheAsStreamOrOpenStream(String path) throws IOException {
        Object ci = openCacheOrInputStream(path);
        if (ci instanceof Cache)
            return new BytesInputStreams(((Cache)ci).fileData);
        else if (ci instanceof InputStream)
            return (InputStream) ci;
        else
            return null;
    }
    public byte[]     getCacheAsBytesOrOpenStreamBytes(String path) throws IOException {
        Object ci = openCacheOrInputStream(path);
        if (ci instanceof Cache)
            return ((Cache)ci).fileData.clone();
        else if (ci instanceof InputStream)
            return readBytesAndClose((InputStream) ci);
        else
            return null;
    }

    /*
     * If it exists in the cache, it will not be cloned
     */
    protected byte[] getCacheBytesOrReadBytes(String path) throws IOException {
        Object ci = openCacheOrInputStream(path);
        if (ci instanceof Cache)
            return ((Cache)ci).fileData; //if it exists in the cache, it will not be cloned
        else if (ci instanceof InputStream)
            return readBytesAndClose((InputStream) ci);
        else
            return null;
    }


    public Version getVersion(String path) throws IOException {
        Object ci = openCacheOrInputStream(path);
        if (ci instanceof Cache) {
            return ((Cache)ci).version;
        } else if (ci instanceof InputStream) {
            T file = ifc.findFile(path);
            return null == file ? null : ifc.getFileVersion(file);
        } else {
            return null;
        }

    }


    public static byte[] readBytesAndClose(InputStream input) throws IOException {
        try {
            return Streams.toBytes(input);
        } finally {
            Streams.close(input);
        }
    }

//		DoubleLinkedList.Element<Cache> last = this.currentCacheLinked.getLast();
//		if (null != last) {
//			Cache  c = last.content();
//			if  (time - c.lastReadTime >= cacheAutoCleanExpiredTime) {
//				this.__linkedCleanExpiredCacheFromSize(UNLIMIT_SIZE);
//			}
//		}

    protected Object openCacheOrInputStream(String path) throws IOException {
        T file; Version version; Cache cache;
        if (null != (cache = getCache0(path))) {
            long time = itm.time();
            if  (time - cache.lastReadTime >= cacheMaxTime) {
                version = null == (file = ifc.findFile(path)) ? null : ifc.getFileVersion(file);
                if (null == version) {
                    removeCache0(cache);
                    return null;// not found file
                }
                //version equals
                if (version.equals(cache.version)) {
                    accessCache0(cache, time);
                    return cache;
                }
            } else {
                //within cache validity
                return cache;
            }
        }
        return openCacheOrInputStream0(path);
    }
    protected Object openCacheOrInputStream0(String path) throws IOException {
        T file; Version version; Cache cache;
        synchronized (this.currentCache) {
            if (null != (cache = getCache0(path))) {
                long time = itm.time();
                if  (time - cache.lastReadTime >= cacheMaxTime) {
                    version = null == (file = ifc.findFile(path)) ? null : ifc.getFileVersion(file);
                    if (null == version) {
                        removeCache0(cache);
                        return null;// not found file
                    }
                    //version equals
                    if (version.equals(cache.version)) {
                        accessCache0(cache, time);
                        return cache;
                    }
                } else {
                    //within cache validity
                    return cache;
                }
            } else if (null == (file = ifc.findFile(path))) {
                return null;
            } else {
                version = null;
            }
            removeCache0(cache);

            InputStream stream = ifc.getFileInputStream(file);
            //unlimited size
            if (cacheMaxSize != UNLIMIT_SIZE) {
                long size = ifc.getFileSize(file);
                if  (size < 0) {
                    return stream;
                }
                //cannot cache even if the cache is empty
                if  (size > cacheMaxSize) {
                    return stream;
                }
                //cache full, clear cache
                if (currentCacheSize + size > cacheMaxSize) {
                    if (!cleanExpiredCacheFromSize(size)) {//no extra space
                        return stream;
                    }
                }
            }

            if (null ==  version) {
                if (null == (version = ifc.getFileVersion(file)))
                    return stream;
            }

            Cache newCache;
            try {
                newCache = new Cache(path, Streams.toBytes(stream));
                newCache.lastReadTime = itm.time();
                newCache.version = version;
            } finally {
                Streams.close(stream);
            }
            addCache0(newCache);
            return newCache;
        }
    }

    public void save(String path, byte[] bytes) throws IOException {
        synchronized (this.currentCache) {
            try {
                OutputStream os = null;
                try {
                    os = ifc.getFileOutputStream(path, null);
                    os.write(bytes);
                    os.flush();
                } finally {
                    Streams.close(os);
                }
            } finally {
                removeCache(path);
            }
        }
    }





    public boolean cleanExpiredCacheFromSize(long size) {
        synchronized (this.currentCache) {
            //sort by time
            if (size == UNLIMIT_SIZE) {
                for (DoubleLinkedList.Element<Cache> current = this.currentCacheLinked.getLast(); null != current;) {
                    DoubleLinkedList.Element<Cache> prev = (DoubleLinkedList.Element<WeakFileCache.Cache>) current.getPrev();
                    long time = itm.time();
                    Cache c = current.content();
                    //it is unnecessary to empty the cache without expiration
                    if (time - c.lastReadTime < cacheMaxTime)
                        break;

                    removeCache0(c);

                    current = prev;
                }
                return true;
            } else {
                for (DoubleLinkedList.Element<Cache> current = this.currentCacheLinked.getLast(); null != current;) {
                    DoubleLinkedList.Element<Cache> prev = (DoubleLinkedList.Element<WeakFileCache.Cache>) current.getPrev();
                    long time = itm.time();
                    Cache c = current.content();
                    //it is unnecessary to empty the cache without expiration
                    if (time - c.lastReadTime < cacheMaxTime)
                        break;

                    removeCache0(c);

                    //there is already enough space
                    if ((size -= c.fileData.length) <= 0)
                        return true;

                    current = prev;
                }
                return false;
            }
        }
    }







    protected void addCacheBefore(Cache cache) {}
    protected void removeCacheBefore(Cache cache) {}
//    protected void accessCacheBefore(Cache cache) {}


    private Cache getCache0(String path) {
        return currentCache.get(path);
    }
    private void addCache0(Cache newCache) {
        synchronized (this.currentCache) {
            try {
                addCacheBefore(newCache);

                __mapAddCache(newCache);
                __linkedAddCache(newCache);
            } catch (OutOfMemoryError e) {
                __mapRemoveCache(newCache);
                __linkedRemoveCache(newCache);
                throw e;
            }
        }
    }
    private void accessCache0(Cache cache, long accessTime) {
        synchronized (this.currentCache) {
//            accessCacheBefore(cache);
            cache.lastReadTime = accessTime;
            __linkedAccessCache(cache);
        }
    }
    private void removeCache0(Cache cache) {
        if (null != cache) {
            synchronized (this.currentCache) {
                removeCacheBefore(cache);

                __mapRemoveCache(cache);
                __linkedRemoveCache(cache);
            }
        }
    }





    private void __mapAddCache(Cache cache) {
        currentCache.put(cache.filePath, cache);
        currentCacheSize += cache.fileData.length;
    }
    private void __mapRemoveCache(Cache cache) {
        currentCache.remove(cache.filePath);
        currentCacheSize -= cache.fileData.length;
    }

    private void __linkedAddCache(Cache cache) {
        synchronized (this.currentCache) {
            this.currentCacheLinked.addFirst(cache.linkedElement = new DoubleLinkedList.Element<>(cache));
        }
    }
    private void __linkedAccessCache(Cache cache) {
        synchronized (this.currentCache) {
            if (null != cache.linkedElement) {
                this.currentCacheLinked.addFirst(cache.linkedElement);
            }
        }
    }
    private void __linkedRemoveCache(Cache cache) {
        synchronized (this.currentCache) {
            this.currentCacheLinked.remove(cache.linkedElement);
            cache.linkedElement = null;
        }
    }

}
