package top.fols.atri.buffer.chars;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import top.fols.atri.buffer.BufferOption;
import top.fols.box.util.XArrays;
import static top.fols.atri.lang.Finals.*;

public abstract class CharBufferOption extends BufferOption<char[]> {
	public CharBufferOption() {
		this(EMPTY_CHAR_BUFFER);
	}
	public CharBufferOption(char[] datas) {
		this(datas, 0, datas.length);
	}
	public CharBufferOption(char[] datas, int position, int size) {
		super(datas, position, size);
	}
	
	
	
	public int indexOfBuffer(char b, int startIndex, int indexRange) { return XArrays.indexOf(buffer, b, startIndex, indexRange); }
	public int lastIndexOfBuffer(char b, int startIndex, int indexRange) { return XArrays.lastIndexOf(buffer, b, startIndex, indexRange); }

	@Override public int indexOfBuffer(char[] b, int startIndex, int indexRange) { return XArrays.indexOf(buffer, b, startIndex, indexRange); }
	@Override public int lastIndexOfBuffer(char[] b, int startIndex, int indexRange) { return XArrays.lastIndexOf(buffer, b, startIndex, indexRange); }
	

	@Override
	public int hashCode() {
		// TODO: Implement this method
		return Arrays.hashCode(this.buffer) + this.position + this.size;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (!(obj instanceof CharBufferOption)) { return false; }
		CharBufferOption object = (CharBufferOption)obj;
		return 
			Arrays.equals(this.buffer, object.buffer) &&
			this.position == object.position &&
			this.size == object.size;
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		return new String(toArray());
	}

	@Override public char[] array_empty() { return EMPTY_CHAR_BUFFER;}
	@Override public char[] array(int count) { return new char[count]; }
	@Override public int sizeof(char[] array) { return array.length; }

	public int read() throws IOException {
		int avail = this.available();
		if (avail > 0) {
			int result = this.buffer[this.position];
			this.positionSkip(1);
			return result;
		} else {
			this.buffer_grow(this.size + this.stream_buffer_size);
			int read = stream_read(this.buffer, this.size, this.stream_buffer_size);
			if (read == 0) {
				return 0;
			} else if (read > 0) {
				int result = this.buffer[this.size];
				this.size += read;
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
		this.buffer_grow(this.size + 1);
		this.buffer[this.size++] = ch;
	}


	public void 			append(CharSequence array) { this.append(array, 0, array.length()); }
	public void 			append(CharSequence array, int offset, int len) {
		if (len <  0 || offset < 0 || offset + len > array.length()) { throw new ArrayIndexOutOfBoundsException(
				String.format("buffer.length=%s, buffer.size=%s, position=%s, array.length=%s, array.position=%s, length=%s"
							  , buffer_length()
							  , size()
							  , position
							  , array.length()
							  , offset
							  , len)); }
		if (len == 0) { return; }
		int index = this.size;
		this.insert(index, len);
		for (int i = 0; i < len; i++) { this.buffer[index++] = array.charAt(offset++);} 
	}



	
	
	public int append_from_stream_read(Reader reader, int len) throws IOException {
		return this.insert_from_stream_read(this.size, reader, len);
	}
	public int insert_from_stream_read(int position, Reader reader, int len) throws IOException {
		if (position < 0 || position > this.size) { throw new ArrayIndexOutOfBoundsException(
				String.format("position=%s, buffer.size=%s"
							  , position  
							  , len)); }
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
		if (position < 0 || len < 0 || position + len > this.size) { throw new ArrayIndexOutOfBoundsException(
				String.format("buffer.length=%s, buffer.size=%s, position=%s, length=%s"
							  , this.buffer_length()
							  , this.size
							  , position
							  , len)); }
		stream.write(this.buffer, position, len);
	}
	

}
