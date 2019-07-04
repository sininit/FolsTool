package top.fols.box.io.os.custom;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import top.fols.box.io.XStream;
import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.io.os.XRandomAccessFileInputStream;
import top.fols.box.io.os.XRandomAccessFileOutputStream;
import top.fols.box.lang.XBits;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XObjects;


/*
 * will char To byte
 * java Unicode Encoding
 * byte length = char count * 2   (automatically fill byte 2 bits) @XBits.putChar/XBits.getChar
 * new String(bytes, "Unicode");
 */
public class XRandomAccessFileCustomCharStreams {
	public static class Read extends java.io.Reader{
		/*
		 * byte encoding
		 */
		public static Charset getEncoding() {
			return Charset.forName("Unicode");
		}

		public File getFile() {
			return this.file;
		}

		private File file;
		private RandomAccessFile stream;
		private long charPos, charLength;
		private long mark = 0;
		public Read(String file) throws FileNotFoundException {
			this(new File(file));
		}
		public Read(File file) throws FileNotFoundException {
			this.file = XObjects.requireNonNull(file);
			this.stream = new RandomAccessFile(file, XStaticFixedValue.FileOptMode.r());
			this.reLoad();
		}
		public Read reLoad() {
			long byteLength = file.length();
			this.charPos = 0;
			this.charLength = byteLength / XBits.char_byte_length;
			return this;
		}





		@Override
		public int read() throws java.io.IOException {
			return this.r0();
		}
		@Override
		public int read(char[] b) throws java.io.IOException {
			return this.read(b, 0 , b.length);
		}
		@Override
		public int read(char[] b, int off, int len) throws java.io.IOException {
			return this.r0(b, off, len);
		}
		public String read(int len) throws IOException {
			char[] cs = new char[len];
			if (this.read(cs, 0, cs.length) == -1) {
				return null;
			} else {
				return new String(cs);
			}
		}



		private byte[] ChBytes = new byte[XBits.char_byte_length]; 
		private int r0() throws IOException {
			int r = this.stream.read(ChBytes);
			if (r == -1) {
				return -1;
			} else if (r != XBits.char_byte_length) {
				throw new IOException("needByte=" + XBits.char_byte_length + ", readTo=" + r);
			}
			return (int)XBits.getChar(ChBytes, 0);
		}
		private int r0(char[] cs, int off, int len)throws IOException {
			byte[] ChsBytes = new byte[XBits.char_byte_length * len];
			int r = this.stream.read(ChsBytes);
			if (r == -1) {
				return -1;
			} else if (r % XBits.char_byte_length != 0) {
				throw new IOException("readBytes error readTo=" + r);
			}
			int rcl = r / XBits.char_byte_length;
			for (int i = 0;i < rcl;i++) {
				cs[i + off] = XBits.getChar(ChsBytes, i * XBits.char_byte_length);
			}
			ChsBytes = null;
			return rcl;
		}




		@Override
		public long skip(long n) throws java.io.IOException {
			return this.skipChars(n);
		}
		@Override
		public boolean ready() {
			return true;
		}
		@Override
		public boolean markSupported() {
			return true;
		}
		@Override
		public void mark(int readAheadLimit) throws java.io.IOException {
			this.mark(readAheadLimit);
		}
		public void mark(long readAheadLimit) {
			this.mark = this.charPos;
		}
		@Override
		public void reset() throws java.io.IOException {
			this.charPos = this.mark;
			this.seek(this.charPos);
		}
		@Override
		public void close() throws java.io.IOException {
			this.stream.close();
		}


		public void seek(long pos) throws java.io.IOException {
			if (pos > this.charLength) {
				throw new IOException("writeIndex=" + pos + ", length=" + this.charLength);
			}
			long abs = XBits.char_byte_length * pos;
			this.charPos = pos;
			this.stream.seek(abs);
		}
		public long length() {
			return this.charLength;
		}
		public void setLength(long newLength) throws java.io.IOException {
			long fileLength = newLength * XBits.char_byte_length;
			this.stream.setLength(fileLength);
			this.charLength = newLength;
			if (this.charPos > this.charLength) {
				this.charPos = this.charLength;
			}
		}
		public long skipChars(long n) throws IOException {
			if (this.charPos + n > this.charLength) {
				long n1 = this.charLength - this.charPos;
				this.charPos = this.charLength;
				this.seek(this.charPos);
				return n1;
			} else {
				this.charPos = this.charPos + n;
				this.seek(this.charPos);
				return n;
			}
		}
		public long getIndex() {
			return this.charPos;
		}


