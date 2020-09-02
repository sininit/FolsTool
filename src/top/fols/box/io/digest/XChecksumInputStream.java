package top.fols.box.io.digest;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;
import top.fols.box.io.interfaces.XInterfaceGetInnerStream;

public class XChecksumInputStream<T extends InputStream> extends InputStream implements XInterfaceGetInnerStream<T> {

	/* NOTE: This should be made a generic UpdaterInputStream */

	private T stream;
	/* Are we on or off? */
	private boolean on = true;

	/**
	 * The Checksum associated with this stream.
	 */
	protected Checksum checksum;

	public XChecksumInputStream(T stream, Checksum d) {
		this.setStream(stream);
		this.setChecksum(d);
	}

	public Checksum getChecksum() {
		return this.checksum;
	}
	public void setChecksum(Checksum d) {
		this.checksum = d;
	}

	/**
	 * 
	 * @return the byte read.
	 *
	 * @exception IOException if an I/O error occurs.
	 *
	 * @see Checksum#update(byte)
	 */
	@Override
	public int read() throws IOException {
		int ch = this.stream.read();
		if (this.on && ch != -1) {
			this.checksum.update((byte)ch);
		}
		return ch;
	}

	/**
	 * 
	 * @param b the array into which the data is read.
	 *
	 * @param off the starting offset into <code>b</code> of where the
	 * data should be placed.
	 *
	 * @param len the maximum number of bytes to be read from the input
	 * stream into b, starting at offset <code>off</code>.
	 *
	 * @return  the actual number of bytes read. This is less than
	 * <code>len</code> if the end of the stream is reached prior to
	 * reading <code>len</code> bytes. -1 is returned if no bytes were
	 * read because the end of the stream had already been reached when
	 * the call was made.
	 *
	 * @exception IOException if an I/O error occurs.
	 *
	 * @see Checksum#update(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int result = this.stream.read(b, off, len);
		if (this.on && result != -1) {
			this.checksum.update(b, off, result);
		}
		return result;
	}

	/**
	 * Turns the checksum function on or off. The default is on.  When
	 * it is on, a call to one of the <code>write</code> methods results in an
	 * update on the checksum.  But when it is off, the checksum is not updated.
	 *
	 * @param on true to turn the checksum function on, false to turn it
	 * off.
	 */
	public void on(boolean on) {
		this.on = on;
	}

	public boolean isOn() {
		return this.on;
	}

	/**
     * Returns the checksum value.
     */
    public long getValue() {
        return this.checksum.getValue();
    }

	/**
	 * @see Checksum.reset()
	 */
	public void reset() {
		this.checksum.reset();
	}

	@Override
	public String toString() {
		return "[Checksum Input Stream] " + this.checksum.toString();
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
