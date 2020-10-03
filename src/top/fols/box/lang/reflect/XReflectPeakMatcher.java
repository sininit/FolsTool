package top.fols.box.lang.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import top.fols.box.lang.XClass;
import top.fols.box.lang.reflect.XReflectCache.ConstructorList;
import top.fols.box.lang.reflect.XReflectCache.MethodList;
import top.fols.box.lang.reflect.optdeclared.XReflectAccessible;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XObjects;

/**
 * 排序匹配度最佳返回结果
 */
public class XReflectPeakMatcher extends XReflectMatcher {



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
            if (XStaticFixedValue.Object_class == comparedCls) {
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
                if (XStaticFixedValue.Object_class == comparedClsRoot) {
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
        } else if (XStaticFixedValue.Object_class == comparedCls) {
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
     * matcher Class type param 
     */

    /**
     * @param paramTypeClass nullable
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
     * @param paramTypeClass nullable
     */
    public static Constructor[] matchConstructors(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                  Class... paramClassArr) {
        if (null == list) {
            return XStaticFixedValue.nullConstructorArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullConstructorArray;
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
            return XStaticFixedValue.nullConstructorArray;
        }
    }








    /**
     * @param returnClass nullable
     * @param listParameterTypes nullable
     * @param methodName nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, Class returnClass, String methodName, boolean nullObjectCanCastToClass,
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
                (null == methodName || methodName.equals(element.getName()))) {
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
     * @param methodName nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, Class returnClass, String methodName, boolean nullObjectCanCastToClass, 
                                        Class... paramClassArr) {
        if (null == list) {
            return XStaticFixedValue.nullMethodArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullMethodArray;
        }
        Method[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length && 
                (null == returnClass || returnClass == element.getReturnType())  && 
                (null == methodName || methodName.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
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
            return XStaticFixedValue.nullMethodArray;
        }
    }











    /**
     * matcher object type param 
     */

    /**
     * @param paramTypeClass nullable
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
     * @param paramTypeClass nullable
     */
    public static Constructor[] matchConstructors(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                  Object... paramInstanceArr) {
        if (null == list) {
            return XStaticFixedValue.nullConstructorArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullConstructorArray;
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
            return XStaticFixedValue.nullConstructorArray;
        }
    }

    /**
     * @param returnClass nullable
     * @param listParameterTypes nullable
     * @param methodName nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, Class returnClass, String methodName, boolean nullObjectCanCastToClass,
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
                (null == methodName || methodName.equals(element.getName()))) {
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
     * @param methodName nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, Class returnClass, String methodName, boolean nullObjectCanCastToClass, 
                                        Object... paramInstanceArr) {
        if (null == list) {
            return XStaticFixedValue.nullMethodArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullMethodArray;
        }
        Method[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length && 
                (null == returnClass || returnClass == element.getReturnType())  && 
                (null == methodName || methodName.equals(element.getName()))) {
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
            return XStaticFixedValue.nullMethodArray;
        }
    }






















    /******* Class == Class time-consuming ********/
    /******* Copy matchMethod remove returnClass validation ********/
    /**
     * @param returnClass nullable
     * @param listParameterTypes nullable
     * @param methodName nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, String methodName, boolean nullObjectCanCastToClass,
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
                (null == methodName || methodName.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
                    if (!XClass.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    // System.out.println("fuck: now: " + peak + "/" + peakMod);
                    long nowPeakMod = SL_UNKNOWN;
                    for (int i3 = 0; i3 < paramClassArr.length;i3++) {
                        nowPeakMod += getSuperLevel(paramClassArr[i3], elementParameterTypes[i3]);
                    }
                    if (null == peak || (nowPeakMod != SL_UNKNOWN && nowPeakMod > peakMod)) {
                        peak = element;
                        peakMod = nowPeakMod;
                        // System.out.println("    new: " + peak + "/" + nowPeakMod);
                    }
                    if (nowPeakMod == SL_EQUALS * elementParameterTypes.length) {
                        // System.out.println("我他妈直接返回");
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
     * @param methodName nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, String methodName, boolean nullObjectCanCastToClass, 
                                        Class... paramClassArr) {
        if (null == list) {
            return XStaticFixedValue.nullMethodArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullMethodArray;
        }
        Method[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length  && 
                (null == methodName || methodName.equals(element.getName()))) {
                boolean b = true;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
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
            return XStaticFixedValue.nullMethodArray;
        }
    }
    /**
     * @param returnClass nullable
     * @param listParameterTypes nullable
     * @param methodName nullable
     */
    public static Method matchMethod(Method[] list, Class[][] listParameterTypes, String methodName, boolean nullObjectCanCastToClass,
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
                (null == methodName || methodName.equals(element.getName()))) {
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
     * @param methodName nullable
     */
    public static Method[] matchMethods(Method[] list, Class[][] listParameterTypes, String methodName, boolean nullObjectCanCastToClass, 
                                        Object... paramInstanceArr) {
        if (null == list) {
            return XStaticFixedValue.nullMethodArray;
        }
        int forlength = list.length;
        if (forlength == 0) {
            return XStaticFixedValue.nullMethodArray;
        }
        Method[] peakList = null;
        long[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forlength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = null == listParameterTypes ? element.getParameterTypes() : listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length  && 
                (null == methodName || methodName.equals(element.getName()))) {
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
            return XStaticFixedValue.nullMethodArray;
        }
    }

    /******* delete ReturnType ********/













    public static final XReflectPeakMatcher defaultInstance = new XReflectPeakMatcher(XReflectCacheFast.defaultInstance);

    public XReflectPeakMatcher() {
        this(null);
    }

    public XReflectPeakMatcher(XReflectCache cacher) {
        this.rc = null == cacher ? new XReflectCacheFast() : cacher;
    }




    /**
     * fast cache
     */
    protected XReflectCache rc;

    public static RuntimeException newThrowNoSuchMatchException(Member[] list, Class returnClass, String name,
                                                                Object... paramInstanceArr) {
        return XReflectMatcher.newThrowNoSuchMatchException(list, returnClass, name, paramInstanceArr);
    }

    public static RuntimeException newThrowNoSuchMatchException(Member[] list, Class returnClass, String name, 
                                                                Class... paramClassArr) {
        return XReflectMatcher.newThrowNoSuchMatchException(list, returnClass, name, paramClassArr);
    }

    @Override
    public XReflectCache getCacher() {
        return this.rc;
    }

    @Override
    public XReflectMatcher clearCacher() {
        this.rc.clear();
        return this;
    }

    /** extension option */
    /**
     * @see @return
     */
    @Override
    public XReflectPoint newPoint(Class<?> cls) {
        return new XReflectPoint(new XReflectPoint.StaticOption(cls), this);
    }

    /**
     * @see @return
     */
    @Override
    public XReflectPoint newPoint(Object obj) {
        return new XReflectPoint(obj, this);
	}




    public Constructor getConstructor(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        ConstructorList gets = this.rc.getConstructors0(cls);
        if (!XObjects.isEmpty(gets)) {
            Constructor match = matchConstructor(gets.list(), gets.parameterTypes(), true,
                null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        ConstructorList throwList = this.rc.getConstructors0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, "", paramInstanceArr);
    }
    public Constructor getConstructor(Class<?> cls, Class... paramClass) throws RuntimeException {
        ConstructorList gets = this.rc.getConstructors0(cls);
        if (!XObjects.isEmpty(gets)) {
            Constructor match = matchConstructor(gets.list(), gets.parameterTypes(), true,
                null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        ConstructorList throwList = this.rc.getConstructors0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, "", paramClass);
    }



    public Constructor[] getConstructors(Class<?> cls, Object... paramInstanceArr) throws RuntimeException {
        ConstructorList gets = this.rc.getConstructors0(cls);
        if (!XObjects.isEmpty(gets)) {
            Constructor[] match = matchConstructors(gets.list(), gets.parameterTypes(), true,
                null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        ConstructorList throwList = this.rc.getConstructors0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, "", paramInstanceArr);
    }
    public Constructor[] getConstructors(Class<?> cls, Class... paramClass) throws RuntimeException {
        ConstructorList gets = this.rc.getConstructors0(cls);
        if (!XObjects.isEmpty(gets)) {
            Constructor[] match = matchConstructors(gets.list(), gets.parameterTypes(), true,
                null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        ConstructorList throwList = this.rc.getConstructors0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, "", paramClass);
    }



    public Field getField(Class cls, String name) throws RuntimeException {
        Field match = this.rc.getField(cls, name);
        if (!XObjects.isEmpty(match)) {
            return XReflectAccessible.setAccessibleTrueOne(match);
        }
        throw newThrowNoSuchMatchException(this.rc.getFields0(cls).list(), null, name, (Class[]) null);
    }



    public Method getMethod(Class cls, String methodName, Object... paramInstanceArr) throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), null, true,
                null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, methodName,
            paramInstanceArr);
    }
    public Method getMethod(Class cls, Class returnClass, String methodName, Object... paramInstanceArr)
    throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), returnClass, null, true, paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), returnClass, methodName,
            null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
    }




    public Method getMethod(Class cls, String methodName, Class... paramClass) throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), null, true,
                null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, methodName, paramClass);
    }
    public Method getMethod(Class cls, Class returnClass, String methodName, Class... paramClass)
    throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method match = matchMethod(gets.list(), gets.parameterTypes(), returnClass, null, true,
                null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrueOne(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), returnClass, methodName,
            paramClass);
    }



    public Method[] getMethods(Class cls, String methodName, Object... paramInstanceArr) throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), null, true,
                null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, methodName,
            paramInstanceArr);
    }
    public Method[] getMethods(Class cls, Class returnClass, String methodName, Object... paramInstanceArr)
    throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), returnClass, null, true,
                null == paramInstanceArr ? XStaticFixedValue.nullObjectArray : paramInstanceArr);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), returnClass, methodName,
            paramInstanceArr);
    }



    public Method[] getMethods(Class cls, String methodName, Class... paramClass) throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), null, true,
                null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), null, methodName, paramClass);
    }
    public Method[] getMethods(Class cls, Class returnClass, String methodName, Class... paramClass)
    throws RuntimeException {
        MethodList gets = this.rc.getMethods0(cls, methodName);
        if (!XObjects.isEmpty(gets)) {
            Method[] match = matchMethods(gets.list(), gets.parameterTypes(), returnClass, null, true,
                null == paramClass ? XStaticFixedValue.nullClassArray : paramClass);
            if (!XObjects.isEmpty(match)) {
                return XReflectAccessible.setAccessibleTrue(match);
            }
        }
        MethodList throwList = this.rc.getMethods0(cls);
        throw newThrowNoSuchMatchException(null == throwList ? null : throwList.list(), returnClass, methodName,
            paramClass);
    }












    public Object newInstance(Class cls, Object... paramInstanceArr) throws InvocationTargetException,
    InstantiationException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        Constructor con = this.getConstructor(cls, paramInstanceArr);
        return con.newInstance(paramInstanceArr);
    }

    public Object newInstance(Object instance, Object... paramInstanceArr) throws InvocationTargetException,
    InstantiationException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        Constructor con = this.getConstructor(instance.getClass(), paramInstanceArr);
        return con.newInstance(paramInstanceArr);
    }

    public Object getFieldValue(Class instanceCls, Object instance, String name)
    throws IllegalArgumentException, IllegalAccessException, RuntimeException {
        Field field = this.getField(instanceCls, name);
        return field.get(instance);
    }

    public Object setFieldValue(Class instanceCls, Object instance, String name, Object newValue)
    throws IllegalArgumentException, IllegalAccessException, RuntimeException {
        Field field = this.getField(instanceCls, name);
        field.set(instance, newValue);
        return newValue;
    }

    public Object getStaticFieldValue(Class instanceCls, String name)
    throws IllegalArgumentException, IllegalAccessException, RuntimeException {
        return this.getFieldValue(instanceCls, null, name);
    }

    public Object setStaticFieldValue(Class instanceCls, String name, Object newValue)
    throws IllegalArgumentException, IllegalAccessException, RuntimeException {
        return this.setFieldValue(instanceCls, null, name, newValue);
    }

    public Object execMethod(Class instanceCls, Object instance, String method, Object... paramInstanceArr)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        Method con = this.getMethod(instanceCls, method, paramInstanceArr);
        return con.invoke(instance, paramInstanceArr);
    }

    public Object execMethod(Object instance, String method, Object... paramInstanceArr)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        return execMethod(instance.getClass(), instance, method, paramInstanceArr);
    }

    public Object execStaticMethod(Class cls, String method, Object... paramInstanceArr)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        return execMethod(cls, null, method, paramInstanceArr);
    }

    public Object execStaticMethod(Object instance, String method, Object... paramInstanceArr)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, RuntimeException {
        return execMethod(instance.getClass(), null, method, paramInstanceArr);
    }




}
