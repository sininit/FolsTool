package top.fols.box.util;
import top.fols.box.io.interfaces.Destroyable;
import top.fols.box.statics.XStaticFixedValue;
public class XArrayRangeLimit<S extends Object> implements Destroyable
{
	@Override
	public void destroyData()
	{
		// TODO: Implement this method
		array = XStaticFixedValue.nullObjectArray;
		off = 0;
		length = 0;
		close = true;
	}
	private Object[] array = XStaticFixedValue.nullObjectArray;
	private int off = 0;
	private int length;
	private boolean close = false;
	public XArrayRangeLimit(S[] array, int off, int len)
	{
		if (array != null)
			this.array = array;
		if (off < 0)
			off = 0;
		if (off + len > array.length)
			throw new ArrayIndexOutOfBoundsException("off=" + off + ", len=" + len + ", array.length=" + this.array.length);
		this.off = off;
		this.length = len;
	}
	public int length()
	{
		return length;
	}
	public S get(int i)
	{
		if (!(i > -1 && i < length))
			throw new ArrayIndexOutOfBoundsException("index=" + i + ", size=" + length + ", close=" + close);
		return (S)array[i + off];
	}
	public S set(int i, S obj)
	{
		if (!(i > -1 && i < length))
			throw new ArrayIndexOutOfBoundsException("index=" + i + ", size=" + length + ", close=" + close);
		return (S)(array[i + off] = obj);
	}
}
