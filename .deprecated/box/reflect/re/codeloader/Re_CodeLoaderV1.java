package top.fols.box.reflect.re;

import java.util.*;

import top.fols.atri.assist.ArrList;
import top.fols.atri.assist.StringJoiner;
import top.fols.atri.io.CharCodeReader;
import top.fols.atri.io.file.Filex;
import top.fols.atri.lang.Strings;
import top.fols.atri.lang.Objects;
import top.fols.atri.io.CharArrayWriters;
import top.fols.atri.util.TabPrint;
import top.fols.box.io.LineEditor;

@SuppressWarnings({"UnnecessaryLocalVariable", "IfCanBeSwitch", "UnnecessaryContinue"})
public class Re_CodeLoader {

    public static final String FILE_EXTENSION_SYMBOL  = Re_CodeSymbol.intern(".");
    public static final String RE_FILE_EXTENSION_NAME = Re_CodeSymbol.intern("re");


    public static final char   PACKAGE_SEPARATOR            = Re_CodeSymbol.intern('.');
    public static final String PACKAGE_SEPARATOR_STRING     = Re_CodeSymbol.intern("" + PACKAGE_SEPARATOR);

    public static final String SUB_CLASS_SEPARATOR          = Re_CodeSymbol.intern("$");


    private static final char[] FILE_SEPARATOR              = Re_CodeSymbol.intern(Filex.separator_all());



    private static final char   lineSeparatorChar  = Re_CodeSymbol.intern(           Re_CodeSymbol.CODE_LINE_SEPARATOR_CHAR);
    private static final char[] lineSeparatorChars = Re_CodeSymbol.intern(new char[]{Re_CodeSymbol.CODE_LINE_SEPARATOR_CHAR} );

    static final char[][] separate = new CharCodeReader() {{
        this.addSeparatorsAndSort(
                " ".toCharArray(),

                Re_CodeSymbol.CODE_NOTE_START.toCharArray(),                    // //
                Re_CodeSymbol.CODE_SEMICOLON_SEPARATOR.toCharArray(),           // ;

                Re_CodeSymbol.CODE_OBJECT_POINT_SYMBOL.toCharArray(),           // .

                Re_CodeSymbol.CODE_VARIABLE_ASSIGNMENT_SYMBOL.toCharArray(),    // =


                Re_CodeSymbol.CODE_PARAM_JOIN_SYMBOL.toCharArray(),            // (
                Re_CodeSymbol.CODE_PARAM_SEPARATOR.toCharArray(),              // ,
                Re_CodeSymbol.CODE_PARAM_END_SYMBOL.toCharArray(),             // )


                //_object
                Re_CodeSymbol.CODE_MAP_JOIN_SYMBOL.toCharArray(),              // {
                Re_CodeSymbol.CODE_MAP_END_SYMBOL.toCharArray(),               // }
                //_list
                Re_CodeSymbol.CODE_LIST_JOIN_SYMBOL.toCharArray(),              // [
                Re_CodeSymbol.CODE_LIST_END_SYMBOL.toCharArray(),               // ]


                Re_CodeSymbol.CODE_STRING_START.toCharArray(),                 // "
                Re_CodeSymbol.CODE_CHAR_START.toCharArray(),                   // '
                Re_CodeSymbol.CODE_STRING2_START.toCharArray(),                // ''  [two ']


                lineSeparatorChars                                             // \n
        );
        this.addSeparatorsAndSort(
            Re_CodeSymbol.getAutomaticConversionOperator()                     //+ - * / ...
        );
        //__println("symbol: " + Arrays.deepToString(getSeparators()) + ", size=" + separate.length);
    }}.getSeparators();


    /**
     * 非继承 类型相同, 标识一个代码 准确来是var a.b   a和b就是code
     */
    public static final Class<Code> CODE_CLASS = Code.class;



    public static boolean codeIsCode(Code object) {
        return object.getClass() == CODE_CLASS;
    }
    /**
     * =
     */
    public static boolean codeIsAssignment(Code object) {
        return object instanceof Assignment;
    }
    /**
     * ()
     */
    public static boolean codeIsCall(Code object) {
        return object instanceof Call;
    }
    /**
     * .
     */
    public static boolean codeIsPoint(Code object) {
        return object instanceof Point;
    }



    //-------------------------------------------------

    /**
     * 运算符
     * 在运行时不会出现
     */
    public static boolean codeIsCTSymbol(Code object) {
        return object instanceof CTSymbol;
    }

    /**
     * ;
     * 在运行时不会出现
     */
    public static boolean codeIsCTSemicolon(Code object) {
        return object instanceof CTSemicolon;
    }

    /**
     * (Expression)
     */
    public static boolean codeIsCTExpression(Code object) {
        return object instanceof CTExpression;
    }




    //-------------------------------------------------

    /**
     * @param object code
     * @return {@link #codeIsAssignment(Code)} ||
     *         {@link #codeIsCTSymbol(Code)}}  ||
     *         {@link #codeIsCTSemicolon(Code)};
     */
    public static boolean codeIsDiscon(Code object) {
        return  codeIsAssignment(object) ||
                codeIsCTSymbol(object) ||
                codeIsCTSemicolon(object);
    }

    /**
     *
     * @param object code
     * @return {@link #codeIsAssignment(Code)} ||
     *         {@link #codeIsCTSymbol(Code)}}  ||
     *         {@link #codeIsCTSemicolon(Code)} ? null : object;
     */
    public static <C extends Code> C notDiscon(C object) {
        return  codeIsAssignment(object) ||
                codeIsCTSymbol(object) ||
                codeIsCTSemicolon(object) ? null :object;
    }















    /**
     * 运算符
     */
    public static boolean isSymbolDeclare(String name) {
        return Re_CodeSymbol.isAutomaticConversionOperatorSymbol(name);
    }
    /**
     * 是否是注释 [//]
     */
    public static boolean isNotesDeclare(String str) {
        return Re_CodeSymbol.CODE_NOTE_START.equals(str);
    }
    /**
     * "
     */
    public static boolean isStringDeclare(String str) {
        return Re_CodeSymbol.CODE_STRING_START.equals(str);
    }
    /**
     * '
     */
    public static boolean isCharDeclare(String str) {
        return Re_CodeSymbol.CODE_CHAR_START.equals(str);
    }
    /**
     * ''
     */
    public static boolean isString2Declare(String str) {
        return Re_CodeSymbol.CODE_STRING2_START.equals(str);
    }


    /**
     * ^([0-9]+)
     */
    public static boolean isDigitDeclareStartWith(String str) {
        char   ch;
        return(ch = str.charAt(0)) >= '0' && ch <= '9';
    }




    /**
     * 运行时 会存在这个类型的code
     *
     * xxx
     * 一般是变量名
     */
    public static class Code {
        public static final CTExpression[] EMPTY_EXPRESSION = {};
        public static final Code[]         EMPTY_CODE       = {};

        static final int CLASS_HASH = Code.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Code code = (Code) o;
            return  Objects.equals(name, code.name);
        }




        @SuppressWarnings("rawtypes")
        Re_Variable staticValue;//修改名称一定要更新 tip
        String      name;       //修改名称一定要更新 tip
        int         line;

        public String getName() {
            return this.name;
        }
        Code setName(String name) {
            this.name = name;
            return this;
        }
        public boolean equalsName(String name) {
            return this.name.equals(name);
        }


        public int getLine() {
            return this.line;
        }
        Code setLine(int line) {
            this.line = line;
            return this;
        }



        public static CharSequence lineAddressString(String fileName, int line) {
            return new StringBuilder()
                    .append('(').append(fileName).append(":").append(line).append(')');
        }






        public static String getCallAsString(Re_CodeFile block, Call call) {
            if (null == call)
                return "";
            return call.toString();
        }
        public static String getExpressionAsString(Re_CodeFile block, CTExpression expression) {
            List<Code> list = expression.getInnerListOrBuildCodeCacheAsList();
            return getExpressionAsString(block, list, 0, list.size());
        }
        public static String getExpressionAsString(Re_CodeFile block, List<Code> codeList) {
            return getExpressionAsString(block, codeList, 0, codeList.size());
        }
        public static String getExpressionAsString(Re_CodeFile block, List<Code> codeList, int startIndex, int len) {
            if (startIndex + len > codeList.size())
                return "";

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < len; i++) {
                Code code = codeList.get(i);
                stringBuilder.append(code);
            }
            return stringBuilder.toString();
        }


