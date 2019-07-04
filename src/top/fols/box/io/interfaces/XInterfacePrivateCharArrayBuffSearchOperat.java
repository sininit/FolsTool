package top.fols.box.io.interfaces;


public interface XInterfacePrivateCharArrayBuffSearchOperat {
	public int indexOfBuff(char b, int start);
	public int indexOfBuff(char[] b, int start);
	public int lastIndexOfBuff(char b, int start);
	public int lastIndexOfBuff(char[] b, int start);

	public int indexOfBuff(char b, int start, int end);
	public int indexOfBuff(char[] b, int start, int end);
	public int lastIndexOfBuff(char b, int start, int end);
	public int lastIndexOfBuff(char[] b, int start, int end);
}
