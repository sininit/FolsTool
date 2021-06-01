package top.fols.atri.lang;

import top.fols.atri.util.Releasable;
import java.io.Serializable;

public class Value<T> implements Releasable, Serializable {
    private static final long serialVersionUID = 1L;
    private T obj;



    @Override public boolean release() { return null == (this.obj = null); }
    @Override public boolean released() { return null == this.obj; }

    public boolean isNull() { return null == this.obj; }

    public Value() { }
    public Value(T o) {
        this.obj = o;
    }

    public T get() {
        return this.obj;
    }
    public T set(T new_obj) {
        return this.obj = new_obj;
    }

    public int      hashCode() {
        return null == this.obj ? 0 : this.obj.hashCode();
    }
    public boolean  equals(Object obj) {
        return Objects.equals(this.obj, obj);
    }
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




    public static  Value<Object> NULL() {
        return new Value<Object>();
    }
    public static  Value<Boolean> TRUE() {
        Value<Boolean> result = new Value<Boolean>();
        result.set(true);
        return result;
    }
    public static  Value<Boolean> FALSE() {
        Value<Boolean> result = new Result<>();
        result.set(false);
        return result;
    }
}