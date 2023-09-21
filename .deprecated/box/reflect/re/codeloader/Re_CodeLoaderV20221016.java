package top.fols.box.reflect.re;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import top.fols.atri.assist.ArrList;
import top.fols.atri.assist.StringJoiner;
import top.fols.atri.io.CharsCodeReader;
import top.fols.atri.io.CharsWriters;
import top.fols.atri.io.file.Filex;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Strings;
import top.fols.atri.lang.Objects;
import top.fols.atri.util.MessageDigests;
import top.fols.box.io.LineEditor;
import top.fols.box.reflect.re.interfaces.Re_IReInnerVariableMap;
import top.fols.box.reflect.re.interfaces.Re_IReObject;
import top.fols.box.reflect.re.primitive.iterable.Re_IReIterable;
import top.fols.box.reflect.re.primitive.iterable.Re_ReIterables;
import top.fols.box.reflect.re.primitive.iterable.Re_AReIterator;
import top.fols.box.util.Escapes;

import static top.fols.box.reflect.re.Re_CodeLoader.CompileTimeCodeSourceReader.getBaseDataToDeclareString;

@SuppressWarnings({"UnnecessaryLocalVariable", "IfCanBeSwitch", "UnnecessaryContinue"})
public class Re_CodeLoader {


    /**
     * 获取所有字符串的 intern, 花里胡哨还不如直接用java的字符池快 花里胡哨
     * 经过测试发现如果用自己的缓存 跑起来会更慢，，，，我也不知道为什么
     */
    public static <T> T intern(T keywords) {
        //noinspection unchecked
        return keywords instanceof String? (T) ((String)keywords).intern(): keywords;
    }


    static public final String FILE_EXTENSION_SYMBOL        = intern(".");
    static public final String RE_FILE_EXTENSION_NAME       = intern("re");

    static public final char   PACKAGE_SEPARATOR            = intern('.');
    static public final String PACKAGE_SEPARATOR_STRING     = intern(String.valueOf(PACKAGE_SEPARATOR));

    static public final String SUB_CLASS_SEPARATOR          = intern("$");

    static final char[] SYSTEM_FILE_SEPARATOR               = intern(Filex.separator_all());





    static private final char[]     CODE_LINE_SEPARATOR                     = intern(new char[]{'\n'} );
    static public final char        CODE_LINE_SEPARATOR_CHAR                = intern(Objects.getChar(new String(CODE_LINE_SEPARATOR)));//换行

    static public final String      CODE_BLANK_SPACE_CHARS = " ";

    static public final String      CODE_STRING2_START 						= intern("\"\"\"");
    static final char[]             CODE_STRING2_START_CHARS                = intern(CODE_STRING2_START.toCharArray());

    static public final String      CODE_STRING_START 						= intern("\"");
    static public final char        CODE_STRING_START_CHAR                  = intern(Objects.getChar(CODE_STRING_START));

    static public final String      CODE_CHAR_START 						= intern("'");
    static public final char        CODE_CHAR_START_CHAR                    = intern(Objects.getChar(CODE_CHAR_START));

    static public final String 		CODE_LIST_END_SYMBOL                    = intern("]");
    static public final String 		CODE_LIST_JOIN_SYMBOL                   = intern("[");
    static public final String 		CODE_MAP_END_SYMBOL                     = intern("}");
    static public final String 		CODE_MAP_JOIN_SYMBOL                    = intern("{");
    static public final String 		CODE_CALL_PARAM_END_SYMBOL 			    = intern(")");//方法参数结束
    static public final String 		CODE_CALL_PARAM_SEPARATOR 			    = intern(",");//方法参数分隔符
    static public final String 		CODE_CALL_PARAM_JOIN_SYMBOL 		    = intern("(");//方法参数开始
    static public final String		CODE_VARIABLE_ASSIGNMENT_SYMBOL 	    = intern("=");//赋值
    static public final String		CODE_OBJECT_POINT_SYMBOL 			    = intern(".");//点语法
    static public final String 		CODE_SEMICOLON_SEPARATOR 			    = intern(";");//代码分隔符
    static public final String      CODE_NOTE_START 					    = intern("//");//注释






    /* -----运算符， 也是basemethod----- */
    /* -----用于代码加载器实现自动转换为方法， 比如 1+1转换为 +(1, 1)----- */
    @SuppressWarnings("rawtypes")
    static final Collection CODE_AUTOMATIC_CONVERSION_OPERATOR = Re_Math.getAutomaticConversionOperator();
    /**
     * @see Re_Math#addMethodToKeyword(Re_IReInnerVariableMap)
     */
    public static boolean isCodeAutomaticConversionOperatorSymbol(String keywords) {
        return CODE_AUTOMATIC_CONVERSION_OPERATOR.contains(keywords);
    }

    /**
     * @see #CODE_AUTOMATIC_CONVERSION_OPERATOR
     */
    public static char[][] getCodeAutomaticConversionOperator() {
        char[][] chars = new char[CODE_AUTOMATIC_CONVERSION_OPERATOR.size()][];
        int i = 0;
        for (Object key: CODE_AUTOMATIC_CONVERSION_OPERATOR) {
            chars[i] = Strings.cast(key).toCharArray();
            i++;
        }
        return chars;
    }





    @SuppressWarnings("resource")
    static final char[][] CODE_CHARS = new CharsCodeReader() {{
        this.addSeparatorsAndSort(
                CODE_BLANK_SPACE_CHARS.toCharArray(),             //

                CODE_NOTE_START.toCharArray(),                    // //
                CODE_SEMICOLON_SEPARATOR.toCharArray(),           // ;

                CODE_OBJECT_POINT_SYMBOL.toCharArray(),           // .

                CODE_VARIABLE_ASSIGNMENT_SYMBOL.toCharArray(),    // =


                CODE_CALL_PARAM_JOIN_SYMBOL.toCharArray(),        // (
                CODE_CALL_PARAM_SEPARATOR.toCharArray(),          // ,
                CODE_CALL_PARAM_END_SYMBOL.toCharArray(),         // )


                //_object
                CODE_MAP_JOIN_SYMBOL.toCharArray(),              // {
                CODE_MAP_END_SYMBOL.toCharArray(),               // }
                //_list
                CODE_LIST_JOIN_SYMBOL.toCharArray(),              // [
                CODE_LIST_END_SYMBOL.toCharArray(),               // ]


                CODE_STRING_START.toCharArray(),                 // "
                CODE_CHAR_START.toCharArray(),                   // '
                CODE_STRING2_START.toCharArray(),                // """


                CODE_LINE_SEPARATOR                        // \n
        );
        this.addSeparatorsAndSort(
            getCodeAutomaticConversionOperator()                     //+ - * / ...
        );
        //__sout("symbol: " + Arrays.deepToString(getSeparators()) + ", size=" + separate.length);
    }}.getSeparators();






    static public final char CONST_LONG_PREFIX      = intern('L');
    static public final char CONST_FLOAT_PREFIX     = intern('F');
    static public final char CONST_DOUBLE_PREFIX    = intern('D');

    static public final char CONST_SHORT_PREFIX     = intern('S');
    static public final char CONST_BYTE_PREFIX      = intern('B');
    static public final char CONST_CHAR_PREFIX      = intern('C');













    /**
     * x
     */
    public static boolean codeIsVar(Base object) {
        return object instanceof Var;
    }
    /**
     * =
     */
    public static boolean codeIsAssignment(Base object) {
        return object instanceof Assignment;
    }
    /**
     * ()
     */
    public static boolean codeIsCall(Base object) {
        return object instanceof Call;
    }
    /**
     * .
     */
    public static boolean codeIsPoint(Base object) {
        return object instanceof Point;
    }


    /**
     * {}
     */
    public static boolean codeIsCallCreateDict(Base call) {
        return call instanceof CallCreateDict;
    }

    /**
     * []
     */
    public static boolean codeIsCallCreateList(Base call) {
        return call instanceof CallCreateList;
    }





    /**
     * 运算符 + - * /
     * 运行前 {@link TempSymbol} 将会转换为 {@link CallSymbol}
     * {@link TempSymbol} 在运行时不会出现
     */
    public static boolean codeIsTempSymbol(Base object) {
        return object instanceof TempSymbol;
    }

    /**
     * symbol(1, 2)
     */
    public static boolean codeIsCallSymbol(Base call) {
        return call instanceof CallSymbol;
    }




    /**
     * @param object code
     * @return {@link #codeIsAssignment(Base)} ||
     *         {@link #codeIsTempSymbol(Base)}} ;
     */
    public static boolean isDiscon(Base object) {
        return  codeIsAssignment(object) ||
                codeIsTempSymbol(object);
    }

    /**
     *
     * @param object code
     * @return {@link #codeIsAssignment(Base)} ||
     *         {@link #codeIsTempSymbol(Base)}} ? null : object;
     */
    public static <C extends Base> C notDiscon(C object) {
        return  codeIsAssignment(object) ||
                codeIsTempSymbol(object) ? null :object;
    }















    /**
     * 运算符
     */
    public static boolean isSymbolDeclare(String name) {
        return isCodeAutomaticConversionOperatorSymbol(name);
    }
    /**
     * 是否是注释 [//]
     */
    public static boolean isNotesDeclare(String str) {
        return CODE_NOTE_START.equals(str);
    }
    /**
     * "
     */
    public static boolean isStringDeclare(String str) {
        return CODE_STRING_START.equals(str);
    }
    /**
     * '
     */
    public static boolean isCharDeclare(String str) {
        return CODE_CHAR_START.equals(str);
    }
    /**
     * ''
     */
    public static boolean isString2Declare(String str) {
        return CODE_STRING2_START.equals(str);
    }


    /**
     * ^([0-9]+)
     */
    public static boolean isDigitDeclareStartWith(String str) {
        char   ch;
        return(ch = str.charAt(0)) >= '0' && ch <= '9';
    }


    /**
     * line of var tracker
     */
    public static class LineTracker {
        int line = 1;

        public void setLine(int line) {
            this.line = line;
        }

        public int getLine() {
            return line;
        }

        public void nextLine() {
            this.line++;
        }
    }








    /**
     * 运行时 不会存在这个类型的數據， 但是所有类型都是以这个类为基础的
     */
    public static class Base {
        static public final Expression[]  EMPTY_EXPRESSION = {};
        static public final Base[]        EMPTY_BASE = {};

        static final int CLASS_HASH = Base.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Base base = (Base) o;
            return  Objects.equals(name, base.name);
        }




        @SuppressWarnings("rawtypes")
        Re_Variable staticVariable;//修改名称一定要更新 tip
        String      name;       //修改名称一定要更新 tip
        int         line;

        public String getName() {
            return this.name;
        }
        Base setName(String name) {
            this.name = name;
            return this;
        }
        public boolean equalsName(String name) {
            return this.name.equals(name);
        }


        public int getLine() {
            return this.line;
        }
        Base setLine(int line) {
            this.line = line;
            return this;
        }





        @Override
        public String toString() {
            ExpressionFormatWriter expressionFormatWriter = new ExpressionFormatWriter();
            expressionFormatWriter.write(this);
            return expressionFormatWriter.toString();
        }
    }




    /**
     * 运行时 会存在这个类型的code
     * 变量名
     */
    public static class Var extends Base implements ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = Var.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            String text;
            if (Re_CodeFile.isCompileVariable(staticVariable))
                text = Re_CodeFile.getCompileDeclareValue(staticVariable);
            else
                text = name;
            return writer.append(line, text);
        }
    }

    /**
     * 运行时 会存在这个类型的code
     * 等号
     */
    public static class Assignment extends Base implements ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = Assignment.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        public Assignment() {
            super.name = CODE_VARIABLE_ASSIGNMENT_SYMBOL;//=
        }

        public static Assignment createAssignment(int len) {
            Assignment assignment = new Assignment();
            assignment.setLine(len);
            return assignment;
        }

        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            return writer.append(line, name);
        }
    }

    /**
     * 运行时 会存在这个类型的code
     * 点
     */
    public static class Point extends Base implements ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = Point.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        public Point() {
            super.name = CODE_OBJECT_POINT_SYMBOL;
        }
        //.

        public static Point createPoint(int len) {
            Point Point = new Point();
            Point.setLine(len);
            return Point;
        }


        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            return writer.append(line, name);
        }
    }




    /**
     * 运算符 + - * / ...
     * 运行前 {@link TempSymbol} 将会转换为 {@link CallSymbol}
     * {@link TempSymbol} 在运行时不会出现
     */
    public static class TempSymbol extends Base implements ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = TempSymbol.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        public TempSymbol(String name) {
            super.name = name;
        }

        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            return writer.append(line, name);
        }
    }



    /**
     * 运行时 会存在这个类型的code
     * ()
     * 函数
     */
    @SuppressWarnings({"UnusedReturnValue"})
    public static class Call extends Base implements ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = Call.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        /**
         * 编译时可以使用
         */
        ArrList<Expression> tempInnerParamList = new ArrList<>(Expression.EMPTY_EXPRESSION);
        int tempInnerParamCount = 0;





        /**
         * 编译时可以使用
         */
        void addToParamInnerTempListLast(Expression newTop) {
            this.tempInnerParamList.add(newTop);
            this.tempInnerParamCount++;
        }

        /**
         * 编译时可以使用
         */
        void removeParamInnerTempListElement(Expression element) {
            for (int i = tempInnerParamCount - 1; i > 0; i--) {
                Base base = tempInnerParamList.get(i);
                if  (base.equals(element)) {
                    tempInnerParamList.remove(i);
                    tempInnerParamCount--;
                    return;
                }
            }
        }



