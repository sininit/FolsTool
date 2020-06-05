package top.fols.box.lang.abstracts;

import top.fols.box.io.interfaces.XInterfaceReleaseBufferable;
import top.fols.box.lang.XSequences;

/*
 * A is array
 * E is A elementType
 */
public abstract class XAbstractSequence <A extends Object,E extends Object> implements XInterfaceReleaseBufferable {
	@Override
	public abstract void releaseBuffer();

	public abstract E get(int index);
	public abstract void set(int index, E newElement);
	public abstract int length();
	
	public abstract A getArray();
	public abstract void setArray(A newArray);

	public abstract boolean isArray(int index);
	public XAbstractSequence toArraySequence(int index) {
		if (isArray(index)) return XSequences.wrap(get(index));
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		return getArray().equals(obj);
	}
	public abstract boolean equals(int index, Object value);

	public Class getArrayClass() {
		return getArray().getClass();
	}
	
	@Override
	public String toString() {
		// TODO: Implement this method
		return getArray().toString();
	}
}
