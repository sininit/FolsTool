package top.fols.box.reflect.re;

/**
 * java class static opt
 * 这玩意是一个很重要的类
 * 数组也是通过它创建的
 *
 * @see Re_IRe_Object
 */
@SuppressWarnings("rawtypes")
public class Re_PrimitiveObject_jimport extends Re_IRe_Object.IPrimitiveObject implements Re_IJavaClassWrap {

    @Override
    public boolean hasObjectKey(Re_Executor executor, Object key) throws Throwable {
        String s = Re_Utilities.toJString(key);
        return Re_Utilities.hasJavaClassVariable(executor, __class__, s);
    }

    @Override
    public boolean removeObjectKey(Re_Executor executor, Object key) throws Throwable {
        String s = Re_Utilities.toJString(key);
        return Re_Utilities.removeJavaClassValue(executor, __class__, s);
    }

    @Override
    public Object getObjectValue(Re_Executor executor, Object key) throws Throwable {
        // TODO: Implement this method
        String s = Re_Utilities.toJString(key);
        return Re_Utilities.getJavaClassValue(executor, __class__, s);
    }

    @Override
    public void putObjectValue(Re_Executor executor, Object key, Object value) throws Throwable {
        // TODO: Implement this method
        String s = Re_Utilities.toJString(key);
        Re_Utilities.setJavaClassValue(executor, __class__, s, value);
    }



    @Override
    public int getObjectKeyCount(Re_Executor executor) throws Throwable {
        return Re_Utilities.getJavaClassSize(executor,__class__);
    }

    @Override
    public Iterable getObjectKeys(Re_Executor executor) throws Throwable {
        return Re_Utilities.getJavaClassKeys(executor, __class__);
    }




    @Override
    public Object executePoint(Re_Executor executor, Object point_key, Re_CodeLoader.Call call) throws Throwable {
        // TODO: Implement this method
        Object[] callParam = executor.getExpressionValues(call, 0, call.getParamExpressionCount());
        if (executor.isReturnOrThrow()) return null;

        return Re_Utilities.invokeJavaClassMethod(executor, __class__, Re_Utilities.toJString(point_key), callParam);
    }


    @Override
    public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
        if (__ia_array__) {
            return Re_Utilities.newJArrayFromElement(executor, __class__, call);
        } else {
            Object[] callParam = executor.getExpressionValues(call, 0, call.getParamExpressionCount());
            if (executor.isReturnOrThrow()) return null;

            return Re_Utilities.newJavaInstance(executor, __class__, callParam);
        }
    }



    @Override
    public String getName() {
        return Re_Keywords.INNER_FUNCTION__JIMPORT + "{" + (__class__.getName()) + '}';
    }







    public final boolean  __ia_array__;
    public final Class<?> __class__;

    public Re_PrimitiveObject_jimport(Class<?> type) {
        this.__class__    = type;
        this.__ia_array__ = null != __class__ && __class__.isArray();
    }

    @Override
    public Class getJavaClass() {
        return __class__;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Re_PrimitiveObject_jimport re_import = (Re_PrimitiveObject_jimport) o;
        return __class__ == re_import.__class__;
    }

    @Override
    public int hashCode() {
        return __class__ != null ? __class__.hashCode() : 0;
    }
    @Override
    public String toString() {
        return "Re_JImport{" + __class__ + '}';
    }


    @Override public boolean isPrimitive() { return true; }
    @Override public boolean hasObjectKeys() { return true; }


}
