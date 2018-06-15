package top.fols.box.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class XEncodingUtils
{
	public static byte[] getBytes(char[] chars, Charset cs)
	{
		if (chars == null)
			return null;
		if (cs == null)
			throw new NullPointerException("charset for null");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		int limit = bb.limit();
		byte[] bytes = bb.array();
		if(bytes != null && bytes.length > limit)
			bytes = Arrays.copyOf(bytes,limit);
		return bytes;
	}

	public static char[] getChars(byte[] bytes, Charset cs) 
	{
		if (bytes == null)
			return null;
		if (cs == null)
			throw new NullPointerException("charset for null");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		int limit = bb.limit();
		char[] chars = cb.array();
		if(chars != null && chars.length > limit)
			chars = Arrays.copyOf(chars,limit);
		return chars;
	}

}
