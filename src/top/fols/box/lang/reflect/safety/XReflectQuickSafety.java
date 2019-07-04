package top.fols.box.lang.reflect.safety;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.fols.box.lang.reflect.XReflect;
import top.fols.box.lang.reflect.optdeclared.XReflectQuick;

public class XReflectQuickSafety extends XReflectQuick {
	public final static XReflectQuickSafety defaultInstance = new XReflectQuickSafety();
	@Override
	protected Constructor[] getConstructors0(Class cls) {
		// TODO: Implement this method
		return XReflect.getConstructors(cls);
	}
	@Override
	protected Method[] getMethods0(Class cls) {
		// TODO: Implement this method
		return XReflect.getMethods(cls);
	}
	@Override
	protected Field[] getFields0(Class cls) {
		// TODO: Implement this method
		return XReflect.getFields(cls);
	}
	
}
