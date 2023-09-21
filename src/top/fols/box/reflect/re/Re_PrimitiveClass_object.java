package top.fols.box.reflect.re;

/**
 * {对象} 和 它的抽象 {类}
 */

public class Re_PrimitiveClass_object extends Re_PrimitiveClass {


    protected Re_PrimitiveClass_object(Re re) {
        this(re, Re_Keywords.INNER_CLASS__OBJECT);
    }
    protected Re_PrimitiveClass_object(Re re, String className) {
        super(re, className);
    }




    @Override
    protected Re_PrimitiveClassInstance newUndefinedInstance(Re_Class reClass) {
        return new Re_PrimitiveClassInstance(reClass);
    }
}
