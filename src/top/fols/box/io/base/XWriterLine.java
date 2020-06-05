package top.fols.box.io.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import top.fols.box.io.interfaces.XInterfaceGetOriginStream;
import top.fols.box.statics.XStaticSystem;

/**
 * XWriterLine
 */
public class XWriterLine<T extends Writer> extends Writer implements XInterfaceGetOriginStream<T> {
    private T stream;

    public XWriterLine(T stream) {
        super();
        this.stream = stream;
    }

    @Override
    public void write(int c) throws IOException {
        this.stream.write(c);
    }
    
    @Override
    public void write(char cbuf[], int off, int len) throws IOException {
        if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        this.stream.write(cbuf, off, len);
    }

    @Override
    public void write(String str) throws IOException {
        this.stream.write(str);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        this.stream.write(str, off, len);
    }

    @Override
    public XWriterLine append(CharSequence csq) throws IOException {
        this.stream.append(csq);
        return this;
    }

    @Override
    public XWriterLine append(CharSequence csq, int start, int end) throws IOException {
        this.stream.append(csq, start, end);
        return this;
    }

    @Override
    public XWriterLine append(char c) throws IOException {
        this.stream.append(c);
        return this;
    }

    @Override
    public String toString() {
        return this.stream.toString();
    }

    @Override
    public void flush() throws IOException {
        this.stream.flush();
        return;
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
        return;
    }

    @Override
    public T getStream() {
        // TODO Auto-generated method stub
        return this.stream;
    }

    public void writeLineString(String string) throws UnsupportedEncodingException, IOException {
        this.writeLineString(string, XStaticSystem.LINUX_UNIX_LINE_SEPARATOR);
    }

    public void writeLineString(String string, String lineSeparator) throws UnsupportedEncodingException, IOException {

        this.write(string);
        this.write(lineSeparator);
    }
}
