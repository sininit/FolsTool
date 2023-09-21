package top.fols.box.io.os;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import top.fols.atri.io.util.Streams;
import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.atri.lang.Mathz;
import top.fols.atri.util.IndexPiece;
import top.fols.atri.util.IndexPieces;
import top.fols.box.io.interfaces.IIOBigSequenceByte;

/**
 * do not contact multiple threads to operate this instance at the same time
 * fast sequential read if the position of the next read data is within the
 * current block range, the speed is the fastest
 * 
 */
@Deprecated
public class FileByteAt implements Closeable, IIOBigSequenceByte, IReleasable {
    private final File file;
    private final RandomAccessFile raf;

    public FileByteAt(String file) throws FileNotFoundException {
        this(new File(file), Streams.DEFAULT_BYTE_BUFF_SIZE);
    }

    public FileByteAt(File file) throws FileNotFoundException {
        this(file, Streams.DEFAULT_BYTE_BUFF_SIZE);
    }

    public FileByteAt(String file, int bufsize) throws FileNotFoundException {
        this(new File(file), bufsize);
    }

    public FileByteAt(File file, int bufsize) throws FileNotFoundException {
        this(new RandomAccessFile(file, Finals.FileOptMode.r()), file, file.length(), bufsize);
    }

    public FileByteAt(RandomAccessFile option, File file) {
        this(option, file, file.length(), Streams.DEFAULT_BYTE_BUFF_SIZE);
    }

    public FileByteAt(RandomAccessFile option, File file, long filelength, int buffsize) {
        this.file = file;
        this.raf = option;
        this.pieceEachPieceSize = buffsize;
        this.pieceCount = IndexPieces.getLongPieceCount(filelength, buffsize);
        this.nowpiecebytes = new byte[this.pieceEachPieceSize];
    }

    private byte[] nowpiecebytes;// 块缓存
    private int pieceEachPieceSize;// 块缓存大小
    private long pieceCount;

    private long nowpiece = -1;// 当前块
    private long nowpiecestart = -1;
    private long nowpieceend = -1;

    private void change(long index) throws IOException {
        long p = IndexPieces.getLongPieceFromIndex(index, this.pieceEachPieceSize);
        if (p != this.nowpiece || null == this.nowpiecebytes) {
            long start = this.getPieceIndexStart(p);
            long end = this.getPieceIndexEnd(p);
            this.raf.seek(start);
            int len = Mathz.toIntExact(IndexPieces.getLongPieceSize(p, this.pieceCount, this.length(), this.pieceEachPieceSize));
            if (null == this.nowpiecebytes || this.nowpiecebytes.length != len) {
                this.nowpiecebytes = new byte[len];
            }
            this.raf.read(this.nowpiecebytes, 0, this.nowpiecebytes.length);
            this.nowpiece = p;

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
        return this.nowpiecebytes[(int) (index % pieceEachPieceSize)];
    }

    public int getBytes(long index, byte[] bs, int off, int len) throws IOException {
        this.release();
        this.raf.seek(index);
        return this.raf.read(bs, off, len);
    }

    public Byte byteAtTry(long index) {
        try {
            if (!(this.nowpiecestart >= index && this.nowpieceend <= index)) {
                this.change(index);
            }
            return this.nowpiecebytes[(int) (index % pieceEachPieceSize)];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public void byteAtBufferedSize(int size) {
        if (size == this.pieceEachPieceSize) {
            return;
        }
        this.pieceEachPieceSize = size;
        this.pieceCount = IndexPieces.getLongPieceCount(this.length(), size);
        this.nowpiecebytes = new byte[size];
        this.nowpiecestart = this.nowpieceend = -1;
        this.nowpiece = -1;
    }


    public byte[] getNowPieceBytes() {
        return this.nowpiecebytes;
    }

    public int getNowPieceLength() {
        return this.nowpiecebytes.length;
    }

    public void seekPiece(long p) throws IOException {
        this.change(IndexPieces.getLongPieceIndexStart(p, this.pieceCount, this.length(), this.pieceEachPieceSize));
    }

    public long getPieceBuffSize() {
        return this.pieceEachPieceSize;
    }

    public long getIndexPiece(long index) {
        return IndexPieces.getLongPieceFromIndex(index, this.pieceEachPieceSize);
    }

    public long getPieceLength(long p) {
        return IndexPieces.getLongPieceSize(p, this.pieceCount, this.length(), this.pieceEachPieceSize);
    }

    public long getPieceIndexStart(long p) {
        return IndexPieces.getLongPieceIndexStart(p, this.pieceCount, this.length(), this.pieceEachPieceSize);
    }

    public long getPieceIndexEnd(long p) {
        return IndexPieces.getLongPieceIndexEnd(p, this.pieceCount, this.length(), this.pieceEachPieceSize);
    }

    public long getPieceCount() {
        return this.pieceCount;
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

    public static long indexOf(FileByteAt drive, byte b, long startIndex, long indexRange) throws IOException {
        return FileByteAt.indexOf(drive, drive.length(), b, startIndex, indexRange);
    }

    public static long indexOf(FileByteAt drive, long filelength, byte b, long startIndex, long indexRange)
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

    public static long indexOf(FileByteAt drive, byte[] b, long startIndex, long indexRange) throws IOException {
        return FileByteAt.indexOf(drive, drive.length(), b, startIndex, indexRange);
    }

    public static long indexOf(FileByteAt drive, long filelength, byte[] b, long startIndex, long indexRange)
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

    public static long lastIndexOf(FileByteAt drive, byte b, long startIndex, long indexRange) throws IOException {
        return FileByteAt.lastIndexOf(drive, drive.length(), b, startIndex, indexRange);
    }

    public static long lastIndexOf(FileByteAt drive, long filelength, byte b, long startIndex, long indexRange)
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

    public static long lastIndexOf(FileByteAt drive, byte[] b, long startIndex, long indexRange) throws IOException {
        return FileByteAt.lastIndexOf(drive, drive.length(), b, startIndex, indexRange);
    }

    public static long lastIndexOf(FileByteAt drive, long filelength, byte[] b, long startIndex, long indexRange)
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



    @Override
    public boolean release() {
        this.nowpiecebytes = null;
        this.nowpiece = this.nowpiecestart = this.nowpieceend = -1;
        return true;
    }

    @Override
    public boolean released() {
        return null == this.nowpiecebytes && ((this.nowpiece == -1) && (-1 == this.nowpiecestart) && (-1 == this.nowpieceend));
    }
}
