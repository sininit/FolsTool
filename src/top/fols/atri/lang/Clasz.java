package top.fols.atri.lang;

import top.fols.box.statics.XStaticFixedValue;

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
            return (castToClass == XStaticFixedValue.byte_class && obj instanceof Byte)
                    || (castToClass == XStaticFixedValue.long_class && obj instanceof Long)
                    || (castToClass == XStaticFixedValue.double_class && obj instanceof Double)
                    || (castToClass == XStaticFixedValue.char_class && obj instanceof Character)
                    || (castToClass == XStaticFixedValue.int_class && obj instanceof Integer)
                    || (castToClass == XStaticFixedValue.boolean_class && obj instanceof Boolean)
                    || (castToClass == XStaticFixedValue.float_class && obj instanceof Float)
                    || (castToClass == XStaticFixedValue.short_class && obj instanceof Short);
        } else {
            // if (instanceCls == XStaticFixedValue.Byte_class) return obj instanceof Byte;
            // if (instanceCls == XStaticFixedValue.Long_class) return obj instanceof Long;
            // if (instanceCls == XStaticFixedValue.Double_class) return obj instanceof
            // Double;
            // if (instanceCls == XStaticFixedValue.Character_class) return obj instanceof
            // Character;
            // if (instanceCls == XStaticFixedValue.Integer_class) return obj instanceof
            // Integer;
            // if (instanceCls == XStaticFixedValue.Boolean_class) return obj instanceof
            // Boolean;
            // if (instanceCls == XStaticFixedValue.Float_class) return obj instanceof Float;
            // if (instanceCls == XStaticFixedValue.Short_class) return obj instanceof Short;

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
            return (castToClass == XStaticFixedValue.byte_class         && objcls == XStaticFixedValue.Byte_class)
                    || (castToClass == XStaticFixedValue.long_class         && objcls == XStaticFixedValue.Long_class)
                    || (castToClass == XStaticFixedValue.double_class       && objcls == XStaticFixedValue.Double_class)
                    || (castToClass == XStaticFixedValue.char_class         && objcls == XStaticFixedValue.Character_class)
                    || (castToClass == XStaticFixedValue.int_class          && objcls == XStaticFixedValue.Integer_class)
                    || (castToClass == XStaticFixedValue.boolean_class      && objcls == XStaticFixedValue.Boolean_class)
                    || (castToClass == XStaticFixedValue.float_class        && objcls == XStaticFixedValue.Float_class)
                    || (castToClass == XStaticFixedValue.short_class        && objcls == XStaticFixedValue.Short_class);
        } else {
            if ((castToClass == XStaticFixedValue.Byte_class            && objcls == XStaticFixedValue.byte_class)
                    || (castToClass == XStaticFixedValue.Long_class         && objcls == XStaticFixedValue.long_class)
                    || (castToClass == XStaticFixedValue.Double_class       && objcls == XStaticFixedValue.double_class)
                    || (castToClass == XStaticFixedValue.Character_class    && objcls == XStaticFixedValue.char_class)
                    || (castToClass == XStaticFixedValue.Integer_class      && objcls == XStaticFixedValue.int_class)
                    || (castToClass == XStaticFixedValue.Boolean_class      && objcls == XStaticFixedValue.boolean_class)
                    || (castToClass == XStaticFixedValue.Float_class        && objcls == XStaticFixedValue.float_class)
                    || (castToClass == XStaticFixedValue.Short_class        && objcls == XStaticFixedValue.short_class)) {
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
        if (classCanonicalName.equals(XStaticFixedValue.byte_classcanonicalname)
                || classCanonicalName.equals(XStaticFixedValue.char_classcanonicalname)
                || classCanonicalName.equals(XStaticFixedValue.double_classcanonicalname)
                || classCanonicalName.equals(XStaticFixedValue.float_classcanonicalname)
                || classCanonicalName.equals(XStaticFixedValue.int_classcanonicalname)
                || classCanonicalName.equals(XStaticFixedValue.long_classcanonicalname)
                || classCanonicalName.equals(XStaticFixedValue.short_classcanonicalname)
                || classCanonicalName.equals(XStaticFixedValue.boolean_classcanonicalname)
                || classCanonicalName.equals(XStaticFixedValue.void_classcanonicalname)) {
            return true;
        } else {
            return false;
        }
    }



    public static Class primitiveClassForName(String classCanonicalName) {
        if (classCanonicalName.equals(XStaticFixedValue.byte_classcanonicalname)) {
            return XStaticFixedValue.byte_class;
        } else if (classCanonicalName.equals(XStaticFixedValue.char_classcanonicalname)) {
            return XStaticFixedValue.char_class;
        } else if (classCanonicalName.equals(XStaticFixedValue.double_classcanonicalname)) {
            return XStaticFixedValue.double_class;
        } else if (classCanonicalName.equals(XStaticFixedValue.float_classcanonicalname)) {
            return XStaticFixedValue.float_class;
        } else if (classCanonicalName.equals(XStaticFixedValue.int_classcanonicalname)) {
            return XStaticFixedValue.int_class;
        } else if (classCanonicalName.equals(XStaticFixedValue.long_classcanonicalname)) {
            return XStaticFixedValue.long_class;
        } else if (classCanonicalName.equals(XStaticFixedValue.short_classcanonicalname)) {
            return XStaticFixedValue.short_class;
        } else if (classCanonicalName.equals(XStaticFixedValue.boolean_classcanonicalname)) {
            return XStaticFixedValue.boolean_class;
        } else if (classCanonicalName.equals(XStaticFixedValue.void_classcanonicalname)) {
            return XStaticFixedValue.void_class;
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
