package app.utils;

import top.fols.atri.util.Throwables;

public class StringUtils {


    public static String toWindowsFilePathFullAngle(String path) {
        String chars = "\\/:*?\"<>|";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i< path.length(); i++) {
            char ch = path.charAt(i);
            if (chars.indexOf(ch) > -1) {
                stringBuilder.append(toFullAngle(ch));
            } else {
                stringBuilder.append(ch);
            }
        }
        return stringBuilder.toString();
    }







    public static boolean isFullAngle(char charValue) {
        return charValue >= (0x20 + 0xFEE0) && charValue <= (0x7E + 0xFEE0);
    }
    public static char toFullAngle(char charValue) {
        return isHalfAngle(charValue) ? (char) (charValue + 0xFEE0): charValue;
    }

    public static String toFullAngle(String string) {
        char[] chars = new char[string.length()];
        for (int i = 0;i<chars.length;i++){
            chars[i] = toFullAngle(string.charAt(i));
        }
        return new String(chars);
    }



    public static boolean isHalfAngle(char charValue) {
        return charValue >= 0x20 && charValue <= 0x7E;
    }
    public static char toHalfAngle(char charValue) {
        return isFullAngle(charValue) ? (char) (charValue - 0xFEE0) : charValue;
    }

    public static String toHalfAngle(String string) {
        char[] chars = new char[string.length()];
        for (int i = 0; i < chars.length; i++){
            chars[i] = toHalfAngle(string.charAt(i));
        }
        return new String(chars);
    }




    public static boolean isPunctuation(char charValue) {
        char c = toHalfAngle(charValue);
        return !Character.isDigit(c) && !Character.isLetter(c) && !Character.isUpperCase(c); /* Half */
    }


    /**
     * as far as possible
     * @param charSequence
     * @param minlen
     * @return
     *
     *
     *
     *  ("1234567890,./；’、", 11) >> "1234567890,"
     *  ("1234567890,./；’、", 9) >> "1234567890"
     */
    public static CharSequence removePunctuation(CharSequence charSequence, int minlen) {
        if (null == charSequence || charSequence.length() == 0) {
            return charSequence;
        }
        StringBuilder strbuf = charSequence instanceof StringBuilder? (StringBuilder)charSequence: new StringBuilder().append(charSequence);
        int strlen = strbuf.length();
        for (int index = strbuf.length() -1; index >= 0 && strlen > minlen; index--) {
            if (isPunctuation(strbuf.charAt(index))) {
                strbuf.setCharAt(index,'\0');
                strlen--;
            }
        }
        for (int i = 0, strbufi = 0; i < strbuf.length(); i++) {
            if (strbuf.charAt(i) != '\0') {
                strbuf.setCharAt(strbufi++, strbuf.charAt(i));
            }
        }
        strbuf.setLength(strlen);
        return strbuf;
    }



    public static String marge(Object... values) {
        StringBuilder concat = new StringBuilder();
        if (null == values || values.length == 0) {
            concat = new StringBuilder();
        } else {
            for (Object value: values) {
                if (null == value) {
                    concat.append((Object) null);
                } else if (value instanceof Throwable) {
                    concat.append(toString((Throwable) value));
                } else {
                    concat.append(value.toString());
                }
            }
        }
        return concat.toString();
    }

    public static String toString(Throwable throwable) {
        if (null == throwable) {
            return null;
        } else {
            return throwable.getMessage() +
                    ':' + ' ' +
                    Throwables.toStrings(throwable);
        }
    }

}
