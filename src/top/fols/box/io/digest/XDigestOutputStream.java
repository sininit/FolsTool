package top.fols.box.io.digest;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import top.fols.box.io.interfaces.XInterfaceGetOriginStream;
import top.fols.box.util.encode.XHexEncoder;

/**
 * @see XChecksumOutputStream
 */
public class XDigestOutputStream<T extends OutputStream> extends OutputStream implements XInterfaceGetOriginStream<T> {

	private T stream;
	private boolean on = true;

	protected MessageDigest d;

	public XDigestOutputStream(MessageDigest d) {
		this(null, d);
	}

	public XDigestOutputStream(T stream, MessageDigest d) {
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
	public void write(int b) throws IOException {
		if (null != this.stream) {
			this.stream.write(b);
		}
		if (this.on) {
			this.d.update((byte)b);
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if (null != this.stream) {
			this.stream.write(b, off, len);
		}
		if (this.on) {
			this.d.update(b, off, len);
		}
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
		return "[Digest Output Stream] " + this.d.toString();
	}

	@Override
	public T getStream() {
		// TODO: Implement this method
		return this.stream;
	}

	public void setStream(T stream) {
		this.stream = stream;
	}

}
