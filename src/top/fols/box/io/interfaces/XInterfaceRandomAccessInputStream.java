package top.fols.box.io.interfaces;
import java.io.IOException;
import java.io.InputStream;
public abstract class XInterfaceRandomAccessInputStream extends InputStream implements XInterfacePrivateFixedStreamIndexBigOperat {
	public abstract int read()throws java.io.IOException;
	public abstract int read(byte[] b, int off, int len)throws IOException;

	public abstract long length()throws IOException;
}
