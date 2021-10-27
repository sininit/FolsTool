package top.fols.atri.array;

public class StringBuilderObject <T extends StringBuilder> extends CharSequenceObject<T> {
    public StringBuilderObject(T stringBuilder) {
        super(stringBuilder);
    }

    @Override
    public void set(int index, Object value) {
        super.array.setCharAt(index, (char) value);
    }

    @Override
    public void set(int index, ArrayObject value, int valueIndex) {
        super.array.setCharAt(index, value.charValue(valueIndex));
    }
}

