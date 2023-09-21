package top.fols.box.reflect.proxyx;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import top.fols.atri.lang.Classz;
import top.fols.atri.lang.Finals;

@SuppressWarnings("SpellCheckingInspection")
public class ProxyInvocationHandler<E> implements InvocationHandler, Serializable {
	private static final long serialVersionUID = 1L;


	public ProxyFactory factory() { return vFactory; }
	public ProxyBuilder builder() { return vBuilder; }

	protected ProxyReturn newReturn() { return new ProxyReturn(); }

	@Override
	public Object invoke(Object tempObject,
						 Method annotationClassMethod, Object[] params) throws Throwable {
		ProxyFactory factory = factory();
		ProxyReturn  pr      = newReturn();

		if (pr.isNonReturn()) {
			factory.doFilter(this,
							 tempObject,
							 annotationClassMethod, params,
							 pr);
		}
		if (pr.isNonReturn()) {
			String methodName = annotationClassMethod.getName();
			Method findMatchInvokeObjectMethod = this.findMatchInvokeObjectMethod(methodName, params);
			if    (findMatchInvokeObjectMethod != null) {
				pr.setReturn(findMatchInvokeObjectMethod.invoke(getInvokeObject(), params));
			} 
		}

		if (pr.isReturn()) {
			return formatReturn(pr.getReturn(), annotationClassMethod);
		} 
		throw new RuntimeException("cannot find match: " + annotationClassMethod);
	}
	public Method findMatchInvokeObjectMethod(String name, Object[] arguments) {
		return this
			.factory()
			.reflecter()
			.method(getInvokeObjectClass(), name, arguments);
	}


	/**
	 * To achieve high performance, GenericReturnType is no longer used
	 */
	public Object formatReturn(Object result, Method annotationClassMethod) {
		if (null == result) 
			return null;
		Class returnType = annotationClassMethod.getReturnType();
		if (Classz.isInstanceNullable(result, returnType)) 
			return result;

		ProxyFactory factory = factory();
		if (null != (factory.toAnnotationInterface(returnType))) {
			ProxyBuilder pb = getProxyBuilder(returnType);
			Class invokeClass = factory.findAnnotationInterfaceInvokeClass(returnType);
			if   (invokeClass == null) {
				invokeClass = result.getClass();
			} 
			Object value = pb.buildProxy(invokeClass, result);
			return value;
		}

		Class resultClass = null == result ? null : result.getClass();
		throw new RuntimeException("cannot convert result: " + "from " + (null == resultClass ?null: resultClass.getName()) + " to " + (null == returnType ?null: returnType.getName()));
	}

	protected ProxyBuilder getProxyBuilder(Class returnType) {
		return factory()
			.newProxyBuilder()
			.setAnnotationInterface(returnType)
			.setExtraIntefaces(Finals.EMPTY_CLASS_ARRAY);
	}






	final ProxyBuilder<E> vBuilder;
	final ProxyFactory    vFactory;

	private final Class<E> vAnnotationClass;
	private final Class[]  vInterfaces;
	private Class  vInvokeObjectClass;
	private Object vInvokeObject;


	public Class<E> getAnnotationInterface() { return this.vAnnotationClass;    }
	public Class[]  getInterface()           { return this.vInterfaces.clone(); }

	public Class  getInvokeObjectClass() { return vInvokeObjectClass;}
	public Object getInvokeObject()      { return vInvokeObject; }

	protected ProxyInvocationHandler(ProxyBuilder<E> builder,

									 Class<?> invokeObjectClass, Object invokeObject, 
									 Class<E> annotationInterface,
									 Class... interfaces) {
		this.vBuilder = Objects.requireNonNull(builder, "builder");
		this.vFactory = builder.factory();

		this.vInvokeObjectClass   = invokeObjectClass;
		this.vInvokeObject 	     = invokeObject;

		this.vAnnotationClass 	 = annotationInterface;
		this.vInterfaces          = interfaces.clone();
	}
}
