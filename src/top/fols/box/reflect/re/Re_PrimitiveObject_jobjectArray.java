package top.fols.box.reflect.re;

import top.fols.atri.lang.Objects;
import top.fols.atri.util.Iterables;
import top.fols.atri.util.Lists;

import java.lang.reflect.Array;

/**
 * java instance opt
 *
 * @see Re_IRe_Object
 */
@SuppressWarnings("rawtypes")
public class Re_PrimitiveObject_jobjectArray extends Re_PrimitiveObject_jobject implements Re_IJavaClassWrap {

    @Override
    public boolean hasObjectKey(Re_Executor executor, Object key) throws Throwable {
        if (key instanceof Number) {
            int index = key instanceof Integer ? (Integer) key : ((Number) key).intValue();
            if (index >= 0 && index < len)
                return true;
        }
        return false;
    }

    @Override
    public boolean removeObjectKey(Re_Executor executor, Object key) throws Throwable {
        if (key instanceof Number) {
            int index = key instanceof Integer ? (Integer) key : ((Number) key).intValue();
            if (index >= 0 && index < len) {
                Array.set(__value__, index, null);
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getObjectValue(Re_Executor executor, Object key) throws Throwable {
        // TODO: Implement this method
        if (key instanceof Number) {
            int index = key instanceof Integer ? (Integer) key : ((Number) key).intValue();
            if (index >= 0 && index < len) {
                return Array.get(__value__, index);
            }
        }
        return null;
    }

    @Override
    public void putObjectValue(Re_Executor executor, Object key, Object value) throws Throwable {
        // TODO: Implement this method
        if (key instanceof Number) {
            int index = key instanceof Integer ? (Integer) key : ((Number) key).intValue();
            if (index >= 0 && index < len) {
                Array.set(__value__, index, value);
            }
        }
    }


    @Override
    public int getObjectKeyCount(Re_Executor executor) throws Throwable {
        return len;
    }


    @Override
    public Iterable getObjectKeys(Re_Executor executor) throws Throwable {
        return Iterables.wrapRange(0, len);
    }

    @Override
    public boolean hasObjectKeys() { return true; }





    @Override
    public Object executePoint(Re_Executor executor, Object point_key, Re_CodeLoader.Call call) throws Throwable {
        // TODO: Implement this method
        Object[] callParam = executor.getExpressionValues(call, 0, call.getParamExpressionCount());
        if (executor.isReturnOrThrow()) return null;

        return Re_Utilities.invokeJavaMethod(executor, __value__, Re_Utilities.toJString(point_key), callParam);
    }


    @Override
    public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
        return executor.executeGVFromJavaArray(__value__, call);
    }

    @Override
    public String getName() {
        return Re_Keywords.INNER_FUNCTION__JOPTIONAL + "{" + (Objects.identityToString(__value__)) + '}';
    }


    int len;
    public Re_PrimitiveObject_jobjectArray(Object __value__) {
        super(__value__);
        this.len        = Array.getLength(__value__);
    }

    @Override
    public Class getJavaClass() {
        return __class__;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o) return null == __value__;
        if (getClass() != o.getClass()) return false;

        Re_PrimitiveObject_jobjectArray re_import = (Re_PrimitiveObject_jobjectArray) o;
        return Objects.equals(__value__, re_import.__value__);
    }

    @Override
    public int hashCode() {
        return __value__ != null ? __value__.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Re_JArray{" + (Objects.identityToString(__value__)) + '}';
    }
}
