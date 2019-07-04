package top.fols.box.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class XEncoding {
	public static byte[] chars2Bytes(char[] chars, String cs) {
		return chars2Bytes(chars, Charset.forName(cs));
	}
	public static byte[] chars2Bytes(char[] chars, Charset cs) {
		if (null == chars)
			return null;
		if (null == cs)
			throw new NullPointerException("charset for null");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		int limit = bb.limit();
		byte[] bytes = bb.array();
		if (null != bytes && bytes.length > limit)
			bytes = Arrays.copyOf(bytes, limit);
		return bytes;
	}


	public static char[] bytes2Chars(byte[] bytes, String cs) {
		return bytes2Chars(bytes, Charset.forName(cs));
	}
	public static char[] bytes2Chars(byte[] bytes, Charset cs) {
		if (null == bytes)
			return null;
		if (null == cs)
			throw new NullPointerException("charset for null");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		int limit = bb.limit();
		char[] chars = cb.array();
		if (null != chars && chars.length > limit)
			chars = Arrays.copyOf(chars, limit);
		return chars;
	}
}
