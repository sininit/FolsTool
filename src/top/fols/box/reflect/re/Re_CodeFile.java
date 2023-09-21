package top.fols.box.reflect.re;

import top.fols.atri.lang.Objects;

@SuppressWarnings({"rawtypes"})
public final class Re_CodeFile implements Cloneable {
    public static final String METHOD_NAME__EMPTY = "";
    public static final String METHOD_NAME__CLASS_INIT           = "<cinit>";
    public static final String METHOD_NAME__PRIMITIVE_CLASS_INIT = "<pcinit>";

    public static final String FILE_NAME__INNER_EVAL  = Re_CodeLoader.intern("<eval>");
    public static final String FILE_NAME__JAVA_SOURCE = Re_CodeLoader.intern("<source>");
    public static final String FILE_NAME__ANONYMOUS   = Re_CodeLoader.intern("<anonymous>");

    public static final int LINE_OFFSET = 1;

    Re_CodeFile() {
        Re_CodeFile block = this;
        block.constCaches = new Re_VariableMapAsConst();
    }
    public static Re_CodeFile create(String methodName, String filePath, int lineOffset) {
        Re_CodeFile block       = new Re_CodeFile();
        block.constCaches       = new Re_VariableMapAsConst();
        block.methodName        = methodName;
        block.filePath          = filePath;
        block.lineOffset        = lineOffset;
        block.expressions       = Re_CodeLoader.Base.EMPTY_EXPRESSION;
        return block;
    }
    public static Re_CodeFile create(Re_VariableMapAsConst constCacheManager,
                                     String methodName, String filePath, int lineOffset,
                                     Re_CodeLoader.Expression[] expressions) {
        Re_CodeFile block       = new Re_CodeFile();
        block.constCaches       = constCacheManager;
        block.methodName        = methodName;
        block.filePath          = filePath;
        block.lineOffset        = lineOffset;
        block.expressions       = expressions;
        return block;
    }




    String                      methodName;
    String                      filePath;
    int                         lineOffset = LINE_OFFSET; //代码在文件中的开始位置, 编译时是从这里算起的
    Re_CodeLoader.Expression[]  expressions;

    //没什么用的临时缓存
    Re_VariableMapAsConst       constCaches;
    String version;

    /**
     * @see Re_CodeLoader#load(String, String, String, String, int)
     */
    public boolean validVersion() {
        return version != null;
    }
    public String getVersion() {
        return version;
    }


