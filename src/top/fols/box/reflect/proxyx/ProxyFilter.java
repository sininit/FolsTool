package top.fols.box.reflect.proxyx;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface ProxyFilter {
	public void doFilter(ProxyInvocationHandler handler,
                         Object tempObject,
                         Method annotationClassMethod, Object[] params,
                         ProxyReturn result) throws Throwable;
}
