package top.fols.box.io.interfaces;
import java.io.IOException;
import java.io.OutputStream;
public abstract class XInterfaceRandomAccessOutputStream extends OutputStream implements XInterfacePrivateFixedStreamIndexBigOperat {
	public abstract void write(int p1) throws IOException;
    public abstract void write(byte[] b, int off, int len) throws IOException;

	public abstract long length() throws IOException;
	public abstract void setLength(long length)throws IOException;
}
