package top.fols.box.io.interfaces;
import java.io.IOException;

import top.fols.box.annotation.BaseAnnotations;

/*
 * you can release buffer data
 * 
 * Used to give up an instance
 * Used for instance reuse
 */
@BaseAnnotations("can still be used normally after release")
public interface XInterfaceReleaseBufferable {
	public abstract void releaseBuffer() throws IOException;
}
