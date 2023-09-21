package bytebuddysmian;

@SuppressWarnings("rawtypes")
public class ByteBuddysDynamic {
    ClassLoader lib;
    Class dynamicClassLoaderByteBuddysClass;
    ByteBuddysDynamic() {}


    public static ByteBuddysDynamic loadForJar(String[] dexLib) {
        ClassLoader currentClassLoader = bytebuddysmian.ByteBuddysMain.class.getClassLoader();
        ClassLoader dynamicClassLoader = ByteBuddysMain.loadJarLib(dexLib, currentClassLoader);

        return loadForClassLoader(dynamicClassLoader);
    }
    public static ByteBuddysDynamic loadForDex(String dexLib) {
        ClassLoader currentClassLoader = bytebuddysmian.ByteBuddysMain.class.getClassLoader();
        ClassLoader dynamicClassLoader = ByteBuddysMain.loadAndroidLib(dexLib, currentClassLoader);

        return loadForClassLoader(dynamicClassLoader);
    }
    public static ByteBuddysDynamic loadForClassLoader(ClassLoader dynamicClassLoader) {
        Class dynamicClassLoaderByteBuddysClass   = ByteBuddysMain.findByteBuddysClass(dynamicClassLoader);
        ByteBuddysDynamic bbu = new ByteBuddysDynamic();
        bbu.lib = dynamicClassLoader;
        bbu.dynamicClassLoaderByteBuddysClass = dynamicClassLoaderByteBuddysClass;

        return bbu;
    }





    public ClassLoader getLibClassLoader() {
        return this.lib;
    }


    public Class createClass(Class<?> subclass, Class<?>[] interfaces,
                             ByteBuddysMain.FieldElement[] elements,
                             ByteBuddysMain.ConstructorInterceptor constructorInterceptor,
                             ByteBuddysMain.MethodInterceptor methodInterceptor,
                             ByteBuddysMain.InterfaceInterceptor interfaceInterceptor) throws Throwable {
        return createClass(getLibClassLoader(),
                subclass, interfaces, elements,
                constructorInterceptor,
                methodInterceptor,
                interfaceInterceptor);
    }
    public Class createClass(ClassLoader dynamicClassLoader,
                             Class<?> subclass, Class<?>[] interfaces,
                             ByteBuddysMain.FieldElement[] elements,
                             ByteBuddysMain.ConstructorInterceptor constructorInterceptor,
                             ByteBuddysMain.MethodInterceptor methodInterceptor,
                             ByteBuddysMain.InterfaceInterceptor interfaceInterceptor) throws Throwable {
        return ByteBuddysMain.createProxyClass(dynamicClassLoaderByteBuddysClass,
                dynamicClassLoader,
                subclass, interfaces, elements,
                constructorInterceptor,
                methodInterceptor,
                interfaceInterceptor
        );
    }
}
