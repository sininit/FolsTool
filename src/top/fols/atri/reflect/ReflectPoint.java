package top.fols.atri.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectPoint implements Cloneable {

	public static class StaticOption implements Cloneable {
        private Class<?> optionClass;

        public StaticOption(Class<?> cls) {
			this.optionClass = cls;
		}
        public void setOptionClass(Class<?> optionClass) { this.optionClass = optionClass; }
        public Class<?> getOptionClass() { return optionClass; }

		@Override
		public int hashCode() {
			// TODO: Implement this method
			return this.getOptionClass().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (this == obj) { return true; }
			if (obj instanceof StaticOption == false) { return false; }
			StaticOption value = (StaticOption) obj;
			return this.optionClass == value.optionClass;
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return ("static operation: "+ this.optionClass.getName());
		}

		@Override
		public StaticOption clone() {
			// TODO: Implement this method
			StaticOption clone = new StaticOption(this.getOptionClass());
			return clone;
		}
    }

	private ReflectMatcher matcher;
	private Object object;
	public ReflectPoint(ReflectMatcher matcher, Object object) {
		this.matcher = matcher;
		this.object = object;
	}
	public static ReflectPoint wrapObjectOption(ReflectMatcher matcher, Object object) {
		ReflectPoint clone = new ReflectPoint(matcher, object);
		return clone;
	}
	public static ReflectPoint wrapStaticOption(ReflectMatcher matcher, Class object) {
		ReflectPoint clone = new ReflectPoint(matcher, new StaticOption(object));
		return clone;
	}




	/**
	 * join new instance
	 */
	public ReflectPoint newInstance(Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException {
        Constructor c = this.matcher().getConstructor(this.valueClass(), args);
        return wrapObjectOption(this.matcher, c.newInstance(args));
    }
	public ReflectPoint newInstance(Class[] argsTypes, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException {
        Constructor c = this.matcher().getConstructor(this.valueClass(), argsTypes);
		return wrapObjectOption(this.matcher, c.newInstance(args));
    }


	/**
	 * join field
	 */
    public ReflectPoint get(String name) throws IllegalAccessException, IllegalArgumentException {
        return this.get(null, name);
    }
	public ReflectPoint get(Class returnClass, String name) throws IllegalAccessException, IllegalArgumentException {
        Field f = this.matcher().getField(this.valueClass(), returnClass, name);
        return wrapObjectOption(this.matcher, f.get(this.value()));
    }


	/*
	 * no callback
	 */
	public ReflectPoint set(String name, Object value) throws IllegalAccessException, IllegalArgumentException {
        return this.set(null, name, value);
    }
	public ReflectPoint set(Class returnClass, String name, Object value) throws IllegalAccessException, IllegalArgumentException {
        Field f = this.matcher().getField(this.valueClass(), returnClass, name);
        f.set(this.value(), value);
        return this;
    }



	/**
	 * join method result
	 */
    public ReflectPoint invoke(String name, Object... args) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        return this.invoke(null, name, args);
    }
    public ReflectPoint invoke(Class returnClass, String name, Object... args) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        Method m = this.matcher().getMethod(this.valueClass(), returnClass, name, args);
		return wrapObjectOption(this.matcher, m.invoke(this.value(), args));
    }
	public ReflectPoint invoke(Class returnClass, String name, Class[] argsTypes, Object... args) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        Method m = this.matcher().getMethod(this.valueClass(), returnClass, name, argsTypes);
        return wrapObjectOption(this.matcher, m.invoke(this.value(), args));
    }



    public ReflectPoint 	matcher(ReflectMatcher obj) { this.matcher = obj; return this; }
    public ReflectMatcher 	matcher() {  return this.matcher; }

    public Object 			value() { return (this.object instanceof StaticOption) ? ((StaticOption) this.object).optionClass : this.object; }
	public Class<?> 		valueClass() {
        return null == this.object ? null
            : (this.object instanceof StaticOption ? ((StaticOption) this.object).optionClass
            : this.object.getClass());
    }


    public boolean 		staticOption() {  return this.object instanceof StaticOption; }
	public boolean 		objectOption() {  return this.object instanceof StaticOption == false; }

    private ReflectPoint() { }

	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (obj instanceof ReflectPoint == false) { return false; }
		ReflectPoint value = (ReflectPoint)obj;
		return null == this.object ?null == value: this.object.equals(value);
	}

	@Override
	public int hashCode() {
		// TODO: Implement this method
		Object value = this.value();
		return null == value ?0: value.hashCode();
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		Object value = this.value();
		return null == value ?null: value.toString();
	}

    @Override
    public ReflectPoint clone() {
        // TODO Auto-generated method stub
        ReflectPoint newInstance = new ReflectPoint();
        newInstance.object = this.object;
        newInstance.matcher = this.matcher;
        return newInstance;
    }


}