//        /**
//         * cache可能编译也可能没编译
//         */
//        static void append0(Re_CodeFile block, LineEditor string, List<Code> codes, int off, int len) {
//            if (null != codes) {
//                if (off + len > codes.size()) {
//                    len = codes.size() - off;
//                }
//
//                for (int ei = 0; ei < len; ei++) {
//                    Code linkedCode = codes.get(ei + off);
//                    if (null == linkedCode)
//                        continue;
//
//                    if (linkedCode instanceof CTSemicolon) {
//                        string.w(linkedCode.line(), linkedCode.getInstanceName());
//                        continue;
//                    } else if (linkedCode instanceof Assignment) {
//                        string.w(linkedCode.line(), linkedCode.getInstanceName());
//                    } else if (linkedCode instanceof Point) {
//                        string.w(linkedCode.line(), linkedCode.getInstanceName());
//                    } else if (linkedCode instanceof Call) {
//                        string.w(linkedCode.line(), linkedCode.getInstanceName());
//
//                        string.w(Re_CodeSymbol.CODE_PARAM_JOIN_SYMBOL);
//
//                        Call contentFun = (Call) linkedCode;
//                        List<CTExpression> list = contentFun.getParamInnerTempListOrBuildParamExpressionCachesAsList();
//                        int size = list.size();
//                        for (int i = 0; i < size; i++) {
//                            CTExpression v = list.get(i);
//
//                            append0(block, string, v.getInnerTempList(), 0, v.getInnerTempListSize());
//
//                            if (i < size -1)
//                                string.w(Re_CodeSymbol.CODE_PARAM_SEPARATOR);
//                        }
//                        string.w(Re_CodeSymbol.CODE_PARAM_END_SYMBOL);
//                    } else {
//                        //const....
//                        //symbol
//                        string.w(linkedCode.line(), linkedCode.getInstanceName());
//                    }
//                }
//            }
//        }
//        public static String formatCodeFromRoot(Re_CodeFile block, List<Code> codeList, int codeIndex, int len) {
//            if (codeIndex >= codeList.size())
//                return "";
//            LineEditor string = new LineEditor(codeList.get(codeIndex).line());
//            append0(block, string, codeList, codeIndex, len);
//            return string.toString();
//        }
//        public static String formatCodeFromRoot(Re_CodeFile block, CTExpression[] expressions, int index, int len) {
//            if (index >= expressions.length) {
//                return "";
//            }
//            LineEditor editor = new LineEditor();
//            for (int i = 0; i < len; i++) {
//                CTExpression expression = expressions[index+i];
//                append0(block, editor, expression.getInnerTempList(), 0, expression.getInnerTempListSize());
//                if (i < expressions.length -1)
//                    editor.w(Re_CodeSymbol.CODE_PARAM_SEPARATOR);
//            }
//            return editor.toString();
//        }
//        public static String formatCodeFromRoot(Re_CodeFile block) {
//            return formatCodeFromRoot(block, block.getExpressions(), block.getExpressionsOffset(), block.getExpressionsNeedExecuteCount());
//        }






        @Override
        public String toString() {
            return ""+name;
        }
    }





    /**
     * 运行时 会存在这个类型的code
     * 等号
     */
    public static class Assignment extends Code {
        static final int CLASS_HASH = Assignment.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        public Assignment() {
            super.name = Re_CodeSymbol.CODE_VARIABLE_ASSIGNMENT_SYMBOL;//=
        }

        public static Assignment createAssignment(int len) {
            Assignment assignment = new Assignment();
            assignment.setLine(len);
            return assignment;
        }

        @Override
        public String toString() {
            return super.name;
        }
    }

    /**
     * 运行时 会存在这个类型的code
     * 点
     */
    public static class Point extends Code {
        static final int CLASS_HASH = Point.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        public Point() {
            super.name = Re_CodeSymbol.CODE_OBJECT_POINT_SYMBOL;
        }
        //.

        public static Point createPoint(int len) {
            Point Point = new Point();
            Point.setLine(len);
            return Point;
        }

        @Override
        public String toString() {
            return super.name;
        }
    }



    /**
     * 运行时 会存在这个类型的code
     * ()
     * 函数
     */
    @SuppressWarnings({"UnusedReturnValue"})
    public static class Call extends Code {
        static final int CLASS_HASH = Call.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        protected ArrList<CTExpression> tempInnerParamList = new ArrList<>(CTExpression.EMPTY_EXPRESSION);
        protected int size = 0;

        void addToParamInnerTempListLast(CTExpression newTop) {
            this.tempInnerParamList.add(newTop);
            this.size++;
        }
        void removeParamInnerTempListElement(CTExpression element) {
            for (int i = size - 1; i > 0; i--) {
                Code code = tempInnerParamList.get(i);
                if  (code.equals(element)) {
                    tempInnerParamList.remove(i);
                    size--;
                    return;
                }
            }
        }



//-------------------------------------------------------
        //将列表转换为数组， 如果在这之后列表还被添加删除 数组将不会有变动， 速度更快 也便于使用

        public boolean isEmptyNameCall() {
            return null == super.name || super.name.length() == 0;
        }
        public int getParamExpressionCount() {
            return size;
        }


        CTExpression[] cache0;
        /**
         * 不要修改
         */
        CTExpression[] getBuildParamExpressionCaches() {
            if (null != cache0) {
                return  cache0;
            }
            cache0 = tempInnerParamList.toArray(CTExpression.EMPTY_EXPRESSION);
            tempInnerParamList = null;
            return cache0;
        }
        CTExpression getBuildParamExpressionCache(int index) {
            if (null != cache0) {
                return  cache0[index];
            }
            cache0 = tempInnerParamList.toArray(CTExpression.EMPTY_EXPRESSION);
            tempInnerParamList = null;
            return cache0[index];
        }


        /**
         * @return 可能会生成一个新的列表
         */
        List<CTExpression> getParamInnerTempListOrBuildParamExpressionCachesAsList() {
            return null == tempInnerParamList ?Arrays.asList(cache0) : tempInnerParamList;
        }
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(null == name?"":name);
            stringBuilder.append(Re_CodeSymbol.CODE_PARAM_JOIN_SYMBOL);

            StringJoiner stringJoiner = new StringJoiner(Re_CodeSymbol.CODE_PARAM_SEPARATOR+' ');
            for (Code e: getParamInnerTempListOrBuildParamExpressionCachesAsList()) {
                if (null != e) {
                    stringJoiner.add(e.toString());
                }
            }
            stringBuilder.append(stringJoiner);
            stringBuilder.append(Re_CodeSymbol.CODE_PARAM_END_SYMBOL);
            return stringBuilder.toString();
        }


        public static class CallCreateDict  extends Call {
            @Override
            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Re_CodeSymbol.CODE_MAP_JOIN_SYMBOL);

                StringJoiner stringJoiner = new StringJoiner(Re_CodeSymbol.CODE_PARAM_SEPARATOR+' ');
                for (Code e: getParamInnerTempListOrBuildParamExpressionCachesAsList()) {
                    if (null != e) {
                        stringJoiner.add(e.toString());
                    }
                }
                stringBuilder.append(stringJoiner);
                stringBuilder.append(Re_CodeSymbol.CODE_MAP_END_SYMBOL);
                return stringBuilder.toString();
            }
        }
        public static boolean codeIsCallCreateDict(Code call) {
            return call instanceof CallCreateDict;
        }

        public static class CallCreateList  extends Call {
            @Override
            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Re_CodeSymbol.CODE_LIST_JOIN_SYMBOL);

                StringJoiner stringJoiner = new StringJoiner(Re_CodeSymbol.CODE_PARAM_SEPARATOR+' ');
                for (Code e: getParamInnerTempListOrBuildParamExpressionCachesAsList()) {
                    if (null != e) {
                        stringJoiner.add(e.toString());
                    }
                }
                stringBuilder.append(stringJoiner);
                stringBuilder.append(Re_CodeSymbol.CODE_LIST_END_SYMBOL);
                return stringBuilder.toString();
            }
        }
        public static boolean codeIsCallCreateList(Code call) {
            return call instanceof CallCreateList;
        }

        public static class CallSymbol      extends Call {
            @Override
            public String toString() {
                List<CTExpression> tempInnerParamList1 = super.getParamInnerTempListOrBuildParamExpressionCachesAsList();
                int size1 = tempInnerParamList1.size();
                if (size1 < 1)
                    return super.toString(); //-()  ?
                if (size1 == 1)
                    return super.toString(); //-(1) ?

                StringJoiner stringJoiner = new StringJoiner(super.getName());
                for (CTExpression ctExpression : tempInnerParamList1) {
                    stringJoiner.add(ctExpression.toString());
                }
                return stringJoiner.toString();
            }
        }
        public static boolean codeIsCallSymbol(Code call) {
            return call instanceof CallSymbol;
        }
    }


    /**
     * 运算符
     */
    public static class CTSymbol extends Code {
        static final int CLASS_HASH = CTSymbol.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        public CTSymbol(String name) {
            super.name = name;
        }

        @Override
        public String toString() {
            return super.name;
        }
    }


    /**
     * 分号
     */
    public static class CTSemicolon extends Code {
        static final int CLASS_HASH = CTSemicolon.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        public CTSemicolon() {
            super.name = Re_CodeSymbol.CODE_SEMICOLON_SEPARATOR;//;
        }

        public static final CTSemicolon DEFAULT_SEMICOLON = new CTSemicolon();//无意义

        @Override
        public String toString() {
            return super.name;
        }

    }

    /**
     * (Expression, Expression, ...)
     * </br>
     * Expression1; Expression2;
     */
    @SuppressWarnings({"FieldMayBeFinal", "UnusedReturnValue"})
    public static class CTExpression extends Code {
        static final int CLASS_HASH = CTExpression.class.hashCode();
        @Override
        public int hashCode() {
            return CLASS_HASH * (name == null ? 0 : name.hashCode());
        }


        protected ArrList<Code> tempInnerCodeList = new ArrList<>(CTExpression.EMPTY_CODE);

        void addToInnerTempListLast(Code newTop) {
            this.tempInnerCodeList.add(newTop);
        }
        Code getInnerTempListFirst() {
            return tempInnerCodeList.size() > 0? tempInnerCodeList.get(0): null;
        }
        Code getInnerTempListLast() {
            int size = tempInnerCodeList.size();
            return size > 0 ? tempInnerCodeList.get(size - 1) : null;
        }
        Code getInnerTempListElement(int index) {
            return index >= 0 && index< tempInnerCodeList.size()? tempInnerCodeList.get(index):null;
        }


        int getInnerTempListSize() {
            return tempInnerCodeList.size();
        }

        ArrList<Code> getInnerTempList() {
            return this.tempInnerCodeList;
        }
        void removeInnerTempListCode(Code element) {
            if (null == element) {
                return;
            }
            for (int i = getInnerTempListSize() - 1; i >= 0; i--) {
                Code code = tempInnerCodeList.get(i);
                if (element == code) {
                    tempInnerCodeList.remove(i);
                    return;
                }
            }
            throw new Re_Exceptions.CompileTimeGrammaticalException("not fount: " + element);
        }


        @Override
        CTExpression setLine(int line) {
            super.line = line;
            return this;
        }
        void setLineFromFirstElement() {
            int siz = tempInnerCodeList.size();
            if (siz == 0)
                return;
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < siz; i++) {
                Code code_;
                if ((code_ = tempInnerCodeList.get(i)).line > 0) {
                    super.line = code_.line;
                    break;
                }
            }
        }




