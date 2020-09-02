package top.fols.box.io.base;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import top.fols.box.io.interfaces.XInterfaceGetInnerStream;
import top.fols.box.statics.XStaticSystem;

public class XOutputStreamLine<T extends OutputStream> extends OutputStream implements XInterfaceGetInnerStream<T> {
    private T stream;

    public XOutputStreamLine(T stream) {
        this.stream = stream;
    }

    @Override
    public void write(int p1) throws IOException {
        this.stream.write(p1);
    }
    @Override
    public void write(byte[] b, int off, int len) throws java.io.IOException {
        this.stream.write(b, off, len);
    }

    @Override
    public void flush() throws java.io.IOException {
        this.stream.flush();
    }

    @Override
    public void close() throws java.io.IOException {
        this.stream.close();
    }

    @Override
    public T getInnerStream() {
        return this.stream;
    }








    private byte[] getLineSeparatorBytes(String lineSeparator, Charset charset) {
        return lineSeparator.getBytes(charset);
    }

    public void writeLineString(String string) throws UnsupportedEncodingException, IOException {
        this.writeLineString(string, null);
    }

    /**
     * @param charset nullable
     */
    public void writeLineString(String string, Charset charset) throws UnsupportedEncodingException, IOException {
        this.writeLineString(string, XStaticSystem.LINUX_UNIX_LINE_SEPARATOR, charset);
    }

    /**
     * @param charset nullable
     */
    public void writeLineString(String string, String lineSeparator, Charset charset)
            throws UnsupportedEncodingException, IOException {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        this.write(string.getBytes(charset));
        this.write(this.getLineSeparatorBytes(lineSeparator, charset));
    }

}
