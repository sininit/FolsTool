package top.fols.box.reflect.proxyx;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import top.fols.atri.lang.Finals;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;
import top.fols.atri.cache.WeakMapCacheConcurrentHash;

public class AnnotationCaches {
	static final Class<Object>  CLASS_OBJECT = Object.class;
	static final Annotation[]   EMPTY_ANNOTATION_ARRAY = Finals.EMPTY_ANNOTATION_ARRAY;


	public AnnotationCaches()	{
	}



	public Map<Class<? extends Annotation>, Annotation> getLinkedHashMap(Class clazz) { return new LinkedHashMap<Class<? extends Annotation>, Annotation>(getAnnotationMap(clazz));}




	WeakMapCacheConcurrentHash<Class, Annotation[], RuntimeException> classAsArrayCache = new WeakMapCacheConcurrentHash<Class, Annotation[], RuntimeException>(){
		@Override
		public Annotation[] newCache(Class clazz) throws RuntimeException {
			// TODO: Implement this method
			return classCache.getOrCreateCache(clazz).values().toArray(EMPTY_ANNOTATION_ARRAY);
		}
	};
	WeakMapCacheConcurrentHash<Class, Map<Class<? extends Annotation>, Annotation>, RuntimeException> classCache = new WeakMapCacheConcurrentHash<Class, Map<Class<? extends Annotation>, Annotation>, RuntimeException>(){
		@Override
		public Map<Class<? extends Annotation>, Annotation> newCache(Class clazz) throws RuntimeException {
			// TODO: Implement this method
			Map<Class<? extends Annotation>, Annotation> result = new LinkedHashMap<>();

			if (clazz == null)
				return result;
			if (clazz == CLASS_OBJECT)
				return result;

			List<Annotation> annotations = getInheritAnnotationList(clazz);
			for (Annotation  annotation: annotations) {
				Class<? extends Annotation> type = annotation.annotationType();
				if (!result.containsKey(type))
					 result.put(type, annotation);
			}
			return result;
		}
	}; 

	protected Annotation[] getAnnotationArray(Class clazz) {
		return classAsArrayCache.getOrCreateCache(clazz);
	}
	protected Map<Class<? extends Annotation>, Annotation> getAnnotationMap(Class clazz) {
		return classCache.getOrCreateCache(clazz);
	}
	public <E extends Annotation> E getAnnotation(Class claszz, Class<E> type) {
		return (E) this
			.getAnnotationMap(claszz)
			.get(type);
	}






	private static List<Annotation> getInheritAnnotationList(Class<?> clazz) {
		List<Annotation> annotations = new ArrayList<>();

		// 获取当前类的所有注解
		Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
		for (Annotation annotation : declaredAnnotations) {
			annotations.add(annotation);
		}

		// 递归获取所有接口的注解
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> interfaceClass : interfaces) {
			List<Annotation> interfaceAnnotations = getInheritInterfaceAnnotations(interfaceClass);
			annotations.addAll(interfaceAnnotations);
		}

		// 递归获取所有父类的注解
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			List<Annotation> superClassAnnotations = getInheritAnnotationList(superClass);
			annotations.addAll(superClassAnnotations);
		}


		return annotations;
	}

	private static List<Annotation> getInheritInterfaceAnnotations(Class<?> interfaceClass) {
		List<Annotation> annotations = new ArrayList<>();

		// 获取当前接口的所有注解
		Annotation[] declaredAnnotations = interfaceClass.getDeclaredAnnotations();
		for (Annotation annotation : declaredAnnotations) {
			annotations.add(annotation);
		}

		// 递归获取所有父接口的注解
		Class<?>[] parentInterfaces = interfaceClass.getInterfaces();
		for (Class<?> parentInterfaceClass : parentInterfaces) {
			List<Annotation> parentInterfaceAnnotations = getInheritInterfaceAnnotations(parentInterfaceClass);
			annotations.addAll(parentInterfaceAnnotations);
		}

		return annotations;
	}

}
