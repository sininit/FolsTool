package top.fols.atri.lang;

import top.fols.atri.lang.Objects;

@SuppressWarnings("ALL")
public class Seted<T extends Object> {
	final T NULL_POINT; 
	public Seted(T NULL_POINT) {
		this.NULL_POINT = Objects.requireNonNull(NULL_POINT);
	}

	public boolean isSet(Object value) 	{ return !(value == null); }

//	public T NULL() 					{ return this.NULL_POINT; }
	
	public T get(T value)               { return value == NULL_POINT ?null: value; }
	public T set(T value) 				{ return null == value ?NULL_POINT: value; }
}
