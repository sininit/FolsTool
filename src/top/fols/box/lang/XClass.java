package top.fols.box.lang;

import java.lang.reflect.Array;
import top.fols.box.statics.XStaticBaseType;
import top.fols.box.util.XStringJoiner;

public class XClass {

    /**
     * (castToClass)obj
     * @param nullObjectCanCastToClass >> (castToClass)null
     * @return @param obj can cast to @param castToClass
     */
    public static boolean isInstance(Object obj, Class castToClass, boolean nullObjectCanCastToClass) {
        // object auto encapsulate / @param obj class >> package class

        if (null == castToClass) {
            return null == obj;
        }
        if (null == obj) {
            return nullObjectCanCastToClass && !castToClass.isPrimitive();//null can be converted to any package class ((?)null)
        }
        if (castToClass.isPrimitive()) {
            return (castToClass == XStaticBaseType.byte_class && obj instanceof Byte)
                || (castToClass == XStaticBaseType.long_class && obj instanceof Long)
                || (castToClass == XStaticBaseType.double_class && obj instanceof Double)
                || (castToClass == XStaticBaseType.char_class && obj instanceof Character)
                || (castToClass == XStaticBaseType.int_class && obj instanceof Integer)
                || (castToClass == XStaticBaseType.boolean_class && obj instanceof Boolean)
                || (castToClass == XStaticBaseType.float_class && obj instanceof Float)
                || (castToClass == XStaticBaseType.short_class && obj instanceof Short);
        } else {
            // if (instanceCls == XStaticBaseType.Byte_class) return obj instanceof Byte;
            // if (instanceCls == XStaticBaseType.Long_class) return obj instanceof Long;
            // if (instanceCls == XStaticBaseType.Double_class) return obj instanceof
            // Double;
            // if (instanceCls == XStaticBaseType.Character_class) return obj instanceof
            // Character;
            // if (instanceCls == XStaticBaseType.Integer_class) return obj instanceof
            // Integer;
            // if (instanceCls == XStaticBaseType.Boolean_class) return obj instanceof
            // Boolean;
            // if (instanceCls == XStaticBaseType.Float_class) return obj instanceof Float;
            // if (instanceCls == XStaticBaseType.Short_class) return obj instanceof Short;

            return castToClass.isInstance(obj);
        }
    }
    /**
     * @see isInstance, @param nullObjectCanCastToClass =  true
     */
    public static boolean isInstance(Object obj, Class castToClass) {
        return XClass.isInstance(obj, castToClass, true);
    }





    /**
     * (castToClass)obj
     * @param nullObjectCanCastToClass >> (castToClass)null
     * @return @param objcls can cast to @param castToClass
     */
    public static boolean isInstance(Class<?> objcls, Class<?> castToClass, boolean nullObjectCanCastToClass) {
        if (objcls == castToClass) {
            return true;
        }

        if (null == castToClass) {
            return false; // objcls == castToClass is false, so null == objcls is false
        }
        if (null == objcls) {
            return nullObjectCanCastToClass && !castToClass.isPrimitive();//null can be converted to any package class ((?)null)
        }
        if (castToClass.isPrimitive()) {
            return (castToClass == XStaticBaseType.byte_class && objcls == XStaticBaseType.Byte_class)
                || (castToClass == XStaticBaseType.long_class && objcls == XStaticBaseType.Long_class)
                || (castToClass == XStaticBaseType.double_class && objcls == XStaticBaseType.Double_class)
                || (castToClass == XStaticBaseType.char_class && objcls == XStaticBaseType.Character_class)
                || (castToClass == XStaticBaseType.int_class && objcls == XStaticBaseType.Integer_class)
                || (castToClass == XStaticBaseType.boolean_class && objcls == XStaticBaseType.Boolean_class)
                || (castToClass == XStaticBaseType.float_class && objcls == XStaticBaseType.Float_class)
                || (castToClass == XStaticBaseType.short_class && objcls == XStaticBaseType.Short_class);
        } else {
            if ((castToClass == XStaticBaseType.Byte_class && objcls == XStaticBaseType.byte_class)
                || (castToClass == XStaticBaseType.Long_class && objcls == XStaticBaseType.long_class)
                || (castToClass == XStaticBaseType.Double_class && objcls == XStaticBaseType.double_class)
                || (castToClass == XStaticBaseType.Character_class && objcls == XStaticBaseType.char_class)
                || (castToClass == XStaticBaseType.Integer_class && objcls == XStaticBaseType.int_class)
                || (castToClass == XStaticBaseType.Boolean_class && objcls == XStaticBaseType.boolean_class)
                || (castToClass == XStaticBaseType.Float_class && objcls == XStaticBaseType.float_class)
                || (castToClass == XStaticBaseType.Short_class && objcls == XStaticBaseType.short_class)) {
                return true;
            } else {
                return castToClass.isAssignableFrom(objcls);
            }
        }
    }
    /**
     * @see isInstance, @param nullObjectCanCastToClass =  true
     */
    public static boolean isInstance(Class objcls, Class castToClass) {
        return XClass.isInstance(objcls, castToClass, true);
    }










