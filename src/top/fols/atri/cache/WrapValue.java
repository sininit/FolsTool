package top.fols.atri.cache;

import top.fols.atri.interfaces.interfaces.IValue;
import top.fols.atri.lang.Objects;

@SuppressWarnings("rawtypes")
public class WrapValue<V> implements IValue<V> {
    public final V value;

    public WrapValue(V v) {
        this.value = v;
    }

    @Override
    public V get() {
        return value;
    }

    @Override
    public int hashCode() {
        // TODO: Implement this method
        return null == value ? 0 : value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // TODO: Implement this method
        return obj instanceof WrapValue &&
                Objects.equals(value, ((WrapValue) obj).value);
    }

    @Override
    public String toString() {
        // TODO: Implement this method
        return String.valueOf(value);
    }
}
