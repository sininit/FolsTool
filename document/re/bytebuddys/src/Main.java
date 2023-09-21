import bytebuddysmian.ByteBuddysMain;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.Callable;

import static bytebuddys.ByteBuddys.createProxyClass;

public class Main {

    public static interface INew{
        public String get();

        default String defaultString(){
            return "default string";
        }
    }
    public static class New implements INew{
        @Override
        public String get() {
            return null;
        }
    }
    public static void main(String[] args) throws Throwable {
//        Class<?> aClass = createProxyClass(
//                Thread.currentThread().getContextClassLoader(), Object.class, new Class[]{List.class},
//                new ByteBuddysMain.FieldElement[]{90-

        Class<?> aClass = createProxyClass(
                Thread.currentThread().getContextClassLoader(),
                New.class, new Class[]{INew.class},
                new ByteBuddysMain.FieldElement[]{
                        new ByteBuddysMain.FieldElement("a", Modifier.PUBLIC)
                },
                new ByteBuddysMain.ConstructorInterceptor() {
                    public Object intercept(
                            Object obj, // 目标对象
                            Object[] allArguments// 注入目标方法的全部参数
                    ) throws Throwable {
                        return null;
                    }
                },
                new ByteBuddysMain.MethodInterceptor() {
                    @Override
                    public Object intercept(Callable<?> superCall, Object superObject, Object thisObject, Method method, Object[] allArguments) throws Throwable {
                        return superCall.call();
                    }
                },
                new ByteBuddysMain.InterfaceInterceptor() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return null;
                    }
                }
        );
        System.out.println(aClass);
        New o = (New) aClass.newInstance();
        System.out.println(o.defaultString());


        Compiler.start(
                "C:\\Program Files\\Java\\jdk1.8.0_212\\bin"
                , "src"
                , "libs"
                , "bytebuddys.jar"
        );
    }
}
