package top.fols.box.util.empty;
import java.util.Iterator;
import top.fols.box.util.XObjects;
public final class XEmptyIterator implements Iterator
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
	public void remove()
	{
		throw XObjects.notPermittedAccess();
	}
}
