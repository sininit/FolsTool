package top.fols.box.reflect.re;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import top.fols.atri.assist.util.ArrayLists;
import top.fols.atri.assist.util.StringJoiner;
import top.fols.atri.io.CharsReaders;
import top.fols.atri.io.CharsWriters;
import top.fols.atri.io.Delimiter;
import top.fols.atri.io.file.Filex;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Strings;
import top.fols.atri.lang.Objects;
import top.fols.atri.util.MessageDigests;
import top.fols.atri.io.other.LineEditor;
import top.fols.box.lang.Escapes;
import top.fols.box.lang.Throwables;

@SuppressWarnings({"UnnecessaryLocalVariable", "IfCanBeSwitch", "UnnecessaryContinue"})
public class Re_CodeLoader {
    public static String version(byte[] content) {
        if (null == content) {
            return null;
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance(MessageDigests.ALGORITHM_SHA1);
                md.update(content);
                return MessageDigests.toHex(md.digest());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static String version(String content) {
        return null == content
                ? null
                : version(content.getBytes(StandardCharsets.UTF_8));
    }


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
    static private final String     CODE_LINE_SEPARATOR_STRING              = intern(new String(CODE_LINE_SEPARATOR) );
    static public final char        CODE_LINE_SEPARATOR_CHAR                = intern(Objects.get_char(new String(CODE_LINE_SEPARATOR)));//换行

    static public final String      CODE_BLANK_SPACE_CHARS = " ";

    static public final String      CODE_STRING2_START 						= intern("\"\"\"");
    static final char[]             CODE_STRING2_START_CHARS                = intern(CODE_STRING2_START.toCharArray());

    static public final String      CODE_STRING_START 						= intern("\"");
    static public final char        CODE_STRING_START_CHAR                  = intern(Objects.get_char(CODE_STRING_START));

    static public final String      CODE_CHAR_START 						= intern("'");
    static public final char        CODE_CHAR_START_CHAR                    = intern(Objects.get_char(CODE_CHAR_START));

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
    static final Collection<String> CODE_AUTOMATIC_CONVERSION_OPERATOR = Re_Math.getAutomaticConversionOperator();
    /**
     * @see Re_Math#_addToKeyword(Re_IRe_VariableMap)
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
        for (String key: CODE_AUTOMATIC_CONVERSION_OPERATOR) {
            chars[i] = key.toCharArray();
            i++;
        }
        return chars;
    }



    static final Delimiter.ICharsDelimiter codeDelimiter = new Delimiter.CharsMappingDelimiterBuilder(){{
        this.addAll(new char[][]{
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


            CODE_LINE_SEPARATOR                              // \n
        });
        this.addAll(
                getCodeAutomaticConversionOperator()                     //+ - * / ...
        );
    }}.build();





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
     * symbol(1, 2)
     */
    public static boolean codeIsCallSymbol(Base call) {
        return call instanceof CallSymbol;
    }





    /**
     * 运算符 + - * /
     * 运行前 {@link ConvertBeforeSymbol} 将会转换为 {@link CallSymbol}
     * {@link ConvertBeforeSymbol} 在运行时不会出现
     */
    public static boolean codeIsConvertBeforeSymbol(Base object) {
        return object instanceof ConvertBeforeSymbol;
    }

    /**
     * @param object code
     * @return {@link #codeIsAssignment(Base)} ||
     *         {@link #codeIsConvertBeforeSymbol(Base)}} ;
     */
    public static boolean isDiscon(Base object) {
        return  codeIsAssignment(object) ||
                codeIsConvertBeforeSymbol(object);
    }

    /**
     *
     * @param object code
     * @return {@link #codeIsAssignment(Base)} ||
     *         {@link #codeIsConvertBeforeSymbol(Base)}} ? null : object;
     */
    public static <C extends Base> C notDiscon(C object) {
        return  codeIsAssignment(object) ||
                codeIsConvertBeforeSymbol(object) ? null :object;
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
    public static class _LineTracker {
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
    static class _ExpressionFormatWriter {
        @SuppressWarnings({"UnnecessaryModifier", "SpellCheckingInspection", "UnnecessaryInterfaceModifier"})
        static interface Writerable {
            public int writeFormatExpression(_ExpressionFormatWriter writer);
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
        public int append(int line, _ExpressionFormatWriter str) {
            if (str.isEmpty())
                return lineEditor.append(line, "");
            return lineEditor.append(line, str.toString());
        }



        @Override
        public String toString() {
            return lineEditor.toString();
        }
    }





    /**
     * 运行时 不会存在这个类型的數據， 但是所有类型都是以这个类为基础的
     */
    public static class Base {
        static public final Expression[]  EMPTY_EXPRESSION = {};
        static public final Base[]        EMPTY_BASE = {};
        static public final Var[]         EMPTY_VAR  = {};

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
        Re_Variable compileVariable;//修改名称一定要更新 tip
        String      name;           //修改名称一定要更新 tip
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



        public String toCodeString(){
            _ExpressionFormatWriter expressionFormatWriter = new _ExpressionFormatWriter();
            expressionFormatWriter.write(this);
            return expressionFormatWriter.toString();
        }

        @Override
        public String toString() {
            return toCodeString();
        }
    }


    /**
     * 运算符 + - * / ...
     * 运行前 {@link ConvertBeforeSymbol} 将会转换为 {@link CallSymbol}
     * 在运行时不会出现
     */
    public static class ConvertBeforeSymbol extends Base implements _ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = ConvertBeforeSymbol.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        public ConvertBeforeSymbol(String name) {
            super.name = name;
        }

        @Override
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            return writer.append(line, name);
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
    public static class Expression extends Base implements _ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = Expression.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        /**
         * 编译时可以使用
         */
        ArrayLists<Base> tempInnerBaseList = new ArrayLists<>(Base.EMPTY_BASE);


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
            return tempInnerBaseList.opt(0);
        }

        /**
         * 编译时可以使用
         */
        Base getInnerTempListLast() {
            int size = tempInnerBaseList.size();
            return tempInnerBaseList.opt(size - 1);
        }

        /**
         * 编译时可以使用
         */
        Base getInnerTempListElement(int index) {
            return tempInnerBaseList.opt(index);
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
        ArrayLists<Base> getInnerTempList() {
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
            throw new Re_Accidents.CompileTimeExpressionProcessingError("not fount: " + element);
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
            cache0 = tempInnerBaseList.toArray(Base.EMPTY_BASE);
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            String text = Expression.getExpressionAsString(this);
            return writer.append(findFirstValidLine(), text);
        }


        public static String getExpressionsAsString(Re_CodeFile file) {
            _ExpressionFormatWriter expressionFormatWriter = new _ExpressionFormatWriter();
            Expression[] expressions = file.getExpressions();
            for (int a = 0, e = a + expressions.length; a < e; a++) {
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
                throw new Re_Accidents.CompileTimeExpressionProcessingError("offset="+startIndex+", len="+len+", size="+list.size());

            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
            Base prev = null;
            Base next;

            int l = text.getLastLine();

            for (int i = 0, size = Math.max(Math.min(list.size(), len), 0); i < size; i++) { //编译时获取到的数据可能是null
                if (null != (next = list.get(i + startIndex))) {
                    if ((codeIsVar(prev) || codeIsCall(prev)) &&
                            (codeIsVar(next) || codeIsCall(next))) {
                        l = text.append(l, CODE_BLANK_SPACE_CHARS);
                    }
                    l = text.write(next);

                    prev = next;
                }
            }
            return text.toString();
        }


        //no staticVariable
        static Expression createExpression(int line, Base base) {
            Expression expression = new Expression();
            expression.setLine(line);
            expression.setName(null);
            expression.compileVariable = null;
            expression.tempInnerBaseList = new ArrayLists<>(Base.EMPTY_BASE);
            expression.tempInnerBaseList.add(base);
            return expression;
        }
        //no staticVariable
        static Expression createExpression(int line, List<Base> base) {
            Expression expression = new Expression();
            expression.setLine(line);
            expression.setName(null);
            expression.compileVariable = null;
            expression.tempInnerBaseList = new ArrayLists<>(Base.EMPTY_BASE);
            expression.tempInnerBaseList.addAll(base);
            return expression;
        }
    }


    static class ____{}


    /**
     * 运行时 会存在这个类型的code
     * 变量名
     */
    public static class Var extends Base implements _ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = Var.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        @Override
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            String text;
            if (Re_CodeFile.isCompileVariable(compileVariable))
                text = Re_CodeFile.getCompileDeclareValue(compileVariable);
            else
                text = name;
            return writer.append(line, text);
        }


        //no staticVariable
        @SuppressWarnings("SameParameterValue")
        static Var createVar(int line, String name) {
            return createVar(line, name, null);
        }

        //no staticVariable
        @SuppressWarnings({"SameParameterValue", "rawtypes"})
        static Var createVar(int line, String name, Re_Variable variable) {
            Var v = new Var();
            v.setLine(line);
            v.setName(name);
            v.compileVariable = variable;
            return v;
        }
    }

    /**
     * 运行时 会存在这个类型的code
     * 等号
     */
    public static class Assignment extends Base implements _ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = Assignment.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        public Assignment() {
            super.name = CODE_VARIABLE_ASSIGNMENT_SYMBOL;//=
        }

        @Override
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            return writer.append(line, name);
        }

        //no staticVariable
        public static Assignment createAssignment(int len) {
            Assignment assignment = new Assignment();
            assignment.setLine(len);
            assignment.compileVariable = null;
            return assignment;
        }
    }

    /**
     * 运行时 会存在这个类型的code
     * 点
     */
    public static class Point extends Base implements _ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = Point.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        public Point() {
            super.name = CODE_OBJECT_POINT_SYMBOL;
        }


        @Override
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            return writer.append(line, name);
        }

