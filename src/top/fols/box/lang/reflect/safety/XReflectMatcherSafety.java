package top.fols.box.lang.reflect.safety;
import top.fols.box.lang.reflect.optdeclared.XReflectMatcher;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import top.fols.box.lang.reflect.XReflect;

public class XReflectMatcherSafety extends XReflectMatcher {
	public final static XReflectMatcherSafety defaultInstance = new XReflectMatcherSafety();
	
	@Override
	public Constructor[] getConstructorsAll0(Class cls) {
		// TODO: Implement this method
		return XReflect.getConstructors(cls);
	}
	@Override
	protected Method[] getMethodsAll0(Class cls) {
		// TODO: Implement this method
		return XReflect.getMethods(cls);
	}
	@Override
	protected Method[] getMethodsAll0(Class cls, String name) {
		// TODO: Implement this method
		return XReflect.getMethodsAll(cls, name);
	}
	@Override
	protected Field[] getFieldsAll0(Class cls) {
		// TODO: Implement this method
		return XReflect.getFields(cls);
	}

}
