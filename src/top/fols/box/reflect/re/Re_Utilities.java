package top.fols.box.reflect.re;

import top.fols.atri.interfaces.annotations.Private;
import top.fols.atri.lang.*;
import top.fols.atri.lang.Objects;
import top.fols.atri.reflect.ReflectCache;
import top.fols.atri.reflect.ReflectMatcher;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.atri.util.Iterables;
import top.fols.box.lang.Arrayx;
import top.fols.box.lang.Classx;
import top.fols.atri.util.Lists;

import java.lang.reflect.*;
import java.util.*;

import static top.fols.atri.lang.Objects.*;
import static top.fols.box.reflect.re.Re_Accidents.Errors.error_out_of_memory;
import static top.fols.box.reflect.re.Re_CodeLoader.PACKAGE_SEPARATOR_STRING;

@SuppressWarnings({"SuspiciousSystemArraycopy", "SpellCheckingInspection", "rawtypes", "RedundantCast"})
public class Re_Utilities {
    Re_Utilities() {}



    @SuppressWarnings("InstantiationOfUtilityClass")
    public static class Break {
        public static final Break DEFAULT = new Break();
        Break() {}

        public static boolean isBreak(Object result) {
            return result instanceof Break;
        }
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static class Continue {
        public static final Continue DEFAULT = new Continue();
        Continue() {}

        public static boolean isContinue(Object result) {
            return result instanceof Continue;
        }
    }


    public static Object setReturnContinue(Re_Executor executor) {
        Object v = Re_Utilities.Continue.DEFAULT;
        executor.setReturn(v);
        return v;
    }
    public static Object setReturnBreak(Re_Executor executor) {
        Object v = Re_Utilities.Break.DEFAULT;
        executor.setReturn(v);
        return v;
    }




    public static boolean isSpace(Object o) {
        return o instanceof Re_Executor;
    }

    public static boolean ifTrue(Object v) {
        if (null == v) return false;
        if (v instanceof Boolean) return (Boolean) v;
        return true;
    }
    public static boolean ifFalse(Object v) {
        if (null == v) return true;
        if (v instanceof Boolean) return !((Boolean) v);
        return false;
    }
    public static boolean ifAnd(Object v, Object v2) {
        return ifTrue(v) && ifTrue(v2) ;
    }
    public static boolean ifOr(Object v, Object v2) {
        return ifTrue(v) || ifTrue(v2);
    }




    public static String objectAsString(Object v) {
        if (null == v) return "null";
        return v.toString();
    }
    public static String objectAsName(Object object) {
        //reobject
        if (object instanceof Re_IRe_Object) {
            return ((Re_IRe_Object) object).getName();
        } else {
            //java null
            if (null == object)
                return String.valueOf((Object) null);

            //java object
            if (object instanceof Class)
                return Classx.getClassGetNameToCanonicalName((Class) object);
            return Objects.identityToString(object);
        }
    }



    public static String toJString(Object obj) {
        if (obj instanceof Re_IRe_Object) {
            //ReClass or ReClassInsatance.toString() ?
            return obj.toString();
        } else {
            return get_String(obj);
        }
    }
    public static char    toJChar(Object obj)    { return get_char(obj);    }
    public static boolean toJBoolean(Object obj) { return get_boolean(obj); }
    public static byte    toJByte(Object obj)    { return get_byte(obj);    }
    public static int     toJInt(Object obj)     { return get_int(obj);     }
    public static long    toJLong(Object obj)    { return get_long(obj);    }
    public static short   toJShort(Object obj)   { return get_short(obj);   }
    public static double  toJDouble(Object obj)  { return get_double(obj);  }
    public static float   toJFloat(Object obj)   { return get_float(obj);   }




    public static boolean isJNull(Object obj)   {
        return null == obj;
    }
    public static boolean isJString(Object obj) {
        return obj instanceof String;
    }
    public static boolean isJChar(Object obj) {
        return obj instanceof Character;
    }
    public static boolean isJBoolean(Object obj) {
        return obj instanceof Boolean;
    }
    public static boolean isJByte(Object obj) {
        return obj instanceof Byte;
    }
    public static boolean isJInt(Object obj) {
        return obj instanceof Integer;
    }
    public static boolean isJLong(Object obj) {
        return obj instanceof Long;
    }
    public static boolean isJShort(Object obj) {
        return obj instanceof Short;
    }
    public static boolean isJDouble(Object obj) {
        return obj instanceof Double;
    }
    public static boolean isJFloat(Object obj) {
        return obj instanceof Float;
    }















    /**
     * return class point object
     * no set var
     *
     * @param cls class
     */
    public static Re_PrimitiveObject_jimport toReJImport(Class<?> cls) {
        return new Re_PrimitiveObject_jimport(cls);
    }

    public static Re_PrimitiveObject_jobject toReJObject(@NotNull Object object) {
        return object.getClass().isArray()
                ? new Re_PrimitiveObject_jobjectArray(object)
                : new Re_PrimitiveObject_jobject(object);
    }




    public static String getObjectJavaClassName(Object value){
        return null == value?null:value.getClass().getName();
    }












    public static boolean isIReObject(Object o) {
        return o instanceof Re_IRe_Object;
    }
    public static boolean isJavaObject(Object o) {
        return !(o instanceof Re_IRe_Object);
    }






    static final Map<Class, Boolean> JBASE_DATA_TYPE = new IdentityHashMap<Class, Boolean>() {{
        put(null, true);

        for (Class<?> aClass : Boxing.listPrimitiveType())
            put(aClass, true);
        for (Class<?> aClass : Boxing.listWrapperType())
            put(aClass, true);

        put(String.class, true);
    }};
    public static boolean isJBaseData(Object obj) {
        return JBASE_DATA_TYPE.containsKey(null == obj ? null : obj.getClass());
    }

    static final Map<String, Boolean> JBASE_DATA_TYPE_CLASS_NAME = new HashMap<String, Boolean>() {{
        put(null, true);

        for (Class<?> aClass : Boxing.listPrimitiveType())
            put(aClass.getName(), true);
        for (Class<?> aClass : Boxing.listWrapperType())
            put(aClass.getName(), true);

        put(String.class.getName(), true);
    }};
    public static boolean isJBaseDataClassName(String className) {
        return JBASE_DATA_TYPE_CLASS_NAME.containsKey(className);
    }




    public static boolean isRePrimitive(Object o) {
        return o instanceof Re_IRe_Object && ((Re_IRe_Object) o).isPrimitive();
    }


    public static boolean isReClass(Object o) {
        return o instanceof Re_Class;
    }
    public static boolean isReClassInstance(Object o) {
        return o instanceof Re_ClassInstance;
    }
    public static boolean isReClassInstance_list(Object o) {
        return o instanceof Re_PrimitiveClass_list.Instance;
    }
    public static boolean isReClassInstance_exception(Object o) {
        return o instanceof Re_PrimitiveClass_exception.Instance;
    }

    public static boolean isReFunction(Object o) {
        return o instanceof Re_ClassFunction;
    }

    public static Re_Class getReClassFromIReGetClass(Object o) {
        return o instanceof Re_IReGetClass          ? (((Re_IReGetClass)o).getReClass()):null;
    }
    public static Re_Class getDeclareReClassFromIReGetDeclaringClass(Object o) {
        return o instanceof Re_IReGetDeclaringClass ?((Re_IReGetDeclaringClass)o).getReDeclareClass():null;
    }