        //no staticVariable
        public static Point createPoint(int len) {
            Point point = new Point();
            point.setLine(len);
            point.compileVariable = null;
            return point;
        }
    }







    /**
     * 运行时 会存在这个类型的code
     * ()
     * 函数
     */
    @SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
    public static class Call extends Base implements _ExpressionFormatWriter.Writerable {
        static final int CLASS_HASH = Call.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        /**
         * 编译时可以使用
         */
        ArrayLists<Expression> tempInnerParamList = new ArrayLists<>(Base.EMPTY_EXPRESSION);
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
            cache0 = tempInnerParamList.toArray(Base.EMPTY_EXPRESSION);
            tempInnerParamList = null;
            return cache0;
        }
        Expression getBuildParamExpressionCache(int index) {
            if (null != cache0) {
                return  cache0[index];
            }
            cache0 = tempInnerParamList.toArray(Base.EMPTY_EXPRESSION);
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
            int textl;
            textl = text.append(line, name);
            textl = text.append(textl, CODE_CALL_PARAM_JOIN_SYMBOL);
            List<Expression> list = getParamInnerTempListOrBuildParamExpressionCachesAsList();
            for (int i = 0; i < list.size(); i++) {
                Expression expression = list.get(i);
                if (expression.isEmpty())
                    continue;
                int expressionLine = expression.findFirstValidLine();

                _ExpressionFormatWriter expressionFormatWriter = new _ExpressionFormatWriter();
                expression.writeFormatExpression(expressionFormatWriter);
                textl = text.append(expressionLine, expressionFormatWriter);
                if (i < list.size() - 1)
                    textl = text.append(textl, CODE_CALL_PARAM_SEPARATOR + ' ');
            }
            textl = text.append(textl, CODE_CALL_PARAM_END_SYMBOL);

            return writer.append(line, text);
        }



        //no staticVariable
        static Call createCall(String name, int line, Expression expression) {
            Call call = new Call();
            call.setLine(line);
            call.setName(name);
            call.compileVariable = null;
            call.tempInnerParamList = new ArrayLists<>(Base.EMPTY_EXPRESSION);
            call.tempInnerParamList.add(expression);
            call.tempInnerParamCount = call.tempInnerParamList.size();
            return call;
        }
        //no staticVariable
        static Call createCall(String name, int line, List<Expression> expression) {
            Call call = new Call();
            call.setLine(line);
            call.setName(name);
            call.compileVariable = null;
            call.tempInnerParamList = new ArrayLists<>(Base.EMPTY_EXPRESSION);
            call.tempInnerParamList.addAll(expression);
            call.tempInnerParamCount = call.tempInnerParamList.size();
            return call;
        }

        static CallCreateDict createCallCreateDict(int line, Expression expression) {
            CallCreateDict call = new CallCreateDict();
            call.setLine(line);
            call.setName(Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_OBJECT);
            call.compileVariable = null;
            call.tempInnerParamList = new ArrayLists<>(Base.EMPTY_EXPRESSION);
            call.tempInnerParamList.add(expression);
            call.tempInnerParamCount = call.tempInnerParamList.size();
            return call;
        }

        static String demoCall1(String name, String... paramName) {
            List<Expression> varElementList = new ArrayList<>();
            if (null != paramName && paramName.length != 0) {
                for (String s : paramName) {
                    Expression expression = Expression.createExpression(Re_CodeFile.LINE_OFFSET, Var.createVar(Re_CodeFile.LINE_OFFSET, s));
                    varElementList.add(expression);
                }
            }
            return createCall(name, Re_CodeFile.LINE_OFFSET, varElementList).toString();
        }
    }


    static class ______{}

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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            String  st = CODE_MAP_JOIN_SYMBOL,
                    sp = CODE_SEMICOLON_SEPARATOR,
                    ed = CODE_MAP_END_SYMBOL;

            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
            int textl;
            textl = text.append(line, st);
            List<Expression> list = getParamInnerTempListOrBuildParamExpressionCachesAsList();
            for (Expression expression : list) {
                if (expression.isEmpty())
                    continue;
                int expressionLine = expression.findFirstValidLine();

                _ExpressionFormatWriter expressionFormatWriter = new _ExpressionFormatWriter();
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
            int textl;
            textl = text.append(line, CODE_LIST_JOIN_SYMBOL);
            List<Expression> list = getParamInnerTempListOrBuildParamExpressionCachesAsList();
            for (int i = 0; i < list.size(); i++) {
                Expression expression = list.get(i);
                if (expression.isEmpty())
                    continue;
                int expressionLine = expression.findFirstValidLine();

                _ExpressionFormatWriter expressionFormatWriter = new _ExpressionFormatWriter();
                expression.writeFormatExpression(expressionFormatWriter);
                textl = text.append(expressionLine, expressionFormatWriter);
                if (i < list.size() - 1)
                    textl = text.append(textl, CODE_CALL_PARAM_SEPARATOR + CODE_BLANK_SPACE_CHARS);
            }
            textl = text.append(textl, CODE_LIST_END_SYMBOL);

            return writer.append(line, text);
        }


        //no staticVariable
        @SuppressWarnings("SameParameterValue")
        static Call createCallCreateList(int line, List<Expression> expression) {
            CallCreateList call = new CallCreateList();
            call.setLine(line);
            call.setName(Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_LIST);
            call.compileVariable = null;
            call.tempInnerParamList = new ArrayLists<>(Base.EMPTY_EXPRESSION);
            ArrayLists<Expression> tempInnerParamList = call.tempInnerParamList;
            if (null != expression) {
                tempInnerParamList.addAll(expression);
            }
            call.tempInnerParamCount = call.tempInnerParamList.size();
            return call;
        }
        static String demoList2(String... paramName) {
            List<Expression> varElementList = new ArrayList<>();
            if (null != paramName && paramName.length != 0) {
                for (String s : paramName) {
                    Expression expression = Expression.createExpression(Re_CodeFile.LINE_OFFSET, Var.createVar(Re_CodeFile.LINE_OFFSET, s));
                    varElementList.add(expression);
                }
            }
            return createCallCreateList(Re_CodeFile.LINE_OFFSET, varElementList).toString();
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
            int textl;
            List<Expression> list = getParamInnerTempListOrBuildParamExpressionCachesAsList();
            int size1 = list.size();
            if (size1 <= 1)
                throw new Re_Accidents.CompileTimeExpressionProcessingError("error symbol param");

            for (int i = 0; i < list.size(); i++) {
                Expression expression = list.get(i);
                if (expression.isEmpty())
                    continue;
                int expressionLine = expression.findFirstValidLine();

                _ExpressionFormatWriter expressionFormatWriter = new _ExpressionFormatWriter();
                expression.writeFormatExpression(expressionFormatWriter);

                textl = text.append(expressionLine, expressionFormatWriter);
                if (i < list.size() - 1)
                    textl = text.append(textl, super.getName());
            }

            return writer.append(line, text);
        }
    }





    static class ________{}

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
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
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

        Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction myFunction;
        Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction.VariableBuilder _builder;
        Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction.Installer       _installer;


        @SuppressWarnings({"StatementWithEmptyBody", "ManualArrayCopy"})
        @Override
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            int oldModifier                     = myFunction.getOldModifier();
            String functionName                 = myFunction.functionName;
            String[] functionParamNames         = myFunction.functionParamNames;
            Re_CodeLoader_ExpressionConverts.CallFunction.FunParamTypesElement[] functionParamTypes = myFunction.functionParamTypes;
            Var[] functionReturnTypes           = myFunction.functionReturnTypes;
            CallCreateDict executeExpressions   = myFunction.executeExpressions;

            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
            int textl = line;
            if (oldModifier != 0) {
                textl = text.append(textl, Re_Modifiers.asString(oldModifier));
                textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
            }
            textl = text.append(textl, super.getName());
            if (null == functionName) {
                //匿名方法
            } else {
                //带名方法
                textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
                textl = text.append(textl, functionName);
            }

            String[] pns = new String[functionParamNames.length];
            for (int i = 0; i < functionParamNames.length; i++) {
                pns[i] = functionParamNames[i];
            }
            for (Re_CodeLoader_ExpressionConverts.CallFunction.FunParamTypesElement pc: functionParamTypes) {
                pns[pc.index] = (pns[pc.index] + Re_CodeLoader_ExpressionConverts.CallFunction.FUNCTION_TYPE_DECLARE_SYMBOL + Re_CodeLoader_ExpressionConverts.CallFunction.paramTypesAsString(pc.typeVar));
            }
            StringJoiner paramStr = new StringJoiner(CODE_CALL_PARAM_SEPARATOR + CODE_BLANK_SPACE_CHARS, CODE_CALL_PARAM_JOIN_SYMBOL, CODE_CALL_PARAM_END_SYMBOL);
            for (String s : pns) {
                paramStr.add(s);
            }
            textl = text.append(textl, paramStr.toString());

            if (null != functionReturnTypes) {
                textl = text.append(textl, Re_CodeLoader_ExpressionConverts.CallFunction.FUNCTION_TYPE_DECLARE_SYMBOL);
                textl = text.append(textl, Re_CodeLoader_ExpressionConverts.CallFunction.paramTypesAsString(functionReturnTypes));
            }

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

        Re_CodeLoader_ExpressionConverts_MyCVF.MyInit myInit;
        Re_CodeLoader_ExpressionConverts_MyCVF.MyInit.Installer _installer;

        public int      getParamCount() {
            return  myInit.functionParamNames.length;
        }
        public String   getParamName(int index) {
            return myInit.functionParamNames[index];
        }

        public CallCreateDict getFunctionExpression() {
            return myInit.executeExpressions;
        }



        @Override
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            StringJoiner paramStr = new StringJoiner(CODE_CALL_PARAM_SEPARATOR + CODE_BLANK_SPACE_CHARS, CODE_CALL_PARAM_JOIN_SYMBOL, CODE_CALL_PARAM_END_SYMBOL);
            for (String s : myInit.functionParamNames) {
                paramStr.add(s);
            }
            textl = text.append(textl, paramStr.toString());
            textl = text.write(myInit.executeExpressions);

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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
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

        Expression     conditionalExpression;
        CallCreateDict executeDict;
        CallCreateDict elseDict;

        boolean _elseIsIfCall;
        ConvertExpressionAsCallIf _elseIfCall;

        @Override
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            textl = text.append(textl, CODE_CALL_PARAM_JOIN_SYMBOL);
            textl = text.write(conditionalExpression);
            textl = text.append(textl, CODE_CALL_PARAM_END_SYMBOL);

            textl = text.write(executeDict);

            if (null != elseDict) {
                if (_elseIsIfCall) {
                    textl = text.append(textl, Re_Keywords.INNER_EXPRESSION_CALL__ELSE);
                    textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
                    textl = text.write(_elseIfCall);
                } else {
                    textl = text.append(textl, Re_Keywords.INNER_EXPRESSION_CALL__ELSE);
                    textl = text.write(elseDict);
                }
            }

            return writer.append(line, text);
        }
    }

    static class ConvertExpressionAsCallBased extends Call {
        static final int CLASS_HASH = ConvertExpressionAsCallBased.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        Expression conditionalExpression;

        CallCreateDict executeExpressions;

        @Override
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());
            textl = text.append(textl, CODE_CALL_PARAM_JOIN_SYMBOL);
            textl = text.write(conditionalExpression);
            textl = text.append(textl, CODE_CALL_PARAM_END_SYMBOL);

            textl = text.write(executeExpressions);

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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
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
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
            int textl;
            textl = text.append(line, super.getName());

            return writer.append(line, text);
        }
    }


    static class ConvertExpressionAsKeywordVar extends Call {
        static final int CLASS_HASH = ConvertExpressionAsKeywordVar.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        Re_CodeLoader_ExpressionConverts_MyCVF.MyVariable myVariable;
        Re_CodeLoader_ExpressionConverts_MyCVF.MyVariable.VariableBuilder _builder;
        Re_CodeLoader_ExpressionConverts_MyCVF.MyVariable.Installer       _installer;

        @Override
        public int writeFormatExpression(_ExpressionFormatWriter writer) {
            _ExpressionFormatWriter text = new _ExpressionFormatWriter();
            int textl = line;

            int variableModifier = myVariable.getVariableModifier();
            if (variableModifier != 0) {
                textl = text.append(textl, Re_Modifiers.asString(variableModifier));
                textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
            }
            textl = text.append(textl, super.getName());
            textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
            textl = text.append(textl, myVariable.name);

            if (null != myVariable.typeChecker) {
                textl = text.append(textl, Re_CodeLoader_ExpressionConverts.CallVar.TYPE_DECLARE_SYMBOL);
                textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
                textl = text.append(textl, Re_CodeLoader_ExpressionConverts.CallFunction.paramTypesAsString(myVariable.typeChecker));
            }

            Expression  expression = myVariable.initExpression;
            if (null != expression) {
                textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
                textl = text.write(Assignment.createAssignment(expression.getLine()));
                textl = text.append(textl, CODE_BLANK_SPACE_CHARS);
                textl = text.write(expression);
            }

            return writer.append(line, text);
        }
    }



    /*
     * 读取string 的思路是 在load前 处理字符串 将所有字符串替换为 变量名，
     * 执行时将从 常量管理器中获取常量
     */
    Re_CodeFile load(String expression, String version,
                     String methodName, String filePath, int lineOffset) throws Re_Accidents.CompileTimeGrammaticalException  {
        {
            Re_CodeFile block = new Re_CodeFile();
            block.methodName = methodName;
            block.filePath   = filePath;
            block.lineOffset = lineOffset;
            block.version    = version;

            {
                //行 跟踪器
                _LineTracker lineTracker = new _LineTracker();
                lineTracker.line = lineOffset;

                //字符 读取器
                CharsReaders reader = new CharsReaders();
                char[] chars = expression.toCharArray();
                reader.buffer(chars, chars.length);
                reader.setDelimiter(codeDelimiter);

                //代码读取器
                _CompileTimeCodeSourceReader compileTimeCodeSourceReader = new _CompileTimeCodeSourceReader(block, lineTracker, reader);
                Expression[] expressions  = compileTimeCodeSourceReader.readAllExpression();

                block.expressions = expressions;
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
    public static class _CompileTimeCodeSourceReader {
        Re_CodeFile block;
        _LineTracker lineTracker;
        CharsReaders reader;
        public _CompileTimeCodeSourceReader(Re_CodeFile block, _LineTracker lineTracker, CharsReaders reader) {
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
            ArrayLists<Expression> expressionList = new ArrayLists<>(Base.EMPTY_EXPRESSION);
            while (reader.findNext()) {
                Expression   expression;
                if (null == (expression = this.readExpression()))
                    continue;
                expressionList.add(expression);
            }
            return expressionList.toArray();
        }

        public Expression readExpression() throws Re_Accidents.CompileTimeGrammaticalException {
            try {
                Expression expression = new Expression();
                while (reader.findNext()) {
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
            } catch (Re_Accidents.CompileTimeGrammaticalException e) {
                throw e;
            } catch(Throwable e) {
                throw new Re_Accidents.CompileTimeGrammaticalException(Throwables.toString(e),
                        block.getFilePath(), lineTracker.getLine());
            }
        }


        /**
         * 读取一个连续的表达式子 遇到 ; symbol = 会断开
         * 可以返回flag 获取读取的数量， 读取的数量不一定是准确的 也可以是0
         */
        public int readNextSection(Expression toExpression) throws Re_Accidents.CompileTimeGrammaticalException {
            boolean hasSymbol = false;
            int     flag = 0; //可能会溢出返回错误的flag

            String  character;
            for (;;) {
                int prev_index = reader.getIndex();
                if (null == (character = reader.readNextAsString())) {
                    break;
                } else {
                    if (CODE_LINE_SEPARATOR_STRING.equals(character)) {
                        lineTracker.nextLine();
                        continue;
                    }
                    if ((character = character.trim()).length() == 0) {
                        continue;
                    }
                    character = intern(character);
                }


                if (isStringDeclare(character)) {
                    Base c = loadString00(block, toExpression, lineTracker, reader, prev_index);
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                }
                if (isCharDeclare(character)) {
                    Base c = loadChar00(block, toExpression, lineTracker, reader, prev_index);
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                }
                if (isString2Declare(character)) {
                    Base c = loadString20(block, toExpression, lineTracker, reader, prev_index);
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                }

                if (isNotesDeclare(character)) {
                    //跳过
                    skipSingleLineNote0(block, toExpression, lineTracker, reader, prev_index);
                    continue;
                }
                if (isDigitDeclareStartWith(character)) {
                    Base c = loadNumbers0(block, toExpression, lineTracker, reader, prev_index);
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                }
                if (isSymbolDeclare(character)) {    //symbol
                    Base c = new ConvertBeforeSymbol(character).setLine(lineTracker.getLine());
                    toExpression.addToInnerTempListLast(c);
                    hasSymbol = true;
                    continue;
                }


                if (character.equals(CODE_VARIABLE_ASSIGNMENT_SYMBOL)) {//=
                    Assignment c = Assignment.createAssignment(lineTracker.getLine());
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                } else if (character.equals(CODE_OBJECT_POINT_SYMBOL)) {//.
                    Point c = Point.createPoint(lineTracker.getLine());
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                } else if (character.equals(CODE_CALL_PARAM_JOIN_SYMBOL)) {//(
                    Base lastContent = null == toExpression ? null : notDiscon(toExpression.getInnerTempListLast());//上个集合的最后一个

                    Call c = new Call();
                    c.setLine(lineTracker.getLine());
                    if (codeIsVar(lastContent)) {
                        c.name = lastContent.name;
                        toExpression.removeInnerTempListCode(lastContent); //***** 删除上一个var 并且现在的新元素将名称设置为方法名
                    } else {
                        // ()
                        if (null == lastContent || null == lastContent.name) {
                            //a = (b);
                            //(b);
                            c.name = Re_Keywords.INNER_FUNCTION__;
                        } else if (codeIsCall(lastContent)) {
                            //(true, false)()       > 第二个(
                            //field(true, false)()  > 第二个(
                            c.name = Re_Keywords.INNER_FUNCTION__;
                        } else {
                            throw new Re_Accidents.CompileTimeGrammaticalException("[ " + lastContent + "(expression...) ]" ,
                                    block.getFilePath(), lineTracker.getLine());
                        }
                    }
                    c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);//将关键字值直接加入代码 以后将不在获取keyword

                    Expression exp = new Expression();
                    try {
                        while (reader.findNext()) {
                            int s;
                            if (isFlag(s = readNextSection(exp))) {
                                if (isFlagSemicolonOrParamSeparator(s)) {
                                    //pass
                                } else if (isFlagExitCall(s)) {
                                    break;
                                } else {
                                    throw new Re_Accidents.CompileTimeGrammaticalException("[ (expression...? ]",
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
                } else if (character.equals(CODE_CALL_PARAM_END_SYMBOL)) {//)
                    flag = FLAG_EXIT_CALL;
                    break;
                } else if (character.equals(CODE_MAP_JOIN_SYMBOL)) {//{
                    CallCreateDict c = new CallCreateDict();
                    c.name = Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_OBJECT;
                    c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);;//将关键字值直接加入代码 以后将不在获取keyword
                    c.setLine(lineTracker.getLine());

                    Expression exp = new Expression();
                    try {
                        while (reader.findNext()) {
                            int s;
                            if (isFlag(s = readNextSection(exp))) {
                                if (isFlagSemicolonOrParamSeparator(s)) {
                                    //pass
                                } else if (isFlagExitDict(s)) {
                                    break;
                                } else {
                                    throw new Re_Accidents.CompileTimeGrammaticalException("[ {expression...? ]",
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
                } else if (character.equals(CODE_MAP_END_SYMBOL)) {//}
                    flag = FLAG_EXIT_DICT;
                    break;
                } else if (character.equals(CODE_LIST_JOIN_SYMBOL)) {//[
                    CallCreateList c = new CallCreateList();
                    c.name = Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_LIST; //_list();
                    c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(c.name);;//将关键字值直接加入代码 以后将不在获取keyword
                    c.setLine(lineTracker.getLine());

                    Expression exp = new Expression();
                    try {
                        while (reader.findNext()) {
                            int s;
                            if (isFlag(s = readNextSection(exp))) {
                                if (isFlagSemicolonOrParamSeparator(s)) {
                                    //pass
                                } else if (isFlagExitList(s)) {
                                    break;
                                } else {
                                    throw new Re_Accidents.CompileTimeGrammaticalException("[ [expression...? ]",
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
                    int paramExpressionCount = c.getParamExpressionCount();
                    if (paramExpressionCount == 0) {
                        if (null == lastContent) {  //list
                            toExpression.addToInnerTempListLast(c);
                            flag++;
                            continue;
                        } else if (codeIsVar(lastContent)) { //var name, exp: Object[][][]
                            lastContent.name += (CODE_LIST_JOIN_SYMBOL + CODE_LIST_END_SYMBOL);
                            lastContent.compileVariable = Re_Variable.Unsafes.getKeywordVariable(lastContent.name);
                            continue;
                        } else {
                            /*
                             * a()[]
                             * .[]
                             */
                            throw new Re_Accidents.CompileTimeGrammaticalException("[ ...expression[]? ]",
                                    block.getFilePath(), lineTracker.getLine());
                        }
                    } else if (paramExpressionCount == 1) {
                        /*
                            a = space["return"];
                            a["b"] = 8;
                            a[0][1] = 9;
                            a=[0][0];
                            a[][][0];
                            a[1]();
                         */
                    } else {
                        if (null == lastContent) {  //list
                        } else if (codeIsVar(lastContent)) {
                            throw new Re_Accidents.CompileTimeGrammaticalException(toExpression.toCodeString() + c.toCodeString(),
                                    block.getFilePath(), lineTracker.getLine());
                        } else {
                            throw new Re_Accidents.CompileTimeGrammaticalException(toExpression.toCodeString() + c.toCodeString(),
                                    block.getFilePath(), lineTracker.getLine());
                        }
                    }
                    //list
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                } else if (character.equals(CODE_LIST_END_SYMBOL)) {//]
                    flag = FLAG_EXIT_LIST;
                    break;
                } else if (character.equals(CODE_SEMICOLON_SEPARATOR) )    {//;
                    flag = FLAG_SEMICOLON;
                    break;
                } else if (character.equals(CODE_CALL_PARAM_SEPARATOR)) {//,
                    flag = FLAG_PARAM_SEPARATOR;
                    break;
                } else {
                    Base c = new Var();
                    c.name = character;
                    c.line = lineTracker.getLine();
                    c.compileVariable = Re_Variable.Unsafes.getKeywordVariable(character);//将关键字值直接加入代码 以后将不在获取keyword
                    toExpression.addToInnerTempListLast(c);
                    flag++;
                    continue;
                }
                // println(character + "<" + cline.line + ">");
            }


            _CompileTimeCodeListEditor reader = this.readNextSection0GetReusingEditorCache(block, toExpression);

            //Interceptors
            formatExpression1ConvertExpression(block, toExpression, reader);

            //Convert Expression Symbol As SymbolCall
            if (hasSymbol) {
                formatExpression2ConvertExpressionTempSymbolAsCallSymbol(block, reader);
            }

            reader.removeNull();
            return flag;
        }
        /**
         * 编译时运行, 转换符号，和过滤拦截器等, 一个读取器 只有一个实例, 不可同时进行编辑
         */
        @SuppressWarnings("FieldMayBeFinal")
        private _CompileTimeCodeListEditor readNextSection0GetReusingEditorCache = new _CompileTimeCodeListEditor();
        private _CompileTimeCodeListEditor readNextSection0GetReusingEditorCache(Re_CodeFile block, Expression expression) {
            readNextSection0GetReusingEditorCache.block = block;
            readNextSection0GetReusingEditorCache.list  = expression.getInnerTempList();
            readNextSection0GetReusingEditorCache.index = _CompileTimeCodeListEditor.INIT_INDEX;
            return readNextSection0GetReusingEditorCache;
        }







        /**
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

            throw new UnsupportedOperationException("type: " + Re_Utilities.objectAsName(o));
        }









        static Var loadNumbers0(Re_CodeFile block, Expression expression, _LineTracker lineTracker, CharsReaders lineCharsCodeReader, int str_prev_index) throws Re_Accidents.CompileTimeGrammaticalException {
            String prefix = "";
            Var result;
            int stLine       = lineTracker.getLine();
            int elementCount = expression.getInnerTempListSize();

            //获取前缀符号
            Base prev_element = expression.getInnerTempListElement(elementCount - 1); //倒数第一个
            if (codeIsConvertBeforeSymbol(prev_element)) {
                if (prev_element.equalsName("+") || prev_element.equalsName("-")) {
                    prefix = prev_element.getName();
                }
                if (null == notDiscon(expression.getInnerTempListElement(elementCount - 2))) {//倒数第二个
                    if (prefix.isEmpty()) {
                        throw new Re_Accidents.CompileTimeGrammaticalException(
                                "can only be + or - before the number: " + Expression.getExpressionAsString(expression),
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
        static Var loadString00(Re_CodeFile block, Expression expression, _LineTracker lineTracker, CharsReaders lineCharsCodeReader, int str_prev_index) throws Re_Accidents.CompileTimeGrammaticalException {
            char symbol   = CODE_STRING_START_CHAR;
            char[] buffer = lineCharsCodeReader.buffer();
            int count     = lineCharsCodeReader.size();
            int stLine    = lineTracker.getLine();

            CharsWriters out = new CharsWriters(0);
            CharsWriters unicode = new CharsWriters(4);
            boolean hadSlash = false;
            boolean inUnicode = false;
            int st = str_prev_index + 1;
            for (; st < count; st++) {
                char ch = buffer[st];
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
                            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS +"Unable to parse unicode tip: " + unicode +": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, st-str_prev_index) + symbol)
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
                    st++;
                    lineCharsCodeReader.seekIndex(st);

                    Var aConst = block.createConstBase(expression, stLine, element);
                    return aConst;
                } else {
                    out.write(ch);
                }
            }
            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS +"error string declaration"+ ": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, st-str_prev_index) + String.valueOf(symbol)),
                    block.getFilePath(), stLine);
        }

        @SuppressWarnings("resource")
        static Var loadChar00(Re_CodeFile block, Expression expression, _LineTracker lineTracker, CharsReaders lineCharsCodeReader, int str_prev_index) throws Re_Accidents.CompileTimeGrammaticalException {
            char symbol = CODE_CHAR_START_CHAR;
            char[] buffer = lineCharsCodeReader.buffer();
            int count     = lineCharsCodeReader.size();
            int stLine    = lineTracker.getLine();

            CharsWriters out = new CharsWriters(0);
            CharsWriters unicode = new CharsWriters(4);
            boolean hadSlash = false;
            boolean inUnicode = false;
            int st = str_prev_index + 1;
            for (; st < count; st++) {
                char ch = buffer[st];
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
                            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS + "Unable to parse unicode tip: [" + unicode +"] "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, st-str_prev_index) + String.valueOf(symbol)),
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
                        throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS +"error char declaration"+ ": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, st-str_prev_index) + String.valueOf(symbol)),
                                block.getFilePath(), stLine);
                    }
                    // //__sout("@" + contnet + "@" + joinStringLineCount + "@");
                    // //__sout("------");
                    st++;
                    lineCharsCodeReader.seekIndex(st);

                    char c1 = contnet.charAt(0);
                    Var aConst = block.createConstBase(expression, stLine, c1);
                    return aConst;
                } else {
                    out.write(ch);
                }
            }
            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS +"error string declaration"+ ": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, st-str_prev_index) + Strings.cast(symbol)),
                    block.getFilePath(), stLine);
        }

        // """
        @SuppressWarnings("resource")
        static Var loadString20(Re_CodeFile block, Expression expression, _LineTracker lineTracker, CharsReaders lineCharsCodeReader, int str_prev_index) throws Re_Accidents.CompileTimeGrammaticalException {
            char[] symbol = CODE_STRING2_START_CHARS;
            char[] buffer = lineCharsCodeReader.buffer();
            int count     = lineCharsCodeReader.size();
            int stLine    = lineTracker.getLine();

            CharsWriters out = new CharsWriters(0);
            CharsWriters unicode = new CharsWriters(4);
            boolean hadSlash = false;
            boolean inUnicode = false;
            int st = str_prev_index + symbol.length;
            for (; st < count; st++) {
                char ch = buffer[st];
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
                            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS + "Unable to parse unicode tip: [" + unicode +"] "+ new String(buffer, str_prev_index, st - str_prev_index),
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
                if (ch == symbol[0] && (buffer[st+1] == symbol[1])  && (buffer[st+2] == symbol[2])) {
                    // String element
                    String element = out.toString();
                    // //__sout("@" + element + "@" + joinStringLineCount + "@");
                    // //__sout("------");
                    st += symbol.length;
                    lineCharsCodeReader.seekIndex(st);

                    Var aConst = block.createConstBase(expression, stLine, element);
                    return aConst;
                } else {
                    out.write(ch);
                }
            }
            throw new Re_Accidents.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+ CODE_BLANK_SPACE_CHARS + "unable to parse string2 tip: [" + out +"] "+ new String(buffer, str_prev_index, st - str_prev_index),
                    block.getFilePath(), stLine);
        }

        static void skipSingleLineNote0(Re_CodeFile block, Expression expression, _LineTracker lineTracker, CharsReaders lineCharsCodeReader, int str_prev_index) throws Re_Accidents.CompileTimeGrammaticalException {
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




        //if a new Expression is created in the compilation process, the expression needs to be formatted
        static public void formatExpression(Re_CodeFile block, Expression expression) {
            _CompileTimeCodeListEditor reader = new _CompileTimeCodeListEditor();
            reader.block = block;
            reader.list  = expression.getInnerTempList();

            formatExpression1ConvertExpression(block, expression, reader);
            formatExpression2ConvertExpressionTempSymbolAsCallSymbol(block, reader);

            reader.removeNull();
        }
        static void formatExpression1ConvertExpression(Re_CodeFile block, Expression toExpression, _CompileTimeCodeListEditor reader) throws Re_Accidents.CompileTimeGrammaticalException {
            try {
                for (Base current; null != (current = reader.nextNonNull()); ) {
                    ExpressionConverter expressionConverter;
                    if (null != (expressionConverter = getExpressionConverter(current))) {
                        expressionConverter.convert(reader, toExpression, current);
                    }
                }
            } finally {
                reader.resetIndex();
            }
        }
        //自动符号转方法
        static void formatExpression2ConvertExpressionTempSymbolAsCallSymbol(Re_CodeFile block, _CompileTimeCodeListEditor reader) throws Re_Accidents.CompileTimeGrammaticalException {
            try {

                //算数表达式转方法 1+1 >> +(1, 1)
                for (Base currentCode0; null != (currentCode0 = reader.nextNonNull());) {
                    if (codeIsConvertBeforeSymbol(currentCode0)) {
                        ConvertBeforeSymbol currentCode = (ConvertBeforeSymbol) currentCode0;
                        //        __sout("*index=" + reader.index() + ", var=" + currentCode.getInstanceName() + ", type=" + currentCode.getClass());
                        ArrayLists<Base> that = reader.list;

                        int current = reader.index();
                        //        __sout("*current: " + reader.now()); //
                        int prev = reader.findSymbolPrevOverallStartIndex(current);
                        int end  = reader.findSymbolNextOverallEndIndex(current);
                        //        __sout("*find_prev_overall_offset: " + prev);
                        //        __sout("*find_next_overall_offset: " + end);
                        if (prev == -1 || end == -1) {
                            int st = prev == -1 ? current : prev;
                            int len = end == -1 ? current - prev : end - prev;

                            throw new Re_Accidents.CompileTimeGrammaticalException(
                                    "[" + Expression.getExpressionAsString(that, st, len + 1) + "]" +
                                            "["+((prev == -1 ? "no-prev-element":"")  + CODE_BLANK_SPACE_CHARS + (end == -1 ? "no-next-element":"")).trim()+"]",
                                    block.getFilePath(), currentCode.getLine());
                        }
                        String name = currentCode.getName();

                        CallSymbol call = new CallSymbol();
                        call.setName(name);
                        call.setLine(currentCode.getLine());
                        call.compileVariable = Re_Variable.Unsafes.getKeywordVariable(name);//将关键字值直接加入代码 以后将不在获取keyword

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
                    }
                }
                //__sout(Expression.formatCodeFromRoot(that, 0, that.size()));
            } finally {
                reader.resetIndex();
            }
        }
























































        @SuppressWarnings({"UnnecessaryModifier", "UnnecessaryInterfaceModifier"})
        static protected interface ExpressionConverter {
            /**
             * 假如你需要删除元素可以直接setNull即可
             * 注意请不要创建新的表达式
             */
            public void convert(_CompileTimeCodeListEditor editor, Expression onExpression, Base current) throws Re_Accidents.CompileTimeGrammaticalException;
        }
        static protected void addExpressionConverter(Base filter, ExpressionConverter expressionConverter) {
            if (/* codeIsConvertBeforeSymbol(filter) || */
                   codeIsCallSymbol(filter)) {
                throw new Re_Accidents.CompileTimeExpressionProcessingError("unsupported filter interceptor: " + filter);
            }
            ExpressionConverter last;
            if (null == (last = expressionConverters.get(filter))) {
                expressionConverters.put(filter, expressionConverter);
            } else {
                throw new Re_Accidents.CompileTimeExpressionProcessingError("already filter interceptor: " + last);
            }
        }
        static protected ExpressionConverter getExpressionConverter(Base next) {
            return expressionConverters.get(next);
        }

        static protected final Map<Base, ExpressionConverter> expressionConverters = new HashMap<>();

        static {
            Re_CodeLoader_ExpressionConverts.CallClass.addExpressionConverterCallClass();
            Re_CodeLoader_ExpressionConverts.CallInit.addExpressionConverterCallInitFunction();
            Re_CodeLoader_ExpressionConverts.CallFunction.addExpressionConverterCallFunction();
            Re_CodeLoader_ExpressionConverts.CallFor.addExpressionConverterCallFor();
            Re_CodeLoader_ExpressionConverts.CallWhile.addExpressionConverterCallWhile();
            Re_CodeLoader_ExpressionConverts.CallForeach.addExpressionConverterCallForeach();
            Re_CodeLoader_ExpressionConverts.CallTry.addExpressionConverterCallTry();
            Re_CodeLoader_ExpressionConverts.CallIf.addExpressionConverterCallIf();
            Re_CodeLoader_ExpressionConverts.CallBased.addExpressionConverterCallBased();


            Re_CodeLoader_ExpressionConverts.CallVar.addExpressionConverterKeywordVar();
            Re_CodeLoader_ExpressionConverts.KeywordImport.addExpressionConverterKeywordImport();

            Re_CodeLoader_ExpressionConverts.KeywordContinue.addExpressionConverterKeywordContinue();
            Re_CodeLoader_ExpressionConverts.KeywordBreak.addExpressionConverterKeywordBreak();

            Re_CodeLoader_ExpressionConverts.KeywordReturn.addExpressionConverterKeywordReturn();
            Re_CodeLoader_ExpressionConverts.KeywordThrow.addExpressionConverterKeywordThrow();

            Re_CodeLoader_ExpressionConverts.KeywordDebugger.addExpressionConverterKeywordDebugger();
        }




    }







    @SuppressWarnings("ConstantConditions")
    public static class _CompileTimeCodeListEditor {
        static final int INIT_INDEX = -1;
        Re_CodeFile block;
        ArrayLists<Base> list;
        int index = INIT_INDEX;

        private _CompileTimeCodeListEditor() {}

        /**
         * 获取上一段开始指针, 当前必须是一个整体
         */
        public int findSymbolPrevOverallStartIndex(int index) {
            Base current = list.get(index);
            //当前是符号 符号不能连接比如 - - -
            if (codeIsConvertBeforeSymbol(current)) {
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
            throw new Re_Accidents.CompileTimeGrammaticalException(
                    "unsupported current-type: " + current.getClass().getName(),
                    block.getFilePath(), current.getLine());
        }
        /**
         * 获取下一段结束指针， 当前必须是一个整体
         */
        public int findSymbolNextOverallEndIndex(int index) {
            Base current = list.get(index);
            //当前是符号 符号不能连接比如 - - -
            if (codeIsConvertBeforeSymbol(current)) {
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
            throw new Re_Accidents.CompileTimeGrammaticalException(
                    "unsupported current-type: " + current.getClass().getName(),
                    block.getFilePath(), current.getLine());
        }


        /**
         * 其实可能不需要Gc工作
         */
        public void removeNull() {
            int set = 0;
            ArrayLists<Base> that = this.list;
            for (Base base : that)
                if (null != base)
                    that.set(set++, base);
            that.setSize(set);
        }

        public void setNull(int index) {
            this.list.set(index, null);
        }
        //set[offset - (indexAfter - 1)]
        public void setNullRange(int offsetIndex, int indexAfter) {
            if (offsetIndex < 0 || indexAfter < 0 || offsetIndex > indexAfter)
                throw new Re_Accidents.CompileTimeExpressionProcessingError("offset=" + offsetIndex + ", indexAfter=" + indexAfter);
            int len = indexAfter - offsetIndex;
            if (len <= 0)
                throw new Re_Accidents.CompileTimeExpressionProcessingError("offset=" + offsetIndex + ", len=" + len + ", require len > 0");
            for (; offsetIndex < indexAfter; offsetIndex++) {
                list.set(offsetIndex, null);
            }
        }



        public void set(int index, Base element) {
            this.list.set(index, element);
        }


        public ArrayLists<Base> subList(int fromIndex, int toIndex) {
            int count = this.size();
            ArrayLists<Base> list = this.list;
            ArrayLists<Base> newList = new ArrayLists<>(Base.EMPTY_BASE);
            if (fromIndex < toIndex) {
                if (toIndex <= count) {
                    newList.addAll(list.subList(fromIndex, toIndex));
                }
            }
            return newList;
        }

        public void resetIndex() {
            this.index = INIT_INDEX;
        }
        public int seek(int index) {
            return this.index = (index < 0 ? -1 : index);
        }
        public int index() {
            return this.index;
        }

        public int size() {
            return this.list.size();
        }


        public boolean isStarted()  { return this.index >= 0; }



        public int findPrevNonNull() {
            return list.lastIndexOfNonNull(this.index - 1);
        }
        public int findPrevNonNull(int index) {
            return list.lastIndexOfNonNull(index);
        }

        public Base prevNonNull() {
            ArrayLists<Base> list = this.list;
            int i = list.lastIndexOfNonNull(this.index - 1);
            if (i == -1) {
                this.index = 0;
                return null;
            } else {
                this.index = i;
                return list.get(i);
            }
        }


        public int findNextNonNull() {
            return list.indexOfNonNull(this.index + 1);
        }
        public int findNextNonNull(int index) {
            return list.indexOfNonNull(index);
        }

        public Base nextNonNull() {
            ArrayLists<Base> list = this.list;
            int i = list.indexOfNonNull(this.index + 1);
            if (i == -1) {
                this.index = list.size();
                return null;
            } else {
                this.index = i;
                return list.get(i);
            }
        }


        public Base getOrNull(int index) {
            return this.list.opt(index);
        }

        public static Var getAsVar(_CompileTimeCodeListEditor editor, int index) {
            Base two         = editor.getOrNull(index);
            if (codeIsVar(two)) {
                return (Var) two;
            }
            return null;
        }
        public static CallCreateDict getAsCallCreateDict(_CompileTimeCodeListEditor editor, int index) {
            Base two         = editor.getOrNull(index);
            if (codeIsCallCreateDict(two)) {
                return (CallCreateDict) two;
            }
            return null;
        }
    }











    public static class _Utilities {
        @SuppressWarnings("DanglingJavadoc")
        public static class Expressions {
            /**
             * 其他的表达式工具
             * {@link Re_CodeLoader_ExpressionConverts.CallFunction.FunParamTypes#readTypes(Call)}
             * {@link Re_CodeLoader_ExpressionConverts.CallFunction.FunParamTypes#readTypesVarsFromVarOrList(Base)}}
             */


            /**
             * @return 如果某个表达式只有变量名则返回变量名 否则返回null
             */
            public static String getExpressionAsVarName(Expression expression) {
                List<Base> param = expression.getInnerListOrBuildCodeCacheAsList();
                if (param.size() == 1) {
                    Base object = param.get(0);
                    if (codeIsVar(object)) {
                        return  object.getName();
                    }
                }
                return null;
            }
            /**
             * call必须符合
             *   name(p1, p2, p3);
             */
            public static String[] getCallParamNameArray(Call expr) {
                List<Expression> expressionList = expr.getParamInnerTempListOrBuildParamExpressionCachesAsList();
                int count = expressionList.size();
                if (count == 0) {
                    return Finals.EMPTY_STRING_ARRAY;
                } else {
                    String[] paramNames = new String[count];
                    for (int i = 0; i < paramNames.length; i++) {
                        Expression expression = expressionList.get(i);
                        String expressionAsLocalName = getExpressionAsVarName(expression);
                        if (null == expressionAsLocalName) {
                            return null;
                        } else {
                            paramNames[i] = expressionAsLocalName;
                        }
                    }
                    return paramNames;
                }
            }


            public static Var getExpressionAsVar(Expression expression) {
                List<Base> param = expression.getInnerListOrBuildCodeCacheAsList();
                if (param.size() == 1) {
                    Base object = param.get(0);
                    if (codeIsVar(object)) {
                        return (Var) object;
                    }
                }
                return null;
            }

            public static Var[] getCallParamVarArray(Call expr) {
                List<Expression> expressionList = expr.getParamInnerTempListOrBuildParamExpressionCachesAsList();
                int count = expressionList.size();
                if (count == 0) {
                    return Base.EMPTY_VAR;
                } else {
                    Var[] paramNames = new Var[count];
                    for (int i = 0; i < paramNames.length; i++) {
                        Expression expression = expressionList.get(i);
                        Var expressionAsLocalName = getExpressionAsVar(expression);
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