package app;

import app.utils.FileUtils;
import top.fols.atri.io.file.Filez;

import java.io.File;

public class res {
    private static Class CURRENT_CLASS = res.class;
    private static File DIRECTORY;
    public static File directory() {
        if (null == DIRECTORY) {
            DIRECTORY = FileUtils.requireDirectory(new File(Filez.RUN.innerFile(), CURRENT_CLASS.getSimpleName()), "not found resource");
        }
        return DIRECTORY;
    }


}
