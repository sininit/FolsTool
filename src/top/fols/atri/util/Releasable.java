package top.fols.atri.util;

import java.io.IOException;

public interface Releasable {
    boolean release() throws IOException;
    boolean released() throws IOException;
}
