package top.fols.box.io.interfaces;
import java.io.IOException;
import top.fols.box.annotation.XAnnotations;
@XAnnotations("will be abandoned after destroy")
public interface Destroyable
{
	public abstract void destroyData() throws IOException;
}
