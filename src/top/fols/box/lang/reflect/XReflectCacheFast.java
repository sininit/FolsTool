package top.fols.box.lang.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * XReflectCacheFast
 * 
 * change method getJavaClassMethodsAll0 *
 */
public class XReflectCacheFast extends XReflectCache {
    public static final XReflectCacheFast defaultInstance = new XReflectCacheFast() {
        @Override
        public XReflectCache setWeakMode(boolean b) throws RuntimeException {
            throw new RuntimeException("the default instance does not support this operation");
        }

        @Override
        protected boolean isLockSource0() {
            return true;
        }
    };

    @Override
    protected Class[] updatingGetJavaClassesAll0(Class cls) {
        // TODO Auto-generated method stub
        return super.updatingGetJavaClassesAll0(cls);
    }

    @Override
    protected Constructor[] updatingGetJavaClassConstructorsAll0(Class cls) {
        // TODO Auto-generated method stub
        return super.updatingGetJavaClassConstructorsAll0(cls);
    }

    @Override
    protected Method[] updatingGetJavaClassMethodsAll0(Class cls) {
        // TODO Auto-generated method stub
        return super.getAllInheritMethodsFast(cls);
    }

    @Override
    protected Field[] updatingGetJavaClassFieldsAll0(Class cls) {
        // TODO Auto-generated method stub
        return super.updatingGetJavaClassFieldsAll0(cls);
    }
}
