package top.fols.atri.io;

import java.io.IOException;
import java.io.Reader;

import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.box.io.interfaces.IPrivateBuffOperat;
import top.fols.box.io.interfaces.IPrivateStreamIndexOperat;

import static top.fols.atri.io.BytesInputStreams.SEPARATOR_INDEX_UNSET;
import top.fols.atri.lang.Arrayz;

//-------copy------
public class StringReaders extends Reader implements IPrivateBuffOperat<String>,
IPrivateStreamIndexOperat, IReleasable, Delimiter.ICharsDelimiterReader {


    private String buf;
    private int pos;
    private int markedPos = 0;
    private int count;

    public StringReaders() {
        this("");
    }

    public StringReaders(String buf) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length();
    }

    public StringReaders(String buf, int offset, int length) {
        if ((offset < 0) || (offset > buf.length()) || (length < 0) || ((offset + length) < 0)) {
            throw new IllegalArgumentException();
        }
        this.buf = buf;
        this.pos = offset;
        this.count = Math.min(offset + length, buf.length());
        this.markedPos = offset;
    }

    
    private void ensureOpen() throws IOException {
        if (null == buf)
            throw new IOException("Stream closed");
    }

    @Override
    public int read() throws IOException {
        ensureOpen();
        if (pos >= count)
            return -1;
        else
            return buf.charAt(pos++);
    }

    
    @Override
    public int read(char b[], int off, int len) throws IOException {
        ensureOpen();
        if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        
        if (pos >= count) {
            return -1;
        }
        if (pos + len > count) {
            len = count - pos;
        }
        if (len <= 0) {
            return 0;
        }
        Arrayz.arraycopy(buf, pos, b, off, len);
        pos += len;
        return len;
    }



    
    @Override
    public long skip(long n) throws IOException {
        ensureOpen();
        if (pos + n > count) {
            n = count - pos;
        }
        if (n < 0) {
            return 0;
        }
        pos += n;
        return n;
    }

    
    @Override
    public boolean ready() throws IOException {
        ensureOpen();
        return (count - pos) > 0;
    }

    
    @Override
    public boolean markSupported() {
        return true;
    }

    
    @Override
    public void mark(int readAheadLimit) throws IOException {
        ensureOpen();
        markedPos = pos;
    }

    
    @Override
    public void reset() throws IOException {
        ensureOpen();
        pos = markedPos;
    }

    /**
     * 关闭、清空buf。
     */
    @Override
    public void close() {
        release();
    }







    @Override
    public String buffer() {
        return buf;
    }

    @Override
    public int buffer_length() {
        // TODO: Implement this method
        return buf.length();
    }

    @Override
    public void buffer(String newBuff, int size) {
        // TODO: Implement this method
        this.buf = null == newBuff ? "" : newBuff;
        this.buffer_length(size);
    }

    @Override
    public void buffer_length(int size) throws ArrayIndexOutOfBoundsException {
        if (size > buf.length())
            throw new ArrayIndexOutOfBoundsException("arrayLen=" + buf.length() + ", setLen=" + size);
        count = size;
    }

    @Override
    public void seekIndex(int index) {
        if (!(index > -1 && index <= count))
            throw new ArrayIndexOutOfBoundsException("can't set pos index=" + index + " length=" + count);
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
        return  (null == buf || buf.length() == 0) &&
			(null == delimiter && separatorIndex == SEPARATOR_INDEX_UNSET);
    }





    @Override
    public boolean findNext() {
        return (count - pos) > 0;
    }
    public boolean hasNext(int pos) {
        return (count - pos) > 0;
    }

	private Delimiter.ICharsDelimiter delimiter;
    private char[][] separators;
    private int separatorIndex = SEPARATOR_INDEX_UNSET;

    @Override
    public Delimiter.ICharsDelimiter getDelimiter() { return this.delimiter; }
    @Override
    public void setDelimiter(Delimiter.ICharsDelimiter delimiter) {
	    this.delimiter  = delimiter;
		this.separators = this.delimiter.innerSeparators();
	}
    @Override
    public void setDelimiter(char[][] delimiter) {
	    setDelimiter(Delimiter.build(delimiter));
	}
    @Override
    public void setDelimiter(String[] delimiter) {
        setDelimiter(Delimiter.build(delimiter));
    }
    @Override
    public void setDelimiterAsLine() {
        setDelimiter(Delimiter.lineCharDelimit());
    }




    /**
     * 如果最后一次next返回的是分隔符 则return >= 0
     */
    @Override
    public boolean lastIsReadReadSeparator() {
        return SEPARATOR_INDEX_UNSET != this.separatorIndex;
    }
    @Override
    public int     lastReadSeparatorIndex() {
        return this.separatorIndex;
    }
    public int     lastReadSeparatorLength() {
        return separatorIndex == SEPARATOR_INDEX_UNSET ? 0: separators[separatorIndex].length;
    }
    @Override
    public char[]  lastReadSeparator() {
        return separatorIndex == SEPARATOR_INDEX_UNSET ? null: separators[separatorIndex].clone();
    }

    /**
     * 返回分隔符 或者分隔符之前的内容
     */
    @Override
    public char[] readNext() {
        this.separatorIndex = SEPARATOR_INDEX_UNSET;
        if (this.pos >= this.count) {
            return null;
        } else {
			Delimiter.ICharsDelimiter delimiter = this.delimiter;
			String data  = this.buf;
			int last     = this.pos, offset = last, limit = this.count;
			int sIndex;
			for (;offset < limit; offset++) {
				if ((sIndex = delimiter.assertSeparator(data, offset, limit)) != -1) {
					if (offset == last) { // is separator
						char[] array = separators[sIndex].clone();
						this.separatorIndex = sIndex;
						this.pos = last + array.length;
						return array;
					} else break;
				}
			}
			char[] array = new char[offset - last];
			Arrayz.arraycopy(data, last, array, 0, array.length);
			this.pos = offset;
			return array;
		}
    }
    //content + (resultAddSeparator?separator:[])
    @Override
    public char[] readNextLine(boolean resultAddSeparator) {
        this.separatorIndex = SEPARATOR_INDEX_UNSET;
        if (this.pos >= this.count) {
            return null;
        } else {
            Delimiter.ICharsDelimiter delimiter = this.delimiter;
            String data  = this.buf;
            int last     = this.pos, offset = last, limit = this.count;
            int sIndex;
            for (;offset < limit; offset++) {
                if ((sIndex = delimiter.assertSeparator(data, offset, limit)) != -1) {
                    int nextIndex = offset + separators[sIndex].length;
                    char[] array = new char[((resultAddSeparator ? nextIndex: offset) - last)];
                    if (array.length > 0)
                        Arrayz.arraycopy(data, last, array, 0, array.length);
                    this.separatorIndex = sIndex;
                    this.pos = nextIndex;
                    return array;
                }
            }
            char[] array = new char[offset - last];
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
			Delimiter.ICharsDelimiter delimiter = this.delimiter;
			String data  = this.buf;
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
            Delimiter.ICharsDelimiter delimiter = this.delimiter;
            String data  = this.buf;
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
    String newString(char[] data) {
        return new String(data);
    }
    String newString(String data, int offset, int count) {
        return data.substring(offset, offset + count);
    }
//------------copy---------



	
    public char[] getBuffer(int index, int len) {
        if (len <= 0)
            return Finals.EMPTY_CHAR_ARRAY;

        char[] arr = new char[len];
        Arrayz.arraycopy(this.buf, index, arr, 0, len);
        return arr;
    }
    public char[] getBuffer(int len) {
        return getBuffer(this.pos, len);
    }

    public char[] subBuffer(int startIndex, int end) {
        int len = end - startIndex;
        if (len <= 0)
            return Finals.EMPTY_CHAR_ARRAY;

        return getBuffer(startIndex, len);
    }

    public void getBuffer(int startIndex, char[] to, int toIndex, int len) {
        Arrayz.arraycopy(this.buf, startIndex, to, toIndex, len);
    }

}



