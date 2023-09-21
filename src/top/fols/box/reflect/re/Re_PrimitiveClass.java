package top.fols.box.reflect.re;

import top.fols.atri.lang.Objects;

public class Re_PrimitiveClass extends Re_Class {
    Re re;
    protected Re_PrimitiveClass(Re re,String className) {
        this(re, className, null, null);
    }
    protected Re_PrimitiveClass(Re re,String className, InitializedBefore initializedBefore) {
        this(re, className, initializedBefore, null);
    }
    protected Re_PrimitiveClass(Re re,
                                String className, InitializedBefore initializedBefore,
                                Re_Class reDeclaringClass) {
        if (null == re)
            throw new NullPointerException("null re");

        this.re = re;
        Re_CodeFile block = Re_CodeFile.create(Re_CodeFile.METHOD_NAME__PRIMITIVE_CLASS_INIT, getClass().getName() + "." + className, Re_CodeFile.LINE_OFFSET);

        Re_Class.createAfter(this, null, className, block, reDeclaringClass);
        if (null != initializedBefore) {
            initializedBefore.doExecute(this);
        }
        Re_Class.setPrimitiveClassInitialized(this);
    }



    public static abstract class InitializedBefore {
        public abstract void doExecute(Re_PrimitiveClass thatClass);
    }


    @Override
    public boolean isPrimitive() {
        return true;
    }




    @Override
    protected Re_PrimitiveClassInstance newUndefinedInstance(Re_Class reClass) {
        return new Re_PrimitiveClassInstance(reClass);
    }





    public void addInit(int line, String modifier, Re_PrimitiveClassMyCVF.IFunction ifun) {
        Re_PrimitiveClassMyCVF.InitInstaller.install(this, line,
                modifier, ifun);
    }

    public void addVariable(int line, String modifier, String name, Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker typeChecker, Object value) {
        Re_PrimitiveClassMyCVF.VarInstaller.install(this, line,
                modifier, name, typeChecker, value);
    }
    public void addVariable(int line, String modifier, String name, Object value) {
        Re_PrimitiveClassMyCVF.VarInstaller.install(this, line,
                modifier, name, null, value);
    }
    public void addVariable(int line, String modifier, String name) {
        Re_PrimitiveClassMyCVF.VarInstaller.install(this, line,
                modifier, name, null);
    }

    public void addFunction(int line, String modifier, Re_PrimitiveClassMyCVF.IFunction ifun) {
        Re_PrimitiveClassMyCVF.FunctionInstaller.install(this, line,
                modifier, ifun.name, ifun);
    }
}
