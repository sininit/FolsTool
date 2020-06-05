package top.fols.box.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import top.fols.box.io.XStream;
import top.fols.box.io.digest.XChecksumOutputStream;

public class XChecksum {
	
	public static long getValue(Checksum d, InputStream input) throws IOException {
		XChecksumOutputStream out = XChecksum.wrapToStream(d);
		XStream.copy(input, out);
		return out.getValue();
	}
	
	public static long getValue(Checksum d, byte[] input) throws IOException {
		XChecksumOutputStream out = XChecksum.wrapToStream(d);
		XStream.copy(input, out);
		return out.getValue();
	}
	
	public static String toHex(long value) {
        return Long.toHexString(value);
    }
	
	public static XChecksumOutputStream<OutputStream> wrapToStream(Checksum d){
		return new XChecksumOutputStream<OutputStream>(d);
	}
	
	public static Adler32 adler32Instance() {
		return new Adler32();
	}
	public static CRC32 crc32Instance() {
		return new CRC32();
	}
}
