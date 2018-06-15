package top.fols.box.util.sequence.interfaces;
import java.io.IOException;

public interface XInterfaceSequenceIOBigByte
{
	public long length() throws IOException;
    public byte byteAt(long p1) throws IOException;
}
