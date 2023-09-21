package top.fols.atri.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import top.fols.atri.io.util.Streams;
import top.fols.atri.io.digest.DigestOutputStream;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.box.util.encode.HexEncoders;

public class MessageDigests {
	
	public static final String ALGORITHM_MD5 = "MD5";
	public static final String ALGORITHM_SHA1 = "SHA1";
	
	/*
	 * MD5 SHA-1 SHA-384 SHA-224 SHA-256 SHA-512
	 */
	public static Set<String> algorithms() {
		return Finals.getMessageDigestAlgorithms();
	}
	public static boolean contains(String name) {
		return MessageDigests.algorithms().contains(name);
	}
	public static String[] list() {
		return MessageDigests.algorithms().toArray(new String[]{});
	}
	
	public static MessageDigest getMessageDigest(String name) throws UnsupportedOperationException {
		try {
			return MessageDigest.getInstance(name);
		} catch (NoSuchAlgorithmException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public static String toHex(byte[] bytes) {
		return HexEncoders.encodeToString(bytes);
	}
	public static DigestOutputStream<OutputStream> wrapToStream(MessageDigest d) {
		return new DigestOutputStream<>(d);
	}
	
	
	public static MessageDigest md5() {
		return MessageDigests.getMessageDigest(ALGORITHM_MD5);
	}
	public static MessageDigest sha1() {
		return MessageDigests.getMessageDigest(ALGORITHM_SHA1);
	}






	OutputStream cast;
	OutputStream ouput() {
		if (!on) {
			throw new UnsupportedOperationException("closed");
		}
		OutputStream cast = this.cast;
		if (null ==  cast) {
			this.cast = cast = top.fols.atri.util.MessageDigests.wrapToStream(this.stream);
		}
		return cast;
	}


	public MessageDigest stream() {
		return this.stream;
	}





	MessageDigest stream;
	boolean on = true;


	public MessageDigests(String algorithms) {
		this(top.fols.atri.util.MessageDigests.getMessageDigest(algorithms));
	}
	public MessageDigests(MessageDigest algorithms) {
		this.stream = Objects.requireNonNull(algorithms, "algorithms");
	}

	public MessageDigests write(int p1) {
		// TODO: Implement this method
		if (!on) {
			throw new UnsupportedOperationException("closed");
		}
		this.stream.update((byte)p1);
		return this;
	}


	public MessageDigests write(byte[] b) {
		if (null != b) {
			this.write(b, 0, b.length);
		}
		return this;
	}
	public MessageDigests write(byte[] b, int off, int len) {
		if (!on) {
			throw new UnsupportedOperationException("closed");
		}
		this.stream.update(b, off, len);
		return this;
	}


	public MessageDigests write(File stream) throws IOException {
		Streams.copy(new FileInputStream(stream), this.ouput());
		return this;
	}
	public MessageDigests write(InputStream stream) throws IOException {
		Streams.copy(stream, this.ouput());
		return this;
	}


	public MessageDigests write(String str) {
		this.write(str.getBytes());
		return this;
	}
	public MessageDigests write(String str, Charset charset) {
		this.write(str.getBytes(charset));
		return this;
	}








	public MessageDigests close() {
		this.on = false;
		return this;
	}

	public boolean isClose() {
		return this.on;
	}

	public MessageDigests reset() {
		this.stream.reset();
		return this;
	}


	public byte[] digest() {
		return this.stream.digest();
	}
	public String digestHex() {
		return HexEncoders.encodeToString(this.digest()).toLowerCase();
	}
	public String digestHexUpper() {
		return this.digestHex().toUpperCase();
	}
}