//-------------------------------------------------------
        //将列表转换为数组， 如果在这之后列表还被添加删除 数组将不会有变动



        Code[] cache0;
        Code[] getBuildCodeCache() {
            if (null != cache0) {
                return  cache0;
            }
            cache0 = tempInnerCodeList.toArray(CTExpression.EMPTY_CODE);
            tempInnerCodeList = null;
            return cache0;
        }




        /**
         * @return 可能会生成一个新的列表
         */
        List<Code> getInnerListOrBuildCodeCacheAsList() {
            return null == tempInnerCodeList ?Arrays.asList(cache0) : tempInnerCodeList;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Code e: getInnerListOrBuildCodeCacheAsList()) {
                if (null != e) {
                    stringBuilder.append(e);
                }
            }
            return stringBuilder.toString();
        }
    }














    /*
     * 读取string 的思路是 在load前 处理字符串 将所有字符串替换为 变量名，
     * 执行时将从 常量管理器中获取常量
     */
    public Re_CodeFile load(String expression,
                            String filePath, int lineOffset) throws Re_Exceptions.CompileTimeGrammaticalException  {
        {
            Re_CodeFile block = new Re_CodeFile();
            block.filePath = filePath;
            block.lineOffset = lineOffset;

            //行 跟踪器
            LineTracker lineTracker = new LineTracker();
            lineTracker.line        = lineOffset;

            //代码读取器
            CharCodeReader reader = new CharCodeReader();
            char[] chars = expression.toCharArray();
            reader.buffer(chars, chars.length);
            reader.setSeparators(separate);

            CompileTimeCodeSourceReader compileTimeCodeSourceReader = new CompileTimeCodeSourceReader(block, lineTracker, reader);
            CTExpression[] expressions = compileTimeCodeSourceReader.readAllExpression();
            block.setExpressions(expressions);

            //System.out.println(TabPrint.wrap(block.getExpressions()));

            return block;
        }
//        {
//            Re_CodeFile block = new Re_CodeFile();
//            block.filePath = filePath;
//            block.lineOffset = lineOffset;
//            if (null != expression && expression.length() != 0) {
//                char[] chars = expression.toCharArray();
//
//                //行 跟踪器
//                _LineTracker lineTracker = new _LineTracker();
//                lineTracker.line = lineOffset;
//
//                //代码读取器
//                CharCodeReader reader = new CharCodeReader();
//                reader.buffer(chars, chars.length);
//                reader.setSeparators(separate);
//
//                try {
//                    //一次性读取所有Code
//                    AssistExpression[] re_lineCodes = readExpressions(block, lineTracker, reader);
//                    block.setExpressions(re_lineCodes);
//                } finally {
//                    reader.release();
//                }
//            } else {
//                block.setExpressions(AssistExpression.EMPTY_EXPRESSION);
//            }
//
//            //System.out.println(TabPrint.wrap(block.getExpressions()));
//            return block;
//        }
    }


















    private CTExpression[] readExpressions(Re_CodeFile block, LineTracker lineTracker, CharCodeReader reader) throws Re_Exceptions.CompileTimeGrammaticalException {
        //一次性读取所有Code
        CTExpression expressions = this.readExpressions0(block, lineTracker, reader);
        CTExpression[] re_lineCodes = this.splitCode(expressions);  //分割表达式
        return re_lineCodes;
    }

    /**
     * 读取所有表达式 为一个表达式
     */
    @SuppressWarnings("ConstantConditions")
    private CTExpression readExpressions0(Re_CodeFile block, LineTracker lineTracker, CharCodeReader reader) throws Re_Exceptions.CompileTimeGrammaticalException {
        final char[] lineSeparator = lineSeparatorChars;
        CTExpression expression = new CTExpression();

        boolean hasSymbol = false;
        String var_name;
        char[] code_chars;
        for (;;) {
            int prev_index = reader.getIndex();
            if (null == (code_chars = reader.readNext())) {
                break;
            } else {
                if (Arrays.equals(code_chars, lineSeparator)) {
                    lineTracker.nextLine();
                    continue;
                }
                if ((var_name = new String(code_chars).trim()).length() == 0) {
                    continue;
                }
                var_name = Re_CodeSymbol.intern(var_name);
            }


            if (isNotesDeclare(var_name)) {
                //跳过
                skipNote0(block, expression, lineTracker, reader, prev_index);
                continue;
            }
            if (isDigitDeclareStartWith(var_name)) {
                Code c = loadConst0(block, expression, lineTracker, reader, prev_index);
                expression.addToInnerTempListLast(c);
                continue;
            }

            if (isStringDeclare(var_name)) {
                Code c = loadString00(block, expression, lineTracker, reader, prev_index);
                expression.addToInnerTempListLast(c);
                continue;
            }
            if (isCharDeclare(var_name)) {
                Code c = loadChar00(block, expression, lineTracker, reader, prev_index);
                expression.addToInnerTempListLast(c);
                continue;
            }
            if (isString2Declare(var_name)) {
                Code c = loadString20(block, expression, lineTracker, reader, prev_index);
                expression.addToInnerTempListLast(c);
                continue;
            }
            if (isSymbolDeclare(var_name)) {
                Code c = new CTSymbol(var_name).setLine(lineTracker.getLine());
                expression.addToInnerTempListLast(c);
                hasSymbol = true;
                continue;
            }

            if (var_name.equals(Re_CodeSymbol.CODE_VARIABLE_ASSIGNMENT_SYMBOL)) {//=
                Assignment c = Assignment.createAssignment(lineTracker.getLine());
                expression.addToInnerTempListLast(c);
            } else if (var_name.equals(Re_CodeSymbol.CODE_OBJECT_POINT_SYMBOL)) {//.
                Point c = Point.createPoint(lineTracker.getLine());
                expression.addToInnerTempListLast(c);
            } else if (var_name.equals(Re_CodeSymbol.CODE_PARAM_JOIN_SYMBOL)) {//(
                Code lastContent = null == expression ? null : notDiscon(expression.getInnerTempListLast());//上个集合的最后一个
                Call c = new Call();
                c.setLine(lineTracker.getLine());
                if (null != lastContent && codeIsCode(lastContent)) {
                    c.name = lastContent.name;
                    expression.removeInnerTempListCode(lastContent); //删除上一个var 并且将名称设置为方法名
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
                            throw new Re_Exceptions.CompileTimeGrammaticalException("[" + lastContent + "]" + "[" + lastContent.name + "]",
                                    block.getFilePath(), lineTracker.getLine());
                        }
                    }
                }
                c.staticValue = Re_Variable.Unsafe.findVariable(c.name, Re_Keywords.keyword);//将关键字值直接加入代码 以后将不在获取keyword

                expression.addToInnerTempListLast(c);
                readFunctionParam0(block, lineTracker, c, reader);//add param
            } else if (var_name.equals(Re_CodeSymbol.CODE_PARAM_END_SYMBOL)) {//)
                throw new Re_Exceptions.CompileTimeGrammaticalException("[" + var_name + "]",
                        block.getFilePath(), lineTracker.getLine());

            } else if (var_name.equals(Re_CodeSymbol.CODE_MAP_JOIN_SYMBOL)) {//{
                Code lastContent = null == expression ? null : notDiscon(expression.getInnerTempListLast());//上个集合的最后一个
                //以function的方式读取表达式
                Call c = new Call.CallCreateDict();
                if (null != lastContent && codeIsCode(lastContent)) {
                    throw new Re_Exceptions.CompileTimeGrammaticalException("[" + lastContent + "]" + "[" + lastContent.name + "]",
                            block.getFilePath(), lineTracker.getLine());
                } else {
                    if (null == lastContent || null == lastContent.name) {
                        //a = (b);
                        //(b);
                        c.name = Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_OBJECT;
                    } else {
                        throw new Re_Exceptions.CompileTimeGrammaticalException("[" + lastContent + "]" + "[" + lastContent.name + "]",
                                block.getFilePath(), lineTracker.getLine());
                    }
                }
                c.staticValue = Re_Variable.Unsafe.findVariable(c.name, Re_Keywords.keyword);;//将关键字值直接加入代码 以后将不在获取keyword
                c.setLine(lineTracker.getLine());

                expression.addToInnerTempListLast(c);
                readFunctionParam0(block, lineTracker, c, reader);//add param
            } else if (var_name.equals(Re_CodeSymbol.CODE_MAP_END_SYMBOL)) {//}
                throw new Re_Exceptions.CompileTimeGrammaticalException("[" + var_name + "]",
                        block.getFilePath(), lineTracker.getLine());

            } else if (var_name.equals(Re_CodeSymbol.CODE_LIST_JOIN_SYMBOL)) {//[
                Code lastContent = null == expression ? null : notDiscon(expression.getInnerTempListLast());//上个集合的最后一个
                //以function的方式读取表达式
                Call c = new Call.CallCreateList();
                c.name = Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_LIST;
                c.staticValue = Re_Variable.Unsafe.findVariable(c.name, Re_Keywords.keyword);;//将关键字值直接加入代码 以后将不在获取keyword
                c.setLine(lineTracker.getLine());

                expression.addToInnerTempListLast(c);
                readFunctionParam0(block, lineTracker, c, reader);//add param

                if (null != lastContent && codeIsCode(lastContent)) {
                    if (c.getParamExpressionCount() == 0) {
                        expression.removeInnerTempListCode(c);
                        lastContent.name += (Re_CodeSymbol.CODE_LIST_JOIN_SYMBOL + Re_CodeSymbol.CODE_LIST_END_SYMBOL);
                        lastContent.staticValue = Re_Variable.Unsafe.findVariable(lastContent.name, Re_Keywords.keyword);
                    } else {
                        throw new Re_Exceptions.CompileTimeGrammaticalException("[" + lastContent + "]" + "[" + lastContent.name + "]",
                                block.getFilePath(), lineTracker.getLine());
                    }
                }
            } else if (var_name.equals(Re_CodeSymbol.CODE_LIST_END_SYMBOL)) {//]
                throw new Re_Exceptions.CompileTimeGrammaticalException("[" + var_name + "]",
                        block.getFilePath(), lineTracker.getLine());

            } else if (var_name.equals(Re_CodeSymbol.CODE_PARAM_SEPARATOR) || var_name.equals(Re_CodeSymbol.CODE_SEMICOLON_SEPARATOR)) {//; or ,
                expression.addToInnerTempListLast(CTSemicolon.DEFAULT_SEMICOLON);//本段代码结束        这个是没有意义的 后面会删掉
            } else {
                Code c = new Code();
                c.name = var_name;
                c.line = lineTracker.getLine();
                c.staticValue = Re_Variable.Unsafe.findVariable(var_name, Re_Keywords.keyword);//将关键字值直接加入代码 以后将不在获取keyword
                expression.addToInnerTempListLast(c);
            }
