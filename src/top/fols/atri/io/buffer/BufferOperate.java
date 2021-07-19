package top.fols.atri.io.buffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import top.fols.atri.io.buffer.bytes.ByteArrayBuffer;
import top.fols.atri.io.buffer.bytes.ByteBufferOperate;
import top.fols.atri.io.buffer.bytes.ByteFileBuffer;
import top.fols.atri.io.buffer.chars.CharArrayBuffer;
import top.fols.atri.io.buffer.chars.CharBufferOperate;
import top.fols.atri.io.buffer.chars.CharFileBuffer;
import top.fols.atri.util.Releasable;

import static top.fols.atri.lang.Finals.MAX_ARRAY_SIZE;


@SuppressWarnings({"rawtypes", "SuspiciousSystemArraycopy", "unchecked", "ConstantConditions", "ManualMinMaxCalculation", "SpellCheckingInspection", "BooleanMethodIsAlwaysInverted", "UnnecessaryLocalVariable", "ForLoopReplaceableByForEach", "UnusedLabel", "UnnecessaryLabelOnBreakStatement", "UnnecessaryLabelOnContinueStatement", "IfStatementWithIdenticalBranches"})
public abstract class BufferOperate<A> implements Releasable {
    public static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
			throw new OutOfMemoryError();// overflow
		}
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }



	public static CharBufferOperate wrapCharBuffer(char[] content) { return new CharArrayBuffer(content); }
	public static CharBufferOperate wrapCharBuffer(File file) throws FileNotFoundException { return new CharFileBuffer(file); }
	public static CharBufferOperate wrapCharBuffer(File file, Charset charset) throws FileNotFoundException { return new CharFileBuffer(file, charset); }

	/**
	 * If you want to read the file byte, it is recommended that you read the entire file directly
	 */
	public static ByteBufferOperate wrapByteBuffer(byte[] content) { return new ByteArrayBuffer(content); }
	public static ByteBufferOperate wrapByteBuffer(File file) throws FileNotFoundException { return new ByteFileBuffer(file); }


	protected Class elementType;
	protected int stream_buffer_size = 8192;

	protected A buffer; //array
	protected int position;
	protected int limit;

	protected BufferOperate() { super(); }
	public BufferOperate(A buffer, int position, int limit) throws NullPointerException {
		if (null == buffer) {				throw new NullPointerException("buffer");}

		this.buffer = buffer;
		this.position = position;
		this.limit = limit;

		if (position < 0 || position > limit || limit > sizeof(buffer)) {
			throw new ArrayIndexOutOfBoundsException(
				String.format("data.length=%s, position=%s, limit=%s"
							  , sizeof(buffer)
							  , position
							  , limit));
		}
	}

	public Class getComponentType() {
		return null == this.elementType ?this.elementType = this.buffer.getClass().getComponentType(): this.elementType;
	}





	public abstract A 			array_empty();
	public abstract A 			array(int count); //{ return (A) Array.newInstance(this.buffer.getClass().getComponentType(), count); }


	public abstract int 			sizeof(A array);

	//Never cross the line
	public abstract int 			stream_read(A buf, int off, int len) throws IOException;

	public abstract int 			indexOfBuffer(A b, int startIndex, int indexRange);
	public abstract int 			lastIndexOfBuffer(A b, int startIndex, int indexRange);





	public int  limit() { return this.limit; }
	public void limit(int limit) {
		if (limit < 0) {
			throw new ArrayIndexOutOfBoundsException("buffer.length=" + sizeof(this.buffer) + ", set.limit=" + limit);
		} else if (limit <= sizeof(this.buffer)) {
			if (this.position > limit) {
				throw new ArrayIndexOutOfBoundsException("buffer.length=" + sizeof(this.buffer) + ", position=" + this.position + ", set.limit=" + limit);
			} else {
				this.limit = limit;
			}
		} else {
			this.insert(this.limit, limit - this.limit);
		}
	}

	public int 					position() { return this.position; }
	public void 				position(int position) { 
		if (position < 0 || position > this.limit) { throw new ArrayIndexOutOfBoundsException(
				String.format("buffer.length=%s, buffer.limit=%s, position=%s"
							  , this.buffer_length()
							  , this.limit
							  , position)); }
		this.position = position;
	}
	public void 				positionSkip(int len) { this.position(this.position + len); }




	public int 				available() {
		return this.limit - this.position;
	}


	public int 			calculateGrowSize(int minCapacity) {
		int newCapacity = minCapacity << 1;
		if (newCapacity > 2) {
			newCapacity -= (minCapacity / 2);
		}
		if (newCapacity - minCapacity < 0) {
			newCapacity = minCapacity;
		}
		if (newCapacity - MAX_ARRAY_SIZE > 0) {
			newCapacity = hugeCapacity(minCapacity);
		}
		return newCapacity;
	}



	/**
	 * 不计算size
	 */
	public A 			buffer_grow(int minCapacity) {
		if (this.buffer_length() < minCapacity) {
			int newCapacity = calculateGrowSize(minCapacity);
//			System.out.println(minCapacity +", "+newCapacity);
			A array = array(newCapacity);
			System.arraycopy(this.buffer, 0, array, 0, this.limit);
			this.buffer = array;
		}
		return this.buffer;
	}



	public int append_from_stream_read(int len) throws IOException {
		if (len < 0 || len + this.limit < 0) { throw new ArrayIndexOutOfBoundsException(
				String.format("buffer.length=%s, buffer.limit=%s, length=%s"
							  , this.buffer_length()
							  , this.limit
							  , len));
		}
		BufferOperate buffer = this;
		buffer.buffer_grow(buffer.limit + len);
		int read = buffer.stream_read(buffer.buffer, buffer.limit, len);
		if (read == 0) {
			return 0;
		} else if (read > 0) {
			buffer.limit += read;
			return Math.min(len, read);
		} else {
			return -1;
		}
	}



	/**
	 * 插入空数据 自动 增加缓存长度, 计算 size
	 */
	public void insert(int position, int len) {
		if (len == 0) { return; }
		if (position < 0 || len < 0 || position > this.limit) {
			throw new ArrayIndexOutOfBoundsException(
				String.format("buffer.length=%s, buffer.limit=%s, position=%s, length=%s"
							  , this.buffer_length()
							  , this.limit
							  , position
							  , len));
		}
		if (position == this.limit) {
			if (this.buffer_length() - this.limit > len) {
				this.limit += len;
			} else {
				this.buffer = buffer_grow(this.limit + len);
				this.limit += len;
			}
		} else {
			A array = this.buffer;
			if (this.limit + len > this.buffer_length()) {
				array = array(calculateGrowSize(this.limit + len));
			}
			if (position > 0) { System.arraycopy(this.buffer, 0, array, 0, position);}
			if (position < this.limit) {System.arraycopy(this.buffer, position, array, position + len, this.limit - position); }
			this.buffer = array;
			this.limit += len;
			if (position < this.position) { this.position += len; }
		}
	}


	/**
	 * 删除数据, 计算 size
	 */
	public void 		remove(int position, int len) {
		if (position + len == this.limit) { // remove tail
			if (len <  0 || position < 0) { throw new ArrayIndexOutOfBoundsException(
					String.format("position=%s, length=%s"
								  , position
								  , len)); }
			if (position == 0) {
				this.buffer = null;
				this.buffer = array_empty();
				this.limit = this.position = 0;
			} else {
				this.buffer = (this.buffer);
				this.limit -= len;

				if (position < this.position)   { this.position = position; }
			}
		} else {
			if (len == 0) { return; }
			if (len <  0 || position < 0 || position + len > this.limit) { throw new ArrayIndexOutOfBoundsException(
					String.format("buffer.length=%s, buffer.limit=%s, position=%s, length=%s"
								  , this.buffer_length()
								  , this.limit
								  , position
								  , len)); }
			A oldArray = this.buffer;
			int newLength = this.buffer_length() - len;
			A newArray = array(newLength);
			if (position != 0) {
				System.arraycopy(oldArray, 0, newArray, 0, position);
			}
			if (newLength - position != 0) {
				System.arraycopy(oldArray, (position + len), newArray, position, (newLength) - position);
			}
			this.buffer = (newArray);
			this.limit -= len;
			if (position < this.position)   { this.position = (this.position - len < 0 ?0: this.position - len); }
		}
	}
	public void 			remove() { this.remove(0, this.limit); }
	public void 			removeBefore(int position) { this.remove(0, position); }
	public void 			removeAfter(int position) { this.remove(position, this.limit - position); }
	public boolean 			removeIfOverflow(int positionLimit) {
		if (this.overflow(positionLimit)) {
			this.remove(0, positionLimit);
			return true;
		} else {
			return false;
		}
	}


	public boolean 			overflow(int positionLimit) {
		return this.position >= positionLimit;
	}



	public A subArray(int st, int len) {
		if (len <  0 || st < 0 || st + len > this.limit) { throw new ArrayIndexOutOfBoundsException(
				String.format("buffer.length=%s, buffer.limit=%s, position=%s, length=%s"
							  , this.buffer_length()
							  , this.limit
							  , st
							  , len)); }
		A array = array(len); 
		System.arraycopy(this.buffer, st, array, 0, len);
		return array;
	}

	public int 			arraycopy(int position, Object toArray, int toArrayPosition, int len) {
		if (position < 0 || len < 0 || position + len > this.limit) { throw new ArrayIndexOutOfBoundsException(
				String.format("buffer.length=%s, buffer.limit=%s, position=%s, length=%s"
							  , this.buffer_length()
							  , this.limit
							  , position
							  , len)); }
		System.arraycopy(this.buffer, position, toArray, toArrayPosition, len);
		return len;
	}


	public A 					buffer_inner_trim() {
		if (0 != position || sizeof(buffer) != limit) {
			int available = available();
			A array = this.array(available);
			System.arraycopy(buffer, position, array, 0, available);
			this.buffer = array;
		}
		return this.buffer;
	}
	public A 					buffer_inner() { return this.buffer; }
	public int 					buffer_length() { return sizeof(this.buffer); }


	public A toArray() {
		A array = this.array(this.limit);
		System.arraycopy(this.buffer, 0, array, 0, this.limit);
		return array;
	}


	public int read(A array) throws IOException { return this.read(array, 0, sizeof(array)); }
	public int read(A array, int off, int len) throws IOException {
		if (off + len > sizeof(array) || off < 0 || len < 0) {
			throw new ArrayIndexOutOfBoundsException(String.format("array.length=%s, offset=%s, read.length=%s", sizeof(array), off, len));
		}

		int avail = this.available();
		if (avail >= len) {
			if (len == 0) { return 0; }
			this.arraycopy(this.position, array, off, len);
			this.positionSkip(len);
			return len;
		} else {
			this.arraycopy(this.position, array, off, avail);
			this.positionSkip(avail);

			off += avail; len -= avail;

			int readMax = Math.max(len, this.stream_buffer_size);
			this.buffer_grow(this.limit + readMax);
			int read = stream_read(this.buffer, this.limit, readMax);
			if (read == 0) {
				return 0;
			} else if (read > 0) {
				int readv = Math.min(len, read);
				this.limit += read;

				this.arraycopy(this.position, array, off, readv);
				this.positionSkip(readv);
				return readv;
			} else {
				return -1;
			}
		}
	}





	/**
	 * @return is read end
	 */
	public boolean readFilterIFEnd(BufferFilter<A> filter) throws IOException {
		int lastFind = this.position;
		int lastRead = -1;

		filter.finded(null, 0, 0, null, true);

		int maxSize = filter.getSeparatorMaxSize();
		int minSize = filter.getSeparatorMinSize();
		int readSize = Math.max(stream_buffer_size, maxSize);
		A[] separators = filter.getSeparators();
		A: while (true) {
			B: {
				if (lastFind + minSize <= this.limit) {
					boolean isFind = false;
					for (int i = 0;i < separators.length;i++) {
						A separator = separators[i];
						if (this.position + sizeof(separator) <= this.limit) {
							//System.out.println(lastFind);
							//System.out.println(last);
							int search = this.indexOfBuffer(separator, lastFind, this.limit());
							if (search != -1) {
								boolean accept = filter.accept(this.position, search, separator, false);
								lastFind = search + sizeof(separator);
								isFind = true;
								if (accept) { 
									filter.finded(this, this.position, search, separator, false);
									this.position(lastFind);
									return false;
								}
								break;
							} 
						}
					}
					if (!isFind) {
						lastFind = this.limit - maxSize + 1;
						if (lastRead == -1) {
							filter.finded(this, 0, 0, null, true);
							break A;
						}
					} else {
						continue A;
					}
				}
				if ((lastRead = this.append_from_stream_read(readSize)) == -1) {
					break;
				}
			} 
		}
		if (this.position != this.limit) {
			boolean accept = filter.accept(this.position, this.limit, null, true);
			if (accept) { 
				filter.finded(this, this.position, this.limit, null, true);
				this.position(this.limit);
				return false;
			}
		}
		return true;
	}








	public int 			readAvailable(A array) { return this.readAvailable(array, 0, sizeof(array)); }
	public int 			readAvailable(A array, int off, int len) {
		if (off + len > sizeof(array) || off < 0 || len < 0) {
			throw new ArrayIndexOutOfBoundsException(String.format("array.length=%s, offset=%s, read.length=%s", sizeof(array), off, len));
		}

		len = Math.min(len, this.available());
		System.arraycopy(this.buffer, this.position, array, off, len);
		this.positionSkip(len);
		return len;
	}



	public void 			append(A array) { this.append(array, 0, sizeof(array)); }
	public void 			append(A array, int offset, int len) {
		if (offset < 0 || len < 0 || offset + len > sizeof(array)) { throw new ArrayIndexOutOfBoundsException(
				String.format("array.length=%s, array.position=%s, length=%s"
							  , sizeof(array)
							  , offset
							  , len)); }
		if (len == 0) { return; }
		int index = this.limit;
		this.insert(index, len);
		System.arraycopy(array, offset, this.buffer, index, len); 
	}


	
	public void insert(int position, A array) {	this.insert(position, array, 0, sizeof(array));	}
	public void insert(int position, A array, int offset, int len) {
		if (len <  0 || offset < 0 || offset + len > sizeof(array)) { throw new ArrayIndexOutOfBoundsException(
				String.format("buffer.length=%s, buffer.limit=%s, position=%s, array.length=%s, array.position=%s, length=%s"
							  , buffer_length()
							  , this.limit
							  , position
							  , sizeof(array)
							  , offset
							  , len)); }
		if (len == 0) { return; }
		int index = position;
		this.insert(index, len);
		System.arraycopy(array, offset, this.buffer, index, len);
	}


	@Override
	public boolean release() {
		this.buffer = null;
		return true;
	}

	@Override
	public boolean released() {
		return null == this.buffer;
	}
}
