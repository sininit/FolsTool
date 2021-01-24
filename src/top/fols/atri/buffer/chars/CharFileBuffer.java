package top.fols.atri.buffer.chars;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import static top.fols.atri.lang.Finals.*;

public class CharFileBuffer extends CharBufferOption {
	public CharFileBuffer(File file) throws FileNotFoundException {
		this(file, null);
	}
	public CharFileBuffer(File file, Charset charset) throws FileNotFoundException {
		super(EMPTY_CHAR_BUFFER, 0, 0);

		this.file = file;
		this.charset = charset;
		this.fis = new FileInputStream(file);
		this.reader = null == charset ? new InputStreamReader(this.fis): new InputStreamReader(this.fis, charset);
	}

	private File file;
	private Charset charset;
	private FileInputStream fis;
	private InputStreamReader reader;

	@Override
	public int stream_read(char[] buf, int off, int len) throws IOException {
		// TODO: Implement this method
		int read = this.reader.read(buf, off, len);
		return read;
	}

	public File getFile() { return this.file; }
	public Charset getCharset() { return null == this.charset ?Charset.defaultCharset(): this.charset; }
	
	
	public FileInputStream getFileInputStream() { return this.fis; }
	public InputStreamReader getReader() { return this.reader; }

	public void close() throws IOException { 
		this.fis.close();
		this.reader.close();
	}
}
