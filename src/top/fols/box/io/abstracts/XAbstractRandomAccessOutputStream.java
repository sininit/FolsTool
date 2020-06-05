package top.fols.box.io.abstracts;

import java.io.IOException;
import java.io.OutputStream;

import top.fols.box.io.interfaces.XInterfacePrivateFixedStreamIndexBigOperat;

public abstract class XAbstractRandomAccessOutputStream extends OutputStream implements XInterfacePrivateFixedStreamIndexBigOperat {
	public abstract void write(int p1) throws IOException;
    public abstract void write(byte[] b, int off, int len) throws IOException;

	public abstract long length() throws IOException;
	public abstract void setLength(long length)throws IOException;
}
