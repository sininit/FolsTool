package top.fols.atri.io.digest;

import top.fols.atri.interfaces.interfaces.IInnerStream;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

/**
 * @see ChecksumOutputStream
 */
public class DigestOutputStream<T extends OutputStream> extends OutputStream implements IInnerStream<T> {

	private T stream;
	private boolean on = true;

	protected MessageDigest d;

	public DigestOutputStream(MessageDigest d) {
		this(null, d);
	}

	public DigestOutputStream(T stream, MessageDigest d) {
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
     * Returns the digest tip.
     */
    public byte[] getValue() {
        return this.d.digest();
    }

	/**
	 * @see java.util.zip.Checksum#reset()
	 */
	public void reset() {
		this.d.reset();
	}

	@Override
	public String toString() {
		return "[Digest Output Stream] " + this.d.toString();
	}

	@Override
	public T getInnerStream() {
		// TODO: Implement this method
		return this.stream;
	}

	public void setStream(T stream) {
		this.stream = stream;
	}

}
