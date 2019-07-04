package top.fols.box.lang.impl.sequences;

import java.io.Serializable;
import java.util.Arrays;
import top.fols.box.lang.interfaces.XInterfacesSequence;
import top.fols.box.statics.XStaticFixedValue;

public class XLongSequenceImpl extends XInterfacesSequence<long[],Long> implements Serializable {
	// 序列版本号  
    private static final long serialVersionUID = 2913154437796280545L;  

	private long[] elementData;
	public XLongSequenceImpl(long[] array) {
		this.elementData = array == null ?XStaticFixedValue.nulllongArray: array;
	}

	@Override
	public Long get(int index) {
		return this.elementData[index];
	}
	@Override
	public void set(int index, Long newElement) {
		this.elementData[index] = newElement;
	}
	@Override
	public int length() {
		return this.elementData.length;
	}
	@Override
	public long[] getArray() {
		// TODO: Implement this method
		return this.elementData;
	}
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.elementData = XStaticFixedValue.nulllongArray;
	}
	@Override
	public XLongSequenceImpl clone() {
		// TODO: Implement this method
		XLongSequenceImpl newObj = new XLongSequenceImpl(Arrays.copyOf(this.elementData, this.elementData.length));
		return newObj;
	}
	@Override
	public void setArray(long[] newArray) {
		// TODO: Implement this method
		this.elementData = newArray;
	}
	@Override
	public boolean equals(int index, Object value) {
		// TODO: Implement this method
		if (null == value) return false;
		if (value instanceof Long) return elementData[index] == ((Long)value).longValue();
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
