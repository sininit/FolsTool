package bytebuddys;

import bytebuddysmian.AndroidLoaders;
import bytebuddysmian.ByteBuddysMain;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;
import top.fols.atri.io.file.Filex;
import top.fols.atri.io.file.Filez;
import top.fols.atri.lang.Finals;
import top.fols.atri.reflect.ReflectCache;
import top.fols.atri.reflect.ReflectMatcher;

import java.io.File;
import java.lang.reflect.*;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.*;

@SuppressWarnings({"SpellCheckingInspection", "Convert2Lambda"})
public class ByteBuddys {

    @SuppressWarnings("UnnecessaryModifier")
    public static interface DynamicTypeLoader {
        public DynamicType.Loaded<?> load(ClassLoader classLoader, DynamicType.Unloaded<?> unloaded) throws Throwable;
    }


    protected static class BeforeConstructorInterceptor {
        ByteBuddysMain.ConstructorInterceptor constructorInterceptor;
        public BeforeConstructorInterceptor(ByteBuddysMain.ConstructorInterceptor constructorInterceptor) {
            this.constructorInterceptor = constructorInterceptor;
        }

        @RuntimeType
        public Object intercept(
                @This Object obj, // 目标对象
                @AllArguments Object[] allArguments// 注入目标方法的全部参数
        ) throws Throwable {
            return constructorInterceptor.intercept(obj, allArguments);
        }
    }
    public static class BeforeMethodInterceptor {
        ByteBuddysMain.MethodInterceptor methodInterceptor;
        public BeforeMethodInterceptor(ByteBuddysMain.MethodInterceptor methodInterceptor) {
            this.methodInterceptor = methodInterceptor;
        }

        @RuntimeType
        public Object intercept(
                @SuperCall Callable<?> superCall,
                @Super Object superObject,
                @This Object thisObject,
                @Origin Method method,
                @AllArguments Object[] allArguments
        ) throws Throwable {
            return methodInterceptor.intercept(superCall, superObject, thisObject, method, allArguments);
        }
    }
    public static class BeforeDefaultMethodInterceptor {
        ByteBuddysMain.MethodInterceptor methodInterceptor;
        public BeforeDefaultMethodInterceptor(ByteBuddysMain.MethodInterceptor methodInterceptor) {
            this.methodInterceptor = methodInterceptor;
        }

        @RuntimeType
        public Object intercept(
                @SuperCall Callable<?> superCall,
                @Super Object superObject,
                @This Object thisObject,
                @Origin Method method,
                @AllArguments Object[] allArguments
        ) throws Throwable {
            return methodInterceptor.intercept(superCall, superObject, thisObject, method, allArguments);
        }
    }

    static final String PACKAGE =  ByteBuddys.class.getPackage().getName();

    public static Class<?> createProxyClass(ClassLoader classLoader, Class<?> subclass, Class<?>[] interfaces,
                                            ByteBuddysMain.FieldElement[] fieldElements,
                                            ByteBuddysMain.ConstructorInterceptor constructorInterceptor,
                                            ByteBuddysMain.MethodInterceptor methodInterceptor,
                                            InvocationHandler interfaceInterceptor
    ) throws Throwable {
        return createProxyClass(getDynamicTypeLoader(),
                classLoader, subclass, interfaces,
                fieldElements,
                constructorInterceptor,
                methodInterceptor,
                interfaceInterceptor);
    }
    public static Class<?> createProxyClass(DynamicTypeLoader dynamicTypeLoader,

                                            ClassLoader classLoader, Class<?> subclass, Class<?>[] interfaces,

                                            ByteBuddysMain.FieldElement[] fieldElements,
                                            ByteBuddysMain.ConstructorInterceptor constructorInterceptor,
                                            ByteBuddysMain.MethodInterceptor methodInterceptor,
                                            InvocationHandler interfaceInterceptor
    ) throws Throwable {
        DynamicType.Builder.MethodDefinition.ImplementationDefinition.Optional<?> implement = new ByteBuddy()
                .with(new NamingStrategy.AbstractBase(){
                    @Override
                    protected String name(TypeDescription typeDescription) {
                        //protected
                        return PACKAGE + "." + typeDescription.getSimpleName() + "_" + System.currentTimeMillis() + "_" + System.identityHashCode(typeDescription);
                    }
                })
                .subclass(subclass)
                .implement(interfaces);

        DynamicType.Builder<?> intercept = implement
                .constructor(any())
                .intercept(SuperMethodCall.INSTANCE.andThen(
                        // 执行完原始构造方法，再开始执行interceptor的代码
                        MethodDelegation.withDefaultConfiguration()
                                .to(new BeforeConstructorInterceptor(constructorInterceptor))
                ));

        for (ByteBuddysMain.FieldElement fieldElement: fieldElements) {
            intercept = intercept.defineField(fieldElement.getName(), fieldElement.getType(), fieldElement.getModify());
        }

        intercept = intercept
                .method(isAbstract())
                .intercept(InvocationHandlerAdapter.of(interfaceInterceptor))

                .method(not(isAbstract()))
                .intercept(MethodDelegation.to(new BeforeMethodInterceptor(methodInterceptor)))

                .method(isDefaultMethod())
                .intercept(InvocationHandlerAdapter.of(interfaceInterceptor));

        DynamicType.Unloaded<?> make = intercept.make();
        DynamicType.Loaded<?> load   = dynamicTypeLoader.load(classLoader, make);
        return load.getLoaded();
    }


    public static DynamicTypeLoader getDynamicTypeLoader() {
        if (AndroidLoaders.isAndroid()) {
            return getAndroidDynamicTypeLoader();
        }
        return getDynamicTypeJarLoader();
    }


    static {
        clearAndroidByteBuddyWorkDirectory();
    }


    public static File getAndroidByteBuddyWorkDirectory() {
        File directory = Filez.TEMP.createsDir("bytebuddy").innerFile();
        boolean mkdirs = directory.mkdirs();
        return directory;
    };
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void clearAndroidByteBuddyWorkDirectory() {
        File   workDirectory = getAndroidByteBuddyWorkDirectory();
        Filex.deletes(workDirectory);
        workDirectory.mkdirs();
    };



    public static DynamicTypeLoader getDynamicTypeJarLoader() {
        return new DynamicTypeLoader(){
            @Override
            public DynamicType.Loaded<?> load(ClassLoader classLoader, DynamicType.Unloaded<?> unloaded) throws Throwable {
                return unloaded.load(classLoader);
            }
        };
    }

    public static DynamicTypeLoader getAndroidDynamicTypeLoader() {
        @SuppressWarnings("rawtypes")
        ClassLoadingStrategy strategy;
        try {
            Class<?> wrapping = Class.forName("net.bytebuddy.android.AndroidClassLoadingStrategy$Wrapping");
            Constructor<?> constructor = wrapping.getConstructor(File.class);
            strategy = (ClassLoadingStrategy<?>) constructor.newInstance(getAndroidByteBuddyWorkDirectory());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return new DynamicTypeLoader() {
            @Override
            public DynamicType.Loaded<?> load(ClassLoader classLoader, DynamicType.Unloaded<?> unloaded) throws Throwable {
                //noinspection unchecked
                return unloaded.load(classLoader, strategy);
            }
        };
    }


}
