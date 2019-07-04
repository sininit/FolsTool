package top.fols.box.lang.impl.sequences;

import java.io.Serializable;
import java.util.Arrays;
import top.fols.box.lang.interfaces.XInterfacesSequence;
import top.fols.box.statics.XStaticFixedValue;

public class XCharSequenceImpl extends XInterfacesSequence<char[],Character> implements Serializable {
	// 序列版本号  
    private static final long serialVersionUID = 2913154437796280545L;  

	private char[] elementData;
	public XCharSequenceImpl(char[] array) {
		this.elementData = array == null ?XStaticFixedValue.nullcharArray: array;
	}

	@Override
	public Character get(int index) {
		return this.elementData[index];
	}
	@Override
	public void set(int index, Character newElement) {
		this.elementData[index] = newElement;
	}
	@Override
	public int length() {
		return this.elementData.length;
	}
	@Override
	public char[] getArray() {
		// TODO: Implement this method
		return this.elementData;
	}
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.elementData = XStaticFixedValue.nullcharArray;
	}
	@Override
	public XCharSequenceImpl clone() {
		// TODO: Implement this method
		XCharSequenceImpl newObj = new XCharSequenceImpl(Arrays.copyOf(this.elementData, this.elementData.length));
		return newObj;
	}
	@Override
	public void setArray(char[] newArray) {
		// TODO: Implement this method
		this.elementData = newArray;
	}
	@Override
	public boolean equals(int index, Object value) {
		// TODO: Implement this method
		if (null == value) return false;
		if (value instanceof Character) return elementData[index] == ((Character)value).charValue();
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
