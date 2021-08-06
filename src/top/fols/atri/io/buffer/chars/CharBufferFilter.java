package top.fols.atri.io.buffer.chars;


import top.fols.atri.io.buffer.BufferFilter;
import top.fols.atri.io.buffer.bytes.ByteBufferFilter;
import top.fols.atri.lang.Objects;

public class CharBufferFilter extends BufferFilter<char[]> {
	
	@Override
	public char[] array(int count) {
		// TODO: Implement this method
		return new char[count];
	}
	@Override
	public char[][] arrays(int count) {
		// TODO: Implement this method
		return new char[count][];
	}
	
	@Override
	public int sizeof(char[] array) {
		// TODO: Implement this method
		return array.length;
	}



	public static final Objects.Cast<char[], char[]> CLONE_CONVERT = new Objects.Cast<char[], char[]>() {
		@Override
		public char[] cast(char[] param) {
			return null == param?null:param.clone();
		}
	};
	@Override
	public CharBufferFilter clone() {
		CharBufferFilter instance = new CharBufferFilter();
		instance.separators = separators.clone(CLONE_CONVERT);
		return instance;
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
