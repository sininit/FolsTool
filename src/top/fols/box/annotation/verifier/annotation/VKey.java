package top.fols.box.annotation.verifier.annotation;

import top.fols.box.annotation.verifier.Verifier;
import top.fols.box.annotation.verifier.VerifierManager;
import top.fols.box.annotation.verifier.VerifierObject;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import top.fols.atri.lang.Arrayz;


/**
 * Key 和 Keys 为同一个数据组 称为 Key组
 * Key 组 不可以和子元素Key组 合并 
 * 所以 如果子元素存在 Key组 一定为 子元素的Key组
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
		ElementType.TYPE,
		ElementType.FIELD,
		ElementType.METHOD,
		ElementType.PARAMETER,
		ElementType.CONSTRUCTOR,
		ElementType.LOCAL_VARIABLE,
		ElementType.ANNOTATION_TYPE,
		ElementType.PACKAGE
	})
public @interface VKey {
	String value() default "";
	
	
	public static final Class<VKey> CLASS = VKey.class;

	public static final Verifier<VKey, String, VerifierObject> verifier = new Verifier<VKey, String, VerifierObject>(){

		@Override
		public VKey newInstance(final String value) {
			return new VKey() {
				@Override
				public Class<? extends Annotation> annotationType() {
					// TODO: Implement this method
					return CLASS;
				}

				@Override
				public String value() {
					// TODO: Implement this method
					return value;
				}

				@Override
				public String toString() {
					// TODO: Implement this method
					return CLASS.getSimpleName()+"(value="+Arrayz.toString(value())+")";
				}
			};
		}

		@Override
		public VKey clone(VKey that) {
			// TODO: Implement this method
			return newInstance(that.value());
		}

		@Override
		public VKey selectMerge(VKey parrent, VKey cover) {
			// TODO: Implement this method
			return (null != cover) ?cover: parrent;
		}
		
		/**
		*
		*/
		@Override
		public boolean verify(VerifierManager.AnnotationVerifier<VKey, String, VerifierObject> that, VerifierObject object) {
			// TODO: Implement this method
			return true;
			
//			//因为 Key 和 Keys 一起注解时 Key 和Keys都会被用来检验，如果Key和keys不互相包含 则永远返回false
//			//因为 查询的时候是先检测 key的 所以根本不需要检测！
//			return that.value().equals(object.key());
		}
	};
}
