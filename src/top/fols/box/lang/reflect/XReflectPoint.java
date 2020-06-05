package top.fols.box.lang.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class XReflectPoint implements Cloneable {

    public static class StaticOption {
        private Class<?> optionClass;

        public StaticOption(Class<?> cls) {
            this.optionClass = cls;
        }

        public void setOptionClass(Class<?> optionClass) {
            this.optionClass = optionClass;
        }

        public Class<?> getOptionClass() {
            return optionClass;
        }
    }

    private Object currentObject;
    private XReflectMatcher matcher;

    /**
     * exec class static method or option field
     * 
     * @param cls
     */
    public XReflectPoint(Class<?> cls) {
        this(new StaticOption(cls), null);
    }
    /**
     * exec instance method or option field
     * 
     * @param obj
     */
    public XReflectPoint(Object instance) {
        this(instance, null);
    }


    public XReflectPoint(Class<?> cls, XReflectMatcher matcher) {
        this(new StaticOption(cls), matcher);
    }
    public XReflectPoint(Object instance, XReflectMatcher matcher) {
        this.currentObject = instance;
        this.matcher = null == matcher ? new XReflectMatcher() : matcher;
    }




    public XReflectPoint c(Object... args)
    throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException {
        Constructor c = this.getMatcher().getConstructor(this.resultClass(), args);
        this.set(c.newInstance(args));
        return this;
    }
    

    public XReflectPoint f(String name) throws IllegalAccessException, IllegalArgumentException {
        Field f = this.getMatcher().getField(this.resultClass(), name);
        this.set(f.get(this.result()));
        return this;
    }


    public XReflectPoint m(String name, Object... args)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        Method m = this.getMatcher().getMethod(this.resultClass(), name, args);
        this.set(m.invoke(this.result(), args));
        return this;
    }

    public XReflectPoint m(Class returnClass, String name, Object... args)
    throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        Method m = this.getMatcher().getMethod(this.resultClass(), returnClass, name, args);
        this.set(m.invoke(this.result(), args));
        return this;
    }




    public XReflectPoint set(Class<?> obj) {
        this.currentObject = new StaticOption(obj);
        return this;
    }

    public XReflectPoint set(Object obj) {
        this.currentObject = obj;
        return this;
    }

    public XReflectPoint setMatcher(XReflectMatcher obj) {
        this.matcher = obj;
        return this;
    }

    public XReflectMatcher getMatcher() {
        return this.matcher;
    }

    public Object result() {
        return (null == this.currentObject || this.currentObject instanceof StaticOption) ? null : this.currentObject;
    }

    public Class<?> resultClass() {
        return null == this.currentObject ? null
            : (this.currentObject instanceof StaticOption ? ((StaticOption) this.currentObject).optionClass
            : this.currentObject.getClass());
    }

    public boolean isStaticOption() {
        return this.currentObject instanceof StaticOption;
    }

    public XReflectPoint thisObject() {
        return this;
    }

    private XReflectPoint() {
    }

    @Override
    public XReflectPoint clone() {
        // TODO Auto-generated method stub
        XReflectPoint newInstance = new XReflectPoint();
        newInstance.currentObject = this.currentObject;
        newInstance.matcher = this.matcher;
        return newInstance;
    }

}
