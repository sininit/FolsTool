package top.fols.box.lang.impl.sequences;

import java.io.Serializable;
import java.util.Arrays;
import top.fols.box.lang.interfaces.XInterfacesSequence;
import top.fols.box.statics.XStaticFixedValue;

public class XStringSequenceImpl extends XInterfacesSequence<String[],Object> implements Serializable {
	// 序列版本号  
    private static final long serialVersionUID = 2913154437796280545L;  

	private String[] elementData;
	public XStringSequenceImpl(String[] array) {
		this.elementData = array == null ?XStaticFixedValue.nullStringArray: array;
	}
	@Override
	public String get(int index) {
		return this.elementData[index];
	}
	@Override
	public void set(int index, Object newElement) {
		this.elementData[index]
			= newElement instanceof String 
			? (String)newElement
			: (newElement == null ? null: newElement.toString());
	}
	@Override
	public int length() {
		return this.elementData.length;
	}
	@Override
	public String[] getArray() {
		// TODO: Implement this method
		return this.elementData;
	}
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.elementData = XStaticFixedValue.nullStringArray;
	}
	@Override
	public XStringSequenceImpl clone() {
		// TODO: Implement this method
		XStringSequenceImpl newObj = new XStringSequenceImpl(Arrays.copyOf(this.elementData, this.elementData.length));
		return newObj;
	}
	@Override
	public void setArray(String[] newArray) {
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
		return false;
	}
	@Override
	public int hashCode() {
		// TODO: Implement this method
		return Arrays.hashCode(elementData);
	}
}
