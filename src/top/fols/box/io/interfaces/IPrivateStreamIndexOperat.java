package top.fols.box.io.interfaces;
import java.io.IOException;

@SuppressWarnings("UnnecessaryModifier")
@Deprecated
public interface IPrivateStreamIndexOperat {
	public void seekIndex(int index) throws IOException;
	public int getIndex();
}
