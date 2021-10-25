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

public @interface VKeys {
	String[] value() default {};

	public static final Class<VKeys> CLASS = VKeys.class;

	public static final Verifier<VKeys, String[], VerifierObject> verifier = new Verifier<VKeys, String[], VerifierObject>(){
		@Override
		public VKeys newInstance(String[] value) {
			return new VKeys() {
				@Override
				public Class<? extends Annotation> annotationType() {
					// TODO: Implement this method
					return CLASS;
				}

				@Override
				public String[] value() {
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
		public VKeys clone(VKeys that) {
			// TODO: Implement this method
			final String[] value = that.value().clone();
			return newInstance(value);
		}

		@Override
		public VKeys selectMerge(VKeys parrent, VKeys cover) {
			// TODO: Implement this method
			return (null != cover) ?cover: parrent;
		}

		@Override
		public boolean verify(VerifierManager.AnnotationVerifier<VKeys, String[], VerifierObject> that, VerifierObject object) {
			// TODO: Implement this method
			return true;
			
//			//因为 Key 和 Keys 一起注解时 Key 和Keys都会被用来检验，如果Key和keys不互相包含 则永远返回false
//			//因为 查询的时候是先检测 key的 所以根本不需要检测！
//			for (String key: that.value()) {
//				if (key.equals(object.key())) {
//					return true;
//				}
//			}
//			return false;
		}
	};
}
