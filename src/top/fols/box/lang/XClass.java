package top.fols.box.lang;

import java.lang.reflect.Array;

import top.fols.atri.lang.Clasz;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XStringJoiner;

public class XClass {




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
        Class c = Clasz.primitiveClassForName(addres);
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
        Class c = Clasz.primitiveClassForName(addres);
        if (null != c) {
            return c;
        }
        return Class.forName(addres);
    }

    public static Class forName(ClassLoader cl, String name) throws ClassNotFoundException {
        String addres = toAbsClassName(name);
        Class cls = Clasz.primitiveClassForName(addres);
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
        if (Clasz.isPrimitiveClassName(absCanonicalName)) {
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


}


