package app;

import app.utils.Configuration;
import app.utils.FileUtils;
import app.utils.MyConfigurationKeys;
import top.fols.atri.io.file.Filez;

import java.io.File;

public class data {

    public  static Class<data> CURRENT_CLASS = data.class;
    private static File DIRECTORY;
    public static File directory() {
        if (null == DIRECTORY){
            DIRECTORY = FileUtils.getOrCreateDirectory(new File(Filez.RUN.innerFile(), CURRENT_CLASS.getSimpleName()));
        }
        return DIRECTORY;
    }

    static Configuration.ConfigurationConstructor configurationConstructor = Configuration.newConfigurationConstructor(directory());
    public static Configuration getConfiguration(Class<?> path, MyConfigurationKeys keys) {
        return configurationConstructor.getConfiguration(path, keys);
    }



}
