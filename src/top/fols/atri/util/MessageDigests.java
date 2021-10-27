package top.fols.atri.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import top.fols.atri.io.Streams;
import top.fols.atri.lang.Finals;
import top.fols.box.util.encode.XHexEncoder;
import top.fols.box.io.digest.XDigestOutputStream;

@SuppressWarnings({"rawtypes", "UnnecessaryLocalVariable"})
public class MessageDigests {
	
	public static final String ALGORITHM_MD5 = "MD5";
	public static final String ALGORITHM_SHA1 = "SHA1";
	
	/*
	 * MD5 SHA-1 SHA-384 SHA-224 SHA-256 SHA-512
	 */
	public static KeySetMap<String> getMessageDigestAlgorithms() {
		return Finals.getMessageDigestAlgorithms();
	}
	
	public static boolean contains(String name) {
		return MessageDigests.getMessageDigestAlgorithms().containsKey(name);
	}
	public static String[] list() {
		KeySetMap<String> map = MessageDigests.getMessageDigestAlgorithms();
		return map.toArray(new String[map.size()]);
	}
	
	public static MessageDigest getMessageDigest(String name) throws RuntimeException {
		try {
			return MessageDigest.getInstance(name);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] getValue(MessageDigest d, InputStream input) throws IOException {
		XDigestOutputStream out = MessageDigests.wrapToStream(d);
		Streams.copy(input, out);
		return out.getValue();
	}
	
	public static byte[] getValue(MessageDigest d, byte[] input) throws IOException {
		XDigestOutputStream out = MessageDigests.wrapToStream(d);
		out.write(input);
		out.flush();
		return out.getValue();
	}
	
	
	public static String toHex(byte[] bytes) {
		return XHexEncoder.encodeToString(bytes);
	}
	
	public static XDigestOutputStream<OutputStream> wrapToStream(MessageDigest d) {
		return new XDigestOutputStream<>(d);
	}
	
	
	public static MessageDigest md5Instance() {
		return MessageDigests.getMessageDigest(ALGORITHM_MD5);
	}
	public static MessageDigest sha1Instance() {
		return MessageDigests.getMessageDigest(ALGORITHM_SHA1);
	}


	public static byte[] getMD5Digest(byte[] bytes) {
		MessageDigest messageDigest = md5Instance();
		try {
			byte[] digest = getValue(messageDigest, bytes);
			return digest;
		} catch (IOException e) {
			return null;
		}
	}

	public static String md5Hex(byte[] bytes) {
		byte[] digest = getMD5Digest(bytes);
		return XHexEncoder.encodeToString(digest);
	}



	public static byte[] getSha1Digest(byte[] bytes) {
		MessageDigest messageDigest = sha1Instance();
		try {
			byte[] digest = getValue(messageDigest, bytes);
			return digest;
		} catch (IOException e) {
			return null;
		}
	}
	public static String sha1Hex(byte[] bytes) {
		byte[] digest = getSha1Digest(bytes);
		return XHexEncoder.encodeToString(digest);
	}
}
