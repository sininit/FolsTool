package top.fols.atri.io.buffer.bytes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static top.fols.atri.lang.Finals.*;
public class ByteFileBuffer extends ByteBufferOperate {
	public ByteFileBuffer(File file) throws FileNotFoundException {
		super(EMPTY_BYTE_ARRAY, 0, 0);
		
		this.file = file;
		this.fis = new FileInputStream(file);
	}
	private File file;
	private FileInputStream fis;
	
	@Override
	public int stream_read(byte[] buf, int off, int len) throws IOException {
		// TODO: Implement this method
		int read = this.fis.read(buf, off, len);
		return read;
	}
	
	public File getFile() { return this.file; }
	public FileInputStream getFileInputStream() { return this.fis; }
	
	public void close() throws IOException { 
		this.fis.close();
	}
	
}
