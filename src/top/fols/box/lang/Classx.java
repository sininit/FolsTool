package top.fols.box.lang;

import java.lang.reflect.Array;

import top.fols.atri.assist.util.StringJoiner;
import top.fols.atri.lang.Classz;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Strings;

@SuppressWarnings({"rawtypes", "UnnecessaryLocalVariable", "SpellCheckingInspection", "StringOperationCanBeSimplified"})
public class Classx {
    public static String joinParamJavaClassName(Object[] objs) {
        StringJoiner sb = new StringJoiner(", ", "(", ")");
        if (null != objs) {
            for (Object obj : objs) {
                sb.add(null == obj ? null : obj.getClass().getName());
            }
        }
        return sb.toString();
    }
    public static String joinParamJavaClassCanonicalName(Object[] objs) {
        StringJoiner sb = new StringJoiner(", ", "(", ")");
        if (null != objs) {
            for (Object obj : objs) {
                sb.add(null == obj ? null : getClassGetNameToCanonicalName(obj.getClass()));
            }
        }
        return sb.toString();
    }
    public static String joinParamJavaClassName(Class[] objs) {
        StringJoiner sb = new StringJoiner(", ", "(", ")");
        if (null != objs) {
            for (Class cls : objs) {
                sb.add(null == cls ? null : cls.getName());
            }
        }
        return sb.toString();
    }
    public static String joinParamJavaClassCanonicalName(Class[] objs) {
        StringJoiner sb = new StringJoiner(", ", "(", ")");
        if (null != objs) {
            for (Class cls : objs) {
                sb.add(null == cls ? null : getClassGetNameToCanonicalName(cls));
            }
        }
        return sb.toString();
    }

















    public static String[] getArrayCanonicalNameToAbsName(String... absCanonicalNames) {
        if (null == absCanonicalNames || absCanonicalNames.length == 0) {
            return null;
        }
        String[] c = new String[absCanonicalNames.length];
        for (int i = 0; i < absCanonicalNames.length; i++) {
            c[i] = null == absCanonicalNames[i] ? null : getArrayCanonicalNameToAbsName(absCanonicalNames[i]);
        }
        return c;
    }
    public static String[] getArrayCanonicalNameToAbsName(Class... absCanonicalNames) {
        if (null == absCanonicalNames || absCanonicalNames.length == 0) {
            return null;
        }
        String[] c = new String[absCanonicalNames.length];
        for (int i = 0; i < absCanonicalNames.length; i++) {
            c[i] = null == absCanonicalNames[i] ? null : absCanonicalNames[i].getName();
        }
        return c;
    }


    public static String[] getClassGetNameToCanonicalNames(String... classGetName) throws RuntimeException {
        if (null == classGetName || classGetName.length == 0) {
            return null;
        }
        String[] c = new String[classGetName.length];
        for (int i = 0; i < classGetName.length; i++) {
            c[i] = null == classGetName[i] ? null : getClassGetNameToCanonicalName(classGetName[i]);
        }
        return c;
    }


    public static String getClassGetNameToCanonicalName(Object classGetName) throws RuntimeException{
        return null == classGetName ? String.valueOf((Object) null) : getClassGetNameToCanonicalName(classGetName.getClass());
    }
    public static String getClassGetNameToCanonicalName(Class clazz) throws RuntimeException {
        return null == clazz ? String.valueOf((Object) null)        : getClassGetNameToCanonicalName(clazz.getName());
    }
    public static String[] getClassGetNameToCanonicalNames(Class... classGetName) throws RuntimeException{
        if (null == classGetName || classGetName.length == 0) {
            return null;
        }
        String[] c = new String[classGetName.length];
        for (int i = 0; i < classGetName.length; i++) {
            c[i] = null == classGetName[i] ? null : getClassGetNameToCanonicalName(classGetName[i]);
        }
        return c;
    }





    public static Class getDeepArrayClass(Class elementClass, int deep) {
        Object array = Array.newInstance(elementClass, new int[deep]);
        Class cls = array.getClass();
        return cls;
    }








    /**
     * [Ljava.lang.Object;
     */
    public static final String java_jvm_array_prefix        = "[";
    public static final String java_array_suffix            = "[]";

    public static final char java_package_separator_char       = '.';
    public static final String java_package_separator          = ".";

    public static final char java_inner_package_separator_char = '$';
    public static final String java_inner_package_separator    = "$";





