package top.fols.atri.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.box.io.interfaces.IPrivateBuffOperat;
import top.fols.box.io.interfaces.IPrivateStreamIndexOperat;

public class BytesOutputStreams extends OutputStream
implements IPrivateBuffOperat<byte[]>,
		IPrivateStreamIndexOperat, IReleasable {

	private byte[] buf;

	private int count;


	public BytesOutputStreams() {
		this(32);
	}


	public BytesOutputStreams(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		buf = new byte[size];
	}

	public byte[] trimToSizeAndGetBuffer() {
		if (count == buf.length) {
			return   buf;
		}
		return buf = (count == 0)
				? Finals.EMPTY_BYTE_ARRAY
				: Arrays.copyOf(buf, count);
	}





	private void ensureCapacity(int minCapacity) {
		// overflow-conscious code
		if (minCapacity - buf.length > 0)
			grow(minCapacity);
	}


	private void grow(int minCapacity) {
		int oldCapacity = buf.length;

		int newCapacity = oldCapacity << 1;

		if (newCapacity - minCapacity < 0)
			newCapacity = minCapacity;
		if (newCapacity < 0) {
			if (minCapacity < 0) // overflow
				throw new OutOfMemoryError();
			newCapacity = Integer.MAX_VALUE;
		}
		buf = Arrays.copyOf(buf, newCapacity);
	}


	@Override
	public void write(int b) {
		ensureCapacity(count + 1);
		buf[count] = (byte) b;
		count += 1;
	}

    @Override
    public void write(byte[] b) {
        this.write(b, 0, b.length);
    }


	@Override
	public void write(byte b[], int off, int len) {
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) - b.length > 0)) {
			throw new IndexOutOfBoundsException();
		}
		ensureCapacity(count + len);
		System.arraycopy(b, off, buf, count, len);
		count += len;
	}


	public void writeTo(OutputStream out) throws IOException {
		out.write(buf, 0, count);
	}


	public void reset() {
		count = 0;
	}


	public byte[] toByteArray() {
		if (count == 0)
			return Finals.EMPTY_BYTE_ARRAY;
		return Arrays.copyOf(buf, count);
	}


	public int size() {
		return count;
	}

	@Override
	public String toString() {
		return new String(buf, 0, count);
	}

	public String toString(String charsetName) throws UnsupportedEncodingException {
		return new String(buf, 0, count, charsetName);
	}

	@Deprecated
	public String toString(int hibyte) {
		return new String(buf, hibyte, 0, count);
	}

	@Override
	public void close() {
		this.release();
	}


	@Override
	public byte[] buffer() {
		return buf;
	}

	@Override
	public int buffer_length() {
		// TODO: Implement this method
		return buf.length;
	}

	@Override
	public void buffer(byte[] newBuff, int size) {
		// TODO: Implement this method
		this.buf = null == newBuff ? Finals.EMPTY_BYTE_ARRAY : newBuff;
		this.buffer_length(size);
	}

	@Override
	public void buffer_length(int size) throws ArrayIndexOutOfBoundsException {
		if (size > buf.length) {
			throw new ArrayIndexOutOfBoundsException("arrayLen=" + buf.length + ", setLen=" + size);
		}
		count = size;
	}

	@Override
	public void seekIndex(int index) {
		buffer_length(index);
	}

	@Override
	public int getIndex() {
		return count;
	}






	@Override
	public boolean release() {
		buf = Finals.EMPTY_BYTE_ARRAY;
		count = 0;
		return true;
	}
	@Override
	public boolean released() {
		return null == buf || buf == Finals.EMPTY_BYTE_ARRAY;
	}
}
