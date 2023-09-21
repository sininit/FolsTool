package top.fols.atri.io;

import top.fols.atri.lang.Finals;
import top.fols.box.util.Arrayx;
import top.fols.box.util.Arrayy;

import static top.fols.atri.io.BytesCodeInputStreams.SEPARATOR_INDEX_UNSET;

public class CharsSeparatorReader {
    char[] content;
    char[] separators;
    int    separatorIndex = SEPARATOR_INDEX_UNSET;

    int prev;
    int last;
    int length;

    public CharsSeparatorReader(char[] content) {
        this(content, Finals.Separator.LINE_SEPARATOR_CHAR_N);
    }
    public CharsSeparatorReader(char[] content, char...   separator) {
        this.setContent(content, separator);
    }

    protected void setContent(char[] content,  char...   separator) {
        this.content   = content;
        this.separators = separator;

        this.prev = -1;
        this.last = 0;
        this.length = content.length;
    }
    public char[] sub(int start, int end) {
        return Arrayx.subArray(content, start, end);
    }
    public int indexOf(char search, int start) {
        return Arrayy.indexOf(content, search, start, content.length);
    }


    public boolean hasNext() {
        return last < length;
    }



    public char[] next(boolean addSeparator) {
        if (!(last < length)) { return null; }

        prev = last;
        separatorIndex = SEPARATOR_INDEX_UNSET;

        int offset = -1;
        int i = 0;
        for (; i < separators.length; i++) {
            char separator = separators[i];
            if ((offset = indexOf(separator, last)) != -1) {
                break;
            }
        }
        if (offset == -1) {
            offset = length;
        } else {
            separatorIndex = i;
        }
        char[] element = sub(last, addSeparator?(offset>=length?offset:offset+1):offset);
        last = offset == length?length:offset + 1;
        return element;
    }

    public char[] getNext(boolean addSeparator) {
        return getNext(last, addSeparator);
    }
    public char[] getNext(int offIndex, boolean addSeparator) {
        if (!(offIndex < length)) { return null; }

        int offset = -1;
        int i = 0;
        for (; i < separators.length; i++) {
            char separator = separators[i];
            if ((offset = indexOf(separator, offIndex)) != -1) {
                break;
            }
        }
        if (offset == -1) {
            offset = length;
        }
        char[] element = sub(offIndex, addSeparator?(offset>=length?offset:offset+1):offset);
        return element;
    }


    public int position_prev() {
        return prev;
    }
    public int position()  {
        return last;
    }


    public boolean separator() {
        return separatorIndex != SEPARATOR_INDEX_UNSET;
    }


    public char    separatorChar() {
        if (SEPARATOR_INDEX_UNSET == separatorIndex) {
            throw new RuntimeException("No separator found");
        }
        return separators[separatorIndex];
    }
    public boolean separatorChar(char equals) {
        return separatorIndex != SEPARATOR_INDEX_UNSET && separators[separatorIndex] == equals;
    }
    public int     separatorSize() {
        return 1;
    }


    public int length(){
        return length;
    }
    public char[] getContent() {
        return content;
    }
}
