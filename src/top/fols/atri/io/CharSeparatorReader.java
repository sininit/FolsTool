package top.fols.atri.io;

import top.fols.atri.lang.Finals;

import java.util.Arrays;

public class CharSeparatorReader {
    String content;
    char[] separators;
    int    separatorIndex = -1;

    int prev;
    int last;
    int length;

    public CharSeparatorReader(String content) {
        this(content, Finals.LINE_SEPARATOR_CHAR_N);
    }
    public CharSeparatorReader(String content, char...   separator) {
        this.setContent(content, separator);
    }

    protected void setContent(String content,  char...   separator) {
        this.content   = content;
        this.separators = separator;

        this.prev = -1;
        this.last = 0;
        this.length = content.length();
    }



    public boolean hasNext() {
        return last < length;
    }


    public String next() {
        if (!(last < length)) { return null; }

        prev = last;
        separatorIndex = -1;

        int offset = -1;
        int i = 0;
        for (; i < separators.length; i++) {
            char separator = separators[i];
            if ((offset = content.indexOf(separator, last)) != -1) {
                break;
            }
        }
        if (offset == -1) {
            offset = length;
        } else {
            separatorIndex = i;
        }
        String element = content.substring(last, offset);
        last = offset == length?length:offset + 1;
        return element;
    }
    public String next(boolean addSeparator) {
        if (!(last < length)) { return null; }

        prev = last;
        separatorIndex = -1;

        int offset = -1;
        int i = 0;
        for (; i < separators.length; i++) {
            char separator = separators[i];
            if ((offset = content.indexOf(separator, last)) != -1) {
                break;
            }
        }
        if (offset == -1) {
            offset = length;
        } else {
            separatorIndex = i;
        }
        String element = content.substring(last, addSeparator?(offset>=length?offset:offset+1):offset);
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



    static char NOT_FOUND;


    public char    separatorChar() {
        return separator() ?  separators[separatorIndex]:NOT_FOUND;
    }
    public boolean separatorChar(char equals) {
        return separator() && separators[separatorIndex] == equals;
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
