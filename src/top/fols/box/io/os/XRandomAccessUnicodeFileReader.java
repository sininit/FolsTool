package top.fols.box.io.os;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.box.util.XArrays;
import top.fols.box.util.XByteEncode;

/**
 * will chars cast To bytes, java BIG_ENDIAN Unicode Encoding
 * <p>
 * byte length = char count * 2
 * <p>
 * can use new String(bytes, "Unicode");
 * 
 */
public class XRandomAccessUnicodeFileReader extends java.io.Reader {
    private File file;
    private RandomAccessFile stream;
    private long charPos, charLength;
    private long mark = 0;
    private static final byte[] UNICODE_FILE_HEADER = XByteEncode.UnicodeOption.BIG_ENDIAN.getFileHeader();
    private static final int ONE_CHAR_BYTE_LENGTH = XByteEncode.UnicodeOption.BIG_ENDIAN.charsLenToBytesLen(1);

    public XRandomAccessUnicodeFileReader(String file) throws FileNotFoundException, IOException {
        this(new File(file));
    }

    public XRandomAccessUnicodeFileReader(File file) throws FileNotFoundException, IOException {
        this.file = Objects.requireNonNull(file);
        this.stream = new RandomAccessFile(file, Finals.FileOptMode.r());
        this.charPos = 0;
        this.reLoad();
    }

    public File getFile() {
        return this.file;
    }

    protected static void checkUnicodeFile(File file) throws IOException {
        XRandomAccessFileInputStream ra = new XRandomAccessFileInputStream(file);
        long length = ra.length();
        if (length == 0) {
            ra.close();
            throw new IOException("null file: " + file.getPath());
        }
        if ((length - UNICODE_FILE_HEADER.length) % ONE_CHAR_BYTE_LENGTH != 0) {
            ra.close();
            throw new IOException("file length error: " + length);
        }
        byte[] head = new byte[ONE_CHAR_BYTE_LENGTH];
        ra.read(head);
        if (!XArrays.equals(head, UNICODE_FILE_HEADER)) {
            ra.close();
            throw new IOException("file header error: " + Arrays.toString(head));
        }
        ra.close();
    }

    public XRandomAccessUnicodeFileReader reLoad() throws IOException {
        XRandomAccessUnicodeFileReader.checkUnicodeFile(this.getFile());

        long byteLength = this.file.length();
        this.charLength = XByteEncode.UnicodeOption.BIG_ENDIAN.bytesLenToCharsLen(byteLength - UNICODE_FILE_HEADER.length);
        this.charPos = this.charPos > this.charLength ? this.charLength : this.charPos;
        this.seek(this.charPos);
        return this;
    }

    @Override
    public int read() throws IOException {
        return this.read0();
    }

    @Override
    public int read(char[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read(char[] b, int off, int len) throws IOException {
        return this.read0(b, off, len);
    }

    public String readToString(int len) throws IOException {
        char[] cs = new char[len];
        if (this.read(cs, 0, cs.length) == -1) {
            return null;
        } else {
            return new String(cs);
        }
    }

    private byte[] chBytes = new byte[ONE_CHAR_BYTE_LENGTH];

    private int read0() throws IOException {
        int r = this.stream.read(chBytes);
        if (r == -1) {
            return -1;
        } else if (r != ONE_CHAR_BYTE_LENGTH) {
            throw new IOException("need byte len =" + ONE_CHAR_BYTE_LENGTH + ", read to =" + r);
        }
        int result = (int) XByteEncode.UnicodeOption.BIG_ENDIAN.getChar(chBytes, 0);
        this.charPos++;
        return result;
    }

    private int read0(char[] cs, int off, int len) throws IOException {
        byte[] chsBytes = new byte[ONE_CHAR_BYTE_LENGTH * len];
        int r = this.stream.read(chsBytes);
        if (r == -1) {
            return -1;
        } else if (r % ONE_CHAR_BYTE_LENGTH != 0) {
            throw new IOException("read byte len error, read to len=" + r);
        }
        int rcl = r / ONE_CHAR_BYTE_LENGTH;
        XByteEncode.UnicodeOption.BIG_ENDIAN.putBytesToChars(chsBytes, 0, cs, off, rcl);
        chsBytes = null;
        this.charPos += rcl;
        return rcl;
    }

    @Override
    public long skip(long n) throws IOException {
        return this.skipChars(n);
    }

    @Override
    public boolean ready() {
        return true;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        this.mark(readAheadLimit);
    }

    public void mark(long readAheadLimit) {
        this.mark = this.charPos;
    }

    @Override
    public void reset() throws IOException {
        this.charPos = this.mark;
        this.seek(this.charPos);
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
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

    public long available() {
        return this.charLength - this.charPos;
    }

    public static long length(File file) {
        long byteLength = file.length();
        return XByteEncode.UnicodeOption.BIG_ENDIAN.bytesLenToCharsLen(byteLength - UNICODE_FILE_HEADER.length);
    }

    public static char[] readFileChars(File file, long off, int len) throws FileNotFoundException, IOException {
        XRandomAccessUnicodeFileReader reader = new XRandomAccessUnicodeFileReader(file);
        reader.seek(off);
        char[] cs = new char[len];
        reader.read(cs);
        reader.close();
        return cs;
    }

    public static char[] readFileChars(File file) throws FileNotFoundException, IOException {
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new OutOfMemoryError("fileSize=" + length);
        }
        return readFileChars(file, 0, XByteEncode.UnicodeOption.BIG_ENDIAN.charsLenToBytesLen((int) length));
    }
}
