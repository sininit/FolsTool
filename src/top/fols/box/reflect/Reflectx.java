package top.fols.box.reflect;

import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.interfaces.annotations.UnsafeOperate;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Boxing;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Strings;
import top.fols.atri.reflect.Reflects;
import top.fols.box.lang.Classx;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("all")
public class Reflectx {
    public static final String GETTER_NAME_IS_PREFIX = "is";
    public static final String GETTER_NAME_PREFIX = "get";
    public static final String SETTER_NAME_PREFIX = "set";


    static final String CLASS_NAME = Reflectx.class.getName();



    static final String  METHOD_NAME_findCallStackTraceElement  = "findCallStackTraceElement";

    @Deprecated
    static public StackTraceElement findCallStackTraceElement() {
        for (StackTraceElement element: Thread.currentThread().getStackTrace()) {
            if (element.isNativeMethod()) continue;

            String className  = element.getClassName();
            if (Classx.isJavaPackage(className)) continue;

            String methodName = element.getMethodName();
            if (className.equals(CLASS_NAME) && methodName.equals(METHOD_NAME_findCallStackTraceElement))
                continue;

            return element;
        }
        throw new UnsupportedOperationException("not found available stack trace");
    }



    static final String  METHOD_NAME_findCallStackTraceElements = "findCallStackTraceElements";


    @Deprecated
    static public List<StackTraceElement> findCallStackTraceElements() {
        List<StackTraceElement> writer = new ArrayList<>();
        for (StackTraceElement element: Thread.currentThread().getStackTrace()) {
            if (element.isNativeMethod()) continue;

            String className  = element.getClassName();
            if (Classx.isJavaPackage(className)) continue;

            String methodName = element.getMethodName();
            if (className.equals(CLASS_NAME) && methodName.equals(METHOD_NAME_findCallStackTraceElements))
                continue;

            writer.add(element);
        }
        return writer;
    }



    static public int getCallLine() {
        return findCallStackTraceElement().getLineNumber();
    }





    private static abstract class ModifierSetter {
        public abstract void accessable(Field field, int newModifier) throws IllegalArgumentException, IllegalAccessException;
    }

    protected static             int TEST_MODIFER1 = 0;
    static final      			 int TEST_MODIFER2 = 0;
    volatile 	 				 int TEST_MODIFER3 = 0;
    int TEST_MODIFER4 = 0;
    private 				     int TEST_MODIFER5 = 0;
    transient  	 				 int TEST_MODIFER6 = 0;
    private final 	 		     int TEST_MODIFER7 = 0;
    static volatile	 			 int TEST_MODIFER8 = 0;

