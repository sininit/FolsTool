package top.fols.atri.io.buffer.chars;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import top.fols.atri.array.ArrayObject;
import top.fols.atri.io.buffer.BufferFilter;
import top.fols.atri.io.buffer.BufferOperate;
import top.fols.atri.io.buffer.bytes.ByteBufferFilter;
import top.fols.atri.lang.Arrayz;
import top.fols.box.util.XArrays;
import static top.fols.atri.lang.Finals.*;
import static top.fols.atri.lang.Finals.EMPTY_CHAR_ARRAY;

public abstract class CharBufferOperate extends BufferOperate<char[]> {
	public CharBufferOperate() {
		this(EMPTY_CHAR_ARRAY);
	}
	public CharBufferOperate(char[] datas) {
		this(datas, 0, datas.length);
	}
	public CharBufferOperate(char[] datas, int position, int limit) {
		super(datas, position, limit);
	}
	
	
	
	public int indexOfBuffer(char b, int startIndex, int indexRange) { return XArrays.indexOf(buffer, b, startIndex, indexRange); }
	public int lastIndexOfBuffer(char b, int startIndex, int indexRange) { return XArrays.lastIndexOf(buffer, b, startIndex, indexRange); }

	@Override public int indexOfBuffer(char[] b, int startIndex, int indexRange) { return XArrays.indexOf(buffer, b, startIndex, indexRange); }
	@Override public int lastIndexOfBuffer(char[] b, int startIndex, int indexRange) { return XArrays.lastIndexOf(buffer, b, startIndex, indexRange); }
	

	@Override
	public int hashCode() {
		// TODO: Implement this method
		return Arrays.hashCode(this.buffer) + this.position + this.limit;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (!(obj instanceof CharBufferOperate)) { return false; }
		CharBufferOperate object = (CharBufferOperate)obj;
		return 
			Arrays.equals(this.buffer, object.buffer) &&
			this.position == object.position &&
			this.limit == object.limit;
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		return new String(toArray());
	}

	@Override public char[] array_empty() { return EMPTY_CHAR_ARRAY;}
	@Override public char[] array(int count) { return new char[count]; }
	@Override public int sizeof(char[] array) { return array.length; }

	public int read() throws IOException {
		int avail = this.available();
		if (avail > 0) {
			int result = this.buffer[this.position];
			this.positionSkip(1);
			return result;
		} else {
			this.buffer_grow(this.limit + this.stream_buffer_size);
			int read = stream_read(this.buffer, this.limit, this.stream_buffer_size);
			if (read == 0) {
				return 0;
			} else if (read > 0) {
				int result = this.buffer[this.limit];
				this.limit += read;
				this.positionSkip(1);
				return result;
			} else {
				return -1;
			}
		}
	}

	public int 			readAvailable() {
		if (this.available() > 0) {
			int result = this.buffer[this.position];
			this.positionSkip(1);
			return result;
		} else {
			return -1;
		}
	}



	public void 			append(char ch) { 
		this.buffer_grow(this.limit + 1);
		this.buffer[this.limit++] = ch;
	}


	public void 			append(CharSequence array) { this.append(array, 0, array.length()); }
	public void 			append(CharSequence array, int offset, int len) {
		if (len <  0 || offset < 0 || offset + len > array.length()) { throw new ArrayIndexOutOfBoundsException(
				String.format("buffer.length=%s, buffer.limit=%s, position=%s, array.length=%s, array.position=%s, length=%s"
							  , buffer_length()
							  , this.limit
							  , position
							  , array.length()
							  , offset
							  , len)); }
		if (len == 0) { return; }
		int index = this.limit;
		this.insert(index, len);
		for (int i = 0; i < len; i++) { this.buffer[index++] = array.charAt(offset++);} 
	}



	
	
	public int append_from_stream_read(Reader reader, int len) throws IOException {
		return this.insert_from_stream_read(this.limit, reader, len);
	}
	public int insert_from_stream_read(int position, Reader reader, int len) throws IOException {
		if (position < 0 || position > this.limit) { throw new ArrayIndexOutOfBoundsException(
				String.format("position=%s, buffer.limit=%s"
							  , position  
							  , this.limit)); }
		if (len <  0) { throw new ArrayIndexOutOfBoundsException(
				String.format("length=%s"
							  , len)); }
		if (null == reader) { throw new NullPointerException("stream"); }
		
		this.insert(position, len);
		int 	read = reader.read(this.buffer, position, len); //Never cross the line
		return 	read;
	}

	public void writeTo(Writer stream) throws IOException{ this.writeTo(stream, this.position, this.available()); }
	public void writeTo(Writer stream, int position, int len) throws IOException {
		if (position < 0 || len < 0 || position + len > this.limit) { throw new ArrayIndexOutOfBoundsException(
				String.format("buffer.length=%s, buffer.limit=%s, position=%s, length=%s"
							  , this.buffer_length()
							  , this.limit
							  , position
							  , len)); }
		stream.write(this.buffer, position, len);
	}


	/**
	 * windows or linux or mac os
	 */
	static final CharBufferFilter READ_LINE_FILTER = new CharBufferFilter() {{
			this.addSeparator(new char[]{'\r', '\n'});
			this.addSeparator(new char[]{'\r'});
			this.addSeparator(new char[]{'\n'});
		}

		@Override
		public boolean accept(int last, int search, char[] split, boolean readEnd) {
			return super.accept(last, search, split, readEnd);
		}
	};
	public static CharBufferFilter lineFilter() {
		return READ_LINE_FILTER.clone();
	}








	/**
	 * @see top.fols.atri.io.buffer.bytes.ByteBufferOperate#readFilterIFEnd(BufferFilter)
	 * @param filter filter
	 * @throws IOException stream.read
	 */
	public boolean readFilterIFEnd(BufferFilter<char[]> filter) throws IOException {
		int lastFind = this.position;

		filter.setFindResult(null, 0, 0, null, true);

		int maxSize  = filter.getSeparatorMaxSize(), minSize = filter.getSeparatorMinSize();
		int readSize = Math.max(stream_buffer_size, maxSize);

		char[][] separators = filter.getSeparators();
		while (true) {
			if (lastFind + minSize <= this.limit) {
				boolean isFind = false;
				for (int i = lastFind; i < this.limit; i++) {
					SW: for (char[] separator : separators) {
						if (this.buffer[i] == separator[0] && i + separator.length <= this.limit) {
							for (int ji = 1; ji < separator.length; ji++) {
								if (!(this.buffer[i + ji] == separator[ji])) {
									continue SW;
								}
							}
							boolean accept = filter.accept(this.position, i, separator, false);
							lastFind = i + sizeof(separator);
							isFind = true;
							if (accept) {
								filter.setFindResult(this, this.position, i, separator, false);
								this.position(lastFind);
								return false;
							}
							break;
						}
					}
				}
				if (!isFind) {
					lastFind = this.limit - maxSize + 1;
				} else {
					continue;
				}
			}
			if (this.append_from_stream_read(readSize) == -1) {
				break;
			}
		}
		if (this.position != this.limit) {
			boolean accept = filter.accept(this.position, this.limit, null, true);
			if (accept) {
				filter.setFindResult(this, this.position, this.limit, null, true);
				this.position(this.limit);
				return false;
			}
		}
		return true;
	}
	public boolean readFilter(BufferFilter<char[]> filter) throws IOException {
		//noinspection PointlessBooleanExpression
		return false == readFilterIFEnd(filter);
	}



}
