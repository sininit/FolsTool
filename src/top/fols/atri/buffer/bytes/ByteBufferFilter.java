package top.fols.atri.buffer.bytes;


import top.fols.atri.buffer.BufferFilter;

import java.nio.charset.Charset;

public class ByteBufferFilter extends BufferFilter<byte[]> {
	@Override
	public byte[] array(int count) {
		// TODO: Implement this method
		return new byte[count];
	}
	@Override
	public byte[][] array2(int count) {
		// TODO: Implement this method
		return new byte[count][];
	}

	@Override
	public int sizeof(byte[] array) {
		// TODO: Implement this method
		return array.length;
	}


	public void addSeparator(String separator, Charset charset) {
		// TODO: Implement this method
		super.addSeparator(separator.getBytes(charset));
	}
}
