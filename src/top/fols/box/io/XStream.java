package top.fols.box.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import top.fols.atri.io.Streams;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.base.XByteArrayInputStream;
import top.fols.box.io.base.XByteArrayOutputStream;
import top.fols.box.io.base.XCharArrayWriter;

public class XStream {
	public static final int DEFAULT_BYTE_BUFF_SIZE = 8192;
	public static final int DEFAULT_CHAR_BUFF_SIZE = 8192;

	public static final int COPY_UNLIMIT_COPYLENGTH = -1;

	public static int copy(byte[] input, OutputStream output) throws IOException {
		return copy(input, output, false);
	}

	public static int copy(byte[] input, OutputStream output, boolean autoflush) throws IOException {
		if (null == input) {
			return 0;
		}
		output.write(input);
		if (autoflush) {
			output.flush();
		}
		return input.length;
	}

	public static long copy(InputStream input, OutputStream output) throws IOException {
		return copy(input, output, XStream.DEFAULT_BYTE_BUFF_SIZE);
	}

	public static long copy(InputStream input, OutputStream output, int bufflen) throws IOException {
		return copy(input, output, bufflen, false);
	}

	public static long copy(InputStream input, OutputStream output, int bufflen, boolean autoflush) throws IOException {
		return copyFixedLength(input, output, bufflen, XStream.COPY_UNLIMIT_COPYLENGTH, autoflush);
	}

	public static long copyFixedLength(InputStream input, OutputStream output, long copyLength) throws IOException {
		return copyFixedLength(input, output, XStream.DEFAULT_BYTE_BUFF_SIZE, copyLength);
	}

	public static long copyFixedLength(InputStream input, OutputStream output, int bufflen, long copyLength)
			throws IOException {
		return copyFixedLength(input, output, bufflen, copyLength, false);
	}

	/**
	 * 
	 * @param input      off
	 * @param output     to
	 * @param bufflen    buff size
	 * @param copyLength <= -1, no limit, == 0 not execution
	 * @param autoflush  write buff after flush();
	 * @return already copy length
	 * @throws IOException
	 */
	public static long copyFixedLength(InputStream input, OutputStream output, int bufflen, long copyLength,
			boolean autoflush) throws IOException {
		return Streams.copyFixedLength(input, output, bufflen, copyLength, autoflush);
	}

	public static int copy(char[] input, Writer output) throws IOException {
		return copy(input, output, false);
	}

	public static int copy(char[] input, Writer output, boolean autoflush) throws IOException {
		if (null == input) {
			return 0;
		}
		output.write(input);
		if (autoflush) {
			output.flush();
		}
		return input.length;
	}

	public static long copy(Reader input, Writer output) throws IOException {
		return copy(input, output, XStream.DEFAULT_CHAR_BUFF_SIZE);
	}

	public static long copy(Reader input, Writer output, int bufflen) throws IOException {
		return copy(input, output, bufflen, false);
	}

	public static long copy(Reader input, Writer output, int bufflen, boolean autoflush) throws IOException {
		return copyFixedLength(input, output, bufflen, XStream.COPY_UNLIMIT_COPYLENGTH, autoflush);
	}

	public static long copyFixedLength(Reader input, Writer output, long copyLength) throws IOException {
		return copyFixedLength(input, output, XStream.DEFAULT_CHAR_BUFF_SIZE, copyLength);
	}

	public static long copyFixedLength(Reader input, Writer output, int bufflen, long copyLength) throws IOException {
		return copyFixedLength(input, output, bufflen, copyLength, false);
	}

