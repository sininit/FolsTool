package top.fols.box.util;
import java.util.List;
import java.util.Map;
import java.util.Set;
import top.fols.box.statics.XStaticFixedValue;
public class XMap<V extends Object> extends HashMapUtils9<String, V>
{
	public XMap(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}
    public XMap(int initialCapacity)
	{
        super(initialCapacity);
    }
    public XMap()
	{
        super(); // all other fields defaulted
    }
    public XMap(Map<? extends String, ? extends V> m)
	{
        super(m);
    }

	public List<String> keys()
	{
		return XObjects.keys(this);
	}
}

