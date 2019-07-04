package top.fols.box.lang.impl.sequences;

import java.io.Serializable;
import java.util.Arrays;
import top.fols.box.lang.interfaces.XInterfacesSequence;
import top.fols.box.statics.XStaticFixedValue;

public class XIntSequenceImpl extends XInterfacesSequence<int[],Integer> implements Serializable{
	// 序列版本号  
    private static final long serialVersionUID = 2913154437796280545L;  
	
	private int[] elementData;
	public XIntSequenceImpl(int[] array) {
		this.elementData = array == null ?XStaticFixedValue.nullintArray: array;
	}

	@Override
	public Integer get(int index) {
		return this.elementData[index];
	}
	@Override
	public void set(int index, Integer newElement) {
		this.elementData[index] = newElement;
	}
	@Override
	public int length() {
		return this.elementData.length;
	}
	@Override
	public int[] getArray() {
		// TODO: Implement this method
		return this.elementData;
	}
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.elementData = XStaticFixedValue.nullintArray;
	}
	@Override
	public XIntSequenceImpl clone() {
		// TODO: Implement this method
		XIntSequenceImpl newObj = new XIntSequenceImpl(Arrays.copyOf(this.elementData, this.elementData.length));
		return newObj;
	}
	@Override
	public void setArray(int[] newArray) {
		// TODO: Implement this method
		this.elementData = newArray;
	}
	@Override
	public boolean equals(int index, Object value) {
		// TODO: Implement this method
		if (null == value) return false;
		if (value instanceof Integer) return elementData[index] == ((Integer)value).intValue();
		return false;
	}
	@Override
	public boolean isArray(int index) {
		// TODO: Implement this method
		return false;
	}
	@Override
	public int hashCode() {
		// TODO: Implement this method
		return Arrays.hashCode(elementData);
	}
}
