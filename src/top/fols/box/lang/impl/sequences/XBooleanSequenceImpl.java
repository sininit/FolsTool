package top.fols.box.lang.impl.sequences;

import java.io.Serializable;
import java.util.Arrays;
import top.fols.box.lang.interfaces.XInterfacesSequence;
import top.fols.box.statics.XStaticFixedValue;

public class XBooleanSequenceImpl extends XInterfacesSequence<boolean[],Boolean> implements Serializable {
	// 序列版本号  
    private static final long serialVersionUID = 2913154437796280545L;  

	private Boolean TRUE = true,FALSE = false;
	private boolean[] elementData;
	public XBooleanSequenceImpl(boolean[] array) {
		this.elementData = array == null ?XStaticFixedValue.nullbooleanArray: array;
	}

	@Override
	public Boolean get(int index) {
		return this.elementData[index] ?TRUE: FALSE;
	}
	@Override
	public void set(int index, Boolean newElement) {
		this.elementData[index] = newElement;
	}
	@Override
	public int length() {
		return this.elementData.length;
	}
	@Override
	public boolean[] getArray() {
		// TODO: Implement this method
		return this.elementData;
	}
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.elementData = XStaticFixedValue.nullbooleanArray;
	}
	@Override
	public XBooleanSequenceImpl clone() {
		// TODO: Implement this method
		XBooleanSequenceImpl newObj = new XBooleanSequenceImpl(Arrays.copyOf(this.elementData, this.elementData.length));
		return newObj;
	}
	@Override
	public void setArray(boolean[] newArray) {
		// TODO: Implement this method
		this.elementData = newArray;
	}
	@Override
	public boolean equals(int index, Object value) {
		// TODO: Implement this method
		if (null == value) return false;
		if (value instanceof Boolean) return elementData[index] == ((Boolean)value).booleanValue();
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
