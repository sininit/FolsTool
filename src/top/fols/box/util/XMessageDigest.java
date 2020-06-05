package top.fols.box.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import top.fols.box.io.XStream;
import top.fols.box.statics.XStaticSystem;
import top.fols.box.util.encode.XHexEncoder;
import top.fols.box.io.digest.XDigestOutputStream;

public class XMessageDigest {
	
	public static final String ALGORITHM_MD5 = "MD5";
	public static final String ALGORITHM_SHA1 = "SHA1";
	
	/*
	 * MD5 SHA-1 SHA-384 SHA-224 SHA-256 SHA-512
	 */
	public static XKeyMap<String> getMessageDigestAlgorithms() {
		return XStaticSystem.getMessageDigestAlgorithms();
	}
	
	public static boolean contains(String name) {
		return XMessageDigest.getMessageDigestAlgorithms().containsKey(name);
	}
	public static String[] list() {
		XKeyMap<String> map = XMessageDigest.getMessageDigestAlgorithms();
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
		XDigestOutputStream out = XMessageDigest.wrapToStream(d);
		XStream.copy(input, out);
		return out.getValue();
	}
	
	public static byte[] getValue(MessageDigest d, byte[] input) throws IOException {
		XDigestOutputStream out = XMessageDigest.wrapToStream(d);
		XStream.copy(input, out);
		return out.getValue();
	}
	
	
	public static String toHex(byte[] bytes) {
		return XHexEncoder.encodeToString(bytes);
	}
	
	public static XDigestOutputStream<OutputStream> wrapToStream(MessageDigest d) {
		return new XDigestOutputStream<OutputStream>(d);
	}
	
	
	public static MessageDigest md5Instance() {
		return XMessageDigest.getMessageDigest(ALGORITHM_MD5);
	}
	public static MessageDigest sha1Instance() {
		return XMessageDigest.getMessageDigest(ALGORITHM_SHA1);
	}
}
