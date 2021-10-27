package top.fols.atri.io.buffer.bytes;


import top.fols.atri.io.buffer.BufferFilter;
import top.fols.atri.lang.Objects;
import top.fols.atri.util.DoubleLinkedList;

import java.nio.charset.Charset;

public class ByteBufferFilter extends BufferFilter<byte[]> implements Cloneable {
	@Override
	public byte[] array(int count) {
		// TODO: Implement this method
		return new byte[count];
	}
	@Override
	public byte[][] arrays(int count) {
		// TODO: Implement this method
		return new byte[count][];
	}

	@Override
	public int sizeof(byte[] array) {
		// TODO: Implement this method
		return array.length;
	}


	public static final Objects.Cast<byte[], byte[]> CLONE_CONVERT = new Objects.Cast<byte[], byte[]>() {
		@Override
		public byte[] cast(byte[] param) {
			return null == param?null:param.clone();
		}
	};
	@Override
	public ByteBufferFilter clone() {
		ByteBufferFilter instance = new ByteBufferFilter();
		instance.separators = separators.clone(CLONE_CONVERT);
		return instance;
	}




	public void addSeparator(String separator, Charset charset) {
		// TODO: Implement this method
		super.addSeparator(separator.getBytes(charset));
	}



}
