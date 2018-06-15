package top.fols.box.io.interfaces;
import java.io.IOException;
import top.fols.box.annotation.XAnnotations;
@XAnnotations("can still be used normally after release")
public interface ReleasableCache
{
	public abstract void releaseCache() throws IOException;
}
