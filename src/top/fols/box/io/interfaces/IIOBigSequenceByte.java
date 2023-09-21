package top.fols.box.io.interfaces;

import java.io.IOException;

@SuppressWarnings("UnnecessaryModifier")
@Deprecated
public interface IIOBigSequenceByte {
	public long length() throws IOException;
    public byte byteAt(long p1) throws IOException;
}
