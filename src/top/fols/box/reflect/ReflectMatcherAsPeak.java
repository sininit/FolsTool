package top.fols.box.reflect;


import top.fols.atri.lang.Classz;
import top.fols.atri.reflect.ReflectCache;
import top.fols.atri.reflect.ReflectMatcher;
import top.fols.box.lang.Classx;

import java.lang.reflect.*;
import java.util.Arrays;

import static top.fols.atri.lang.Finals.*;

@SuppressWarnings("rawtypes")
public class ReflectMatcherAsPeak<T extends ReflectCache> extends ReflectMatcher<T> {
	public ReflectMatcherAsPeak(T cache) {
		super(cache);
	}





//    // * 考虑数组类型
//
//    /**
//     * @return instanceClassRootElementClass, instanceClassDeep,
//     *             comparedClsRootElementClass, comparedClsDeep
//     */
//    private static Object[] getArrayClassMessage0(Class instanceClass, Class comparedCls) {
//        int deep = 0;
//        Class now = instanceClass;
//        while (true) {
//            Class tmp = now.getComponentType();
//            if (null != tmp) {
//                deep++;
//                now = tmp;
//            } else {
//                break;
//            }
//        }
//        int deep1 = 0;
//        Class now1 = comparedCls;
//        while (true) {
//            Class tmp = now1.getComponentType();
//            if (null != tmp) {
//                deep1++;
//                now1 = tmp;
//            } else {
//                break;
//            }
//        }
//        return new Object[]{now, deep, now1, deep1};
//    }
    // * 考虑数组类型

    //返回 instanceClass 在comparedCls中的父类等级

