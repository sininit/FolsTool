package top.fols.box.io.interfaces;

public interface XInterfacePrivateByteArrayBuffSearchOperat {
	public int indexOfBuff(byte b, int start);
	public int indexOfBuff(byte[] b, int start);
	public int lastIndexOfBuff(byte b, int start);
	public int lastIndexOfBuff(byte[] b, int start);

	public int indexOfBuff(byte b, int start, int end);
	public int indexOfBuff(byte[] b, int start, int end);
	public int lastIndexOfBuff(byte b, int start, int end);
	public int lastIndexOfBuff(byte[] b, int start, int end);
}
