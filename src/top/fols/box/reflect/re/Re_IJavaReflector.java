package top.fols.box.reflect.re;

import top.fols.atri.reflect.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 主要处理Java的反射
 */
@SuppressWarnings({"rawtypes", "UnusedReturnValue", "UnnecessaryModifier", "UnnecessaryInterfaceModifier", "SpellCheckingInspection"})
public interface Re_IJavaReflector {

    public ReflectCache cacher();


    /**
     * 可能异常
     */
    public Class classes(Class cls, String name) ;
    /**
     * 可能异常
     */
    public Field field(Class cls, Class type, String name) ;
    /**
     * 可能异常
     */
    public Constructor constructor(Class<?> cls, Object... paramInstanceArr) ;
    /**
     * 可能异常
     */
    public Constructor constructor(Class<?> cls, Class... paramClass) ;

    public Method method(Class cls, Class returnClass, String name, Object... paramInstanceArr) ;
    /**
     * 可能异常
     */
    public Method method(Class cls, Class returnClass, String name, Class... paramClass) ;



    public String buildNoSuchClasses(Class cls, String name);
    public String buildNoSuchField(Class cls, Class type, String name);
    public String buildNoSuchConstructor(Class<?> cls, Object... paramInstanceArr);
    public String buildNoSuchConstructor(Class<?> cls, Class... paramClass);
    public String buildNoSuchMethod(Class cls, Class returnClass, String name, Object... paramInstanceArr);
    public String buildNoSuchMethod(Class cls, Class returnClass, String name, Class... paramClass);
}
