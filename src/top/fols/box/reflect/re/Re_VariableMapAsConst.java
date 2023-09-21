package top.fols.box.reflect.re;

import java.util.Collection;
import java.util.HashMap;

/**
 * 线程不安全一般用于不可修改值的场景
 */
@SuppressWarnings("rawtypes")
class Re_VariableMapAsConst implements Re_IRe_VariableMap {
    private HashMap<Object, Re_Variable> map = new HashMap<>();

    @Override
    public Re_IRe_VariableMap _variable_clone_all() {
        Re_VariableMapAsConst instance = new Re_VariableMapAsConst();
        instance.map = (HashMap<Object, Re_Variable>) Re_Variable.Unsafes.cloneToMap(this.map, new HashMap<Object, Re_Variable>());
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
        return map.remove(key);
    }

    /**
     * 原始获取
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     *
     */
    @Override
    public Re_Variable _variable_find_table_or_parent(Object key) {
        return map.get(key);
    }

    /**
     * 原始获取
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     *
     */
    @Override
    public Re_Variable _variable_find_table_var(Object key) {
        return map.get(key);
    }

    @Override
    public Re_Variable _variable_get(Object key) {
        return map.get(key);
    }

    /**
     * 原始提交
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     *
     * @return 没有意义
     */
    @Override
    public Re_Variable _variable_put(Object key, Re_Variable value) {
        return map.put(key, value);
    }

    @Override
    public boolean _variable_has(Object key) {
        return map.containsKey(key);
    }





    /**
     * 数量
     */
    @Override
    public int _variable_key_count() {
        return map.size();
    }

    /**
     * @return 变量名
     * 返回不可修改的集合， 或者克隆
     */
    @Override
    public Collection<?> _variable_keys() {
        return map.keySet();
    }







    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Re_VariableMapAsConst) {
            return map.equals(((Re_VariableMapAsConst) obj).map);
        }
        return map.equals(obj);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
