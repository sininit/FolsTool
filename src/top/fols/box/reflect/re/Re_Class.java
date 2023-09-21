package top.fols.box.reflect.re;

import top.fols.atri.lang.Classz;
import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.atri.interfaces.annotations.NotNull;

import java.util.*;

import static top.fols.box.reflect.re.Re_ClassFunction.argumentsAsVariableMap;
import static top.fols.box.reflect.re.Re_CodeLoader.SUB_CLASS_SEPARATOR;
import static top.fols.box.reflect.re.Re_CodeLoader.PACKAGE_SEPARATOR_STRING;

/**
 * 理论上来讲这不是一个类 它就像是
 * import java.lang.String; 里的 String, 直接操作String 的功能 比如 String.trim();
 *
 * 你可以称这个{@link Re_Class}为 {类操作}
 * 请勿重写
 */
@SuppressWarnings({"SynchronizeOnNonFinalField", "rawtypes"})
public class Re_Class
        implements Re_IRe_Object, Re_IRe_VariableMap,
                   Re_IReGetDeclaringClass, Re_IReGetClass {
    Re_Class() {}


    private Re_CodeFile             reCodeBlock;
    /**
     * 为空则代表是一个顶级类
     * 本类来自哪个类 一个文件里的第二个类? 也就是说 这是值上一水平的类    创建时复制 不可修改
     */
    private Re_Class                reClassDeclaringClass;
    /**
     * 类名, 不可改
     */
    private String                  reClassName;
    private Re_NativeClassLoader reClassLoader;

    private Re_Executor             parentExecutor;


    private Re_IRe_VariableMap reClassVariable;




    private boolean isAnonymous;
    public boolean isAnonymous() {
        return isAnonymous;
    }





    static Re_Class createAfter(@NotNull Re_Class newReClass,

                                Re_Executor parent,
                                @Nullable String name, @NotNull Re_CodeFile reCodeBlock,
                                @Nullable Re_Class reDeclaringClass) {
        newReClass.reClassDeclaringClass = reDeclaringClass;
        newReClass.isAnonymous = null == name;
        newReClass.reClassName = Re_CodeLoader.intern(buildReClassName(reDeclaringClass, reCodeBlock.getLineOffset(), name, newReClass));
        newReClass.reCodeBlock = reCodeBlock;
        newReClass.parentExecutor = parent;
        newReClass.reClassVariable = Re.newObjectVariableMap();

        newReClass.initializeIsRun = false;
        newReClass.initializedLock = new Object();
        return newReClass;
    }
    @NotNull
    static String buildReClassName(@Nullable Re_Class reDeclaringClass,
                                   @NotNull int line, @Nullable String oldName, @NotNull Re_Class newReClass) {
        if (null == oldName || oldName.length() == 0) {
            return (null == reDeclaringClass ? "" : reDeclaringClass.getName() + SUB_CLASS_SEPARATOR) + (Re_Keywords.INNER_EXPRESSION_CALL__CLASS +"_"+line+"_" + Integer.toHexString(newReClass.hashCode())) ;    //匿名类
        } else {
            return (null == reDeclaringClass ? "" : reDeclaringClass.getName() + SUB_CLASS_SEPARATOR) + oldName;  // 主类 or 子类
        }
    }





    public Re_Class getTopLevelClass() {
        Re_Class reClass = this;
        while (true) {
            Re_Class reDeclareClass = reClass.getReDeclareClass();
            if (reDeclareClass == null) break;
            reClass = reDeclareClass;
        }
        return reClass;
    }




    @Override
    public final Re_Class getReClass() {
        return this;
    }
    @Override
    public final Re_Class getReDeclareClass() {
        return reClassDeclaringClass;
    }


    private String simple_name0;
    public final String getReClassSimpleName() {
        if (null == simple_name0) {
            String name = reClassName;
            int index = name.lastIndexOf(PACKAGE_SEPARATOR_STRING);
            if (index > -1) {
                name = name.substring(index + PACKAGE_SEPARATOR_STRING.length(), name.length());
            }
            int i = name.lastIndexOf(SUB_CLASS_SEPARATOR);
            if (i > -1) {
                name = name.substring(i + SUB_CLASS_SEPARATOR.length(), name.length());
            }
            simple_name0 = Re_CodeLoader.intern(name);
        }
        return simple_name0;
    }

    public final String getReClassPackageName() {
        String name = reClassName;
        int index = name.lastIndexOf(PACKAGE_SEPARATOR_STRING);
        if (index > -1) {
            name = Re_CodeLoader.intern(name.substring(0, index));
        } else {
            name = null;
        }
        return Re_CodeLoader.intern(name);
    }

    @Override
    public final String getName() {
        return reClassName;
    }

    public final Re_NativeClassLoader getReClassLoader() {
        return reClassLoader;
    }
    /**
     * 只是设置 并没有实际意义
     * 类加载器将不会有任何变动
     * 不可用重复执行
     *
     * @param classLoader 类加载器
     */
    final void        setReClassLoader(Re_NativeStack stack, Re_NativeClassLoader classLoader) {
        if (null == this.reClassLoader) {
            this.reClassLoader = classLoader;
        } else {
            stack.setThrow("already resolve loader: " + getName());
        }
    }

    /**
     * 断开类加载器
     */
    final void disconnectReClassLoader() {
        reClassLoader = null;
    }

    /**
     * 获取代码块
     */
    protected final Re_CodeFile getCodeBlock() {
        return reCodeBlock;
    }


















    /**
     * 如果已经初始化过这个类 则返回null 结果
     * 一般用于class函数 和 re_classloader 编译加载类
     */
    @SuppressWarnings("ConstantConditions")
    static void runReClassInitialize0(@NotNull Re host, Re_NativeStack stack, @NotNull Re_CodeFile block,
                                      @NotNull Re_Class re_class) {
        if (stack.isThrow())
            return;  //已经异常

        if (null == re_class) {
            stack.setThrow(Re_Accidents.reclass_initialize_failed(re_class));
            return;
        }

        re_class.initializeStaticObject(host, stack, block);
        //最后一行不需要判断return
    }






    boolean initialized;
    public final boolean isInitialized() {
        return initialized;
    }
    @SuppressWarnings("PointlessBooleanExpression")
    public final boolean isInitialing() {
        return initialized == false;
    }

    /**
     * 不要修改
     */
    Object              initializedLock;
    boolean             initializeIsRun;                //只是执行过不是代表已经初始化成功了
    final Object initializeStaticObject(@NotNull Re host, Re_NativeStack stack, @NotNull Re_CodeFile block) {
        if (stack.isThrow()) return null;

        //该类的 唯一锁
        synchronized (initializedLock) {
            if (initializeIsRun) {
                if (initialized) {
                    return this;//初始化成功
                } else {
                    return null;//初始化失败
                }
            } else {
                Object result = null;
                initializeIsRun = true;

                boolean runOk = false;
                Re_Executor initExecutor;
                try {
                    initExecutor = Re_Executor.createReClassInitializeExecutor(host, stack, block, this);
                    if (null != initExecutor) {  //获取执行器成功
                        if (!initExecutor.isThrow()) {   //没有出错
                            result = initExecutor.run(); //执行所有代码
                            if (!initExecutor.isThrow()) {   //执行所有代码没有出错
                                runOk = true;
                            }
                        }
                    }
                } finally {
                    if (runOk) {
                        initialized = true;
                    }
                }
                if (initialized) {
                    initializedFromExecutor(initExecutor);
                }
                return result;
            }
        }
    }
    //正常的初始化完成的操作
    final void initializedFromExecutor(Re_Executor initExecutor) {
        instanceInitializationStatementList.setInitFieldFunctionFromPrimitiveOrExecutor(initExecutor); //设置field init function
    }


    static void setPrimitiveClassInitialized(Re_PrimitiveClass primitiveClass) {
        synchronized (primitiveClass.initializedLock) {
            primitiveClass.initializeIsRun = true;
            primitiveClass.initialized = true;

            initializedFromPrimitiveClass(primitiveClass);
        }
    }
    //PrimitiveClass初始化完成的操作
    static void initializedFromPrimitiveClass(Re_PrimitiveClass primitiveClass) {
        primitiveClass.instanceInitializationStatementList.setInitFieldFunctionFromPrimitiveOrExecutor(null); //设置field init function
    }






    InstanceInitializationStatementList instanceInitializationStatementList = new InstanceInitializationStatementList();
    class InstanceInitializationStatementList {
        List<Re_CodeLoader.Expression> expressionList = new ArrayList<>();
        /**
         * 必须保证是在类文件中执行的
         * @param expression 类文件 expression
         */
        void addExpression(Re_CodeLoader.Expression expression) {
            expressionList.add(expression);
        }
        boolean isHasExpression() {
            return expressionList.size() > 0;
        }

        void setInitFieldFunctionFromPrimitiveOrExecutor(@Nullable Re_Executor executor) {
            if (isHasExpression()) {
                Re_CodeFile codeBlock = getCodeBlock();
                Re_CodeLoader.Expression[] expressions = expressionList.toArray(Re_CodeLoader.Base.EMPTY_EXPRESSION);
                Re_ClassFunction initFieldFunction = Re_ClassFunction.Unsafes.createFieldInitFunction(codeBlock.getFilePath(), codeBlock.getConstCaches(),
                        codeBlock.getLineOffset(),
                        executor,
                        Re_Class.this,
                        expressions);

                init.setFieldInstaller(initFieldFunction);
            } else {
                init.setFieldInstaller(null);
            }
        }
    }
    public Re_CodeLoader.Expression addInstanceInitializationStatementExpression(Re_CodeLoader.Expression expression) {
        if (!initialized) {
            instanceInitializationStatementList.addExpression(expression);
            return expression;
        }
        return null;
    }







    Re_ClassFunction.Init init = new Re_ClassFunction.Init();
    public final Re_ClassFunction getInitFunction() {
        return init.that();
    }
    public final void setInitFunction(Re_ClassFunction initFunction) {
        if (!initialized) {
            init.setConstructor(initFunction);
        }
    }


























    Re_ClassInstance newUndefinedInstance(Re_Class reClass) {
        return new Re_ClassInstance(reClass);
    }









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
//        if (isTopClass())
//            return Re_CodeLoader.CallCreateClass.wrapTopClass(this);
//        else
//            return Re_CodeLoader.CallCreateClass.wrapSubClass(this);
        return "re-" + Re_Keywords.INNER_EXPRESSION_CALL__CLASS + ": " + getName();
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }





    @Override
    public Re_Variable _variable_remove(Object key) {
        return reClassVariable._variable_remove(key);
    }

    @Override
    public Re_Variable _variable_find_table_or_parent(Object key) {
        Re_Variable re_variable = reClassVariable._variable_find_table_or_parent(key);
        if (null == re_variable)
            if (null != parentExecutor)
                re_variable = parentExecutor._variable_find_table_or_parent(key);
        return re_variable;
    }

    @Override
    public Re_Variable _variable_find_table_var(Object key) {
        return reClassVariable._variable_find_table_var(key);
    }

    @Override
    public Re_Variable _variable_get(Object key) {
        return reClassVariable._variable_get(key);
    }

    @Override
    public Re_Variable _variable_put(Object key, Re_Variable value) {
        return reClassVariable._variable_put(key, value);
    }


    @Override
    public boolean _variable_has(Object key) {
        return reClassVariable._variable_has(key);
    }

    @Override
    public int _variable_key_count() {
        return reClassVariable._variable_key_count();
    }

    @Override
    public Iterable<?> _variable_keys() {
        return reClassVariable._variable_keys();
    }

    @Override
    public Re_IRe_VariableMap _variable_clone_all() {
        return reClassVariable._variable_clone_all();
    }











    //------------------------------------------
    @Override
    public final Object getObjectValue(Re_Executor executor, Object key) throws Throwable {
        return Re_Variable.accessFindTableValue(executor, key, this);
    }
    @Override
    public final boolean hasObjectKey(Re_Executor executor, Object key) throws Throwable {
        return Re_Variable.has(key, this);
    }
    @Override
    public final boolean removeObjectKey(Re_Executor executor, Object key) throws Throwable {
        return Re_Variable.accessRemove(executor, key, this);
    }
    @Override
    public final void putObjectValue(Re_Executor executor, Object key, Object value) throws Throwable {
        Re_Variable.accessSetValue(executor, key, value, this);
    }

    @Override
    public final int getObjectKeyCount(Re_Executor executor) throws Throwable {
        return Re_Variable.size(this);
    }

    @Override
    public final @NotNull Iterable getObjectKeys(Re_Executor executor) throws Throwable {
        return Re_Variable.key(this );
    }

    @Override
    public boolean hasObjectKeys() { return true; }


    @Override
    public final Object executePoint(Re_Executor executor, Object point_key, Re_CodeLoader.Call call) throws Throwable {
        Object funCall = Re_Variable.accessFindTableValue(executor, point_key, this);
        if (executor.isReturnOrThrow()) return null;

        if (Re_Utilities.isReFunction(funCall)) {
            Re_ClassFunction reClassFunction = (Re_ClassFunction) funCall;

            Object[] arguments = executor.getExpressionValues(call);
            if (executor.isReturnOrThrow()) return null;

            //为null 执行时自动生成
            return authenticationAndExecuteOnRe(executor, reClassFunction, arguments, null);
        } else {
            if (null == funCall) {
                String s = Re_Utilities.toJString(point_key);
                executor.setThrow(Re_Accidents.undefined(this, s));
                return null;
            } else {
                return executor.executeCallThis(funCall, call);
            }
        }
    }
    @Override
    public final Re_ClassInstance executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
        Object[] arguments = executor.getExpressionValues(call);
        if (executor.isReturnOrThrow()) return null;

        return SafesRe.createInstance(executor, this, arguments, null);
    }






    //指定某个实例运行特定方法, 这个方法不一定是在这个类声明的 但是可以强制运行
    final Object authenticationAndExecuteOnRe(Re_Executor executor, Re_ClassFunction function, Object[] arguments, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {//no permissions
        //最后一行 不需要判断return
        return function.authenticationAndExecuteOnRe(executor, this, null, arguments, variableMap);
    }

    //------------------------------------------







    //不安全
    @SuppressWarnings({"UnusedReturnValue"})
    static final class UnsafesRe {
        static void setClassValue(Re_Class reClass, Object key, Object value) {
            if (null == reClass) return;
            Re_Variable.UnsafesRe.fromUnsafeAccessorSetValueIntern(key, value, reClass);
        }
        static Object getClassValue(@Nullable Re_Class reClass, Object key) {
            if (null != reClass) {
                return Re_Variable.UnsafesRe.fromUnsafeAccessorGetClassValue(key, reClass);
            } else {
                return null;
            }
        }

        static void setInstanceValue(Re_ClassInstance instance, Object key, Object value) {
            if (null == instance)  return;
            Re_Variable.UnsafesRe.fromUnsafeAccessorSetValueIntern(key, value, instance);
        }
        static Object getInstanceOrClassValue(@Nullable Re_ClassInstance instance, Object key) {
            if (null != instance) {
                return Re_Variable.UnsafesRe.fromUnsafeAccessorGetInstanceOrClassValue(key, instance);
            } else {
                return null;
            }
        }

        static Re_ClassFunction getFunctionFromClass(Re_Class reClass, Object key) {
            Object v = getClassValue(reClass, key);

            if (Re_Utilities.isReFunction(v)) {
                return (Re_ClassFunction) v;
            }
            return null;
        }
        static Re_ClassFunction getFunctionFromInstanceOrClass(Re_ClassInstance instance, Object key) {
            Object v = getInstanceOrClassValue(instance, key);

            if (Re_Utilities.isReFunction(v)) {
                return (Re_ClassFunction) v;
            }
            return null;
        }

        static Object executeFunction(Re re, Re_NativeStack stack,
                                      Re_ClassFunction reClassFunction,
                                      Re_Class runInClass, Re_ClassInstance runInInstance,
                                      Object[] arguments,  Re_IRe_VariableMap functionLocal) throws Throwable {
            if (null == reClassFunction) return null;
            if (null == functionLocal)   functionLocal = argumentsAsVariableMap(reClassFunction, arguments);

            Re_ClassFunction.InnerAccessor innerAccessor = reClassFunction.getInnerAccessor();
            //获取一个不检测权限的 function. 不再检测权限 （修改方法声明类 和声明实例）
            Re_ClassFunction function     = innerAccessor.getDeletePermissionDetectionFunction(reClassFunction,
                                                                                     runInClass, runInInstance);

            //这里随便一个executor都可以
            Re_Executor      baseExecutor = Re_Executor.createReRootExecutor(re, stack,
                    function.getReCodeBlock(),
                    null, null);
            if (stack.isThrow()) return null;

            return function.authenticationAndExecuteOnRe(baseExecutor,
                    runInClass, runInInstance,
                    arguments,  functionLocal);
        }

        /**
         * @see Re_ClassFunction#authenticationAndExecuteOnRe_FromDeclare(Re_Executor, Object[], Re_IRe_VariableMap)
         */
        static Object executeFunction(Re re, Re_NativeStack stack,
                                      Re_ClassFunction reClassFunction,
                                      Object[] arguments, Re_IRe_VariableMap functionLocal) throws Throwable {
            if (null == reClassFunction) return null;

            return executeFunction(re, stack,
                    reClassFunction, reClassFunction.getReDeclareClass(), reClassFunction.getReDeclareReClassInstance(),
                    arguments, functionLocal);
        }

        /**
         * @param params 参数
         * @param variableMap 参数表 为null时 自动根据 params生成对于方法执行器的参数表
         */
        static Re_ClassInstance createInstance(Re re, Re_NativeStack stack,
                                               Re_Class reClassz,
                                               Object[] params, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            if (null == reClassz) {
                stack.setThrow("reClass is null");
                return null;
            }
            Re_ClassInstance undefinedInstance     = reClassz.newUndefinedInstance(reClassz);

            Re_ClassFunction initFunction = reClassz.getInitFunction();
            if (null == initFunction) {
                return  undefinedInstance;
            }
            executeFunction(re, stack,
                    initFunction, reClassz, undefinedInstance,
                    params, variableMap);

            return undefinedInstance;
        }
    }


    /**
     * 在Java里执行
     * 不安全
     */
    static final class Unsafes {
        static void setClassValueOrThrowEx(Re_Class reClass, Object key, Object value) {
            if (null == reClass) return;
            Re_Variable.Unsafes.fromUnsafeAccessorSetValueInternOrThrowEx(key, value, reClass);
        }
        static Object getClassValueOrThrowEx(@Nullable Re_Class reClass, Object key) {
            if (null != reClass) {
                return Re_Variable.Unsafes.fromUnsafeAccessorGetClassValueOrThrowEx(key, reClass);
            } else {
                return null;
            }
        }

        static void setInstanceValueOrThrowEx(Re_ClassInstance instance, Object key, Object value) {
            if (null == instance)  return;
            Re_Variable.Unsafes.fromUnsafeAccessorSetValueInternOrThrowEx(key, value, instance);
        }
        static Object getInstanceOrClassValueOrThrowEx(@Nullable Re_ClassInstance instance, Object key) {
            if (null != instance) {
                return Re_Variable.Unsafes.fromUnsafeAccessorGetInstanceOrClassValueOrThrowEx(key, instance);
            } else {
                return null;
            }
        }

        static Re_ClassFunction getFunctionFromClassOrThrowEx(Re_Class reClass, Object key) {
            Object v = getClassValueOrThrowEx(reClass, key);

            if (Re_Utilities.isReFunction(v)) {
                return (Re_ClassFunction) v;
            }

            String s = Re_Utilities.objectAsString(key);
            throw new IllegalArgumentException("[" + Re_Utilities.objectAsName(v) + "] is not a ReFunction: " + s);
        }
        static Re_ClassFunction getFunctionFromInstanceOrClassOrThrowEx(Re_ClassInstance instance, Object key) {
            Object v = getInstanceOrClassValueOrThrowEx(instance, key);

            if (Re_Utilities.isReFunction(v)) {
                return (Re_ClassFunction) v;
            }

            String s = Re_Utilities.objectAsString(key);
            throw new IllegalArgumentException("[" + Re_Utilities.objectAsName(v) + "] is not a ReFunction: " + s);
        }



        /**
         * 这里会创建两次执行器
         * @param params 参数
         * @param variableMap 参数表 为null时 自动根据 params生成对于方法执行器的参数表
         */
        static Object executeFunctionOrThrowEx(Re re, Re_NativeStack stack,
                                               Re_Class reClass, Re_ClassInstance reClassInstance, @NotNull Re_ClassFunction invokeFunction,
                                               Object[] params, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            Object o = UnsafesRe.executeFunction(re, stack,
                    invokeFunction, reClass, reClassInstance,
                    params, variableMap);
            Re.throwStackException(stack);
            return o;
        }
        static Object executeFunctionOrThrowEx(Re re, Re_NativeStack stack,
                                               Re_ClassFunction invokeFunction,
                                               Object[] params, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            Object o = UnsafesRe.executeFunction(re, stack,
                    invokeFunction,
                    params, variableMap);
            Re.throwStackException(stack);
            return o;
        }

        /**
         * @param params 参数
         * @param variableMap 参数表 为null时 自动根据 params生成对于方法执行器的参数表
         */
        static Re_ClassInstance createInstanceOrThrowEx(Re re, Re_NativeStack stack,
                                                        Re_Class reClassz,
                                                        Object[] params, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            Re_ClassInstance instance = UnsafesRe.createInstance(re, stack, reClassz, params, variableMap);
            Re.throwStackException(stack);
            return instance;
        }


        static void ____________(){}

        /**
         * 直接将代码运行在类中
         *
         * @param parent          父执行器 可空
         * @param re              主体
         * @param reClass         类
         * @param reClassInstance 类实例
         * @param expression      方法内的表达式
         */
        static Object runOnReClassOrThrowEx(@Nullable Re_Executor parent,
                                            Re re, Re_NativeStack stack,
                                            @Nullable Re_Class reClass, @Nullable Re_ClassInstance reClassInstance,
                                            String expression, Object[] params) throws Throwable {
            Re_ClassFunction function = createReClassFunctionOrThrowEx(re, parent, reClass, reClassInstance, expression);
            return executeFunctionOrThrowEx(re, stack,
                    reClass, reClassInstance, function,
                    params, null);
        }



        /**
         * 创建一个实例方法
         *
         * @param reClass           类
         * @param reClassInstance   类实例
         * @param expression        方法代码不需要声明参数之类的
         */
        static Re_ClassFunction createReClassFunctionOrThrowEx(Re re,
                                                               Re_Executor parent,
                                                               Re_Class reClass, Re_ClassInstance reClassInstance,
                                                               String expression) {
            Re_CodeFile code = re.compileJavaSource(expression);
            return Re_ClassFunction.Unsafes.createReFunction(code,
                    null,null,
                    parent, reClass, reClassInstance);
        }


        /**
         * 创建顶级类或者子类
         *
         * @param name             确定的类名
         * @param reCodeBlock      代码
         * @param reDeclaringClass 所属的类，也就是说是谁的子类
         */
        static Re_Class createReClass(Re_Executor parent,
                                      @Nullable String name, @NotNull Re_CodeFile reCodeBlock,
                                      Re_Class reDeclaringClass) {
            return createAfter(new Re_Class(), parent, name, reCodeBlock, reDeclaringClass);
        }
    }


    @SuppressWarnings("UnnecessaryLocalVariable")
    public static class SafesRe {
        public static void setClassValue(Re_Executor executor,
                                         Re_Class reClass,
                                         Object key, Object value) {
            if (null == reClass) return;

            Re_Variable.accessSetValue(executor,
                    key, value,
                    reClass);
        }
        public static Object getClassValue(Re_Executor executor,
                                           @Nullable Re_Class reClass, Object key) {
            if (null != reClass) {
                Object o = Re_Variable.accessGetClassValue(executor, key,
                        reClass);
                return o;
            } else {
                return null;
            }
        }

        public static void setInstanceValue(Re_Executor executor,
                                            Re_ClassInstance instance,
                                            Object key, Object value) {
            if (null == instance) return;

            Re_Variable.accessSetValue(executor,
                    key, value,
                    instance);
        }
        public static Object getInstanceOrClassValue(Re_Executor executor,
                                                     @Nullable Re_ClassInstance instance, Object key) {
            if (null != instance) {
                Object o = Re_Variable.accessGetInstanceOrClassValue(executor, key,
                        instance);
                return o;
            } else {
                return null;
            }
        }



        public static Re_ClassFunction getFunctionFromClass(Re_Executor executor,
                                                            Re_Class reClass, Object key) {
            Object v = getClassValue(executor,
                    reClass, key);
            if (executor.isReturnOrThrow()) return null;

            if (Re_Utilities.isReFunction(v)) {
                return (Re_ClassFunction) v;
            } else {
                String s = Re_Utilities.toJString(key);
                executor.setThrow("[" + Re_Utilities.objectAsName(v) + "] is not a ReFunction: " + s);
                return null;
            }
        }
        public static Re_ClassFunction getFunctionFromInstanceOrClass(Re_Executor executor,
                                                                      Re_ClassInstance instance, Object key) {
            Object v = getInstanceOrClassValue(executor,
                    instance, key);
            if (executor.isReturnOrThrow()) return null;

            if (Re_Utilities.isReFunction(v)) {
                return (Re_ClassFunction) v;
            } else {
                String s = Re_Utilities.toJString(key);
                executor.setThrow("[" + Re_Utilities.objectAsName(v) + "] is not a ReFunction: " + s);
                return null;
            }
        }


        public static Re_ClassInstance createInstance(Re_Executor executor,
                                                      Re_Class reClasz,
                                                      Object[] params, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            if (null == reClasz) return null;

            Re_ClassInstance undefinedInstance     = reClasz.newUndefinedInstance(reClasz);

            Re_ClassFunction initFunction = reClasz.getInitFunction();
            if (null == initFunction) {
                return  undefinedInstance;
            }
            initFunction.authenticationAndExecuteOnRe(executor,
                    reClasz, undefinedInstance,
                    params, variableMap);
            if (executor.isReturnOrThrow()) return null;
            return undefinedInstance;
        }



        public static Object executeReClassFunction(Re_Executor executor,
                                                    Re_Class reClass, Object functionName,
                                                    Object[] args, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            if (null == reClass) {
                executor.setThrow("reClass is null");
                return null;
            }

            Re_ClassFunction function = getFunctionFromClass(executor, reClass, functionName);
            if (executor.isReturnOrThrow()) return null;

            return reClass.authenticationAndExecuteOnRe(executor, function, args, variableMap);
        }
        @SuppressWarnings("ConstantConditions")
        public static Object executeInstanceOrClassFunction(Re_Executor executor,
                                                            Re_ClassInstance reClassInstance, Object functionName,
                                                            Object[] args, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            if (null == reClassInstance) {
                executor.setThrow("reClassInstance is null");
                return null;
            }

            Re_ClassFunction function = getFunctionFromInstanceOrClass(executor, reClassInstance, functionName);
            if (executor.isReturnOrThrow()) return null;

            return reClassInstance.authenticationAndExecuteOnRe(executor, function, args, variableMap);
        }




        public static Object executeFunction(Re_Executor executor,
                                             Re_ClassFunction function,
                                             Object[] args, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            if (null == function)
                return null;

            return function.authenticationAndExecuteOnRe_FromDeclare(executor, args, variableMap);
        }
    }
    public static class Safes {
        @SuppressWarnings("SameParameterValue")
        static Re_PrimitiveClass_exception.Instance createInstance_exception(Re re,
                                                                             Re_Class reClass,
                                                                             String throwReason, Re_NativeStack stack) {
            Re_ClassInstance instance = re.java.newInstanceOrThrowEx(reClass,
                    Re_PrimitiveClass_exception.buildNewInstanceParameters(throwReason, false));
            if (Re_Utilities.isReClassInstance_exception(instance)) {
                Re_PrimitiveClass_exception.Instance exception = (Re_PrimitiveClass_exception.Instance) instance;
                Re_NativeStack.fillExceptionInstanceTraceElements(exception, stack);
                return exception;
            } else {
                throw new Re_Accidents.RuntimeInternalError("unsupported exception instance type: " + Classz.getName(instance));//理论上不可能
            }
        }



        public static Re_Executor createExecutorOrThrowEx(Re re, Re_NativeStack stack) {
            return Re_Executor.createReRootExecutor(re, stack,
                       Re_CodeFile.create(Re_CodeFile.METHOD_NAME__EMPTY, Re_CodeFile.FILE_NAME__ANONYMOUS, Re_CodeFile.LINE_OFFSET),
                    null, null);
        }


        public static void setClassValueOrThrowEx(Re re, Re_NativeStack stack,
                                                  Re_Class reClass,
                                                  Object key, Object value) {
            setClassValueOrThrowEx(createExecutorOrThrowEx(re, stack),
                    reClass,
                    key, value);
        }
        public static void setClassValueOrThrowEx(Re_Executor anonymousExecutor,
                                                  Re_Class reClass,
                                                  Object key, Object value) {
            SafesRe.setClassValue(anonymousExecutor,
                    reClass,
                    key, value);
            Re.throwStackException(anonymousExecutor);
        }
        public static Object getClassValueOrThrowEx(Re re, Re_NativeStack stack,
                                                    Re_Class reClass, Object key) {
            return getClassValueOrThrowEx(createExecutorOrThrowEx(re, stack),
                    reClass, key);
        }
        public static Object getClassValueOrThrowEx(Re_Executor executor,
                                                    Re_Class reClass, Object key) {
            Object result = SafesRe.getClassValue(executor, reClass, key);
            Re.throwStackException(executor);
            return result;
        }


        public static void setInstanceValueOrThrowEx(Re re, Re_NativeStack stack,
                                                     Re_ClassInstance instance,
                                                     Object key, Object value) {
            setInstanceValueOrThrowEx(createExecutorOrThrowEx(re, stack),
                    instance,
                    key, value);
        }
        public static void setInstanceValueOrThrowEx(Re_Executor anonymousExecutor,
                                                     Re_ClassInstance instance,
                                                     Object key, Object value) {
            SafesRe.setInstanceValue(anonymousExecutor,
                    instance,
                    key, value);
            Re.throwStackException(anonymousExecutor);
        }
        public static Object getInstanceOrClassValueOrThrowEx(Re re, Re_NativeStack stack,
                                                              Re_ClassInstance instance, Object key) {
            return getInstanceOrClassValueOrThrowEx(createExecutorOrThrowEx(re, stack),
                    instance, key);
        }
        public static Object getInstanceOrClassValueOrThrowEx(Re_Executor executor,
                                                              Re_ClassInstance instance, Object key) {
            Object result = SafesRe.getInstanceOrClassValue(executor, instance, key);
            Re.throwStackException(executor);
            return result;
        }

        public static Re_ClassFunction getFunctionFromClassOrThrowEx(Re re, Re_NativeStack stack,
                                                                     Re_Class reClass, Object key) {
            return getFunctionFromClassOrThrowEx(createExecutorOrThrowEx(re, stack),
                    reClass,
                    key);
        }
        public static Re_ClassFunction getFunctionFromClassOrThrowEx(Re_Executor executor,
                                                                     Re_Class reClass, Object key) {
            Re_ClassFunction functionValue = SafesRe.getFunctionFromClass(executor, reClass, key);
            Re.throwStackException(executor);
            return functionValue;
        }
        public static Re_ClassFunction getFunctionFromInstanceOrClassOrThrowEx(Re re, Re_NativeStack stack,
                                                                               Re_ClassInstance instance, Object key) {
            return getFunctionFromInstanceOrClassOrThrowEx(createExecutorOrThrowEx(re, stack),
                    instance,
                    key);
        }
        public static Re_ClassFunction getFunctionFromInstanceOrClassOrThrowEx(Re_Executor executor,
                                                                               Re_ClassInstance instance, Object key) {
            Re_ClassFunction functionValue = SafesRe.getFunctionFromInstanceOrClass(executor, instance, key);
            Re.throwStackException(executor);
            return functionValue;
        }


        /**
         * @param params 参数
         * @param variableMap 参数表 为null时 自动根据 params生成对于方法执行器的参数表
         */
        public static Re_ClassInstance createInstanceOrThrowEx(Re re, Re_NativeStack stack,
                                                               Re_Class reClassz,
                                                               Object[] params, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            return createInstanceOrThrowEx(createExecutorOrThrowEx(re, stack),
                    reClassz,
                    params, variableMap);
        }

        public static Re_ClassInstance createInstanceOrThrowEx(Re_Executor executor,
                                                               Re_Class reClasz,
                                                               Object[] params, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            Re_ClassInstance instance = SafesRe.createInstance(executor, reClasz,
                    params, variableMap);
            Re.throwStackException(executor);
            return instance;
        }


        public static Object executeClassFunctionOrThrowEx(Re re, Re_NativeStack stack,
                                                           Re_Class reClass, Object functionName,
                                                           Object[] args, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {

            return executeClassFunctionOrThrowEx(createExecutorOrThrowEx(re, stack),
                    reClass, functionName,
                    args, variableMap);
        }
        public static Object executeClassFunctionOrThrowEx(Re_Executor executor,
                                                           Re_Class reClass, Object functionName,
                                                           Object[] args, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            Object ret = SafesRe.executeReClassFunction(executor, reClass, functionName, args, variableMap);
            Re.throwStackException(executor);
            return ret;
        }

        public static Object executeInstanceOrClassFunctionOrThrowEx(Re re, Re_NativeStack stack,
                                                                     Re_ClassInstance reClassInstance, Object functionName,
                                                                     Object[] args, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {

            return executeInstanceOrClassFunctionOrThrowEx(createExecutorOrThrowEx(re, stack),
                    reClassInstance, functionName,
                    args, variableMap);
        }
        public static Object executeInstanceOrClassFunctionOrThrowEx(Re_Executor executor,
                                                                     Re_ClassInstance reClassInstance, Object functionName,
                                                                     Object[] args, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            Object ret = SafesRe.executeInstanceOrClassFunction(executor, reClassInstance, functionName, args, variableMap);
            Re.throwStackException(executor);
            return ret;
        }



        @SuppressWarnings("UnusedReturnValue")
        public static Object executeFunctionOrThrowEx(Re re, Re_NativeStack stack,
                                                      Re_ClassFunction function,
                                                      Object[] args, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            return executeFunctionOrThrowEx(createExecutorOrThrowEx(re, stack),
                    function,
                    args, variableMap);
        }
        public static Object executeFunctionOrThrowEx(Re_Executor executor,
                                                      Re_ClassFunction function,
                                                      Object[] args, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {
            Object o = SafesRe.executeFunction(executor, function, args, variableMap);
            Re.throwStackException(executor);
            return o;
        }
    }








    @SuppressWarnings("SameParameterValue")
    public static class RuntimeUtils {
        /**
         * 运行时动态创建
         */
        @NotNull
        public static Re_Class createReClassAndInitializeSetLocalVar(@NotNull  final Re_Executor executor,
                                                                     @Nullable String simpleName,
                                                                     @NotNull  int lineOffset,
                                                                     @NotNull  Re_CodeLoader.Expression[] expressions) {
            Re_CodeFile block = Re_CodeFile.create(executor.reCodes.getConstCaches(),
                                                   Re_CodeFile.METHOD_NAME__CLASS_INIT, executor.reCodes.getFilePath(), lineOffset,
                                                   expressions);
            //内部类
            Re_Class reDeclaringClass = executor.reClass;
            Re_Class newClass = Unsafes.createReClass(executor, simpleName, block, reDeclaringClass);

            if (null != reDeclaringClass) {
                Re_NativeStack stack = executor.getStack();
                newClass.setReClassLoader(stack, reDeclaringClass.getReClassLoader());
                if (stack.isThrow()) return null;
            }

            if (null != simpleName) {
                Re_Variable.accessSetValue(executor, simpleName, newClass, executor);
                if (executor.isReturnOrThrow()) return null;
            }

            runReClassInitialize0(executor.re, executor.getStack(), block, newClass);
            if (executor.isReturnOrThrow()) return null;

            return newClass;
        }
    }



    public static boolean isInstanceOf(Re_Class from, Re_Class to) {
        if (null != from && null != to) {
            return  from == to;
        }
        return false;
    }
    public static boolean isInstanceOf(Object instance, Re_Class c) {
        if (instance instanceof Re_ClassInstance) {
            Re_ClassInstance obj = (Re_ClassInstance) instance;
            return obj.reClass == c;
        }
        return false;
    }
    public static boolean isInstanceOfNullable(Object param, Re_Class c) {
        if (null == param) {
            return true;
        } else {
            if (param instanceof Re_ClassInstance) {
                Re_ClassInstance obj = (Re_ClassInstance) param;
                return obj.reClass == c;
            }
            return false;
        }
    }
}







