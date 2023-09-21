package top.fols.atri.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import top.fols.atri.io.util.Streams;
import top.fols.atri.lang.Objects;

import top.fols.atri.io.digest.ChecksumOutputStream;

public class Checksums {

	public static String toHex(long value) {
        return Long.toHexString(value);
    }
	
	public static ChecksumOutputStream<OutputStream> wrapToStream(Checksum d) {
		return new ChecksumOutputStream<>(d);
	}
	
	public static Adler32 adler32() {
		return new Adler32();
	}
	public static CRC32 crc32() {
		return new CRC32();
	}







	OutputStream cast;
	OutputStream ouput() {
		if (!on) {
			throw new UnsupportedOperationException("closed");
		}
		OutputStream cast = this.cast;
		if (null ==  cast) {
			this.cast = cast = top.fols.atri.util.Checksums.wrapToStream(this.stream);
		}
		return cast;
	}

	public Checksum stream() {
		return this.stream;
	}





	Checksum stream;
	boolean on = true;



	public Checksums(Checksum algorithms) {
		this.stream = Objects.requireNonNull(algorithms, "algorithms");
	}

	public Checksums write(int p1) {
		// TODO: Implement this method
		if (!on) {
			throw new UnsupportedOperationException("closed");
		}
		this.stream.update((byte)p1);
		return this;
	}


	public Checksums write(byte[] b) {
		if (null != b) {
			this.write(b, 0, b.length);
		}
		return this;
	}
	public Checksums write(byte[] b, int off, int len) {
		if (!on) {
			throw new UnsupportedOperationException("closed");
		}
		this.stream.update(b, off, len);
		return this;
	}


	public Checksums write(File stream) throws IOException {
		Streams.copy(new FileInputStream(stream), this.ouput());
		return this;
	}
	public Checksums write(InputStream stream) throws IOException {
		Streams.copy(stream, this.ouput());
		return this;
	}


	public Checksums write(String str) {
		this.write(str.getBytes());
		return this;
	}
	public Checksums write(String str, Charset charset) {
		this.write(str.getBytes(charset));
		return this;
	}








	public Checksums close() {
		this.on = false;
		return this;
	}

	public boolean isClose() {
		return this.on;
	}

	public Checksums reset() {
		this.stream.reset();
		return this;
	}


	public long digest() {
		return this.stream.getValue();
	}
	public String digestHex() {
		return Long.toHexString(this.digest()).toLowerCase();
	}
	public String digestHexUpper() {
		return this.digestHex().toUpperCase();
	}
}
