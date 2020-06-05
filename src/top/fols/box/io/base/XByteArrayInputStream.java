package top.fols.box.io.base;

import java.io.InputStream;
import top.fols.box.io.interfaces.XInterfacePrivateBuffOperat;
import top.fols.box.io.interfaces.XInterfacePrivateByteArrayBuffSearchOperat;
import top.fols.box.io.interfaces.XInterfacePrivateFixedStreamIndexOperat;
import top.fols.box.io.interfaces.XInterfaceLineReaderStream;
import top.fols.box.util.XArrays;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.io.interfaces.XInterfaceReleaseBufferable;

/**
 * @java.io.ByteArrayInputStream
 **/

public class XByteArrayInputStream extends InputStream implements XInterfacePrivateBuffOperat<byte[]>,
XInterfaceLineReaderStream<byte[]>, XInterfacePrivateByteArrayBuffSearchOperat,
XInterfacePrivateFixedStreamIndexOperat, XInterfaceReleaseBufferable {
	// 保存字节输入流数据的字节数组
	private byte buf[];
	// 下一个会被读取的字节的索引
	private int pos;
	// 标记的索引
	private int mark = 0;
	// 字节流的长度
	private int count;

	// 构造函数：创建一个内容为buf的字节流
	public XByteArrayInputStream(byte buf[]) {
		// 初始化“字节流对应的字节数组为buf”
		this.buf = buf;
		// 初始化“下一个要被读取的字节索引号为0”
		this.pos = 0;
		// 初始化“字节流的长度为buf的长度”
		this.count = buf.length;
	}

	// 构造函数：创建一个内容为buf的字节流，并且是从offset开始读取数据，读取的长度为length
	public XByteArrayInputStream(byte buf[], int offset, int length) {
		// 初始化“字节流对应的字节数组为buf”
		this.buf = buf;
		// 初始化“下一个要被读取的字节索引号为offset”
		this.pos = offset;
		// 初始化“字节流的长度”
		this.count = Math.min(offset + length, buf.length);
		// 初始化“标记的字节流读取位置”
		this.mark = offset;
	}

	// 读取下一个字节
	@Override
	public int read() {
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}

    @Override
    public int read(byte[] b) {
        return this.read(b, 0, b.length);
    }

	// 将“字节流的数据写入到字节数组b中”
	// off是“字节数组b的偏移地址”，表示从数组b的off开始写入数据
	// len是“写入的字节长度”
	@Override
	public int read(byte b[], int off, int len) {
		if (null == b) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		}
		if (pos >= count) {
			return -1;
		}
		int avail = count - pos;
		if (len > avail) {
			len = avail;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(buf, pos, b, off, len);
		pos += len;
		return len;
	}

	@Override
	public byte[] readLine(byte[] splitChar) {
		return readLine(splitChar, true);
	}

	private int readLineDefaultSeparatorIndex = -1;

	/**
	 * Get Next Line
	 * 
	 * @param separators         lineSeparator
	 * @param resultAddSeparator if read to separator, add return or not?
	 * 
	 * @return if the content is not read, null will be returned
	 */
	@Override
	public byte[] readLine(byte[] separators, boolean resultAddSeparator) {
		this.readLineDefaultSeparatorIndex = -1;
		if (this.pos >= this.count) {
			return null;
		}

		int findindex = -1;
		byte[] split = XStaticFixedValue.nullbyteArray;
		int search = XArrays.indexOf(buf, separators, this.pos, this.count);
		if (search != -1 && (findindex == -1 || search < findindex)) {
			this.readLineDefaultSeparatorIndex = 0;
			findindex = search;
			split = separators;
		}

		if (findindex == -1) {
			byte[] newArray = new byte[this.count - this.pos];
			System.arraycopy(buf, this.pos, newArray, 0, newArray.length);
			this.pos = this.count;
			return newArray;
		} else {
			int arrlen = findindex - this.pos;
			if (resultAddSeparator) {
				arrlen += split.length;
			}
			byte[] newArray = new byte[arrlen];
			System.arraycopy(buf, this.pos, newArray, 0, newArray.length);

			this.pos = findindex + split.length;
			return newArray;
		}
	}

	@Override
	public byte[] readLine(byte[][] separators, boolean resultAddSeparator) {
		this.readLineDefaultSeparatorIndex = -1;
		if (this.pos >= this.count) {
			return null;
		}

		int endIndex = this.buf.length;
		int offIndex = this.pos;
		byte[] data = this.buf;

		int len = endIndex - offIndex;
		int lastIndex = this.pos;
		for (int ii = 0; ii < len; ii++) {
			byte b1 = data[offIndex + ii];

			for (int ii2 = 0; ii2 < separators.length; ii2++) {
				if (separators[ii2][0] == b1 && (offIndex + ii + separators[ii2].length) <= endIndex) {
					int j = 1;
					for (int ii3 = 1; ii3 < separators[ii2].length; ii3++) {
						if (separators[ii2][ii3] == data[offIndex + ii + ii3]) {
							j++;
						}
					}
					if (j == separators[ii2].length) {
						this.readLineDefaultSeparatorIndex = ii2;

						int st = lastIndex;
						int et = offIndex + ii + separators[ii2].length;
						int l = et - st;
						byte[] array = new byte[l - (resultAddSeparator ? 0 : separators[ii2].length)];
						System.arraycopy(data, st, array, 0, array.length);

						lastIndex = et;
						ii += separators[ii2].length;

						ii -= 1;// for (offset for self-increment)

						// System.out.println("*1" + st + "," + et);

						this.pos = lastIndex;
						return array;
					}
				}
			}
		}
		if (lastIndex != endIndex) {
			int st = lastIndex;
			int et = endIndex;
			// System.out.println("*2 " + st + "," + et);
			int l = et - st;
			byte[] array = new byte[l];
			System.arraycopy(data, st, array, 0, array.length);
			this.pos = endIndex;
			return array;
		} else {
			return null;
		}
	}

	@Override
	public int readLineSeparatorsIndex() {
		return this.readLineDefaultSeparatorIndex;
	}

	@Override
	public boolean isReadLineReadToSeparator() {
		return this.readLineDefaultSeparatorIndex != -1;
	}

	// 跳过“字节流”中的n个字节。
	@Override
	public long skip(long n) {
		long k = count - pos;
		if (n < k) {
			k = n < 0 ? 0 : n;
		}
		pos += k;
		return k;
	}

	// “能否读取字节流的下一个字节”
	@Override
	public int available() {
		return count - pos;
	}

	// 是否支持“标签”
	@Override
	public boolean markSupported() {
		return true;
	}

	// 保存当前位置。readAheadLimit在此处没有任何实际意义
	@Override
	public void mark(int readAheadLimit) {
		mark = pos;
	}

	// 重置“字节流的读取索引”为“mark所标记的位置”
	@Override
	public void reset() {
		pos = mark;
	}

	@Override
	public void close() {
		return;
	}

	@Override
	public void releaseBuffer() {
		// TODO: Implement this method
		setBuff(null, 0);
	}

	@Override
	public byte[] getBuff() {
		return buf;
	}

	@Override
	public int getBuffSize() {
		// TODO: Implement this method
		return buf.length;
	}

	@Override
	public void setBuff(byte[] newBuff, int size) {
		// TODO: Implement this method
		this.buf = null == newBuff ? XStaticFixedValue.nullbyteArray : newBuff;
		this.setBuffSize(size);
	}

	@Override
	public void setBuffSize(int size) throws ArrayIndexOutOfBoundsException {
		if (size > buf.length) {
			throw new ArrayIndexOutOfBoundsException("arrayLen=" + buf.length + ", setLen=" + size);
		}
		count = size;
	}

	@Override
	public void seekIndex(int index) {
		if (!(index > -1 && index <= count)) {
			throw new ArrayIndexOutOfBoundsException("can't set pos index=" + index + " length=" + count);
		}
		pos = index;
	}

	@Override
	public int getIndex() {
		return pos;
	}

	@Override
	public int indexOfBuff(byte b, int startIndex, int indexRange) {
		return XArrays.indexOf(buf, b, startIndex, indexRange);
	}

	@Override
	public int indexOfBuff(byte[] b, int startIndex, int indexRange) {
		return XArrays.indexOf(buf, b, startIndex, indexRange);
	}

	@Override
	public int lastIndexOfBuff(byte b, int startIndex, int indexRange) {
		return XArrays.lastIndexOf(buf, b, startIndex, indexRange);
	}

	@Override
	public int lastIndexOfBuff(byte[] b, int startIndex, int indexRange) {
		return XArrays.lastIndexOf(buf, b, startIndex, indexRange);
	}

	public int size() {
		return count;
	}

}
