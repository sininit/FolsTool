package top.fols.box.reflect.re;

import top.fols.atri.lang.Objects;

/**
 * java instance opt
 *
 * @see Re_IRe_Object
 */
@SuppressWarnings("rawtypes")
public class Re_PrimitiveObject_jobject extends Re_IRe_Object.IPrimitiveObject implements Re_IJavaClassWrap {


    @Override
    public boolean hasObjectKey(Re_Executor executor, Object key) throws Throwable {
        String s = Re_Utilities.toJString(key);
        return Re_Utilities.hasJavaVariable(executor, __class__, s);
    }

    @Override
    public boolean removeObjectKey(Re_Executor executor, Object key) throws Throwable {
        String s = Re_Utilities.toJString(key);
        return Re_Utilities.removeJavaValue(executor, __class__, __value__, s);
    }

    @Override
    public Object getObjectValue(Re_Executor executor, Object key) throws Throwable {
        // TODO: Implement this method
        String s = Re_Utilities.toJString(key);
        return Re_Utilities.getJavaValue(executor, __class__, __value__, s);
    }

    @Override
    public void putObjectValue(Re_Executor executor, Object key, Object value) throws Throwable {
        // TODO: Implement this method
        String s = Re_Utilities.toJString(key);
        Re_Utilities.setJavaValue(executor, __class__, __value__, s, value);
    }


    @Override
    public int getObjectKeyCount(Re_Executor executor) throws Throwable {
        return Re_Utilities.getJavaObjectSize(executor, __class__);
    }


    @Override
    public Iterable getObjectKeys(Re_Executor executor) throws Throwable {
        return Re_Utilities.getJavaObjectKeys(executor, __class__);
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
        int paramExpressionCount = call.getParamExpressionCount();
        if (paramExpressionCount == 0) {
            return __value__;
        }
        executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
        return null;
    }

    @Override
    public String getName() {
        return Re_Keywords.INNER_FUNCTION__JOPTIONAL + "{" + (Objects.identityToString(__value__)) + '}';
    }


    public final Class<?>  __class__;
    public final Object    __value__;

    public Re_PrimitiveObject_jobject(Object __value__) {
        this.__class__  = __value__.getClass();
        this.__value__  = __value__;
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

        Re_PrimitiveObject_jobject re_import = (Re_PrimitiveObject_jobject) o;
        return Objects.equals(__value__, re_import.__value__);
    }

    @Override
    public int hashCode() {
        return __value__ != null ? __value__.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Re_JObject{" + (Objects.identityToString(__value__)) + '}';
    }
}
