package top.fols.atri.reflect;


import top.fols.atri.lang.Finals;
import top.fols.box.lang.XClass;

import java.lang.reflect.*;
import java.util.Arrays;

import static top.fols.atri.reflect.ReflectCache.*;
import static top.fols.atri.lang.Finals.*;

public class ReflectPeakMatcher extends ReflectMatcher{
	public ReflectPeakMatcher(ReflectCache cache) {
		super(cache);
	}





    // * 考虑数组类型

    /**
     * @return instanceClassRootElementClass, instanceClassDeep,
     *             comparedClsRootElementClass, comparedClsDeep
     */
    private static Object[] getArrayClassMessage0(Class instanceClass, Class comparedCls) {
        int deep = 0;
        Class now = instanceClass;
        while (true) {
            Class tmp = now.getComponentType();
            if (null != tmp) {
                deep++;
                now = tmp;
            } else {
                break;
            }
        }
        int deep1 = 0;
        Class now1 = comparedCls;
        while (true) {
            Class tmp = now1.getComponentType();
            if (null != tmp) {
                deep1++;
                now1 = tmp;
            } else {
                break;
            }
        }
        return new Object[]{now, deep, now1, deep1};
    }

    //返回 instanceClass 在comparedCls中的父类等级

    //如果不是返回0
    //在此类中 instanceClass 为匹配参数,  comparedCls为Method的参数
    private static final int SL_EQUALS = -1; // class == class, true
    private static final int SL_NULLINSTANCE = SL_EQUALS - 1;//(Class)null
    private static final int SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS = SL_NULLINSTANCE - 1;// int == Integer/...
    //other
    private static final int SL_UNKNOWN = 0;
    //使用前 XClass.isInstance()结果必须为true
    //还是使用缓存比较实在
    private static long getSuperLevel0(Class instanceClass, Class comparedCls) throws RuntimeException {
        if (null == instanceClass) {
            if (null == comparedCls) {
                return SL_EQUALS;
            }
            //(comparedCls)null null可以转换为任意类型
            if (!comparedCls.isPrimitive()) {
                return SL_NULLINSTANCE;
            }
//            return SL_UNKNOWN;
            throw new RuntimeException("error calc: " + XClass.toAbsCanonicalName(instanceClass) + "  compared  " + XClass.toAbsCanonicalName(comparedCls));
        }
        if (instanceClass == comparedCls) { return SL_EQUALS; }
        if (comparedCls.isPrimitive()) {
            if (XClass.packageClassToPrimitiveClass(instanceClass) == comparedCls) {
                return SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS;
            }
            throw new RuntimeException("error calc: " + XClass.toAbsCanonicalName(instanceClass) + "  compared  " + XClass.toAbsCanonicalName(comparedCls));
        } else {
            if (instanceClass.isPrimitive()) {
                if (XClass.primitiveClassToPackageClass(instanceClass) == comparedCls) {
                    return SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS;
                }
                throw new RuntimeException("error calc: " + XClass.toAbsCanonicalName(instanceClass) + "  compared  " + XClass.toAbsCanonicalName(comparedCls));
            }
        }
        if (instanceClass.isInterface()) {
            if (OBJECT_CLASS == comparedCls) {
                /**
                 * 对比: java.lang.String >>java.lang.Object
                 * String extends Object
                 * 对比结果: (SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS -1) -1
                 *
                 * 所以应该
                 * 对比: java.lang.CharSequence >>java.lang.Object
                 * 对比结果: SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS -1 -1
                 */
                return (SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS - 1)
                        - 1;//
            }
            //接口不能实例化只可以继承
            throw new RuntimeException("instanceClass cannot be an interface: " + XClass.toAbsCanonicalName(instanceClass) + "  compared  " + XClass.toAbsCanonicalName(comparedCls));
        }


        //假设 comparedCls为Array呢
        //假设参数是int[]   方法接收参数是Object[]
        //假设参数是int[][]   方法接收参数是Object[]
        //假设参数是int[][][]   方法接收参数是Object[]
        //假设参数是int[][][]   方法接收参数是Object[][]
        if (comparedCls.isArray()) {
            Object[] ms = getArrayClassMessage0(instanceClass, comparedCls);

            Class instanceClassRoot = (Class) ms[0];
            int instanceClassDeep = (Integer) ms[1];

            Class comparedClsRoot = (Class) ms[2];
            int comparedClsDeep = (Integer) ms[3];

            //不需要考虑instanceClassRoot和comparedClsRoot不匹配的情况
            //所以不需要考虑int[]可以匹配 Integer[]之类的情况
            //所以不需要考虑Integer[]可以匹配 int[]之类的情况
            if (comparedClsRoot.isPrimitive()) {
                if (comparedClsDeep == instanceClassDeep) {
                    return SL_EQUALS;
                }
            } else {
                //因为数组也是对象
                if (OBJECT_CLASS == comparedClsRoot) {
                    if (comparedClsDeep == instanceClassDeep) {
                        return getSuperLevel0(instanceClassRoot, comparedClsRoot);
                    } else {
                        //instance为 int[][][] 方法参数为(Object[])的之类情况
                        //降维打击
                        //instanceClass - comparedClsDeep
                        //最终得到 comparedClsRoot为 Object
                        Class newInstanceClass = XClass.getDeepArrayClass(instanceClassRoot, instanceClassDeep - comparedClsDeep);
                        return getSuperLevel0(newInstanceClass, comparedClsRoot);
                    }
                } else {
                    //comparedClsDeep = instanceClassDeep
                    return getSuperLevel0(instanceClassRoot, comparedClsRoot);
                }
            }
            throw new RuntimeException("error calc: " + XClass.toAbsCanonicalName(instanceClass) + "  compared  " + XClass.toAbsCanonicalName(comparedCls));
        } else if (OBJECT_CLASS == comparedCls) {
            if (instanceClass.isArray()) {
                Object[] ms = getArrayClassMessage0(instanceClass, comparedCls);
                Class instanceClassRoot = (Class) ms[0];
                int instanceClassDeep = (Integer) ms[1];

                long level = SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS - 1;// inherit level start
                level--;//level --
                level -= instanceClassDeep;//deep difference
                level -= Math.abs(getSuperLevel0(instanceClassRoot.isPrimitive() ?XClass.primitiveClassToPackageClass(instanceClassRoot): instanceClassRoot, comparedCls));//element super level
                return level;
            }
        }



        long level = SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS;
        if (comparedCls.isInterface()) {
            //如果comparedCls为接口(interface a); 判断subClass所有父类是否存在这个接口
            Class now = instanceClass;
            boolean b = false;
            while (null != now) {
                Class[] interfaces = now.getInterfaces();
                if (null != interfaces) {
                    for (Class c: interfaces) {
                        if (c == comparedCls) {
                            b = true;
                            break;
                        }
                    }
                }
                level--;
                if (b) {return level;}
                now = now.getSuperclass();
            }
//            return SL_UNKNOWN;
            throw new RuntimeException("error calc: " + XClass.toAbsCanonicalName(instanceClass) + "  compared  " + XClass.toAbsCanonicalName(comparedCls));
        } else {
            /**
             * fuck: now: null/0
             * new: public Main(java.lang.Object[])/0
             * search=(java.lang.String[][])
             * result=Main(java.lang.Object[])
             */

            //如果comparedCls为类(Class a); 判断 comparedCls 所有继承类是否存在 subClass
            Class now = instanceClass;
            boolean b = false;
            while (null != now) {
                if (now == comparedCls) {b = true;}
                level--;
                if (b) { return level;}
                now = now.getSuperclass();
            }
//            return SL_UNKNOWN;
            throw new RuntimeException("error calc: " + XClass.toAbsCanonicalName(instanceClass) + "  compared  " + XClass.toAbsCanonicalName(comparedCls));
        }
    }
    private static long getSuperLevel(Class instanceClass, Class comparedCls) throws RuntimeException {
        // System.out.println("对比: " + XClass.toAbsCanonicalName(instanceClass) + " >>" + XClass.toAbsCanonicalName(comparedCls));
        long pm = getSuperLevel0(instanceClass, comparedCls);
        // System.out.println("对比结果: " + pm);
        return pm;
    }
    private static long getSuperLevel(Object instance, Class comparedCls) throws RuntimeException {
        return getSuperLevel(null == instance ?null: instance.getClass(), comparedCls);
    }



