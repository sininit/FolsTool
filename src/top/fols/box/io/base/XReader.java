package top.fols.box.io.base;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XObjects;
import top.fols.box.io.interfaces.XInterfaceGetOriginStream;

public class XReader <T extends Reader> extends Reader implements XInterfaceGetOriginStream<T> {
	public static  <T extends Reader> XReader wrap(T in) {
		return new XReader(in);
	}
	
	
	
	
	@Override
	public int read() throws java.io.IOException {
		irc = false;
		int read = stream.read();
		if (read == -1)
			irc = true;
		return read;
	}
    @Override
	public int read(char[] b) throws java.io.IOException {
		return read(b, 0, b.length);
	}
	@Override
	public int read(char[] b, int off, int len) throws java.io.IOException {
		irc = false;
		int read = stream.read(b, off, len);
		if (read == -1)
			irc = true;
		return read;
	}
	@Override
	public long skip(long n) throws java.io.IOException {
		return stream.skip(n);
	}
    @Override
	public void close() throws java.io.IOException {
		stream.close();
	}
    @Override
	public synchronized void mark(int readlimit) throws IOException {
		stream.mark(readlimit);
	}
    @Override
	public synchronized void reset() throws java.io.IOException {
		stream.reset();
	}
    @Override
	public boolean markSupported() {
		return stream.markSupported();
	}
	@Override
	public boolean ready() throws java.io.IOException {
		return stream.ready();
	}






	public char[] readLine() throws IOException {
		return readLine(char_NextLineN);
	}
	private boolean irc = false;
	private boolean irs = false;
	@XAnnotations("this no buffered")
	public char[] readLine(char separator) throws IOException {
		int bufsize = 64 * 1024;
		char[] buf = new char[bufsize];
		int size = 0;
		int rb;

		irc = false;
		irs = false;
		do {
			if ((rb = stream.read()) == -1) {
				irc = true;
				if (size == 0)
					return null;
				break;
			}
			buf[size++] = (char) rb;
			if (size >= buf.length) {
				char[] originbuf = buf;
				char[] newbuf = new char[size + bufsize];
				System.arraycopy(buf, 0, newbuf, 0, buf.length);
				buf = newbuf;
				originbuf = null;
			}
		}
		while (rb != separator);// 读取一行10代表\n
		char[] bytearr = Arrays.copyOfRange(buf, 0, size);
		if (size != 0)
			irs = bytearr[bytearr.length - 1] == separator;
		buf = null;
		return bytearr.length == 0 ? null: bytearr;
	}
	@XAnnotations("last read stream result equals -1")
	public synchronized boolean isReadComplete() {
		return irc;
	}
	public synchronized boolean readLineIsReadToSeparator() {
		return irs;
	}
	@Override
	public T getStream() {
		return stream;
	}



	public static char char_NextLineN = XStaticFixedValue.Char_NextLineN;
	private final T stream;
	
	public XReader(T inputstream) {
		this.stream = XObjects.requireNonNull(inputstream);
	}


}
