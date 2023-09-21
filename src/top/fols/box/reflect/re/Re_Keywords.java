package top.fols.box.reflect.re;

import top.fols.atri.lang.Classz;
import top.fols.atri.lang.Finals;
import top.fols.box.lang.Classx;

import java.lang.reflect.*;

import static top.fols.box.reflect.re.Re_CodeLoader.*;
import static top.fols.box.reflect.re.Re_Variable.FALSE;
import static top.fols.box.reflect.re.Re_Variable.TRUE;

/**
 * 除了表达式{@link Re_CodeLoader_ExpressionConverts}和计算方法{@link Re_CodeLoader#getCodeAutomaticConversionOperator()}
 * 其他方法都应该检测每一个参数，保证每个参数被执行
 */
@SuppressWarnings({"DanglingJavadoc", "UnnecessaryBoxing", "BooleanConstructorCall", "rawtypes"})
public class Re_Keywords {
    Re_Keywords(){}

    // (true, false) --> false 空名字方法 全部执行 返回最后一个值（如果return 将会中断）
    public static final String INNER_FUNCTION__             = "";


    //-----------------------------------------------表达式-关键词
    public static final String INNER_EXPRESSION_VAR__DEBUGGER   = "debugger";

    public static final String INNER_EXPRESSION_VAR__VAR        = "var";
    public static final String INNER_EXPRESSION_VAR__BREAK      = "break";
    public static final String INNER_EXPRESSION_VAR__CONTINUE   = "continue";

    public static final String INNER_EXPRESSION_CALL__IMPORT    = "import";
    public static final String INNER_EXPRESSION_CALL__RETURN    = "return";
    public static final String INNER_EXPRESSION_CALL__THROW     = "throw";
    //-----------------------------------------------

    //-----------------------------------------------表达式-2
    public static final String INNER_EXPRESSION_CALL__CLASS             = "class";
    public static final String INNER_EXPRESSION_CALL__SET_INIT_FUNCTION = "init";
    public static final String INNER_EXPRESSION_CALL__FUNCTION          = "function";

    public static final String INNER_EXPRESSION_CALL__WHILE             = "while";
    public static final String INNER_EXPRESSION_CALL__FOR               = "for";
    public static final String INNER_EXPRESSION_CALL__FOREACH           = "foreach";

    public static final String INNER_EXPRESSION_CALL__TRY               = "try";
    public static final String INNER_EXPRESSION_CALL__CATCH             = "catch";
    public static final String INNER_EXPRESSION_CALL__FINALLY           = "finally";

    public static final String INNER_EXPRESSION_CALL__IF                = "if";
    public static final String INNER_EXPRESSION_CALL__ELSE              = "else";

    public static final String INNER_EXPRESSION_CALL__BASED             = "based";

    //-----------------------------------------------







    //-----------------------------------------------计算和自动转换
    /**
     * 内置自动符号转换计算方法  {@link Re_Math#getAutomaticConversionOperator()}
     */
    public static final String INNER_MATH_FUNCTION__AAND            = "&&";
    public static final String INNER_MATH_FUNCTION__AND             = "&";

    public static final String INNER_MATH_FUNCTION__OR              = "|";
    public static final String INNER_MATH_FUNCTION__OOR             = "||";

    public static final String INNER_MATH_FUNCTION__XOR             = "^";

    public static final String INNER_MATH_FUNCTION__SHIFT_RIGHT             = ">>";
    public static final String INNER_MATH_FUNCTION__SHIFT_LEFT              = "<<";
    public static final String INNER_MATH_FUNCTION__UNSIGNED_SHIFT_RIGHT    = ">>>";

    public static final String INNER_MATH_FUNCTION__ADD             = "+";
    public static final String INNER_MATH_FUNCTION__SUBTRACT        = "-";
    public static final String INNER_MATH_FUNCTION__MULTIPLY        = "*";
    public static final String INNER_MATH_FUNCTION__DIVICE          = "/";
    public static final String INNER_MATH_FUNCTION__MOD             = "%";

    public static final String INNER_MATH_FUNCTION__NE              = "!=";
    public static final String INNER_MATH_FUNCTION__EQ              = "==";

    public static final String INNER_MATH_FUNCTION__EQO             = "===";
    public static final String INNER_MATH_FUNCTION__NEQO            = "!==";

    public static final String INNER_MATH_FUNCTION__GREATER         = ">";
    public static final String INNER_MATH_FUNCTION__LESS            = "<";
    public static final String INNER_MATH_FUNCTION__LESS_EQ         = "<=";
    public static final String INNER_MATH_FUNCTION__EQ_LESS         = "=<";
    public static final String INNER_MATH_FUNCTION__EQ_GREATER      = "=>";
    public static final String INNER_MATH_FUNCTION__GREATER_EQ      = ">=";

    public static final String INNER_MATH_FUNCTION__COLON_SET_VALUE = ":";



    //----------------------
    public static final String INNER_FUNCTION__TILDE        = "~";      //不会自动转换
    public static final String INNER_FUNCTION__NOT          = "not";    //作用相当于!
    public static final String INNER_FUNCTION__NVL          = "nvl";
    //-----------------------------------------------




    //-----------------------------------------------内置类
    public static final String INNER_CLASS__OBJECT      = "object";
    public static final String INNER_CLASS__LIST        = "list";
    public static final String INNER_CLASS__JSON        = "json";
    public static final String INNER_CLASS__REFLECT     = "reflect";
    public static final String INNER_CLASS__EXCEPTION   = "exception";
    public static final String INNER_CLASS__THREAD      = "thread";
    //-----------------------------------------------