    //↓以下全都是缓存↓
    //可以重复利用以下数据
    @Override
    public Re_CodeFile clone() {
        try {
            return (Re_CodeFile) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); //不可能
        }
    }

    public String getMethodName(){ return methodName; }
    public String getFilePath() { return filePath; }
    public int getLineOffset()  { return lineOffset; }







    Re_CodeLoader.Expression[] getExpressions() {
        return this.expressions;
    }





    //自从Var自带variable后就弃用了
    Re_VariableMapAsConst getConstCaches() {
        return constCaches;
    }








    /**
     * const.xxx
     * 将全部常量包装 包装每次get 的值都不会被修改
     */
    public Re_CodeLoader.Var createConstBase(Re_CodeLoader.Expression c, int line, long javavalue) {
        String name = Re_CodeLoader.intern("l_" + javavalue);
        Re_Variable variable = Re_Variable.Unsafes.getConstVariable(name, this);
        if (null == variable) {
            variable = Re_Variable.createCompileLong(javavalue);
            Re_Variable.Unsafes.addVariableOrThrowEx(name, variable, constCaches);
        } else {
            if (!Objects.equals(javavalue, Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(name, constCaches))) {
                throw new UnsupportedOperationException("repeat var: " + name);
            }
        }
        Re_CodeLoader.Var newTop = new Re_CodeLoader.Var();
        newTop.line = line;
        newTop.name = name;
        newTop.compileVariable = variable;//将关键字值直接加入代码 以后将不在获取keyword

        return newTop;
    }

    public Re_CodeLoader.Var createConstBase(Re_CodeLoader.Expression c, int line, float javavalue) {
        String name = Re_CodeLoader.intern("f_" + javavalue);
        Re_Variable variable = Re_Variable.Unsafes.getConstVariable(name, this);
        if (null == variable) {
            variable = Re_Variable.createCompileFloat(javavalue);
            Re_Variable.Unsafes.addVariableOrThrowEx(name, variable, constCaches);
        } else {
            if (!Objects.equals(javavalue, Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(name, constCaches))) {
                throw new UnsupportedOperationException("repeat var: " + name);
            }
        }
        Re_CodeLoader.Var newTop = new Re_CodeLoader.Var();
        newTop.line = line;
        newTop.name = name;
        newTop.compileVariable = variable;//将关键字值直接加入代码 以后将不在获取keyword

        return newTop;
    }

    public Re_CodeLoader.Var createConstBase(Re_CodeLoader.Expression c, int line, double javavalue) {
        String name = Re_CodeLoader.intern("d_" + javavalue);
        Re_Variable variable = Re_Variable.Unsafes.getConstVariable(name, this);
        if (null == variable) {
            variable = Re_Variable.createCompileDouble(javavalue);
            Re_Variable.Unsafes.addVariableOrThrowEx(name, variable, constCaches);
        } else {
            if (!Objects.equals(javavalue, Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(name, constCaches))) {
                throw new UnsupportedOperationException("repeat var: " + name);
            }
        }
        Re_CodeLoader.Var newTop = new Re_CodeLoader.Var();
        newTop.line = line;
        newTop.name = name;
        newTop.compileVariable = variable;//将关键字值直接加入代码 以后将不在获取keyword


        return newTop;
    }

    public Re_CodeLoader.Var createConstBase(Re_CodeLoader.Expression c, int line, short javavalue) {
        String name = Re_CodeLoader.intern("s_" + javavalue);
        Re_Variable variable = Re_Variable.Unsafes.getConstVariable(name, this);
        if (null == variable) {
            variable = Re_Variable.createCompileShort(javavalue);
            Re_Variable.Unsafes.addVariableOrThrowEx(name, variable, constCaches);
        } else {
            if (!Objects.equals(javavalue, Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(name, constCaches))) {
                throw new UnsupportedOperationException("repeat var: " + name);
            }
        }
        Re_CodeLoader.Var newTop = new Re_CodeLoader.Var();
        newTop.line = line;
        newTop.name = name;
        newTop.compileVariable = variable;//将关键字值直接加入代码 以后将不在获取keyword


        return newTop;
    }

    public Re_CodeLoader.Var createConstBase(Re_CodeLoader.Expression c, int line, byte javavalue) {
        String name = Re_CodeLoader.intern("b_" + javavalue);
        Re_Variable variable = Re_Variable.Unsafes.getConstVariable(name, this);
        if (null == variable) {
            variable = Re_Variable.createCompileByte(javavalue);
            Re_Variable.Unsafes.addVariableOrThrowEx(name, variable, constCaches);
        } else {
            if (!Objects.equals(javavalue, Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(name, constCaches))) {
                throw new UnsupportedOperationException("repeat var: " + name);
            }
        }
        Re_CodeLoader.Var newTop = new Re_CodeLoader.Var();
        newTop.line = line;
        newTop.name = name;
        newTop.compileVariable = variable;//将关键字值直接加入代码 以后将不在获取keyword


        return newTop;
    }

    public Re_CodeLoader.Var createConstBase(Re_CodeLoader.Expression c, int line, int javavalue) {
        String name = Re_CodeLoader.intern("i_" + javavalue);
        Re_Variable variable = Re_Variable.Unsafes.getConstVariable(name, this);
        if (null == variable) {
            variable = Re_Variable.createCompileInt(javavalue);
            Re_Variable.Unsafes.addVariableOrThrowEx(name, variable, constCaches);
        } else {
            if (!Objects.equals(javavalue, Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(name, constCaches))) {
                throw new UnsupportedOperationException("repeat var: " + name);
            }
        }
        Re_CodeLoader.Var newTop = new Re_CodeLoader.Var();
        newTop.line = line;
        newTop.name = name;
        newTop.compileVariable = variable;//将关键字值直接加入代码 以后将不在获取keyword


        return newTop;
    }





    public Re_CodeLoader.Var createConstBase(Re_CodeLoader.Expression c, int line, char javavalue) {
        String name = Re_CodeLoader.intern("c_" + javavalue);
        Re_Variable variable = Re_Variable.Unsafes.getConstVariable(name, this);
        if (null == variable) {
            variable = Re_Variable.createCompileChar(javavalue);
            Re_Variable.Unsafes.addVariableOrThrowEx(name, variable, constCaches);
        } else {
            if (!Objects.equals(javavalue, Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(name, constCaches))) {
                throw new UnsupportedOperationException("repeat var: " + name);
            }
        }
        Re_CodeLoader.Var newTop = new Re_CodeLoader.Var();
        newTop.line = line;
        newTop.name = name;
        newTop.compileVariable = variable;//将关键字值直接加入代码 以后将不在获取keyword


        return newTop;
    }


    private long id = 0;
    private long nextID() { return ++id; }
    /**
     * 下下策， string不能比较只能每个string做一个新的对象了
     */
    public Re_CodeLoader.Var createConstBase(Re_CodeLoader.Expression c, int line, String javavalue) {
        String v = Re_CodeLoader.intern(javavalue); //破罐子破摔
        String name = Re_CodeLoader.intern("str_" + nextID());
        Re_Variable variable = Re_Variable.Unsafes.getConstVariable(name, this);
        if (null == variable) {
            variable = Re_Variable.createCompileString(v);
            Re_Variable.Unsafes.addVariableOrThrowEx(name, variable, constCaches);
        } else {
            if (!Objects.equals(javavalue, Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(name, constCaches))) {
                throw new UnsupportedOperationException("repeat var: " + name);
            }
        }
        Re_CodeLoader.Var newTop = new Re_CodeLoader.Var();
        newTop.line = line;
        newTop.name = name;
        newTop.compileVariable = variable;//将关键字值直接加入代码 以后将不在获取keyword

        return newTop;
    }








    @SuppressWarnings({"UnnecessaryModifier", "UnnecessaryInterfaceModifier"})
    public static interface IRe_CompileVariable {
        public String getCompileDeclareValue();
    }
    public static boolean isCompileVariable(Re_Variable<?> o) {
        return o instanceof IRe_CompileVariable;
    }
    public static String getCompileDeclareValue(Re_Variable<?> o) {
        return o instanceof IRe_CompileVariable ?((IRe_CompileVariable)o).getCompileDeclareValue():null;
    }

}