    public static boolean isPrimitiveClassName(String classCanonicalName) {
        if (classCanonicalName.equals(XStaticBaseType.byte_classcanonicalname)
            || classCanonicalName.equals(XStaticBaseType.char_classcanonicalname)
            || classCanonicalName.equals(XStaticBaseType.double_classcanonicalname)
            || classCanonicalName.equals(XStaticBaseType.float_classcanonicalname)
            || classCanonicalName.equals(XStaticBaseType.int_classcanonicalname)
            || classCanonicalName.equals(XStaticBaseType.long_classcanonicalname)
            || classCanonicalName.equals(XStaticBaseType.short_classcanonicalname)
            || classCanonicalName.equals(XStaticBaseType.boolean_classcanonicalname)
            || classCanonicalName.equals(XStaticBaseType.void_classcanonicalname)) {
            return true;
        } else {
            return false;
        }
    }


    
    public static Class primitiveClassForName(String classCanonicalName) {
        if (classCanonicalName.equals(XStaticBaseType.byte_classcanonicalname)) {
            return XStaticBaseType.byte_class;
        } else if (classCanonicalName.equals(XStaticBaseType.char_classcanonicalname)) {
            return XStaticBaseType.char_class;
        } else if (classCanonicalName.equals(XStaticBaseType.double_classcanonicalname)) {
            return XStaticBaseType.double_class;
        } else if (classCanonicalName.equals(XStaticBaseType.float_classcanonicalname)) {
            return XStaticBaseType.float_class;
        } else if (classCanonicalName.equals(XStaticBaseType.int_classcanonicalname)) {
            return XStaticBaseType.int_class;
        } else if (classCanonicalName.equals(XStaticBaseType.long_classcanonicalname)) {
            return XStaticBaseType.long_class;
        } else if (classCanonicalName.equals(XStaticBaseType.short_classcanonicalname)) {
            return XStaticBaseType.short_class;
        } else if (classCanonicalName.equals(XStaticBaseType.boolean_classcanonicalname)) {
            return XStaticBaseType.boolean_class;
        } else if (classCanonicalName.equals(XStaticBaseType.void_classcanonicalname)) {
            return XStaticBaseType.void_class;
        }
        return null;
    }






    //基础数据类型转换为包装类
    public static Class primitiveClassToPackageClass(Class baseClass) {
        if (baseClass == XStaticBaseType.boolean_class) {
            return XStaticBaseType.Boolean_class;
        } else if (baseClass == XStaticBaseType.byte_class) {
            return XStaticBaseType.Byte_class;
        } else if (baseClass == XStaticBaseType.char_class) {
            return XStaticBaseType.Character_class;
        } else if (baseClass == XStaticBaseType.double_class) {
            return XStaticBaseType.Double_class;
        } else if (baseClass == XStaticBaseType.float_class) {
            return XStaticBaseType.Float_class;
        } else if (baseClass == XStaticBaseType.int_class) {
            return XStaticBaseType.Integer_class;
        } else if (baseClass == XStaticBaseType.long_class) {
            return XStaticBaseType.Long_class;
        } else if (baseClass == XStaticBaseType.short_class) {
            return XStaticBaseType.Short_class;
        } else if (baseClass == XStaticBaseType.void_class) {
            return XStaticBaseType.Void_class;
        }
        return baseClass;
    }
    //将包装类转换成基础数据类型的类
    public static Class packageClassToPrimitiveClass(Class baseClass) {
        if (baseClass == XStaticBaseType.Boolean_class) {
            return XStaticBaseType.boolean_class;
        } else if (baseClass == XStaticBaseType.Byte_class) {
            return XStaticBaseType.byte_class;
        } else if (baseClass == XStaticBaseType.Character_class) {
            return XStaticBaseType.char_class;
        } else if (baseClass == XStaticBaseType.Double_class) {
            return XStaticBaseType.double_class;
        } else if (baseClass == XStaticBaseType.Float_class) {
            return XStaticBaseType.float_class;
        } else if (baseClass == XStaticBaseType.Integer_class) {
            return XStaticBaseType.int_class;
        } else if (baseClass == XStaticBaseType.Long_class) {
            return XStaticBaseType.long_class;
        } else if (baseClass == XStaticBaseType.Short_class) {
            return XStaticBaseType.short_class;
        } else if (baseClass == XStaticBaseType.Void_class) {
            return XStaticBaseType.void_class;
        }
        return baseClass;
    }







    private static final String CLASS_CANONICAL_NAME_ARRAY_STATEMENT = "[]";

