package top.fols.box.io.buffer;

import top.fols.box.io.buffer.bytes.ByteBufferFilter;
import top.fols.box.io.buffer.bytes.ByteBufferOperate;
import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.atri.interfaces.interfaces.IInnerStream;

import java.io.IOException;
import java.io.InputStream;

public class ByteBufferLinesInputStream<T extends InputStream> extends InputStream implements IInnerStream<T>, IReleasable {
    private int mark;
    private int markLimit;
    private final ByteBufferOperate buffer;
    private final T inputStream;

    BufferFilter<byte[]> READ_LINE_FILTER = ByteBufferFilter.lineFilterBytes();

    public ByteBufferLinesInputStream(T inputStream) {
        this.inputStream = inputStream;
        this.buffer = new ByteBufferOperate() {
            @Override
            public int stream_read(byte[] buf, int off, int len) throws IOException {
                return ByteBufferLinesInputStream.this.inputStream.read(buf, off, len);
            }
        };
        this.mark = -1;
        this.markLimit = 8192;
    }

    public byte[] readLine() throws IOException {
        return this.readLine(true);
    }
    public byte[] readLine(boolean addSeparator) throws IOException {
        byte[] buffer = null;
        if (!this.buffer.readFilterIFEnd(READ_LINE_FILTER)) {
            buffer = READ_LINE_FILTER.result(addSeparator);
        }
        if (this.buffer.removeIfOverflow(markLimit)) {
            this.mark = -1;
        }
        return buffer;
    }

    @Override
    public int read() throws IOException {
        int read = this.buffer.read();
        if (read > -1) {
            if (this.buffer.removeIfOverflow(markLimit)) {
                this.mark = -1;
            }
        }
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = this.buffer.read(b, off, len);
        if (read > -1) {
            if (this.buffer.removeIfOverflow(markLimit)) {
                this.mark = -1;
            }
        }
        return read;
    }

    @Override
    public long skip(long n) throws IOException {
        if (n < 0) {
            return 0;
        }
        long ki = Math.min(this.buffer.available(), (int) Math.min(n, Integer.MAX_VALUE));
        this.buffer.positionSkip((int) ki);
        if (ki != n) {
            long s_skip = this.inputStream.skip(n - ki);
            ki += s_skip;
        }
        return ki;
    }

    @Override
    public void close() {
        this.buffer.remove();
    }

    @Override
    public void mark(int readlimit) {
        int position = this.buffer.position();
        this.markLimit = position + readlimit;
        this.mark = position;
    }

    @Override
    public void reset() throws IOException {
        if (this.mark < 0) {
            throw new IOException("Resetting to invalid mark");
        }
        this.buffer.position(this.mark);
    }

    @Override
    public boolean markSupported() {
        return true;
    }


    @Override
    public int available() throws IOException {
        return this.buffer.available() + this.inputStream.available();
    }

    public ByteBufferOperate buffer() {
        return this.buffer;
    }


    @Override
    public boolean release() {
        this.buffer.remove();
        return true;
    }

    @Override
    public boolean released() {
        return false;
    }

    @Override
    public T getInnerStream() {
        return this.inputStream;
    }
}
