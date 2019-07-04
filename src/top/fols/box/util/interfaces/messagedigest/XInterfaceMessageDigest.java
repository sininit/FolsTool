package top.fols.box.util.interfaces.messagedigest;
import java.io.OutputStream;

public abstract class XInterfaceMessageDigest extends OutputStream {
	public abstract void clear();
	public abstract String getHash();

	public abstract void write(int p1);
	public void write(byte[] b) {
		write(b, 0, b.length);
	}
    public void write(byte[] b, int off, int len) {
		for (int i = 0;i < len;i++)
			write(b[off++]);
	}
    public void flush() {
		return;
	}
    public void close() {
		return;
	}
}
