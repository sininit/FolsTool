package top.fols.box.reflect;

import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.reflect.Reflects;
import top.fols.box.reflect.Reflectx;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import static top.fols.atri.lang.Finals.INVOCATION_HANDLER_CLASS;

@SuppressWarnings({"rawtypes", "unused", "SpellCheckingInspection"})
public class Proxys {

    public static Class createProxyClass(ClassLoader classLoader, Class... interfaceList) {
        return Proxy.getProxyClass(
                classLoader,
                interfaceList);
    }



    public static Class createProxyClass(Class interfaceOne) {
        try {
            return Proxy.getProxyClass(
                    interfaceOne.getClassLoader(),
                    interfaceOne);
        } catch (Throwable ignored) {}

        try {
            return Proxy.getProxyClass(
                    Thread.currentThread().getContextClassLoader(),
                    interfaceOne);
        }  catch (RuntimeException e) {
            throw e;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static Class createProxyClass(Class... interfaceList) {
        for (Class aInterface : interfaceList) {
            try {
                return Proxy.getProxyClass(
                        aInterface.getClassLoader(),
                        interfaceList);
            } catch (Throwable ignored) {}
        }

        try {
              return Proxy.getProxyClass(
                    Thread.currentThread().getContextClassLoader(),
                    interfaceList);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

    }






    @NotNull
    public static Object createProxyClassInstance(Class<?> proxyClass, InvocationHandler handler) throws RuntimeException {
        try {
            Constructor<?> constructor = proxyClass.getConstructor(INVOCATION_HANDLER_CLASS);
            if (!(Reflectx.isPublic(constructor))) {
                Reflects.accessible(constructor); //一般来说不可能，创建的代理类一般都是public的
            }
            return constructor.newInstance(handler);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static Object createProxyClassInstance(ClassLoader loader,
                                                  @NotNull Class<?>[] interfaces,
                                                  @NotNull InvocationHandler h)
            throws IllegalArgumentException {
        return Proxy.newProxyInstance(loader, interfaces, h);
    }
}
