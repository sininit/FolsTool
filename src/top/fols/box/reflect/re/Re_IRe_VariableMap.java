package top.fols.box.reflect.re;


/**
 * 变量存储表
 * 该对象所有方法操作应该由Re_Variable进行
 * variable是用来存储object值的
 * 不用stack 因此不出现任何异常
 *
 * 也不要在方法内判断任何权限
 */
@SuppressWarnings({"UnnecessaryModifier", "rawtypes", "UnnecessaryInterfaceModifier"})
public interface Re_IRe_VariableMap {

    /**
     * 直接删除
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     * 删除当前层的变量
     * 删除什么返回什么
     */
    public Re_Variable _variable_remove(Object key);


    /**
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     * 可能向上搜索变量
     * 一般是搜索向上查找局部变量
     */
    public Re_Variable _variable_find_table_or_parent(Object key);


    /**
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     * 可能向上搜索变量
     * 一般是获取对象的变量
     */
    public Re_Variable _variable_find_table_var(Object key);

    /**
     * 获取当前层的变量， 一般用来判断如果不存在则put， 不要向上获取
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     */
    public Re_Variable _variable_get(Object key);


    /**
     * 原始提交
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     *
     * 提交当前层的变量
     */
    public Re_Variable _variable_put(Object key, Re_Variable value);


    /**
     * 判断当前层是否存在变量
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     */
    public boolean _variable_has(Object key);

    /**
     * 当前层数量
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     */
    public int _variable_key_count();


    /**
     * @return 变量名
     * 返回不可修改的集合， 或者克隆
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     *
     * 当前层的变量名集合
     */
    public Iterable<?> _variable_keys();

    /**
     * 克隆变量表
     */
    public Re_IRe_VariableMap _variable_clone_all();
}