//            println(var_name + "<" + cline.line + ">");
        }
        if (hasSymbol) {
            convertExpressionSymbol(block, expression);
        }
        return expression;
    }

    private void readFunctionParam0(Re_CodeFile block, LineTracker lineTracker, Call host, CharCodeReader reader) throws Re_Exceptions.CompileTimeGrammaticalException {
        final char[] lineSeparator = lineSeparatorChars;
        CTExpression expression = null;

        boolean hasSymbol = false;
        String var_name;
        char[] code_chars;
        while (true) {
            int prev_index = reader.getIndex();
            if (null == (code_chars = reader.readNext())) {
                break;
            } else {
                if (Arrays.equals(code_chars, lineSeparator)) {
                    lineTracker.nextLine();
                    continue;
                }
                if ((var_name = new String(code_chars).trim()).length() == 0) {
                    continue;
                }
                var_name = Re_CodeSymbol.intern(var_name);
            }


            if (isNotesDeclare(var_name)) {
                //跳过
                skipNote0(block, expression, lineTracker, reader, prev_index);
                continue;
            }
            if (isDigitDeclareStartWith(var_name)) {
                if (null == expression) {
                    host.addToParamInnerTempListLast(expression = new CTExpression().setLine(lineTracker.getLine()));
                }
                Code c = loadConst0(block, expression, lineTracker, reader, prev_index);
                expression.addToInnerTempListLast(c);
                continue;
            }

            if (isStringDeclare(var_name)) {
                if (null == expression) {
                    host.addToParamInnerTempListLast(expression = new CTExpression().setLine(lineTracker.getLine()));
                }
                Code c = loadString00(block, expression, lineTracker, reader, prev_index);
                expression.addToInnerTempListLast(c);
                continue;
            }
            if (isCharDeclare(var_name)) {
                if (null == expression) {
                    host.addToParamInnerTempListLast(expression = new CTExpression().setLine(lineTracker.getLine()));
                }
                Code c = loadChar00(block, expression, lineTracker, reader, prev_index);
                expression.addToInnerTempListLast(c);
                continue;
            }
            if (isString2Declare(var_name)) {
                if (null == expression) {
                    host.addToParamInnerTempListLast(expression = new CTExpression().setLine(lineTracker.getLine()));
                }
                Code c = loadString20(block, expression, lineTracker, reader, prev_index);
                expression.addToInnerTempListLast(c);
                continue;
            }
            if (isSymbolDeclare(var_name)) {
                if (null == expression) {
                    host.addToParamInnerTempListLast(expression = new CTExpression().setLine(lineTracker.getLine()));
                }
                Code c = new CTSymbol(var_name).setLine(lineTracker.getLine());
                expression.addToInnerTempListLast(c);
                hasSymbol = true;
                continue;
            }

            if (var_name.equals(Re_CodeSymbol.CODE_VARIABLE_ASSIGNMENT_SYMBOL)) {//=
                if (null == expression) {
                    host.addToParamInnerTempListLast(expression = new CTExpression().setLine(lineTracker.getLine()));
                }
                Assignment c = Assignment.createAssignment(lineTracker.getLine());
                expression.addToInnerTempListLast(c);
            } else if (var_name.equals(Re_CodeSymbol.CODE_OBJECT_POINT_SYMBOL)) {//.
                if (null == expression) {
                    host.addToParamInnerTempListLast(expression = new CTExpression().setLine(lineTracker.getLine()));
                }
                Point c = Point.createPoint(lineTracker.getLine());
                expression.addToInnerTempListLast(c);
            } else if (var_name.equals(Re_CodeSymbol.CODE_PARAM_JOIN_SYMBOL)) {//(
                Code lastContent = null == expression ? null : notDiscon(expression.getInnerTempListLast());//上个集合的最后一个
                Call c = new Call();
                c.setLine(lineTracker.getLine());
                if (null != lastContent && codeIsCode(lastContent)) {
                    c.name = lastContent.name;
                    expression.removeInnerTempListCode(lastContent); //删除上一个var 并且将名称设置为方法名
                } else {
                    if (null == lastContent || null == lastContent.name) {
                        //a = (b);
                        //(b);
                        c.name = "";
                    } else {
                        if (codeIsCall(lastContent)) {
                            ////field(()())  > 第二个(
                            c.name = "";
                        } else {
                            throw new Re_Exceptions.CompileTimeGrammaticalException("[" + lastContent + "]" + "[" + lastContent.name + "]",
                                    block.getFilePath(), lineTracker.getLine());
                        }
                    }
                }
                c.staticValue = Re_Variable.Unsafe.findVariable(c.name, Re_Keywords.keyword);//将关键字值直接加入代码 以后将不在获取keyword

                if (null == expression) {
                    host.addToParamInnerTempListLast(expression = new CTExpression().setLine(lineTracker.getLine()));
                }
                expression.addToInnerTempListLast(c);
                readFunctionParam0(block, lineTracker, c, reader);//add param
            } else if (var_name.equals(Re_CodeSymbol.CODE_PARAM_END_SYMBOL)) {//)
                break;

            } else if (var_name.equals(Re_CodeSymbol.CODE_MAP_JOIN_SYMBOL)) {//{
                Code lastContent = null == expression ? null : notDiscon(expression.getInnerTempListLast());//上个集合的最后一个
                //以function的方式读取表达式
                Call c = new Call.CallCreateDict();
                if (null != lastContent && codeIsCode(lastContent)) {
                    throw new Re_Exceptions.CompileTimeGrammaticalException("[" + lastContent + "]" + "[" + lastContent.name + "]",
                            block.getFilePath(), lineTracker.getLine());
                } else {
                    if (null == lastContent || null == lastContent.name) {
                        //a = (b);
                        //(b);
                        c.name = Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_OBJECT;
                    } else {
                        throw new Re_Exceptions.CompileTimeGrammaticalException("[" + lastContent + "]" + "[" + lastContent.name + "]",
                                block.getFilePath(), lineTracker.getLine());
                    }
                }
                c.staticValue = Re_Variable.Unsafe.findVariable(c.name, Re_Keywords.keyword);//将关键字值直接加入代码 以后将不在获取keyword
                c.setLine(lineTracker.getLine());

                if (null == expression) {
                    host.addToParamInnerTempListLast(expression = new CTExpression().setLine(lineTracker.getLine()));
                }
                expression.addToInnerTempListLast(c);
                readFunctionParam0(block, lineTracker, c, reader);//add param
            } else if (var_name.equals(Re_CodeSymbol.CODE_MAP_END_SYMBOL)) {//}
                if (Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_OBJECT.equals(host.getName())) {
                    break;
                } else {
                    throw new Re_Exceptions.CompileTimeGrammaticalException("[" + var_name + "]",
                            block.getFilePath(), lineTracker.getLine());
                }
            } else if (var_name.equals(Re_CodeSymbol.CODE_LIST_JOIN_SYMBOL)) {//[
                Code lastContent = null == expression ? null : notDiscon(expression.getInnerTempListLast());//上个集合的最后一个
                Call c = new Call.CallCreateList();
                c.name = Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_LIST;
                c.staticValue = Re_Variable.Unsafe.findVariable(c.name, Re_Keywords.keyword);//将关键字值直接加入代码 以后将不在获取keyword
                c.setLine(lineTracker.getLine());

                if (null == expression) {
                    host.addToParamInnerTempListLast(expression = new CTExpression().setLine(lineTracker.getLine()));
                }
                expression.addToInnerTempListLast(c);
                readFunctionParam0(block, lineTracker, c, reader);//add param

                if (null != lastContent && codeIsCode(lastContent)) {
                    if (c.getParamExpressionCount() == 0) {
                        expression.removeInnerTempListCode(c);
                        lastContent.name += (Re_CodeSymbol.CODE_LIST_JOIN_SYMBOL + Re_CodeSymbol.CODE_LIST_END_SYMBOL);
                        lastContent.staticValue = Re_Variable.Unsafe.findVariable(lastContent.name, Re_Keywords.keyword);
                    } else {
                        throw new Re_Exceptions.CompileTimeGrammaticalException("[" + lastContent + "]" + "[" + lastContent.name + "]",
                                block.getFilePath(), lineTracker.getLine());
                    }
                }
            } else if (var_name.equals(Re_CodeSymbol.CODE_LIST_END_SYMBOL)) {//]
                if (Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_LIST.equals(host.getName())) {
                    break;
                } else {
                    throw new Re_Exceptions.CompileTimeGrammaticalException("[" + var_name + "]",
                            block.getFilePath(), lineTracker.getLine());
                }
            } else if (var_name.equals(Re_CodeSymbol.CODE_PARAM_SEPARATOR) || var_name.equals(Re_CodeSymbol.CODE_SEMICOLON_SEPARATOR)) {//  ',' or ';' 最终都不会添加到代码里
                convertExpressionSymbol(block, expression);
                expression = null;
                hasSymbol = false;
                continue;
            } else {
                if (null == expression) {
                    host.addToParamInnerTempListLast(expression = new CTExpression().setLine(lineTracker.getLine()));
                }
                Code c = new Code();
                c.name = var_name;
                c.line = lineTracker.getLine();
                c.staticValue = Re_Variable.Unsafe.findVariable(var_name, Re_Keywords.keyword);//将关键字值直接加入代码 以后将不在获取keyword

                expression.addToInnerTempListLast(c);
            }
//          println(var_name + "<" + cline.line + ">");
        }
        if (hasSymbol) {
            convertExpressionSymbol(block, expression);
        }
    }
    private CTExpression[] splitCode(CTExpression codeblock) {
        List<CTExpression> result = new ArrayList<>();
        CTExpression exp = new CTExpression();
        try {
            List<Code> list = codeblock.getInnerTempList();

            for (Code code_ : list) {
                if (codeIsCTSemicolon(code_)) {
                    if (exp.getInnerTempListSize() != 0) {
                        exp.setLineFromFirstElement();
                        result.add(exp);

                        exp = new CTExpression();
                    }
                } else {
                    exp.addToInnerTempListLast(code_);
                }
            }
        } finally {
            if (exp.getInnerTempListSize() != 0) {
                exp.setLineFromFirstElement();

                result.add(exp);
            }
        }
        return result.toArray(CTExpression.EMPTY_EXPRESSION);
    }














    static Code loadConst0(Re_CodeFile block, CTExpression ctExpression, LineTracker lineTracker, CharCodeReader lineCharCodeReader, int str_prev_index) throws Re_Exceptions.CompileTimeGrammaticalException {
        String prefix = "";
        Code result;
        int stLine       = lineTracker.getLine();
        int elementCount = ctExpression.getInnerTempListSize();

        //获取前缀符号
        Code prev_element = ctExpression.getInnerTempListElement(elementCount - 1); //倒数第一个
        if (codeIsCTSymbol(prev_element)) {
            if (prev_element.equalsName("+") || prev_element.equalsName("-")) {
                prefix = prev_element.getName();
            }
            if (null == notDiscon(ctExpression.getInnerTempListElement(elementCount - 2))) {//倒数第二个
                if (prefix.isEmpty()) {
                    throw new Re_Exceptions.CompileTimeGrammaticalException(Code.getExpressionAsString(block, ctExpression),
                            block.getFilePath(), lineTracker.getLine());
                }
                ctExpression.removeInnerTempListCode(prev_element);//删除最后一个符号
            } else {
                prefix = "";
            }
        }

        char[] buffer = lineCharCodeReader.buffer();
        int count     = lineCharCodeReader.size();
        int end = str_prev_index + 1;
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
        char type = end < count ? Character.toUpperCase(buffer[end]) : '\0';
        String content = prefix + new String(lineCharCodeReader.buffer(), str_prev_index, end - str_prev_index);

        //__println("prev_index=" + prev_index + ", " + "end=" + end);
        //__println("content=" + content);
        //__println("type=" + type);

        boolean next = true;
        if (type == 'L')      result = block.createConst(ctExpression, stLine, Long.parseLong(content));
        else if (type == 'F') result = block.createConst(ctExpression, stLine, Float.parseFloat(content));
        else if (type == 'D') result = block.createConst(ctExpression, stLine, Double.parseDouble(content));
        else if (type == 'S') result = block.createConst(ctExpression, stLine, Short.parseShort(content));
        else if (type == 'B') result = block.createConst(ctExpression, stLine, Byte.parseByte(content));
        else if (type == 'C') result = block.createConst(ctExpression, stLine, (char)Integer.parseInt(content));
        else {
            //未知的下一个字符类型 直接跳过这个不跳过这个字符
            if (haspoint) {
                result = block.createConst(ctExpression, stLine, Double.parseDouble(content));
            } else {
                long javavalue = Long.parseLong(content);
                if ((int)javavalue == javavalue) {
                    result = block.createConst(ctExpression, stLine, (int)javavalue);
                } else {
                    result = block.createConst(ctExpression, stLine, javavalue);
                }
            }
            next = false;
        }
        int index = end + (next ? 1 : 0);

        //__println("seek: " + index);
        lineCharCodeReader.seekIndex(index);

        return result;
    }


    static Code loadString00(Re_CodeFile block, CTExpression ctExpression, LineTracker lineTracker, CharCodeReader lineCharCodeReader, int str_prev_index) throws Re_Exceptions.CompileTimeGrammaticalException {
        char symbol   = Re_CodeSymbol.CODE_STRING_START_CHAR;
        char[] buffer = lineCharCodeReader.buffer();
        int count     = lineCharCodeReader.size();
        int stLine    = lineTracker.getLine();

        CharArrayWriters out = new CharArrayWriters(0);
        CharArrayWriters unicode = new CharArrayWriters(4);
        boolean hadSlash = false;
        boolean inUnicode = false;
        int end = str_prev_index + 1;
        for (; end < count; end++) {
            char ch = buffer[end];
            if (ch == lineSeparatorChar) {
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
                        throw new Re_Exceptions.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+" " +"Unable to parse unicode tip: " + unicode +": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, end-str_prev_index) + String.valueOf(symbol))
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
                // //__println("@" + element + "@" + joinStringLineCount + "@");
                // //__println("------");
                end++;
                lineCharCodeReader.seekIndex(end);

                Code aConst = block.createConst(ctExpression, stLine, element);
                return aConst;
            } else {
                out.write(ch);
            }
        }
        throw new Re_Exceptions.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+" " +"error string declaration"+ ": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, end-str_prev_index) + String.valueOf(symbol)),
                block.getFilePath(), stLine);
    }

    static Code loadChar00(Re_CodeFile block, CTExpression ctExpression, LineTracker lineTracker, CharCodeReader lineCharCodeReader, int str_prev_index) throws Re_Exceptions.CompileTimeGrammaticalException {
        char symbol = Re_CodeSymbol.CODE_CHAR_START_CHAR;
        char[] buffer = lineCharCodeReader.buffer();
        int count     = lineCharCodeReader.size();
        int stLine    = lineTracker.getLine();

        CharArrayWriters out = new CharArrayWriters(0);
        CharArrayWriters unicode = new CharArrayWriters(4);
        boolean hadSlash = false;
        boolean inUnicode = false;
        int end = str_prev_index + 1;
        for (; end < count; end++) {
            char ch = buffer[end];
            if (ch == lineSeparatorChar) {
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
                        throw new Re_Exceptions.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+" "+ "Unable to parse unicode tip: [" + unicode +"] "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, end-str_prev_index) + String.valueOf(symbol)),
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
                    throw new Re_Exceptions.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+" " +"error char declaration"+ ": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, end-str_prev_index) + String.valueOf(symbol)),
                            block.getFilePath(), stLine);
                }
                // //__println("@" + contnet + "@" + joinStringLineCount + "@");
                // //__println("------");
                end++;
                lineCharCodeReader.seekIndex(end);

                char c1 = contnet.charAt(0);
                Code aConst = block.createConst(ctExpression, stLine, c1);
                return aConst;
            } else {
                out.write(ch);
            }
        }
        throw new Re_Exceptions.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+" " +"error string declaration"+ ": "+ (String.valueOf(symbol) + new String(buffer, str_prev_index, end-str_prev_index) + Strings.cast(symbol)),
                block.getFilePath(), stLine);
    }
    // ''
    static Code loadString20(Re_CodeFile block, CTExpression ctExpression, LineTracker lineTracker, CharCodeReader lineCharCodeReader, int str_prev_index) throws Re_Exceptions.CompileTimeGrammaticalException {
        char[] symbol = Re_CodeSymbol.CODE_STRING2_START_CHARS;
        char[] buffer = lineCharCodeReader.buffer();
        int count     = lineCharCodeReader.size();
        int stLine    = lineTracker.getLine();

        CharArrayWriters out = new CharArrayWriters(0);
        CharArrayWriters unicode = new CharArrayWriters(4);
        boolean hadSlash = false;
        boolean inUnicode = false;
        int end = str_prev_index + symbol.length;
        for (; end < count; end++) {
            char ch = buffer[end];
            if  (ch == lineSeparatorChar) {
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
                        throw new Re_Exceptions.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+" "+ "Unable to parse unicode tip: [" + unicode +"] "+ new String(buffer, str_prev_index, end - str_prev_index),
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
            if (ch == symbol[0] && (buffer[end+1] == symbol[1])) {
                // String element
                String element = out.toString();
                // //__println("@" + element + "@" + joinStringLineCount + "@");
                // //__println("------");
                end += symbol.length;
                lineCharCodeReader.seekIndex(end);

                Code aConst = block.createConst(ctExpression, stLine, element);
                return aConst;
            } else {
                out.write(ch);
            }
        }
        throw new Re_Exceptions.CompileTimeGrammaticalException("[line:"+stLine+"-"+lineTracker.getLine()+"]"+" "+ "unable to parse string2 tip: [" + out +"] "+ new String(buffer, str_prev_index, end - str_prev_index),
                block.getFilePath(), stLine);
    }

    static void skipNote0(Re_CodeFile block, CTExpression ctExpression, LineTracker lineTracker, CharCodeReader lineCharCodeReader, int str_prev_index) throws Re_Exceptions.CompileTimeGrammaticalException {
        char[] buffer = lineCharCodeReader.buffer();
        int count = lineCharCodeReader.size();
        int end = count;
        for (int i = str_prev_index; i < count; i++) {
            char c1 = buffer[i];
            if (c1 == lineSeparatorChar) {
                end = i;
                break;
            }
        }
        lineCharCodeReader.seekIndex(end);
    }











    static void convertExpressionSymbol(Re_CodeFile block, CTExpression expression) throws Re_Exceptions.CompileTimeGrammaticalException {
        convertExpressionSymbolAsCallSymbol0(block, new CompileTimeCodeListEditor(block, expression.getInnerTempList()));
    }
    //自动符号转方法
    static void convertExpressionSymbolAsCallSymbol0(Re_CodeFile block, CompileTimeCodeListEditor reader) throws Re_Exceptions.CompileTimeGrammaticalException {
        //算数表达式转方法 1+1 >> +(1, 1)
        //println(reader.size());
        int symbolCount = 0;
        for (Code currentCode; reader.hasNext();) {
            //将符号转为function  比如 6 +7 转换为 +(6,7)
            if (null != (currentCode = reader.next())) {
                if (codeIsCTSymbol(currentCode)) {
                    convertExpressionSymbolAsCallSymbol1(block, reader, (CTSymbol) currentCode);
                    symbolCount++;
                }
            }
        }
        if (symbolCount > 0) {
            reader.removeNull();
        }
        //__println(Expression.formatCodeFromRoot(that, 0, that.size()));
    }

    /**
     *
     * @param block         代码块
     * @param reader        读取器
     * @param currentCode   元素指针必须等于 {@link  CompileTimeCodeListEditor#index()}
     * @throws Re_Exceptions.CompileTimeGrammaticalException 表达式异常
     */
    static Call.CallSymbol convertExpressionSymbolAsCallSymbol1(Re_CodeFile block, CompileTimeCodeListEditor reader, CTSymbol currentCode) throws Re_Exceptions.CompileTimeGrammaticalException {
//        __println("*index=" + reader.index() + ", var=" + currentCode.getInstanceName() + ", type=" + currentCode.getClass());
        ArrList<Code> that = reader.list;

        int current = reader.index();
//        __println("*current: " + reader.now()); //
        int prev = reader.findSymbolPrevOverallStartIndex(current);
        int end  = reader.findSymbolNextOverallEndIndex(current);
//        __println("*find_prev_overall_offset: " + prev);
//        __println("*find_next_overall_offset: " + end);
        if (prev == -1 || end == -1) {
            int st = prev == -1 ? current : prev;
            int len = end == -1 ? current - prev : end - prev;

            throw new Re_Exceptions.CompileTimeGrammaticalException("[" + CTExpression.getExpressionAsString(block, that, st, len + 1) + "]" + "["+((prev == -1 ? "no-prev-element":"") + " " + (end == -1 ? "no-next-element":"")).trim()+"]",
                    block.getFilePath(), currentCode.getLine());
        }
        String name = currentCode.getName();

        Call.CallSymbol call = new Call.CallSymbol();
        call.setName(name);
        call.setLine(currentCode.getLine());
        call.staticValue = Re_Variable.Unsafe.findVariable(name, Re_Keywords.keyword);//将关键字值直接加入代码 以后将不在获取keyword

        CTExpression prev_expression = new CTExpression();
        prev_expression.setLine(currentCode.getLine());
        for (int i = prev; i < current; i++) {
            Code s = that.get(i);
            if (null != s) {
//                __println("prev: " + s.getInstanceName());
                that.set(i, null);
                prev_expression.addToInnerTempListLast(s);
            }
        }
        call.addToParamInnerTempListLast(prev_expression);

//        __println("symbol: " + currentCode.getInstanceName());
        that.set(current, null);

        CTExpression next_expression = new CTExpression();
        next_expression.setLine(currentCode.getLine());
        for (int i = current + 1; i <= end; i++) {
            Code s = that.get(i);
            if (null != s) {
//                __println("next: " + s.getInstanceName());
                that.set(i, null);
                next_expression.addToInnerTempListLast(s);
            }
        }
        call.addToParamInnerTempListLast(next_expression);

//        __println(TabPrint.wrap(that.toArray()));
        that.set(end, call);
//        __println(TabPrint.wrap(that.toArray()));
//        __println(Code.getExpressionAsString(block, that));
//        __println("----");
        return call;
    }














    static boolean log = true;
    public static void __println(Object content) {
        if (log)
            System.out.println(content);
    }











    @SuppressWarnings("StatementWithEmptyBody")
    public static class CompileTimeCodeSourceReader {
        Re_CodeFile block;
        LineTracker lineTracker;
        CharCodeReader reader;
        public CompileTimeCodeSourceReader(Re_CodeFile block, LineTracker lineTracker, CharCodeReader reader) {
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









        public CTExpression[] readAllExpression() throws Re_Exceptions.CompileTimeGrammaticalException {
            List<CTExpression> expressionList = new ArrayList<>();
            while (reader.hasNext()) {
                CTExpression expression;
                if (null == (expression = this.readExpression()))
                    continue;
                expressionList.add(expression);
            }
            return expressionList.toArray(CTExpression.EMPTY_EXPRESSION);
        }

        public CTExpression readExpression() throws Re_Exceptions.CompileTimeGrammaticalException {
            CTExpression expression = new CTExpression();
            while (reader.hasNext()) {
                int s = this.readNextSection(expression);
                if (isFlag(s)) {
                    if (isFlagSemicolonOrParamSeparator(s)) {
                        // semicolon
                        break;
                    } else {
                        throw new Re_Exceptions.CompileTimeGrammaticalException("[error-flag]",
                                block.getFilePath(), lineTracker.getLine());
                    }
                }
            }
            if (expression.getInnerTempListSize() == 0) {
                return null;
            } else {
                expression.setLineFromFirstElement();
            }
            return expression;
        }

        /**
         * 读取一个连续的表达式子 遇到 ; symbol = 会断开
         * 可以返回flag 获取读取的数量， 读取的数量不一定是准确的 也可以是0
         */
        public int readNextSection(CTExpression toExpression) throws Re_Exceptions.CompileTimeGrammaticalException {
            boolean hasSymbol = false;
            char[]  chars;
            String  var_name;
            int     count = 0;
            try {
                for (;;) {
                    int prev_index = reader.getIndex();
                    if (null == (chars = reader.readNext())) {
                        break;
                    } else {
                        if (Arrays.equals(chars, lineSeparatorChars)) {
                            lineTracker.nextLine();
                            continue;
                        }
                        if ((var_name = new String(chars).trim()).length() == 0) {
                            continue;
                        }
                        var_name = Re_CodeSymbol.intern(var_name);
                    }


                    if (isStringDeclare(var_name)) {
                        Code c = loadString00(block, toExpression, lineTracker, reader, prev_index);
                        toExpression.addToInnerTempListLast(c);
                        count++;
                        continue;
                    }
                    if (isCharDeclare(var_name)) {
                        Code c = loadChar00(block, toExpression, lineTracker, reader, prev_index);
                        toExpression.addToInnerTempListLast(c);
                        count++;
                        continue;
                    }
                    if (isString2Declare(var_name)) {
                        Code c = loadString20(block, toExpression, lineTracker, reader, prev_index);
                        toExpression.addToInnerTempListLast(c);
                        count++;
                        continue;
                    }


                    if (isNotesDeclare(var_name)) {
                        //跳过
                        skipNote0(block, toExpression, lineTracker, reader, prev_index);
                        continue;
                    }
                    if (isDigitDeclareStartWith(var_name)) {
                        Code c = loadConst0(block, toExpression, lineTracker, reader, prev_index);
                        toExpression.addToInnerTempListLast(c);
                        count++;
                        continue;
                    }
                    if (isSymbolDeclare(var_name)) {    //symbol
                        Code c = new CTSymbol(var_name).setLine(lineTracker.getLine());
                        toExpression.addToInnerTempListLast(c);
                        hasSymbol = true;
                        continue;
                    }

                    if (var_name.equals(Re_CodeSymbol.CODE_VARIABLE_ASSIGNMENT_SYMBOL)) {//=
                        Assignment c = Assignment.createAssignment(lineTracker.getLine());
                        toExpression.addToInnerTempListLast(c);
                        count++;
                        continue;
                    } else if (var_name.equals(Re_CodeSymbol.CODE_OBJECT_POINT_SYMBOL)) {//.
                        Point c = Point.createPoint(lineTracker.getLine());
                        toExpression.addToInnerTempListLast(c);
                        count++;
                        continue;
                    } else if (var_name.equals(Re_CodeSymbol.CODE_PARAM_JOIN_SYMBOL)) {//(
                        Code lastContent = null == toExpression ? null : notDiscon(toExpression.getInnerTempListLast());//上个集合的最后一个

                        Call c = new Call();
                        c.setLine(lineTracker.getLine());
                        if (null != lastContent && codeIsCode(lastContent)) {
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
                                    throw new Re_Exceptions.CompileTimeGrammaticalException("[ " + lastContent + "(...) ]" ,
                                            block.getFilePath(), lineTracker.getLine());
                                }
                            }
                        }
                        c.staticValue = Re_Variable.Unsafe.findVariable(c.name, Re_Keywords.keyword);//将关键字值直接加入代码 以后将不在获取keyword

                        CTExpression exp = new CTExpression();
                        try {
                            while (reader.hasNext()) {
                                int s;
                                if (isFlag(s = readNextSection(exp))) {
                                    if (isFlagExit(s)) {
                                        if (isFlagExitCall(s)) {
                                            break;
                                        } else {
                                            throw new Re_Exceptions.CompileTimeGrammaticalException("[ (...? ]",
                                                    block.getFilePath(), lineTracker.getLine());
                                        }
                                    } else if (isFlagSemicolonOrParamSeparator(s)) {
                                        //pass
                                    }
                                }
                                if (exp.getInnerTempListSize() != 0) {
                                    exp.setLineFromFirstElement();
                                    c.addToParamInnerTempListLast(exp);

                                    exp = new CTExpression();
                                }
                            }
                        } finally {
                            if (exp.getInnerTempListSize() != 0) {
                                exp.setLineFromFirstElement();
                                c.addToParamInnerTempListLast(exp);
                            }
                        }
                        toExpression.addToInnerTempListLast(c);
                        count++;
                        continue;
                    } else if (var_name.equals(Re_CodeSymbol.CODE_PARAM_END_SYMBOL)) {//)
                        return FLAG_EXIT_CALL;
                    } else if (var_name.equals(Re_CodeSymbol.CODE_MAP_JOIN_SYMBOL)) {//{
                        Call c = new Call.CallCreateDict();
                        c.name = Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_OBJECT;
                        c.staticValue = Re_Variable.Unsafe.findVariable(c.name, Re_Keywords.keyword);;//将关键字值直接加入代码 以后将不在获取keyword
                        c.setLine(lineTracker.getLine());

                        CTExpression exp = new CTExpression();
                        try {
                            while (reader.hasNext()) {
                                int s;
                                if (isFlag(s = readNextSection(exp))) {
                                    if (isFlagExit(s)) {
                                        if (isFlagExitDict(s)) {
                                            break;
                                        } else {
                                            throw new Re_Exceptions.CompileTimeGrammaticalException("[ {...? ]",
                                                    block.getFilePath(), lineTracker.getLine());
                                        }
                                    } else if (isFlagSemicolonOrParamSeparator(s)) {
                                        //pass
                                    }
                                }
                                if (exp.getInnerTempListSize() != 0) {
                                    exp.setLineFromFirstElement();
                                    c.addToParamInnerTempListLast(exp);

                                    exp = new CTExpression();
                                }
                            }
                        } finally {
                            if (exp.getInnerTempListSize() != 0) {
                                exp.setLineFromFirstElement();
                                c.addToParamInnerTempListLast(exp);
                            }
                        }
                        toExpression.addToInnerTempListLast(c);
                        count++;
                        continue;
                    } else if (var_name.equals(Re_CodeSymbol.CODE_MAP_END_SYMBOL)) {//}
                        return FLAG_EXIT_DICT;
                    } else if (var_name.equals(Re_CodeSymbol.CODE_LIST_JOIN_SYMBOL)) {//[
                        Call c = new Call.CallCreateList();
                        c.name = Re_Keywords.INNER_CONVERT_FUNCTION__CREATE_LIST; //_list();
                        c.staticValue = Re_Variable.Unsafe.findVariable(c.name, Re_Keywords.keyword);;//将关键字值直接加入代码 以后将不在获取keyword
                        c.setLine(lineTracker.getLine());

                        CTExpression exp = new CTExpression();
                        try {
                            while (reader.hasNext()) {
                                int s;
                                if (isFlag(s = readNextSection(exp))) {
                                    if (isFlagExit(s)) {
                                        if (isFlagExitList(s)) {
                                            break;
                                        } else {
                                            throw new Re_Exceptions.CompileTimeGrammaticalException("[ [...? ]",
                                                    block.getFilePath(), lineTracker.getLine());
                                        }
                                    } else if (isFlagSemicolonOrParamSeparator(s)) {
                                        //pass
                                    }
                                }
                                if (exp.getInnerTempListSize() != 0) {
                                    exp.setLineFromFirstElement();
                                    c.addToParamInnerTempListLast(exp);

                                    exp = new CTExpression();
                                }
                            }
                        } finally {
                            if (exp.getInnerTempListSize() != 0) {
                                exp.setLineFromFirstElement();
                                c.addToParamInnerTempListLast(exp);
                            }
                        }

                        Code lastContent = null == toExpression ? null : notDiscon(toExpression.getInnerTempListLast());//上个集合的最后一个
                        if (null != lastContent && codeIsCode(lastContent)) {
                            if (c.getParamExpressionCount() == 0) {
                                lastContent.name += (Re_CodeSymbol.CODE_LIST_JOIN_SYMBOL + Re_CodeSymbol.CODE_LIST_END_SYMBOL);
                                lastContent.staticValue = Re_Variable.Unsafe.findVariable(lastContent.name, Re_Keywords.keyword);
                                continue;
                            }
                        }

                        toExpression.addToInnerTempListLast(c);
                        count++;
                        continue;
                    } else if (var_name.equals(Re_CodeSymbol.CODE_LIST_END_SYMBOL)) {//]
                        return FLAG_EXIT_LIST;
                    } else if (var_name.equals(Re_CodeSymbol.CODE_SEMICOLON_SEPARATOR) )    {//;
                        return FLAG_SEMICOLON;
                    } else if (var_name.equals(Re_CodeSymbol.CODE_PARAM_SEPARATOR)) {//,
                        return FLAG_PARAM_SEPARATOR;
                    } else {
                        Code c = new Code();
                        c.name = var_name;
                        c.line = lineTracker.getLine();
                        c.staticValue = Re_Variable.Unsafe.findVariable(var_name, Re_Keywords.keyword);//将关键字值直接加入代码 以后将不在获取keyword
                        toExpression.addToInnerTempListLast(c);
                        count++;
                        continue;
                    }
                    // println(var_name + "<" + cline.line + ">");
                }
                return count;
            } finally {
                CompileTimeCodeListEditor compileTimeCodeListEditor;

                compileTimeCodeListEditor = this.getLastEditorAndResetIndex(block, toExpression);
                //Interceptors
                while (compileTimeCodeListEditor.hasNext()) {
                    Code current = compileTimeCodeListEditor.next();
                    FilterInterceptor filterInterceptor;
                    if (null != (filterInterceptor = getFilterInterceptor(current))) {
                        filterInterceptor.intercept(compileTimeCodeListEditor, current);
                    }
                }

                //Convert Expression Symbol As SymbolCall
                if (hasSymbol) {
                    compileTimeCodeListEditor = this.getLastEditorAndResetIndex(block, toExpression);
                    convertExpressionSymbolAsCallSymbol0(block, compileTimeCodeListEditor);
                }
            }
        }


        /**
         * 转换符号，和过滤拦截器等
         * 一个读取器 只有一个实例
         * 不可同时进行编辑
         */
        protected CompileTimeCodeListEditor compileTimeCodeListEditor = new CompileTimeCodeListEditor();
        protected CompileTimeCodeListEditor getLastEditorAndResetIndex(Re_CodeFile block, CTExpression expression) {
            compileTimeCodeListEditor.block = block;
            compileTimeCodeListEditor.list  = expression.getInnerTempList();
            compileTimeCodeListEditor.resetIndex();
            return compileTimeCodeListEditor;
        }

        @SuppressWarnings("UnnecessaryModifier")
        static interface FilterInterceptor {
            /**
             * 加入你需要删除元素可以直接setNull即可
             */
            public void intercept(CompileTimeCodeListEditor compileTimeCodeListEditor, Code current) throws Re_Exceptions.CompileTimeGrammaticalException;
        }
        static FilterInterceptor getFilterInterceptor(Code next) {
            return interceptors.get(next);
        }
        static void addFilterInterceptor(Code next, FilterInterceptor filterInterceptor) {
            FilterInterceptor last;
            if (null == (last = interceptors.get(next)))
                interceptors.put(next, filterInterceptor);
            else
                throw new Re_Exceptions.CompileTimeGrammaticalException("already filter interceptor: " + last);
        }


        static public class FunCall extends Call {
            String   functionName;
            String[] functionParams;

            public int      getParamCount() {
                if (null != functionParams)
                    return  functionParams.length;;
                return 0;
            }
            public String   getParamName(int index) {
                return functionParams[index];
            }
        }
        /**
         *
         */
        static protected final Map<Code, FilterInterceptor> interceptors = new HashMap<>();
        static {
            {
                final Code funCall = new Call().setName("fun");
                addFilterInterceptor(funCall, new FilterInterceptor() {
                    final String help = "use " + funCall.getName() + "() { ... }";
                    @Override
                    public void intercept(CompileTimeCodeListEditor compileTimeCodeListEditor, Code current) throws Re_Exceptions.CompileTimeGrammaticalException {
                        Code next = compileTimeCodeListEditor.getNext();
                        if (Call.codeIsCallCreateDict(next)) {
                        }
                        throw new Re_Exceptions.CompileTimeGrammaticalException(
                                Code.getExpressionAsString(compileTimeCodeListEditor.block, compileTimeCodeListEditor.list) + " >> " + help,
                                compileTimeCodeListEditor.block.getFilePath(), current.getLine());
                    }
                });
                Code funCode = new Code().setName("fun");
                addFilterInterceptor(funCode, new FilterInterceptor() {
                    @Override
                    public void intercept(CompileTimeCodeListEditor compileTimeCodeListEditor, Code current) throws Re_Exceptions.CompileTimeGrammaticalException {
                        Code next = compileTimeCodeListEditor.getNext();
                        if (Call.codeIsCallCreateDict(next)) {
                            System.out.println();
                        }
                    }
                });
            }
        }
    }


    static class CompileTimeUtils{

    }




    public static class CompileTimeCodeListEditor {
        private Re_CodeFile block;
        private ArrList<Code> list;
        private int index = -1;

        public CompileTimeCodeListEditor(Re_CodeFile block, ArrList<Code> now) {
            this.block = block;
            this.list  = now;
        }
        private CompileTimeCodeListEditor() { }


        /**
         * 获取上一段开始指针, 当前必须是一个整体
         */
        public int findSymbolPrevOverallStartIndex(int index) {
            Code current = list.get(index);
            //当前是符号 符号不能连接比如 - - -
            if (codeIsCTSymbol(current)) {
                for (int i = index - 1; i >= -1; i--) {
                    if (i == -1) {
                        return i + 1 == index ? -1 : i + 1;
                    }
                    Code c = list.get(i);
                    if (codeIsDiscon(c)) {
                        return i + 1 == index ? -1 : i + 1;
                    }
                }
                return -1;
            }
            throw new Re_Exceptions.CompileTimeGrammaticalException("unsupported current-type: " + current.getClass());
        }
        /**
         * 获取下一段结束指针， 当前必须是一个整体
         */
        public int findSymbolNextOverallEndIndex(int index) {
            Code current = list.get(index);
            //当前是符号 符号不能连接比如 - - -
            if (codeIsCTSymbol(current)) {
                for (int i = index + 1; i <= size(); i++) {
                    if (i == size()) {
                        return i - 1 == index ? -1 : i - 1;
                    }
                    Code c = list.get(i);
                    if (codeIsDiscon(c)) {
                        return i - 1 == index ? -1 : i - 1;
                    }
                }
                return -1;
            }
            throw new Re_Exceptions.CompileTimeGrammaticalException("unsupported current-type: " + current.getClass());
        }


        /**
         * 其实可能不需要Gc工作
         */
        public void removeNull() {
            int set = 0;
            ArrList<Code> that = this.list;
            for (Code code: that)
                if (null != code)
                    that.set(set++, code);
            that.setSize(set);
        }

        public void setNull(int index) {
            this.list.set(index, null);
        }

        public boolean isNull(int index) {
            return null == this.list.get(index);
        }

        public Code get(int index) {
            return this.list.get(index);
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


        public boolean no() {
            return -1 == index;
        }

        public int size() {
            return this.list.size();
        }

        public Code now() {
            return index < list.size() ? list.get(index) : null;
        }
        public boolean isLast() {
            return null == this.getNext();
        }



        public boolean hasNext() {
            return list.size() > index + 1;
        }
        public Code getNext() {
            return list.get(index + 1);
        }
        public Code next() {
            return list.get(++index);
        }
    }

    /**
     * line of var tracker
     */
    private static class LineTracker {
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

    public static class RuntimeUtils {

        static String formatClassName(String name) {
            if (null == name)
                return null;
            if (name.length() == 0)
                return null;

            int st = 0;
            int len = name.length();
            for (int i = 0; i < len; i++) {
                char ch = name.charAt(i);
                if (ch == Filex.NAME_CURRENT_DIRECTORY) {
                    st++;
                } else {
                    A:{
                        for (char c : FILE_SEPARATOR) {
                            if (ch == c) {
                                st++;
                                break A;
                            }
                        }
                        break;
                    }
                }
            }
            if (st != 0) {
                return name.substring(st, name.length());
            }
            return name;
        }

        /**
         * @param path 文件路径 支持 \ / 扩展名
         * @return 将文件路径转换为类名
         */
        public static String pathToClassName(String path) {
            if ((null == path) || path.length() == 0) {
                return null;
            }

            int len = path.length();
            String extendName = Filex.getExtensionName(path, FILE_EXTENSION_SYMBOL);
            if (null != extendName) {
                len = len - extendName.length() - FILE_EXTENSION_SYMBOL.length();
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < len; i++){
                char ch = path.charAt(i);
                boolean sep = false;
                for (char c : FILE_SEPARATOR) {
                    if (ch == c){
                        sep = true;
                        break;
                    }
                }
                if (sep) {
                    int b_length = stringBuilder.length();
                    if (b_length != 0) {
                        char last = stringBuilder.charAt(b_length - 1);
                        if  (last != ch) {
                            stringBuilder.append(PACKAGE_SEPARATOR);
                        }
                    }
                } else {
                    if (ch == PACKAGE_SEPARATOR) {
                        throw new UnsupportedOperationException("invalid name: " + path+" "+" "+"["+i+"]");
                    }
                    stringBuilder.append(ch);
                }
            }
            return stringBuilder.toString();
        }

        public static String classNameToPath(String name) {
            if ((null == name) || name.length() == 0) {
                return null;
            }

            int len = name.length();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < len; i++){
                char ch = name.charAt(i);
                boolean sep = false;
                for (char c : FILE_SEPARATOR) {
                    if (ch == c){
                        sep = true;
                        break;
                    }
                }
                if (sep) {
                    throw new UnsupportedOperationException("invalid name: " + name+" "+" "+"["+i+"]");
                } else {
                    if (ch == PACKAGE_SEPARATOR) {
                        stringBuilder.append(Filex.system_separator);
                        continue;
                    }
                    stringBuilder.append(ch);
                }
            }
            return stringBuilder
                    .append(FILE_EXTENSION_SYMBOL).append(RE_FILE_EXTENSION_NAME).toString();
        }
    }


}

