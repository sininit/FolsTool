package top.fols.box.reflect.re;

import top.fols.atri.assist.util.ArrayLists;
import top.fols.atri.lang.Classz;
import top.fols.atri.interfaces.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

import static top.fols.box.reflect.re.Re_CodeLoader.*;
import static top.fols.box.reflect.re.Re_CodeLoader.Call.createCallCreateDict;
import static top.fols.box.reflect.re.Re_CodeLoader._CompileTimeCodeListEditor.getAsCallCreateDict;
import static top.fols.box.reflect.re.Re_CodeLoader._CompileTimeCodeListEditor.getAsVar;
import static top.fols.box.reflect.re.Re_CodeLoader._CompileTimeCodeSourceReader.*;
import static top.fols.box.reflect.re.Re_CodeLoader_ExpressionConverts.CallFunction.*;
import static top.fols.box.reflect.re.Re_Modifiers.*;
import static top.fols.box.reflect.re.Re_Utilities.getReClassFromIReGetClass;
import static top.fols.box.reflect.re.Re_Keywords.keyword;

public class Re_CodeLoader_ExpressionConverts {
    static void checkModifierResult(Re_CodeLoader._CompileTimeCodeListEditor editor, Base current, String help, String modifierError) {
        if (null != modifierError) {
            throw new Re_Accidents.CompileTimeGrammaticalException(
                    "error:[" + modifierError + "], " +
                            "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                            "help:[" + help + "]",
                    editor.block.getFilePath(), current.getLine());
        }
    }
    static void checkParameters(Re_CodeLoader._CompileTimeCodeListEditor editor, Base current, String help, String... params) {
        for (String paramName : params) {
            if (Re_Keywords.is_keyword_key(paramName)) {
                throw new Re_Accidents.CompileTimeGrammaticalException(
                    "error:[" + paramName + " is keyword], " +
                    "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                    "help:[" + help + "]",
                    editor.block.getFilePath(), current.getLine());
            }
        }
    }