		public static long length(File file) {
			return file.length() / XBits.char_byte_length;
		}
		public static char[] readFileChars(File file, long off, int len) throws FileNotFoundException, IOException {
			XRandomAccessFileCustomCharStreams.Read reader = new XRandomAccessFileCustomCharStreams.Read(file);
			reader.seek(off);
			char[] cs = new char[len];
			reader.read(cs);
			return cs;
		}
		public static char[] readFileChars(File file) throws FileNotFoundException, IOException {
			long length = file.length();
			if (length > Integer.MAX_VALUE) {
				throw new OutOfMemoryError("fileSize=" + length);
			}
			return readFileChars(file, 0, ((int)length) / XBits.char_byte_length);
		}
	}
	
	
	
	
	
	
	
	
	
	/*
	 * will char To byte
	 * java Unicode Encoding
	 * byte length = char count * 2   (automatically fill byte 2 bits)
	 */
	public static class Write extends Writer {
		/*
		 * bytes encoding
		 */
		public static Charset getEncoding() {
			return Charset.forName("Unicode");
		}

		public File getFile() {
			return this.file;
		}




		private File file;
		private RandomAccessFile stream;
		private long charPos, charLength;
		public Write(String file) throws FileNotFoundException, IOException {
			this(file, false);
		}
		public Write(File file) throws FileNotFoundException, IOException {
			this(file, false);
		}
		public Write(String file, boolean append) throws FileNotFoundException, IOException {
			this(new File(file), append);
		}
		public Write(File file, boolean append) throws FileNotFoundException, IOException {
			this.file = XObjects.requireNonNull(file);
			this.open0();
			if (append) {
				this.reLoad();
				this.seek(this.charLength);
			} else {
				this.reCreateFile();
				this.reLoad();
				this.seek(0);
			}
		}
		public Write reLoad() {
			long byteLength = file.length();
			this.charPos = 0;
			this.charLength = byteLength / XBits.char_byte_length;
			return this;
		}
		public Write reCreateFile() throws IOException {
			this.file.delete();
			this.file.createNewFile();
			this.charPos = 0;
			this.charLength = 0;
			this.open0();
			return this;
		}
		private void open0() throws FileNotFoundException {
			this.stream = new RandomAccessFile(file, XStaticFixedValue.FileOptMode.rw());
		}



		@Override
		public void write(int b) throws java.io.IOException {
			this.w0(b);
		}
		@Override
		public void write(char[] b) throws java.io.IOException {
			this.w0(b, 0, b.length);
		}
		@Override
		public void write(char[] b, int off, int len) throws java.io.IOException {
			this.w0(b, off, len);
		}


		/*
		 * 将Bytes转换编码为Unicode(char)并且写入
		 */
		public void write(byte[] b, int off, int len, String encoding) throws java.io.IOException {
			this.write(bytesEncodingConversion(b, off, len, Charset.forName(encoding)));
		}
		public void write(byte[] b, int off, int len, Charset encoding) throws java.io.IOException {
			this.write(bytesEncodingConversion(b, off, len, encoding));
		}


		@Override
		public void flush() {
			// TODO: Implement this method
			return;
		}


		private byte[] ChBytes = new byte[XBits.char_byte_length];
		private void w0(int c) throws IOException {
			char ch = (char)c;
			XBits.putChar(ChBytes, 0, ch);
			this.stream.write(ChBytes);
			this.charLength += 1;
		}
		private void w0(char[] b, int off, int len) throws IOException {
			byte[] ChsBytes = new byte[XBits.char_byte_length * len];
			for (int i = 0;i < len;i++) {
				XBits.putChar(ChsBytes, i * XBits.char_byte_length, b[i + off]);
			}
			this.stream.write(ChsBytes);
			ChsBytes = null;
			this.charLength += len;
		}



