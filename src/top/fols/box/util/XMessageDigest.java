package top.fols.box.util;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.XStream;
import top.fols.box.statics.XStaticSystem;
import top.fols.box.util.interfaces.messagedigest.XInterfaceMessageDigest;

public class XMessageDigest {
	public static final String ALGORITHM_MD5 = "MD5";
	/*
	 * SHA-1
	 * SHA-384
	 * SHA-224
	 * SHA-256
	 * SHA-512
	 * MD5
	 */
	// 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）  
	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static XMapKeyCheck<String> MessageDigestAlgorithmss;
	public static MessageDigest getMessageDigest(String manager) {
		if (!getMessageDigestAlgorithms().contains(manager))
			throw new RuntimeException("no such algorithm " + manager);
		try {
			return MessageDigest.getInstance(manager);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	private static XMapKeyCheck<String> getMessageDigestAlgorithms() {
		if (null == MessageDigestAlgorithmss)
			MessageDigestAlgorithmss = XStaticSystem.getMessageDigestAlgorithms();
		return MessageDigestAlgorithmss;
	}
	
	
	
	public static String bufferToHex(MessageDigest m) {
		return bufferToHex(m.digest());
	}
	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}
	private static String bufferToHex(byte bytes[], int offset, int len) {
		char buf[] = new char[len * 2];
        for (int i = offset, x = 0; i < bytes.length; i++) {
            buf[x++] = hexDigits[(bytes[i] >>> 4) & 0xf];
            buf[x++] = hexDigits[bytes[i] & 0xf];
        }
        return new String(buf);
	}
	
	
	
	
	public static String get(String MessageDigestAlgorithms, InputStream input) {
		MessageDigest messageDigest = getMessageDigest(MessageDigestAlgorithms);  
		copy(input, messageDigest);
		return bufferToHex(messageDigest.digest());
	}
	public static String get(String MessageDigestAlgorithms, byte[] input) {
		MessageDigest messageDigest = getMessageDigest(MessageDigestAlgorithms);
		messageDigest.update(input);
		return bufferToHex(messageDigest.digest());
	}
	public static void copy(InputStream input, MessageDigest output) {
		try {
			byte[] buff = new byte[XStream.defaultStreamByteArrBuffSize];
			int read;
			while ((read = input.read(buff)) != -1)
				output.update(buff, 0, read);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



	/*
	 * If the system does not support the md5 algorithm, it will use java computing.
	 * If you use java, the efficiency will be very low.
	 */
	@XAnnotations("if the system does not support the md5 algorithm, it will use java computing. If you use java, the efficiency will be very low.")
	public static XInterfaceMessageDigest md5Instance() {
		try { 
			return new XInterfaceMessageDigest(){

				private MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
				@Override
				public void write(byte[] b, int off, int len) {
					md.update(b, off, len);
				}
				@Override
				public void write(byte[] b) {
					md.update(b);
				}
				@Override
				public void close() {
					md.reset();
				}
				@Override
				public void write(int p1) {
					// TODO: Implement this method
					md.update((byte)p1);
				}
				@Override
				public void clear() {
					// TODO: Implement this method
					md.reset();
				}
				@Override
				public String getHash() {
					// TODO: Implement this method
					return bufferToHex(md);
				}
			};
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}








