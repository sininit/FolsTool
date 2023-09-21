package top.fols.box.reflect.re;

import top.fols.atri.lang.Objects;
import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.atri.interfaces.annotations.NotNull;

import java.util.Map;

import top.fols.box.reflect.re.Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker;

import static top.fols.box.reflect.re.Re_CodeLoader._CompileTimeCodeSourceReader.getBaseDataToDeclareString;

/**
 * 变量存储器，和对象不是一个概念
 * 变量是用来存储对象的
 *
 * 不能继承变量
 * 不能继承变量
 * 不能继承变量
 *
 * ，所有变量类型都预置好了
 */
@SuppressWarnings({"StaticInitializerReferencesSubClass", "rawtypes", "unchecked", "JavadocDeclaration", "ConstantConditions", "SameParameterValue"})
public class Re_Variable<E> implements Cloneable {
    @SuppressWarnings("DanglingJavadoc")
    Re_Variable superClone() {
        if (isStaticVariable()) {
            try {
                return (Re_Variable) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        /**
         * 动态变量,不需要克隆值 类似：
         * {@link Re_Keywords#INNER_VAR__THIS}
         * {@link Re_Keywords#INNER_VAR__STATIC}
         */
        return this;
    }







    static Re_Variable _newModifierVariable_(int modifier)  {
        Re_Variable variable = new Re_Variable<>();
        variable.mod = modifier;
        return variable;
    }
    static Re_Variable _newModifierVariable_Final(int modifier)  {
        Re_Variable____Final variable = new Re_Variable____Final();
        variable.mod = modifier;
        return variable;
    }
    static Re_Variable _newModifierVariable_TypeRequired(int modifier, Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker typeChecker)   {
        Re_Variable____TypeRequired variable = new Re_Variable____TypeRequired(typeChecker);
        variable.mod = modifier;
        return variable;
    }
    static Re_Variable _newModifierVariable_Final_TypeRequired(int modifier, Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker typeChecker)   {
        Re_Variable____TypeRequired_Final variable = new Re_Variable____TypeRequired_Final(typeChecker);
        variable.mod = modifier;
        return variable;
    }

    static class Re_Variable____Final extends Re_Variable {
        private Re_Variable____Final() {}

        boolean set;


        @Override protected boolean isAccessibleAndRemovable(Re_Executor executor) { return false; }
        @Override protected boolean isUnsafeRemovable() { return false; }

        @Override
        protected void set(Re_Executor executor, Object value) {
            if (set) {
                executor.setThrow(Re_Accidents.assignment_to_final_variable());
            } else {
                __value__ = value;
                set = true;
            }
        }

        @Override
        protected InnerAccessor getInnerAccessor() {
            return INNER_ACCESSOR;
        }
        static final InnerAccessor INNER_ACCESSOR = new InnerAccessor() {
            @Override
            public Object get(Re_Variable ins) {
                return ins.__value__;
            }
            @Override
            public void set(Re_Variable ins, Object value) {
                Re_Variable____Final variable = (Re_Variable____Final) ins;
                variable.__value__ = value;
                variable.set       = true;
            }
        };
    }
    static class Re_Variable____TypeRequired extends Re_Variable {
        private Re_Variable____TypeRequired(TypeChecker typeChecker) {
            this.typeChecker = typeChecker;
        }

        TypeChecker typeChecker;

        @Override
        protected void set(Re_Executor executor, Object value) {
            if (typeChecker.isInstanceof(value)) {
                __value__ = value;
            } else {
                executor.setThrow(Re_CodeLoader_ExpressionConverts.CallVar.unsupported_var_type(typeChecker, value));
            }
        }

        @Override
        protected InnerAccessor getInnerAccessor() {
            return INNER_ACCESSOR;
        }
        static final InnerAccessor INNER_ACCESSOR = new InnerAccessor(){
            @Override
            public Object get(Re_Variable ins) {
                return ins.__value__;
            }
            @Override
            public void set(Re_Variable ins, Object value) {
                Re_Variable____TypeRequired variable = (Re_Variable____TypeRequired)ins;
                TypeChecker typeChecker = variable.typeChecker;
                if (typeChecker.isInstanceof(value)) {
                    variable.__value__ = value;
                } else {
                    throw new UnsupportedOperationException(Re_CodeLoader_ExpressionConverts.CallVar.unsupported_var_type(typeChecker, value));
                }
            }
        };
    }
    static class Re_Variable____TypeRequired_Final extends Re_Variable {
        private Re_Variable____TypeRequired_Final(TypeChecker typeChecker) {
            this.typeChecker = typeChecker;
        }

        TypeChecker typeChecker;
        boolean set;


        @Override protected boolean isAccessibleAndRemovable(Re_Executor executor) { return false; }
        @Override protected boolean isUnsafeRemovable() { return false; }

        @Override
        protected void set(Re_Executor executor, Object value) {
            if (set) {
                executor.setThrow(Re_Accidents.assignment_to_final_variable());
            } else {
                if (typeChecker.isInstanceof(value)) {
                    __value__ = value;
                } else {
                    executor.setThrow(Re_CodeLoader_ExpressionConverts.CallVar.unsupported_var_type(typeChecker, value));
                }
                set = true;
            }
        }


        @Override
        protected InnerAccessor getInnerAccessor() {
            return INNER_ACCESSOR;
        }
        static final InnerAccessor INNER_ACCESSOR = new InnerAccessor(){
            @Override
            public Object get(Re_Variable ins) {
                return ins.__value__;
            }
            @Override
            public void set(Re_Variable ins, Object value) {
                Re_Variable____TypeRequired_Final variable = (Re_Variable____TypeRequired_Final)ins;
                TypeChecker typeChecker = variable.typeChecker;
                if (typeChecker.isInstanceof(value)) {
                    variable.__value__ = value;
                    variable.set       = true;
                } else {
                    throw new UnsupportedOperationException(Re_CodeLoader_ExpressionConverts.CallVar.unsupported_var_type(typeChecker, value));
                }
            }
        };
    }



    static class ____________{}

    static public final Re_Variable_Builtin_Compile_Null_Variable    Null  = new Re_Variable_Builtin_Compile_Null_Variable();
    static public final Re_Variable_Builtin_Compile_Boolean_Variable TRUE  = new Re_Variable_Builtin_Compile_Boolean_Variable(true);
    static public final Re_Variable_Builtin_Compile_Boolean_Variable FALSE = new Re_Variable_Builtin_Compile_Boolean_Variable(false);



    @Deprecated
    static Re_CodeLoader.Expression createDynamicCallAsExpression(int line, String varName, /*safe*/Re_Variable_DynamicCall.DynamicCall value) {
        return Re_CodeLoader.Expression.createExpression(line,
                Re_CodeLoader.Var.createVar(line, varName, /*safe*/new Re_Variable_DynamicCall(varName, value)));
    }
    @Deprecated
    static final class Re_Variable_DynamicCall extends Re_Variable implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_DynamicCall(String thatName, DynamicCall call) {
            this.name = thatName;
            this.call = Objects.requireNonNull(call, "call");
        }

        String name;
        DynamicCall call;

        @Override
        protected Object get(Re_Executor executor) {
            return call.execute(executor);
        }

        @Override
        public String getCompileDeclareValue() {
            return name;
        }

        static abstract class DynamicCall { public abstract Object execute(Re_Executor executor); }
    }


    public static Re_Variable                           createVariable(Object value)        { return new Re_Variable<>(value); }

    static Re_Variable_Builtin_ReusingTempIntegerVariable createReusingTempIntegerVariable(int javavalue)  {return new Re_Variable_Builtin_ReusingTempIntegerVariable(javavalue);}
    static Re_Variable_Builtin_ReusingTempObjectVariable  createReusingTempObjectVariable(Object javavalue){return new Re_Variable_Builtin_ReusingTempObjectVariable(javavalue);}




    static Re_Variable_Builtin_Compile_DynamicVariable_Environment              createCompileDynamicEnvironment()               {return Re_Variable_Builtin_Compile_DynamicVariable_Environment.DEFAULT;}
    static Re_Variable_Builtin_Compile_DynamicVariable_InheritExecutorArguments createCompileDynamicInheritExecutorArguments()  {return Re_Variable_Builtin_Compile_DynamicVariable_InheritExecutorArguments.DEFAULT;}
    static Re_Variable_Builtin_Compile_DynamicVariable_Arguments                createCompileDynamicArguments()                 {return Re_Variable_Builtin_Compile_DynamicVariable_Arguments.DEFAULT;}
    static Re_Variable_Builtin_Compile_DynamicVariable_Space                    createCompileDynamicSpace()                     {return Re_Variable_Builtin_Compile_DynamicVariable_Space.DEFAULT;}
    static Re_Variable_Builtin_Compile_DynamicVariable_This                     createCompileDynamicThis()                      {return Re_Variable_Builtin_Compile_DynamicVariable_This.DEFAULT;}
    static Re_Variable_Builtin_Compile_DynamicVariable_Static                   createCompileDynamicStatic()                    {return Re_Variable_Builtin_Compile_DynamicVariable_Static.DEFAULT;}

    static Re_Variable_Builtin_Compile_Null_Variable    createCompileNull()                 {return Null;}
    static Re_Variable_Builtin_Compile_Boolean_Variable createCompileBoolean(boolean value) {return value? TRUE : FALSE;}
    static Re_Variable_Builtin_Compile_Long_Variable    createCompileLong(long value)       {return new Re_Variable_Builtin_Compile_Long_Variable(value);}
    static Re_Variable_Builtin_Compile_Float_Variable   createCompileFloat(float value)     {return new Re_Variable_Builtin_Compile_Float_Variable(value);}
    static Re_Variable_Builtin_Compile_Double_Variable  createCompileDouble(double value)   {return new Re_Variable_Builtin_Compile_Double_Variable(value);}
    static Re_Variable_Builtin_Compile_Short_Variable   createCompileShort(short value)     {return new Re_Variable_Builtin_Compile_Short_Variable(value);}
    static Re_Variable_Builtin_Compile_Byte_Variable    createCompileByte(byte value)       {return new Re_Variable_Builtin_Compile_Byte_Variable(value);}
    static Re_Variable_Builtin_Compile_Char_Variable    createCompileChar(char value)       {return new Re_Variable_Builtin_Compile_Char_Variable(value);}
    static Re_Variable_Builtin_Compile_Int_Variable     createCompileInt(int value)         {return new Re_Variable_Builtin_Compile_Int_Variable(value);}
    static Re_Variable_Builtin_Compile_String_Variable  createCompileString(String value)   {return new Re_Variable_Builtin_Compile_String_Variable(value);}



    /**
     * 强制执行在某个或者某在实例上
     * 不经过任何权限检测
     *
     * 理论上不会抛出Java异常
     */
    static abstract class InnerAccessor {
        public abstract Object get (Re_Variable ins);
        public abstract void   set (Re_Variable ins, Object value);
        public Re_Variable     copy(Re_Variable ins) {
            return ins.superClone();
        }

        //普通变量访问器
        static final InnerAccessor STANDARD_VARIABLE = new InnerAccessor() {
            @Override
            public Object get(Re_Variable ins) {
                return ins.__value__;
            }
            @Override
            public void   set(Re_Variable ins, Object value) {
                ins.__value__ = value;
            }
        };

        static final InnerAccessor STANDARD_VARIABLE__BUILTIN = new InnerAccessor() {
            @Override public Object get(Re_Variable ins) { return ins.__value__; }
            @Override public void   set(Re_Variable ins, Object value) {}
        };
        static final InnerAccessor STANDARD_VARIABLE__BUILTIN_COMPILE_NATIVE = new InnerAccessor() {
            @Override public Object get(Re_Variable ins) { return ins.__value__; }
            @Override public void   set(Re_Variable ins, Object value) {}
        };
    }

    /**
     * 静态变量必须具备一个 内部服务器
     * 动态变量（运行时变量）是没有 内部访问器， 比如 arguments 和 this static 都是根据Re_Executor获取的 所以动态变量返回null
     */
    InnerAccessor getInnerAccessor() {
        return InnerAccessor.STANDARD_VARIABLE;
    }

    protected boolean isStaticVariable() {
        return null != getInnerAccessor();
    }
    protected boolean isDynamicVariable() {
        return null == getInnerAccessor();
    }


    protected int mod;
    protected int getModifier() { return mod; }


    protected E __value__;


    private Re_Variable() {}
    private Re_Variable(E __value__) {
        this.__value__ = __value__;
    }


    protected Object get(Re_Executor executor) {
        return this.__value__;
    }
    protected void   set(Re_Executor executor, E value0) {
        this.__value__ = value0;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (isStaticVariable()) {
            if (o instanceof Re_Variable) {
                Re_Variable other = (Re_Variable) o;
                if (other.isStaticVariable()) {
                    InnerAccessor innerAccessor1 = this. getInnerAccessor();
                    Object o1 = innerAccessor1.get(this);

                    InnerAccessor innerAccessor2 = other.getInnerAccessor();
                    Object o2 = innerAccessor2.get(other);
                    return Re_Math.eq(o1, o2);
                }
            }
        }
        return false;
    }
    @Override
    public int hashCode() {
        if (isStaticVariable()) {
            InnerAccessor innerAccessor1 = this. getInnerAccessor();
            Object o1 = innerAccessor1.get(this);
            return null == o1 ? 0 : o1.hashCode();
        }
        return super.hashCode();
    }
    @Override
    public String toString() {
        return super.toString();
    }


    /**
     * 这个方法不会 {@link Re_Executor#setThrow(String)} 也不会抛出异常
     * 检测是否可访问并可删除
     */
    protected boolean isAccessibleAndRemovable(@Nullable Re_Executor executor) {
        return true;
    }
    /**
     * 检测是否可删除, 不检测访问权限
     */
    protected boolean isUnsafeRemovable() {
        return true;
    }





    //no use

    /**
     *  合并所有key
     *
     * @param map1  @NotNull
     */
    public static Iterable key(@NotNull Re_IRe_VariableMap map1) {
        return map1._variable_keys();
    }


    public static int size(@NotNull Re_IRe_VariableMap map1) {
        return map1._variable_key_count();
    }




    /**
     * @param key @NotNull
     * @param map1  @NotNull
     *
     * 不会向上查找
     */
    public static boolean has(@NotNull Object key,
                              @NotNull Re_IRe_VariableMap map1) {
        return map1._variable_has(key);
    }




    static void  ______() {}

    /**
     *  @see Re_Variable#set(Re_Executor, Object)
     * @param key @NotNull
     * @param map1  @NotNull
     *
     * 不会向上查找
     */
    public static boolean accessRemove(Re_Executor executor, @NotNull Object key, @NotNull Re_IRe_VariableMap map1) {
        Re_Variable variable = map1._variable_get(key);
        if (null != variable) {
            if (variable.isAccessibleAndRemovable(executor)) {
                map1._variable_remove(key);
                return true;
            } //不抛出异常
        }
        return false;
    }
    /**
     * 一般为动态添加的时候使用
     *
     * 如果不存在变量才会创建新的变量，Key将会被转换为 {@link Re_CodeLoader#intern(Object)}
     * 如果已存在将不会 {@link Re_CodeLoader#intern(Object)} 变量中的原始key将不会变化
     *
     *  不会向上查找
     */
    public static void accessPutNewVariableRequire(Re_Executor executor, @NotNull Object key, @NotNull Re_Variable newVariable, @NotNull Re_IRe_VariableMap map) {
        //var name not null
        Re_Variable variable = map._variable_get(key);
        if (null != variable) {
            if (variable.isAccessibleAndRemovable(executor)) {
                map._variable_put(key, newVariable);
            } else {
                String s = Re_Utilities.toJString(key);
                executor.setThrow("unmovable variable: " + s);
            }
        } else {
            map._variable_put(key, newVariable);
        }
    }




    /**
     * @see Re_Variable#set(Re_Executor, Object)
     * 如果已经存在变量 会 执行{@link Re_Variable#set(Re_Executor, Object)}
     *
     * 应该由执行时设置 因为
     * 变量名执行前都是经过 {@link String#intern()} 的
     *
     * 在 {@link Re_IRe_Object} 或者 {@link Re_IRe_VariableMap} 方法中如果name没有被你改变可以直接执行该方法
     *
     * 不规则方法
     *  {@link Re_Executor#executeGVFromReObject(Re_IRe_Object, Re_CodeLoader.Call)}
     *  {@link Re_Executor#executeGVFromJavaArray(Object, Re_CodeLoader.Call)}
     *
     *  不会向上查找
     */
    public static void accessSetValue(Re_Executor executor, @NotNull Object key, Object value, @NotNull Re_IRe_VariableMap map) {
        Re_Variable variable = map._variable_get(key);
        if (null != variable) {
            variable.set(executor, value);
        } else {
            map._variable_put(key, new Re_Variable(value));
        }
    }
    @SuppressWarnings("rawtypes")
    public static void accessSetValue(Re_Executor executor,
                                      @NotNull Re_Variable variable, Object key,
                                      Object value) {
        variable.set(executor, value);
    }




    static void  _______() {}

    public static Object accessGetValue(Re_Executor executor, @NotNull Object key,
                                        @NotNull Re_IRe_VariableMap variableMap) {
        Re_Variable variable = variableMap._variable_get(key);
        if (null != variable) {
            return variable.get(executor);
        }
        return null;
    }





    /**
     * 如果不存在将会设置 {@link Re_Executor#setThrow(String)} }
     * 会向上查询
     */
    public static Object accessFindTableOrParentValueRequire(@NotNull Re_Executor executor, @NotNull Object key,
                                                             @NotNull Re_IRe_VariableMap map1) {
        Re_Variable variable = map1._variable_find_table_or_parent(key);
        if (null != variable) {
            return variable.get(executor);
        }
        String s = Re_Utilities.toJString(key);
        executor.setThrow(Re_Accidents.undefined(s));
        return null;
    }
    /**
     * 如果不存在将会设置 {@link Re_Executor#setThrow(String)} }
     * 会向上查询
     */
    public static Object accessFindTableOrParentValueRequire(@NotNull Re_Executor executor, @NotNull Object key,
                                                             @NotNull Re_IRe_VariableMap map1, @NotNull Re_IRe_VariableMap map2) {
        Re_Variable variable = map1._variable_find_table_or_parent(key);
        if (null != variable) {
            return variable.get(executor);
        } else {
            variable = map2._variable_find_table_or_parent(key);
            if (null != variable) {
                return variable.get(executor);
            }
        }
        String s = Re_Utilities.toJString(key);
        executor.setThrow(Re_Accidents.undefined(s));
        return null;
    }

    /**
     * @param key @NotNull
     * @param map1  @NotNull
     *
     *  会向上查找
     */
    public static Object accessFindTableValue(Re_Executor executor, @NotNull Object key,
                                              @NotNull Re_IRe_VariableMap map1) {
        Re_Variable variable = map1._variable_find_table_var(key);
        if (null != variable) {
            return  variable.get(executor);
        }
        return null;
    }




    /**
     * 不会向上查询
     */
    public static Object accessGetClassValue(Re_Executor executor,
                                             @NotNull Object key,
                                             @NotNull Re_Class reClass) {
        Re_Variable variable = reClass._variable_get(key);
        if (null != variable) {
            return  variable.get(executor);
        }
        return null;
    }

    /**
     * 会向上查询 实例变量和类变量表
     */
    public static Object accessGetInstanceOrClassValue(Re_Executor executor,
                                                       @NotNull Object key,
                                                       @NotNull Re_ClassInstance instance) {
        Re_Variable variable = instance._variable_get(key);
        if (null != variable) {
            return  variable.get(executor);
        } else {
            variable = instance.getReClass()._variable_get(key);
            if (null != variable) {
                return  variable.get(executor);
            }
        }
        return null;
    }



    static void  ________() {}




    //不安全
    @SuppressWarnings({"unchecked"})
    static final class UnsafesRe {
        static class VariableInstaller {
            private final String k_var_name;
            private final Re_Variable variable;

            public VariableInstaller(String k_var_name, Re_Variable variable) {
                this.k_var_name = k_var_name;
                this.variable = variable;
            }
            private Re_Executor variableMap;
            public void installOrSetThrow(Re_Executor executor) {
                if (null == variableMap) {
                    Re_Variable.accessPutNewVariableRequire(executor,
                            k_var_name, variable,
                            executor);
                    if (executor.isReturnOrThrow()) return;

                    this.variableMap = executor;
                } else {
                    executor.setThrow("installed");
                }
            }
            public void uninstall() {
                if (null != variableMap) {
                    Unsafes.removeVariableInstallerVariable(this);
                }
            }
        }


        /**
         * 不会向上查询
         */
        public static Object fromUnsafeAccessorGetClassValue(@Nullable Object key,
                                                             @NotNull Re_Class reClass) {
            if (null != reClass) {
                Re_Variable variable = reClass._variable_get(key);
                if (null != variable) {
                    return fromUnsafeAccessorGetValue(variable);
                }
                return null;
            }  else {
                return null;
            }
        }
        /**
         * 会向上查询 实例变量和类变量表
         */
        public static Object fromUnsafeAccessorGetInstanceOrClassValue(@Nullable Object key, @NotNull Re_ClassInstance instance) {
            Re_Variable variable = instance._variable_get(key);
            if (null == variable) {
                variable = instance.getReClass()._variable_get(key);
                if (null == variable) {
                    return null;
                }
            }
            return fromUnsafeAccessorGetValue(variable);
        }



        public static Object fromUnsafeAccessorGetValue(@Nullable Object key, Re_IRe_VariableMap map) {
            Re_Variable variable = map._variable_get(key);
            if (null == variable) {
                return null;
            } else {
                InnerAccessor innerAccessor = variable.getInnerAccessor();
                if (null == innerAccessor) {
                    return null;
                }
                return innerAccessor.get(variable);
            }
        }
        public static Object fromUnsafeAccessorGetValue(Re_Variable variable) {
            InnerAccessor innerAccessor = variable.getInnerAccessor();
            if (null == innerAccessor) {
                return null;
            }
            return innerAccessor.get(variable);
        }


        public static void fromUnsafeAccessorSetValue(Re_Variable variable, Object v) {
            InnerAccessor innerAccessor = variable.getInnerAccessor();
            if (null == innerAccessor) {
                return;
            }
            innerAccessor.set(variable, v);
        }

        /**
         * 一般为动态添加的时候使用
         *
         * 如果不存在变量才会创建新的变量，Key将会被转换为 {@link Re_CodeLoader#intern(Object)}
         * 如果已存在将不会 {@link Re_CodeLoader#intern(Object)} 变量中的原始key将不会变化
         *
         * 不会向上查询
         */
        public static void fromUnsafeAccessorSetValueIntern(Object key, Object v, Re_IRe_VariableMap map) {
            Re_Variable variable = map._variable_get(key);
            if (null == variable) {
                map._variable_put(Re_CodeLoader.intern(key), new Re_Variable(v));
            } else {
                InnerAccessor innerAccessor = variable.getInnerAccessor();
                if (null == innerAccessor) {
                    return;
                }
                innerAccessor.set(variable, v);
            }
        }
        /**
         * 不会向上查询
         */
        public static void fromUnsafeAccessorSetValue(Object key, Object v, Re_IRe_VariableMap map) {
            Re_Variable variable = map._variable_get(key);
            if (null == variable) {
                map._variable_put(key, new Re_Variable(v));
            } else {
                InnerAccessor innerAccessor = variable.getInnerAccessor();
                if (null == innerAccessor) {
                    return;
                }
                innerAccessor.set(variable, v);
            }
        }


        static void addVariableOrSetThrow(Re_Executor executor, @NotNull Object key, @NotNull Re_Variable newVariable, @NotNull Re_IRe_VariableMap map) {
            //var name not null
            Re_Variable variable = map._variable_get(key);
            if (null != variable) {
                String s = Re_Utilities.toJString(key);
                executor.setThrow("already set variable: " + s);
            } else {
                map._variable_put(key, newVariable);
            }
        }

        static void setInstanceVariableFromReClass(Re_Executor executor, Re_Class reClass, Re_ClassInstance reClassInstance, Object key) {
            Re_Variable variable = reClass._variable_get(key);
            if (null != variable) {
                addVariableOrSetThrow(executor, key, variable, reClassInstance);
            }
        }
    }

    /**
     * 在Java里执行
     * 不安全
     */
    @SuppressWarnings({"rawtypes"})
    static final class Unsafes {
        private Unsafes(){}

        public static <T extends Re_IRe_VariableMap> T clone(T variableMap)  {
            return (T) variableMap._variable_clone_all();
        }

        public static <K> Map<K, Re_Variable> cloneToMap(Map<K, Re_Variable> from, Map<K, Re_Variable> to) {
            for (K k : from.keySet()) {
                Re_Variable v = from.get(k);
                v = v.superClone(); //克隆变量

                to.put(k, v);
            }
            return to;
        }

        public static Re_Variable cloneVariable(@NotNull Object key, @NotNull Re_IRe_VariableMap map) {
            Re_Variable re_variable = map._variable_get(key);
            if (null != re_variable) {
                return re_variable.superClone();
            }
            return null;
        }






        private static void removeVariableInstallerVariable(UnsafesRe.VariableInstaller installer) {
            installer.variableMap._variable_remove(installer.k_var_name);
        }

        static boolean isDynamicVariable(Object key, @NotNull Re_IRe_VariableMap map) {
            Re_Variable variable  = map._variable_get(key);
            return null != variable && variable.isDynamicVariable();
        }
        static void removeVariableOrThrowEx(Object key,
                                            @NotNull Re_IRe_VariableMap map) {
            Re_Variable variable  = map._variable_get(key);
            if (null != variable) {
                if (variable.isUnsafeRemovable()) {
                    map._variable_remove(key);
                } else {
                    String s = Re_Utilities.objectAsString(key);
                    throw new RuntimeException("unmovable variable: " + s);
                }
            }
        }




        //----------------------------------------------------------------




        static Re_Variable getConstVariable(Object key, Re_CodeFile file) {
            return file.getConstCaches()._variable_get(key);
        }
        static Re_Variable getKeywordVariable(@NotNull Object key) {
            return Re_Keywords.keyword._variable_get(key);
        }

        /**
         * 内部使用，禁止使用本方法
         * 在已知是绝对不存在原变量的情况下替换变量
         */
        @SuppressWarnings("DeprecatedIsStillUsed")
        @Deprecated
        static void putVariable(Object key, @NotNull Re_Variable variable, @NotNull Re_IRe_VariableMap map1) {
            map1._variable_put(key, variable);
        }
        /**
         * 内部使用，禁止使用本方法
         * 在已知是绝对不存在原变量的情况下替换变量
         */
        @SuppressWarnings("DeprecatedIsStillUsed")
        @Deprecated
        static void putListVariable(Object key, Object value, Re_PrimitiveClass_list.Instance instance) {
            instance._variable_put(key, Re_Variable.createVariable(value));
        }




        //----------------------------------------------------------------











        public static void addVariableInternOrThrowEx(@NotNull Re_CodeFile.IRe_CompileVariable value, @NotNull Re_IRe_VariableMap map) {
            String key = value.getCompileDeclareValue();
            if (null == key)
                throw new NullPointerException("null name");
            Re_Variable reVariable = (Re_Variable) value;

            Re_Variable variable = map._variable_get(key);
            if (null != variable) {
                throw new RuntimeException("already set variable: " + key);
            } else {
                map._variable_put(Re_CodeLoader.intern(key), reVariable);
            }
        }
        /**
         * @see Re_Variable#set(Re_Executor, Object)
         * 如果已经存在变量 会异常
         *
         * 一般为动态添加的时候使用
         * 如果不存在 Key将会被转换为 {@link Re_CodeLoader#intern(Object)} 再提交
         *
         * 不会向上查询
         */
        public static void addVariableInternOrThrowEx(@NotNull Object key, @NotNull Re_Variable value, @NotNull Re_IRe_VariableMap map) {
            //var name not null
            Re_Variable variable = map._variable_get(key);
            if (null != variable) {
                String s = Re_Utilities.objectAsString(key);
                throw new RuntimeException("already set variable: " + s);
            } else {
                map._variable_put(Re_CodeLoader.intern(key), value);
            }
        }

        /**
         * @see Re_Variable#set(Re_Executor, Object)
         * 如果已经存在变量 会 执行{@link Re_Variable#set(Re_Executor, Object)}
         *
         * 应该由执行时设置 因为
         * 变量名执行前都是经过 {@link String#intern()} 的
         * 如果你从 {@link Re_IRe_Object} 中的回调中 不改变name名称 可以直接使用该方法而不使用 intern
         *
         * 不会向上查询
         */
        public static void addVariableOrThrowEx(@NotNull Object key, @NotNull Re_Variable value, @NotNull Re_IRe_VariableMap map) {
            Re_Variable variable = map._variable_get(key);
            if (null != variable) {
                String s = Re_Utilities.objectAsString(key);
                throw new RuntimeException("already set variable: " + s);
            } else {
                map._variable_put(key, value);
            }
        }


        /**
         * @see Re_Variable#set(Re_Executor, Object)
         * 如果已经存在变量 会抛出异常
         *
         * 一般为动态添加的时候使用
         * 如果不存在 Key将会被转换为 {@link Re_CodeLoader#intern(Object)} 再提交
         *
         * 不会向上查找
         */
        protected static void addBuiltinValueInternOrThrowEx(@NotNull Object key, @Nullable Object value, @NotNull Re_IRe_VariableMap map) {
            //var name not null
            Re_Variable variable = map._variable_get(key);
            if (null != variable) {
                String s = Re_Utilities.objectAsString(key);
                throw new RuntimeException("already set variable: " +s);
            } else {
                map._variable_put(Re_CodeLoader.intern(key), new Re_Variable_Builtin(value));
            }
        }
        protected static void addBuiltinValueInternOrThrowEx(@Nullable Re_IRe_Object.IPrimitiveCall primitiveCall, @NotNull Re_IRe_VariableMap map) {
            addBuiltinValueInternOrThrowEx(primitiveCall.getName(), primitiveCall, map);
        }




        public static Object fromUnsafeAccessorGetValueOrThrowEx(@Nullable Object key, Re_IRe_VariableMap map) {
            Re_Variable variable = map._variable_get(key);
            if (null == variable) {
                return null;
            } else {
                InnerAccessor innerAccessor = variable.getInnerAccessor();
                if (null == innerAccessor) {
                    throw new RuntimeException("variable is not direct accessor: " + variable.getClass().getName());
                }
                return innerAccessor.get(variable);
            }
        }


        /**
         * 不会向上查询
         */
        public static Object fromUnsafeAccessorGetClassValueOrThrowEx(@Nullable Object key,
                                                                      @NotNull Re_Class reClass) {
            if (null != reClass) {
                Re_Variable variable = reClass._variable_get(key);
                if (null != variable) {
                    return fromUnsafeAccessorGetValueOrThrowEx(variable);
                }
                return null;
            }  else {
                throw new RuntimeException("not a reClass: " + Re_Utilities.objectAsName(reClass));
            }
        }


        /**
         * 会向上查询 实例变量和类变量表
         */
        public static Object fromUnsafeAccessorGetInstanceOrClassValueOrThrowEx(@Nullable Object key,
                                                                                @NotNull Re_ClassInstance instance) {
            Re_Variable variable = instance._variable_get(key);
            if (null == variable) {
                variable = instance.getReClass()._variable_get(key);
                if (null == variable) {
                    return null;
                }
            }
            return fromUnsafeAccessorGetValueOrThrowEx(variable);
        }



        public static Object fromUnsafeAccessorGetValueOrThrowEx(Re_Variable variable) {
            InnerAccessor innerAccessor = variable.getInnerAccessor();
            if (null == innerAccessor) {
                throw new RuntimeException("variable is not direct accessor: " + variable.getClass().getName());
            }
            return innerAccessor.get(variable);
        }

        public static void fromUnsafeAccessorSetValueOrThrowEx(Re_Variable variable, Object v) {
            InnerAccessor innerAccessor = variable.getInnerAccessor();
            if (null == innerAccessor) {
                throw new RuntimeException("variable is not direct accessor: " + variable.getClass().getName());
            }
            innerAccessor.set(variable, v);
        }




        /**
         * 一般为动态添加的时候使用
         *
         * 如果不存在变量才会创建新的变量，Key将会被转换为 {@link Re_CodeLoader#intern(Object)}
         * 如果已存在将不会 {@link Re_CodeLoader#intern(Object)} 变量中的原始key将不会变化
         *
         * 不会向上查询
         */
        public static void fromUnsafeAccessorSetValueInternOrThrowEx(Object key, Object v, Re_IRe_VariableMap map) {
            Re_Variable variable = map._variable_get(key);
            if (null == variable) {
                map._variable_put(Re_CodeLoader.intern(key), new Re_Variable(v));
            } else {
                InnerAccessor innerAccessor = variable.getInnerAccessor();
                if (null == innerAccessor) {
                    throw new RuntimeException("variable is not direct accessor: " + variable.getClass().getName());
                }
                innerAccessor.set(variable, v);
            }
        }
        /**
         * 不会向上查询
         */
        public static void fromUnsafeAccessorSetValueOrThrowEx(Object key, Object v, Re_IRe_VariableMap map) {
            Re_Variable variable = map._variable_get(key);
            if (null == variable) {
                map._variable_put(key, new Re_Variable(v));
            } else {
                InnerAccessor innerAccessor = variable.getInnerAccessor();
                if (null == innerAccessor) {
                    throw new RuntimeException("variable is not direct accessor: " + variable.getClass().getName());
                }
                innerAccessor.set(variable, v);
            }
        }
    }




    static class _____________{}
    /**
     * 一般是编译时变量
     * 不可修改 没有权限要求
     */
    static class Re_Variable_Builtin<E> extends Re_Variable<E> {
        private Re_Variable_Builtin() {
            super(null);
            this.mod = STRUCT_MODIFIER;
        }
        private Re_Variable_Builtin(E value0) {
            super(value0);
            this.mod = STRUCT_MODIFIER;
        }

        static final int STRUCT_MODIFIER = Re_Modifiers.buildModifier(
                true, true,
                false, false,
                true);


        @Override
        protected  InnerAccessor getInnerAccessor() {
            return InnerAccessor.STANDARD_VARIABLE__BUILTIN;
        }

        @Override protected boolean isAccessibleAndRemovable(@Nullable Re_Executor executor) { return false; }
        @Override protected boolean isUnsafeRemovable() { return false; }

        @Override protected void set(Re_Executor executor, Object value0) { executor.setThrow("unmodifiable builtin variable"); }
    }



    @SuppressWarnings({"UnnecessaryBoxing", "UnnecessaryUnboxing"})
    public static class Re_Variable_Builtin_ReusingTempIntegerVariable extends Re_Variable_Builtin<Integer> {
        private Re_Variable_Builtin_ReusingTempIntegerVariable(int javavalue) {
            cache = javavalue;
            __value__ = new Integer(javavalue);
        }

        public int cache;


        @Override
        protected Object get(Re_Executor executor) {
            return (__value__.intValue() == cache)?__value__:(__value__ = new Integer(cache));
        }
        public Object get() {
            return (__value__.intValue() == cache)?__value__:(__value__ = new Integer(cache));
        }
    }



    @SuppressWarnings("unchecked")
    public static class Re_Variable_Builtin_ReusingTempObjectVariable extends Re_Variable_Builtin {
        private Re_Variable_Builtin_ReusingTempObjectVariable(Object value) {
            super(value);
        }

        public Object get() {
            return super.__value__;
        }
        public void set(Object value) {
            super.__value__ = value;
        }
    }



    static class _________________{}





    public static class Re_Variable_Builtin_Compile_Null_Variable extends Re_Variable_Builtin<Object> implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_Null_Variable() {
            super(null);
        }

        @Override
        protected InnerAccessor getInnerAccessor() {
            return InnerAccessor.STANDARD_VARIABLE__BUILTIN_COMPILE_NATIVE;
        }


        @Override
        protected Object get(Re_Executor executor) {
            return null;
        }
        public Object get() {
            return null;
        }

        @Override
        public String getCompileDeclareValue() {
            return getBaseDataToDeclareString(get());
        }
    }


