package top.fols.box.reflect.re;

public class Re_PrimitiveClassInstance extends Re_ClassInstance {
    protected Re_PrimitiveClassInstance(Re_Class reClass) {
        super(reClass, Re.newPrimitiveObjectVariableMap());
    }
    protected Re_PrimitiveClassInstance(Re_Class reClass, Re_IRe_VariableMap variableMap) {
        super(reClass, variableMap);
    }


    @Override
    public boolean isPrimitive() {
        return true;
    }
}
