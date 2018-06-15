package top.fols.box.io.interfaces;
public interface XInterfaceStreamFixedLength<A extends Object>
{
	public abstract long getFixedLengthFree();
	public abstract long getFixedLengthUseSize();
	public abstract boolean isFixedLengthAvailable();
	public abstract void resetFixedLengthUseSize();
	
	public abstract long getFixedLengthMaxSize();
	public abstract void setFixedLengthMaxSize(long maxSize);
	
	public abstract boolean getFixedLength();
	public abstract void setFixedLength(boolean b);
	public abstract A getStream();
}
