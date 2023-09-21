package top.fols.box.reflect.re;

import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.interfaces.annotations.Nullable;

import static top.fols.box.reflect.re.Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker;

@SuppressWarnings({"rawtypes", "UnusedReturnValue", "SameParameterValue"})
public class Re_ClassFunction implements Re_IRe_Object, Re_IReGetDeclaringClass, Re_IReGetClass, Cloneable {
    Re_ClassFunction() {}
    Re_ClassFunction superClone(int newModifier,
                                Re_Class declareReClass,
                                Re_ClassInstance declareReClassInstance) {
        try {
            Re_ClassFunction clone = (Re_ClassFunction) super.clone();
            clone.modifier = newModifier;
            clone.declareReClass = declareReClass;
            clone.declareReClassInstance = declareReClassInstance;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);//不可能
        }
    }

    @NotNull
    InnerAccessor getInnerAccessor() {
        return InnerAccessor.StandardFunction;
    }





    private int                 modifier;
    private boolean             isAnonymous;

    private String              name;
    private String[]            params;
    private Re_CodeFile         reCodeBlock;

    private Re_Executor         declareExecutor; //parent
    private Re_Class            declareReClass;
    private Re_ClassInstance    declareReClassInstance;


    public int getModifier() {
        return modifier;
    }
    public boolean isAnonymous() { return isAnonymous; }


    /**
     * modifier必须进过处理
     * @param modifier {@link Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction#getFunctionModifier(boolean, int)}
     */
    @NotNull
    static Re_ClassFunction createReCassFunctionAfter(
            Re_ClassFunction reFunction, Re_CodeFile reCodeBlock,
            int modifier,
            @Nullable String name, @Nullable String[] paramName,
            @Nullable Re_Executor parent,
            @Nullable Re_Class declaringReClass, @Nullable Re_ClassInstance declaringReClassInstance) {
        reFunction.modifier       = modifier;
        reFunction.isAnonymous    = null == name;
        reFunction.name           = Re_CodeLoader.intern(buildReClassFunctionName(reCodeBlock.getLineOffset(), name, reFunction));
        reFunction.params         = null == paramName? Finals.EMPTY_STRING_ARRAY:paramName;
        reFunction.reCodeBlock    = reCodeBlock;
        reFunction.declareReClass = declaringReClass;
        reFunction.declareReClassInstance = declaringReClassInstance;
        reFunction.declareExecutor = parent;
        return reFunction;
    }
    @NotNull
    static String buildReClassFunctionName(int line, String oldName, Re_ClassFunction function) {
        if (null == oldName || oldName.length() == 0) {
            return Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION + "_" + line + "_" + Integer.toHexString(function.hashCode());
        } else {
            return oldName;
        }
    }






    @Override
    public final Re_Class getReClass() {
        return getReDeclareClass();
    }

    @Override
    public Re_Class getReDeclareClass() {
        return declareReClass;
    }
    public Re_ClassInstance getReDeclareReClassInstance() {
        return declareReClassInstance;
    }
    protected Re_CodeFile getReCodeBlock() {
        return reCodeBlock;
    }
    Re_Executor getDeclareExecutor() {
        return declareExecutor;
    }

    @Override
    public String  getName() {
        return name;
    }
    public int     getParamCount() {
        return params.length;
    }
    public String  getParamName(int index) {
        return params[index];
    }
    protected String[] getParams() {return params;}




    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        Re_Class reClass = this.getReClass();
        if (null == reClass)
            return "re-" + Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION + " " +
                    getName();
        else
            return "re-" + Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION + " " +
                    reClass.getName() + Re_CodeLoader.CODE_OBJECT_POINT_SYMBOL + getName();
    }



    @Override
    public boolean isPrimitive() {
        return false;
    }




    @Override public final boolean hasObjectKey(Re_Executor executor, Object key) { return false; }
    @Override public final boolean removeObjectKey(Re_Executor executor, Object key) { return false; }
    @Override public final Object getObjectValue(Re_Executor executor, Object key) { return null; }
    @Override public final void putObjectValue(Re_Executor executor, Object key, Object value) { }
    @Override public final int getObjectKeyCount(Re_Executor executor) throws Throwable { return 0; }
    @Override public final @NotNull Iterable getObjectKeys(Re_Executor executor) throws Throwable { return null; }
    @Override public boolean hasObjectKeys() { return false; }

    @Override public final Object            executePoint(Re_Executor executor, Object point_key, Re_CodeLoader.Call call) throws Throwable {
        String s = Re_Utilities.toJString(point_key);
        executor.setThrow(Re_Accidents.undefined(this, s));
        return null;
    }

    /**
     * 执行之前不可能已经return， 如果中间有执行了表达式应该判断表达式是否已经return 如果已经return 则返回return数据 而不是继续操作, 如果已经return返回的任何数据都是无效的
     * 只要用了{@link Re_Executor#getExpressionValue(Re_CodeLoader.Call, int)} 后都要手动检测是否return
     * <p>
     * 假定本对象名称x
     * 那么执行的是 x()
     *
     *
     * 只有这个 {@link #authenticationAndExecuteOnRe(Re_Executor, Re_Class, Re_ClassInstance, Object[], Re_IRe_VariableMap)} 能保证正确的声明来源
     */
    @Override
    public final Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
        Object[] arguments = executor.getExpressionValues(call);
        if (executor.isReturnOrThrow()) return null;

        //最后一行不需要判断return
        //为null 执行时自动生成
        return authenticationAndExecuteOnRe_FromDeclare(executor, arguments, null);
    }


    /**
     * 这个方法除了
     * {@link Re_Class}
     * {@link Re_ClassInstance}
     * {@link Re_ClassFunction}
     *
     * 如果其他方法需要方法必须通过 {@link Re_Class.UnsafesRe}
     *
     * @see Re_Class.UnsafesRe#executeFunction(Re, Re_NativeStack, Re_ClassFunction, Object[], Re_IRe_VariableMap)}
     */
    protected final Object authenticationAndExecuteOnRe_FromDeclare(Re_Executor executor, 
                                                                    Object[] arguments, @Nullable Re_IRe_VariableMap functionLocal) throws Throwable {
        return authenticationAndExecuteOnRe(executor,
                getReDeclareClass(), getReDeclareReClassInstance(),
                arguments, functionLocal);
    }
    /**
     * 这个方法除了
     * {@link Re_Class}
     * {@link Re_ClassInstance}
     * {@link Re_ClassFunction}
     *
     * 如果其他方法需要方法必须通过 {@link Re_Class.UnsafesRe}
     */
    protected final Object authenticationAndExecuteOnRe(Re_Executor executor,         //这里可以进行权限检测
                                                  Re_Class runInClass, Re_ClassInstance runInInstance,
                                                  Object[] arguments, @Nullable Re_IRe_VariableMap functionLocal) throws Throwable {//permissions check
        return unauthenticationExecuteOnNative(executor,
                runInClass, runInInstance,
                arguments, functionLocal);
    }

    /**
     * 不要进行任何的权限检测
     */
    protected Object unauthenticationExecuteOnNative(Re_Executor executor,
                                                     Re_Class runInClass, Re_ClassInstance runInInstance,
                                                     Object[] arguments, @Nullable Re_IRe_VariableMap functionLocal) throws Throwable {//permissions check
        Re_Executor re_executor = Re_Executor
                .createReClassFunctionExecutor(
                        executor.re, executor.getStack(),
                        runInClass, runInInstance,
                        this, arguments, functionLocal);
        if (null == re_executor) return null;//re throw

        return re_executor.run();
    }












    @NotNull
    public static Re_IRe_VariableMap argumentsAsVariableMap(Re_ClassFunction reClassFunction, Object[] arguments) {
        Re_IRe_VariableMap variableMap = Re.newLocalVariableMap();
        String[] params = reClassFunction.getParams();
        int functionParamCount  = params.length;
        if (functionParamCount <= arguments.length) {
            for (int i = 0; i < functionParamCount; i++)
                /*safe*/Re_Variable.Unsafes.putVariable(params[i], Re_Variable.createVariable(arguments[i]),           variableMap);
        } else {
            int i = 0;
            for (; i < arguments.length; i++)
                /*safe*/Re_Variable.Unsafes.putVariable(params[i], Re_Variable.createVariable(arguments[i]),           variableMap);
            for (; i < functionParamCount; i++)
                /*safe*/Re_Variable.Unsafes.putVariable(params[i], Re_Variable.createVariable(null), variableMap);
        }
        return variableMap;
    }



    static final class Init extends Re_ClassFunction {
        Init() {}
        Re_ClassFunction fieldInit;
        Re_ClassFunction constructor;

        Re_ClassFunction that() { return isNeed() ? this : null; }
        Re_ClassFunction able() {
            if (null != constructor)
                return  constructor;
            return fieldInit;
        }
        boolean isNeed() { return null != constructor || null != fieldInit; }

        void setFieldInstaller(Re_ClassFunction function) {
            fieldInit = function;
        }
        void setConstructor   (Re_ClassFunction function) {
            constructor = function;
        }


        @Override
        Init superClone(int newModifier, Re_Class declareReClass, Re_ClassInstance declareReClassInstance) {
            //理论上只有ReClass使用而且是直接创建的所以所有值都是null
            Init function = (Init) super.superClone(super.getModifier(), super.getReClass(), super.getReDeclareReClassInstance());
            if (null != fieldInit)   {
                //字段构造器不需要任何 修饰符
                function.fieldInit = fieldInit.superClone(Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction.getFunctionModifierEmpty(),
                        null,
                        null);
            }
            if (null != constructor) {
                function.constructor = constructor.superClone(newModifier,
                        declareReClass,
                        declareReClassInstance);
            }
            return function;
        }


        @Override public int getModifier() { return able().getModifier();}
        @Override public boolean isAnonymous()   { return able().isAnonymous(); }

        @Override public Re_Class getReDeclareClass()                   { return able().getReDeclareClass(); }
        @Override public Re_ClassInstance getReDeclareReClassInstance() { return able().getReDeclareReClassInstance(); }
        @Override protected Re_CodeFile getReCodeBlock()                { return able().getReCodeBlock(); }
        @Override Re_Executor getDeclareExecutor()                      { return able().getDeclareExecutor(); }
        @Override public String getName()                               { return able().getName(); }

        @Override public int getParamCount()                            { return able().getParamCount(); }
        @Override public String getParamName(int index)                 { return able().getParamName(index); }
        @Override protected String[] getParams()                        { return able().getParams(); }

        @Override public boolean isPrimitive()                          { return able().isPrimitive(); }


        @Override
        protected Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments, Re_IRe_VariableMap functionLocal) throws Throwable {
            if (null != fieldInit) {
                fieldInit.unauthenticationExecuteOnNative(executor, runInClass, runInInstance, arguments, functionLocal);
                if (executor.isReturnOrThrow()) return null;
            }
            if (null != constructor) {
                constructor.unauthenticationExecuteOnNative(executor, runInClass, runInInstance, arguments, functionLocal);
                if (executor.isReturnOrThrow()) return null;
            }
            return null;
        }
    }






    /**
     * 强制执行在某个或者某在实例上
     * 不经过任何权限检测
     *
     * 理论上不会抛出Java异常
     */
    static abstract class InnerAccessor<T extends Re_ClassFunction> {
        abstract Re_ClassFunction getDeletePermissionDetectionFunction(Re_ClassFunction reClassFunction,
                                                                       Re_Class runInClass, Re_ClassInstance runInInstance);
        static final InnerAccessor StandardFunction = new InnerAccessor() {
            @Override
            Re_ClassFunction getDeletePermissionDetectionFunction(Re_ClassFunction reClassFunction,
                                                                  Re_Class runInClass, Re_ClassInstance runInInstance) {
                return Unsafes.cloneAndDeletePermissionDetection(reClassFunction,
                        runInClass, runInInstance);
            }
        };
    }




    static class ParamCheckerFunction extends Re_ClassFunction {
        ParamCheckerFunction(){}

        @Override
        protected InnerAccessor getInnerAccessor() {
            return InnerAccessor.StandardFunction;
        }

        @NotNull Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker[] paramChecker;
        @NotNull Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker  returnChecker;

        @Override
        protected Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments, @Nullable Re_IRe_VariableMap functionLocal) throws Throwable {
            Re_NativeStack stack = executor.getStack();

            for (Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker tc: paramChecker)	{
                Object v   = tc.index < arguments.length ? arguments[tc.index] : null;
                if (!tc.isInstanceof(v)) {
                    stack.setThrow(Re_CodeLoader_ExpressionConverts.CallFunction.unsupported_param_type(tc, v));
                    return null;
                }
            }

            Re_Executor newExecutor = Re_Executor
                    .createReClassFunctionExecutor(
                            executor.re, stack,
                            runInClass, runInInstance,
                            this, arguments, functionLocal);
            if (null == newExecutor) return null;//re throw

            Object result = newExecutor.run();
            if (stack.isThrow()) return null;


            if (!returnChecker.isInstanceof(result)) {
                Re_NativeStack.ReNativeTraceElement lastTrace = newExecutor.getStackElement().clone(); // return result line
                stack.addStackElementAndGetIndex(lastTrace); //
                stack.setThrow(Re_CodeLoader_ExpressionConverts.CallFunction.unsupported_return_type(returnChecker, result));
                return null;
            }
            return result;
        }
    }




    static final class UnsafesRe {
//        static Re_ClassFunction fromReClassCloneToInstance(Re_Executor executor,
//                                                           Re_Class fromReClass,
//                                                           String name,
//                                                           Re_ClassInstance toInstance) {
//            if (null == executor) {
//                return null;
//            }
//            Object o = Re_Variable.UnsafesRe.fromUnsafeAccessorGetClassValue(name, fromReClass);
//            if (o instanceof Re_ClassFunction) {
//                Re_ClassFunction function = (Re_ClassFunction) o;
//                if (fromReClass != function.getReClass()) {
//                    executor.setThrow("copy class function ["+name+"] error: " + "the target method is already bound to another class: " + name);
//                    return null;
//                } else {
//                    return function.superClone(
//                            function.getModifier(),
//                            function.getReClass(),
//                            toInstance
//                    );
//                }
//            } else {
//                executor.setThrow("copy class function ["+name+"] error: " + Re_Accidents.unsupported_type(Re_Utilities.objectAsName(o)));
//                return null;
//            }
//        }
    }


    static final class Unsafes {
        static Re_ClassFunction cloneAndDeletePermissionDetection(Re_ClassFunction function,
                                                                  Re_Class          declareReClass,
                                                                  Re_ClassInstance  declareReClassInstance) {
            if (null == function) {
                return null;
            } else {
                return function.superClone(
                        Re_Modifiers.del(function.getModifier(), Re_Modifiers.ACCESS_PERMISSION_MODIFIER),
                        declareReClass,
                        declareReClassInstance
                );
            }
        }



        static Re_ClassFunction createFieldInitFunction(String filePath, Re_VariableMapAsConst constCacheManager,
                                                        int lineOffset,
                                                        @Nullable final Re_Executor declaringExecutor,
                                                        @Nullable Re_Class declaringClass,
                                                        Re_CodeLoader.Expression[] expressions) {

            return Unsafes.createReFunction(filePath, constCacheManager,
                    lineOffset,
                    declaringExecutor, declaringClass, null,
                    Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction.getFunctionModifierEmpty(),
                    null,
                    null, null,
                    null, expressions);
        }

        static Re_ClassFunction createReFunction(@NotNull Re_CodeFile compileFile,
                                                 @Nullable String name, @Nullable String[] paramName,
                                                 @Nullable Re_Executor declaringExecutor, @Nullable Re_Class  declaringReClass, @Nullable Re_ClassInstance declaringReClassInstance) {
            return createReFunction(compileFile.getFilePath(), compileFile.getConstCaches(),
                    compileFile.getLineOffset(),
                    declaringExecutor, declaringReClass, declaringReClassInstance,
                    Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction.getFunctionModifierEmpty(),
                    name,
                    paramName, null,
                    null, compileFile.getExpressions());
        }

        /**
         * modifier必须进过处理
         * @param modifier {@link Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction#getFunctionModifier(boolean, int)}
         */
        @NotNull
        private static Re_ClassFunction createReFunction(@NotNull String filePath, @NotNull Re_VariableMapAsConst constCacheManager,
                                                         @NotNull int lineOffset,
                                                         @Nullable Re_Executor declaringExecutor, @Nullable Re_Class declaringClass, @Nullable Re_ClassInstance declaringClassInstance,

                                                         int modifier,
                                                         @Nullable String functionName,
                                                         @Nullable String[] functionParamNameArray, @Nullable TypeChecker[] functionParamCheckers,
                                                         @Nullable TypeChecker functionReturnChecker,
                                                         @NotNull Re_CodeLoader.Expression[] expressions) {
            if (null == functionParamCheckers && null == functionReturnChecker) {
                Re_ClassFunction reFunction = new Re_ClassFunction();
                String buildFunctionName = Re_ClassFunction.buildReClassFunctionName(lineOffset, functionName, reFunction);
                Re_CodeFile block = Re_CodeFile.create(constCacheManager,
                        buildFunctionName, filePath, lineOffset,
                        expressions);
                return createReCassFunctionAfter(reFunction, block,
                        modifier,
                        buildFunctionName, functionParamNameArray,
                        declaringExecutor,
                        declaringClass, declaringClassInstance);
            } else {
                ParamCheckerFunction reFunction = new ParamCheckerFunction();
                String buildFunctionName = Re_ClassFunction.buildReClassFunctionName(lineOffset, functionName, reFunction);
                Re_CodeFile block = Re_CodeFile.create(constCacheManager,
                        buildFunctionName, filePath, lineOffset,
                        expressions);
                ParamCheckerFunction newFunction = (ParamCheckerFunction) createReCassFunctionAfter(reFunction, block,
                        modifier,
                        buildFunctionName, functionParamNameArray,
                        declaringExecutor,
                        declaringClass, declaringClassInstance);
                newFunction.paramChecker  = null == functionParamCheckers ? Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker.EMPTY_ARRAY     : functionParamCheckers;
                newFunction.returnChecker = null == functionReturnChecker ? Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker.NOT_CHECK_RETURN: functionReturnChecker;
                return newFunction;
            }
        }
    }
    public static class RuntimeUtils {
        /**
         * modifier必须进过处理
         * @param modifier {@link Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction#getFunctionModifier(boolean, int)}
         */
        @NotNull
        static Re_ClassFunction createReFunction(int lineOffset, Re_Executor declaringExecutor,

                                                 int modifier,
                                                 @Nullable String functionName,
                                                 @Nullable String[] functionParamNameArray, @Nullable Re_CodeLoader_ExpressionConverts.CallFunction.FunParamTypesElement[] functionParamTypes,
                                                 @Nullable Re_CodeLoader.Var[] returnType,
                                                 @NotNull Re_CodeLoader.Expression[] expressions) {
            Re_CodeFile      reCodeFile             = declaringExecutor.getReCodeFile();
            Re_Class         declaringClass         = declaringExecutor.getReClass();
            Re_ClassInstance declaringClassInstance = declaringExecutor.getReClassInstance();

            Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker[] functionParamCheckers = Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker.createParamTypesCheckerFromRuntime(declaringExecutor, functionParamTypes);
            if (declaringExecutor.isReturnOrThrow()) return null;

            Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker   functionReturnChecker = Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker.createReturnTypeCheckerFromRuntime(declaringExecutor, returnType);
            if (declaringExecutor.isReturnOrThrow()) return null;

            return Unsafes.createReFunction(reCodeFile.getFilePath(), reCodeFile.getConstCaches(),
                    lineOffset,
                    declaringExecutor,
                    declaringClass,

                    declaringClassInstance,
                    modifier,
                    functionName, functionParamNameArray,
                    functionParamCheckers,
                    functionReturnChecker, expressions);
        }
    }






//
//
//    @Override public final Re_Variable             innerRemoveVariable(Object key) {return null;}
//    @Override public final Re_Variable             innerFindTableOrParentVariable(Object key) {
//        return null;
//    }
//    @Override public final Re_Variable             innerFindTableVariable(Object key) {return null;}
//    @Override public final Re_Variable             innerGetVariable(Object key) {
//        return null;
//    }
//    @Override public final Re_Variable             innerPutVariable(Object key, Re_Variable value) {return null;}
//    @Override public final boolean                 innerContainsVariable(Object key) {return false;}
//    @Override public final int                     innerGetVariableCount() {return 0;}
//    @Override public final Iterable<?>             innerGetVariableKeys() {return null;}
//    @Override public final Re_IRe_VariableMap innerCloneVariableMap() {return null;}
}











    