    //如果不是返回0
    //在此类中 instanceClass 为匹配参数,  comparedCls为Method的参数
    private static final int SL_EQUALS = -1; // class == class, true
    private static final int SL_NULLINSTANCE = SL_EQUALS - 1;//(Class)null
    private static final int SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS = SL_NULLINSTANCE - 1;// int == Integer/...
    //other
    private static final int SL_UNKNOWN = 0;
    //使用前 Clasz.isInstance()结果必须为true
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
            throw new RuntimeException("error calc: " + Classx.getClassGetNameToCanonicalName(instanceClass) + "  compared  " + Classx.getClassGetNameToCanonicalName(comparedCls));
        }
        if (instanceClass == comparedCls) { return SL_EQUALS; }
        if (comparedCls.isPrimitive()) {
            if (Classz.wrapperClassToPrimitiveClass(instanceClass) == comparedCls) {
                return SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS;
            }
            throw new RuntimeException("error calc: " + Classx.getClassGetNameToCanonicalName(instanceClass) + "  compared  " + Classx.getClassGetNameToCanonicalName(comparedCls));
        } else {
            if (instanceClass.isPrimitive()) {
                if (Classz.primitiveClassToWrapperClass(instanceClass) == comparedCls) {
                    return SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS;
                }
                throw new RuntimeException("error calc: " + Classx.getClassGetNameToCanonicalName(instanceClass) + "  compared  " + Classx.getClassGetNameToCanonicalName(comparedCls));
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
            throw new RuntimeException("instanceClass cannot be an interface: " + Classx.getClassGetNameToCanonicalName(instanceClass) + "  compared  " + Classx.getClassGetNameToCanonicalName(comparedCls));
        }


        //假设 comparedCls为Array呢
        //假设参数是int[]   方法接收参数是Object[]
        //假设参数是int[][]   方法接收参数是Object[]
        //假设参数是int[][][]   方法接收参数是Object[]
        //假设参数是int[][][]   方法接收参数是Object[][]
        if (comparedCls.isArray()) {
//            Object[] ms = getArrayClassMessage0(instanceClass, comparedCls);
//            Class instanceClassRoot = (Class) ms[0];
//            int instanceClassDeep = (Integer) ms[1];
//            Class comparedClsRoot = (Class) ms[2];
//            int comparedClsDeep = (Integer) ms[3];
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
            Class instanceClassRoot = now;
            int instanceClassDeep = deep;
            Class comparedClsRoot = now1;
            int comparedClsDeep = deep1;

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
                        Class newInstanceClass = Classx.getDeepArrayClass(instanceClassRoot, instanceClassDeep - comparedClsDeep);
                        return getSuperLevel0(newInstanceClass, comparedClsRoot);
                    }
                } else {
                    //comparedClsDeep = instanceClassDeep
                    return getSuperLevel0(instanceClassRoot, comparedClsRoot);
                }
            }
            throw new RuntimeException("error calc: " + Classx.getClassGetNameToCanonicalName(instanceClass) + "  compared  " + Classx.getClassGetNameToCanonicalName(comparedCls));
        } else if (OBJECT_CLASS == comparedCls) {
            if (instanceClass.isArray()) {
//                Object[] ms = getArrayClassMessage0(instanceClass, comparedCls);
//                Class instanceClassRoot = (Class) ms[0];
//                int instanceClassDeep = (Integer) ms[1];
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
                Class instanceClassRoot = now;
                int   instanceClassDeep = deep;

                long level = SL_PRIMITIVE_AND_PACKAGECLASS_EQUALS - 1;// inherit level start
                level--;//level --
                level -= instanceClassDeep;//deep difference
                level -= Math.abs(getSuperLevel0(instanceClassRoot.isPrimitive() ? Classz.primitiveClassToWrapperClass(instanceClassRoot): instanceClassRoot, comparedCls));//element super level
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
                for (Class c: interfaces) {
                    if (c == comparedCls) {
                        b = true;
                        break;
                    }
                }
                level--;
                if (b) {return level;}
                now = now.getSuperclass();
            }
//            return SL_UNKNOWN;
            throw new RuntimeException("error calc: " + Classx.getClassGetNameToCanonicalName(instanceClass) + "  compared  " + Classx.getClassGetNameToCanonicalName(comparedCls));
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
            throw new RuntimeException("error calc: " + Classx.getClassGetNameToCanonicalName(instanceClass) + "  compared  " + Classx.getClassGetNameToCanonicalName(comparedCls));
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
    @Override
	public Field matchField(Field[] list, Class returnClass, String name) {
        for (int i = 0; i < list.length; i++) {
            Field element = list[i];
            if ((null == returnClass || returnClass == element.getType())
				&& (null == name || name.equals(element.getName()))) {
				return element;
			}
        }
        return null;
    }




    @Override
    public Constructor matchConstructorArgumentsTypes(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                      Class... paramClassArr) {
        Constructor peak = null;
        long peakMod = SL_UNKNOWN;

        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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
    @Override
    public Constructor[] matchConstructorsArgumentsTypes(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                         Class... paramClassArr) {
        int forLength = list.length;
        if (forLength == 0) {
            return null;
        }
        Constructor[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forLength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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
            return null;
        }
    }





    /**
     * @param returnClass nullable
     * @param name nullable
     */
    @Override
    public Method matchMethodArgumentsTypes(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                            Class... paramClassArr) {
        Method peak = null;
        long peakMod = SL_UNKNOWN;

        int forLength = list.length;
        for (int i = 0; i < forLength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length &&
                    (null == returnClass || returnClass == element.getReturnType()) &&
                    (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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
     * @param name nullable
     */
    @Override
    public Method[] matchMethodsArgumentsTypes(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                               Class... paramClassArr) {
        int forLength = list.length;
        if (forLength == 0) {
            return null;
        }
        Method[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forLength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length &&
                    (null == returnClass || returnClass == element.getReturnType())  &&
                    (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    System.out.println(paramClassArr[i2]+"*"+elementParameterTypes[i2]+"="+ Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass));
                    if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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
            return null;
        }
    }









	/* matcher object type param */

    @Override
    public Constructor matchConstructorArguments(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                 Object... paramInstanceArr) {
        Constructor peak = null;
        long peakMod = SL_UNKNOWN;

        int forLength = list.length;
        for (int i = 0; i < forLength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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

    @Override
    public Constructor[] matchConstructorsArguments(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                    Object... paramInstanceArr) {
        int forLength = list.length;
        if (forLength == 0) {
            return null;
        }
        Constructor[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forLength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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
            return null;
        }
    }

    /**
     * @param returnClass nullable
     * @param name nullable
     */
    @Override
    public Method matchMethodArguments(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                       Object... paramInstanceArr) {
        Method peak = null;
        long peakMod = SL_UNKNOWN;

        int forLength = list.length;
        for (int i = 0; i < forLength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length &&
                    (null == returnClass || returnClass == element.getReturnType()) &&
                    (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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
     * @param name nullable
     */
    @Override
    public Method[] matchMethodsArguments(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                          Object... paramInstanceArr) {
        int forLength = list.length;
        if (forLength == 0) {
            return null;
        }
        Method[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forLength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length &&
                    (null == returnClass || returnClass == element.getReturnType())  &&
                    (null == name || name.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
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
            return null;
        }
    }



    //****************************

    @Override  public T 	            cacher() { return super.cacher(); }


    //****************************


    @Override
    public boolean isDefault() { return this == DEFAULT; }
	public static final ReflectMatcherAsPeak<ReflectCache> DEFAULT = new ReflectMatcherAsPeak<ReflectCache>(ReflectCache.DEFAULT){};
}
