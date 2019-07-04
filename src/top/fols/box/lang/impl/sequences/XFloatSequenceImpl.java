package top.fols.box.lang.impl.sequences;

import java.io.Serializable;
import java.util.Arrays;
import top.fols.box.lang.interfaces.XInterfacesSequence;
import top.fols.box.statics.XStaticFixedValue;

public class XFloatSequenceImpl extends XInterfacesSequence<float[],Float> implements Serializable {
	// 序列版本号  
    private static final long serialVersionUID = 2913154437796280545L;  

	private float[] elementData;
	public XFloatSequenceImpl(float[] array) {
		this.elementData = array == null ?XStaticFixedValue.nullfloatArray: array;
	}

	@Override
	public Float get(int index) {
		return this.elementData[index];
	}
	@Override
	public void set(int index, Float newElement) {
		this.elementData[index] = newElement;
	}
	@Override
	public int length() {
		return this.elementData.length;
	}
	@Override
	public float[] getArray() {
		// TODO: Implement this method
		return this.elementData;
	}
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.elementData = XStaticFixedValue.nullfloatArray;
	}
	@Override
	public XFloatSequenceImpl clone() {
		// TODO: Implement this method
		XFloatSequenceImpl newObj = new XFloatSequenceImpl(Arrays.copyOf(this.elementData, this.elementData.length));
		return newObj;
	}
	@Override
	public void setArray(float[] newArray) {
		// TODO: Implement this method
		this.elementData = newArray;
	}
	@Override
	public boolean equals(int index, Object value) {
		// TODO: Implement this method
		if (null == value) return false;
		if (value instanceof Float) return elementData[index] == ((Float)value).floatValue();
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
 
