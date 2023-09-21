package top.fols.box.reflect.re;

import java.lang.reflect.Member;

import top.fols.atri.reflect.*;
import top.fols.box.reflect.ReflectMatcherAsPeak;


/**
 * Java反射器
 * 作用是可以防止直接反射私有字段
 * 这玩意一般是在re里面用的，需要防止在re里面反射 re
 * 其实没啥用 但是起码得弄一个吧
 */
@SuppressWarnings("rawtypes")
public class Re_ReflectJava extends ReflectMatcher<Re_ReflectJava.Cache> implements Re_IJavaReflector {
    static final String RE_PACKAGE = Re.class.getPackage().getName();

    @SuppressWarnings({"SameParameterValue"})
    public static class Cache extends ReflectCache {
        public Cache() {}


        //-------------------------------------------只是公开方法而已没有实际意义---------------------------------------------
        protected ClassesList 	    getClassesList(Class cls) 			 			{ return super.getClassesList(cls);      }
        protected Class             getClasses(Class cls, String simpleName) 	    { return super.getClasses(cls, simpleName); }
        protected ConstructorList   getConstructorList(Class cls)  				    { return super.getConstructorList(cls); }
        protected FieldList         getFieldList(Class cls)						    { return super.getFieldList(cls);       }
        protected FieldList         getFieldList(Class cls, String name)  		    { return super.getFieldList(cls, name); }
        protected MethodList        getMethodList(Class p1)   					    { return super.getMethodList(p1);   }
        protected MethodList        getMethodList(Class p1, String p2)    		    { return super.getMethodList(p1, p2);   }
        //------------------------------------------------------------------------------------------------------------------



        @Override
        protected String getSimpleName0(Class<?> cls) {
            return Re_CodeLoader.intern(super.getSimpleName0(cls));
        }

        @Override
        protected String getMemberName0(Member member) {
            return Re_CodeLoader.intern(super.getMemberName0(member));
        }




        Re_ReflectJavaFilter filter = new Re_ReflectJavaFilter() {{
            addFinalEqualsClass_Name(requireFinal(Class.class));


            addFinalEqualsClass_Name(requireFinal(String.class));

            addFinalEqualsClass_Name(requireFinal(Integer.class));
            addFinalEqualsClass_Name(requireFinal(Character.class));
            addFinalEqualsClass_Name(requireFinal(Byte.class));
            addFinalEqualsClass_Name(requireFinal(Long.class));
            addFinalEqualsClass_Name(requireFinal(Float.class));
            addFinalEqualsClass_Name(requireFinal(Double.class));
            addFinalEqualsClass_Name(requireFinal(Short.class));
            addFinalEqualsClass_Name(requireFinal(Boolean.class));

            addProcessInheritedClass_Name(Object.class);
            addProcessInheritedPackage_Name(Re.class.getPackage(), Re_ReflectJavaFilter.HideMode.process_filter_ALL);
        }};



        @Override
        protected ClassesList createClassesList(Class cls) {
            return super.createClassesList(cls);
        }
        @Override
        protected ConstructorList createConstructorList(Class cls) {
            return super.createConstructorList(cls);
        }

        @Override
        protected FieldList createFieldList(Class cls) {
            return FieldList.wrap(Reflects.accessible(filter.getQuery(cls).fields()));
        }
        @Override
        protected MethodList createMethodList(Class cls) {
            return MethodList.wrap(Reflects.accessible(filter.getQuery(cls).method()));
        }
    }



    public Re_ReflectJava() {
        super(new Cache());
    }

    static class AsPeak extends ReflectMatcherAsPeak<Cache> implements Re_IJavaReflector {
        public AsPeak() {
            super(new Re_ReflectJava.Cache());
        }
    }
}