//-------------------------------------------------------
        //将列表转换为数组， 如果在这之后列表还被添加删除 数组将不会有变动， 速度更快 也便于使用

        public int getParamExpressionCount() {
            return tempInnerParamCount;
        }


        public boolean isEmptyName() {
            return null == super.name || super.name.length() == 0;
        }



        Expression[] cache0;
        /**
         * 不要修改
         */
        Expression[] getBuildParamExpressionCaches() {
            if (null != cache0) {
                return  cache0;
            }
            cache0 = tempInnerParamList.toArray(Expression.EMPTY_EXPRESSION);
            tempInnerParamList = null;
            return cache0;
        }
        Expression getBuildParamExpressionCache(int index) {
            if (null != cache0) {
                return  cache0[index];
            }
            cache0 = tempInnerParamList.toArray(Expression.EMPTY_EXPRESSION);
            tempInnerParamList = null;
            return cache0[index];
        }


        /**
         * @return 可能会生成一个新的列表
         */
        List<Expression> getParamInnerTempListOrBuildParamExpressionCachesAsList() {
            return null == tempInnerParamList ?Arrays.asList(cache0) : tempInnerParamList;
        }



        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, name);
            textl = text.append(textl, CODE_CALL_PARAM_JOIN_SYMBOL);
            List<Expression> list = getParamInnerTempListOrBuildParamExpressionCachesAsList();
            for (int i = 0; i < list.size(); i++) {
                Expression expression = list.get(i);
                if (expression.isEmpty())
                    continue;
                int expressionLine = expression.findFirstValidLine();

                ExpressionFormatWriter expressionFormatWriter = new ExpressionFormatWriter();
                expression.writeFormatExpression(expressionFormatWriter);
                textl = text.append(expressionLine, expressionFormatWriter);
                if (i < list.size() - 1)
                    textl = text.append(textl, CODE_CALL_PARAM_SEPARATOR + ' ');
            }
            textl = text.append(textl, CODE_CALL_PARAM_END_SYMBOL);

            return writer.append(line, text);
        }







        static String demo1(String name, String... paramName) {
            Call call = new Call();
            call.setName(name);
            call.setLine(0);
            call.staticVariable = null;

            if (null != paramName && paramName.length != 0) {
                for (String s : paramName) {
                    Expression expression = new Expression();
                    expression.setName(null);
                    expression.setLine(0);
                    expression.staticVariable = null;

                    Var v = new Var();
                    v.setName(s);
                    v.setLine(0);
                    v.staticVariable = null;

                    expression.addToInnerTempListLast(v);

                    call.addToParamInnerTempListLast(expression);
                }
            }
            return call.toString();
        }
    }



    /**
     * 运行时 会存在这个类型的code
     * {}
     */
    public static class CallCreateDict  extends Call {
        static final int CLASS_HASH = CallCreateDict.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }




        // } line
        int lastLine = 0;

        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            String  st = CODE_MAP_JOIN_SYMBOL,
                    sp = CODE_SEMICOLON_SEPARATOR,
                    ed = CODE_MAP_END_SYMBOL;

            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, st);
            List<Expression> list = getParamInnerTempListOrBuildParamExpressionCachesAsList();
            for (Expression expression : list) {
                if (expression.isEmpty())
                    continue;
                int expressionLine = expression.findFirstValidLine();

                ExpressionFormatWriter expressionFormatWriter = new ExpressionFormatWriter();
                expression.writeFormatExpression(expressionFormatWriter);

                textl = text.append(expressionLine, expressionFormatWriter);
                textl = text.append(textl, sp + CODE_BLANK_SPACE_CHARS);
            }

            int last = lastLine;
            if (last <= 0)
                last = textl;
            textl = text.append(last, ed);


            String s;
            if (text.isSet() && (text.getOffsetLine() == text.getLastLine())) {
                s = text.toString();
            } else {
                s = text.toString();
                if (s.startsWith(st) && s.endsWith(ed))
                    s = st + Strings.tabs(s.substring(st.length(), s.length() - ed.length())) + ed;
                else
                    s = Strings.tabs(s);
            }
            return writer.append(line, s);
        }
    }

    /**
     * 运行时 会存在这个类型的code
     * []
     */
    public static class CallCreateList  extends Call {
        static final int CLASS_HASH = CallCreateList.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }



        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, CODE_LIST_JOIN_SYMBOL);
            List<Expression> list = getParamInnerTempListOrBuildParamExpressionCachesAsList();
            for (int i = 0; i < list.size(); i++) {
                Expression expression = list.get(i);
                if (expression.isEmpty())
                    continue;
                int expressionLine = expression.findFirstValidLine();

                ExpressionFormatWriter expressionFormatWriter = new ExpressionFormatWriter();
                expression.writeFormatExpression(expressionFormatWriter);
                textl = text.append(expressionLine, expressionFormatWriter);
                if (i < list.size() - 1)
                    textl = text.append(textl, CODE_CALL_PARAM_SEPARATOR + CODE_BLANK_SPACE_CHARS);
            }
            textl = text.append(textl, CODE_LIST_END_SYMBOL);

            return writer.append(line, text);
        }
    }



    /**
     * 运算符 方法
     */
    public static class CallSymbol      extends Call {
        static final int CLASS_HASH = CallSymbol.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            List<Expression> list = getParamInnerTempListOrBuildParamExpressionCachesAsList();
            int size1 = list.size();
            if (size1 <= 1)
                throw new Re_Accidents.CompileTimeGrammaticalException("error symbol param");

            for (int i = 0; i < list.size(); i++) {
                Expression expression = list.get(i);
                if (expression.isEmpty())
                    continue;
                int expressionLine = expression.findFirstValidLine();

                ExpressionFormatWriter expressionFormatWriter = new ExpressionFormatWriter();
                expression.writeFormatExpression(expressionFormatWriter);

                textl = text.append(expressionLine, expressionFormatWriter);
                if (i < list.size() - 1)
                    textl = text.append(textl, super.getName());
            }

            return writer.append(line, text);
        }
    }


    /**
     * 执行的时候只会执行 里面的列表 本身表达式不是用来执行的
     *
     * (Expression, Expression, ...)
     * </br>
     * Expression1; Expression2;
     */
    @SuppressWarnings({"FieldMayBeFinal", "UnusedReturnValue"})
    public static class Expression extends Base implements ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = Expression.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        /**
         * 编译时可以使用
         */
        ArrList<Base> tempInnerBaseList = new ArrList<>(Expression.EMPTY_BASE);


        /**
         * 编译时可以使用
         */
        void addToInnerTempListLast(Base newTop) {
            this.tempInnerBaseList.add(newTop);
        }

        /**
         * 编译时可以使用
         */
        Base getInnerTempListFirst() {
            return tempInnerBaseList.getOrNull(0);
        }

        /**
         * 编译时可以使用
         */
        Base getInnerTempListLast() {
            int size = tempInnerBaseList.size();
            return tempInnerBaseList.getOrNull(size - 1);
        }

        /**
         * 编译时可以使用
         */
        Base getInnerTempListElement(int index) {
            return tempInnerBaseList.getOrNull(index);
        }


        /**
         * 编译时可以使用
         */
        int getInnerTempListSize() {
            return tempInnerBaseList.size();
        }


        /**
         * 编译时可以使用
         */
        ArrList<Base> getInnerTempList() {
            return this.tempInnerBaseList;
        }

        /**
         * 编译时可以使用
         */
        void removeInnerTempListCode(Base element) {
            if (null == element) {
                return;
            }
            for (int i = getInnerTempListSize() - 1; i >= 0; i--) {
                Base base = tempInnerBaseList.get(i);
                if (element == base) {
                    tempInnerBaseList.remove(i);
                    return;
                }
            }
            throw new Re_Accidents.CompileTimeGrammaticalException("not fount: " + element);
        }



        /**
         * 编译时可以使用
         */
        @Override
        Expression setLine(int line) {
            super.line = line;
            return this;
        }
        /**
         * 编译时可以使用
         * 设置行为第一个有效行元素的行
         */
        void setLine() {
            int firstValidLine = findFirstValidLine();
            if (firstValidLine > 0)
                super.line = firstValidLine;
        }


//-------------------------------------------------------
        //将列表转换为数组， 如果在这之后列表还被添加删除 数组将不会有变动



        Base[] cache0;
        Base[] getBuildCodeCache() {
            if (null != cache0) {
                return  cache0;
            }
            cache0 = tempInnerBaseList.toArray(Expression.EMPTY_BASE);
            tempInnerBaseList = null;
            return cache0;
        }



