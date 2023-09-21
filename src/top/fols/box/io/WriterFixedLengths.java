package top.fols.box.io;

import top.fols.atri.interfaces.interfaces.IInnerStream;
import top.fols.box.io.OutputStreamFixedLengths.OutOfLengthException;
import top.fols.box.io.interfaces.IFixedLengthStream;

import java.io.IOException;
import java.io.Writer;

public class WriterFixedLengths<T extends Writer> extends Writer
    implements IFixedLengthStream, IInnerStream<T> {
    private T stream;
    private long maxCount;
    private long nowCount;
    private boolean fixed;

    public WriterFixedLengths(T stream, long maxCount) {
        if (null == stream) {
            throw new NullPointerException("stream for null");
        }
        if (maxCount < 0) {
            maxCount = 0;
        }
        this.stream = stream;
        this.maxCount = maxCount;
        this.nowCount = 0;
        this.fixed = true;
    }

    // 写入一个字节b到“字节数组输出流”中，并将计数+1
    @Override
    public void write(int b) throws IOException, OutOfLengthException {
        if (fixed && nowCount + 1 > maxCount) {
            throw new OutOfLengthException("maxsize=" + maxCount + ", nowsize=" + nowCount + ", writesize=" + 1);
        }
        stream.write(b);
        nowCount++;
    }

    // 写入字节数组b到“字节数组输出流”中。off是“写入字节数组b的起始位置”，len是写入的长度
    @Override
    public void write(char b[], int off, int len) throws IOException, OutOfLengthException {
        if (len < 0) {
            return;
        } else if (fixed) {
            if (nowCount + len > maxCount) {
                throw new OutOfLengthException("maxsize=" + maxCount + ", nowsize=" + nowCount + ", writesize=" + len);
            }
        }
        stream.write(b, off, len);
        nowCount += len;
    }

    @Override
    public void write(String str, int off, int len) throws IOException, OutOfLengthException {
        if (len < 0) {
            return;
        } else if (fixed) {
            if (nowCount + len > maxCount) {
                throw new OutOfLengthException("maxsize=" + maxCount + ", nowsize=" + nowCount + ", writesize=" + len);
            }
        }
        stream.write(str, off, len);
        nowCount += len;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    @Override
    public void flush() throws IOException {
        stream.flush();
    }

    @Override
    public long getFreeLength() {
        return maxCount - nowCount;
    }

    @Override
    public boolean isAvailable() {
        return !fixed || fixed && nowCount < maxCount;
    }

    @Override
    public long getUseLength() {
        return nowCount;
    }

    @Override
    public void resetUseLength() {
        nowCount = 0;
    }

    @Override
    public long getMaxUseLength() {
        return maxCount;
    }

    @Override
    public void setMaxUseLength(long maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    public void fixed(boolean b) {
        this.fixed = b;
    }

    @Override
    public boolean isFixed() {
        return fixed;
    }

    @Override
    public T getInnerStream() {
        return stream;
    }
}

