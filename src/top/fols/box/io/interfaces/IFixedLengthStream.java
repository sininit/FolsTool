package top.fols.box.io.interfaces;

@SuppressWarnings("UnnecessaryModifier")
@Deprecated
public interface IFixedLengthStream {
	public abstract long getFreeLength();
	public abstract long getUseLength();
	public abstract boolean isAvailable();
	public abstract void resetUseLength();

	public abstract long getMaxUseLength();
	public abstract void setMaxUseLength(long maxSize);

	public abstract boolean isFixed();
	public abstract void fixed(boolean b);
}