	/**
	 * 
	 * @param input      off
	 * @param output     to
	 * @param bufflen    buff size
	 * @param copyLength <= -1, no limit, == 0 not execution 
	 * @param autoflush  write buff after flush();
	 * @return already copy length
	 * @throws IOException
	 */
	public static long copyFixedLength(Reader input, Writer output, int bufflen, long copyLength, boolean autoflush)
			throws IOException {
		return Streams.copyFixedLength(input, output, bufflen, copyLength, autoflush);
	}





//	public static class InputStreamTool {
//		public static String toString(InputStream input, String encoding) throws IOException {
//			return new String(toByteArray(input), encoding);
//		}
//
//		public static String toString(InputStream input) throws IOException {
//			return new String(toByteArray(input));
//		}
//
//		public static byte[] toByteArray(InputStream input) throws IOException {
//			if (null != input) {
//				XByteArrayOutputStream byteArrayout = new XByteArrayOutputStream();
//				copy(input, byteArrayout);
//				byte[] bs = byteArrayout.toByteArray();
//				byteArrayout.releaseBuffer();
//				return bs;
//			}
//			return null;
//		}
//
//		public static byte[] toByteArray(InputStream input, int length) throws IOException {
//			if (null != input) {
//				XByteArrayOutputStream byteArrayout = new XByteArrayOutputStream();
//				copyFixedLength(input, byteArrayout, length);
//				byte[] bs = byteArrayout.toByteArray();
//				byteArrayout.releaseBuffer();
//				return bs;
//			}
//			return null;
//		}
//
//		@XAnnotations("this no buffered")
//		public byte[] readLine(InputStream input, byte separator) throws IOException {
//			int bufsize = XStream.DEFAULT_BYTE_BUFF_SIZE;
//			byte[] buf = new byte[bufsize];
//			int size = 0;
//			int rb;
//			do {
//				if ((rb = input.read()) == -1) {
//					if (size == 0) {
//						return null;
//					}
//					break;
//				}
//				buf[size++] = (byte) rb;
//				if (size >= buf.length) {
//					// byte[] originbuf = buf;
//					byte[] newbuf = new byte[size + bufsize];
//					System.arraycopy(buf, 0, newbuf, 0, buf.length);
//					buf = newbuf;
//					// originbuf = null;
//				}
//			} while (rb != separator);// 读取一行10代表\n
//			byte[] bytearr = Arrays.copyOfRange(buf, 0, size);
//			buf = null;
//			return bytearr.length == 0 ? null : bytearr;
//		}
//	}
//
//	public static class ReaderTool {
//		public static String toString(Reader input) throws IOException {
//			return new String(toCharArray(input));
//		}
//
//		public static char[] toCharArray(Reader input) throws IOException {
//			if (null != input) {
//				XCharArrayWriter Arrayout = new XCharArrayWriter();
//				copy(input, Arrayout);
//				char[] cs = Arrayout.toCharArray();
//				Arrayout.releaseBuffer();
//				return cs;
//			}
//			return null;
//		}
//
//		public static char[] toCharArray(Reader input, int length) throws IOException {
//			if (null != input) {
//				XCharArrayWriter Arrayout = new XCharArrayWriter();
//				copyFixedLength(input, Arrayout, length);
//				char[] cs = Arrayout.toCharArray();
//				Arrayout.releaseBuffer();
//				return cs;
//			}
//			return null;
//		}
//
//		@XAnnotations("this no buffered")
//		public char[] readLine(Reader input, char separator) throws IOException {
//			int bufsize = XStream.DEFAULT_BYTE_BUFF_SIZE;
//			char[] buf = new char[bufsize];
//			int size = 0;
//			int rb;
//			do {
//				if ((rb = input.read()) == -1) {
//					if (size == 0) {
//						return null;
//					}
//					break;
//				}
//				buf[size++] = (char) rb;
//				if (size >= buf.length) {
//					// char[] originbuf = buf;
//					char[] newbuf = new char[size + bufsize];
//					System.arraycopy(buf, 0, newbuf, 0, buf.length);
//					buf = newbuf;
//					// originbuf = null;
//				}
//			} while (rb != separator);// 读取一行10代表\n
//			char[] bytearr = Arrays.copyOfRange(buf, 0, size);
//			buf = null;
//			return bytearr.length == 0 ? null : bytearr;
//		}
//	}




	public static class ObjectTool {
		public static void writeObject(OutputStream out, Object obj) throws IOException {
			new ObjectOutputStream(out).writeObject(obj);
		}

		public static Object readObject(InputStream in) throws IOException, ClassNotFoundException {
			return new ObjectInputStream(in).readObject();
		}




		public static byte[] toBytes(Object obj) throws IOException {
			XByteArrayOutputStream out = new XByteArrayOutputStream();
			writeObject(out, obj);
			byte[] bs = out.toByteArray();
			out.releaseBuffer();
			return bs;
		}
		public static Object toObject(byte[] bytes) throws ClassNotFoundException, IOException {
			XByteArrayInputStream in = new XByteArrayInputStream(bytes);
			return readObject(in);
		}
	}

	public static boolean tryClose(Closeable c) {
		try {
			c.close();
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

}
