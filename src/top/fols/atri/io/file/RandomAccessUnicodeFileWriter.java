package top.fols.atri.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import top.fols.atri.interfaces.interfaces.IInnerFile;
import top.fols.atri.io.util.Streams;
import top.fols.atri.lang.Finals;
import top.fols.box.util.encode.ByteEncoders;
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
public class RandomAccessUnicodeFileWriter extends java.io.Writer implements IInnerFile {
    private final File file;
    private RandomAccessFile stream;

    private long charPos, charLength;
    private static final byte[] UNICODE_FILE_HEADER = UnicodeFileEncoders.BIG_ENDIAN.getFileHeader();
    private static final int ONE_CHAR_BYTE_LENGTH = UnicodeFileEncoders.BIG_ENDIAN.mathCharsLenToBytesLen(1);

    public RandomAccessUnicodeFileWriter(String file) throws FileNotFoundException, IOException {
        this(file, false);
    }

    public RandomAccessUnicodeFileWriter(File file) throws FileNotFoundException, IOException {
        this(file, false);
    }

    public RandomAccessUnicodeFileWriter(String file, boolean append) throws FileNotFoundException, IOException {
        this(new File(file), append);
    }

    public RandomAccessUnicodeFileWriter(File file, boolean append) throws FileNotFoundException, IOException {
        this.file = file;
        this.charPos = 0;
        if (append) {
            this.reCreateStreamInstance();

            long byteLength = this.file.length();
            if (byteLength == 0) {
                this.stream.write(UNICODE_FILE_HEADER);
                byteLength += UNICODE_FILE_HEADER.length;
            } else {
                RandomAccessUnicodeFileReader.checkUnicodeFile(this.innerFile());
            }

            this.charLength = UnicodeFileEncoders.BIG_ENDIAN.mathBytesLenToCharsLen(byteLength - UNICODE_FILE_HEADER.length);
            this.charPos = this.charLength;
            this.seekIndex(this.charLength);
        } else {
            this.reCreateStreamInstance();

            this.stream.setLength(0);
            this.stream.seek(0);
            this.stream.write(UNICODE_FILE_HEADER);

            this.charLength = 0;
            this.charPos = 0;
            this.seekIndex(0);
        }
    }

    @Override
    public File innerFile() {
        return this.file;
    }

    public RandomAccessUnicodeFileWriter reload() throws IOException {
        RandomAccessUnicodeFileReader.checkUnicodeFile(this.innerFile());

        long byteLength = this.file.length();
        this.charLength = UnicodeFileEncoders.BIG_ENDIAN.mathBytesLenToCharsLen(byteLength - UNICODE_FILE_HEADER.length);
        this.charPos = this.charPos > this.charLength ? this.charLength : this.charPos;
        this.seekIndex(this.charPos);
        return this;
    }

    private void reCreateStreamInstance() throws FileNotFoundException {
        Streams.close(stream);
        this.stream = new RandomAccessFile(file, Finals.FileOptMode.rw());
    }
    public RandomAccessUnicodeFileWriter reCreateFile() throws IOException {
        this.file.delete();
        this.file.createNewFile();

        this.reCreateStreamInstance();

        this.stream.write(UNICODE_FILE_HEADER);

        this.charLength = 0;
        this.charPos = 0;
        this.seekIndex(0);
        return this;
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
        this.write(ByteEncoders.bytesToChars(b, off, len, encoding));
    }

    @Override
    public void flush() {
        // TODO: Implement this method
    }




    private final byte[] charBuffer = new byte[ONE_CHAR_BYTE_LENGTH];
    private void writeChar0(int c) throws IOException {
        char ch = (char) c;
        UnicodeFileEncoders.BIG_ENDIAN.putChar(charBuffer, 0, ch);
        this.stream.write(charBuffer);
        this.charPos++;
        if (this.charPos > this.charLength) {
            this.charLength = this.charPos;
        }
    }

    private void writeChars0(char[] b, int off, int len) throws IOException {
        byte[] chsBytes = new byte[ONE_CHAR_BYTE_LENGTH * len];
        UnicodeFileEncoders.BIG_ENDIAN.putCharsToBytes(b, off, len, chsBytes, 0);
        this.stream.write(chsBytes);
        chsBytes = null;
        this.charPos += len;
        if (this.charPos > this.charLength) {
            this.charLength = this.charPos;
        }
    }


    public long byteLength() {
        return this.file.length();
    }

    public long count() {
        return this.charLength;
    }
    public void setCount(long newCount) throws IOException {
        long abslen = UNICODE_FILE_HEADER.length + newCount * ONE_CHAR_BYTE_LENGTH;

        this.stream.setLength(abslen);
        this.charLength = newCount;
        if (this.charPos > this.charLength) {
            this.charPos = this.charLength;
        }
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
            this.seekIndex(this.charPos);
            return n1;
        } else {
            this.charPos = this.charPos + n;
            this.seekIndex(this.charPos);
            return n;
        }
    }


    public static long count(File file) {
        return RandomAccessUnicodeFileReader.mathCount(file);
    }

    public static void appendFileChars(File file, long fileCharOff, char[] cs, int off, int len) throws IOException {
        RandomAccessUnicodeFileWriter writer = new RandomAccessUnicodeFileWriter(file, true);
        try {
            writer.seekIndex(fileCharOff);
            writer.write(cs, off, len);
            writer.flush();
        } finally {
            Streams.close(writer);
        }
    }

    public static void writeFileChars(File file, char[] cs, int off, int len) throws IOException {
        RandomAccessUnicodeFileWriter writer = new RandomAccessUnicodeFileWriter(file, false);
        try {
            writer.write(cs, off, len);
            writer.flush();
        } finally {
            Streams.close(writer);
        }
    }
}
