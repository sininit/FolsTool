package top.fols.box.io.interfaces;
import java.io.IOException;
import top.fols.box.annotation.XAnnotations;
/*
 * you can release buffer data
 * 
 * Used to give up an instance
 * Used for instance reuse
 */
@XAnnotations("can still be used normally after release")
public interface XInterfereReleaseBufferable {
	public abstract void releaseBuffer() throws IOException;
}
