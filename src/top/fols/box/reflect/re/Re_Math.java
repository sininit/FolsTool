package top.fols.box.reflect.re;

import top.fols.atri.lang.Mathz;
import top.fols.atri.lang.Strings;
import top.fols.box.lang.Arrayy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static top.fols.box.reflect.re.Re_Variable.FALSE;
import static top.fols.box.reflect.re.Re_Variable.TRUE;

import static top.fols.box.reflect.re.Re_Keywords.*;

@SuppressWarnings({"UnnecessaryUnboxing", "rawtypes"})
public class Re_Math {


    /**
     * 由代码加载器自动转换 表达式 为 方法
     * 比如 1 + 1 代码加载器应该转换为+(1, 1)
     * 只需要将{@link Re_Math#_addToKeyword(Re_IRe_VariableMap)} 添加到keywords里即可实现表达式！
     */
    public static Collection<String> getAutomaticConversionOperator() {
        List<String> ks = new ArrayList<>();
        ks.add(INNER_MATH_FUNCTION__AND);		            //& hash2_
        ks.add(INNER_MATH_FUNCTION__OR);		            //| hash3_

        ks.add(INNER_MATH_FUNCTION__XOR);		            // ^ hash4_
        ks.add(INNER_MATH_FUNCTION__SHIFT_RIGHT);           // >> hash5_
        ks.add(INNER_MATH_FUNCTION__SHIFT_LEFT);	        // << hash6_
        ks.add(INNER_MATH_FUNCTION__UNSIGNED_SHIFT_RIGHT);	//>>> hash7_

        ks.add(INNER_MATH_FUNCTION__ADD);		    // + hash10_
        ks.add(INNER_MATH_FUNCTION__SUBTRACT);	    // - hash11_
        ks.add(INNER_MATH_FUNCTION__MULTIPLY);	    // * hash12_
        ks.add(INNER_MATH_FUNCTION__DIVICE);		// / hash13_
        ks.add(INNER_MATH_FUNCTION__MOD);		    // % hash14_


        //对比符 不会产生新数据
        ks.add(INNER_MATH_FUNCTION__AAND);          //&&
        ks.add(INNER_MATH_FUNCTION__OOR);           //||

        ks.add(INNER_MATH_FUNCTION__NE);	        //!= hash8_
        ks.add(INNER_MATH_FUNCTION__EQ);	        //== hash9_
        ks.add(INNER_MATH_FUNCTION__EQO);           //===
        ks.add(INNER_MATH_FUNCTION__NEQO);          //!==

        ks.add(INNER_MATH_FUNCTION__LESS);		        //< hash15_
        ks.add(INNER_MATH_FUNCTION__GREATER);	        //> hash16_

        ks.add(INNER_MATH_FUNCTION__LESS_EQ);	        //<= hash17_
        ks.add(INNER_MATH_FUNCTION__GREATER_EQ);        //>= hash18_

        ks.add(INNER_MATH_FUNCTION__COLON_SET_VALUE);   //:

        return ks;
    }









    
    /**
     * 由代码加载器自动转换 表达式 为 方法
     * 比如 1 + 1 代码加载器应该转换为+(1, 1)
     * 只需要将{@link Re_Math#_addToKeyword(Re_IRe_VariableMap)} 添加到keywords里即可实现表达式！
     */
    public static void _addToKeyword(Re_IRe_VariableMap keyword) {
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__AAND) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 0) {
                    return FALSE.get();
                }
                for (int i = 0; i < paramExpressionCount; i++) {
                    Object v = executor.getExpressionValue(call, i);
                    if (executor.isReturnOrThrow()) return null;

                    if (!Re_Utilities.ifTrue(v)) {
                        return FALSE.get();
                    }
                }
                return TRUE.get();
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__OOR) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 0) {
                    return FALSE.get();
                }
                for (int i = 0; i < paramExpressionCount; i++) {
                    Object v = executor.getExpressionValue(call, i);
                    if (executor.isReturnOrThrow()) return null;

                    if (Re_Utilities.ifTrue(v)) {
                        return v;
                    }
                }
                return FALSE.get();
            }
        }, keyword);




        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__AND) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.and(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__OR) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.or(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__XOR) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.xor(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__SHIFT_RIGHT) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.shiftRight(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__SHIFT_LEFT) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.shiftLeft(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__UNSIGNED_SHIFT_RIGHT) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.unsignedShiftRight(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);


        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__NE) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.neq(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__EQ) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.eq(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__EQO) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object o = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return o == executor.getExpressionValue(call,1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__NEQO) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object o = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return o != executor.getExpressionValue(call,1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);





        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__ADD) {@Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.add(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__SUBTRACT) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.subtract(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__MULTIPLY) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.multiply(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__DIVICE) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.divide(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__MOD) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.mod(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);



        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__LESS) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.less(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__GREATER) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.greater(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);


        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_MATH_FUNCTION__EQ_LESS, null, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__LESS_EQ) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.less_eq(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_MATH_FUNCTION__EQ_GREATER, null, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__GREATER_EQ) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object x1 = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    return Re_Math.greater_eq(x0, x1);
                } else {
                    executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                    return null;
                }
            }
        }, keyword);





        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_MATH_FUNCTION__COLON_SET_VALUE) {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
                if (call.getParamExpressionCount() == 2) {
                    Object key = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object value = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;

                    Re_Variable.accessSetValue(executor, key, value , executor);
                    return value;
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                return null;
            }
        }, keyword);

    }









    /**
     * calc:
     *
     * byte
     * long
     * double
     * char
     * int
     * boolean
     * float
     * short
     */


    //Number 被 、
    //Byte Integer Long Double Float Short继承
    //Boolean 和 Character 没有继承他

    //~
    static Object tilde(Object x1) {
        if (x1 instanceof Byte)
            return ~((Byte)x1).byteValue();
        else if (x1 instanceof Long)
            return ~((Long)x1).longValue();
            //else if (x instanceof Double)
            //	return ~((Double)x).doubleValue();
        else if (x1 instanceof Integer)
            return ~((Integer)x1).intValue();
            //else if (x instanceof Float)
            //	return ~((Float)x).floatValue();
        else if (x1 instanceof Short)
            return ~((Short)x1).shortValue();

        else if (x1 instanceof Character)
            return ~((Character)x1).charValue();
        else if (x1 instanceof Boolean)
        	return ~(((Boolean)x1).booleanValue() ? 1 : 0);
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Keywords.INNER_FUNCTION__TILDE + " " + Re_Utilities.objectAsName(x1));
    }
    //&
    static Object and(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() & ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() & ((Long)x1).longValue();
                //else if (x1 instanceof Double)
                //	return ((Byte)x0).byteValue() & ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() & ((Integer)x1).intValue();
                //else if (x1 instanceof Float)
                //	return ((Byte)x0).byteValue() & ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() & ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() & ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
            	return ((Byte)x0).byteValue() & (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() & ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() & ((Long)x1).longValue();
                //else if (x1 instanceof Double)
                //	return ((Long)x0).longValue() & ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() & ((Integer)x1).intValue();
                //else if (x1 instanceof Float)
                //	return ((Long)x0).longValue() & ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() & ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() & ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
            	return ((Long)x0).longValue() & (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() & ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() & ((Long)x1).longValue();
                //else if (x1 instanceof Double)
                //	return ((Character)x0).charValue() & ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() & ((Integer)x1).intValue();
                //else if (x1 instanceof Float)
                //	return ((Character)x0).charValue() & ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() & ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() & ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
            	return ((Character)x0).charValue() & (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() & ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() & ((Long)x1).longValue();
                //else if (x1 instanceof Double)
                //	return ((Integer)x0).intValue() & ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() & ((Integer)x1).intValue();
                //else if (x1 instanceof Float)
                //	return ((Integer)x0).intValue() & ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() & ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() & ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
            	return ((Integer)x0).intValue() & (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Boolean) {
            if (x1 instanceof Byte)
                return (((Boolean)x0).booleanValue() ? 1 : 0) & ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return (((Boolean)x0).booleanValue() ? 1 : 0) & ((Long)x1).longValue();
//            else if (x1 instanceof Double)
//                return (((Boolean)x0).booleanValue() ? 1 : 0) & ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return (((Boolean)x0).booleanValue() ? 1 : 0) & ((Integer)x1).intValue();
//            else if (x1 instanceof Float)
//                return (((Boolean)x0).booleanValue() ? 1 : 0) & ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return (((Boolean)x0).booleanValue() ? 1 : 0) & ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return (((Boolean)x0).booleanValue() ? 1 : 0) & ((Character)x1).charValue();
            if (x1 instanceof Boolean)
                return ((Boolean)x0).booleanValue() && ((Boolean)x1).booleanValue()? TRUE.get(): FALSE.get();
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() & ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() & ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Short)x0).shortValue() & ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() & ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Short)x0).shortValue() & ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() & ((Short)x1).shortValue();


            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() & ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Short)x0).shortValue() & (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        return Re_Utilities.ifAnd(x0, x1) ? TRUE.get(): FALSE.get();
    }

    //|
    static Object or(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() | ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() | ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Byte)x0).byteValue() | ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() | ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Byte)x0).byteValue() | ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() | ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() | ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Byte)x0).byteValue() | (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() | ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() | ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Long)x0).longValue() | ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() | ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Long)x0).longValue() | ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() | ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() | ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Long)x0).longValue() | (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() | ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() | ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Character)x0).charValue() | ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() | ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Character)x0).charValue() | ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() | ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() | ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Character)x0).charValue() | (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() | ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() | ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Integer)x0).intValue() | ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() | ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Integer)x0).intValue() | ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() | ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() | ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Integer)x0).intValue() | (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Boolean) {
				if (x1 instanceof Byte)
					return (((Boolean)x0).booleanValue() ? 1 : 0) | ((Byte)x1).byteValue();
				else if (x1 instanceof Long)
					return (((Boolean)x0).booleanValue() ? 1 : 0) | ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return (((Boolean)x0).booleanValue() ? 1 : 0) | ((Double)x1).doubleValue();
				else if (x1 instanceof Integer)
					return (((Boolean)x0).booleanValue() ? 1 : 0)| ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return (((Boolean)x0).booleanValue() ? 1 : 0) | ((Float)x1).floatValue();
				else if (x1 instanceof Short)
					return (((Boolean)x0).booleanValue() ? 1 : 0) | ((Short)x1).shortValue();

				else if (x1 instanceof Character)
					return (((Boolean)x0).booleanValue() ? 1 : 0) | ((Character)x1).charValue();
            if (x1 instanceof Boolean)
                return ((Boolean)x0).booleanValue() || ((Boolean)x1).booleanValue()? TRUE.get(): FALSE.get();
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() | ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() | ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Short)x0).shortValue() | ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() | ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Short)x0).shortValue() | ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() | ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() | ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Short)x0).shortValue() | (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        return Re_Utilities.ifOr(x0, x1) ? TRUE.get(): FALSE.get();
    }

    //^
    static Object xor(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() ^ ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() ^ ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Byte)x0).byteValue() ^ ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() ^ ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Byte)x0).byteValue() ^ ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() ^ ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() ^ ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Byte)x0).byteValue() ^ (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() ^ ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() ^ ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Long)x0).longValue() ^ ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() ^ ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Long)x0).longValue() ^ ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() ^ ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() ^ ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Long)x0).longValue() ^ (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() ^ ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() ^ ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Character)x0).charValue() ^ ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() ^ ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Character)x0).charValue() ^ ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() ^ ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() ^ ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Character)x0).charValue() ^ (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() ^ ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() ^ ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Integer)x0).intValue() ^ ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() ^ ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Integer)x0).intValue() ^ ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() ^ ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() ^ ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Integer)x0).intValue() ^ (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Boolean) {
            if (x1 instanceof Byte)
                return (((Boolean)x0).booleanValue() ? 1: 0) ^ ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return (((Boolean)x0).booleanValue() ? 1: 0) ^ ((Long)x1).longValue();
//            else if (x1 instanceof Double)
//                return (((Boolean)x0).booleanValue() ? 1: 0) ^ ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return (((Boolean)x0).booleanValue() ? 1: 0) ^ ((Integer)x1).intValue();
//            else if (x1 instanceof Float)
//                return ((Boolean)x0).booleanValue() ^ ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return (((Boolean)x0).booleanValue() ? 1: 0) ^ ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return (((Boolean)x0).booleanValue() ? 1: 0) ^ ((Character)x1).charValue();
            if (x1 instanceof Boolean)
                return ((Boolean)x0).booleanValue() ^ ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() ^ ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() ^ ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Short)x0).shortValue() ^ ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() ^ ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Short)x0).shortValue() ^ ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() ^ ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() ^ ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Short)x0).shortValue() ^ (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__XOR +" "+ Re_Utilities.objectAsName(x1) );

    }


    //>>
    static Object shiftRight(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() >> ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() >> ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Byte)x0).byteValue() >> ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() >> ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Byte)x0).byteValue() >> ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() >> ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() >> ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Byte)x0).byteValue() >> (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() >> ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() >> ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Long)x0).longValue() >> ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() >> ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Long)x0).longValue() >> ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() >> ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() >> ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Long)x0).longValue() >> (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() >> ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() >> ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Character)x0).charValue() >> ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() >> ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Character)x0).charValue() >> ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() >> ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() >> ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Character)x0).charValue() >> (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() >> ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() >> ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Integer)x0).intValue() >> ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() >> ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Integer)x0).intValue() >> ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() >> ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() >> ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Integer)x0).intValue() >> (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() >> ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() >> ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Short)x0).shortValue() >> ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() >> ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Short)x0).shortValue() >> ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() >> ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() >> ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Short)x0).shortValue() >> (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__SHIFT_RIGHT +" "+ Re_Utilities.objectAsName(x1));

    }

    //<<
    static Object shiftLeft(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() << ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() << ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Byte)x0).byteValue() << ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() << ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Byte)x0).byteValue() << ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() << ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() << ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Byte)x0).byteValue() << (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() << ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() << ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Long)x0).longValue() << ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() << ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Long)x0).longValue() << ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() << ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() << ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Long)x0).longValue() << (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() << ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() << ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Character)x0).charValue() << ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() << ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Character)x0).charValue() << ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() << ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() << ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Character)x0).charValue() << (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() << ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() << ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Integer)x0).intValue() << ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() << ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Integer)x0).intValue() << ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() << ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() << ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Integer)x0).intValue() << (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() << ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() << ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Short)x0).shortValue() << ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() << ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Short)x0).shortValue() << ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() << ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() << ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Short)x0).shortValue() << (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__SHIFT_LEFT +" "+ Re_Utilities.objectAsName(x1));

    }


    //>>>
    static Object unsignedShiftRight(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() >>> ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() >>> ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Byte)x0).byteValue() >>> ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() >>> ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Byte)x0).byteValue() >>> ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() >>> ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() >>> ((Character)x1).charValue();
				else if (x1 instanceof Boolean)
					return ((Byte)x0).byteValue() >>> (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() >>> ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() >>> ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Long)x0).longValue() >>> ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() >>> ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Long)x0).longValue() >>> ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() >>> ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() >>> ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Long)x0).longValue() >>> (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() >>> ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() >>> ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Character)x0).charValue() >>> ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() >>> ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Character)x0).charValue() >>> ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() >>> ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() >>> ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Character)x0).charValue() >>> (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() >>> ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() >>> ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Integer)x0).intValue() >>> ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() >>> ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Integer)x0).intValue() >>> ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() >>> ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() >>> ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Integer)x0).intValue() >>> (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() >>> ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() >>> ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Short)x0).shortValue() >>> ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() >>> ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Short)x0).shortValue() >>> ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() >>> ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() >>> ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Short)x0).shortValue() >>> (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__UNSIGNED_SHIFT_RIGHT +" "+ Re_Utilities.objectAsName(x1));

    }


    // !=
    static boolean neq(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() != ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() != ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Byte)x0).byteValue() != ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() != ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Byte)x0).byteValue() != ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() != ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() != ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Byte)x0).byteValue() != ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() != ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() != ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Long)x0).longValue() != ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() != ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Long)x0).longValue() != ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() != ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() != ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Long)x0).longValue() != ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Double) {
            if (x1 instanceof Byte)
                return ((Double)x0).doubleValue() != ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Double)x0).doubleValue() != ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Double)x0).doubleValue() != ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Double)x0).doubleValue() != ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Double)x0).doubleValue() != ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Double)x0).doubleValue() != ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Double)x0).doubleValue() != ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Double)x0).doubleValue() != ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() != ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() != ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Character)x0).charValue() != ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() != ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Character)x0).charValue() != ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() != ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() != ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Character)x0).charValue() != ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() != ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() != ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Integer)x0).intValue() != ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() != ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Integer)x0).intValue() != ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() != ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() != ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Integer)x0).intValue() != ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Boolean) {
//				if (x1 instanceof Byte)
//					return ((Boolean)x0).booleanValue() != ((Byte)x1).byteValue();
//				else if (x1 instanceof Long)
//					return ((Boolean)x0).booleanValue() != ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Boolean)x0).booleanValue() != ((Double)x1).doubleValue();
//				else if (x1 instanceof Integer)
//					return ((Boolean)x0).booleanValue() != ((Integer)x1).intValue();
//				else
//				else if (x1 instanceof Float)
//					return ((Boolean)x0).booleanValue() != ((Float)x1).floatValue();
//				else if (x1 instanceof Short)
//					return ((Boolean)x0).booleanValue() != ((Short)x1).shortValue();

//				else if (x1 instanceof Character)
//					return ((Boolean)x0).booleanValue() != ((Character)x1).charValue();
            if (x1 instanceof Boolean)
                return ((Boolean)x0).booleanValue() != ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Float) {
            if (x1 instanceof Byte)
                return ((Float)x0).floatValue() != ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Float)x0).floatValue() != ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Float)x0).floatValue() != ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Float)x0).floatValue() != ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Float)x0).floatValue() != ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Float)x0).floatValue() != ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Float)x0).floatValue() != ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Float)x0).floatValue() != ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() != ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() != ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Short)x0).shortValue() != ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() != ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Short)x0).shortValue() != ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() != ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() != ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Short)x0).shortValue() != ((Boolean)x1).booleanValue();
        }
        if (null == x0)
            return null != x1;

        if (x0.getClass().isArray())
            return !Arrayy.deepEquals(x0, x1);

        return !x0.equals(x1);
    }

    //==
    static boolean eq(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() == ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() == ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Byte)x0).byteValue() == ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() == ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Byte)x0).byteValue() == ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() == ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() == ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Byte)x0).byteValue() == ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() == ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() == ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Long)x0).longValue() == ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() == ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Long)x0).longValue() == ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() == ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() == ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Long)x0).longValue() == ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Double) {
            if (x1 instanceof Byte)
                return ((Double)x0).doubleValue() == ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Double)x0).doubleValue() == ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Double)x0).doubleValue() == ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Double)x0).doubleValue() == ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Double)x0).doubleValue() == ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Double)x0).doubleValue() == ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Double)x0).doubleValue() == ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Double)x0).doubleValue() == ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() == ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() == ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Character)x0).charValue() == ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() == ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Character)x0).charValue() == ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() == ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() == ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Character)x0).charValue() == ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() == ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() == ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Integer)x0).intValue() == ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() == ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Integer)x0).intValue() == ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() == ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() == ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Integer)x0).intValue() == ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Boolean) {
//				if (x1 instanceof Byte)
//					return ((Boolean)x0).booleanValue() == ((Byte)x1).byteValue();
//				else if (x1 instanceof Long)
//					return ((Boolean)x0).booleanValue() == ((Long)x1).longValue();
//				else if (x1 instanceof Double)
//					return ((Boolean)x0).booleanValue() == ((Double)x1).doubleValue();
//				else if (x1 instanceof Integer)
//					return ((Boolean)x0).booleanValue() == ((Integer)x1).intValue();
//				else if (x1 instanceof Float)
//					return ((Boolean)x0).booleanValue() == ((Float)x1).floatValue();
//				else if (x1 instanceof Short)
//					return ((Boolean)x0).booleanValue() == ((Short)x1).shortValue();

//				else if (x1 instanceof Character)
//					return ((Boolean)x0).booleanValue() == ((Character)x1).charValue();
//				else
            if (x1 instanceof Boolean)
                return ((Boolean)x0).booleanValue() == ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Float) {
            if (x1 instanceof Byte)
                return ((Float)x0).floatValue() == ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Float)x0).floatValue() == ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Float)x0).floatValue() == ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Float)x0).floatValue() == ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Float)x0).floatValue() == ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Float)x0).floatValue() == ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Float)x0).floatValue() == ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Float)x0).floatValue() == ((Boolean)x1).booleanValue();
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() == ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() == ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Short)x0).shortValue() == ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() == ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Short)x0).shortValue() == ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() == ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() == ((Character)x1).charValue();
//				else if (x1 instanceof Boolean)
//					return ((Short)x0).shortValue() == ((Boolean)x1).booleanValue();
        }

        if (null == x0)
            return null == x1;

        if (x0.getClass().isArray())
            return Arrayy.deepEquals(x0, x1);

        return x0.equals(x1);
    }











    //+
    static Object add(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() + ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() + ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Byte)x0).byteValue() + ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() + ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Byte)x0).byteValue() + ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() + ((Short)x1).shortValue();

            else if (x1 instanceof String)
                return ((Byte)x0).byteValue() + (String)x1;
            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() + ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Byte)x0).byteValue() + (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() + ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() + ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Long)x0).longValue() + ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() + ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Long)x0).longValue() + ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() + ((Short)x1).shortValue();

            else if (x1 instanceof String)
                return ((Long)x0).longValue() + (String)x1;
            else if (x1 instanceof Character)
                return ((Long)x0).longValue() + ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Long)x0).longValue() + (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Double) {
            if (x1 instanceof Byte)
                return ((Double)x0).doubleValue() + ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Double)x0).doubleValue() + ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Double)x0).doubleValue() + ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Double)x0).doubleValue() + ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Double)x0).doubleValue() + ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Double)x0).doubleValue() + ((Short)x1).shortValue();

            else if (x1 instanceof String)
                return ((Double)x0).doubleValue() + (String)x1;
            else if (x1 instanceof Character)
                return ((Double)x0).doubleValue() + ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Double)x0).doubleValue() + (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() + ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() + ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Character)x0).charValue() + ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() + ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Character)x0).charValue() + ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() + ((Short)x1).shortValue();

            else if (x1 instanceof String)
                return ((Character)x0).charValue() + (String)x1;
            else if (x1 instanceof Character)
                return ((Character)x0).charValue() + ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Character)x0).charValue() + (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() + ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() + ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Integer)x0).intValue() + ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() + ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Integer)x0).intValue() + ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() + ((Short)x1).shortValue();

            else if (x1 instanceof String)
                return ((Integer)x0).intValue() + (String)x1;
            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() + ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Integer)x0).intValue() + (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Float) {
            if (x1 instanceof Byte)
                return ((Float)x0).floatValue() + ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Float)x0).floatValue() + ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Float)x0).floatValue() + ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Float)x0).floatValue() + ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Float)x0).floatValue() + ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Float)x0).floatValue() + ((Short)x1).shortValue();

            else if (x1 instanceof String)
                return ((Float)x0).floatValue() + (String)x1;
            else if (x1 instanceof Character)
                return ((Float)x0).floatValue() + ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Float)x0).floatValue() + (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() + ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() + ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Short)x0).shortValue() + ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() + ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Short)x0).shortValue() + ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() + ((Short)x1).shortValue();

            else if (x1 instanceof String)
                return ((Short)x0).shortValue() + (String)x1;
            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() + ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Short)x0).shortValue() + (((Boolean)x1).booleanValue() ? 1 : 0);
        }  else if (x0 instanceof Boolean) {
			if (x1 instanceof Byte)
				return (((Boolean)x0).booleanValue() ? 1 : 0) + ((Byte)x1).byteValue();
			else if (x1 instanceof Long)
				return (((Boolean)x0).booleanValue() ? 1 : 0) + ((Long)x1).longValue();
			else if (x1 instanceof Double)
				return (((Boolean)x0).booleanValue() ? 1 : 0) + ((Double)x1).doubleValue();
			else if (x1 instanceof Integer)
				return (((Boolean)x0).booleanValue() ? 1 : 0) + ((Integer)x1).intValue();
			else if (x1 instanceof Float)
				return (((Boolean)x0).booleanValue() ? 1 : 0) + ((Float)x1).floatValue();
			else if (x1 instanceof Short)
				return (((Boolean)x0).booleanValue() ? 1 : 0) + ((Short)x1).shortValue();

			else if (x1 instanceof String)
                return ((Boolean)x0).booleanValue() + (String)x1;
            else if (x1 instanceof Character)
                return String.valueOf(x0) + ((Character) x1).charValue();
			else if (x1 instanceof Boolean)
				return (((Boolean)x0).booleanValue() ? 1 : 0) + (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof CharSequence) {
            return "" + x0 + Re_Utilities.toJString(x1);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__ADD +" "+ Re_Utilities.objectAsName(x1));
    }













    //-
    static Object subtract(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() - ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() - ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Byte)x0).byteValue() - ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() - ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Byte)x0).byteValue() - ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() - ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() - ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Byte)x0).byteValue() - (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() - ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() - ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Long)x0).longValue() - ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() - ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Long)x0).longValue() - ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() - ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() - ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Long)x0).longValue() - (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Double) {
            if (x1 instanceof Byte)
                return ((Double)x0).doubleValue() - ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Double)x0).doubleValue() - ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Double)x0).doubleValue() - ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Double)x0).doubleValue() - ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Double)x0).doubleValue() - ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Double)x0).doubleValue() - ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Double)x0).doubleValue() - ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Double)x0).doubleValue() - (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() - ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() - ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Character)x0).charValue() - ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() - ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Character)x0).charValue() - ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() - ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() - ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Character)x0).charValue() - (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() - ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() - ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Integer)x0).intValue() - ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() - ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Integer)x0).intValue() - ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() - ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() - ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Integer)x0).intValue() - (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Float) {
            if (x1 instanceof Byte)
                return ((Float)x0).floatValue() - ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Float)x0).floatValue() - ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Float)x0).floatValue() - ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Float)x0).floatValue() - ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Float)x0).floatValue() - ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Float)x0).floatValue() - ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Float)x0).floatValue() - ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Float)x0).floatValue() - (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() - ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() - ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Short)x0).shortValue() - ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() - ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Short)x0).shortValue() - ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() - ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() - ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Short)x0).shortValue() - (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__SUBTRACT +" "+ Re_Utilities.objectAsName(x1));
    }
    //-
    static Object subtract(Object x1) {
        if (null == x1)
            return  TRUE.get();
        if (x1 instanceof Boolean)
            return  ((Boolean)x1).booleanValue()? FALSE.get(): TRUE.get();

        if (x1 instanceof Byte)
            return  - ((Byte)x1).byteValue();
        else if (x1 instanceof Long)
            return  - ((Long)x1).longValue();
        else if (x1 instanceof Double)
            return - ((Double)x1).doubleValue();
        else if (x1 instanceof Integer)
            return - ((Integer)x1).intValue();
        else if (x1 instanceof Float)
            return - ((Float)x1).floatValue();
        else if (x1 instanceof Short)
            return - ((Short)x1).shortValue();

        else if (x1 instanceof Character)
            return - ((Character)x1).charValue();

        else
            throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Keywords.INNER_MATH_FUNCTION__SUBTRACT + " " + Re_Utilities.objectAsName(x1));
    }





    //*
    static Object multiply(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() * ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() * ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Byte)x0).byteValue() * ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() * ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Byte)x0).byteValue() * ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() * ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() * ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Byte)x0).byteValue() * (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() * ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() * ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Long)x0).longValue() * ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() * ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Long)x0).longValue() * ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() * ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() * ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Long)x0).longValue() * (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Double) {
            if (x1 instanceof Byte)
                return ((Double)x0).doubleValue() * ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Double)x0).doubleValue() * ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Double)x0).doubleValue() * ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Double)x0).doubleValue() * ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Double)x0).doubleValue() * ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Double)x0).doubleValue() * ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Double)x0).doubleValue() * ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Double)x0).doubleValue() * (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() * ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() * ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Character)x0).charValue() * ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() * ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Character)x0).charValue() * ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() * ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() * ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Character)x0).charValue() * (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() * ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() * ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Integer)x0).intValue() * ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() * ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Integer)x0).intValue() * ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() * ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() * ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Integer)x0).intValue() * (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Float) {
            if (x1 instanceof Byte)
                return ((Float)x0).floatValue() * ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Float)x0).floatValue() * ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Float)x0).floatValue() * ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Float)x0).floatValue() * ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Float)x0).floatValue() * ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Float)x0).floatValue() * ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Float)x0).floatValue() * ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Float)x0).floatValue() * (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() * ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() * ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Short)x0).shortValue() * ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() * ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Short)x0).shortValue() * ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() * ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() * ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Short)x0).shortValue() * (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof String) {
            if (x1 instanceof Byte)
                return Strings.repeat((String)x0, ((Byte)x1).byteValue());
            else if (x1 instanceof Long)
                return Strings.repeat((String)x0, Mathz.toIntExact(((Long) x1).longValue()));
            else if (x1 instanceof Double)
                return Strings.repeat((String)x0, (int) ((Double)x1).doubleValue());
            else if (x1 instanceof Integer)
                return Strings.repeat((String)x0, ((Integer)x1).intValue());
            else if (x1 instanceof Float)
                return Strings.repeat((String)x0, (int) ((Float)x1).floatValue());
            else if (x1 instanceof Short)
                return Strings.repeat((String)x0, ((Short)x1).shortValue());

            else if (x1 instanceof Character)
                return Strings.repeat((String)x0, ((Character)x1).charValue());
            else if (x1 instanceof Boolean)
                return Strings.repeat((String)x0, (((Boolean)x1).booleanValue() ? 1 : 0));
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__MULTIPLY +" "+ Re_Utilities.objectAsName(x1));

    }



    // /
    static Object divide(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() / ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() / ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Byte)x0).byteValue() / ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() / ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Byte)x0).byteValue() / ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() / ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() / ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Byte)x0).byteValue() / (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() / ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() / ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Long)x0).longValue() / ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() / ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Long)x0).longValue() / ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() / ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() / ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Long)x0).longValue() / (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Double) {
            if (x1 instanceof Byte)
                return ((Double)x0).doubleValue() / ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Double)x0).doubleValue() / ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Double)x0).doubleValue() / ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Double)x0).doubleValue() / ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Double)x0).doubleValue() / ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Double)x0).doubleValue() / ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Double)x0).doubleValue() / ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Double)x0).doubleValue() / (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() / ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() / ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Character)x0).charValue() / ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() / ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Character)x0).charValue() / ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() / ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() / ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Character)x0).charValue() / (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() / ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() / ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Integer)x0).intValue() / ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() / ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Integer)x0).intValue() / ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() / ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() / ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Integer)x0).intValue() / (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Float) {
            if (x1 instanceof Byte)
                return ((Float)x0).floatValue() / ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Float)x0).floatValue() / ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Float)x0).floatValue() / ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Float)x0).floatValue() / ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Float)x0).floatValue() / ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Float)x0).floatValue() / ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Float)x0).floatValue() / ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Float)x0).floatValue() / (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() / ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() / ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Short)x0).shortValue() / ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() / ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Short)x0).shortValue() / ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() / ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() / ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Short)x0).shortValue() / (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__DIVICE +" "+ Re_Utilities.objectAsName(x1));

    }


    //%
    static Object mod(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() % ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() % ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Byte)x0).byteValue() % ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() % ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Byte)x0).byteValue() % ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() % ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() % ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Byte)x0).byteValue() % (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() % ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() % ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Long)x0).longValue() % ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() % ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Long)x0).longValue() % ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() % ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() % ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Long)x0).longValue() % (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Double) {
            if (x1 instanceof Byte)
                return ((Double)x0).doubleValue() % ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Double)x0).doubleValue() % ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Double)x0).doubleValue() % ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Double)x0).doubleValue() % ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Double)x0).doubleValue() % ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Double)x0).doubleValue() % ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Double)x0).doubleValue() % ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Double)x0).doubleValue() % (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() % ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() % ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Character)x0).charValue() % ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() % ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Character)x0).charValue() % ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() % ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() % ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Character)x0).charValue() % (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() % ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() % ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Integer)x0).intValue() % ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() % ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Integer)x0).intValue() % ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() % ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() % ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Integer)x0).intValue() % (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Float) {
            if (x1 instanceof Byte)
                return ((Float)x0).floatValue() % ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Float)x0).floatValue() % ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Float)x0).floatValue() % ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Float)x0).floatValue() % ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Float)x0).floatValue() % ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Float)x0).floatValue() % ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Float)x0).floatValue() % ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Float)x0).floatValue() % (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() % ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() % ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Short)x0).shortValue() % ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() % ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Short)x0).shortValue() % ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() % ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() % ((Character)x1).charValue();
            else if (x1 instanceof Boolean)
                return ((Short)x0).shortValue() % (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__MOD +" "+ Re_Utilities.objectAsName(x1));

    }


    //<
    static boolean less(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() < ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() < ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Byte)x0).byteValue() < ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() < ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Byte)x0).byteValue() < ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() < ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() < ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Byte)x0).byteValue() < (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() < ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() < ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Long)x0).longValue() < ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() < ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Long)x0).longValue() < ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() < ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() < ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Long)x0).longValue() < (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Double) {
            if (x1 instanceof Byte)
                return ((Double)x0).doubleValue() < ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Double)x0).doubleValue() < ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Double)x0).doubleValue() < ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Double)x0).doubleValue() < ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Double)x0).doubleValue() < ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Double)x0).doubleValue() < ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Double)x0).doubleValue() < ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Double)x0).doubleValue() < (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() < ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() < ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Character)x0).charValue() < ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() < ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Character)x0).charValue() < ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() < ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() < ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Character)x0).charValue() < (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() < ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() < ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Integer)x0).intValue() < ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() < ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Integer)x0).intValue() < ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() < ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() < ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Integer)x0).intValue() < (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Float) {
            if (x1 instanceof Byte)
                return ((Float)x0).floatValue() < ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Float)x0).floatValue() < ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Float)x0).floatValue() < ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Float)x0).floatValue() < ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Float)x0).floatValue() < ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Float)x0).floatValue() < ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Float)x0).floatValue() < ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Float)x0).floatValue() < (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() < ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() < ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Short)x0).shortValue() < ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() < ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Short)x0).shortValue() < ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() < ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() < ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Short)x0).shortValue() < (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__LESS +" "+ Re_Utilities.objectAsName(x1));

    }


    //>
    static boolean greater(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() > ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() > ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Byte)x0).byteValue() > ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() > ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Byte)x0).byteValue() > ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() > ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() > ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Byte)x0).byteValue() > (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() > ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() > ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Long)x0).longValue() > ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() > ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Long)x0).longValue() > ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() > ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() > ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Long)x0).longValue() > (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Double) {
            if (x1 instanceof Byte)
                return ((Double)x0).doubleValue() > ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Double)x0).doubleValue() > ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Double)x0).doubleValue() > ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Double)x0).doubleValue() > ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Double)x0).doubleValue() > ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Double)x0).doubleValue() > ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Double)x0).doubleValue() > ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Double)x0).doubleValue() > (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() > ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() > ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Character)x0).charValue() > ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() > ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Character)x0).charValue() > ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() > ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() > ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Character)x0).charValue() > (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() > ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() > ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Integer)x0).intValue() > ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() > ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Integer)x0).intValue() > ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() > ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() > ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Integer)x0).intValue() > (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Float) {
            if (x1 instanceof Byte)
                return ((Float)x0).floatValue() > ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Float)x0).floatValue() > ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Float)x0).floatValue() > ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Float)x0).floatValue() > ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Float)x0).floatValue() > ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Float)x0).floatValue() > ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Float)x0).floatValue() > ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Float)x0).floatValue() > (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() > ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() > ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Short)x0).shortValue() > ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() > ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Short)x0).shortValue() > ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() > ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() > ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Short)x0).shortValue() > (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__GREATER +" "+ Re_Utilities.objectAsName(x1));

    }


    //<=
    static boolean less_eq(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() <= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() <= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Byte)x0).byteValue() <= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() <= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Byte)x0).byteValue() <= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() <= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() <= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Byte)x0).byteValue() <= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() <= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() <= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Long)x0).longValue() <= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() <= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Long)x0).longValue() <= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() <= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() <= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Long)x0).longValue() <= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Double) {
            if (x1 instanceof Byte)
                return ((Double)x0).doubleValue() <= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Double)x0).doubleValue() <= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Double)x0).doubleValue() <= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Double)x0).doubleValue() <= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Double)x0).doubleValue() <= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Double)x0).doubleValue() <= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Double)x0).doubleValue() <= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Double)x0).doubleValue() <= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() <= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() <= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Character)x0).charValue() <= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() <= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Character)x0).charValue() <= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() <= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() <= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Character)x0).charValue() <= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() <= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() <= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Integer)x0).intValue() <= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() <= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Integer)x0).intValue() <= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() <= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() <= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Integer)x0).intValue() <= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Float) {
            if (x1 instanceof Byte)
                return ((Float)x0).floatValue() <= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Float)x0).floatValue() <= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Float)x0).floatValue() <= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Float)x0).floatValue() <= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Float)x0).floatValue() <= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Float)x0).floatValue() <= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Float)x0).floatValue() <= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Float)x0).floatValue() <= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() <= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() <= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Short)x0).shortValue() <= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() <= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Short)x0).shortValue() <= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() <= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() <= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Short)x0).shortValue() <= (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__LESS_EQ +" "+ Re_Utilities.objectAsName(x1));

    }

    //>=
    static boolean greater_eq(Object x0, Object x1) {
        if (x0 instanceof Byte) {
            if (x1 instanceof Byte)
                return ((Byte)x0).byteValue() >= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Byte)x0).byteValue() >= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Byte)x0).byteValue() >= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Byte)x0).byteValue() >= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Byte)x0).byteValue() >= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Byte)x0).byteValue() >= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Byte)x0).byteValue() >= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Byte)x0).byteValue() >= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Long) {
            if (x1 instanceof Byte)
                return ((Long)x0).longValue() >= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Long)x0).longValue() >= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Long)x0).longValue() >= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Long)x0).longValue() >= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Long)x0).longValue() >= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Long)x0).longValue() >= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Long)x0).longValue() >= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Long)x0).longValue() >= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Double) {
            if (x1 instanceof Byte)
                return ((Double)x0).doubleValue() >= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Double)x0).doubleValue() >= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Double)x0).doubleValue() >= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Double)x0).doubleValue() >= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Double)x0).doubleValue() >= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Double)x0).doubleValue() >= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Double)x0).doubleValue() >= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Double)x0).doubleValue() >= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Character) {
            if (x1 instanceof Byte)
                return ((Character)x0).charValue() >= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Character)x0).charValue() >= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Character)x0).charValue() >= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Character)x0).charValue() >= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Character)x0).charValue() >= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Character)x0).charValue() >= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Character)x0).charValue() >= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Character)x0).charValue() >= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Integer) {
            if (x1 instanceof Byte)
                return ((Integer)x0).intValue() >= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Integer)x0).intValue() >= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Integer)x0).intValue() >= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Integer)x0).intValue() >= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Integer)x0).intValue() >= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Integer)x0).intValue() >= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Integer)x0).intValue() >= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Integer)x0).intValue() >= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Float) {
            if (x1 instanceof Byte)
                return ((Float)x0).floatValue() >= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Float)x0).floatValue() >= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Float)x0).floatValue() >= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Float)x0).floatValue() >= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Float)x0).floatValue() >= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Float)x0).floatValue() >= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Float)x0).floatValue() >= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Float)x0).floatValue() >= (((Boolean)x1).booleanValue() ? 1 : 0);
        } else if (x0 instanceof Short) {
            if (x1 instanceof Byte)
                return ((Short)x0).shortValue() >= ((Byte)x1).byteValue();
            else if (x1 instanceof Long)
                return ((Short)x0).shortValue() >= ((Long)x1).longValue();
            else if (x1 instanceof Double)
                return ((Short)x0).shortValue() >= ((Double)x1).doubleValue();
            else if (x1 instanceof Integer)
                return ((Short)x0).shortValue() >= ((Integer)x1).intValue();
            else if (x1 instanceof Float)
                return ((Short)x0).shortValue() >= ((Float)x1).floatValue();
            else if (x1 instanceof Short)
                return ((Short)x0).shortValue() >= ((Short)x1).shortValue();

            else if (x1 instanceof Character)
                return ((Short)x0).shortValue() >= ((Character)x1).charValue();
//            else if (x1 instanceof Boolean)
//                return ((Short)x0).shortValue() >= (((Boolean)x1).booleanValue() ? 1 : 0);
        }
        throw new Re_Accidents.RuntimeInternalMathError("unknown operating: " + Re_Utilities.objectAsName(x0)+" "+ Re_Keywords.INNER_MATH_FUNCTION__GREATER_EQ +" "+ Re_Utilities.objectAsName(x1));
    }
}
