package top.fols.atri.lang;

public class StringLineReader {
    String content;
    char   separator;

    int prev;
    int last;
    int length;

    public StringLineReader(String content) {
        this(content, Finals.LINE_SEPARATOR_CHAR_N);
    }
    public StringLineReader(String content, char   separator) {
        this.setContent(content, separator);
    }

    protected void setContent(String content, char   separator) {
        this.content   = content;
        this.separator = separator;

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

        int offset = content.indexOf(separator, last);
        if (offset == -1) {
            offset = length;
        }
        String element = content.substring(last, offset);
        last = offset + 1;
        return element;
    }
    public String next(boolean addSeparator) {
        if (!(last < length)) { return null; }

        prev = last;

        int offset = content.indexOf(separator, last);
        if (offset == -1) {
            offset = length;
        }
        String element = content.substring(last, addSeparator?(offset>=length?offset:offset+1):offset);
        last = offset + 1;
        return element;
    }




    public int position_prev() {
        return prev;
    }
    public int position()  {
        return last;
    }


    public boolean separator() {
        return last - 1 < length && content.charAt(last - 1) == separator;
    }

    public char    separatorChar() {
        return separator;
    }
    public char    separatorSize() {
        return 1;
    }
}
