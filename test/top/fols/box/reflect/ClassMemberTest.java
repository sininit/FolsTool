package top.fols.box.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ClassMemberTest {


    @MyClassAnnotation("1")
    class MyBaseClass {
        @MyMethodAnnotation("2")
        public void myMethod() {
        }
    }

    @MyClassAnnotation("3")
    class MyClass extends MyBaseClass {
        @Override
        @MyMethodAnnotation("4")
        public void myMethod() {
        }
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyClassAnnotation {
        String value();
    }
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyMethodAnnotation {
        String value();
    }

    public static void main(String[] args) {
        ClassMember.Builder builder = new ClassMember.Builder();
        ClassMember parse = builder.parse(MyClass.class);
        System.out.println(parse.getAnnotationTable());

        ClassMember.MethodMemberTable methodMemberTable = parse.getMethodMemberTable();
        ClassMember.MethodMemberTable.MethodMember methodMemberReturnAssignableFrom = methodMemberTable.getMethodMemberReturnAssignableFrom(void.class,  "myMethod");
        System.out.println(methodMemberReturnAssignableFrom.getInheritAnnotationTable());
        System.out.println("----------------------------------");
        System.out.println(parse.getAnnotationTable());
        System.out.println(parse.getInheritAnnotationTable());
        System.out.println(parse.getClassesMemberTable());
        System.out.println(parse.getConstructorMemberTable());
        System.out.println(parse.getFieldMemberTable());
        System.out.println(parse.getMethodMemberTable());

    }
}