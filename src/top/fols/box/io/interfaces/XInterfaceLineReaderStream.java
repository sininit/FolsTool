package top.fols.box.io.interfaces;

import java.io.IOException;

import top.fols.box.statics.XStaticSystem;

public interface XInterfaceLineReaderStream<E extends Object> {
	public static final byte[] LINUX_LINE_SEPARATOR_BYTES = XStaticSystem.LINUX_UNIX_LINE_SEPARATOR.getBytes();
	public static final char[] LINUX_LINE_SEPARATOR_CHARS = XStaticSystem.LINUX_UNIX_LINE_SEPARATOR.toCharArray();

	public abstract E readLine(E separator) throws IOException;
	public abstract E readLine(E separator, boolean resultAddSeparator) throws IOException;
	public abstract E readLine(E[] separator, boolean resultAddSeparator) throws IOException;

	public abstract boolean isReadLineReadToSeparator();
	public abstract int readLineSeparatorsIndex();
	
	//public abstract E test() throws IOException;
}
