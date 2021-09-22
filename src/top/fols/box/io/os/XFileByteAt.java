package top.fols.box.io.os;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import top.fols.atri.io.Streams;
import top.fols.atri.lang.Finals;
import top.fols.box.io.interfaces.XInterfaceReleaseBufferable;
import top.fols.atri.util.ArrayPieceIndex;
import top.fols.box.io.interfaces.XInterfaceSequenceBigByteIO;

/**
 * do not contact multiple threads to operate this instance at the same time
 * fast sequential read if the position of the next read data is within the
 * current block range, the speed is the fastest
 * 
 */
public class XFileByteAt implements Closeable, XInterfaceSequenceBigByteIO, XInterfaceReleaseBufferable {
    private File file;
    private RandomAccessFile raf;

    public XFileByteAt(String file) throws FileNotFoundException {
        this(new File(file), Streams.DEFAULT_BYTE_BUFF_SIZE);
    }

    public XFileByteAt(File file) throws FileNotFoundException {
        this(file, Streams.DEFAULT_BYTE_BUFF_SIZE);
    }

    public XFileByteAt(String file, int bufsize) throws FileNotFoundException {
        this(new File(file), bufsize);
    }

    public XFileByteAt(File file, int bufsize) throws FileNotFoundException {
        this(new RandomAccessFile(file, Finals.FileOptMode.r()), file, file.length(), bufsize);
    }

    public XFileByteAt(RandomAccessFile option, File file) {
        this(option, file, file.length(), Streams.DEFAULT_BYTE_BUFF_SIZE);
    }

    public XFileByteAt(RandomAccessFile option, File file, long filelength, int buffsize) {
        this.file = file;
        this.raf = option;
        this.piecesize = buffsize;
        this.nowpiecebytes = new byte[this.piecesize];
        this.xpm = new ArrayPieceIndex(filelength, this.piecesize);
    }

    private byte[] nowpiecebytes;// 块缓存
    private int piecesize;// 块缓存大小
    private ArrayPieceIndex xpm;

    private long nowpiece = -1;// 当前块
    private long nowpiecestart = -1;
    private long nowpieceend = -1;

    private void change(long index) throws IOException {
        long newPiece = this.xpm.getIndexPiece(index);
        if (newPiece != this.nowpiece || null == this.nowpiecebytes) {
            long start = this.xpm.getPieceIndexStart(newPiece);
            long end = this.xpm.getPieceIndexEnd(newPiece);
            this.raf.seek(start);
            int len = (int) this.xpm.getPieceSize(newPiece);
            if (null == this.nowpiecebytes || this.nowpiecebytes.length != len) {
                this.nowpiecebytes = null;
                this.nowpiecebytes = new byte[len];
            }
            this.raf.read(this.nowpiecebytes, 0, this.nowpiecebytes.length);
            this.nowpiece = newPiece;

            this.nowpiecestart = start;
            this.nowpieceend = end;
        }
    }

    // if (index < 0 || index >= length)
    // throw new ArrayIndexOutOfBoundsException(String.format("index=%s
    // filelength=%s", index, length));
    public byte byteAt(long index) throws IOException {
        if (!(this.nowpiecestart >= index && this.nowpieceend <= index)) {
            this.change(index);
        }
        return this.nowpiecebytes[(int) (index % piecesize)];
    }

    public int getBytes(long index, byte[] bs, int off, int len) throws IOException {
        this.releaseBuffer();
        this.raf.seek(index);
        int read = this.raf.read(bs, off, len);
        return read;
    }

