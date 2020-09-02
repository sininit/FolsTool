package top.fols.box.io.digest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Checksum;
import top.fols.box.io.interfaces.XInterfaceGetInnerStream;

public class XChecksumOutputStream<T extends OutputStream> extends OutputStream implements XInterfaceGetInnerStream<T> {
	
	private T stream;
	private boolean on = true;

	/**
	 * The checksum associated with this stream.
	 */
	protected Checksum d;
	
	public XChecksumOutputStream(Checksum d) {
		this(null, d);
	}
	
	public XChecksumOutputStream(T stream, Checksum d) {
		this.setStream(stream);
		this.setChecksum(d);
	}

	public Checksum getChecksum() {
		return this.d;
	}
	public void setChecksum(Checksum d) {
		this.d = d;
	}

	/**
	 * Updates the checksum (if the checksum function is on) using
	 * the specified byte, and in any case writes the byte
	 * to the output stream. That is, if the checksum function is on
	 * (see {@link #on(boolean) on}), this method calls
	 * <code>update</code> on the checksum associated with this
	 * stream, passing it the byte <code>b</code>. This method then
	 * writes the byte to the output stream, blocking until the byte
	 * is actually written.
	 *
	 * @param b the byte to be used for updating and writing to the
	 * output stream.
	 *
	 * @exception IOException if an I/O error occurs.
	 *
	 * @see Checksum#update(byte)
	 */

	@Override
	public void write(int b) throws IOException {
		if (null != this.stream) {
			this.stream.write(b);
		}
		if (this.on) {
			this.d.update((byte)b);
		}
	}

	/**
	 * Updates the checksum (if the digest function is on) using
	 * the specified subarray, and in any case writes the subarray to
	 * the output stream. That is, if the digest function is on (see
	 * {@link #on(boolean) on}), this method calls <code>update</code>
	 * on the checksum associated with this stream, passing it
	 * the subarray specifications. This method then writes the subarray
	 * bytes to the output stream, blocking until the bytes are actually
	 * written.
	 *
	 * @param b the array containing the subarray to be used for updating
	 * and writing to the output stream.
	 *
	 * @param off the offset into <code>b</code> of the first byte to
	 * be updated and written.
	 *
	 * @param len the number of bytes of data to be updated and written
	 * from <code>b</code>, starting at offset <code>off</code>.
	 *
	 * @exception IOException if an I/O error occurs.
	 *
	 * @see Checksum#update(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if (null != this.stream) {
			this.stream.write(b, off, len);
		}
		if (this.on) {
			this.d.update(b, off, len);
		}
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
        return this.d.getValue();
    }
	
	/**
	 * @see Checksum.reset()
	 */
	public void reset() {
		this.d.reset();
	}

	@Override
	public String toString() {
		return "[Checksum Output Stream] " + this.d.toString();
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