    public static Boolean isJArray(Object array) {
        return null != array && array.getClass().isArray();
    }








    public static Re_NativeClassLoader getCurrentClassLoader(Re_Executor executor) {
        if (null == executor) {
            return null;
        }
        Re_Class reClass = executor.reClass;
        return null == reClass ? null : reClass.getReClassLoader();
    }
    public static String getCurrentPackageName(Re_Executor executor) {
        Re_Class    reClass = executor.reClass;
        if (null == reClass) {
            return null;
        }
        String reClassPackageName = reClass.getReClassPackageName();
        return null != reClassPackageName && reClassPackageName.length() != 0 ? reClassPackageName : null;
    }


    public static Re_Class loadCurrentClassLoaderClass(Re_Executor executor, String name) {
        Re_NativeClassLoader reClassLoader = getCurrentClassLoader(executor);
        if (executor.isReturnOrThrow()) return null;

        return loadReClass(executor, reClassLoader, name);
    }
    @SuppressWarnings({"UnnecessaryLocalVariable", "StringOperationCanBeSimplified"})
    public static Re_Class loadReClass(Re_Executor executor, @NotNull Re_NativeClassLoader reClassLoader, String name) {
        if (null == name) {
            executor.setThrow(Re_Accidents.not_found_reclass(null));
            return null;
        }
        //当前包下面的类
        if (name.startsWith(PACKAGE_SEPARATOR_STRING)) {
            String cuurentPackageName = getCurrentPackageName(executor);
            if (executor.isReturnOrThrow()) return null;

            if (null == cuurentPackageName) {
                name = name.substring(PACKAGE_SEPARATOR_STRING.length(), name.length());
            } else {
                name  = cuurentPackageName + PACKAGE_SEPARATOR_STRING + name;
            }
        }
        Re_Class loadClass = loadReClass(executor.getStack(), reClassLoader, name);
        return   loadClass;
    }
    public static Re_Class loadReClass(Re_NativeStack stack, Re_NativeClassLoader reClassLoader, String className) {
        if (null == reClassLoader) {
            stack.setThrow(Re_Accidents.executor_no_bind_class_loader());
            return null;
        }
        Re_Class reClass = reClassLoader.lookupClass(stack, className);
        if (stack.isThrow()) return null;
        if (null == reClass)
            stack.setThrow(Re_Accidents.not_found_reclass(className));
        return reClass;
    }

    public static Object loadReClassOrJavaClassAndSetLocalVar(Re_Executor executor, String className) throws Throwable {
        Re_Class reClass = null;
        try {
            reClass = Re_Utilities.loadCurrentClassLoaderClass(executor, className);
        } catch (OutOfMemoryError memoryError) {
            executor.setThrow(error_out_of_memory());
            return null;
        } catch (Throwable ignored) {}

        if (executor.isThrow()) {
            Class<?> JavaClass = null;
            try {
                JavaClass = Re_Utilities.jforNameFindClass(className);
            } catch (OutOfMemoryError memoryError) {
                executor.setThrow(error_out_of_memory());
                return null;
            } catch (Throwable ignored) {}
            if (null == JavaClass) return null; //throw re exception

            executor.clearReturnOrThrow();//clear exception

            Re_PrimitiveObject_jimport jimport = Re_Utilities.toReJImport(JavaClass);
            String simpleName = Classx.findSimpleName(jimport.getJavaClass());
            executor.localValue(simpleName, jimport);
            return jimport;
        } else if (null == reClass) {
            return null;
        } else {
            String simpleName = reClass.getReClassSimpleName();
            executor.localValue(simpleName, reClass);
            return reClass;
        }
    }









    static void _____________(){}

    public static boolean isInstanceofIJavaClassWrap(Object object) {
        return object instanceof Re_IJavaClassWrap;
    }


    public static Class<?> jasclass(Object object) {
        if (object instanceof Class)
            return ((Class<?>) object);
        if (isInstanceofIJavaClassWrap(object))
            return ((Re_IJavaClassWrap) object).getJavaClass();

        if (null == object)
            return null;

        return object.getClass();
    }



    /**
     * @param object 如果是Class直接返回
     *               如果是Jimport则返回import的类
     *               否者返回null
     */
    public static Class<?> objectAsJavaClass(Object object) {
        if (object instanceof Class)
            return ((Class<?>) object);
        if (isInstanceofIJavaClassWrap(object))
            return ((Re_IJavaClassWrap) object).getJavaClass();

        return null;
    }