		public void seek(long pos) throws java.io.IOException {
			if (pos > this.charLength) {
				throw new IOException("writeIndex=" + pos + ", length=" + this.charLength);
			}
			long abs = XBits.char_byte_length * pos;
			this.charPos = pos;
			this.stream.seek(abs);
		}
		public long length() {
			return this.charLength;
		}
		public void setLength(long newLength) throws java.io.IOException {
			long fileLength = newLength * XBits.char_byte_length;
			this.stream.setLength(fileLength);
			this.charLength = newLength;
			if (this.charPos > this.charLength) {
				this.charPos = this.charLength;
			}
		}
		public long available() {
			return this.charLength - this.charPos;
		}
		@Override
		public void close() throws java.io.IOException {
			this.stream.close();
		}
		public long skipChars(long n) throws IOException {
			if (this.charPos + n > this.charLength) {
				long n1 = this.charLength - this.charPos;
				this.charPos = this.charLength;
				this.seek(this.charPos);
				return n1;
			} else {
				this.charPos = this.charPos + n;
				this.seek(this.charPos);
				return n;
			}
		}
		public long getIndex() {
			return this.charPos;
		}


		public static long length(File file) {
			return file.length() / XBits.char_byte_length;
		}
		public static void appendFileChars(File file, long fileCharOff, char[] cs, int off, int len) throws IOException {
			Write writer = new Write(file, true);
			writer.seek(fileCharOff);
			writer.write(cs, off, len);
			writer.flush();
			writer.close();
		}
		public static void writeFileChars(File file, char[] cs, int off, int len) throws IOException {
			Write writer = new Write(file, false);
			writer.write(cs, off, len);
			writer.flush();
			writer.close();
		}






		/*
		 * file encoding conversion
		 */
		public static void fileBytesEncodingConversion(File file, Charset originEncoding,
													   File writeFile) throws FileNotFoundException, UnsupportedEncodingException, IOException {
			long charCount = fileBytesEncodingConversion(file, 0, file.length(), originEncoding, writeFile, 0);

			RandomAccessFile rafw;
			rafw = new RandomAccessFile(writeFile, XStaticFixedValue.FileOptMode.rw());
			rafw.setLength(charCount * XBits.char_byte_length);
			rafw.close();
		}
		public static long fileBytesEncodingConversion(File file, long fileByteOff, long fileByteLen, Charset originEncoding,
													   File writeFile, long writeFileByteOff) throws FileNotFoundException, UnsupportedEncodingException, IOException {
			XRandomAccessFileInputStream fr0 = new XRandomAccessFileInputStream(file);
			fr0.seekIndex(fileByteOff);
			Reader rafr = new InputStreamReader(new XInputStreamFixedLength<XRandomAccessFileInputStream>(fr0, fileByteLen), originEncoding);

			XRandomAccessFileOutputStream rafw = new XRandomAccessFileOutputStream(writeFile);
			rafw.seekIndex(writeFileByteOff);
			char[] buf = new char[XStream.defaultStreamCharArrBuffSize];
			byte[] bufBytes = new byte[buf.length * XBits.char_byte_length];
			int read;
			long wlCharCount = 0;
			while ((read = rafr.read(buf)) != -1) {
				for (int i = 0;i < read;i++) {
					XBits.putChar(bufBytes, i * XBits.char_byte_length, buf[i]);
				}
				wlCharCount += read;
				rafw.write(bufBytes, 0, read * XBits.char_byte_length);
			}
			rafr.close();
			rafw.close();
			buf = null; bufBytes = null;
			return wlCharCount;
		}

		public static char[] fileBytesEncodingConversion(File file, long fileByteOff, int fileByteLen, Charset originEncoding) throws FileNotFoundException, UnsupportedEncodingException, IOException {
			XRandomAccessFileInputStream fr0 = new XRandomAccessFileInputStream(file);
			fr0.seekIndex(fileByteOff);
			Reader rafr = new InputStreamReader(new XInputStreamFixedLength<XRandomAccessFileInputStream>(fr0, fileByteLen), originEncoding);

			char[] buf = new char[fileByteLen / XBits.char_byte_length];
			byte[] bufBytes = new byte[fileByteLen];
			int read = rafr.read(buf);
			if (read == -1) {
				return null;
			}
			int count = read / XBits.char_byte_length;
			for (int i = 0;i < count;i++) {
				buf[i] = XBits.getChar(bufBytes, i * XBits.char_byte_length);
			}
			rafr.close();
			return buf;
		}



		public static char[] bytesEncodingConversion(byte[] bs, int bsOff, int bsLen, Charset originEncoding) {
			char[] cs = originEncoding.decode(ByteBuffer.wrap(bs, bsOff, bsLen)).array();
			return cs;
		}
	}
	
}
