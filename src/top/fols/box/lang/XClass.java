package top.fols.box.lang;

import java.lang.reflect.Array;

import top.fols.box.statics.XStaticFixedValue;
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
     * @see isInstance, @param nullObjectCanCastToClass =  true
     */
    public static boolean isInstance(Class objcls, Class castToClass) {
        return XClass.isInstance(objcls, castToClass, true);
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
        if (baseClass == XStaticFixedValue.boolean_class) {
            return XStaticFixedValue.Boolean_class;
        } else if (baseClass == XStaticFixedValue.byte_class) {
            return XStaticFixedValue.Byte_class;
        } else if (baseClass == XStaticFixedValue.char_class) {
            return XStaticFixedValue.Character_class;
        } else if (baseClass == XStaticFixedValue.double_class) {
            return XStaticFixedValue.Double_class;
        } else if (baseClass == XStaticFixedValue.float_class) {
            return XStaticFixedValue.Float_class;
        } else if (baseClass == XStaticFixedValue.int_class) {
            return XStaticFixedValue.Integer_class;
        } else if (baseClass == XStaticFixedValue.long_class) {
            return XStaticFixedValue.Long_class;
        } else if (baseClass == XStaticFixedValue.short_class) {
            return XStaticFixedValue.Short_class;
        } else if (baseClass == XStaticFixedValue.void_class) {
            return XStaticFixedValue.Void_class;
        }
        return baseClass;
    }
    //将包装类转换成基础数据类型的类
    public static Class packageClassToPrimitiveClass(Class baseClass) {
        if (baseClass == XStaticFixedValue.Boolean_class) {
            return XStaticFixedValue.boolean_class;
        } else if (baseClass == XStaticFixedValue.Byte_class) {
            return XStaticFixedValue.byte_class;
        } else if (baseClass == XStaticFixedValue.Character_class) {
            return XStaticFixedValue.char_class;
        } else if (baseClass == XStaticFixedValue.Double_class) {
            return XStaticFixedValue.double_class;
        } else if (baseClass == XStaticFixedValue.Float_class) {
            return XStaticFixedValue.float_class;
        } else if (baseClass == XStaticFixedValue.Integer_class) {
            return XStaticFixedValue.int_class;
        } else if (baseClass == XStaticFixedValue.Long_class) {
            return XStaticFixedValue.long_class;
        } else if (baseClass == XStaticFixedValue.Short_class) {
            return XStaticFixedValue.short_class;
        } else if (baseClass == XStaticFixedValue.Void_class) {
            return XStaticFixedValue.void_class;
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

    private static final String CLASS_NAME_ARRAY_STATEMENT = "[";
    public static int getArrayDimensionalFromClassName(String name) {
        if (null == name || name.length() == 0) {
            return 0;
        }
        if (name.startsWith(CLASS_NAME_ARRAY_STATEMENT)) {
            return (name.lastIndexOf(CLASS_NAME_ARRAY_STATEMENT) + CLASS_NAME_ARRAY_STATEMENT.length()) / CLASS_NAME_ARRAY_STATEMENT.length();
        } else {
            return getArrayDimensionalFromClassCanonicalName(name);
        }
    }



    /**
     * You need to pay attention to anonymous classes! And inner class '$' !
     * You need to pay attention to anonymous classes! And inner class '$' !
     * You need to pay attention to anonymous classes! And inner class '$' !
     *
     * Examples:
     * toAbsClassName(String.class.getCanonicalName())
     *     java.lang.String 	returns "java.lang.String"
     * toAbsClassName(byte.class.getCanonicalName())
     *     byte 				returns "byte"
     * toAbsClassName((new Object[3]).getClass().getCanonicalName())
     *     java.lang.Object[]   returns "[Ljava.lang.Object;"
     * toAbsClassName((new int[3][4][5][6][7][8][9]).getClass().getCanonicalName())
     *     int[][][][][][][]    returns "[[[[[[[I"
     */
    public static String toAbsClassName(String absCanonicalName) {
        if (XClass.isPrimitiveClassName(absCanonicalName)) {
            return absCanonicalName;
        } else {
            int dimensional = XClass.getArrayDimensionalFromClassCanonicalName(absCanonicalName);
            String c;
            if (dimensional > 0) {
                StringBuilder prefix = new StringBuilder();
                for (int i = 0; i < dimensional; i++) {
                    prefix.append('[');
                }
                absCanonicalName = absCanonicalName.substring(0, absCanonicalName.length() - (dimensional * CLASS_CANONICAL_NAME_ARRAY_STATEMENT.length()));
                if (absCanonicalName.equals("byte")) {
                    c = "B";
                } else if (absCanonicalName.equals("char")) {
                    c = "C";
                } else if (absCanonicalName.equals("double")) {
                    c = "D";
                } else if (absCanonicalName.equals("float")) {
                    c = "F";
                } else if (absCanonicalName.equals("int")) {
                    c = "I";
                } else if (absCanonicalName.equals("long")) {
                    c = "J";
                } else if (absCanonicalName.equals("short")) {
                    c = "S";
                } else if (absCanonicalName.equals("boolean")) {
                    c = "Z";
                } else if (absCanonicalName.equals("void")) {
                    c = "V";
                } else {
                    return c = prefix.append('L').append(absCanonicalName).append(';').toString();
                }
                return prefix.append(c).toString();
            } else {
                return absCanonicalName;
            }
        }
    }


    public static String toAbsCanonicalName(String classGetName) throws RuntimeException {
        if (null == classGetName) {
            return null;
        }
        if (0 == classGetName.length()) {
            return "";
        }
        if (classGetName.startsWith(CLASS_NAME_ARRAY_STATEMENT)) {
            int typeSt = classGetName.lastIndexOf(CLASS_NAME_ARRAY_STATEMENT) + CLASS_NAME_ARRAY_STATEMENT.length();
            int dimensional = typeSt / CLASS_NAME_ARRAY_STATEMENT.length();
            char type = classGetName.charAt(typeSt);
            StringBuilder sb = new StringBuilder();
            if (type == 'B') {
                sb.append("byte");
            } else if (type == 'C') {
                sb.append("char");
            } else if (type == 'D') {
                sb.append("double");
            } else if (type == 'F') {
                sb.append("float");
            } else if (type == 'I') {
                sb.append("int");
            } else if (type == 'J') {
                sb.append("long");
            } else if (type == 'S') {
                sb.append("short");
            } else if (type == 'Z') {
                sb.append("boolean");
            } else if (type == 'V') {
                sb.append("void");
            } else if (type == 'L') {
                sb.append(classGetName.substring(typeSt + 1, classGetName.indexOf(";", typeSt + 1)));
            } else {
                throw new RuntimeException("unknown type: " + type);
            }
            for (int i = 0; i < dimensional; i++) {
                sb.append(CLASS_CANONICAL_NAME_ARRAY_STATEMENT);
            }
            return sb.toString();
        } else {
            return classGetName;
        }
    }




    public static String[] toAbsClassName(String... absCanonicalNames) {
        if (null == absCanonicalNames || absCanonicalNames.length == 0) {
            return null;
        }
        String c[] = new String[absCanonicalNames.length];
        for (int i = 0; i < absCanonicalNames.length; i++) {
            c[i] = null == absCanonicalNames[i] ? null : toAbsClassName(absCanonicalNames[i]);
        }
        return c;
    }
    public static String[] toAbsClassName(Class... absCanonicalNames) {
        if (null == absCanonicalNames || absCanonicalNames.length == 0) {
            return null;
        }
        String c[] = new String[absCanonicalNames.length];
        for (int i = 0; i < absCanonicalNames.length; i++) {
            c[i] = null == absCanonicalNames[i] ? null : absCanonicalNames[i].getName();
        }
        return c;
    }


    public static String[] toAbsCanonicalNames(String... classGetName) throws RuntimeException {
        if (null == classGetName || classGetName.length == 0) {
            return null;
        }
        String c[] = new String[classGetName.length];
        for (int i = 0; i < classGetName.length; i++) {
            c[i] = null == classGetName[i] ? null : toAbsCanonicalName(classGetName[i]);
        }
        return c;
    }


    public static String toAbsCanonicalName(Class classGetName) throws RuntimeException{
        return toAbsCanonicalName(classGetName.getName());
    }
    public static String[] toAbsCanonicalNames(Class... classGetName) throws RuntimeException{
        if (null == classGetName || classGetName.length == 0) {
            return null;
        }
        String c[] = new String[classGetName.length];
        for (int i = 0; i < classGetName.length; i++) {
            c[i] = null == classGetName[i] ? null : toAbsCanonicalName(classGetName[i]);
        }
        return c;
    }





    public static Class getDeepArrayClass(Class elementClass, int deep) {
        Object array = Array.newInstance(elementClass, new int[deep]);
        Class cls = array.getClass();
        array = null;
        return cls;
    }







    public static Class forName(String name, boolean initialize, ClassLoader loader)
            throws ClassNotFoundException {
        String addres = toAbsClassName(name);
        Class c = XClass.primitiveClassForName(addres);
        if (null != c) {
            return c;
        }
        return Class.forName(addres, initialize, loader);
    }

    /**
     * You need to pay attention to anonymous classes! And inner class '$' !
     * You need to pay attention to anonymous classes! And inner class '$' !
     * You need to pay attention to anonymous classes! And inner class '$' !
     *
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
        String addres = toAbsClassName(name);
        Class cls = XClass.primitiveClassForName(addres);
        if (null != cls) {
            return cls;
        }
        if (null == cl) {
            cls = Class.forName(addres);
        } else {
            cls = Class.forName(addres, true, cl);
        }
        return cls;
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




    public static String joinParamJavaClassName(Object[] objs) {
        XStringJoiner sb = new XStringJoiner(", ", "(", ")");
        if (null != objs) {
            for (Object obj : objs) {
                sb.add(null == obj ? null : obj.getClass().getName());
            }
        }
        return sb.toString();
    }
    public static String joinParamJavaClassCanonicalName(Object[] objs) {
        XStringJoiner sb = new XStringJoiner(", ", "(", ")");
        if (null != objs) {
            for (Object obj : objs) {
                sb.add(null == obj ? null : toAbsCanonicalName(obj.getClass()));
            }
        }
        return sb.toString();
    }

    public static String joinParamJavaClassName(Class[] objs) {
        XStringJoiner sb = new XStringJoiner(", ", "(", ")");
        if (null != objs) {
            for (Class cls : objs) {
                sb.add(null == cls ? null : cls.getName());
            }
        }
        return sb.toString();
    }
    public static String joinParamJavaClassCanonicalName(Class[] objs) {
        XStringJoiner sb = new XStringJoiner(", ", "(", ")");
        if (null != objs) {
            for (Class cls : objs) {
                sb.add(null == cls ? null : toAbsCanonicalName(cls));
            }
        }
        return sb.toString();
    }


}


