package top.fols.box.lang.impl.sequences;

import java.io.Serializable;
import java.util.Arrays;
import top.fols.box.lang.interfaces.XInterfacesSequence;
import top.fols.box.statics.XStaticFixedValue;

public class XShortSequenceImpl extends XInterfacesSequence<short[],Short> implements Serializable {
	// 序列版本号  
    private static final long serialVersionUID = 2913154437796280545L;  

	private short[] elementData;
	public XShortSequenceImpl(short[] array) {
		this.elementData = array == null ?XStaticFixedValue.nullshortArray: array;
	}

	@Override
	public Short get(int index) {
		return this.elementData[index];
	}
	@Override
	public void set(int index, Short newElement) {
		this.elementData[index] = newElement;
	}
	@Override
	public int length() {
		return this.elementData.length;
	}
	@Override
	public short[] getArray() {
		// TODO: Implement this method
		return this.elementData;
	}
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.elementData = XStaticFixedValue.nullshortArray;
	}
	@Override
	public XShortSequenceImpl clone() {
		// TODO: Implement this method
		XShortSequenceImpl newObj = new XShortSequenceImpl(Arrays.copyOf(this.elementData, this.elementData.length));
		return newObj;
	}
	@Override
	public void setArray(short[] newArray) {
		// TODO: Implement this method
		this.elementData = newArray;
	}
	@Override
	public boolean equals(int index, Object value) {
		// TODO: Implement this method
		if (null == value) return false;
		if (value instanceof Short) return elementData[index] == ((Short)value).shortValue();
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
