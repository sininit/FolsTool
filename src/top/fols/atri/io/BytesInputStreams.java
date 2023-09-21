package top.fols.atri.io;

import java.io.InputStream;

import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Finals;
import top.fols.box.io.interfaces.IPrivateBuffOperat;
import top.fols.box.io.interfaces.IPrivateStreamIndexOperat;
import java.nio.charset.Charset;

//-------copy------
public class BytesInputStreams extends InputStream implements IPrivateBuffOperat<byte[]>,
IPrivateStreamIndexOperat, IReleasable, Delimiter.IBytesDelimiterInputStream {


    private byte[] buf;
    private int pos;
    private int mark = 0;
    private int count;

    public BytesInputStreams() {
        this(Finals.EMPTY_BYTE_ARRAY);
    }


    public BytesInputStreams(byte[] buf) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }


    public BytesInputStreams(byte[] buf, int offset, int length) {
        this.buf = buf;
        this.pos = offset;
        this.count = Math.min(offset + length, buf.length);
        this.mark = offset;
    }


    @Override
    public int read() {
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }

    @Override
    public int read(byte[] b) {
        return this.read(b, 0, b.length);
    }




    @Override
    public int read(byte b[], int off, int len) {
        if (null == b) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        if (pos >= count) {
            return -1;
        }
        int avail = count - pos;
        if (len > avail) {
            len = avail;
        }
        if (len <= 0) {
            return 0;
        }
        Arrayz.arraycopy(buf, pos, b, off, len);
        pos += len;
        return len;
    }


    @Override
    public long skip(long n) {
        long k = count - pos;
        if (n < k) {
            k = n < 0 ? 0 : n;
        }
        pos += k;
        return k;
    }


    @Override
    public int available() {
        return count - pos;
    }


    @Override
    public boolean markSupported() {
        return true;
    }


    @Override
    public void mark(int readAheadLimit) {
        mark = pos;
    }


    @Override
    public void reset() {
        pos = mark;
    }

    @Override
    public void close() {
        release();
    }







    @Override
    public byte[] buffer() {
        return buf;
    }

    @Override
    public int buffer_length() {
        // TODO: Implement this method
        return buf.length;
    }

    @Override
    public void buffer(byte[] newBuff, int size) {
        // TODO: Implement this method
        this.buf = null == newBuff ? Finals.EMPTY_BYTE_ARRAY : newBuff;
        this.buffer_length(size);
    }

    @Override
    public void buffer_length(int size) throws ArrayIndexOutOfBoundsException {
        if (size > buf.length) {
            throw new ArrayIndexOutOfBoundsException("arrayLen=" + buf.length + ", setLen=" + size);
        }
        count = size;
    }



    @Override
    public void seekIndex(int index) {
        if (!(index > -1 && index <= count)) {
            throw new ArrayIndexOutOfBoundsException("can't set pos index=" + index + " length=" + count);
        }
        pos = index;
    }

    @Override
    public int getIndex() {
        return pos;
    }


    public int size() {
        return count;
    }



    @Override
    public boolean release() {
        this.buffer(null, 0);
        this.separatorIndex = SEPARATOR_INDEX_UNSET;
		this.delimiter  = null;
        this.separators = null;
        return true;
    }
    @Override
    public boolean released() {
        return  (null == buf || buf.length == 0) &&
			(null == delimiter && separatorIndex == SEPARATOR_INDEX_UNSET);
    }
	









    @Override
    public boolean findNext() {
        return (count - pos) > 0;
    }
    public boolean hasNext(int pos) {
        return (count - pos) > 0;
    }

    public static final int SEPARATOR_INDEX_UNSET = -1;

    private Delimiter.IBytesDelimiter delimiter;
    private byte[][] separators;
    private int separatorIndex = SEPARATOR_INDEX_UNSET;

    @Override
    public Delimiter.IBytesDelimiter getDelimiter() { return this.delimiter; }
    @Override
    public void setDelimiter(Delimiter.IBytesDelimiter delimiter) {
	    this.delimiter  = delimiter;
		this.separators = this.delimiter.innerSeparators();
	}
    @Override
    public void setDelimiter(byte[][] delimiter) {
        setDelimiter(Delimiter.build(delimiter));
	}
    @Override
    public void setDelimiterAndSetCharset(char[][] delimiter, Charset charset) {
	    this.setDelimiter(Delimiter.convertAsBytes(delimiter, charset));
		this.setDelimiterAsStringCharset(charset);
	}
    Charset charset = Charset.defaultCharset();
    @Override
    public void setDelimiterAsStringCharset(Charset charset) {
        this.charset = null == charset ? Charset.defaultCharset(): charset;
    }
    @Override
    public Charset getDelimiterAsStringCharset() {
        return this.charset = null == charset ? Charset.defaultCharset(): charset;
    }

    /**
     * 如果最后一次next返回的是分隔符 则return >= 0
     */
    @Override
    public boolean lastIsReadReadSeparator(){
        return SEPARATOR_INDEX_UNSET != this.separatorIndex;
    }
    @Override
    public int lastReadSeparatorIndex() {
        return this.separatorIndex;
    }
    public int     lastReadSeparatorLength() {
        return separatorIndex == SEPARATOR_INDEX_UNSET ? 0: separators[separatorIndex].length;
    }
    @Override
    public byte[] lastReadSeparator() {
        return separatorIndex == SEPARATOR_INDEX_UNSET ? null: separators[separatorIndex].clone();
    }

    /**
     * 返回分隔符 或者分隔符之前的内容
     */
    @Override
    public byte[] readNext() {
        this.separatorIndex = SEPARATOR_INDEX_UNSET;
        if (this.pos >= this.count) {
            return null;
        } else {
			Delimiter.IBytesDelimiter delimiter = this.delimiter;
			byte[] data  = this.buf;
			int last     = this.pos, offset = last, limit = this.count;
			int sIndex;
			for (;offset < limit; offset++) {
				if ((sIndex = delimiter.assertSeparator(data, offset, limit)) != -1) {
					if (offset == last) { // is separator
						byte[] array = separators[sIndex].clone();
						this.separatorIndex = sIndex;
						this.pos = last + array.length;
						return array;
					} else break;
				}
			}
			byte[] array = new byte[offset - last];
			Arrayz.arraycopy(data, last, array, 0, array.length);
			this.pos = offset;
			return array;
		}
    }
    //content + (resultAddSeparator?separator:[])
    @Override
    public byte[] readNextLine(boolean resultAddSeparator) {
        this.separatorIndex = SEPARATOR_INDEX_UNSET;
        if (this.pos >= this.count) {
            return null;
        } else {
            Delimiter.IBytesDelimiter delimiter = this.delimiter;
            byte[] data  = this.buf;
            int last     = this.pos, offset = last, limit = this.count;
            int sIndex;
            for (;offset < limit; offset++) {
                if ((sIndex = delimiter.assertSeparator(data, offset, limit)) != -1) {
                    int nextIndex = offset + separators[sIndex].length;
                    byte[] array = new byte[((resultAddSeparator ? nextIndex: offset) - last)];
                    if (array.length > 0)
                        Arrayz.arraycopy(data, last, array, 0, array.length);
                    this.separatorIndex = sIndex;
                    this.pos = nextIndex;
                    return array;
                }
            }
            byte[] array = new byte[offset - last];
            Arrayz.arraycopy(data, last, array, 0, array.length);
            this.pos = offset;
            return array;
        }
    }




