package top.fols.box.reflect.proxyx;
import java.util.Objects;

public class ProxyReturn {
	Object value;
	boolean isReturn;
	//Throwable ex;

	public ProxyReturn(Object value) {
		this.value = value;
	}
	public ProxyReturn() {}


	public void setReturn(Object value) {
		this.value    = value;
		this.isReturn = true;
		//this.ex       = null;
	}
	public Object getReturn() {
		return value;
	}

	public boolean isReturn() {
		return this.isReturn;
	}
	public boolean isNonReturn() {
		return !this.isReturn;
	}

//	public void setThrowable(Throwable e) {
//		this.value = null;
//		this.isReturn = false;
//		this.ex = e;
//	}
//	public Throwable getThrowable() {
//		return this.ex;
//	}
//	public boolean isThrow() {
//		return null != this.ex;
//	}


	@Override
	public String toString() {
		// TODO: Implement this method
		return String.valueOf(value);
	}

	@Override
	public int hashCode() {
		// TODO: Implement this method
		return null == value ?0: value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (null == obj) return false;
		if (obj == this) return true;
		if (obj instanceof      ProxyReturn) {
			ProxyReturn that = (ProxyReturn) obj;
			return Objects.equals(value, that.value);
		}
		return false;
	}
}