    public static final String INNER_VAR__THIS          = "this";
    public static final String INNER_VAR__STATIC        = "static";
    public static final String INNER_VAR__ENVIRONMENT   = "env";  //返回原始对象//
    public static final String INNER_VAR__SPACE         = "space";//返回原始对象//

    public static final String INNER_VAR__TRUE      = "true";
    public static final String INNER_VAR__FALSE     = "false";
    public static final String INNER_VAR__NULL      = "null";

    public static final String INNER_VAR__FUN_ARGUMENTS_ARGUMENTS           = "arguments";        //function 参数
    public static final String INNER_VAR__FUN_ARGUMENTS_ARGUMENTS_$         = "$";              //function 参数

    public static final String INNER_VAR__INHERIT_FUN_ARGUMENTS_ARGUMENTS   = "arguments_inherit";      //eval or inherit 参数
    public static final String INNER_VAR__INHERIT_FUN_ARGUMENTS_$           = "$inherit";               //eval or inherit 参数



    //变量操作
    public static final String INNER_FUNCTION__GETATTR   = "getattr";
    public static final String INNER_FUNCTION__SETATTR   = "setattr";
    public static final String INNER_FUNCTION__HASATTR   = "hasattr";
    public static final String INNER_FUNCTION__DELATTR   = "delattr";
    public static final String INNER_FUNCTION__LENATTR   = "lenattr";
    public static final String INNER_FUNCTION__KEYATTR   = "keyattr";


    public static final String INNER_FUNCTION__EVAL        = "eval";//编译执行字符串代码

    /**
     * 判断是否为 Re_IObject
     * 所有实现都以 Re_IObject 为基础
     */
    public static final String INNER_FUNCTION__IS_RE_OBJECT         = "is_re";
    public static final String INNER_FUNCTION__IS_JAVA_OBJECT       = "is_java";
    public static final String INNER_FUNCTION__IS_PRIMITIVE         = "is_primitive";//判断变量是否为primitive
    public static final String INNER_FUNCTION__IS_SPACE             = "is_space";    //判断是否为space(Executor)


    public static final String INNER_FUNCTION__IS_TRUE      = "is_true";
    public static final String INNER_FUNCTION__IS_FALSE     = "is_false";

    public static final String INNER_FUNCTION__ISSTR      = "is_str";
    public static final String INNER_FUNCTION__ISINT      = "is_int";
    public static final String INNER_FUNCTION__ISLONG     = "is_long";
    public static final String INNER_FUNCTION__ISCHAR     = "is_char";
    public static final String INNER_FUNCTION__ISFLOAT    = "is_float";
    public static final String INNER_FUNCTION__ISDOUBLE   = "is_double";
    public static final String INNER_FUNCTION__ISSHORT    = "is_short";
    public static final String INNER_FUNCTION__ISBYTE     = "is_byte";
    public static final String INNER_FUNCTION__ISBOOLEAN  = "is_boolean";

    public static final String INNER_FUNCTION__IS_BASE_DATA  = "is_base_data";

    public static final String INNER_FUNCTION__IS_CLASS             = "is_class";       //判断是否为re class
    public static final String INNER_FUNCTION__IS_FUNCTION          = "is_function";    //判断是否为re function


    //内置数据转换
    public static final String INNER_FUNCTION__STR      = "str";
    public static final String INNER_FUNCTION__INT      = "int";
    public static final String INNER_FUNCTION__LONG     = "long";
    public static final String INNER_FUNCTION__CHAR     = "char";
    public static final String INNER_FUNCTION__FLOAT    = "float";
    public static final String INNER_FUNCTION__DOUBLE   = "double";
    public static final String INNER_FUNCTION__SHORT    = "short";
    public static final String INNER_FUNCTION__BYTE     = "byte";
    public static final String INNER_FUNCTION__BOOLEAN  = "boolean";



    //返回原始对象//
    public static final String INNER_FUNCTION__IMPORT_CLASS         = "import_class";           //导入re 类
    //返回原始对象//
    public static final String INNER_FUNCTION__FIND_CLASS           = "find_class";             //寻找并加载类

    //返回原始对象//
    public static final String INNER_FUNCTION__GET_CLASS            = "get_class";      //如果是实例则获取类
    //返回原始对象//
    public static final String INNER_FUNCTION__GET_CLASS_LOADER     = "get_class_loader";      //如果是实例则获取类
    //返回原始对象//
    public static final String INNER_FUNCTION__GET_DECLARE_CLASS    = "get_declare_class";      //如果是实例则获取类

    public static final String INNER_FUNCTION__INSTANCEOF  = "instanceof";





    public static final String INNER_FUNCTION__ARRAY        = "array";
    public static final String INNER_FUNCTION__ARRAYOF      = "arrayof";
    public static final String INNER_FUNCTION__IS_ARRAY     = "is_array";
    public static final String INNER_FUNCTION__TO_ARRAY     = "to_array";

    public static final String INNER_CONVERT_FUNCTION__CREATE_OBJECT    = "_instance";          //{"key": tip}; 语法自动转换为方法 创建一个对象实例
    public static final String INNER_FUNCTION__IS_CLASS_INSTANCE        = "is_instance";      //判断是否为re class 实例
    public static final String INNER_FUNCTION__TO_CLASS_INSTANCE        = "to_instance";

