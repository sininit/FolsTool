package top.fols.box.io.interfaces;

/*
 * get instance internal buffer
 */
public interface XInterfacePrivateBuffOperat <E extends Object> {
	public abstract E getBuff();
	public abstract int getBuffSize();
	
	public abstract void setBuff(E newBuff,int size);
	public abstract void setBuffSize(int size);
}
