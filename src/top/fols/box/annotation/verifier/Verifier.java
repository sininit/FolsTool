package top.fols.box.annotation.verifier;

import top.fols.box.annotation.verifier.VerifierManager.AnnotationVerifier;
import java.lang.annotation.Annotation;

/**
 * 在Annotation中添加 static 变量 Verifier 
 * 只能有一个
 */
public abstract class Verifier<T extends Annotation, V, O extends VerifierObject> {
	public static final Verifier[]      EMPTY = {};
	public static final Class<Verifier> CLASS = Verifier.class;

	public abstract T newInstance(V value);

	//克隆 Annotation (因为 Annotation 默认是 代理, 性能非常低)
	public abstract T clone(T that);
	//合并 Annotation 
	public abstract T selectMerge(T parrent, T cover); //
	
	//验证数据
	public abstract boolean verify(AnnotationVerifier<T, V, O> that, O object);
}