    @SuppressWarnings({"BooleanConstructorCall", "UnnecessaryBoxing", "UnnecessaryUnboxing"})
    public static class Re_Variable_Builtin_Compile_Boolean_Variable extends Re_Variable_Builtin<Boolean> implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_Boolean_Variable(boolean javavalue) {
            cache     = javavalue;
            __value__ = javavalue? JAVA_CACHE_TRUE : JAVA_CACHE_FALSE;
        }

        private static final Boolean JAVA_CACHE_TRUE  = true;  //java auto boxing cache
        private static final Boolean JAVA_CACHE_FALSE = false; //java auto boxing cache
        final boolean cache;

        @Override
        protected InnerAccessor getInnerAccessor() {
            return InnerAccessor.STANDARD_VARIABLE__BUILTIN_COMPILE_NATIVE;
        }


        @Override
        protected Boolean get(Re_Executor executor) {
            return (__value__.booleanValue() == cache)?__value__:(__value__ = new Boolean(cache));
        }
        public Boolean get() {
            return (__value__.booleanValue() == cache)?__value__:(__value__ = new Boolean(cache));
        }



        @Override
        public String getCompileDeclareValue() {
            return getBaseDataToDeclareString(get());
        }
    }


    @SuppressWarnings({"UnnecessaryBoxing", "UnnecessaryUnboxing"})
    public static class Re_Variable_Builtin_Compile_Long_Variable extends Re_Variable_Builtin<Long> implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_Long_Variable(long javavalue) {
            cache = javavalue;
            __value__ = new Long(javavalue);
        }

        final long cache;

        @Override
        protected InnerAccessor getInnerAccessor() {
            return InnerAccessor.STANDARD_VARIABLE__BUILTIN_COMPILE_NATIVE;
        }


        @Override
        protected Long get(Re_Executor executor) {
            return (__value__.longValue() == cache)?__value__:(__value__ = new Long(cache));
        }
        public Long get() {
            return (__value__.longValue() == cache)?__value__:(__value__ = new Long(cache));
        }

        @Override
        public String getCompileDeclareValue() {
            return getBaseDataToDeclareString(get());
        }
    }
    @SuppressWarnings({"UnnecessaryBoxing", "UnnecessaryUnboxing"})
    public static class Re_Variable_Builtin_Compile_Float_Variable extends Re_Variable_Builtin<Float> implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_Float_Variable(float javavalue) {
            cache = javavalue;
            __value__ = new Float(javavalue);
        }

        final float cache;


        @Override
        protected InnerAccessor getInnerAccessor() {
            return InnerAccessor.STANDARD_VARIABLE__BUILTIN_COMPILE_NATIVE;
        }


        @Override
        protected Float get(Re_Executor executor) {
            return (__value__.floatValue() == cache)?__value__:(__value__ = new Float(cache));
        }
        public Float get() {
            return (__value__.floatValue() == cache)?__value__:(__value__ = new Float(cache));
        }


        @Override
        public String getCompileDeclareValue() {
            return getBaseDataToDeclareString(get());
        }
    }
    @SuppressWarnings({"UnnecessaryBoxing", "UnnecessaryUnboxing"})
    public static class Re_Variable_Builtin_Compile_Double_Variable extends Re_Variable_Builtin<Double> implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_Double_Variable(double javavalue) {
            cache = javavalue;
            __value__ = new Double(javavalue);
        }

        final double cache;

        @Override
        protected InnerAccessor getInnerAccessor() {
            return InnerAccessor.STANDARD_VARIABLE__BUILTIN_COMPILE_NATIVE;
        }


        @Override
        protected Double get(Re_Executor executor) {
            return (__value__.doubleValue() == cache)?__value__:(__value__ = new Double(cache));
        }
        public Double get() {
            return (__value__.doubleValue() == cache)?__value__:(__value__ = new Double(cache));
        }

        @Override
        public String getCompileDeclareValue() {
            return getBaseDataToDeclareString(get());
        }
    }

    @SuppressWarnings({"UnnecessaryBoxing", "UnnecessaryUnboxing"})
    public static class Re_Variable_Builtin_Compile_Short_Variable extends Re_Variable_Builtin<Short> implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_Short_Variable(short javavalue) {
            cache = javavalue;
            __value__ = new Short(javavalue);
        }

        final short cache;

        @Override
        protected InnerAccessor getInnerAccessor() {
            return InnerAccessor.STANDARD_VARIABLE__BUILTIN_COMPILE_NATIVE;
        }


        @Override
        protected Short get(Re_Executor executor) {
            return (__value__.shortValue() == cache)?__value__:(__value__ = new Short(cache));
        }
        public Short get() {
            return (__value__.shortValue() == cache)?__value__:(__value__ = new Short(cache));
        }


        @Override
        public String getCompileDeclareValue() {
            return getBaseDataToDeclareString(get());
        }
    }


    @SuppressWarnings({"UnnecessaryBoxing", "UnnecessaryUnboxing"})
    public static class Re_Variable_Builtin_Compile_Byte_Variable extends Re_Variable_Builtin<Byte> implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_Byte_Variable(byte javavalue) {
            cache = javavalue;
            __value__ = new Byte(javavalue);
        }

        final byte cache;

        @Override
        protected InnerAccessor getInnerAccessor() {
            return InnerAccessor.STANDARD_VARIABLE__BUILTIN_COMPILE_NATIVE;
        }


        @Override
        protected Byte get(Re_Executor executor) {
            return (__value__.byteValue() == cache)?__value__:(__value__ = new Byte(cache));
        }
        public Byte get() {
            return (__value__.byteValue() == cache)?__value__:(__value__ = new Byte(cache));
        }


        @Override
        public String getCompileDeclareValue() {
            return getBaseDataToDeclareString(get());
        }
    }

    @SuppressWarnings({"UnnecessaryBoxing", "UnnecessaryUnboxing"})
    public static class Re_Variable_Builtin_Compile_Char_Variable extends Re_Variable_Builtin<Character> implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_Char_Variable(char javavalue) {
            cache = javavalue;
            __value__ = new Character(javavalue);
        }

        final char cache;


        @Override
        protected InnerAccessor getInnerAccessor() {
            return InnerAccessor.STANDARD_VARIABLE__BUILTIN_COMPILE_NATIVE;
        }


        @Override
        protected Character get(Re_Executor executor) {
            return (__value__.charValue() == cache)?__value__:(__value__ = new Character(cache));
        }
        public Character get() {
            return (__value__.charValue() == cache)?__value__:(__value__ = new Character(cache));
        }


        @Override
        public String getCompileDeclareValue() {
            return getBaseDataToDeclareString(get());
        }
    }



    @SuppressWarnings({"UnnecessaryBoxing", "UnnecessaryUnboxing"})
    public static class Re_Variable_Builtin_Compile_Int_Variable extends Re_Variable_Builtin<Integer> implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_Int_Variable(int javavalue) {
            cache = javavalue;
            __value__ = new Integer(javavalue);
        }

        final int cache;

        @Override
        protected InnerAccessor getInnerAccessor() {
            return InnerAccessor.STANDARD_VARIABLE__BUILTIN_COMPILE_NATIVE;
        }


        @Override
        protected Integer get(Re_Executor executor) {
            return (__value__.intValue() == cache)?__value__:(__value__ = new Integer(cache));
        }
        public Integer get() {
            return (__value__.intValue() == cache)?__value__:(__value__ = new Integer(cache));
        }

        @Override
        public String getCompileDeclareValue() {
            return getBaseDataToDeclareString(get());
        }
    }




    public static class Re_Variable_Builtin_Compile_String_Variable extends Re_Variable_Builtin<String> implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_String_Variable(String string) {
            super(string);
        }

        @Override
        protected InnerAccessor getInnerAccessor() {
            return InnerAccessor.STANDARD_VARIABLE__BUILTIN_COMPILE_NATIVE;
        }

        @Override
        protected String get(Re_Executor executor) {
            return __value__;
        }
        public String get() {
            return __value__;
        }

        @Override
        public String getCompileDeclareValue() {
            return getBaseDataToDeclareString(get());
        }
    }













    static class ______________{}

    static Re_Variable_Builtin_Compile_DynamicVariable_class_exception createCompileDynamicClass_exception() {return Re_Variable_Builtin_Compile_DynamicVariable_class_exception.DEFAULT;}
    static class Re_Variable_Builtin_Compile_DynamicVariable_class_exception extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_class_exception() {}
        private static final Re_Variable_Builtin_Compile_DynamicVariable_class_exception DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_class_exception();
        @Override protected InnerAccessor getInnerAccessor() { return null; }
        @Override protected Object get(Re_Executor executor) { return executor.re.class_exception(); }
        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_CLASS__EXCEPTION;
        }
    }

    static Re_Variable_Builtin_Compile_DynamicVariable_class_json               createCompileDynamicClass_json()            {return Re_Variable_Builtin_Compile_DynamicVariable_class_json.DEFAULT;}
    static class Re_Variable_Builtin_Compile_DynamicVariable_class_json extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_class_json() {}
        private static final Re_Variable_Builtin_Compile_DynamicVariable_class_json DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_class_json();
        @Override protected InnerAccessor getInnerAccessor() { return null; }
        @Override protected Object get(Re_Executor executor) { return executor.re.class_json(); }
        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_CLASS__JSON;
        }
    }
    static Re_Variable_Builtin_Compile_DynamicVariable_class_list               createCompileDynamicClass_list()            {return Re_Variable_Builtin_Compile_DynamicVariable_class_list.DEFAULT;}
    static class Re_Variable_Builtin_Compile_DynamicVariable_class_list extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_class_list() {}
        private static final Re_Variable_Builtin_Compile_DynamicVariable_class_list DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_class_list();
        @Override protected InnerAccessor getInnerAccessor() { return null; }
        @Override protected Object get(Re_Executor executor) { return executor.re.class_list(); }
        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_CLASS__LIST;
        }
    }
    static Re_Variable_Builtin_Compile_DynamicVariable_class_object             createCompileDynamicClass_object()       {return Re_Variable_Builtin_Compile_DynamicVariable_class_object.DEFAULT;}
    static class Re_Variable_Builtin_Compile_DynamicVariable_class_object extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_class_object() {}
        private static final Re_Variable_Builtin_Compile_DynamicVariable_class_object DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_class_object();
        @Override protected InnerAccessor getInnerAccessor() { return null; }
        @Override protected Object get(Re_Executor executor) { return executor.re.class_object(); }
        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_CLASS__OBJECT;
        }
    }
    static Re_Variable_Builtin_Compile_DynamicVariable_class_reflect            createCompileDynamicClass_reflect()       {return Re_Variable_Builtin_Compile_DynamicVariable_class_reflect.DEFAULT;}
    static class Re_Variable_Builtin_Compile_DynamicVariable_class_reflect extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_class_reflect() {}
        private static final Re_Variable_Builtin_Compile_DynamicVariable_class_reflect DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_class_reflect();
        @Override protected InnerAccessor getInnerAccessor() { return null; }
        @Override protected Object get(Re_Executor executor) { return executor.re.class_reflect(); }
        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_CLASS__REFLECT;
        }
    }
    static Re_Variable_Builtin_Compile_DynamicVariable_class_thread             createCompileDynamicClass_thread()       {return Re_Variable_Builtin_Compile_DynamicVariable_class_thread.DEFAULT;}
    static class Re_Variable_Builtin_Compile_DynamicVariable_class_thread extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_class_thread() {}
        private static final Re_Variable_Builtin_Compile_DynamicVariable_class_thread DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_class_thread();
        @Override protected InnerAccessor getInnerAccessor() { return null; }
        @Override protected Object get(Re_Executor executor) { return executor.re.class_thread(); }
        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_CLASS__THREAD;
        }
    }

    static class ___________{}

    static class Re_Variable_Builtin_Compile_DynamicVariable_Environment extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_Environment() {}

        private static final Re_Variable_Builtin_Compile_DynamicVariable_Environment DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_Environment();

        @Override
        protected InnerAccessor getInnerAccessor() {
            return null;
        }

        /**
         * @param executor 默认无意义
         */
        @Override
        protected Object get(Re_Executor executor) {
            return executor.re.getEnvironmentMap();
        }

        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_VAR__ENVIRONMENT;
        }
    }

    static class Re_Variable_Builtin_Compile_DynamicVariable_InheritExecutorArguments extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_InheritExecutorArguments() {}

        private static final Re_Variable_Builtin_Compile_DynamicVariable_InheritExecutorArguments DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_InheritExecutorArguments();


        @Override
        protected InnerAccessor getInnerAccessor() {
            return null;
        }


        @Override
        protected Object get(Re_Executor  executor) {
            return Re_Executor.findInheritArguments(executor);
        }

        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_VAR__INHERIT_FUN_ARGUMENTS_ARGUMENTS;
        }
    }

    static class Re_Variable_Builtin_Compile_DynamicVariable_Arguments extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_Arguments() { }

        private static final Re_Variable_Builtin_Compile_DynamicVariable_Arguments DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_Arguments();

        @Override
        protected InnerAccessor getInnerAccessor() {
            return null;
        }


        @Override
        protected Object get(Re_Executor executor) {
            return executor.getArguments();
        }

        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_VAR__FUN_ARGUMENTS_ARGUMENTS;
        }
    }

    static class Re_Variable_Builtin_Compile_DynamicVariable_Space extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_Space() {}

        private static final Re_Variable_Builtin_Compile_DynamicVariable_Space DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_Space();

        @Override
        protected InnerAccessor getInnerAccessor() {
            return null;
        }


        @Override
        protected Object get(Re_Executor executor) {
            return executor;
        }

        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_VAR__SPACE;
        }
    }

    static class Re_Variable_Builtin_Compile_DynamicVariable_This extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_This() {}

        private static final Re_Variable_Builtin_Compile_DynamicVariable_This DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_This();

        @Override
        protected InnerAccessor getInnerAccessor() {
            return null;
        }

        /**
         * @param executor 默认无意义
         */
        @Override
        protected Object get(Re_Executor executor) {
            Re_ClassInstance reClassInstance = executor.reClassInstance;
            if (null != reClassInstance) {
                return  reClassInstance;
            }
            executor.setThrow(Re_Accidents.executor_no_bind_class_instance());
            return null;
        }


        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_VAR__THIS;
        }
    }

    static class Re_Variable_Builtin_Compile_DynamicVariable_Static extends Re_Variable_Builtin implements Re_CodeFile.IRe_CompileVariable {
        private Re_Variable_Builtin_Compile_DynamicVariable_Static() {}

        private static final Re_Variable_Builtin_Compile_DynamicVariable_Static DEFAULT = new Re_Variable_Builtin_Compile_DynamicVariable_Static();


        @Override
        protected InnerAccessor getInnerAccessor() {
            return null;
        }


        /**
         * @param executor 默认无意义
         */
        @Override
        protected Object get(Re_Executor executor) {
            Re_Class reClass = executor.reClass;
            if (null != reClass) {
                return  reClass;
            }
            executor.setThrow(Re_Accidents.executor_no_bind_class());
            return null;
        }


        @Override
        public String getCompileDeclareValue() {
            return Re_Keywords.INNER_VAR__STATIC;
        }
    }



}
