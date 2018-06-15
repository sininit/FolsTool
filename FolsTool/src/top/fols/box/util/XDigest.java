package top.fols.box.util;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import top.fols.box.io.base.XHexStream;
import top.fols.box.lang.XString;
import top.fols.box.statics.XStaticEncode;
public class XDigest
{
	public static class Digest
	{
		public static String getSHA1(InputStream input) 
		{
			return XMessageDigest.getSHA1(input);
		}
		public static String getMD5(InputStream input) 
		{
			return XMessageDigest.getMD5(input);
		}
		public static String getSHA1(byte[] input) 
		{
			return XMessageDigest.getSHA1(input);
		}
		public static String getMD5(byte[] input) 
		{
			return XMessageDigest.getMD5(input);
		}
	}




	public static class Hex
	{
		public static String encode(byte[] src)  
		{  
			return XHexStream.encode2String(src);
		} 
		public static byte[] decode(String hexString)
		{  
			return XHexStream.decode(hexString);
		}   
	}





	public static class Unicode
	{
		//"([^\\x00-\\xff])"双字节字符
		public static String encode(String str)
		{
			String tmp;
			StringBuffer sb = new StringBuffer(1000);
			char c;
			int i, j;
			sb.setLength(0);
			try
			{
				for (i = 0; i < str.length(); i++)
				{
					c = str.charAt(i);
					if (c > 255)
					{
						sb.append("\\u");
						j = (c >>> 8);
						tmp = Integer.toHexString(j);
						if (tmp.length() == 1)
							sb.append("0");
						sb.append(tmp);
						j = (c & 0xFF);
						tmp = Integer.toHexString(j);
						if (tmp.length() == 1)
							sb.append("0");
						sb.append(tmp);
					}
					else
					{
						sb.append(c);
					}

				}
				return (new String(sb));
			}
			catch (Exception e2)
			{
				throw new IllegalAccessError(e2.getMessage());
			}
		}
		public static String decode(String str)
		{
			int i = 0;
			try
			{
				Charset forName = Charset.forName("UTF-16");
				Matcher matcher = Pattern.compile("\\\\u([0-9a-fA-F]{4})").matcher(str);
				StringBuffer stringBuffer = new StringBuffer();
				while (matcher.find(i))
				{
					int start = matcher.start();
					if (start > i)
					{
						stringBuffer.append(str.substring(i, start));
					}
					i = Integer.valueOf(matcher.group(1), 16).intValue();
					byte[] bArr = new byte[4];
					bArr[0] = (byte) ((i >> 8) & 255);
					bArr[1] = (byte) (i & 255);
					stringBuffer.append(String.valueOf(forName.decode(ByteBuffer.wrap(bArr))).trim());
					i = matcher.end();
				}
				int length = str.length();
				if (length > i)
				{
					stringBuffer.append(str.substring(i, length));
				}
				return stringBuffer.toString();
			}
			catch (Exception e)
			{
				throw new IllegalAccessError(e.getMessage());
			}
		}
	}