    private static void sortPeakList(long[] peakMods, Object[] peakList , int off, int ed) {
        int i, j;
        for (i = off; i < ed - 1; i++) {
            for (j = off; j < ed - 1 - i + off; j++) {
                if (peakMods[j] < peakMods[j + 1]) {
                    long tempp = peakMods[j];
                    peakMods[j] = peakMods[j + 1];
                    peakMods[j + 1] = tempp;

                    Object tempc = peakList[j];
                    peakList[j] = peakList[j + 1];
                    peakList[j + 1] = tempc;
                }
            }
        }
    }














	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
	public static Field matchField(Field[] list, Class returnClass, String name) {
        if (null == list) { return null; }
        for (int i = 0; i < list.length; i++) {
            Field element = list[i];
            if ((null == returnClass || returnClass == element.getType())
				&& (null == name || name.equals(element.getName()))) {
				return element;
			}
        }
        return null;
    }




    /**
     * @param listParameterTypes nullable
     */
    public static Constructor matchConstructor(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                               Class... paramClassArr) {
        if (null == list) {
            return null;
        }
        Constructor peak = null;
        long peakMod = SL_UNKNOWN;

        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
//                    System.out.println("fuck: now: " + peak + "/" + peakMod);
                    long nowPeakMod = SL_UNKNOWN;
                    for (int i3 = 0; i3 < paramClassArr.length;i3++) {
                        nowPeakMod += getSuperLevel(paramClassArr[i3], elementParameterTypes[i3]);
                    }
                    if (null == peak || (nowPeakMod != SL_UNKNOWN && nowPeakMod > peakMod)) {
                        peak = element;
                        peakMod = nowPeakMod;
//                        System.out.println("    new: " + peak + "/" + nowPeakMod);
                    }
                    if (nowPeakMod == SL_EQUALS * elementParameterTypes.length) {
//                        System.out.println("我他妈直接返回");
                        peak = element;
                        return peak;
                    }
                }
            }
        }
        return peak;
    }
    /**
     * @param listParameterTypes nullable
     */
    public static Constructor[] matchConstructors(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                  Class... paramClassArr) {
        if (null == list) {
            return EMPTY_CONSTRUCTOR_ARRAY;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return EMPTY_CONSTRUCTOR_ARRAY;
        }
        Constructor[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    if (null == peakList) {
                        peakList = new Constructor[list.length];
                        peakMods = new long[list.length];
                    }

                    long nowPeakMod = SL_UNKNOWN;
                    for (int i3 = 0; i3 < paramClassArr.length;i3++) {
                        nowPeakMod += getSuperLevel(paramClassArr[i3], elementParameterTypes[i3]);
                    }
                    if (nowPeakMod != SL_UNKNOWN) {
                        peakList[index] = element;
                        peakMods[index] = nowPeakMod;
                    } else {
                        continue;
                    }
                    index++;
                }
            }
        }
        if (null != peakList) {
            //sort array
            sortPeakList(peakMods, peakList, 0, index);
            return Arrays.copyOf(peakList, index);
        } else {
            return EMPTY_CONSTRUCTOR_ARRAY;
        }
    }





    /**
     * @param returnClass nullable
     * @param listParameterTypes nullable
     * @param name nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                     Class... paramClassArr) {
        if (null == list) {
            return null;
        }
        Method peak = null;
        long peakMod = SL_UNKNOWN;

        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length &&
                    (null == returnClass || returnClass == element.getReturnType()) &&
                    (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
//                    System.out.println("fuck: now: " + peak + "/" + peakMod);
                    long nowPeakMod = SL_UNKNOWN;
                    for (int i3 = 0; i3 < paramClassArr.length;i3++) {
                        nowPeakMod += getSuperLevel(paramClassArr[i3], elementParameterTypes[i3]);
                    }
                    if (null == peak || (nowPeakMod != SL_UNKNOWN && nowPeakMod > peakMod)) {
                        peak = element;
                        peakMod = nowPeakMod;
//                        System.out.println("    new: " + peak + "/" + nowPeakMod);
                    }
                    if (nowPeakMod == SL_EQUALS * elementParameterTypes.length) {
//                        System.out.println("我他妈直接返回");
                        peak = element;
                        return peak;
                    }
                }
            }
        }
        return peak;

    }

    /**
     * @param returnClass nullable
     * @param listParameterTypes nullable
     * @param name nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                        Class... paramClassArr) {
        if (null == list) {
            return EMPTY_METHOD_ARRAY;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return EMPTY_METHOD_ARRAY;
        }
        Method[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length &&
                    (null == returnClass || returnClass == element.getReturnType())  &&
                    (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    System.out.println(paramClassArr[i2]+"*"+elementParameterTypes[i2]+"="+XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass));
                    if (!XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    if (null == peakList) {
                        peakList = new Method[list.length];
                        peakMods = new long[list.length];
                    }

                    long nowPeakMod = SL_UNKNOWN;
                    for (int i3 = 0; i3 < paramClassArr.length;i3++) {
                        nowPeakMod += getSuperLevel(paramClassArr[i3], elementParameterTypes[i3]);
                    }
                    if (nowPeakMod != SL_UNKNOWN) {
                        peakList[index] = element;
                        peakMods[index] = nowPeakMod;
                    } else {
                        continue;
                    }
                    index++;
                }
            }
        }
        if (null != peakList) {
            //sort array
            sortPeakList(peakMods, peakList, 0, index);
            return Arrays.copyOf(peakList, index);
        } else {
            return Finals.EMPTY_METHOD_ARRAY;
        }
    }









	/**
     * matcher object type param
     */

    /**
     * @param listParameterTypes nullable
     */
    public static Constructor matchConstructor(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                               Object... paramInstanceArr) {
        if (null == list) {
            return null;
        }
        Constructor peak = null;
        long peakMod = SL_UNKNOWN;

        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
//                    System.out.println("fuck: now: " + peak + "/" + peakMod);
                    long nowPeakMod = SL_UNKNOWN;
                    for (int i3 = 0; i3 < paramInstanceArr.length;i3++) {
                        nowPeakMod += getSuperLevel(paramInstanceArr[i3], elementParameterTypes[i3]);
                    }
                    if (null == peak || (nowPeakMod != SL_UNKNOWN && nowPeakMod > peakMod)) {
                        peak = element;
                        peakMod = nowPeakMod;
//                        System.out.println("    new: " + peak + "/" + nowPeakMod);
                    }
                    if (nowPeakMod == SL_EQUALS * elementParameterTypes.length) {
//                        System.out.println("我他妈直接返回");
                        peak = element;
                        return peak;
                    }
                }
            }
        }
        return peak;
    }

    /**
     * @param listParameterTypes nullable
     */
    public static Constructor[] matchConstructors(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                  Object... paramInstanceArr) {
        if (null == list) {
            return EMPTY_CONSTRUCTOR_ARRAY;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return EMPTY_CONSTRUCTOR_ARRAY;
        }
        Constructor[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    if (null == peakList) {
                        peakList = new Constructor[list.length];
                        peakMods = new long[list.length];
                    }

                    long nowPeakMod = SL_UNKNOWN;
                    for (int i3 = 0; i3 < paramInstanceArr.length;i3++) {
                        nowPeakMod += getSuperLevel(paramInstanceArr[i3], elementParameterTypes[i3]);
                    }
                    if (nowPeakMod != SL_UNKNOWN) {
                        peakList[index] = element;
                        peakMods[index] = nowPeakMod;
                    } else {
                        continue;
                    }
                    index++;
                }
            }
        }
        if (null != peakList) {
            //sort array
            sortPeakList(peakMods, peakList, 0, index);
            return Arrays.copyOf(peakList, index);
        } else {
            return EMPTY_CONSTRUCTOR_ARRAY;
        }
    }

    /**
     * @param returnClass nullable
     * @param listParameterTypes nullable
     * @param name nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                     Object... paramInstanceArr) {
        if (null == list) {
            return null;
        }
        Method peak = null;
        long peakMod = SL_UNKNOWN;

        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length &&
                    (null == returnClass || returnClass == element.getReturnType()) &&
                    (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
//                    System.out.println("fuck: now: " + peak + "/" + peakMod);
                    long nowPeakMod = SL_UNKNOWN;
                    for (int i3 = 0; i3 < paramInstanceArr.length;i3++) {
                        nowPeakMod += getSuperLevel(paramInstanceArr[i3], elementParameterTypes[i3]);
                    }
                    if (null == peak || (nowPeakMod != SL_UNKNOWN && nowPeakMod > peakMod)) {
                        peak = element;
                        peakMod = nowPeakMod;
//                        System.out.println("    new: " + peak + "/" + nowPeakMod);
                    }
                    if (nowPeakMod == SL_EQUALS * elementParameterTypes.length) {
//                        System.out.println("我他妈直接返回");
                        peak = element;
                        return peak;
                    }
                }
            }
        }
        return peak;
    }

    /**
     * @param returnClass nullable
     * @param listParameterTypes nullable
     * @param name nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                        Object... paramInstanceArr) {
        if (null == list) {
            return EMPTY_METHOD_ARRAY;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return EMPTY_METHOD_ARRAY;
        }
        Method[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length &&
                    (null == returnClass || returnClass == element.getReturnType())  &&
                    (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    if (null == peakList) {
                        peakList = new Method[list.length];
                        peakMods = new long[list.length];
                    }

                    long nowPeakMod = SL_UNKNOWN;
                    for (int i3 = 0; i3 < paramInstanceArr.length;i3++) {
                        nowPeakMod += getSuperLevel(paramInstanceArr[i3], elementParameterTypes[i3]);
                    }
                    if (nowPeakMod != SL_UNKNOWN) {
                        peakList[index] = element;
                        peakMods[index] = nowPeakMod;
                    } else {
                        continue;
                    }
                    index++;
                }
            }
        }
        if (null != peakList) {
            //sort array
            sortPeakList(peakMods, peakList, 0, index);
            return Arrays.copyOf(peakList, index);
        } else {
            return EMPTY_METHOD_ARRAY;
        }
    }










    public static String throwNoSuchMatch(Member[] finds
										  , Class returnClass, String name,
										  Object... paramInstanceArr) {
        StringBuilder strbuf = new StringBuilder();
        strbuf.append("cannot found: ").append(null == returnClass ? "" : XClass.toAbsCanonicalName(returnClass)).append(" ")
			.append(name).append(null == finds ? "" : XClass.joinParamJavaClassCanonicalName(paramInstanceArr))
			.append("    ");
        strbuf.append("matching list: ");
        if (null == finds) {
            strbuf.append((String) null);
        } else {
			StringBuilder xsj = new StringBuilder();
            String separator = ",    ";
            for (Member constructor22 : finds) {
                xsj.append(null == constructor22 ? "null" : constructor22.toString()).append(separator);
            }
			String xsjstr = xsj.toString();
			int offset = 0; int end = (xsjstr.endsWith(separator) ?xsjstr.length() - separator.length(): xsjstr.length());
            strbuf.append(xsjstr, offset, end);
        }
        return strbuf.toString();
    }

    public static String throwNoSuchMatch(Member[] finds
										  , Class returnClass, String name,
										  Class... paramClassArr) {
        StringBuilder strbuf = new StringBuilder();
        strbuf.append("cannot found: ").append(null == returnClass ? "" : XClass.toAbsCanonicalName(returnClass)).append(" ")
			.append(name).append(null == finds ? "" : XClass.joinParamJavaClassCanonicalName(paramClassArr))
			.append("    ");
        strbuf.append("matching list: ");
        if (null == finds) {
            strbuf.append((String) null);
        } else {
            StringBuilder xsj = new StringBuilder();
            String separator = ",    ";
            for (Member constructor22 : finds) {
                xsj.append(null == constructor22 ? "null" : constructor22.toString()).append(separator);
            }
			String xsjstr = xsj.toString();
			int offset = 0; int end = (xsjstr.endsWith(separator) ?xsjstr.length() - separator.length(): xsjstr.length());
            strbuf.append(xsjstr, offset, end);
        }
        return strbuf.toString();
    }
	
	
	
	
	public ReflectCache 	cacher() { return this.cache; }
    public ReflectPeakMatcher cacherRelease() {
        this.cacher().release();
        return this;
    }





	
	
	
	
	
	
	
	
	

	/**
	 * @param type Nullable
	 * @param name Nullable
	 */
	public Field getField(Class cls, Class type, String name) throws RuntimeException {
        ReflectCache cache = this.cacher();
		FieldList gets = cache.getFieldsList(cls, name);
        if (null != gets && gets.list.length != 0) {
			Field match = matchField(gets.list(), type, name);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getFieldsList(cls).list(), null, name, (Class[]) null));
    }



	public Constructor getConstructor(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        ReflectCache cache = this.cacher();
		ConstructorList gets = cache.getConstructorsList(cls);
        if (null != gets && gets.list.length != 0) {
            Constructor match = matchConstructor(gets.list(), gets.parameterTypes(), true,
												 null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getConstructorsList(cls).list(), null, "", paramInstanceArr));
    }
    public Constructor getConstructor(Class<?> cls, Class... paramClass) throws RuntimeException {
        ReflectCache cache = this.cacher();
		ConstructorList gets = cache.getConstructorsList(cls);
        if (null != gets && gets.list.length != 0) {
            Constructor match = matchConstructor(gets.list(), gets.parameterTypes(), true,
												 null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getConstructorsList(cls).list(), null, "", paramClass));
    }



    public Constructor[] getConstructors(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        ReflectCache cache = this.cacher();
		ConstructorList gets = cache.getConstructorsList(cls);
        if (null != gets && gets.list.length != 0) {
            Constructor[] match = matchConstructors(gets.list(), gets.parameterTypes(), true,
													null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getConstructorsList(cls).list(), null, "", paramInstanceArr));
    }
    public Constructor[] getConstructors(Class<?> cls, Class... paramClass) throws RuntimeException {
        ReflectCache cache = this.cacher();
		ConstructorList gets = cache.getConstructorsList(cls);
        if (null != gets && gets.list.length != 0) {
            Constructor[] match = matchConstructors(gets.list(), gets.parameterTypes(), true,
													null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getConstructorsList(cls).list(), null, "", paramClass));
    }








	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method getMethod(Class cls, Class returnClass, String name, Object... paramInstanceArr)
	throws RuntimeException {
        ReflectCache cache = this.cacher();
		MethodList gets = cache.getMethodsList(cls, name);
        if (null != gets && gets.list.length != 0) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), returnClass, null, true, paramInstanceArr);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getMethodsList(cls).list(), returnClass, name,
													null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr));
    }
	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method getMethod(Class cls, Class returnClass, String name, Class... paramClass)
	throws RuntimeException {
        ReflectCache cache = this.cacher();
		MethodList gets = cache.getMethodsList(cls, name);
        if (null != gets && gets.list.length != 0) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), returnClass, null, true,
									   null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getMethodsList(cls).list(), returnClass, name,
													null == paramClass ? EMPTY_CLASS_ARRAY : paramClass));
    }



	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method[] getMethods(Class cls, Class returnClass, String name, Object... paramInstanceArr)
	throws RuntimeException {
        ReflectCache cache = this.cacher();
		MethodList gets = cache.getMethodsList(cls, name);
        if (null != gets && gets.list.length != 0) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), returnClass, null, true,
										  null == paramInstanceArr ? EMPTY_OBJECT_ARRAY : paramInstanceArr);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getMethodsList(cls).list(), returnClass, name,
													paramInstanceArr));
    }
	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    public Method[] getMethods(Class cls, Class returnClass, String name, Class... paramClass)
	throws RuntimeException {
        ReflectCache cache = this.cacher();
		MethodList gets = cache.getMethodsList(cls, name);
        if (null != gets && gets.list.length != 0) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), returnClass, null, true,
										  null == paramClass ? EMPTY_CLASS_ARRAY : paramClass);
            return Reflects.accessible(match);
        }
        throw new RuntimeException(throwNoSuchMatch(cache.getMethodsList(cls).list(), returnClass, name,
													paramClass));
    }





    public Object invoke(Class  cls, String name, Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return this.invoke(cls, null, null, name, args);
    }
    public Object invoke(Object object, String name, Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (null == object) { throw new NullPointerException("object"); }
        return this.invoke(object.getClass(), object, null, name, args);
    }
    public Object invoke(Class cls, Object object, Class returnClass, String name, Object... args) throws NullPointerException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method invoke = this.getMethod(null == cls ?object.getClass():cls, returnClass, name, args);
        return invoke.invoke(object, args);
    }

    public Object get(Class cls, Class returnClass, String name) throws IllegalArgumentException, IllegalAccessException { return this.get(cls, null, returnClass, name); }
    public Object get(Class cls, String name) throws IllegalArgumentException, IllegalAccessException { return this.get(cls, null, null, name); }
    public Object get(Object object, Class returnClass, String name) throws IllegalArgumentException, IllegalAccessException { if (null == object) { throw new NullPointerException("object"); } return this.get(null, object, returnClass, name); }
    public Object get(Object object, String name) throws IllegalArgumentException, IllegalAccessException { if (null == object) { throw new NullPointerException("object"); } return this.get(null, object, null, name); }
    public Object get(Class cls, Object object, Class returnClass, String name) throws IllegalArgumentException, IllegalAccessException {
        Field invoke = this.getField(null == cls ?cls = object.getClass(): cls, returnClass, name);
        return invoke.get(object);
    }

    public void set(Class cls, Class returnClass, String name, Object value) throws IllegalArgumentException, IllegalAccessException { this.set(cls, null, returnClass, name, value); }
    public void set(Class cls, String name, Object value) throws IllegalArgumentException, IllegalAccessException { this.set(cls, null, null, name, value); }
    public void set(Object object, Class returnClass, String name, Object value) throws IllegalArgumentException, IllegalAccessException { if (null == object) { throw new NullPointerException("object"); } this.set(null, object, returnClass, name, value); }
    public void set(Object object, String name, Object value) throws IllegalArgumentException, IllegalAccessException { if (null == object) { throw new NullPointerException("object"); } this.set(null, object, null, name, value); }
    public void set(Class cls, Object object, Class returnClass, String name, Object value) throws IllegalArgumentException, IllegalAccessException {
        Field invoke = this.getField(null == cls ?cls = object.getClass(): cls, returnClass, name);
        invoke.set(object, value);
    }


    public Object newInstance(Object object, Object... args) throws IllegalArgumentException, InstantiationException, NullPointerException, IllegalAccessException, InvocationTargetException  {
        if (null == object) { throw new NullPointerException("object"); }
        return this.newInstance(object.getClass(), args);
    }
    public Object newInstance(Class cls, Object... args) throws NullPointerException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (null == cls) { throw new NullPointerException("class"); }
        Constructor invoke = this.getConstructor(cls, args);
        return invoke.newInstance(args);
    }


	public ReflectPoint point(Object object) 	{ return ReflectPoint.wrapObjectOption(this, object); }
	public ReflectPoint point(Class object) 	{ return ReflectPoint.wrapStaticOption(this, object); }
	
	
	public boolean isDefault(){ return this == DEFAULT_INSTANCE; }
	public static final ReflectPeakMatcher DEFAULT_INSTANCE = new ReflectPeakMatcher(ReflectCache.DEFAULT_INSTANCE){};
}
