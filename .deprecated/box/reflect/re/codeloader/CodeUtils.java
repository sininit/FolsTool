package top.fols.box.reflect.re;

import top.fols.atri.lang.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static top.fols.box.reflect.re.Re_CodeLoader.CompileTimeCodeSourceReader.getBaseDataToDeclareString;

public class CodeUtils {
    public static String argumentsAt(int index) {
        return call(Re_Keywords.INNER_FUNCTION__GETATTR, Re_Keywords.INNER_VAR__FUN_ARGUMENTS_ARGUMENTS_$, getBaseDataToDeclareString(index));
    }

    public static String call(String name, String... paramNames) {
        return name +
                Re_CodeLoader.CODE_CALL_PARAM_JOIN_SYMBOL +
                Strings.join(paramNames, Re_CodeLoader.CODE_CALL_PARAM_SEPARATOR) +
                Re_CodeLoader.CODE_CALL_PARAM_END_SYMBOL;
    }


    public static Object[] executePointBuildArguments(Object object, String funName, Object... param) {
        List<Object> args = new ArrayList<>();
        args.add(object);
        args.add(funName);
        if (null != param) {
            Collections.addAll(args, param);
        }
        return args.toArray();
    }

    public static String executePoint(String funName, int paramCount) {
        //object, key
        String expression;
        expression = argumentsAt(0);
        expression = expression + Re_CodeLoader.CODE_OBJECT_POINT_SYMBOL;
        expression = expression + funName;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Re_CodeLoader.CODE_CALL_PARAM_JOIN_SYMBOL);
        int offset = 2;
        for (int i = 0; i < paramCount; i++) {
            int index = i + offset;
            if (i < paramCount - 1) {
                stringBuilder.append(argumentsAt(index));
                stringBuilder.append(Re_CodeLoader.CODE_CALL_PARAM_SEPARATOR);
            } else {
                stringBuilder.append(argumentsAt(index));
            }
        }
        stringBuilder.append(Re_CodeLoader.CODE_CALL_PARAM_END_SYMBOL);

        expression = expression + stringBuilder;
        return expression;
    }


    public static Object[] executeCallBuildArguments(Object object, Object... param) {
        List<Object> args = new ArrayList<>();
        args.add(object);
        if (null != param) {
            Collections.addAll(args, param);
        }
        return args.toArray();
    }

    public static String executeCall(int paramCount) {
        //object, key
        String expression;
        expression = call(Re_Keywords.INNER_FUNCTION__GETATTR, argumentsAt(0));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Re_CodeLoader.CODE_CALL_PARAM_JOIN_SYMBOL);
        int offset = 2;
        for (int i = 0; i < paramCount; i++) {
            int index = i + offset;
            if (i < paramCount - 1) {
                stringBuilder.append(argumentsAt(index));
                stringBuilder.append(Re_CodeLoader.CODE_CALL_PARAM_SEPARATOR);
            } else {
                stringBuilder.append(argumentsAt(index));
            }
        }
        stringBuilder.append(Re_CodeLoader.CODE_CALL_PARAM_END_SYMBOL);

        expression = expression + stringBuilder;
        return expression;
    }
}
