package top.fols.box.util.empty;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XObjects;
import top.fols.box.util.ArrayListUtils;
public final class XEmptyList extends AbstractList implements List,RandomAccess, Cloneable, java.io.Serializable
{
	@Override
	public int size()
	{
		return 0;
	}
	@Override
	public boolean isEmpty()
	{
		return true;
	}
	@Override
	public boolean contains(Object p1)
	{
		return false;
	}
	@Override
	public Iterator iterator()
	{
		return XStaticFixedValue.nullIterator;
	}
	@Override
	public Object[] toArray()
	{
		return XStaticFixedValue.nullObjectArray;
	}
	@Override
	public Object[] toArray(Object[] p1)
	{
		return p1;
	}
	@Override
	public boolean add(Object p1)
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public boolean remove(Object p1)
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public boolean containsAll(Collection p1)
	{
		return false;
	}
	@Override
	public boolean addAll(Collection p1)
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public boolean addAll(int p1, Collection p2)
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public boolean removeAll(Collection p1)
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public boolean retainAll(Collection p1)
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public void clear()
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public Object get(int p1)
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public Object set(int p1, Object p2)
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public void add(int p1, Object p2)
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public Object remove(int p1)
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public int indexOf(Object p1)
	{
		return -1;
	}
	@Override
	public int lastIndexOf(Object p1)
	{
		return -1;
	}
	@Override
	public ListIterator listIterator()
	{
		return XStaticFixedValue.nullListIterator;
	}
	@Override
	public ListIterator listIterator(int p1)
	{
		return XStaticFixedValue.nullListIterator;
	}
	@Override
	public List subList(int p1, int p2)
	{
		return XStaticFixedValue.nullList;
	}
}
