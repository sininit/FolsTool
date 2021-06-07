package top.fols.atri.io.buffer.bytes;


import top.fols.atri.io.buffer.BufferFilter;

import java.nio.charset.Charset;

public class ByteBufferFilter extends BufferFilter<byte[]> implements Cloneable {
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

	@Override
	public ByteBufferFilter clone() {
		ByteBufferFilter instance = new ByteBufferFilter();
		instance.separators = separators.clone();
		return instance;
	}

	static final ByteBufferFilter READ_LINE_FILTER = new ByteBufferFilter() {{
		this.addSeparator(new byte[]{'\r', '\n'});
		this.addSeparator(new byte[]{'\r'});
		this.addSeparator(new byte[]{'\n'});
	}
		@Override
		protected boolean accept(int last, int search, byte[] split, boolean readEnd) {
			return super.accept(last, search, split, readEnd);
		}
	};
	public static ByteBufferFilter getReadLineFilter() {
		return READ_LINE_FILTER.clone();
	}




	public void addSeparator(String separator, Charset charset) {
		// TODO: Implement this method
		super.addSeparator(separator.getBytes(charset));
	}
}
