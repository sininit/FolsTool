package top.fols.box.io.digest;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import top.fols.box.io.interfaces.XInterfaceGetInnerStream;

/** 
 * @see XChecksumInputStream
 */
public class XDigestInputStream<T extends InputStream> extends InputStream implements XInterfaceGetInnerStream<T> {
	private T stream;
	private boolean on = true;

	protected MessageDigest d;

	public XDigestInputStream(T stream, MessageDigest d) throws NullPointerException {
		this.setStream(stream);
		this.setMessageDigest(d);
	}
	
	public MessageDigest getMessageDigest() {
		return this.d;
	}
	public void setMessageDigest(MessageDigest d) {
		this.d = d;
	}

	@Override
	public int read() throws IOException {
		int ch = this.stream.read();
		if (this.on && ch != -1) {
			this.d.update((byte)ch);
		}
		return ch;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int result = this.stream.read(b, off, len);
		if (this.on && result != -1) {
			this.d.update(b, off, result);
		}
		return result;
	}

	public void on(boolean on) {
		this.on = on;
	}
	public boolean isOn() {
		return this.on;
	}

	/**
     * Returns the digest value.
     */
    public byte[] getValue() {
        return this.d.digest();
    }

	/**
	 * @see Checksum.reset()
	 */
	public void reset() {
		this.d.reset();
	}


	@Override
	public String toString() {
		return "[Digest Input Stream] " + this.d.toString();
	}


	@Override
	public T getInnerStream() {
		// TODO: Implement this method
		return this.stream;
	}

	/**
	 * @param stream notnull
	 */
	public void setStream(T stream) {
		if (null == stream) {
			throw new NullPointerException("stream");
		}
		this.stream = stream;
	}
}
