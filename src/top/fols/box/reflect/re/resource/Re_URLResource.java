package top.fols.box.reflect.re.resource;

import top.fols.atri.io.util.Streams;
import top.fols.atri.net.URLs;
import top.fols.box.reflect.re.Re_NativeClassLoader;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Objects;

public class Re_URLResource implements Re_IReResource {
    URL url;
    Charset contentCharset;
    public Re_URLResource(URL url) {
        this(url, null);
    }
    public Re_URLResource(URL url, Charset contentCharset) {
        this.url = url;
        this.contentCharset(contentCharset);
    }

    public void contentCharset(Charset charset) {
        this.contentCharset = null == charset ? DEFAULT_CONTENT_CHARSET: charset;
    }
    @Override
    public Charset contentCharset() {
        return this.contentCharset;
    }

    public URLConnection openRelativePathURLConnection(String path) {
        return URLs.openRelativeURLConnection(url, path);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Re_URLResource)) return false;

        Re_URLResource that = (Re_URLResource) o;

        if (!Objects.equals(url, that.url)) return false;
        if (!Objects.equals(contentCharset, that.contentCharset))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (contentCharset != null ? contentCharset.hashCode() : 0);
        return result;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public Re_IReResourceFile getFileResource(String relativePath) {
        URLConnection urlConnection = openRelativePathURLConnection(relativePath);
        if (null !=   urlConnection) {
            URL absolutePath = urlConnection.getURL();
            try {
                return new TargetFile(this,
                        absolutePath, relativePath,
                        relativePath,
                        contentCharset);
            } finally {
                Streams.close(urlConnection);
            }
        }
        return null;
    }

    @Override
    public Long getFileSize(String relativePath) {
        URLConnection urlConnection = openRelativePathURLConnection(relativePath);
        if (null !=   urlConnection) {
            try {
                return urlConnection.getContentLengthLong();
            } finally {
                Streams.close(urlConnection);
            }
        }
        return null;
    }

    @Override
    public Long getFileLastModified(String relativePath) {
        URLConnection urlConnection = openRelativePathURLConnection(relativePath);
        if (null !=   urlConnection) {
            try {
                return urlConnection.getLastModified();
            } finally {
                Streams.close(urlConnection);
            }
        }
        return null;
    }

    @Override
    public InputStream getFileInputStream(String relativePath) {
        URLConnection urlConnection = openRelativePathURLConnection(relativePath);
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
    public Re_IReResourceFile findClassResource(String className) {
        String relativePath = Re_NativeClassLoader.classNameToPath(className, URLs.PATH_SEPARATOR_CHAR);
        if (null == relativePath)
            return null;
        URLConnection urlConnection = openRelativePathURLConnection(relativePath);
        if (null !=   urlConnection) {
            try {
                URL absolutePath = urlConnection.getURL();
                return new TargetFile(this,
                        absolutePath, relativePath,
                        relativePath,
                        contentCharset);
            } catch (Exception ignored) {
            } finally {
                Streams.close(urlConnection);
            }
        }
        return null;
    }



    public static class TargetFile extends Re_IReResourceFile {
        String name;
        String relativePth;
        Charset charset;

        TargetFile(Re_URLResource resource,  URL url,
                   String name,
                   String relativePath,
                   Charset charset) {
            super(resource, url);

            this.name = name;
            this.relativePth = relativePath;
            this.charset = charset;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String getRelativePth() {
            return relativePth;
        }

        @Override
        public Charset charset() {
            return charset;
        }

    }
}
