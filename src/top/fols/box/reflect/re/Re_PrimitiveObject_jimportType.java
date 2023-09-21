package top.fols.box.reflect.re;

import top.fols.atri.interfaces.annotations.NotNull;

@SuppressWarnings("rawtypes")
public class Re_PrimitiveObject_jimportType extends Re_PrimitiveObject_jimport {
    public Re_PrimitiveObject_jimportType(Class<?> type) {
        super(type);
    }


    @Override
    public final boolean hasObjectKey(Re_Executor executor, Object key) throws Throwable {
        return false;
    }

    @Override
    public final boolean removeObjectKey(Re_Executor executor, Object key) throws Throwable {
        return false;
    }

    @Override
    public final Object getObjectValue(Re_Executor executor, Object key) throws Throwable {
        return null;
    }

    @Override
    public final void putObjectValue(Re_Executor executor, Object key, Object value) throws Throwable {
    }

    @Override
    public final int getObjectKeyCount(Re_Executor executor) throws Throwable {
        return 0;
    }

    @Override
    public @NotNull final Iterable getObjectKeys(Re_Executor executor) throws Throwable {
        return null;
    }

    @Override
    public final Object executePoint(Re_Executor executor, Object point_key, Re_CodeLoader.Call call) throws Throwable {
        String s = Re_Utilities.toJString(point_key);
        executor.setThrow(Re_Accidents.undefined(this, s));
        return null;
    }


}