    public static int getArrayDimensionalFromClassGetCanonicalName(String name) {
        if (null == name)
            return 0;

        int index = name.indexOf(java_array_suffix); // java.lang.System[][]
        if (index == -1)
            return 0;

        if (!name.endsWith(java_array_suffix))
            return 0;

        return (name.length() - index) / java_array_suffix.length();// [][]
    }

    public static int getArrayDimensionalFromClassGetName(String name) {
        if (null == name || name.length() == 0)
            return 0;

        if (name.startsWith(java_jvm_array_prefix)) {
            return (name.lastIndexOf(java_jvm_array_prefix) + java_jvm_array_prefix.length()) / java_jvm_array_prefix.length();
        } else {
            return getArrayDimensionalFromClassGetCanonicalName(name);
        }
    }



    /**
     * You need to pay attention to anonymous classes! And inner class '$' ! <br>
     * You need to pay attention to anonymous classes! And inner class '$' ! <br>
     * You need to pay attention to anonymous classes! And inner class '$' ! <br>
     * <br>
     * Examples: <br>
     * toAbsClassName(String.class.getCanonicalName()) <br>
     *     java.lang.String 	returns "java.lang.String" <br>
     *      <br>
     * toAbsClassName(byte.class.getCanonicalName()) <br>
     *     byte 				returns "byte" <br>
     *      <br>
     *
     * toAbsClassName((new Object[3]).getClass().getCanonicalName()) <br>
     *     java.lang.Object[]   returns "[Ljava.lang.Object;" <br>
     *      <br>
     *
     * toAbsClassName((new int[3][4][5][6][7][8][9]).getClass().getCanonicalName()) <br>
     *     int[][][][][][][]    returns "[[[[[[[I" <br>
     */
    public static String getArrayCanonicalNameToAbsName(String absCanonicalName) {
        int dimensional = Classx.getArrayDimensionalFromClassGetCanonicalName(absCanonicalName);
        if (dimensional > 0) {
            StringBuilder prefix = new StringBuilder();
            for (int i = 0; i < dimensional; i++) {
                prefix.append('[');
            }
            String c;
            absCanonicalName = absCanonicalName.substring(0, absCanonicalName.length() - (dimensional * java_array_suffix.length()));
            switch (absCanonicalName) {
                case "byte":
                    c = "B";
                    break;
                case "char":
                    c = "C";
                    break;
                case "double":
                    c = "D";
                    break;
                case "float":
                    c = "F";
                    break;
                case "int":
                    c = "I";
                    break;
                case "long":
                    c = "J";
                    break;
                case "short":
                    c = "S";
                    break;
                case "boolean":
                    c = "Z";
                    break;
                case "void":
                    c = "V";
                    break;
                default:
                    return prefix.append('L').append(absCanonicalName).append(';').toString();
            }
            return prefix.append(c).toString();
        } else {
            return absCanonicalName;
        }
    }



    /**
     * {@link Class#getName()}
     * 转换为 canonical 地址
     */
    public static String getClassGetNameToCanonicalName(String classGetName) throws RuntimeException {
        if (null == classGetName)
            return null;
        if (classGetName.startsWith(java_jvm_array_prefix)) {
            int typeSt = classGetName.lastIndexOf(java_jvm_array_prefix) + java_jvm_array_prefix.length();
            int dimensional = typeSt / java_jvm_array_prefix.length();
            char type = classGetName.charAt(typeSt);
            StringBuilder sb = new StringBuilder();
            switch (type) {
                case 'B':
                    sb.append("byte");
                    break;
                case 'C':
                    sb.append("char");
                    break;
                case 'D':
                    sb.append("double");
                    break;
                case 'F':
                    sb.append("float");
                    break;
                case 'I':
                    sb.append("int");
                    break;
                case 'J':
                    sb.append("long");
                    break;
                case 'S':
                    sb.append("short");
                    break;
                case 'Z':
                    sb.append("boolean");
                    break;
                case 'V':
                    sb.append("void");
                    break;
                case 'L':
                    sb.append(classGetName.substring(typeSt + 1, classGetName.indexOf(";", typeSt + 1))
                            .replace(java_inner_package_separator, java_package_separator));
                    break;
                default:
                    throw new RuntimeException("unknown type: " + type);
            }
            for (int i = 0; i < dimensional; i++) {
                sb.append(java_array_suffix);
            }
            return sb.toString();
        } else {
            return classGetName;
        }
    }





