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
import top.fols.atri.util.abstracts.ABitsOptions;

public class XByteEncode {

	public static final Charset UNICODE_CHARSET = Charset.forName("UNICODE");


	public static abstract class UnicodeOption {
		public abstract byte[] getFileHeader();

		public abstract long charsLenToBytesLen(long length);
		public abstract long bytesLenToCharsLen(long length);
		public abstract int charsLenToBytesLen(int length);
		public abstract int bytesLenToCharsLen(int length);




		public abstract void putCharsToBytes(char[] chars, int charsOff, int charsLen, byte[] bytes, int bytesOff);
		public abstract void putCharsToBytes(CharSequence chars, int charsOff, int charsLen, byte[] bytes, int bytesOff);
		public abstract void putBytesToChars(byte[] bytes, int bytesOff, char[] chars, int charsOff, int charsLen);


		public abstract void putChar(byte[] bytes, int bytesOff, char ch);
		public abstract char getChar(byte[] bytes, int bytesOff);


		public static final XByteEncode.BigEndianUnicode BIG_ENDIAN = new XByteEncode.BigEndianUnicode();//java default
		public static final XByteEncode.LittleEndianUnicode LITTLE_ENDIAN = new XByteEncode.LittleEndianUnicode();
	}






	/**
	 * java char is unicode
	 */
	public static class BigEndianUnicode extends UnicodeOption {
		/**
		 * Unicode file header for [FE, FF]
		 * using big-endian byte ordering.
		 */
		@Override
		public final byte[] getFileHeader() {
			return new byte[] { (byte) 0xFE, (byte) 0xFF };
		}

		/*
		 * data
		 */
		@Override
		public long charsLenToBytesLen(long length) { return length * ABitsOptions.BIG_ENDIAN.char_byte_length; }
		@Override
		public long bytesLenToCharsLen(long length) { return length / ABitsOptions.BIG_ENDIAN.char_byte_length; }
		@Override
		public int charsLenToBytesLen(int length) { return length * ABitsOptions.BIG_ENDIAN.char_byte_length; }
		@Override
		public int bytesLenToCharsLen(int length) { return length / ABitsOptions.BIG_ENDIAN.char_byte_length; }

		@Override
		public void putCharsToBytes(char[] chars, int charsOff, int charsLen, byte[] bytes, int bytesOff) {
			int coff = charsOff;
			int boff = bytesOff;
			for (int i = 0; i < charsLen; i++) {
				ABitsOptions.BIG_ENDIAN.putBytes(bytes, boff, chars[coff++]);
				boff += ABitsOptions.BIG_ENDIAN.char_byte_length;
			}
		}

		@Override
		public void putCharsToBytes(CharSequence chars, int charsOff, int charsLen, byte[] bytes, int bytesOff) {
			int coff = charsOff;
			int boff = bytesOff;
			for (int i = 0; i < charsLen; i++) {
				ABitsOptions.BIG_ENDIAN.putBytes(bytes, boff, chars.charAt(coff++));
				boff += ABitsOptions.BIG_ENDIAN.char_byte_length;
			}
		}

		@Override
		public void putBytesToChars(byte[] bytes, int bytesOff, char[] chars, int charsOff, int charsLen) {
			int coff = charsOff;
			int boff = bytesOff;
			for (int i = 0; i < charsLen; i++) {
				chars[coff++] = ABitsOptions.BIG_ENDIAN.getChar(bytes, boff);
				boff += ABitsOptions.BIG_ENDIAN.char_byte_length;
			}
		}

		@Override
		public void putChar(byte[] bytes, int bytesOff, char ch) {
			ABitsOptions.BIG_ENDIAN.putBytes(bytes, bytesOff, ch);
		}

		@Override
		public char getChar(byte[] bytes, int bytesOff) {
			return ABitsOptions.BIG_ENDIAN.getChar(bytes, bytesOff);
		}
	}
	public static class LittleEndianUnicode extends UnicodeOption {
		/**
		 * Unicode file header for [FF, FE]
		 * using little-endian byte ordering.
		 */

		@Override
		public final byte[] getFileHeader() { return new byte[] { (byte) 0xFF, (byte) 0xFE }; }

		/*
		 * data
		 */
		@Override
		public long charsLenToBytesLen(long length) { return length * ABitsOptions.LITTLE_ENDIAN.char_byte_length; }
		@Override
		public long bytesLenToCharsLen(long length) { return length / ABitsOptions.LITTLE_ENDIAN.char_byte_length; }
		@Override
		public int charsLenToBytesLen(int length) { return length * ABitsOptions.LITTLE_ENDIAN.char_byte_length; }
		@Override
		public int bytesLenToCharsLen(int length) { return length / ABitsOptions.LITTLE_ENDIAN.char_byte_length; }

		@Override
		public void putCharsToBytes(char[] chars, int charsOff, int charsLen, byte[] bytes, int bytesOff) {
			int coff = charsOff;
			int boff = bytesOff;
			for (int i = 0; i < charsLen; i++) {
				ABitsOptions.LITTLE_ENDIAN.putBytes(bytes, boff, chars[coff++]);
				boff += ABitsOptions.LITTLE_ENDIAN.char_byte_length;
			}
		}

		@Override
		public void putCharsToBytes(CharSequence chars, int charsOff, int charsLen, byte[] bytes, int bytesOff) {
			int coff = charsOff;
			int boff = bytesOff;
			for (int i = 0; i < charsLen; i++) {
				ABitsOptions.LITTLE_ENDIAN.putBytes(bytes, boff, chars.charAt(coff++));
				boff += ABitsOptions.LITTLE_ENDIAN.char_byte_length;
			}
		}

		@Override
		public void putBytesToChars(byte[] bytes, int bytesOff, char[] chars, int charsOff, int charsLen) {
			int coff = charsOff;
			int boff = bytesOff;
			for (int i = 0; i < charsLen; i++) {
				chars[coff++] = ABitsOptions.LITTLE_ENDIAN.getChar(bytes, boff);
				boff += ABitsOptions.LITTLE_ENDIAN.char_byte_length;
			}
		}

		@Override
		public void putChar(byte[] bytes, int bytesOff, char ch) {
			ABitsOptions.LITTLE_ENDIAN.putBytes(bytes, bytesOff, ch);
		}

		@Override
		public char getChar(byte[] bytes, int bytesOff) {
			return ABitsOptions.LITTLE_ENDIAN.getChar(bytes, bytesOff);
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
