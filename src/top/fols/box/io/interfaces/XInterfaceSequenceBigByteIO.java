package top.fols.box.io.interfaces;

import java.io.IOException;

public interface XInterfaceSequenceBigByteIO {
	public long length() throws IOException;
    public byte byteAt(long p1) throws IOException;
}