    public Byte byteAtTry(long index) {
        try {
            if (!(this.nowpiecestart >= index && this.nowpieceend <= index)) {
                this.change(index);
            }
            return this.nowpiecebytes[(int) (index % piecesize)];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public void byteAtBufferedSize(int size) {
        if (size == this.piecesize) {
            return;
        }
        this.piecesize = size;
        this.nowpiecebytes = new byte[size];
        this.xpm.updatePieceInfo(this.length(), size);
        this.nowpiecestart = this.nowpieceend = -1;
        this.nowpiece = -1;
    }

    @Override
    public void releaseBuffer() {
        // TODO: Implement this method
        this.nowpiecebytes = null;
        this.nowpiece = this.nowpiecestart = this.nowpieceend = -1;
    }

    public byte[] getNowPieceBytes() {
        return this.nowpiecebytes;
    }

    public int getNowPieceLength() {
        return this.nowpiecebytes.length;
    }

    public void seekPiece(long newPiece) throws IOException {
        this.change(this.xpm.getPieceIndexStart(newPiece));
    }

    public long getPieceBuffSize() {
        return this.xpm.getEachPieceSize();
    }

    public long getIndexPiece(long newPiece) {
        return this.xpm.getIndexPiece(newPiece);
    }

    public long getPieceLength(long newPiece) {
        return this.xpm.getPieceSize(newPiece);
    }

    public long getPieceIndexEnd(long newPiece) {
        return this.xpm.getPieceIndexEnd(newPiece);
    }

    public long getPieceIndexStart(long newPiece) {
        return this.xpm.getPieceIndexStart(newPiece);
    }

    public long getPieceCount() {
        return this.xpm.getPieceCount();
    }

    public long length() {
        return this.file.length();
    }

    public File getFile() {
        return this.file;
    }

    @Override
    public void close() throws IOException {
        // TODO: Implement this method
        this.raf.close();
    }

    public static long indexOf(XFileByteAt drive, byte b, long startIndex, long indexRange) throws IOException {
        return XFileByteAt.indexOf(drive, drive.length(), b, startIndex, indexRange);
    }

    public static long indexOf(XFileByteAt drive, long filelength, byte b, long startIndex, long indexRange)
            throws IOException {
        startIndex = startIndex < 0 ? 0 : startIndex;
        indexRange = indexRange < filelength ? indexRange : filelength;
        for (long i = startIndex; i < indexRange; i++) {
            if (drive.byteAt(i) == b) {
                return i;
            }
        }
        return -1;
    }

    public static long indexOf(XFileByteAt drive, byte[] b, long startIndex, long indexRange) throws IOException {
        return XFileByteAt.indexOf(drive, drive.length(), b, startIndex, indexRange);
    }

    public static long indexOf(XFileByteAt drive, long filelength, byte[] b, long startIndex, long indexRange)
            throws IOException {
        if (filelength == 0 || startIndex > indexRange || null == b || b.length > filelength || b.length == 0
                || indexRange - startIndex + 1 < b.length) {
            return -1;
        }
        startIndex = startIndex < 0 ? 0 : startIndex;
        indexRange = indexRange < filelength ? indexRange : filelength;
        int i2;
        for (long i = startIndex; i < indexRange; i++) {
            if (drive.byteAt(i) == b[0]) {
                if (indexRange - i < b.length) {
                    break;
                }
                for (i2 = 1; i2 < b.length; i2++) {
                    if (drive.byteAt(i + i2) != b[i2]) {
                        break;
                    }
                }
                if (i2 == b.length) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static long lastIndexOf(XFileByteAt drive, byte b, long startIndex, long indexRange) throws IOException {
        return XFileByteAt.lastIndexOf(drive, drive.length(), b, startIndex, indexRange);
    }

    public static long lastIndexOf(XFileByteAt drive, long filelength, byte b, long startIndex, long indexRange)
            throws IOException {
        if (filelength == 0 || indexRange > startIndex) {
            return -1;
        }
        indexRange = indexRange < 0 ? 0 : indexRange;
        if (startIndex > filelength - 1) {
            startIndex = filelength - 1;
        }
        while (startIndex >= indexRange) {
            if (drive.byteAt(startIndex) == b) {
                return startIndex;
            }
            startIndex--;
        }
        return -1;
    }

    public static long lastIndexOf(XFileByteAt drive, byte[] b, long startIndex, long indexRange) throws IOException {
        return XFileByteAt.lastIndexOf(drive, drive.length(), b, startIndex, indexRange);
    }

    public static long lastIndexOf(XFileByteAt drive, long filelength, byte[] b, long startIndex, long indexRange)
            throws IOException {
        if (filelength == 0 || indexRange > startIndex || null == b || b.length > filelength || b.length == 0
                || startIndex - indexRange + 1 < b.length) {
            return -1;
        }
        indexRange = indexRange < 0 ? 0 : indexRange;
        if (startIndex > filelength) {
            startIndex = filelength;
        }
        long i;
        int i2;
        for (i = startIndex == filelength ? filelength - 1 : startIndex; i >= indexRange; i--) {
            if (drive.byteAt(i) == b[0]) {
                if (i + b.length > startIndex) {
                    continue;
                }
                for (i2 = 1; i2 < b.length; i2++) {
                    if (drive.byteAt(i + i2) != b[i2]) {
                        break;
                    }
                }
                if (i2 == b.length) {
                    return i;
                }
            }
        }
        return -1;
    }
}
