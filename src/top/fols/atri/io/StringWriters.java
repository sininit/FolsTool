package top.fols.atri.io;

import java.io.Writer;

import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.box.io.interfaces.IPrivateBuffOperat;

public class StringWriters extends Writer
        implements IPrivateBuffOperat<StringBuilder>, IReleasable {
    private StringBuilder buf;

    public StringWriters() {
        buf = new StringBuilder();
        lock = buf;
    }

    public StringWriters(int initialSize) throws IllegalArgumentException {
        if (initialSize < 0) {
            throw new IllegalArgumentException("negative buffer size");
        }
        buf = new StringBuilder(initialSize);
        lock = buf;
    }

    @Override
    public void write(int c) {
        buf.append((char) c);
    }
    
    @Override
    public void write(char[] cbuf) {
        this.write(cbuf, 0, cbuf.length);
    }
    
    @Override
    public void write(char cbuf[], int off, int len) {
        if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        buf.append(cbuf, off, len);
    }

    @Override
    public void write(String str) {
        buf.append(str);
    }

    @Override
    public void write(String str, int off, int len) {
        buf.append(str, off, off + len);
    }

    @Override
    public StringWriters append(CharSequence csq) {
        write(String.valueOf(csq));
        return this;
    }

    @Override
    public StringWriters append(CharSequence csq, int start, int end) {
        if (null == csq)
            csq = "null";
        return append(csq.subSequence(start, end));
    }

    @Override
    public StringWriters append(char c) {
        write(c);
        return this;
    }

    @Override
    public String toString() {
        return buf.toString();
    }

    public StringBuilder getBuffer() {
        return buf;
    }

    @Override
    public void flush() {
        return;
    }

    @Override
    public void close() {
        release();
    }

    public int length() {
        return buf.length();
    }


    @Override
    public StringBuilder buffer() {
        return buf;
    }

    @Override
    public int buffer_length() {
        // TODO: Implement this method
        return buf.length();
    }

    @Override
    public void buffer(StringBuilder newBuff, int size) {
        // TODO: Implement this method
        this.buf = null == newBuff ? new StringBuilder() : newBuff;
        this.buffer_length(size);
    }

    @Override
    public void buffer_length(int size) throws ArrayIndexOutOfBoundsException {
        if (size > buf.length()) {
            throw new ArrayIndexOutOfBoundsException("arrayLen=" + buf.length() + ", setLen=" + size);
        }
        this.buf.setLength(size);
    }





    @Override
    public boolean release() {
        buffer(null, 0);
        return true;
    }

    @Override
    public boolean released() {
        return null == buf || buf.length()==0 ;
    }
}
