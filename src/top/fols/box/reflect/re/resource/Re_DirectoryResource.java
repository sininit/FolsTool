package top.fols.box.reflect.re.resource;

import top.fols.atri.io.file.Filex;
import top.fols.atri.io.file.Filez;
import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.atri.lang.Objects;
import top.fols.atri.net.URLs;
import top.fols.box.reflect.re.Re_NativeClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class Re_DirectoryResource implements Re_IReResource {
    File directory; File absolute;
    Charset contentCharset;

    /**
     * @param directory default tip UTF_8
     */
    public Re_DirectoryResource(File directory) {
        this(directory, null);
    }
    public Re_DirectoryResource(File directory, Charset contentCharset) {
        directory = (null == directory || !directory.isDirectory() || !directory.canRead()) ? null : directory;
        this.directory    =  directory;
        if (null != directory) {
            this.absolute = Filez.wrap(directory).getCanonical().innerFile();
        }
        this.contentCharset(contentCharset);
    }


    public void contentCharset(Charset charset) {
        this.contentCharset = null == charset ?DEFAULT_CONTENT_CHARSET: charset;
    }
    @Override
    public Charset contentCharset() {
        return this.contentCharset;
    }

    URL url;
    @Override
    public URL getURL() {
        if (null == this.directory)
            return null;
        if (null != url)
            return  url;
        try {
            return this.url = absolute.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    URL getURL(String path) {
        URL url = getURL();
        if (url == null)
            return null;
        try {
            return new URL(url, Filex.convertFileSeparator(path, URLs.PATH_SEPARATOR_CHAR));
        } catch (MalformedURLException e) {
            throw new UnsupportedOperationException(e);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Re_DirectoryResource))
            return false;

        Re_DirectoryResource other = (Re_DirectoryResource) obj;
        return Filez.wrap(absolute).equals(Filez.wrap(other.absolute)) &&
               Objects.equals(contentCharset, other.contentCharset);
    }



    @Nullable
    @Override
    public Re_IReResourceFile getFileResource(final String relativePath) {
        if (null == this.directory)
            return null;
        final File absolute = new File(this.directory, relativePath);
        if (absolute.exists()) {
            return new TargetFile(this,
                    getURL(relativePath), relativePath,
                    absolute.getPath(),
                                    relativePath,
                                    contentCharset);
        }
        return null;
    }
    @Override
    public Long getFileSize(String relativePath) {
        if (null == this.directory)
            return null;
        final File absolute = new File(this.directory, relativePath);
        if (absolute.exists()) {
            return absolute.length();
        }
        return null;
    }
    @Override
    public Long getFileLastModified(String relativePath) {
        if (null == this.directory)
            return null;
        final File absolute = new File(this.directory, relativePath);
        if (absolute.exists()) {
            return absolute.lastModified();
        }
        return null;
    }
    @Override
    public InputStream getFileInputStream(String relativePath) {
        if (null == this.directory)
            return null;
        final File absolute = new File(this.directory, relativePath);
        try {
            return new FileInputStream(absolute);
        } catch (Exception e) {
            return null;
        }
    }



    @Nullable
    @Override
    public Re_IReResourceFile findClassResource(String className) {
        if (null == this.directory)
            return null;
        String relativePath = Re_NativeClassLoader.classNameToPath(className, Filex.system_separator);
        if (null == relativePath)
            return null;
        final File absolute = new File(this.directory, relativePath);
        if (absolute.exists()) {
            return new TargetFile(this,
                    getURL(relativePath), className,
                    absolute.getPath(),
                    relativePath,
                    contentCharset);
        }
        return null;
    }




    public static class TargetFile extends Re_IReResourceFile {
        String name;
        String fileLocalPath;
        String relativePth;
        Charset charset;

        TargetFile(Re_DirectoryResource resource, URL url,
                   String name,
                   String fileLocalPath,
                   String relativePath,
                   Charset charset) {
            super(resource, url);

            this.name = name;
            this.fileLocalPath = fileLocalPath;
            this.relativePth   = relativePath;
            this.charset = charset;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String getResourceURL() {
            return fileLocalPath;
        }

        @Override
        public String getRelativePth() {
            return relativePth;
        }

        @Override
        public Charset charset() {
            return charset;
        }

        @Override
        public InputStream asStream() {
            try {
                return new FileInputStream(fileLocalPath);
            } catch (Throwable e) {
                return null;
            }
        }
    }
}
