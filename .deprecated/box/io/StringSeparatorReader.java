package top.fols.atri.io;

import top.fols.atri.lang.Finals;

import static top.fols.atri.io.BytesCodeInputStreams.SEPARATOR_INDEX_UNSET;

public class StringSeparatorReader {
    String content;
    char[] separators;
    int    separatorIndex = SEPARATOR_INDEX_UNSET;

    int prev;
    int last;
    int length;

    public StringSeparatorReader(String content) {
        this(content, Finals.Separator.LINE_SEPARATOR_CHAR_N);
    }
    public StringSeparatorReader(String content, char...   separator) {
        this.setContent(content, separator);
    }

    protected void setContent(String content,  char...   separator) {
        this.content   = content;
        this.separators = separator;

        this.prev = -1;
        this.last = 0;
        this.length = content.length();
    }
    public String sub(int start, int end) {
        return content.substring(start, end);
    }
    public int indexOf(char search, int start) {
        return content.indexOf(search, start);
    }




    public boolean hasNext() {
        return last < length;
    }



    public String next(boolean addSeparator) {
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
        String element = sub(last, addSeparator?(offset>=length?offset:offset+1):offset);
        last = offset == length?length:offset + 1;
        return element;
    }

    public String getNext(boolean addSeparator) {
        return getNext(last, addSeparator);
    }
    public String getNext(int offIndex, boolean addSeparator) {
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
        String element = sub(offIndex, addSeparator?(offset>=length?offset:offset+1):offset);
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



    public char    separatorChar() {
        if (-1 == separatorIndex) {
            throw new RuntimeException("No separator found");
        }
        return separators[separatorIndex];
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
    public String getContent() {
        return content;
    }
}
