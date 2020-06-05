package top.fols.box.io.abstracts;

import java.io.IOException;
import java.io.InputStream;
import top.fols.box.io.interfaces.XInterfacePrivateFixedStreamIndexBigOperat;

public abstract class XAbstractRandomAccessInputStream extends InputStream implements XInterfacePrivateFixedStreamIndexBigOperat {
	public abstract int read()throws java.io.IOException;
	public abstract int read(byte[] b, int off, int len)throws IOException;

	public abstract long length()throws IOException;
}
