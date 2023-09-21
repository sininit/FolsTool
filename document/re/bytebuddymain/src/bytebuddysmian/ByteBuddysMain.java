package bytebuddysmian;

import top.fols.atri.lang.Finals;
import top.fols.atri.reflect.ReflectCache;
import top.fols.atri.reflect.ReflectMatcher;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static bytebuddysmian.AndroidLoaders.isAndroid;
import static bytebuddysmian.ByteBuddysMain.FieldElement.EMPTY_ARRAY;

@SuppressWarnings("SpellCheckingInspection")
public class ByteBuddysMain {
    public static final String CLASS_PATH__BYTEBUDDYS_BYTE_BUDDYS = "bytebuddys.ByteBuddys";
    public static final String METHOD_CREATE_PROXY_CLASS = "createProxyClass";



    public static class FieldElement {
        public static final FieldElement[] EMPTY_ARRAY = {};


        String name;
        int modify;
        Class<?> type;

        public FieldElement(String name, int modify) {
            this(name, Finals.OBJECT_CLASS, modify);
        }
        public FieldElement(String name, Class<?> type, int modify) {
            this.name = name;
            this.type = type;
            this.modify = modify;
        }

        public Class<?> getType() {
            return type;
        }

        public String getName() {
            return name;
        }
        public int getModify() {
            return modify;
        }
    }

    public static abstract class InterfaceInterceptor implements InvocationHandler {
        public InterfaceInterceptor() {}

        @Override
        public abstract Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
    }

    public static abstract class ConstructorInterceptor {
        public abstract Object intercept(
                Object obj, // 目标对象
                Object[] allArguments// 注入目标方法的全部参数
        ) throws Throwable;
    }

    public static abstract class MethodInterceptor {
        public abstract Object intercept(
                Callable<?> superCall,
                Object superObject,
                Object thisObject,
                Method method,
                Object[] allArguments
        ) throws Throwable;
    }


    //第一步
    public static ClassLoader loadLib(File file, ClassLoader parentClassLoader) {
        if (isAndroid()) {
            AndroidLoaders androidLoaders = new AndroidLoaders(file.getAbsolutePath(), parentClassLoader);
            return androidLoaders.getClassLoader();
        }
        JarLoaders jarLoaders = new JarLoaders(new String[]{file.getAbsolutePath()}, parentClassLoader);
        return jarLoaders.getClassLoader();
    }
    public static ClassLoader loadAndroidLib(String file, ClassLoader parentClassLoader) {
        AndroidLoaders androidLoaders = new AndroidLoaders(file, parentClassLoader);
        return androidLoaders.getClassLoader();
    }
    public static ClassLoader loadJarLib(String[] file, ClassLoader parentClassLoader) {
        JarLoaders jarLoaders = new JarLoaders(null==file? Finals.EMPTY_STRING_ARRAY:file, parentClassLoader);
        return jarLoaders.getClassLoader();
    }




    //第二步
    public static Class<?> findByteBuddysClass(ClassLoader loadLibClassLoader) {
        try {
            return loadLibClassLoader.loadClass(CLASS_PATH__BYTEBUDDYS_BYTE_BUDDYS);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ByteBuddyMain     jar-library
     * JarLoaders
     * AndroidLoaders
     *     dynamic-load
     *         ByteBuddy.jar
     *         ByteBuddys.jar
     *         ByteBuddys.createProxyClass(ClassLoader, ByteBuddyMain.ConstructorInterceptor, ...)
     */

    static final ReflectCache
            REFLECT_CACHE = new ReflectCache();
    static final ReflectMatcher<ReflectCache>
            REFLECT_MATCHER = new ReflectMatcher<>(REFLECT_CACHE);

    /**
     * 第三步
     * @param bytebuddysClass           工具类
     */
    public static Class<?> createProxyClass(Class<?> bytebuddysClass,
            ClassLoader classLoader, Class<?> subclass, Class<?>[] interfaces, FieldElement[] fieldElements,
            ByteBuddysMain.ConstructorInterceptor   constructorInterceptor,
            ByteBuddysMain.MethodInterceptor        methodInterceptor,
            InvocationHandler                       interfaceInterceptor
    ) throws Throwable {
        Object[] params = {
                classLoader, subclass, null==interfaces?Finals.EMPTY_CLASS_ARRAY:interfaces, null==fieldElements?EMPTY_ARRAY:fieldElements,
                constructorInterceptor,
                methodInterceptor,
                interfaceInterceptor};
        Method method = REFLECT_MATCHER.method(bytebuddysClass, null, METHOD_CREATE_PROXY_CLASS, params);
        return (Class<?>) method.invoke(null, params);
    }


}
