package top.fols.box.lang.impl.sequences;

import java.io.Serializable;
import java.util.Arrays;
import top.fols.box.lang.interfaces.XInterfacesSequence;
import top.fols.box.statics.XStaticFixedValue;

public class XDoubleSequenceImpl extends XInterfacesSequence<double[],Double> implements Serializable {
	// 序列版本号  
    private static final long serialVersionUID = 2913154437796280545L;  

	private double[] elementData;
	public XDoubleSequenceImpl(double[] array) {
		this.elementData = array == null ?XStaticFixedValue.nulldoubleArray: array;
	}

	@Override
	public Double get(int index) {
		return this.elementData[index];
	}
	@Override
	public void set(int index, Double newElement) {
		this.elementData[index] = newElement;
	}
	@Override
	public int length() {
		return this.elementData.length;
	}
	@Override
	public double[] getArray() {
		// TODO: Implement this method
		return this.elementData;
	}
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.elementData = XStaticFixedValue.nulldoubleArray;
	}
	@Override
	public XDoubleSequenceImpl clone() {
		// TODO: Implement this method
		XDoubleSequenceImpl newObj = new XDoubleSequenceImpl(Arrays.copyOf(this.elementData, this.elementData.length));
		return newObj;
	}
	@Override
	public void setArray(double[] newArray) {
		// TODO: Implement this method
		this.elementData = newArray;
	}
	@Override
	public boolean equals(int index, Object value) {
		// TODO: Implement this method
		if (null == value) return false;
		if (value instanceof Double) return elementData[index] == ((Double)value).doubleValue();
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
