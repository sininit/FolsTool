package top.fols.box.reflect.re;

import java.util.*;

/**
 * key 按 添加顺序排序
 */
@SuppressWarnings("rawtypes")
class Re_VariableMapAsObjectSynchronizedLinked implements Re_IRe_VariableMap {
    private LinkedHashMap<Object, Re_Variable>  original     = new LinkedHashMap<>();
    private Map<Object, Re_Variable>            originalSync = Collections.synchronizedMap(original);

    @Override
    public Re_IRe_VariableMap _variable_clone_all() {
        Re_VariableMapAsObjectSynchronizedLinked instance = new Re_VariableMapAsObjectSynchronizedLinked();
        instance.original = (LinkedHashMap<Object, Re_Variable>) Re_Variable.Unsafes.cloneToMap(this.original, new LinkedHashMap<Object, Re_Variable>());
        instance.originalSync = Collections.synchronizedMap(instance.original);
        return instance;
    }


    /**
     * 直接删除
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     *
     * @return 没有意义
     */
    @Override
    public Re_Variable _variable_remove(Object key) {
        return originalSync.remove(key);
    }

    /**
     * 原始获取
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     *
     */
    @Override
    public Re_Variable _variable_find_table_or_parent(Object key) {
        return originalSync.get(key);
    }

    @Override
    public Re_Variable _variable_find_table_var(Object key) {
        return originalSync.get(key);
    }

    @Override
    public Re_Variable _variable_get(Object key) {
        return originalSync.get(key);
    }

    /**
     * 原始提交
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     *
     * @return 没有意义
     */
    @Override
    public Re_Variable _variable_put(Object key, Re_Variable value) {
        return originalSync.put(key, value);
    }

    @Override
    public boolean _variable_has(Object key) {
        return originalSync.containsKey(key);
    }





    /**
     * 数量
     */
    @Override
    public int _variable_key_count() {
        return originalSync.size();
    }

    /**
     * @return 变量名
     * 返回不可修改的集合， 或者克隆
     */
    @Override
    public Collection<?> _variable_keys() {
        return originalSync.keySet();
    }






    @Override
    public int hashCode() {
        return originalSync.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Re_VariableMapAsObjectSynchronizedLinked) {
            return originalSync.equals(((Re_VariableMapAsObjectSynchronizedLinked) obj).originalSync);
        }
        return originalSync.equals(obj);
    }

    @Override
    public String toString() {
        return originalSync.toString();
    }
}
