package top.fols.box.lang.impl.sequences;

import java.io.Serializable;
import java.util.Arrays;
import top.fols.box.lang.interfaces.XInterfacesSequence;
import top.fols.box.statics.XStaticFixedValue;

public class XByteSequenceImpl extends XInterfacesSequence<byte[],Byte> implements Serializable {
	// 序列版本号  
    private static final long serialVersionUID = 2913154437796280545L;  

	private byte[] elementData;
	public XByteSequenceImpl(byte[] array) {
		this.elementData = array == null ?XStaticFixedValue.nullbyteArray: array;
	}

	@Override
	public Byte get(int index) {
		return this.elementData[index];
	}
	@Override
	public void set(int index, Byte newElement) {
		this.elementData[index] = newElement;
	}
	@Override
	public int length() {
		return this.elementData.length;
	}
	@Override
	public byte[] getArray() {
		// TODO: Implement this method
		return this.elementData;
	}
	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		this.elementData = XStaticFixedValue.nullbyteArray;
	}
	@Override
	public XByteSequenceImpl clone() {
		// TODO: Implement this method
		XByteSequenceImpl newObj = new XByteSequenceImpl(Arrays.copyOf(this.elementData, this.elementData.length));
		return newObj;
	}
	@Override
	public void setArray(byte[] newArray) {
		// TODO: Implement this method
		this.elementData = newArray;
	}
	@Override
	public boolean equals(int index, Object value) {
		// TODO: Implement this method
		if (null == value) return false;
		if (value instanceof Byte) return elementData[index] == ((Byte)value).byteValue();
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
