package top.fols.box.util;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import top.fols.atri.io.file.Filex;
import top.fols.atri.io.util.Streams;

import static top.fols.box.util.Zips.*;


public class ZipWriter extends OutputStream implements Closeable {
	private final ZipOutputStream zout;

	public ZipWriter(OutputStream out) {
		this(out, StandardCharsets.UTF_8);
	}
	public ZipWriter(OutputStream out, Charset charset) {
		zout = new ZipOutputStream(out, charset);
	}


	public static long crc32(byte[] file) {
		CRC32 crc32s = new CRC32();
		crc32s.update(file);
		return crc32s.getValue();
	}
	public static long crc32(File file) throws IOException {
		return crc32AndClose(new FileInputStream(file));
	}
	public static long crc32AndClose(InputStream fis) throws IOException {
		CRC32 crc32s = new CRC32();
		try {
			int read;
			byte[] buffer = new byte[8192];
			while ((read = fis.read(buffer)) != -1) {
				crc32s.update(buffer, 0, read);
			}
			return crc32s.getValue();
		} finally {
			Streams.close(fis);
		}
	}



	public void putFolder(String filePath, File localFile) throws IOException {
		long lastModifiedTime = Filex.getLastModified(localFile);
		this.putFolder(filePath,
				lastModifiedTime);
	}
	public void putFolder(String filePath) throws IOException {
		this.putFolder(filePath, System.currentTimeMillis());
	}
	public void putFolder(String filePath,
						  long lastModifiedTime) throws IOException {
		filePath = formatPath(filePath, false);

		this.addEntry(filePath, 0,
				lastModifiedTime,
				0);
		this.closeEntry();
	}



	public void putFile(String filePath, File localFile) throws IOException {
		long size = localFile.length();
		long crc32 = crc32(localFile);
		long lastModifiedTime = Filex.getLastModified(localFile);

		this.addEntry(filePath, size,
				lastModifiedTime,
				crc32);
		this.writeAndClose(new FileInputStream(localFile));
	}
	public void putFile(String filePath, byte[] localFile,
						long lastModifiedTime) throws IOException {
		long size = localFile.length;
		long crc32 = crc32(localFile);

		this.addEntry(filePath, size,
				lastModifiedTime,
				crc32);
		this.writeAndClose(new ByteArrayInputStream(localFile));
	}


	public void putFileEntry(String filePath, long size,
							 long createTime,
							 long crc32) throws IOException {
		filePath = formatPath(filePath, true);

		this.addEntry(filePath, size,
				createTime,
				crc32);
	}



	protected void addEntry(String filePath, long size,
							long lastModifiedTime,
							long crc32) throws IOException {
//		ZipParameters zp = new ZipParameters();
//		zp.setCompressionMethod(CompressionMethod.STORE);
//		zp.setCompressionLevel(CompressionLevel.NO_COMPRESSION);
//		zp.setFileNameInZip(filePath);
//		zp.setLastModifiedFileTime(time);
//		zp.setEntrySize(size);

		ZipEntry zipEntry = createZipEntry(filePath, size, lastModifiedTime, crc32);

		zout.putNextEntry(zipEntry);
	}

	protected ZipEntry createZipEntry(String filePath, long size, long lastModifiedTime, long crc32) {
		ZipEntry zipEntry = new ZipEntry(filePath);
		zipEntry.setMethod(ZipEntry.STORED);
		zipEntry.setTime(lastModifiedTime);
		zipEntry.setSize(size);
		zipEntry.setCrc(crc32);
		return zipEntry;
	}

	public void closeEntry() throws IOException {
		zout.flush();
		zout.closeEntry();
	}


	public void writeAndClose(InputStream fis) throws IOException {
		try {
			Streams.copy(fis, this);
			this.closeEntry();
		} finally {
			Streams.close(fis);
		}
	}


	@Override
	public void close() throws IOException {
		zout.flush();
		zout.close();
	}
	public void write(int p1) throws IOException {
		zout.write(p1);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		zout.write(b, off, len);
	}

	public void flush() throws IOException {
		zout.flush();
	}


}





