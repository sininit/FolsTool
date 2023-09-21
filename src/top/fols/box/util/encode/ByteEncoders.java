package top.fols.box.util.encode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import top.fols.atri.interfaces.abstracts.BitsOptions;
import top.fols.atri.io.util.Streams;
import top.fols.box.io.InputStreamFixedLengths;
import top.fols.atri.io.file.RandomAccessFileInputStream;

public class ByteEncoders {


	public static char[] fileBytesToChars(File file, long fileByteOff, int fileByteLen, Charset originEncoding)
			throws IOException {
		RandomAccessFileInputStream stream = new RandomAccessFileInputStream(file);
		stream.seekIndex(fileByteOff);

		InputStream is = new InputStreamFixedLengths<>(stream, fileByteLen);
		try {
			byte[] bufBytes = new byte[fileByteLen];
			int read = is.read(bufBytes);
			if (read == -1) {
				return null;
			} else {
				return ByteEncoders.bytesToChars(bufBytes, 0, bufBytes.length, originEncoding);
			}
		} finally {
			Streams.close(is);
			Streams.close(stream);
		}
	}



	public static byte[] charsToBytes(char[] chars, String charset) {
		return charsToBytes(chars, 0, chars.length, Charset.forName(charset));
	}
	public static byte[] charsToBytes(char[] chars, int offset, int length, Charset charset) {
		if (null == chars)
			return null;
		if (null == charset)
			throw new NullPointerException("charset for null");

		CharBuffer cb = CharBuffer.wrap(chars, offset, length);
		ByteBuffer bb = charset.encode(cb);
		int limit = bb.limit();
		byte[] bytes = bb.array();
		if (bytes.length > limit) {
			bytes = Arrays.copyOf(bytes, limit);
		}
		bb = null;
		cb = null;
		return bytes;
	}

	public static byte[] charsToBytes(CharSequence chars, String charset) {
		return charsToBytes(chars, 0, chars.length(), Charset.forName(charset));
	}
	public static byte[] charsToBytes(CharSequence chars, int offset, int length, Charset charset) {
		if (null == chars)
			return null;
		if (null == charset)
			throw new NullPointerException("charset for null");

		CharBuffer cb = CharBuffer.wrap(chars, offset, length);
		ByteBuffer bb = charset.encode(cb);
		int limit = bb.limit();
		byte[] bytes = bb.array();
		if (bytes.length > limit) {
			bytes = Arrays.copyOf(bytes, limit);
		}
		bb = null;
		cb = null;
		return bytes;
	}

	public static char[] bytesToChars(byte[] bytes, String charset) {
		return bytesToChars(bytes, 0, bytes.length, Charset.forName(charset));
	}
	public static char[] bytesToChars(byte[] bytes, int offset, int length, Charset charset) {
		if (null == bytes) {
			return null;
		}
		if (null == charset) {
			throw new NullPointerException("charset for null");
		}
		ByteBuffer bb = ByteBuffer.wrap(bytes, offset, length);
		CharBuffer cb = charset.decode(bb);
		int limit = cb.limit();
		char[] chars = cb.array();
		if (chars.length > limit) {
			chars = Arrays.copyOf(chars, limit);
		}
		bb = null;
		cb = null;
		return chars;
	}

}
