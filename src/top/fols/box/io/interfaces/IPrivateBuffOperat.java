package top.fols.box.io.interfaces;

/*
 * get instance internal buffer
 */
@SuppressWarnings("UnnecessaryModifier")
@Deprecated
public interface IPrivateBuffOperat<E extends Object> {
	public abstract E buffer();
	public abstract int buffer_length();
	
	public abstract void buffer(E newBuff, int size);
	public abstract void buffer_length(int size);
}
