package app.utils;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

public class FileLogStream extends OutputStream {
    File file;
    Path path;
    public FileLogStream(File file) {
        this.file = file;
        this.path = Paths.get(file.getPath());
    }
    final Object lock = new Object();
    final AtomicLong size = new AtomicLong();
    long maxSize = 32L * 1024L * 1024;

    OutputStream outputStream = null;
    OutputStream openStream() throws FileNotFoundException {
        OutputStream os = outputStream;
        if (null == os || (maxSize != -1 && size.get() >= maxSize)) {
            Streams.close(os);
            outputStream = os = new FileOutputStream(this.getFile());
            size.set(0);
        }
        return os;
    }

    public long size() { return size.get(); }
    public long getMaxSize() { return maxSize; }
    public void setMaxSize(long size) { this.maxSize = size <= 0?-1:size; }



    public File getFile() { return this.file; }

    @Override
    public void write(int b) throws IOException {
        this.write(new byte[]{(byte) b});
    }
    @Override
    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        synchronized (this.lock) {
            openStream().write(b, off, len);
            size.addAndGet(len);
        }
    }

    @Override
    public void flush() throws IOException {
        synchronized (this.lock) {
            openStream().flush();
            file.setLastModified(System.currentTimeMillis());
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (this.lock) {
            openStream().close();
        }
    }

}
