package top.fols.atri.lang;

@SuppressWarnings("ALL")
public class Defined<T extends Object> {
	final T null_value;
	public Defined(T null_value) {
		this.null_value = Objects.requireNonNull(null_value);
	}


	public boolean isUndefined(Object value) 	{ return value == null; }

	public T null_value() 						{ return null_value; }

	public T get(T value)               		{ return value == null_value ?null: value; }
	public T set(T value) 						{ return null == value ? null_value : value; }
}
