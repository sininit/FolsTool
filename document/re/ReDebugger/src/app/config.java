package app;

import app.utils.Configuration;
import app.utils.MyConfigurationKeys;
import top.fols.atri.io.file.Filez;
import app.utils.FileUtils;

import java.io.File;

public class config {
    public static final String version = "2.0";

    private static final Class<config> CURRENT_CLASS = config.class;
    private static File DIRECTORY;
    public static File directory() {
        if (null == DIRECTORY) {
            DIRECTORY = FileUtils.requireDirectory(new File(Filez.RUN.innerFile(), CURRENT_CLASS.getSimpleName()), "not found resource");
        }
        return DIRECTORY;
    }

           static Configuration.ConfigurationConstructor configurationConstructor = Configuration.newConfigurationConstructor(directory());
    public static Configuration getConfiguration(Class<?> path, MyConfigurationKeys keys) {
       return configurationConstructor.getConfiguration(path, keys);
    }




}
