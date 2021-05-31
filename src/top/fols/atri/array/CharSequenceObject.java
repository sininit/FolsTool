package top.fols.atri.array;

import java.util.AbstractList;

public class CharSequenceObject <T extends CharSequence> extends ArrayObject<T> {
    public CharSequenceObject(T list) {
        super(list);
    }

    @Override
    public byte byteValue(int index) {
        // TODO: Implement this method
        return (byte) super.array.charAt(index);
    }

    @Override
    public long longValue(int index) {
        // TODO: Implement this method
        return (long) super.array.charAt(index);
    }

    @Override
    public double doubleValue(int index) {
        // TODO: Implement this method
        return (double) super.array.charAt(index);
    }

    @Override
    public char charValue(int index) {
        // TODO: Implement this method
        return (char) super.array.charAt(index);
    }

    @Override
    public int intValue(int index) {
        // TODO: Implement this method
        return (int) super.array.charAt(index);
    }

    @Override
    public float floatValue(int index) {
        // TODO: Implement this method
        return (float) super.array.charAt(index);
    }

    @Override
    public short shortValue(int index) {
        // TODO: Implement this method
        return (short) super.array.charAt(index);
    }



    @Override
    public boolean booleanValue(int index) {
        // TODO: Implement this method
        return super.array.charAt(index) != 0;
    }

    @Override
    public Character objectValue(int index) {
        // TODO: Implement this method
        return super.array.charAt(index);
    }
    @Override
    public String stringValue(int index) {
        return String.valueOf(super.array.charAt(index));
    }


    @Override public boolean isNull(int index) { return false; }
    @Override public boolean isPrimitive() { return false; }

    @Override
    public int lengthOf(CharSequence array) {
        // TODO: Implement this method
        return array.length();
    }

    @Override
    public int length() {
        // TODO: Implement this method
        return super.array.length();
    }



    public Character get(int index) { return super.array.charAt(index); }

    @Override
    public boolean equals(Object obj) {
        // TODO: Implement this method
        if (this == obj) { return true; }
        if (null == obj) { return false; }
        if (false == ArrayObject.wrapable(obj)) { return false; }

        ArrayObject value = ArrayObject.wrap(obj);
        int valueLength = value.length();
        if (this.length() != valueLength) { return false; }
        for (int index = 0; index < valueLength; index++) {
            if (!this.equals(index, value, index)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void set(int index, Object value) {
        // TODO: Implement this method
        throw new UnsupportedOperationException();
    }
    @Override
    public void set(int index, ArrayObject value, int valueIndex) {
        // TODO: Implement this method
        throw new UnsupportedOperationException();
    }




    @Override
    public boolean equals(int index, ArrayObject value, int index2) {
        // TODO: Implement this method
        return super.array.charAt(index) == value.charValue(index2);
    }


    @Override
    public boolean release() {
        super.array  = null;
        super.length = 0;
        return true;
    }
    @Override
    public boolean released() {
        return null == super.array;
    }
}