    /**
     * 不建议使用本方法 因为{@link Class#forName(String)} 是根据获取了调用该方法的类来确定加载器，由于你用了这个方法所有 调用的类永远不是你的类而是{@link Re_Utilities} 也就是说永远是从本工具类的加载器中寻找你要找的类
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static Class<?> jforNameFindClass(String name) throws ClassNotFoundException {
        return Classx.forName(name);
    }
    @SuppressWarnings("SpellCheckingInspection")
    public static Class<?> jforNameFindClass(String name, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        return Classx.forName(name, initialize, loader);
    }
    public static Class<?> jforNameFindClassOrAsClass(Object ele) throws ClassNotFoundException {
        if (ele instanceof Class)
            return (Class<?>) ele;
        if (isInstanceofIJavaClassWrap(ele))
            return ((Re_IJavaClassWrap) ele).getJavaClass();

        if (null == ele)
            return null;

        String s = Re_Utilities.toJString(ele);
        return jforNameFindClass(s);
    }
    public static Class<?>[] jforNameFindClassOrAsClass(Object[] value, int off, int len) throws ClassNotFoundException {
        if (value instanceof Class[]) {
            if (off <= 0 && len >= value.length) {
                return (Class<?>[])value;
            } else {
                Class<?>[] nvalue = new Class[Math.max(len, 0)];
                System.arraycopy(value, Math.max(off, 0), nvalue,0,nvalue.length);
                return nvalue;
            }
        } else {
            Class<?>[] nvalue = new Class[Math.max(len, 0)];
            for (int i = 0; i < len; i++) {
                Object ele = value[off+i];
                if (ele instanceof Class) {
                    nvalue[i] = (Class<?>) ele;
                } else if (Re_Utilities.isInstanceofIJavaClassWrap(ele)) {
                    nvalue[i] = ((Re_IJavaClassWrap) ele).getJavaClass();
                } else {
                    if (null == ele) {
                        nvalue[i] = null;
                    } else {
                        String name = Re_Utilities.toJString(ele);
                        Class<?> cls = jforNameFindClass(name);
                        nvalue[i] = cls;
                    }
                }
            }
            return nvalue;
        }
    }

















    //***********************************************************************************************************************

    static void ________________(){}

    public static boolean hasJavaClassVariable(Re_Executor executor, @NotNull Class cls, @NotNull String s) {
        Re_IJavaReflector reflector = executor.reReflector;
        Field member = reflector.field(cls, null, s);
        if (null == member) {
            Class classes = reflector.classes(cls, s);
            return null != classes;
        }
        return Modifier.isStatic(member.getModifiers());
    }

    public static boolean hasJavaVariable(Re_Executor executor, @NotNull Class cls, @NotNull String s) {
        Re_IJavaReflector reflector = executor.reReflector;
        Field member = reflector.field(cls, null, s);
        if (null == member) {
            Class classes = reflector.classes(cls, s);
            return null != classes;
        }
        return true;
    }

    public static Object getJavaClassValue(Re_Executor executor, @NotNull Class cls, @NotNull String currentName) throws IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Field field = reflector.field(cls, null, currentName);
        if (null == field) {
            Class classes = reflector.classes(cls, currentName);
            if (null != classes) {
                return toReJImport(classes);
            }
            executor.setThrow(reflector.buildNoSuchField(cls, null, currentName));
            return null;
        }
        if (Modifier.isStatic(field.getModifiers())) {
            return field.get(null);
        }
        executor.setThrow("not a static variable: " + currentName);
        return null;
    }

    public static Object getJavaValue(Re_Executor executor, @NotNull Class cls, @NotNull Object object, @NotNull String currentName) throws IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Field field = reflector.field(cls, null, currentName);
        if (null == field) {
            Class classes = reflector.classes(cls, currentName);
            if (null != classes) {
                return toReJImport(classes);
            }
            executor.setThrow(reflector.buildNoSuchField(cls, null, currentName));
            return null;
        }
        return field.get(object);
    }

    public static Object getJavaValue(Re_Executor executor, @NotNull Class cls, @NotNull Object object, @Nullable Class returnClass, @NotNull String currentName) throws IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Field field = reflector.field(cls, returnClass, currentName);
        if (null == field) {
            Class classes = reflector.classes(cls, currentName);
            if (null != classes) {
                return toReJImport(classes);
            }
            executor.setThrow(reflector.buildNoSuchField(cls, returnClass, currentName));
            return null;
        }
        return field.get(object);
    }





    public static void setJavaClassValue(Re_Executor executor, @NotNull Class cls, @NotNull String currentName, Object value) throws IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Field field = reflector.field(cls, null, currentName);
        if (null == field) {
            executor.setThrow(reflector.buildNoSuchField(cls, null, currentName));
            return;
        }
        if (Modifier.isStatic(field.getModifiers())) {
            field.set(null, value);
            return;
        }
        executor.setThrow("not a static variable: " + currentName);
    }


    public static void setJavaValue(Re_Executor executor, @NotNull Class cls, @NotNull Object object, @NotNull String currentName, Object value) throws IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Field field = reflector.field(cls, null, currentName);
        if (null == field) {
            executor.setThrow(reflector.buildNoSuchField(cls, null, currentName));
            return;
        }
        field.set(object, value);
    }

    public static void setJavaValue(Re_Executor executor, @NotNull Class cls, @NotNull Object object, @Nullable Class returnClass, @NotNull String currentName, Object value) throws IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Field field = reflector.field(cls, returnClass, currentName);
        if (null == field) {
            executor.setThrow(reflector.buildNoSuchField(cls, returnClass, currentName));
            return;
        }
        field.set(object, value);
    }



    public static Object invokeJavaClassMethod(Re_Executor executor, @NotNull Class cls, @NotNull String point_var_name, Object[] param) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Method member   = reflector.method(cls, null, point_var_name, param);
        if (null == member) {
            Class classes = reflector.classes(cls, point_var_name);
            if (null != classes) {
                Constructor constructor = reflector.constructor(classes, param);
                if (null != constructor) {
                    return  constructor.newInstance(param);
                }
                executor.setThrow(reflector.buildNoSuchConstructor(classes, param));
                return null;
            }
            executor.setThrow(reflector.buildNoSuchMethod(cls, null, point_var_name, param));
            return null;
        }
        if (Modifier.isStatic(member.getModifiers())) {
            return member.invoke(null, param);
        }
        executor.setThrow("not a static member: " + member);
        return null;
    }

    public static Object invokeJavaMethod(Re_Executor executor, @NotNull Object object, @NotNull String point_var_name, Object[] param) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        //java object reflect
        Class<?> cls = object.getClass();
        Re_IJavaReflector reflector = executor.reReflector;
        Method method   = reflector.method(cls, null, point_var_name, param);
        if (null == method) {
            Class classes = reflector.classes(cls, point_var_name);
            if (null != classes) {
                Constructor constructor = reflector.constructor(classes, param);
                if (null != constructor) {
                    return  constructor.newInstance(param);
                }
                executor.setThrow(reflector.buildNoSuchConstructor(classes, param));
                return null;
            }
            executor.setThrow(reflector.buildNoSuchMethod(cls, null, point_var_name, param));
            return null;
        }
        return method.invoke(object, param);
    }

    public static Object invokeJavaMethod(Re_Executor executor, @NotNull Class<?> cls, @NotNull Object object, @Nullable Class returnClass, @NotNull String point_var_name, Object[] param) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Method method   = reflector.method(cls, returnClass, point_var_name, param);
        if (null == method) {
            Class classes = reflector.classes(cls, point_var_name);
            if (null != classes) {
                Constructor constructor = reflector.constructor(classes, param);
                if (null != constructor) {
                    return  constructor.newInstance(param);
                }
                executor.setThrow(reflector.buildNoSuchConstructor(classes, param));
                return null;
            }
            executor.setThrow(reflector.buildNoSuchMethod(cls, returnClass, point_var_name, param));
            return null;
        }
        return method.invoke(object, param);
    }






    public static boolean removeJavaClassValue(Re_Executor executor, @NotNull Class cls, @NotNull String currentName) throws IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Field member = reflector.field(cls, null, currentName);
        if (null == member) {
            executor.setThrow(reflector.buildNoSuchField(cls, null, currentName));
            return false;
        }
        if (Modifier.isStatic(member.getModifiers()) ) {
            member.set(null, null);
            return true;
        }
        return false;
    }

    public static boolean removeJavaValue(Re_Executor executor, @NotNull Class cls, @NotNull Object object, @NotNull String currentName) throws IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Field member = reflector.field(cls, null, currentName);
        if (null == member) {
            executor.setThrow(reflector.buildNoSuchField(cls, null, currentName));
            return false;
        }
        member.set(object, null);
        return true;
    }




    public static int getJavaClassSize(Re_Executor executor, @NotNull Class cls) throws IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        int count = 0;
        Field[] fields = reflector.cacher().fields(cls);
        if (null != fields)
            for (Field field: fields)
                if (Modifier.isStatic(field.getModifiers()))
                    count++;
        return count;
    }

    /**
     * 不计算static变量
     */
    public static int getJavaObjectSize(Re_Executor executor, @NotNull Class cls) {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        int count = 0;
        Field[] fields = reflector.cacher().fields(cls);
        if (null != fields)
            for (Field field: fields)
                if (!Modifier.isStatic(field.getModifiers()))
                    count++;
        return count;
    }
    public static int getJavaSize(Re_Executor executor, @NotNull Class cls) {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        int count = 0;
        Field[] fields = reflector.cacher().fields(cls);
        if (null != fields)
            for (Field field: fields)
                count++;
        return count;
    }


    public static Iterable getJavaClassKeys(Re_Executor executor, @NotNull Class cls) {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Field[] fields = reflector.cacher().fields(cls);
        Set<String> objects = new LinkedHashSet<>();
        if (null != fields)
            for (Field field: fields)
                if (Modifier.isStatic(field.getModifiers()))
                    objects.add(field.getName());
        return objects;
    }

