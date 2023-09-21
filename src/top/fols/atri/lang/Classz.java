package top.fols.atri.lang;


import top.fols.box.lang.GenericTypes;

import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
public class Classz {
    /**
     * (castToClass)obj
     * @param nullable >> (castToClass)null
     * @return @param obj can cast to @param castToClass
     */
    public static boolean isInstance(Object obj, Class castToClass, boolean nullable) {
        // object auto encapsulate / @param obj class >> package class

        if (null == castToClass) {
            return null == obj;
        }
        if (null == obj) {
            return nullable && !castToClass.isPrimitive();//null can be converted to any package class ((?)null)
        }
        if (castToClass.isPrimitive()) {
            return (castToClass == Finals.BYTE_CLASS        && obj instanceof Byte)
                    || (castToClass == Finals.LONG_CLASS    && obj instanceof Long)
                    || (castToClass == Finals.DOUBLE_CLASS  && obj instanceof Double)
                    || (castToClass == Finals.CHAR_CLASS    && obj instanceof Character)
                    || (castToClass == Finals.INT_CLASS     && obj instanceof Integer)
                    || (castToClass == Finals.BOOLEAN_CLASS && obj instanceof Boolean)
                    || (castToClass == Finals.FLOAT_CLASS   && obj instanceof Float)
                    || (castToClass == Finals.SHORT_CLASS   && obj instanceof Short);
        } else {
            // if (instanceCls == Finals.Byte_class) return obj instanceof Byte;
            // if (instanceCls == Finals.Long_class) return obj instanceof Long;
            // if (instanceCls == Finals.Double_class) return obj instanceof Double;
            // if (instanceCls == Finals.Character_class) return obj instanceof Character;
            // if (instanceCls == Finals.Integer_class) return obj instanceof Integer;
            // if (instanceCls == Finals.Boolean_class) return obj instanceof Boolean;
            // if (instanceCls == Finals.Float_class) return obj instanceof Float;
            // if (instanceCls == Finals.Short_class) return obj instanceof Short;
            return castToClass.isAssignableFrom(obj.getClass());
        }
    }
    /**
     * @see #isInstanceNullable(Object, Class) , @param nullObjectCanCastToClass =  true
     */
    public static boolean isInstanceNullable(Object obj, Class castToClass) {
        return Classz.isInstance(obj, castToClass, true);
    }





    /**
     * (castToClass)obj
     * @param nullable >> (castToClass)null
     * @return @param objcls can cast to @param castToClass
     */
    public static boolean isInstance(Class<?> objcls, Class<?> castToClass, boolean nullable) {
        if (objcls == castToClass) {
            return true;
        }

        if (null == castToClass) {
            return false; // objcls == castToClass is false, so null == objcls is false
        }
        if (null == objcls) {
            return nullable && !castToClass.isPrimitive();//null can be converted to any package class ((?)null)
        }
        if (castToClass.isPrimitive()) {
            return (castToClass == Finals.BYTE_CLASS && objcls == Finals.BYTE_PACKAGE_CLASS)
                    || (castToClass == Finals.LONG_CLASS && objcls == Finals.LONG_PACKAGE_CLASS)
                    || (castToClass == Finals.DOUBLE_CLASS && objcls == Finals.DOUBLE_PACKAGE_CLASS)
                    || (castToClass == Finals.CHAR_CLASS && objcls == Finals.CHAR_PACKAGE_CLASS)
                    || (castToClass == Finals.INT_CLASS && objcls == Finals.INT_PACKAGE_CLASS)
                    || (castToClass == Finals.BOOLEAN_CLASS && objcls == Finals.BOOLEAN_PACKAGE_CLASS)
                    || (castToClass == Finals.FLOAT_CLASS && objcls == Finals.FLOAT_PACKAGE_CLASS)
                    || (castToClass == Finals.SHORT_CLASS && objcls == Finals.SHORT_PACKAGE_CLASS);
        } else {
            if ((castToClass == Finals.BYTE_PACKAGE_CLASS && objcls == Finals.BYTE_CLASS)
                    || (castToClass == Finals.LONG_PACKAGE_CLASS && objcls == Finals.LONG_CLASS)
                    || (castToClass == Finals.DOUBLE_PACKAGE_CLASS && objcls == Finals.DOUBLE_CLASS)
                    || (castToClass == Finals.CHAR_PACKAGE_CLASS && objcls == Finals.CHAR_CLASS)
                    || (castToClass == Finals.INT_PACKAGE_CLASS && objcls == Finals.INT_CLASS)
                    || (castToClass == Finals.BOOLEAN_PACKAGE_CLASS && objcls == Finals.BOOLEAN_CLASS)
                    || (castToClass == Finals.FLOAT_PACKAGE_CLASS && objcls == Finals.FLOAT_CLASS)
                    || (castToClass == Finals.SHORT_PACKAGE_CLASS && objcls == Finals.SHORT_CLASS)) {
                return true;
            } else {
                return castToClass.isAssignableFrom(objcls);
            }
        }
    }
    /**
     * @see #isInstanceNullable(Class, Class) , @param nullObjectCanCastToClass =  true
     */
    public static boolean isInstanceNullable(Class objcls, Class castToClass) {
        return Classz.isInstance(objcls, castToClass, true);
    }





