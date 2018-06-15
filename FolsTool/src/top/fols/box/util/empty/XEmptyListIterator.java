package top.fols.box.util.empty;
import java.util.ListIterator;
import top.fols.box.util.XObjects;

public final class XEmptyListIterator implements ListIterator
{
	@Override
	public boolean hasNext()
	{
		return false;
	}
	@Override
	public Object next()
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public boolean hasPrevious()
	{
		return false;
	}
	@Override
	public Object previous()
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public int nextIndex()
	{
		return -1;
	}
	@Override
	public int previousIndex()
	{
		return -1;
	}
	@Override
	public void remove()
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public void set(Object p1)
	{
		throw XObjects.notPermittedAccess();
	}
	@Override
	public void add(Object p1)
	{
		throw XObjects.notPermittedAccess();
	}
}
