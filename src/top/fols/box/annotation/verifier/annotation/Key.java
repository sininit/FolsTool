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
 * @see annotation_verifier.annotation.test.TestMain
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
public @interface Key {
	String value() default "";
	
	
	public static final Class<Key> CLASS = Key.class;
	
	
	public static final Verifier<Key, VerifierObject> verifier = new Verifier<Key, VerifierObject>(){
		
		@Override
		public Key clone(Key that) {
			// TODO: Implement this method
		    final String value = that.value();
			return new Key() {
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
		public Key selectMerge(Key parrent, Key cover) {
			// TODO: Implement this method
			return (null != cover) ?cover: parrent;
		}
		
		/**
		*
		*/
		@Override
		public boolean verify(VerifierManager.AnnotationVerifier<Key, VerifierObject> that, VerifierObject object) {
			// TODO: Implement this method
			return true;
			
//			//因为 Key 和 Keys 一起注解时 Key 和Keys都会被用来检验，如果Key和keys不互相包含 则永远返回false
//			//因为 查询的时候是先检测 key的 所以根本不需要检测！
//			return that.value().equals(object.key());
		}
	};
}