    public static Class    getClass(Object obj) {
        return null == obj ? null : obj.getClass();
    }
    public static String   getName(Object cls) {
        return null == cls ?null: cls.getClass().getName();
    }
    public static String   getName(Class cls) {
        return null == cls ?null: cls.getName();
    }
    public static String[] getName(Class[] classes) {
        String[] names = new String[classes.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = null == classes[i] ?null: classes[i].getName();
        }
        return names;
    }
    public static String   getSimpleName(Class cls) {
        return null == cls ?null: cls.getSimpleName();
    }

    public static boolean isString(Object object)       { return object instanceof String; }
    public static boolean isChar(Object object)         { return object instanceof Character; }
    public static boolean isBoolean(Object object)      { return object instanceof Boolean; }
    public static boolean isByte(Object object)         { return object instanceof Byte; }
    public static boolean isInt(Object object)          { return object instanceof Integer; }
    public static boolean isLong(Object object)         { return object instanceof Long; }
    public static boolean isShort(Object object)        { return object instanceof Short; }
    public static boolean isDouble(Object object)       { return object instanceof Double; }
    public static boolean isFloat(Object object)        { return object instanceof Float; }














    public static boolean isPrimitiveClassName(String classCanonicalName) {
        return     classCanonicalName.equals(Finals.BYTE_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.CHAR_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.DOUBLE_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.FLOAT_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.INT_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.LONG_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.SHORT_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.BOOLEAN_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.VOID_CLASS_CANONICAL_NAME);
    }



    public static Class<?> primitiveClassForName(String classCanonicalName) {
        if (classCanonicalName.equals(Finals.BYTE_CLASS_CANONICAL_NAME)) {
            return Finals.BYTE_CLASS;
        } else if (classCanonicalName.equals(Finals.CHAR_CLASS_CANONICAL_NAME)) {
            return Finals.CHAR_CLASS;
        } else if (classCanonicalName.equals(Finals.DOUBLE_CLASS_CANONICAL_NAME)) {
            return Finals.DOUBLE_CLASS;
        } else if (classCanonicalName.equals(Finals.FLOAT_CLASS_CANONICAL_NAME)) {
            return Finals.FLOAT_CLASS;
        } else if (classCanonicalName.equals(Finals.INT_CLASS_CANONICAL_NAME)) {
            return Finals.INT_CLASS;
        } else if (classCanonicalName.equals(Finals.LONG_CLASS_CANONICAL_NAME)) {
            return Finals.LONG_CLASS;
        } else if (classCanonicalName.equals(Finals.SHORT_CLASS_CANONICAL_NAME)) {
            return Finals.SHORT_CLASS;
        } else if (classCanonicalName.equals(Finals.BOOLEAN_CLASS_CANONICAL_NAME)) {
            return Finals.BOOLEAN_CLASS;
        } else if (classCanonicalName.equals(Finals.VOID_CLASS_CANONICAL_NAME)) {
            return Finals.VOID_CLASS;
        }
        return null;
    }






    //基础数据类型转换为包装类
    public static Class<?> primitiveClassToWrapperClass(Class<?> baseClass) {
        return Boxing.toWrapperType(baseClass);
    }
    //将包装类转换成基础数据类型的类
    public static Class<?> wrapperClassToPrimitiveClass(Class<?> baseClass) {
        return Boxing.toPrimitiveType(baseClass);
    }






    public static Class getArraySuperclass(Class clszz) {
        if (clszz.isArray()) {
            int dimension = 0;
            Class rootClass = null;
            while (null != (clszz = clszz.getComponentType())) {
                rootClass = clszz;
                dimension++;
            }
            Class superclass = rootClass.getSuperclass();
            if (null == superclass) {
                if (rootClass == Finals.OBJECT_CLASS) { // Object[] -> Object
                    return       Finals.OBJECT_CLASS;
                }
            }
            return Arrayz.dimensionClass(superclass, dimension);
        } else {
            return clszz.getSuperclass();
        }
    }
    public static Class[] getArrayInterfaces(Class clszz) {
        if (clszz.isArray()) {
            int dimension = 0;
            Class component = null;
            while (null != (clszz = clszz.getComponentType())) {
                component = clszz;
                dimension++;
            }
            Class[] interfaces = component.getInterfaces();
            for (int i = 0; i < interfaces.length;i++) {
                interfaces[i] = Arrayz.dimensionClass(interfaces[i], dimension);
            }
            return interfaces;
        } else {
            return clszz.getInterfaces();
        }
    }



    public static Type getArrayGenericSuperclass(Class clszz) {
        if (clszz.isArray()) {
            Class rootClass = Arrayz.getRootComponentType(clszz);
            int dimension   = Arrayz.dimension(clszz);
            Type superclass = rootClass.getGenericSuperclass();
            if (null == superclass) {
                if (rootClass == Finals.OBJECT_CLASS) { // Object[] -> Object
                    return       Finals.OBJECT_CLASS;
                }
            }
            Type   result = GenericTypes.dimensionType(superclass, dimension);
            return result;
        } else {
            return clszz.getGenericSuperclass();
        }
    }
    public static Type[] getArrayGenericInterfaces(Class clszz) {
        if (clszz.isArray()) {
            Class  rootClass = Arrayz.getRootComponentType(clszz);
            int dimension    = Arrayz.dimension(clszz);
            Type[] interfaces = rootClass.getGenericInterfaces();
            for (int i = 0; i < interfaces.length;i++) {
                interfaces[i] = GenericTypes.dimensionType(interfaces[i], dimension);
            }
            return interfaces;
        } else {
            return clszz.getGenericInterfaces();
        }
    }
}
