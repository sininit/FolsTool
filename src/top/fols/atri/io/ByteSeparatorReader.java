package top.fols.atri.io;

import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Finals;
import top.fols.box.util.XArray;
import top.fols.box.util.XArrays;

public class ByteSeparatorReader {
    byte[] content;
    byte[] separators;
    int    separatorIndex = -1;

    int prev;
    int last;
    int length;

    public ByteSeparatorReader(byte[] content) {
        this(content, Finals.Separator.LINE_SEPARATOR_BYTE_N);
    }
    public ByteSeparatorReader(byte[] content, byte...   separator) {
        this.setContent(content, separator);
    }

    protected void setContent(byte[] content, byte...   separator) {
        this.content   = content;
        this.separators = separator;

        this.prev = -1;
        this.last = 0;
        this.length = content.length;
    }

    public byte[] sub(int start, int end) {
        return XArray.subArray(content, start, end);
    }
    public int indexOf(byte search, int start) {
        return XArrays.indexOf(content, search, start, content.length);
    }

    public boolean hasNext() {
        return last < length;
    }


    public byte[] next() {
        if (!(last < length)) { return null; }

        prev = last;
        separatorIndex = -1;

        int offset = -1;
        int i = 0;
        for (; i < separators.length; i++) {
            byte separator = separators[i];
            if ((offset = indexOf(separator, last)) != -1) {
                break;
            }
        }
        if (offset == -1) {
            offset = length;
        } else {
            separatorIndex = i;
        }
        byte[] element = sub(last, offset);
        last = offset == length?length:offset + 1;
        return element;
    }
    public byte[] next(boolean addSeparator) {
        if (!(last < length)) { return null; }

        prev = last;
        separatorIndex = -1;

        int offset = -1;
        int i = 0;
        for (; i < separators.length; i++) {
            byte separator = separators[i];
            if ((offset = indexOf(separator, last)) != -1) {
                break;
            }
        }
        if (offset == -1) {
            offset = length;
        } else {
            separatorIndex = i;
        }
        byte[] element = sub(last, addSeparator?(offset>=length?offset:offset+1):offset);
        last = offset == length?length:offset + 1;
        return element;
    }




    public int position_prev() {
        return prev;
    }
    public int position()  {
        return last;
    }


    public boolean separator() {
        return separatorIndex != -1;
    }



    static byte NOT_FOUND;


    public byte    separatorChar() {
        return separatorIndex != -1 ?  separators[separatorIndex]:NOT_FOUND;
    }
    public boolean separatorChar(char equals) {
        return separatorIndex != -1 && separators[separatorIndex] == equals;
    }
    public int     separatorSize() {
        return 1;
    }


    public int length(){
        return length;
    }
    public byte[] getContent() {
        return content;
    }
}
