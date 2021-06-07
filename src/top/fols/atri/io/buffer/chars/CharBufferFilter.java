package top.fols.atri.io.buffer.chars;


import top.fols.atri.io.buffer.BufferFilter;
import top.fols.atri.io.buffer.bytes.ByteBufferFilter;

public class CharBufferFilter extends BufferFilter<char[]> {
	
	@Override
	public char[] array(int count) {
		// TODO: Implement this method
		return new char[count];
	}
	@Override
	public char[][] array2(int count) {
		// TODO: Implement this method
		return new char[count][];
	}
	
	@Override
	public int sizeof(char[] array) {
		// TODO: Implement this method
		return array.length;
	}


	@Override
	public CharBufferFilter clone() {
		CharBufferFilter instance = new CharBufferFilter();
		instance.separators = separators.clone();
		return instance;
	}

	static final CharBufferFilter READ_LINE_FILTER = new CharBufferFilter() {{
		this.addSeparator(new char[]{'\r', '\n'});
		this.addSeparator(new char[]{'\r'});
		this.addSeparator(new char[]{'\n'});
	}
		@Override
		protected boolean accept(int last, int search, char[] split, boolean readEnd) {
			return super.accept(last, search, split, readEnd);
		}
	};
	public static CharBufferFilter getReadLineFilter() {
		return READ_LINE_FILTER.clone();
	}


	public void addSeparator(String separator) {
		// TODO: Implement this method
		super.addSeparator(separator.toCharArray());
	}

	public String resultString(boolean addSeparator) {
		// TODO: Implement this method
		char[] buffer = buffer().buffer_inner();
		boolean readToSeparator = this.contentReadToSeparator();
		char[] seachSeparator = this.contentSeparator();
		int offset = this.contentOffset();
		int count = this.contentLength() + (readToSeparator && addSeparator ?sizeof(seachSeparator): 0);
		return new String(buffer, offset, count);
	}
	
	
	
	
	
	
	
}
