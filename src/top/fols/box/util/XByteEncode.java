package top.fols.box.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.io.os.XRandomAccessFileInputStream;
import top.fols.box.lang.XBits;

public class XByteEncode {

	public static final Charset UNICODE_CHARSET = Charset.forName("UNICODE");

	/**
	 * java char is unicode
	 */
	public static class Unicode {
		/**
		 * Unicode file header for [FE, FF]
		 * using big-endian byte ordering.
		 */

		public static final byte[] getFileHeader() {
			return new byte[] { (byte) 0xFE, (byte) 0xFF };
		}

		/*
		 * data
		 */

		public static long charsLenToBytesLen(long length) {
			return length * XBits.char_byte_length;
		}

		public static long bytesLenToCharsLen(long length) {
			return length / XBits.char_byte_length;
		}

		public static int charsLenToBytesLen(int length) {
			return length * XBits.char_byte_length;
		}
		
		public static int bytesLenToCharsLen(int length) {
			return length / XBits.char_byte_length;
		}

		public static void putCharsToBytes(char[] chars, int charsOff, int charsLen, byte[] bytes, int bytesOff) {
			int coff = charsOff;
			int boff = bytesOff;
			for (int i = 0; i < charsLen; i++) {
				XBits.putBytes(bytes, boff, chars[coff++]);
				boff += XBits.char_byte_length;
			}
		}

		public static void putCharsToBytes(CharSequence chars, int charsOff, int charsLen, byte[] bytes, int bytesOff) {
			int coff = charsOff;
			int boff = bytesOff;
			for (int i = 0; i < charsLen; i++) {
				XBits.putBytes(bytes, boff, chars.charAt(coff++));
				boff += XBits.char_byte_length;
			}
		}

		public static void putBytesToChars(byte[] bytes, int bytesOff, char[] chars, int charsOff, int charsLen) {
			int coff = charsOff;
			int boff = bytesOff;
			for (int i = 0; i < charsLen; i++) {
				chars[coff++] = XBits.getChar(bytes, boff);
				boff += XBits.char_byte_length;
			}
		}

		public static void putChar(byte[] bytes, int bytesOff, char ch) {
			XBits.putBytes(bytes, bytesOff, ch);
		}

		public static char getChar(byte[] bytes, int bytesOff) {
			return XBits.getChar(bytes, bytesOff);
		}
	}

	public static char[] fileBytesToChars(File file, long fileByteOff, int fileByteLen, Charset originEncoding)
			throws FileNotFoundException, UnsupportedEncodingException, IOException {
		XRandomAccessFileInputStream fr0 = new XRandomAccessFileInputStream(file);
		fr0.seekIndex(fileByteOff);

		InputStream is = new XInputStreamFixedLength<XRandomAccessFileInputStream>(fr0, fileByteLen);
		byte[] bufBytes = new byte[fileByteLen];
		int read = is.read(bufBytes);
		if (read == -1) {
			is.close();
			return null;
		} else {
			char[] cs = XByteEncode.bytesToChars(bufBytes, 0, bufBytes.length, originEncoding);
			is.close();
			return cs;
		}
	}

	public static byte[] charsToBytes(char[] chars, String charset) {
		return charsToBytes(chars, 0, chars.length, Charset.forName(charset));
	}

	public static byte[] charsToBytes(char[] chars, int offset, int length, Charset charset) {
		if (null == chars) {
			return null;
		}
		if (null == charset) {
			throw new NullPointerException("charset for null");
		}

		CharBuffer cb = CharBuffer.wrap(chars, offset, length);
		ByteBuffer bb = charset.encode(cb);
		int limit = bb.limit();
		byte[] bytes = bb.array();
		if (null != bytes && bytes.length > limit) {
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
		if (null == chars) {
			return null;
		}
		if (null == charset) {
			throw new NullPointerException("charset for null");
		}
		CharBuffer cb = CharBuffer.wrap(chars, offset, length);
		ByteBuffer bb = charset.encode(cb);
		int limit = bb.limit();
		byte[] bytes = bb.array();
		if (null != bytes && bytes.length > limit) {
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
		if (null != chars && chars.length > limit) {
			chars = Arrays.copyOf(chars, limit);
		}
		bb = null;
		cb = null;
		return chars;
	}

}
