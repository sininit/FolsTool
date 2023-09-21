package top.fols.atri.lang;

import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.atri.interfaces.interfaces.IValue;

import java.io.Serializable;

@SuppressWarnings("ConstantConditions")
public class Value<T> implements IReleasable, IValue<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private T obj;



    @Override public boolean release()  { return null == (this.obj = null); }
    @Override public boolean released() { return null == this.obj; }

    public boolean isNull() { return null == this.obj; }

    public Value() { }
    public Value(T o) {
        this.obj = o;
    }

    @Override
    public T get() {
        return this.obj;
    }
    public Value<T> set(T new_obj) {
        this.obj = new_obj;
        return this;
    }

    @Override
    public int      hashCode() {
        return null == this.obj ? 0 : this.obj.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IValue)) return Objects.equals(obj, o);

        IValue<?> value = (IValue<?>) o;
        return Objects.equals(obj, value.get());
    }

    @Override
    public String   toString() {
        return null == this.obj ? null : this.obj.toString();
    }



    public int      innerHashCode() {
        return super.hashCode();
    }
    public boolean  innerEquals(Object o) {
        return super.equals(o);
    }
    public String   innerToString() {
        return super.toString();
    }





    public static <T> Value<T> wrap(T v) {
        return new Value<>(v);
    }



    public Value<T> from(Value<T> from) {
        if (null == from) {
            this.set(null);
        } else {
            this.set(from.get());
        }
        return this;
    }



    public static <T> boolean hasValue(Value<T> value) {
        return null != value;
    }
    public static <T> T get(Value<T> value) {
        return null == value?null:value.get();
    }
}