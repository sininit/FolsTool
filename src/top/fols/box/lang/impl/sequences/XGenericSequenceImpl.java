package top.fols.box.lang.impl.sequences;

import java.io.Serializable;
import java.util.Arrays;
import top.fols.box.lang.interfaces.XInterfacesSequence;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XObjects;

public class XGenericSequenceImpl<A extends Object> extends XInterfacesSequence<A[],A> implements Serializable {
	// 序列版本号  
    private static final long serialVersionUID = 2913154437796280545L;  

	private A[] elementData;
	public XGenericSequenceImpl(A[] array) {
		this.elementData = array;
	}
	@Override
	public A get(int index) {
		return null == this.elementData ?null : this.elementData[index];
	}
	@Override
	public void set(int index, A newElement) {
		this.elementData[index] = newElement;
	}
	@Override
	public int length() {
		return null == this.elementData ?0: this.elementData.length;
	}
	@Override
	public A[] getArray() {
		// TODO: Implement this method
		return this.elementData;
	}
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.elementData = null;
	}
	@Override
	public XGenericSequenceImpl clone() {
		// TODO: Implement this method
		XGenericSequenceImpl newObj = new XGenericSequenceImpl(null == this.elementData ?null: Arrays.copyOf(this.elementData, this.elementData.length));
		return newObj;
	}
	@Override
	public void setArray(A[] newArray) {
		// TODO: Implement this method
		this.elementData = newArray;
	}
	@Override
	public boolean equals(int index, Object value) {
		// TODO: Implement this method
		return null == this.elementData[index] ?null == value: this.elementData[index].equals(value);
	}
	@Override
	public boolean isArray(int index) {
		// TODO: Implement this method
		return null == this.elementData ?false: (null == this.elementData[index] ?false: this.elementData[index].getClass().isArray());
	}
	@Override
	public int hashCode() {
		// TODO: Implement this method
		return null == this.elementData ?0: Arrays.hashCode(this.elementData);
	}
}
