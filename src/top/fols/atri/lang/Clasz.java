package top.fols.atri.lang;


public class Clasz {
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
            return (castToClass == Finals.BYTE_CLASS && obj instanceof Byte)
                    || (castToClass == Finals.LONG_CLASS && obj instanceof Long)
                    || (castToClass == Finals.DOUBLE_CLASS && obj instanceof Double)
                    || (castToClass == Finals.CHAR_CLASS && obj instanceof Character)
                    || (castToClass == Finals.INT_CLASS && obj instanceof Integer)
                    || (castToClass == Finals.BOOLEAN_CLASS && obj instanceof Boolean)
                    || (castToClass == Finals.FLOAT_CLASS && obj instanceof Float)
                    || (castToClass == Finals.SHORT_CLASS && obj instanceof Short);
        } else {
            // if (instanceCls == Finals.Byte_class) return obj instanceof Byte;
            // if (instanceCls == Finals.Long_class) return obj instanceof Long;
            // if (instanceCls == Finals.Double_class) return obj instanceof
            // Double;
            // if (instanceCls == Finals.Character_class) return obj instanceof
            // Character;
            // if (instanceCls == Finals.Integer_class) return obj instanceof
            // Integer;
            // if (instanceCls == Finals.Boolean_class) return obj instanceof
            // Boolean;
            // if (instanceCls == Finals.Float_class) return obj instanceof Float;
            // if (instanceCls == Finals.Short_class) return obj instanceof Short;

            return castToClass.isInstance(obj);
        }
    }
    /**
     * @see #isInstance(Object, Class) , @param nullObjectCanCastToClass =  true
     */
    public static boolean isInstance(Object obj, Class castToClass) {
        return Clasz.isInstance(obj, castToClass, true);
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
     * @see #isInstance(Class, Class) , @param nullObjectCanCastToClass =  true
     */
    public static boolean isInstance(Class objcls, Class castToClass) {
        return Clasz.isInstance(objcls, castToClass, true);
    }










    public static boolean isPrimitiveClassName(String classCanonicalName) {
        if (classCanonicalName.equals(Finals.BYTE_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.CHAR_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.DOUBLE_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.FLOAT_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.INT_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.LONG_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.SHORT_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.BOOLEAN_CLASS_CANONICAL_NAME)
                || classCanonicalName.equals(Finals.VOID_CLASS_CANONICAL_NAME)) {
            return true;
        } else {
            return false;
        }
    }



    public static Class primitiveClassForName(String classCanonicalName) {
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
    public static Class primitiveClassToPackageClass(Class baseClass) {
        if (baseClass == Finals.BOOLEAN_CLASS) {
            return Finals.BOOLEAN_PACKAGE_CLASS;
        } else if (baseClass == Finals.BYTE_CLASS) {
            return Finals.BYTE_PACKAGE_CLASS;
        } else if (baseClass == Finals.CHAR_CLASS) {
            return Finals.CHAR_PACKAGE_CLASS;
        } else if (baseClass == Finals.DOUBLE_CLASS) {
            return Finals.DOUBLE_PACKAGE_CLASS;
        } else if (baseClass == Finals.FLOAT_CLASS) {
            return Finals.FLOAT_PACKAGE_CLASS;
        } else if (baseClass == Finals.INT_CLASS) {
            return Finals.INT_PACKAGE_CLASS;
        } else if (baseClass == Finals.LONG_CLASS) {
            return Finals.LONG_PACKAGE_CLASS;
        } else if (baseClass == Finals.SHORT_CLASS) {
            return Finals.SHORT_PACKAGE_CLASS;
        } else if (baseClass == Finals.VOID_CLASS) {
            return Finals.VOID_PACKAGE_CLASS;
        }
        return baseClass;
    }
    //将包装类转换成基础数据类型的类
    public static Class packageClassToPrimitiveClass(Class baseClass) {
        if (baseClass == Finals.BOOLEAN_PACKAGE_CLASS) {
            return Finals.BOOLEAN_CLASS;
        } else if (baseClass == Finals.BYTE_PACKAGE_CLASS) {
            return Finals.BYTE_CLASS;
        } else if (baseClass == Finals.CHAR_PACKAGE_CLASS) {
            return Finals.CHAR_CLASS;
        } else if (baseClass == Finals.DOUBLE_PACKAGE_CLASS) {
            return Finals.DOUBLE_CLASS;
        } else if (baseClass == Finals.FLOAT_PACKAGE_CLASS) {
            return Finals.FLOAT_CLASS;
        } else if (baseClass == Finals.INT_PACKAGE_CLASS) {
            return Finals.INT_CLASS;
        } else if (baseClass == Finals.LONG_PACKAGE_CLASS) {
            return Finals.LONG_CLASS;
        } else if (baseClass == Finals.SHORT_PACKAGE_CLASS) {
            return Finals.SHORT_CLASS;
        } else if (baseClass == Finals.VOID_PACKAGE_CLASS) {
            return Finals.VOID_CLASS;
        }
        return baseClass;
    }
    public static Class getClass(Object obj) {
        return null == obj ? null : obj.getClass();
    }
    public static String getName(Class cls) {
        return null == cls ?null: cls.getName();
    }
    public static String getSimpleName(Class cls) {
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

}