    public static final String INNER_CONVERT_FUNCTION__CREATE_LIST      = "_list";              //["key"]; 语法自动转换为方法 创建一个列表
    public static final String INNER_FUNCTION__IS_LIST_INSTANCE         = "is_list";          //判断是否为re class 实例
    public static final String INNER_FUNCTION__TO_LIST_INSTANCE         = "to_list";







    public static final String INNER_FUNCTION__JIMPORT                  = "jimport";   //直接把对象作为了类对象操作    [创建实例/静态方法/静态字段]
    public static final String INNER_FUNCTION__JOPTIONAL                = "joptional";  //直接把对象作为Java对象操作   [方法/字段]

    public static final String INNER_FUNCTION__JNEW                     = "jnew";
    public static final String INNER_FUNCTION__JINVOKE                  = "jinvoke";
    public static final String INNER_FUNCTION__JSET                     = "jset";
    public static final String INNER_FUNCTION__JGET                     = "jget";

    public static final String INNER_FUNCTION__JASCLASS                 = "jasclass";
    public static final String INNER_FUNCTION__JFORNAME                 = "jforname";


    public static final String INNER_FUNCTION__JNEW_PROXY               = "jproxy"; //创建Java自定义实例(代理)
    @SuppressWarnings("unused")
    public static final String INNER_FUNCTION__JNEW_CLASS               = "jclass"; //创建Java自定义类 （不实现）






    public static final String INNER_FUNCTION__LIST_KEYWORD             = "list_keyword";
    public static final String INNER_FUNCTION__IS_KEYWORD_KEY           = "is_keyword_key";
    public static final String INNER_FUNCTION__IS_KEYWORD               = "is_keyword";
    public static final String INNER_FUNCTION__GET_KEYWORD_KEY          = "get_keyword_key";
    public static final String INNER_FUNCTION__IS_RUNTIME_KEYWORD_KEY   = "is_runtime_keyword_key";


    //返回原始对象
    public static final String INNER_FUNCTION__RANGE       = "range";// 只能在foreach中使用

    public static final String INNER_FUNCTION__PRINT       = "print";
    public static final String INNER_FUNCTION__PRINTLN     = "println";

    public static final String INNER_FUNCTION__TIME        = "time";






    //额外的数据类型
    public static final String INNER_JIMPORT__RE_CLASS          = "re_class";
    public static final String INNER_JIMPORT__RE_CLASS_LOADER   = "re_class_loader";
    public static final String INNER_JIMPORT__RE_CLASS_INSTANCE = "re_instance"; //class instace
    public static final String INNER_JIMPORT__RE_CLASS_FUNCTION = "re_function";
    public static final String INNER_JIMPORT__RE_ITERABLE       = "re_iterable";
    public static final String INNER_JIMPORT__RE_OBJECT         = "re_object";
    public static final String INNER_JIMPORT__RE_SPACE = "re_space";


    public static final String INNER_VAR__INT_MAX       = "int_max";
    public static final String INNER_VAR__INT_MIN       = "int_min";
    public static final String INNER_VAR__CHAR_MAX      = "char_max";
    public static final String INNER_VAR__CHAR_MIN      = "char_min";
    public static final String INNER_VAR__FLOAT_MAX     = "float_max";
    public static final String INNER_VAR__FLOAT_MIN     = "float_min";
    public static final String INNER_VAR__DOUBLE_MAX    = "double_max";
    public static final String INNER_VAR__DOUBLE_MIN    = "double_min";
    public static final String INNER_VAR__SHORT_MAX     = "short_max";
    public static final String INNER_VAR__SHORT_MIN     = "short_min";
    public static final String INNER_VAR__LONG_MAX      = "long_max";
    public static final String INNER_VAR__LONG_MIN      = "long_min";
    public static final String INNER_VAR__BOOLEAN_MAX   = "boolean_max";
    public static final String INNER_VAR__BOOLEAN_MIN   = "boolean_min";
    public static final String INNER_VAR__BYTE_MAX      = "byte_max";
    public static final String INNER_VAR__BYTE_MIN      = "byte_min";




    public static boolean is_runtime_keyword_key(Object key) {
        return keyword.isRuntimeKeyword(key);
    }
    public static Object  get_runtime_keyword(Re_Executor executor, Object key) {
        return keyword.findRuntimeKeyword(executor, key);
    }


    public static boolean is_keyword_key(Object key) {
        return keyword.isKeywordKey(key);
    }
    public static boolean is_keyword_value(Object value) {
        return keyword.isKeywordValue(value);
    }
    public static Object findKeywordKeyFromValue(Object value) {
        return keyword.getKeywordKey(value);
    }
    public static Object findKeywordValueFromKey(Object key) {
        return keyword.findKeyword(key);
    }
    public static String[] get_keyword_keys() {
        return keyword.getKeywordNames();
    }




