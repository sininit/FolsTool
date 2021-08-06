package top.fols.atri.io;

import top.fols.atri.io.buffer.BufferFilter;
import top.fols.atri.io.buffer.chars.CharArrayBuffer;
import top.fols.atri.io.buffer.chars.CharBufferFilter;
import top.fols.atri.lang.Arrayz;

import java.util.Arrays;

public class StringLineReader {
    String content;
    CharArrayBuffer     operate;
    CharBufferFilter    filter;

    int prev;
    int last;
    int length;

    public StringLineReader(String content) {
        this.setContent(content, BufferFilter.lineFilterChars());
    }
    public StringLineReader(String content, String... separator) {
        this.setContent(content, BufferFilter.filterChars(separator));
    }
    public StringLineReader(String content, char[]... separator) {
        this.setContent(content, BufferFilter.filterChars(separator));
    }


    protected void setContent(String content, CharBufferFilter filter) {
        this.content   = content;
        this.filter = filter;

        this.prev = -1;
        this.last = 0;
        this.operate = new CharArrayBuffer(content.toCharArray());
        this.length = content.length();
    }



    public boolean hasNext() {
        return operate.position() < operate.limit();
    }

    public String next() {
        if (!(operate.position() < operate.limit())) { return null; }
        prev = last;
        try {
            boolean end    = operate.readFilterIFEnd(filter);
            String  result = end?null:filter.resultString(false);
            last = operate.position();
            return  result;
        } catch (Throwable e) {
            // theoretically, IO exception cannot occur
            throw new RuntimeException("internal error");
        }
    }
    public String next(boolean addSeparator) {
        if (!(operate.position() < operate.limit())) { return null; }
        prev = last;
        try {
            boolean end    = operate.readFilterIFEnd(filter);
            String  result = end?null:filter.resultString(addSeparator);
            last = operate.position();
            return  result;
        } catch (Throwable e) {
            // theoretically, IO exception cannot occur
            throw new RuntimeException("internal error");
        }
    }




    public int position_prev() {
        return prev;
    }
    public int position()  {
        return last;
    }


    public boolean separator() {
        return filter.contentReadToSeparator();
    }


    protected CharArrayBuffer  operate() {
        return operate;
    }
    protected CharBufferFilter filter()  {
        return filter;
    }

    /**
     * @return Do not modify the returned separator !!!
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public char[] separatorChars() {
        char[] chars = filter.contentSeparator();
        return chars;
    }
    public boolean separatorChars(char[] equals) {
        char[]      chars = separatorChars();
        if (null == chars) {
            return null == equals;
        } else {
            return Arrays.equals(chars, equals);
        }
    }


    public int    separatorSize() {
        char[]         chars = separatorChars();
        return null == chars?0:chars.length;
    }




    public int length() {
        return length;
    }
    public String getContent() {
        return content;
    }
}
