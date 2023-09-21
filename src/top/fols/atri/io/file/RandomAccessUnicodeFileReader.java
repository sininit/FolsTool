package top.fols.atri.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import top.fols.atri.interfaces.interfaces.IInnerFile;
import top.fols.atri.io.util.Streams;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.box.lang.Arrayy;
import top.fols.box.util.encode.UnicodeFileEncoders;

/**
 * will chars cast To bytes, java BIG_ENDIAN Unicode Encoding
 * <p>
 * byte length = char count * 2
 * <p>
 * can use new String(bytes, "Unicode");
 * 
 */
@SuppressWarnings("ManualMinMaxCalculation")
public class RandomAccessUnicodeFileReader extends java.io.Reader implements IInnerFile {
    private final File file;
    private RandomAccessFile stream;

    private long charPos, charLength;
    private long mark = 0;
    private static final byte[] UNICODE_FILE_HEADER = UnicodeFileEncoders.BIG_ENDIAN.getFileHeader();
    private static final int ONE_CHAR_BYTE_LENGTH = UnicodeFileEncoders.BIG_ENDIAN.mathCharsLenToBytesLen(1);

    public RandomAccessUnicodeFileReader(String file) throws FileNotFoundException, IOException {
        this(new File(file));
    }

    public RandomAccessUnicodeFileReader(File file) throws FileNotFoundException, IOException {
        this.file = Objects.requireNonNull(file);
        this.stream = new RandomAccessFile(file, Finals.FileOptMode.r());
        this.charPos = 0;
        this.reload();
    }

    protected static void checkUnicodeFile(File file) throws IOException {
        RandomAccessFileInputStream ra = new RandomAccessFileInputStream(file);
        try {
            long length = ra.length();
            if (length == 0) {
                throw new IOException("null file: " + file.getPath());
            }
            if ((length - UNICODE_FILE_HEADER.length) % ONE_CHAR_BYTE_LENGTH != 0) {
                throw new IOException("file length error: " + length);
            }
            byte[] head = new byte[ONE_CHAR_BYTE_LENGTH];
            //noinspection ResultOfMethodCallIgnored
            ra.read(head);
            if (!Arrayy.equals(head, UNICODE_FILE_HEADER)) {
                ra.close();
                throw new IOException("file header error: " + Arrays.toString(head));
            }
        } finally {
            Streams.close(ra);
        }
    }

    public RandomAccessUnicodeFileReader reload() throws IOException {
        RandomAccessUnicodeFileReader.checkUnicodeFile(this.innerFile());

        long byteLength = this.file.length();
        this.charLength = UnicodeFileEncoders.BIG_ENDIAN.mathBytesLenToCharsLen(byteLength - UNICODE_FILE_HEADER.length);
        this.charPos = this.charPos > this.charLength ? this.charLength : this.charPos;
        this.seekIndex(this.charPos);
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
        int read = this.read(cs, 0, cs.length);
        if (read == -1) {
            return null;
        } else {
            return new String(cs, 0, read);
        }
    }

    private final byte[] charBuffer = new byte[ONE_CHAR_BYTE_LENGTH];
    private int read0() throws IOException {
        int read = this.stream.read(charBuffer);
        if (read == -1) {
            return -1;
        } else if (read != ONE_CHAR_BYTE_LENGTH) {
            throw new IOException("need byte len =" + ONE_CHAR_BYTE_LENGTH + ", read to =" + read);
        }
        int result = UnicodeFileEncoders.BIG_ENDIAN.getChar(charBuffer, 0);
        this.charPos++;
        return result;
    }

    private int read0(char[] cs, int off, int len) throws IOException {
        byte[] chsBytes = new byte[ONE_CHAR_BYTE_LENGTH * len];
        int read = this.stream.read(chsBytes);
        if (read == -1) {
            return -1;
        } else if (read % ONE_CHAR_BYTE_LENGTH != 0) {
            throw new IOException("read byte len error, read to len=" + read);
        }
        int rcl = read / ONE_CHAR_BYTE_LENGTH;
        UnicodeFileEncoders.BIG_ENDIAN.putBytesToChars(chsBytes, 0, cs, off, rcl);
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
        this.mark((long) readAheadLimit);
    }

    public void mark(long readAheadLimit) {
        this.mark = this.charPos;
    }

    @Override
    public void reset() throws IOException {
        this.charPos = this.mark;
        this.seekIndex(this.charPos);
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }


    public long available() {
        return this.charLength - this.charPos;
    }

    public long skipChars(long n) throws IOException {
        if (this.charPos + n > this.charLength) {
            long n1 = this.charLength - this.charPos;
            this.charPos = this.charLength;
            this.seekIndex(this.charPos);
            return n1;
        } else {
            this.charPos = this.charPos + n;
            this.seekIndex(this.charPos);
            return n;
        }
    }


    public long byteLength() {
        return this.file.length();
    }
    public long count() {
        return this.charLength;
    }

    public long getIndex() {
        return this.charPos;
    }
    public void seekIndex(long pos) throws IOException {
        if (pos > this.charLength) {
            throw new IOException("seek index=" + pos + ", length=" + this.charLength);
        }
        long abs = UNICODE_FILE_HEADER.length + ONE_CHAR_BYTE_LENGTH * pos;
        this.charPos = pos;
        this.stream.seek(abs);
    }



    @Override
    public File innerFile() {
        return this.file;
    }



    public static long mathCount(File file) {
        long byteLength = file.length();
        return UnicodeFileEncoders.BIG_ENDIAN.mathBytesLenToCharsLen(byteLength - UNICODE_FILE_HEADER.length);
    }
    public static char[] readFileChars(File file, long off, int len) throws FileNotFoundException, IOException {
        RandomAccessUnicodeFileReader reader = new RandomAccessUnicodeFileReader(file);
        try {
            reader.seekIndex(off);
            char[] cs = new char[len];
            int read = reader.read(cs);
            if (read != len)
                cs = Arrays.copyOf(cs, read);
            return cs;
        } finally {
            reader.close();
        }
    }

    public static char[] readFileChars(File file) throws FileNotFoundException, IOException {
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new OutOfMemoryError("fileSize=" + length);
        }
        return readFileChars(file, 0, UnicodeFileEncoders.BIG_ENDIAN.mathCharsLenToBytesLen((int) length));
    }
}
