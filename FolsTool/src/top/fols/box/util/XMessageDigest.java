package top.fols.box.util;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import top.fols.box.statics.XStaticSystem;
import top.fols.box.io.XStream;

public class XMessageDigest
{
	// 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）  
	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static List<String> MessageDigestAlgorithmss;
	public static MessageDigest getMessageDigest(String manager)
	{
		if (!getMessageDigestAlgorithms().contains(manager))
			throw new RuntimeException("no such algorithm " + manager);
		try
		{
			return MessageDigest.getInstance(manager);
		}
		catch (NoSuchAlgorithmException e)
		{
			return null;
		}
	}

	
	
	public static String bufferToHex(MessageDigest m)
	{
		return bufferToHex(m.digest());
	}
	
	
	private static String bufferToHex(byte bytes[])
	{
		return bufferToHex(bytes, 0, bytes.length);
	}
	private static String bufferToHex(byte bytes[], int m, int n)
	{
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++)
			appendHexPair(bytes[l], stringbuffer);
		return stringbuffer.toString();
	}
	private static void appendHexPair(byte bt, StringBuffer stringbuffer)
	{
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}



	public static List<String> getMessageDigestAlgorithms()
	{
		if (MessageDigestAlgorithmss == null)
			MessageDigestAlgorithmss = XStaticSystem.getMessageDigestAlgorithms();
		return MessageDigestAlgorithmss;
	}
	public static String get(String MessageDigestAlgorithms, InputStream input) 
	{
		MessageDigest messageDigest = getMessageDigest(MessageDigestAlgorithms);  
		copyInputStream(input, messageDigest);
		return bufferToHex(messageDigest.digest());
	}
	public static String get(String MessageDigestAlgorithms, byte[] input) 
	{
		MessageDigest messageDigest = getMessageDigest(MessageDigestAlgorithms);
		messageDigest.update(input);
		return bufferToHex(messageDigest.digest());
	}



	public static String getSHA1(InputStream input) 
	{
		return get("SHA-1", input);
	}
	public static String getSHA1(byte[] input)  
	{
		return get("SHA-1", input);
	}
	public static String getMD5(InputStream input) 
	{
		return get("MD5", input);
	}
	public static String getMD5(byte[] input) 
	{
		return get("MD5", input);
	}




	private static void copyInputStream(InputStream input, MessageDigest output) 
	{
		try
		{
			byte[] buff = new byte[XStream.default_streamByteArrBuffSize];
			int read;
			while ((read = input.read(buff)) != -1)
				output.update(buff, 0, read);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
