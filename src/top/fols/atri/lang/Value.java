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
}