    /**
     * 不计算static变量
     */
    public static Iterable<String> getJavaObjectKeys(Re_Executor executor, @NotNull Class cls) {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Field[] fields = reflector.cacher().fields(cls);
        Set<String> objects = new LinkedHashSet<>();
        if (null != fields)
            for (Field field: fields)
                if (!Modifier.isStatic(field.getModifiers()))
                    objects.add(field.getName());
        return objects;
    }

    public static Iterable<String> getJavaKeys(Re_Executor executor, @NotNull Class cls) {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Field[] fields = reflector.cacher().fields(cls);
        Set<String> objects = new LinkedHashSet<>();
        if (null != fields)
            for (Field field: fields)
                objects.add(field.getName());
        return objects;
    }



    public static Object newJavaInstance(Re_Executor executor, @NotNull Class __class__, Object[] param) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        //java object reflect
        Re_IJavaReflector reflector = executor.reReflector;
        Constructor<?> constructor = reflector.constructor(__class__, param);
        if (null == constructor) {
            executor.setThrow(reflector.buildNoSuchConstructor(__class__, param));
            return null;
        }
        return constructor.newInstance(param);
    }


    static void _________________(){}

    //***********************************************************************************************************************
    public static Object newJArrayFromArrayCall(Re_Executor executor, Re_CodeLoader.Call callParamController) throws ClassNotFoundException {
        int paramExpressionCount = callParamController.getParamExpressionCount();
        Class<?> aClass  = null;
        Object elements0 = null;

        if (paramExpressionCount == 1) {
            aClass = Finals.OBJECT_CLASS;

            elements0 = executor.getExpressionValue(callParamController, 0);
            if (executor.isReturnOrThrow()) return null;
        } else if (paramExpressionCount == 2) {
            Object typeValue = executor.getExpressionValue(callParamController, 0);
            if (executor.isReturnOrThrow()) return null;

            aClass = jforNameFindClassOrAsClass(typeValue);
            if (null != aClass) {
                elements0 = executor.getExpressionValue(callParamController, 1);
                if (executor.isReturnOrThrow()) return null;
            }
        }

        if (null != aClass) {
            Object values = null;
            if (Re_Utilities.isReClassInstance_list(elements0)) {
                Re_PrimitiveClass_list.Instance instance = (Re_PrimitiveClass_list.Instance) elements0;
                int size = instance.size();
                values = Array.newInstance(aClass, size);
                for (int i = 0; i < size; i++) {
                    Object element = instance.getElement(executor, i);
                    if (executor.isReturnOrThrow()) return null;

                    Array.set(values, i, element);
                }
            } else if (Re_Utilities.isJArray(elements0)) {
                values = Arrayz.copyOf(elements0);
            }

            if (null != values) {
                return  values;
            }
        }

        final String help = "use " +
                Re_CodeLoader.Call.demoCall1(callParamController.getName(), "java.lang.Class:class", "list: elements | array: elements");
        executor.setThrow(help);
        return null;
    }


