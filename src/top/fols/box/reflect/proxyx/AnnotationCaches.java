package top.fols.box.reflect.proxyx;
import java.lang.annotation.Annotation;

import top.fols.atri.cache.WeakMapCacheConcurrentHash;
import top.fols.box.reflect.ClassMember;

public class AnnotationCaches {
	static final ClassMember.CacheBuilder instance = new ClassMember.CacheBuilder();
	public AnnotationCaches() {}


	@SuppressWarnings("unchecked")
	public <E extends Annotation> E getAnnotation(Class<?> clazz, Class<E> type) {
		ClassMember parse = instance.parse(clazz);
		ClassMember.AnnotationMemberTable annotationTable = parse.getAnnotationTable();
		return (E) annotationTable.getAnnotationFirst(type);
	}

}
