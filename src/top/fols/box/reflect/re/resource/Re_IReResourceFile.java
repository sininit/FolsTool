package top.fols.box.reflect.re.resource;

import top.fols.atri.io.util.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

@SuppressWarnings({"UnnecessaryModifier"})
public abstract class Re_IReResourceFile {
    final Re_IReResource resources;
    final URL    url;
    final String urlString;
    public Re_IReResourceFile(Re_IReResource resources, URL url) {
        this.resources = resources;
        this.url = url;
        this.urlString = null == url ? null : url.toExternalForm();
    }

    public Re_IReResource getResources() {
        return resources;
    }

    public URL toURL() {
        return url;
    }
    public String getResourceURL() {
        return urlString;
    }


    //统一名称
    public abstract String name();

    public abstract String getRelativePth();

    public abstract Charset charset();


    public InputStream asStream() {
        try {
            return toURL().openStream();
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] asBytes() {
        InputStream inputStream = asStream();
        if (null != inputStream) {
            try {
                return Streams.toBytes(inputStream);
            } catch (IOException ignored) {
            } finally {
                Streams.close(inputStream);
            }
        }
        return null;
    }
    public String asString() {
        byte[] bytes = asBytes();
        if (bytes == null)
            return null;
        return new String(bytes, charset());
    }

    public String toString() {
        return name();
    }
}
