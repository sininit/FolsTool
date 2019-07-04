package top.fols.box.io;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.base.XByteArrayInputStream;
import top.fols.box.io.base.XByteArrayOutputStream;
import top.fols.box.io.base.XCharArrayWriter;

public class XStream {
	public static final int defaultStreamByteArrBuffSize = 8192;
	public static final int defaultStreamCharArrBuffSize = 8192;

	public static int copy(byte[] input, OutputStream output) throws IOException {
		return copy(input, output, true);
	}
	public static int copy(byte[] in, OutputStream out, boolean autoflush) throws IOException {
		if (null == in) return 0;
		out.write(in);
		if (autoflush) out.flush();
		return in.length;
	}
	public static long copy(InputStream input, OutputStream output) throws IOException {
		return copy(input, output, defaultStreamByteArrBuffSize);
	}
	public static long copy(InputStream input, OutputStream output, int bufflen) throws IOException {
		return copy(input, output, bufflen, true);
	}
	public static long copy(InputStream in, OutputStream out, int bufflen, boolean autoflush) throws IOException {
		return copyFixedLength(in, out, bufflen, -1, autoflush);
	}
	public static long copyFixedLength(InputStream input, OutputStream output, long copyLength) throws IOException {
		return copyFixedLength(input, output, defaultStreamByteArrBuffSize, copyLength);
	}
	public static long copyFixedLength(InputStream input, OutputStream output, int bufflen, long copyLength) throws IOException {
		return copyFixedLength(input, output, bufflen, copyLength, true);
	}
	public static long copyFixedLength(InputStream in, OutputStream out, int bufflen, @XAnnotations("less than 0, there is no limit") long copyLength, boolean autoflush) throws IOException {
		if (null == in) return 0;
		if (copyLength == 0) return 0;
		if (bufflen <= 0) throw new IOException("buffer lenth cannot <= 0");
		byte[] buff = new byte[bufflen];
		int read = -1;
		int tmpbufflen;
		long already = 0;
		boolean nolimit = copyLength < 0;
		while (true) {
			if (nolimit) {
				tmpbufflen = bufflen;
			} else {
				tmpbufflen = copyLength - already < bufflen
					? (int)(copyLength - already)
					: bufflen;
				if (tmpbufflen == 0) break;
			}
			if ((read = in.read(buff, 0, tmpbufflen)) == -1) break;
			already += read;

			if (null == out) continue;
			out.write(buff, 0, read);
			if (autoflush) out.flush();
		}
		buff = null;
		return already;
	}




	public static int copy(char[] input, Writer output) throws IOException {
		return copy(input, output, true);
	}
	public static int copy(char[] in, Writer out, boolean autoflush) throws IOException {
		if (null == in) return 0;
		out.write(in);
		if (autoflush) out.flush();
		return in.length;
	}
	public static long copy(Reader reader, Writer writer) throws IOException {
		return copy(reader, writer, defaultStreamCharArrBuffSize);
	}
	public static long copy(Reader reader, Writer writer, int bufflen) throws IOException {
		return copy(reader, writer, bufflen, true);
	}
	public static long copy(Reader in, Writer out, int bufflen, boolean autoflush) throws IOException {
		return copyFixedLength(in, out, bufflen, -1, autoflush);
	}
	public static long copyFixedLength(Reader input, Writer output, long copyLength) throws IOException {
		return copyFixedLength(input, output, defaultStreamCharArrBuffSize, copyLength);
	}
	public static long copyFixedLength(Reader input, Writer output, int bufflen, long copyLength) throws IOException {
		return copyFixedLength(input, output, bufflen, copyLength, true);
	}
	public static long copyFixedLength(Reader in, Writer out, int bufflen, @XAnnotations("less than 0, there is no limit") long copyLength, boolean autoflush) throws IOException {
		if (null == in) return 0;
		if (copyLength == 0) return 0;
		if (bufflen <= 0) throw new IOException("buffer lenth cannot <= 0");
		char[] buff = new char[bufflen];
		int read = -1;
		int tmpbufflen;
		long already = 0;
		boolean nolimit = copyLength < 0;
		while (true) {
			if (nolimit) {
				tmpbufflen = bufflen;
			} else {
				tmpbufflen = copyLength - already < bufflen
					? (int)(copyLength - already)
					: bufflen;
				if (tmpbufflen == 0) break;
			}
			if ((read = in.read(buff, 0, tmpbufflen)) == -1) break;
			already += read;

			if (null == out) continue;
			out.write(buff, 0, read);
			if (autoflush) out.flush();
		}
		buff = null;
		return already;
	}







	public static class inputstream {
		public static String toString(InputStream input, String encoding) throws IOException {
			return new String(toByteArray(input), encoding);
		}
		public static String toString(InputStream input) throws IOException {
			return new String(toByteArray(input));
		}

		public static byte[] toByteArray(InputStream input) throws IOException {
			if (null != input) {
				XByteArrayOutputStream byteArrayout =  new XByteArrayOutputStream();
				copy(input, byteArrayout);
				byte[] bs = byteArrayout.toByteArray();
				byteArrayout.releaseBuffer();
				return bs;
			}
			return null;
		}
		public static byte[] toByteArray(InputStream input, int length) throws IOException {
			if (null != input) {
				XByteArrayOutputStream byteArrayout =  new XByteArrayOutputStream();
				copyFixedLength(input, byteArrayout, length);
				byte[] bs = byteArrayout.toByteArray();
				byteArrayout.releaseBuffer();
				return bs;
			}
			return null;
		}
	}

	public static class reader {
		public static String toString(Reader input) throws IOException {
			return new String(toCharArray(input));
		}
		public static char[] toCharArray(Reader input) throws IOException {
			if (null != input) {
				XCharArrayWriter Arrayout =  new XCharArrayWriter();
				copy(input, Arrayout);
				char[] cs = Arrayout.toCharArray();
				Arrayout.releaseBuffer();
				return cs;
			}
			return null;
		}
		public static char[] toCharArray(Reader input, int length) throws IOException {
			if (null != input) {
				XCharArrayWriter Arrayout =  new XCharArrayWriter();
				copyFixedLength(input, Arrayout, length);
				char[] cs = Arrayout.toCharArray();
				Arrayout.releaseBuffer();
				return cs;
			}
			return null;
		}
	}

	public static class object {
		public static void writeObject(OutputStream out, Object obj) throws IOException {
			new ObjectOutputStream(out).writeObject(obj);
		}
		public static Object readObject(InputStream in) throws IOException, ClassNotFoundException {
			return new ObjectInputStream(in).readObject();
		}

		public static byte[] toByteArray(Object obj) throws IOException {
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









}
