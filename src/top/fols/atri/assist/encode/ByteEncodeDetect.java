package top.fols.atri.assist.encode;

/**
 * <Detect encoding .>
 *  Copyright (C) <2009>  <Fluck,ACC http://androidos.cc/dev>
 *
 *   This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * EncodingDetect.java<br>
 * @author Billows.Van
 * @since Create on 2010-01-27 11:19:00   
 * @version 1.0 
 * https://github.com/userkdg/FileEncode-IO <?>
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;

import top.fols.atri.io.BytesOutputStreams;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Mathz;
import top.fols.box.io.interfaces.IIOBigSequenceByte;

@SuppressWarnings("SameParameterValue")
public class ByteEncodeDetect {
	private static IIOBigSequenceByte wrapBigByteSequence(final byte[] bytes) {
		return wrapBigByteSequence(bytes, 0, bytes.length);
	}
	private static IIOBigSequenceByte wrapBigByteSequence(final byte[] bytes, final int off, final int len) {
		return new IIOBigSequenceByte() {
			@Override
			public long length() {
				// TODO: Implement this method
				return len;
			}

			@Override
			public byte byteAt(long p1) {
				// TODO: Implement this method
				return bytes[off + (int) p1];
			}
		};
	}


	public static String getJavaEncode(File bytes) throws IOException, RuntimeException {
		return detectEncoding(bytes);
	}
	public static Charset getJavaEncode2Charset(File bytes) throws IOException, RuntimeException {
		return charsetForName(detectEncoding(bytes));
	}

	public static String getJavaEncode(byte[] bytes) throws RuntimeException {
		try {
			return detectEncoding(bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static Charset getJavaEncode2Charset(byte[] bytes) throws RuntimeException {
		try {
			return charsetForName(detectEncoding(bytes));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getJavaEncode(InputStream bytes) throws IOException, RuntimeException {
		return detectEncoding(bytes);
	}
	public static Charset getJavaEncode2Charset(InputStream bytes) throws IOException, RuntimeException {
		return charsetForName(detectEncoding(bytes));
	}

	private static Charset charsetForName(String name) throws NullPointerException, UnsupportedCharsetException {
		return Charset.forName(name);
	}



	public static String detectEncoding(File rawtextFile) throws IOException {
		byte[] pbs = findResolveBytes(rawtextFile, 8192, rawtextFile.length());
		return getEncodingType(pbs);
	}
	public static String detectEncoding(byte[] rawtextFile) throws IOException {
		byte[] pbs = findResolveBytes(rawtextFile, 8192, rawtextFile.length);
		return getEncodingType(pbs);
	}
	public static String detectEncoding(InputStream rawtextFile) throws IOException {
		byte[] pbs = findResolveBytes(rawtextFile, 8192, Long.MAX_VALUE);
		return getEncodingType(pbs);
	}

	private static String getEncodingType(byte[] pbs) throws IOException {
		IIOBigSequenceByte rawtext = ByteEncodeDetect.wrapBigByteSequence(pbs);
		return new ByteEncodingDetect().detectEncodingAsName(rawtext);
	}


	/*
	 * 各种字符的unicode编码的范围： 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
	 * 数字：[0x30,0x39]（或十进制[48, 57]） 小写字母：[0x61,0x7a]（或十进制[97, 122]）
	 * 大写字母：[0x41,0x5a]（或十进制[65, 90]）
	 *
	 * !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`
	 * abcdefghijklmnopqrstuvwxyz{|}~ byte 33-126;
	 *
	 */
	public static byte[] findResolveBytes(File f, int length, long maxForLength) throws IOException {
		FileInputStream input = new FileInputStream(f);
		byte[] bytes = findResolveBytes(input, length, maxForLength);
		input.close();
		return bytes;
	}

	public static byte[] findResolveBytes(InputStream input, int length, long maxForLength) throws IOException {
		byte[] test = new byte[length];
		int read = -1;
		long startIndex = 0;
		BytesOutputStreams out = new BytesOutputStreams();
		while ((read = input.read(test)) != -1) {
			for (int i = 0; i < read; i++) {
				byte b = test[i];
				if (!((b >= 33 && b <= 126) || b == '\t' || b == '\r' || b == '\n' || b == ' ' || b == '\b' || b == '\f')) {
					out.write(test, i, read - i);

					read = input.read(test);
					if (read != -1)
						out.write(test, 0, read);
					test = out.toByteArray();
					out.close();
					return test;
				}
				startIndex++;
				if (startIndex > maxForLength) {
					read = input.read(test);
					if (read == -1) {
						out.close();
						return Finals.EMPTY_BYTE_ARRAY;
					}
					out.close();
					return Arrays.copyOfRange(test, 0, read);
				}
			}
		}
		out.close();
		return Finals.EMPTY_BYTE_ARRAY;
	}

	@SuppressWarnings("ForLoopReplaceableByForEach")
	public static byte[] findResolveBytes(byte[] f, int length, int maxForLength) {
		int filelength = f.length;
		if (filelength < 1) {
			return Finals.EMPTY_BYTE_ARRAY;
		}
		int startIndex = 0;
		for (int i = 0; i < filelength; i++) {
			byte b = f[i];
			if (!((b >= 33 && b <= 126) || b == '\t' || b == '\r' || b == '\n' || b == ' ' || b == '\b' || b == '\f')) {
				break;
			}
			startIndex++;
			if (startIndex > maxForLength) {
				break;
			}
		}
		if (startIndex >= filelength) {
			return Finals.EMPTY_BYTE_ARRAY;
		}
		return Arrays.copyOfRange(f, startIndex, Mathz.min(startIndex + length, filelength));
	}

}
