package top.fols.box.reflect.proxyx;

import java.lang.reflect.InvocationHandler;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import top.fols.atri.lang.Finals;
import top.fols.box.reflect.Proxys;
import top.fols.atri.reflect.ReflectMatcher;
import top.fols.atri.reflect.ReflectCache;
import java.lang.reflect.Proxy;

public class ProxyBuilder<E> implements Cloneable {
	private ProxyFactory vFactory;

	private Class<E> vAnnotationInterface;
	private Class[]  vInterfaces = Finals.EMPTY_CLASS_ARRAY; // no midifiy


	ProxyBuilder(ProxyFactory factory) {
		this.vFactory = factory;
	}



	public ProxyFactory factory() { return vFactory; }

	protected ProxyInvocationHandler newProxyInvocationHandler(Class  invokeClass, 
															   Object invokeObject,

															   Class<E> annotationInterfaceOne,
															   Class[]  intefaces) {
		return new ProxyInvocationHandler(this,
										  invokeClass, invokeObject, 
										  annotationInterfaceOne,
										  intefaces);
	}
	protected Object newProxyClassInstance(ClassLoader classLoader,
										   Class[]  finalInterfaces,
										   InvocationHandler invocationHandler) {
		return Proxys.createProxyClassInstance(classLoader, finalInterfaces, invocationHandler);
	}




	@Override
	protected ProxyBuilder<E>  clone() {
		// TODO: Implement this method
		try {
			return ((ProxyBuilder<E>) super.clone());
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e.getMessage());
		}
	}


	public ProxyBuilder setAnnotationInterface(Class<E> claszz0) { 
	    Class<E>    annotationInterface = (Class<E>) factory().toAnnotationInterface(claszz0);
		if (null == annotationInterface) {
			throw new UnsupportedOperationException("not a annotation interface");
		}
		this.vAnnotationInterface = annotationInterface;
		this.finalInterfaces = null;
		return this;
	}
	public Class<E>     getAnnotationInterface() {return vAnnotationInterface;}


	public ProxyBuilder setExtraIntefaces(Class[] claszz) {
		this.vInterfaces     = null == claszz ? Finals.EMPTY_CLASS_ARRAY : claszz.clone(); 
		this.finalInterfaces = null;
		return this;
	}
	public    Class[]      getExtraIntefaces()         {return this.getExtraIntefacesInternal().clone();}
	protected Class[]      getExtraIntefacesInternal() {return this.vInterfaces;}



	transient Class[] finalInterfaces;
	protected Class[] getInnerFinalIntefaces() {
		Class[] cache = finalInterfaces;
		if (null == cache) {
			Class annotationInterface = getAnnotationInterface();

			Class[] extraIntefaces = getExtraIntefacesInternal();
			if (extraIntefaces.length == 0) {
				cache = new Class[]{ annotationInterface };
			} else {
				Set<Class> set = new LinkedHashSet<>();
				if (null != annotationInterface) {
					set.add(annotationInterface);
				}
				Collections.addAll(set, extraIntefaces);
				cache = set.toArray(Finals.EMPTY_CLASS_ARRAY);
			}
			finalInterfaces = cache;
		}
		return  cache;
	}



	public E buildProxy() { 
		return buildProxy(null, 
						  factory().findAnnotationInterfaceInvokeClass(getAnnotationInterface()), 
						  null);
	}
	public E buildProxy(Class invokeClass, Object invokeObject) { 
		return buildProxy(null, 
						  invokeClass, 
						  invokeObject);
	}
	public E buildProxy(ClassLoader fromClassLoader, 
						Class invokeClass, Object invokeObject) {
//		Class  invokeClass  = getInvokeClass(); 
//		Object invokeObject = getInvokeObject(); 

		Class<E> annotationInterface = getAnnotationInterface();
		Class[]  intefaces       	 = getInnerFinalIntefaces();
		Class[]  finalInterfaces 	 = intefaces;

		if (null == invokeClass) 		 throw new NullPointerException("invoke class");
		if (null == annotationInterface) throw new NullPointerException("annotation-inteface-class");

		if (null == fromClassLoader)
			fromClassLoader = annotationInterface.getClassLoader();

		ProxyInvocationHandler invocationHandler = newProxyInvocationHandler(invokeClass, invokeObject,
																			 annotationInterface,
																			 finalInterfaces);
		Object o = newProxyClassInstance(fromClassLoader, finalInterfaces, invocationHandler);
//			if (!annotationInterfaceOne.isAssignableFrom(o.getClass()))
//				throw new IllegalArgumentException("interfaces[" + Arrays.toString(interfaces) + "] not assignable: " + annotationInterfaceOne);
		return (E) o;
	}


}
