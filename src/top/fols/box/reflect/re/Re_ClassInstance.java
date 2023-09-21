package top.fols.box.reflect.re;

import top.fols.atri.interfaces.annotations.Nullable;

import java.util.Objects;

/**
 * 请勿重写
 */
@SuppressWarnings("rawtypes")
public class Re_ClassInstance
        implements Re_IRe_Object, Re_IRe_VariableMap,
        Re_IReGetDeclaringClass, Re_IReGetClass,
        Cloneable {

    protected Re_Class               reClass;
    protected Re_IRe_VariableMap reClassInstanceVariableMap;
    private String                   instanceName0;


    /**
     * 克隆实例
     */
    Re_ClassInstance superClone() {
        try {
            Re_ClassInstance clone = (Re_ClassInstance) super.clone();
            clone.reClass = this.reClass;
            clone.reClassInstanceVariableMap = Re_Variable.Unsafes.clone(this.reClassInstanceVariableMap);
            clone.instanceName0 = null;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);//不可能
        }
    }





    static void createReClassInstanceAfter(Re_Class runClass, Re_ClassInstance instance,
                                                     Re_IRe_VariableMap variableMap) {
        instance.reClass  = runClass;
        instance.reClassInstanceVariableMap = variableMap;
    }



    protected Re_ClassInstance(Re_Class reClass) {
        createReClassInstanceAfter(reClass, this,
                Re.newObjectVariableMap());
    }
    protected Re_ClassInstance(Re_Class reClass, Re_IRe_VariableMap variableMap) {
        createReClassInstanceAfter(reClass, this,
                variableMap);
    }







    @Override
    public Re_Class getReClass() {
        return reClass;
    }

    @Override
    public Re_Class getReDeclareClass() {
        return reClass;
    }


    @Override
    public String getName() {
        return getInstanceName();
    }


    public final String getInstanceName() {
        if (null != instanceName0)
            return  instanceName0;
        return instanceName0 = reClass.getName() + "@" + System.identityHashCode(this);
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Re_ClassInstance)) return false;

        Re_ClassInstance that = (Re_ClassInstance) o;
        return Objects.equals(reClass,  that.reClass)  &&
               Objects.equals(reClassInstanceVariableMap, that.reClassInstanceVariableMap);
    }

    @Override
    public int hashCode() {
        int result = reClass != null ? reClass.hashCode() : 0;
        result = 31 * result + (reClassInstanceVariableMap != null ? reClassInstanceVariableMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getInstanceName();
    }


    @Override
    public boolean isPrimitive() { return false; }
    @Override
    public boolean hasObjectKeys() { return true; }


    /**
     * 原始获取
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     *
     *            递归获取
     */
    @Override
    public Re_Variable _variable_find_table_or_parent(Object key) {
        Re_Variable re_variable = reClassInstanceVariableMap._variable_find_table_or_parent(key);
        if (null == re_variable)
            re_variable = reClass._variable_find_table_or_parent(key);
        return re_variable;
    }

    @Override
    public Re_Variable _variable_find_table_var(Object key) {
        Re_Variable re_variable = reClassInstanceVariableMap._variable_find_table_var(key);
        if (null == re_variable)
            re_variable = reClass._variable_find_table_var(key);
        return re_variable;
    }

    @Override
    public Re_Variable _variable_get(Object key) {
        return reClassInstanceVariableMap._variable_get(key);
    }


    /**
     * 直接删除
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     *
     */
    @Override
    public Re_Variable _variable_remove(Object key) {
        return reClassInstanceVariableMap._variable_remove(key);
    }

    /**
     * 原始提交
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     */
    @Override
    public Re_Variable _variable_put(Object key, Re_Variable value) {
        return reClassInstanceVariableMap._variable_put(key, value);
    }

    @Override
    public boolean _variable_has(Object key) {
        return reClassInstanceVariableMap._variable_has(key);
    }

    /**
     * 数量
     */
    @Override
    public int _variable_key_count() {
        return reClassInstanceVariableMap._variable_key_count();
    }

    /**
     * @return 变量名
     * 返回不可修改的集合， 或者克隆
     */
    @Override
    public Iterable<?> _variable_keys() {
        return reClassInstanceVariableMap._variable_keys();
    }

    @Override
    public Re_IRe_VariableMap _variable_clone_all() {
        return reClassInstanceVariableMap._variable_clone_all();
    }





    /**
     * 获取子变量，不是获取自己
     * 只要用了{@link Re_Executor#getExpressionValue(Re_CodeLoader.Call, int)} 后都要手动检测是否return
     */
    @Override
    public final Object getObjectValue(Re_Executor executor, Object key) {
        return Re_Variable.accessFindTableValue(executor, key, this);
    }


    @Override
    public final boolean hasObjectKey(Re_Executor executor, Object key) {
        return Re_Variable.has(key, this);
    }

    /**
     * 只要用了{@link Re_Executor#getExpressionValue(Re_CodeLoader.Call, int)} 后都要手动检测是否return
     */
    @Override
    public final boolean removeObjectKey(Re_Executor executor, Object key) throws Throwable {
        return Re_Variable.accessRemove(executor, key, this);
    }


    /**
     * 设置子变量，不是设置自己
     * 只要用了{@link Re_Executor#getExpressionValue(Re_CodeLoader.Call, int)} 后都要手动检测是否return
     */
    @Override
    public final void putObjectValue(Re_Executor executor, Object key, Object value) throws Throwable {
        Re_Variable.accessSetValue(executor, key, value, this);
    }

    @Override
    public final int getObjectKeyCount(Re_Executor executor) throws Throwable {
        return Re_Variable.size(this);
    }

    @Override
    public final Iterable getObjectKeys(Re_Executor executor) throws Throwable {
        return Re_Variable.key(this);
    }

    //方法如果设置名称 的话则设置为final


    /**
     * 执行之前不可能已经return， 如果中间有执行了表达式应该判断表达式是否已经return 如果已经return 则返回return数据 而不是继续操作, 如果已经return返回的任何数据都是无效的
     * <p>
     * 只要用了{@link Re_Executor#getExpressionValue(Re_CodeLoader.Call, int)} 后都要手动检测是否return
     * <p>
     * 如果你不想处理，建议使用 {@link Re_IRe_Object#executePoint(Re_Executor, Object, Re_CodeLoader.Call)}
     * <p>
     * 假定本对象名称x
     * 那么执行的是 x.x()
     * @param point_key          指子变量名称，假设这是个map里面有个a
     *                            执行的就是map.a();
     * @param call 如果是true 则callParam 为空 ，如果false则 callParam会经过计算后传入
     */
    @Override
    public final Object executePoint(Re_Executor executor, Object point_key, Re_CodeLoader.Call call) throws Throwable {
        Object funCall = Re_Variable.accessFindTableValue(executor, point_key, this);
        if (executor.isReturnOrThrow()) return null;

        if (Re_Utilities.isReFunction(funCall)) {
            Re_ClassFunction function = (Re_ClassFunction) funCall;

            Object[] arguments = executor.getExpressionValues(call);
            if (executor.isReturnOrThrow()) return null;

            //为null 执行时自动生成
            return authenticationAndExecuteOnRe(executor, function, arguments, null);
        }
        if (null == funCall) {
            String s = Re_Utilities.toJString(point_key);
            executor.setThrow(Re_Accidents.undefined(this, s));
            return null;
        }
        return executor.executeCallThis(funCall, call);
    }

    /**
     * 已经是实例了 你还要执行？
     */
    @Override
    public final Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
        return executor.executeGVFromReObject(this, call);
    }



    //指定某个实例运行特定方法, 这个方法不一定是在这个类声明的 但是可以强制运行
    final Object authenticationAndExecuteOnRe(Re_Executor executor, Re_ClassFunction function, Object[] arguments, @Nullable Re_IRe_VariableMap variableMap) throws Throwable {//no permissions
        //最后一行 不需要判断return
        return function.authenticationAndExecuteOnRe(executor, reClass, this, arguments, variableMap);
    }



}
