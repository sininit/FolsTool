package top.fols.box.util;

import top.fols.atri.interfaces.annotations.UnsafeOperate;

public class CharsWrap implements CharSequence {
    int beginIndex;
    int length;
    char[] value;

    public CharsWrap(char[] content) {
        this.value      = content.clone();
        this.beginIndex = 0;
        this.length     = content.length;
    }
    public CharsWrap(char[] content, int begin, int count) {
		if (begin < 0 || count < 0)
			throw new ArrayIndexOutOfBoundsException();
		if (begin > content.length - count)
            throw new ArrayIndexOutOfBoundsException(begin + count);

		this.value      = content.clone();
        this.beginIndex = begin;
        this.length     = count;
	}
	
	@UnsafeOperate
	protected CharsWrap(char[] content, int begin, int count, boolean fromBuffer) {
		if (begin < 0 || count < 0)
			throw new ArrayIndexOutOfBoundsException();
		if (begin > content.length - count)
            throw new ArrayIndexOutOfBoundsException(begin + count);

		this.value      = content;
        this.beginIndex = begin;
        this.length     = count;
	}
	@UnsafeOperate
	private CharsWrap() {}
	
	
    protected int beginIndex() { return beginIndex; }

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
        return value[beginIndex + p1];
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        // TODO: Implement this method
        if (beginIndex < 0)
            throw new StringIndexOutOfBoundsException(beginIndex);
        if (endIndex < 0) 
            throw new StringIndexOutOfBoundsException(endIndex);
        if (endIndex > this.length) 
            throw new StringIndexOutOfBoundsException(endIndex);

        int subLen = endIndex - beginIndex;
        if (subLen < 0) 
            throw new StringIndexOutOfBoundsException(subLen);

        CharsWrap n  = new CharsWrap();
        n.value      = this.value;
        n.beginIndex = this.beginIndex + beginIndex;
        n.length     = subLen;
        return n;
    }
    public CharSequence subSequence(int beginIndex) {
        return subSequence(beginIndex, this.length);
    }

    @Override
    public String toString() {
        // TODO: Implement this method
        return new String(value, beginIndex, this.length);
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

