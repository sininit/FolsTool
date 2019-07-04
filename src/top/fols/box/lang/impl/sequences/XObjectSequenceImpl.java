package top.fols.box.lang.impl.sequences;

import java.io.Serializable;
import java.util.Arrays;
import top.fols.box.lang.interfaces.XInterfacesSequence;
import top.fols.box.statics.XStaticFixedValue;

public class XObjectSequenceImpl extends XInterfacesSequence<Object[],Object> implements Serializable {
	// 序列版本号  
    private static final long serialVersionUID = 2913154437796280545L;  

	private Object[] elementData;
	public XObjectSequenceImpl(Object[] array) {
		this.elementData = array == null ?XStaticFixedValue.nullObjectArray: array;
	}
	@Override
	public Object get(int index) {
		return this.elementData[index];
	}
	@Override
	public void set(int index, Object newElement) {
		this.elementData[index] = newElement;
	}
	@Override
	public int length() {
		return this.elementData.length;
	}
	@Override
	public Object[] getArray() {
		// TODO: Implement this method
		return this.elementData;
	}
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.elementData = XStaticFixedValue.nullObjectArray;
	}
	@Override
	public XObjectSequenceImpl clone() {
		// TODO: Implement this method
		XObjectSequenceImpl newObj = new XObjectSequenceImpl(Arrays.copyOf(this.elementData, this.elementData.length));
		return newObj;
	}
	@Override
	public void setArray(Object[] newArray) {
		// TODO: Implement this method
		this.elementData = newArray;
	}
	@Override
	public boolean equals(int index, Object value) {
		// TODO: Implement this method
		return null == elementData[index] ?null == value: elementData[index].equals(value);
	}
	@Override
	public boolean isArray(int index) {
		// TODO: Implement this method
		return null == elementData[index] ?false: elementData[index].getClass().isArray();
	}
	@Override
	public int hashCode() {
		// TODO: Implement this method
		return Arrays.hashCode(elementData);
	}
}
