package top.fols.box.reflect.re;

import top.fols.atri.lang.Finals;
import top.fols.box.lang.Throwables;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.interfaces.annotations.Nullable;

import java.lang.reflect.Array;

import static top.fols.box.reflect.re.Re_Accidents.Errors.error_out_of_memory;
import static top.fols.box.reflect.re.Re_CodeLoader.*;


/**
 * 只要执行 {@link Re_Executor} 的方法
 * 后都要手动检测是否 {@link Re_Executor#isReturnOrThrow()} 如果是直接返回null
 *
 *
 * 一个 {@link Re_Executor}不能被多个线程同时执行 因为这是非常不安全的
 * 一个 {@link Re_NativeStack}   不能被多个线程同时使用 因为这是非常不安全的
 * 会变得很糟糕
 */
@SuppressWarnings({"rawtypes", "FieldCanBeLocal", "SameParameterValue", "UnusedReturnValue", "CloneableClassWithoutClone"})
public abstract class Re_Executor
        implements Re_IRe_Object, Re_IRe_VariableMap,
        Cloneable {
    //常量
    final protected Re                            re;
    final protected Re_CodeFile                   reCodes;

    final protected Re_IJavaReflector             reReflector;

    final protected Re_Class                      reClass;
    final protected Re_ClassInstance              reClassInstance; // 对象实例, 不能为空, 也不能是static实例



    //需要新创建
    private final Re_NativeStack                                reStack;
    private int                                                 reStackCurrentIndex;
    private Re_NativeStack.ReNativeTraceElement reStackCurrentElement;


    //使用这个变量主要是 因为有eval执行器 必须要引用真实的执行器的变量

    //private
    @NotNull
    protected Re_Executor  mThis;
    //private
    protected  boolean     mIsReturn;
    //private
    protected Object       mResult;



    /**
     * @param re            主机
     * @param stack           栈
     * @param reCodes         代码
     * @param re_class        类        其实可以为空
     * @param reClassInstance 类实例     其实可以为空
     */
    private Re_Executor(@NotNull Re re, @NotNull Re_NativeStack stack, @NotNull Re_CodeFile reCodes,
                        @Nullable Re_Class re_class, @Nullable Re_ClassInstance reClassInstance) {
        this.re = re;
        this.reReflector     = re.reflector;
        this.reCodes         = reCodes;
        this.reClass         = re_class;
        this.reClassInstance = reClassInstance;

        this.reStack = stack;
        this.reStackCurrentElement = null;
        this.reStackCurrentIndex = Re_NativeStack.INDEX_REMOVED;
    }





    /**
     * print(a);
     * {@link Re_IRe_VariableMap#_variable_find_table_or_parent(Object)} 一般是在 Executor 执行过程中获取（局部变量 或者向上查找）的时候执行的	 *
     *
     *
     * use {@link Re_Variable#accessFindTableOrParentValueRequire}
     */
    public abstract Object localValue(String key);

    /**
     * x = tip;
     * 如果异常请抛出java异常 而不是 setThrow
     */
    public abstract Object localValue(String key, Object value);


    public abstract Object[] getArguments();



    public String getFilePath() {
        return reCodes.getFilePath();
    }
    Re_CodeFile getReCodeFile(){
        return reCodes;
    }

    protected Re                getRe() {
        return re;
    }
    @Nullable
    public Re_Class             getReClass() {
        return reClass;
    }
    @Nullable
    public Re_ClassInstance     getReClassInstance() {
        return reClassInstance;
    }

    protected Re_NativeStack    getStack() {
        return reStack;
    }
    protected Re_NativeStack.ReNativeTraceElement getStackElement() {
        return reStackCurrentElement;
    }























    /**
     * 禁止从外部访问本方法
     * 执行前不可以已经return状态, 如果执行时return 则会返回return结果
     * 本办法只会从{@link Re_Executor#executeExpressions0(Expression, int)} 访问
     *
     * xx.xx
     */
    public Object getPointVariableValue(@NotNull Object instance, @NotNull Base current, String currentName) throws Throwable {
        if (instance instanceof Re_IRe_Object) {
            Object value = ((Re_IRe_Object) instance).getObjectValue(this, currentName);
            if (mThis.mIsReturn || reStack.throwed) return mThis.mResult;
            return value;
        } else {
            Object value = Re_Utilities.getJavaValue(this,  instance.getClass(), instance, currentName);
            if (mThis.mIsReturn || reStack.throwed) return mThis.mResult;
            return value;
        }
    }

    /**
     * 禁止从外部访问本方法
     * 执行前不可以已经return状态, 如果执行时return 则会返回return结果
     * 本办法只会从{@link Re_Executor#executeExpressions0(Expression, int)} 访问
     *
     * xx.xx = ?
     */
    public Object setPointVariableValue(@NotNull Object instance, @NotNull Base current, @NotNull String currentName, Object value) throws Throwable {
        if (instance instanceof Re_IRe_Object) {
            ((Re_IRe_Object) instance).putObjectValue(this, currentName, value);
            if (mThis.mIsReturn || reStack.throwed) return mThis.mResult;
            return value;
        } else {
            Re_Utilities.setJavaValue(this,  instance.getClass(), instance, currentName, value);
            if (mThis.mIsReturn || reStack.throwed) return mThis.mResult;
            return value;
        }
    }




    /**
     * 执行后应该判断  {@link #isReturnOrThrow()} ()} 如果返回真就不要再继续执行你的代码了 直接返回 {@link #getResult()}
     *
     * 禁止从外部访问本方法
     * 执行前不可以已经return状态, 如果执行时return 则会返回return结果
     * 本办法只会从{@link Re_Executor#executeExpressions0(Expression, int)} 访问
     *
     * obj.point_var_name()
     * obj is obj-tip
     * point_var_name is point_var_name
     */
    public Object executeCallPoint(@NotNull Object obj, Object point_var_name, Call call) throws Throwable {
        if (obj instanceof Re_IRe_Object) {
            Object o = ((Re_IRe_Object) obj).executePoint(this, point_var_name, call);
            if (mThis.mIsReturn || reStack.throwed) return mThis.mResult;//throw or return
            return o;
        } else {
            Object[] param = getExpressionValues(call, 0, call.getParamExpressionCount());
            if (mThis.mIsReturn || reStack.throwed) return mThis.mResult;//throw or return

            String s = Re_Utilities.toJString(point_var_name);
            return Re_Utilities.invokeJavaMethod(this, obj, s, param);
        }
    }

    /**
     * 执行后应该判断  {@link #isReturnOrThrow()} ()} 如果返回真就不要再继续执行你的代码了 直接返回 {@link #getResult()}
     *
     * 禁止从外部访问本方法
     * 执行前不可以已经return状态, 如果执行时return 则会返回return结果
     * 本办法只会从{@link Re_Executor#executeExpressions0(Expression, int)} 访问
     *
     * var_name()
     *
     * instance is var_name-tip
     */
    public Object executeCallThis(@NotNull Object instance, @NotNull Call call) throws Throwable {
        if (instance instanceof Re_IRe_Object) {
            //re reflect
            Re_IRe_Object vp = ((Re_IRe_Object) instance);
            Object o = vp.executeThis(this, call);
            if (mThis.mIsReturn || reStack.throwed) return mThis.mResult;//throw or return
            return o;
        } else {
            if (instance.getClass().isArray()) {
                //操作java数组
                return executeGVFromJavaArray(instance, call);
            }
            this.setThrow(Re_Accidents.cannot_execute(Re_Utilities.objectAsName(instance)));
            return null;
        }
    }
















    /**
     * variable_map()               获取长度 {@link Re_IRe_VariableMap#_variable_key_count()} <br>
     * variable_map(key)            获取数据 {@link Re_IRe_VariableMap#_variable_find_table_or_parent(Object)} <br>
     * variable_map(key， tip)     设置数据 {@link Re_IRe_VariableMap#_variable_put(Object, Re_Variable)} <br>
     *
     * 本方法是用于外部扩展访问的 本执行器不会访问该方法 <br>
     * 执行后应该判断  {@link Re_Executor#isReturnOrThrow()} ()} 如果返回真就不要再继续执行你的代码了 直接返回 {@link Re_Executor#getResult()} <br>
     *
     * 区别是不用code参数, 可能会抛出Java异常 <br>
     */
    public Object executeGVFromReObject(@NotNull Re_IRe_Object instance, @NotNull Call call) throws Throwable {
        int paramExpressionCount = call.getParamExpressionCount();
        Object key, value;
        switch (paramExpressionCount) {
            case 0:
                return instance.getObjectKeyCount(this);
            case 1:
                key = getExpressionValue(call,0);
                if (mThis.mIsReturn || reStack.throwed) return null;//throw or return

                return instance.getObjectValue(this, key);
            case 2:
                key = getExpressionValue(call,0);
                if (mThis.mIsReturn || reStack.throwed) return null;//throw or return

                value = getExpressionValue(call,1);
                if (mThis.mIsReturn || reStack.throwed) return null;//throw or return

                instance.putObjectValue(this, key, value); //强制提交 不 使用 intern
                return value;
        }
        return null;
    }
    /**
     * 禁止从外部访问本方法
     * 执行前不可以已经return状态, 如果执行时return 则会返回return结果
     * 本办法只会从{@link Re_Executor#executeExpressions0(Expression, int)} 访问
     */
    public Object executeGVFromJavaArray(@NotNull Object instance, @NotNull Call call) {
        int paramExpressionCount = call.getParamExpressionCount();
        Object obj;
        switch (paramExpressionCount) {
            case 0:
                return Array.getLength(instance);
            case 1:
                obj = getExpressionValue(call,0);
                if (mThis.mIsReturn || reStack.throwed) return null;//throw or return

                return Array.get(instance, Re_Utilities.toJInt(obj));
            case 2:
                obj = getExpressionValue(call,0);
                if (mThis.mIsReturn || reStack.throwed) return null;//throw or return

                Object value = getExpressionValue(call,1);
                if (mThis.mIsReturn || reStack.throwed) return null;//throw or return

                Array.set(instance, Re_Utilities.toJInt(obj), value);
                return value;
        }
        return null;
    }
















    /**
     * 不安全的操作，必须严格使用本方法
     * 如果之前已经return 会抛出java异常, 如果执行时return 则会返回return结果
     * 执行后应该判断  {@link #isReturnOrThrow()} ()} 如果返回真就不要再继续执行你的代码了 直接返回 {@link #getResult()}
     *
     * 如果这里使用了另一个文件的Call 栈文件也不会变化，只有行号会变化， 慎用
     *
     * @param call call
     * @param index no check
     */
    public Object getExpressionValue(Call call, int index) {
        Expression expression = call.getBuildParamExpressionCache(index);
        return this.executeExpressions0(expression, 0);
    }

    /**
     * 如果这里使用了另一个文件的Call 栈文件也不会变化，只有行号会变化， 慎用
     */
    public Object[] getExpressionValues(Call call) {
        return getExpressionValues(call, 0, call.getParamExpressionCount());
    }
    /**
     * 不安全的操作，必须严格使用本方法
     * 获取参数转为java 对象， 如果之前已经return 会抛出java异常, 如果执行时return 则会中断获取并返回已经读取到的结果
     * 执行后应该判断  {@link #isReturnOrThrow()} ()} 如果返回真就不要再继续执行你的代码了 直接返回 {@link #getResult()}
     *
     * 本方法为内部方法
     * 如果这里使用了另一个文件的Call 栈文件也不会变化，只有行号会变化， 慎用
     *
     * @param call call         表达式的集合 (Expression, Expression, Expression...)
     * @param off, no check
     * @param len, no check
     */
    public Object[] getExpressionValues(Call call, int off, int len) {
        if (mThis.mIsReturn || reStack.throwed) return null;//throw or return

        Object[] paramList = new Object[len];
        Expression[] nowParam = call.getBuildParamExpressionCaches();
        for (int i = 0; i < len; i++) {
            Expression expression = nowParam[i+off];
            paramList[i] = this.executeExpressions0(expression, 0);
            if (mThis.mIsReturn || reStack.throwed) return null;//throw or return
        }
        //returned?
        return paramList;
    }



    /**
     * 不安全的操作，必须严格使用本方法
     * 如果之前已经return 会抛出java异常, 如果执行时return 则会返回return结果
     * 执行后应该判断  {@link #isReturnOrThrow()} ()} 如果返回真就不要再继续执行你的代码了 直接返回 {@link #getResult()}
     *
     * 如果这里使用了另一个文件的Call 栈文件也不会变化，只有行号会变化， 慎用
     *
     * @param call call
     * @param off, no check
     * @param len, no check
     */
    public Object getExpressionLastValue(Call call, int off, int len) {
        if (mThis.mIsReturn || reStack.throwed) return null;//throw or return

        Object last = null;
        Expression[] nowParam = call.getBuildParamExpressionCaches();
        for (int i = 0; i < len; i++) {
            Expression code = nowParam[i+off];
            last = this.executeExpressions0(code, 0);
            if (mThis.mIsReturn || reStack.throwed) return null;//throw or return
        }
        return last;
    }











    /**
     * 不安全的操作，必须严格使用本方法
     * 如果之前已经return 会抛出java异常, 如果执行时return 则会返回return结果
     * 执行后应该判断  {@link #isReturnOrThrow()} ()} 如果返回真就不要再继续执行你的代码了 直接返回 {@link #getResult()}
     *
     * 如果这里使用了另一个文件的 Expression 栈文件也不会变化，只有行号会变化， 慎用
     */
    public Object getExpressionValue(Expression expression) {
        if (mThis.mIsReturn || reStack.throwed) return null;//throw or return

        return this.executeExpressions0(expression, 0);
    }
    /**
     * 不安全的操作，必须严格使用本方法
     * 如果之前已经return 会抛出java异常, 如果执行时return 则会返回return结果
     * 执行后应该判断  {@link #isReturnOrThrow()} ()} 如果返回真就不要再继续执行你的代码了 直接返回 {@link #getResult()}
     *
     * 如果这里使用了另一个文件的 Expression 栈文件也不会变化，只有行号会变化， 慎用
     *
     * @param expression expression
     * @param off, no check
     * @param len, no check
     */
    public Object getExpressionLastValue(Expression[] expression, int off, int len) {
        Object last = null;
        for (int i = 0; i < len; i++) {
            Expression code = expression[i+off];
            last = this.executeExpressions0(code, 0);
            if (mThis.mIsReturn || reStack.throwed) return null;//throw or return
        }
        return last;
    }















    /**
     * 执行后应该判断  {@link #isReturnOrThrow()} ()} 如果返回真就不要再继续执行你的代码了 直接返回 {@link #getResult()}
     */
    protected void try_SetLocalErrorValue(String name, Re_PrimitiveClass_exception.Instance instance) {
        Re_Variable.accessSetValue(this, name, instance,this);
    }


    /**
     * 如果之前已经return 会抛出java异常
     * 执行后应该判断  {@link #isReturnOrThrow()} ()} 如果返回真就不要再继续执行你的代码了 直接返回 {@link #getResult()}
     */
    protected Object try_(@NotNull Expression[] executeExp, String catchName, @Nullable Expression[] catchExp, @Nullable Expression[] finallyExp) {
        if (mThis.mIsReturn || reStack.throwed) return null;//returned

        Object execute = null;
        try {
            execute = this.getExpressionLastValue (executeExp, 0, executeExp.length);
        } catch (Throwable innerEx) {
            //inner error
            if (!this.isThrow())
                this.setThrowFromJavaExceptionToStringAsReason(innerEx);
        }
        if (isThrow()) {
            /*其中一个代码块异常了
                try{代码块}； try{代码块}finally{}； try{代码块}catch(e){}；  try{代码块}catch(e){}finally{}
            */
            if (null == catchExp && null == finallyExp) {
                /*
                    try{代码块}
                */
                this.clearReturnOrThrow();
            } else {
                /*其中一个代码块异常了
                    try{代码块}finally{}； try{代码块}catch(e){}； try{代码块}catch(e){}finally{}
                */
                if (null != catchExp) {
                    this.try_SetLocalErrorValue(catchName, getThrow());    //set error variable error？
                    this.clearReturnOrThrow();//清除代码块的异常
                    execute = this.getExpressionLastValue(catchExp, 0, catchExp.length);
                }
                /*
                    最后如果有finally肯定要执行
                 */
                if (null != finallyExp) {
                    if (isThrow()) {
                        //这种情况可能是finally里的正常执行但是可能会覆盖之前的异常栈信息

                        //备份throw
                        int     cacheLine                                       = reStackCurrentElement.line;
                        Re_PrimitiveClass_exception.Instance  cacheThrowReason = reStack.getThrow();

                        this.clearReturnOrThrow();

                        execute = this.getExpressionLastValue(finallyExp, 0, finallyExp.length);

                        if (!isReturnOrThrow()) {
                            this.reStackCurrentElement.line = cacheLine;
                            this.setThrow(cacheThrowReason);
                        }
                    } else {
                        //catch执行过并且没有异常

                        //备份结果
                        boolean cacheIsReturn = this.isReturnOrThrow();
                        Object  cacheReturn   = this.getResult();

                        this.clearReturn();

                        this.getExpressionLastValue(finallyExp, 0, finallyExp.length);

                        if (!isReturnOrThrow()) {
                            this.setReturn(cacheIsReturn, cacheReturn);
                        }
                    }
                }
            }
        } else {
            //return 或者执行完毕并且未return
            if (null != catchExp) {
                this.try_SetLocalErrorValue(catchName, null);
                if (isThrow()) {    //set error variable error？
                    this.clearReturnOrThrow();
                }
            }
            /*正常运行  catch(e){} 不会运行 只会将 e设置为null
                try{代码块}； try{代码块}finally{}； try{代码块}catch(e){}； try{代码块}catch(e){}finally{} 正常运行
             */
            if (null != finallyExp) {
                //备份结果
                boolean cacheIsReturn = this.isReturnOrThrow();
                Object  cacheReturn   = this.getResult();

                this.clearReturn();

                this.getExpressionLastValue(finallyExp, 0, finallyExp.length);

                if (!isReturnOrThrow()) {
                    this.setReturn(cacheIsReturn, cacheReturn);
                }
            }
        }
        return execute;
    }





    /**
     *    thread unsafe 线程不安全
     *    不能同时执行本（实例方法）否则 发生非常坑爹的事情
     *    每段代码都必须新建一个Executor
     *    本方法其实是一次性的， 重复执行 栈就没有了 请不要执行第二次
     */
    public Object run() {
        if (Re_NativeStack.INDEX_REMOVED == reStackCurrentIndex) {
            Re_NativeStack.ReNativeTraceElement newNativeTrace = new Re_NativeStack.ReNativeTraceElement(this, reCodes.methodName, reCodes.filePath, reCodes.lineOffset);
            this.reStackCurrentElement = newNativeTrace;

            this.reStackCurrentIndex = this.reStack.addStackElementAndGetIndex(newNativeTrace);//no start

            Re_CodeFile reCodes      = this.reCodes;
            Expression[] expressions = reCodes.getExpressions();
            run0(expressions, 0, expressions.length);

            if (isThrow()) {
                return null;
            }

            this.reStackCurrentIndex = this.reStack.removeStackElementFromIndex(this.reStackCurrentIndex, this.reStackCurrentElement);
            return mThis.mResult;
        } else {
            throw new Re_Accidents.RuntimeInternalError("exited"); //exited  or re add stack
        }
    }
    protected void run0(Expression[] expressions, int offset, int limit) {
        while (!(mThis.mIsReturn || reStack.throwed) && offset <  limit) {
            Expression expression = expressions[offset];

            reStackCurrentElement.line = expression.line;
            mThis.mResult = executeExpressions0(expression, 0);

            offset++;// next line
        }
    }



    protected Object localValue(Var current) {
        return null == current.compileVariable ? localValue(current.getName()) : current.compileVariable.get(this);
    }
    /**
     * 禁止外部包访问本方法
     */
    private Object executeExpressions0(Expression expression, int offset) {
        try {
            Base[] bases = expression.getBuildCodeCache();
            Object  stack       = null;
            boolean prev  = false;
            boolean joinPoint   = false;
            int size            = bases.length;
            Base current;

            for (int i = offset; i < size; i++) {
                if (mThis.mIsReturn || reStack.throwed) return mThis.mResult;

                current = bases[i];
                if (current instanceof Var) {
                    // 栈底为空值
                    int next_index = i + 1;
                    if (null == stack) {
                        if (prev) {
                            if (joinPoint) {
                                // 无法从空结果获取字段
                                this.setThrow(Re_Accidents.undefined_object_var(null, current));
                            } else {
                                this.setThrow(Re_Accidents.runtime_grammatical_error(bases, current));
                            }
                            return null;
                        }
                        Base next = next_index < bases.length?bases[next_index]:null;
                        if (next instanceof Assignment) {
                            // x = ...
                            Object value = this.executeExpressions0(expression, i = next_index + 1);
                            if (mThis.mIsReturn || reStack.throwed) return mThis.mResult;

                            this.reStackCurrentElement.line = current.line;
                            stack = this.localValue(current.getName(), value); //可能会异常
                            return stack;
                        } else {
                            // x
                            this.reStackCurrentElement.line = current.line;
                            stack = null == current.compileVariable ? localValue(current.getName()) : current.compileVariable.get(this);
                            prev = true;
                        }
                    } else if (joinPoint) {
                        // 上一个元素为点
                        // 当前元素为name
                        Base next = next_index < bases.length?bases[next_index]:null;//获取下一个元素
                        if (null == next) {
                            // 数据被读完 x.x
                            this.reStackCurrentElement.line = current.line;
                            stack = this.getPointVariableValue(stack, current, current.getName());
                            return stack;
                        } else {
                            if (next instanceof Assignment) {
                                // x.x = ...
                                //这个i = next_index + 1 是不对 的！ 不应该是i=next_index+1 这样可能会重复执, 应该弄个代码读取器，或者I记录器, 又或者应该是直接return
                                Object value = this.executeExpressions0(expression, i = next_index + 1);
                                if (mThis.mIsReturn || reStack.throwed) return mThis.mResult;

                                this.reStackCurrentElement.line = current.line;
                                stack = this.setPointVariableValue(stack, current, current.getName(), value);
                                return stack;//测试
                            } else if (next instanceof Point) {
                                // x.x.?
                                this.reStackCurrentElement.line = current.line;
                                stack = this.getPointVariableValue(stack, current, current.getName());
                                prev = true;
                            } else {
                                this.setThrow(Re_Accidents.runtime_grammatical_error(bases, current));
                                return null;
                            }
                            joinPoint = false;
                        }
                    } else {
                        this.setThrow(Re_Accidents.runtime_grammatical_error(bases, current));
                        return null;
                    }
                } else if (current instanceof Call) {
                    if (null == stack) {
                        // 栈底为空值
                        Call currentCall = (Call) current;
                        if (prev) {
                            if (joinPoint) {
                                // 无法从空结果获取字段
                                this.setThrow(Re_Accidents.undefined_object_call(null, current, ((Call) current).getParamExpressionCount()));
                            } else {
                                this.setThrow(Re_Accidents.runtime_grammatical_error(bases, current));
                            }
                            return null;
                        }
                        // x();
                        String name = current.getName();
                        Object instance = null == current.compileVariable ? localValue(name) : current.compileVariable.get(this);
                        if (mThis.mIsReturn || reStack.throwed) return mThis.mResult;
                        if (null == instance) {
                            this.setThrow(Re_Accidents.unsupported_type(name, null, "call"));
                            return null;
                        }
                        this.reStackCurrentElement.line = current.line;
                        stack = this.executeCallThis(instance, currentCall);
                        prev = true;
                    } else if (joinPoint) {
                        // ?.x()
                        Call currentCall = (Call) current;
                        this.reStackCurrentElement.line = current.line;
                        stack = this.executeCallPoint(stack, current.getName(), currentCall);
                        prev = true;
                        joinPoint = false;
                    } else {
                        //对上个结果执行 basemethod
                        //a()()     y
                        //a()b()    x grammatical_error
                        Call call = (Call) current;
                        if (call.isEmptyName()) {
                            String name = current.getName();

                            this.reStackCurrentElement.line = current.line;
                            stack = this.executeCallThis(stack, call);// 这里是对栈进行调用而不是变量//xxx()()
                            prev = true;
                        } else {
                            this.setThrow(Re_Accidents.runtime_grammatical_error(bases, current));
                            return null;
                        }
                    }
                } else if (current instanceof Point) {
                    joinPoint = true;
                } else {
                    this.setThrow(Re_Accidents.runtime_grammatical_error(bases, current));
                    return null;
                }
            }
            return stack;
        }  catch (OutOfMemoryError memoryError) {
            this.setThrow(error_out_of_memory());
            return null;
        } catch (Throwable innerEx) {
            this.setThrowFromJavaExceptionToStringAsReason(innerEx);
            return null;
        }
    }















    public boolean isReturnOrThrow() {
        return mThis.mIsReturn || reStack.throwed;
    }
    /**
     * 如果已经return 会抛出java异常
     */
    public void setReturn(Object result) {
        if (mThis.mIsReturn || reStack.throwed) return;//returned

        mThis.mIsReturn = true;
        mThis.mResult = result;
    }
    public void setReturn(boolean ret, Object result) {
        if (mThis.mIsReturn || reStack.throwed) return;//returned

        mThis.mIsReturn = ret;
        mThis.mResult = result;
    }
    public Object getResult() {
        return mThis.mResult;
    }
    protected void clearReturn() {
        mThis.mIsReturn = false;
        mThis.mResult = null;
    }





    public boolean isThrow() {
        return reStack.isThrow();
    }
    public Re_PrimitiveClass_exception.Instance getThrow() {
        return reStack.getThrow();
    }


    /**
     * 设置后
     * {@link #setReturn(Object)}
     * 也会一起设置
     */
    public void setThrow(String reason) {
        if (mThis.mIsReturn || reStack.throwed) return;//returned

        this.reStack.setThrow(this.reStack.createExceptionInstance(reason, this.getStack()));

        mThis.mIsReturn = true;
        mThis.mResult = null;
    }
    public void setThrow(Re_PrimitiveClass_exception.Instance reason) {
        if (mThis.mIsReturn || reStack.throwed) return;//returned

        this.reStack.setThrow(reason);

        mThis.mIsReturn = true;
        mThis.mResult = null;
    }

    public void setThrowFromJavaExceptionToStringAsReason(Throwable reason) {
        this.setThrow(Throwables.toString(reason));
    }




    /**
     * 清除结果和 清除throw, 并断开新增的栈元素
     */
    public void clearReturnOrThrow() {
        if (Re_NativeStack.INDEX_REMOVED == reStackCurrentIndex) throw new Re_Accidents.RuntimeInternalError("exited"); //exited  or re add stack

        this.reStack.clearThrow();
        this.reStack.disconBeforeFromIndex(this.reStackCurrentIndex, this.reStackCurrentElement);

        mThis.mIsReturn = false;
        mThis.mResult = null;
    }




















    /**
     * 创建一个没有绑定任何类的执行器 {@link Re#execute(String, Object...)}
     */
    @NotNull
    public static ReRootExecutor createReRootExecutor(@NotNull Re host, Re_NativeStack stack, @NotNull Re_CodeFile block,
                                                      @Nullable Object[] arguments, @NotNull Re_IRe_VariableMap local) {
        if (stack.isThrow()) return null;

        if (null == arguments)
            arguments = Finals.EMPTY_OBJECT_ARRAY;
        if (null == local)
            local = Re.newLocalVariableMap();

        return new ReRootExecutor(host, stack,
                block,
                null, null,
                local, arguments);
    }


    /**
     * @see Re_Class#initializeStaticObject(Re, Re_NativeStack, Re_CodeFile)
     * 创建类 static 实例初始化执行器，用于初始化类的无论是顶级类还是内部类 匿名类都可以使用
     * 如果已经 throw 必定返回null;
     *
     * @param block 类的代码
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    @NotNull
    static ReClassInitializeExecutor createReClassInitializeExecutor(@NotNull Re host, Re_NativeStack stack, @NotNull Re_CodeFile block,
                                                                     @NotNull final Re_Class re_class) {
        if (stack.isThrow()) return null;

        final Object[]               arguments   = Finals.EMPTY_OBJECT_ARRAY;
        final Re_IRe_VariableMap variable    = re_class;

        return new ReClassInitializeExecutor(host, stack,
                block,
                re_class, variable,
                arguments);
    }


    /**
     * 执行类方法 或者对象方法
     *
     * @param host     主机
     * @param stack    stack
     * @param reClass  类
     * @param instance 非静态的 类实例
     * @param function 方法
     * @param local    执行器的local变量， 注意这里不会自动转换
     */
    @NotNull
    static Re_Executor createReClassFunctionExecutor(@NotNull Re host,          @NotNull Re_NativeStack stack,
                                                     @NotNull Re_Class reClass, @Nullable final Re_ClassInstance instance,
                                                     @NotNull final Re_ClassFunction function,
                                                     Object[] arguments, Re_IRe_VariableMap local) {
        if (stack.isThrow()) return null;

        if (null == arguments)
            arguments = Finals.EMPTY_OBJECT_ARRAY;
        if (null == local)
            local = Re_ClassFunction.argumentsAsVariableMap(function, arguments);

        Re_CodeFile reCodeBlock    = function.getReCodeBlock();
        Re_Executor functionParent = function.getDeclareExecutor();
        if (null == instance) { //类方法
            if (null == functionParent) {
                return new ReClassStaticFunctionNoParentExecutor(host, stack,
                        reCodeBlock,
                        reClass,
                        local, arguments);
            } else {
                return new ReClassStaticFunctionHasParentExecutor(host, stack,
                        reCodeBlock,
                        functionParent, reClass, function,
                        arguments, local);
            }
        } else {//实例方法
            if (null == functionParent) {
                return new ReClassObjectFunctionNoParentExecutor(host, stack,
                        reCodeBlock,
                        reClass, instance,
                        arguments, local);
            } else {
                return new ReClassObjectFunctionHasParentExecutor(host, stack,
                        reCodeBlock,
                        functionParent, reClass, instance,
                        function,
                        arguments, local);
            }
        }
    }

    /**
     * __EVAL__
     * 继承父 executor 执行 代码块
     * 一般用于eval
     *
     * @param re             主机
     * @param stack            current_executor 的stack
     * @param block            代码块
     * @param current_executor 继承当前变量
     */
    @NotNull
    private static ReInheritExecutor createReInheritExecutor(@NotNull Re re, Re_NativeStack stack, @NotNull Re_CodeFile block,
                                                             @NotNull final Re_Executor current_executor,
                                                             @Nullable Object[] inheritArguments) {
        if (stack.isThrow()) return null;

        return new ReInheritExecutor(re, stack,
                block, current_executor,
                current_executor.reClass, current_executor.reClassInstance,
                null == inheritArguments ? Finals.EMPTY_OBJECT_ARRAY : inheritArguments);
    }


    @NotNull
    private static ReInheritBasedExecutor createReBasedExecutor(@NotNull Re re, Re_NativeStack stack, @NotNull Re_CodeFile block,
                                                                @NotNull final Re_Executor current_executor,
                                                                @NotNull Re_IRe_VariableMap variableMap) {
        if (stack.isThrow()) return null;

        return new ReInheritBasedExecutor(re, stack,block,
                current_executor, current_executor.reClass, current_executor.reClassInstance,
                variableMap);
    }



    /**
     * { }
     *
     * @param executor              executor
     * @return {@link Re_ClassInstance}
     */
    static Re_ClassInstance createReDict(@NotNull final Re_Executor executor, @NotNull Call callParamController) throws Throwable {
        if (executor.isReturnOrThrow()) return null;

        final Re_ClassInstance dictInstance = Rez.SafesRe.createInstance_object(executor);
        if (executor.isReturnOrThrow()) return null;

        final Re_CodeFile block  = Re_CodeFile.create(executor.reCodes.getConstCaches(),
                                                      Re_CodeFile.METHOD_NAME__EMPTY, executor.reCodes.getFilePath(), callParamController.getLine(),
                                                      callParamController.getBuildParamExpressionCaches());

        //只有这样创建字典代码才能import其他加载的类
        Re_Executor re_executor = new ReInheritCreateDictExecutor(block, executor, executor.reClass, executor.reClassInstance,
                dictInstance);
        re_executor.run();

        return dictInstance;
    }

    /**
     * []
     *
     * @param executor executor
     * @return {@link Re_ClassInstance}
     *
     * 并没有创建Executor
     */
    static Re_PrimitiveClassInstance createReList(@NotNull final Re_Executor executor, @NotNull Call callParamController) throws Throwable {
        if (executor.isReturnOrThrow()) return null;

        final Re_PrimitiveClass_list.Instance dictInstance = Rez.SafesRe.createInstance_list(executor);
        if (executor.isReturnOrThrow()) return null;

        dictInstance.setElements(executor, callParamController);
        return dictInstance;
    }





    @SuppressWarnings("UnnecessaryLocalVariable")
    Object based_(Expression conditionalExpression, CallCreateDict executeExpressions) {
        if (mThis.mIsReturn || reStack.throwed) return null;//returned

        Object variableMap0 = getExpressionValue(conditionalExpression);
        if (isReturnOrThrow()) return null;

        Re_IRe_VariableMap variableMap;
        if (variableMap0 instanceof Re_IRe_VariableMap) {
            variableMap = ((Re_IRe_VariableMap) variableMap0);
        } else {
            setThrow(Re_Accidents.unsupported_type(Re_Utilities.objectAsName(variableMap0)));
            return null;
        }

        Re_Executor executor = this;
        Re re                = executor.re;
        Re_NativeStack stack = executor.reStack;

        Re_CodeFile block        = Re_CodeFile.create(executor.reCodes.getConstCaches(),
                                                      Re_CodeFile.METHOD_NAME__EMPTY, executor.reCodes.getFilePath(), executeExpressions.getLine(),
                                                      executeExpressions.getBuildParamExpressionCaches());

        Re_Executor newExecutor = Re_Executor.createReBasedExecutor(re, stack,
                                                                  block,
                                                                  executor, variableMap);
        if (null == newExecutor) return null;//re throw

        Object result = newExecutor.run();
        return result;
    }

    /**
     * __EVAL__
     * eval执行等同于代码执行，状态应该是同步的
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    Object eval_(String code, Object[] inheritArguments) {
        if (mThis.mIsReturn || reStack.throwed) return null;//returned

        Re_Executor executor = this;
        Re re                = executor.re;
        Re_NativeStack stack = executor.reStack;
        Re_CodeFile block    = re.compileEval(code);

        Re_Executor     inheritExecutor = Re_Executor.createReInheritExecutor(re, stack, block, executor, inheritArguments);
        if (null ==     inheritExecutor) return null;//re throw

        Object result = inheritExecutor.run();
        return result;
    }









    /**
     * 防止嵌套
     */
    @SuppressWarnings({"UnnecessaryModifier", "UnnecessaryInterfaceModifier"})
    public static interface IInheritExecutor {
        public Re_Executor getRealExecutor();

        public Object[] getInheritArguments();
    }
    public static Re_Executor findRealExecutor(Re_Executor current_executor) {
        while (current_executor instanceof IInheritExecutor)
            current_executor = ((IInheritExecutor) current_executor).getRealExecutor();
        return current_executor;
    }
    static Object[] findInheritArguments(Re_Executor current_executor) {
        if (current_executor instanceof IInheritExecutor)
            return ((IInheritExecutor) current_executor).getInheritArguments();
        return Finals.EMPTY_OBJECT_ARRAY;
    }






    //直接执行 不通过类初始化的方式进行， 没有绑定 类 类实例 方法
    private static class ReRootExecutor extends Re_Executor {
        Re_IRe_VariableMap local;
        Object[] arguments;

        private ReRootExecutor(@NotNull Re re, Re_NativeStack stack,
                               @NotNull Re_CodeFile reCodes,

                               @Nullable Re_Class re_class, @Nullable Re_ClassInstance reClassInstance,
                               Re_IRe_VariableMap local, Object[] arguments) {
            super(re, stack, reCodes, re_class, reClassInstance);
            this.mThis = this;

            this.local = local;
            this.arguments = arguments;
        }

        @Override
        public Object localValue(String key) {
            return Re_Variable.accessFindTableOrParentValueRequire(this, key, local);
        }
        @Override
        public Object localValue(String key, Object value) {
            Re_Variable.accessSetValue(this, key, value, local);
            return value;
        }

        @Override
        public Re_Variable _variable_remove(Object key) {
            return local._variable_remove(key);
        }

        @Override
        public Re_Variable _variable_find_table_or_parent(Object key) {
            return local._variable_find_table_or_parent(key);
        }

        @Override
        public Re_Variable _variable_find_table_var(Object key) {
            return local._variable_find_table_var(key);
        }

        @Override
        public Re_Variable _variable_get(Object key) {
            return local._variable_get(key);
        }

        @Override
        public Re_Variable _variable_put(Object key, Re_Variable value) {
            return local._variable_put(key, value);
        }

        @Override
        public boolean _variable_has(Object key) {
            return local._variable_has(key);
        }

        public int _variable_key_count() {
            return local._variable_key_count();
        }

        @Override
        public Iterable<?> _variable_keys() {
            return local._variable_keys();
        }

        @Override
        public Re_IRe_VariableMap _variable_clone_all() {
            return local._variable_clone_all();
        }

        @Override
        public Object[] getArguments() {
            return arguments;
        }

    }



    //初始化类
    private static class ReClassInitializeExecutor extends Re_Executor {
        final Re_IRe_VariableMap classVariable;
        final Object[] arguments;

        private ReClassInitializeExecutor(Re host, Re_NativeStack stack,
                                          Re_CodeFile block,

                                          Re_Class reClass,
                                          Re_IRe_VariableMap classVariable, Object[] arguments) {
            super(host, stack, block, reClass, null);
            this.mThis = this;

            this.classVariable = classVariable;
            this.arguments     = arguments;
        }

        @Override
        public Object localValue(String key) {
            return Re_Variable.accessFindTableOrParentValueRequire(this, key, reClass);
        }
        @Override
        public Object localValue(String key, Object value) {
            Re_Variable.accessSetValue(this, key, value, classVariable);
            return value;
        }


        @Override
        public Re_Variable _variable_remove(Object key) {
            return classVariable._variable_remove(key);
        }

        @Override
        public Re_Variable _variable_find_table_or_parent(Object key) {
            return reClass._variable_find_table_or_parent(key);
        }

        @Override
        public Re_Variable _variable_find_table_var(Object key) {
            return classVariable._variable_find_table_var(key);
        }

        @Override
        public Re_Variable _variable_get(Object key) {
            return classVariable._variable_get(key);
        }

        @Override
        public Re_Variable _variable_put(Object key, Re_Variable value) {
            return classVariable._variable_put(key, value);
        }

        @Override
        public boolean _variable_has(Object key) {
            return classVariable._variable_has(key);
        }

        @Override
        public int _variable_key_count() {
            return classVariable._variable_key_count();
        }

        @Override
        public Iterable<?> _variable_keys() {
            return classVariable._variable_keys();
        }

        @Override
        public Re_IRe_VariableMap _variable_clone_all() {
            return classVariable._variable_clone_all();
        }


        @Override
        public Object[] getArguments() {
            return arguments;
        }
    }

    private static class ReClassStaticFunctionNoParentExecutor extends Re_Executor {
        final Object[] arguments;
        final Re_IRe_VariableMap local;

        private ReClassStaticFunctionNoParentExecutor(Re host, Re_NativeStack stack,
                                                      Re_CodeFile block,

                                                      Re_Class reClass,
                                                      Re_IRe_VariableMap local, Object[] arguments) {
            super(host, stack, block, reClass, null);
            this.mThis = this;

            this.local = local;
            this.arguments = arguments;
        }

        @Override
        public Object localValue(String key) {
            return Re_Variable.accessFindTableOrParentValueRequire(this, key, local);
        }
        @Override
        public Object localValue(String key, Object value) {
            Re_Variable.accessSetValue(this, key, value, local);
            return value;
        }


        @Override
        public Re_Variable _variable_remove(Object key) {
            return local._variable_remove(key);
        }

        @Override
        public Re_Variable _variable_find_table_or_parent(Object key) {
            return local._variable_find_table_or_parent(key);
        }

        @Override
        public Re_Variable _variable_find_table_var(Object key) {
            return local._variable_find_table_var(key);
        }

        @Override
        public Re_Variable _variable_get(Object key) {
            return local._variable_get(key);
        }

        @Override
        public Re_Variable _variable_put(Object key, Re_Variable value) {
            return local._variable_put(key, value);
        }

        @Override
        public boolean _variable_has(Object key) {
            return local._variable_has(key);
        }

        @Override
        public int _variable_key_count() {
            return local._variable_key_count();
        }

        @Override
        public Iterable<?> _variable_keys() {
            return local._variable_keys();
        }

        @Override
        public Re_IRe_VariableMap _variable_clone_all() {
            return local._variable_clone_all();
        }


        @Override
        public Object[] getArguments() {
            return arguments;
        }

    }

    private static class ReClassStaticFunctionHasParentExecutor extends Re_Executor {
        final Re_ClassFunction function;
        final Re_Executor      parentExecutor;
        final Object[]         arguments;
        final Re_IRe_VariableMap local;

        private ReClassStaticFunctionHasParentExecutor(Re host, Re_NativeStack stack,
                                                       Re_CodeFile block,
                                                       Re_Executor current_executor,

                                                       Re_Class reClass,
                                                       Re_ClassFunction function,
                                                       Object[] arguments, Re_IRe_VariableMap local) {
            super(host, stack, block, reClass, null);
            this.mThis = this;

            this.function = function;
            this.parentExecutor = current_executor;
            this.arguments = arguments;
            this.local = local;
        }

        @Override
        public Object localValue(String key) {
            return Re_Variable.accessFindTableOrParentValueRequire(this, key, local, parentExecutor);
        }
        @Override
        public Object localValue(String key, Object value) {
            Re_Variable.accessSetValue(this, key, value, local);
            return value;
        }


        @Override
        public Re_Variable _variable_remove(Object key) {
            return local._variable_remove(key);
        }

        @Override
        public Re_Variable _variable_find_table_or_parent(Object key) {
            Re_Variable re_variable = local._variable_find_table_or_parent(key);
            if (null == re_variable)
                re_variable = parentExecutor._variable_find_table_or_parent(key);
            return re_variable;
        }

        @Override
        public Re_Variable _variable_find_table_var(Object key) {
            return local._variable_find_table_var(key);
        }

        @Override
        public Re_Variable _variable_get(Object key) {
            return local._variable_get(key);
        }

        @Override
        public Re_Variable _variable_put(Object key, Re_Variable value) {
            return local._variable_put(key, value);
        }

        @Override
        public boolean _variable_has(Object key) {
            return local._variable_has(key);
        }

        @Override
        public int _variable_key_count() {
            return local._variable_key_count();
        }

        @Override
        public Iterable<?> _variable_keys() {
            return local._variable_keys();
        }

        @Override
        public Re_IRe_VariableMap _variable_clone_all() {
            return local._variable_clone_all();
        }

        @Override
        public Object[] getArguments() {
            return arguments;
        }

    }

    private static class ReClassObjectFunctionNoParentExecutor extends Re_Executor {
        final Re_ClassInstance       instance;
        final Re_IRe_VariableMap local;
        final Object[]               arguments;

        private ReClassObjectFunctionNoParentExecutor(Re host, Re_NativeStack stack,
                                                     Re_CodeFile block,

                                                     Re_Class reClass, Re_ClassInstance instance,
                                                     Object[] finalArguments, Re_IRe_VariableMap local) {
            super(host, stack, block, reClass, instance);
            this.mThis = this;

            this.instance = instance;
            this.local = local;
            this.arguments = finalArguments;
        }

        @Override
        public Object localValue(String key) {
            return Re_Variable.accessFindTableOrParentValueRequire(this, key, local, instance);
        }
        @Override
        public Object localValue(String key, Object value) {
            Re_Variable.accessSetValue(this, key, value, local);
            return value;
        }


        @Override
        public Re_Variable _variable_remove(Object key) {
            return local._variable_remove(key);
        }

        @Override
        public Re_Variable _variable_find_table_or_parent(Object key) {
            Re_Variable re_variable = local._variable_find_table_or_parent(key);
            if (null == re_variable)
                re_variable = instance._variable_find_table_or_parent(key);
            return re_variable;
        }

        @Override
        public Re_Variable _variable_find_table_var(Object key) {
            return local._variable_find_table_var(key);
        }

        @Override
        public Re_Variable _variable_get(Object key) {
            return local._variable_get(key);
        }

        @Override
        public Re_Variable _variable_put(Object key, Re_Variable value) {
            return local._variable_put(key, value);
        }

        @Override
        public boolean _variable_has(Object key) {
            return local._variable_has(key);
        }

        @Override
        public int _variable_key_count() {
            return local._variable_key_count();
        }

        @Override
        public Iterable<?> _variable_keys() {
            return local._variable_keys();
        }

        @Override
        public Re_IRe_VariableMap _variable_clone_all() {
            return local._variable_clone_all();
        }

        @Override
        public Object[] getArguments() {
            return arguments;
        }
    }

    private static class ReClassObjectFunctionHasParentExecutor extends Re_Executor {
        final Re_ClassFunction       function;
        final Re_Executor            parentExecutor;
        final Object[]               arguments;
        final Re_IRe_VariableMap local;

        private ReClassObjectFunctionHasParentExecutor(Re host, Re_NativeStack stack,
                                                       Re_CodeFile block,
                                                       Re_Executor current_executor,

                                                       Re_Class reClass, Re_ClassInstance instance,
                                                       Re_ClassFunction function,
                                                       Object[] arguments, Re_IRe_VariableMap local) {
            super(host, stack, block, reClass, instance);
            this.mThis = this;

            this.function = function;
            this.parentExecutor = current_executor;
            this.arguments = arguments;
            this.local = local;
        }

        @Override
        public Object localValue(String key) {
            return Re_Variable.accessFindTableOrParentValueRequire(this, key, local, parentExecutor);
        }
        @Override
        public Object localValue(String key, Object value) {
            Re_Variable.accessSetValue(this, key, value, local);
            return value;
        }


        @Override
        public Re_Variable _variable_remove(Object key) {
            return local._variable_remove(key);
        }

        @Override
        public Re_Variable _variable_find_table_or_parent(Object key) {
            Re_Variable re_variable = local._variable_find_table_or_parent(key);
            if (null == re_variable)
                re_variable = parentExecutor._variable_find_table_or_parent(key);    //不搜索实例变量 需要获取实例变量需要使用this.xx
            return re_variable;
        }

        @Override
        public Re_Variable _variable_find_table_var(Object key) {
            return local._variable_find_table_var(key);
        }

        @Override
        public Re_Variable _variable_get(Object key) {
            return local._variable_get(key);
        }

        @Override
        public Re_Variable _variable_put(Object key, Re_Variable value) {
            return local._variable_put(key, value);
        }

        @Override
        public boolean _variable_has(Object key) {
            return local._variable_has(key);
        }

        @Override
        public int _variable_key_count() {
            return local._variable_key_count();
        }

        @Override
        public Iterable<?> _variable_keys() {
            return local._variable_keys();
        }

        @Override
        public Re_IRe_VariableMap _variable_clone_all() {
            return local._variable_clone_all();
        }

        @Override
        public Object[] getArguments() {
            return arguments;
        }
    }









    /**
     * 继承变量以及所有功能 相当于只是个代理 这个
     * @see ReInheritExecutor
     * 的所有操作都会 操作传入的执行器
     */
    //eval();
    private static class ReInheritExecutor extends Re_Executor implements IInheritExecutor {
        final Object[] inheritArguments;

        private ReInheritExecutor(Re host, Re_NativeStack stack,
                                  Re_CodeFile block,
                                  Re_Executor current_executor,

                                  Re_Class reClass, Re_ClassInstance reClassInstance,
                                  Object[] inheritArguments) {
            super(host, stack, block, reClass, reClassInstance);
            this.mThis = findRealExecutor(current_executor);

            this.inheritArguments = inheritArguments;
        }


        @Override
        public Object localValue(String key) {
            return mThis.localValue(key);
        }
        @Override
        public Object localValue(String key, Object value) {
            return mThis.localValue(key, value);
        }

        @Override
        public Re_Variable _variable_remove(Object key) {
            return mThis._variable_remove(key);
        }

        @Override
        public Re_Variable _variable_find_table_or_parent(Object key) {
            return mThis._variable_find_table_or_parent(key);
        }

        @Override
        public Re_Variable _variable_find_table_var(Object key) {
            return mThis._variable_find_table_var(key);
        }

        @Override
        public Re_Variable _variable_get(Object key) {
            return mThis._variable_get(key);
        }

        @Override
        public Re_Variable _variable_put(Object key, Re_Variable value) {
            return mThis._variable_put(key, value);
        }

        @Override
        public boolean _variable_has(Object key) {
            return mThis._variable_has(key);
        }


        @Override
        public int _variable_key_count() {
            return mThis._variable_key_count();
        }

        @Override
        public Iterable<?> _variable_keys() {
            return mThis._variable_keys();
        }

        @Override
        public Re_IRe_VariableMap _variable_clone_all() {
            return mThis._variable_clone_all();
        }

        @Override
        public Object[] getArguments() {
            return mThis.getArguments();
        }



        @Override
        public Re_Executor getRealExecutor() {
            return mThis;
        }

        @Override
        public Object[] getInheritArguments() {
            return inheritArguments;
        }
    }



    //{};
    private static class ReInheritCreateDictExecutor extends Re_Executor implements IInheritExecutor {
        final Re_Executor parentExecutor;
        final Re_ClassInstance dictInstance;

        private ReInheritCreateDictExecutor(Re_CodeFile block,
                                            Re_Executor current_executor,

                                            Re_Class reClass, Re_ClassInstance reClassInstance,
                                            Re_ClassInstance dictInstance) {
            super(current_executor.re, current_executor.reStack, block, reClass, reClassInstance);
            this.mThis = findRealExecutor(current_executor);

            this.parentExecutor = current_executor;
            this.dictInstance = dictInstance;
        }

        @Override
        public Object localValue(String key) {
            return Re_Variable.accessFindTableOrParentValueRequire(this, key, mThis); //不获取实例的变量
        }
        @Override
        public Object localValue(String key, Object value) {
            Re_Variable.accessSetValue(this, key, value, dictInstance);
            return value;
        }


        @Override
        public Re_Variable _variable_remove(Object key) {
            return dictInstance._variable_remove(key);
        }

        @Override
        public Re_Variable _variable_find_table_or_parent(Object key) {
            return mThis._variable_find_table_or_parent(key);
        }

        @Override
        public Re_Variable _variable_find_table_var(Object key) {
            return dictInstance._variable_find_table_var(key);
        }

        @Override
        public Re_Variable _variable_get(Object key) {
            return dictInstance._variable_get(key);
        }


        @Override
        public Re_Variable _variable_put(Object key, Re_Variable value) {
            return dictInstance._variable_put(key, value);
        }


        @Override
        public boolean _variable_has(Object key) {
            return dictInstance._variable_has(key);
        }

        @Override
        public int _variable_key_count() {
            return dictInstance._variable_key_count();
        }

        @Override
        public Iterable<?> _variable_keys() {
            return dictInstance._variable_keys();
        }

        @Override
        public Re_IRe_VariableMap _variable_clone_all() {
            return dictInstance._variable_clone_all();
        }

        @Override
        public Object[] getArguments() {
            return mThis.getArguments();
        }




        @Override
        public Re_Executor getRealExecutor() {
            return mThis;
        }
        @Override
        public Object[] getInheritArguments() {
            return findInheritArguments(parentExecutor);
        }
    }

    //比较奇葩的一个执行器
    //based({}) {};
    private static class ReInheritBasedExecutor extends Re_Executor implements IInheritExecutor {
        final Re_Executor            parentExecutor;
        final Re_IRe_VariableMap local;

        private ReInheritBasedExecutor(Re host, Re_NativeStack stack,
                                       Re_CodeFile block,
                                       Re_Executor current_executor,

                                       Re_Class reClass, Re_ClassInstance reClassInstance,
                                       Re_IRe_VariableMap local) {
            super(host, stack, block, reClass, reClassInstance);
            this.mThis = findRealExecutor(current_executor);

            this.parentExecutor = current_executor;
            this.local = local;
        }

        @Override
        public Object localValue(String key) {
            return Re_Variable.accessFindTableOrParentValueRequire(this, key, this);
        }
        @Override
        public Object localValue(String key, Object value) {
            Re_Variable.accessSetValue(this, key, value, this);
            return value;
        }


        @Override
        public Re_Variable _variable_remove(Object key) {
            return local._variable_remove(key);
        }

        @Override
        public Re_Variable _variable_find_table_or_parent(Object key) {
            Re_Variable re_variable = local._variable_get(key);
            if (null == re_variable)
                re_variable = parentExecutor._variable_find_table_or_parent(key);    //不搜索实例变量 需要获取实例变量需要使用this.xx
            return re_variable;
        }

        @Override
        public Re_Variable _variable_find_table_var(Object key) {
            return local._variable_find_table_var(key);
        }

        @Override
        public Re_Variable _variable_get(Object key) {
            return local._variable_get(key);
        }


        @Override
        public Re_Variable _variable_put(Object key, Re_Variable value) {
            return local._variable_put(key, value);
        }


        @Override
        public boolean _variable_has(Object key) {
            return local._variable_has(key);
        }

        @Override
        public int _variable_key_count() {
            return local._variable_key_count();
        }

        @Override
        public Iterable<?> _variable_keys() {
            return local._variable_keys();
        }

        @Override
        public Re_IRe_VariableMap _variable_clone_all() {
            return local._variable_clone_all();
        }

        @Override
        public Object[] getArguments() {
            return mThis.getArguments();
        }


        @Override
        public Re_Executor getRealExecutor() {
            return mThis;
        }
        @Override
        public Object[] getInheritArguments() {
            return findInheritArguments(parentExecutor);
        }
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
        return Re_Variable.key(this).toString();
    }



    @Override
    public boolean isPrimitive() {
        return true;
    }



    @Override
    public Object getObjectValue(Re_Executor executor, Object key) {
        return Re_Variable.accessFindTableValue(executor, key, Re_Executor.this);
    }

    @Override
    public boolean hasObjectKey(Re_Executor executor, Object key) {
        return Re_Variable.has(key, Re_Executor.this);
    }

    @Override
    public boolean removeObjectKey(Re_Executor executor, Object key) throws Throwable {
        return Re_Variable.accessRemove(executor, key, Re_Executor.this);
    }

    @Override
    public void putObjectValue(Re_Executor executor, Object key, Object value) {
        Re_Variable.accessSetValue(executor, key, value, Re_Executor.this);
    }

    @Override
    public int getObjectKeyCount(Re_Executor executor) {
        return Re_Variable.size(Re_Executor.this);
    }

    @Override
    public Iterable getObjectKeys(Re_Executor executor) throws Throwable {
        return Re_Variable.key(Re_Executor.this);
    }

    @Override
    public boolean hasObjectKeys() { return true; }

    @Override
    public final Object executePoint(Re_Executor executor, Object point_key, Call call) throws Throwable {
        Object primitiveCall = Re_Variable.accessFindTableValue(executor, point_key, this);
        if (executor.isReturnOrThrow()) return null;

        if (null == primitiveCall) {
            String s = Re_Utilities.toJString(point_key);
            this.setThrow(Re_Accidents.undefined(this, s));
            return null;
        }
        return this.executeCallThis(primitiveCall, call);
    }
    @Override
    public final Object executeThis(Re_Executor executor, Call call) throws Throwable {
        return executor.executeGVFromReObject(Re_Executor.this, call);
    }



    @Override
    public String getName() {
        return Re_Keywords.INNER_VAR__SPACE + "{" + hashCode() + '}';
    }
}
