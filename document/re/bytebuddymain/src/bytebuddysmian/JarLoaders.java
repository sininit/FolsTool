package bytebuddysmian;

import java.net.URL;
import java.net.URLClassLoader;
public class JarLoaders {
    ClassLoader parentLoader;
    ClassLoader classLoader;



    public JarLoaders(String[] jarFile, ClassLoader parentLoader) {
        URL[] urls = new URL[jarFile.length];
        if (null == parentLoader)
            throw new RuntimeException("null parent class loader");
        this.parentLoader = parentLoader;
        this.classLoader = new URLClassLoader(urls, parentLoader);
    };

    public Class<?> loadClass(String clsName) throws ClassNotFoundException {
        return this.classLoader.loadClass(clsName);
    };

    public ClassLoader getClassLoader() {
        return this.classLoader;
    };

}