    public static String getJavaRootPageckage() {
        Class<Object> objectClass = Finals.OBJECT_CLASS;
        String name = objectClass.getName();
        String substring = name.substring(0, name.indexOf(java_package_separator));
        return substring;
    }
    static final String JAVA_PACKAGE_PREFIX = getJavaRootPageckage() + java_package_separator;
    public static boolean isJavaPackage(String className) {
        return className.startsWith(JAVA_PACKAGE_PREFIX);
    }


    /**
     * 内部类也会返回SimleName
     */
    public static String findSimpleName(Class<?> aClass) {
        if (null == aClass)
            return null;

        if (aClass.isArray())
            return findSimpleName(aClass.getComponentType()) + java_array_suffix;

        String sn = aClass.getSimpleName();
        if (sn.length() != 0) {
            return sn;
        } else {
            final char[] separator = {java_package_separator_char, java_inner_package_separator_char};
            String name = aClass.getName();
            int index = Strings.lastIndexOfChar(name, separator);
            if (index > -1) {
                return name.substring(index + 1, name.length());
            }
            return name;
        }
    }





    /**
	 * System.out.println(XClass.forName("java.util.Map"));   <br>
     * System.out.println(XClass.forName("java.util.Map[]"));   <br>
     * System.out.println(XClass.forName("java.util.Map.Entry"));   <br>
     * System.out.println(XClass.forName("java.util.Map.Entry[]"));  <br>
     * System.out.println(XClass.forName("java.util.Map.Entry[][]"));  <br>
     * System.out.println(XClass.forName("int"));  <br>
     * System.out.println(XClass.forName("int[][]"));  <br>
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static Class<?> forName(String name) throws ClassNotFoundException {
        Class<?> c = Classz.primitiveClassForName(name);
        if (null != c)
            return  c;

        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e2) {
            //数组地址转绝对地址
            //int[] -> [I
            //java.lang.String[] -> [Ljava.lang.String;

            //去掉数组类后缀
            String className;
            String suffix;
            int d = name.indexOf(java_array_suffix);
            if (d > -1) {
                className = name.substring(0, d);
                suffix = name.substring(d, name.length());
            } else {
                className = name;
                suffix = "";
            }

            //可能的情况:
            //  内部类 $
            //  内部类数组 $
            //
            //  类数组
            //  primitive 基础类数组
            if (null != top.fols.atri.lang.Classz.primitiveClassForName(className)) {
                //primitive class array
                return Class.forName(Classx.getArrayCanonicalNameToAbsName(name));
            } else {
                //先搜索到这个类再转回数组地址
                String aClassName = className;
                Class<?> file = null;
                String fileName = null;
                int index = 0, last = 0;
				try { 
					file = Class.forName(fileName = aClassName); 
				} catch (ClassNotFoundException ignored) {}
				if (null == file) {
					while (-1 != (index = aClassName.indexOf(java_package_separator, index))) {
						String temp = aClassName.substring(0, index);
						try {
							//找到类文件 才能搜索匿名类
							file = Class.forName(fileName = temp);
							break;
						} catch (ClassNotFoundException ignored) {}
						last = index;
						index += java_package_separator.length();
					}
				} 

                //没有找到类文件
                if (null == file)
                    throw e2;

                try {
                    //找到类文件后直接尝试 将后面的地址全部替换为$ 并加上数组地址
                    return Class.forName(Classx.getArrayCanonicalNameToAbsName((fileName
																			   + aClassName.substring(last, aClassName.length()).replace(java_package_separator, java_inner_package_separator))
																			   + suffix));
                } catch (ClassNotFoundException ee) {
                    //指向的内部类可能是继承的
                    //只能一个个内部类递归查询了
                    aClassName += java_package_separator; //可能会更快 你信吗

                    Class<?> cacheClass = file;
                    last = fileName.length();
                    index = fileName.length() + java_package_separator.length();
                    while (-1 != (index = aClassName.indexOf(java_package_separator, index))) {
                        String temp = name.substring(last + java_package_separator.length(), index);

                        //自己实现搜索内部类
                        Class<?> s = null;
                        try {
                            Class tempClass = cacheClass;
                            while (null != tempClass) {
                                Class[] declaredClasses = tempClass.getDeclaredClasses();
                                for (Class temp2: declaredClasses) {
                                    if (temp.equals(findSimpleName(temp2))) {
                                        s = temp2;
                                        break;
                                    }
                                }
                                tempClass = tempClass.getSuperclass();
                            }
                        } catch (Exception ignored) {}

                        if (null == s) {
                            throw new ClassNotFoundException(cacheClass.getName() + " : " + temp);
                        } else {
                            cacheClass = s;
                        }
                        last = index;
                        index += java_package_separator.length();
                    }
                    return Class.forName(Classx.getArrayCanonicalNameToAbsName(cacheClass.getName() + suffix));
                }
            }
        }
    }

    /**
     * @param name 类名
     * @param callerClass 你从哪个地方调用了这个方法 直接传入getClass即可
     */
    public static Class<?> forName(String name, Class<?> callerClass) throws ClassNotFoundException {
        return forName(name, true, null==callerClass?null:callerClass.getClassLoader());
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static Class<?> forName(String name, boolean initialize,
                                   ClassLoader loader) throws ClassNotFoundException {
        Class<?> c = Classz.primitiveClassForName(name);
        if (null != c)
            return  c;
        try {
            return Class.forName(name, initialize, loader);
        } catch (ClassNotFoundException e2) {
            //数组地址转绝对地址
            //int[] -> [I
            //java.lang.String[] -> [Ljava.lang.String;

            //去掉数组类后缀
            String className;
            String suffix;
            int d = name.indexOf(java_array_suffix);
            if (d > -1) {
                className = name.substring(0, d);
                suffix = name.substring(d, name.length());
            } else {
                className = name;
                suffix = "";
            }

            //可能的情况:
            //  内部类 $
            //  内部类数组 $
            //
            //  类数组
            //  primitive 基础类数组
            if (null != Classz.primitiveClassForName(className)) {
                //primitive class array
                return Class.forName(Classx.getArrayCanonicalNameToAbsName(name), initialize, loader);
            } else {
                //先搜索到这个类再转回数组地址
                String aClassName = className;
                Class<?> file = null;
                String fileName = null;
                int index = 0, last = 0;
				try { 
					file = Class.forName(fileName = aClassName, initialize, loader);
				} catch (ClassNotFoundException ignored) {}
				if (null == file) {
					while (-1 != (index = aClassName.indexOf(java_package_separator, index))) {
						String temp = aClassName.substring(0, index);
						try {
							//找到类文件 才能搜索匿名类
							file = Class.forName(fileName = temp, initialize, loader);
							break;
						} catch (ClassNotFoundException ignored) {}
						last = index;
						index += java_package_separator.length();
					}
				}

                //没有找到类文件
                if (null == file)
                    throw e2;

                try {
                    //找到类文件后直接尝试 将后面的地址全部替换为$ 并加上数组地址
                    return Class.forName(Classx.getArrayCanonicalNameToAbsName((fileName
																			   + aClassName.substring(last, aClassName.length()).replace(java_package_separator, java_inner_package_separator))
																			   + suffix), initialize, loader);
                } catch (ClassNotFoundException ee) {
                    //指向的内部类可能是继承的
                    //只能一个个内部类递归查询了
                    aClassName += java_package_separator; //可能会更快 你信吗

                    Class<?> cacheClass = file;
                    last = fileName.length();
                    index = fileName.length() + java_package_separator.length();
                    while (-1 != (index = aClassName.indexOf(java_package_separator, index))) {
                        String temp = name.substring(last + java_package_separator.length(), index);

                        Class<?> s = null;
                        try {
                            Class tempClass = cacheClass;
                            while (null != tempClass) {
                                Class[] declaredClasses = tempClass.getDeclaredClasses();
                                for (Class temp2: declaredClasses) {
                                    if (temp.equals(findSimpleName(temp2))) {
                                        s = temp2;
                                        break;
                                    }
                                }
                                tempClass = tempClass.getSuperclass();
                            }
                        } catch (Exception ignored) {}

                        if (null == s) {
                            throw new ClassNotFoundException(cacheClass.getName() + " : " + temp);
                        } else {
                            cacheClass = s;
                        }
                        last = index;
                        index += java_package_separator.length();
                    }
                    return Class.forName(Classx.getArrayCanonicalNameToAbsName(cacheClass.getName() + suffix), initialize, loader);
                }
            }
        }
    }



}


