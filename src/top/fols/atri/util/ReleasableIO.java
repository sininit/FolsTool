package top.fols.atri.util;

import java.io.IOException;

public interface ReleasableIO {
	boolean release() throws IOException;
    boolean released() throws IOException;
}
