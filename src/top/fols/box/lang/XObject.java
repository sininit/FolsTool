package top.fols.box.lang;
import top.fols.box.io.interfaces.XInterfereReleaseBufferable;
/*
 * Save the object
 */
public class XObject <T extends Object> implements XInterfereReleaseBufferable {
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.obj = null;
	}
	private T obj;
	public XObject() {
		super();
	}
	public XObject(T o) {
		this.obj = o;
	}
	public T get() {
		return this.obj;
	}
	public T set(T newobj) {
		return this.obj = newobj;
	}
	@Override
	public int hashCode() {
		// TODO: Implement this method
		return null == this.obj ?0: this.obj.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		return null == this.obj ?null == obj: this.obj.equals(obj);
	}
	@Override
	public String toString() {
		// TODO: Implement this method
		return null == this.obj ?null: this.obj.toString();
	}
	
	
	
	public int superHashCode() {
		// TODO: Implement this method
		return super.hashCode();
	}
	public boolean superEquals(Object o) {
		// TODO: Implement this method
		return super.equals(o);
	}
	public String superToString() {
		// TODO: Implement this method
		return super.toString();
	}
}