    public static int getArrayDimensionalFromClassCanonicalName(String name) {
        if (null == name) {
            return 0;
        }
        int index = name.indexOf(CLASS_CANONICAL_NAME_ARRAY_STATEMENT); // java.lang.System[][]
        if (index == -1) {
            return 0;
        }
        if (!name.endsWith(CLASS_CANONICAL_NAME_ARRAY_STATEMENT)) {
            return 0;
        }
        return (name.length() - index) / CLASS_CANONICAL_NAME_ARRAY_STATEMENT.length();// [][]
    }

    /**
     * 把简单的类地址转为真的类地址
     * <p>
     * Main[] >> [LMain;
     * <p>
     * String[] >> [Ljava.lang.String;
     * <p>
     * byte[] >> [B
     * 
     * @param className
     * @return
     */
    // 把简单的类地址转换为真实的类地址
    public static String toAbsClassName(String className) {
        // 没有这些地址
        if (XClass.isPrimitiveClassName(className)) {
            return className;
        }
        // 判断数组纬度
        int dimensional = getArrayDimensionalFromClassCanonicalName(className);
        StringBuilder dimensionalPrefix = new StringBuilder();
        for (int i = 0; i < dimensional; i++) {
            dimensionalPrefix.append('[');
        }
        String e;
        if (dimensional > 0) {
            className = className.substring(0, className.indexOf(CLASS_CANONICAL_NAME_ARRAY_STATEMENT));
            if (className.equals("byte")) {
                e = "B";
            } else if (className.equals("char")) {
                e = "C";
            } else if (className.equals("double")) {
                e = "D";
            } else if (className.equals("float")) {
                e = "F";
            } else if (className.equals("int")) {
                e = "I";
            } else if (className.equals("long")) {
                e = "J";
            } else if (className.equals("short")) {
                e = "S";
            } else if (className.equals("boolean")) {
                e = "Z";
            } else if (className.equals("void")) {
                e = "V";
            } else {
                e = new StringBuilder().append('L').append(className).append(';').toString();
            }
            return dimensionalPrefix.append(e).toString();
        } else {
            return className;
        }
    }

    public static String[] toAbsClassName(String... classNames) {
        if (null == classNames || classNames.length == 0) {
            return null;
        }
        String c[] = new String[classNames.length];
        for (int i = 0; i < classNames.length; i++) {
            c[i] = null == classNames[i] ? null : toAbsClassName(classNames[i]);
        }
        return c;
    }





    public static Class getDeepArrayClass(Class elementClass, int deep) {
        Object array = Array.newInstance(elementClass, new int[deep]);
        Class cls = array.getClass();
        array = null;
        return cls;
    }







    public static Class forName(String name, boolean initialize, java.lang.ClassLoader loader)
    throws ClassNotFoundException {
        String addres = toAbsClassName(name);
        Class c = XClass.primitiveClassForName(addres);
        if (null != c) {
            return c;
        }
        return Class.forName(addres, initialize, loader);
    }

    /**
     * 根据类名获取Class
     * <p>
     * byte[] >> byte[].class([B)
     * <p>
     * java.lang.String[] >> [Ljava.lang.String(String[].class);
     * 
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    public static Class forName(String name) throws ClassNotFoundException {
        String addres = toAbsClassName(name);
        Class c = XClass.primitiveClassForName(addres);
        if (null != c) {
            return c;
        }
        return Class.forName(addres);
    }

    public static Class forName(ClassLoader cl, String name) throws ClassNotFoundException {
        String absClassName = toAbsClassName(name);
        Class cls = XClass.primitiveClassForName(absClassName);
        if (null != cls) {
            return cls;
        }
        if (null == cl) {
            cls = Class.forName(absClassName);
        } else {
            cls = Class.forName(absClassName, true, cl);
        }
        return cls;
    }



    public static Class getClass(Object obj) {
        return null == obj ? null : obj.getClass();
    }

    public static String getCanonicalName(Class cls) {
        return null == cls ?null: cls.getCanonicalName();
    }
    public static String getName(Class cls) {
        return null == cls ?null: cls.getName();
    }
    public static String getSimpleName(Class cls) {
        return null == cls ?null: cls.getSimpleName();
    }

    public static String joinParamJavaClassCanonicalName(Object[] objs) {
        XStringJoiner sb = new XStringJoiner(", ", "(", ")");
        if (null != objs) {
            for (Object obj : objs) {
                sb.add(null == obj ? null : obj.getClass().getCanonicalName());
            }
        }
        return sb.toString();
    }

    public static String joinParamJavaClassCanonicalName(Class[] objs) {
        XStringJoiner sb = new XStringJoiner(", ", "(", ")");
        if (null != objs) {
            for (Class cls : objs) {
                sb.add(null == cls ? null : cls.getCanonicalName());
            }
        }
        return sb.toString();
	}


}