    /**
     * @param type 执行前确保是个数组
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static Object newJArrayFromElement(Re_Executor executor, Class<?> type, Re_CodeLoader.Call callParamController) {
        if ((type = type.getComponentType()).isPrimitive()) {
            //str(), int(), char(), float(), double)(short(), boolean(), long(), byte()
            if (type == Finals.INT_CLASS) {
                int dataCount = callParamController.getParamExpressionCount();
                int arrLen = dataCount;

                int[] array = (int[]) Array.newInstance(type, arrLen);
                for (int i = 0; i < dataCount; i++) {
                    Object expressionValue = executor.getExpressionValue(callParamController, i);
                    if (executor.isReturnOrThrow()) return null;

                    array[i] = toJInt(expressionValue);
                }
                return array;
            } else if (type == Finals.CHAR_CLASS) {
                int dataCount = callParamController.getParamExpressionCount();
                int arrLen = dataCount;

                char[] array = (char[]) Array.newInstance(type, arrLen);
                for (int i = 0; i < dataCount; i++) {
                    Object expressionValue = executor.getExpressionValue(callParamController, i);
                    if (executor.isReturnOrThrow()) return null;

                    array[i] = toJChar(expressionValue);
                }
                return array;
            } else if (type == Finals.FLOAT_CLASS) {
                int dataCount = callParamController.getParamExpressionCount();
                int arrLen = dataCount;

                float[] array = (float[]) Array.newInstance(type, arrLen);
                for (int i = 0; i < dataCount; i++) {
                    Object expressionValue = executor.getExpressionValue(callParamController, i);
                    if (executor.isReturnOrThrow()) return null;

                    array[i] = toJFloat(expressionValue);
                }
                return array;
            } else if (type == Finals.DOUBLE_CLASS) {
                int dataCount = callParamController.getParamExpressionCount();
                int arrLen = dataCount;

                double[] array = (double[]) Array.newInstance(type, arrLen);
                for (int i = 0; i < dataCount; i++) {
                    Object expressionValue = executor.getExpressionValue(callParamController, i);
                    if (executor.isReturnOrThrow()) return null;

                    array[i] = toJDouble(expressionValue);
                }
                return array;
            } else if (type == Finals.SHORT_CLASS) {
                int dataCount = callParamController.getParamExpressionCount();
                int arrLen = dataCount;

                short[] array = (short[]) Array.newInstance(type, arrLen);
                for (int i = 0; i < dataCount; i++) {
                    Object expressionValue = executor.getExpressionValue(callParamController, i);
                    if (executor.isReturnOrThrow()) return null;

                    array[i] = toJShort(expressionValue);
                }
                return array;
            } else if (type == Finals.BOOLEAN_CLASS) {
                int dataCount = callParamController.getParamExpressionCount();
                int arrLen = dataCount;

                boolean[] array = (boolean[]) Array.newInstance(type, arrLen);
                for (int i = 0; i < dataCount; i++) {
                    Object expressionValue = executor.getExpressionValue(callParamController, i);
                    if (executor.isReturnOrThrow()) return null;

                    array[i] = toJBoolean(expressionValue);
                }
                return array;
            } else if (type == Finals.LONG_CLASS) {
                int dataCount = callParamController.getParamExpressionCount();
                int arrLen = dataCount;

                long[] array = (long[]) Array.newInstance(type, arrLen);
                for (int i = 0; i < dataCount; i++) {
                    Object expressionValue = executor.getExpressionValue(callParamController, i);
                    if (executor.isReturnOrThrow()) return null;

                    array[i] = toJLong(expressionValue);
                }
                return array;
            } else if (type == Finals.BYTE_CLASS) {
                int dataCount = callParamController.getParamExpressionCount();
                int arrLen = dataCount;

                byte[] array = (byte[]) Array.newInstance(type, arrLen);
                for (int i = 0; i < dataCount; i++) {
                    Object expressionValue = executor.getExpressionValue(callParamController, i);
                    if (executor.isReturnOrThrow()) return null;

                    array[i] = toJByte(expressionValue);
                }
                return array;
            }
        } else {
            int dataCount = callParamController.getParamExpressionCount();
            int arrLen = dataCount;

            Object[] array = (Object[]) Array.newInstance(type, arrLen);
            for (int i = 0; i < dataCount; i++) {
                Object expressionValue = executor.getExpressionValue(callParamController, i);
                if (executor.isReturnOrThrow()) return null;

                array[i] = expressionValue;
            }
            return array;
        }
        return null;
    }

    public static Object newJArrayForLength(Re_Executor executor, Re_CodeLoader.Call callParamController) throws ClassNotFoundException {
        int paramExpressionCount = callParamController.getParamExpressionCount();
        if (paramExpressionCount == 2) {
            Object typeValue = executor.getExpressionValue(callParamController, 0);
            if (executor.isReturnOrThrow()) return null;

            Class<?> aClass = jforNameFindClassOrAsClass(typeValue);
            if (null != aClass) {
                Object size0 = executor.getExpressionValue(callParamController, 1);
                if (executor.isReturnOrThrow()) return null;

                if (null != size0) {
                    if (size0 instanceof Number) {
                        int size = ((Number) size0).intValue();
                        return Array.newInstance(aClass, size);
                    } else {
                        if (Re_Utilities.isReClassInstance_list(size0)) {
                            Re_PrimitiveClass_list.Instance instance = (Re_PrimitiveClass_list.Instance) size0;
                            Object[] integers = instance.toArray(executor);
                            int[] convert = Arrayx.convert(integers, Finals.EMPTY_INT_ARRAY);
                            return Array.newInstance(aClass, convert);
                        } else {
                            Class<?> sizeClass = size0.getClass();
                            if (sizeClass.isArray()) {
                                int[] convert = Arrayx.convert(size0, Finals.EMPTY_INT_ARRAY);
                                return Array.newInstance(aClass, convert);
                            }
                        }
                    }
                }
            }
        }

        final String help = "use " +
                Re_CodeLoader.Call.demoCall1(callParamController.getName(), "java.lang.Class:class", "int:length | int[]:length | list:length");
        executor.setThrow(help);
        return null;
    }

    static void _______________(){}




















    public static class ArrayBuffer<E> {
        private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
        Object[] elementData;
        private int size;
        private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
        private static final int DEFAULT_CAPACITY = 10;

        public ArrayBuffer() {
            this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
        }
        public ArrayBuffer(E[] value) {
            this.elementData = null == value?DEFAULTCAPACITY_EMPTY_ELEMENTDATA:value;
        }

        private static int hugeCapacity(int minCapacity) {
            if (minCapacity < 0) // overflow
                throw new OutOfMemoryError();
            return (minCapacity > MAX_ARRAY_SIZE) ?
                    Integer.MAX_VALUE :
                    MAX_ARRAY_SIZE;
        }
        private void grow(int minCapacity) {
            // overflow-conscious code
            int oldCapacity = elementData.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity - minCapacity < 0)
                newCapacity = minCapacity;
            if (newCapacity - MAX_ARRAY_SIZE > 0)
                newCapacity = hugeCapacity(minCapacity);
            // minCapacity is usually close to size, so this is a win:
            elementData = Arrays.copyOf(elementData, newCapacity);
        }


        private void ensureExplicitCapacity(int minCapacity) {
            // overflow-conscious code
            if (minCapacity - elementData.length > 0)
                grow(minCapacity);
        }

        private static int calculateCapacity(Object[] elementData, int minCapacity) {
            if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
                return Math.max(DEFAULT_CAPACITY, minCapacity);
            }
            return minCapacity;
        }

        private void ensureCapacityInternal(int minCapacity) {
            ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
        }




        public int size() {
            return size;
        }

        public E get(int index) {
            if (index >= 0 || index < size)
                //noinspection unchecked
                return (E) elementData[index];
            return null;
        }

        public void set(Object index, E element) {
            if (index instanceof Number) {
                this.set(((Number) index).intValue(), element);
            }
        }
        public void set(Number index, E element) {
            this.set(index.intValue(), element);
        }

        public void set(int index, E element) {
            if (index >= size) {
                int newSize = index + 1;
                ensureCapacityInternal(newSize);
                elementData[index] = element;
                size = newSize;
            } else {
                elementData[index] = element;
            }
        }

        public void setSize(int size) {
            if (elementData.length < size) {
                ensureCapacityInternal(size);
            }
            this.size = size;
        }

        public void trimToSize() {
            if (size < elementData.length) {
                elementData = (size == 0)
                        ? DEFAULTCAPACITY_EMPTY_ELEMENTDATA
                        : Arrays.copyOf(elementData, size);
            }
        }

        public void clear() {
            size = 0;
            elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
        }

        public boolean add(E e) {
            ensureCapacityInternal(size + 1);  // Increments modCount!!
            elementData[size++] = e;
            return true;
        }

        public Object[] toArray() {
            return Arrays.copyOf(elementData, size);
        }
    }










    @SuppressWarnings("unchecked")
    public static Object[] toarray(Re_Executor executor, Object object) throws Throwable {
        //reobject
        if (object instanceof Re_IRe_Object) {
            if (Re_Utilities.isReClassInstance_list(object)) {
                return ((Re_PrimitiveClass_list.Instance) object).toArray(executor);
            } else {
                Re_IRe_Object reObject = (Re_IRe_Object) object;
                if (reObject.hasObjectKeys()) {
                    Iterable keysProcess = reObject.getObjectKeys(executor);
                    if (executor.isReturnOrThrow()) return null;

                    ArrayBuffer array = new ArrayBuffer();
                    for (Object index : keysProcess) {
                        Object  value = reObject.getObjectValue(executor, index);
                        if (executor.isReturnOrThrow()) return null;

                        array.set(index, value);
                    }
                    return array.toArray();
                } else {
                    executor.setThrow("unsupport cast to array, from type: " + objectAsName(object));
                    return null;
                }
            }
        } else {
            //java null
            if (null == object) {
                return null;
            }

            //java array
            Class<?> aClass = object.getClass();
            if (aClass.isArray()) {
                Class<?> componentType = aClass.getComponentType();
                if (componentType.isPrimitive()) {
                    return (Object[]) Arrayx.convert(object, Finals.EMPTY_OBJECT_ARRAY);
                } else {
                    if (componentType == Finals.OBJECT_CLASS) {
                        return (Object[]) object;
                    } else {
                        return (Object[]) Arrayx.convert(object, Finals.EMPTY_OBJECT_ARRAY);
                    }
                }
            }

            //java iterable keyset
            if (object instanceof Iterable) {
                Iterable<?> o = (Iterable) object;

                Iterator<?> iterator = o.iterator();
                List<Object> cache = new ArrayList<>();
                while (iterator.hasNext()) {
                    cache.add(iterator.next());
                }

                return cache.toArray();
            }

            //java object
            executor.setThrow("unsupport cast to array, from type: " + objectAsName(object));
            return null;
        }
    }


    @SuppressWarnings({"WhileLoopReplaceableByForEach", "unchecked"})
    public static Re_PrimitiveClass_list.Instance tolist(Re_Executor executor, Object object) throws Throwable {
        Re_PrimitiveClass_list.Instance instance = Rez.SafesRe.createInstance_list(executor);
        if (executor.isReturnOrThrow()) return null;

        //reobject
        if (object instanceof Re_IRe_Object) {
            if (Re_Utilities.isReClassInstance_list(object)) {
                Re_PrimitiveClass_list.Instance otherList = (Re_PrimitiveClass_list.Instance) object;
                Iterable<?> realKeySet = otherList.innerGetRealKeySet();
                for (Object key : realKeySet) {
                    Object  value = Re_Variable.accessGetInstanceOrClassValue(executor, key, otherList);
                    if (executor.isReturnOrThrow()) return null;

                    /*safe*/Re_Variable.Unsafes.putListVariable(key, value, instance);
                }
            } else {
                Re_IRe_Object reObject = (Re_IRe_Object) object;
                if (reObject.hasObjectKeys()) {
                    Iterable keysProcess = reObject.getObjectKeys(executor);
                    if (executor.isReturnOrThrow()) return null;

                    for (Object key : keysProcess) {
                        Object  value = reObject.getObjectValue(executor, key);
                        if (executor.isReturnOrThrow()) return null;

                        /*safe*/Re_Variable.Unsafes.putListVariable(key, value, instance);
                    }
                } else {
                    executor.setThrow("unsupport cast to list, from type: " + objectAsName(object));
                    return null;
                }
            }
            return instance;
        } else {
            //java null
            if (null == object) {
                return null;
            }

            //java array
            Class<?> aClass = object.getClass();
            if (aClass.isArray()) {
                if (aClass.getComponentType().isPrimitive()) {
                    Object[] convert = Arrayx.convert(object, Finals.EMPTY_OBJECT_ARRAY);
                    instance.setElements(executor, convert);
                } else {
                    instance.setElements(executor, (Object[]) object);
                }
                if (executor.isReturnOrThrow()) return null;
                return instance;
            }

            //java iterable keyset
            if (object instanceof Iterable) {
                Iterable<?> o = (Iterable) object;

                List cache = new ArrayList();
                Iterator iterator = o.iterator();
                while (iterator.hasNext()) {
                    cache.add(iterator.next());
                }

                instance.setElements(executor, cache.toArray());
                if (executor.isReturnOrThrow()) return null;
                return instance;
            }

            //java object
            Iterable<String> javaObjectKeys = getJavaObjectKeys(executor, aClass);
            if (executor.isReturnOrThrow()) return null;

            for (String key : javaObjectKeys) {
                Object value = getJavaValue(executor, aClass, object, key);
                if (executor.isReturnOrThrow()) return null;

                /*safe*/Re_Variable.Unsafes.putListVariable(key, value, instance);
            }

            return instance;
        }
    }



    public static Re_ClassInstance toinstance(Re_Executor executor, Object object) throws Throwable {
        Re_ClassInstance instance = Rez.SafesRe.createInstance_object(executor);
        if (executor.isReturnOrThrow()) return null;

        //reobject
        if (object instanceof Re_IRe_Object) {
            if (Re_Utilities.isReClassInstance_list(object)) {
                Re_PrimitiveClass_list.Instance otherList = (Re_PrimitiveClass_list.Instance) object;
                Iterable<?> realKeySet = otherList.innerGetRealKeySet();
                for (Object key : realKeySet) {
                    Object  value = Re_Variable.accessGetInstanceOrClassValue(executor, key, otherList);
                    if (executor.isReturnOrThrow()) return null;

                    /*safe*/Re_Variable.Unsafes.putVariable(key, Re_Variable.createVariable(value), instance);
                }
            } else {
                Re_IRe_Object reObject = (Re_IRe_Object) object;
                if (reObject.hasObjectKeys()) {
                    Iterable keysProcess = reObject.getObjectKeys(executor);
                    if (executor.isReturnOrThrow()) return null;

                    for (Object key : keysProcess) {
                        Object value = reObject.getObjectValue(executor, key);
                        if (executor.isReturnOrThrow()) return null;

                        /*safe*/Re_Variable.Unsafes.putVariable(key, Re_Variable.createVariable(value), instance);
                    }
                } else {
                    executor.setThrow("unsupport cast to object, from type: " + objectAsName(object));
                    return null;
                }
            }
            return instance;
        } else {
            //java null
            if (null == object) {
                return null;
            }

            //java array
            Class<?> aClass = object.getClass();
            if (aClass.isArray()) {
                for (int key = 0; key < Array.getLength(object); key++) {
                    Object value = Array.get(object, key);

                    Re_Variable.accessSetValue(executor, key, value, instance);
                    if (executor.isReturnOrThrow()) return null;
                }
                return instance;
            }

            //java iterable keyset
            if (object instanceof Iterable) {
                Iterable<?> o = (Iterable) object;

                int key = 0;
                for (Object value : o) {
                    Re_Variable.accessSetValue(executor, key, value, instance);
                    if (executor.isReturnOrThrow()) return null;

                    key++;
                }
                return instance;
            }

            //java object
            Iterable<String> javaObjectKeys = getJavaObjectKeys(executor, aClass);
            if (executor.isReturnOrThrow()) return null;

            for (String key : javaObjectKeys) {
                Object value = getJavaValue(executor, aClass, object, key);
                if (executor.isReturnOrThrow()) return null;

                /*safe*/Re_Variable.Unsafes.putVariable(key, Re_Variable.createVariable(value), instance);
            }

            return instance;
//        executor.setThrow("unsupport cast to instance, from type: " + getName(object));
//        return null;
        }
    }


    public static Re_IRe_Iterable toReIterable(Re_Executor executor, Object object0) throws Throwable {
        if (object0 instanceof Re_IRe_Iterable) return (Re_IRe_Iterable) object0; // this

        if (object0 instanceof Re_IRe_Object) {
            return Re_IRe_Iterable.Utilities.wrapIReObject(executor, (Re_IRe_Object) object0);
        } else {
            if (null == object0) {
                executor.setThrow(Re_Accidents.unsupported_type(Re_Utilities.objectAsName(object0)));
                return null;
            }

            Class<?> aClass = object0.getClass();
            if (aClass.isArray())
                return Re_IRe_Iterable.Utilities.wrapJavaArray(executor, object0);

            //java (Set, List)
            if (object0 instanceof Iterable)
                return Re_IRe_Iterable.Utilities.wrapJavaIterable(executor, (Set) object0);

            executor.setThrow(Re_Accidents.unsupported_type(Re_Utilities.objectAsName(object0)));
            return null;
        }
    }

    static void __________________(){}

    public static Object getattr(Re_Executor executor, Object object, Object key) {
        if (object instanceof Re_IRe_Object) {
            try {
                Re_IRe_Object reObject = (Re_IRe_Object) object;
                return reObject.getObjectValue(executor, key);
            } catch (Throwable innerEx) {
                executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                return null;
            }
        } else {
            //java null
            if (null == object) {
                return null;
            }

            //java array
            Class<?> aClass = object.getClass();
            if (aClass.isArray()) {
                if (key instanceof Number) {
                    int index = ((Number) key).intValue();
                    return index >= 0 && index < Array.getLength(object) ? Array.get(object, index) : null;
                }
                return null;
            }

            //java object
            String s = Re_Utilities.toJString(key);
            try {
                return getJavaValue(executor, aClass, object, s);
            } catch (Throwable innerEx) {
                executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                return null;
            }
        }
    }


    public static boolean setattr(Re_Executor executor, Object object, Object key, Object value) {
        //reobject
        if (object instanceof Re_IRe_Object) {
            Re_IRe_Object reObject = (Re_IRe_Object) object;
            try {
                reObject.putObjectValue(executor, key, value);
                return true;
            } catch (Throwable innerEx) {
                executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                return false;
            }
        } else {
            //java null
            if (null == object) {
                return false;
            }

            //java array
            Class<?> aClass = object.getClass();
            if (aClass.isArray()) {
                if (key instanceof Number) {
                    int index = ((Number) key).intValue();
                    if (index >= 0 && index < Array.getLength(object)) {
                        Array.set(object, index, value);
                        return true;
                    }
                }
                return false;
            }

            //java object
            String s = Re_Utilities.toJString(key);
            try {
                setJavaValue(executor, aClass, object, s, value);
                return true;
            } catch (Throwable innerEx) {
                executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                return false;
            }
        }
    }
    public static boolean hasattr(Re_Executor executor, Object object, Object key) {
        //reobject
        if (object instanceof Re_IRe_Object) {
            Re_IRe_Object reObject = (Re_IRe_Object) object;
            try {
                return reObject.hasObjectKey(executor, key);
            } catch (Throwable innerEx) {
                executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                return false;
            }
        } else {
            //java null
            if (null == object) {
                return false;
            }

            //java array
            Class<?> aClass = object.getClass();
            if (aClass.isArray()) {
                if (key instanceof Number) {
                    int index = ((Number) key).intValue();
                    return index >= 0 && index < Array.getLength(object);
                }
                return false;
            }

            //java object
            String s = Re_Utilities.toJString(key);
            try {
                return hasJavaVariable(executor, aClass, s);
            } catch (Throwable innerEx) {
                executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                return false;
            }
        }
    }

    public static boolean delattr(Re_Executor executor, Object object, Object key) {
        //reobject
        if (object instanceof Re_IRe_Object) {
            Re_IRe_Object reObject = (Re_IRe_Object) object;
            try {
                return reObject.removeObjectKey(executor, key);
            } catch (Throwable innerEx) {
                executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                return false;
            }
        } else {
            //java null
            if (null == object) {
                return false;
            }

            //java array
            Class<?> aClass = object.getClass();
            if (aClass.isArray()) {
                if (key instanceof Number) {
                    int index = ((Number) key).intValue();
                    if (index >= 0 && index < Array.getLength(object)) {
                        Array.set(object, index, null);
                        return true;
                    }
                }
                return false;
            }

            //java object
            String s = Re_Utilities.toJString(key);
            try {
                removeJavaValue(executor, aClass, object, s);
                return true;
            } catch (Throwable innerEx) {
                executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                return false;
            }
        }
    }
    public static int lenattr(Re_Executor executor, Object object) {
        //reobject
        if (object instanceof Re_IRe_Object) {
            Re_IRe_Object reObject = (Re_IRe_Object) object;
            try {
                return reObject.getObjectKeyCount(executor);
            } catch (Throwable innerEx) {
                executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                return 0;
            }
        } else {
            //java null
            if (null == object) {
                return 0;
            }

            //java array
            Class<?> aClass = object.getClass();
            if (aClass.isArray()) {
                return Array.getLength(object);
            }

            //java object
            try {
                return getJavaObjectSize(executor, aClass);
            } catch (Throwable innerEx) {
                executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                return 0;
            }
        }
    }
    public static Iterable keyattr(Re_Executor executor, Object object) {
        //reobject
        if (object instanceof Re_IRe_Object) {
            Re_IRe_Object reObject = (Re_IRe_Object) object;
            if (reObject.hasObjectKeys()) {
                return null;
            } else {
                try {
                    return reObject.getObjectKeys(executor);
                } catch (Throwable innerEx) {
                    executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                    return null;
                }
            }
        } else {
            //java null
            if (null == object) {
                return null;
            }

            //java array
            Class<?> aClass = object.getClass();
            if (aClass.isArray()) {
                return Iterables.wrapRange(0, Array.getLength(object));
            }

            //java object
            try {
                return getJavaObjectKeys(executor, aClass);
            } catch (Throwable innerEx) {
                executor.setThrowFromJavaExceptionToStringAsReason(innerEx);
                return null;
            }
        }
    }


    static void ______________________(){}

    @Private
    @SuppressWarnings("UnusedReturnValue")
    static class UnsafeDebugger {
        static NReflectMatcher matcher = new NReflectMatcher(new NReflectMatcher.NCache());
        @Private
        static class NReflectMatcher extends ReflectMatcher<NReflectMatcher.NCache> implements Re_IJavaReflector {
            public NReflectMatcher(NCache cache) {
                super(cache);
            }

            static class NCache extends ReflectCache {
                //-------------------------------------------只是公开方法而已没有实际意义---------------------------------------------
                protected ClassesList 	    getClassesList(Class cls) 			 			{ return super.getClassesList(cls);      }
                protected Class             getClasses(Class cls, String simpleName) 	    { return super.getClasses(cls, simpleName); }
                protected ConstructorList   getConstructorList(Class cls)  				    { return super.getConstructorList(cls); }
                protected FieldList         getFieldList(Class cls)						    { return super.getFieldList(cls);       }
                protected FieldList         getFieldList(Class cls, String name)  		    { return super.getFieldList(cls, name); }
                protected MethodList        getMethodList(Class p1)   					    { return super.getMethodList(p1);   }
                protected MethodList        getMethodList(Class p1, String p2)    		    { return super.getMethodList(p1, p2);   }
                //------------------------------------------------------------------------------------------------------------------
            }
        }
        @Private
        static Object getObjectFieldValue(Object object, Object key) {
            if (object instanceof Re_IRe_Object) {
                if (Re_Utilities.isReClass(object)) {
                    Re_Class instance = (Re_Class) object;
                    return Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(key, instance);
                } else if (Re_Utilities.isReClassInstance(object)) {
                    if (Re_Utilities.isReClassInstance_list(object)) {
                        Re_PrimitiveClass_list.Instance instance = (Re_PrimitiveClass_list.Instance) object;
                        return Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(key, instance);
                    } else {
                        Re_ClassInstance instance = (Re_ClassInstance) object;
                        return Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(key, instance);
                    }
                } else if (Re_Utilities.isSpace(object)) {
                    Re_Executor instance = (Re_Executor) object;
                    return Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(key, instance);
                } else {
                    return null;
                }
            } else {
                //java null
                if (null == object) {
                    return null;
                }

                //java array
                Class<?> aClass = object.getClass();
                if (aClass.isArray()) {
                    if (key instanceof Number) {
                        int index = ((Number) key).intValue();
                        return index >= 0 && index < Array.getLength(object) ? Array.get(object, index) : null;
                    }
                    return null;
                }

                //java object
                String s = Re_Utilities.objectAsString(key);
                NReflectMatcher.NCache cacher = matcher.cacher();
                Field  field = cacher.field(aClass, s);
                if (null == field) {
                    return null;
                }
                try {
                    return field.get(object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        @Private
        static boolean setObjectFieldValue(Object object, Object key, Object value) {
            if (object instanceof Re_IRe_Object) {
                if (Re_Utilities.isReClass(object)) {
                    Re_Class instance = (Re_Class) object;
                    Re_Variable.Unsafes.fromUnsafeAccessorSetValueOrThrowEx(key, value, instance);
                    return true;
                } else if (Re_Utilities.isReClassInstance(object)) {
                    if (Re_Utilities.isReClassInstance_list(object)) {
                        Re_PrimitiveClass_list.Instance instance = (Re_PrimitiveClass_list.Instance) object;
                        Re_Variable.Unsafes.fromUnsafeAccessorSetValueOrThrowEx(key, value, instance);
                        return true;
                    } else {
                        Re_ClassInstance instance = (Re_ClassInstance) object;
                        Re_Variable.Unsafes.fromUnsafeAccessorSetValueOrThrowEx(key, value, instance);
                        return true;
                    }
                } else if (Re_Utilities.isSpace(object)) {
                    Re_Executor instance = (Re_Executor) object;
                    Re_Variable.Unsafes.fromUnsafeAccessorSetValueOrThrowEx(key, value, instance);
                    return true;
                } else {
                    return false;
                }
            } else {
                //java null
                if (null == object) {
                    return false;
                }

                //java array
                Class<?> aClass = object.getClass();
                if (aClass.isArray()) {
                    if (key instanceof Number) {
                        int index = ((Number) key).intValue();
                        if (index >= 0 && index < Array.getLength(object)) {
                            Array.set(object, index, value);
                            return true;
                        }
                    }
                    return false;
                }

                //java object
                String s = Re_Utilities.objectAsString(key);
                NReflectMatcher.NCache cacher = matcher.cacher();
                Field field = cacher.field(aClass, s);
                if (null == field) {
                    return false;
                }
                try {
                    field.set(object, value);
                    return true;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        @Private
        static boolean contanisObjectKey(Object object, Object key) {
            if (object instanceof Re_IRe_Object) {
                if (Re_Utilities.isReClass(object)) {
                    Re_Class instance = (Re_Class) object;
                    return Re_Variable.has(key, instance);
                } else if (Re_Utilities.isReClassInstance(object)) {
                    if (Re_Utilities.isReClassInstance_list(object)) {
                        Re_PrimitiveClass_list.Instance instance = (Re_PrimitiveClass_list.Instance) object;
                        return Re_Variable.has(key, instance);
                    } else {
                        Re_ClassInstance instance = (Re_ClassInstance) object;
                        return Re_Variable.has(key, instance);
                    }
                } else if (Re_Utilities.isSpace(object)) {
                    Re_Executor instance = (Re_Executor) object;
                    return Re_Variable.has(key, instance);
                } else {
                    return false;
                }
            } else {
                //java null
                if (null == object) {
                    return false;
                }

                //java array
                Class<?> aClass = object.getClass();
                if (aClass.isArray()) {
                    if (key instanceof Number) {
                        int index = ((Number) key).intValue();
                        return index >= 0 && index < Array.getLength(object);
                    }
                    return false;
                }

                //java object
                String s = Re_Utilities.objectAsString(key);
                NReflectMatcher.NCache cacher = matcher.cacher();
                return null != cacher.field(aClass, s);
            }
        }

        @Private
        static boolean delObjectKey(Object object, Object key) {
            if (object instanceof Re_IRe_Object) {
                if (Re_Utilities.isReClass(object)) {
                    Re_Class instance = (Re_Class) object;
                    Re_Variable.Unsafes.removeVariableOrThrowEx(key, instance);
                    return true;
                } else if (Re_Utilities.isReClassInstance(object)) {
                    if (Re_Utilities.isReClassInstance_list(object)) {
                        Re_PrimitiveClass_list.Instance instance = (Re_PrimitiveClass_list.Instance) object;
                        Re_Variable.Unsafes.removeVariableOrThrowEx(key, instance);
                        return true;
                    } else {
                        Re_ClassInstance instance = (Re_ClassInstance) object;
                        Re_Variable.Unsafes.removeVariableOrThrowEx(key, instance);
                        return true;
                    }
                } else if (Re_Utilities.isSpace(object)) {
                    Re_Executor instance = (Re_Executor) object;
                    Re_Variable.Unsafes.removeVariableOrThrowEx(key, instance);
                    return true;
                } else {
                    return false;
                }
            } else {
                //java null
                if (null == object) {
                    return false;
                }

                //java array
                Class<?> aClass = object.getClass();
                if (aClass.isArray()) {
                    throw new RuntimeException("unsupport type: " + objectAsName(object));
                }

                //java object
                throw new RuntimeException("unsupport type: " + objectAsName(object));
            }
        }
        @Private
        static int getObjectLength(Object object) {
            if (object instanceof Re_IRe_Object) {
                if (Re_Utilities.isReClass(object)) {
                    Re_Class instance = (Re_Class) object;
                    return Re_Variable.size(instance);
                } else if (Re_Utilities.isReClassInstance(object)) {
                    if (Re_Utilities.isReClassInstance_list(object)) {
                        Re_PrimitiveClass_list.Instance instance = (Re_PrimitiveClass_list.Instance) object;
                        return Re_Variable.size(instance);
                    } else {
                        Re_ClassInstance instance = (Re_ClassInstance) object;
                        return Re_Variable.size(instance);
                    }
                } else if (Re_Utilities.isSpace(object)) {
                    Re_Executor instance = (Re_Executor) object;
                    return Re_Variable.size(instance);
                } else {
                    return 0;
                }
            } else {
                //java null
                if (null == object) {
                    return 0;
                }

                //java array
                Class<?> aClass = object.getClass();
                if (aClass.isArray()) {
                    return Array.getLength(object);
                }

                //java object
                NReflectMatcher.NCache cacher = matcher.cacher();
                Field[] fieldList = cacher.fields(aClass);
                return  fieldList.length;
            }
        }

        @Private
        static Iterable getObjectKeys(Object object) {
            if (object instanceof Re_IRe_Object) {
                if (Re_Utilities.isReClass(object)) {
                    Re_Class instance = (Re_Class) object;
                    return Re_Variable.key(instance);
                } else if (Re_Utilities.isReClassInstance(object)) {
                    if (Re_Utilities.isReClassInstance_list(object)) {
                        Re_PrimitiveClass_list.Instance instance = (Re_PrimitiveClass_list.Instance) object;
                        return instance.innerGetRealKeySet();
                    } else {
                        Re_ClassInstance instance = (Re_ClassInstance) object;
                        return Re_Variable.key(instance);
                    }
                } else if (Re_Utilities.isSpace(object)) {
                    Re_Executor instance = (Re_Executor) object;
                    return Re_Variable.key(instance);
                } else {
                    return null;
                }
            } else {
                //java null
                if (null == object) {
                    return null;
                }

                //java array
                Class<?> aClass = object.getClass();
                if (aClass.isArray()) {
                    return Iterables.wrapRange(0, Array.getLength(object));
                }

                //java object
                NReflectMatcher.NCache cacher = matcher.cacher();
                Field[] fieldList = cacher.fields(aClass);
                List<String> nameList = new ArrayList<>();
                for (Field field : fieldList) {
                    nameList.add(field.getName());
                }
                String[] names = nameList.toArray(Finals.EMPTY_STRING_ARRAY);
                return Iterables.wrapArray(names, 0, names.length);
            }
        }
    }
}