    static class CallClass {
        static public void addExpressionConverterCallClass() {
            final String name = Re_Keywords.INNER_EXPRESSION_CALL__CLASS;
            {
                final Re_CodeLoader.Var var0;
                var0 = new Re_CodeLoader.Var();
                var0.name = name;

                final Re_CodeLoader.Call call0;
                call0 = new Re_CodeLoader.Call();
                call0.name = name;

                final String help = "use " +
                        var0 + new Re_CodeLoader.CallCreateDict()+
                        " or " +
                        var0 + " name " + new Re_CodeLoader.CallCreateDict();

                //cls a {} ...
                addExpressionConverter(var0, new Re_CodeLoader._CompileTimeCodeSourceReader.ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Re_CodeLoader.Var current      = (Re_CodeLoader.Var) now;

                        int  twoIndex    = editor.findNextNonNull();
                        Re_CodeLoader.Base two         = editor.getOrNull(twoIndex);

                        Re_CodeLoader.CallCreateDict dict;
                        String className;
                        if (codeIsVar(two)) {
                            className = two.getName();
                            int  threeIndex   = editor.findNextNonNull(twoIndex + 1);
                            Re_CodeLoader.Base three0       = editor.getOrNull(threeIndex);

                            if (codeIsCallCreateDict(three0)) {
                                Re_CodeLoader_ExpressionConverts.checkParameters(editor, current, help, className);//check keyword name

                                dict = (Re_CodeLoader.CallCreateDict) three0;
                                Re_CodeLoader.ConvertExpressionAsCallClass c = new Re_CodeLoader.ConvertExpressionAsCallClass();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                                c.className  = className;
                                c.executeExpressions = dict;

                                editor.setNull(currentIndex);
                                editor.setNull(twoIndex);
                                editor.set(threeIndex,   c);

                                editor.seek(threeIndex + 1);
                                return;
                            }
                        } else if (codeIsCallCreateDict(two)) {
                            className = null;
                            dict = (Re_CodeLoader.CallCreateDict) two;

                            Re_CodeLoader.ConvertExpressionAsCallClass c = new Re_CodeLoader.ConvertExpressionAsCallClass();
                            c.setLine(current.getLine());
                            c.setName(current.getName());
                            c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                            c.className  = className;
                            c.executeExpressions = dict;

                            editor.setNull(currentIndex);
                            editor.set(twoIndex,       c);

                            editor.seek(twoIndex + 1);
                            return;
                        }

                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[ " + Re_CodeLoader.Expression.getExpressionAsString(editor.list) + " ]" +
                                        "help:[ " + help + " ]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
                addExpressionConverter(call0, new Re_CodeLoader._CompileTimeCodeSourceReader.ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[ " + Re_CodeLoader.Expression.getExpressionAsString(editor.list) + " ]" +
                                        "help:[ " + help + " ]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) {
                        // TODO: Implement this method
                        Re_CodeLoader.ConvertExpressionAsCallClass c = (Re_CodeLoader.ConvertExpressionAsCallClass) call;
                        Re_CodeLoader.Expression[] expressions = c.getClassExpression().getBuildParamExpressionCaches();
                        return Re_Class.RuntimeUtils.createReClassAndInitializeSetLocalVar(executor, c.className, call.getLine(), expressions);
                    }
                }, keyword);
            }
        }
    }
    static class CallInit {
        static public void addExpressionConverterCallInitFunction() {
            final String name = Re_Keywords.INNER_EXPRESSION_CALL__SET_INIT_FUNCTION;
            {
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;

                final String help = "use " +
                        Call.demoCall1(name, "param") + new CallCreateDict();

                //init() {}
                addExpressionConverter(call0, new ExpressionConverter() {
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        int index    = editor.index();
                        Call current = (Call) now;

                        int rmOffset = index;
                        //read modifiers
                        Set<String> modifierSet = new LinkedHashSet<>();
                        for (int offset = editor.findPrevNonNull(index - 1); offset >= 0; offset = editor.findPrevNonNull(offset - 1)) {
                            Base orNull = editor.getOrNull(offset);
                            if (codeIsVar(orNull)) {
                                Var v = (Var) orNull;
                                String name = v.getName();

                                if (Re_Modifiers.isModifier(name)) {
                                    modifierSet.add(name);
                                    rmOffset = offset;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        int mod       = Re_Modifiers.parseModifier(modifierSet);

                        int  lastIndex = editor.findNextNonNull();
                        Base two      = editor.getOrNull(lastIndex);

                        Base dict = two;

                        if (codeIsCallCreateDict(dict)) {
                            CallCreateDict functionCodes = (CallCreateDict) dict;

                            ConvertExpressionAsCallInit c = new ConvertExpressionAsCallInit();
                            c.setLine(current.getLine());
                            c.setName(current.getName());
                            c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(name);

                            CallFunction.FunParamTypes pe = CallFunction.FunParamTypes.readTypes(current);
                            if (null != pe) {
                                String modifierError = INIT_MV.testModifier(modifierSet);
                                Re_CodeLoader_ExpressionConverts.checkModifierResult(editor, current, help, modifierError);//check modifier result
                                Re_CodeLoader_ExpressionConverts.checkParameters(editor, current, help, pe.paramName);//check keyword name

                                Re_CodeLoader_ExpressionConverts_MyCVF.MyInit myInit = Re_CodeLoader_ExpressionConverts_MyCVF.createMyInit(mod,
                                        pe.paramName, pe.types,
                                        functionCodes);
                                c.myInit = myInit;
                                c._installer = myInit.installer;

                                editor.seek(lastIndex + 1);
                                int rmCount = editor.index() - rmOffset;
                                editor.setNullRange(rmOffset, rmOffset + rmCount);
                                editor.set(rmOffset, c);
                                return;
                            }
                        }

                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
                //init ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallInit c = (ConvertExpressionAsCallInit) call;
                        Object result     = c._installer.set(c, executor);
                        if (executor.isReturnOrThrow()) return null;

                        return result;
                    }
                }, keyword);
            }
        }
    }

    static class CallVar {
        static final String TYPE_DECLARE_SYMBOL = Re_Keywords.INNER_MATH_FUNCTION__COLON_SET_VALUE;


        static final  String name = Re_Keywords.INNER_EXPRESSION_VAR__VAR;

        static final  Var var0;
        static   {
            var0 = new Var();
            var0.name = name;
        }
        static final  Call call0;
        static  {
            call0 = new Call();
            call0.name = name;
        }

        static final String help =
                "use " + var0 + CODE_BLANK_SPACE_CHARS + "variableName = expression..." + CODE_SEMICOLON_SEPARATOR;



        static String unsupported_var_type(TypeChecker checker, Object vale) {
            return "var mismatch, " + "(" + checker.name + FUNCTION_TYPE_DECLARE_SYMBOL + checker.getTypesAsName() + ")"
                    + ", require=" + checker.getTypesValueAsName()
                    + ", input="   + Re_Accidents.typeReOrJava(vale) + Re_Utilities.objectAsName(vale);
        }

        static TypeChecker createTypeCheckerFromRuntime(Re_Executor executor, String name, final Var[] types) {
            int index = -1;
            TypeChecker t;
            if (types.length == 1) {
                Var type = types[0];
                Object typeObject = executor.localValue(type);
                if (executor.isReturnOrThrow()) return null;

                if (null == (t = TypeChecker.createCheckerFromRuntime(index, name, type, typeObject))) {
                    executor.setThrow("not a type " + name + FUNCTION_TYPE_DECLARE_SYMBOL + type + ", type=" + Re_Utilities.objectAsName(typeObject));
                    return null;
                }
            } else {
                final TypeChecker[] subChecker = new TypeChecker[types.length];
                for (int ti = 0; ti < types.length; ti++) {
                    Var type = types[ti];
                    Object typeObject = executor.localValue(type);
                    if (executor.isReturnOrThrow()) return null;

                    if (null == (t = TypeChecker.createCheckerFromRuntime(index, name, type, typeObject))) {
                        executor.setThrow("not a type " + name + FUNCTION_TYPE_DECLARE_SYMBOL + type + ", type=" + Re_Utilities.objectAsName(typeObject));
                        return null;
                    }
                    subChecker[ti] = t;
                }
                t = new TypeChecker(index, name, null, null) {
                    @Override
                    public String getTypesAsName() {
                        return paramTypesAsString(types);
                    }
                    @Override
                    public String getTypesValueAsName() {
                        String[] names = new String[subChecker.length];
                        for (int i = 0; i < subChecker.length; i++) {
                            names[i] = Re_Accidents.typeReOrJava(subChecker[i].typeValue) + Re_Utilities.objectAsName(subChecker[i].typeValue);
                        }
                        return Re_CodeLoader.CallCreateList.demoList2(names);
                    }

                    @Override
                    public boolean isInstanceof(Object value) {
                        // TODO: Implement this method
                        for (TypeChecker tc: subChecker) {
                            if (tc.isInstanceof(value)) {
                                return true;
                            }
                        }
                        return false;
                    }
                };
            }
            return t;
        }


        static Var[] readTypesVarsFromVarOrList(Re_CodeLoader.Base ass) {
            if (codeIsVar(ass)) {
                return new Var[]{(Var) ass};
            } else if (codeIsCallCreateList(ass)) {
                Call call = (Call) ass;
                return _Utilities.Expressions.getCallParamVarArray(call);
            }
            return null;
        }

        static final Re_Modifiers.ModifierVerifier varMv = Re_Modifiers.VAR_MV;
        static public void addExpressionConverterKeywordVar() {
            {
                //var ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        int index = editor.index;
                        int count = editor.size();

                        int rmOffset = index;
                        //read modifiers
                        Set<String> modifierSet = new LinkedHashSet<>();
                        for (int offset = editor.findPrevNonNull(index - 1); offset >= 0; offset = editor.findPrevNonNull(offset - 1)) {
                            Base orNull = editor.getOrNull(offset);
                            if (codeIsVar(orNull)) {
                                Var v = (Var) orNull;
                                String name = v.getName();

                                if (Re_Modifiers.isModifier(name)) {
                                    modifierSet.add(name);
                                    rmOffset = offset;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        int mod       = Re_Modifiers.parseModifier(modifierSet);

                        //var
                        index = (-1 == index ? -1 : editor.findNextNonNull(index + 1));

                        //read    var: type  var
                        Base variable = editor.getOrNull(index);
                        index = (-1 == index ? -1 : editor.findNextNonNull(index + 1));
                        if (!codeIsVar(variable)) {
                            throw new Re_Accidents.CompileTimeGrammaticalException(
                                    "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                            "help:[" + help + "]",
                                    editor.block.getFilePath(), current.getLine());
                        }

                        String varName = variable.getName();
                        Var[]  types = null;

                        //: =
                        Base typeSymbol = editor.getOrNull(index);
                        index = (-1 == index ? -1 : editor.findNextNonNull(index + 1));
                        T: {
                            //这个时候 符号还没有转换为符号方法 *****和方法参数不同
                            if (codeIsConvertBeforeSymbol(typeSymbol) && TYPE_DECLARE_SYMBOL.equals(typeSymbol.getName())) {
                                Base type_VarOrList = editor.getOrNull(index);
                                index = (-1 == index ? -1 : editor.findNextNonNull(index + 1));
                                if (null != (types = readTypesVarsFromVarOrList(type_VarOrList))) {
                                    if (types.length == 0) types = null;

                                    Base assignmentSymbol = editor.getOrNull(index);
                                    index = (-1 == index ? -1 : editor.findNextNonNull(index + 1));
                                    if (codeIsAssignment(assignmentSymbol)) {
                                        break T;
                                    } else if (index == -1) {
                                        break T;
                                    }
                                }
                            } else if (codeIsAssignment(typeSymbol)) {
                                break T;
                            } else if (index == -1) {
                                break T;
                            }

                            throw new Re_Accidents.CompileTimeGrammaticalException(
                                    "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                            "help:[" + help + "]",
                                    editor.block.getFilePath(), current.getLine());
                        }
                        if (index == -1)
                            index = count;

                        //read = after
                        ArrayLists<Base> ass = editor.subList(index, count);

                        Expression expression = Expression.createExpression(current.getLine(), ass);
                        Re_CodeLoader._CompileTimeCodeSourceReader.formatExpression(editor.block, expression);

                        ConvertExpressionAsKeywordVar c = new ConvertExpressionAsKeywordVar();
                        c.setLine(current.getLine());
                        c.setName(current.getName());
                        c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(name);

                        String modifierError = varMv.testModifier(modifierSet);
                        Re_CodeLoader_ExpressionConverts.checkModifierResult(editor, current, help, modifierError);//check modifier result
                        Re_CodeLoader_ExpressionConverts.checkParameters(editor, current, help, varName);//check keyword name

                        Re_CodeLoader_ExpressionConverts_MyCVF.MyVariable myVariable = Re_CodeLoader_ExpressionConverts_MyCVF.createMyVariable(varName, types, mod,
                                ass.size() == 0? null : expression);
                        c.myVariable = myVariable;
                        c._builder   = myVariable.variableBuilder;
                        c._installer = myVariable.installer;

                        editor.setNullRange(rmOffset, count);
                        editor.set(rmOffset, c);
                        return;
                    }
                });
                //var() ...
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });


                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) {
                        ConvertExpressionAsKeywordVar c = (ConvertExpressionAsKeywordVar) call;
                        String variableName = c.myVariable.name;

                        Object result     = c._installer.set(c,
                                executor, variableName);
                        if (executor.isReturnOrThrow()) return null;

                        return result;
                    }
                }, keyword);
            }
        }
    }

    static class CallFunction {
        static final String FUNCTION_TYPE_DECLARE_SYMBOL = Re_Keywords.INNER_MATH_FUNCTION__COLON_SET_VALUE;

        static String paramTypesAsString(Var[] types) {
            if (null == types) {
                return CallCreateList.demoList2();
            } else if (types.length == 1) {
                return types[0].getName();
            } else {
                String[] names = new String[types.length];
                for (int i = 0; i < types.length; i++) {
                    names[i] = types[i].getName();
                }
                return CallCreateList.demoList2(names);
            }
        }


        static String unsupported_param_type(TypeChecker checker, Object vale) {
            return "param mismatch, " + "(" + checker.name + FUNCTION_TYPE_DECLARE_SYMBOL + checker.getTypesAsName() + ")"
                    + ", require=" + checker.getTypesValueAsName()
                    + ", input="   + Re_Accidents.typeReOrJava(vale) + Re_Utilities.objectAsName(vale);
        }
        static String unsupported_return_type(TypeChecker checker, Object vale) {
            return "return mismatch, " + "(" + FUNCTION_TYPE_DECLARE_SYMBOL + checker.getTypesAsName() + ")"
                    + ", require=" + checker.getTypesValueAsName()
                    + ", output="  + Re_Accidents.typeReOrJava(vale) + Re_Utilities.objectAsName(vale);
        }



        @SuppressWarnings("SpellCheckingInspection")
        public static abstract class TypeChecker {
            public static final TypeChecker TYPECHECKER_FUNCTION_TYPE_CHECK = new TypeChecker(0, "value",
                    null, null) {

                public String getTypesAsName()      { return Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION; }
                public String getTypesValueAsName() { return Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION; }

                @Override
                public boolean isInstanceof(Object value) {
                    return Re_Utilities.isReFunction(value);
                }
            };

            static final TypeChecker[] EMPTY_ARRAY = {};

            int    index;
            String name;
            Var    type;
            Object typeValue;

            TypeChecker(int index, String name, Var type, Object typeValue) {
                this.index = index;
                this.name = name;
                this.type = type;
                this.typeValue = typeValue;
            }
            public abstract boolean isInstanceof(Object value);

            public String getTypesAsName() {
                return type.getName();
            }
            public String getTypesValueAsName() {
                return Re_Accidents.typeReOrJava(typeValue) + Re_Utilities.objectAsName(typeValue);
            }


            static TypeChecker createCheckerFromRuntime(int index, String name, Var typeName, Object typeValue) {
                if (null == typeValue) {
                    return new TypeChecker(index, name, typeName, typeValue) {
                        @Override
                        public boolean isInstanceof(Object o) {
                            // TODO: Implement this method
                            return null == o;
                        }
                    };
                } else {
                    final Re_Class reClass = getReClassFromIReGetClass(typeValue);
                    if (null != reClass) {
                        return new TypeChecker(index, name, typeName, reClass) {
                            @Override
                            public boolean isInstanceof(Object o) {
                                // TODO: Implement this method
                                return Re_Class.isInstanceOf(o, reClass);
                            }
                        };
                    } else {
                        final Class<?> clasz = Re_Utilities.objectAsJavaClass(typeValue);
                        if (null != clasz) {
                            return new TypeChecker(index, name, typeName, clasz) {
                                @Override
                                public boolean isInstanceof(Object o) {
                                    // TODO: Implement this method
                                    return Classz.isInstance(o, clasz, false);
                                }
                            };
                        }
                    }
                }
                return null;
            }

            @Nullable
            static TypeChecker[] createParamTypesCheckerFromRuntime(Re_Executor executor, FunParamTypesElement[] ptypes) {
                if (null == ptypes || ptypes.length == 0)
                    return null;
                TypeChecker[] checkers = new TypeChecker[ptypes.length];
                for (int i = 0; i < ptypes.length; i++) {
                    FunParamTypesElement element = ptypes[i];

                    int index            = element.index;
                    String name          = element.name;
                    final Var[] types    = element.typeVar;

                    TypeChecker t = createReturnTypeCheckerFromRuntime(executor, index, name, types);
                    if (executor.isReturnOrThrow()) return null;
                    checkers[i] = t;
                }
                return checkers;
            }

            static final TypeChecker NOT_CHECK_RETURN = new TypeChecker(-1, "return", null, null) {
                @Override
                public boolean isInstanceof(Object o) {
                    // TODO: Implement this method
                    return true;
                }
            };
            @Nullable
            static TypeChecker createReturnTypeCheckerFromRuntime(Re_Executor executor, final Var[] types) {
                if (null == types || types.length == 0)
                    return null;

                int index      = -1;
                String name    = "return";

                return createReturnTypeCheckerFromRuntime(executor, index, name, types);
            }
            static TypeChecker createReturnTypeCheckerFromRuntime(Re_Executor executor, int index, String name, final Var[] types) {
                TypeChecker t;
                if (types.length == 1) {
                    Var type = types[0];
                    Object typeObject = executor.localValue(type);
                    if (executor.isReturnOrThrow()) return null;

                    if (null == (t = createCheckerFromRuntime(index, name, type, typeObject))) {
                        executor.setThrow("not a type " + name + FUNCTION_TYPE_DECLARE_SYMBOL + type + ", type=" + Re_Utilities.objectAsName(typeObject));
                        return null;
                    }
                } else {
                    final TypeChecker[] subChecker = new TypeChecker[types.length];
                    for (int ti = 0; ti < types.length; ti++) {
                        Var type = types[ti];
                        Object typeObject = executor.localValue(type);
                        if (executor.isReturnOrThrow()) return null;

                        if (null == (t = createCheckerFromRuntime(index, name, type, typeObject))) {
                            executor.setThrow("not a type " + name + FUNCTION_TYPE_DECLARE_SYMBOL + type + ", type=" + Re_Utilities.objectAsName(typeObject));
                            return null;
                        }
                        subChecker[ti] = t;
                    }
                    t = new TypeChecker(index, name, null, null) {
                        @Override
                        public String getTypesAsName() {
                            return paramTypesAsString(types);
                        }
                        @Override
                        public String getTypesValueAsName() {
                            String[] names = new String[subChecker.length];
                            for (int i = 0; i < subChecker.length; i++) {
                                names[i] = Re_Accidents.typeReOrJava(subChecker[i].typeValue) + Re_Utilities.objectAsName(subChecker[i].typeValue);
                            }
                            return Re_CodeLoader.CallCreateList.demoList2(names);
                        }

                        @Override
                        public boolean isInstanceof(Object value) {
                            // TODO: Implement this method
                            for (TypeChecker tc: subChecker) {
                                if (tc.isInstanceof(value)) {
                                    return true;
                                }
                            }
                            return false;
                        }
                    };
                }
                return t;
            }

            public static class PrimitiveTypeCheckerJavaNull extends TypeChecker {
                String typeName;
                public PrimitiveTypeCheckerJavaNull(int index, String name, String type, Re_Class typeValue) {
                    super(index, name, null, typeValue);
                    this.typeName = type;
                }

                public String getTypesAsName() { return typeName; }
                public String getTypesValueAsName() { return Re_Accidents.typeReOrJava(typeValue) + Re_Utilities.objectAsName(typeValue); }

                @Override
                public boolean isInstanceof(Object value) {
                    return null == value;
                }
            }
            public static class PrimitiveTypeCheckerJavaClass extends TypeChecker {
                String typeName;
                public PrimitiveTypeCheckerJavaClass(int index, String name, String type, Class<?> typeValue) {
                    super(index, name, null, typeValue);
                    this.typeName = type;
                }

                public String getTypesAsName() { return typeName; }
                public String getTypesValueAsName() { return Re_Accidents.typeReOrJava(typeValue) + Re_Utilities.objectAsName(typeValue); }

                @Override
                public boolean isInstanceof(Object value) {
                    Class<?> clasz = (Class<?>) typeValue;
                    return Classz.isInstance(value, clasz, false);
                }
            }
            public static class PrimitiveTypeCheckerReClass extends TypeChecker {
                String typeName;
                public PrimitiveTypeCheckerReClass(int index, String name, String type, Re_Class typeValue) {
                    super(index, name, null, typeValue);
                    this.typeName = type;
                }

                public String getTypesAsName() { return typeName; }
                public String getTypesValueAsName() { return Re_Accidents.typeReOrJava(typeValue) + Re_Utilities.objectAsName(typeValue); }

                @Override
                public boolean isInstanceof(Object value) {
                    Re_Class reClass = (Re_Class) typeValue;
                    return Re_Class.isInstanceOf(value, reClass);
                }
            }
        }


        static class FunParamTypesElement {
            int    index;
            String name;
            Var[]  typeVar;

            static final FunParamTypesElement[] EMPTY_ARRAY = {};

            public FunParamTypesElement(int index, String name, Var[] typeVar) {
                this.index = index;
                this.name = name;
                this.typeVar = typeVar;
            }
            static ArrayLists<FunParamTypesElement> arr() {
                return new ArrayLists<>(EMPTY_ARRAY);
            }
        }

        static class FunParamTypes {
            String[] paramName;
            FunParamTypesElement[] types;//length is not necessarily equal

            static Var[] readTypesVarsFromVarOrList(Re_CodeLoader.Base ass) {
                if (codeIsVar(ass)) {
                    return new Var[]{(Var) ass};
                } else if (codeIsCallCreateList(ass)) {
                    Call call = (Call) ass;
                    return _Utilities.Expressions.getCallParamVarArray(call);
                }
                return null;
            }
            static FunParamTypes readTypes(Re_CodeLoader.Call call) {
                int paramCount = call.getParamExpressionCount();
                String[] names = new String[paramCount];

                ArrayLists<FunParamTypesElement> typesTemp = FunParamTypesElement.arr();
                for (int i = 0; i < paramCount; i++) {
                    Re_CodeLoader.Expression    expression          = call.tempInnerParamList.get(i);
                    ArrayLists<Base> expressionBaseList  = expression.tempInnerBaseList;

                    if (expressionBaseList.size() == 1) {
                        Base base = expressionBaseList.get(0);
                        if (codeIsVar(base)) {
                            names[i] = base.getName();
                            continue;
                        } else {
                            Re_CodeLoader.Base symbol = expressionBaseList.get(0); // symbol : converted > :(p1, p2)
                            if (codeIsCallSymbol(symbol) && symbol.equalsName(FUNCTION_TYPE_DECLARE_SYMBOL)) {
                                Re_CodeLoader.CallSymbol cs = (Re_CodeLoader.CallSymbol) symbol;

                                Re_CodeLoader.Expression nameExpr = cs.tempInnerParamList.opt(0);
                                Re_CodeLoader.Base nameExprB0 = nameExpr.tempInnerBaseList.get(0);
                                if (codeIsVar(nameExprB0)) { String name = nameExprB0.getName();
                                    names[i] = name;

                                    Re_CodeLoader.Expression typeExpr = cs.tempInnerParamList.opt(1);
                                    if (typeExpr.tempInnerBaseList.size() == 1) {
                                        Re_CodeLoader.Base typeExprB0 = typeExpr.tempInnerBaseList.get(0);
                                        Var[] types = readTypesVarsFromVarOrList(typeExprB0);
                                        if (null != types) {
                                            if (types.length != 0) {
                                                typesTemp.add(new FunParamTypesElement(i, name, types));
                                            }
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return null;
                }
                FunParamTypes pe = new FunParamTypes();
                pe.paramName = names;
                pe.types = typesTemp.toArray();
                return pe;
            }
        }


        static public void addExpressionConverterCallFunction() {
            final String name = Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION;
            {
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;

                final String help = "use " +
                        var0 + CODE_BLANK_SPACE_CHARS + Call.demoCall1("", "param")     + new CallCreateDict() +
                        " or " +
                        var0 + CODE_BLANK_SPACE_CHARS + Call.demoCall1("name", "param") + new CallCreateDict();

                //fun(){} 匿名
                addExpressionConverter(call0, new _CompileTimeCodeSourceReader.ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int  index   = editor.index();
                        Call current = (Call) now;

                        int rmOffset = index;
                        //read modifiers
                        Set<String> modifierSet = new LinkedHashSet<>();
                        for (int offset = editor.findPrevNonNull(index - 1); offset >= 0; offset = editor.findPrevNonNull(offset - 1)) {
                            Base orNull = editor.getOrNull(offset);
                            if (codeIsVar(orNull)) {
                                Var v = (Var) orNull;
                                String name = v.getName();

                                if (Re_Modifiers.isModifier(name)) {
                                    modifierSet.add(name);
                                    rmOffset = offset;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        int mod       = Re_Modifiers.parseModifier(modifierSet);


                        int  lastIndex = editor.findNextNonNull();
                        Base two       = editor.getOrNull(lastIndex);

                        C: {
                            Var[] returnType = null;
                            Base dict;
                            if (null != two && two.equalsName(FUNCTION_TYPE_DECLARE_SYMBOL)) {
                                int sIndex = editor.findNextNonNull(lastIndex + 1);
                                Base  s = editor.getOrNull(sIndex);
                                Var[] types = FunParamTypes.readTypesVarsFromVarOrList(s);
                                if (null != types) {
                                    int fIndex = editor.findNextNonNull(sIndex + 1);
                                    Base f = editor.getOrNull(fIndex);

                                    returnType = types;
                                    dict = f;
                                    lastIndex = fIndex;
                                } else {
                                    break C;
                                }
                            } else {
                                dict = two;
                            }

                            if (codeIsCallCreateDict(dict)) {
                                Re_CodeLoader.CallCreateDict functionCodes = (Re_CodeLoader.CallCreateDict) dict;

                                ConvertExpressionAsCallFunction c = new ConvertExpressionAsCallFunction();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(name);

                                FunParamTypes pe = FunParamTypes.readTypes(current);
                                if (null !=   pe) {
                                    String modifierError = FUNCTION_ANONYMOUS_MV.testModifier(modifierSet);
                                    Re_CodeLoader_ExpressionConverts.checkModifierResult(editor, current, help, modifierError);//check modifier result
                                    Re_CodeLoader_ExpressionConverts.checkParameters(editor, current, help, pe.paramName);//check keyword name

                                    Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction myFunction = Re_CodeLoader_ExpressionConverts_MyCVF.createMyFunction(mod,
                                            null,
                                            pe.paramName, pe.types,
                                            returnType,
                                            functionCodes);
                                    c.myFunction = myFunction;
                                    c._builder   = myFunction.variableBuilder;
                                    c._installer = myFunction.installer;

                                    editor.seek(lastIndex + 1);
                                    int rmCount = editor.index() - rmOffset;
                                    editor.setNullRange(rmOffset, rmOffset + rmCount);
                                    editor.set(rmOffset, c);
                                    return;
                                }
                            }
                        }


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
                //fun a() {}
                addExpressionConverter(var0, new _CompileTimeCodeSourceReader.ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int index = editor.index();
                        Base current     = editor.getOrNull(index);

                        int rmOffset = index;
                        //read modifiers
                        Set<String> modifierSet = new LinkedHashSet<>();
                        for (int offset = editor.findPrevNonNull(index - 1); offset >= 0; offset = editor.findPrevNonNull(offset - 1)) {
                            Base orNull = editor.getOrNull(offset);
                            if (codeIsVar(orNull)) {
                                Var v = (Var) orNull;
                                String name = v.getName();

                                if (Re_Modifiers.isModifier(name)) {
                                    modifierSet.add(name);
                                    rmOffset = offset;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        int mod       = Re_Modifiers.parseModifier(modifierSet);

                        int firstIndex    = editor.findNextNonNull();
                        Base first        = editor.getOrNull(firstIndex);

                        if (codeIsCall(first)) {
                            Call firstCall = (Call) first;

                            int lastIndex  = editor.findNextNonNull(firstIndex + 1);
                            Base two       = editor.getOrNull(lastIndex);

                            C: {
                                Var[] returnType = null;
                                Base dict;
                                if (null != two && two.equalsName(FUNCTION_TYPE_DECLARE_SYMBOL)) {
                                    int sIndex = editor.findNextNonNull(lastIndex + 1);
                                    Base  s = editor.getOrNull(sIndex);
                                    Var[] types = FunParamTypes.readTypesVarsFromVarOrList(s);
                                    if (null != types) {
                                        int fIndex = editor.findNextNonNull(sIndex + 1);
                                        Base f = editor.getOrNull(fIndex);

                                        returnType = types;
                                        dict = f;
                                        lastIndex = fIndex;
                                    } else {
                                        break C;
                                    }
                                } else {
                                    dict = two;
                                }

                                if (codeIsCallCreateDict(dict)) {
                                    Re_CodeLoader.CallCreateDict functionCodes = (Re_CodeLoader.CallCreateDict) dict;
                                    String functionName = firstCall.getName();

                                    ConvertExpressionAsCallFunction c = new ConvertExpressionAsCallFunction();
                                    c.setLine(current.getLine());
                                    c.setName(current.getName());
                                    c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(name);

                                    FunParamTypes pe = FunParamTypes.readTypes(firstCall);
                                    if (null != pe) {
                                        String modifierError = FUNCTION_MV.testModifier(modifierSet);
                                        Re_CodeLoader_ExpressionConverts.checkModifierResult(editor, current, help, modifierError);//check modifier result
                                        Re_CodeLoader_ExpressionConverts.checkParameters(editor, current, help, functionName);//check keyword name
                                        Re_CodeLoader_ExpressionConverts.checkParameters(editor, current, help, pe.paramName);//check keyword name

                                        Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction myFunction = Re_CodeLoader_ExpressionConverts_MyCVF.createMyFunction(mod,
                                                functionName,
                                                pe.paramName, pe.types,
                                                returnType,
                                                functionCodes);
                                        c.myFunction = myFunction;
                                        c._builder   = myFunction.variableBuilder;
                                        c._installer = myFunction.installer;

                                        editor.seek(lastIndex + 1);
                                        int rmCount = editor.index() - rmOffset;
                                        editor.setNullRange(rmOffset, rmOffset + rmCount);
                                        editor.set(rmOffset, c);
                                        return;
                                    }
                                }
                            }
                        }

                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @SuppressWarnings("rawtypes")
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallFunction c = (ConvertExpressionAsCallFunction) call;
                        String variableName = c.myFunction.functionName;

                        Object result     = c._installer.set(c,
                                executor, variableName);
                        if (executor.isReturnOrThrow()) return null;

                        return result;
                    }
                }, keyword);
            }
        }
    }

    static class CallFor {
        static public void addExpressionConverterCallFor() {
            final String name = Re_Keywords.INNER_EXPRESSION_CALL__FOR;
            {
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;

                final String help = "use " +
                        Call.demoCall1(name, "initExpression", "conditionalExpression", "afterExpression") + new CallCreateDict();

                //for() {}
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Call current = (Call) now;

                        if (current.getParamExpressionCount() == 3) {
                            int  twoIndex   = editor.findNextNonNull();
                            Base two        = editor.getOrNull(twoIndex);

                            if (codeIsCallCreateDict(two)) {
                                Re_CodeLoader.CallCreateDict twoDict = (Re_CodeLoader.CallCreateDict) two;

                                ConvertExpressionAsCallFor c = new ConvertExpressionAsCallFor();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);


                                c.initExpression = current.tempInnerParamList.get(0);
                                c.conditionalExpression = current.tempInnerParamList.get(1);
                                c.afterExpression = current.tempInnerParamList.get(2);

                                c.executeExpressions = twoDict;

                                editor.setNull(currentIndex);
                                editor.set(twoIndex, c);

                                editor.seek(twoIndex + 1);
                                return;
                            }
                        }


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
                //for ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallFor c = (ConvertExpressionAsCallFor) call;
                        Expression[] buildParamExpressionCaches = c.executeExpressions.getBuildParamExpressionCaches();

                        executor.getExpressionValue(c.initExpression);
                        if (executor.isReturnOrThrow()) return null;

                        while (true) {
                            Object v = executor.getExpressionValue(c.conditionalExpression);
                            if (executor.isReturnOrThrow()) return null;

                            if (!Re_Utilities.ifTrue(v)) return null;

                            executor.getExpressionLastValue(buildParamExpressionCaches, 0, buildParamExpressionCaches.length);     //expression
                            if (executor.isReturnOrThrow()) {
                                Object aReturn = executor.getResult();
                                if (Re_Utilities.Continue.isContinue(aReturn)) {
                                    executor.clearReturn();
//                              	continue;   //Java里不能直接 continue 因为后面的第三段代码还需要执行， 这里直接省略continue 即可， 第三段代码会执行
                                } else if (Re_Utilities.Break.isBreak(aReturn)) {
                                    executor.clearReturn();
                                    return aReturn;
                                } else {
                                    return aReturn;
                                }
                            }

                            executor.getExpressionValue(c.afterExpression);
                            if (executor.isReturnOrThrow()) return null;
                        }
                    }
                }, keyword);
            }
        }

    }
    static class CallWhile {
        static public void addExpressionConverterCallWhile() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_CALL__WHILE;
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;

                final String help = "use " +
                        Call.demoCall1(name, "condition") + new CallCreateDict();

                //while() {}
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Call current = (Call) now;

                        if (current.getParamExpressionCount() == 1) {
                            int  twoIndex   = editor.findNextNonNull();
                            Base two        = editor.getOrNull(twoIndex);

                            if (codeIsCallCreateDict(two)) {
                                Re_CodeLoader.CallCreateDict twoDict = (Re_CodeLoader.CallCreateDict) two;

                                ConvertExpressionAsCallWhile c = new ConvertExpressionAsCallWhile();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                                c.conditionalExpression = current.tempInnerParamList.get(0);

                                c.executeExpressions = twoDict;

                                editor.setNull(currentIndex);
                                editor.set(twoIndex, c);

                                editor.seek(twoIndex + 1);
                                return;
                            }
                        }


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
                //while ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_CALL__WHILE) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallWhile c = (ConvertExpressionAsCallWhile) call;
                        Expression[] buildParamExpressionCaches = c.executeExpressions.getBuildParamExpressionCaches();
                        while (true) {
                            Object v = executor.getExpressionValue(c.conditionalExpression);
                            if (executor.isReturnOrThrow()) return null;

                            if (!Re_Utilities.ifTrue(v)) return null;

                            executor.getExpressionLastValue(buildParamExpressionCaches, 0, buildParamExpressionCaches.length);    //expression
                            if (executor.isReturnOrThrow()) {
                                Object aReturn = executor.getResult();
                                if (Re_Utilities.Continue.isContinue(aReturn)) {
                                    executor.clearReturn();
                                    continue;
                                } else if (Re_Utilities.Break.isBreak(aReturn)) {
                                    executor.clearReturn();
                                    return aReturn;
                                } else {
                                    return aReturn;
                                }
                            }
                        }
                    }
                }, keyword);
            }
        }
    }
    static class CallForeach {
        static public void addExpressionConverterCallForeach() {
            final String name = Re_Keywords.INNER_EXPRESSION_CALL__FOREACH;
            {
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;

                final String help = "use " +
                        Call.demoCall1(name, "k", "v", "objectExpression") + new CallCreateDict();

                //foreach() {}
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Call current = (Call) now;

                        if (current.getParamExpressionCount() == 3) {
                            int  twoIndex = editor.findNextNonNull();
                            Base two      = editor.getOrNull(twoIndex);

                            String kName = _Utilities.Expressions.getExpressionAsVarName(current.tempInnerParamList.get(0));
                            String vName = _Utilities.Expressions.getExpressionAsVarName(current.tempInnerParamList.get(1));
                            Expression objectExpression = current.tempInnerParamList.get(2);

                            if (null != kName && null != vName && codeIsCallCreateDict(two)) {
                                Re_CodeLoader.CallCreateDict twoDict = (Re_CodeLoader.CallCreateDict) two;

                                ConvertExpressionAsCallForeach c = new ConvertExpressionAsCallForeach();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);


                                c.kName = kName;
                                c.vName = vName;
                                c.objectExpression = objectExpression;

                                c.executeExpressions = twoDict;

                                editor.setNull(currentIndex);
                                editor.set(twoIndex, c);

                                editor.seek(twoIndex + 1);
                                return;
                            }
                        }


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
                //foreach
                addExpressionConverter(var0, new ExpressionConverter() {

                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @SuppressWarnings("rawtypes")
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallForeach c = (ConvertExpressionAsCallForeach) call;

                        String k_var_name   = c.kName;
                        String v_var_name   = c.vName;

                        Object iterable = executor.getExpressionValue(c.objectExpression);
                        if (executor.isReturnOrThrow()) return null;

                        Re_IRe_Iterable iterableWrap = Re_Utilities.toReIterable(executor, iterable);
                        if (executor.isReturnOrThrow()) return null;

                        @SuppressWarnings("ConstantConditions")
                        Re_IRe_Iterable.ReIterator iterator = iterableWrap.iterator();

                        Re_Variable key_variable   = iterator.keyVariable();
                        Re_Variable.UnsafesRe.VariableInstaller k = new Re_Variable.UnsafesRe.VariableInstaller(k_var_name, key_variable);
                        k.installOrSetThrow(executor);
                        if (executor.isReturnOrThrow()) return null;


                        Re_Variable value_variable = iterator.valueVariable();
                        Re_Variable.UnsafesRe.VariableInstaller v = new Re_Variable.UnsafesRe.VariableInstaller(v_var_name, value_variable);
                        v.installOrSetThrow(executor);
                        if (executor.isReturnOrThrow()) return null;

                        Expression[] buildParamExpressionCaches = c.executeExpressions.getBuildParamExpressionCaches();

                        try {
                            while (iterator.hasNext()) {
                                iterator.next(executor);
                                if (executor.isReturnOrThrow()) return null;

                                executor.getExpressionLastValue(buildParamExpressionCaches, 0, buildParamExpressionCaches.length);    //expression
                                if (executor.isReturnOrThrow()) {
                                    Object aReturn = executor.getResult();
                                    if (Re_Utilities.Continue.isContinue(aReturn)) {
                                        executor.clearReturn();
                                        continue;
                                    } else if (Re_Utilities.Break.isBreak(aReturn)) {
                                        executor.clearReturn();
                                        return aReturn;
                                    } else {
                                        return aReturn;
                                    }
                                }
                            }
                        } finally {
                            //remove
                            k.uninstall();
                            v.uninstall();
                        }
                        return null;
                    }
                }, keyword);
            }
        }
    }
    static class CallTry {
        static public void addExpressionConverterCallTry() {
            final String name = Re_Keywords.INNER_EXPRESSION_CALL__TRY;
            {
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;


                final String help = "use " +
                        name + new CallCreateDict() + Call.demoCall1(Re_Keywords.INNER_EXPRESSION_CALL__CATCH,"e") + new CallCreateDict() + Re_Keywords.INNER_EXPRESSION_CALL__FINALLY + new CallCreateDict();

                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
                //try{}catch(e){}finally{}
                addExpressionConverter(var0, new ExpressionConverter() {



                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        int currentIndex = editor.index();
                        Var current      = (Var) now; //try

                        CallCreateDict executeExpression = null;
                        String catchName                      = null;
                        CallCreateDict catchExpression   = null;
                        CallCreateDict finallyExpression = null;

                        int endIndex;

                        int  twoIndex               = editor.findNextNonNull();
                        CallCreateDict twoDict = getAsCallCreateDict(editor, twoIndex);//{}

                        T: if (null != twoDict) {
                            executeExpression = twoDict;
                            endIndex = twoIndex;

                            int  threeIndex = editor.findNextNonNull(twoIndex + 1);
                            Base three      = editor.getOrNull(threeIndex);

                            if (codeIsCall(three) && three.equalsName(Re_Keywords.INNER_EXPRESSION_CALL__CATCH)) {//catch(e)
                                Call threeCall = (Call) three;
                                if (threeCall.getParamExpressionCount() == 1) {
                                    if (null == (catchName = _Utilities.Expressions.getExpressionAsVarName(threeCall.tempInnerParamList.get(0)))) {
                                        break T;
                                    };
                                    endIndex = threeIndex;
                                    int  fourIndex = editor.findNextNonNull(threeIndex + 1);
                                    CallCreateDict fourDict  = getAsCallCreateDict(editor, fourIndex);//{}
                                    if (null == fourDict) {
                                        break T;
                                    } else {
                                        catchExpression = fourDict;
                                        endIndex = fourIndex;

                                        int fiveIndex = editor.findNextNonNull(fourIndex + 1);
                                        Var asVar = getAsVar(editor, fiveIndex);
                                        if (null != asVar && asVar.equalsName(Re_Keywords.INNER_EXPRESSION_CALL__FINALLY)) {
                                            int  sixIndex = editor.findNextNonNull(fiveIndex + 1);
                                            CallCreateDict sixDict  = getAsCallCreateDict(editor, sixIndex);//{}
                                            if (null == sixDict) {
                                                break T;
                                            } else {
                                                finallyExpression = sixDict;
                                                endIndex = sixIndex;
                                            }
                                        } else {
                                            // pass
                                        }
                                    }
                                } else break T;
                            } else if (codeIsVar(three) && three.equalsName(Re_Keywords.INNER_EXPRESSION_CALL__FINALLY)) {   //finally
                                int  fourIndex = editor.findNextNonNull(threeIndex + 1);
                                CallCreateDict fourDict  = getAsCallCreateDict(editor, fourIndex);//{}
                                if (null == fourDict) {
                                    break T;
                                } else {
                                    finallyExpression = fourDict;
                                    endIndex = fourIndex;
                                }
                            }

                            ConvertExpressionAsCallTry c = new ConvertExpressionAsCallTry();
                            c.setLine(current.getLine());
                            c.setName(current.getName());
                            c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                            c.executeExpressions = executeExpression;
                            c.catchName          = catchName;
                            c.catchExpressions   = catchExpression;
                            c.finallyExpressions = finallyExpression;

                            for (int i = currentIndex; i <= endIndex; i++) {
                                editor.setNull(i);
                            }
                            editor.set(endIndex, c);

                            editor.seek(endIndex + 1);
                            return;
                        }

                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());

                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallTry c = (ConvertExpressionAsCallTry) call;
                        Expression[] executeExpressions = c.executeExpressions.getBuildParamExpressionCaches();
                        String catchName                  = c.catchName;
                        Expression[] catchExpressions   = null == c.catchExpressions ? null : c.catchExpressions.getBuildParamExpressionCaches();
                        Expression[] finallyExpressions = null == c.finallyExpressions ? null : c.finallyExpressions.getBuildParamExpressionCaches();
                        return executor.try_(executeExpressions, catchName, catchExpressions, finallyExpressions);
                    }
                }, keyword);
            }
        }
    }
    static class CallIf {
        static final String name = Re_Keywords.INNER_EXPRESSION_CALL__IF;
        static class IfReadResult {
            ConvertExpressionAsCallIf result;
            int lastIndex;

            public IfReadResult(ConvertExpressionAsCallIf result, int lastIndex) {
                this.result = result;
                this.lastIndex = lastIndex;
            }
        }
        public static IfReadResult readIfCall0(_CompileTimeCodeListEditor editor, int index) {
            Base base = editor.getOrNull(index);
            Call current = (Call) base;
            if (name.equals(current.name)) {
                if (current.getParamExpressionCount() == 1) {
                    //if(){}else {}
                    //if(){}else if(){}
                    Expression conditionalExpression = current.tempInnerParamList.get(0);
                    CallCreateDict executeExpressions;
                    CallCreateDict elseDict = null;

                    int twoIndex            = editor.findNextNonNull(index + 1);
                    CallCreateDict trueDict = getAsCallCreateDict(editor, twoIndex);//{}
                    T: if (null != trueDict) {
                        executeExpressions = trueDict;

                        int seekIndex;

                        int  threeIndex = editor.findNextNonNull(twoIndex + 1);
                        Base three      = editor.getOrNull(threeIndex);

                        boolean elseIsIfCall = false;
                        ConvertExpressionAsCallIf elseIfCall = null;

                        if (codeIsVar(three) && three.equalsName(Re_Keywords.INNER_EXPRESSION_CALL__ELSE)) {   //else
                            int  fourIndex = editor.findNextNonNull(threeIndex + 1);
                            Base f = editor.getOrNull(fourIndex);
                            if (codeIsCall(f) && name.equals(f.getName())) {
                                IfReadResult ifCall = readIfCall0(editor, fourIndex);
                                ConvertExpressionAsCallIf readResult = ifCall.result;
                                Expression elseExpression = Expression.createExpression(readResult.getLine(), ifCall.result);
                                elseDict = createCallCreateDict(readResult.getLine(), elseExpression);
                                seekIndex = ifCall.lastIndex;

                                elseIsIfCall = true;
                                elseIfCall = readResult;
                            } else {
                                CallCreateDict elseDict0  = getAsCallCreateDict(editor, fourIndex);//{}
                                if (null == elseDict0) {
                                    break T;
                                } else {
                                    seekIndex = fourIndex + 1;
                                    elseDict = elseDict0;
                                }
                            }
                        } else {
                            seekIndex = twoIndex + 1;
                        }

                        ConvertExpressionAsCallIf c = new ConvertExpressionAsCallIf();
                        c.setLine(current.getLine());
                        c.setName(current.getName());
                        c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                        c.conditionalExpression = conditionalExpression;
                        c.executeDict    = executeExpressions;
                        c.elseDict       = elseDict;

                        //无用字段
                        c._elseIsIfCall = elseIsIfCall;
                        c._elseIfCall   = elseIfCall;
                        return new IfReadResult(c, seekIndex);
                    }
                }
            }
            throw new Re_Accidents.CompileTimeGrammaticalException(
                    "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                            "help:[" + help + "]",
                    editor.block.getFilePath(), current.getLine());
        }


        static final Call call0;
        static final Var var0;
        static final String help;
        static {
            call0 = new Call();
            call0.name = name;

            var0 = new Var();
            var0.name = name;

            help = "use " +
                    Call.demoCall1(name, "conditionExpression") + new CallCreateDict() + Re_Keywords.INNER_EXPRESSION_CALL__ELSE + new CallCreateDict();
        }


        static public void addExpressionConverterCallIf() {
            {
                //if(){}else{}
                addExpressionConverter(call0, new ExpressionConverter() {


                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int index = editor.index();
                        IfReadResult read = readIfCall0(editor, index);
                        for (int i = index; i < read.lastIndex;i++) {
                            editor.setNull(i);
                        }
                        editor.set(index, read.result);
                        editor.seek(read.lastIndex);
                    }
                });
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());

                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallIf c = (ConvertExpressionAsCallIf) call;
                        boolean b = Re_Utilities.ifTrue(executor.getExpressionValue(c.conditionalExpression));
                        if (executor.isReturnOrThrow()) return null;

                        if (b) {
                            Expression[] buildParamExpressionCaches = c.executeDict.getBuildParamExpressionCaches();
                            return executor.getExpressionLastValue(buildParamExpressionCaches, 0, buildParamExpressionCaches.length);
                        } else {
                            if (null != c.elseDict) {
                                Expression[] buildParamExpressionCaches = c.elseDict.getBuildParamExpressionCaches();
                                return executor.getExpressionLastValue(buildParamExpressionCaches, 0, buildParamExpressionCaches.length);
                            }
                            return null;
                        }
                    }
                }, keyword);
            }
        }
    }
    static class CallBased {
        static String name = Re_Keywords.INNER_EXPRESSION_CALL__BASED;

        static final String help =
                "use " + Call.demoCall1(name, "varExpression") + new CallCreateDict();

        static public void addExpressionConverterCallBased() {
            final Var var0;
            var0 = new Var();
            var0.name = name;

            final Call call0;
            call0 = new Call();
            call0.name = name;

            addExpressionConverter(var0, new ExpressionConverter() {
                @Override
                public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                    // TODO: Implement this method
                    throw new Re_Accidents.CompileTimeGrammaticalException(
                            "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                    "help:[" + help + "]",
                            editor.block.getFilePath(), current.getLine());
                }
            });

            addExpressionConverter(call0, new ExpressionConverter() {
                @Override
                public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                    // TODO: Implement this method

                    int currentIndex = editor.index();
                    Call current = (Call) now;

                    if (current.getParamExpressionCount() == 1) {
                        int  twoIndex   = editor.findNextNonNull();
                        Base two        = editor.getOrNull(twoIndex);

                        if (codeIsCallCreateDict(two)) {
                            Re_CodeLoader.CallCreateDict twoDict = (Re_CodeLoader.CallCreateDict) two;

                            ConvertExpressionAsCallBased c = new ConvertExpressionAsCallBased();
                            c.setLine(current.getLine());
                            c.setName(current.getName());
                            c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(name);

                            c.conditionalExpression = current.tempInnerParamList.get(0);

                            c.executeExpressions = twoDict;

                            editor.setNull(currentIndex);
                            editor.set(twoIndex, c);

                            editor.seek(twoIndex + 1);
                            return;
                        }
                    }


                    throw new Re_Accidents.CompileTimeGrammaticalException(
                            "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                    "help:[" + help + "]",
                            editor.block.getFilePath(), current.getLine());
                }
            });

            Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                @Override
                public Object executeThis(Re_Executor executor, Call call) {
                    ConvertExpressionAsCallBased c = (ConvertExpressionAsCallBased) call;
                    return executor.based_(c.conditionalExpression, c.executeExpressions);
                }
            }, keyword);
        }
    }



    static class KeywordContinue {
        static public void addExpressionConverterKeywordContinue() {
            final String name = Re_Keywords.INNER_EXPRESSION_VAR__CONTINUE;
            {
                final Var var0;
                var0 = new Var();
                var0.name = name;

                final Call call0;
                call0 = new Call();
                call0.name = name;

                final String help = "use " + var0;


                //continue ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int offset = editor.index + 1;
                        int len = editor.list.size() - offset;
                        if (len != 0) {
                            throw new Re_Accidents.CompileTimeGrammaticalException(
                                    "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                            "help:[" + help + "]",
                                    editor.block.getFilePath(), current.getLine());
                        }

                        ConvertExpressionAsCallKeywordContinue c = new ConvertExpressionAsCallKeywordContinue();
                        c.setLine(current.getLine());
                        c.setName(current.getName());
                        c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);

                        editor.set(editor.index, c);

                        editor.seek(editor.index + 1);
                    }
                });
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });

                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        // TODO: Implement this method
                        return Re_Utilities.setReturnContinue(executor);
                    }
                }, keyword);
            }
        }
    }
    static class KeywordBreak {
        static public void addExpressionConverterKeywordBreak() {
            final String name = Re_Keywords.INNER_EXPRESSION_VAR__BREAK;
            {
                final Var var0;
                var0 = new Var();
                var0.name = name;

                final Call call0;
                call0 = new Call();
                call0.name = name;

                final String help = "use " + var0;

                //break ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int offset = editor.index + 1;
                        int len = editor.list.size() - offset;
                        if (len != 0) {
                            throw new Re_Accidents.CompileTimeGrammaticalException(
                                    "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                            "help:[" + help + "]",
                                    editor.block.getFilePath(), current.getLine());
                        }

                        ConvertExpressionAsCallKeywordBreak c = new ConvertExpressionAsCallKeywordBreak();
                        c.setLine(current.getLine());
                        c.setName(current.getName());
                        c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);

                        editor.set(editor.index, c);

                        editor.seek(editor.index + 1);
                    }
                });
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });

                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        // TODO: Implement this method
                        return Re_Utilities.setReturnBreak(executor);
                    }
                }, keyword);
            }
        }
    }


    static class KeywordImport {
        static public void addExpressionConverterKeywordImport() {
            final String name = Re_Keywords.INNER_EXPRESSION_CALL__IMPORT;
            {
                final Var var0;
                var0 = new Var();
                var0.name = name;

                final Call call0;
                call0 = new Call();
                call0.name = name;

                final String help = "use " + var0 + CODE_BLANK_SPACE_CHARS + "class-name";

                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallKeywordImport c = (ConvertExpressionAsCallKeywordImport) call;
                        String className = c.getJavaOrReClassName();
                        return Re_Utilities.loadReClassOrJavaClassAndSetLocalVar(executor, className);
                    }
                }, keyword);

                //import ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int offset = editor.index + 1;
                        int len = editor.list.size() - offset;
                        if (len == 0) {
                            throw new Re_Accidents.CompileTimeGrammaticalException(
                                    "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                            "help:[" + help + "]",
                                    editor.block.getFilePath(), current.getLine());
                        }
                        String JavaOrReClassName = Re_CodeLoader.Expression.getExpressionAsString(editor.list, offset, len);

                        editor.setNull(editor.index);

                        for (int i = editor.index + 1; i < editor.list.size(); i++) {
                            editor.setNull(i);
                        }

                        ConvertExpressionAsCallKeywordImport c = new ConvertExpressionAsCallKeywordImport();
                        c.JavaOrReClassName = JavaOrReClassName;
                        c.setLine(current.getLine());
                        c.setName(current.getName());
                        c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);

                        editor.set(editor.index, c);

                        editor.seek(editor.list.size());
                    }
                });
                //import()...
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
        }
    }



    static class KeywordReturn {
        static public void addExpressionConverterKeywordReturn() {
            final String name = Re_Keywords.INNER_EXPRESSION_CALL__RETURN;
            {
                final Var var0;
                var0 = new Var();
                var0.name = name;

                final Call call0;
                call0 = new Call();
                call0.name = name;

                final String help = "use " + var0 + CODE_BLANK_SPACE_CHARS + "expression...";

                //return ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        int index  = editor.index;
                        if (index != 0) {
                            throw new Re_Accidents.CompileTimeGrammaticalException(
                                    "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                    "help:[" + help + "]",
                                    editor.block.getFilePath(), current.getLine());
                        }
                        editor.setNull(0);
                        Expression expression = Expression.createExpression(current.getLine(), editor.list);
                        Re_CodeLoader._CompileTimeCodeSourceReader.formatExpression(editor.block, expression);

                        Call call = Call.createCall(current.getName(), current.getLine(), expression);
                        call.compileVariable = Re_Variable.Unsafes.getKeywordVariable(name);

                        ArrayLists<Base> list = new ArrayLists<>(Base.EMPTY_BASE);
                        list.add(call);
                        onExpression.tempInnerBaseList = list;
                        editor.list = list;

                        editor.seek(1);
                    }
                });
                //return() ... 不处理
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                    }
                });

                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        Object result = executor.getExpressionLastValue(call, 0, call.getParamExpressionCount());
                        if (executor.isReturnOrThrow()) return null; // throw or return();

                        executor.setReturn(result);
                        return result;
                    }
                }, keyword);
            }
        }
    }


    static class KeywordThrow {
        static public void addExpressionConverterKeywordThrow() {
            final String name = Re_Keywords.INNER_EXPRESSION_CALL__THROW;
            {
                final Var var0;
                var0 = new Var();
                var0.name = name;

                final Call call0;
                call0 = new Call();
                call0.name = name;

                final String help = "use " + var0 + CODE_BLANK_SPACE_CHARS + "expression...";

                //throw ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        int index  = editor.index;
                        if (index != 0) {
                            throw new Re_Accidents.CompileTimeGrammaticalException(
                                    "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                            "help:[" + help + "]",
                                    editor.block.getFilePath(), current.getLine());
                        }
                        editor.setNull(0);
                        Expression expression = Expression.createExpression(current.getLine(), editor.list);
                        Re_CodeLoader._CompileTimeCodeSourceReader.formatExpression(editor.block, expression);

                        Call call = Call.createCall(current.getName(), current.getLine(), expression);
                        call.compileVariable = Re_Variable.Unsafes.getKeywordVariable(name);

                        ArrayLists<Base> list = new ArrayLists<>(Base.EMPTY_BASE);
                        list.add(call);
                        onExpression.tempInnerBaseList = list;
                        editor.list = list;

                        editor.seek(1);
                    }
                });
                //throw() ... 不处理
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                    }
                });

                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(name) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        Object result = executor.getExpressionLastValue(call, 0, call.getParamExpressionCount());
                        if (executor.isReturnOrThrow()) return null;

                        if (Re_Utilities.isReClassInstance_exception(result)) {
                            Re_PrimitiveClass_exception.Instance instance = (Re_PrimitiveClass_exception.Instance) result;
                            executor.setThrow(instance);
                        } else {
                            String reason = Re_Utilities.toJString(result);
                            executor.setThrow(reason);
                        }
                        return null;
                    }
                }, keyword);
            }
        }
    }



    static class KeywordDebugger {
        static public void addExpressionConverterKeywordDebugger() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_VAR__DEBUGGER;
                final Var var0;
                var0 = new Var();
                var0.name = name;

                final Call call0;
                call0 = new Call();
                call0.name = name;

                final String help = "use " + var0;

                Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_VAR__DEBUGGER) {
                    @Override
                    public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                        // TODO: Implement this method
                        int paramExpressionCount = call.getParamExpressionCount();
                        if (paramExpressionCount != 0) {
                            executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                            return null;
                        }

                        Re host = executor.re;
                        _Re_TestDebuggerServer debuggerServer = host.open_debugger();
                        debuggerServer.debugger(executor.getStack());

                        return null;
                    }
                }, keyword);

                //break ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int offset = editor.index + 1;
                        int len = editor.list.size() - offset;
                        if (len != 0) {
                            throw new Re_Accidents.CompileTimeGrammaticalException(
                                    "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                            "help:[" + help + "]",
                                    editor.block.getFilePath(), current.getLine());
                        }

                        ConvertExpressionAsCallKeywordDebugger c = new ConvertExpressionAsCallKeywordDebugger();
                        c.setLine(current.getLine());
                        c.setName(current.getName());
                        c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);

                        editor.set(editor.index, c);

                        editor.seek(editor.index + 1);
                    }
                });
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
        }
    }




}
