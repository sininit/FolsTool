package top.fols.box.util;

public class CharSequenceWrap implements CharSequence {
    int start;
    int length;
    CharSequence content;
    public CharSequenceWrap(CharSequence content) {
        this.content = content;
        this.start = 0;
        this.length = content.length();
    }
    CharSequenceWrap() {}

    protected int beginIndex() { return start; }

    @Override
    public int length() {
        // TODO: Implement this method
        return length;
    }

    @Override
    public char charAt(int p1) {
        // TODO: Implement this method
        if (p1 < 0 || p1 > length)
            throw new ArrayIndexOutOfBoundsException("length=" + length + ", index=" + p1);
        return content.charAt(start + p1);
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        // TODO: Implement this method
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        if (endIndex < 0) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if (endIndex > this.length) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }

        int subLen = endIndex - beginIndex;
        if (subLen < 0) {
            throw new StringIndexOutOfBoundsException(subLen);
        }

        CharSequenceWrap n = new CharSequenceWrap();
        n.content = this.content;
        n.start   = this.start + beginIndex;
        n.length  = subLen;
        return n;
    }
    public CharSequence subSequence(int beginIndex) {
        return subSequence(beginIndex, length());
    }

    @Override
    public String toString() {
        // TODO: Implement this method
        char[] data = new char[length];
        for (int i = 0; i < length;i++)
            data[i] = charAt(i);
        return new String(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharSequence)) return false;

        CharSequence that = (CharSequence) o;

        if (length != that.length()) return false;
        for (int i = 0; i < length; i++) {
            if (charAt(i) != that.charAt(i)) return false;
        }

        return true;
    }

    int     hash;
    boolean hashed;
    @Override
    public int hashCode() {
        int h = hash;
        if (!hashed) {
             hash   = h = toString().hashCode();
             hashed = true;
        }
        return h;
    }
}