	public static class Base64
	{   
		private static char[] base64EncodeChars = new char[] {   
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',   
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',   
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',   
			'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',   
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',   
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v',   
			'w', 'x', 'y', 'z', '0', '1', '2', '3',   
			'4', '5', '6', '7', '8', '9', '+', '/' };   
		private static byte[] base64DecodeChars = new byte[] {   
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,   
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,   
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,   
			52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,   
			-1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,   
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,   
			-1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,   
			41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };
		public static String encode(byte[] data)
		{   
			StringBuffer sb = new StringBuffer();   
			int len = data.length;   
			int i = 0;   
			int b1, b2, b3;   
			try
			{
				while (i < len)
				{   
					b1 = data[i++] & 0xff;   
					if (i == len)   
					{   
						sb.append(base64EncodeChars[b1 >>> 2]);   
						sb.append(base64EncodeChars[(b1 & 0x3) << 4]);   
						sb.append("==");   
						break;   
					}   
					b2 = data[i++] & 0xff;   
					if (i == len)   
					{   
						sb.append(base64EncodeChars[b1 >>> 2]);   
						sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);   
						sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);   
						sb.append("=");   
						break;   
					}   
					b3 = data[i++] & 0xff;   
					sb.append(base64EncodeChars[b1 >>> 2]);   
					sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);   
					sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);   
					sb.append(base64EncodeChars[b3 & 0x3f]);   
				}
			}
			catch (Exception e)
			{
			}
			return sb.toString();   
		}   

		public static byte[] decode(String str) throws UnsupportedEncodingException
		{   
			StringBuffer sb = new StringBuffer();   
			byte[] data = str.getBytes("US-ASCII");   
			int len = data.length;   
			int i = 0;   
			int b1, b2, b3, b4;
			try
			{
				while (i < len)
				{   
					/* b1 */   
					do {
						b1 = base64DecodeChars[data[i++]];   
					} while (i < len && b1 == -1);   
					if (b1 == -1) break;   

					/* b2 */   
					do {   
						b2 = base64DecodeChars[data[i++]];   
					} while (i < len && b2 == -1);   
					if (b2 == -1) break;   
					sb.append((char)((b1 << 2) | ((b2 & 0x30) >>> 4)));   

					/* b3 */   
					do {   
						b3 = data[i++];   
						if (b3 == 61) return sb.toString().getBytes("ISO-8859-1");   
						b3 = base64DecodeChars[b3];   
					} while (i < len && b3 == -1);   
					if (b3 == -1) break;   
					sb.append((char)(((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));   

					/* b4 */   
					do {
						b4 = data[i++];   
						if (b4 == 61) return sb.toString().getBytes("ISO-8859-1");   
						b4 = base64DecodeChars[b4];   
					} while (i < len && b4 == -1);   
					if (b4 == -1) break;   
					sb.append((char)(((b3 & 0x03) << 6) | b4));   
				}
			}
			catch (Exception e)
			{
			}
			return sb.toString().getBytes("ISO-8859-1");   
		}   
	}


	public static class URL
	{
		/*
		 search %XX index
		 */
		public static class indexOfURLCode
		{
			private String text;
			private int start = -1;
			private int end = -1;
			public indexOfURLCode(String text)
			{
				this.text = text;
			}
			public int find(String text, int start)
			{
				for (;start < text.length();start++)
				{
					if (text.charAt(start) == '%')
						if (isURLCode(start))
							return start;
				}

				return -1;
			}
			public boolean find()
			{
				int start = this.end > -1 ?this.end: 0;
				int starti = find(text, start);
				this.start = starti;
				if (starti == -1)
				{
					this.end = -1;
					return false;
				}
				starti += 3;
				while (true)
				{
					if (!isURLCode(starti))
					{
						break;
					}
					starti += 3;
				}
				this.end = starti;
				return true;
			}
			/*0-9
			 48>=x<=57*/
			/*a-f
			 97>=x<=102*/
			/*a-f
			 65>=x<=70*/
			private boolean isURLCode(int start)
			{
				if (start + 2 > text.length())
					return false;
				if (text.charAt(start) == '%' && isURLCode(text.charAt(start + 1)) && isURLCode(text.charAt(start + 2)))
				{
					return true;
				}
				return false;
			}
			private boolean isURLCode(char c)
			{
				//if(c == '0' || c== '1' || c== '2' || c== '3' || c== '4' || c== '5' || c== '6' || c== '7' || c== '8' || c== '9' || c== 'a' || c== 'b' || c== 'c' || c== 'd' || c== 'e' || c== 'f' || c== 'A'|| c== 'B' || c== 'C' || c== 'D' || c== 'E' || c== 'F')return true;
				return "0123456789abcdefABCDEF".indexOf(c) > -1;
			}
			public int start()
			{
				return start;
			}
			public int end()
			{
				return end;
			}
			public String group()
			{
				return text.substring(start, end);
			}
		}
		/*
		 [\u4E00-\u9FA5] 中文
		 %([0-9a-fA-F]{2})匹配%xx
		 适配一段范围的。(%[0-9a-fA-F]{2})+
		 */
		//格式化URL 将% 替换为%25
		public static String decodeFormat(String text)
		{
			return text.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
		}
		//URL编码
		public static String decodeS(String text, String str2)
		{
			try
			{
				if (XString.isEmpty(text))
					return "";
				str2 = XString.isEmpty(str2) ?XStaticEncode.encodeName_UTF_8: str2;
				//"(%[0-9a-fA-F]{2})+"
				indexOfURLCode indexOf = new indexOfURLCode(text);
				StringBuffer buf = new StringBuffer();
				int start = 0,end;
				if (!indexOf.find())
					return text;
				while (true)
				{
					end = indexOf.start();
					buf.append(text.substring(start, end)).append(URLDecoder.decode(indexOf.group(), str2));
					start = indexOf.end();
					if (!indexOf.find())
						break;

				}
				buf.append(text.substring(start));
				return buf.toString();
			}
			catch (Exception e)
			{
				return text;
			}
		}
		//解码 官方的
		public static String decode(String str, String str2)
		{
			try
			{
				return URLDecoder.decode(str.replaceAll("%(?![0-9a-fA-F]{2})", "%25"), XString.isEmpty(str2) ?XStaticEncode.encodeName_UTF_8: str2);
			}
			catch (Exception e)
			{
				return str;
			}
		}
		//编码 官方的
		public static String encode(String str, String str2)
		{
			try
			{
				return URLEncoder.encode(str, XString.isEmpty(str2) ?XStaticEncode.encodeName_UTF_8: str2);
			}
			catch (Exception e)
			{
				return str;
			}
		}
	}
}