    private static ModifierSetter java_lang_reflect_Field__modifier;
    private static ModifierSetter java_lang_reflect_Field__modifier() {
        ModifierSetter cache = java_lang_reflect_Field__modifier;
        if (null == cache) {
            ModifierSetter result = null;

            Field[] seekFields;
            try {
                seekFields = new Field[] {
                        Reflectx.class.getDeclaredField("TEST_MODIFER1"),
                        Reflectx.class.getDeclaredField("TEST_MODIFER2"),
                        Reflectx.class.getDeclaredField("TEST_MODIFER3"),
                        Reflectx.class.getDeclaredField("TEST_MODIFER4"),
                        Reflectx.class.getDeclaredField("TEST_MODIFER5"),
                        Reflectx.class.getDeclaredField("TEST_MODIFER6"),
                        Reflectx.class.getDeclaredField("TEST_MODIFER7"),
                        Reflectx.class.getDeclaredField("TEST_MODIFER8")
                };
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
            Field[] fieldFields = null;
            try {
                fieldFields = Reflects.accessible(Reflects.getDeclaredFields(Finals.FIELD_CLASS));
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }

            if (null == result) {
                try {
                    Field[] resultBuff = fieldFields.clone();
                    Object value;
                    for (Field f: seekFields) {
                        List<Field> filterFields = new ArrayList<>();
                        int findModifier = f.getModifiers();
                        for (Field fi : resultBuff) {
                            if ((value = fi.get(f)) instanceof Integer) {
                                if (((Integer) value) == findModifier) {
                                    filterFields.add(fi);
                                }
                            }
                        }
                        resultBuff = filterFields.toArray(Finals.EMPTY_FIELD_ARRAY);//filter
                    }
                    final Field[] list = resultBuff;
                    if (resultBuff.length > 0) {
                        result = new ModifierSetter() {
                            @Override
                            public void accessable(Field field, int newModifier) throws IllegalArgumentException, IllegalAccessException {
                                // TODO: Implement this method
                                for (Field modifier: list) {
                                    modifier.setInt(field, newModifier);
                                }
                            }

                            @Override
                            public String toString() {
                                return "modifier";
                            }
                        };
                    }
                } catch (Exception e) {}
            }
            if (null == result) {
                try { //Android System  accessFlags & 0xffff
                    Map<Field, Integer> accessFlagsListFilter = new LinkedHashMap<>();
                    Map<Field, Integer> accessFlagsListFilterX = new LinkedHashMap<>();
                    Field[] resultBuff = fieldFields.clone();
                    Object value;
                    for (Field f: seekFields) {
                        int r = f.getModifiers();
                        for (Field fi : resultBuff) {
                            if ((value = fi.get(f)) instanceof Integer) {
                                int  accessFlags = (Integer) value;
                                if ((accessFlags & (accessFlags & r)) == r) {
                                    int x = accessFlags & ~r;

                                    Integer last  = accessFlagsListFilter.get(fi);
                                    Integer lastX = accessFlagsListFilterX.get(fi);
                                    accessFlagsListFilter.put(fi,  (null == last  ?0: last) + 1) ;
                                    accessFlagsListFilterX.put(fi, (null == lastX ?0: x));
                                }
                            }
                        }
                    }

//					System.out.println(accessFlagsListFilter);
//					System.out.println(accessFlagsListFilterX);

                    List<Field>   accessFlagsList  = new ArrayList<>();
                    List<Integer> accessFlagsListx = new ArrayList<>();
                    for (Map.Entry<Field, Integer> entry: accessFlagsListFilter.entrySet()) {
                        int s = entry.getValue();
                        if (s == seekFields.length) {
                            Field key = entry.getKey();
                            Integer x = accessFlagsListFilterX.get(key);
                            accessFlagsList.add(key);
                            accessFlagsListx.add(x);
                        }
                    }
                    if (accessFlagsList.size() > 0) {
                        final Field[]   accessFlagsFields = accessFlagsList.toArray(new Field[]{});
                        final Integer[] accessFlagsx      = accessFlagsListx.toArray(new Integer[]{});

                        result = new ModifierSetter() {
                            @Override
                            public void accessable(Field field, int newModifier) throws IllegalArgumentException, IllegalAccessException {
                                // TODO: Implement this method
                                for (int i = 0; i < accessFlagsFields.length; i++) {
                                    int x   = accessFlagsx[i];
                                    int accessFlags = x | newModifier;

                                    Field f = accessFlagsFields[i];
                                    f.set(field, accessFlags);
                                }
                            }
                            @Override
                            public String toString() {
                                return "accessFlags & x";
                            }
                        };
                    }
                } catch (Exception e) {}
            }

            //System.out.println(result);

            if (null == result) {
                throw new UnsupportedOperationException("not found field: " + Field.class.getName() + "." + "modifers");
            }
            java_lang_reflect_Field__modifier = cache = result;
        }
        return cache;
    }

    @UnsafeOperate
    public static Field setFinalFieldAccessAble(Field f) throws IllegalArgumentException, IllegalAccessException {
        return setFinalFieldAccessAble(f, f.getModifiers() & ~Modifier.FINAL);
    }

    @UnsafeOperate
    public static Field setFinalFieldAccessAble(Field f, int modifier) throws IllegalArgumentException, IllegalAccessException {
        ModifierSetter modifierList = java_lang_reflect_Field__modifier();
        modifierList.accessable(f, modifier);
        return f;
    }











    private static final ReflectPermission suppressAccessChecksReflectPermission = new ReflectPermission(
            "suppressAccessChecks");
    /**
     * Checks whether can control member accessible.
     *
     * @return If you can control member accessible, it return {@literal true}
     */
    public static boolean canSetAccessible() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(suppressAccessChecksReflectPermission);
            }
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }






    static private String unsafeClassName = "sun.misc.Unsafe";
    static private Object unsafe;
    public static Object getUnsafe() {
        if (null == unsafe) {
            try {
                Class unsafeClass = Class.forName(unsafeClassName);
                //通过反射获取Unsafe的成员变量theUnsafe
                Field field = Reflects.accessible(unsafeClass.getDeclaredField("theUnsafe"));
                unsafe = field.get(null);
                if (null == unsafe)
                    throw new RuntimeException("cannot get unsafe object");
                return unsafe;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return unsafe;
    }

    static private Method allocateInstanceMethod;
    public static Method  allocateInstanceMethod() {
        if (null == allocateInstanceMethod) {
            try {
                allocateInstanceMethod = Class.forName(unsafeClassName)
                        .getMethod("allocateInstance", Class.class);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return allocateInstanceMethod;
    }

    public static <T> T allocateInstance(Class<T> type) {
        if (type.isInterface())
            throw new RuntimeException("cannot create interface instance");
        try {
            return (T) allocateInstanceMethod().invoke(getUnsafe(), type);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





    /**
     * from对象是否能转换成to
     * @param from
     * @param to
     * @return
     */
    public static final boolean isConvert(Class<?> from, Class<?> to) {
        if (!isAssignable(from, to)) {
            return to.isPrimitive() && Boxing.toWrapperType(to) == from;
        }
        return true;
    }
    /**
     * from对象能否则直接赋值给to
     * @param from
     * @param to
     * @return
     */
    public static final boolean isAssignable(Class<?> from, Class<?> to) {
        if (from.isPrimitive()) {
            if (to != from && !to.isAssignableFrom(Boxing.toWrapperType(from)))
                return false;
        } else if (!to.isAssignableFrom(from))
            return false;
        return true;
    }


    public static boolean isPublic(@NotNull Class member) {
        do{
            if (!Modifier.isPublic(member.getModifiers())) {
                return false;
            }
        } while (null != (member = member.getDeclaringClass()));
        return true;
    }
    public static boolean isPublic(@NotNull Member member) {
        return Modifier.isPublic(member.getModifiers()) &&
                isPublic(member.getDeclaringClass());
    }

}






