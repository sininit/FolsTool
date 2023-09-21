package top.fols.box.io.buffer.bytes;

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
	private final File file;
	private final FileInputStream fis;
	
	@Override
	public int stream_read(byte[] buf, int off, int len) throws IOException {
		// TODO: Implement this method
		return this.fis.read(buf, off, len);
	}
	
	public File getFile() { return this.file; }
	public FileInputStream getFileInputStream() { return this.fis; }
	
	public void close() throws IOException { 
		this.fis.close();
	}
	
}
