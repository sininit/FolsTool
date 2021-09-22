package top.fols.box.io.os;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import top.fols.atri.lang.Finals;
import top.fols.box.util.XByteEncode;

/**
 * will chars cast To bytes, java BIG_ENDIAN Unicode Encoding
 * <p>
 * byte length = char count * 2
 * <p>
 * can use new String(bytes, "Unicode");
 * 
 */
public class XRandomAccessUnicodeFileWriter extends java.io.Writer {

    private File file;
    private RandomAccessFile stream;
    private long charPos, charLength;
    private static final byte[] UNICODE_FILE_HEADER = XByteEncode.UnicodeOption.BIG_ENDIAN.getFileHeader();
    private static final int ONE_CHAR_BYTE_LENGTH = XByteEncode.UnicodeOption.BIG_ENDIAN.charsLenToBytesLen(1);

    public XRandomAccessUnicodeFileWriter(String file) throws FileNotFoundException, IOException {
        this(file, false);
    }

    public XRandomAccessUnicodeFileWriter(File file) throws FileNotFoundException, IOException {
        this(file, false);
    }

    public XRandomAccessUnicodeFileWriter(String file, boolean append) throws FileNotFoundException, IOException {
        this(new File(file), append);
    }

    public XRandomAccessUnicodeFileWriter(File file, boolean append) throws FileNotFoundException, IOException {
        this.file = file;
        this.charPos = 0;
        if (append) {
            this.reCreateStreamInstance();

            long byteLength = this.file.length();
            if (byteLength == 0) {
                this.stream.write(UNICODE_FILE_HEADER);
                byteLength += UNICODE_FILE_HEADER.length;
            } else {
                XRandomAccessUnicodeFileReader.checkUnicodeFile(this.getFile());
            }

            this.charLength = XByteEncode.UnicodeOption.BIG_ENDIAN.bytesLenToCharsLen(byteLength - UNICODE_FILE_HEADER.length);
            this.charPos = this.charLength;
            this.seek(this.charLength);
        } else {
            this.reCreateStreamInstance();

            this.stream.setLength(0);
            this.stream.seek(0);
            this.stream.write(UNICODE_FILE_HEADER);

            this.charLength = 0;
            this.charPos = 0;
            this.seek(0);
        }
    }

    public File getFile() {
        return this.file;
    }

    public XRandomAccessUnicodeFileWriter reLoad() throws IOException {
        XRandomAccessUnicodeFileReader.checkUnicodeFile(this.getFile());

        long byteLength = this.file.length();
        this.charLength = XByteEncode.UnicodeOption.BIG_ENDIAN.bytesLenToCharsLen(byteLength - UNICODE_FILE_HEADER.length);
        this.charPos = this.charPos > this.charLength ? this.charLength : this.charPos;
        this.seek(this.charPos);
        return this;
    }

    public XRandomAccessUnicodeFileWriter reCreateFile() throws IOException {
        this.file.delete();
        this.file.createNewFile();

        this.reCreateStreamInstance();

        this.stream.write(UNICODE_FILE_HEADER);

        this.charLength = 0;
        this.charPos = 0;
        this.seek(0);
        return this;
    }

    private void reCreateStreamInstance() throws FileNotFoundException {
        this.stream = new RandomAccessFile(file, Finals.FileOptMode.rw());
    }

    @Override
    public void write(int b) throws IOException {
        this.writeChar0(b);
    }

    @Override
    public void write(char[] b) throws IOException {
        this.writeChars0(b, 0, b.length);
    }

    @Override
    public void write(char[] b, int off, int len) throws IOException {
        this.writeChars0(b, off, len);
    }

    /*
     * 将Bytes转换编码为Unicode(char)并且写入
     */
    public void write(byte[] b, int off, int len, String encoding) throws IOException {
        this.write(b, off, len, Charset.forName(encoding));
    }

    public void write(byte[] b, int off, int len, Charset encoding) throws IOException {
        this.write(XByteEncode.bytesToChars(b, off, len, encoding));
    }

    @Override
    public void flush() {
        // TODO: Implement this method
        return;
    }

    private byte[] chBytes = new byte[ONE_CHAR_BYTE_LENGTH];

    private void writeChar0(int c) throws IOException {
        char ch = (char) c;

        XByteEncode.UnicodeOption.BIG_ENDIAN.putChar(chBytes, 0, ch);
        this.stream.write(chBytes);
        this.charPos++;
        if (this.charPos > this.charLength) {
            this.charLength = this.charPos;
        }
    }

    private void writeChars0(char[] b, int off, int len) throws IOException {
        byte[] chsBytes = new byte[ONE_CHAR_BYTE_LENGTH * len];
        XByteEncode.UnicodeOption.BIG_ENDIAN.putCharsToBytes(b, off, len, chsBytes, 0);
        this.stream.write(chsBytes);
        chsBytes = null;
        this.charPos += len;
        if (this.charPos > this.charLength) {
            this.charLength = this.charPos;
        }
    }

    public void seek(long pos) throws IOException {
        if (pos > this.charLength) {
            throw new IOException("seek index=" + pos + ", length=" + this.charLength);
        }
        long abs = UNICODE_FILE_HEADER.length + ONE_CHAR_BYTE_LENGTH * pos;
        this.charPos = pos;
        this.stream.seek(abs);
    }

    public long length() {
        return this.charLength;
    }

    public long byteLength() {
        return this.file.length();
    }

    public void setLength(long newLength) throws IOException {
        long abslen = UNICODE_FILE_HEADER.length + newLength * ONE_CHAR_BYTE_LENGTH;

        this.stream.setLength(abslen);
        this.charLength = newLength;
        if (this.charPos > this.charLength) {
            this.charPos = this.charLength;
        }
    }

    public long available() {
        return this.charLength - this.charPos;
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

    public long skipChars(long n) throws IOException {
        if (this.charPos + n > this.charLength) {
            long n1 = this.charLength - this.charPos;
            this.charPos = this.charLength;
            this.seek(this.charPos);
            return n1;
        } else {
            this.charPos = this.charPos + n;
            this.seek(this.charPos);
            return n;
        }
    }

    public long getIndex() {
        return this.charPos;
    }

    public static long length(File file) {
        return XRandomAccessUnicodeFileReader.length(file);
    }

    public static void appendFileChars(File file, long fileCharOff, char[] cs, int off, int len) throws IOException {
        XRandomAccessUnicodeFileWriter writer = new XRandomAccessUnicodeFileWriter(file, true);
        writer.seek(fileCharOff);
        writer.write(cs, off, len);
        writer.flush();
        writer.close();
    }

    public static void writeFileChars(File file, char[] cs, int off, int len) throws IOException {
        XRandomAccessUnicodeFileWriter writer = new XRandomAccessUnicodeFileWriter(file, false);
        writer.write(cs, off, len);
        writer.flush();
        writer.close();
    }
}
