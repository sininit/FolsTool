package top.fols.box.reflect.proxyx;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import top.fols.box.lang.Classx;
import top.fols.box.reflect.proxyx.filter.IExecuteForward;
import top.fols.box.reflect.proxyx.filter.ProxyFilterImpl;
import top.fols.box.reflect.proxyx.helper.IInvokeClass;

import java.lang.reflect.InvocationHandler;

public class ProxyFactory {
	protected <T> ProxyBuilder<T> newProxyBuilder()   { return new ProxyBuilder<T>(this); }
	protected ProxyReflecter 	  newProxyReflecter() { return new ProxyReflecter(); }
	protected ProxyFilter     	  newProxyFilter()    { return new ProxyFilterImpl(this); }

	public ProxyReflecter reflecter() { return vReflecter; }
	public ProxyFilter    filter()    { return vFilter; }



	private PropertyCaches   propertys        = new PropertyCaches();
	private AnnotationCaches annotationCaches = new AnnotationCaches();
	private ProxyReflecter vReflecter = newProxyReflecter();
	private ProxyFilter    vFilter    = newProxyFilter();





	public <T> ProxyBuilder<T> newBuilder(Class<T> annotationClass) {
		ProxyBuilder<T> pb = newProxyBuilder();
		pb.setAnnotationInterface(annotationClass);
		pb.setExtraIntefaces(null);
		return pb;
	}
	public static <T> ProxyInvocationHandler getProxyInvocationHandler(T value) {
		InvocationHandler handle = Proxy.getInvocationHandler(value);
		if (handle instanceof ProxyInvocationHandler) {
			ProxyInvocationHandler pih = (ProxyInvocationHandler) handle;
			return pih;
		}
		throw new UnsupportedOperationException(String.valueOf(value.getClass()));
	}



	static final Class<IInvokeClass> CLASS_IINVOKECLASS = IInvokeClass.class;

	public Class toAnnotationInterface(Class annotationClass) {
		if (annotationClass == null) return null;
		if (annotationClass.isInterface()) {
			return annotationClass;
		}
		return null;
	}
	public Class findAnnotationInterfaceInvokeClass(Class annotationClass) {
		IInvokeClass     isource = annotationCaches.getAnnotation(annotationClass, CLASS_IINVOKECLASS);
//		System.out.println(annotationClass);
//		System.out.println(isource);
		if (null != isource) {
			try {
				Class valueClass = isource.valueClass();
				if (null != valueClass) {
					return  valueClass;
				}
			} catch (Exception ignored) {}
			String      className = isource.value();
			if (null != className && className.length() > 0) {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				if (null == loader) {
					try {
						return Classx.forName(className);
					} catch (ClassNotFoundException ignored) {}
				} else {
					try {
						return Classx.forName(className, false, loader);
					} catch (ClassNotFoundException ignored) {}
				}
			}
			throw new UnsupportedOperationException("input @source parse failed: " + isource);
		}
		return null;
	}


	protected void doFilter(ProxyInvocationHandler handler,
							Object tempObject,
							Method annotationClassMethod, Object[] params,
							ProxyReturn result) throws Throwable {
		ProxyFilter filter;
		filter = filter();
		filter.doFilter(handler, 
						tempObject,
						annotationClassMethod, params,
						result);
	}




	public <T> T getProperty(Class<T> type) {
		return propertys.getValue(propertys.newProperty(type));
	}
	public IExecuteForward.ForwardManager getForwarder() { 
		return getProperty(IExecuteForward.ForwardManager.CLASS_FPRWARD_MANAGER);
	}
}
