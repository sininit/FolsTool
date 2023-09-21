package top.fols.atri.interfaces.interfaces;

import java.io.IOException;

@SuppressWarnings("UnnecessaryInterfaceModifier")
public interface IReleasableIO {
	public boolean release() throws IOException;
    public boolean released() throws IOException;
}