//------------copy---------
    @Override
    public String readNextAsString() {
        this.separatorIndex = SEPARATOR_INDEX_UNSET;
        if (this.pos >= this.count) {
            return null;
        } else {
			Delimiter.IBytesDelimiter delimiter = this.delimiter;
			byte[] data  = this.buf;
			int last     = this.pos, offset = last, limit = this.count;
			int sIndex;
			for (;offset < limit; offset++) {
				if ((sIndex = delimiter.assertSeparator(data, offset, limit)) != -1) {
					if (offset == last) { // is separator
						String array = newString(separators[sIndex]);
						this.separatorIndex = sIndex;
						this.pos = last + array.length();
						return array;
					} else break;
				}
			}
			String array = newString(data, last, offset - last);
			this.pos = offset;
			return array;
		}
    }
    //content + (resultAddSeparator?separator:[])
    @Override
    public String readNextLineAsString(boolean resultAddSeparator) {
        this.separatorIndex = SEPARATOR_INDEX_UNSET;
        if (this.pos >= this.count) {
            return null;
        } else {
            Delimiter.IBytesDelimiter delimiter = this.delimiter;
            byte[] data  = this.buf;
            int last     = this.pos, offset = last, limit = this.count;
            int sIndex;
            for (;offset < limit; offset++) {
                if ((sIndex = delimiter.assertSeparator(data, offset, limit)) != -1) {
                    int nextIndex = offset + separators[sIndex].length;
                    String array = newString(data, last, ((resultAddSeparator ? nextIndex: offset) - last));
                    this.separatorIndex = sIndex;
                    this.pos = nextIndex;
                    return array;
                }
            }
            String array = newString(data, last, offset - last);
            this.pos = offset;
            return array;
        }
    }
    String newString(byte[] data) {
        return new String(data, charset);
    }
    String newString(byte[] data, int offset, int count) {
        return new String(data, offset, count, charset);
    }
//------------copy---------

    public byte[] getBuffer(int index, int len) {
        if (len <= 0)
            return Finals.EMPTY_BYTE_ARRAY;

        byte[] arr = new byte[len];
        Arrayz.arraycopy(this.buf, index, arr, 0, len);
        return arr;
    }
    public byte[] getBuffer(int len) {
        return getBuffer(this.pos, len);
    }

    public byte[] subBuffer(int startIndex, int end) {
        int len = end - startIndex;
        if (len <= 0)
            return Finals.EMPTY_BYTE_ARRAY;

        return getBuffer(startIndex, len);
    }

    public void getBuffer(int startIndex, byte[] to, int toIndex, int len) {
        Arrayz.arraycopy(this.buf, startIndex, to, toIndex, len);
    }
}