//-------------------------------------------------------

        /**
         * @return 可能会生成一个新的列表
         */
        List<Base> getInnerListOrBuildCodeCacheAsList() {
            if (null == tempInnerBaseList) {
                return Arrays.asList(cache0);
            } else {
                return tempInnerBaseList;
            }
        }
        int findFirstValidLine() {
            if (null == tempInnerBaseList) {
                for (Base base : cache0) {
                    Base base_;
                    if (null != (base_ = base) && base_.line > 0) {
                        return base_.line;
                    }
                }
            } else {
                for (Base base : tempInnerBaseList) {
                    Base base_;
                    if (null != (base_ = base) && base_.line > 0) {
                        return base_.line;
                    }
                }
            }
            return 0;
        }
        public boolean isEmpty() {
            if (null == tempInnerBaseList) {
                return cache0.length == 0;
            } else {
                return tempInnerBaseList.size() == 0;
            }
        }




        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            String text = Expression.getExpressionAsString(this);
            return writer.append(findFirstValidLine(), text);
        }


        public static String getExpressionsAsString(Re_CodeFile file) {
            ExpressionFormatWriter expressionFormatWriter = new ExpressionFormatWriter();
            Expression[] expressions = file.getExpressions();
            for (int a = file.getExpressionsOffset(), e = a + file.getExpressionsLength();
                 a < e; a++) {
                Expression expression = expressions[a];
                if (expression.isEmpty())
                    continue;

                int l = expressionFormatWriter.write(expression);
                l = expressionFormatWriter.append(l, CODE_SEMICOLON_SEPARATOR);
            }
            return expressionFormatWriter.toString();
        }
        public static String getExpressionAsString(Expression expression) {
            return getExpressionAsString(expression.getInnerListOrBuildCodeCacheAsList());
        }
        public static String getExpressionAsString(List<Base> baseList) {
            return getExpressionAsString(baseList, 0, baseList.size());
        }
        public static String getExpressionAsString(List<Base> list, int startIndex, int len) {
            if (startIndex + len > list.size())
                throw new Re_Accidents.CompileTimeGrammaticalException("offset="+startIndex+", len="+len+", size="+list.size());

            ExpressionFormatWriter text = new ExpressionFormatWriter();
            Base prev = null;
            Base next;

            int l = text.getLastLine();

            for (int i = 0, size = Math.max(Math.min(list.size(), len), 0);
                 i < size && null != (next = list.get(i + startIndex));
                 i++) { //编译时获取到的数据可能是null

                if ((codeIsVar(prev) || codeIsCall(prev)) &&
                    (codeIsVar(next) || codeIsCall(next))) {
                    l = text.append(l, CODE_BLANK_SPACE_CHARS);
                }
                l = text.write(next);

                prev = next;
            }
            return text.toString();
        }
    }

    static class ExpressionFormatWriter {
        @SuppressWarnings({"UnnecessaryModifier", "SpellCheckingInspection"})
        static interface Writerable {
            public int writeFormatExpression(ExpressionFormatWriter writer);
        }

        LineEditor lineEditor = new LineEditor(new String(CODE_LINE_SEPARATOR));
        public boolean isEmpty() {
            return lineEditor.isEmpty();
        }

        public int getOffsetLine() {
            return lineEditor.getOffsetLine();
        }
        public int getLastLine() {
            return lineEditor.getLastLine();
        }

        public boolean isSet(){
            return lineEditor.isSet();
        }


        public int write(Base o) {
            if(!(o instanceof Writerable))
                throw new UnsupportedOperationException("type: " + (null==o?null:o.getClass()));
            return ((Writerable)o).writeFormatExpression(this);
        }
        public int append(int line, String str) {
            if (str.isEmpty())
                return lineEditor.append(line, "");
            return lineEditor.append(line, str);
        }
        public int append(int line, ExpressionFormatWriter str) {
            if (str.isEmpty())
                return lineEditor.append(line, "");
            return lineEditor.append(line, str.toString());
        }



        @Override
        public String toString() {
            return lineEditor.toString();
        }
    }



    //-----------------------------------------表达式转换器转换的Call类型的类
    //class A {}
    @SuppressWarnings("StatementWithEmptyBody")
    static class ConvertExpressionAsCallClass extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallClass.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        String              className;
        CallCreateDict executeExpressions;

        public String getClassName() {
            return className;
        }

        public CallCreateDict getClassExpression() {
            return executeExpressions;
        }





        public static String wrapTopClass(Re_Class reClass) {
            Re_CodeFile codeBlock = reClass.getCodeBlock();
            return Expression.getExpressionsAsString(codeBlock);
        }

        public static String wrapSubClass(Re_Class reClass) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl= text.getLastLine();
            textl = text.append(textl, Re_Keywords.INNER_EXPRESSION_CALL__CLASS);
            if (reClass.isAnonymous()) {
                //匿名 class {}
            } else {
                //class A {}
                textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
                textl = text.append(textl, reClass.getName());
            }
            Re_CodeFile codeBlock = reClass.getCodeBlock();
            String expressionsAsString = Expression.getExpressionsAsString(codeBlock);
            textl = text.append(textl, expressionsAsString);
            return  text.toString();
        }

        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            if (null == className) {
                //匿名 class {}
            } else {
                //class A {}
                textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
                textl = text.append(textl, className);
            }
            textl = text.write(executeExpressions);

            return writer.append(line, text);
        }
    }


    //function a(){}
    static class ConvertExpressionAsCallFunction extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallFunction.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        String              functionName;
        String[]            functionParam = Finals.EMPTY_STRING_ARRAY;
        CallCreateDict executeExpressions;

        public String getFunctionName() {
            return functionName;
        }

        public int      getParamCount() {
            return  functionParam.length;
        }
        public String   getParamName(int index) {
            return functionParam[index];
        }

        public CallCreateDict getFunctionExpression() {
            return executeExpressions;
        }


        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            if (null == functionName) {
                //匿名方法 function() {}
            } else {
                //方法 function a() {}
                textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
                textl = text.append(textl, functionName);
            }
            StringJoiner paramStr = new StringJoiner(CODE_CALL_PARAM_SEPARATOR + CODE_BLANK_SPACE_CHARS, CODE_CALL_PARAM_JOIN_SYMBOL, CODE_CALL_PARAM_END_SYMBOL);
            for (String s : functionParam) {
                paramStr.add(s);
            }
            textl = text.append(textl, paramStr.toString());
            textl = text.write(executeExpressions);

            return writer.append(line, text);
        }
    }

    //init(){}
    static class ConvertExpressionAsCallInit extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallInit.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        String[]            functionParam = Finals.EMPTY_STRING_ARRAY;
        CallCreateDict      executeExpressions;

        public int      getParamCount() {
            return  functionParam.length;
        }
        public String   getParamName(int index) {
            return functionParam[index];
        }

        public CallCreateDict getFunctionExpression() {
            return executeExpressions;
        }



        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            StringJoiner paramStr = new StringJoiner(CODE_CALL_PARAM_SEPARATOR + CODE_BLANK_SPACE_CHARS, CODE_CALL_PARAM_JOIN_SYMBOL, CODE_CALL_PARAM_END_SYMBOL);
            for (String s : functionParam) {
                paramStr.add(s);
            }
            textl = text.append(textl, paramStr.toString());
            textl = text.write(executeExpressions);

            return writer.append(line, text);
        }
    }


    //inherit name(){}
    static class ConvertExpressionAsCallInherit extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallInherit.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        String              functionName;

        CallCreateDict executeExpressions;


        public CallCreateDict getFunctionExpression() {
            return executeExpressions;
        }


        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            textl = text.append(textl, CODE_CALL_PARAM_JOIN_SYMBOL);
            textl = text.append(textl, CODE_CALL_PARAM_END_SYMBOL);

            textl = text.write(executeExpressions);

            return writer.append(line, text);
        }
    }




    //for(i=0; i<1; i=i+1){}
    static class ConvertExpressionAsCallFor extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallFor.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        Expression initExpression;
        Expression conditionalExpression;
        Expression afterExpression;

        CallCreateDict executeExpressions;


        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            textl = text.append(textl, CODE_CALL_PARAM_JOIN_SYMBOL);
            textl = text.write(initExpression);
            textl = text.append(textl, CODE_SEMICOLON_SEPARATOR + CODE_BLANK_SPACE_CHARS);
            textl = text.write(conditionalExpression);
            textl = text.append(textl, CODE_SEMICOLON_SEPARATOR + CODE_BLANK_SPACE_CHARS);
            textl = text.write(afterExpression);
            textl = text.append(textl, CODE_CALL_PARAM_END_SYMBOL);

            textl = text.write(executeExpressions);

            return writer.append(line, text);
        }
    }

    //while(i < 1){}
    static class ConvertExpressionAsCallWhile extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallWhile.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        Expression conditionalExpression;

        CallCreateDict executeExpressions;

        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            textl = text.append(textl, CODE_CALL_PARAM_JOIN_SYMBOL);
            textl = text.write(conditionalExpression);
            textl = text.append(textl, CODE_CALL_PARAM_END_SYMBOL);

            textl = text.write(executeExpressions);

            return writer.append(line, text);
        }
    }


    //foreach(k, v, []) {};
    static class ConvertExpressionAsCallForeach extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallForeach.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        String kName;
        String vName;
        Expression objectExpression;

        CallCreateDict executeExpressions;

        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            textl = text.append(textl, CODE_CALL_PARAM_JOIN_SYMBOL);
            textl = text.append(textl, kName);
            textl = text.append(textl, CODE_CALL_PARAM_SEPARATOR + CODE_BLANK_SPACE_CHARS);
            textl = text.append(textl, vName);
            textl = text.append(textl, CODE_CALL_PARAM_SEPARATOR + CODE_BLANK_SPACE_CHARS);
            textl = text.write(objectExpression);
            textl = text.append(textl, CODE_CALL_PARAM_END_SYMBOL);

            textl = text.write(executeExpressions);

            return writer.append(line, text);
        }
    }

    //try{}catch(e){}finally{}
    static class ConvertExpressionAsCallTry extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallTry.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        CallCreateDict executeExpressions;
        String              catchName;
        CallCreateDict catchExpressions;
        CallCreateDict finallyExpressions;



        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            textl = text.write(executeExpressions);

            if (null != catchExpressions) {
                textl = text.append(textl, Re_Keywords.INNER_EXPRESSION_CALL__CATCH);
                textl = text.append(textl, CODE_CALL_PARAM_JOIN_SYMBOL);
                textl = text.append(textl, catchName);
                textl = text.append(textl, CODE_CALL_PARAM_END_SYMBOL);
                textl = text.write(catchExpressions);
            }
            if (null != finallyExpressions) {
                textl = text.append(textl, Re_Keywords.INNER_EXPRESSION_CALL__FINALLY);
                textl = text.write(finallyExpressions);
            }
            return writer.append(line, text);
        }
    }

    //if(){}else{}
    static class ConvertExpressionAsCallIf extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallIf.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        Expression conditionalExpression;
        CallCreateDict executeExpressions;
        CallCreateDict elseExpressions;



        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            textl = text.append(textl, CODE_CALL_PARAM_JOIN_SYMBOL);
            textl = text.write(conditionalExpression);
            textl = text.append(textl, CODE_CALL_PARAM_END_SYMBOL);

            textl = text.write(executeExpressions);

            if (null != elseExpressions) {
                textl = text.append(textl, Re_Keywords.INNER_EXPRESSION_CALL__ELSE);
                textl = text.write(elseExpressions);
            }

            return writer.append(line, text);
        }
    }



    //import ... 后面只是将表达式转为字符串，...和后面的;之前的代码将失去意义
    public static class ConvertExpressionAsCallKeywordImport extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallKeywordImport.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        String JavaOrReClassName;
        public String getJavaOrReClassName() {
            return JavaOrReClassName;
        }

        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
            textl = text.append(textl, JavaOrReClassName);

            return writer.append(line, text);
        }
    }

    //continue ...
    public static class ConvertExpressionAsCallKeywordContinue extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallKeywordContinue.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());

            return writer.append(line, text);
        }
    }

    //break ...
    public static class ConvertExpressionAsCallKeywordBreak extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallKeywordBreak.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());

            return writer.append(line, text);
        }
    }


    //debugger ...
    public static class ConvertExpressionAsCallKeywordDebugger extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallKeywordDebugger.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        @Override
        public int writeFormatExpression(ExpressionFormatWriter writer) {
            ExpressionFormatWriter text = new ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());

            return writer.append(line, text);
        }
    }


    public static long version(String content) {
        try {
            MessageDigest md = MessageDigest.getInstance(MessageDigests.ALGORITHM_SHA1);
            md.update(content.getBytes());

            long result = 1;
            for (byte element : md.digest())
                result = 31 * result + element;

            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    /*
     * 读取string 的思路是 在load前 处理字符串 将所有字符串替换为 变量名，
     * 执行时将从 常量管理器中获取常量
     */
    public Re_CodeFile load(String expression,
                            String filePath, int lineOffset) throws Re_Accidents.CompileTimeGrammaticalException  {
        {
            Re_CodeFile block = new Re_CodeFile();
            block.filePath = filePath;
            block.lineOffset = lineOffset;

            {
                //行 跟踪器
                LineTracker lineTracker = new LineTracker();
                lineTracker.line = lineOffset;

                //字符 读取器
                CharsCodeReader reader = new CharsCodeReader();
                char[] chars = expression.toCharArray();
                reader.buffer(chars, chars.length);
                reader.setSeparators(CODE_CHARS);

                //代码读取器
                CompileTimeCodeSourceReader compileTimeCodeSourceReader = new CompileTimeCodeSourceReader(block, lineTracker, reader);
                Expression[] expressions  = compileTimeCodeSourceReader.readAllExpression();

                block.setExpressions(expressions);
            }

            //System.out.println(TabPrint.wrap(block.getExpressions()));

            return block;
        }
    }


    static boolean log = true;
    public static void __sout(Object content) {
        if (log)
            System.out.println(content);
    }











    @SuppressWarnings({"StatementWithEmptyBody", "resource"})
    public static class CompileTimeCodeSourceReader {
        Re_CodeFile block;
        LineTracker lineTracker;
        CharsCodeReader reader;
        public CompileTimeCodeSourceReader(Re_CodeFile block, LineTracker lineTracker, CharsCodeReader reader) {
            this.block = block;
            this.lineTracker = lineTracker;
            this.reader = reader;
        }




        static boolean isFlag(int ret) {
            return ret < 0;
        }



        static final int FLAG_EXIT_CALL = -1;
        static final int FLAG_EXIT_DICT = -2;
        static final int FLAG_EXIT_LIST = -3;

        static boolean isFlagExit(int ret) {
            return ret <= FLAG_EXIT_CALL && ret >= FLAG_EXIT_LIST;
        }

        static boolean isFlagExitCall(int ret) {
            return ret == FLAG_EXIT_CALL;
        }
        static boolean isFlagExitDict(int ret) {
            return ret == FLAG_EXIT_DICT;
        }
        static boolean isFlagExitList(int ret) {
            return ret == FLAG_EXIT_LIST;
        }


        static final int FLAG_SEMICOLON = -4;
        static final int FLAG_PARAM_SEPARATOR = -5;

        static boolean isFlagSemicolonOrParamSeparator(int ret) {
            return ret <= FLAG_SEMICOLON && ret >= FLAG_PARAM_SEPARATOR;
        }





        public Expression[] readAllExpression() throws Re_Accidents.CompileTimeGrammaticalException {
            ArrList<Expression> expressionList = new ArrList<>(Expression.EMPTY_EXPRESSION);
            while (reader.hasNext()) {
                Expression   expression;
                if (null == (expression = this.readExpression()))
                    continue;
                expressionList.add(expression);
            }
            return expressionList.toArray();
        }

        public Expression readExpression() throws Re_Accidents.CompileTimeGrammaticalException {
            Expression expression = new Expression();
            while (reader.hasNext()) {
                int s = this.readNextSection(expression);
                if (isFlag(s)) {
                    if (isFlagSemicolonOrParamSeparator(s)) {
                        // semicolon
                        break;
                    } else {
                        throw new Re_Accidents.CompileTimeGrammaticalException("[error-flag]",
                                block.getFilePath(), lineTracker.getLine());
                    }
                }
            }
            if (expression.getInnerTempListSize() == 0) {
                return null;
            } else {
                expression.setLine();
            }
            return expression;
        }



        /**
         * 读取一个连续的表达式子 遇到 ; symbol = 会断开
         * 可以返回flag 获取读取的数量， 读取的数量不一定是准确的 也可以是0
         */
        public int readNextSection(Expression toExpression) throws Re_Accidents.CompileTimeGrammaticalException {
            boolean hasSymbol = false;
            char[]  chars;
            String  var_name;
            int     flag = 0;
            for (;;) {
                int prev_index = reader.getIndex();
                if (null == (chars = reader.readNext())) {
                    break;
                } else {
                    if (Arrays.equals(chars, CODE_LINE_SEPARATOR)) {
                        lineTracker.nextLine();
                        continue;
                    }
                    if ((var_name = new String(chars).trim()).length() == 0) {
                        continue;
                    }
                    var_name = intern(var_name);
                }


                if (isStringDeclare(var_name)) {
                    Base c = loadString00(block, toExpression, lineTracker, reader, prev_index);
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                }
                if (isCharDeclare(var_name)) {
                    Base c = loadChar00(block, toExpression, lineTracker, reader, prev_index);
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                }
                if (isString2Declare(var_name)) {
                    Base c = loadString20(block, toExpression, lineTracker, reader, prev_index);
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                }


                if (isNotesDeclare(var_name)) {
                    //跳过
                    skipSingleLineNote0(block, toExpression, lineTracker, reader, prev_index);
                    continue;
                }
                if (isDigitDeclareStartWith(var_name)) {
                    Base c = loadNumbers0(block, toExpression, lineTracker, reader, prev_index);
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                }
                if (isSymbolDeclare(var_name)) {    //symbol
                    Base c = new TempSymbol(var_name).setLine(lineTracker.getLine());
                    toExpression.addToInnerTempListLast(c);
                    hasSymbol = true;
                    continue;
                }

                if (var_name.equals(CODE_VARIABLE_ASSIGNMENT_SYMBOL)) {//=
                    Assignment c = Assignment.createAssignment(lineTracker.getLine());
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                } else if (var_name.equals(CODE_OBJECT_POINT_SYMBOL)) {//.
                    Point c = Point.createPoint(lineTracker.getLine());
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                } else if (var_name.equals(CODE_CALL_PARAM_JOIN_SYMBOL)) {//(
                    Base lastContent = null == toExpression ? null : notDiscon(toExpression.getInnerTempListLast());//上个集合的最后一个

                    Call c = new Call();
                    c.setLine(lineTracker.getLine());
                    if (codeIsVar(lastContent)) {
                        c.name = lastContent.name;
                        toExpression.removeInnerTempListCode(lastContent); //***** 删除上一个var 并且现在的新元素将名称设置为方法名
                    } else {
                        if (null == lastContent || null == lastContent.name) {
                            //a = (b);
                            //(b);
                            c.name = "";
                        } else {
                            if (codeIsCall(lastContent)) {
                                //(true, false)()       > 第二个(
                                //field(true, false)()  > 第二个(
                                c.name = "";
                            } else {
                                throw new Re_Accidents.CompileTimeGrammaticalException("[ " + lastContent + "(...) ]" ,
                                        block.getFilePath(), lineTracker.getLine());
                            }
                        }
                    }
                    c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);//将关键字值直接加入代码 以后将不在获取keyword

                    Expression exp = new Expression();
                    try {
                        while (reader.hasNext()) {
                            int s;
                            if (isFlag(s = readNextSection(exp))) {
                                if (isFlagSemicolonOrParamSeparator(s)) {
                                    //pass
                                } else if (isFlagExitCall(s)) {
                                    break;
                                } else {
                                    throw new Re_Accidents.CompileTimeGrammaticalException("[ (...? ]",
                                            block.getFilePath(), lineTracker.getLine());
                                }
                            }
                            if (exp.getInnerTempListSize() != 0) {
                                exp.setLine();
                                c.addToParamInnerTempListLast(exp);

                                exp = new Expression();
                            }
                        }
                    } finally {
                        if (exp.getInnerTempListSize() != 0) {
                            exp.setLine();
                            c.addToParamInnerTempListLast(exp);
                        }
                    }
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                } else if (var_name.equals(CODE_CALL_PARAM_END_SYMBOL)) {//)
                    flag = FLAG_EXIT_CALL;
                    break;
                } else if (var_name.equals(CODE_MAP_JOIN_SYMBOL)) {//{
                    CallCreateDict c = new CallCreateDict();
                    c.name = Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_OBJECT;
                    c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);;//将关键字值直接加入代码 以后将不在获取keyword
                    c.setLine(lineTracker.getLine());

                    Expression exp = new Expression();
                    try {
                        while (reader.hasNext()) {
                            int s;
                            if (isFlag(s = readNextSection(exp))) {
                                if (isFlagSemicolonOrParamSeparator(s)) {
                                    //pass
                                } else if (isFlagExitDict(s)) {
                                    break;
                                } else {
                                    throw new Re_Accidents.CompileTimeGrammaticalException("[ {...? ]",
                                            block.getFilePath(), lineTracker.getLine());
                                }
                            }
                            if (exp.getInnerTempListSize() != 0) {
                                exp.setLine();
                                c.addToParamInnerTempListLast(exp);

                                exp = new Expression();
                            }
                        }
                    } finally {
                        if (exp.getInnerTempListSize() != 0) {
                            exp.setLine();
                            c.addToParamInnerTempListLast(exp);
                        }
                    }
                    c.lastLine = lineTracker.getLine();
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                } else if (var_name.equals(CODE_MAP_END_SYMBOL)) {//}
                    flag = FLAG_EXIT_DICT;
                    break;
                } else if (var_name.equals(CODE_LIST_JOIN_SYMBOL)) {//[
                    CallCreateList c = new CallCreateList();
                    c.name = Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_LIST; //_list();
                    c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);;//将关键字值直接加入代码 以后将不在获取keyword
                    c.setLine(lineTracker.getLine());

                    Expression exp = new Expression();
                    try {
                        while (reader.hasNext()) {
                            int s;
                            if (isFlag(s = readNextSection(exp))) {
                                if (isFlagSemicolonOrParamSeparator(s)) {
                                    //pass
                                } else if (isFlagExitList(s)) {
                                    break;
                                } else {
                                    throw new Re_Accidents.CompileTimeGrammaticalException("[ [...? ]",
                                            block.getFilePath(), lineTracker.getLine());
                                }
                            }
                            if (exp.getInnerTempListSize() != 0) {
                                exp.setLine();
                                c.addToParamInnerTempListLast(exp);

                                exp = new Expression();
                            }
                        }
                    } finally {
                        if (exp.getInnerTempListSize() != 0) {
                            exp.setLine();
                            c.addToParamInnerTempListLast(exp);
                        }
                    }

                    Base lastContent = null == toExpression ? null : notDiscon(toExpression.getInnerTempListLast());//上个集合的最后一个
                    if (codeIsVar(lastContent)) {
                        if (c.getParamExpressionCount() == 0) {
                            lastContent.name += (CODE_LIST_JOIN_SYMBOL + CODE_LIST_END_SYMBOL);
                            lastContent.staticVariable = Re_Variable.Unsafes.getKeywordVariable(lastContent.name);
                            continue;
                        }
                    }

                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                } else if (var_name.equals(CODE_LIST_END_SYMBOL)) {//]
                    flag = FLAG_EXIT_LIST;
                    break;
                } else if (var_name.equals(CODE_SEMICOLON_SEPARATOR) )    {//;
                    flag = FLAG_SEMICOLON;
                    break;
                } else if (var_name.equals(CODE_CALL_PARAM_SEPARATOR)) {//,
                    flag = FLAG_PARAM_SEPARATOR;
                    break;
                } else {
                    Base c = new Var();
                    c.name = var_name;
                    c.line = lineTracker.getLine();
                    c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(var_name);//将关键字值直接加入代码 以后将不在获取keyword
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                }
                // println(var_name + "<" + cline.line + ">");
            }


            CompileTimeCodeListEditor reader = this.getReusingEditor(block, toExpression);

            //Interceptors
            while (reader.hasNext()) {
                Base current;
                ExpressionConverter expressionConverter;
                if (null != (current = reader.next())) {
                    if (null != (expressionConverter = getExpressionConverter(current))) {
                        expressionConverter.convert(reader, current);
                    }
                }
            }
            reader.resetIndex();

            //Convert Expression Symbol As SymbolCall
            if (hasSymbol) {
                convertExpressionTempSymbolAsCallSymbol0(block, reader);
            }
            reader.resetIndex();

            reader.removeNull();
            return flag;
        }



        /**
         * 编译时运行
         * 转换符号，和过滤拦截器等
         * 一个读取器 只有一个实例
         * 不可同时进行编辑
         */
        protected CompileTimeCodeListEditor mReusingEditor = new CompileTimeCodeListEditor();
        protected CompileTimeCodeListEditor getReusingEditor(Re_CodeFile block, Expression expression) {
            mReusingEditor.block = block;
            mReusingEditor.list  = expression.getInnerTempList();
            return mReusingEditor;
        }







        /**
         * 只支持基础类型
         * 将Java的基本类型值转为声明字符串
         */
        public static Object parseBaseDataDeclare(String declare) {
            if (null == declare || Re_Keywords.INNER_VAR__NULL.equals(declare))
                return null;

            if (Objects.isBoolean(declare))
                return Objects.getBoolean(declare);

            if (declare.startsWith(Re_CodeLoader.CODE_STRING_START) && declare.endsWith(Re_CodeLoader.CODE_STRING_START) &&
                    declare.length() >= Re_CodeLoader.CODE_STRING_START.length() * 2)
                return Escapes.unescapeJava(declare.substring(Re_CodeLoader.CODE_STRING_START.length(), declare.length() - Re_CodeLoader.CODE_STRING_START.length()));
            if (declare.startsWith(Re_CodeLoader.CODE_STRING2_START) && declare.endsWith(Re_CodeLoader.CODE_STRING2_START) &&
                    declare.length() >= Re_CodeLoader.CODE_STRING2_START.length() * 2)
                return Escapes.unescapeJava(declare.substring(Re_CodeLoader.CODE_STRING2_START.length(), declare.length() - Re_CodeLoader.CODE_STRING2_START.length()));



            int count     = declare.length();
            int end       = 0;
            boolean haspoint = false;
            for (; end < count; end++) {
                char ch = declare.charAt(end);
                if (!(ch >= '0' && ch <= '9')) {
                    if (ch == '.') {
                        haspoint = true;
                    } else {
                        break;
                    }
                }
            }
            char type = end < count ? Character.toUpperCase(declare.charAt(end)) : '\0';

            if (type == CONST_LONG_PREFIX)          return Long.parseLong(declare);
            else if (type == CONST_FLOAT_PREFIX)    return Float.parseFloat(declare);
            else if (type == CONST_DOUBLE_PREFIX)   return Double.parseDouble(declare);

            else if (type == CONST_SHORT_PREFIX)    return Short.parseShort(declare);
            else if (type == CONST_BYTE_PREFIX)     return Byte.parseByte(declare);
            else if (type == CONST_CHAR_PREFIX)     return (char)Integer.parseInt(declare);
            else {
                //未知的下一个字符类型 直接跳过这个不跳过这个字符
                if (haspoint) {
                    return Double.parseDouble(declare); //double
                } else {
                    long javavalue = Long.parseLong(declare);
                    if ((int)javavalue == javavalue) {
                        return (int)javavalue;   //integer
                    } else {
                        return javavalue;        //long
                    }
                }
            }
        }

        /**
         * @see #parseBaseDataDeclare(String)
         * 只支持基础类型
         * 将声明字符串解析为Java基本类型
         */
        public static String getBaseDataToDeclareString(Object o) {
            if (null == o)
                return String.valueOf((Object) null);
            if (o instanceof Boolean)
                return o.toString();
            if (o instanceof String)
                return Re_CodeLoader.CODE_STRING_START + Escapes.escapeJava(o.toString()) + Re_CodeLoader.CODE_STRING_START;


            if (o instanceof Long)
                return o.toString();
            if (o instanceof Float)
                return BigDecimal.valueOf((Float)o) + String.valueOf(Re_CodeLoader.CONST_FLOAT_PREFIX);
            if (o instanceof Double)
                return BigDecimal.valueOf((Double)o).toString();

            if (o instanceof Short)
                return String.valueOf(o) + Re_CodeLoader.CONST_SHORT_PREFIX;
            if (o instanceof Byte)
                return String.valueOf(o) + Re_CodeLoader.CONST_BYTE_PREFIX;
            if (o instanceof CharSequence)
                return Re_CodeLoader.CODE_CHAR_START + Escapes.escapeJava(o.toString()) + Re_CodeLoader.CODE_CHAR_START;

            if (o instanceof Integer)
                return o.toString();

            throw new UnsupportedOperationException("type: " + Re_Utilities.getName(o));
        }






        static Var loadNumbers0(Re_CodeFile block, Expression expression, LineTracker lineTracker, CharsCodeReader lineCharsCodeReader, int str_prev_index) throws Re_Accidents.CompileTimeGrammaticalException {
            String prefix = "";
            Var result;
            int stLine       = lineTracker.getLine();
            int elementCount = expression.getInnerTempListSize();

            //获取前缀符号
            Base prev_element = expression.getInnerTempListElement(elementCount - 1); //倒数第一个
            if (codeIsTempSymbol(prev_element)) {
                if (prev_element.equalsName("+") || prev_element.equalsName("-")) {
                    prefix = prev_element.getName();
                }
                if (null == notDiscon(expression.getInnerTempListElement(elementCount - 2))) {//倒数第二个
                    if (prefix.isEmpty()) {
                        throw new Re_Accidents.CompileTimeGrammaticalException(Expression.getExpressionAsString(expression),
                                block.getFilePath(), lineTracker.getLine());
                    }
                    expression.removeInnerTempListCode(prev_element);//删除最后一个符号
                } else {
                    prefix = "";
                }
            }

            char[] buffer = lineCharsCodeReader.buffer();
            int count     = lineCharsCodeReader.size();
            int end       = str_prev_index + 1;
            boolean haspoint = false;
            for (; end < count; end++) {
                char ch = buffer[end];
                if (!(ch >= '0' && ch <= '9')) {
                    if (ch == '.') {
                        haspoint = true;
                    } else {
                        break;
                    }
                }
            }

            //(1, 2L, 4f, 65535c,5.5d, 6s, 7b, true, false, null, ‘3’, "")
            String content = prefix + new String(lineCharsCodeReader.buffer(), str_prev_index, end - str_prev_index);
            char type = end < count ? Character.toUpperCase(buffer[end]) : '\0';

            //__sout("prev_index=" + prev_index + ", " + "end=" + end);
            //__sout("content=" + content);
            //__sout("type=" + type);



            boolean isType = true;
            if (type == CONST_LONG_PREFIX)          result = block.createConstBase(expression, stLine, Long.parseLong(content));
            else if (type == CONST_FLOAT_PREFIX)    result = block.createConstBase(expression, stLine, Float.parseFloat(content));
            else if (type == CONST_DOUBLE_PREFIX)   result = block.createConstBase(expression, stLine, Double.parseDouble(content));

            else if (type == CONST_SHORT_PREFIX)    result = block.createConstBase(expression, stLine, Short.parseShort(content));
            else if (type == CONST_BYTE_PREFIX)     result = block.createConstBase(expression, stLine, Byte.parseByte(content));
            else if (type == CONST_CHAR_PREFIX)     result = block.createConstBase(expression, stLine, (char)Integer.parseInt(content));
            else {
                //未知的下一个字符类型 直接跳过这个不跳过这个字符
                if (haspoint) {
                    result = block.createConstBase(expression, stLine, Double.parseDouble(content)); //double
                } else {
                    long javavalue = Long.parseLong(content);
                    if ((int)javavalue == javavalue) {
                        result = block.createConstBase(expression, stLine, (int)javavalue);   //integer
                    } else {
                        result = block.createConstBase(expression, stLine, javavalue);        //long
                    }
                }
                isType = false;
            }
            int index = end + (isType ? 1 : 0);

            //__sout("seek: " + index);
            lineCharsCodeReader.seekIndex(index);

            return result;
        }

        @SuppressWarnings("resource")
        static Var loadString00(Re_CodeFile block, Expression expression, LineTracker lineTracker, CharsCodeReader lineCharsCodeReader, int str_prev_index) throws Re_Accidents.CompileTimeGrammaticalException {
            char symbol   = CODE_STRING_START_CHAR;
            char[] buffer = lineCharsCodeReader.buffer();
            int count     = lineCharsCodeReader.size();
            int stLine    = lineTracker.getLine();

            CharsWriters out = new CharsWriters(0);
            CharsWriters unicode = new CharsWriters(4);
            boolean hadSlash = false;
            boolean inUnicode = false;
            int end = str_prev_index + 1;
            for (; end < count; end++) {
                char ch = buffer[end];
                if  (ch == CODE_LINE_SEPARATOR_CHAR) {
                    lineTracker.nextLine();
                }
                if (inUnicode) {
                    // if in unicode, then we're reading unicode
                    // values in somehow
                    unicode.append(ch);
                    if (unicode.size() == 4) {
                        // unicode now contains the four hex digits
                        // which represents our unicode character
                        try {
                            int value = Integer.parseInt(unicode.toString(), 16);
                            out.write((char) value);
                            unicode.buffer_length(0);
                            inUnicode = false;
                            hadSlash = false;
                        } catch (NumberFormatException nfe) {
                            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS +"Unable to parse unicode tip: " + unicode +": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, end-str_prev_index) + String.valueOf(symbol))
                                    , block.getFilePath(), stLine, nfe);
                        }
                    }
                    continue;
                }
                if (hadSlash) {
                    // handle an escaped tip
                    hadSlash = false;
                    switch (ch) {
                        case '\\':
                            out.write('\\');
                            break;
                        case '\'':
                            out.write('\'');
                            break;
                        case '\"':
                            out.write('"');
                            break;
                        case 'r':
                            out.write('\r');
                            break;
                        case 'f':
                            out.write('\f');
                            break;
                        case 't':
                            out.write('\t');
                            break;
                        case 'n':
                            out.write('\n');
                            break;
                        case 'b':
                            out.write('\b');
                            break;
                        case 'u': {
                            // uh-oh, we're in unicode country....
                            inUnicode = true;
                            break;
                        }
                        default:
                            out.write(ch);
                            break;
                    }
                    continue;
                } else if (ch == '\\') {
                    hadSlash = true;
                    continue;
                }
                if (ch == symbol) {
                    // String element
                    String element = out.toString();
                    // //__sout("@" + element + "@" + joinStringLineCount + "@");
                    // //__sout("------");
                    end++;
                    lineCharsCodeReader.seekIndex(end);

                    Var aConst = block.createConstBase(expression, stLine, element);
                    return aConst;
                } else {
                    out.write(ch);
                }
            }
            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS +"error string declaration"+ ": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, end-str_prev_index) + String.valueOf(symbol)),
                    block.getFilePath(), stLine);
        }

        @SuppressWarnings("resource")
        static Var loadChar00(Re_CodeFile block, Expression expression, LineTracker lineTracker, CharsCodeReader lineCharsCodeReader, int str_prev_index) throws Re_Accidents.CompileTimeGrammaticalException {
            char symbol = CODE_CHAR_START_CHAR;
            char[] buffer = lineCharsCodeReader.buffer();
            int count     = lineCharsCodeReader.size();
            int stLine    = lineTracker.getLine();

            CharsWriters out = new CharsWriters(0);
            CharsWriters unicode = new CharsWriters(4);
            boolean hadSlash = false;
            boolean inUnicode = false;
            int end = str_prev_index + 1;
            for (; end < count; end++) {
                char ch = buffer[end];
                if (ch == CODE_LINE_SEPARATOR_CHAR) {
                    lineTracker.nextLine();
                }
                if (inUnicode) {
                    // if in unicode, then we're reading unicode
                    // values in somehow
                    unicode.append(ch);
                    if (unicode.size() == 4) {
                        // unicode now contains the four hex digits
                        // which represents our unicode character
                        try {
                            int value = Integer.parseInt(unicode.toString(), 16);
                            out.write((char) value);
                            unicode.buffer_length(0);
                            inUnicode = false;
                            hadSlash = false;
                        } catch (NumberFormatException nfe) {
                            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS + "Unable to parse unicode tip: [" + unicode +"] "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, end-str_prev_index) + String.valueOf(symbol)),
                                    block.getFilePath(), stLine, nfe);
                        }
                    }
                    continue;
                }
                if (hadSlash) {
                    // handle an escaped tip
                    hadSlash = false;
                    switch (ch) {
                        case '\\':
                            out.write('\\');
                            break;
                        case '\'':
                            out.write('\'');
                            break;
                        case '\"':
                            out.write('"');
                            break;
                        case 'r':
                            out.write('\r');
                            break;
                        case 'f':
                            out.write('\f');
                            break;
                        case 't':
                            out.write('\t');
                            break;
                        case 'n':
                            out.write('\n');
                            break;
                        case 'b':
                            out.write('\b');
                            break;
                        case 'u': {
                            // uh-oh, we're in unicode country....
                            inUnicode = true;
                            break;
                        }
                        default:
                            out.write(ch);
                            break;
                    }
                    continue;
                } else if (ch == '\\') {
                    hadSlash = true;
                    continue;
                }
                if (ch == symbol) {
                    // String contnet
                    String contnet = out.toString();
                    if (contnet.length() != 1) {
                        throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS +"error char declaration"+ ": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, end-str_prev_index) + String.valueOf(symbol)),
                                block.getFilePath(), stLine);
                    }
                    // //__sout("@" + contnet + "@" + joinStringLineCount + "@");
                    // //__sout("------");
                    end++;
                    lineCharsCodeReader.seekIndex(end);

                    char c1 = contnet.charAt(0);
                    Var aConst = block.createConstBase(expression, stLine, c1);
                    return aConst;
                } else {
                    out.write(ch);
                }
            }
            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS +"error string declaration"+ ": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, end-str_prev_index) + Strings.cast(symbol)),
                    block.getFilePath(), stLine);
        }

        // """
        @SuppressWarnings("resource")
        static Var loadString20(Re_CodeFile block, Expression expression, LineTracker lineTracker, CharsCodeReader lineCharsCodeReader, int str_prev_index) throws Re_Accidents.CompileTimeGrammaticalException {
            char[] symbol = CODE_STRING2_START_CHARS;
            char[] buffer = lineCharsCodeReader.buffer();
            int count     = lineCharsCodeReader.size();
            int stLine    = lineTracker.getLine();

            CharsWriters out = new CharsWriters(0);
            CharsWriters unicode = new CharsWriters(4);
            boolean hadSlash = false;
            boolean inUnicode = false;
            int end = str_prev_index + symbol.length;
            for (; end < count; end++) {
                char ch = buffer[end];
                if  (ch == CODE_LINE_SEPARATOR_CHAR) {
                    lineTracker.nextLine();
                }
                if (inUnicode) {
                    // if in unicode, then we're reading unicode
                    // values in somehow
                    unicode.append(ch);
                    if (unicode.size() == 4) {
                        // unicode now contains the four hex digits
                        // which represents our unicode character
                        try {
                            int value = Integer.parseInt(unicode.toString(), 16);
                            out.write((char) value);
                            unicode.buffer_length(0);
                            inUnicode = false;
                            hadSlash = false;
                        } catch (NumberFormatException nfe) {
                            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS + "Unable to parse unicode tip: [" + unicode +"] "+ new String(buffer, str_prev_index, end - str_prev_index),
                                    block.getFilePath(), stLine, nfe);
                        }
                    }
                    continue;
                }
                if (hadSlash) {
                    // handle an escaped tip
                    hadSlash = false;
                    switch (ch) {
                        case '\\':
                            out.write('\\');
                            break;
                        case '\'':
                            out.write('\'');
                            break;
                        case '\"':
                            out.write('"');
                            break;
                        case 'r':
                            out.write('\r');
                            break;
                        case 'f':
                            out.write('\f');
                            break;
                        case 't':
                            out.write('\t');
                            break;
                        case 'n':
                            out.write('\n');
                            break;
                        case 'b':
                            out.write('\b');
                            break;
                        case 'u': {
                            // uh-oh, we're in unicode country....
                            inUnicode = true;
                            break;
                        }
                        default:
                            out.write(ch);
                            break;
                    }
                    continue;
                } else if (ch == '\\') {
                    hadSlash = true;
                    continue;
                }
                if (ch == symbol[0] && (buffer[end+1] == symbol[1])  && (buffer[end+2] == symbol[2])) {
                    // String element
                    String element = out.toString();
                    // //__sout("@" + element + "@" + joinStringLineCount + "@");
                    // //__sout("------");
                    end += symbol.length;
                    lineCharsCodeReader.seekIndex(end);

                    Var aConst = block.createConstBase(expression, stLine, element);
                    return aConst;
                } else {
                    out.write(ch);
                }
            }
            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS + "unable to parse string2 tip: [" + out +"] "+ new String(buffer, str_prev_index, end - str_prev_index),
                    block.getFilePath(), stLine);
        }

        static void skipSingleLineNote0(Re_CodeFile block, Expression expression, LineTracker lineTracker, CharsCodeReader lineCharsCodeReader, int str_prev_index) throws Re_Accidents.CompileTimeGrammaticalException {
            char[] buffer = lineCharsCodeReader.buffer();
            int count = lineCharsCodeReader.size();
            int end = count;
            for (int i = str_prev_index; i < count; i++) {
                char c1 = buffer[i];
                if (c1 == CODE_LINE_SEPARATOR_CHAR) {
                    end = i;
                    break;
                }
            }
            lineCharsCodeReader.seekIndex(end);
        }

        //自动符号转方法
        static void convertExpressionTempSymbolAsCallSymbol0(Re_CodeFile block, CompileTimeCodeListEditor reader) throws Re_Accidents.CompileTimeGrammaticalException {
            //算数表达式转方法 1+1 >> +(1, 1)
            //println(reader.size());
            for (Base current; reader.hasNext();) {
                //将符号转为function  比如 6 +7 转换为 +(6,7)
                if (null != (current = reader.next())) {
                    if (codeIsTempSymbol(current)) {
                        convertExpressionTempSymbolAsCallSymbol1(block, reader, (TempSymbol) current);
                    }
                }
            }
            //__sout(Expression.formatCodeFromRoot(that, 0, that.size()));
        }

        /**
         *
         * @param block         代码块
         * @param reader        读取器
         * @param currentCode   元素指针必须等于 {@link  CompileTimeCodeListEditor#index()}
         * @throws Re_Accidents.CompileTimeGrammaticalException 表达式异常
         */
        static CallSymbol convertExpressionTempSymbolAsCallSymbol1(Re_CodeFile block, CompileTimeCodeListEditor reader, TempSymbol currentCode) throws Re_Accidents.CompileTimeGrammaticalException {
    //        __sout("*index=" + reader.index() + ", var=" + currentCode.getInstanceName() + ", type=" + currentCode.getClass());
            ArrList<Base> that = reader.list;

            int current = reader.index();
    //        __sout("*current: " + reader.now()); //
            int prev = reader.findSymbolPrevOverallStartIndex(current);
            int end  = reader.findSymbolNextOverallEndIndex(current);
    //        __sout("*find_prev_overall_offset: " + prev);
    //        __sout("*find_next_overall_offset: " + end);
            if (prev == -1 || end == -1) {
                int st = prev == -1 ? current : prev;
                int len = end == -1 ? current - prev : end - prev;

                throw new Re_Accidents.CompileTimeGrammaticalException("[" + Expression.getExpressionAsString(that, st, len + 1) + "]" + "["+((prev == -1 ? "no-prev-element":"") + CODE_BLANK_SPACE_CHARS + (end == -1 ? "no-next-element":"")).trim()+"]",
                        block.getFilePath(), currentCode.getLine());
            }
            String name = currentCode.getName();

            CallSymbol call = new CallSymbol();
            call.setName(name);
            call.setLine(currentCode.getLine());
            call.staticVariable = Re_Variable.Unsafes.getKeywordVariable(name);//将关键字值直接加入代码 以后将不在获取keyword

            Expression prev_expression = new Expression();
            prev_expression.setLine(currentCode.getLine());
            for (int i = prev; i < current; i++) {
                Base s = that.get(i);
                if (null != s) {
    //                __sout("prev: " + s.getInstanceName());
                    that.set(i, null);
                    prev_expression.addToInnerTempListLast(s);
                }
            }
            call.addToParamInnerTempListLast(prev_expression);

    //        __sout("symbol: " + currentCode.getInstanceName());
            that.set(current, null);

            Expression next_expression = new Expression();
            next_expression.setLine(currentCode.getLine());
            for (int i = current + 1; i <= end; i++) {
                Base s = that.get(i);
                if (null != s) {
    //                __sout("next: " + s.getInstanceName());
                    that.set(i, null);
                    next_expression.addToInnerTempListLast(s);
                }
            }
            call.addToParamInnerTempListLast(next_expression);

    //        __sout(TabPrint.wrap(that.toArray()));
            that.set(end, call);
    //        __sout(TabPrint.wrap(that.toArray()));
    //        __sout(Code.getExpressionAsString(block, that));
    //        __sout("----");
            return call;
        }

























































        @SuppressWarnings("UnnecessaryModifier")
        static protected interface ExpressionConverter {
            /**
             * 假如你需要删除元素可以直接setNull即可
             * 注意请不要创建新的表达式
             */
            public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException;
        }
        static protected void addExpressionConverter(Base filter, ExpressionConverter expressionConverter) {
            if (codeIsTempSymbol(filter) || codeIsCallSymbol(filter))
                throw new Re_Accidents.CompileTimeGrammaticalException("cannot add filter interceptor: " + filter);
            ExpressionConverter last;
            if (null == (last = expressionConverters.get(filter))) {
                expressionConverters.put(filter, expressionConverter);
            } else {
                throw new Re_Accidents.CompileTimeGrammaticalException("already filter interceptor: " + last);
            }
        }
        static protected ExpressionConverter getExpressionConverter(Base next) {
            return expressionConverters.get(next);
        }

        static protected final Map<Base, ExpressionConverter> expressionConverters = new HashMap<>();

        static {
            addExpressionConverterCallClass();
            addExpressionConverterCallInitFunction();
            addExpressionConverterCallFunction();
            addExpressionConverterCallFor();
            addExpressionConverterCallWhile();
            addExpressionConverterCallForeach();
            addExpressionConverterCallInherit();
            addExpressionConverterCallTry();
            addExpressionConverterCallIf();


            addExpressionConverterKeywordImport();
            addExpressionConverterKeywordContinue();
            addExpressionConverterKeywordBreak();

            addExpressionConverterKeywordDebugger();
        }
        static public void addExpressionConverterCallClass() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_CALL__CLASS;
                final Var var0;
                var0 = new Var();
                var0.name = name;

                final Call call0;
                call0 = new Call();
                call0.name = name;

                final String help = "use " +
                        var0 + new CallCreateDict()+
                        " or " +
                        var0 + " name " + new CallCreateDict();

                //cls a {} ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Var current      = (Var) now;

                        int  twoIndex    = editor.findNext();
                        Base two        = editor.getOrNull(twoIndex);

                        Re_CodeLoader.CallCreateDict dict;
                        String className;
                        if (codeIsVar(two)) {
                            className = two.getName();
                            int  threeIndex   = editor.findNext(twoIndex + 1);
                            Base three0       = editor.getOrNull(threeIndex);

                            if (codeIsCallCreateDict(three0)) {
                                dict = (CallCreateDict) three0;

                                ConvertExpressionAsCallClass c = new ConvertExpressionAsCallClass();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                                c.className  = className;
                                c.executeExpressions = dict;

                                editor.set(currentIndex,    null);
                                editor.set(twoIndex,       null);
                                editor.set(threeIndex,   c);

                                editor.seek(threeIndex + 1);
                                return;
                            }
                        } else if (codeIsCallCreateDict(two)) {
                            className = null;
                            dict = (Re_CodeLoader.CallCreateDict) two;

                            ConvertExpressionAsCallClass c = new ConvertExpressionAsCallClass();
                            c.setLine(current.getLine());
                            c.setName(current.getName());
                            c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                            c.className  = className;
                            c.executeExpressions = dict;

                            editor.set(currentIndex,    null);
                            editor.set(twoIndex,       c);

                            editor.seek(twoIndex + 1);
                            return;
                        }

                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[ " + Expression.getExpressionAsString(editor.list) + " ]" +
                                "help:[ " + help + " ]",
                                editor.block.getFilePath(), now.getLine());
                    }
                });
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[ " + Expression.getExpressionAsString(editor.list) + " ]" +
                                        "help:[ " + help + " ]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_CALL__CLASS) {
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallClass c = (ConvertExpressionAsCallClass) call;
                        Expression[] expressions = c.getClassExpression().getBuildParamExpressionCaches();
                        return Re_Class.RuntimeUtils.createReClassAndInitialize(executor, c.className, call.getLine(), expressions);
                    }
                }, Re_Keywords.keyword);
            }
        }


        static public void addExpressionConverterCallInitFunction() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_CALL__SET_INIT_FUNCTION;
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;

                final String help = "use " +
                        Call.demo1(name, "param") + new CallCreateDict();

                //init() {}
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Call current     = (Call) now;

                        int  twoIndex = editor.findNext();
                        Base two    = editor.getOrNull(twoIndex);

                        if (codeIsCallCreateDict(two)) {
                            Re_CodeLoader.CallCreateDict twoDict = (Re_CodeLoader.CallCreateDict) two;

                            ConvertExpressionAsCallInit c = new ConvertExpressionAsCallInit();
                            c.setLine(current.getLine());
                            c.setName(current.getName());
                            c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                            if (null != (c.functionParam = CompileTimeUtils.Expressions.getCallParamNameArray(current))) {
                                c.executeExpressions = twoDict;

                                editor.set(currentIndex, null);
                                editor.set(twoIndex , c);

                                editor.seek(twoIndex  + 1);
                                return;
                            }
                        }

                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), now.getLine());
                    }
                });
                //init ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_CALL__SET_INIT_FUNCTION) {
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallInit c = (ConvertExpressionAsCallInit) call;
                        Expression[] expressions = c.getFunctionExpression().getBuildParamExpressionCaches();
                        Re_ClassFunction function = Re_Class.RuntimeUtils.createReFunction(executor, null, c.functionParam, call.getLine(), expressions);
                        if (executor.isReturnOrThrow()) return null;

                        Re_Class reClass = executor.reClass;
                        if (null == reClass) {
                            executor.setThrow("executor no bind class");
                            return null;
                        }
                        if (null == function) {
                            executor.setThrow("create function fail");
                            return null;
                        }
                        if (reClass.isInitialized()) {
                            executor.setThrow("class initialized");
                            return null;
                        }
                        reClass.setInitFunction(function);
                        return null;
                    }
                }, Re_Keywords.keyword);
            }
        }


        static public void addExpressionConverterCallFunction() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION;
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;

                final String help = "use " +
                        var0 + CODE_BLANK_SPACE_CHARS + Call.demo1("", "param")     + new CallCreateDict()+
                        " or " +
                        var0 + CODE_BLANK_SPACE_CHARS + Call.demo1("name", "param") + new CallCreateDict();

                //function(){} 匿名
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int  currentIndex = editor.index();
                        Call current     = (Call) now;

                        int  twoIndex   = editor.findNext();
                        Base two        = editor.getOrNull(twoIndex);

                        if (codeIsCallCreateDict(two)) {
                            Re_CodeLoader.CallCreateDict twoDict = (Re_CodeLoader.CallCreateDict) two;

                            ConvertExpressionAsCallFunction c = new ConvertExpressionAsCallFunction();
                            c.setLine(current.getLine());
                            c.setName(current.getName());
                            c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                            c.functionName  = null;
                            if (null != (c.functionParam = CompileTimeUtils.Expressions.getCallParamNameArray(current))) {
                                c.executeExpressions = twoDict;

                                editor.set(currentIndex, null);
                                editor.set(twoIndex, c);

                                editor.seek(twoIndex + 1);
                                return;
                            }
                        }


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), now.getLine());
                    }
                });
                //function a() {}
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Var current      = (Var) now;

                        int nextIndex    = editor.findNext();
                        Base next        = editor.getOrNull(nextIndex);

                        if (codeIsCall(next)) {
                            Call nextCall = (Call) next;

                            int nextNextIndex   = editor.findNext(nextIndex + 1);
                            Base nextNext       = editor.getOrNull(nextNextIndex);

                            if (codeIsCallCreateDict(nextNext)) {
                                Re_CodeLoader.CallCreateDict nextNextDict = (Re_CodeLoader.CallCreateDict) nextNext;

                                ConvertExpressionAsCallFunction c = new ConvertExpressionAsCallFunction();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                                c.functionName  = nextCall.getName();
                                if (null != (c.functionParam = CompileTimeUtils.Expressions.getCallParamNameArray(nextCall))) {
                                    c.executeExpressions = nextNextDict;

                                    editor.set(currentIndex,    null);
                                    editor.set(nextIndex,       null);
                                    editor.set(nextNextIndex,   c);

                                    editor.seek(nextNextIndex + 1);
                                    return;
                                }
                            }
                        }


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), now.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION) {
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallFunction c = (ConvertExpressionAsCallFunction) call;
                        Expression[] expressions = c.getFunctionExpression().getBuildParamExpressionCaches();
                        return Re_Class.RuntimeUtils.createReFunction(executor, c.functionName, c.functionParam, call.getLine(), expressions);
                    }
                }, Re_Keywords.keyword);
            }
        }


        static public void addExpressionConverterCallFor() {

            {
                final String name = Re_Keywords.INNER_EXPRESSION_CALL__FOR;
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;

                final String help = "use " +
                        Call.demo1(name, "initExpression", "conditionalExpression", "afterExpression") + new CallCreateDict();

                //for() {}
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Call current = (Call) now;

                        if (current.getParamExpressionCount() == 3) {
                            int  twoIndex   = editor.findNext();
                            Base two        = editor.getOrNull(twoIndex);

                            if (codeIsCallCreateDict(two)) {
                                Re_CodeLoader.CallCreateDict twoDict = (Re_CodeLoader.CallCreateDict) two;

                                ConvertExpressionAsCallFor c = new ConvertExpressionAsCallFor();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);


                                c.initExpression = current.tempInnerParamList.get(0);
                                c.conditionalExpression = current.tempInnerParamList.get(1);
                                c.afterExpression = current.tempInnerParamList.get(2);

                                c.executeExpressions = twoDict;

                                editor.set(currentIndex, null);
                                editor.set(twoIndex, c);

                                editor.seek(twoIndex + 1);
                                return;
                            }
                        }


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), now.getLine());
                    }
                });
                //for ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_CALL__FOR) {
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallFor c = (ConvertExpressionAsCallFor) call;
                        Expression[] buildParamExpressionCaches = c.executeExpressions.getBuildParamExpressionCaches();

                        executor.getExpressionValue(c.initExpression);//init
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
                }, Re_Keywords.keyword);
            }
        }


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
                        Call.demo1(name, "condition") + new CallCreateDict();

                //while() {}
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Call current = (Call) now;

                        if (current.getParamExpressionCount() == 1) {
                            int  twoIndex   = editor.findNext();
                            Base two        = editor.getOrNull(twoIndex);

                            if (codeIsCallCreateDict(two)) {
                                Re_CodeLoader.CallCreateDict twoDict = (Re_CodeLoader.CallCreateDict) two;

                                ConvertExpressionAsCallWhile c = new ConvertExpressionAsCallWhile();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                                c.conditionalExpression = current.tempInnerParamList.get(0);

                                c.executeExpressions = twoDict;

                                editor.set(currentIndex, null);
                                editor.set(twoIndex, c);

                                editor.seek(twoIndex + 1);
                                return;
                            }
                        }


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), now.getLine());
                    }
                });
                //while ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_CALL__WHILE) {
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
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
                }, Re_Keywords.keyword);
            }
        }


        static public void addExpressionConverterCallForeach() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_CALL__FOREACH;
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;

                final String help = "use " +
                        Call.demo1(name, "k", "v", "objectExpression") + new CallCreateDict();

                //foreach() {}
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Call current = (Call) now;

                        if (current.getParamExpressionCount() == 3) {
                            int  twoIndex = editor.findNext();
                            Base two      = editor.getOrNull(twoIndex);

                            String kName = Re_CodeLoader.CompileTimeUtils.Expressions.getExpressionAsLocalName(current.tempInnerParamList.get(0));
                            String vName = Re_CodeLoader.CompileTimeUtils.Expressions.getExpressionAsLocalName(current.tempInnerParamList.get(1));
                            Expression objectExpression = current.tempInnerParamList.get(2);

                            if (null != kName && null != vName && codeIsCallCreateDict(two)) {
                                Re_CodeLoader.CallCreateDict twoDict = (Re_CodeLoader.CallCreateDict) two;

                                ConvertExpressionAsCallForeach c = new ConvertExpressionAsCallForeach();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);


                                c.kName = kName;
                                c.vName = vName;
                                c.objectExpression = objectExpression;

                                c.executeExpressions = twoDict;

                                editor.set(currentIndex, null);
                                editor.set(twoIndex, c);

                                editor.seek(twoIndex + 1);
                                return;
                            }
                        }


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), now.getLine());
                    }
                });
                //foreach
                addExpressionConverter(var0, new ExpressionConverter() {

                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_CALL__FOREACH) {
                    @SuppressWarnings("rawtypes")
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallForeach c = (ConvertExpressionAsCallForeach) call;

                        String k_var_name   = c.kName;
                        String v_var_name   = c.vName;

                        Object iterable = executor.getExpressionValue(c.objectExpression);
                        if (executor.isReturnOrThrow()) return null;

                        Re_IReIterable iterableWrap = Re_ReIterables.wrap(executor, iterable);
                        if (executor.isReturnOrThrow()) return null;

                        @SuppressWarnings("ConstantConditions")
                        Re_AReIterator iterator = iterableWrap.iterator();
                        Re_Variable key_variable   = iterator.key_variable();
                        Re_Variable value_variable = iterator.value_variable();

                        Re_Variable.accessPutNewVariable(executor, k_var_name, key_variable,   executor);
                        if (executor.isReturnOrThrow()) return null;

                        Re_Variable.accessPutNewVariable(executor, v_var_name, value_variable, executor);
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
                            Re_Variable.Unsafes.removeVariable(k_var_name, executor);
                            Re_Variable.Unsafes.removeVariable(v_var_name, executor);
                        }
                        return null;
                    }
                }, Re_Keywords.keyword);
            }
        }


        static public void addExpressionConverterCallInherit() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_CALL__INHERIT;
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;


                final String help = "use " +
                    var0 + CODE_BLANK_SPACE_CHARS + Call.demo1("")     + new CallCreateDict()+
                    " or " +
                    var0 + CODE_BLANK_SPACE_CHARS + Call.demo1("name") + new CallCreateDict();

                //inherit() {}
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Call current     = (Call) now;

                        int  twoIndex  = editor.findNext();
                        Base two       = editor.getOrNull(twoIndex);

                        if (codeIsCallCreateDict(two)) {
                            Re_CodeLoader.CallCreateDict twoDict = (Re_CodeLoader.CallCreateDict) two;

                            ConvertExpressionAsCallInherit c = new ConvertExpressionAsCallInherit();
                            c.setLine(current.getLine());
                            c.setName(current.getName());
                            c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                            String[]    callParamNameArray = CompileTimeUtils.Expressions.getCallParamNameArray(current);
                            if (null == callParamNameArray || callParamNameArray.length == 0) {
                                c.executeExpressions = twoDict;

                                editor.set(currentIndex, null);
                                editor.set(twoIndex, c);

                                editor.seek(twoIndex + 1);
                                return;
                            }
                        }


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), now.getLine());
                    }
                });
                //inherit a() {}
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method


                        int currentIndex = editor.index();
                        Var current      = (Var) now;

                        int nextIndex    = editor.findNext();
                        Base next        = editor.getOrNull(nextIndex);

                        if (codeIsCall(next)) {
                            Call nextCall = (Call) next;

                            int nextNextIndex   = editor.findNext(nextIndex + 1);
                            Base nextNext       = editor.getOrNull(nextNextIndex);

                            if (codeIsCallCreateDict(nextNext)) {
                                Re_CodeLoader.CallCreateDict nextNextDict = (Re_CodeLoader.CallCreateDict) nextNext;

                                ConvertExpressionAsCallInherit c = new ConvertExpressionAsCallInherit();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                                c.functionName  = nextCall.getName();
                                String[]    callParamNameArray = CompileTimeUtils.Expressions.getCallParamNameArray(nextCall);
                                if (null == callParamNameArray || callParamNameArray.length == 0) {
                                    c.executeExpressions = nextNextDict;

                                    editor.set(currentIndex,    null);
                                    editor.set(nextIndex,       null);
                                    editor.set(nextNextIndex,   c);

                                    editor.seek(nextNextIndex + 1);
                                    return;
                                }
                            }
                        }


                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), now.getLine());
                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_CALL__INHERIT) {
                    @Override
                    public Object executeThis(Re_Executor executor, String that_key, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallInherit c = (ConvertExpressionAsCallInherit) call;
                        Expression[] expressions = c.getFunctionExpression().getBuildParamExpressionCaches();

                        Re_ClassFunction function = Re_Class.RuntimeUtils.createReInheritFunction(executor, c.functionName, call.getLine(), expressions);
                        if (executor.isReturnOrThrow()) return null;

                        if (null == function) {
                            executor.setThrow("create function fail");
                            return null;
                        }
                        return function;
                    }
                }, Re_Keywords.keyword);
            }
        }


        static public void addExpressionConverterCallTry() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_CALL__TRY;
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;


                final String help = "use " +
                        Re_Keywords.INNER_EXPRESSION_CALL__TRY + new CallCreateDict() + Call.demo1(Re_Keywords.INNER_EXPRESSION_CALL__CATCH,"e") + new CallCreateDict() + Re_Keywords.INNER_EXPRESSION_CALL__FINALLY + new CallCreateDict();

                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
                //try{}catch(e){}finally{}
                addExpressionConverter(var0, new ExpressionConverter() {

                    public CallCreateDict getAsCallCreateDict(CompileTimeCodeListEditor editor, int index) {
                        Base two         = editor.getOrNull(index);
                        if (codeIsCallCreateDict(two)) {
                            return (CallCreateDict) two;
                        }
                        return null;
                    }
                    public Var getAsVar(CompileTimeCodeListEditor editor, int index) {
                        Base two         = editor.getOrNull(index);
                        if (codeIsVar(two)) {
                            return (Var) two;
                        }
                        return null;
                    }


                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        int currentIndex = editor.index();
                        Var current      = (Var) now; //try

                        CallCreateDict executeExpression = null;
                        String catchName                      = null;
                        CallCreateDict catchExpression   = null;
                        CallCreateDict finallyExpression = null;

                        int endIndex;

                        int  twoIndex               = editor.findNext();
                        CallCreateDict twoDict = getAsCallCreateDict(editor, twoIndex);//{}

                        T: if (null != twoDict) {
                            executeExpression = twoDict;
                                endIndex = twoIndex;

                            int  threeIndex = editor.findNext(twoIndex + 1);
                            Base three      = editor.getOrNull(threeIndex);

                            if (codeIsCall(three) && three.equalsName(Re_Keywords.INNER_EXPRESSION_CALL__CATCH)) {//catch(e)
                                Call threeCall = (Call) three;
                                if (threeCall.getParamExpressionCount() == 1) {
                                    if (null == (catchName = Re_CodeLoader.CompileTimeUtils.Expressions.getExpressionAsLocalName(threeCall.tempInnerParamList.get(0)))) {
                                        break T;
                                    };
                                        endIndex = threeIndex;
                                    int  fourIndex = editor.findNext(threeIndex + 1);
                                    CallCreateDict fourDict  = getAsCallCreateDict(editor, fourIndex);//{}
                                    if (null == fourDict) {
                                        break T;
                                    } else {
                                        catchExpression = fourDict;
                                            endIndex = fourIndex;

                                        int fiveIndex = editor.findNext(fourIndex + 1);
                                        Var asVar = getAsVar(editor, fiveIndex);
                                        if (null != asVar && asVar.equalsName(Re_Keywords.INNER_EXPRESSION_CALL__FINALLY)) {
                                            int  sixIndex = editor.findNext(fiveIndex + 1);
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
                                int  fourIndex = editor.findNext(threeIndex + 1);
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
                            c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                            c.executeExpressions = executeExpression;
                            c.catchName          = catchName;
                            c.catchExpressions   = catchExpression;
                            c.finallyExpressions = finallyExpression;

                            for (int i = currentIndex; i <= endIndex; i++) {
                                editor.set(i, null);
                            }
                            editor.set(endIndex, c);

                            editor.seek(endIndex + 1);
                            return;
                        }

                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                "help:[" + help + "]",
                                editor.block.getFilePath(), now.getLine());

                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_CALL__TRY) {
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallTry c = (ConvertExpressionAsCallTry) call;
                        Expression[] executeExpressions = c.executeExpressions.getBuildParamExpressionCaches();
                        String catchName                  = c.catchName;
                        Expression[] catchExpressions   = null == c.catchExpressions ? null : c.catchExpressions.getBuildParamExpressionCaches();
                        Expression[] finallyExpressions = null == c.finallyExpressions ? null : c.finallyExpressions.getBuildParamExpressionCaches();
                        return executor.trying(executeExpressions, catchName, catchExpressions, finallyExpressions);
                    }
                }, Re_Keywords.keyword);
            }
        }

        static public void addExpressionConverterCallIf() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_CALL__IF;
                final Call call0;
                call0 = new Call();
                call0.name = name;

                final Var var0;
                var0 = new Var();
                var0.name = name;


                final String help = "use " +
                        Call.demo1(name, "conditionExpression") + new CallCreateDict() + Re_Keywords.INNER_EXPRESSION_CALL__ELSE + new CallCreateDict();

                //if(){}else{}
                addExpressionConverter(call0, new ExpressionConverter() {

                    public CallCreateDict getAsCallCreateDict(CompileTimeCodeListEditor editor, int index) {
                        Base two         = editor.getOrNull(index);
                        if (codeIsCallCreateDict(two)) {
                            return (CallCreateDict) two;
                        }
                        return null;
                    }
                    public Var getAsVar(CompileTimeCodeListEditor editor, int index) {
                        Base two         = editor.getOrNull(index);
                        if (codeIsVar(two)) {
                            return (Var) two;
                        }
                        return null;
                    }


                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base now) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        int currentIndex = editor.index();
                        Call current     = (Call) now; //if()

                        if (current.getParamExpressionCount() == 1) {
                            //if(){}else {}
                            //if(){}else if(){}
                            Expression conditionalExpression = current.tempInnerParamList.get(0);
                            CallCreateDict executeExpressions;
                            CallCreateDict elseExpressions = null;

                            int endIndex;

                            int  twoIndex               = editor.findNext();
                            CallCreateDict twoDict = getAsCallCreateDict(editor, twoIndex);//{}

                            T: if (null != twoDict) {
                                executeExpressions = twoDict;
                                endIndex = twoIndex;

                                int  threeIndex = editor.findNext(twoIndex + 1);
                                Base three      = editor.getOrNull(threeIndex);

                                if (codeIsVar(three) && three.equalsName(Re_Keywords.INNER_EXPRESSION_CALL__ELSE)) {   //else
                                    int  fourIndex = editor.findNext(threeIndex + 1);
                                    CallCreateDict fourDict  = getAsCallCreateDict(editor, fourIndex);//{}
                                    if (null == fourDict) {
                                        break T;
                                    } else {
                                        elseExpressions = fourDict;
                                        endIndex = fourIndex;
                                    }
                                }

                                ConvertExpressionAsCallIf c = new ConvertExpressionAsCallIf();
                                c.setLine(current.getLine());
                                c.setName(current.getName());
                                c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(current.name);

                                c.conditionalExpression = conditionalExpression;
                                c.executeExpressions    = executeExpressions;
                                c.elseExpressions       = elseExpressions;

                                for (int i = currentIndex; i <= endIndex; i++) {
                                    editor.set(i, null);
                                }
                                editor.set(endIndex, c);

                                editor.seek(endIndex + 1);
                                return;
                            }
                        }

                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), now.getLine());
                    }
                });
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method

                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());

                    }
                });
            }
            {
                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_CALL__IF) {
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallIf c = (ConvertExpressionAsCallIf) call;
                        boolean b = Re_Utilities.ifTrue(executor.getExpressionValue(c.conditionalExpression));
                        if (executor.isReturnOrThrow()) return null;

                        if (b) {
                            Expression[] buildParamExpressionCaches = c.executeExpressions.getBuildParamExpressionCaches();
                            return executor.getExpressionLastValue(buildParamExpressionCaches, 0, buildParamExpressionCaches.length);
                        } else {
                            if (null != c.elseExpressions) {
                                Expression[] buildParamExpressionCaches = c.elseExpressions.getBuildParamExpressionCaches();
                                return executor.getExpressionLastValue(buildParamExpressionCaches, 0, buildParamExpressionCaches.length);
                            }
                            return null;
                        }
                    }
                }, Re_Keywords.keyword);
            }
        }










        static public void addExpressionConverterKeywordContinue() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_VAR__CONTINUE;
                final Var var0;
                var0 = new Var();
                var0.name = name;

                final Call call0;
                call0 = new Call();
                call0.name = name;

                final String help = "use " + var0;

                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_VAR__CONTINUE) {
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
                        // TODO: Implement this method
                        return Re_Utilities.setReturnContinue(executor);
                    }
                }, Re_Keywords.keyword);

                //continue ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
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
                        c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);

                        editor.set(editor.index, c);

                        editor.seek(editor.index + 1);
                    }
                });
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
        }
        static public void addExpressionConverterKeywordBreak() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_VAR__BREAK;
                final Var var0;
                var0 = new Var();
                var0.name = name;

                final Call call0;
                call0 = new Call();
                call0.name = name;

                final String help = "use " + var0;

                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_VAR__BREAK) {
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
                        // TODO: Implement this method
                        return Re_Utilities.setReturnBreak(executor);
                    }
                }, Re_Keywords.keyword);

                //break ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
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
                        c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);

                        editor.set(editor.index, c);

                        editor.seek(editor.index + 1);
                    }
                });
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
        }

        static public void addExpressionConverterKeywordImport() {
            {
                final String name = Re_Keywords.INNER_EXPRESSION_CALL__IMPORT;
                final Var var0;
                var0 = new Var();
                var0.name = name;

                final Call call0;
                call0 = new Call();
                call0.name = name;

                final String help = "use " + var0 + CODE_BLANK_SPACE_CHARS + "class-name";

                //关键字中已经有一个import()了不在加入这个import了
                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_CALL__IMPORT) {
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
                        // TODO: Implement this method
                        ConvertExpressionAsCallKeywordImport c = (ConvertExpressionAsCallKeywordImport) call;
                        String className = c.getJavaOrReClassName();
                        return Re_Utilities.loadReClassOrJavaClass(executor, className);
                    }
                }, Re_Keywords.keyword);

                //import ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
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
                        c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);

                        editor.set(editor.index, c);

                        editor.seek(editor.list.size());
                    }
                });
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
                        // TODO: Implement this method
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "expression:[" + Expression.getExpressionAsString(editor.list) + "], " +
                                        "help:[" + help + "]",
                                editor.block.getFilePath(), current.getLine());
                    }
                });
            }
        }




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

                Re_Variable.Unsafes.addBuiltinValueIntern(new Re_IReObject.IPrimitiveCall(Re_Keywords.INNER_EXPRESSION_VAR__DEBUGGER) {
                    @Override
                    public Object executeThis(Re_Executor executor, String var_name, Call call) throws Throwable {
                        // TODO: Implement this method
                        int paramExpressionCount = call.getParamExpressionCount();
                        if (paramExpressionCount != 0) {
                            executor.setThrow(Re_Accidents.unable_to_process_parameters(var_name, paramExpressionCount));
                            return null;
                        }

                        Re host = executor.getRe();
                        Re_ZDebuggerServer debuggerServer = host.open_debugger();
                        debuggerServer.debugger(executor.getStack());

                        return null;
                    }
                }, Re_Keywords.keyword);

                //break ...
                addExpressionConverter(var0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
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
                        c.staticVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);

                        editor.set(editor.index, c);

                        editor.seek(editor.index + 1);
                    }
                });
                addExpressionConverter(call0, new ExpressionConverter() {
                    @Override
                    public void convert(CompileTimeCodeListEditor editor, Base current) throws Re_Accidents.CompileTimeGrammaticalException {
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






















    public static class CompileTimeCodeListEditor {
        Re_CodeFile block;
        ArrList<Base> list;
        int index = -1;

        private CompileTimeCodeListEditor() {}


        /**
         * 获取上一段开始指针, 当前必须是一个整体
         */
        public int findSymbolPrevOverallStartIndex(int index) {
            Base current = list.get(index);
            //当前是符号 符号不能连接比如 - - -
            if (codeIsTempSymbol(current)) {
                for (int i = index - 1; i >= -1; i--) {
                    if (i == -1) {
                        return i + 1 == index ? -1 : i + 1;
                    }
                    Base c = list.get(i);
                    if (isDiscon(c)) {
                        return i + 1 == index ? -1 : i + 1;
                    }
                }
                return -1;
            }
            throw new Re_Accidents.CompileTimeGrammaticalException("unsupported current-type: " + Re_Utilities.getName(current));
        }
        /**
         * 获取下一段结束指针， 当前必须是一个整体
         */
        public int findSymbolNextOverallEndIndex(int index) {
            Base current = list.get(index);
            //当前是符号 符号不能连接比如 - - -
            if (codeIsTempSymbol(current)) {
                for (int i = index + 1; i <= size(); i++) {
                    if (i == size()) {
                        return i - 1 == index ? -1 : i - 1;
                    }
                    Base c = list.get(i);
                    if (isDiscon(c)) {
                        return i - 1 == index ? -1 : i - 1;
                    }
                }
                return -1;
            }
            throw new Re_Accidents.CompileTimeGrammaticalException("unsupported current-type: " + Re_Utilities.getName(current));
        }


        /**
         * 其实可能不需要Gc工作
         */
        public void removeNull() {
            int set = 0;
            ArrList<Base> that = this.list;
            for (Base base : that)
                if (null != base)
                    that.set(set++, base);
            that.setSize(set);
        }

        public void setNull(int index) {
            this.list.set(index, null);
        }
        public void set(int index, Base element) {
            this.list.set(index, element);
        }

        public Base get(int index) {
            return this.list.get(index);
        }
        public Base getOrNull(int index) {
            return this.list.getOrNull(index);
        }

        public boolean isStarted() {
            return this.index >= 0;
        }

        public void resetIndex() {
            this.index = -1;
        }
        public int seek(int index) {
            return this.index = index < 0 ? -1 : index;
        }
        public int index() {
            return this.index;
        }

        public int size() {
            return this.list.size();
        }

        public Base now() {
            return index >= 0 ? list.getOrNull(index): null;
        }
        public boolean isLast() {
            return null == this.getNext();
        }


        public int findNext() {
            return list.indexOfNonNull(this.index + 1);
        }
        public int findNext(int index) {
            return list.indexOfNonNull(index);
        }

        public boolean hasNext() {
            return list.size() > index + 1;
        }
        public Base getNext() {
            return list.getOrNull(index + 1);
        }
        public Base next() {
            return list.getOrNull(++index);
        }
    }











    public static class CompileTimeUtils {
        public static class Expressions {

            /**
             * @return 如果某个表达式只有变量名则返回变量名 否则返回null
             */
            public static String getExpressionAsLocalName(Expression expression) {
                ArrList<Base> param = expression.tempInnerBaseList;
                if (param.size() == 1) {
                    Base object = param.get(0);
                    if (codeIsVar(object) && null == object.staticVariable) {
                        return  object.getName();
                    }
                }
                return null;
            }

            /**
             * call必须符合 name(param);
             */
            public static String[] getCallParamNameArray(Call expr) {
                ArrList<Expression> expressionList = expr.tempInnerParamList;
                int count = expressionList.size();
                if (count == 0) {
                    return Finals.EMPTY_STRING_ARRAY;
                } else {
                    String[] paramNames = new String[count];
                    for (int i = 0; i < paramNames.length; i++) {
                        Expression expression = expressionList.get(i);
                        String expressionAsLocalName = getExpressionAsLocalName(expression);
                        if (null == expressionAsLocalName) {
                            return null;
                        } else {
                            paramNames[i] = expressionAsLocalName;
                        }
                    }
                    return paramNames;
                }
            }

        }
    }


}

