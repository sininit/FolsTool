package bytebuddysmian;

import top.fols.atri.io.file.Filex;
import top.fols.atri.io.file.Filez;
import top.fols.box.lang.Classx;

import java.io.File;
import java.lang.reflect.Constructor;

public class AndroidLoaders {
    String dexPath;
    File dexOutputDir;


    ClassLoader parentLoader;
    ClassLoader classLoader;

    public AndroidLoaders(String file, ClassLoader parentLoader)  {
        this.dexPath = file;
        this.dexOutputDir = getDexWorkDirectory();
        if (null == parentLoader)
            throw new RuntimeException("null parent class loader");
        //String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent

        this.parentLoader = parentLoader;

        try {
            Class<?> aClass = Classx.forName("dalvik.system.DexClassLoader");

            Constructor<?> constructor = aClass.getConstructor(String.class, String.class, String.class, ClassLoader.class);
            this.classLoader  = (ClassLoader) constructor.newInstance(this.dexPath, this.dexOutputDir.getAbsolutePath(), null, this.parentLoader);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    };


    public Class<?> loadClass(String clsName) throws ClassNotFoundException {
        return this.classLoader.loadClass(clsName);
    };
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }


    public void free() {
        Filez.wrap(this.dexOutputDir).delete();
    };

    public static File getDexWorkDirectory() {
        File directory = Filez.TEMP.createsDir("tempDex").innerFile();
        boolean mkdirs = directory.mkdirs();
        return directory;
    };
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void clearDexWorkDirectory() {
        File workDirectory = getDexWorkDirectory();
        Filex.deletes(workDirectory);
        workDirectory.mkdirs();
    };


    static {
        clearDexWorkDirectory();
    }

    public static boolean isAndroid() {
        try {
            return null != Classx.forName("android.os.Build");
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