    //导入最基础的 ***
    static final Re_VariableMapAsKeyword keyword = new Re_VariableMapAsKeyword();
    static {//() 空名称方法 这是个非常重要的东西
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                //最后一行不需要判断isReturn
                return executor.getExpressionLastValue(call, 0, call.getParamExpressionCount());
            }
        }, keyword);
    }
    static {//{} [] 的 映射创建
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_CONVERT_FUNCTION__CREATE_OBJECT) {
            final String text = "m = { \"key\": tip }; ";

            /**
             * 控制参数 代码由自己执行
             */
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                return Re_Executor.createReDict(executor, call);
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_CONVERT_FUNCTION__CREATE_LIST) {
            final String text = "m = [1, 2, 3]; ";

            /**
             * 控制参数 代码由自己执行
             */
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                return Re_Executor.createReList(executor, call);
            }
        }, keyword);
    }

    static {//符号方法 [+,-,*,/] 等等
        Re_Math._addToKeyword(keyword);
    }

    static {//primitive-reClass
        Re_Variable.Unsafes.addVariableInternOrThrowEx(Re_Variable.createCompileDynamicClass_exception(),    keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(Re_Variable.createCompileDynamicClass_json(),         keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(Re_Variable.createCompileDynamicClass_list(),         keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(Re_Variable.createCompileDynamicClass_object(),    keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(Re_Variable.createCompileDynamicClass_reflect(),      keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(Re_Variable.createCompileDynamicClass_thread(),       keyword);
    }


    static {//动/静 内置变量
        Re_Variable.Unsafes.addVariableInternOrThrowEx(INNER_VAR__NULL,         Re_Variable.createCompileNull(), keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(INNER_VAR__TRUE,         Re_Variable.createCompileBoolean(true), keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(INNER_VAR__FALSE,        Re_Variable.createCompileBoolean(false), keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(INNER_VAR__ENVIRONMENT,  Re_Variable.createCompileDynamicEnvironment(), keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(INNER_VAR__SPACE,                        Re_Variable.createCompileDynamicSpace(), keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(INNER_VAR__FUN_ARGUMENTS_ARGUMENTS,      Re_Variable.createCompileDynamicArguments(), keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(INNER_VAR__FUN_ARGUMENTS_ARGUMENTS_$,    Re_Variable.createCompileDynamicArguments(), keyword);

        Re_Variable.Unsafes.addVariableInternOrThrowEx(INNER_VAR__INHERIT_FUN_ARGUMENTS_ARGUMENTS,  Re_Variable.createCompileDynamicInheritExecutorArguments(), keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(INNER_VAR__INHERIT_FUN_ARGUMENTS_$,          Re_Variable.createCompileDynamicInheritExecutorArguments(), keyword);
    }


    static {//getattr, setattr...
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__GETATTR) {
            String text = "(object, key)";
            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 2) {
                    Object object = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object key = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.getattr(executor, object, key);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__SETATTR) {
            String text = "(object, key, value)";
            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 3) {
                    Object object = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object key = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;

                    Object value = executor.getExpressionValue(call, 2);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.setattr(executor, object, key, value)? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__HASATTR) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 2) {
                    Object object = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object key = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.hasattr(executor, object, key)? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__DELATTR) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 2) {
                    Object object = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object key = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.delattr(executor, object, key)? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__LENATTR) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object object = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.lenattr(executor, object);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__KEYATTR) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object object = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.keyattr(executor, object);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
    }













    static {//reClass 相关操作
        Re_Variable.Unsafes.addVariableInternOrThrowEx(INNER_VAR__THIS,     Re_Variable.createCompileDynamicThis(),        keyword);
        Re_Variable.Unsafes.addVariableInternOrThrowEx(INNER_VAR__STATIC,   Re_Variable.createCompileDynamicStatic(),      keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_CLASS_INSTANCE) {
            String text = "use "+ INNER_FUNCTION__IS_CLASS_INSTANCE +"()";
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isReClassInstance(temp)? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__TO_CLASS_INSTANCE) {
            String text = "use "+ INNER_FUNCTION__TO_CLASS_INSTANCE +"()";
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.toinstance(executor, temp);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_LIST_INSTANCE) {
            String text = "use "+ INNER_FUNCTION__IS_LIST_INSTANCE +"()";
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isReClassInstance_list(temp)? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__TO_LIST_INSTANCE) {
            final String text = "use "+ INNER_FUNCTION__TO_LIST_INSTANCE +"()";
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.tolist(executor, temp);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IMPORT_CLASS) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    String name = Re_Utilities.toJString(executor.getExpressionValue(call, 0));
                    if (executor.isReturnOrThrow()) return null;

                    Re_Class re_class = Re_Utilities.loadCurrentClassLoaderClass(executor, name);
                    if (executor.isReturnOrThrow()) return null;

                    String simpleName = re_class.getReClassSimpleName();
                    executor.localValue(simpleName, re_class);
                    return   re_class;
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__FIND_CLASS) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                if (call.getParamExpressionCount() == 1) {
                    String name = Re_Utilities.toJString(executor.getExpressionValue(call, 0));
                    if (executor.isReturnOrThrow()) return null;

                    Re_Class re_class = Re_Utilities.loadCurrentClassLoaderClass(executor, name);
                    if (executor.isReturnOrThrow()) return null;

                    return   re_class;
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), call.getParamExpressionCount()));
                return null;
            }

        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__GET_CLASS) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.getReClassFromIReGetClass(temp);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__GET_CLASS_LOADER) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Re_Class    reClass = Re_Utilities.getReClassFromIReGetClass(temp);
                    if (null == reClass) {
                        executor.setThrow("not a reClass or reClassInstance: " + Re_Utilities.objectAsName(temp));
                        return null;
                    }
                    return reClass.getReClassLoader();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__GET_DECLARE_CLASS) {
            String text = "use "+ INNER_FUNCTION__GET_DECLARE_CLASS +"()";
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.getDeclareReClassFromIReGetDeclaringClass(temp);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__INSTANCEOF) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws ClassNotFoundException {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 2) {
                    Object value = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object type = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;

                    if (Re_Utilities.isReClass(type)) {
                        Re_Class reClass = (Re_Class) type;
                        return Re_Class.isInstanceOf(value, reClass) ? TRUE.get(): FALSE.get();
                    } else {
                        Class<?>    javaType = Re_Utilities.objectAsJavaClass(type);
                        if (null != javaType) {
                            return Classz.isInstance(value, javaType, false) ? TRUE.get(): FALSE.get();
                        }

                        executor.setThrow("not a type: " + Re_Utilities.objectAsName(type));
                        return null;
                    }
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_CLASS) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isReClass(temp)? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_FUNCTION) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isReFunction(temp)? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
    };



    static {//基本数据转换方法 str(), int(), char(), float(), double)(, short(), boolean(), long(), byte()
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__STR,       new Re_PrimitiveObject_jimportType(String.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.toJString(obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__INT,       new Re_PrimitiveObject_jimportType(int.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.toJInt(obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__LONG,      new Re_PrimitiveObject_jimportType(long.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.toJLong(obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__CHAR,      new Re_PrimitiveObject_jimportType(char.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.toJChar(obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__FLOAT,     new Re_PrimitiveObject_jimportType(float.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.toJFloat(obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__DOUBLE,    new Re_PrimitiveObject_jimportType(double.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.toJDouble(obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__SHORT,     new Re_PrimitiveObject_jimportType(short.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.toJShort(obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__BYTE,      new Re_PrimitiveObject_jimportType(byte.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.toJByte(obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__BOOLEAN,   new Re_PrimitiveObject_jimportType(boolean.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.toJBoolean(obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
    }
    static {//基本数据类型判断 is_str(), is_int(), is_char(), is_float(), is_double)(, is_short(), is_boolean(), is_long(), is_byte()
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__ISSTR,       new Re_PrimitiveObject_jimportType(String.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isJString(obj) ? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__ISINT,       new Re_PrimitiveObject_jimportType(int.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isJInt(obj) ? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__ISLONG,      new Re_PrimitiveObject_jimportType(long.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isJLong(obj) ? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__ISCHAR,      new Re_PrimitiveObject_jimportType(char.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isJChar(obj) ? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__ISFLOAT,     new Re_PrimitiveObject_jimportType(float.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isJFloat(obj) ? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__ISDOUBLE,    new Re_PrimitiveObject_jimportType(double.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isJDouble(obj) ? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__ISSHORT,     new Re_PrimitiveObject_jimportType(short.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isJShort(obj) ? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__ISBYTE,      new Re_PrimitiveObject_jimportType(byte.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isJByte(obj) ? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_FUNCTION__ISBOOLEAN,   new Re_PrimitiveObject_jimportType(boolean.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isJBoolean(obj) ? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_BASE_DATA){
           @Override
           public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) throws Throwable {
               // TODO: Implement this method
               int paramExpressionCount = call.getParamExpressionCount();
               if (paramExpressionCount == 1) {
                   Object obj = executor.getExpressionValue(call, 0);
                   if (executor.isReturnOrThrow()) return null;

                   return Re_Utilities.isJBaseData(obj) ? TRUE.get(): FALSE.get();
               }
               executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
               return null;
           }
       }, keyword);
    }
    static {//基本数据最小值+最大值
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__INT_MAX,      new Integer(Integer.MAX_VALUE),     keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__INT_MIN,      new Integer(Integer.MIN_VALUE),     keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__CHAR_MAX,     new Character(Character.MAX_VALUE), keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__CHAR_MIN,     new Character(Character.MIN_VALUE), keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__FLOAT_MAX,    new Float(Float.MAX_VALUE),         keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__FLOAT_MIN,    new Float(Float.MIN_VALUE),         keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__DOUBLE_MAX,   new Double(Double.MAX_VALUE),       keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__DOUBLE_MIN,   new Double(Double.MIN_VALUE),       keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__SHORT_MAX,    new Short(Short.MAX_VALUE),         keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__SHORT_MIN,    new Short(Short.MIN_VALUE),         keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__LONG_MAX,     new Long(Long.MAX_VALUE),           keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__LONG_MIN,     new Long(Long.MIN_VALUE),           keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__BOOLEAN_MAX,  new Boolean(true),            keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__BOOLEAN_MIN,  new Boolean(false),           keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__BYTE_MAX,     new Byte(Byte.MAX_VALUE),           keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_VAR__BYTE_MIN,     new Byte(Byte.MIN_VALUE),           keyword);
    }






    static {//方法列表 1
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__TILDE) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object x0 = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Math.tilde(x0);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__NOT) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object expressionValue = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Math.subtract(expressionValue);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__NVL) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 2) {
                    Object checkValue = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object orValue = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;

                    return null == checkValue ? orValue : checkValue;
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__RANGE) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                // TODO: Implement this method
                int paramCount = call.getParamExpressionCount();
                if (paramCount == 1) {
                    Object end = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;
                    if (end instanceof Integer) {
                        return Re_IRe_Iterable.Utilities.wrapRange(executor, 0, (Integer) end);
                    }
                    executor.setThrow(Re_Accidents.unsupported_type("end", Re_Utilities.objectAsName(end), "int"));
                    return null;
                }
                if (paramCount == 2) {
                    Object start = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object end = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;

                    if (start instanceof Integer && end instanceof Integer) {
                        return Re_IRe_Iterable.Utilities.wrapRange(executor, (Integer) start, (Integer) end);
                    }
                    executor.setThrow(Re_Accidents.unsupported_type(
                            "start, end",
                            Re_Utilities.objectAsName(start) + "," + Re_Utilities.objectAsName(end),
                            "int, int")
                    );
                    return null;
                }
                if (paramCount == 3) {
                    Object start = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Object end = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;

                    Object step = executor.getExpressionValue(call, 2);
                    if (executor.isReturnOrThrow()) return null;

                    if (start instanceof Integer && end instanceof Integer && step instanceof Integer) {
                        return Re_IRe_Iterable.Utilities.wrapRange(executor, (Integer) start, (Integer) end, (Integer) step);
                    }
                    executor.setThrow(Re_Accidents.unsupported_type(
                            "start, end",
                            Re_Utilities.objectAsName(start) + "," + Re_Utilities.objectAsName(end),
                            "int, int")
                    );
                    return null;
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__EVAL) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount > 0) {
                    Object codeObj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    String code = Re_Utilities.toJString(codeObj);

                    if (paramExpressionCount == 1) {
                        Object[] argumentArr = Finals.EMPTY_OBJECT_ARRAY;

                        return executor.eval_(code, argumentArr);
                    } else if (paramExpressionCount == 2) {
                        Object[] argumentArr;
                        Object argumentArr0 = executor.getExpressionValue(call, 1);
                        if (executor.isReturnOrThrow()) return null;

                        if (null == argumentArr0) {
                            argumentArr = Finals.EMPTY_OBJECT_ARRAY;
                        } else {
                            argumentArr = Re_Utilities.toarray(executor, argumentArr0);
                            if (executor.isReturnOrThrow()) return null;
                        }

                        return executor.eval_(code, argumentArr);
                    }
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_TRUE) {

            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.ifTrue(temp) ? TRUE.get() : FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_FALSE) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.ifFalse(temp) ? TRUE.get() : FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_PRIMITIVE) {
            String text = "use "+ INNER_FUNCTION__IS_PRIMITIVE +"()";
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isRePrimitive(temp)? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_SPACE) {
            String text = "use "+ INNER_FUNCTION__IS_SPACE +"()";
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isSpace(temp)? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_RE_OBJECT) {
            String text = "use "+ INNER_FUNCTION__IS_RE_OBJECT +"()";
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isIReObject(temp)? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_JAVA_OBJECT) {
            String text = "use "+ INNER_FUNCTION__IS_JAVA_OBJECT +"()";
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object temp = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isJavaObject(temp) ? TRUE.get(): FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);


        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__PRINT) {
            /**
             * @see Re_IRe_Object#executeThis(Re_Executor, Call)
             * @see Re_IRe_Object#executePoint(Re_Executor, Object, Call)
             * <p>
             * 如果返回 true  则需要自己获取参数 执行器不会获取参数	所以传入参数 (Object[] callParam) 为null
             * 如果返回 false 执行器会自动执行获取参数并传入(Object[] callParam)
             * <p>
             * 千万要注意执行后判断是否已经return了，如果return应该立即返回return数据而不是继续执行
             * <p>
             * 只要用了{@link Re_Executor#getExpressionValue(Call, int)} 后都要手动检测是否return
             */@Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount != 0) {
                    for (int i = 0; i < paramExpressionCount; i++) {
                        Object value = executor.getExpressionValue(call, i);
                        if (executor.isReturnOrThrow()) return null;

                        String str = Re_Utilities.toJString(value);
                        executor.re.print(str);
                    }
                }
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__PRINTLN) {
            /**
             * @see Re_IRe_Object#executeThis(Re_Executor, Call)
             * @see Re_IRe_Object#executePoint(Re_Executor, Object, Call)
             * <p>
             * 如果返回 true  则需要自己获取参数 执行器不会获取参数	所以传入参数 (Object[] callParam) 为null
             * 如果返回 false 执行器会自动执行获取参数并传入(Object[] callParam)
             * <p>
             * 千万要注意执行后判断是否已经return了，如果return应该立即返回return数据而不是继续执行
             * <p>
             * 只要用了{@link Re_Executor#getExpressionValue(Call, int)} 后都要手动检测是否return
             */@Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 0) {
                    executor.re.println();
                } else {
                    for (int i = 0; i < paramExpressionCount; i++) {
                        Object value = executor.getExpressionValue(call, i);
                        if (executor.isReturnOrThrow()) return null;

                        String str = Re_Utilities.toJString(value);
                        executor.re.println(str);
                    }
                }
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__TIME) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 0) {
                    return System.currentTimeMillis();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
    }

    static {//list_keyword
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__LIST_KEYWORD) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                executor.getExpressionValues(call);
                if (executor.isReturnOrThrow()) return null;

                return get_keyword_keys();
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_KEYWORD_KEY) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Keywords.is_keyword_key(obj) ? TRUE.get() : FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_KEYWORD) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Keywords.is_keyword_value(obj) ? TRUE.get() : FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__GET_KEYWORD_KEY) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Keywords.findKeywordKeyFromValue(obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_RUNTIME_KEYWORD_KEY) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Keywords.is_runtime_keyword_key(obj) ? TRUE.get() : FALSE.get();
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
    }



    static {//jimport
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_JIMPORT__RE_CLASS,             new Re_PrimitiveObject_jimportType(Re_Class.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_JIMPORT__RE_CLASS_LOADER,      new Re_PrimitiveObject_jimportType(Re_NativeClassLoader.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_JIMPORT__RE_CLASS_INSTANCE,    new Re_PrimitiveObject_jimportType(Re_ClassInstance.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_JIMPORT__RE_CLASS_FUNCTION,    new Re_PrimitiveObject_jimportType(Re_ClassFunction.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_JIMPORT__RE_ITERABLE,          new Re_PrimitiveObject_jimportType(Re_IRe_Iterable.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_JIMPORT__RE_OBJECT,            new Re_PrimitiveObject_jimportType(Re_IRe_Object.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(INNER_JIMPORT__RE_SPACE,             new Re_PrimitiveObject_jimportType(Re_Executor.class) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
    }



    static {//java array方法
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__ARRAY) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                return Re_Utilities.newJArrayFromArrayCall(executor, call);
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__ARRAYOF) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                return Re_Utilities.newJArrayForLength(executor, call);
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__IS_ARRAY) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.isJArray(obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__TO_ARRAY) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object obj = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.toarray(executor, obj);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
    }
    static {//java反射,jget...
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__JNEW) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramCount = call.getParamExpressionCount();
                if (paramCount == 2) {
                    Object classValue = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object argsValue = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;
                    Object[] array = Re_Utilities.toarray(executor, argsValue);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.newJavaInstance(executor, Re_Utilities.objectAsJavaClass(classValue), array);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__JINVOKE) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramCount = call.getParamExpressionCount();
                if (paramCount == 3) {
                    Object variableValue = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object nameValue = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;

                    Object argsValue = executor.getExpressionValue(call,2);
                    if (executor.isReturnOrThrow()) return null;

                    Object[] array = Re_Utilities.toarray(executor, argsValue);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.invokeJavaMethod(executor, variableValue, Re_Utilities.toJString(nameValue), array);
                }
                if (paramCount == 4) {
                    Object classValue = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object variableValue = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;

                    Object nameValue = executor.getExpressionValue(call,2);
                    if (executor.isReturnOrThrow()) return null;

                    Object argsValue = executor.getExpressionValue(call,3);
                    if (executor.isReturnOrThrow()) return null;
                    Object[] array = Re_Utilities.toarray(executor, argsValue);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.invokeJavaMethod(executor, Re_Utilities.objectAsJavaClass(classValue), variableValue, null, Re_Utilities.toJString(nameValue), array);
                }
                if (paramCount == 5) {
                    Object classValue = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object variableValue = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;

                    Object returnClass = executor.getExpressionValue(call,2);
                    if (executor.isReturnOrThrow()) return null;

                    Object nameValue = executor.getExpressionValue(call,3);
                    if (executor.isReturnOrThrow()) return null;

                    Object argsValue = executor.getExpressionValue(call,4);
                    if (executor.isReturnOrThrow()) return null;
                    Object[] array = Re_Utilities.toarray(executor, argsValue);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.invokeJavaMethod(executor, Re_Utilities.objectAsJavaClass(classValue), variableValue, Re_Utilities.objectAsJavaClass(returnClass), Re_Utilities.toJString(nameValue), array);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__JSET) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramCount = call.getParamExpressionCount();
                if (paramCount == 3) {
                    Object variableValue = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object nameValue = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;

                    Object argsValue = executor.getExpressionValue(call,2);
                    if (executor.isReturnOrThrow()) return null;

                    Re_Utilities.setJavaValue(executor, variableValue.getClass(), variableValue, Re_Utilities.toJString(nameValue), argsValue);
                    return argsValue;
                }
                if (paramCount == 4) {
                    Object classValue = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object variableValue = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;

                    Object nameValue = executor.getExpressionValue(call,2);
                    if (executor.isReturnOrThrow()) return null;

                    Object argsValue = executor.getExpressionValue(call,3);
                    if (executor.isReturnOrThrow()) return null;

                    Re_Utilities.setJavaValue(executor, Re_Utilities.objectAsJavaClass(classValue), variableValue, Re_Utilities.toJString(nameValue), argsValue);
                    return argsValue;
                }
                if (paramCount == 5) {
                    Object classValue = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object variableValue = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;

                    Object returnClass = executor.getExpressionValue(call,2);
                    if (executor.isReturnOrThrow()) return null;

                    Object nameValue = executor.getExpressionValue(call,3);
                    if (executor.isReturnOrThrow()) return null;

                    Object argsValue = executor.getExpressionValue(call,4);
                    if (executor.isReturnOrThrow()) return null;

                    Re_Utilities.setJavaValue(executor, Re_Utilities.objectAsJavaClass(classValue), variableValue, Re_Utilities.objectAsJavaClass(returnClass), Re_Utilities.toJString(nameValue), argsValue);
                    return argsValue;
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramCount));
                return null;
            }
        }, keyword);
        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__JGET) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramCount = call.getParamExpressionCount();
                if (paramCount == 2) {
                    Object variableValue = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object nameValue = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.getJavaValue(executor, variableValue.getClass(), variableValue, Re_Utilities.toJString(nameValue));
                }
                if (paramCount == 3) {
                    Object classValue = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object variableValue = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;

                    Object nameValue = executor.getExpressionValue(call,2);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.getJavaValue(executor, Re_Utilities.objectAsJavaClass(classValue), variableValue, Re_Utilities.toJString(nameValue));
                }
                if (paramCount == 4) {
                    Object classValue = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object variableValue = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;

                    Object returnClass = executor.getExpressionValue(call,2);
                    if (executor.isReturnOrThrow()) return null;

                    Object nameValue = executor.getExpressionValue(call,3);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.getJavaValue(executor, Re_Utilities.objectAsJavaClass(classValue), variableValue, Re_Utilities.objectAsJavaClass(returnClass), Re_Utilities.toJString(nameValue));
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__JFORNAME) {
            final String text =   "use @Deprecated (java.lang.String:ClassName);\n"+
                                  "use (java.lang.ClassLoader:ClassLoader,  java.lang.String:ClassName);\n" +
                                  "use (java.lang.ClassLoader:ClassLoader,  boolean:init, java.lang.String:ClassName);\n";;

            /**
             */
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object classValue = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.jforNameFindClassOrAsClass(classValue);
                } else if (paramExpressionCount == 2) {
                    Object p1 = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object p2 = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;

                    if (p1 instanceof ClassLoader) {
                        String name = Re_Utilities.toJString(p2);
                        return Re_Utilities.jforNameFindClass(name, true, (ClassLoader) p1);
                    }
                } else if (paramExpressionCount == 3) {
                    Object p1 = executor.getExpressionValue(call,0);
                    if (executor.isReturnOrThrow()) return null;

                    Object p2 = executor.getExpressionValue(call,1);
                    if (executor.isReturnOrThrow()) return null;

                    Object p3 = executor.getExpressionValue(call,2);
                    if (executor.isReturnOrThrow()) return null;

                    if (p1 instanceof ClassLoader) {
                        String name  = Re_Utilities.toJString(p3);
                        boolean init = Re_Utilities.toJBoolean(p2);
                        return Re_Utilities.jforNameFindClass(name, init, (ClassLoader) p1);
                    }
                }
                executor.setThrow(text);
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__JNEW_PROXY) {
            final String text =
                    "([java.lang.Class: class, ...], re_object_instance); //the first parameter must be a list";
            @Override
            public Object executeThis(final Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 2) {
                    Object interfaceListObject = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;
                    Object[] interfaceListArray = Re_Utilities.toarray(executor, interfaceListObject);
                    if (executor.isReturnOrThrow()) return null;

                    Object reClassInstanceObject = executor.getExpressionValue(call, 1);
                    if (executor.isReturnOrThrow()) return null;
                    if (!Re_Utilities.isReClassInstance(reClassInstanceObject)) {
                        executor.setThrow(text);
                        return null;
                    }
                    final Re_ClassInstance classInstance = (Re_ClassInstance) reClassInstanceObject;

                    final Re_NativeStack cloneStack = executor.getStack().clone();
                    InvocationHandler invocationHandler = new InvocationHandler() {
                        @SuppressWarnings("UnnecessaryLocalVariable")
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            String name = method.getName();
                            Object result = Re_Class.Safes.executeInstanceOrClassFunctionOrThrowEx(executor.re, cloneStack,
                                    classInstance,
                                    name, args, null);
                            return result;
                        }
                    };

                    Class[] interfaceList = Re_Utilities.jforNameFindClassOrAsClass(interfaceListArray, 0, interfaceListArray.length);
                    Object proxy = null;
                    Throwable ex = null;
                    for (Class anInterface : interfaceList) {
                        try {
                            ClassLoader classLoader = anInterface.getClassLoader();
                            proxy = Proxy.newProxyInstance(
                                    classLoader,
                                    interfaceList,
                                    invocationHandler);
                            break;
                        } catch (Throwable e) {
                            ex = e;
                        }
                    }
                    if (null == proxy) {
                        executor.setThrow(null == ex? " new proxy error" : ex.getMessage());
                        return null;
                    }
                    return proxy;
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__JASCLASS) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object value = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    return Re_Utilities.jasclass(value);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__JIMPORT) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) throws Throwable {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object value = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    Class<?> aClass = Re_Utilities.jforNameFindClassOrAsClass(value);

                    if (null == aClass) return null;
                    Re_PrimitiveObject_jimport jimport = Re_Utilities.toReJImport(aClass);

                    String simpleName = Classx.findSimpleName(jimport.getJavaClass());
                    executor.localValue(simpleName, jimport);
                    return jimport;
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);

        Re_Variable.Unsafes.addBuiltinValueInternOrThrowEx(new Re_IRe_Object.IPrimitiveCall(INNER_FUNCTION__JOPTIONAL) {
            @Override
            public Object executeThis(Re_Executor executor, Call call) {
                int paramExpressionCount = call.getParamExpressionCount();
                if (paramExpressionCount == 1) {
                    Object value = executor.getExpressionValue(call, 0);
                    if (executor.isReturnOrThrow()) return null;

                    if (null == value) return null;
                    return Re_Utilities.toReJObject(value);
                }
                executor.setThrow(Re_Accidents.unable_to_process_parameters(getName(), paramExpressionCount));
                return null;
            }
        }, keyword);
    }










}
